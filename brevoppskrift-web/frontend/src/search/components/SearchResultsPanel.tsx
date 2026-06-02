import { css } from "@emotion/react";
import { Detail, HStack, Pagination } from "@navikt/ds-react";
import { type ReactNode } from "react";

/** Results per page. Sized to roughly fill a normal laptop/desktop screen so a full
 * page needs little or no scrolling, while keeping pagination simple (no viewport
 * measuring). */
export const PAGE_SIZE = 10;

/** A search results tab: an optional summary line, the results list, and a centered
 * pagination control pinned to the bottom of the panel. */
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
    <div
      css={css`
        display: flex;
        flex-direction: column;
        margin-top: var(--ax-space-16);
        height: 70vh;
      `}
    >
      {summary ? (
        <Detail aria-live="polite" css={css`margin-bottom: var(--ax-space-12); flex-shrink: 0;`} textColor="subtle">
          {summary}
        </Detail>
      ) : null}
      <div
        css={css`
          flex: 1;
          min-height: 0;
          overflow-y: auto;
          display: flex;
          flex-direction: column;
          gap: var(--ax-space-12);
        `}
      >
        {children}
      </div>
      {pageCount > 1 ? (
        <HStack
          css={css`
            flex-shrink: 0;
            padding: var(--ax-space-12) 0;
            border-top: 1px solid var(--ax-border-subtle);
          `}
          justify="center"
        >
          <Pagination count={pageCount} onPageChange={setPage} page={page} size="small" />
        </HStack>
      ) : null}
    </div>
  );
}
