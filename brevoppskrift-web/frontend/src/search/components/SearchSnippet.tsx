import { css } from "@emotion/react";
import { BodyShort, Detail, HStack } from "@navikt/ds-react";
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
    <div
      css={css`
        padding: var(--ax-space-4) 0;
      `}
    >
      <HStack align="baseline" gap="space-8" wrap>
        <Link
          css={css`
            font-weight: var(--ax-font-weight-bold);
          `}
          params={{ malType: template.malType, templateId: template.id }}
          preload="intent"
          search={{ language: template.language, bid: template.blockIds[lineIndex] }}
          to="/template/$malType/$templateId"
        >
          {template.title}
        </Link>
        <Detail textColor="subtle">
          {template.id} · {languageLabel(template.language)} · {matchCount} treff i malen
        </Detail>
      </HStack>
      <div
        css={css`
          margin-top: var(--ax-space-2);
          mark {
            background: transparent;
            color: inherit;
            font-weight: var(--ax-font-weight-bold);
          }
        `}
      >
        {visibleLines.map((line, i) => {
          const absoluteIndex = start + i;
          const isPrimary = absoluteIndex === lineIndex;
          return (
            <BodyShort
              css={css`
                white-space: normal;
                word-break: break-word;
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
            >
              <LineContent
                line={isPrimary ? line : truncateLine(line, SNIPPET_CHARS)}
                needle={isPrimary ? needle : undefined}
              />
            </BodyShort>
          );
        })}
      </div>
    </div>
  );
}
