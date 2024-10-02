import { css } from "@emotion/react";
import type { SortState } from "@navikt/ds-react";
import { BodyShort, Button, Table, VStack } from "@navikt/ds-react";
import { sortBy } from "lodash";
import { useState } from "react";

import type { Samhandler } from "~/types/apiTypes";
import { humanizeName } from "~/utils/stringUtils";

export function SamhandlerSearchResults({
  samhandlere,
  onSelect,
}: {
  samhandlere: Samhandler[];
  onSelect: (idTSSEkstern: string) => void;
}) {
  const [sort, setSort] = useState<SortState | undefined>({
    orderBy: "navn",
    direction: "ascending",
  });

  const handleSort = (sortKey: string | undefined) => {
    if (sortKey) {
      setSort({
        orderBy: sortKey,
        direction: sort && sortKey === sort.orderBy && sort.direction === "ascending" ? "descending" : "ascending",
      });
    } else {
      // eslint-disable-next-line unicorn/no-useless-undefined
      setSort(undefined);
    }
  };

  const sortedSamhandlereAscending = sort?.orderBy ? sortBy(samhandlere, sort.orderBy) : samhandlere;
  const reversed = sort?.direction === "descending";

  const sortedSamhandlere = reversed ? sortedSamhandlereAscending.reverse() : sortedSamhandlereAscending;

  return (
    <VStack gap="2">
      <BodyShort size="small">{sortedSamhandlere.length} treff</BodyShort>
      {sortedSamhandlere.length > 0 && (
        <Table onSortChange={(sortKey) => handleSort(sortKey)} sort={sort}>
          <Table.Header>
            <Table.Row>
              <Table.ColumnHeader colSpan={2} sortKey="navn" sortable>
                Navn
              </Table.ColumnHeader>
            </Table.Row>
          </Table.Header>
          <Table.Body>
            {sortedSamhandlere.map((samhandler) => (
              <Table.Row key={samhandler.idTSSEkstern}>
                <Table.DataCell
                  css={css`
                    font-weight: var(--a-font-weight-regular);
                  `}
                  scope="row"
                >
                  {humanizeName(samhandler.navn)}
                </Table.DataCell>
                <Table.DataCell align="right">
                  <Button
                    data-cy="velg-samhandler"
                    onClick={() => onSelect(samhandler.idTSSEkstern)}
                    size="small"
                    type="button"
                    variant="secondary-neutral"
                  >
                    Velg
                  </Button>
                </Table.DataCell>
              </Table.Row>
            ))}
          </Table.Body>
        </Table>
      )}
    </VStack>
  );
}
