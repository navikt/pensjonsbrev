import { css } from "@emotion/react";
import { PlusIcon } from "@navikt/aksel-icons";
import { Button, Heading, Radio, RadioGroup, Textarea, TextField } from "@navikt/ds-react";
import { Controller, useFieldArray, useFormContext } from "react-hook-form";

import type { P1RedigerbarForm } from "~/types/p1FormTypes";

import { emptyInnvilgetRow } from "./emptyP1";
import { GRUNNLAG_INNVILGET_OPTIONS, PENSJONSTYPE_OPTIONS, REDUKSJONSGRUNNLAG_OPTIONS } from "./p1Constants";

export const P1InnvilgetTab = () => {
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

  return (
    <div>
      <Heading size="small" spacing>
        3. Innvilget pensjon
      </Heading>

      <table className="p1-table p1-table--innvilget">
        <thead>
          <tr>
            <th>
              <span className="header-number">3.1</span>
              <span className="header-text">
                Institusjon som gir pensjonen – med PIN/saksnummer og dato for vedtaket
              </span>
            </th>
            <th>
              <span className="header-number">3.2</span>
              <span className="header-text">Pensjonstype (1), (2), (3)</span>
            </th>
            <th>
              <span className="header-number">3.3</span>
              <span className="header-text">Dato for første utbetaling</span>
            </th>
            <th>
              <span className="header-number">3.4</span>
              <span className="header-text">Bruttobeløp med betalingshyppighet og valuta</span>
            </th>
            <th>
              <span className="header-number">3.5</span>
              <span className="header-text">Pensjonen er innvilget: (4), (5), (6)</span>
            </th>
            <th>
              <span className="header-number">3.6</span>
              <span className="header-text">Pensjonen er redusert: (7), (8)</span>
            </th>
            <th>
              <span className="header-number">3.7</span>
              <span className="header-text">Vurderingsperiode (starter den datoen samlet melding ble mottatt)</span>
            </th>
            <th>
              <span className="header-number">3.8</span>
              <span className="header-text">Hvor kravet om ny vurdering skal sendes</span>
            </th>
          </tr>
        </thead>

        <tbody>
          {fields.map((field, index) => (
            <tr key={field.id}>
              {/* 3.1 Institusjon + PIN/saksnr + vedtaksdato */}
              <td>
                <TextField
                  error={errors.innvilgedePensjoner?.[index]?.institusjon?.institusjonsnavn?.message}
                  label="Institusjon"
                  size="small"
                  {...register(`innvilgedePensjoner.${index}.institusjon.institusjonsnavn` as const)}
                  css={css`
                    margin-bottom: 0.5rem;
                  `}
                />
                <TextField
                  error={errors.innvilgedePensjoner?.[index]?.institusjon?.pin?.message}
                  label="PIN/saksnummer"
                  size="small"
                  {...register(`innvilgedePensjoner.${index}.institusjon.pin` as const)}
                  css={css`
                    margin-bottom: 0.5rem;
                  `}
                />
                <TextField
                  error={errors.innvilgedePensjoner?.[index]?.institusjon?.vedtaksdato?.message}
                  label="Vedtaksdato"
                  size="small"
                  {...register(`innvilgedePensjoner.${index}.institusjon.vedtaksdato` as const)}
                />
              </td>

              {/* 3.2 Pensjonstype */}
              <td>
                <Controller
                  control={control}
                  name={`innvilgedePensjoner.${index}.pensjonstype` as const}
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

              {/* 3.3 Dato første utbetaling */}
              <td className="cell-seamless">
                <Textarea
                  className="p1-seamless-textarea"
                  error={errors.innvilgedePensjoner?.[index]?.datoFoersteUtbetaling?.message}
                  hideLabel
                  label="Dato for første utbetaling"
                  size="small"
                  {...register(`innvilgedePensjoner.${index}.datoFoersteUtbetaling` as const)}
                />
              </td>

              {/* 3.4 Bruttobeløp */}
              <td className="cell-seamless">
                <Textarea
                  className="p1-seamless-textarea"
                  error={errors.innvilgedePensjoner?.[index]?.utbetalt?.message}
                  hideLabel
                  label="Bruttobeløp / hyppighet / valuta"
                  size="small"
                  {...register(`innvilgedePensjoner.${index}.utbetalt` as const)}
                />
              </td>

              {/* 3.5 Grunnlag innvilget */}
              <td>
                <Controller
                  control={control}
                  name={`innvilgedePensjoner.${index}.grunnlagInnvilget` as const}
                  render={({ field: radioField, fieldState }) => (
                    <RadioGroup
                      error={fieldState.error?.message}
                      legend="Velg"
                      onChange={(val) => radioField.onChange(val || null)}
                      size="small"
                      value={radioField.value ?? ""}
                    >
                      {GRUNNLAG_INNVILGET_OPTIONS.map((option) => (
                        <Radio key={option.value} value={option.value}>
                          {option.label}
                        </Radio>
                      ))}
                    </RadioGroup>
                  )}
                />
              </td>

              {/* 3.6 Reduksjonsgrunnlag */}
              <td>
                <Controller
                  control={control}
                  name={`innvilgedePensjoner.${index}.reduksjonsgrunnlag` as const}
                  render={({ field: radioField, fieldState }) => (
                    <RadioGroup
                      error={fieldState.error?.message}
                      legend="Velg"
                      onChange={(val) => radioField.onChange(val || null)}
                      size="small"
                      value={radioField.value ?? ""}
                    >
                      {REDUKSJONSGRUNNLAG_OPTIONS.map((option) => (
                        <Radio key={option.value} value={option.value}>
                          {option.label}
                        </Radio>
                      ))}
                    </RadioGroup>
                  )}
                />
              </td>

              {/* 3.7 Vurderingsperiode */}
              <td className="cell-seamless">
                <Textarea
                  className="p1-seamless-textarea"
                  error={errors.innvilgedePensjoner?.[index]?.vurderingsperiode?.message}
                  hideLabel
                  label="Vurderingsperiode"
                  size="small"
                  {...register(`innvilgedePensjoner.${index}.vurderingsperiode` as const)}
                />
              </td>

              {/* 3.8 Adresse ny vurdering */}
              <td className="cell-seamless">
                <Textarea
                  className="p1-seamless-textarea"
                  error={errors.innvilgedePensjoner?.[index]?.adresseNyVurdering?.message}
                  hideLabel
                  label="Adresse for ny vurdering"
                  size="small"
                  {...register(`innvilgedePensjoner.${index}.adresseNyVurdering` as const)}
                />
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="p1-add-row-container">
        <Button icon={<PlusIcon />} onClick={addRow} size="small" type="button" variant="tertiary">
          Legg til ny rad
        </Button>
      </div>
    </div>
  );
};
