import { Select } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { useFormContext } from "react-hook-form";

import { getEnheter } from "~/api/skribenten-api-endpoints";

function SelectEnhet() {
  const enheterQuery = useQuery(getEnheter);
  const { register, formState } = useFormContext();
  const options = enheterQuery.data ?? [];

  return (
    <Select
      {...register("enhetsId")}
      data-cy="avsenderenhet-select"
      error={formState.errors.enhetsId?.message?.toString()}
      label="Avsenderenhet"
      size="small"
    >
      <option value={""}>Velg enhet</option>
      {options.map((option) => (
        <option key={option.id} value={option.id}>
          {option.navn}
        </option>
      ))}
    </Select>
  );
}

export default SelectEnhet;
