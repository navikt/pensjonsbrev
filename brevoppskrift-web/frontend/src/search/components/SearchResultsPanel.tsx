import { css } from "@emotion/react";
import { Box, Detail, HStack, Pagination, VStack } from "@navikt/ds-react";
import { type ReactNode } from "react";

export const CONTENT_PAGE_SIZE = 10;
export const LETTER_PAGE_SIZE = 100;

/** Serialized once, at module load, and toggled through `data-pending` rather
 *  than by interpolating the opacity into the style. An interpolated value
 *  makes Emotion serialize and insert a new rule on every toggle, which
 *  invalidates style for the whole document - twice per search, while the user
 *  is typing. A single static rule keyed off an attribute costs one selector
 *  match on one element instead. */
const resultsList = css`
  opacity: 1;
  transition: opacity 0.15s ease-out;

  &[data-pending="true"] {
    opacity: 0.5;
  }

  @media (prefers-reduced-motion: reduce) {
    transition: none;
  }
`;

export function SearchResultsPanel({
  page,
  pageCount,
  setPage,
  summary,
  isPending,
  children,
}: {
  page: number;
  pageCount: number;
  setPage: (page: number) => void;
  summary?: ReactNode;
  /** Dims the results while a newer query is still being searched for. The
   *  previous query's results stay visible and readable in the meantime. */
  isPending?: boolean;
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
      <VStack
        aria-busy={isPending}
        css={resultsList}
        data-pending={isPending ? "true" : "false"}
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
