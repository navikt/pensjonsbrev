import { PadlockLockedIcon } from "@navikt/aksel-icons";
import { Heading, HStack, Table, TextField } from "@navikt/ds-react";
import { useFormContext } from "react-hook-form";

import type { P1RedigerbarForm } from "~/types/p1FormTypes";

import { DateField } from "./P1DateField";

type P1ForsikredeTabProps = {
  disabled?: boolean;
};

export const P1ForsikredeTab = ({ disabled }: P1ForsikredeTabProps) => {
  const { getValues, register } = useFormContext<P1RedigerbarForm>();

  const tabContent = (
    <>
      <HStack gap="space-8">
        <Heading size="small" spacing>
          2. Personopplysninger om den forsikrede (låst for redigering)
        </Heading>
        {disabled && (
          <PadlockLockedIcon fontSize="var(--ax-font-size-heading-medium)" title="Denne fanen er skrivebeskyttet" />
        )}
      </HStack>

      <Table
        className="p1-table p1-table--two-column p1-table--zebra-stripes"
        css={{ maxWidth: "1068px" }}
        size="small"
      >
        <Table.Body>
          <Table.Row>
            <Table.DataCell css={{ width: "250px" }}>2.1 Etternavn</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield"
                hideLabel
                label="Etternavn"
                readOnly={disabled}
                size="small"
                {...register("forsikrede.etternavn")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>2.2 Fornavn</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield"
                hideLabel
                label="Fornavn"
                readOnly={disabled}
                size="small"
                {...register("forsikrede.fornavn")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>2.3 Etternavn ved fødsel (*)</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield"
                hideLabel
                label="Etternavn ved fødsel"
                readOnly={disabled}
                size="small"
                {...register("forsikrede.etternavnVedFoedsel")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>2.4 Fødselsdato</Table.DataCell>
            <Table.DataCell>
              <DateField date={getValues("forsikrede.foedselsdato")} hideLabel label="Fødselsdato" />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>2.5 Sist kjente adresse</Table.DataCell>
            <Table.DataCell>
              <TextField className="p1-seamless-textfield" hideLabel label="Sist kjente adresse" readOnly={disabled} />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>2.5.1 Gatenavn og -nummer</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield"
                hideLabel
                label="Gatenavn og -nummer"
                readOnly={disabled}
                size="small"
                {...register("forsikrede.adresselinje")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>2.5.2 Poststed</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield"
                hideLabel
                label="Poststed"
                readOnly={disabled}
                size="small"
                {...register("forsikrede.poststed")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>2.5.3 Postnummer</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield"
                hideLabel
                label="Postnummer"
                readOnly={disabled}
                size="small"
                {...register("forsikrede.postnummer")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>2.5.4 Landskode</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield"
                hideLabel
                label="Landskode"
                readOnly={disabled}
                size="small"
                {...register("forsikrede.landkode")}
              />
            </Table.DataCell>
          </Table.Row>
        </Table.Body>
      </Table>
    </>
  );
  return disabled ? <div className="p1-tab-disabled">{tabContent}</div> : tabContent;
};
