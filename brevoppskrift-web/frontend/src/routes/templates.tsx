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
  Loader,
  Search,
  Tabs,
  Tag,
  VStack,
} from "@navikt/ds-react";
import { type QueryClient } from "@tanstack/react-query";
import { createFileRoute, Link } from "@tanstack/react-router";
import { useMemo, useState } from "react";

import { getBrevkoderMedMetadata, getTemplateDescription, type MalType } from "~/api/brevbaker-api-endpoints";
import { type TemplateDescription } from "~/api/brevbakerTypes";
import {
  BrevResultList,
  CONTENT_PAGE_SIZE,
  type DisplayedSearch,
  LETTER_PAGE_SIZE,
  SearchResultsPanel,
  SearchSnippet,
  type TemplateRef,
  templateKey,
  useTemplateSearch,
} from "~/search";

function displayTitleOf(description: { name: string; metadata?: { displayTitle?: string } | null }): string {
  const title = description.metadata?.displayTitle;
  return title?.trim() ? title : description.name;
}
const MAL_TYPE_LABELS: Record<MalType, string> = {
  autobrev: "automatiske brev",
  redigerbar: "redigerbare brev",
};
/** Human-readable Norwegian label for the malType(s) whose corpus failed to load. */
function malTypeLabels(malTypes: MalType[]): string {
  return malTypes.map((malType) => MAL_TYPE_LABELS[malType]).join(" og ");
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
function toRefs(templates: TemplateDescription[], malType: MalType): TemplateRef[] {
  return templates.map((description) => ({
    malType,
    brevkode: description.name,
    title: displayTitleOf(description),
    languages: description.languages,
  }));
}
const SEARCH_FAILED_MESSAGE = "Søket kunne ikke gjennomføres på grunn av en teknisk feil.";
/** A page number, and the search it is a page of. */
type Paging = { page: number; of: DisplayedSearch | undefined };
function AllTemplates() {
  const { autobrev, redigerbar } = Route.useLoaderData();
  const refs = useMemo<TemplateRef[]>(
    () => [...toRefs(autobrev, "autobrev"), ...toRefs(redigerbar, "redigerbar")],
    [autobrev, redigerbar],
  );
  const {
    query,
    setQuery,
    exactOnly,
    setExactOnly,
    isLoading,
    isPending,
    displayed,
    searchFailed,
    failedMalTypes,
    retryFailed,
  } = useTemplateSearch(refs);
  const isWorking = isLoading || isPending;
  const [activeTab, setActiveTab] = useState<"innhold" | "brev">("innhold");
  // A page belongs to the search it was chosen in. Once a different search is
  // on screen the stored page no longer applies and reads as page 1 - in the
  // same render the new hits arrive in, so the list is never rendered at a
  // stale page first. The tab switch resets it explicitly.
  const [paging, setPaging] = useState<Paging>({ page: 1, of: undefined });
  const page = paging.of === displayed ? paging.page : 1;
  const setPage = (next: number) => setPaging({ page: next, of: displayed });
  const activeHits = (activeTab === "innhold" ? displayed?.contentHits : displayed?.brevHits) ?? [];
  const pageSize = activeTab === "innhold" ? CONTENT_PAGE_SIZE : LETTER_PAGE_SIZE;
  // Only the active tab's panel is mounted, so both panels can share the page
  // state; each still slices with its own page size.
  const pageCount = Math.max(1, Math.ceil(activeHits.length / pageSize));
  const safePage = Math.min(page, pageCount);
  // The result lists are by far the most expensive thing on this page, and they
  // depend only on the search on screen and the page - never on `isPending`.
  // Memoising the elements lets React bail out of the whole subtree when the
  // only thing that changed is the spinner, so toggling it twice per search
  // costs nothing while the user is typing.
  const contentList = useMemo(() => {
    if (!displayed || displayed.contentHits.length === 0) {
      return null;
    }
    const { contentHits, highlight } = displayed;
    const start = (safePage - 1) * CONTENT_PAGE_SIZE;
    return contentHits
      .slice(start, start + CONTENT_PAGE_SIZE)
      .map((hit) => (
        <SearchSnippet
          exact={highlight.exactOnly}
          hit={hit}
          key={templateKey(hit.template)}
          needle={highlight.needle}
        />
      ));
  }, [displayed, safePage]);
  const brevList = useMemo(() => {
    if (!displayed || displayed.brevHits.length === 0) {
      return null;
    }
    const { brevHits, highlight } = displayed;
    const start = (safePage - 1) * LETTER_PAGE_SIZE;
    return (
      <BrevResultList
        exact={highlight.exactOnly}
        hits={brevHits.slice(start, start + LETTER_PAGE_SIZE)}
        needle={highlight.needle}
      />
    );
  }, [displayed, safePage]);
  // Summaries describe the search on screen, so while the next one runs the
  // previous summary simply stays. Zero hits is reported here too, leaving the
  // panel body empty.
  const error = searchFailed ? SEARCH_FAILED_MESSAGE : undefined;
  const contentSummary = isLoading ? (
    "Indekserer innhold …"
  ) : !displayed ? undefined : displayed.contentHits.length === 0 ? (
    "Ingen treff i innholdet"
  ) : (
    <>
      Frasen du søker på er brukt i <b>{displayed.contentTemplateCount} maler</b> i {displayed.contentLineCount} avsnitt
    </>
  );
  const brevSummary = isLoading ? (
    "Indekserer innhold …"
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
        {/* No wrapping: the spinner must never drop to a line of its own, or
            it would push the page down every time it appears. */}
        <HStack align="center" gap="space-8" wrap={false}>
          <Box maxWidth="480px" width="100%">
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
          </Box>
          {isWorking ? <Loader size="small" title={isLoading ? "Indekserer" : "Søker"} /> : null}
        </HStack>
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
              onChange={(tab) => {
                setActiveTab(tab === "brev" ? "brev" : "innhold");
                setPaging({ page: 1, of: displayed });
              }}
              value={activeTab}
            >
              <Tabs.List>
                <Tabs.Tab
                  label={<TabLabel count={displayed?.contentTemplateCount} label="Innhold" />}
                  value="innhold"
                />
                <Tabs.Tab label={<TabLabel count={displayed?.brevTemplateCount} label="Brev" />} value="brev" />
              </Tabs.List>

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
            </Tabs>
          </Bleed>
        </VStack>
      </VStack>
    </Box>
  );
}
