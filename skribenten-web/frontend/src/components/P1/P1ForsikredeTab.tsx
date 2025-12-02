import { Heading, TextField } from "@navikt/ds-react";
import { useFormContext } from "react-hook-form";

import type { P1Redigerbar } from "~/types/p1";

export const P1ForsikredeTab = () => {
  const { register } = useFormContext<P1Redigerbar>();

  return (
    <div>
      <Heading size="small" spacing>
        2. Personopplysninger om den forsikrede
      </Heading>

      <table className="p1-table p1-table--two-column">
        <tbody>
          <tr>
            <td>2.1 Etternavn</td>
            <td className="cell-seamless">
              <TextField
                className="p1-seamless-textfield"
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
                className="p1-seamless-textfield"
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
                className="p1-seamless-textfield"
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
                className="p1-seamless-textfield"
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
                className="p1-seamless-textfield"
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
                className="p1-seamless-textfield"
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
                className="p1-seamless-textfield"
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
                className="p1-seamless-textfield"
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
