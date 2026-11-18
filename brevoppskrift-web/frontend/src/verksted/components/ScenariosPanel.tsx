import { Box, Button, Checkbox, HStack, Select, Textarea, TextField, VStack } from "@navikt/ds-react";
import { useState } from "react";

import { removeAt, replaceAt } from "../draftOps";
import { newId } from "../ids";
import { type Draft, type Field, type JsonValue, type Scenario } from "../types";
import { useEditor } from "./EditorContext";

function JsonInput({
  label,
  value,
  onChange,
}: {
  label: string;
  value: JsonValue | undefined;
  onChange: (v: JsonValue) => void;
}) {
  const { canEdit } = useEditor();
  const [text, setText] = useState(JSON.stringify(value ?? null, null, 2));
  const [error, setError] = useState<string>();
  return (
    <Textarea
      description="JSON. Bare syntetiske verdier, aldri persondata."
      error={error}
      label={label}
      onChange={(event) => {
        setText(event.target.value);
        try {
          onChange(JSON.parse(event.target.value) as JsonValue);
          setError(undefined);
        } catch {
          setError("Ugyldig JSON");
        }
      }}
      readOnly={!canEdit}
      size="small"
      value={text}
    />
  );
}

function ValueInput({
  field,
  value,
  onChange,
}: {
  field: Field;
  value: JsonValue | undefined;
  onChange: (v: JsonValue | undefined) => void;
}) {
  const { canEdit } = useEditor();
  const label = field.name;
  if (field.nullable && value === undefined) {
    return (
      <HStack align="center" gap="space-8">
        <span>{label}: ingen verdi</span>
        {canEdit ? (
          <Button onClick={() => onChange(defaultValue(field))} size="xsmall" variant="tertiary">
            Sett verdi for {label}
          </Button>
        ) : null}
      </HStack>
    );
  }
  const clear =
    field.nullable && canEdit ? (
      <Button onClick={() => onChange(undefined)} size="xsmall" variant="tertiary">
        Fjern verdi for {label}
      </Button>
    ) : null;
  switch (field.kind) {
    case "BOOLEAN": {
      return (
        <HStack align="center" gap="space-8">
          <Checkbox
            checked={value === true}
            disabled={!canEdit}
            onChange={(e) => onChange(e.target.checked)}
            size="small"
          >
            {label}
          </Checkbox>
          {clear}
        </HStack>
      );
    }
    case "ENUM": {
      return (
        <HStack align="end" gap="space-8">
          <Select
            disabled={!canEdit}
            label={label}
            onChange={(e) => onChange(e.target.value)}
            size="small"
            value={String(value ?? "")}
          >
            <option value="">Velg …</option>
            {field.enumValues
              .filter((v) => v.trim() !== "")
              .map((v) => (
                <option key={v} value={v}>
                  {v}
                </option>
              ))}
          </Select>
          {clear}
        </HStack>
      );
    }
    case "OBJECT":
    case "LIST": {
      return <JsonInput label={label} onChange={onChange} value={value} />;
    }
    default: {
      const numeric = ["NUMBER", "DOUBLE", "PERCENT", "YEAR"].includes(field.kind);
      const type = numeric ? "number" : "text";
      const hint = field.kind === "DATE" ? "ÅÅÅÅ-MM-DD" : field.kind === "MONTH" ? "ÅÅÅÅ-MM" : undefined;
      return (
        <HStack align="end" gap="space-8">
          <TextField
            description={hint}
            label={label}
            onChange={(e) => onChange(numeric ? (e.target.value === "" ? 0 : Number(e.target.value)) : e.target.value)}
            readOnly={!canEdit}
            size="small"
            type={type}
            value={String(value ?? "")}
          />
          {clear}
        </HStack>
      );
    }
  }
}

function defaultValue(field: Field): JsonValue {
  switch (field.kind) {
    case "BOOLEAN": {
      return false;
    }
    case "NUMBER":
    case "DOUBLE":
    case "PERCENT":
    case "YEAR": {
      return 0;
    }
    case "OBJECT": {
      return {};
    }
    case "LIST": {
      return [];
    }
    default: {
      return "";
    }
  }
}

export function ScenariosPanel({
  draft,
  onChange,
  selectedId,
  onSelect,
}: {
  draft: Draft;
  onChange: (next: Draft) => void;
  selectedId: string | undefined;
  onSelect: (id: string | undefined) => void;
}) {
  const { canEdit } = useEditor();
  const scenarios = draft.scenarios ?? [];
  const selected = scenarios.find((s) => s.id === selectedId);
  const update = (scenario: Scenario) =>
    onChange({ ...draft, scenarios: replaceAt(scenarios, scenarios.indexOf(selected as Scenario), scenario) });

  const add = () => {
    const scenario: Scenario = { id: newId(), name: `Scenario ${scenarios.length + 1}`, values: {}, choices: {} };
    for (const field of draft.fields) {
      if (!field.nullable) {
        scenario.values[field.id] = defaultValue(field);
      }
    }
    onChange({ ...draft, scenarios: [...scenarios, scenario] });
    onSelect(scenario.id);
  };

  return (
    <VStack gap="space-12">
      <span>Syntetiske testdata. Bruk aldri ekte person- eller saksopplysninger.</span>
      <HStack align="end" gap="space-8">
        <Select
          label="Scenario"
          onChange={(event) => onSelect(event.target.value || undefined)}
          size="small"
          value={selectedId ?? ""}
        >
          <option value="">Ingen scenario</option>
          {scenarios.map((s) => (
            <option key={s.id} value={s.id}>
              {s.name}
            </option>
          ))}
        </Select>
        {canEdit ? (
          <Button onClick={add} size="small" variant="secondary">
            Nytt scenario
          </Button>
        ) : null}
      </HStack>
      {selected ? (
        <Box borderColor="neutral-subtle" borderRadius="4" borderWidth="1" padding="space-12">
          <VStack gap="space-12">
            <TextField
              label="Navn på scenario"
              onChange={(event) => update({ ...selected, name: event.target.value })}
              readOnly={!canEdit}
              size="small"
              value={selected.name}
            />
            {draft.fields.length === 0 ? <span>Malen har ingen datafelt.</span> : null}
            {draft.fields.map((field) => (
              <ValueInput
                field={field}
                key={field.id}
                onChange={(value) => {
                  const values = { ...selected.values };
                  if (value === undefined) {
                    delete values[field.id];
                  } else {
                    values[field.id] = value;
                  }
                  update({ ...selected, values });
                }}
                value={selected.values[field.id]}
              />
            ))}
            {draft.choices.map((choice) => (
              <Checkbox
                checked={selected.choices[choice.id] ?? false}
                disabled={!canEdit}
                key={choice.id}
                onChange={(event) =>
                  update({ ...selected, choices: { ...selected.choices, [choice.id]: event.target.checked } })
                }
                size="small"
              >
                Valg: {choice.name}
              </Checkbox>
            ))}
            {canEdit ? (
              <div>
                <Button
                  data-color="danger"
                  onClick={() => {
                    onChange({ ...draft, scenarios: removeAt(scenarios, scenarios.indexOf(selected)) });
                    onSelect(undefined);
                  }}
                  size="xsmall"
                  variant="tertiary"
                >
                  Slett scenario
                </Button>
              </div>
            ) : null}
          </VStack>
        </Box>
      ) : null}
    </VStack>
  );
}
