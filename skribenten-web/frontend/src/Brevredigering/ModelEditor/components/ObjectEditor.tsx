import { css } from "@emotion/react";
import { Switch } from "@navikt/ds-react";
import { useState } from "react";
import { useFormContext } from "react-hook-form";

import { useModelSpecification } from "~/api/brev-queries";
import { EnumEditor } from "~/Brevredigering/ModelEditor/components/EnumEditor";
import { ScalarEditor } from "~/Brevredigering/ModelEditor/components/ScalarEditor";
import { convertFieldToReadableLabel, getFieldDefaultValue } from "~/Brevredigering/ModelEditor/components/utils";
import type { FieldType } from "~/types/brevbakerTypes";

const FieldEditor = ({ brevkode, field, fieldType }: { brevkode: string; field: string; fieldType: FieldType }) => {
  switch (fieldType.type) {
    case "object": {
      return fieldType.nullable ? (
        <ToggleableObjectEditor brevkode={brevkode} parentFieldName={field} typeName={fieldType.typeName} />
      ) : (
        <ObjectEditor brevkode={brevkode} parentFieldName={field} typeName={fieldType.typeName} />
      );
    }
    case "scalar": {
      return <ScalarEditor field={field} fieldType={fieldType} />;
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
};

export const ObjectEditor = ({ brevkode, typeName, parentFieldName }: ObjectEditorProperties) => {
  const objectTypeSpecification = useModelSpecification(brevkode, (s) => s.types[typeName]);

  return (
    <>
      {Object.entries(objectTypeSpecification ?? {}).map(([field, fieldType]) => {
        const fieldName = parentFieldName ? `${parentFieldName}.${field}` : field;
        return <FieldEditor brevkode={brevkode} field={fieldName} fieldType={fieldType} key={field} />;
      })}
    </>
  );
};

function ToggleableObjectEditor({
  brevkode,
  parentFieldName,
  typeName,
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
          <ObjectEditor brevkode={brevkode} parentFieldName={parentFieldName} typeName={typeName} />
        </div>
      )}
    </>
  );
}
