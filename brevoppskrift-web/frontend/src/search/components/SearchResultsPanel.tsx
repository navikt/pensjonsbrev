import { css } from "@emotion/react";
import { Box, Detail, ErrorMessage, HStack, Pagination, VStack } from "@navikt/ds-react";
import { type ReactNode } from "react";

export const CONTENT_PAGE_SIZE = 10;
export const LETTER_PAGE_SIZE = 100;

export function SearchResultsPanel({
  page,
  pageCount,
  setPage,
  summary,
  error,
  isPending,
  children,
}: {
  page: number;
  pageCount: number;
  setPage: (page: number) => void;
  /** One line describing the search on screen, directly above the results.
   *  Callers keep it unchanged while the next search runs, so it is announced
   *  once per completed search rather than on every burst of typing. */
  summary?: ReactNode;
  /** Shown in place of the summary when the search failed. */
  error?: ReactNode;
  /** Marks the results as stale for assistive technology while a newer query is
   *  still running. Deliberately has no visual effect: the results stay fully
   *  opaque and readable, and the spinner next to the search field carries the
   *  visual signal instead. Dimming the text here would drop it below the WCAG
   *  1.4.3 contrast floor. */
  isPending?: boolean;
  children: ReactNode;
}) {
  const status = error ? (
    <ErrorMessage showIcon size="small">
      {error}
    </ErrorMessage>
  ) : summary ? (
    <Detail textColor="subtle">{summary}</Detail>
  ) : null;
  return (
    <VStack height="100%" paddingBlock="space-16 space-0">
      {/* The live region stays mounted even when empty: screen readers only
          reliably announce changes to a region that already exists. */}
      <Box aria-live="polite" paddingInline="space-16">
        {status ? <Box paddingBlock="space-0 space-4">{status}</Box> : null}
      </Box>
      <VStack
        aria-busy={isPending}
        flexGrow="1"
        gap="space-8"
        overflow="auto"
        paddingBlock="space-12 space-16"
        paddingInline="space-16"
      >
        {children}
      </VStack>
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
    </VStack>
  );
}
