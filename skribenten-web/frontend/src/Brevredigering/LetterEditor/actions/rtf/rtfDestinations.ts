import { type RtfDestination } from "~/Brevredigering/LetterEditor/actions/rtf/walkRtf";

/** RTF destination groups the paste parsers skip or read specially. */

/** Destinations without visible document text for the native interpreter. Unknown `\*` destinations are skipped too. */
export const NATIVE_SKIPPED_DESTINATIONS: ReadonlySet<string> = new Set([
  "aftncn",
  "aftnsep",
  "aftnsepc",
  "annotation",
  "atnauthor",
  "atndate",
  "atnid",
  "bkmkend",
  "bkmkstart",
  "colortbl",
  "colschememapping",
  "datastore",
  "docvar",
  "filetbl",
  "fldinst",
  "fonttbl",
  "footer",
  "footerf",
  "footerl",
  "footerr",
  "footnote",
  "ftncn",
  "ftnsep",
  "ftnsepc",
  "generator",
  "header",
  "headerf",
  "headerl",
  "headerr",
  "info",
  "latentstyles",
  "listoverridetable",
  "listpicture",
  "listtable",
  "nonesttables",
  "nonshppict",
  "object",
  "objdata",
  "pgdsctbl",
  "pict",
  "pnseclvl",
  "pntxta",
  "pntxtb",
  "private",
  "revtbl",
  "rsidtbl",
  "shpinst",
  "shprslt",
  "stylesheet",
  "tc",
  "template",
  "themedata",
  "userprops",
  "wgrffmtfilter",
  "xe",
]);

/** The rendered bullet or number of a list item. */
export const LIST_MARKER_DESTINATIONS: ReadonlySet<string> = new Set(["listtext", "pntext"]);

/** Destinations without original HTML or text when de-encapsulating Outlook RTF. */
export const ENCAPSULATION_SKIPPED_DESTINATIONS: ReadonlySet<string> = new Set([
  "colortbl",
  "fonttbl",
  "info",
  "pict",
  "pntext",
  "stylesheet",
]);

/** What each destination word opens for the native interpreter. */
export const NATIVE_DESTINATIONS: ReadonlyMap<string, RtfDestination> = new Map([
  ...[...NATIVE_SKIPPED_DESTINATIONS].map((word) => [word, "skip"] as const),
  ...[...LIST_MARKER_DESTINATIONS].map((word) => [word, "listMarker"] as const),
  ["pn", "pn"],
]);

/** What each destination word opens when de-encapsulating Outlook RTF. */
export const ENCAPSULATION_DESTINATIONS: ReadonlyMap<string, RtfDestination> = new Map([
  ...[...ENCAPSULATION_SKIPPED_DESTINATIONS].map((word) => [word, "skip"] as const),
  ["htmltag", "htmltag"],
]);
