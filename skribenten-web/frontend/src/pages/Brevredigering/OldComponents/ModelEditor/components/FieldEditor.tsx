import type { FieldType, ObjectTypeSpecifications, TArray, TEnum, TObject, TScalar } from "~/types/brevbakerTypes";

import type { BoundAction } from "../../LetterEditor/lib/actions";
import type { FieldValue } from "../model";
import { initValueFromSpec } from "../model";
import { ArrayEditor } from "./ArrayEditor";
import { EnumEditor } from "./EnumEditor";
import { ObjectEditor } from "./ObjectEditor";
import { ScalarEditor } from "./ScalarEditor";

export interface FieldEditorProperties {
  allSpecs: ObjectTypeSpecifications;
  spec: FieldType;
  value: FieldValue<FieldType> | null;
  updateValue: BoundAction<[value: FieldValue<FieldType>]>;
}

export const FieldEditor = ({ allSpecs, spec, value, updateValue }: FieldEditorProperties) => {
  if (value == null) {
    if (spec.nullable) {
      return (
        <button onClick={() => updateValue(initValueFromSpec(allSpecs, spec, false))} type="button">
          Give value
        </button>
      );
    } else {
      // Since the entire model should be initialized we shouldn't end up here, but it is technically possible.
      updateValue(initValueFromSpec(allSpecs, spec, false));
      return <div>Initierer verdi</div>;
    }
  } else {
    switch (spec.type) {
      case "object": {
        return (
          <ObjectEditor
            allSpecs={allSpecs}
            spec={allSpecs[spec.typeName]}
            updateValue={updateValue}
            value={value as FieldValue<TObject>}
          />
        );
      }
      case "scalar": {
        return <ScalarEditor spec={spec} updateValue={updateValue} value={value as FieldValue<TScalar>} />;
      }
      case "array": {
        return (
          <ArrayEditor allSpecs={allSpecs} array={value as FieldValue<TArray>} spec={spec} updateArray={updateValue} />
        );
      }
      case "enum": {
        return <EnumEditor spec={spec} updateValue={updateValue} value={value as FieldValue<TEnum>} />;
      }
    }
  }
};
