import { Box, Button, Checkbox, HStack, Select, Textarea, TextField, VStack } from "@navikt/ds-react";

import { countChoiceReferences, countFieldReferences, newChoice, newField, removeAt, replaceAt } from "../draftOps";
import { type Choice, type Draft, FIELD_KINDS, type Field, type FieldKind } from "../types";
import { useEditor } from "./EditorContext";
import { TranslationsField } from "./TranslationsField";

const KIND_LABELS: Record<FieldKind, string> = {
  STRING: "Tekst",
  NUMBER: "Heltall",
  DOUBLE: "Desimaltall",
  BOOLEAN: "Ja/nei",
  DATE: "Dato",
  YEAR: "År",
  MONTH: "Måned",
  PERCENT: "Prosent",
  ENUM: "Enum (faste verdier)",
  OBJECT: "Objekt",
  LIST: "Liste",
};

function FieldNode({
  field,
  draft,
  onChange,
  onDelete,
  depth,
}: {
  field: Field;
  draft: Draft;
  onChange: (next: Field) => void;
  onDelete: () => void;
  depth: number;
}) {
  const { canEditStructure: canEdit, typing } = useEditor();
  const references = countFieldReferences(draft, field.id);
  const hasChildren = field.kind === "OBJECT" || field.kind === "LIST";
  return (
    <Box borderColor="neutral-subtle" borderRadius="4" borderWidth="1" padding="space-8">
      <VStack gap="space-8">
        <HStack align="end" gap="space-8" wrap>
          <TextField
            label="Feltnavn"
            onChange={(event) => {
              typing(`field-name:${field.id}`);
              onChange({ ...field, name: event.target.value });
            }}
            readOnly={!canEdit}
            size="small"
            value={field.name}
          />
          <Select
            disabled={!canEdit}
            label="Type"
            onChange={(event) => {
              const kind = event.target.value as FieldKind;
              onChange({
                ...field,
                kind,
                children: kind === "OBJECT" || kind === "LIST" ? field.children : [],
                enumValues: kind === "ENUM" ? field.enumValues : [],
              });
            }}
            size="small"
            value={field.kind}
          >
            {FIELD_KINDS.map((kind) => (
              <option key={kind} value={kind}>
                {KIND_LABELS[kind]}
              </option>
            ))}
          </Select>
          <Checkbox
            checked={field.nullable}
            disabled={!canEdit}
            onChange={(event) => onChange({ ...field, nullable: event.target.checked })}
            size="small"
          >
            Kan mangle
          </Checkbox>
          {canEdit ? (
            <Button
              data-color="danger"
              disabled={references > 0}
              onClick={onDelete}
              size="xsmall"
              title={
                references > 0
                  ? `Brukes ${references} sted${references === 1 ? "" : "er"} i brevet`
                  : `Slett feltet ${field.name}`
              }
              variant="tertiary"
            >
              Slett felt
            </Button>
          ) : null}
        </HStack>
        {references > 0 ? (
          <span>
            Brukt {references} gang{references === 1 ? "" : "er"} i brevet.
          </span>
        ) : null}
        {field.kind === "ENUM" ? (
          <Textarea
            description="Én verdi per linje"
            label={`Verdier for ${field.name}`}
            onChange={(event) => onChange({ ...field, enumValues: event.target.value.split("\n") })}
            readOnly={!canEdit}
            size="small"
            value={field.enumValues.join("\n")}
          />
        ) : null}
        {hasChildren ? (
          <VStack gap="space-8" paddingInline={depth < 4 ? "space-16 space-0" : undefined}>
            {field.children.map((child, index) => (
              <FieldNode
                depth={depth + 1}
                draft={draft}
                field={child}
                key={child.id}
                onChange={(next) => onChange({ ...field, children: replaceAt(field.children, index, next) })}
                onDelete={() => onChange({ ...field, children: removeAt(field.children, index) })}
              />
            ))}
            {canEdit ? (
              <div>
                <Button
                  onClick={() => onChange({ ...field, children: [...field.children, newField(draft.fields)] })}
                  size="xsmall"
                  variant="secondary"
                >
                  Legg til delfelt i {field.name}
                </Button>
              </div>
            ) : null}
          </VStack>
        ) : null}
      </VStack>
    </Box>
  );
}

export function FieldsPanel({ draft, onChange }: { draft: Draft; onChange: (next: Draft) => void }) {
  const { canEditStructure: canEdit } = useEditor();
  return (
    <VStack gap="space-8">
      {draft.fields.length === 0 ? <span>Ingen datafelt ennå.</span> : null}
      {draft.fields.map((field, index) => (
        <FieldNode
          depth={0}
          draft={draft}
          field={field}
          key={field.id}
          onChange={(next) => onChange({ ...draft, fields: replaceAt(draft.fields, index, next) })}
          onDelete={() => onChange({ ...draft, fields: removeAt(draft.fields, index) })}
        />
      ))}
      {canEdit ? (
        <div>
          <Button
            onClick={() => onChange({ ...draft, fields: [...draft.fields, newField(draft.fields)] })}
            size="small"
            variant="secondary"
          >
            Legg til datafelt
          </Button>
        </div>
      ) : null}
    </VStack>
  );
}

export function ChoicesPanel({ draft, onChange }: { draft: Draft; onChange: (next: Draft) => void }) {
  const { canEditStructure: canEdit, languages, typing } = useEditor();
  const update = (index: number, choice: Choice) =>
    onChange({ ...draft, choices: replaceAt(draft.choices, index, choice) });
  return (
    <VStack gap="space-8">
      {draft.choices.length === 0 ? <span>Ingen saksbehandlervalg ennå.</span> : null}
      {draft.choices.map((choice, index) => {
        const references = countChoiceReferences(draft, choice.id);
        return (
          <Box borderColor="neutral-subtle" borderRadius="4" borderWidth="1" key={choice.id} padding="space-8">
            <VStack gap="space-8">
              <TextField
                label="Valgnavn"
                onChange={(event) => {
                  typing(`choice-name:${choice.id}`);
                  update(index, { ...choice, name: event.target.value });
                }}
                readOnly={!canEdit}
                size="small"
                value={choice.name}
              />
              <TranslationsField
                label="Spørsmål til saksbehandler"
                onChange={(prompt) => update(index, { ...choice, prompt })}
                value={choice.prompt}
              />
              {canEdit ? (
                <div>
                  <Button
                    data-color="danger"
                    disabled={references > 0}
                    onClick={() => onChange({ ...draft, choices: removeAt(draft.choices, index) })}
                    size="xsmall"
                    title={references > 0 ? `Brukes ${references} sted i vilkår` : `Slett valget ${choice.name}`}
                    variant="tertiary"
                  >
                    Slett valg
                  </Button>
                </div>
              ) : null}
            </VStack>
          </Box>
        );
      })}
      {canEdit ? (
        <div>
          <Button
            onClick={() => onChange({ ...draft, choices: [...draft.choices, newChoice(draft.choices, languages)] })}
            size="small"
            variant="secondary"
          >
            Legg til valg
          </Button>
        </div>
      ) : null}
    </VStack>
  );
}
