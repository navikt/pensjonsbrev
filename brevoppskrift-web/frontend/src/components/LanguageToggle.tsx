import { ToggleGroup, Tooltip } from "@navikt/ds-react";

import { LANGUAGES, languageLabel, languageShortLabel } from "~/search/components/format";

/**
 * Switches between a template's language versions. Only the languages the
 * template is written in are shown, always in the same order (NB, NN, EN).
 *
 * Selection follows focus, as in a radio group: the arrow keys switch
 * language right away, without Enter or Space.
 */
export function LanguageToggle({
  available,
  value,
  onChange,
}: {
  /** The languages the template is written in. */
  available: readonly string[];
  value: string;
  onChange: (language: string) => void;
}) {
  const languages = [
    ...LANGUAGES.filter((language) => available.includes(language)),
    ...available.filter((language) => !LANGUAGES.includes(language)),
  ];
  return (
    <ToggleGroup aria-label="Språk" onChange={onChange} size="small" value={value}>
      {languages.map((language) => (
        <Tooltip content={languageLabel(language)} key={language}>
          <ToggleGroup.Item
            label={languageShortLabel(language)}
            onFocus={() => language !== value && onChange(language)}
            value={language}
          />
        </Tooltip>
      ))}
    </ToggleGroup>
  );
}
