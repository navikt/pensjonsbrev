import { Select } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { Controller } from "react-hook-form";

import { getEnheter } from "~/api/skribenten-api-endpoints";

function SelectEnhet() {
  const navigate = useNavigate({ from: "/saksnummer/$saksId/brevvelger" });
  const enheterQuery = useQuery(getEnheter);
  const options = enheterQuery.data ?? [];

  return (
    <Controller
      name="enhetsId"
      render={({ field, fieldState }) => (
        <Select
          data-testid="avsenderenhet-select"
          error={fieldState.error?.message}
          label="Avsenderenhet"
          name={field.name}
          onBlur={field.onBlur}
          onChange={(event) => {
            field.onChange(event);

            navigate({
              search: (search) => ({
                ...search,
                enhetsId: event.target.value || undefined,
              }),
              replace: true,
            });
          }}
          ref={field.ref}
          size="small"
          value={field.value ?? ""}
        >
          <option value="">Velg enhet</option>
          {options.map((option) => (
            <option key={option.id} value={option.id}>
              {option.navn}
            </option>
          ))}
        </Select>
      )}
      rules={{ required: "Du må velge avsenderenhet" }}
    />
  );
}

export default SelectEnhet;
