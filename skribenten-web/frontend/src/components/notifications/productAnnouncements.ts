import { type UserInfo } from "~/api/bff-endpoints";
import {
  ATTESTANT_DIFF_ANNOUNCEMENT_ID,
  ATTESTANT_DIFF_ANNOUNCEMENT_REPLAYABLE_UNTIL,
} from "~/Brevredigering/LetterEditor/diff/AttestantDiffAnnouncement";

export type ProductAnnouncement = {
  /** Same id as the `FeatureAnnouncement` that shows the tour. */
  id: string;
  /** Last day listed in the bell. Same as the tour's `replayableUntil`, so «Se hvordan» works as long as it is listed. */
  replayableUntil: string;
  /** Day (YYYY-MM-DD) the feature was released, shown above the title. */
  publishedAt: string;
  title: string;
  description: string;
  /** Section in the documentation about the feature. */
  documentationUrl: string;
  /** Limits the announcement to some users. Shown to everyone when omitted. */
  isRelevantFor?: (user: UserInfo) => boolean;
};

export const PRODUCT_ANNOUNCEMENTS: ProductAnnouncement[] = [
  {
    id: ATTESTANT_DIFF_ANNOUNCEMENT_ID,
    replayableUntil: ATTESTANT_DIFF_ANNOUNCEMENT_REPLAYABLE_UNTIL,
    publishedAt: "2026-11-10",
    title: "Se endringer gjort av saksbehandler",
    description:
      "Nå kan attestant se endringer som saksbehandler har gjort i malen. Man kan se hva som er lagt til, og hva som er slettet.",
    documentationUrl: "https://navikt.github.io/pensjonsbrev/docs/main/skribenten-101.html#_attestering",
    isRelevantFor: (user) => user.erAttestant,
  },
];
