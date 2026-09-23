import { type RtfToken } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";
import { ListType } from "~/types/brevbakerTypes";

const HEADING_NAME_PATTERN = /(?:heading|overskrift|tittel)\s*([123])/i;

/**
 * Parses the `{\stylesheet ...}` destination (if present) into a map from
 * paragraph style index (`\sN`) to a heading level (0/1/2, corresponding to
 * H1/H2/H3), so that paragraphs referencing a heading style via `\sN` (with
 * no direct `\outlinelevelN` of their own) can still be recognised as
 * headings.
 *
 * Level is resolved, in order of preference, from:
 * 1. A direct `\outlinelevelN` (0/1/2) on the style definition.
 * 2. The style name matching a known heading keyword ("heading 1",
 *    "overskrift 1", "tittel 1", ...), which is how Word typically names
 *    its built-in heading styles regardless of document language.
 *
 * Operates directly on the raw token stream (not through `walkRtfContent`),
 * since the stylesheet destination is otherwise fully swallowed as
 * metadata - its content must never leak into the document body.
 */
export function extractStylesheetHeadingLevels(tokens: RtfToken[]): Map<number, 0 | 1 | 2> {
  const levels = new Map<number, 0 | 1 | 2>();

  for (let index = 0; index < tokens.length; index++) {
    const token = tokens[index];
    const next = tokens[index + 1];
    if (token.type === "groupStart" && next?.type === "controlWord" && next.name === "stylesheet") {
      parseStylesheetBody(tokens, index + 2, levels);
      break;
    }
  }

  return levels;
}

// Parses the body of the stylesheet destination (starting right after its
// opening `{\stylesheet`), consuming each direct child group as one style
// definition, until the stylesheet's own closing `}`.
function parseStylesheetBody(tokens: RtfToken[], start: number, levels: Map<number, 0 | 1 | 2>): number {
  let depth = 1; // already inside the stylesheet group
  let index = start;

  while (index < tokens.length && depth > 0) {
    const token = tokens[index];
    if (token.type === "groupStart") {
      if (depth === 1) {
        index = parseStyleDefinition(tokens, index + 1, levels);
        continue;
      }
      depth++;
      index++;
    } else if (token.type === "groupEnd") {
      depth--;
      index++;
    } else {
      index++;
    }
  }

  return index;
}

// Parses one style definition group's content (starting just past its
// opening `{`), extracting its style index (`\sN`), any direct
// `\outlinelevelN`, and its style name text (the fallback heuristic).
// Returns the index just past this definition's closing `}`.
function parseStyleDefinition(tokens: RtfToken[], start: number, levels: Map<number, 0 | 1 | 2>): number {
  let depth = 1;
  let index = start;
  let styleIndex: number | undefined;
  let outlineLevel: number | undefined;
  let nameText = "";

  while (index < tokens.length && depth > 0) {
    const token = tokens[index];
    if (token.type === "groupStart") {
      depth++;
    } else if (token.type === "groupEnd") {
      depth--;
    } else if (depth === 1 && token.type === "controlWord") {
      if (token.name === "s" && token.param !== undefined) styleIndex = token.param;
      if (token.name === "outlinelevel" && token.param !== undefined) outlineLevel = token.param;
    } else if (depth === 1 && token.type === "text") {
      nameText += token.text;
    }
    index++;
  }

  if (styleIndex !== undefined) {
    const level = resolveHeadingLevel(outlineLevel, nameText);
    if (level !== undefined) levels.set(styleIndex, level);
  }

  return index;
}

function resolveHeadingLevel(outlineLevel: number | undefined, nameText: string): 0 | 1 | 2 | undefined {
  if (outlineLevel === 0 || outlineLevel === 1 || outlineLevel === 2) {
    return outlineLevel;
  }
  const match = HEADING_NAME_PATTERN.exec(nameText);
  if (match) {
    const headingNumber = Number.parseInt(match[1], 10);
    if (headingNumber >= 1 && headingNumber <= 3) return (headingNumber - 1) as 0 | 1 | 2;
  }
  return undefined;
}

/**
 * Classifies a list item's marker text - the literal bullet/number fallback
 * text captured from RTF's `{\listtext ...}` destination (see
 * `rtfContentWalker.ts`'s `listMarkerText` event) - as a bulleted or
 * numbered list.
 *
 * This is a pragmatic heuristic rather than a full resolution of the
 * `\listtable`/`\listoverridetable` chain (which would require correlating
 * `\lsN` on the paragraph to a `\listoverride` entry, in turn to a
 * `\list`/`\listlevel`'s `\levelnfc` numbering-format code) - see the plan's
 * documented limitations. Falls back to bulleted if no marker text was
 * captured or it couldn't be classified as numbered, since that's the more
 * common list type and the safer default when in doubt.
 */
export function classifyListMarkerText(markerText: string | undefined): ListType {
  const trimmed = markerText?.trim() ?? "";
  const isNumbered =
    /^\d+[.)]?$/.test(trimmed) || /^[a-zA-Z][.)]$/.test(trimmed) || /^[ivxlcdmIVXLCDM]+[.)]$/.test(trimmed);
  return isNumbered ? ListType.NUMMERERT_LISTE : ListType.PUNKTLISTE;
}
