import { Heading, TextField } from "@navikt/ds-react";
import { useFormContext } from "react-hook-form";

import type { P1Redigerbar } from "~/types/p1";
type P1InstitusjonTabProps = {
  disabled?: boolean;
};

export const P1InstitusjonTab = ({ disabled }: P1InstitusjonTabProps) => {
  const { register } = useFormContext<P1Redigerbar>();

  return (
    <div>
      <Heading size="small" spacing>
        5. Institusjonen som har fylt ut skjemaet
      </Heading>

      <table className="p1-table p1-table--two-column ">
        <tbody>
          <tr>
            <td>5.1 Navn</td>
            <td className="cell-seamless">
              <TextField
                className="p1-seamless-textfield "
                disabled={disabled}
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
                className="p1-seamless-textfield "
                disabled={disabled}
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
                className="p1-seamless-textfield "
                disabled={disabled}
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
                className="p1-seamless-textfield "
                disabled={disabled}
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
                className="p1-seamless-textfield "
                disabled={disabled}
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
                className="p1-seamless-textfield "
                disabled={disabled}
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
                className="p1-seamless-textfield "
                disabled={disabled}
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
                className="p1-seamless-textfield "
                disabled={disabled}
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
                className="p1-seamless-textfield "
                disabled={disabled}
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
                className="p1-seamless-textfield "
                disabled={disabled}
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
