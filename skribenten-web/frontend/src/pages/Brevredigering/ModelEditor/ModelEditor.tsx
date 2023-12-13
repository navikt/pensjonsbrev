import type { LetterModelSpecification } from "~/types/brevbakerTypes";

import type { BoundAction } from "../LetterEditor/lib/actions";
import { ObjectEditor } from "./components/ObjectEditor";
import type { ObjectValue } from "./model";
import styles from "./ModelEditor.module.css";

export interface ModelSpecificationEditorProperties {
  spec: LetterModelSpecification;
  value: ObjectValue;
  updateValue: BoundAction<[value: ObjectValue]>;
}

export const ModelEditor = ({ spec, value, updateValue }: ModelSpecificationEditorProperties) => {
  const objectTypeSpec = spec.types[spec.letterModelTypeName];

  return (
    <div className={styles.container}>
      <div>
        <h2>Brevredigering</h2>
      </div>
      <div className={styles.tabmenu}>
        <div>Overstyring</div>
      </div>
      <div>
        <form>
          <ObjectEditor allSpecs={spec.types} spec={objectTypeSpec} updateValue={updateValue} value={value} />
        </form>
      </div>
    </div>
  );
};
