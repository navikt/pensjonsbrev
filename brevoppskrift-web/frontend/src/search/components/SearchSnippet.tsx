import { css } from "@emotion/react";
import { BodyShort, Box, Detail, HStack } from "@navikt/ds-react";
import { Link } from "@tanstack/react-router";

import { languageLabel } from "~/search/components/format";
import { LineContent, truncateLine } from "~/search/components/highlight";
import { type ContentHit } from "~/search/textSearch";

const SNIPPET_CHARS = 160;
export function SearchSnippet({ hit, needle }: { hit: ContentHit; needle?: string }) {
  const { template, lineIndex, matchCount } = hit;
  const start = Math.max(0, lineIndex - 1);
  const visibleLines = template.lines.slice(start, lineIndex + 2);
  return (
    <div>
      <HStack align="baseline" gap="space-8" wrap>
        <Link
          params={{ malType: template.malType, templateId: template.id }}
          preload="intent"
          search={{ language: template.language, index: template.indexes[lineIndex] }}
          to="/template/$malType/$templateId"
        >
          <BodyShort weight="semibold">{template.title}</BodyShort>
        </Link>
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
                line={isPrimary ? line : truncateLine(line, SNIPPET_CHARS)}
                needle={isPrimary ? needle : undefined}
              />
            </BodyShort>
          );
        })}
      </Box>
    </div>
  );
}
