import { css } from "@emotion/react";
import { BodyShort, Box, Detail, HStack } from "@navikt/ds-react";

import { languageLabel } from "~/search/components/format";
import { LineContent, truncateLine } from "~/search/components/highlight";
import { ResultLink, selectedRowCss } from "~/search/components/ResultLink";
import { type ResultRef, resultRefOf, type SearchCriteria } from "~/search/searchContext";
import { type ContentHit } from "~/search/textSearch";

const SNIPPET_CHARS = 160;
export function SearchSnippet({
  hit,
  criteria,
  selected = false,
  onOpen,
}: {
  hit: ContentHit;
  /** The search these hits came from. Its query is also the needle to mark. */
  criteria: SearchCriteria;
  selected?: boolean;
  onOpen?: (result: ResultRef) => Promise<void>;
}) {
  const { template, lineIndex, matchCount } = hit;
  const start = Math.max(0, lineIndex - 1);
  const visibleLines = template.lines.slice(start, lineIndex + 2);
  return (
    <div css={selectedRowCss} data-selected-result={selected || undefined}>
      <HStack align="baseline" gap="space-8" wrap>
        <ResultLink
          block={template.indexes[lineIndex]}
          criteria={criteria}
          current={selected}
          onOpen={onOpen}
          result={resultRefOf(template)}
        >
          <BodyShort weight="semibold">{template.title}</BodyShort>
        </ResultLink>
        <Detail textColor="subtle">
          {template.id} · {languageLabel(template.language)} · {matchCount} treff i malen
        </Detail>
      </HStack>
      <Box marginBlock="space-2 space-0">
        {visibleLines.map((line, i) => {
          const absoluteIndex = start + i;
          const isPrimary = absoluteIndex === lineIndex;
          return (
            <BodyShort
              css={css`
                overflow-wrap: anywhere;
                ${
                  !isPrimary &&
                  css`
                  margin: var(--ax-space-2) 0;
                `
                }
              `}
              key={absoluteIndex}
              size="small"
              textColor={isPrimary ? "default" : "subtle"}
              truncate
            >
              <LineContent
                exact={criteria.exact}
                line={isPrimary ? line : truncateLine(line, SNIPPET_CHARS)}
                needle={isPrimary ? criteria.q : undefined}
              />
            </BodyShort>
          );
        })}
      </Box>
    </div>
  );
}
