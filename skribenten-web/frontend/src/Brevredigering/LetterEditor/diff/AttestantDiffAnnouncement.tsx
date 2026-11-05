import { useMatch } from "@tanstack/react-router";
import { useMemo } from "react";

import {
  FeatureAnnouncement,
  type FeatureAnnouncementStep,
} from "~/components/featureAnnouncement/FeatureAnnouncement";
import { useUserInfo } from "~/hooks/useUserInfo";

export const ATTESTANT_DIFF_ANNOUNCEMENT_ID = "diff-toggles";
export const ATTESTANT_DIFF_ANNOUNCEMENT_SHOW_UNTIL = "2026-11-30";

/**
 * Introduces/announces the diff toggles to attestants. Render it right after `AttestantDiffMenu`, once the letter
 * is loaded, and pass the rendered menu and its toggle group (or null while they are not rendered).
 * The tooltip points at the toggles, while the whole menu is highlighted.
 */
export const AttestantDiffAnnouncement = ({
  menu,
  toggles,
}: {
  menu: HTMLElement | null;
  toggles: HTMLElement | null;
}) => {
  const isAttestant = useUserInfo()?.erAttestant === true;
  const isOnAttestantEditingRoute =
    useMatch({ from: "/saksnummer_/$saksId/attester/$brevId/redigering", shouldThrow: false }) !== undefined;

  const steps = useMemo<FeatureAnnouncementStep[]>(
    () =>
      menu && toggles
        ? [
            {
              target: toggles,
              spotlightTarget: menu,
              spotlightPadding: 0,
              spotlightRadius: 0,
              title: "Se manuelle endringer i brevet",
              content:
                "Slå på bryterne for å se hva som er lagt til eller slettet sammenlignet med den opprinnelige brevmalen.",
              placement: "bottom-start",
            },
          ]
        : [],
    [menu, toggles],
  );

  return (
    <FeatureAnnouncement
      id={ATTESTANT_DIFF_ANNOUNCEMENT_ID}
      showUntil={ATTESTANT_DIFF_ANNOUNCEMENT_SHOW_UNTIL}
      steps={steps}
      when={isAttestant && isOnAttestantEditingRoute && menu !== null && toggles !== null}
    />
  );
};
