import { Checkbox, DatePicker, TextField, useDatepicker } from "@navikt/ds-react";
import { Controller, get, useFormContext } from "react-hook-form";

import { convertFieldToReadableLabel, getFieldDefaultValue } from "~/Brevredigering/ModelEditor/components/utils";
import { FullWidthDatePickerWrapper } from "~/components/FullWidthDatePickerWrapper";
import type { TScalar } from "~/types/brevbakerTypes";
import { formatDateWithoutTimezone, parseDate } from "~/utils/dateUtils";

export const ScalarEditor = ({ fieldType, field }: { field: string; fieldType: TScalar }) => {
  const {
    register,
    formState: { errors },
  } = useFormContext();

  const potentialError = get(errors, field)?.message?.toString();
  const registerProperties = register(field, { required: fieldType.nullable ? false : "Må oppgis" });

  const commonTextFieldProperties = {
    ...registerProperties,
    autoComplete: "off",
    error: potentialError,
    label: convertFieldToReadableLabel(field),
    size: "small" as const,
  };

  switch (fieldType.kind) {
    case "NUMBER": {
      return <TextField {...commonTextFieldProperties} step={1} type="number" />;
    }
    case "DOUBLE": {
      return <TextField {...commonTextFieldProperties} step={0.1} type="number" />;
    }
    case "STRING": {
      return <TextField {...commonTextFieldProperties} type="text" />;
    }
    case "BOOLEAN": {
      // TODO: reimplement when an example template exists
      return <Checkbox>{convertFieldToReadableLabel(field)}</Checkbox>;
    }
    case "DATE": {
      return <ControlledDatePicker field={field} />;
    }
  }
};

function ControlledDatePicker({ field }: { field: string }) {
  const { control } = useFormContext();
  return (
    <Controller
      control={control}
      name={field}
      render={({ field: { onChange } }) => <DatePickerEditor field={field} onChange={onChange} />}
    />
  );
}
function DatePickerEditor({ field, onChange }: { field: string; onChange: (newDate: string) => void }) {
  const {
    formState: { errors, defaultValues },
  } = useFormContext();
  // For some reason form defaultValues does not work with datepicker, and we have to pick it ourselves
  const defaultValue = getFieldDefaultValue(defaultValues, field);

  const datepicker = useDatepicker({
    inputFormat: "yyyy-MM-dd",
    defaultSelected: defaultValue ? parseDate(defaultValue) : undefined,
    onDateChange: (date) => {
      onChange(date ? formatDateWithoutTimezone(date) : "");
    },
  });

  const potentialError = get(errors, field)?.message?.toString();

  return (
    <FullWidthDatePickerWrapper>
      <DatePicker {...datepicker.datepickerProps}>
        <DatePicker.Input
          {...datepicker.inputProps}
          error={potentialError}
          label={convertFieldToReadableLabel(field)}
          size="small"
        />
      </DatePicker>
    </FullWidthDatePickerWrapper>
  );
}
