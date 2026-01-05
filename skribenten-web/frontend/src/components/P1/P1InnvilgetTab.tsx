import { PlusIcon } from "@navikt/aksel-icons";
import { BoxNew, Button, Heading, Radio, RadioGroup, Table, Textarea, TextField } from "@navikt/ds-react";
import { Controller, useFieldArray, useFormContext } from "react-hook-form";

import type { P1RedigerbarForm } from "~/types/p1FormTypes";

import { emptyInnvilgetRow } from "./emptyP1";
import {
  GRUNNLAG_INNVILGET_OPTIONS,
  PENSJONSTYPE_OPTIONS,
  REDUKSJONSGRUNNLAG_OPTIONS,
  SOFT_HYPHEN,
} from "./p1Constants";
import { P1CountryField } from "./P1CountryField";
import { ManagedDatePicker } from "./P1ManagedDatePicker";
export const P1InnvilgetTab = ({ landListe }: { landListe: Array<{ kode: string; navn: string }> }) => {
  const {
    control,
    register,
    formState: { errors },
  } = useFormContext<P1RedigerbarForm>();

  const { fields, append } = useFieldArray<P1RedigerbarForm>({
    control,
    name: "innvilgedePensjoner",
  });

  const addRow = () => append(emptyInnvilgetRow());

  const hasInstitusjonError = (index: number) =>
    !!(
      errors.innvilgedePensjoner?.[index]?.institusjon?.land ||
      errors.innvilgedePensjoner?.[index]?.institusjon?.institusjonsnavn ||
      errors.innvilgedePensjoner?.[index]?.institusjon?.pin ||
      errors.innvilgedePensjoner?.[index]?.institusjon?.saksnummer ||
      errors.innvilgedePensjoner?.[index]?.institusjon?.vedtaksdato
    );

  const hasPensjonstypeError = (index: number) => !!errors.innvilgedePensjoner?.[index]?.pensjonstype;

  const hasDatoError = (index: number) => !!errors.innvilgedePensjoner?.[index]?.datoFoersteUtbetaling;

  const hasUtbetaltError = (index: number) => !!errors.innvilgedePensjoner?.[index]?.utbetalt;

  const hasGrunnlagError = (index: number) => !!errors.innvilgedePensjoner?.[index]?.grunnlagInnvilget;

  const hasReduksjonsgrunnlagError = (index: number) => !!errors.innvilgedePensjoner?.[index]?.reduksjonsgrunnlag;

  const hasVurderingsperiodeError = (index: number) => !!errors.innvilgedePensjoner?.[index]?.vurderingsperiode;

  const hasAdresseError = (index: number) => !!errors.innvilgedePensjoner?.[index]?.adresseNyVurdering;

  return (
    <>
      <Heading size="small" spacing>
        3. Innvilget pensjon
      </Heading>

      <Table
        border={2}
        className="p1-table p1-table--zebra-stripes"
        css={{ minWidth: "1200px", tableLayout: "fixed" }}
        size="small"
      >
        <BoxNew asChild background="accent-soft">
          <Table.Header>
            <Table.Row>
              <Table.HeaderCell>
                <span>3.1</span>
                <span>Institusjon som gir pensjonen - med PIN/saks{SOFT_HYPHEN}nummer og dato for vedtaket</span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>3.2</span>
                <span>Pensjonstype (1), (2), (3)</span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>3.3</span>
                <span>Dato for første utbetaling</span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>3.4</span>
                <span>
                  Brutto{SOFT_HYPHEN}beløp med betalings{SOFT_HYPHEN}hyppighet og valuta
                </span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>3.5</span>
                <span>Pensjonen er innvilget: (4), (5), (6)</span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>3.6</span>
                <span>Pensjonen er redusert: (7), (8)</span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>3.7</span>
                <span>Vurderings{SOFT_HYPHEN}periode (starter den datoen samlet melding ble mottatt)</span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>3.8</span>
                <span>Hvor kravet om ny vurdering skal sendes</span>
              </Table.HeaderCell>
            </Table.Row>
          </Table.Header>
        </BoxNew>

        <Table.Body>
          {fields.map((field, index) => (
            <Table.Row key={field.id}>
              {/* 3.1 Institusjon */}
              <Table.DataCell className={hasInstitusjonError(index) ? "p1-cell-error" : ""}>
                <P1CountryField
                  control={control}
                  error={errors.innvilgedePensjoner?.[index]?.institusjon?.land?.message}
                  index={index}
                  landListe={landListe}
                  name={`innvilgedePensjoner.${index}.institusjon.land` as const}
                />
                <TextField
                  error={errors.innvilgedePensjoner?.[index]?.institusjon?.institusjonsnavn?.message}
                  label="Institusjon"
                  size="small"
                  {...register(`innvilgedePensjoner.${index}.institusjon.institusjonsnavn` as const)}
                  css={{ marginBottom: "var(--ax-space-8)" }}
                />
                <TextField
                  error={errors.innvilgedePensjoner?.[index]?.institusjon?.pin?.message}
                  label="PIN"
                  size="small"
                  {...register(`innvilgedePensjoner.${index}.institusjon.pin` as const)}
                  css={{ marginBottom: "var(--ax-space-8)" }}
                />
                <TextField
                  error={errors.innvilgedePensjoner?.[index]?.institusjon?.saksnummer?.message}
                  label={`Saks${SOFT_HYPHEN}nummer`}
                  size="small"
                  {...register(`innvilgedePensjoner.${index}.institusjon.saksnummer` as const)}
                  css={{ marginBottom: "var(--ax-space-8)" }}
                />
                <Controller
                  control={control}
                  name={`innvilgedePensjoner.${index}.institusjon.vedtaksdato` as const}
                  render={({ field: dateField, fieldState }) => (
                    <ManagedDatePicker
                      dateField={dateField}
                      fieldState={fieldState}
                      label={`Vedtaks${SOFT_HYPHEN}dato`}
                    />
                  )}
                />
              </Table.DataCell>

              {/* 3.2 Pensjonstype */}
              <Table.DataCell className={hasPensjonstypeError(index) ? "p1-cell-error" : ""}>
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
              <Table.DataCell className={hasDatoError(index) ? "p1-cell-error" : ""}>
                <Controller
                  control={control}
                  name={`innvilgedePensjoner.${index}.datoFoersteUtbetaling` as const}
                  render={({ field: dateField, fieldState }) => (
                    <ManagedDatePicker dateField={dateField} fieldState={fieldState} label="Første utbetaling" />
                  )}
                />
              </Table.DataCell>
              {/* 3.4 Bruttobeløp */}
              <Table.DataCell className={`cell-seamless ${hasUtbetaltError(index) ? "p1-cell-error" : ""}`}>
                <Textarea
                  className="p1-seamless-textarea"
                  error={errors.innvilgedePensjoner?.[index]?.utbetalt?.message}
                  // hideLabel
                  label={`Brutto${SOFT_HYPHEN}beløp og hyppighet`}
                  size="small"
                  {...register(`innvilgedePensjoner.${index}.utbetalt` as const)}
                />
              </Table.DataCell>

              {/* 3.5 Grunnlag innvilget */}
              <Table.DataCell className={hasGrunnlagError(index) ? "p1-cell-error" : ""}>
                <Controller
                  control={control}
                  name={`innvilgedePensjoner.${index}.grunnlagInnvilget` as const}
                  render={({ field: radioField, fieldState }) => (
                    <RadioGroup
                      error={fieldState.error?.message}
                      legend="Innvilget"
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
              <Table.DataCell className={hasReduksjonsgrunnlagError(index) ? "p1-cell-error" : ""}>
                <Controller
                  control={control}
                  name={`innvilgedePensjoner.${index}.reduksjonsgrunnlag` as const}
                  render={({ field: radioField, fieldState }) => (
                    <RadioGroup
                      error={fieldState.error?.message}
                      legend="Redusert"
                      onChange={(val) => {
                        radioField.onChange(val === "IKKE_RELEVANT" ? null : val || null);
                      }}
                      size="small"
                      value={radioField.value ?? "IKKE_RELEVANT"}
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
              <Table.DataCell className={`cell-seamless ${hasVurderingsperiodeError(index) ? "p1-cell-error" : ""}`}>
                <Textarea
                  className="p1-seamless-textarea"
                  error={errors.innvilgedePensjoner?.[index]?.vurderingsperiode?.message}
                  // hideLabel
                  label={`Vurderings${SOFT_HYPHEN}periode`}
                  size="small"
                  {...register(`innvilgedePensjoner.${index}.vurderingsperiode` as const)}
                />
              </Table.DataCell>

              {/* 3.8 Adresse ny vurdering */}
              <Table.DataCell className={`cell-seamless ${hasAdresseError(index) ? "p1-cell-error" : ""}`}>
                <Textarea
                  className="p1-seamless-textarea"
                  error={errors.innvilgedePensjoner?.[index]?.adresseNyVurdering?.message}
                  // hideLabel
                  label="Adresse ny vurdering"
                  size="small"
                  {...register(`innvilgedePensjoner.${index}.adresseNyVurdering` as const)}
                />
              </Table.DataCell>
            </Table.Row>
          ))}
        </Table.Body>
      </Table>

      <BoxNew asChild marginBlock="space-16 0" minWidth="fit-content" width="100%">
        <Button icon={<PlusIcon />} onClick={addRow} size="small" type="button" variant="secondary">
          Legg til ny rad
        </Button>
      </BoxNew>
    </>
  );
};
