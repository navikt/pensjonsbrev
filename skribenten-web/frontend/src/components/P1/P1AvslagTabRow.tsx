import { Radio, RadioGroup, Table, Textarea, TextField } from "@navikt/ds-react";
import { memo } from "react";
import type { Control, UseFormRegister } from "react-hook-form";
import { Controller, useFormState } from "react-hook-form";

import { SOFT_HYPHEN } from "~/Brevredigering/LetterEditor/model/utils";
import type { LandOption, P1RedigerbarForm } from "~/types/p1FormTypes";
import { P1CountryField } from "./P1CountryField";
import { ManagedDatePicker } from "./P1ManagedDatePicker";
import { AVSLAGSBEGRUNNELSE_OPTIONS, PENSJONSTYPE_OPTIONS } from "./p1Constants";

interface P1AvslagTabRowProps {
  index: number;
  landListe: LandOption[];
  control: Control<P1RedigerbarForm>;
  register: UseFormRegister<P1RedigerbarForm>;
}

export const P1AvslagTabRow = memo(({ index, landListe, control, register }: P1AvslagTabRowProps) => {
  // Subscribe only to this row's errors - prevents re-renders from other rows
  const fieldName = `avslaattePensjoner.${index}` as const;
  const { errors } = useFormState({
    control,
    name: [fieldName],
  });

  const rowErrors = errors.avslaattePensjoner?.[index];
  const institusjonErrors = rowErrors?.institusjon;

  const hasInstitusjonError = !!(
    institusjonErrors?.land ||
    institusjonErrors?.institusjonsnavn ||
    institusjonErrors?.pin ||
    institusjonErrors?.saksnummer ||
    institusjonErrors?.datoForVedtak
  );

  return (
    <Table.Row>
      {/* 4.1 Institusjon + PIN/saksnr + datoForVedtak */}
      <Table.DataCell className={hasInstitusjonError ? "p1-cell-error" : ""}>
        <P1CountryField
          control={control}
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
          name={`avslaattePensjoner.${index}.institusjon.datoForVedtak` as const}
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
