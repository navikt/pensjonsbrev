import { css } from "@emotion/react";
import { Detail, HStack, VStack } from "@navikt/ds-react";
import { Link } from "@tanstack/react-router";
import { useMemo } from "react";

import { languageLabel } from "~/search/components/format";
import { LineContent } from "~/search/components/highlight";
import { type BrevHit, type TemplateText } from "~/search/textSearch";
export function BrevResultList({ hits, needle }: { hits: BrevHit[]; needle?: string }) {
  const rows = useMemo(() => {
    const byTemplate = new Map<string, { template: TemplateText; languages: string[] }>();
    for (const { template } of hits) {
      const key = `${template.malType}/${template.id}`;
      const existing = byTemplate.get(key);
      if (existing) {
        if (!existing.languages.includes(template.language)) {
          existing.languages.push(template.language);
        }
      } else {
        byTemplate.set(key, { template, languages: [template.language] });
      }
    }
    return [...byTemplate.values()];
  }, [hits]);
  return (
    <VStack gap="space-8">
      {rows.map(({ template, languages }) => (
        <HStack align="baseline" gap="space-8" key={`${template.malType}/${template.id}`} wrap>
          <Link
            css={css`
              mark {
                background: transparent;
                color: inherit;
                font-weight: var(--ax-font-weight-bold);
              }
            `}
            params={{ malType: template.malType, templateId: template.id }}
            preload="intent"
            search={{ language: template.language }}
            to="/template/$malType/$templateId"
          >
            <LineContent line={[{ type: "text", value: template.title }]} needle={needle} />
          </Link>
          <Detail textColor="subtle">
            {template.id} · {languages.map(languageLabel).join(", ")}
          </Detail>
        </HStack>
      ))}
    </VStack>
  );
}
