import { Table, Textarea, TextField } from "@navikt/ds-react";
import { RadioGroup } from "@navikt/ds-react";
import { Radio } from "@navikt/ds-react";
import React, { memo } from "react";
import type { Control, FieldErrors, UseFormRegister } from "react-hook-form";
import { Controller } from "react-hook-form";

import { SOFT_HYPHEN } from "~/Brevredigering/LetterEditor/model/utils";
import type { LandOption, P1AvslaattPensjonForm, P1RedigerbarForm } from "~/types/p1FormTypes";

import { AVSLAGSBEGRUNNELSE_OPTIONS, PENSJONSTYPE_OPTIONS } from "./p1Constants";
import { P1CountryField } from "./P1CountryField";
import { ManagedDatePicker } from "./P1ManagedDatePicker";

interface P1AvslagTabRowProps {
  index: number;
  landListe: LandOption[];
  control: Control<P1RedigerbarForm>;
  register: UseFormRegister<P1RedigerbarForm>;
  error?: FieldErrors<P1AvslaattPensjonForm>;
}

export const P1AvslagTabRow = memo(({ index, landListe, control, register, error }: P1AvslagTabRowProps) => {
  const rowErrors = error;
  const institusjonErrors = rowErrors?.institusjon;

  const hasInstitusjonError = !!(
    institusjonErrors?.land ||
    institusjonErrors?.institusjonsnavn ||
    institusjonErrors?.pin ||
    institusjonErrors?.saksnummer ||
    institusjonErrors?.vedtaksdato
  );

  return (
    <Table.Row>
      {/* 4.1 Institusjon + PIN/saksnr + vedtaksdato */}
      <Table.DataCell className={hasInstitusjonError ? "p1-cell-error" : ""}>
        <P1CountryField
          control={control}
          error={institusjonErrors?.land?.message}
          index={index}
          landListe={landListe}
          name={`avslaattePensjoner.${index}.institusjon.land` as const}
        />
        <TextField
          error={institusjonErrors?.institusjonsnavn?.message}
          label="Institusjon"
          size="small"
          {...register(`avslaattePensjoner.${index}.institusjon.institusjonsnavn` as const)}
          css={{ marginBottom: "var(--ax-space-8)" }}
        />
        <TextField
          error={institusjonErrors?.pin?.message}
          label="PIN"
          size="small"
          {...register(`avslaattePensjoner.${index}.institusjon.pin` as const)}
          css={{ marginBottom: "var(--ax-space-8)" }}
        />
        <TextField
          error={institusjonErrors?.saksnummer?.message}
          label={`Saks${SOFT_HYPHEN}nummer`}
          size="small"
          {...register(`avslaattePensjoner.${index}.institusjon.saksnummer` as const)}
          css={{ marginBottom: "var(--ax-space-8)" }}
        />
        <Controller
          control={control}
          name={`avslaattePensjoner.${index}.institusjon.vedtaksdato` as const}
          render={({ field: dateField, fieldState }) => (
            <ManagedDatePicker dateField={dateField} fieldState={fieldState} label={`Vedtaks${SOFT_HYPHEN}dato`} />
          )}
        />
      </Table.DataCell>

      {/* 4.2 Pensjonstype */}
      <Table.DataCell className={rowErrors?.pensjonstype ? "p1-cell-error" : ""}>
        <Controller
          control={control}
          name={`avslaattePensjoner.${index}.pensjonstype` as const}
          render={({ field: radioField, fieldState }) => (
            <RadioGroup
              error={fieldState.error?.message}
              legend="Pensjonstype"
              onChange={(val) => radioField.onChange(val || null)}
              size="small"
              value={radioField.value ?? ""}
            >
              {PENSJONSTYPE_OPTIONS.map((option) => (
                <Radio key={option.value} value={option.value}>
                  {option.label}
                </Radio>
              ))}
            </RadioGroup>
          )}
        />
      </Table.DataCell>

      {/* 4.3 Avslagsbegrunnelse */}
      <Table.DataCell className={rowErrors?.avslagsbegrunnelse ? "p1-cell-error" : ""}>
        <Controller
          control={control}
          name={`avslaattePensjoner.${index}.avslagsbegrunnelse` as const}
          render={({ field: radioField, fieldState }) => (
            <RadioGroup
              error={fieldState.error?.message}
              legend={`Avslags${SOFT_HYPHEN}begrunnelse`}
              onChange={(val) => radioField.onChange(val || null)}
              size="small"
              value={radioField.value ?? ""}
            >
              {AVSLAGSBEGRUNNELSE_OPTIONS.map((option) => (
                <Radio key={option.value} value={option.value}>
                  {option.label}
                </Radio>
              ))}
            </RadioGroup>
          )}
        />
      </Table.DataCell>

      {/* 4.4 Vurderingsperiode */}
      <Table.DataCell className={`cell-seamless ${rowErrors?.vurderingsperiode ? "p1-cell-error" : ""}`}>
        <Textarea
          className="p1-seamless-textarea"
          error={rowErrors?.vurderingsperiode?.message}
          hideLabel
          label={`Vurderings${SOFT_HYPHEN}periode`}
          size="small"
          {...register(`avslaattePensjoner.${index}.vurderingsperiode` as const)}
        />
      </Table.DataCell>

      {/* 4.5 Adresse ny vurdering */}
      <Table.DataCell className={`cell-seamless ${rowErrors?.adresseNyVurdering ? "p1-cell-error" : ""}`}>
        <Textarea
          className="p1-seamless-textarea"
          error={rowErrors?.adresseNyVurdering?.message}
          hideLabel
          label="Adresse for ny vurdering"
          size="small"
          {...register(`avslaattePensjoner.${index}.adresseNyVurdering` as const)}
        />
      </Table.DataCell>
    </Table.Row>
  );
});
