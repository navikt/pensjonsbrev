import { css } from "@emotion/react";
import { BodyShort, Box, ErrorMessage, Label, Switch, VStack } from "@navikt/ds-react";
import { useEffect, useState } from "react";
import { useFormContext, useWatch } from "react-hook-form";

import { useModelSpecification } from "~/api/brev-queries";
import { EnumEditor } from "~/Brevredigering/ModelEditor/components/EnumEditor";
import { ScalarEditor } from "~/Brevredigering/ModelEditor/components/ScalarEditor";
import {
  convertFieldToReadableLabel,
  getFieldDefaultValue,
  isFieldNullableOrBoolean,
} from "~/Brevredigering/ModelEditor/components/utils";
import { type FieldType, type ObjectTypeSpecifications } from "~/types/brevbakerTypes";

export const FieldEditor = ({
  prependedName,
  brevkode,
  field,
  fieldType,
  submitOnChange,
}: {
  prependedName?: string;
  brevkode: string;
  field: string;
  fieldType: FieldType;
  submitOnChange?: () => void;
}) => {
  switch (fieldType.type) {
    case "object": {
      return fieldType.nullable ? (
        <ToggleableObjectEditor
          brevkode={brevkode}
          fieldType={fieldType}
          parentFieldName={prependedName ? `${prependedName}.${field}` : field}
          submitOnChange={submitOnChange}
          typeName={fieldType.typeName}
        />
      ) : (
        <ObjectEditor
          brevkode={brevkode}
          parentFieldName={prependedName ? `${prependedName}.${field}` : field}
          submitOnChange={submitOnChange}
          typeName={fieldType.typeName}
        />
      );
    }
    case "scalar": {
      return (
        <ScalarEditor field={field} fieldType={fieldType} prependName={prependedName} submitOnChange={submitOnChange} />
      );
    }
    case "array": {
      return <ArrayEditor brevkode={brevkode} field={field} fieldType={fieldType} prependedName={prependedName} />;
    }
    case "enum": {
      return (
        <EnumEditor
          fieldName={prependedName ? `${prependedName}.${field}` : field}
          spec={fieldType}
          submitOnChange={submitOnChange}
        />
      );
    }
  }
};

export type ObjectEditorProperties = {
  brevkode: string;
  typeName: string;
  parentFieldName?: string;
  submitOnChange?: () => void;
};

export const ObjectEditor = ({ brevkode, typeName, parentFieldName, submitOnChange }: ObjectEditorProperties) => {
  const { specification: objectTypeSpecification } = useModelSpecification(brevkode, (s) => s.types[typeName]);

  return (
    <>
      {Object.entries(objectTypeSpecification ?? {})
        .filter(([, fieldType]) => !isFieldNullableOrBoolean(fieldType))
        .map(([field, fieldType]) => {
          const fieldName = parentFieldName ? `${parentFieldName}.${field}` : field;
          return (
            <FieldEditor
              brevkode={brevkode}
              field={fieldName}
              fieldType={fieldType}
              key={field}
              submitOnChange={submitOnChange}
            />
          );
        })}
    </>
  );
};

function ToggleableObjectEditor({
  brevkode,
  parentFieldName,
  typeName,
  fieldType,
  submitOnChange,
}: ObjectEditorProperties & { parentFieldName: string; fieldType: FieldType }) {
  const {
    formState: { defaultValues },
    unregister,
  } = useFormContext();
  const defaultValue = getFieldDefaultValue(defaultValues, parentFieldName);
  const [open, setOpen] = useState(defaultValue !== null && defaultValue !== undefined);

  const handleToggle = () => {
    if (open) {
      unregister(parentFieldName, { keepDefaultValue: true });
    }
    //TODO - kaller på timeout for å sørge for at requestSubmit blir kalt etter at the togglebare feltene er blitt registert
    setTimeout(() => {
      submitOnChange?.();
    }, 500);
    setOpen(!open);
  };

  return (
    <>
      <Switch checked={open} onChange={handleToggle}>
        {fieldType.displayText ?? convertFieldToReadableLabel(parentFieldName)}
      </Switch>

      {open && (
        <div
          css={css`
            display: contents;

            > * {
              margin-left: var(--ax-space-16);
            }
          `}
        >
          <ObjectEditor
            brevkode={brevkode}
            parentFieldName={parentFieldName}
            submitOnChange={submitOnChange}
            typeName={typeName}
          />
        </div>
      )}
    </>
  );
}

function ArrayEditor({
  prependedName,
  brevkode,
  field,
  fieldType,
}: {
  prependedName?: string;
  brevkode: string;
  field: string;
  fieldType: FieldType & { type: "array" };
}) {
  const fieldName = prependedName ? `${prependedName}.${field}` : field;
  const { specification: types } = useModelSpecification(brevkode, (s) => s.types);

  return <ReadOnlyArray fieldName={fieldName} fieldType={fieldType} types={types} />;
}

function ReadOnlyArray({
  fieldName,
  fieldType,
  types,
}: {
  fieldName: string;
  fieldType: FieldType & { type: "array" };
  types: ObjectTypeSpecifications | undefined;
}) {
  const { control, formState, getFieldState, register } = useFormContext();
  const value = useWatch({ control, name: fieldName });
  const fieldState = getFieldState(fieldName, formState);
  const label = fieldType.displayText ?? convertFieldToReadableLabel(fieldName);

  useEffect(() => {
    register(fieldName, {
      required: fieldType.nullable ? false : "Må oppgis",
      validate: (value) => fieldType.nullable || Array.isArray(value) || "Må oppgis",
    });
  }, [fieldName, fieldType.nullable, register]);

  const values = Array.isArray(value) ? value : [];

  return (
    <Box borderColor="neutral-subtle" borderWidth="1" padding="space-12" style={{ borderRadius: "var(--ax-radius-8)" }}>
      <VStack gap="space-8">
        <Label size="small">{label}</Label>
        {fieldState.error?.message && <ErrorMessage size="small">{fieldState.error.message}</ErrorMessage>}
        {values.length === 0 ? (
          <BodyShort size="small">Ingen verdier</BodyShort>
        ) : (
          <VStack gap="space-12">
            {values.map((_, index) => (
              <ReadOnlyField
                fieldName={`${fieldName}.${index}`}
                fieldType={fieldType.items}
                key={`${fieldName}.${index}`}
                label={`${label} ${index + 1}`}
                types={types}
              />
            ))}
          </VStack>
        )}
      </VStack>
    </Box>
  );
}

function ReadOnlyField({
  fieldName,
  fieldType,
  label,
  types,
}: {
  fieldName: string;
  fieldType: FieldType;
  label?: string;
  types: ObjectTypeSpecifications | undefined;
}) {
  const { control, formState, getFieldState, register, unregister } = useFormContext();
  const value = useWatch({ control, name: fieldName });
  const fieldState = getFieldState(fieldName, formState);
  const displayLabel = label ?? fieldType.displayText ?? convertFieldToReadableLabel(fieldName);

  // Only register actual leaves (scalar/enum). Container types (object/array) are not
  // registered as leaves so we don't shadow the leaf registrations of their children.
  const isLeaf = fieldType.type === "scalar" || fieldType.type === "enum";
  useEffect(() => {
    if (!isLeaf) {
      return;
    }
    register(fieldName, {
      validate: (v) => fieldType.nullable || (v !== null && v !== undefined && v !== "") || "Må oppgis",
    });
    return () => unregister(fieldName, { keepValue: true, keepDefaultValue: true });
  }, [fieldName, fieldType.nullable, isLeaf, register, unregister]);

  switch (fieldType.type) {
    case "object": {
      const objectSpec = types?.[fieldType.typeName];
      if (!objectSpec || value === null || value === undefined) {
        return <ReadOnlyValue error={fieldState.error?.message} label={displayLabel} value={value} />;
      }

      return (
        <Box
          borderColor="neutral-subtle"
          borderWidth="1"
          padding="space-12"
          style={{ borderRadius: "var(--ax-radius-8)" }}
        >
          <VStack gap="space-8">
            <Label size="small">{displayLabel}</Label>
            {Object.entries(objectSpec).map(([field, childField]) => (
              <ReadOnlyField
                fieldName={`${fieldName}.${field}`}
                fieldType={childField}
                key={`${fieldName}.${field}`}
                types={types}
              />
            ))}
          </VStack>
        </Box>
      );
    }
    case "array":
      return <ReadOnlyArray fieldName={fieldName} fieldType={fieldType} types={types} />;
    case "enum":
    case "scalar":
      return (
        <ReadOnlyValue error={fieldState.error?.message} label={displayLabel} value={formatReadOnlyValue(value)} />
      );
  }
}

function ReadOnlyValue({ label, value, error }: { label: string; value: unknown; error?: string }) {
  return (
    <div>
      <Label size="small">{label}</Label>
      <BodyShort size="small">
        {value === null || value === undefined || value === "" ? "Ikke oppgitt" : String(value)}
      </BodyShort>
      {error && <ErrorMessage size="small">{error}</ErrorMessage>}
    </div>
  );
}

function formatReadOnlyValue(value: unknown): unknown {
  if (typeof value === "boolean") {
    return value ? "Ja" : "Nei";
  }
  return value;
}
