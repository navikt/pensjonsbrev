import { type MalType } from "~/api/brevbaker-api-endpoints";
import { type TemplateText } from "~/search/textSearch";

/** Identifies one template in one language, stable across threads. Search
 *  results cross the worker boundary as keys rather than as whole
 *  `TemplateText` objects, so a query never structured-clones the corpus back
 *  to the main thread. */
export type TemplateKey = string;

export function templateKey(template: { malType: MalType; id: string; language: string }): TemplateKey {
  return `${template.malType}/${template.id}/${template.language}`;
}

/** A content hit, with the template replaced by its key. Mirrors `ContentHit`
 *  field for field otherwise. */
export type ContentHitRef = {
  key: TemplateKey;
  lineIndex: number;
  matchCount: number;
  score: number;
};

/** A brev (title/brevkode) hit, with the template replaced by its key. */
export type BrevHitRef = {
  key: TemplateKey;
};

/** Both hit lists, in the order the worker produced them. Content hits are
 *  already sorted by `toContentHits` and brev hits carry Fuse's ranking order,
 *  so the main thread must preserve this order when rehydrating. */
export type HitRefs = {
  content: ContentHitRef[];
  brev: BrevHitRef[];
};

export const NO_HITS: HitRefs = { content: [], brev: [] };

/** Messages to the worker. `setCorpus` has no reply: the worker handles
 *  messages in order, so a search posted after it always runs against it. */
export type WorkerRequest =
  | { type: "setCorpus"; corpus: TemplateText[] }
  | { type: "search"; requestId: number; query: string; exactOnly: boolean };

/** Replies to `search`. A failure is a reply like any other, so the caller
 *  always hears back and can tell the user, rather than waiting forever or
 *  reading a crash as "no hits". */
export type WorkerResponse =
  | { type: "results"; requestId: number; hits: HitRefs }
  | { type: "error"; requestId: number; message: string };
