import { css } from "@emotion/react";
import { Heading, TextField } from "@navikt/ds-react";
import { useFormContext } from "react-hook-form";

import type { P1Redigerbar } from "~/types/p1";

const tableStyles = css`
  width: 100%;
  max-width: 800px;
  border-collapse: collapse;
  margin-bottom: 1rem;

  tbody tr:nth-of-type(even) {
    background-color: #f2f3f5;
  }
  tbody tr:nth-of-type(odd) {
    background-color: var(--a-surface-default);
  }

  td {
    border: 1px solid var(--a-border-default);
    vertical-align: middle;
    text-align: left;
    padding: 0.75rem 1rem;
    font-size: 0.875rem;
    height: 3rem;
  }

  td:first-of-type {
    width: 35%;
    font-weight: 600;
  }

  td:last-of-type {
    width: 65%;
  }

  .cell-seamless {
    padding: 0;
    height: 3rem;
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
    height: 100%;
  }

  .navds-text-field__input {
    border: none;
    border-radius: 0;
    box-shadow: none;
    background-color: transparent;
    padding: 0.75rem 1rem;
    height: 100%;

    &:focus {
      outline: none;
      box-shadow: inset 0 0 0 2px var(--a-border-focus);
    }
  }
`;

export const P1ForsikredeTab = () => {
  const { register } = useFormContext<P1Redigerbar>();

  return (
    <div>
      <Heading size="small" spacing>
        2. Personopplysninger om den forsikrede
      </Heading>

      <table css={tableStyles}>
        <tbody>
          <tr>
            <td>2.1 Etternavn</td>
            <td className="cell-seamless">
              <TextField
                css={seamlessInputStyles}
                hideLabel
                label="Etternavn"
                size="small"
                {...register("forsikrede.etternavn")}
              />
            </td>
          </tr>
          <tr>
            <td>2.2 Fornavn</td>
            <td className="cell-seamless">
              <TextField
                css={seamlessInputStyles}
                hideLabel
                label="Fornavn"
                size="small"
                {...register("forsikrede.fornavn")}
              />
            </td>
          </tr>
          <tr>
            <td>2.3 Etternavn ved fødsel (*)</td>
            <td className="cell-seamless">
              <TextField
                css={seamlessInputStyles}
                hideLabel
                label="Etternavn ved fødsel"
                size="small"
                {...register("forsikrede.etternavnVedFoedsel")}
              />
            </td>
          </tr>
          <tr>
            <td>2.4 Fødselsdato</td>
            <td className="cell-seamless">
              <TextField
                css={seamlessInputStyles}
                hideLabel
                label="Fødselsdato"
                size="small"
                {...register("forsikrede.foedselsdato")}
              />
            </td>
          </tr>
          <tr>
            <td>2.5.1 Gatenavn og -nummer</td>
            <td className="cell-seamless">
              <TextField
                css={seamlessInputStyles}
                hideLabel
                label="Adresselinje"
                size="small"
                {...register("forsikrede.adresselinje")}
              />
            </td>
          </tr>
          <tr>
            <td>2.5.2 Poststed</td>
            <td className="cell-seamless">
              <TextField
                css={seamlessInputStyles}
                hideLabel
                label="Poststed"
                size="small"
                {...register("forsikrede.poststed")}
              />
            </td>
          </tr>
          <tr>
            <td>2.5.3 Postnummer</td>
            <td className="cell-seamless">
              <TextField
                css={seamlessInputStyles}
                hideLabel
                label="Postnummer"
                size="small"
                {...register("forsikrede.postnummer")}
              />
            </td>
          </tr>
          <tr>
            <td>2.5.4 Landskode</td>
            <td className="cell-seamless">
              <TextField
                css={seamlessInputStyles}
                hideLabel
                label="Landskode"
                size="small"
                {...register("forsikrede.landkode")}
              />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  );
};
