import { DatePicker, useDatepicker } from "@navikt/ds-react";
import { useMemo } from "react";
import type { ControllerFieldState, ControllerRenderProps, FieldPath, FieldValues } from "react-hook-form";

import { formatDateWithoutTimezone, parseIsoDateToDate } from "~/utils/dateUtils";

/**
 *
 * This component handles dates as ISO strings (yyyy-MM-dd) in form state,
 * while displaying them in Norwegian format (dd.MM.yyyy) to the user.
 *
 * The Aksel DatePicker handles the display format internally, so we:
 * 1. Parse ISO string → Date for the datepicker's defaultSelected
 * 2. Format Date → ISO string when user selects a date
 */
interface ManagedDatePickerProps<
  TFieldValues extends FieldValues = FieldValues,
  TName extends FieldPath<TFieldValues> = FieldPath<TFieldValues>,
> {
  dateField: ControllerRenderProps<TFieldValues, TName>;
  fieldState: ControllerFieldState;
  hideLabel?: boolean;
  label: string;
  "data-cy"?: string;
}

// Constants moved outside component to prevent recreation on each render
const currentYear = new Date().getFullYear();
const FROM_DATE = new Date(currentYear - 50, 0, 1);
const TO_DATE = new Date(currentYear + 5, 11, 31);

export const ManagedDatePicker = <
  TFieldValues extends FieldValues = FieldValues,
  TName extends FieldPath<TFieldValues> = FieldPath<TFieldValues>,
>({
  dateField,
  fieldState,
  hideLabel = false,
  label,
  "data-cy": dataCy,
}: ManagedDatePickerProps<TFieldValues, TName>) => {
  const selectedDate = useMemo(() => parseIsoDateToDate(dateField.value as string), [dateField.value]);

  const { datepickerProps, inputProps } = useDatepicker({
    defaultSelected: selectedDate,
    fromDate: FROM_DATE,
    toDate: TO_DATE,
    onDateChange: (date) => {
      if (date) {
        dateField.onChange(formatDateWithoutTimezone(date));
      } else {
        dateField.onChange("");
      }
    },
    onValidate: (validation) => {
      if (validation.isInvalid) {
        dateField.onChange("invalid-date");
      }
    },
    inputFormat: "dd.MM.yyyy",
  });

  return (
    <DatePicker dropdownCaption {...datepickerProps} wrapperClassName="p1-date-picker-full-width">
      <DatePicker.Input
        {...inputProps}
        data-cy={dataCy}
        error={fieldState.error?.message}
        hideLabel={hideLabel}
        label={label}
        placeholder="dd.mm.åååå"
        size="small"
      />
    </DatePicker>
  );
};
