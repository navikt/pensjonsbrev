import { UNSAFE_Combobox } from "@navikt/ds-react";
import { type FieldError } from "react-hook-form";

import { SAMHANDLER_ENUM_TO_TEXT } from "~/types/nameMappings";

export const SamhandlerTypeSelect = ({
  description,
  onChange,
  error,
  value,
}: {
  onChange: (s: string | undefined) => void;
  description?: string;
  error?: FieldError;
  value: string;
}) => {
  const options = Object.entries(SAMHANDLER_ENUM_TO_TEXT).map(([value, label]) => ({ label, value }));

  return (
    <UNSAFE_Combobox
      description={description}
      error={error?.message}
      label="Samhandlertype"
      onToggleSelected={(option) => onChange(option)}
      options={options}
      selectedOptions={options.filter((option) => option.value === value) ?? undefined}
      shouldAutocomplete
      size="small"
    />
  );
};
