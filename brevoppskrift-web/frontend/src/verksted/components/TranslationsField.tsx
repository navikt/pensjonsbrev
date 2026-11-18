import { HStack, Tag, Textarea, TextField, VStack } from "@navikt/ds-react";
import { useId } from "react";

import { withTranslation } from "../draftOps";
import { LANGUAGE_LABELS, type Language, type Translations } from "../types";
import { useEditor } from "./EditorContext";

/**
 * Redigerer tekst for ett språk (eller alle samtidig). Bytte av språk endrer ingen tekst:
 * hver språkverdi ligger i samme `Translations`-objekt og bevares ved enhver redigering.
 */
export function TranslationsField({
  label,
  value,
  onChange,
  multiline = false,
  hideLabel = false,
  description,
}: {
  label: string;
  value: Translations;
  onChange: (next: Translations) => void;
  multiline?: boolean;
  hideLabel?: boolean;
  description?: string;
}) {
  const { languages, language, showAllLanguages, canEdit, typing } = useEditor();
  const key = useId();
  const shown: Language[] = showAllLanguages ? languages : [language];
  const missing = languages.filter((l) => (value[l] ?? "").trim() === "");
  const Input = multiline ? Textarea : TextField;
  return (
    <VStack gap="space-4">
      {shown.map((lang) => {
        const empty = (value[lang] ?? "").trim() === "";
        const fieldLabel = showAllLanguages || hideLabel ? `${label} (${LANGUAGE_LABELS[lang]})` : label;
        return (
          <Input
            description={description}
            error={empty ? `Mangler tekst på ${LANGUAGE_LABELS[lang].toLowerCase()}` : undefined}
            hideLabel={hideLabel}
            key={lang}
            label={fieldLabel}
            lang={lang.toLowerCase()}
            onChange={(event) => {
              typing(`${key}:${lang}`);
              onChange(withTranslation(value, lang, event.target.value));
            }}
            readOnly={!canEdit}
            size="small"
            value={value[lang] ?? ""}
          />
        );
      })}
      {!showAllLanguages && missing.some((l) => l !== language) ? (
        <HStack gap="space-4">
          {missing
            .filter((l) => l !== language)
            .map((l) => (
              <Tag data-color="warning" key={l} size="xsmall" variant="moderate">
                Mangler {l}
              </Tag>
            ))}
        </HStack>
      ) : null}
    </VStack>
  );
}
