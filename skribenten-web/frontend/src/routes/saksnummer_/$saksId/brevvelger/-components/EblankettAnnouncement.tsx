import { useMatch } from "@tanstack/react-router";
import { type RefObject, useMemo, useRef } from "react";

import {
  FeatureAnnouncement,
  type FeatureAnnouncementStep,
} from "~/components/featureAnnouncement/FeatureAnnouncement";

export const EBLANKETT_ANNOUNCEMENT_ID = "brevvelger-eblanketter";

// The e-blankett and its panel render after a navigation and an accordion animation, so allow extra time.
const TARGET_WAIT_MS = 3000;
// Aksel's accordion opens with a 250 ms transition. Joyride scrolls to the target once `before` resolves,
// so wait for the category to be fully open; otherwise the list is still too short to scroll far enough.
const ACCORDION_TRANSITION_MS = 300;

/**
 * Walks through the new E-blanketter category: the category, an e-blankett selected on the user's behalf,
 * and the panel where it is filled in. `onShowEblankett` opens the category and selects the e-blankett;
 * `onEnd` puts brevvelger back the way it was before the walkthrough.
 */
export const EblankettAnnouncement = ({
  eblankettId,
  ready,
  categoryRef,
  eblankettRef,
  panelRef,
  onShowEblankett,
  onEnd,
}: {
  /** The e-blankett the walkthrough selects, or undefined while there is none to show. */
  eblankettId: string | undefined;
  /** True once everything that affects the layout of brevvelger has loaded. */
  ready: boolean;
  categoryRef: RefObject<HTMLElement | null>;
  eblankettRef: RefObject<HTMLElement | null>;
  panelRef: RefObject<HTMLElement | null>;
  onShowEblankett: () => void;
  onEnd: () => void;
}) => {
  const erIBrevvelger = useMatch({ from: "/saksnummer_/$saksId/brevvelger", shouldThrow: false }) !== undefined;
  const onShowEblankettReference = useRef(onShowEblankett);
  onShowEblankettReference.current = onShowEblankett;

  const steps = useMemo<FeatureAnnouncementStep[]>(
    () => [
      {
        target: categoryRef,
        title: "E-blanketter har fått egen kategori",
        content: "Alle e-blanketter er nå samlet i én kategori nederst i listen over brevmaler.",
        placement: "right",
        targetWaitTimeout: TARGET_WAIT_MS,
      },
      {
        target: eblankettRef,
        // The e-blankett sits in the accordion's content, which clips overflow; scroll the list itself by
        // bringing the category to the top, with the selected e-blankett right below it.
        scrollTarget: categoryRef,
        title: "Velg en e-blankett",
        content: "Vi har åpnet kategorien og valgt en e-blankett for deg, slik at du ser hvordan det fungerer.",
        placement: "right",
        targetWaitTimeout: TARGET_WAIT_MS,
        before: () => {
          onShowEblankettReference.current();
          return new Promise((resolve) => setTimeout(resolve, ACCORDION_TRANSITION_MS));
        },
      },
      {
        target: panelRef,
        spotlightPadding: 0,
        spotlightRadius: 0,
        title: "Fyll ut e-blanketten",
        content: "Her fyller du ut og bestiller e-blanketten. Implementasjonen her kommer snart.",
        placement: "left",
        targetWaitTimeout: TARGET_WAIT_MS,
      },
    ],
    [categoryRef, eblankettRef, panelRef],
  );

  return (
    <FeatureAnnouncement
      id={EBLANKETT_ANNOUNCEMENT_ID}
      onDismiss={onEnd}
      steps={steps}
      when={erIBrevvelger && ready && eblankettId !== undefined}
    />
  );
};
