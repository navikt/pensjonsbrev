import { UNSAFE_Combobox } from "@navikt/ds-react";
import { useMemo } from "react";
import type { Control, FieldPath, FieldValues } from "react-hook-form";
import { Controller } from "react-hook-form";

import type { LandOption } from "~/types/p1FormTypes";

interface P1CountryFieldProps<
  TFieldValues extends FieldValues = FieldValues,
  TName extends FieldPath<TFieldValues> = FieldPath<TFieldValues>,
> {
  control: Control<TFieldValues>;
  name: TName;
  index: number;
  landListe: LandOption[];
  error?: string;
}

export const P1CountryField = <
  TFieldValues extends FieldValues = FieldValues,
  TName extends FieldPath<TFieldValues> = FieldPath<TFieldValues>,
>({
  control,
  name,
  index,
  landListe,
  error,
}: P1CountryFieldProps<TFieldValues, TName>) => {
  const sortedOptions = useMemo(
    () => landListe.toSorted((a, b) => a.navn.localeCompare(b.navn, "no")).map((land) => land.navn),
    [landListe],
  );

  return (
    <Controller
      control={control}
      name={name}
      render={({ field }) => {
        const selectedCountryName = field.value ? landListe.find((l) => l.kode === field.value)?.navn : "";

        return (
          <UNSAFE_Combobox
            css={{
              marginBottom: "var(--ax-space-8)",
              "& *": {
                fontSize: "var(--ax-font-size-medium)",
              },
            }}
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
