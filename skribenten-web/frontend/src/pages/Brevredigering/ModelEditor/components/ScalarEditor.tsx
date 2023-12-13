import { Checkbox, DatePicker, TextField, useDatepicker } from "@navikt/ds-react";
import { useFormContext } from "react-hook-form";

import type { TScalar } from "~/types/brevbakerTypes";

export const ScalarEditor = ({ fieldType, field }: { field: string; fieldType: TScalar }) => {
  const {
    register,
    formState: { errors },
  } = useFormContext();

  const potentialError = errors[field]?.message?.toString();
  const registerProperties = register(field, { required: fieldType.nullable ? false : "Må oppgis" });

  switch (fieldType.kind) {
    case "NUMBER": {
      return <TextField {...registerProperties} error={potentialError} label={field} step={1} type="number" />;
    }
    case "DOUBLE": {
      return <TextField {...registerProperties} error={potentialError} label={field} step={0.1} type="number" />;
    }
    case "STRING": {
      return <TextField {...registerProperties} error={potentialError} label={field} type="text" />;
    }
    case "BOOLEAN": {
      return <Checkbox>{field}</Checkbox>;
    }
    case "DATE": {
      return <DatePickerEditor field={field} />;
    }
  }
};

function DatePickerEditor({ field }: { field: string }) {
  const {
    setValue,
    formState: { errors },
  } = useFormContext();

  const datepicker = useDatepicker({
    onDateChange: (date) => {
      setValue(field, date ?? "");
    },
  });

  const potentialError = errors[field]?.message?.toString();

  return (
    <DatePicker {...datepicker.datepickerProps}>
      <DatePicker.Input {...datepicker.inputProps} error={potentialError} label={field} />
    </DatePicker>
  );
}
