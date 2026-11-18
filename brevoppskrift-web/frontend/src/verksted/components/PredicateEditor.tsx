import { Box, Button, HStack, Select, TextField, VStack } from "@navikt/ds-react";

import { fieldsInScope, removeAt, replaceAt } from "../draftOps";
import { type FieldKind, type Literal, type Operator, type Predicate } from "../types";
import { useEditor } from "./EditorContext";

const OPERATORS: [Operator, string][] = [
  ["EQUAL", "er lik"],
  ["NOT_EQUAL", "er ikke lik"],
  ["GREATER_THAN", "er større enn"],
  ["GREATER_THAN_OR_EQUAL", "er større enn eller lik"],
  ["LESS_THAN", "er mindre enn"],
  ["LESS_THAN_OR_EQUAL", "er mindre enn eller lik"],
];

const PREDICATE_TYPES: [Predicate["type"], string][] = [
  ["IS_NOT_NULL", "Feltet har verdi"],
  ["COMPARE", "Sammenlign felt"],
  ["CHOICE", "Saksbehandlervalg"],
  ["ALL", "Alle av"],
  ["ANY", "Minst én av"],
  ["NOT", "Ikke"],
];

export function literalTypeFor(kind: FieldKind | undefined): Literal["type"] {
  switch (kind) {
    case "BOOLEAN": {
      return "BOOLEAN";
    }
    case "NUMBER":
    case "DOUBLE":
    case "PERCENT":
    case "YEAR": {
      return "NUMBER";
    }
    case "DATE":
    case "MONTH": {
      return "DATE";
    }
    default: {
      return "TEXT";
    }
  }
}

export function emptyLiteral(type: Literal["type"]): Literal {
  switch (type) {
    case "TEXT": {
      return { type, value: "" };
    }
    case "NUMBER": {
      return { type, value: 0 };
    }
    case "BOOLEAN": {
      return { type, value: true };
    }
    case "DATE": {
      return { type, value: "" };
    }
  }
}

export function emptyPredicate(type: Predicate["type"]): Predicate {
  switch (type) {
    case "IS_NOT_NULL": {
      return { type, fieldId: "" };
    }
    case "COMPARE": {
      return { type, fieldId: "", operator: "EQUAL", value: { type: "TEXT", value: "" } };
    }
    case "CHOICE": {
      return { type, choiceId: "", value: true };
    }
    case "ALL":
    case "ANY": {
      return { type, predicates: [emptyPredicate("IS_NOT_NULL")] };
    }
    case "NOT": {
      return { type, predicate: emptyPredicate("IS_NOT_NULL") };
    }
  }
}

export function FieldSelect({
  label,
  value,
  onChange,
  scope,
  onlyKinds,
  hideLabel,
}: {
  label: string;
  value: string;
  onChange: (fieldId: string) => void;
  scope: string[];
  onlyKinds?: FieldKind[];
  hideLabel?: boolean;
}) {
  const { fieldIndex, canEditStructure: canEdit } = useEditor();
  const options = fieldsInScope(fieldIndex, scope).filter((info) => !onlyKinds || onlyKinds.includes(info.field.kind));
  const unknown = value !== "" && !fieldIndex.has(value);
  return (
    <Select
      disabled={!canEdit}
      error={value === "" ? "Velg felt" : unknown ? "Feltet finnes ikke" : undefined}
      hideLabel={hideLabel}
      label={label}
      onChange={(event) => onChange(event.target.value)}
      size="small"
      value={value}
    >
      <option value="">Velg felt …</option>
      {unknown ? <option value={value}>Ukjent felt</option> : null}
      {options.map((info) => (
        <option key={info.field.id} value={info.field.id}>
          {info.path}
        </option>
      ))}
    </Select>
  );
}

export function PredicateEditor({
  predicate,
  onChange,
  scope,
  depth = 0,
}: {
  predicate: Predicate;
  onChange: (next: Predicate) => void;
  scope: string[];
  depth?: number;
}) {
  const { draft, fieldIndex, canEditStructure: canEdit } = useEditor();
  return (
    <Box background={depth % 2 === 0 ? "neutral-soft" : "default"} borderRadius="4" padding="space-8">
      <VStack gap="space-8">
        <Select
          disabled={!canEdit}
          label="Vilkår"
          onChange={(event) => onChange(emptyPredicate(event.target.value as Predicate["type"]))}
          size="small"
          value={predicate.type}
        >
          {PREDICATE_TYPES.map(([value, label]) => (
            <option key={value} value={value}>
              {label}
            </option>
          ))}
        </Select>
        {predicate.type === "IS_NOT_NULL" ? (
          <FieldSelect
            label="Felt"
            onChange={(fieldId) => onChange({ ...predicate, fieldId })}
            scope={scope}
            value={predicate.fieldId}
          />
        ) : null}
        {predicate.type === "COMPARE" ? (
          <HStack align="end" gap="space-8" wrap>
            <FieldSelect
              label="Felt"
              onChange={(fieldId) => {
                const kind = fieldIndex.get(fieldId)?.field.kind;
                const type = literalTypeFor(kind);
                onChange({
                  ...predicate,
                  fieldId,
                  value: predicate.value.type === type ? predicate.value : emptyLiteral(type),
                });
              }}
              scope={scope}
              value={predicate.fieldId}
            />
            <Select
              disabled={!canEdit}
              label="Operator"
              onChange={(event) => onChange({ ...predicate, operator: event.target.value as Operator })}
              size="small"
              value={predicate.operator}
            >
              {OPERATORS.map(([value, label]) => (
                <option key={value} value={value}>
                  {label}
                </option>
              ))}
            </Select>
            <LiteralInput onChange={(value) => onChange({ ...predicate, value })} value={predicate.value} />
          </HStack>
        ) : null}
        {predicate.type === "CHOICE" ? (
          <HStack align="end" gap="space-8">
            <Select
              disabled={!canEdit}
              error={predicate.choiceId === "" ? "Velg valg" : undefined}
              label="Valg"
              onChange={(event) => onChange({ ...predicate, choiceId: event.target.value })}
              size="small"
              value={predicate.choiceId}
            >
              <option value="">Velg valg …</option>
              {draft.choices.map((choice) => (
                <option key={choice.id} value={choice.id}>
                  {choice.name}
                </option>
              ))}
            </Select>
            <Select
              disabled={!canEdit}
              label="Er"
              onChange={(event) => onChange({ ...predicate, value: event.target.value === "true" })}
              size="small"
              value={String(predicate.value)}
            >
              <option value="true">Ja</option>
              <option value="false">Nei</option>
            </Select>
          </HStack>
        ) : null}
        {predicate.type === "NOT" ? (
          <PredicateEditor
            depth={depth + 1}
            onChange={(inner) => onChange({ ...predicate, predicate: inner })}
            predicate={predicate.predicate}
            scope={scope}
          />
        ) : null}
        {predicate.type === "ALL" || predicate.type === "ANY" ? (
          <VStack gap="space-8">
            {predicate.predicates.map((inner, index) => (
              <HStack align="start" gap="space-8" key={index}>
                <div style={{ flex: 1 }}>
                  <PredicateEditor
                    depth={depth + 1}
                    onChange={(next) =>
                      onChange({ ...predicate, predicates: replaceAt(predicate.predicates, index, next) })
                    }
                    predicate={inner}
                    scope={scope}
                  />
                </div>
                {canEdit && predicate.predicates.length > 1 ? (
                  <Button
                    onClick={() => onChange({ ...predicate, predicates: removeAt(predicate.predicates, index) })}
                    size="xsmall"
                    variant="tertiary"
                  >
                    Fjern vilkår
                  </Button>
                ) : null}
              </HStack>
            ))}
            {canEdit ? (
              <div>
                <Button
                  onClick={() =>
                    onChange({ ...predicate, predicates: [...predicate.predicates, emptyPredicate("IS_NOT_NULL")] })
                  }
                  size="xsmall"
                  variant="secondary"
                >
                  Legg til vilkår
                </Button>
              </div>
            ) : null}
          </VStack>
        ) : null}
      </VStack>
    </Box>
  );
}

function LiteralInput({ value, onChange }: { value: Literal; onChange: (value: Literal) => void }) {
  const { canEditStructure: canEdit } = useEditor();
  return (
    <HStack align="end" gap="space-8">
      <Select
        disabled={!canEdit}
        label="Verditype"
        onChange={(event) => onChange(emptyLiteral(event.target.value as Literal["type"]))}
        size="small"
        value={value.type}
      >
        <option value="TEXT">Tekst</option>
        <option value="NUMBER">Tall</option>
        <option value="BOOLEAN">Ja/nei</option>
        <option value="DATE">Dato</option>
      </Select>
      {value.type === "BOOLEAN" ? (
        <Select
          disabled={!canEdit}
          label="Verdi"
          onChange={(event) => onChange({ type: "BOOLEAN", value: event.target.value === "true" })}
          size="small"
          value={String(value.value)}
        >
          <option value="true">Ja</option>
          <option value="false">Nei</option>
        </Select>
      ) : (
        <TextField
          description={value.type === "DATE" ? "Format: ÅÅÅÅ-MM-DD" : undefined}
          label="Verdi"
          onChange={(event) =>
            onChange(
              value.type === "NUMBER"
                ? { type: "NUMBER", value: Number(event.target.value) }
                : { type: value.type, value: event.target.value },
            )
          }
          readOnly={!canEdit}
          size="small"
          type={value.type === "NUMBER" ? "number" : "text"}
          value={String(value.value)}
        />
      )}
    </HStack>
  );
}
