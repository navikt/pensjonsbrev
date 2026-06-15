import { type MalType } from "~/api/brevbaker-api-endpoints";
import { type Line, type LineSegment } from "~/api/brevbakerTypes";

export type { Line, LineSegment };
/** One template rendered in a single language, flattened to searchable lines. */
export type TemplateText = {
  id: string; // brevkode
  malType: MalType;
  title: string;
  language: string;
  lines: Line[];
  blockIds: string[];
};
/**
 * A content hit: the best-matching line in a template (its neighbours are
 * reachable via `template.lines`) plus the total number of matching lines.
 */
export type ContentHit = {
  template: TemplateText;
  lineIndex: number;
  matchCount: number;
  score: number;
};
/** A metadata hit: the query matched a template's title or brevkode. */
export type BrevHit = {
  template: TemplateText;
};
export type SearchResults = {
  content: ContentHit[];
  brev: BrevHit[];
};
/** Lowercased, whitespace-collapsed text of a line. Variable segments are not
 *  searchable and are skipped. */
export function lineText(line: Line): string {
  return line
    .filter((segment) => segment.type === "text")
    .map((segment) => segment.value)
    .join(" ")
    .toLowerCase()
    .replace(/\s+/g, " ")
    .trim();
}
function normalize(value: string): string {
  return value.toLowerCase().replace(/\s+/g, " ").trim();
}
/** A line scores higher when it contains the whole phrase; otherwise every term
 *  must be present (AND). Returns 0 when the line does not match. */
function lineScore(text: string, phrase: string, terms: string[]): number {
  if (!text) {
    return 0;
  }
  if (text.includes(phrase)) {
    return terms.length + 1;
  }
  return terms.every((term) => text.includes(term)) ? terms.length : 0;
}
/** Pre-computes line text so each search is a cheap substring scan. */
export type SearchIndex = {
  templates: { template: TemplateText; lineTexts: string[] }[];
};
export function buildIndex(templates: TemplateText[]): SearchIndex {
  return {
    templates: templates.map((template) => ({
      template,
      lineTexts: template.lines.map(lineText),
    })),
  };
}
export function search(index: SearchIndex, rawQuery: string): SearchResults {
  const phrase = normalize(rawQuery);
  const content: ContentHit[] = [];
  const brev: BrevHit[] = [];
  if (!phrase) {
    return { content, brev };
  }
  const terms = phrase.split(" ");
  for (const { template, lineTexts } of index.templates) {
    let lineIndex = -1;
    let bestScore = 0;
    let matchCount = 0;
    for (let i = 0; i < lineTexts.length; i++) {
      const score = lineScore(lineTexts[i], phrase, terms);
      if (score > 0) {
        matchCount++;
        if (score > bestScore) {
          bestScore = score;
          lineIndex = i;
        }
      }
    }
    if (lineIndex >= 0) {
      content.push({ template, lineIndex, matchCount, score: bestScore });
    }
    if (template.title.toLowerCase().includes(phrase) || template.id.toLowerCase().includes(phrase)) {
      brev.push({ template });
    }
  }
  content.sort(
    (a, b) =>
      b.score - a.score || b.matchCount - a.matchCount || a.template.title.localeCompare(b.template.title, "no"),
  );
  brev.sort((a, b) => a.template.title.localeCompare(b.template.title, "no"));
  return { content, brev };
}
