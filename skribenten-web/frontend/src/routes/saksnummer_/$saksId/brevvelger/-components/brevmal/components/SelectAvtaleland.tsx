import { Select } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { Controller } from "react-hook-form";

import { getAvtaleLand } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";

function SelectAvtalelandComponent() {
  const avtalelandQuery = useQuery(getAvtaleLand);
  const options = (avtalelandQuery.data ?? []).toSorted((a, b) => Number(b.kode === "NOR") - Number(a.kode === "NOR"));

  return (
    <>
      <Controller
        name="landkode"
        render={({ field, fieldState }) => (
          <Select {...field} error={fieldState.error?.message} label="Land" size="small">
            <option value="">{avtalelandQuery.isPending ? "Laster land..." : "Velg land"}</option>
            {options.map((option) => (
              <option key={option.kode} value={option.kode}>
                {option.navn}
              </option>
            ))}
          </Select>
        )}
      />
      {avtalelandQuery.isError && <ApiError error={avtalelandQuery.error} title="Klarte ikke å hente land" />}
    </>
  );
}

export default SelectAvtalelandComponent;
