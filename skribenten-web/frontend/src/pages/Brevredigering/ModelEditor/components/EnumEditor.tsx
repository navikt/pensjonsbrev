import type { TEnum } from "~/types/brevbakerTypes";

import type { BoundAction } from "../../LetterEditor/lib/actions";
import type { FieldValue } from "../model";

interface EnumEditorProperties {
  spec: TEnum;
  value: FieldValue<TEnum>;
  updateValue: BoundAction<[FieldValue<TEnum>]>;
}

export const EnumEditor = ({ spec, value, updateValue }: EnumEditorProperties) => (
  <select onChange={(event) => updateValue(event.target.value)} value={value}>
    {spec.values.map((opt) => (
      <option key={opt} value={opt}>
        {opt}
      </option>
    ))}
  </select>
);
