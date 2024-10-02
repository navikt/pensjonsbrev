import { css } from "@emotion/react";
import { ArrowLeftIcon } from "@navikt/aksel-icons";
import { PencilIcon } from "@navikt/aksel-icons";
import { Button, HStack, Table, VStack } from "@navikt/ds-react";
import type { AxiosError } from "axios";

import { ApiError } from "~/components/ApiError";
import type { Adresse } from "~/types/apiTypes";
import type { Nullable } from "~/types/Nullable";
import { humanizeName } from "~/utils/stringUtils";

const BackButton = (properties: { icon: React.ReactNode; text: string; onClick: () => void }) => {
  return (
    <Button
      css={css`
        width: fit-content;
        align-self: flex-start;
      `}
      icon={properties.icon}
      onClick={properties.onClick}
      size="small"
      type="button"
      variant="tertiary"
    >
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
    <VStack gap="4">
      {properties.onTilbake.plassering === "top" && (
        <BackButton icon={<PencilIcon />} onClick={properties.onTilbake.fn} text="Rediger" />
      )}
      <OppsummeringAvAdresse adresse={properties.adresse} type={properties.samhandlerType} />
      {properties.onTilbake.plassering === "bottom" && (
        <BackButton icon={<ArrowLeftIcon />} onClick={properties.onTilbake.fn} text="Tilbake til søk" />
      )}
      <HStack
        css={css`
          align-self: flex-end;
        `}
        gap="4"
      >
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
      {properties.error && <ApiError error={properties.error} title={"En feil skjedde"} />}
    </VStack>
  );
};

export default OppsummeringAvValgtMottaker;

const OppsummeringAvAdresse = (properties: { type: Nullable<string>; adresse: Adresse }) => {
  return (
    <Table>
      <Table.Body>
        {properties.type && <InversedTableRow label="Type" value={properties.type} />}
        <InversedTableRow label="Navn" value={properties.adresse.navn} />
        <InversedTableRow label="Adresselinje 1" value={properties.adresse.linje1} />
        <InversedTableRow label="Adresselinje 2" value={properties.adresse.linje2} />
        <InversedTableRow label="Adresselinje 3" value={properties.adresse.linje3} />
        <InversedTableRow label="Postnummer" value={properties.adresse.postnr} />
        <InversedTableRow label="Poststed" value={properties.adresse.poststed} />
        <InversedTableRow label="Land" value={properties.adresse.land} />
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
