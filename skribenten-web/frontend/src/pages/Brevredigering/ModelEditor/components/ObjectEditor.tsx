import type { ObjectTypeSpecification, ObjectTypeSpecifications, TObject } from "~/types/brevbakerTypes";

import type { BoundAction } from "../../LetterEditor/lib/actions";
import { bindAction } from "../../LetterEditor/lib/actions";
import { ModelValueAction } from "../actions";
import type { FieldValue } from "../model";
import { FieldEditor } from "./FieldEditor";
import styles from "./ObjectEditor.module.css";

export interface ObjectEditorProperties {
  allSpecs: ObjectTypeSpecifications;
  spec: ObjectTypeSpecification;
  value: FieldValue<TObject>;
  updateValue: BoundAction<[value: FieldValue<TObject>]>;
}

export const ObjectEditor = ({ allSpecs, spec, value, updateValue }: ObjectEditorProperties) => {
  return (
    <div className={styles.container}>
      {Object.entries(spec).map(([name, field]) => {
        const updateField = bindAction(ModelValueAction.UpdateField, updateValue, value, name);
        return (
          <div key={name}>
            <label>
              {name}:
              {field.nullable && value[name] != null && (
                <button onClick={() => updateField(null)} type="button">
                  Remove
                </button>
              )}
              <FieldEditor allSpecs={allSpecs} spec={field} updateValue={updateField} value={value[name]} />
            </label>
          </div>
        );
      })}
    </div>
  );
};
