import { css } from "@emotion/react";
import { PlusIcon } from "@navikt/aksel-icons";
import { Button, Heading, Radio, RadioGroup, Textarea, TextField } from "@navikt/ds-react";
import { Controller, useFieldArray, useFormContext } from "react-hook-form";

import type { P1RedigerbarForm } from "~/types/p1FormTypes";

import { emptyAvslaattRow } from "./emptyP1";
import { AVSLAGSBEGRUNNELSE_OPTIONS, PENSJONSTYPE_OPTIONS } from "./p1Constants";

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
              <td>
                <TextField
                  label="Institusjon"
                  size="small"
                  {...register(`avslaattePensjoner.${index}.institusjon.institusjonsnavn` as const)}
                  css={css`
                    margin-bottom: 0.5rem;
                  `}
                />
                <TextField
                  label="PIN/saksnummer"
                  size="small"
                  {...register(`avslaattePensjoner.${index}.institusjon.pin` as const)}
                  css={css`
                    margin-bottom: 0.5rem;
                  `}
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
              <td className="cell-seamless">
                <Textarea
                  className="p1-seamless-textarea"
                  hideLabel
                  label="Vurderingsperiode"
                  size="small"
                  {...register(`avslaattePensjoner.${index}.vurderingsperiode` as const)}
                />
              </td>

              {/* 4.5 Adresse ny vurdering */}
              <td className="cell-seamless">
                <Textarea
                  className="p1-seamless-textarea"
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
        <Button icon={<PlusIcon />} onClick={addRow} size="small" type="button" variant="tertiary">
          Legg til ny rad
        </Button>
      </div>
    </div>
  );
};
