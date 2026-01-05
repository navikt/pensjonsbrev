import { DatePicker, useDatepicker } from "@navikt/ds-react";
import { useRef } from "react";
import type { ControllerFieldState, FieldValues } from "react-hook-form";

export const ManagedDatePicker = ({
  dateField,
  fieldState,
  hideLabel = false,
  label,
}: {
  fieldState: ControllerFieldState;
  dateField: FieldValues;
  hideLabel?: boolean;
  label: string;
}) => {
  const currentYear = new Date().getFullYear();
  const inputRef = useRef<HTMLInputElement>(null);

  const { datepickerProps, inputProps } = useDatepicker({
    defaultSelected: dateField.value,
    fromDate: new Date(`${currentYear - 50}-01-01`),
    toDate: new Date(`${currentYear + 5}-12-31`),
    onDateChange: (date) => {
      if (date) {
        dateField.onChange(date);
      } else if (inputRef.current?.value) {
        dateField.onChange(new Date(inputRef.current.value));
      } else {
        dateField.onChange(undefined);
      }
    },
    inputFormat: "dd.MM.yyyy",
  });

  return (
    <DatePicker dropdownCaption mode="single" {...datepickerProps} wrapperClassName="p1-date-picker-full-width">
      <DatePicker.Input
        ref={inputRef}
        {...inputProps}
        error={fieldState.error?.message}
        hideLabel={hideLabel}
        label={label}
        placeholder="dd.mm.åååå"
        size="small"
      />
    </DatePicker>
  );
};
