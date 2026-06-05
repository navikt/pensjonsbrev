import { css } from "@emotion/react";
import { BodyShort, Detail, Heading, Loader, Search, Tabs, Tag, VStack } from "@navikt/ds-react";
import { type QueryClient } from "@tanstack/react-query";
import { createFileRoute, Link } from "@tanstack/react-router";
import { useEffect, useMemo, useState } from "react";

import { getBrevkoderMedMetadata, getTemplateDescription, type MalType } from "~/api/brevbaker-api-endpoints";
import { type TemplateDescription } from "~/api/brevbakerTypes";
import {
  BrevResultList,
  MIN_QUERY_LENGTH,
  PAGE_SIZE,
  SearchResultsPanel,
  SearchSnippet,
  type TemplateRef,
  useTemplateSearch,
} from "~/search";

function displayTitleOf(description: { name: string; metadata?: { displayTitle?: string } | null }): string {
  const title = description.metadata?.displayTitle;
  return title?.trim() ? title : description.name;
}
export const Route = createFileRoute("/templates")({
  loader: async ({ context }) => {
    const [autobrev, redigerbar] = await Promise.all([
      fetchAllDescriptions(context.queryClient, "autobrev"),
      fetchAllDescriptions(context.queryClient, "redigerbar"),
    ]);
    return { autobrev, redigerbar };
  },
  component: AllTemplates,
});
async function fetchAllDescriptions(queryClient: QueryClient, malType: MalType): Promise<TemplateDescription[]> {
  const descriptions = await queryClient.ensureQueryData({
    queryKey: getBrevkoderMedMetadata.queryKey(malType),
    queryFn: () => getBrevkoderMedMetadata.queryFn(malType),
  });
  if (!Array.isArray(descriptions)) {
    throw new TypeError(
      `Forventet en liste med brevkoder for "${malType}", men fikk noe annet. Går kallene til brevbaker-APIet (f.eks. via BFF eller Vite-proxy)?`,
    );
  }
  // Seed the per-template description cache so the detail route loads instantly.
  for (const description of descriptions) {
    queryClient.setQueryData(getTemplateDescription.queryKey(malType, description.name), description);
  }
  return descriptions;
}
function TemplateList({ templates, malType }: { templates: TemplateDescription[]; malType: MalType }) {
  if (templates.length === 0) {
    return <span>Ingen treff</span>;
  }
  return (
    <VStack gap="space-8">
      {templates.map((description) => (
        <Link
          key={description.name}
          params={{ malType, templateId: description.name }}
          preload="intent"
          to="/template/$malType/$templateId"
        >
          {displayTitleOf(description)}
        </Link>
      ))}
    </VStack>
  );
}
function TabLabel({ label, count, isSearching }: { label: string; count: number; isSearching: boolean }) {
  return (
    <span
      css={css`
        display: inline-flex;
        align-items: center;
        gap: var(--ax-space-8);
      `}
    >
      {label}
      {isSearching ? (
        <Tag data-color="neutral" size="xsmall" variant="moderate">
          {count}
        </Tag>
      ) : null}
    </span>
  );
}
function toRefs(templates: TemplateDescription[], malType: MalType): TemplateRef[] {
  return templates.map((description) => ({
    malType,
    brevkode: description.name,
    title: displayTitleOf(description),
    languages: description.languages,
  }));
}
function AllTemplates() {
  const { autobrev, redigerbar } = Route.useLoaderData();
  const refs = useMemo<TemplateRef[]>(
    () => [...toRefs(autobrev, "autobrev"), ...toRefs(redigerbar, "redigerbar")],
    [autobrev, redigerbar],
  );
  const {
    query,
    setQuery,
    isSearching,
    isLoading,
    failedCount,
    contentHits,
    brevHits,
    contentTemplateCount,
    contentLineCount,
    brevTemplateCount,
    templateTotal,
    languageTotal,
  } = useTemplateSearch(refs);
  const [activeTab, setActiveTab] = useState<"innhold" | "brev">("innhold");
  const [page, setPage] = useState(1);
  // biome-ignore lint/correctness/useExhaustiveDependencies: reset pagination whenever the query or tab changes.
  useEffect(() => setPage(1), [query, activeTab]);
  const activeHits = activeTab === "innhold" ? contentHits : brevHits;
  const pageCount = Math.max(1, Math.ceil(activeHits.length / PAGE_SIZE));
  const safePage = Math.min(page, pageCount);
  const pageStart = (safePage - 1) * PAGE_SIZE;
  const contentItems = contentHits.slice(pageStart, pageStart + PAGE_SIZE);
  const brevItems = brevHits.slice(pageStart, pageStart + PAGE_SIZE);
  return (
    <div
      css={css`
        flex: 1;
        margin: 0 -24px;
        padding: var(--ax-space-24) 24px var(--ax-space-32);
        background: var(--ax-bg-default);
      `}
    >
      <Heading level="1" size="small">
        Brevoppskrift
      </Heading>
      <div
        css={css`
          display: flex;
          gap: var(--ax-space-12);
          align-items: center;
          margin-top: var(--ax-space-16);
        `}
      >
        <Search
          css={css`
            width: 100%;
            max-width: 480px;
          `}
          hideLabel={false}
          label="Søk i innholdet i alle maler"
          onChange={setQuery}
          onClear={() => setQuery("")}
          size="small"
          value={query}
          variant="simple"
        />
        {isLoading ? <Loader size="small" title="Indekserer innhold…" /> : null}
      </div>
      <Detail
        aria-live="polite"
        css={css`
          margin-top: var(--ax-space-8);
        `}
        textColor="subtle"
      >
        {isLoading
          ? "Indekserer innhold …"
          : `Søker i innholdet til ${templateTotal} maler på ${languageTotal} språk${
              failedCount > 0 ? ` (${failedCount} feilet)` : ""
            }`}
      </Detail>
      <Tabs
        css={css`
          margin-top: var(--ax-space-16);
        `}
        onChange={(tab) => setActiveTab(tab === "brev" ? "brev" : "innhold")}
        value={activeTab}
      >
        <Tabs.List>
          <Tabs.Tab
            label={<TabLabel count={contentTemplateCount} isSearching={isSearching} label="Innhold" />}
            value="innhold"
          />
          <Tabs.Tab
            label={<TabLabel count={brevTemplateCount} isSearching={isSearching} label="Brev" />}
            value="brev"
          />
        </Tabs.List>
        <Tabs.Panel value="innhold">
          <SearchResultsPanel
            page={safePage}
            pageCount={Math.max(1, Math.ceil(contentHits.length / PAGE_SIZE))}
            setPage={setPage}
            summary={
              isSearching && contentHits.length > 0 ? (
                <>
                  Frasen du søker på er brukt i <b>{contentTemplateCount} maler</b> i {contentLineCount} avsnitt
                </>
              ) : undefined
            }
          >
            {!isSearching ? (
              <BodyShort textColor="subtle">
                Skriv minst {MIN_QUERY_LENGTH} tegn for å søke i innholdet i malene.
              </BodyShort>
            ) : contentHits.length === 0 ? (
              <BodyShort>Ingen treff i innholdet</BodyShort>
            ) : (
              contentItems.map((hit) => (
                <SearchSnippet
                  hit={hit}
                  key={`${hit.template.malType}/${hit.template.id}/${hit.template.language}`}
                  needle={query}
                />
              ))
            )}
          </SearchResultsPanel>
        </Tabs.Panel>
        <Tabs.Panel value="brev">
          <SearchResultsPanel
            page={safePage}
            pageCount={Math.max(1, Math.ceil(brevHits.length / PAGE_SIZE))}
            setPage={setPage}
            summary={
              isSearching && brevHits.length > 0 ? (
                <>
                  Søket traff tittel, navn eller brevkode i <b>{brevTemplateCount} maler</b>
                </>
              ) : undefined
            }
          >
            {isSearching ? (
              brevHits.length === 0 ? (
                <BodyShort>Ingen treff i tittel, navn eller brevkode</BodyShort>
              ) : (
                <BrevResultList hits={brevItems} needle={query} />
              )
            ) : (
              <VStack gap="space-20">
                <VStack gap="space-8">
                  <Heading level="2" size="xsmall">
                    Automatiske brev
                  </Heading>
                  <TemplateList malType="autobrev" templates={autobrev} />
                </VStack>
                <VStack gap="space-8">
                  <Heading level="2" size="xsmall">
                    Redigerbare brev
                  </Heading>
                  <TemplateList malType="redigerbar" templates={redigerbar} />
                </VStack>
              </VStack>
            )}
          </SearchResultsPanel>
        </Tabs.Panel>
      </Tabs>
    </div>
  );
}
