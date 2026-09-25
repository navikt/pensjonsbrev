/** RTF destination groups the paste parsers skip or read specially. */

/** Destinations without visible document text for the native interpreter. Unknown `\*` destinations are skipped too. */
export const NATIVE_SKIPPED_DESTINATIONS: ReadonlySet<string> = new Set([
  "annotation",
  "atnauthor",
  "atndate",
  "atnid",
  "bkmkend",
  "bkmkstart",
  "colortbl",
  "colschememapping",
  "datastore",
  "fldinst",
  "fonttbl",
  "footer",
  "footerf",
  "footerl",
  "footerr",
  "footnote",
  "generator",
  "header",
  "headerf",
  "headerl",
  "headerr",
  "info",
  "latentstyles",
  "listoverridetable",
  "listtable",
  "nonesttables",
  "nonshppict",
  "object",
  "objdata",
  "pict",
  "pnseclvl",
  "pntxta",
  "pntxtb",
  "revtbl",
  "rsidtbl",
  "shpinst",
  "shprslt",
  "stylesheet",
  "tc",
  "themedata",
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
