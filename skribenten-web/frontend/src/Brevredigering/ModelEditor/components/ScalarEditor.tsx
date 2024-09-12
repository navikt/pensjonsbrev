import { Checkbox, DatePicker, TextField, useDatepicker } from "@navikt/ds-react";
import { useEffect } from "react";
import { Controller, useFormContext, useWatch } from "react-hook-form";

import { convertFieldToReadableLabel } from "~/Brevredigering/ModelEditor/components/utils";
import { FullWidthDatePickerWrapper } from "~/components/FullWidthDatePickerWrapper";
import type { SaksbehandlerValg } from "~/types/brev";
import type { TScalar } from "~/types/brevbakerTypes";
import { formatDateWithoutTimezone, parseDate } from "~/utils/dateUtils";

const AutoSavingTextField = (props: {
  field: string;
  fieldType: TScalar;
  type: "number" | "text";
  step?: number;
  timeoutTimer: number;
  onSubmit?: (valg: SaksbehandlerValg) => void;
}) => {
  const { register, getFieldState, watch, reset } = useFormContext();

  const registerProperties = register(props.field, { required: props.fieldType.nullable ? false : "Må oppgis" });
  const fieldState = getFieldState(registerProperties.name);
  const watchedValue = watch(registerProperties.name);

  useEffect(() => {
    if (fieldState.isDirty && !!watchedValue && props.onSubmit) {
      const timeout = setTimeout(() => {
        props.onSubmit!({ ...watch(), [props.field]: watchedValue });
        //dette burde vi egentlig bare gjøre ved en OK respons?
        reset({ ...watch(), [props.field]: watchedValue });
      }, props.timeoutTimer);

      return () => clearTimeout(timeout);
    }
  }, [fieldState.isDirty, watchedValue, watch, props.timeoutTimer, props.onSubmit, props.field, reset]);

  const commonTextFieldProperties = {
    ...registerProperties,
    autoComplete: "off",
    error: fieldState.error?.message,
    label: convertFieldToReadableLabel(props.field),
    size: "small" as const,
  };

  return <TextField {...commonTextFieldProperties} step={props.step} type={props.type} />;
};

export const ScalarEditor = ({
  fieldType,
  field,
  submitOnChange,
}: {
  field: string;
  fieldType: TScalar;
  submitOnChange?: (valg: SaksbehandlerValg) => void;
}) => {
  switch (fieldType.kind) {
    case "NUMBER": {
      return (
        <AutoSavingTextField
          field={field}
          fieldType={fieldType}
          onSubmit={submitOnChange}
          step={1}
          timeoutTimer={2000}
          type={"number"}
        />
      );
    }
    case "DOUBLE": {
      return (
        <AutoSavingTextField
          field={field}
          fieldType={fieldType}
          onSubmit={submitOnChange}
          step={0.1}
          timeoutTimer={3000}
          type="number"
        />
      );
    }
    case "STRING": {
      return (
        <AutoSavingTextField
          field={field}
          fieldType={fieldType}
          onSubmit={submitOnChange}
          timeoutTimer={3000}
          type="text"
        />
      );
    }
    case "BOOLEAN": {
      // TODO: reimplement when an example template exists
      return <Checkbox>{convertFieldToReadableLabel(field)}</Checkbox>;
    }
    case "DATE": {
      return <ControlledDatePicker field={field} onSubmit={submitOnChange} />;
    }
  }
};

const ControlledDatePicker = (props: { field: string; onSubmit?: (v: SaksbehandlerValg) => void }) => {
  const {
    control,
    getFieldState,
    getValues,
    reset,
    formState: { defaultValues },
  } = useFormContext();

  const watchedValue = useWatch({ name: props.field, control: control });
  const fieldState = getFieldState(props.field);

  useEffect(() => {
    if (fieldState.isDirty && !!watchedValue && props.onSubmit) {
      const timeout = setTimeout(() => {
        props.onSubmit!({ ...getValues(), [props.field]: watchedValue });
        //dette burde vi egentlig bare gjøre ved en OK respons?
        reset({ ...getValues(), [props.field]: watchedValue });
      }, 500);

      return () => clearTimeout(timeout);
    }
  }, [fieldState.isDirty, watchedValue, getValues, props.onSubmit, props.field, reset]);

  return (
    <Controller
      control={control}
      name={props.field}
      render={({ field, fieldState }) => (
        <DatePickerEditor
          defaultValue={defaultValues?.[props.field]}
          error={fieldState.error?.message}
          field={props.field}
          onChange={field.onChange}
        />
      )}
    />
  );
};

function DatePickerEditor({
  field,
  error,
  defaultValue,
  onChange,
}: {
  field: string;
  error?: string;
  defaultValue?: string;
  onChange: (newDate: string) => void;
}) {
  const datepicker = useDatepicker({
    defaultSelected: defaultValue ? parseDate(defaultValue) : undefined,
    onDateChange: (date) => {
      onChange(date ? formatDateWithoutTimezone(date) : "");
    },
  });

  return (
    <FullWidthDatePickerWrapper>
      <DatePicker {...datepicker.datepickerProps}>
        <DatePicker.Input
          data-cy="datepicker-editor"
          {...datepicker.inputProps}
          error={error}
          label={convertFieldToReadableLabel(field)}
          size="small"
        />
      </DatePicker>
    </FullWidthDatePickerWrapper>
  );
}
