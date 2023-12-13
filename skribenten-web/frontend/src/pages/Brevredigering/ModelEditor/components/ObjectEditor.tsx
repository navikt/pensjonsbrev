import { EnumEditor } from "~/pages/Brevredigering/ModelEditor/components/EnumEditor";
import { ScalarEditor } from "~/pages/Brevredigering/ModelEditor/components/ScalarEditor";
import { useTestIfThisWorks } from "~/pages/Brevredigering/ModelEditor/components/useTestIfThisWorks";
import type { FieldType } from "~/types/brevbakerTypes";

const FieldEditor = ({ field, fieldType }: { field: string; fieldType: FieldType }) => {
  switch (fieldType.type) {
    case "object": {
      return <ObjectEditor parentFieldName={field} typeName={fieldType.typeName} />;
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
  const objectTypeSpecification = useTestIfThisWorks(typeName);

  return (
    <div>
      {Object.entries(objectTypeSpecification ?? {}).map(([field, fieldType]) => {
        const fieldName = parentFieldName ? `${parentFieldName}.${field}` : field;
        return <FieldEditor field={fieldName} fieldType={fieldType} key={field} />;
      })}
    </div>
  );
};
