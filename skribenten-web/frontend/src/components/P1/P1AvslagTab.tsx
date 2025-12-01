import { css } from "@emotion/react";
import { PlusIcon } from "@navikt/aksel-icons";
import { Button, Heading, Radio, RadioGroup, Textarea, TextField } from "@navikt/ds-react";
import { Controller, useFieldArray, useFormContext } from "react-hook-form";

import type { P1RedigerbarForm } from "~/types/p1FormTypes";

import { emptyAvslaattRow } from "./emptyP1";
import { AVSLAGSBEGRUNNELSE_OPTIONS, PENSJONSTYPE_OPTIONS } from "./p1Constants";

const tableStyles = css`
  width: 100%;
  min-width: 900px;
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

const seamlessInputStyles = css`
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

export const P1AvslagTab = () => {
  const { control, register } = useFormContext<P1RedigerbarForm>();

  const { fields, append } = useFieldArray<P1RedigerbarForm>({
    control,
    name: "avslaattePensjoner",
  });

  const addRow = () => append(emptyAvslaattRow());

  return (
    <div>
      <Heading size="small" spacing>
        4. Avslått pensjon
      </Heading>

      <table css={tableStyles}>
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
              <td>
                <TextField
                  label="Institusjon"
                  size="small"
                  {...register(`avslaattePensjoner.${index}.institusjon.institusjonsnavn` as const)}
                  style={{ marginBottom: "0.5rem" }}
                />
                <TextField
                  label="PIN/saksnummer"
                  size="small"
                  {...register(`avslaattePensjoner.${index}.institusjon.pin` as const)}
                  style={{ marginBottom: "0.5rem" }}
                />
                <TextField
                  label="Vedtaksdato"
                  size="small"
                  {...register(`avslaattePensjoner.${index}.institusjon.vedtaksdato` as const)}
                />
              </td>

              {/* 4.2 Pensjonstype */}
              <td>
                <Controller
                  control={control}
                  name={`avslaattePensjoner.${index}.pensjonstype` as const}
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

              {/* 4.3 Avslagsbegrunnelse */}
              <td>
                <Controller
                  control={control}
                  name={`avslaattePensjoner.${index}.avslagsbegrunnelse` as const}
                  render={({ field: radioField }) => (
                    <RadioGroup
                      legend="Velg"
                      onChange={(val) => radioField.onChange(val || null)}
                      size="small"
                      value={radioField.value ?? ""}
                    >
                      {AVSLAGSBEGRUNNELSE_OPTIONS.map((opt) => (
                        <Radio key={opt.value} value={opt.value}>
                          {opt.label}
                        </Radio>
                      ))}
                    </RadioGroup>
                  )}
                />
              </td>

              {/* 4.4 Vurderingsperiode */}
              <td className="cell-seamless">
                <Textarea
                  css={seamlessInputStyles}
                  hideLabel
                  label="Vurderingsperiode"
                  size="small"
                  {...register(`avslaattePensjoner.${index}.vurderingsperiode` as const)}
                />
              </td>

              {/* 4.5 Adresse ny vurdering */}
              <td className="cell-seamless">
                <Textarea
                  css={seamlessInputStyles}
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

      <div css={centerButtonContainer}>
        <Button icon={<PlusIcon />} onClick={addRow} size="small" type="button" variant="tertiary">
          Legg til ny rad
        </Button>
      </div>
    </div>
  );
};
