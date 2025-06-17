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

  return (
    <Controller
      control={control}
      defaultValue={spec.values[0]}
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
          {spec.values.map((value) => (
            <option key={value} value={value}>
              {value}
            </option>
          ))}
        </Select>
      )}
    />
  );
};
