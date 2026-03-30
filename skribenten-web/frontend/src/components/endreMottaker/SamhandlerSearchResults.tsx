import { Bleed, BodyShort, Box, Skeleton, Table, Tag, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { useState } from "react";

import { hentSamhandlerAdresse } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import { type Adresse, type Samhandler } from "~/types/apiTypes";
import { type Nullable } from "~/types/Nullable";
import { humanizeName } from "~/utils/stringUtils";
import { trackEvent } from "~/utils/umami";

type SelectionMethod = "radio" | "row-click" | "keyboard";

export function SamhandlerSearchResults({
  samhandlere,
  selectedId,
  onSelectedChange,
}: {
  samhandlere: Samhandler[];
  selectedId: Nullable<string>;
  onSelectedChange: (idTSSEkstern: string) => void;
}) {
  return (
    <VStack gap="space-8">
      <BodyShort size="small" weight="semibold">
        {samhandlere.length} treff
      </BodyShort>
      {samhandlere.length > 0 && (
        <Bleed
          css={{
            maxHeight: "500px",
            overflowY: "auto",
          }}
          marginInline="space-12"
        >
          <Box marginInline="space-12">
            <Table size="small">
              <Table.Header css={{ position: "sticky" }}>
                <Table.Row>
                  <Table.HeaderCell />
                  <Table.ColumnHeader>Navn</Table.ColumnHeader>
                  <Table.HeaderCell />
                  <Table.HeaderCell />
                </Table.Row>
              </Table.Header>
              <Table.Body>
                {samhandlere.map((samhandler) => (
                  <SamhandlerResultRow
                    key={samhandler.idTSSEkstern}
                    onSelect={() => onSelectedChange(samhandler.idTSSEkstern)}
                    samhandler={samhandler}
                    selected={selectedId === samhandler.idTSSEkstern}
                  />
                ))}
              </Table.Body>
            </Table>
          </Box>
        </Bleed>
      )}
    </VStack>
  );
}

function SamhandlerResultRow({
  samhandler,
  selected,
  onSelect,
}: {
  samhandler: Samhandler;
  selected: boolean;
  onSelect: () => void;
}) {
  const [open, setOpen] = useState(false);

  const selectWithTracking = (method: SelectionMethod) => {
    onSelect();
    trackEvent("samhandler valgt", { method });
  };

  return (
    <Table.ExpandableRow
      content={<SamhandlerAdresseDetaljer idTSSEkstern={samhandler.idTSSEkstern} />}
      css={{ cursor: "pointer" }}
      onOpenChange={setOpen}
      open={open}
      togglePlacement="right"
    >
      <Table.DataCell
        onClick={(e) => {
          e.stopPropagation();
          selectWithTracking("radio");
        }}
      >
        <span className="aksel-radio aksel-radio--small">
          <input
            aria-label={humanizeName(samhandler.navn)}
            checked={selected}
            className="aksel-radio__input"
            data-cy="velg-samhandler"
            name="samhandler-valg"
            onChange={() => selectWithTracking("keyboard")}
            type="radio"
            value={samhandler.idTSSEkstern}
          />
        </span>
      </Table.DataCell>
      <Table.DataCell
        onClick={() => {
          selectWithTracking("row-click");
          if (!open) setOpen(true);
        }}
      >
        {humanizeName(samhandler.navn)}
      </Table.DataCell>
      <Table.DataCell
        onClick={() => {
          selectWithTracking("row-click");
          if (!open) setOpen(true);
        }}
      >
        <Tag data-color="neutral" size="xsmall" variant="moderate">
          Samhandler
        </Tag>
      </Table.DataCell>
    </Table.ExpandableRow>
  );
}

function SamhandlerAdresseDetaljer({ idTSSEkstern }: { idTSSEkstern: string }) {
  const { data, isPending, isError, error } = useQuery(hentSamhandlerAdresse(idTSSEkstern));

  if (isPending) {
    return (
      <VStack gap="space-8">
        <Skeleton height={24} variant="rectangle" width="80%" />
        <Skeleton height={24} variant="rectangle" width="60%" />
        <Skeleton height={24} variant="rectangle" width="60%" />
      </VStack>
    );
  }

  if (isError) {
    return <ApiError error={error} title="Fant ikke samhandleradresse" />;
  }

  return <AdresseDetaljer adresse={data} />;
}

function AdresseDetaljer({ adresse }: { adresse: Adresse }) {
  const rows: { label: string; value: Nullable<string> }[] = [
    { label: "Navn", value: adresse.navn },
    { label: "Adresselinje 1", value: adresse.linje1 },
    { label: "Adresselinje 2", value: adresse.linje2 },
    { label: "Postnummer", value: adresse.postnr },
    { label: "Poststed", value: adresse.poststed },
    { label: "Land", value: adresse.land },
  ];

  return (
    <Table size="small">
      <Table.Body>
        {rows.map(
          (row) =>
            row.value && (
              <Table.Row key={row.label}>
                <Table.HeaderCell scope="row">{row.label}</Table.HeaderCell>
                <Table.DataCell>{humanizeName(row.value)}</Table.DataCell>
              </Table.Row>
            ),
        )}
      </Table.Body>
    </Table>
  );
}
