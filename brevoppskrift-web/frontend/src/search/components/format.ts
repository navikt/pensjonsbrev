const LANGUAGE_LABELS: Record<string, string> = { BOKMAL: "Bokmål", NYNORSK: "Nynorsk", ENGLISH: "Engelsk" };

/** Human-readable label for a brevbaker language code, falling back to the raw code. */
export function languageLabel(language: string): string {
  return LANGUAGE_LABELS[language] ?? language;
}
