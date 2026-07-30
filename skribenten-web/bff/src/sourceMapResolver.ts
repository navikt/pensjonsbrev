import fs from "node:fs";
import path from "node:path";

import { originalPositionFor, TraceMap } from "@jridgewell/trace-mapping";

// Sourcemaps for the built frontend are copied here at build time (see
// .github/workflows/.build-frontend.yaml) and baked into the docker image.
// They are intentionally kept out of ./public so they are never served to
// the browser - they are only used here, server-side, to resolve minified
// stack traces reported by the frontend before they are written to the log.
const sourcemapsRoot = path.resolve("./sourcemaps");

// Matches a stack frame referencing one of our built assets, e.g.
// "at Component (https://skribenten.intern.dev.nav.no/assets/index-CzilmHAy.js:1:12345)"
// or "at https://skribenten.intern.dev.nav.no/assets/index-CzilmHAy.js:1:12345".
const STACK_FRAME_REGEX = /https?:\/\/[^\s)]+(\/assets\/[^\s):]+\.js):(\d+):(\d+)/;

const traceMapCache = new Map<string, TraceMap | undefined>();

function loadTraceMap(assetPath: string): TraceMap | undefined {
  if (traceMapCache.has(assetPath)) {
    return traceMapCache.get(assetPath);
  }

  let traceMap: TraceMap | undefined;
  try {
    const mapPath = path.join(sourcemapsRoot, `${assetPath}.map`);
    const rawSourceMap = fs.readFileSync(mapPath, "utf-8");
    traceMap = new TraceMap(rawSourceMap);
  } catch {
    // No sourcemap available for this asset (e.g. running locally without a
    // build, or an unrecognized/legacy asset) - fall back to the raw frame.
    traceMap = undefined;
  }

  traceMapCache.set(assetPath, traceMap);
  return traceMap;
}

function resolveStackFrame(line: string): string {
  const match = STACK_FRAME_REGEX.exec(line);
  if (!match) {
    return line;
  }

  const [, assetPath, lineNumber, columnNumber] = match;
  const traceMap = loadTraceMap(assetPath);
  if (!traceMap) {
    return line;
  }

  const originalPosition = originalPositionFor(traceMap, {
    line: Number.parseInt(lineNumber, 10),
    column: Number.parseInt(columnNumber, 10),
  });

  if (!originalPosition.source) {
    return line;
  }

  const originalLocation = `${originalPosition.source}:${originalPosition.line}:${originalPosition.column}`;
  return originalPosition.name
    ? line.replace(match[0], `${originalLocation} (${originalPosition.name})`)
    : line.replace(match[0], originalLocation);
}

/**
 * Resolves a minified stack trace produced by the built (minified) frontend
 * bundle back to its original source locations, using the sourcemaps that
 * were generated at build time. Falls back to the untouched frame whenever
 * a sourcemap can't be found or a position can't be resolved.
 */
export function resolveStackTrace(stack: string | undefined): string | undefined {
  if (!stack) {
    return stack;
  }

  return stack
    .split("\n")
    .map((line) => resolveStackFrame(line))
    .join("\n");
}
