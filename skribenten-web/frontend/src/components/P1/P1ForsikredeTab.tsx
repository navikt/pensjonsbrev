import { PadlockLockedIcon } from "@navikt/aksel-icons";
import { Heading, TextField } from "@navikt/ds-react";
import { useFormContext } from "react-hook-form";

import type { P1Redigerbar } from "~/types/p1";

import { DateField } from "./P1DateField";
type P1ForsikredeTabProps = {
  disabled?: boolean;
};

export const P1ForsikredeTab = ({ disabled }: P1ForsikredeTabProps) => {
  const { getValues, register } = useFormContext<P1Redigerbar>();

  return (
    <div className={disabled ? "p1-tab-disabled" : undefined}>
      <div className="p1-tab-header">
        <Heading size="small" spacing>
          2. Personopplysninger om den forsikrede
        </Heading>
        {disabled && <PadlockLockedIcon fontSize="1.5rem" title="Denne fanen er skrivebeskyttet" />}
      </div>

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
              <DateField date={getValues("forsikrede.foedselsdato")} hideLabel label="Fødselsdato" />
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
