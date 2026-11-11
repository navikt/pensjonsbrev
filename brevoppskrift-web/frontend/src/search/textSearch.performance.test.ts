// @vitest-environment node
import { readFileSync } from "node:fs";
import { performance } from "node:perf_hooks";

import { it } from "vitest";

import { type SearchableContent } from "~/api/brevbaker-api-endpoints";
import { textFlowHasMatch } from "~/search/matchTextFlow";
import {
  buildIndex,
  hasShortTermsVerbatim,
  type Line,
  lineFlow,
  queryTerms,
  type SearchIndex,
  search,
  type TemplateText,
} from "~/search/textSearch";

// npm run bench:search; optionally set SEARCH_BENCHMARK_CORPUS to a saved /templates/{malType}/all response
// (or a concatenation of several) and SEARCH_BENCHMARK_SAMPLES to the number of paired samples (default 20).
// Measures what verifying fuzzy candidates with textFlowHasMatch costs: for every candidate line, or (as search() does)
// only for the line shown per template. Excludes indexing and rendering; search() also includes aggregation and
// metadata search.
const QUERIES = [
  "pensjon",
  "barnetillegg",
  "barnetillegg uføretrygd",
  "barnetileg uføretrygd",
  "folketrygdloven §",
  "difor brukt",
];
const SAMPLES = Number(process.env.SEARCH_BENCHMARK_SAMPLES ?? 20);

function syntheticCorpus(): TemplateText[] {
  const lines: Line[] = [
    [{ type: "text", value: "Vi har beregnet din alderspensjon. Pensjonen blir utbetalt hver måned." }],
    [{ type: "text", value: "Du kan ha rett til barnetillegg når du mottar uføretrygd." }],
    [
      { type: "text", value: "Vi har beregnet barnetillegg for " },
      { type: "var", label: "antallBarn" },
      { type: "text", value: " barn som får uføretrygd." },
    ],
    [{ type: "text", value: "Barnetillegg" }, { type: "cell" }, { type: "text", value: "Uføretrygd" }],
    [{ type: "text", value: "Vedtaket er gjort etter folketrygdloven § 19." }],
    [{ type: "text", value: "Utbetaling av pensjon skjer til bankkontoen du har oppgitt." }],
    [{ type: "text", value: "Du må melde fra til Nav dersom inntekten din endrer seg." }],
    [{ type: "text", value: "Klagefristen er seks uker fra du mottok dette brevet." }],
  ];
  return Array.from({ length: 100 }, (_, index) => ({
    id: `BENCH_${index}`,
    malType: "autobrev",
    language: "BOKMAL",
    title: `Brev ${index}`,
    lines,
    indexes: lines.map((_, line) => line),
  }));
}

function loadCorpus(): TemplateText[] {
  const path = process.env.SEARCH_BENCHMARK_CORPUS;
  if (!path) {
    return syntheticCorpus();
  }
  const entries = JSON.parse(readFileSync(path, "utf8")) as (SearchableContent | TemplateText)[];
  if (!Array.isArray(entries) || entries.length === 0) {
    throw new Error("SEARCH_BENCHMARK_CORPUS must contain a non-empty JSON array.");
  }
  return entries.map((entry) =>
    "brevkode" in entry
      ? {
          id: entry.brevkode,
          malType: "autobrev",
          language: entry.language,
          title: entry.brevkode,
          lines: entry.lines.map((line) => line.segments),
          indexes: entry.lines.map((line) => line.index),
        }
      : entry,
  );
}

type Variant = "unverified" | "every line" | "search()";
const VARIANTS: Variant[] = ["unverified", "every line", "search()"];

/** "unverified" and "every line" time the fuzzy candidate loop without and with verifying every candidate;
 *  "search()" times the production search, which verifies only the line shown per template. */
function measure(index: SearchIndex, query: string, variant: Variant) {
  const start = performance.now();
  if (variant === "search()") {
    const { content } = search(index, query, false);
    return { ms: performance.now() - start, candidates: 0, lines: 0, templates: content.length };
  }
  const candidates = index.contentFuse.search(query);
  const terms = queryTerms(query);
  const matches = [];
  for (const result of candidates) {
    const { item } = result;
    if (
      hasShortTermsVerbatim(terms, [item.text]) &&
      (variant === "unverified" || textFlowHasMatch(lineFlow(item.template.lines[item.lineIndex]), query, false))
    ) {
      matches.push(result);
    }
  }
  const ms = performance.now() - start;
  const templates = new Set(matches.map(({ item }) => item.template)).size;
  return { ms, candidates: candidates.length, lines: matches.length, templates };
}

function percentile(values: number[], fraction: number): number {
  const sorted = [...values].sort((a, b) => a - b);
  return sorted[Math.ceil(sorted.length * fraction) - 1];
}

const round = (value: number) => Number(value.toFixed(2));

it.runIf(process.env.SEARCH_BENCHMARK === "1")(
  "compares unverified, every-line and shown-line verification of fuzzy candidates",
  () => {
    const corpus = loadCorpus();
    const index = buildIndex(corpus);
    console.info(
      `${process.env.SEARCH_BENCHMARK_CORPUS ? "Exported" : "SYNTHETIC"} corpus: ${corpus.length} templates, ` +
        `${index.contentRecords.length} searchable lines. ${SAMPLES} samples per variant, two warm-up rounds, rotating order. ` +
        `${process.version}, ${process.platform}/${process.arch}.`,
    );
    const rows = QUERIES.map((query) => {
      for (let warmup = 0; warmup < 2; warmup++) {
        for (const variant of VARIANTS) {
          measure(index, query, variant);
        }
      }
      const samples = new Map<Variant, ReturnType<typeof measure>[]>(VARIANTS.map((variant) => [variant, []]));
      for (let sample = 0; sample < SAMPLES; sample++) {
        for (let offset = 0; offset < VARIANTS.length; offset++) {
          const variant = VARIANTS[(sample + offset) % VARIANTS.length];
          samples.get(variant)?.push(measure(index, query, variant));
        }
      }
      const of = (variant: Variant) => samples.get(variant) ?? [];
      const [unverified, everyLine, shownLine] = VARIANTS.map((variant) => of(variant)[0]);
      const row: Record<string, string | number> = {
        query,
        candidates: unverified.candidates,
        "lines rejected (every line)": unverified.lines - everyLine.lines,
        templates: unverified.templates,
        "templates dropped (every line)": unverified.templates - everyLine.templates,
        "templates dropped (search())": unverified.templates - shownLine.templates,
      };
      for (const variant of VARIANTS) {
        const times = of(variant).map((sample) => sample.ms);
        row[`${variant} median ms`] = round(percentile(times, 0.5));
        row[`${variant} p95 ms`] = round(percentile(times, 0.95));
      }
      console.info(JSON.stringify(row));
      return row;
    });
    // biome-ignore lint/suspicious/noConsole: the opt-in benchmark reports measurements, not pass/fail timing thresholds.
    console.table(rows);
  },
  3_600_000,
);
