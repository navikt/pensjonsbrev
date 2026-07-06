import Fuse from "fuse.js";

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
  indexes: number[];
};

/**
 * A content hit: the best-matching line in a template (its neighbours are
 * reachable via `template.lines`) plus the total number of matching lines.
 * `score` follows Fuse.js convention: 0 is a perfect match, 1 is the worst.
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

type ContentRecord = {
  template: TemplateText;
  lineIndex: number;
  text: string;
};
type BrevRecord = {
  template: TemplateText;
  title: string;
  id: string;
};

/** Multi-word queries are typo-tolerant per term (token search) and must match
 *  every term (`tokenMatch: "all"`), mirroring the previous AND-of-terms
 *  behaviour while adding fuzziness. Tuned tighter than Fuse's default
 *  (0.6) so short brevkoder/terms don't match unrelated text. */
const TOKEN_SEARCH_OPTIONS = {
  useTokenSearch: true,
  tokenMatch: "all",
  includeScore: true,
  threshold: 0.3,
  minMatchCharLength: 2,
  // Lines/titles vary a lot in length; without this, matches in longer text
  // are penalized purely for being longer, which isn't a signal we want here.
  ignoreFieldNorm: true,
} as const;

/** Pre-built Fuse indexes: one over every searchable line (content search),
 *  one over template title/brevkode (metadata search). */
export type SearchIndex = {
  contentFuse: Fuse<ContentRecord>;
  brevFuse: Fuse<BrevRecord>;
};

export function buildIndex(templates: TemplateText[]): SearchIndex {
  const contentRecords: ContentRecord[] = [];
  const brevRecords: BrevRecord[] = [];
  for (const template of templates) {
    template.lines.forEach((line, lineIndex) => {
      const text = lineText(line);
      if (text) {
        contentRecords.push({ template, lineIndex, text });
      }
    });
    brevRecords.push({ template, title: template.title, id: template.id });
  }
  const contentFuse = new Fuse(contentRecords, {
    ...TOKEN_SEARCH_OPTIONS,
    keys: ["text"],
  });
  const brevFuse = new Fuse(brevRecords, {
    ...TOKEN_SEARCH_OPTIONS,
    keys: [
      { name: "title", weight: 2 },
      { name: "id", weight: 1 },
    ],
  });
  return { contentFuse, brevFuse };
}

export function search(index: SearchIndex, rawQuery: string): SearchResults {
  const query = rawQuery.trim();
  if (!query) {
    return { content: [], brev: [] };
  }

  const contentByTemplate = new Map<string, ContentHit>();
  for (const { item, score } of index.contentFuse.search(query)) {
    const { template, lineIndex } = item;
    const key = `${template.malType}/${template.id}/${template.language}`;
    const resolvedScore = score ?? 1;
    const existing = contentByTemplate.get(key);
    if (existing) {
      existing.matchCount++;
      if (resolvedScore < existing.score) {
        existing.score = resolvedScore;
        existing.lineIndex = lineIndex;
      }
    } else {
      contentByTemplate.set(key, { template, lineIndex, matchCount: 1, score: resolvedScore });
    }
  }
  const content = [...contentByTemplate.values()].sort(
    (a, b) =>
      a.score - b.score || b.matchCount - a.matchCount || a.template.title.localeCompare(b.template.title, "no"),
  );

  const brev = index.brevFuse.search(query).map(({ item }) => ({ template: item.template }));

  return { content, brev };
}
