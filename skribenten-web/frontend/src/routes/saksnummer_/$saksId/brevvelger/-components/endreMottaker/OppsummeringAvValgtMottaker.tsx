import { ArrowLeftIcon } from "@navikt/aksel-icons";
import { PencilIcon } from "@navikt/aksel-icons";
import { Button, HStack, Table, VStack } from "@navikt/ds-react";
import type { AxiosError } from "axios";

import { ApiError } from "~/components/ApiError";
import { useLandData } from "~/hooks/useLandData";
import type { Adresse } from "~/types/apiTypes";
import type { Nullable } from "~/types/Nullable";
import { getCountryNameByKode } from "~/utils/countryUtils";
import { humanizeName } from "~/utils/stringUtils";

const BackButton = (properties: { icon: React.ReactNode; text: string; onClick: () => void }) => {
  return (
    <Button icon={properties.icon} onClick={properties.onClick} size="small" type="button" variant="tertiary">
      {properties.text}
    </Button>
  );
};

const OppsummeringAvValgtMottaker = (properties: {
  samhandlerType: Nullable<string>;
  adresse: Adresse;
  onAvbryt: () => void;
  onBekreft: () => void;
  error: Nullable<AxiosError>;
  isPending: Nullable<boolean>;
  onTilbake: {
    plassering: "top" | "bottom";
    fn: () => void;
  };
}) => {
  return (
    <VStack gap="space-16">
      {properties.onTilbake.plassering === "top" && (
        <HStack justify="start">
          <BackButton icon={<PencilIcon />} onClick={properties.onTilbake.fn} text="Rediger" />
        </HStack>
      )}
      <OppsummeringAvAdresse adresse={properties.adresse} type={properties.samhandlerType} />
      {properties.onTilbake.plassering === "bottom" && (
        <BackButton icon={<ArrowLeftIcon />} onClick={properties.onTilbake.fn} text="Tilbake til søk" />
      )}
      <HStack gap="space-16" justify="end">
        <Button onClick={properties.onAvbryt} size="small" type="button" variant="tertiary">
          Avbryt
        </Button>
        <Button
          data-cy="bekreft-ny-mottaker"
          loading={properties.isPending ?? false}
          onClick={properties.onBekreft}
          size="small"
          type="button"
        >
          Bekreft ny mottaker
        </Button>
      </HStack>
      {properties.error && <ApiError error={properties.error} title="En feil skjedde" />}
    </VStack>
  );
};

export default OppsummeringAvValgtMottaker;

const OppsummeringAvAdresse = (properties: { type: Nullable<string>; adresse: Adresse }) => {
  const { data: landData } = useLandData();

  return (
    <Table size="small">
      <Table.Body>
        {properties.type && <InversedTableRow label="Type" value={properties.type} />}
        <InversedTableRow label="Navn" value={properties.adresse.navn} />
        <InversedTableRow label="Adresselinje 1" value={properties.adresse.linje1} />
        <InversedTableRow label="Adresselinje 2" value={properties.adresse.linje2} />
        <InversedTableRow label="Adresselinje 3" value={properties.adresse.linje3} />
        <InversedTableRow label="Postnummer" value={properties.adresse.postnr} />
        <InversedTableRow label="Poststed" value={properties.adresse.poststed} />
        <InversedTableRow label="Land" value={getCountryNameByKode(properties.adresse.land, landData || [])} />
      </Table.Body>
    </Table>
  );
};

function InversedTableRow({ label, value }: { label: string; value: Nullable<string> }) {
  if (!value) {
    return <></>;
  }

  return (
    <Table.Row>
      <Table.HeaderCell scope="row">{label}</Table.HeaderCell>
      <Table.DataCell>{humanizeName(value)}</Table.DataCell>
    </Table.Row>
  );
}
