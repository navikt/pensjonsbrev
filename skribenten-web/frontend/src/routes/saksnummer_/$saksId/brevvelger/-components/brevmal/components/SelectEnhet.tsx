import { Select } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { Controller, useFormContext } from "react-hook-form";

import { getEnheter } from "~/api/skribenten-api-endpoints";

function SelectEnhet() {
  const navigate = useNavigate();
  const { control } = useFormContext();
  const enheterQuery = useQuery(getEnheter);
  const options = enheterQuery.data ?? [];

  return (
    <Controller
      control={control}
      name="enhetsId"
      render={({ field, fieldState }) => {
        const handleChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
          field.onChange(e);
          navigate({
            to: ".",
            search: (old) => ({
              ...old,
              enhetsId: e.target.value,
            }),
            replace: true,
          });
        };

        return (
          <Select
            data-cy="avsenderenhet-select"
            error={fieldState.error?.message}
            label="Avsenderenhet"
            onChange={handleChange}
            size="small"
            value={field.value || ""}
          >
            <option value="">Velg enhet</option>
            {options.map((option) => (
              <option key={option.id} value={option.id}>
                {option.navn}
              </option>
            ))}
          </Select>
        );
      }}
    />
  );
}

export default SelectEnhet;
