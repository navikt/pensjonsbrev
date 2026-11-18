import { type Inline, type Language } from "./types";

export type AiKind = "nynorsk" | "klarsprak";

export type AiRequest = {
  kind: AiKind;
  /** Språket teksten sendes inn på. */
  sourceLanguage: Language;
  sourceText: string;
  /** Språket forslaget skal skrives til. */
  targetLanguage: Language;
  /** Innholdet i målspråket da forespørselen ble gjort. */
  targetBefore: string;
  /** Språket som var valgt i editoren. */
  activeLanguage: Language;
};

export function buildAiRequest(
  kind: AiKind,
  inline: Extract<Inline, { type: "TEXT" }>,
  activeLanguage: Language,
  languages: readonly Language[],
): AiRequest | { error: string } {
  if (kind === "nynorsk") {
    if (!languages.includes("NN")) {
      return { error: "Malen har ikke nynorsk som språk." };
    }
    const source = inline.text.NB ?? "";
    if (source.trim() === "") {
      return { error: "Skriv bokmålsteksten først." };
    }
    return {
      kind,
      sourceLanguage: "NB",
      sourceText: source,
      targetLanguage: "NN",
      targetBefore: inline.text.NN ?? "",
      activeLanguage,
    };
  }
  const source = inline.text[activeLanguage] ?? "";
  if (source.trim() === "") {
    return { error: "Teksten er tom." };
  }
  return {
    kind,
    sourceLanguage: activeLanguage,
    sourceText: source,
    targetLanguage: activeLanguage,
    targetBefore: source,
    activeLanguage,
  };
}

/** Modellsvar er ubetrodd. Avviser tomt, urimelig langt eller kontrolltegn-fylt svar. */
export function validateSuggestion(request: AiRequest, suggestion: unknown): string | undefined {
  if (typeof suggestion !== "string") {
    return undefined;
  }
  const trimmed = suggestion.trim();
  if (trimmed === "") {
    return undefined;
  }
  // biome-ignore lint/suspicious/noControlCharactersInRegex: bevisst sjekk av kontrolltegn
  if (/[\u0000-\u0008\u000B\u000C\u000E-\u001F]/.test(trimmed)) {
    return undefined;
  }
  if (trimmed.length > Math.max(2000, request.sourceText.length * 4)) {
    return undefined;
  }
  return trimmed;
}

/** Forslaget er utdatert hvis kildeteksten, måltekst eller redigeringsspråket er endret siden forespørselen. */
export function isSuggestionStale(
  inline: Extract<Inline, { type: "TEXT" }>,
  request: AiRequest,
  activeLanguage: Language,
): boolean {
  return (
    activeLanguage !== request.activeLanguage ||
    (inline.text[request.sourceLanguage] ?? "") !== request.sourceText ||
    (inline.text[request.targetLanguage] ?? "") !== request.targetBefore
  );
}
