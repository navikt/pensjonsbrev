import { Checkbox, CheckboxGroup, Select, TextField, VStack } from "@navikt/ds-react";

import { languagesWithContent } from "../draftOps";
import { type Draft, type DraftType, LANGUAGE_LABELS, LANGUAGES, type Language, type TargetModule } from "../types";
import { useEditor } from "./EditorContext";
import { TranslationsField } from "./TranslationsField";

const TYPE_LABELS: Record<DraftType, string> = {
  AUTOBREV: "Autobrev",
  REDIGERBAR: "Redigerbart brev",
  VEDLEGG: "Vedlegg",
};
const MODULE_LABELS: Record<TargetModule, string> = {
  UFOERE: "Uføre",
  PENSJON: "Pensjon",
  ALDER: "Alder",
  PLANLEGGE_PENSJON: "Planlegge pensjon",
};

export function MetadataPanel({ draft, onChange }: { draft: Draft; onChange: (next: Draft) => void }) {
  const { canEditStructure: canEdit, typing } = useEditor();
  const used = languagesWithContent(draft);
  const setMeta = (patch: Partial<Draft["metadata"]>) =>
    onChange({ ...draft, metadata: { ...draft.metadata, ...patch } });
  return (
    <VStack gap="space-16">
      <TextField
        label="Navn"
        onChange={(event) => {
          typing("metadata-name");
          setMeta({ name: event.target.value });
        }}
        readOnly={!canEdit}
        size="small"
        value={draft.metadata.name}
      />
      <TranslationsField label="Tittel" onChange={(title) => onChange({ ...draft, title })} value={draft.title} />
      <Select
        disabled={!canEdit}
        label="Brevtype"
        onChange={(event) => setMeta({ type: event.target.value as DraftType })}
        size="small"
        value={draft.metadata.type}
      >
        {Object.entries(TYPE_LABELS).map(([value, label]) => (
          <option key={value} value={value}>
            {label}
          </option>
        ))}
      </Select>
      <Select
        disabled={!canEdit}
        label="Målmodul"
        onChange={(event) => setMeta({ targetModule: event.target.value as TargetModule })}
        size="small"
        value={draft.metadata.targetModule}
      >
        {Object.entries(MODULE_LABELS).map(([value, label]) => (
          <option key={value} value={value}>
            {label}
          </option>
        ))}
      </Select>
      <CheckboxGroup
        description="Et språk med innhold kan ikke fjernes, siden det ville slette oversettelser."
        legend="Språk"
        onChange={(values: Language[]) =>
          setMeta({ languages: LANGUAGES.filter((language) => values.includes(language)) })
        }
        size="small"
        value={draft.metadata.languages}
      >
        {LANGUAGES.map((language) => (
          <Checkbox
            disabled={!canEdit || (draft.metadata.languages.includes(language) && used.has(language))}
            key={language}
            value={language}
          >
            {LANGUAGE_LABELS[language]} ({language})
          </Checkbox>
        ))}
      </CheckboxGroup>
      {draft.source ? (
        <dl>
          <dt>Kilde</dt>
          <dd>
            {draft.source.id} @ {draft.source.commit.slice(0, 10)}
          </dd>
        </dl>
      ) : null}
    </VStack>
  );
}
