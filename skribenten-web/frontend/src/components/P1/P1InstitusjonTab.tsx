import { PadlockLockedIcon } from "@navikt/aksel-icons";
import { Heading, HStack, Table, TextField } from "@navikt/ds-react";
import { useFormContext } from "react-hook-form";

import type { P1RedigerbarForm } from "~/types/p1FormTypes";

import { DateField } from "./P1DateField";

type P1InstitusjonTabProps = {
  disabled?: boolean;
};

export const P1InstitusjonTab = ({ disabled }: P1InstitusjonTabProps) => {
  const { getValues, register } = useFormContext<P1RedigerbarForm>();

  const tabContent = (
    <>
      <HStack gap="space-8">
        <Heading size="small" spacing>
          5. Institusjonen som har fylt ut skjemaet (låst for redigering)
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
            <Table.DataCell css={{ width: "250px" }}>5.1 Navn</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Navn"
                size="small"
                {...register("utfyllendeInstitusjon.navn")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>5.2 Gatenavn og -nummer</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Gatenavn og -nummer"
                size="small"
                {...register("utfyllendeInstitusjon.adresselinje")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>5.3 Poststed</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Poststed"
                size="small"
                {...register("utfyllendeInstitusjon.poststed")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>5.4 Postnummer</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Postnummer"
                size="small"
                {...register("utfyllendeInstitusjon.postnummer")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>5.5 Landskode</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Landskode"
                size="small"
                {...register("utfyllendeInstitusjon.landkode")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>5.6 Institusjons-ID</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Institusjons-ID"
                size="small"
                {...register("utfyllendeInstitusjon.institusjonsID")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>5.7 Faksnummer</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Faksnummer"
                size="small"
                {...register("utfyllendeInstitusjon.faksnummer")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>5.8 Telefonnummer</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="Telefonnummer"
                size="small"
                {...register("utfyllendeInstitusjon.telefonnummer")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>5.9 E-post</Table.DataCell>
            <Table.DataCell>
              <TextField
                className="p1-seamless-textfield "
                hideLabel
                label="E-post"
                size="small"
                {...register("utfyllendeInstitusjon.epost")}
              />
            </Table.DataCell>
          </Table.Row>
          <Table.Row>
            <Table.DataCell>5.10 Dato</Table.DataCell>
            <Table.DataCell>
              <DateField date={getValues("utfyllendeInstitusjon.dato")} hideLabel label="Dato" />
            </Table.DataCell>
          </Table.Row>
        </Table.Body>
      </Table>
    </>
  );
  return disabled ? <div className="p1-tab-disabled">{tabContent}</div> : <>{tabContent}</>;
};
