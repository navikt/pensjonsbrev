import { UNSAFE_Combobox } from "@navikt/ds-react";
import type { Control, FieldPath } from "react-hook-form";
import { Controller } from "react-hook-form";

import type { LandOption, P1RedigerbarForm } from "~/types/p1FormTypes";

interface P1CountryFieldProps {
  control: Control<P1RedigerbarForm>;
  name: FieldPath<P1RedigerbarForm>;
  index: number;
  landListe: LandOption[];
  error?: string;
}

export const P1CountryField = ({ control, name, index, landListe, error }: P1CountryFieldProps) => {
  const sortedOptions = landListe.toSorted((a, b) => a.navn.localeCompare(b.navn, "no")).map((land) => land.navn);

  return (
    <Controller
      control={control}
      name={name}
      render={({ field }) => {
        const selectedCountryName = field.value ? landListe.find((l) => l.kode === field.value)?.navn : "";

        return (
          <UNSAFE_Combobox
            css={{ marginBottom: "var(--ax-space-8)" }}
            data-cy={`land-combobox-${index}`}
            error={error}
            label="Land"
            onToggleSelected={(option, isSelected) => {
              if (isSelected) {
                const selectedCountry = landListe.find((l) => l.navn === option);
                if (selectedCountry) {
                  field.onChange(selectedCountry.kode);
                }
              } else {
                field.onChange("");
              }
            }}
            options={sortedOptions}
            selectedOptions={selectedCountryName ? [selectedCountryName] : []}
            shouldAutocomplete
            size="small"
          />
        );
      }}
    />
  );
};
