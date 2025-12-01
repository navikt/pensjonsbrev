import { css } from "@emotion/react";
import { PlusIcon } from "@navikt/aksel-icons";
import { Button, Heading, Radio, RadioGroup, Textarea, TextField } from "@navikt/ds-react";
import { Controller, useFieldArray, useFormContext } from "react-hook-form";

import type { P1RedigerbarForm } from "~/types/p1FormTypes";

import { emptyInnvilgetRow } from "./emptyP1";
import { GRUNNLAG_INNVILGET_OPTIONS, PENSJONSTYPE_OPTIONS, REDUKSJONSGRUNNLAG_OPTIONS } from "./p1Constants";

const tableStyles = css`
  width: 100%;
  min-width: 1200px;
  border-collapse: collapse;
  table-layout: fixed;
  margin-bottom: 1rem;

  tbody tr:nth-of-type(even) {
    background-color: #f2f3f5;
  }
  tbody tr:nth-of-type(odd) {
    background-color: var(--a-surface-default);
  }

  th,
  td {
    border: 1px solid var(--a-border-default);
    vertical-align: top;
    text-align: left;
    padding: 0.5rem;
    font-size: 0.875rem;
    word-break: break-word;
    overflow-wrap: anywhere;
  }

  th {
    background: #ebfcff;
  }
  .header-number {
    display: block;
    font-weight: 600;
    font-size: 16px;
    margin-bottom: 0.25rem;
  }

  .header-text {
    display: block;
    font-weight: 600;
    font-size: 16px;
  }

  .cell-seamless {
    padding: 0;
    height: 212px;
  }

  td .navds-radio-group {
    margin: 0;
    gap: 0.25rem;
  }

  td .navds-error-message {
    font-size: 0.75rem;
    margin-top: 0.15rem;
    white-space: normal;
  }
`;

const inputStyles = css`
  && {
    width: 100%;
    height: 100%;
    margin: 0;
  }

  .navds-form-field {
    width: 100%;
    height: 100%;
    margin: 0;
  }

  .navds-form-field__control {
    width: 100%;
    height: 100%;
  }

  .navds-textarea__wrapper {
    height: 100%;
  }

  .navds-textarea__input {
    border: none;
    border-radius: 0;
    box-shadow: none;
    background-color: transparent;
    padding: 0.5rem;
    width: 100%;
    height: 100%;
    resize: none;

    &:focus {
      outline: none;
      box-shadow: inset 0 0 0 2px var(--a-border-focus);
    }
  }
`;

const centerButtonContainer = css`
  display: flex;
  justify-content: center;
  margin-top: 1rem;
`;

export const P1InnvilgetTab = () => {
  const { control, register } = useFormContext<P1RedigerbarForm>();

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

      <table css={tableStyles}>
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
                  label="Institusjon"
                  size="small"
                  {...register(`innvilgedePensjoner.${index}.institusjon.institusjonsnavn` as const)}
                  style={{ marginBottom: "0.5rem" }}
                />
                <TextField
                  label="PIN/saksnummer"
                  size="small"
                  {...register(`innvilgedePensjoner.${index}.institusjon.pin` as const)}
                  style={{ marginBottom: "0.5rem" }}
                />
                <TextField
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
                  render={({ field: radioField }) => (
                    <RadioGroup
                      legend="Velg"
                      onChange={(val) => radioField.onChange(val || null)}
                      size="small"
                      value={radioField.value ?? ""}
                    >
                      {PENSJONSTYPE_OPTIONS.map((opt) => (
                        <Radio key={opt.value} value={opt.value}>
                          {opt.label}
                        </Radio>
                      ))}
                    </RadioGroup>
                  )}
                />
              </td>

              {/* 3.3 Dato første utbetaling */}
              <td className="cell-seamless">
                <Textarea
                  css={inputStyles}
                  hideLabel
                  label="Dato for første utbetaling"
                  size="small"
                  {...register(`innvilgedePensjoner.${index}.datoFoersteUtbetaling` as const)}
                />
              </td>

              {/* 3.4 Bruttobeløp */}
              <td className="cell-seamless">
                <Textarea
                  css={inputStyles}
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
                  render={({ field: radioField }) => (
                    <RadioGroup
                      legend="Velg"
                      onChange={(val) => radioField.onChange(val || null)}
                      size="small"
                      value={radioField.value ?? ""}
                    >
                      {GRUNNLAG_INNVILGET_OPTIONS.map((opt) => (
                        <Radio key={opt.value} value={opt.value}>
                          {opt.label}
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
                  render={({ field: radioField }) => (
                    <RadioGroup
                      legend="Velg"
                      onChange={(val) => radioField.onChange(val || null)}
                      size="small"
                      value={radioField.value ?? ""}
                    >
                      {REDUKSJONSGRUNNLAG_OPTIONS.map((opt) => (
                        <Radio key={opt.value} value={opt.value}>
                          {opt.label}
                        </Radio>
                      ))}
                    </RadioGroup>
                  )}
                />
              </td>

              {/* 3.7 Vurderingsperiode */}
              <td className="cell-seamless">
                <Textarea
                  css={inputStyles}
                  hideLabel
                  label="Vurderingsperiode"
                  size="small"
                  {...register(`innvilgedePensjoner.${index}.vurderingsperiode` as const)}
                />
              </td>

              {/* 3.8 Adresse ny vurdering */}
              <td className="cell-seamless">
                <Textarea
                  css={inputStyles}
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

      <div css={centerButtonContainer}>
        <Button icon={<PlusIcon />} onClick={addRow} size="small" type="button" variant="tertiary">
          Legg til ny rad
        </Button>
      </div>
    </div>
  );
};
