import { css } from "@emotion/react";
import { Heading, HGrid, HStack, Spacer, VStack } from "@navikt/ds-react";
import { createFileRoute, notFound, redirect, useNavigate } from "@tanstack/react-router";
import axios from "axios";
import { useMemo, useRef, useState } from "react";

import {
  getTemplateDescription,
  getTemplateDocumentation,
  isMalType,
  type MalType,
} from "~/api/brevbaker-api-endpoints";
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
import { blockIndexesOf } from "~/templateSearch/blockIndexes";
import { type SearchHighlight, SearchHighlightProvider } from "~/templateSearch/SearchHighlightContext";
import { BackButton, SearchHitToolbar } from "~/templateSearch/SearchHitToolbar";
import { useScrollToBlock } from "~/templateSearch/useScrollToBlock";
import { useTemplateHits } from "~/templateSearch/useTemplateHits";

/** An unknown template answers 404, which is a missing page rather than an error. */
function notFoundOn404(error: unknown): never {
  if (axios.isAxiosError(error) && error.response?.status === 404) {
    throw notFound();
  }
  throw error;
}

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
    if (!isMalType(params.malType)) {
      throw notFound();
    }
    const description = await context.queryClient
      .ensureQueryData({
        queryKey: getTemplateDescription.queryKey(params.malType, params.templateId),
        queryFn: () => getTemplateDescription.queryFn(params.malType, params.templateId),
      })
      .catch(notFoundOn404);

    const defaultLanguage = description.languages[0];
    if (!defaultLanguage) {
      throw notFound();
    }

    // A missing or unknown language (e.g. a hand-edited URL) opens the
    // default one. Keeps the rest of the search params, so a search context survives.
    const languageIsValid = deps.language !== undefined && description.languages.includes(deps.language);
    if (!languageIsValid && !preload) {
      throw redirect({
        to: "/template/$malType/$templateId",
        search: (prev) => ({ ...prev, language: defaultLanguage }),
        params,
        replace: true,
      });
    }

    const language = languageIsValid && deps.language ? deps.language : defaultLanguage;

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
  // A block that is not in this template (e.g. a stale or hand-edited URL)
  // is ignored, so the page opens at the first hit, or at the top.
  const requestedBlock = useMemo(
    () => (index !== undefined && blockIndexesOf(documentation).has(index) ? index : undefined),
    [index, documentation],
  );
  const hits = useTemplateHits(criteria, current, requestedBlock);
  // Local on purpose: every visit, refresh and shared link starts the same way.
  // On from the Innhold tab, where the hits are the point; off from the Brev
  // tab, whose hits are on the title. Kept while moving between results, since
  // the route stays mounted.
  const [marking, setMarking] = useState(criteria?.tab !== "brev");

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
  // Without a search, a plain `index` link still scrolls to its block. With
  // one, only while the hits are shown, so turning them on scrolls there.
  const scrollTarget = criteria ? (marking && hits?.status === "ready" ? hits.activeBlock : undefined) : requestedBlock;
  useScrollToBlock(previewRef, scrollTarget, documentation);
  const selectBlock = (block: number) => navigate({ replace: true, search: (s) => ({ ...s, index: block }) });
  // Turning the hits on takes the user to the active one, or, with none
  // active (e.g. opened from the Brev tab), to the first.
  const changeMarking = (on: boolean) => {
    setMarking(on);
    if (on && hits?.status === "ready" && hits.activeBlock === undefined) {
      const [firstBlock] = hits.matchingBlocks;
      if (firstBlock !== undefined) {
        void selectBlock(firstBlock);
      }
    }
  };

  return (
    <VStack height="100%" overflow="hidden">
      <HStack align="center" flexShrink="0" gap="space-16" marginBlock="space-8" marginInline="space-16" wrap>
        <BackButton criteria={criteria} current={current} />
        <Spacer />
        {criteria && hits ? (
          <SearchHitToolbar
            criteria={criteria}
            hits={hits}
            marking={marking}
            onMarkingChange={changeMarking}
            onSelectBlock={selectBlock}
          />
        ) : null}
        <Heading level="1" size="small">
          {templateId}
        </Heading>
        <LanguageToggle
          available={description.languages}
          onChange={(value) => navigate({ replace: true, search: (s) => ({ ...s, language: value }) })}
          value={language}
        />
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
