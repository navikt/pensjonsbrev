import { css } from "@emotion/react";
import { BodyShort, Button, Detail, Heading, HStack, Pagination, Search, Tabs, Tag, VStack } from "@navikt/ds-react";
import { type QueryClient, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, Link, useRouter } from "@tanstack/react-router";
import {
  Fragment,
  type ReactNode,
  type Ref,
  useDeferredValue,
  useEffect,
  useLayoutEffect,
  useMemo,
  useRef,
  useState,
} from "react";

import {
  brevkoderKeys,
  getBrevkoder,
  getBrevkoderMedMetadata,
  getTemplateDescription,
  type MalType,
  templateDescriptionKeys,
} from "~/api/brevbaker-api-endpoints";
import {
  invalidateTemplateCache,
  loadTemplateCache,
  saveTemplateCache,
  type TemplateWithDescription,
} from "~/api/templateCache";
import { type IndexableTemplate } from "~/api/textIndexCache";
import {
  exactRanges,
  type HighlightRange,
  type Line,
  type SnippetResult,
  searchFuzzy,
  splitSegmentByRanges,
} from "~/api/textSearch";
import { invalidateTextIndexCache, useTextIndex } from "~/components/useTextIndex";

const MIN_QUERY_LENGTH = 2;
const PAGE_SIZE = 20;
const SNIPPET_CHARS = 160;
/** Fewest results to show per page even on a short viewport. */
const MIN_PAGE_SIZE = 3;
/** Vertical space (px) kept free below the results list for the pagination
 * control and the page's bottom padding, so the list never forces a scroll. */
const PAGINATION_RESERVE_PX = 72;
/** Row gap (px) between result items; matches the list's `space-12` gap. */
const ROW_GAP_PX = 12;

const LANGUAGE_LABELS: Record<string, string> = { BOKMAL: "Bokmål", NYNORSK: "Nynorsk", ENGLISH: "Engelsk" };

function languageLabel(language: string): string {
  return LANGUAGE_LABELS[language] ?? language;
}

/** The human-readable title from metadata, falling back to the technical name/brevkode. */
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

  // The metadata list does not carry the brevkode, but the backend derives both lists from the
  // same template map in the same order, so they align by index. Fall back to per-template fetches
  // if that assumption ever fails to break.
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

function VariableChip({ label }: { label: string }) {
  return (
    <span
      css={css`
        display: inline-flex;
        align-items: baseline;
        gap: 2px;
        margin: 0 1px;
        padding: 0 var(--ax-space-4);
        border-radius: var(--ax-radius-4);
        background: var(--ax-bg-accent-moderate);
        color: var(--ax-text-accent-subtle);
        font-style: italic;
        white-space: nowrap;
      `}
      title={`Variabel: ${label}`}
    >
      <span aria-hidden>⟨</span>
      {label}
      <span aria-hidden>⟩</span>
    </span>
  );
}

function HighlightedLine({ line, ranges, metaNeedle }: { line: Line; ranges: HighlightRange[]; metaNeedle?: string }) {
  return (
    <>
      {line.map((segment, index) => {
        if (segment.kind === "var") {
          return <VariableChip key={index} label={segment.label} />;
        }
        const segmentRanges = metaNeedle
          ? exactRanges(segment.value, metaNeedle)
          : ranges
              .filter((range) => range.segmentIndex === index)
              .map((range) => ({ start: range.start, end: range.end }));
        return splitSegmentByRanges(segment.value, segmentRanges).map((part, partIndex) =>
          part.match ? (
            <mark key={`${index}-${partIndex}`}>{part.text}</mark>
          ) : (
            <Fragment key={`${index}-${partIndex}`}>{part.text}</Fragment>
          ),
        );
      })}
    </>
  );
}

/** Visible length of one line, counting variable chips by their label length. */
function lineLength(line: Line): number {
  return line.reduce(
    (sum, segment) => sum + (segment.kind === "text" ? segment.value.length : segment.label.length),
    0,
  );
}

/** Lowercased plain text of a line; variable chips contribute their label so that
 * indices line up with {@link clampLine}'s segment-based slicing. */
function linePlain(line: Line): string {
  return line
    .map((segment) => (segment.kind === "text" ? segment.value : segment.label))
    .join("")
    .toLowerCase();
}

/** Character index to center truncation on: the middle of the first occurrence of
 * `needle` in the line, or null when it is not found. */
function indexOfNeedleCenter(line: Line, needle: string): number | null {
  if (!needle) {
    return null;
  }
  const idx = linePlain(line).indexOf(needle.toLowerCase());
  return idx < 0 ? null : idx + Math.floor(needle.length / 2);
}

/** Global character offset (counting variable labels) at the centre of the matched
 * span described by `ranges`, used to centre {@link clampLine} on the match. */
function rangesCenter(line: Line, ranges: HighlightRange[]): number | null {
  if (ranges.length === 0) {
    return null;
  }
  const globalOffset = (segmentIndex: number, offset: number): number => {
    let global = 0;
    for (let i = 0; i < segmentIndex; i++) {
      const segment = line[i];
      global += segment.kind === "text" ? segment.value.length : segment.label.length;
    }
    return global + offset;
  };
  const first = ranges[0];
  const last = ranges[ranges.length - 1];
  return Math.floor((globalOffset(first.segmentIndex, first.start) + globalOffset(last.segmentIndex, last.end)) / 2);
}

type ClampedLine = { line: Line; ranges: HighlightRange[] };

/** Trim a line to about `maxChars`, centered on `center` (or the head when null),
 * keeping variable chips atomic and adding "…" sentinels where text was removed.
 * `highlight` ranges (in the original line's coordinates) are remapped onto the
 * trimmed output so the full match — even when it spans several segments — stays
 * highlighted. */
function clampLine(line: Line, center: number | null, maxChars: number, highlight: HighlightRange[]): ClampedLine {
  if (lineLength(line) <= maxChars) {
    return { line, ranges: highlight };
  }
  const half = Math.floor(maxChars / 2);
  const windowStart = center == null ? 0 : Math.max(0, center - half);
  const windowEnd = windowStart + maxChars;

  type Piece = { seg: Line[number]; srcIndex: number; srcFrom: number; srcTo: number };
  const pieces: Piece[] = [];
  let pos = 0;
  let trimmedEnd = false;
  for (let srcIndex = 0; srcIndex < line.length; srcIndex++) {
    const segment = line[srcIndex];
    const segLen = segment.kind === "text" ? segment.value.length : segment.label.length;
    const segStart = pos;
    const segEnd = pos + segLen;
    pos = segEnd;
    if (segEnd <= windowStart || segStart >= windowEnd) {
      if (segStart >= windowEnd) {
        trimmedEnd = true;
      }
      continue;
    }
    if (segment.kind === "var") {
      pieces.push({ seg: segment, srcIndex, srcFrom: 0, srcTo: 0 });
      continue;
    }
    const from = Math.max(0, windowStart - segStart);
    const to = Math.min(segment.value.length, windowEnd - segStart);
    if (to < segment.value.length) {
      trimmedEnd = true;
    }
    pieces.push({ seg: { kind: "text", value: segment.value.slice(from, to) }, srcIndex, srcFrom: from, srcTo: to });
  }

  // Per-piece offset bookkeeping so highlight ranges can be remapped: `lead` is the
  // number of sentinel chars prepended to the piece's visible content; `strip` is the
  // number of leading whitespace chars removed.
  const leadByPiece = pieces.map(() => 0);
  const stripByPiece = pieces.map(() => 0);

  const trimmedStart = windowStart > 0;
  if (trimmedStart) {
    const first = pieces[0];
    if (first && first.seg.kind === "text") {
      const stripped = first.seg.value.replace(/^\s+/, "");
      stripByPiece[0] = first.seg.value.length - stripped.length;
      leadByPiece[0] = 2; // "… " prefix length
      first.seg = { kind: "text", value: `… ${stripped}` };
    } else {
      pieces.unshift({ seg: { kind: "text", value: "… " }, srcIndex: -1, srcFrom: 0, srcTo: 0 });
      leadByPiece.unshift(0);
      stripByPiece.unshift(0);
    }
  }
  if (trimmedEnd) {
    const last = pieces[pieces.length - 1];
    if (last && last.seg.kind === "text") {
      last.seg = { kind: "text", value: `${last.seg.value.replace(/\s+$/, "")} …` };
    } else {
      pieces.push({ seg: { kind: "text", value: " …" }, srcIndex: -1, srcFrom: 0, srcTo: 0 });
      leadByPiece.push(0);
      stripByPiece.push(0);
    }
  }

  const ranges: HighlightRange[] = [];
  for (let outIndex = 0; outIndex < pieces.length; outIndex++) {
    const piece = pieces[outIndex];
    if (piece.seg.kind !== "text" || piece.srcIndex < 0) {
      continue;
    }
    const strip = stripByPiece[outIndex];
    const lead = leadByPiece[outIndex];
    const contentStart = piece.srcFrom + strip;
    const contentEnd = piece.srcTo;
    for (const range of highlight) {
      if (range.segmentIndex !== piece.srcIndex) {
        continue;
      }
      const overlapStart = Math.max(range.start, contentStart);
      const overlapEnd = Math.min(range.end, contentEnd);
      if (overlapStart < overlapEnd) {
        ranges.push({
          segmentIndex: outIndex,
          start: lead + (overlapStart - piece.srcFrom - strip),
          end: lead + (overlapEnd - piece.srcFrom - strip),
        });
      }
    }
  }

  return { line: pieces.map((piece) => piece.seg), ranges };
}

function SearchResult({ result, title }: { result: SnippetResult; title: string }) {
  const primaryIndex = result.highlightLineIndex;
  // Show only the matched line plus the line before and after (max three lines).
  const start = primaryIndex === undefined ? 0 : Math.max(0, primaryIndex - 1);
  const end =
    primaryIndex === undefined ? Math.min(result.lines.length, 3) : Math.min(result.lines.length, primaryIndex + 2);
  const visibleLines = result.lines.slice(start, end);
  const needle = result.anchorQuery;

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
          const center = isPrimary ? (rangesCenter(line, highlight) ?? indexOfNeedleCenter(line, needle)) : null;
          const clamped = clampLine(line, center, SNIPPET_CHARS, highlight);
          return (
            <BodyShort
              css={css`
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
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

/** Distinct templates whose name/brevkode/title matched the query (one row per template). */
function BrevList({
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
            params={{ malType: result.malType, templateId: result.id }}
            preload="intent"
            search={{ language: result.language, q: result.anchorQuery }}
            to="/template/$malType/$templateId"
          >
            <HighlightedLine
              line={[{ kind: "text", value: getTitle(result.malType, result.id) }]}
              metaNeedle={result.metaNeedle}
              ranges={[]}
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

function SearchTabPanel({
  page,
  pageCount,
  setPage,
  summary,
  children,
  contentRef,
}: {
  page: number;
  pageCount: number;
  setPage: (page: number) => void;
  summary?: ReactNode;
  children: ReactNode;
  contentRef?: Ref<HTMLDivElement>;
}) {
  return (
    <VStack
      css={css`
        margin-top: var(--ax-space-16);
      `}
      gap="space-12"
    >
      {summary ? (
        <Detail aria-live="polite" textColor="subtle">
          {summary}
        </Detail>
      ) : null}
      <div
        css={css`
          display: flex;
          flex-direction: column;
          gap: var(--ax-space-12);
        `}
        ref={contentRef}
      >
        {children}
      </div>
      {pageCount > 1 ? (
        <HStack justify="center">
          <Pagination count={pageCount} onPageChange={setPage} page={page} size="small" />
        </HStack>
      ) : null}
    </VStack>
  );
}

function AllTemplates() {
  const { autobrev, redigerbar } = Route.useLoaderData();
  const router = useRouter();
  const queryClient = useQueryClient();
  const [query, setQuery] = useState("");
  const [reindexNonce, setReindexNonce] = useState(0);

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

  const index = useTextIndex(indexable, reindexNonce);

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

  const templateTotal = useMemo(() => new Set(indexable.map((t) => `${t.malType}/${t.id}`)).size, [indexable]);
  const languageTotal = useMemo(() => new Set(indexable.map((t) => t.language)).size, [indexable]);

  const deferredQuery = useDeferredValue(query);
  const trimmedQuery = deferredQuery.trim();
  const isSearching = trimmedQuery.length >= MIN_QUERY_LENGTH;

  const results = useMemo(
    () => (isSearching ? searchFuzzy({ entries: index.entries }, trimmedQuery) : []),
    [isSearching, index.entries, trimmedQuery],
  );

  const refresh = () => {
    invalidateTemplateCache();
    invalidateTextIndexCache();
    queryClient.removeQueries({ queryKey: brevkoderKeys.all });
    queryClient.removeQueries({ queryKey: templateDescriptionKeys.all });
    setReindexNonce((nonce) => nonce + 1);
    router.invalidate();
  };

  const contentResults = useMemo(() => results.filter((result) => !result.meta), [results]);
  const brevResults = useMemo(() => results.filter((result) => result.meta), [results]);

  const contentTemplateCount = useMemo(
    () => new Set(contentResults.map((result) => `${result.malType}/${result.id}`)).size,
    [contentResults],
  );
  const brevTemplateCount = useMemo(
    () => new Set(brevResults.map((result) => `${result.malType}/${result.id}`)).size,
    [brevResults],
  );

  const [activeTab, setActiveTab] = useState<"innhold" | "brev">("innhold");
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(PAGE_SIZE);
  const listRef = useRef<HTMLDivElement>(null);

  // Fit the page size to the viewport so the active list plus its pagination
  // control fit without scrolling. Measured from the live DOM (list top + the
  // first item's real height) and recomputed on resize / tab / result changes.
  // biome-ignore lint/correctness/useExhaustiveDependencies: re-measure whenever the rendered list could change height or position.
  useLayoutEffect(() => {
    const measure = () => {
      const region = listRef.current;
      const firstItem = region?.firstElementChild;
      if (!region || !firstItem) {
        return;
      }
      const itemHeight = firstItem.getBoundingClientRect().height + ROW_GAP_PX;
      if (itemHeight <= ROW_GAP_PX) {
        return;
      }
      const available = window.innerHeight - region.getBoundingClientRect().top - PAGINATION_RESERVE_PX;
      const fit = Math.max(MIN_PAGE_SIZE, Math.floor(available / itemHeight));
      setPageSize((current) => (current === fit ? current : fit));
    };
    measure();
    window.addEventListener("resize", measure);
    return () => window.removeEventListener("resize", measure);
  }, [activeTab, isSearching, contentResults.length, brevResults.length, pageSize]);

  // Reset pagination on a new search, but never auto-switch the active tab — the
  // user stays on whichever tab they have selected while typing.
  // biome-ignore lint/correctness/useExhaustiveDependencies: trimmedQuery is the intentional trigger; the effect resets pagination whenever the query changes.
  useEffect(() => {
    setPage(1);
  }, [trimmedQuery]);

  const activeResults = activeTab === "innhold" ? contentResults : brevResults;
  const pageCount = Math.max(1, Math.ceil(activeResults.length / pageSize));
  const safePage = Math.min(page, pageCount);
  const pageItems = activeResults.slice((safePage - 1) * pageSize, safePage * pageSize);
  const innholdPageItems = activeTab === "innhold" ? pageItems : contentResults.slice(0, pageSize);
  const brevPageItems = activeTab === "brev" ? pageItems : brevResults.slice(0, pageSize);

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
          align-items: flex-end;
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
        />
        <Button onClick={refresh} size="small" type="button" variant="secondary">
          Oppdater
        </Button>
      </div>

      <Detail
        aria-live="polite"
        css={css`
          margin-top: var(--ax-space-8);
        `}
        textColor="subtle"
      >
        {index.status === "indexing"
          ? `Indekserer innhold … ${index.indexed}/${index.total}`
          : `Søker i innholdet til ${templateTotal} maler på ${languageTotal} språk${index.failed > 0 ? ` (${index.failed} feilet)` : ""}`}
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
            label={
              <span
                css={css`
                  display: inline-flex;
                  align-items: center;
                  gap: var(--ax-space-8);
                `}
              >
                Innhold
                {isSearching ? (
                  <Tag data-color="neutral" size="xsmall" variant="moderate">
                    {contentTemplateCount}
                  </Tag>
                ) : null}
              </span>
            }
            value="innhold"
          />
          <Tabs.Tab
            label={
              <span
                css={css`
                  display: inline-flex;
                  align-items: center;
                  gap: var(--ax-space-8);
                `}
              >
                Brev
                {isSearching ? (
                  <Tag data-color="neutral" size="xsmall" variant="moderate">
                    {brevTemplateCount}
                  </Tag>
                ) : null}
              </span>
            }
            value="brev"
          />
        </Tabs.List>

        <Tabs.Panel value="innhold">
          <SearchTabPanel
            contentRef={listRef}
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
                <SearchResult
                  key={`${result.malType}/${result.id}/${result.language}/${result.startLine}`}
                  result={result}
                  title={getTitle(result.malType, result.id)}
                />
              ))
            )}
          </SearchTabPanel>
        </Tabs.Panel>

        <Tabs.Panel value="brev">
          <SearchTabPanel
            contentRef={listRef}
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
                <BrevList getTitle={getTitle} results={brevPageItems} />
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
          </SearchTabPanel>
        </Tabs.Panel>
      </Tabs>
    </div>
  );
}
