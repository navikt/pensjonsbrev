import { css } from "@emotion/react";
import { BodyShort, Button, Detail, Heading, HStack, Search, VStack } from "@navikt/ds-react";
import { type QueryClient, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, Link, useRouter } from "@tanstack/react-router";
import { Fragment, useDeferredValue, useMemo, useState } from "react";

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
const MAX_RESULTS = 50;

const LANGUAGE_LABELS: Record<string, string> = { BOKMAL: "Bokmål", NYNORSK: "Nynorsk", ENGLISH: "Engelsk" };

function languageLabel(language: string): string {
  return LANGUAGE_LABELS[language] ?? language;
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
          {description.name || id}
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

function SearchResult({ result }: { result: SnippetResult }) {
  return (
    <div
      css={css`
        padding: var(--ax-space-12) var(--ax-space-16);
        border: 1px solid var(--ax-border-subtle);
        border-radius: var(--ax-radius-8);
        background: var(--ax-bg-default);
      `}
    >
      <HStack align="baseline" gap="space-8" wrap>
        <Link
          params={{ malType: result.malType, templateId: result.id }}
          preload="intent"
          search={{ language: result.language, q: result.anchorQuery, qi: result.matchOrdinal }}
          to="/template/$malType/$templateId"
        >
          {result.name}
        </Link>
        <Detail textColor="subtle">
          {result.id} · {languageLabel(result.language)}
          {result.meta ? " · treff i navn eller brevkode" : ` · ${result.templateMatchCount} treff i malen`}
        </Detail>
      </HStack>
      <div
        css={css`
          margin-top: var(--ax-space-8);
          padding-left: var(--ax-space-12);
          border-left: 2px solid var(--ax-border-accent-subtle);
          white-space: pre-wrap;

          mark {
            background: var(--ax-warning-300);
            color: inherit;
          }
        `}
      >
        {result.lines.map((line, lineIndex) => {
          const isPrimary = result.highlightLineIndex === undefined || lineIndex === result.highlightLineIndex;
          return (
            <BodyShort key={lineIndex} size="small" textColor="subtle">
              <HighlightedLine
                line={line}
                metaNeedle={result.metaNeedle}
                ranges={isPrimary ? result.highlightRanges : []}
              />
            </BodyShort>
          );
        })}
      </div>
    </div>
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
        description.languages.map((language) => ({ id, malType, name: description.name || id, language })),
      );
    return [...toIndexable(autobrev, "autobrev"), ...toIndexable(redigerbar, "redigerbar")];
  }, [autobrev, redigerbar]);

  const index = useTextIndex(indexable, reindexNonce);

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

  const visibleResults = results.slice(0, MAX_RESULTS);
  const templateCount = useMemo(
    () => new Set(results.map((result) => `${result.malType}/${result.id}`)).size,
    [results],
  );

  return (
    <div>
      <div
        css={css`
          display: flex;
          gap: var(--ax-space-16);
          align-items: flex-end;
          margin-top: 28px;
        `}
      >
        <Search
          css={css`
            flex: 1;
          `}
          hideLabel={false}
          label="Søk i innholdet i alle maler"
          onChange={setQuery}
          onClear={() => setQuery("")}
          size="medium"
          value={query}
          variant="simple"
        />
        <Button onClick={refresh} size="medium" type="button" variant="secondary">
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
      {isSearching ? (
        <div
          css={css`
            margin-top: var(--ax-space-16);
          `}
        >
          <Detail
            aria-live="polite"
            css={css`
              margin-bottom: var(--ax-space-8);
            `}
          >
            {results.length} tekstutdrag i {templateCount} maler
            {results.length > visibleResults.length ? ` (viser ${visibleResults.length})` : ""}
            {index.status === "indexing" ? " – fulltekstsøk indekseres fortsatt …" : ""}
          </Detail>
          {visibleResults.length === 0 ? (
            <BodyShort>Ingen treff</BodyShort>
          ) : (
            <VStack gap="space-12">
              {visibleResults.map((result) => (
                <SearchResult
                  key={`${result.malType}/${result.id}/${result.language}/${result.startLine}`}
                  result={result}
                />
              ))}
            </VStack>
          )}
        </div>
      ) : (
        <>
          <Heading
            css={css`
              margin-top: 28px;
              margin-bottom: 10px;
            `}
            level="2"
            size="large"
          >
            Automatiske brev
          </Heading>
          <TemplateList malType={"autobrev"} templates={autobrev} />
          <Heading
            css={css`
              margin-top: 28px;
              margin-bottom: 10px;
            `}
            level="2"
            size="large"
          >
            Redigerbare brev
          </Heading>
          <TemplateList malType={"redigerbar"} templates={redigerbar} />
        </>
      )}
    </div>
  );
}
