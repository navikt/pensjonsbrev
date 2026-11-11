import { Loader, ToggleGroup, Tooltip } from "@navikt/ds-react";
import { type KeyboardEvent, useRef, useState } from "react";

import { LANGUAGES, languageLabel, languageShortLabel } from "~/search/components/format";

/**
 * Switches between a template's language versions. Only the languages the
 * template is written in are shown, always in the same order (NB, NN, EN).
 *
 * Selection follows focus, as in a radio group: the arrow keys switch
 * language right away, without Enter or Space. The arrow keys wrap around, so
 * the other languages are always one key press away.
 *
 * The chosen language is selected right away, and a spinner replaces its name
 * until `value` follows, so selection, focus and the language that opens stay
 * in step while it loads. When the navigation `onChange` returns settles
 * without `value` following (e.g. redirected or superseded), the selection
 * falls back to `value`.
 */
export function LanguageToggle({
  available,
  value,
  onChange,
}: {
  /** The languages the template is written in. */
  available: readonly string[];
  value: string;
  onChange: (language: string) => void | Promise<void>;
}) {
  const languages = [
    ...LANGUAGES.filter((language) => available.includes(language)),
    ...available.filter((language) => !LANGUAGES.includes(language)),
  ];
  const items = useRef(new Map<string, HTMLButtonElement>());

  const [requested, setRequested] = useState(value);
  const [previousValue, setPreviousValue] = useState(value);
  if (value !== previousValue) {
    setPreviousValue(value);
    setRequested(value);
  }
  const latestValue = useRef(value);
  latestValue.current = value;
  const latestRequest = useRef(0);
  const select = (language: string) => {
    if (language !== requested) {
      setRequested(language);
      const request = ++latestRequest.current;
      void onChange(language)?.finally(() => {
        if (request === latestRequest.current) {
          setRequested(latestValue.current);
        }
      });
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
    <ToggleGroup aria-label="Språk" onChange={select} size="small" value={requested}>
      {languages.map((language) => (
        <Tooltip content={languageLabel(language)} key={language}>
          <ToggleGroup.Item
            label={
              language === requested && requested !== value ? (
                <Loader size="xsmall" title="Laster" />
              ) : (
                languageShortLabel(language)
              )
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
