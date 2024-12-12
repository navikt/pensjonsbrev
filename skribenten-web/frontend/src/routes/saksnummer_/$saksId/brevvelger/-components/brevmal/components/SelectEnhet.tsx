import { Select } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { Controller } from "react-hook-form";

import { getEnheter } from "~/api/skribenten-api-endpoints";

function SelectEnhet() {
  const enheterQuery = useQuery(getEnheter);
  const options = enheterQuery.data ?? [];

  return (
    <Controller
      name={"enhetsId"}
      render={({ field, fieldState }) => (
        <Select
          data-cy="avsenderenhet-select"
          error={fieldState.error?.message}
          label="Avsenderenhet"
          size="small"
          {...field}
        >
          <option value={""}>Velg enhet</option>
          {options.map((option) => (
            <option key={option.id} value={option.id}>
              {option.navn}
            </option>
          ))}
        </Select>
      )}
    />
  );
}

export default SelectEnhet;
