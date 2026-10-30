import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { AxiosError } from "axios";
import { isAfter, parseISO, startOfToday } from "date-fns";
import { type ReactNode, useCallback, useEffect, useId, useMemo, useRef, useState, useSyncExternalStore } from "react";
import {
  ACTIONS,
  EVENTS,
  LIFECYCLE,
  ORIGIN,
  type Placement,
  STATUS,
  type Step,
  type StepTarget,
  useJoyride,
} from "react-joyride";

import { acknowledgeAnnouncement, getAcknowledgedAnnouncements } from "~/api/skribenten-api-endpoints";
import { logError } from "~/utils/logger";

import {
  FEATURE_ANNOUNCEMENT_LOCALE,
  FeatureAnnouncementArrow,
  type FeatureAnnouncementStepData,
  FeatureAnnouncementTooltip,
} from "./FeatureAnnouncementTooltip";

export type FeatureAnnouncementStep = {
  /** Element the tooltip points at. */
  target: StepTarget;
  title: string;
  content: ReactNode;
  placement?: Placement;
  /** Area to highlight, e.g. the whole menu around `target`. Defaults to `target`. */
  spotlightTarget?: StepTarget;
  /** Defaults to Aksel space-4. Use 0 for a region that should be highlighted edge to edge. */
  spotlightPadding?: number;
  /** Defaults to Aksel radius-8. Use 0 for a rectangular region. */
  spotlightRadius?: number;
  /** Element to scroll into view instead of the target, e.g. when the target is inside a container that clips overflow. */
  scrollTarget?: StepTarget;
};

type FeatureAnnouncementProps = {
  /** Unique per announcement and stored when the user acknowledges it, e.g. "diff-toggles". At most 100 characters. */
  id: string;
  /** Last day (YYYY-MM-DD) the announcement is shown, typically 30 days after release. */
  showUntil: string;
  /** Pass a memoized array: a new identity restarts the step configuration in Joyride. */
  steps: FeatureAnnouncementStep[];
  /** All contextual conditions (role, route, data and targets available) combined by the caller. */
  when: boolean;
  onDismiss?: () => void;
  /** Dims the page around each step's target. Defaults to true. */
  spotlight?: boolean;
};

/**
 * Modal, contextual announcement of a feature. One step gives a single announcement, several steps give
 * a short walkthrough. Focus moves into the announcement and returns to where the user was when it closes.
 * Closing it (button, Escape or a click on the dimmed page) acknowledges it for the user, so it is not shown again.
 * If several announcements are eligible at the same time, they are shown one after another.
 */
export const FeatureAnnouncement = (props: FeatureAnnouncementProps) => (
  <FeatureAnnouncementInstance key={props.id} {...props} />
);

const FeatureAnnouncementInstance = ({
  id,
  showUntil,
  steps,
  when,
  onDismiss,
  spotlight = true,
}: FeatureAnnouncementProps) => {
  const [resumeIndex, setResumeIndex] = useState(0);
  const [unsuccessfulRun, setUnsuccessfulRun] = useState(false);
  const queryClient = useQueryClient();

  const expired = isAfter(startOfToday(), parseISO(showUntil));
  const acknowledgedQuery = useQuery({ ...getAcknowledgedAnnouncements, enabled: when && !expired });
  const acknowledged = acknowledgedQuery.data?.includes(id) === true;
  const { mutate: saveAcknowledgement } = useMutation({
    mutationFn: () => acknowledgeAnnouncement(id),
    retry: 2,
    onError: (error) =>
      logError(error, error instanceof AxiosError ? error.response?.status : undefined).catch(() =>
        console.error("Unable to log error message"),
      ),
  });

  const onDismissReference = useRef(onDismiss);
  onDismissReference.current = onDismiss;
  const hasDismissed = useRef(false);
  const hasBeenShown = useRef(false);
  const dismiss = useCallback(() => {
    if (hasDismissed.current) return;
    hasDismissed.current = true;
    queryClient.setQueryData(getAcknowledgedAnnouncements.queryKey, (previous: string[] = []) => [...previous, id]);
    saveAcknowledgement();
    onDismissReference.current?.();
  }, [id, queryClient, saveAcknowledgement]);

  const eligible = when && !expired && acknowledgedQuery.isSuccess && !acknowledged && steps.length > 0;
  // A run that ended without showing anything (e.g. the target was never found) is retried the next time the
  // announcement becomes eligible, instead of being treated as dismissed.
  if (unsuccessfulRun && !eligible) setUnsuccessfulRun(false);
  const pending = eligible && !unsuccessfulRun;
  const hasTurn = useAnnouncementTurn(pending);
  const blocked = useIsBlockedByOpenDialog(pending && hasTurn);
  const run = pending && hasTurn && !blocked;

  const joyrideSteps = useMemo<Step[]>(
    () =>
      steps.map((step, index) => ({
        id: `${id}-${index}`,
        target: toLiveTarget(step.target),
        // Setting the spotlight target explicitly keeps its padding out of the tooltip's offset.
        spotlightTarget: toLiveTarget(step.spotlightTarget ?? step.target),
        spotlightPadding: step.spotlightPadding ?? 4,
        spotlightRadius: step.spotlightRadius ?? 8,
        ...(step.scrollTarget && { scrollTarget: toLiveTarget(step.scrollTarget) }),
        title: step.title,
        content: step.content,
        placement: step.placement ?? "bottom",
        data: { announcementId: id } satisfies FeatureAnnouncementStepData,
      })),
    [id, steps],
  );

  const { state, Tour } = useJoyride({
    run,
    steps: joyrideSteps,
    continuous: steps.length > 1,
    // Joyride restarts from initialStepIndex whenever `run` turns true again, e.g. after a blocking dialog closes.
    initialStepIndex: resumeIndex,
    // Joyride scrolls each step's target into view, but skips the first step unless asked to.
    scrollToFirstStep: true,
    tooltipComponent: FeatureAnnouncementTooltip,
    arrowComponent: FeatureAnnouncementArrow,
    loaderComponent: null,
    locale: FEATURE_ANNOUNCEMENT_LOCALE,
    // Position relative to the viewport, so targets inside scrolling panes (like the letter editor) work.
    floatingOptions: { strategy: "fixed" },
    styles: {
      // Aksel's backdrop token, toned down: the announcement should draw attention, not feel like an error.
      overlay: { backgroundColor: "var(--ax-bg-overlay)", opacity: 0.4 },
    },
    options: {
      arrowBase: 20,
      arrowSize: 10,
      // Keeps the arrow clear of the tooltip's rounded corners.
      arrowSpacing: 16,
      // Escape is handled below so it dismisses the whole announcement, not just the current step.
      dismissKeyAction: false,
      hideOverlay: !spotlight,
      // Small gap (Aksel space-8) between the target and the arrow tip.
      offset: 8,
      overlayClickAction: "close",
      skipBeacon: true,
      // Below Aksel popovers (1000) so e.g. the help popover is not covered.
      zIndex: 900,
    },
    onEvent: (data) => {
      if (data.type === EVENTS.TOOLTIP) {
        hasBeenShown.current = true;
        setResumeIndex(data.index);
      }
      if (data.type === EVENTS.TOUR_END && !hasBeenShown.current) {
        setUnsuccessfulRun(true);
        return;
      }
      // A click on the dimmed page closes the whole announcement, also in the middle of a walkthrough.
      const closedByOverlay = data.action === ACTIONS.CLOSE && data.origin === ORIGIN.OVERLAY;
      if (data.type === EVENTS.TOUR_END || closedByOverlay) dismiss();
    },
  });

  const tooltipVisible = run && state.status === STATUS.RUNNING && state.lifecycle === LIFECYCLE.TOOLTIP;

  useEffect(() => {
    if (!tooltipVisible) return;
    const dismissOnEscape = (event: KeyboardEvent) => {
      if (event.key === "Escape") dismiss();
    };
    document.addEventListener("keydown", dismissOnEscape);
    return () => document.removeEventListener("keydown", dismissOnEscape);
  }, [tooltipVisible, dismiss]);

  return run ? Tour : null;
};

// Joyride copies the steps, which freezes a React ref at its value when the steps were created (often null).
// A function is kept by reference, so it always reads the ref's current element.
const toLiveTarget = (target: StepTarget): StepTarget =>
  typeof target === "object" && "current" in target ? () => target.current : target;

const announcementQueue: string[] = [];
const queueListeners = new Set<() => void>();
const notifyQueueListeners = () => {
  for (const listener of queueListeners) listener();
};
const subscribeToQueue = (listener: () => void) => {
  queueListeners.add(listener);
  return () => {
    queueListeners.delete(listener);
  };
};

/**
 * Several announcements can become eligible within the same Skribenten page.
 * Lets announcements take turns: each pending announcement queues up, and only the first in line is shown.
 * The next one gets its turn when the first is dismissed or no longer pending.
 */
function useAnnouncementTurn(pending: boolean) {
  const key = useId();

  useEffect(() => {
    if (!pending) return;
    announcementQueue.push(key);
    notifyQueueListeners();
    return () => {
      const index = announcementQueue.indexOf(key);
      if (index !== -1) announcementQueue.splice(index, 1);
      notifyQueueListeners();
    };
  }, [key, pending]);

  return useSyncExternalStore(subscribeToQueue, () => pending && announcementQueue[0] === key);
}

/** True while a native dialog (e.g. an Aksel Modal) is open. Only observes the DOM while `enabled`. */
function useIsBlockedByOpenDialog(enabled: boolean) {
  const [blocked, setBlocked] = useState(false);

  useEffect(() => {
    if (!enabled) return;
    const update = () => setBlocked(document.querySelector("dialog[open]") !== null);
    update();
    const observer = new MutationObserver(update);
    observer.observe(document.body, { attributes: true, attributeFilter: ["open"], childList: true, subtree: true });
    return () => observer.disconnect();
  }, [enabled]);

  return enabled && blocked;
}
