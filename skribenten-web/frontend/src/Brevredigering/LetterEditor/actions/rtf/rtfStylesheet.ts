import { type RtfToken } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";

/** Heading levels from the RTF stylesheet (`{\stylesheet …}`). */

export type HeadingType = "H1" | "H2" | "H3";

export const HEADING_BY_OUTLINE_LEVEL: readonly HeadingType[] = ["H1", "H2", "H3"];

/** Fallback for writers that set no `\outlinelevel` on heading styles. */
const HEADING_STYLE_NAMES: ReadonlyMap<string, HeadingType> = new Map([
  ["heading 1", "H1"],
  ["heading 2", "H2"],
  ["heading 3", "H3"],
  ["overskrift 1", "H1"],
  ["overskrift 2", "H2"],
  ["overskrift 3", "H3"],
]);

function headingFromStyleName(name: string): HeadingType | undefined {
  // Style names may carry aliases, e.g. "heading 1,Overskrift 1".
  for (const alias of name.split(",")) {
    const heading = HEADING_STYLE_NAMES.get(alias.trim().toLowerCase());
    if (heading) return heading;
  }
  return undefined;
}

/** Maps paragraph style index (`\sN`) to heading level, from `\outlinelevel` or the style name. */
export function parseHeadingStyles(tokens: readonly RtfToken[]): Map<number, HeadingType> {
  const headingStyles = new Map<number, HeadingType>();
  const start = tokens.findIndex((token) => token.type === "control" && token.word === "stylesheet");
  if (start === -1) return headingStyles;

  let depth = 1;
  let entry: { styleIndex?: number; outlineLevel?: number; name: string } | undefined;

  for (let index = start + 1; index < tokens.length && depth > 0; index++) {
    const token = tokens[index];
    if (token.type === "groupStart") {
      depth++;
      if (depth === 2) entry = { name: "" };
    } else if (token.type === "groupEnd") {
      if (depth === 2 && entry?.styleIndex !== undefined) {
        const heading =
          entry.outlineLevel === undefined
            ? headingFromStyleName(entry.name.split(";")[0])
            : HEADING_BY_OUTLINE_LEVEL[entry.outlineLevel];
        if (heading) headingStyles.set(entry.styleIndex, heading);
      }
      depth--;
    } else if (depth === 2 && entry) {
      if (token.type === "text") {
        entry.name += token.value;
      } else if (token.type === "control") {
        // Character (`\*\csN`) and table (`\*\tsN`) styles are not paragraph styles.
        if (token.word === "*") entry = undefined;
        else if (token.word === "s" && token.hasParam) entry.styleIndex = token.param;
        else if (token.word === "outlinelevel" && token.hasParam) entry.outlineLevel = token.param;
      }
    }
  }

  return headingStyles;
}
