import { css } from "@emotion/react";
import { Detail, HStack, VStack } from "@navikt/ds-react";
import { Link } from "@tanstack/react-router";
import { useMemo } from "react";

import { type MalType } from "~/api/brevbaker-api-endpoints";
import { languageLabel } from "~/search/components/format";
import { LineContent } from "~/search/components/highlight";
import { type SnippetResult } from "~/search/textSearch";

export function BrevResultList({
  results,
  getTitle,
}: {
  results: SnippetResult[];
  getTitle: (malType: MalType, id: string) => string;
}) {
  const rows = useMemo(() => {
    const byTemplate = new Map<string, { result: SnippetResult; languages: string[] }>();
    for (const result of results) {
      const key = `${result.malType}/${result.id}`;
      const existing = byTemplate.get(key);
      if (existing) {
        if (!existing.languages.includes(result.language)) {
          existing.languages.push(result.language);
        }
      } else {
        byTemplate.set(key, { result, languages: [result.language] });
      }
    }
    return [...byTemplate.values()];
  }, [results]);

  return (
    <VStack gap="space-8">
      {rows.map(({ result, languages }) => (
        <HStack align="baseline" gap="space-8" key={`${result.malType}/${result.id}`} wrap>
          <Link
            css={css`
              mark {
                background: transparent;
                color: inherit;
                font-weight: var(--ax-font-weight-bold);
              }
            `}
            params={{ malType: result.malType, templateId: result.id }}
            preload="intent"
            search={{ language: result.language }}
            to="/template/$malType/$templateId"
          >
            <LineContent
              line={[{ kind: "text", value: getTitle(result.malType, result.id) }]}
              needle={result.metaNeedle}
            />
          </Link>
          <Detail textColor="subtle">
            {result.id} · {languages.map(languageLabel).join(", ")}
          </Detail>
        </HStack>
      ))}
    </VStack>
  );
}
