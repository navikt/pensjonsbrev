import type { TScalar } from "../../../../../../types/brevbakerTypes";
import type { BoundAction } from "../../../LetterEditor/lib/actions";
import type { ScalarValue } from "../../model";

export interface ScalarEditorProperties {
  spec: TScalar;
  value: ScalarValue;
  updateValue: BoundAction<[value: ScalarValue]>;
}

const ScalarEditor = ({ spec, value, updateValue }: ScalarEditorProperties) => {
  switch (spec.kind) {
    case "NUMBER": {
      return (
        <input
          onChange={(e) => updateValue(e.target.value)}
          required={!spec.nullable}
          step={1}
          type="number"
          value={value as number}
        />
      );
    }
    case "DOUBLE": {
      return (
        <input
          onChange={(e) => updateValue(e.target.value)}
          required={!spec.nullable}
          step={0.1}
          type="number"
          value={value as number}
        />
      );
    }
    case "STRING": {
      return (
        <input
          onChange={(e) => updateValue(e.target.value)}
          required={!spec.nullable}
          type="text"
          value={value as string}
        />
      );
    }
    case "BOOLEAN": {
      return <input checked={value as boolean} onChange={(e) => updateValue(e.target.checked)} type="checkbox" />;
    }
    case "DATE": {
      return (
        <input
          onChange={(e) => updateValue(e.target.value)}
          required={!spec.nullable}
          type="date"
          value={value as string}
        />
      );
    }
  }
};

export default ScalarEditor;
