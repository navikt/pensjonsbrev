import { type UserInfo } from "~/api/bff-endpoints";
import {
  ATTESTANT_DIFF_ANNOUNCEMENT_ID,
  ATTESTANT_DIFF_ANNOUNCEMENT_SHOW_UNTIL,
} from "~/Brevredigering/LetterEditor/diff/AttestantDiffAnnouncement";

export type ProductAnnouncement = {
  /** Same id as the `FeatureAnnouncement` that shows the tour. */
  id: string;
  /** Same last display day as the `FeatureAnnouncement`. */
  showUntil: string;
  title: string;
  description: string;
  /** Shown when the user chooses «Vis meg» where the tour cannot be shown. Phrased like the app's other warnings. */
  unavailable: { heading: string; body: string };
  /** Limits the announcement to some users. Shown to everyone when omitted. */
  isRelevantFor?: (user: UserInfo) => boolean;
};

export const PRODUCT_ANNOUNCEMENTS: ProductAnnouncement[] = [
  {
    id: ATTESTANT_DIFF_ANNOUNCEMENT_ID,
    showUntil: ATTESTANT_DIFF_ANNOUNCEMENT_SHOW_UNTIL,
    title: "Se manuelle endringer i brevet",
    description: "Du kan nå markere tekst som er lagt til, eller vise tekst som er slettet manuelt.",
    unavailable: {
      heading: "Du må åpne et brev til attestering",
      body: "Manuelle endringer i brevet kan bare vises når du attesterer et brev.",
    },
    isRelevantFor: (user) => user.erAttestant,
  },
];
