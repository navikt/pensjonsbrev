import { addDays, addMonths, format, parseISO } from "date-fns";

export type ProductAnnouncement = {
  /** Unique id, also used by the `FeatureAnnouncement` that shows the tour, e.g. "diff-toggles". */
  id: string;
  /** Day (YYYY-MM-DD) the feature was released. Shown above the title, and the other dates are counted from it. */
  publishedAt: string;
  title: string;
  description: string;
  /** Section in the documentation about the feature. */
  documentationUrl: string;
};

const AUTOMATIC_TOUR_DAYS = 30;
const LISTED_IN_BELL_MONTHS = 3;

const toIsoDate = (date: Date) => format(date, "yyyy-MM-dd");

/** Last day the tour starts automatically, for users who have not acknowledged it. */
export const automaticTourUntil = (announcement: ProductAnnouncement) =>
  toIsoDate(addDays(parseISO(announcement.publishedAt), AUTOMATIC_TOUR_DAYS));

/** Last day the announcement is listed in the bell, where «Se hvordan» can start its tour. */
export const listedUntil = (announcement: ProductAnnouncement) =>
  toIsoDate(addMonths(parseISO(announcement.publishedAt), LISTED_IN_BELL_MONTHS));

export const DIFF_TOGGLES_ANNOUNCEMENT: ProductAnnouncement = {
  id: "diff-toggles",
  publishedAt: "2026-11-09",
  title: "Se endringer gjort av saksbehandler",
  description:
    "Nå kan attestant se endringer som saksbehandler har gjort i malen. Man kan se hva som er lagt til, og hva som er slettet.",
  documentationUrl: "https://navikt.github.io/pensjonsbrev/docs/main/skribenten-101.html#_attestering",
};

export const PRODUCT_ANNOUNCEMENTS: ProductAnnouncement[] = [DIFF_TOGGLES_ANNOUNCEMENT];
