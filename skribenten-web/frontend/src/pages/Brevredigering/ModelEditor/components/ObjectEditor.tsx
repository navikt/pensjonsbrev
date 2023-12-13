import { css } from "@emotion/react";
import { Switch } from "@navikt/ds-react";
import { useState } from "react";

import { EnumEditor } from "~/pages/Brevredigering/ModelEditor/components/EnumEditor";
import { ScalarEditor } from "~/pages/Brevredigering/ModelEditor/components/ScalarEditor";
import { useObjectTypeSpecification } from "~/pages/Brevredigering/ModelEditor/components/useObjectTypeSpecification";
import type { FieldType, TObject } from "~/types/brevbakerTypes";

const FieldEditor = ({ field, fieldType }: { field: string; fieldType: FieldType }) => {
  switch (fieldType.type) {
    case "object": {
      return fieldType.nullable ? (
        <ToggleableObjectEditor field={field} fieldType={fieldType} />
      ) : (
        <ObjectEditor parentFieldName={field} typeName={fieldType.typeName} />
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

export const ObjectEditor = ({ typeName, parentFieldName }: { typeName: string; parentFieldName?: string }) => {
  const objectTypeSpecification = useObjectTypeSpecification(typeName);

  return (
    <>
      {Object.entries(objectTypeSpecification ?? {}).map(([field, fieldType]) => {
        const fieldName = parentFieldName ? `${parentFieldName}.${field}` : field;
        return <FieldEditor field={fieldName} fieldType={fieldType} key={field} />;
      })}
    </>
  );
};

function ToggleableObjectEditor({ field, fieldType }: { field: string; fieldType: TObject }) {
  const [open, setOpen] = useState(false);

  return (
    <>
      <Switch checked={open} onChange={() => setOpen(!open)}>
        {field}
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
          <ObjectEditor parentFieldName={field} typeName={fieldType.typeName} />
        </div>
      )}
    </>
  );
}
