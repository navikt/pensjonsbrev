import { css } from "@emotion/react";
import { Detail, HStack, Pagination, VStack } from "@navikt/ds-react";
import { type ReactNode } from "react";

/** Results per page. Sized to roughly fill a normal laptop/desktop screen so a full
 * page needs little or no scrolling, while keeping pagination simple (no viewport
 * measuring). */
export const PAGE_SIZE = 10;

/** A search results tab: an optional summary line, the results list, and a centered
 * pagination control shown only when there is more than one page. */
export function SearchResultsPanel({
  page,
  pageCount,
  setPage,
  summary,
  children,
}: {
  page: number;
  pageCount: number;
  setPage: (page: number) => void;
  summary?: ReactNode;
  children: ReactNode;
}) {
  return (
    <VStack
      css={css`
        margin-top: var(--ax-space-16);
      `}
      gap="space-12"
    >
      {summary ? (
        <Detail aria-live="polite" textColor="subtle">
          {summary}
        </Detail>
      ) : null}
      <div
        css={css`
          display: flex;
          flex-direction: column;
          gap: var(--ax-space-12);
        `}
      >
        {children}
      </div>
      {pageCount > 1 ? (
        <HStack justify="center">
          <Pagination count={pageCount} onPageChange={setPage} page={page} size="small" />
        </HStack>
      ) : null}
    </VStack>
  );
}
