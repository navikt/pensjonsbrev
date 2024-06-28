import { Button, HStack, Table, VStack } from "@navikt/ds-react";

import type { SamhandlerTypeCode } from "~/types/apiTypes";
import { capitalizeString } from "~/utils/stringUtils";

import type { TypeMottaker } from "./EndreMottakerUtils";

interface AdresseObject {
  adresselinje1: string;
  adresselinje2: string;
  adresselinje3: string;
  postnummer: string;
  poststed: string;
  land: string;
}

const OppsummeringAvValgtMottaker = (properties: {
  type: SamhandlerTypeCode | TypeMottaker;
  navn: string;
  adresse: AdresseObject;
  onAvbryt: () => void;
  onBekreft: (adresse: AdresseObject) => void;
}) => {
  return (
    <VStack gap="4">
      <Table>
        <Table.Body>
          <InversedTableRow label="Type" value={properties.type} />
          <InversedTableRow label="Navn" value={properties.navn} />
          <InversedTableRow label="Adresselinje 1" value={properties.adresse.adresselinje1} />
          <InversedTableRow label="Adresselinje 2" value={properties.adresse.adresselinje2} />
          <InversedTableRow label="Adresselinje 3" value={properties.adresse.adresselinje3} />
          <InversedTableRow label="Postnummer" value={properties.adresse.postnummer} />
          <InversedTableRow label="Poststed" value={properties.adresse.poststed} />
          <InversedTableRow label="Land" value={properties.adresse.land} />
        </Table.Body>
      </Table>
      <HStack>
        <Button onClick={properties.onAvbryt} type="button" variant="tertiary">
          Avbryt
        </Button>
        <Button onClick={() => properties.onBekreft(properties.adresse)} type="button">
          Bekreft ny mottaker
        </Button>
      </HStack>
    </VStack>
  );
};

export default OppsummeringAvValgtMottaker;

function InversedTableRow({ label, value }: { label: string; value?: string }) {
  if (!value) {
    return <></>;
  }

  return (
    <Table.Row>
      <Table.HeaderCell scope="row">{label}</Table.HeaderCell>
      <Table.DataCell>{capitalizeString(value)}</Table.DataCell>
    </Table.Row>
  );
}
