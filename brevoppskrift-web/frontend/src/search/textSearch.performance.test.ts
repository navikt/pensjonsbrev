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
  type TemplateText,
} from "~/search/textSearch";

// npm run bench:search; optionally set SEARCH_BENCHMARK_CORPUS to a saved /templates/{malType}/all response
// (or a concatenation of several) and SEARCH_BENCHMARK_SAMPLES to the number of paired samples (default 20).
// Measures what verifying fuzzy candidates with textFlowHasMatch would cost, and how many candidates it rejects.
// Covers content candidate selection only, excluding indexing, hit aggregation, metadata search and rendering.
const QUERIES = ["pensjon", "barnetillegg", "barnetillegg uføretrygd", "barnetileg uføretrygd", "folketrygdloven §"];
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

function measure(index: SearchIndex, query: string, verify: boolean) {
  const start = performance.now();
  const candidates = index.contentFuse.search(query);
  const afterFuse = performance.now();
  const terms = queryTerms(query);
  const matches = [];
  let checked = 0;
  for (const result of candidates) {
    const { item } = result;
    if (hasShortTermsVerbatim(terms, [item.text])) {
      checked++;
      if (!verify || textFlowHasMatch(lineFlow(item.template.lines[item.lineIndex]), query, false)) {
        matches.push(result);
      }
    }
  }
  const end = performance.now();
  return {
    candidates: candidates.length,
    checked,
    matches,
    fuseMs: afterFuse - start,
    filterMs: end - afterFuse,
    totalMs: end - start,
  };
}

function percentile(values: number[], fraction: number): number {
  const sorted = [...values].sort((a, b) => a - b);
  return sorted[Math.ceil(sorted.length * fraction) - 1];
}

const ms = (value: number) => Number(value.toFixed(2));

it.runIf(process.env.SEARCH_BENCHMARK === "1")(
  "compares fuzzy candidate selection with and without textFlowHasMatch verification",
  () => {
    const corpus = loadCorpus();
    const index = buildIndex(corpus);
    console.info(
      `${process.env.SEARCH_BENCHMARK_CORPUS ? "Exported" : "SYNTHETIC"} corpus: ${corpus.length} templates, ` +
        `${index.contentRecords.length} searchable lines. ${SAMPLES} paired samples, two warm-up pairs, alternating order. ` +
        `${process.version}, ${process.platform}/${process.arch}.`,
    );
    const rows = QUERIES.map((query) => {
      for (let warmup = 0; warmup < 2; warmup++) {
        measure(index, query, false);
        measure(index, query, true);
      }
      const without: ReturnType<typeof measure>[] = [];
      const withCheck: ReturnType<typeof measure>[] = [];
      for (let sample = 0; sample < SAMPLES; sample++) {
        if (sample % 2 === 0) {
          without.push(measure(index, query, false));
          withCheck.push(measure(index, query, true));
        } else {
          withCheck.push(measure(index, query, true));
          without.push(measure(index, query, false));
        }
      }
      const row = {
        query,
        candidates: withCheck[0].candidates,
        checked: withCheck[0].checked,
        rejected: without[0].matches.length - withCheck[0].matches.length,
        "Fuse median ms": ms(
          percentile(
            [...without, ...withCheck].map((sample) => sample.fuseMs),
            0.5,
          ),
        ),
        "without median ms": ms(
          percentile(
            without.map((sample) => sample.totalMs),
            0.5,
          ),
        ),
        "with median ms": ms(
          percentile(
            withCheck.map((sample) => sample.totalMs),
            0.5,
          ),
        ),
        "added filter median ms": ms(
          percentile(
            withCheck.map((sample, index) => sample.filterMs - without[index].filterMs),
            0.5,
          ),
        ),
        "without p95 ms": ms(
          percentile(
            without.map((sample) => sample.totalMs),
            0.95,
          ),
        ),
        "with p95 ms": ms(
          percentile(
            withCheck.map((sample) => sample.totalMs),
            0.95,
          ),
        ),
      };
      console.info(JSON.stringify(row));
      return row;
    });
    // biome-ignore lint/suspicious/noConsole: the opt-in benchmark reports measurements, not pass/fail timing thresholds.
    console.table(rows);
  },
  3_600_000,
);
