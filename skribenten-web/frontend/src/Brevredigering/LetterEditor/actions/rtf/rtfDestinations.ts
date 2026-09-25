// Destinations holding metadata rather than document text. Their content is skipped.
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

// Word and Outlook use Windows-1252 (\ansicpg1252) by default. Other code pages are not supported.
const windows1252Decoder = new TextDecoder("windows-1252");

export function decodeAnsiByte(byte: number): string {
  return windows1252Decoder.decode(Uint8Array.of(byte));
}
