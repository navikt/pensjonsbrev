import { type ReactNode, useCallback, useEffect, useMemo, useRef, useState } from "react";
import { ACTIONS, EVENTS, ORIGIN, type Placement, STATUS, type Step, type StepTarget, useJoyride } from "react-joyride";

import {
  FEATURE_ANNOUNCEMENT_ATTRIBUTE,
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
  /**
   * Element to scroll into view instead of the target, e.g. when the target sits inside a container that
   * clips overflow (and so looks scrollable to Joyride) rather than in the list that actually scrolls.
   */
  scrollTarget?: StepTarget;
  /**
   * Prepares the UI before the step is shown (e.g. opens a menu or selects an item). The page stays
   * dimmed while it runs; afterwards the announcement waits for `target` to become visible.
   */
  before?: () => Promise<void> | void;
  /** How long to wait for `target` to become visible, in ms. The step is skipped if it does not. */
  targetWaitTimeout?: number;
};

type FeatureAnnouncementProps = {
  /** Unique per announcement; used to tell this announcement apart from other active ones. */
  id: string;
  /** Pass a memoized array: a new identity restarts the step configuration in Joyride. */
  steps: FeatureAnnouncementStep[];
  /** All contextual conditions (role, route, data and targets available) combined by the caller. */
  when: boolean;
  /**
   * Optional controlled dismissal, for when the announcement can unmount and remount within the same
   * view (e.g. when switching between documents) and must not reopen.
   */
  dismissed?: boolean;
  onDismiss?: () => void;
  /** Dims the page around each step's target. Defaults to true. */
  spotlight?: boolean;
};

/**
 * Modal, contextual announcement of a feature. One step gives a single announcement, several steps give
 * a short walkthrough. Focus moves into the announcement and returns to where the user was when it closes.
 * Dismissal (button, Escape or a click on the dimmed page) lasts for as long as the component stays mounted.
 */
export const FeatureAnnouncement = ({
  id,
  steps,
  when,
  dismissed: dismissedProp,
  onDismiss,
  spotlight = true,
}: FeatureAnnouncementProps) => {
  const [internalDismissed, setInternalDismissed] = useState(false);
  const [resumeIndex, setResumeIndex] = useState(0);

  const dismissed = dismissedProp ?? internalDismissed;
  const onDismissReference = useRef(onDismiss);
  onDismissReference.current = onDismiss;
  // Several Joyride events can report the same close, so make sure the caller is only notified once.
  const dismissedReference = useRef(false);
  const dismiss = useCallback(() => {
    if (dismissedReference.current) return;
    dismissedReference.current = true;
    setInternalDismissed(true);
    onDismissReference.current?.();
  }, []);

  const pending = when && !dismissed && steps.length > 0;
  const blocked = useIsBlockedByOtherOverlay(id, pending);
  const run = pending && !blocked;

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
        // Joyride only waits for a target when a step has no `before` hook, so every step gets one that
        // runs the step's own preparation and then waits for the target itself. Joyride then scrolls to it.
        before: async () => {
          await step.before?.();
          await waitUntilVisible(step.target, step.targetWaitTimeout ?? DEFAULT_TARGET_WAIT_MS);
        },
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
    // The editor pane scrolls independently of the page, so position relative to the viewport.
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
      if (data.type === EVENTS.TOOLTIP) setResumeIndex(data.index);
      // A click on the dimmed page closes the whole announcement, also in the middle of a walkthrough.
      const closedByOverlay = data.action === ACTIONS.CLOSE && data.origin === ORIGIN.OVERLAY;
      if (data.type === EVENTS.TOUR_END || closedByOverlay) dismiss();
    },
  });

  // Also while a step is being prepared (page dimmed, tooltip not shown yet), so Escape always works.
  const isRunning = run && state.status === STATUS.RUNNING;

  useEffect(() => {
    if (!isRunning) return;
    const dismissOnEscape = (event: KeyboardEvent) => {
      if (event.key === "Escape") dismiss();
    };
    document.addEventListener("keydown", dismissOnEscape);
    return () => document.removeEventListener("keydown", dismissOnEscape);
  }, [isRunning, dismiss]);

  return run ? Tour : null;
};

const DEFAULT_TARGET_WAIT_MS = 1000;
const TARGET_POLL_MS = 50;

const resolveTarget = (target: StepTarget): HTMLElement | null => {
  if (typeof target === "string") return document.querySelector<HTMLElement>(target);
  if (typeof target === "function") return target();
  if ("current" in target) return target.current;
  return target;
};

// Joyride deep-copies steps, which freezes a React ref at whatever it pointed to when the steps were
// created (often null). A function is copied by reference, so it always reads the ref's current element.
const toLiveTarget = (target: StepTarget): StepTarget =>
  typeof target === "object" && "current" in target ? () => target.current : target;

// Matches Joyride's own check: rendered and not hidden by `display` or `visibility` (e.g. a closed accordion).
const isVisible = (element: HTMLElement | null): element is HTMLElement =>
  element?.isConnected === true && (element.checkVisibility?.({ visibilityProperty: true }) ?? true);

/** Resolves with the target once it is visible, or with null after the timeout. */
const waitUntilVisible = (target: StepTarget, timeoutMs: number) =>
  new Promise<HTMLElement | null>((resolve) => {
    const startedAt = Date.now();
    const check = () => {
      const element = resolveTarget(target);
      if (isVisible(element)) return resolve(element);
      if (Date.now() - startedAt >= timeoutMs) return resolve(null);
      setTimeout(check, TARGET_POLL_MS);
    };
    check();
  });

const hasOtherOverlay = (id: string) =>
  document.querySelector(
    `dialog[open], [${FEATURE_ANNOUNCEMENT_ATTRIBUTE}]:not([${FEATURE_ANNOUNCEMENT_ATTRIBUTE}=${JSON.stringify(id)}])`,
  ) !== null;

/**
 * True while a native dialog (Aksel Modal) or another feature announcement is open. Only observes the
 * DOM while the announcement is still pending, so there is no cost once it has been dismissed.
 */
function useIsBlockedByOtherOverlay(id: string, enabled: boolean) {
  const [blocked, setBlocked] = useState(false);

  useEffect(() => {
    if (!enabled) return;
    const update = () => setBlocked(hasOtherOverlay(id));
    update();
    const observer = new MutationObserver(update);
    observer.observe(document.body, { attributes: true, attributeFilter: ["open"], childList: true, subtree: true });
    return () => observer.disconnect();
  }, [id, enabled]);

  return enabled && blocked;
}
