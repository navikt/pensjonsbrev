import { ToggleGroup, Tooltip } from "@navikt/ds-react";
import { type KeyboardEvent, useRef } from "react";

import { LANGUAGES, languageLabel, languageShortLabel } from "~/search/components/format";

/**
 * Switches between a template's language versions. Only the languages the
 * template is written in are shown, always in the same order (NB, NN, EN).
 *
 * Selection follows focus, as in a radio group: the arrow keys switch
 * language right away, without Enter or Space. The arrow keys wrap around, so
 * the other languages are always one key press away.
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
  const items = useRef(new Map<string, HTMLButtonElement>());

  // ToggleGroup stops at the ends. Wrapping is handled here, before it, and
  // preventDefault keeps ToggleGroup from handling the same key press.
  const wrapAround = (event: KeyboardEvent, language: string) => {
    const hasModifiers = event.shiftKey || event.ctrlKey || event.altKey || event.metaKey;
    const first = languages[0];
    const last = languages[languages.length - 1];
    const target =
      event.key === "ArrowRight" && language === last
        ? first
        : event.key === "ArrowLeft" && language === first
          ? last
          : undefined;
    if (target === undefined || target === language || hasModifiers) {
      return;
    }
    event.preventDefault();
    items.current.get(target)?.focus();
  };
  return (
    <ToggleGroup aria-label="Språk" onChange={onChange} size="small" value={value}>
      {languages.map((language) => (
        <Tooltip content={languageLabel(language)} key={language}>
          <ToggleGroup.Item
            label={languageShortLabel(language)}
            onFocus={() => language !== value && onChange(language)}
            onKeyDown={(event) => wrapAround(event, language)}
            ref={(element) => {
              if (element) {
                items.current.set(language, element);
              } else {
                items.current.delete(language);
              }
            }}
            value={language}
          />
        </Tooltip>
      ))}
    </ToggleGroup>
  );
}
