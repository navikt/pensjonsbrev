const LANGUAGE_LABELS: Record<string, string> = { BOKMAL: "Bokmål", NYNORSK: "Nynorsk", ENGLISH: "Engelsk" };

export function languageLabel(language: string): string {
  return LANGUAGE_LABELS[language] ?? language;
}
