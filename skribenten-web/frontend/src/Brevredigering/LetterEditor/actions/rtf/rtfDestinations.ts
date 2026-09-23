/**
 * RTF destination names whose content is metadata (font/color/style tables,
 * document info, embedded objects, headers/footers, list-numbering fallback
 * text, etc.) rather than visible body text, and should be skipped entirely
 * when interpreting RTF content - both when extracting encapsulated
 * HTML/text (see `extractEncapsulation.ts`) and when interpreting genuine
 * native RTF (see `interpretNativeRtf.ts`).
 *
 * `listtext` is intentionally excluded from this set: its content (the
 * rendered bullet/number fallback text for a list item) is peeked at - but
 * not emitted - by the native RTF interpreter to classify bullet vs.
 * numbered lists. See `classifyListMarkerText` in `interpretNativeRtf.ts`.
 */
export const SKIPPED_RTF_DESTINATIONS: ReadonlySet<string> = new Set([
  "fonttbl",
  "colortbl",
  "stylesheet",
  "info",
  "generator",
  "pict",
  "object",
  "objdata",
  "header",
  "headerf",
  "headerl",
  "headerr",
  "footer",
  "footerf",
  "footerl",
  "footerr",
  "footnote",
  "xe",
  "tc",
  "fldinst",
  "listtable",
  "listoverridetable",
  "revtbl",
  "rsidtbl",
  "wgrffmtfilter",
  "themedata",
  "colschememapping",
  "latentstyles",
  "datastore",
  "nonshppict",
  "shprslt",
  "bkmkstart",
  "bkmkend",
  "pntext",
  "pntxta",
  "pntxtb",
  "atnid",
  "atnauthor",
  "atndate",
]);

/**
 * Decodes a single RTF `\'hh` hex-escaped byte using the Windows-1252
 * codepage, which is what Word/Outlook emit by default (`\ansicpg1252`) and
 * covers the Norwegian letters æ/ø/å used throughout Nav letters. Other
 * codepages are not supported in this first iteration (see plan's open
 * risks) - such bytes decode via the same table, which is a reasonable
 * approximation for the ANSI/Latin-1 range but may be wrong for genuinely
 * different codepages.
 */
const windows1252Decoder = new TextDecoder("windows-1252");
export function decodeAnsiByte(byte: number): string {
  return windows1252Decoder.decode(Uint8Array.of(byte));
}
