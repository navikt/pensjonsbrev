import { css } from "@emotion/react";
import { Heading, HGrid, HStack, Spacer, VStack } from "@navikt/ds-react";
import { createFileRoute, notFound, redirect, useNavigate } from "@tanstack/react-router";
import { useMemo, useRef, useState } from "react";

import { getTemplateDescription, getTemplateDocumentation, type MalType } from "~/api/brevbaker-api-endpoints";
import { type TemplateDocumentation } from "~/api/templateDocumentationTypes";
import { DataClassesPanel } from "~/components/DataClasses";
import { ExportTemplateButton } from "~/components/ExportTemplateButton";
import { LanguageToggle } from "~/components/LanguageToggle";
import { Document, KnownDataClassesProvider } from "~/components/TemplateDocumentationView";
import {
  criteriaOf,
  type ResultRef,
  type TemplateSearchContextParams,
  validateTemplateSearchContext,
} from "~/search/searchContext";
import { type SearchHighlight, SearchHighlightProvider } from "~/templateSearch/SearchHighlightContext";
import { BackButton, SearchHitToolbar } from "~/templateSearch/SearchHitToolbar";
import { useScrollToBlock } from "~/templateSearch/useScrollToBlock";
import { useTemplateHits } from "~/templateSearch/useTemplateHits";

type TemplateSearchParams = TemplateSearchContextParams & {
  language?: string;
  highlightedDataClass?: string;
  highlightedDataField?: string;
  highlightedDataFieldOwner?: string;
  /** The block to open at, a matching block when opened from a search. */
  index?: number;
};

export const Route = createFileRoute("/template/$malType/$templateId")({
  loaderDeps: ({ search: { language } }) => ({ language }),
  params: {
    parse: (raw: Record<string, string>) => ({
      templateId: raw.templateId,
      malType: raw.malType as MalType,
    }),
  },
  loader: async ({ context, deps, params, preload }) => {
    const description = await context.queryClient.ensureQueryData({
      queryKey: getTemplateDescription.queryKey(params.malType, params.templateId),
      queryFn: () => getTemplateDescription.queryFn(params.malType, params.templateId),
    });

    const defaultLanguage = description.languages[0];
    if (!defaultLanguage) {
      throw notFound();
    }

    if (!deps.language && !preload) {
      // Keeps the rest of the search params, so a search context survives.
      throw redirect({
        to: "/template/$malType/$templateId",
        search: (prev) => ({ ...prev, language: defaultLanguage }),
        params,
      });
    }

    const language = deps.language ?? defaultLanguage;

    const documentation = await context.queryClient.ensureQueryData({
      queryKey: getTemplateDocumentation.queryKey(params.malType, params.templateId, language),
      queryFn: () => getTemplateDocumentation.queryFn(params.malType, params.templateId, language),
    });

    return { documentation, description };
  },
  validateSearch: (search: Record<string, unknown>): TemplateSearchParams => {
    const indexRaw = search.index?.toString();
    const index = indexRaw !== undefined && /^\d+$/.test(indexRaw) ? Number(indexRaw) : undefined;
    return {
      language: search.language?.toString(),
      highlightedDataClass: search.highlightedDataClass?.toString(),
      highlightedDataField: search.highlightedDataField?.toString(),
      highlightedDataFieldOwner: search.highlightedDataFieldOwner?.toString(),
      index,
      ...validateTemplateSearchContext(search),
    };
  },
  component: TemplateExplorer,
});

function TemplateExplorer() {
  const { documentation, description } = Route.useLoaderData();
  const { malType, templateId } = Route.useParams();
  const { index, language: languageParam, q, exact, tab } = Route.useSearch();
  const navigate = useNavigate({ from: Route.fullPath });
  const previewRef = useRef<HTMLDivElement>(null);
  const language = languageParam ?? description.languages[0];

  const criteria = useMemo(() => criteriaOf({ q, exact, tab }), [q, exact, tab]);
  const current = useMemo<ResultRef>(() => ({ malType, id: templateId, language }), [malType, templateId, language]);
  const hits = useTemplateHits(criteria, current, index);
  // Local on purpose: every visit, refresh and shared link starts with marking
  // on. Kept while moving between results, since the route stays mounted.
  const [marking, setMarking] = useState(true);

  const highlight = useMemo<SearchHighlight | undefined>(
    () =>
      marking && hits?.status === "ready"
        ? {
            needle: hits.highlight.needle,
            exact: hits.highlight.exactOnly,
            matchingBlocks: new Set(hits.matchingBlocks),
            activeBlock: hits.activeBlock,
          }
        : undefined,
    [marking, hits],
  );
  // Without a search, a plain `index` link still scrolls to its block.
  const scrollTarget = criteria ? (hits?.status === "ready" ? hits.activeBlock : undefined) : index;
  useScrollToBlock(previewRef, scrollTarget, documentation);

  return (
    <VStack height="100%" overflow="hidden">
      <HStack align="center" flexShrink="0" gap="space-16" marginBlock="space-8" marginInline="space-16" wrap>
        <BackButton criteria={criteria} current={current} />
        {criteria && hits ? (
          <SearchHitToolbar
            criteria={criteria}
            hits={hits}
            marking={marking}
            onMarkingChange={setMarking}
            onSelectBlock={(block) => navigate({ replace: true, search: (s) => ({ ...s, index: block }) })}
          />
        ) : null}
        <LanguageToggle
          available={description.languages}
          onChange={(value) => navigate({ replace: true, search: (s) => ({ ...s, language: value }) })}
          value={language}
        />
        <Heading level="1" size="small">
          {templateId}
        </Heading>
        <Spacer />
        <ExportTemplateButton documentation={documentation} language={language} templateId={templateId} />
      </HStack>
      <HGrid columns="auto 1fr" flexGrow="1" minHeight="0" overflow="hidden">
        <DataClassesPanel templateModelSpecification={documentation.templateModelSpecification} />
        <VStack
          align="center"
          gap="space-16"
          overflow="auto"
          paddingBlock="space-16"
          paddingInline="space-16"
          width="100%"
        >
          <div
            css={css`
              width: 100%;

              [data-search-hit] {
                border-radius: var(--ax-radius-4);
                scroll-margin-top: var(--ax-space-64);
              }

              [data-search-hit="match"] {
                outline: 1px solid var(--ax-border-warning-subtle);
                outline-offset: var(--ax-space-2);
              }

              [data-search-hit="active"] {
                outline: 3px solid var(--ax-border-warning);
                outline-offset: var(--ax-space-2);
              }
            `}
            ref={previewRef}
          >
            <SearchHighlightProvider highlight={highlight}>
              <TemplateDocumentationSection documentation={documentation} />
            </SearchHighlightProvider>
          </div>
        </VStack>
      </HGrid>
    </VStack>
  );
}

function TemplateDocumentationSection({ documentation }: { documentation: TemplateDocumentation }) {
  return (
    <KnownDataClassesProvider templateModelSpecification={documentation.templateModelSpecification}>
      <Document templateDocumentation={documentation} />
      {documentation.attachments.map((attachment, index) => (
        <Document key={index} templateDocumentation={attachment} />
      ))}
    </KnownDataClassesProvider>
  );
}
