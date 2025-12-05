import { css } from "@emotion/react";
import { PlusIcon } from "@navikt/aksel-icons";
import { Button, DatePicker, Heading, Radio, RadioGroup, Textarea, TextField } from "@navikt/ds-react";
import { Controller, useFieldArray, useFormContext } from "react-hook-form";

import type { P1RedigerbarForm } from "~/types/p1FormTypes";

import { emptyAvslaattRow } from "./emptyP1";
import { AVSLAGSBEGRUNNELSE_OPTIONS, PENSJONSTYPE_OPTIONS } from "./p1Constants";

const currentYear = new Date().getFullYear();

export const P1AvslagTab = () => {
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
      errors.avslaattePensjoner?.[index]?.institusjon?.vedtaksdato
    );

  const hasPensjonstypeError = (index: number) => !!errors.avslaattePensjoner?.[index]?.pensjonstype;

  const hasAvslagsbegrunnelseError = (index: number) => !!errors.avslaattePensjoner?.[index]?.avslagsbegrunnelse;

  const hasVurderingsperiodeError = (index: number) => !!errors.avslaattePensjoner?.[index]?.vurderingsperiode;

  const hasAdresseError = (index: number) => !!errors.avslaattePensjoner?.[index]?.adresseNyVurdering;

  return (
    <div>
      <Heading size="small" spacing>
        4. Avslått pensjon
      </Heading>

      <table className="p1-table p1-table--avslag">
        <thead>
          <tr>
            <th>
              <span className="header-number">4.1</span>
              <span className="header-text">
                Institusjon som har avslått søknaden – med PIN/saksnummer og dato for vedtaket
              </span>
            </th>
            <th>
              <span className="header-number">4.2</span>
              <span className="header-text">Pensjonstype (1), (2), (3)</span>
            </th>
            <th>
              <span className="header-number">4.3</span>
              <span className="header-text">Begrunnelse for avslag (4), (5), (6), (7), (8), (9), (10)</span>
            </th>
            <th>
              <span className="header-number">4.4</span>
              <span className="header-text">Vurderingsperiode (starter den datoen samlet melding ble mottatt)</span>
            </th>
            <th>
              <span className="header-number">4.5</span>
              <span className="header-text">Hvor kravet om ny vurdering skal sendes</span>
            </th>
          </tr>
        </thead>

        <tbody>
          {fields.map((field, index) => (
            <tr key={field.id}>
              {/* 4.1 Institusjon + PIN/saksnr + vedtaksdato */}
              <td className={hasInstitusjonError(index) ? "p1-cell-error" : ""}>
                <TextField
                  error={errors.avslaattePensjoner?.[index]?.institusjon?.land?.message}
                  label="Land"
                  size="small"
                  {...register(`avslaattePensjoner.${index}.institusjon.land` as const)}
                  css={css`
                    margin-bottom: 0.5rem;
                  `}
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
                  label="PIN/saksnummer"
                  size="small"
                  {...register(`avslaattePensjoner.${index}.institusjon.pin` as const)}
                  css={css`
                    margin-bottom: 0.5rem;
                  `}
                />
                <Controller
                  control={control}
                  name={`avslaattePensjoner.${index}.institusjon.vedtaksdato` as const}
                  render={({ field: dateField, fieldState }) => (
                    <DatePicker
                      dropdownCaption
                      fromDate={new Date(`1 Jan ${currentYear - 50}`)}
                      onSelect={(date) => {
                        if (date) {
                          const day = String(date.getDate()).padStart(2, "0");
                          const month = String(date.getMonth() + 1).padStart(2, "0");
                          const year = date.getFullYear();
                          dateField.onChange(`${day}.${month}.${year}`);
                        } else {
                          dateField.onChange("");
                        }
                      }}
                      toDate={new Date(`31 Dec ${currentYear + 5}`)}
                    >
                      <DatePicker.Input
                        className="p1-datepicker"
                        error={fieldState.error?.message}
                        label="Vedtaksdato"
                        placeholder="dd.mm.åååå"
                        size="small"
                        {...dateField}
                      />
                    </DatePicker>
                  )}
                />
              </td>

              {/* 4.2 Pensjonstype */}
              <td className={hasPensjonstypeError(index) ? "p1-cell-error" : ""}>
                <Controller
                  control={control}
                  name={`avslaattePensjoner.${index}.pensjonstype` as const}
                  render={({ field: radioField, fieldState }) => (
                    <RadioGroup
                      error={fieldState.error?.message}
                      legend="Velg"
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
              </td>

              {/* 4.3 Avslagsbegrunnelse */}
              <td className={hasAvslagsbegrunnelseError(index) ? "p1-cell-error" : ""}>
                <Controller
                  control={control}
                  name={`avslaattePensjoner.${index}.avslagsbegrunnelse` as const}
                  render={({ field: radioField, fieldState }) => (
                    <RadioGroup
                      error={fieldState.error?.message}
                      legend="Velg"
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
              </td>

              {/* 4.4 Vurderingsperiode */}
              <td className={`cell-seamless ${hasVurderingsperiodeError(index) ? "p1-cell-error" : ""}`}>
                <Textarea
                  className="p1-seamless-textarea"
                  error={errors.avslaattePensjoner?.[index]?.vurderingsperiode?.message}
                  hideLabel
                  label="Vurderingsperiode"
                  size="small"
                  {...register(`avslaattePensjoner.${index}.vurderingsperiode` as const)}
                />
              </td>

              {/* 4.5 Adresse ny vurdering */}
              <td className={`cell-seamless ${hasAdresseError(index) ? "p1-cell-error" : ""}`}>
                <Textarea
                  className="p1-seamless-textarea"
                  error={errors.avslaattePensjoner?.[index]?.adresseNyVurdering?.message}
                  hideLabel
                  label="Adresse for ny vurdering"
                  size="small"
                  {...register(`avslaattePensjoner.${index}.adresseNyVurdering` as const)}
                />
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="p1-add-row-container">
        <Button icon={<PlusIcon />} onClick={addRow} size="small" type="button" variant="secondary">
          Legg til ny rad
        </Button>
      </div>
    </div>
  );
};
