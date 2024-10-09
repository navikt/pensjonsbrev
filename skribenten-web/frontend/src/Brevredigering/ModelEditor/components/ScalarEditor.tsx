import { DatePicker, Switch, TextField, useDatepicker } from "@navikt/ds-react";
import { useEffect } from "react";
import { Controller, useFormContext, useWatch } from "react-hook-form";

import { convertFieldToReadableLabel, getFieldDefaultValue } from "~/Brevredigering/ModelEditor/components/utils";
import { FullWidthDatePickerWrapper } from "~/components/FullWidthDatePickerWrapper";
import type { TScalar } from "~/types/brevbakerTypes";
import { formatDateWithoutTimezone, parseDate } from "~/utils/dateUtils";

export const ScalarEditor = ({
  prependName,
  fieldType,
  field,
  submitOnChange,
}: {
  prependName?: string;
  field: string;
  fieldType: TScalar;
  submitOnChange?: () => void;
}) => {
  switch (fieldType.kind) {
    case "NUMBER": {
      return (
        <AutoSavingTextField
          field={field}
          fieldType={fieldType}
          onSubmit={submitOnChange}
          prependName={prependName}
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
          prependName={prependName}
          step={0.1}
          timeoutTimer={2500}
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
          prependName={prependName}
          timeoutTimer={2500}
          type="text"
        />
      );
    }
    case "BOOLEAN": {
      return <SwitchField field={field} fieldType={fieldType} onSubmit={submitOnChange} prependName={prependName} />;
    }
    case "DATE": {
      return (
        <ControlledDatePicker field={field} fieldType={fieldType} onSubmit={submitOnChange} prependName={prependName} />
      );
    }
  }
};

const SwitchField = (props: { prependName?: string; field: string; fieldType: TScalar; onSubmit?: () => void }) => {
  const fieldName = props.prependName ? `${props.prependName}.${props.field}` : props.field;
  return (
    <div>
      <Controller
        defaultValue={false}
        name={fieldName}
        render={({ field }) => (
          <Switch
            {...field}
            checked={field.value}
            onChange={(v) => {
              field.onChange(v.target.checked);
              props.onSubmit?.();
            }}
          >
            {convertFieldToReadableLabel(props.field)}
          </Switch>
        )}
      />
    </div>
  );
};

/**
 * Componenten har mulighet til å autolagre endringer i feltet etter en gitt timeout dersom onSubmit sendes med.
 * Ellers, kan den også brukes som et vanlig tekst felt.
 */
export const AutoSavingTextField = (props: {
  prependName?: string;
  field: string;
  fieldType: TScalar;
  type: "number" | "text";
  step?: number;
  timeoutTimer: number;
  onSubmit?: () => void;
}) => {
  const { register, getFieldState, watch, formState } = useFormContext();

  const fieldName = props.prependName ? `${props.prependName}.${props.field}` : props.field;
  const registerProperties = register(fieldName, {
    required: props.fieldType.nullable ? false : "Må oppgis",
    pattern:
      props.fieldType.kind === "NUMBER"
        ? {
            value: /^\d+$/,
            message: "Må være et tall",
          }
        : undefined,
  });
  const fieldState = getFieldState(registerProperties.name, formState);
  const watchedValue = watch(registerProperties.name);

  /**
   * useEffekten er brukt kun i forbindelse med autolagring
   */
  useEffect(() => {
    if (fieldState.isDirty && (props.fieldType.nullable ? true : !!watchedValue) && props.onSubmit) {
      const timeout = setTimeout(() => {
        props.onSubmit!();
      }, props.timeoutTimer);

      return () => clearTimeout(timeout);
    }
  }, [
    fieldState.isDirty,
    watchedValue,
    watch,
    props.timeoutTimer,
    props.onSubmit,
    props.field,
    props.fieldType.nullable,
  ]);

  const commonTextFieldProperties = {
    ...registerProperties,
    autoComplete: "off",
    error: fieldState.error?.message,
    label: convertFieldToReadableLabel(props.field),
    size: "small" as const,
  };

  return (
    <TextField
      {...commonTextFieldProperties}
      inputMode={props.type === "number" ? "numeric" : undefined}
      step={props.step}
    />
  );
};

/**
 * Componenten har mulighet til å autolagre endringer i feltet etter en gitt timeout dersom onSubmit sendes med.
 * Ellers, kan den også brukes som et vanlig tekst felt.
 */
const ControlledDatePicker = (props: {
  prependName?: string;
  field: string;
  fieldType: TScalar;
  onSubmit?: () => void;
}) => {
  const {
    control,
    getFieldState,
    watch,
    formState: { defaultValues },
    register,
  } = useFormContext();

  const fieldName = props.prependName ? `${props.prependName}.${props.field}` : props.field;
  register(fieldName, { required: props.fieldType.nullable ? false : "Må oppgis" });
  const watchedValue = useWatch({ name: fieldName, control: control });
  const fieldState = getFieldState(fieldName);

  /**
   * useEffekten er brukt kun i forbindelse med autolagring
   */
  useEffect(() => {
    if (fieldState.isDirty && !!watchedValue && props.onSubmit) {
      const timeout = setTimeout(() => {
        props.onSubmit!();
      }, 500);

      return () => clearTimeout(timeout);
    }
  }, [fieldState.isDirty, watchedValue, watch, props.onSubmit, fieldName]);

  const defaultValue = getFieldDefaultValue(defaultValues, fieldName);

  return (
    <Controller
      control={control}
      name={fieldName}
      render={({ field, fieldState }) => (
        <DatePickerEditor
          defaultValue={defaultValue}
          error={fieldState.error?.message}
          label={convertFieldToReadableLabel(fieldName)}
          onChange={field.onChange}
        />
      )}
    />
  );
};

/**
 * en basic datepicker som tar hånd om setup.
 */
function DatePickerEditor({
  error,
  defaultValue,
  onChange,
  label,
}: {
  error?: string;
  label: string;
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
          label={label}
          size="small"
        />
      </DatePicker>
    </FullWidthDatePickerWrapper>
  );
}
