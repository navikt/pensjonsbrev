import { css } from "@emotion/react";
import { Box, HStack, Pagination, VStack } from "@navikt/ds-react";
import { type ReactNode } from "react";

export const CONTENT_PAGE_SIZE = 10;
export const LETTER_PAGE_SIZE = 100;

export function SearchResultsPanel({
  page,
  pageCount,
  setPage,
  status,
  isPending,
  children,
}: {
  page: number;
  pageCount: number;
  setPage: (page: number) => void;
  /** The search status line, rendered directly above the results. Owns its own
   *  typography so it can pair the message with a spinner or checkmark. */
  status?: ReactNode;
  /** Marks the results as stale for assistive technology while a newer query is
   *  still running. Deliberately has no visual effect: the results stay fully
   *  opaque and readable, and the spinner in the status line carries the visual
   *  signal instead. Dimming the text here would drop it below the WCAG 1.4.3
   *  contrast floor. */
  isPending?: boolean;
  children: ReactNode;
}) {
  return (
    <VStack height="100%" paddingBlock="space-16 space-0">
      {status ? (
        <Box paddingBlock="space-0 space-4" paddingInline="space-16">
          {status}
        </Box>
      ) : null}
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
      {/* </div> */}
    </VStack>
  );
}
