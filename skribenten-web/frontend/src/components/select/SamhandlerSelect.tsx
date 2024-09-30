import { Controller, type FieldError, useFormContext } from "react-hook-form";

import { BasicSelect, SelectLayoutWrapper } from "~/components/select/CustomSelectComponents";
import { SAMHANDLER_ENUM_TO_TEXT } from "~/types/nameMappings";

export function SamhandlerTypeSelectFormPart({
  name = "samhandlerType",
  description,
}: {
  name?: string;
  description?: string;
}) {
  const { control } = useFormContext();

  return (
    <Controller
      control={control}
      name={name}
      render={({ field, fieldState }) => (
        <SamhandlerTypeSelect
          description={description}
          error={fieldState.error}
          onChange={field.onChange}
          value={field.value}
        />
      )}
    />
  );
}

export function SamhandlerTypeSelect({
  description,
  onChange,
  error,
  value,
}: {
  onChange: (s: string | undefined) => void;
  description?: string;
  error?: FieldError;
  value: string;
}) {
  const options = Object.entries(SAMHANDLER_ENUM_TO_TEXT).map(([value, label]) => ({ label, value }));

  const currentOption = options.find((option) => option.value === value);

  //TODO - bruk aksels combobox istedenfor med size small
  return (
    <SelectLayoutWrapper description={description} error={error} htmlFor="samhandlerType" label="Samhandlertype">
      <BasicSelect
        inputId="samhandlerType"
        onChange={(option) => onChange(option?.value)}
        options={options}
        value={currentOption ?? null}
      />
    </SelectLayoutWrapper>
  );
}
