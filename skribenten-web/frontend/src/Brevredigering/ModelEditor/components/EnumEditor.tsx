import { Select } from "@navikt/ds-react";
import { Controller, useFormContext } from "react-hook-form";

import type { TEnum } from "~/types/brevbakerTypes";

import { convertFieldToReadableLabel } from "./utils";

export const EnumEditor = ({
  fieldName,
  spec,
  submitOnChange,
}: {
  fieldName: string;
  spec: TEnum;
  submitOnChange?: () => void;
}) => {
  const { control } = useFormContext();

  const values = spec.values.toSorted((v1, v2) =>
    (v1.displayText || v1.value) > (v2.displayText || v2.value) ? 1 : -1,
  );

  return (
    <Controller
      control={control}
      defaultValue={values[0].value}
      name={fieldName}
      render={({ field }) => (
        <Select
          label={spec.displayText ?? convertFieldToReadableLabel(fieldName)}
          size="small"
          {...field}
          onChange={(e) => {
            field.onChange(e.target.value);
            submitOnChange?.();
          }}
        >
          {values.map((value) => (
            <option key={value.value} value={value.value}>
              {value.displayText ?? value.value}
            </option>
          ))}
        </Select>
      )}
    />
  );
};
