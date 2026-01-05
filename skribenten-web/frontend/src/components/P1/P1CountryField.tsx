import { UNSAFE_Combobox } from "@navikt/ds-react";
import type { Control, FieldPath } from "react-hook-form";
import { Controller } from "react-hook-form";

import { ZERO_WIDTH_SPACE } from "~/Brevredigering/LetterEditor/model/utils";
import type { P1RedigerbarForm } from "~/types/p1FormTypes";

interface P1CountryFieldProps {
  control: Control<P1RedigerbarForm>;
  name: FieldPath<P1RedigerbarForm>;
  index: number;
  landListe: Array<{ kode: string; navn: string }>;
  error?: string;
}

export const P1CountryField = ({ control, name, index, landListe, error }: P1CountryFieldProps) => {
  return (
    <Controller
      control={control}
      name={name}
      render={({ field }) => {
        const options = landListe
          .toSorted((a, b) => (a.navn > b.navn ? 1 : -1))
          .map((land) => ({ label: land.navn, value: land.kode }));
        options.unshift({ label: ZERO_WIDTH_SPACE, value: "" });
        return (
          <UNSAFE_Combobox
            css={{ marginBottom: "var(--ax-space-8)" }}
            data-cy={`land-combobox-${index}`}
            defaultValue={field.value ? landListe.find((option) => option.kode === field.value)?.navn : undefined}
            error={error}
            label="Land"
            onToggleSelected={(option) => field.onChange(option)}
            options={options}
            shouldAutocomplete
            size="small"
          />
        );
      }}
    />
  );
};
