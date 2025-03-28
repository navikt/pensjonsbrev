import { css } from "@emotion/react";
import { Switch } from "@navikt/ds-react";
import { useState } from "react";
import { useFormContext } from "react-hook-form";

import { useModelSpecification } from "~/api/brev-queries";
import { EnumEditor } from "~/Brevredigering/ModelEditor/components/EnumEditor";
import { ScalarEditor } from "~/Brevredigering/ModelEditor/components/ScalarEditor";
import {
  convertFieldToReadableLabel,
  getFieldDefaultValue,
  isFieldNullableOrBoolean,
} from "~/Brevredigering/ModelEditor/components/utils";
import type { FieldType } from "~/types/brevbakerTypes";

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
      return <div>ARRAY TODO</div>;
    }
    case "enum": {
      return <EnumEditor spec={fieldType} />;
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
  submitOnChange,
}: ObjectEditorProperties & { parentFieldName: string }) {
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
        {convertFieldToReadableLabel(parentFieldName)}
      </Switch>

      {open && (
        <div
          css={css`
            display: contents;

            > * {
              margin-left: var(--a-spacing-4);
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
