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

export const P1InstitusjonTab = () => {
  const { register } = useFormContext<P1Redigerbar>();

  return (
    <div>
      <Heading size="small" spacing>
        5. Institusjonen som har fylt ut skjemaet
      </Heading>

      <table css={tableStyles}>
        <tbody>
          <tr>
            <td>5.1 Navn</td>
            <td className="cell-seamless">
              <TextField
                css={seamlessInputStyles}
                hideLabel
                label="Navn"
                size="small"
                {...register("utfyllendeInstitusjon.navn")}
              />
            </td>
          </tr>
          <tr>
            <td>5.2 Gatenavn og -nummer</td>
            <td className="cell-seamless">
              <TextField
                css={seamlessInputStyles}
                hideLabel
                label="Adresselinje"
                size="small"
                {...register("utfyllendeInstitusjon.adresselinje")}
              />
            </td>
          </tr>
          <tr>
            <td>5.3 Poststed</td>
            <td className="cell-seamless">
              <TextField
                css={seamlessInputStyles}
                hideLabel
                label="Poststed"
                size="small"
                {...register("utfyllendeInstitusjon.poststed")}
              />
            </td>
          </tr>
          <tr>
            <td>5.4 Postnummer</td>
            <td className="cell-seamless">
              <TextField
                css={seamlessInputStyles}
                hideLabel
                label="Postnummer"
                size="small"
                {...register("utfyllendeInstitusjon.postnummer")}
              />
            </td>
          </tr>
          <tr>
            <td>5.5 Landskode</td>
            <td className="cell-seamless">
              <TextField
                css={seamlessInputStyles}
                hideLabel
                label="Landskode"
                size="small"
                {...register("utfyllendeInstitusjon.landkode")}
              />
            </td>
          </tr>
          <tr>
            <td>5.6 Institusjons-ID</td>
            <td className="cell-seamless">
              <TextField
                css={seamlessInputStyles}
                hideLabel
                label="Institusjons-ID"
                size="small"
                {...register("utfyllendeInstitusjon.institusjonsID")}
              />
            </td>
          </tr>
          <tr>
            <td>5.7 Faksnummer</td>
            <td className="cell-seamless">
              <TextField
                css={seamlessInputStyles}
                hideLabel
                label="Faksnummer"
                size="small"
                {...register("utfyllendeInstitusjon.faksnummer")}
              />
            </td>
          </tr>
          <tr>
            <td>5.8 Telefonnummer</td>
            <td className="cell-seamless">
              <TextField
                css={seamlessInputStyles}
                hideLabel
                label="Telefonnummer"
                size="small"
                {...register("utfyllendeInstitusjon.telefonnummer")}
              />
            </td>
          </tr>
          <tr>
            <td>5.9 E-post</td>
            <td className="cell-seamless">
              <TextField
                css={seamlessInputStyles}
                hideLabel
                label="E-post"
                size="small"
                {...register("utfyllendeInstitusjon.epost")}
              />
            </td>
          </tr>
          <tr>
            <td>5.10 Dato</td>
            <td className="cell-seamless">
              <TextField
                css={seamlessInputStyles}
                hideLabel
                label="Dato"
                size="small"
                {...register("utfyllendeInstitusjon.dato")}
              />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  );
};
