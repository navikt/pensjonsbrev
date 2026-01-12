import { Table, Textarea, TextField } from "@navikt/ds-react";
import { RadioGroup } from "@navikt/ds-react";
import { Radio } from "@navikt/ds-react";
import React, { memo } from "react";
import type { Control, FieldErrors, UseFormRegister } from "react-hook-form";
import { Controller } from "react-hook-form";

import { SOFT_HYPHEN } from "~/Brevredigering/LetterEditor/model/utils";
import type { LandOption, P1InnvilgetPensjonForm, P1RedigerbarForm } from "~/types/p1FormTypes";

import { GRUNNLAG_INNVILGET_OPTIONS, PENSJONSTYPE_OPTIONS, REDUKSJONSGRUNNLAG_OPTIONS } from "./p1Constants";
import { P1CountryField } from "./P1CountryField";
import { ManagedDatePicker } from "./P1ManagedDatePicker";

interface P1InnvilgetTabRowProps {
  index: number;
  landListe: LandOption[];
  control: Control<P1RedigerbarForm>;
  register: UseFormRegister<P1RedigerbarForm>;
  error?: FieldErrors<P1InnvilgetPensjonForm>;
}

export const P1InnvilgetTabRow = memo(({ index, landListe, control, register, error }: P1InnvilgetTabRowProps) => {
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
      {/* 3.1 Institusjon */}
      <Table.DataCell className={hasInstitusjonError ? "p1-cell-error" : ""}>
        <P1CountryField
          control={control}
          error={institusjonErrors?.land?.message}
          index={index}
          landListe={landListe}
          name={`innvilgedePensjoner.${index}.institusjon.land` as const}
        />
        <TextField
          error={institusjonErrors?.institusjonsnavn?.message}
          label="Institusjon"
          size="small"
          {...register(`innvilgedePensjoner.${index}.institusjon.institusjonsnavn` as const)}
          css={{ marginBottom: "var(--ax-space-8)" }}
        />
        <TextField
          error={institusjonErrors?.pin?.message}
          label="PIN"
          size="small"
          {...register(`innvilgedePensjoner.${index}.institusjon.pin` as const)}
          css={{ marginBottom: "var(--ax-space-8)" }}
        />
        <TextField
          error={institusjonErrors?.saksnummer?.message}
          label={`Saks${SOFT_HYPHEN}nummer`}
          size="small"
          {...register(`innvilgedePensjoner.${index}.institusjon.saksnummer` as const)}
          css={{ marginBottom: "var(--ax-space-8)" }}
        />
        <Controller
          control={control}
          name={`innvilgedePensjoner.${index}.institusjon.vedtaksdato` as const}
          render={({ field: dateField, fieldState }) => (
            <ManagedDatePicker dateField={dateField} fieldState={fieldState} label="Vedtaksdato" />
          )}
        />
      </Table.DataCell>

      {/* 3.2 Pensjonstype */}
      <Table.DataCell className={rowErrors?.pensjonstype ? "p1-cell-error" : ""}>
        <Controller
          control={control}
          name={`innvilgedePensjoner.${index}.pensjonstype` as const}
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

      {/* 3.3 Dato første utbetaling */}
      <Table.DataCell className={rowErrors?.datoFoersteUtbetaling ? "p1-cell-error" : ""}>
        <Controller
          control={control}
          name={`innvilgedePensjoner.${index}.datoFoersteUtbetaling` as const}
          render={({ field: dateField, fieldState }) => (
            <ManagedDatePicker dateField={dateField} fieldState={fieldState} label="Dato" />
          )}
        />
      </Table.DataCell>

      {/* 3.4 Bruttobeløp */}
      <Table.DataCell className={`cell-seamless ${rowErrors?.utbetalt ? "p1-cell-error" : ""}`}>
        <Textarea
          className="p1-seamless-textarea"
          error={rowErrors?.utbetalt?.message}
          hideLabel
          label={`Brutto${SOFT_HYPHEN}beløp og hyppighet`}
          size="small"
          {...register(`innvilgedePensjoner.${index}.utbetalt` as const)}
        />
      </Table.DataCell>

      {/* 3.5 Grunnlag innvilget */}
      <Table.DataCell className={rowErrors?.grunnlagInnvilget ? "p1-cell-error" : ""}>
        <Controller
          control={control}
          name={`innvilgedePensjoner.${index}.grunnlagInnvilget` as const}
          render={({ field: radioField, fieldState }) => (
            <RadioGroup
              error={fieldState.error?.message}
              legend={`Beregnings${SOFT_HYPHEN}grunnlag`}
              onChange={(val) => {
                radioField.onChange(val === "IKKE_RELEVANT" ? null : val || null);
              }}
              size="small"
              value={radioField.value ?? "IKKE_RELEVANT"}
            >
              {GRUNNLAG_INNVILGET_OPTIONS.map((option) => (
                <Radio key={option.value} value={option.value}>
                  {option.label}
                </Radio>
              ))}
            </RadioGroup>
          )}
        />
      </Table.DataCell>

      {/* 3.6 Reduksjonsgrunnlag */}
      <Table.DataCell className={rowErrors?.reduksjonsgrunnlag ? "p1-cell-error" : ""}>
        <Controller
          control={control}
          name={`innvilgedePensjoner.${index}.reduksjonsgrunnlag` as const}
          render={({ field: radioField, fieldState }) => (
            <RadioGroup
              error={fieldState.error?.message}
              legend="Årsak til reduksjon"
              onChange={(val) => {
                radioField.onChange(val === "IKKE_REDUSERT" ? null : val || null);
              }}
              size="small"
              value={radioField.value ?? "IKKE_REDUSERT"}
            >
              {REDUKSJONSGRUNNLAG_OPTIONS.map((option) => (
                <Radio key={option.value} value={option.value}>
                  {option.label}
                </Radio>
              ))}
            </RadioGroup>
          )}
        />
      </Table.DataCell>

      {/* 3.7 Vurderingsperiode */}
      <Table.DataCell className={`cell-seamless ${rowErrors?.vurderingsperiode ? "p1-cell-error" : ""}`}>
        <Textarea
          className="p1-seamless-textarea"
          error={rowErrors?.vurderingsperiode?.message}
          hideLabel
          label={`Vurderings${SOFT_HYPHEN}periode`}
          size="small"
          {...register(`innvilgedePensjoner.${index}.vurderingsperiode` as const)}
        />
      </Table.DataCell>

      {/* 3.8 Adresse ny vurdering */}
      <Table.DataCell className={`cell-seamless ${rowErrors?.adresseNyVurdering ? "p1-cell-error" : ""}`}>
        <Textarea
          className="p1-seamless-textarea"
          error={rowErrors?.adresseNyVurdering?.message}
          hideLabel
          label="Adresse ny vurdering"
          size="small"
          {...register(`innvilgedePensjoner.${index}.adresseNyVurdering` as const)}
        />
      </Table.DataCell>
    </Table.Row>
  );
});
