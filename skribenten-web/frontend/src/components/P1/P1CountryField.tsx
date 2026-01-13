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
    () =>
      landListe
        .toSorted((aLand, bLand) => aLand.navn.localeCompare(bLand.navn, "no"))
        .map((land) => ({ label: land.navn, value: land.kode })),
    [landListe],
  );

  return (
    <Controller
      control={control}
      name={name}
      render={({ field }) => {
        const selectedCountry = sortedOptions.find((land) => land.value === field.value);

        return (
          <UNSAFE_Combobox
            css={{
              marginBottom: "var(--ax-space-8)",
            }}
            data-cy={`land-combobox-${index}`}
            error={error}
            label="Land"
            onToggleSelected={(landCode, isSelected) => (isSelected ? field.onChange(landCode) : field.onChange(""))}
            options={sortedOptions}
            selectedOptions={selectedCountry ? [selectedCountry] : undefined}
            shouldAutocomplete
            size="small"
          />
        );
      }}
    />
  );
};
