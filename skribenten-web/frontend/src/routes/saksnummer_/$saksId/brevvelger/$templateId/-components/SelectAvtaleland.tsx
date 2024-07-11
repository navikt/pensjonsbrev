import { Select } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { useFormContext } from "react-hook-form";

import { getAvtaleLand } from "~/api/skribenten-api-endpoints";

function SelectAvtalelandComponent() {
  const avtalelandQuery = useQuery(getAvtaleLand);
  const { register, formState } = useFormContext();
  const options = avtalelandQuery.data ?? [];

  return (
    <Select {...register("landkode")} error={formState.errors.landkode?.message?.toString()} label="Land" size="small">
      <option value={""}>Velg land</option>
      {options.map((option) => (
        <option key={option.kode} value={option.kode}>
          {option.navn}
        </option>
      ))}
    </Select>
  );
}

export default SelectAvtalelandComponent;
