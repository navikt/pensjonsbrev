import { css } from "@emotion/react";
import { BodyShort, Detail, HStack } from "@navikt/ds-react";
import { Link } from "@tanstack/react-router";

import { languageLabel } from "~/search/components/format";
import { clampLine, HighlightedLine } from "~/search/components/highlight";
import { type SnippetResult } from "~/search/textSearch";

const SNIPPET_CHARS = 160;

export function SearchSnippet({ result, title }: { result: SnippetResult; title: string }) {
  const primaryIndex = result.highlightLineIndex;
  const start = primaryIndex === undefined ? 0 : Math.max(0, primaryIndex - 1);
  const end =
    primaryIndex === undefined ? Math.min(result.lines.length, 3) : Math.min(result.lines.length, primaryIndex + 2);
  const visibleLines = result.lines.slice(start, end);

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
          params={{ malType: result.malType, templateId: result.id }}
          preload="intent"
          search={{ language: result.language, q: result.anchorQuery, qi: result.matchOrdinal }}
          to="/template/$malType/$templateId"
        >
          {title}
        </Link>
        <Detail textColor="subtle">
          {result.id} · {languageLabel(result.language)} · {result.templateMatchCount} treff i malen
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
          const lineIndex = start + i;
          const isPrimary = lineIndex === primaryIndex;
          const highlight = isPrimary ? result.highlightRanges : [];
          const clamped = clampLine(line, null, SNIPPET_CHARS, highlight);
          return (
            <BodyShort
              css={css`
                white-space: normal;
                word-break: break-word;
              `}
              key={lineIndex}
              size="small"
              textColor={isPrimary ? "default" : "subtle"}
            >
              <HighlightedLine line={clamped.line} ranges={clamped.ranges} />
            </BodyShort>
          );
        })}
      </div>
    </div>
  );
}
