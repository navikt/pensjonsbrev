import { Checkbox, DatePicker, TextField, useDatepicker } from "@navikt/ds-react";
import { useEffect } from "react";
import { Controller, useFormContext, useWatch } from "react-hook-form";

import { convertFieldToReadableLabel } from "~/Brevredigering/ModelEditor/components/utils";
import { FullWidthDatePickerWrapper } from "~/components/FullWidthDatePickerWrapper";
import type { SaksbehandlerValg } from "~/types/brev";
import type { TScalar } from "~/types/brevbakerTypes";
import { formatDateWithoutTimezone, parseDate } from "~/utils/dateUtils";

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
      return <ControlledDatePicker field={field} fieldType={fieldType} onSubmit={submitOnChange} />;
    }
  }
};

/**
 * Componenten har mulighet til å autolagre endringer i feltet etter en gitt timeout dersom onSubmit sendes med.
 * Ellers, kan den også brukes som et vanlig tekst felt.
 */
const AutoSavingTextField = (props: {
  field: string;
  fieldType: TScalar;
  type: "number" | "text";
  step?: number;
  timeoutTimer: number;
  onSubmit?: (valg: SaksbehandlerValg) => void;
}) => {
  const { register, getFieldState, watch, reset, formState } = useFormContext();

  const registerProperties = register(props.field, { required: props.fieldType.nullable ? false : "Må oppgis" });
  const fieldState = getFieldState(registerProperties.name, formState);
  const watchedValue = watch(registerProperties.name);

  /**
   * useEffekten er brukt kun i forbindelse med autolagring
   * Merk at noen felter er avhengig av at andre felter er fyllt ut, før vi prøver å gjøre et kall til backend
   */
  useEffect(() => {
    if (fieldState.isDirty && !!watchedValue && props.onSubmit) {
      const timeout = setTimeout(() => {
        props.onSubmit!({ ...watch(), [props.field]: watchedValue });
        /* 
            Vi burde kanskje bare gjøre en resett dersom onSubmiten gir en OK? 
            Det vil nok kreve en del koding for det
          */
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

/**
 * Componenten har mulighet til å autolagre endringer i feltet etter en gitt timeout dersom onSubmit sendes med.
 * Ellers, kan den også brukes som et vanlig tekst felt.
 */
const ControlledDatePicker = (props: {
  field: string;
  fieldType: TScalar;
  onSubmit?: (v: SaksbehandlerValg) => void;
}) => {
  const {
    control,
    getFieldState,
    watch,
    reset,
    formState: { defaultValues },
    register,
  } = useFormContext();

  register(props.field, { required: props.fieldType.nullable ? false : "Må oppgis" });
  const watchedValue = useWatch({ name: props.field, control: control });
  const fieldState = getFieldState(props.field);

  /**
   * useEffekten er brukt kun i forbindelse med autolagring
   * Merk at noen felter er avhengig av at andre felter er fyllt ut, før vi prøver å gjøre et kall til backend
   */
  useEffect(() => {
    if (fieldState.isDirty && !!watchedValue && props.onSubmit) {
      const timeout = setTimeout(() => {
        props.onSubmit!({ ...watch(), [props.field]: watchedValue });
        /* 
            Vi burde kanskje bare gjøre en resett dersom onSubmiten gir en OK? 
            Det vil nok kreve en del koding for det
          */
        reset({ ...watch(), [props.field]: watchedValue });
      }, 500);

      return () => clearTimeout(timeout);
    }
  }, [fieldState.isDirty, watchedValue, watch, props.onSubmit, props.field, reset]);

  return (
    <Controller
      control={control}
      name={props.field}
      render={({ field, fieldState }) => (
        <DatePickerEditor
          defaultValue={defaultValues?.[props.field]}
          error={fieldState.error?.message}
          label={convertFieldToReadableLabel(props.field)}
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
