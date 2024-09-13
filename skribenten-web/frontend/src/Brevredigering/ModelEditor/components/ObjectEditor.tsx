import { css } from "@emotion/react";
import { Switch } from "@navikt/ds-react";
import { useState } from "react";
import { useFormContext } from "react-hook-form";

import { useModelSpecification } from "~/api/brev-queries";
import { EnumEditor } from "~/Brevredigering/ModelEditor/components/EnumEditor";
import { ScalarEditor } from "~/Brevredigering/ModelEditor/components/ScalarEditor";
import { convertFieldToReadableLabel, getFieldDefaultValue } from "~/Brevredigering/ModelEditor/components/utils";
import type { SaksbehandlerValg } from "~/types/brev";
import type { FieldType } from "~/types/brevbakerTypes";

const FieldEditor = ({
  brevkode,
  field,
  fieldType,
  submitOnChange,
  siblings,
}: {
  brevkode: string;
  field: string;
  fieldType: FieldType;
  submitOnChange?: (valg: SaksbehandlerValg) => void;
  siblings: {
    name: string;
    siblingType: FieldType;
    parentFieldName: string | undefined;
  }[];
}) => {
  switch (fieldType.type) {
    case "object": {
      return fieldType.nullable ? (
        <ToggleableObjectEditor
          brevkode={brevkode}
          parentFieldName={field}
          submitOnChange={submitOnChange}
          typeName={fieldType.typeName}
        />
      ) : (
        <ObjectEditor
          brevkode={brevkode}
          parentFieldName={field}
          submitOnChange={submitOnChange}
          typeName={fieldType.typeName}
        />
      );
    }
    case "scalar": {
      return <ScalarEditor field={field} fieldType={fieldType} siblings={siblings} submitOnChange={submitOnChange} />;
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
  submitOnChange?: (valg: SaksbehandlerValg) => void;
};

export const ObjectEditor = ({ brevkode, typeName, parentFieldName, submitOnChange }: ObjectEditorProperties) => {
  const objectTypeSpecification = useModelSpecification(brevkode, (s) => s.types[typeName]);

  const selfAndSiblings = Object.entries(objectTypeSpecification ?? {});

  return (
    <>
      {Object.entries(objectTypeSpecification ?? {}).map(([field, fieldType]) => {
        const fieldName = parentFieldName ? `${parentFieldName}.${field}` : field;
        const siblings = selfAndSiblings
          .filter((sibling) => sibling[0] !== field)
          .map(([sibling, siblingType]) => ({
            name: sibling,
            siblingType: siblingType,
            parentFieldName: parentFieldName,
          }));
        return (
          <FieldEditor
            brevkode={brevkode}
            field={fieldName}
            fieldType={fieldType}
            key={field}
            siblings={siblings}
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
