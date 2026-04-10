import { Bleed, BodyShort, Box, Skeleton, type SortState, Table, Tag, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { sortBy } from "lodash";
import { useRef, useState } from "react";

import { hentSamhandlerAdresse } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import { type Adresse, type Samhandler } from "~/types/apiTypes";
import { type Nullable } from "~/types/Nullable";
import { humanizeName } from "~/utils/stringUtils";
import { trackEvent } from "~/utils/umami";

type SelectionMethod = "radio-klikk" | "rad-klikk" | "tastatur";

export function SamhandlerSearchResults({
  samhandlere,
  selectedId,
  onSelectedChange,
}: {
  samhandlere: Samhandler[];
  selectedId: Nullable<string>;
  onSelectedChange: (idTSSEkstern: string) => void;
}) {
  const [sort, setSort] = useState<SortState>({
    orderBy: "navn",
    direction: "ascending",
  });

  const handleSort = (sortKey: string | undefined) => {
    if (sortKey) {
      setSort({
        orderBy: sortKey,
        direction: sort.orderBy === sortKey && sort.direction === "ascending" ? "descending" : "ascending",
      });
    }
  };

  const sortedAscending = sort.orderBy ? sortBy(samhandlere, sort.orderBy) : samhandlere;
  const sortedSamhandlere = sort.direction === "descending" ? [...sortedAscending].reverse() : sortedAscending;

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
            <Table onSortChange={handleSort} size="small" sort={sort}>
              <Table.Header css={{ position: "sticky" }}>
                <Table.Row>
                  <Table.HeaderCell />
                  <Table.ColumnHeader
                    css={{
                      '&[aria-sort="ascending"] button, &[aria-sort="descending"] button': {
                        backgroundColor: "transparent",
                        color: "var(--ax-text-subtle)",
                      },
                    }}
                    sortable
                    sortKey="navn"
                  >
                    Navn
                  </Table.ColumnHeader>
                  <Table.HeaderCell />
                  <Table.HeaderCell />
                </Table.Row>
              </Table.Header>
              <Table.Body>
                {sortedSamhandlere.map((samhandler) => (
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
  const clickedByMouse = useRef(false);

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
        }}
      >
        <span className="aksel-radio aksel-radio--small">
          <input
            aria-label={humanizeName(samhandler.navn)}
            checked={selected}
            className="aksel-radio__input"
            data-cy="velg-samhandler"
            name="samhandler-valg"
            onChange={() => {
              selectWithTracking(clickedByMouse.current ? "radio-klikk" : "tastatur");
              clickedByMouse.current = false;
            }}
            onMouseDown={() => {
              clickedByMouse.current = true;
            }}
            type="radio"
            value={samhandler.idTSSEkstern}
          />
        </span>
      </Table.DataCell>
      <Table.DataCell
        onClick={() => {
          if (!selected) selectWithTracking("rad-klikk");
          if (!open) setOpen(true);
        }}
      >
        {humanizeName(samhandler.navn)}
      </Table.DataCell>
      <Table.DataCell
        onClick={() => {
          if (!selected) selectWithTracking("rad-klikk");
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
