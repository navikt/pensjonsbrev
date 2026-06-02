import { type TextIndexEntry } from "~/search/textSearch";

export type IndexableTemplate = {
  id: string;
  malType: "autobrev" | "redigerbar";
  name: string;
  displayTitle: string;
  language: string;
};

type CachedTextIndex = {
  signature: string;
  entries: TextIndexEntry[];
  cachedAt: number;
};

const CACHE_KEY = "brevoppskrift.textindex.v4";
const CACHE_TTL_MS = 5 * 60 * 1000;

/** Signature of the index inputs (templates + languages). A change invalidates a
 * cached index even within the TTL window. */
export function buildIndexSignature(templates: IndexableTemplate[]): string {
  return templates
    .map((t) => `${t.malType}:${t.id}:${t.language}`)
    .sort()
    .join("|");
}

export function loadTextIndexCache(signature: string): TextIndexEntry[] | null {
  try {
    const raw = localStorage.getItem(CACHE_KEY);
    if (!raw) {
      return null;
    }
    const parsed = JSON.parse(raw) as CachedTextIndex;
    if (
      parsed.signature !== signature ||
      typeof parsed.cachedAt !== "number" ||
      Date.now() - parsed.cachedAt > CACHE_TTL_MS ||
      !Array.isArray(parsed.entries)
    ) {
      return null;
    }
    return parsed.entries;
  } catch {
    return null;
  }
}

export function saveTextIndexCache(signature: string, entries: TextIndexEntry[]): void {
  try {
    const value: CachedTextIndex = { signature, entries, cachedAt: Date.now() };
    localStorage.setItem(CACHE_KEY, JSON.stringify(value));
  } catch {
    // Best-effort: a quota error just means we re-index next time. The in-memory
    // index keeps working regardless.
  }
}

export function invalidateTextIndexCache(): void {
  try {
    localStorage.removeItem(CACHE_KEY);
  } catch {
    // Ignore.
  }
}
