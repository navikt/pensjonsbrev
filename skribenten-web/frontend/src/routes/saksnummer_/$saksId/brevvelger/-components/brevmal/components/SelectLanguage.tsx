import { Select } from "@navikt/ds-react";
import { useFormContext } from "react-hook-form";

import type { SpraakKode } from "~/types/apiTypes";
import { SPRAAK_ENUM_TO_TEXT } from "~/types/nameMappings";

function SelectLanguage({
  sorterteSpråk,
  preferredLanguage,
}: {
  sorterteSpråk: SpraakKode[];
  preferredLanguage: SpraakKode | null;
}) {
  const { register, getFieldState } = useFormContext();
  const fieldName = "spraak";
  const fieldState = getFieldState(fieldName);

  return (
    <Select
      {...register(fieldName)}
      data-cy="språk-velger-select"
      error={fieldState.error?.message}
      label="Språk"
      size="small"
    >
      {sorterteSpråk.map((spraak) => (
        <option key={spraak} value={spraak}>
          {SPRAAK_ENUM_TO_TEXT[spraak]} {preferredLanguage === spraak ? "(foretrukket språk)" : ""}
        </option>
      ))}
    </Select>
  );
}

export default SelectLanguage;
