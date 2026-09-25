import { type RtfToken } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";

export type HeadingLevel = 1 | 2 | 3;

const HEADING_STYLE_NAME = /^(?:heading|overskrift)\s*([123])\b/i;

/** Word writes `\outlinelevel0` for "Heading 1", `\outlinelevel1` for "Heading 2", and so on. */
export function headingLevelFromOutlineLevel(outlineLevel: number | undefined): HeadingLevel | undefined {
  return outlineLevel === 0 || outlineLevel === 1 || outlineLevel === 2
    ? ((outlineLevel + 1) as HeadingLevel)
    : undefined;
}

/**
 * Maps paragraph style numbers (`\sN`) to heading levels, so paragraphs that only reference a
 * heading style are still recognised. A style is a heading if it has an outline level, or if its
 * name is "heading N" / "overskrift N".
 */
export function extractStylesheetHeadingLevels(tokens: RtfToken[]): Map<number, HeadingLevel> {
  const levels = new Map<number, HeadingLevel>();
  const start = tokens.findIndex(
    (token, index) => token.type === "groupStart" && isControlWord(tokens[index + 1], "stylesheet"),
  );
  if (start === -1) return levels;

  // Depth 1 is the stylesheet itself, depth 2 is one style definition.
  let depth = 0;
  let styleNumber: number | undefined;
  let outlineLevel: number | undefined;
  let name = "";

  for (const token of tokens.slice(start)) {
    if (token.type === "groupStart") {
      depth++;
      if (depth === 2) {
        styleNumber = undefined;
        outlineLevel = undefined;
        name = "";
      }
    } else if (token.type === "groupEnd") {
      if (depth === 2 && styleNumber !== undefined) {
        const level = headingLevelFromOutlineLevel(outlineLevel) ?? headingLevelFromName(name);
        if (level !== undefined) levels.set(styleNumber, level);
      }
      depth--;
      if (depth === 0) break;
    } else if (depth === 2 && token.type === "controlWord") {
      if (token.name === "s") styleNumber = token.param;
      if (token.name === "outlinelevel") outlineLevel = token.param;
    } else if (depth === 2 && token.type === "text") {
      name += token.text;
    }
  }

  return levels;
}

function headingLevelFromName(name: string): HeadingLevel | undefined {
  const match = HEADING_STYLE_NAME.exec(name.trim());
  return match ? (Number(match[1]) as HeadingLevel) : undefined;
}

function isControlWord(token: RtfToken | undefined, name: string): boolean {
  return token?.type === "controlWord" && token.name === name;
}
