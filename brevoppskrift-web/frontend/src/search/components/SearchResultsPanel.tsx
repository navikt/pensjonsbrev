import { css } from "@emotion/react";
import { Box, Detail, HStack, Pagination, VStack } from "@navikt/ds-react";
import { type ReactNode } from "react";

export const CONTENT_PAGE_SIZE = 10;
export const LETTER_PAGE_SIZE = 100;

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
    <VStack height="100%" paddingBlock="space-16 space-0">
      {summary ? (
        <Box asChild paddingBlock="space-0 space-4" paddingInline="space-16">
          <Detail aria-live="polite" textColor="subtle">
            {summary}
          </Detail>
        </Box>
      ) : null}
      <VStack flexGrow="1" gap="space-8" overflow="auto" paddingBlock="space-12 space-16" paddingInline="space-16">
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
