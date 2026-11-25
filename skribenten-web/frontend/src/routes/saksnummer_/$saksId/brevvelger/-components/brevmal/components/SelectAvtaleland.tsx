import { Select } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { Controller } from "react-hook-form";

import { getAvtaleLand } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";

const prioriterteLand = ["NOR", "FRO", "GRL", "GBR"];

function SelectAvtalelandComponent({ onLandChange }: { onLandChange?: (landkode: string) => void }) {
  const avtalelandQuery = useQuery(getAvtaleLand);
  const options = (avtalelandQuery.data ?? []).toSorted((a, b) => {
    const prioritetA = prioriterteLand.indexOf(a.kode);
    const prioritetB = prioriterteLand.indexOf(b.kode);
    return (
      (prioritetA === -1 ? prioriterteLand.length : prioritetA) -
        (prioritetB === -1 ? prioriterteLand.length : prioritetB) || a.navn.localeCompare(b.navn, "nb")
    );
  });

  return (
    <>
      <Controller
        name="landkode"
        render={({ field, fieldState }) => (
          <Select
            {...field}
            error={fieldState.error?.message}
            label="Land"
            onChange={(event) => {
              field.onChange(event);
              onLandChange?.(event.target.value);
            }}
            size="small"
          >
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
