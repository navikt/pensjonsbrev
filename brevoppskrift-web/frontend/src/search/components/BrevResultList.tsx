import { Detail, HStack, VStack } from "@navikt/ds-react";
import { useMemo } from "react";

import { languageLabel } from "~/search/components/format";
import { LineContent } from "~/search/components/highlight";
import { ResultLink, selectedRowCss } from "~/search/components/ResultLink";
import { isSelectedResult } from "~/search/resultNavigation";
import { type ResultRef, resultRefOf, type SearchCriteria } from "~/search/searchContext";
import { type BrevHit, type TemplateText } from "~/search/textSearch";

/** Title hits, one row per template with all its matching languages. */
export function BrevResultList({
  hits,
  criteria,
  selected,
  onOpen,
}: {
  hits: BrevHit[];
  /** The search these hits came from. Its query is also the needle to mark. */
  criteria: SearchCriteria;
  /** The result last opened; any language of its template marks the row. */
  selected?: ResultRef;
  onOpen?: (result: ResultRef) => Promise<void>;
}) {
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
      {rows.map(({ template, languages }) => {
        const result = resultRefOf(template);
        const isSelected = isSelectedResult(result, selected, true);
        return (
          <HStack
            align="baseline"
            css={selectedRowCss}
            data-selected-result={isSelected || undefined}
            gap="space-8"
            key={`${template.malType}/${template.id}`}
            wrap
          >
            <ResultLink criteria={criteria} current={isSelected} onOpen={onOpen} result={result}>
              <LineContent
                exact={criteria.exact}
                line={[{ type: "text", value: template.title }]}
                needle={criteria.q}
              />
            </ResultLink>
            <Detail textColor="subtle">
              {template.id} · {languages.map(languageLabel).join(", ")}
            </Detail>
          </HStack>
        );
      })}
    </VStack>
  );
}
