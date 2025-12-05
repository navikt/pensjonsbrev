import { PadlockLockedIcon } from "@navikt/aksel-icons";
import { Heading, TextField } from "@navikt/ds-react";
import { useFormContext } from "react-hook-form";

import type { P1Redigerbar } from "~/types/p1";

type P1InnehaverTabProps = {
  disabled?: boolean;
};

export const P1InnehaverTab = ({ disabled }: P1InnehaverTabProps) => {
  const { register } = useFormContext<P1Redigerbar>();

  return (
    <div className={disabled ? "p1-tab-disabled" : undefined}>
      <div className="p1-tab-header">
        <Heading size="small" spacing>
          1. Personopplysninger om innehaveren
        </Heading>
        {disabled && <PadlockLockedIcon fontSize="1.5rem" title="Denne fanen er skrivebeskyttet" />}
      </div>

      <table className="p1-table p1-table--two-column">
        <tbody>
          <tr>
            <td>1.1 Etternavn</td>
            <td className="cell-seamless">
              <TextField
                className="p1-seamless-textfield"
                hideLabel
                label="Etternavn"
                size="small"
                {...register("innehaver.etternavn")}
              />
            </td>
          </tr>
          <tr>
            <td>1.2 Fornavn</td>
            <td className="cell-seamless">
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Fornavn"
                size="small"
                {...register("innehaver.fornavn")}
              />
            </td>
          </tr>
          <tr>
            <td>1.3 Etternavn ved fødsel (*)</td>
            <td className="cell-seamless">
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Etternavn ved fødsel"
                size="small"
                {...register("innehaver.etternavnVedFoedsel")}
              />
            </td>
          </tr>
          <tr>
            <td>1.4 Fødselsdato</td>
            <td className="cell-seamless">
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Fødselsdato"
                size="small"
                {...register("innehaver.foedselsdato")}
              />
            </td>
          </tr>
          <tr>
            <td>1.4.1 Gatenavn og -nummer</td>
            <td className="cell-seamless">
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Adresselinje"
                size="small"
                {...register("innehaver.adresselinje")}
              />
            </td>
          </tr>
          <tr>
            <td>1.4.2 Poststed</td>
            <td className="cell-seamless">
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Poststed"
                size="small"
                {...register("innehaver.poststed")}
              />
            </td>
          </tr>
          <tr>
            <td>1.4.3 Postnummer</td>
            <td className="cell-seamless">
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Postnummer"
                size="small"
                {...register("innehaver.postnummer")}
              />
            </td>
          </tr>
          <tr>
            <td>1.4.4 Landskode</td>
            <td className="cell-seamless">
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Landskode"
                size="small"
                {...register("innehaver.landkode")}
              />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  );
};
