import { ObjectEditor } from "~/pages/Brevredigering/ModelEditor/components/ObjectEditor";
import type { FieldType } from "~/types/brevbakerTypes";

import { EnumEditor } from "./EnumEditor";
import { ScalarEditor } from "./ScalarEditor";

export const FieldEditor = ({ field, fieldType }: { field: string; fieldType: FieldType }) => {
  switch (fieldType.type) {
    case "object": {
      return <ObjectEditor typeName={fieldType.typeName} />;
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
