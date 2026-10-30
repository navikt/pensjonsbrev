import { css } from "@emotion/react";
import {
  Alert,
  Bleed,
  BodyShort,
  Box,
  Button,
  Checkbox,
  Heading,
  HStack,
  Search,
  Tabs,
  Tag,
  VStack,
} from "@navikt/ds-react";
import { createFileRoute, Link, useNavigate } from "@tanstack/react-router";
import { useCallback, useEffect, useMemo, useRef } from "react";

import { type MalType } from "~/api/brevbaker-api-endpoints";
import { type TemplateDescription } from "~/api/brevbakerTypes";
import { AnimatedEllipsis } from "~/components/AnimatedEllipsis";
import {
  BrevResultList,
  CONTENT_PAGE_SIZE,
  LETTER_PAGE_SIZE,
  SearchActivityOutline,
  SearchResultsPanel,
  SearchSnippet,
  templateKey,
  useTemplateSearch,
} from "~/search";
import { isSelectedResult, pageOfResult } from "~/search/resultNavigation";
import {
  DEFAULT_TAB,
  formatResultRef,
  parseResultRef,
  type ResultRef,
  type ResultTab,
  resultRefOf,
  type SearchCriteria,
  validateTemplatesSearch,
} from "~/search/searchContext";
import { displayTitleOf, fetchAllDescriptions, toRefs } from "~/search/templateRefs";
import { useScrollToSelectedResult } from "~/search/useScrollToSelectedResult";

const MAL_TYPE_LABELS: Record<MalType, string> = {
  autobrev: "automatiske brev",
  redigerbar: "redigerbare brev",
};
/** Human-readable Norwegian label for the malType(s) whose corpus failed to load. */
function malTypeLabels(malTypes: MalType[]): string {
  return malTypes.map((malType) => MAL_TYPE_LABELS[malType]).join(" og ");
}
export const Route = createFileRoute("/templates")({
  // The search lives in the URL, so a copied address, a refresh and the way
  // back from a template all restore it. See `searchContext.ts`.
  validateSearch: validateTemplatesSearch,
  // Typing, paging and switching tab only change search params; the template
  // list does not need loading again for that.
  shouldReload: ({ cause }) => cause !== "stay",
  loader: async ({ context }) => {
    const [autobrev, redigerbar] = await Promise.all([
      fetchAllDescriptions(context.queryClient, "autobrev"),
      fetchAllDescriptions(context.queryClient, "redigerbar"),
    ]);
    return { autobrev, redigerbar };
  },
  component: AllTemplates,
});
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
/** `count` is left out until a search has completed, so the first search shows
 *  no count rather than a `0` it hasn't earned. */
function TabLabel({ label, count }: { label: string; count?: number }) {
  return (
    <span
      css={css`
        display: inline-flex;
        align-items: center;
        gap: var(--ax-space-8);
      `}
    >
      {label}
      {count === undefined ? null : (
        <Tag data-color="neutral" size="xsmall" variant="moderate">
          {count}
        </Tag>
      )}
    </span>
  );
}
const SEARCH_FAILED_MESSAGE = "Søket kunne ikke gjennomføres på grunn av en teknisk feil.";
const INDEXING_SUMMARY = (
  <>
    Klargjør malene for søk
    <AnimatedEllipsis />
  </>
);
function AllTemplates() {
  const { autobrev, redigerbar } = Route.useLoaderData();
  const search = Route.useSearch();
  const navigate = useNavigate({ from: Route.fullPath });
  const refs = useMemo(
    () => [...toRefs(autobrev, "autobrev"), ...toRefs(redigerbar, "redigerbar")],
    [autobrev, redigerbar],
  );
  const {
    query,
    setQuery,
    settledQuery,
    exactOnly,
    setExactOnly,
    isLoading,
    isPending,
    displayed,
    searchFailed,
    failedMalTypes,
    retryFailed,
  } = useTemplateSearch(refs, { initialQuery: search.q, initialExactOnly: search.exact });

  // The input is the source of truth while the page is open; the URL follows
  // it. Only the settled query is written, with `replace`, so typing neither
  // adds history entries nor navigates per keystroke. A new search starts on
  // page 1 with nothing selected.
  const latestSearch = useRef(search);
  latestSearch.current = search;
  useEffect(() => {
    const q = settledQuery || undefined;
    const exact = exactOnly || undefined;
    if (q === latestSearch.current.q && exact === latestSearch.current.exact) {
      return;
    }
    void navigate({ replace: true, search: (s) => ({ ...s, q, exact, page: undefined, selected: undefined }) });
  }, [settledQuery, exactOnly, navigate]);

  const activeTab: ResultTab = search.tab ?? DEFAULT_TAB;
  const selected = useMemo(() => parseResultRef(search.selected), [search.selected]);
  const groupsLanguages = activeTab === "brev";
  const activeHits = (activeTab === "innhold" ? displayed?.contentHits : displayed?.brevHits) ?? [];
  const pageSize = activeTab === "innhold" ? CONTENT_PAGE_SIZE : LETTER_PAGE_SIZE;
  // Coming back from a template, the list opens at the page showing it.
  const page =
    search.page ??
    (selected &&
      pageOfResult(
        activeHits.map((hit) => resultRefOf(hit.template)),
        selected,
        pageSize,
        groupsLanguages,
      )) ??
    1;
  // Only the active tab's panel is mounted, so both panels can share the page
  // state; each still slices with its own page size.
  const pageCount = Math.max(1, Math.ceil(activeHits.length / pageSize));
  const safePage = Math.min(page, pageCount);
  const setPage = (next: number) =>
    void navigate({ replace: true, search: (s) => ({ ...s, page: next, selected: undefined }) });
  // Records the opened result in this history entry before leaving, so
  // browser Back returns to the list with it marked.
  const openResult = useCallback(
    (result: ResultRef) =>
      navigate({ replace: true, search: (s) => ({ ...s, page: undefined, selected: formatResultRef(result) }) }),
    [navigate],
  );
  const criteria = useMemo<SearchCriteria | undefined>(
    () => displayed && { q: displayed.highlight.needle, exact: displayed.highlight.exactOnly, tab: activeTab },
    [displayed, activeTab],
  );
  // The result lists are by far the most expensive thing on this page, and they
  // depend only on the search on screen, the page and the selection - never on
  // `isPending`. Memoising the elements lets React bail out of the whole
  // subtree when the only thing that changed is the search outline, so
  // toggling it twice per search costs nothing while the user is typing.
  const contentList = useMemo(() => {
    if (!displayed || !criteria || displayed.contentHits.length === 0) {
      return null;
    }
    const start = (safePage - 1) * CONTENT_PAGE_SIZE;
    return displayed.contentHits
      .slice(start, start + CONTENT_PAGE_SIZE)
      .map((hit) => (
        <SearchSnippet
          criteria={criteria}
          hit={hit}
          key={templateKey(hit.template)}
          onOpen={openResult}
          selected={isSelectedResult(resultRefOf(hit.template), selected, false)}
        />
      ));
  }, [displayed, criteria, safePage, selected, openResult]);
  const brevList = useMemo(() => {
    if (!displayed || !criteria || displayed.brevHits.length === 0) {
      return null;
    }
    const start = (safePage - 1) * LETTER_PAGE_SIZE;
    return (
      <BrevResultList
        criteria={criteria}
        hits={displayed.brevHits.slice(start, start + LETTER_PAGE_SIZE)}
        onOpen={openResult}
        selected={selected}
      />
    );
  }, [displayed, criteria, safePage, selected, openResult]);
  useScrollToSelectedResult(search.selected, activeTab === "innhold" ? contentList : brevList);
  // Summaries describe the search on screen, so while the next one runs the
  // previous summary simply stays. Zero hits is reported here too, leaving the
  // panel body empty.
  const error = searchFailed ? SEARCH_FAILED_MESSAGE : undefined;
  const contentSummary = isLoading ? (
    INDEXING_SUMMARY
  ) : !displayed ? undefined : displayed.contentHits.length === 0 ? (
    "Ingen treff i innholdet"
  ) : (
    <>
      Frasen du søker etter er brukt i <b>{displayed.contentTemplateCount} maler</b> i {displayed.contentLineCount}{" "}
      avsnitt
    </>
  );
  const brevSummary = isLoading ? (
    INDEXING_SUMMARY
  ) : !displayed ? undefined : displayed.brevHits.length === 0 ? (
    "Ingen treff i tittel, navn eller brevkode"
  ) : (
    <>
      Søket traff tittel, navn eller brevkode i <b>{displayed.brevTemplateCount} maler</b>
    </>
  );
  return (
    <Box asChild background="default" height="100vh" overflow="hidden">
      <VStack flexGrow="1" gap="space-16" paddingBlock="space-16 space-0" paddingInline="space-16">
        <Heading level="1" size="small">
          Brevoppskrift
        </Heading>
        <Box maxWidth="480px" width="100%">
          <SearchActivityOutline active={isLoading || isPending}>
            <Search
              autoFocus
              label="Søk etter innholdet eller brevmal"
              onChange={setQuery}
              onClear={() => setQuery("")}
              placeholder="Søk etter innholdet eller brevmal"
              size="small"
              value={query}
              variant="simple"
            />
          </SearchActivityOutline>
        </Box>
        <Checkbox checked={exactOnly} onChange={(e) => setExactOnly(e.target.checked)} size="small">
          Vis kun nøyaktige treff
        </Checkbox>
        {failedMalTypes.length > 0 ? (
          <Alert size="small" variant="warning">
            <HStack align="center" gap="space-16" justify="space-between">
              <BodyShort>
                Kunne ikke hente innhold for {malTypeLabels(failedMalTypes)}. Søkeresultatene kan derfor være
                ufullstendige.
              </BodyShort>
              <Button onClick={retryFailed} size="small" variant="secondary">
                Prøv igjen
              </Button>
            </HStack>
          </Alert>
        ) : null}
        <VStack asChild flexGrow="1" overflow="hidden">
          <Bleed asChild marginInline="space-16">
            <Tabs
              css={{ ">div:first-of-type": { marginInline: "var(--ax-space-16)" } }}
              onChange={(tab) =>
                void navigate({
                  replace: true,
                  search: (s) => ({
                    ...s,
                    tab: tab === "innhold" ? "innhold" : undefined,
                    page: undefined,
                    selected: undefined,
                  }),
                })
              }
              value={activeTab}
            >
              <Tabs.List>
                <Tabs.Tab label={<TabLabel count={displayed?.brevTemplateCount} label="Brev" />} value="brev" />
                <Tabs.Tab
                  label={<TabLabel count={displayed?.contentTemplateCount} label="Innhold" />}
                  value="innhold"
                />
              </Tabs.List>

              <Box asChild flexGrow="1" overflow="hidden">
                <Tabs.Panel value="brev">
                  <SearchResultsPanel
                    error={error}
                    isPending={isPending}
                    page={safePage}
                    pageCount={pageCount}
                    setPage={setPage}
                    summary={brevSummary}
                  >
                    {displayed || searchFailed ? (
                      brevList
                    ) : (
                      // Until a search is on screen the full index is browsable:
                      // it comes from the route loader, not the search corpus, so
                      // it is there before indexing finishes and stays while the
                      // first search runs.
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
              </Box>
              <Box asChild flexGrow="1" overflow="hidden">
                <Tabs.Panel value="innhold">
                  <SearchResultsPanel
                    error={error}
                    isPending={isPending}
                    page={safePage}
                    pageCount={pageCount}
                    setPage={setPage}
                    summary={contentSummary}
                  >
                    {contentList}
                  </SearchResultsPanel>
                </Tabs.Panel>
              </Box>
            </Tabs>
          </Bleed>
        </VStack>
      </VStack>
    </Box>
  );
}
