import { css } from "@emotion/react";
import { BodyShort, Detail, Heading, Loader, Search, Tabs, Tag, VStack } from "@navikt/ds-react";
import { type QueryClient } from "@tanstack/react-query";
import { createFileRoute, Link } from "@tanstack/react-router";
import { useEffect, useMemo, useState } from "react";

import {
  getBrevkoder,
  getBrevkoderMedMetadata,
  getTemplateDescription,
  type MalType,
} from "~/api/brevbaker-api-endpoints";
import { loadTemplateCache, saveTemplateCache, type TemplateWithDescription } from "~/api/templateCache";
import {
  BrevResultList,
  type IndexableTemplate,
  MIN_QUERY_LENGTH,
  PAGE_SIZE,
  SearchResultsPanel,
  SearchSnippet,
  useTemplateSearch,
} from "~/search";

function displayTitleOf(
  description: { name: string; metadata?: { displayTitle?: string } | null },
  id: string,
): string {
  const title = description.metadata?.displayTitle;
  return title?.trim() ? title : description.name || id;
}

export const Route = createFileRoute("/templates")({
  loader: async ({ context }) => {
    const cached = loadTemplateCache();
    if (cached) {
      populateQueryCache(context.queryClient, "autobrev", cached.autobrev);
      populateQueryCache(context.queryClient, "redigerbar", cached.redigerbar);
      return { autobrev: cached.autobrev, redigerbar: cached.redigerbar };
    }

    const [autobrev, redigerbar] = await Promise.all([
      fetchAllDescriptions(context.queryClient, "autobrev"),
      fetchAllDescriptions(context.queryClient, "redigerbar"),
    ]);

    saveTemplateCache(autobrev, redigerbar);
    return { autobrev, redigerbar };
  },

  component: AllTemplates,
});

function populateQueryCache(queryClient: QueryClient, malType: MalType, templates: TemplateWithDescription[]) {
  for (const { id, description } of templates) {
    queryClient.setQueryData(getTemplateDescription.queryKey(malType, id), description);
  }
}

async function fetchAllDescriptions(queryClient: QueryClient, malType: MalType): Promise<TemplateWithDescription[]> {
  const [ids, descriptions] = await Promise.all([
    queryClient.ensureQueryData({
      queryKey: getBrevkoder.queryKey(malType),
      queryFn: () => getBrevkoder.queryFn(malType),
    }),
    queryClient.ensureQueryData({
      queryKey: getBrevkoderMedMetadata.queryKey(malType),
      queryFn: () => getBrevkoderMedMetadata.queryFn(malType),
    }),
  ]);

  if (!Array.isArray(ids)) {
    throw new TypeError(
      `Forventet en liste med brevkoder for "${malType}", men fikk noe annet. Går kallene til brevbaker-APIet (f.eks. via BFF eller Vite-proxy)?`,
    );
  }

  // Metadata and brevkode lists share the same order; fall back to per-template fetches if misaligned.
  const templates: TemplateWithDescription[] =
    Array.isArray(descriptions) && descriptions.length === ids.length
      ? ids.map((id, index) => ({ id, description: descriptions[index] }))
      : await Promise.all(
          ids.map(async (id) => ({
            id,
            description: await queryClient.ensureQueryData({
              queryKey: getTemplateDescription.queryKey(malType, id),
              queryFn: () => getTemplateDescription.queryFn(malType, id),
            }),
          })),
        );

  populateQueryCache(queryClient, malType, templates);
  return templates;
}

function TemplateList({ templates, malType }: { templates: TemplateWithDescription[]; malType: MalType }) {
  if (templates.length === 0) {
    return <span>Ingen treff</span>;
  }
  return (
    <VStack gap="space-8">
      {templates.map(({ id, description }) => (
        <Link key={id} params={{ malType, templateId: id }} preload="intent" to="/template/$malType/$templateId">
          {displayTitleOf(description, id)}
        </Link>
      ))}
    </VStack>
  );
}

function TabLabelWithCount({ label, count, isSearching }: { label: string; count: number; isSearching: boolean }) {
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

function AllTemplates() {
  const { autobrev, redigerbar } = Route.useLoaderData();

  const indexable = useMemo<IndexableTemplate[]>(() => {
    const toIndexable = (templates: TemplateWithDescription[], malType: MalType) =>
      templates.flatMap(({ id, description }) =>
        description.languages.map((language) => ({
          id,
          malType,
          name: description.name || id,
          displayTitle: displayTitleOf(description, id),
          language,
        })),
      );
    return [...toIndexable(autobrev, "autobrev"), ...toIndexable(redigerbar, "redigerbar")];
  }, [autobrev, redigerbar]);

  const search = useTemplateSearch(indexable);

  const titleByKey = useMemo(() => {
    const map = new Map<string, string>();
    const add = (templates: TemplateWithDescription[], malType: MalType) => {
      for (const { id, description } of templates) {
        map.set(`${malType}/${id}`, displayTitleOf(description, id));
      }
    };
    add(autobrev, "autobrev");
    add(redigerbar, "redigerbar");
    return map;
  }, [autobrev, redigerbar]);
  const getTitle = (malType: MalType, id: string) => titleByKey.get(`${malType}/${id}`) ?? id;

  const { isSearching, contentResults, brevResults, contentTemplateCount, brevTemplateCount, indexProgress } = search;

  const [activeTab, setActiveTab] = useState<"innhold" | "brev">("innhold");
  const [page, setPage] = useState(1);

  // biome-ignore lint/correctness/useExhaustiveDependencies: resets pagination when query changes.
  useEffect(() => {
    setPage(1);
  }, [search.query]);

  const activeResults = activeTab === "innhold" ? contentResults : brevResults;
  const pageCount = Math.max(1, Math.ceil(activeResults.length / PAGE_SIZE));
  const safePage = Math.min(page, pageCount);
  const pageItems = activeResults.slice((safePage - 1) * PAGE_SIZE, safePage * PAGE_SIZE);
  const innholdPageItems = activeTab === "innhold" ? pageItems : contentResults.slice(0, PAGE_SIZE);
  const brevPageItems = activeTab === "brev" ? pageItems : brevResults.slice(0, PAGE_SIZE);

  const changeTab = (tab: string) => {
    setActiveTab(tab === "brev" ? "brev" : "innhold");
    setPage(1);
  };

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
          onChange={search.setQuery}
          onClear={() => search.setQuery("")}
          size="small"
          value={search.query}
          variant="simple"
        />
        {indexProgress && indexProgress.status === "indexing" ? (
          <Loader size="small" title="Indekserer innhold…" />
        ) : null}
      </div>

      <Detail
        aria-live="polite"
        css={css`
          margin-top: var(--ax-space-8);
        `}
        textColor="subtle"
      >
        {indexProgress && indexProgress.status === "indexing"
          ? `Indekserer innhold … ${indexProgress.indexed}/${indexProgress.total}`
          : `Søker i innholdet til ${search.templateTotal} maler på ${search.languageTotal} språk${
              indexProgress && indexProgress.failed > 0 ? ` (${indexProgress.failed} feilet)` : ""
            }`}
      </Detail>

      <Tabs
        css={css`
          margin-top: var(--ax-space-16);
        `}
        onChange={changeTab}
        value={activeTab}
      >
        <Tabs.List>
          <Tabs.Tab
            label={<TabLabelWithCount count={contentTemplateCount} isSearching={isSearching} label="Innhold" />}
            value="innhold"
          />
          <Tabs.Tab
            label={<TabLabelWithCount count={brevTemplateCount} isSearching={isSearching} label="Brev" />}
            value="brev"
          />
        </Tabs.List>

        <Tabs.Panel value="innhold">
          <SearchResultsPanel
            page={safePage}
            pageCount={activeTab === "innhold" ? pageCount : 1}
            setPage={setPage}
            summary={
              isSearching && contentResults.length > 0 ? (
                <>
                  Frasen du søker på er brukt i <b>{contentTemplateCount} maler</b> i {contentResults.length} avsnitt
                </>
              ) : undefined
            }
          >
            {!isSearching ? (
              <BodyShort textColor="subtle">
                Skriv minst {MIN_QUERY_LENGTH} tegn for å søke i innholdet i malene.
              </BodyShort>
            ) : contentResults.length === 0 ? (
              <BodyShort>Ingen treff i innholdet</BodyShort>
            ) : (
              innholdPageItems.map((result) => (
                <SearchSnippet
                  key={`${result.malType}/${result.id}/${result.language}/${result.startLine}`}
                  needle={search.query}
                  result={result}
                  title={getTitle(result.malType, result.id)}
                />
              ))
            )}
          </SearchResultsPanel>
        </Tabs.Panel>

        <Tabs.Panel value="brev">
          <SearchResultsPanel
            page={safePage}
            pageCount={activeTab === "brev" ? pageCount : 1}
            setPage={setPage}
            summary={
              isSearching && brevResults.length > 0 ? (
                <>
                  Søket traff tittel, navn eller brevkode i <b>{brevTemplateCount} maler</b>
                </>
              ) : undefined
            }
          >
            {isSearching ? (
              brevResults.length === 0 ? (
                <BodyShort>Ingen treff i tittel, navn eller brevkode</BodyShort>
              ) : (
                <BrevResultList getTitle={getTitle} results={brevPageItems} />
              )
            ) : (
              <VStack gap="space-20">
                <VStack gap="space-8">
                  <Heading level="2" size="xsmall">
                    Automatiske brev
                  </Heading>
                  <TemplateList malType={"autobrev"} templates={autobrev} />
                </VStack>
                <VStack gap="space-8">
                  <Heading level="2" size="xsmall">
                    Redigerbare brev
                  </Heading>
                  <TemplateList malType={"redigerbar"} templates={redigerbar} />
                </VStack>
              </VStack>
            )}
          </SearchResultsPanel>
        </Tabs.Panel>
      </Tabs>
    </div>
  );
}
