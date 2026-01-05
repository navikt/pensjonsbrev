import { css } from "@emotion/react";
import { PlusIcon } from "@navikt/aksel-icons";
import { BoxNew, Button, Heading, Radio, RadioGroup, Table, Textarea, TextField } from "@navikt/ds-react";
import { Controller, useFieldArray, useFormContext } from "react-hook-form";

import { SOFT_HYPHEN } from "~/Brevredigering/LetterEditor/model/utils";
import type { P1RedigerbarForm } from "~/types/p1FormTypes";

import { emptyAvslaattRow } from "./emptyP1";
import { AVSLAGSBEGRUNNELSE_OPTIONS, PENSJONSTYPE_OPTIONS } from "./p1Constants";
import { P1CountryField } from "./P1CountryField";
import { ManagedDatePicker } from "./P1ManagedDatePicker";

export const P1AvslagTab = ({ landListe }: { landListe: Array<{ kode: string; navn: string }> }) => {
  const {
    control,
    register,
    formState: { errors },
  } = useFormContext<P1RedigerbarForm>();

  const { fields, append } = useFieldArray<P1RedigerbarForm>({
    control,
    name: "avslaattePensjoner",
  });

  const addRow = () => append(emptyAvslaattRow());

  const hasInstitusjonError = (index: number) =>
    !!(
      errors.avslaattePensjoner?.[index]?.institusjon?.land ||
      errors.avslaattePensjoner?.[index]?.institusjon?.institusjonsnavn ||
      errors.avslaattePensjoner?.[index]?.institusjon?.pin ||
      errors.avslaattePensjoner?.[index]?.institusjon?.saksnummer ||
      errors.avslaattePensjoner?.[index]?.institusjon?.vedtaksdato
    );

  const hasPensjonstypeError = (index: number) => !!errors.avslaattePensjoner?.[index]?.pensjonstype;

  const hasAvslagsbegrunnelseError = (index: number) => !!errors.avslaattePensjoner?.[index]?.avslagsbegrunnelse;

  const hasVurderingsperiodeError = (index: number) => !!errors.avslaattePensjoner?.[index]?.vurderingsperiode;

  const hasAdresseError = (index: number) => !!errors.avslaattePensjoner?.[index]?.adresseNyVurdering;

  return (
    <>
      <Heading size="small" spacing>
        4. Avslått pensjon
      </Heading>

      <Table className="p1-table p1-table--zebra-stripes" css={{ minWidth: "988px" }} size="small">
        <BoxNew asChild background="accent-soft">
          <Table.Header>
            <Table.Row>
              <Table.HeaderCell>
                <span>4.1</span>
                <span>Institusjon som har avslått søknaden - med PIN/saksnummer og dato for vedtaket</span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>4.2</span>
                <span>Pensjonstype (1), (2), (3)</span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>4.3</span>
                <span>Begrunnelse for avslag (4), (5), (6), (7), (8), (9), (10)</span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>4.4</span>
                <span>Vurderings{SOFT_HYPHEN}periode (starter den datoen samlet melding ble mottatt)</span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>4.5</span>
                <span>Hvor kravet om ny vurdering skal sendes</span>
              </Table.HeaderCell>
            </Table.Row>
          </Table.Header>
        </BoxNew>

        <Table.Body>
          {fields.map((field, index) => (
            <Table.Row key={field.id}>
              {/* 4.1 Institusjon + PIN/saksnr + vedtaksdato */}
              <Table.DataCell className={hasInstitusjonError(index) ? "p1-cell-error" : ""}>
                <P1CountryField
                  control={control}
                  error={errors.avslaattePensjoner?.[index]?.institusjon?.land?.message}
                  index={index}
                  landListe={landListe}
                  name={`avslaattePensjoner.${index}.institusjon.land` as const}
                />
                <TextField
                  error={errors.avslaattePensjoner?.[index]?.institusjon?.institusjonsnavn?.message}
                  label="Institusjon"
                  size="small"
                  {...register(`avslaattePensjoner.${index}.institusjon.institusjonsnavn` as const)}
                  css={css`
                    margin-bottom: 0.5rem;
                  `}
                />
                <TextField
                  error={errors.avslaattePensjoner?.[index]?.institusjon?.pin?.message}
                  label="PIN"
                  size="small"
                  {...register(`avslaattePensjoner.${index}.institusjon.pin` as const)}
                  css={css`
                    margin-bottom: 0.5rem;
                  `}
                />
                <TextField
                  error={errors.avslaattePensjoner?.[index]?.institusjon?.saksnummer?.message}
                  label={`Saks${SOFT_HYPHEN}nummer`}
                  size="small"
                  {...register(`avslaattePensjoner.${index}.institusjon.saksnummer` as const)}
                  css={css`
                    margin-bottom: 0.5rem;
                  `}
                />
                <Controller
                  control={control}
                  name={`avslaattePensjoner.${index}.institusjon.vedtaksdato` as const}
                  render={({ field: dateField, fieldState }) => (
                    <ManagedDatePicker
                      dateField={dateField}
                      fieldState={fieldState}
                      label={`Vedtaks${SOFT_HYPHEN}dato`}
                    />
                  )}
                />
              </Table.DataCell>

              {/* 4.2 Pensjonstype */}
              <Table.DataCell className={hasPensjonstypeError(index) ? "p1-cell-error" : ""}>
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
              <Table.DataCell className={hasAvslagsbegrunnelseError(index) ? "p1-cell-error" : ""}>
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
              <Table.DataCell className={`cell-seamless ${hasVurderingsperiodeError(index) ? "p1-cell-error" : ""}`}>
                <Textarea
                  className="p1-seamless-textarea"
                  error={errors.avslaattePensjoner?.[index]?.vurderingsperiode?.message}
                  // hideLabel
                  label={`Vurderings${SOFT_HYPHEN}periode`}
                  size="small"
                  {...register(`avslaattePensjoner.${index}.vurderingsperiode` as const)}
                />
              </Table.DataCell>

              {/* 4.5 Adresse ny vurdering */}
              <Table.DataCell className={`cell-seamless ${hasAdresseError(index) ? "p1-cell-error" : ""}`}>
                <Textarea
                  className="p1-seamless-textarea"
                  error={errors.avslaattePensjoner?.[index]?.adresseNyVurdering?.message}
                  // hideLabel
                  label="Adresse for ny vurdering"
                  size="small"
                  {...register(`avslaattePensjoner.${index}.adresseNyVurdering` as const)}
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
