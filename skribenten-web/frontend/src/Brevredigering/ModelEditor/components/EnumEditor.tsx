import { Radio, RadioGroup } from "@navikt/ds-react";
import { Controller, useFormContext } from "react-hook-form";

import { type TEnum } from "~/types/brevbakerTypes";

import { convertFieldToReadableLabel } from "./utils";

export const EnumEditor = ({
  fieldName,
  spec,
  submitOnChange,
  readOnly,
}: {
  fieldName: string;
  spec: TEnum;
  submitOnChange?: () => void;
  readOnly?: boolean;
}) => {
  const { control } = useFormContext();

  const values = spec.values.toSorted((v1, v2) =>
    (v1.displayText || v1.value) > (v2.displayText || v2.value) ? 1 : -1,
  );

  return (
    <Controller
      control={control}
      name={fieldName}
      render={({ field, fieldState }) => (
        <RadioGroup
          {...field}
          error={fieldState.error?.message}
          legend={spec.displayText ?? convertFieldToReadableLabel(fieldName)}
          name={fieldName}
          onChange={(value) => {
            field.onChange(value);
            submitOnChange?.();
          }}
          readOnly={readOnly}
          size="small"
        >
          {values.map((value) => (
            <Radio key={value.value} value={value.value}>
              {value.displayText ?? value.value}
            </Radio>
          ))}
        </RadioGroup>
      )}
    />
  );
};
