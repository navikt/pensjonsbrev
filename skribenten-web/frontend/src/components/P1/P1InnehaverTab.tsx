import { PadlockLockedIcon } from "@navikt/aksel-icons";
import { Heading, HStack, Table, TextField } from "@navikt/ds-react";
import { useFormContext } from "react-hook-form";

import type { P1RedigerbarForm } from "~/types/p1FormTypes";

type P1InnehaverTabProps = {
  disabled?: boolean;
};

export const P1InnehaverTab = ({ disabled }: P1InnehaverTabProps) => {
  const { register } = useFormContext<P1RedigerbarForm>();

  const tabContent = (
    <>
      <HStack gap="space-8">
        <Heading size="small" spacing>
          1. Personopplysninger om innehaveren (låst for redigering)
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
            <Table.DataCell css={{ width: "250px" }}>1.1 Etternavn</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield"
                hideLabel
                label="Etternavn"
                readOnly={disabled}
                size="small"
                {...register("innehaver.etternavn")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>1.2 Fornavn</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Fornavn"
                readOnly={disabled}
                size="small"
                {...register("innehaver.fornavn")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>1.3 Etternavn ved fødsel (*)</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Etternavn ved fødsel"
                readOnly={disabled}
                size="small"
                {...register("innehaver.etternavnVedFoedsel")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>1.4 Gjeldende adresse</Table.DataCell>
            <Table.DataCell>
              <TextField className="p1-seamless-textfield " hideLabel label="Gjeldende adresse" readOnly={disabled} />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>1.4.1 Gatenavn og -nummer</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Gatenavn og -nummer"
                readOnly={disabled}
                size="small"
                {...register("innehaver.adresselinje")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>1.4.2 Poststed</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Poststed"
                readOnly={disabled}
                size="small"
                {...register("innehaver.poststed")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>1.4.3 Postnummer</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Postnummer"
                readOnly={disabled}
                size="small"
                {...register("innehaver.postnummer")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>1.4.4 Landskode</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Landskode"
                readOnly={disabled}
                size="small"
                {...register("innehaver.landkode")}
              />
            </Table.DataCell>
          </Table.Row>
        </Table.Body>
      </Table>
    </>
  );
  return disabled ? <div className="p1-tab-disabled">{tabContent}</div> : <>{tabContent}</>;
};
