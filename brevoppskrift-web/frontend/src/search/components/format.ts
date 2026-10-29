const LANGUAGE_LABELS: Record<string, string> = { BOKMAL: "Bokmål", NYNORSK: "Nynorsk", ENGLISH: "Engelsk" };
const LANGUAGE_SHORT_LABELS: Record<string, string> = { BOKMAL: "NB", NYNORSK: "NN", ENGLISH: "EN" };

/** The languages templates are written in, in the order they are offered. */
export const LANGUAGES: readonly string[] = ["BOKMAL", "NYNORSK", "ENGLISH"];

export function languageLabel(language: string): string {
  return LANGUAGE_LABELS[language] ?? language;
}

export function languageShortLabel(language: string): string {
  return LANGUAGE_SHORT_LABELS[language] ?? language;
}
