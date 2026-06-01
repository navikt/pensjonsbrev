import { type TemplateDescription } from "~/api/brevbakerTypes";

export type TemplateWithDescription = {
  id: string;
  description: TemplateDescription;
};

export type CachedTemplates = {
  autobrev: TemplateWithDescription[];
  redigerbar: TemplateWithDescription[];
  cachedAt: number;
};

const CACHE_KEY = "brevoppskrift.templates";
const CACHE_TTL_MS = 5 * 60 * 1000;

export function loadTemplateCache(): CachedTemplates | null {
  try {
    const raw = localStorage.getItem(CACHE_KEY);
    if (!raw) {
      return null;
    }
    const parsed = JSON.parse(raw) as CachedTemplates;
    if (typeof parsed.cachedAt !== "number" || Date.now() - parsed.cachedAt > CACHE_TTL_MS) {
      return null;
    }
    if (!Array.isArray(parsed.autobrev) || !Array.isArray(parsed.redigerbar)) {
      return null;
    }
    return parsed;
  } catch {
    return null;
  }
}

export function saveTemplateCache(autobrev: TemplateWithDescription[], redigerbar: TemplateWithDescription[]): void {
  try {
    const value: CachedTemplates = { autobrev, redigerbar, cachedAt: Date.now() };
    localStorage.setItem(CACHE_KEY, JSON.stringify(value));
  } catch {
    // Ignore quota or serialization errors; cache is a best-effort optimization.
  }
}

export function invalidateTemplateCache(): void {
  try {
    localStorage.removeItem(CACHE_KEY);
  } catch {
    // Ignore.
  }
}
