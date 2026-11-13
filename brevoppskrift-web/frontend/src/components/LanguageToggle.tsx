import { Loader, ToggleGroup, Tooltip } from "@navikt/ds-react";
import { type KeyboardEvent, useRef } from "react";

import { LANGUAGES, languageLabel, languageShortLabel } from "~/search/components/format";

/**
 * Switches between a template's language versions. Only the languages the
 * template is written in are shown, always in the same order (NB, NN, EN).
 *
 * Selection follows focus, as in a radio group: the arrow keys switch
 * language right away, without Enter or Space. The arrow keys wrap around, so
 * the other languages are always one key press away.
 *
 * `value` is the chosen language, and `loading` whether it is still loading,
 * in which case a spinner replaces its name. Selection, focus and the
 * language that opens stay in step while it loads.
 */
export function LanguageToggle({
  available,
  value,
  loading = false,
  onChange,
}: {
  /** The languages the template is written in. */
  available: readonly string[];
  value: string;
  loading?: boolean;
  onChange: (language: string) => void;
}) {
  const languages = [
    ...LANGUAGES.filter((language) => available.includes(language)),
    ...available.filter((language) => !LANGUAGES.includes(language)),
  ];
  const items = useRef(new Map<string, HTMLButtonElement>());

  const select = (language: string) => {
    if (language !== value) {
      onChange(language);
    }
  };

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
    <ToggleGroup aria-label="Språk" onChange={select} size="small" value={value}>
      {languages.map((language) => (
        <Tooltip content={languageLabel(language)} key={language}>
          <ToggleGroup.Item
            label={
              language === value && loading ? <Loader size="xsmall" title="Laster" /> : languageShortLabel(language)
            }
            onFocus={() => select(language)}
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
