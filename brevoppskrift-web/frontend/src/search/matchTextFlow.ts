import {
  FUZZY_MATCH_OPTIONS,
  isFuzzyTerm,
  normalizeForExactMatch,
  queryTerms,
  SPECIAL_CHAR_QUERY,
} from "~/search/matchingRules";

export type HighlightRange = { start: number; end: number; terms: number[] };
export type HighlightParts = { ranges: HighlightRange[]; fullMatch: boolean };

export type TextFlow<T> =
  | { type: "text"; source: T; text: string }
  | { type: "separator"; text: string }
  | { type: "branches"; branches: TextFlow<T>[][] }
  | { type: "variable" }
  | { type: "break" }
  | { type: "space" };

/** Reduced decision diagrams keep branch compatibility symbolic, not a list of document variants. */
class Conditions {
  private nodes = [
    { variable: Infinity, low: 0, high: 0 },
    { variable: Infinity, low: 1, high: 1 },
  ];
  private unique = new Map<string, number>();
  private intersections = new Map<string, number>();
  private complements = new Map<number, number>([
    [0, 1],
    [1, 0],
  ]);
  private variables = 0;

  variable(): number {
    return this.node(this.variables++, 0, 1);
  }

  private node(variable: number, low: number, high: number): number {
    if (low === high) {
      return low;
    }
    const key = `${variable}:${low}:${high}`;
    const previous = this.unique.get(key);
    if (previous !== undefined) {
      return previous;
    }
    const id = this.nodes.length;
    this.nodes.push({ variable, low, high });
    this.unique.set(key, id);
    return id;
  }

  not(value: number): number {
    const previous = this.complements.get(value);
    if (previous !== undefined) {
      return previous;
    }
    const { variable, low, high } = this.nodes[value];
    const result = this.node(variable, this.not(low), this.not(high));
    this.complements.set(value, result);
    this.complements.set(result, value);
    return result;
  }

  and(a: number, b: number): number {
    if (a === 0 || b === 0) {
      return 0;
    }
    if (a === 1 || a === b) {
      return b;
    }
    if (b === 1) {
      return a;
    }
    if (a > b) {
      [a, b] = [b, a];
    }
    const key = `${a}:${b}`;
    const previous = this.intersections.get(key);
    if (previous !== undefined) {
      return previous;
    }
    const left = this.nodes[a];
    const right = this.nodes[b];
    const variable = Math.min(left.variable, right.variable);
    const result = this.node(
      variable,
      this.and(left.variable === variable ? left.low : a, right.variable === variable ? right.low : b),
      this.and(left.variable === variable ? left.high : a, right.variable === variable ? right.high : b),
    );
    this.intersections.set(key, result);
    return result;
  }

  or(a: number, b: number): number {
    return this.not(this.and(this.not(a), this.not(b)));
  }
}

type Pattern = { text: string; errors: number };
type Prefix = { term: number; start: number; prefix: number; errors: number; length: number; condition: number };
type Occurrence = { term: number; start: number; end: number; errors: number };
type Origin<T> = {
  position: { source: T; index: number } | undefined;
  character: string;
  condition: number;
};
type LineState = {
  condition: number;
  pendingSpace: number;
  prefixes: Map<string, Prefix>;
  occurrences: Map<number, number>;
};

function prefixKey(prefix: Omit<Prefix, "condition">): string {
  return `${prefix.term}:${prefix.start}:${prefix.prefix}:${prefix.errors}:${prefix.length}`;
}

export function mergeRanges(ranges: HighlightRange[]): HighlightRange[] {
  const sorted = ranges.map((range) => ({ ...range })).sort((a, b) => a.start - b.start);
  const merged: HighlightRange[] = [];
  for (const range of sorted) {
    const last = merged.at(-1);
    if (last && range.start <= last.end) {
      last.end = Math.max(last.end, range.end);
      last.terms = [...new Set([...last.terms, ...range.terms])];
    } else {
      merged.push(range);
    }
  }
  return merged;
}

/** Incremental edit-distance prefixes are bounded by query length and its error budget.
 * Branch choices and term coverage use decision diagrams instead of enumerating paths or term subsets. */
export function matchTextFlow<T>(
  flow: TextFlow<T>[],
  needle: string,
  exact = false,
): { parts: ReadonlyMap<T, HighlightParts>; fullMatch: boolean } {
  const parts = new Map<T, HighlightParts>();
  const query = normalizeForExactMatch(needle);
  if (!query) {
    return { parts, fullMatch: false };
  }
  const phrase = exact || SPECIAL_CHAR_QUERY.test(query);
  const patterns: Pattern[] = (phrase ? [query] : queryTerms(query)).map((text) => ({
    text,
    errors: !phrase && isFuzzyTerm(text) ? Math.floor(text.length * FUZZY_MATCH_OPTIONS.threshold) : 0,
  }));
  const conditions = new Conditions();
  const origins: Origin<T>[] = [];
  const occurrences: Occurrence[] = [];
  const occurrenceIds = new Map<string, number>();
  const transitions = new Map<string, { prefix: number; errors: number }[]>();
  let nextLine = 0;
  let fullMatch = false;

  const emptyLine = (condition: number): LineState => ({
    condition,
    pendingSpace: 0,
    prefixes: new Map(),
    occurrences: new Map(),
  });
  const addPrefix = (target: Map<string, Prefix>, prefix: Prefix) => {
    if (prefix.condition !== 0) {
      const key = prefixKey(prefix);
      target.set(key, {
        ...prefix,
        condition: conditions.or(target.get(key)?.condition ?? 0, prefix.condition),
      });
    }
  };
  const advance = (term: number, prefix: number, errors: number, character: string) => {
    const key = `${term}:${prefix}:${errors}:${character}`;
    const cached = transitions.get(key);
    if (cached) {
      return cached;
    }
    const pattern = patterns[term];
    const result = new Map<string, { prefix: number; errors: number }>();
    const add = (prefix: number, errors: number) => {
      for (let skipped = 0; errors + skipped <= pattern.errors && prefix + skipped <= pattern.text.length; skipped++) {
        result.set(`${prefix + skipped}:${errors + skipped}`, {
          prefix: prefix + skipped,
          errors: errors + skipped,
        });
      }
    };
    for (let skipped = 0; errors + skipped <= pattern.errors && prefix + skipped <= pattern.text.length; skipped++) {
      const position = prefix + skipped;
      const cost = errors + skipped;
      if (position < pattern.text.length) {
        add(position + 1, cost + Number(pattern.text[position] !== character));
      }
      add(position, cost + 1);
    }
    const values = [...result.values()];
    transitions.set(key, values);
    return values;
  };
  const characterAt = (line: LineState, origin: number, present: number): LineState => {
    if (present === 0) {
      return line;
    }
    const prefixes = new Map<string, Prefix>();
    const found = line.occurrences;
    const consume = (previous: Prefix) => {
      const condition = conditions.and(previous.condition, present);
      const pattern = patterns[previous.term];
      if (!condition || previous.length >= pattern.text.length + pattern.errors) {
        return;
      }
      for (const next of advance(previous.term, previous.prefix, previous.errors, origins[origin].character)) {
        const prefix = { ...previous, ...next, length: previous.length + 1, condition };
        addPrefix(prefixes, prefix);
        if (next.prefix === pattern.text.length) {
          const key = `${prefix.term}:${prefix.start}:${origin}:${prefix.errors}`;
          let id = occurrenceIds.get(key);
          if (id === undefined) {
            id = occurrences.length;
            occurrenceIds.set(key, id);
            occurrences.push({ term: prefix.term, start: prefix.start, end: origin, errors: prefix.errors });
          }
          found.set(id, conditions.or(found.get(id) ?? 0, condition));
        }
      }
    };
    for (const prefix of line.prefixes.values()) {
      addPrefix(prefixes, { ...prefix, condition: conditions.and(prefix.condition, conditions.not(present)) });
      consume(prefix);
    }
    patterns.forEach((_, term) => {
      consume({ term, start: origin, prefix: 0, errors: 0, length: 0, condition: present });
    });
    return { ...line, prefixes, occurrences: found };
  };
  const mark = (occurrence: Occurrence, condition: number, complete: number) => {
    const pattern = patterns[occurrence.term];
    const ranges = new Map<T, HighlightRange>();
    let before = condition;
    let inRun = 0;
    for (let index = occurrence.start; index <= occurrence.end; index++) {
      const origin = origins[index];
      const present = conditions.and(origin.condition, condition);
      let selected = present;
      if (occurrence.errors > 0) {
        if (pattern.text.includes(origin.character)) {
          selected = conditions.and(present, conditions.or(before, inRun));
          before = conditions.and(before, conditions.not(present));
          inRun = conditions.or(inRun, selected);
        } else {
          inRun = conditions.and(inRun, conditions.not(present));
          selected = 0;
        }
      }
      if (selected && origin.position) {
        const { source, index } = origin.position;
        const part = parts.get(source);
        if (part) {
          const previous = ranges.get(source);
          ranges.set(source, {
            start: Math.min(previous?.start ?? index, index),
            end: Math.max(previous?.end ?? 0, index + 1),
            terms: [occurrence.term],
          });
          part.fullMatch ||= conditions.and(selected, complete) !== 0;
        }
      }
    }
    for (const [source, range] of ranges) {
      parts.get(source)?.ranges.push(range);
    }
  };
  const finishLine = (line: LineState) => {
    const selected: { occurrence: Occurrence; condition: number }[] = [];
    const coverage = patterns.map(() => 0);
    const ordered = [...line.occurrences]
      .map(([id, condition]) => ({ occurrence: occurrences[id], condition: conditions.and(condition, line.condition) }))
      .sort(
        (a, b) =>
          a.occurrence.errors - b.occurrence.errors ||
          a.occurrence.start - b.occurrence.start ||
          b.occurrence.end - a.occurrence.end,
      );
    let exactSpans: { term: number; end: number; condition: number }[] = [];
    for (const { occurrence, condition } of ordered) {
      let allowed = condition;
      if (occurrence.errors > 0) {
        allowed = conditions.and(allowed, conditions.not(coverage[occurrence.term]));
      } else {
        exactSpans = exactSpans.filter((previous) => previous.end >= occurrence.start);
        for (const previous of exactSpans) {
          if (previous.term === occurrence.term && previous.end >= occurrence.start) {
            allowed = conditions.and(allowed, conditions.not(previous.condition));
          }
        }
        exactSpans.push({ term: occurrence.term, end: occurrence.end, condition: allowed });
      }
      if (allowed) {
        coverage[occurrence.term] = conditions.or(coverage[occurrence.term], allowed);
        selected.push({ occurrence, condition: allowed });
      }
    }
    const complete = coverage.reduce((a, b) => conditions.and(a, b), line.condition);
    fullMatch ||= complete !== 0;
    for (const { occurrence, condition } of selected) {
      mark(occurrence, condition, complete);
    }
  };
  const restrict = (line: LineState, guard: number): LineState => ({
    condition: conditions.and(line.condition, guard),
    pendingSpace: conditions.and(line.pendingSpace, guard),
    prefixes: new Map(
      [...line.prefixes].map(([key, prefix]) => [
        key,
        { ...prefix, condition: conditions.and(prefix.condition, guard) },
      ]),
    ),
    occurrences: new Map([...line.occurrences].map(([id, condition]) => [id, conditions.and(condition, guard)])),
  });
  const walk = (items: TextFlow<T>[], initial: Map<number, LineState>): Map<number, LineState> => {
    let lines = initial;
    for (const item of items) {
      if (item.type === "branches") {
        const joined = new Map<number, LineState>();
        let remaining = 1;
        item.branches.forEach((branch, index) => {
          const guard =
            index === item.branches.length - 1 ? remaining : conditions.and(remaining, conditions.variable());
          remaining = conditions.and(remaining, conditions.not(guard));
          const incoming = new Map([...lines].map(([id, line]) => [id, restrict(line, guard)]));
          for (const [id, line] of walk(branch, incoming)) {
            const previous = joined.get(id);
            if (!previous) {
              joined.set(id, line);
            } else {
              const prefixes = new Map(previous.prefixes);
              for (const prefix of line.prefixes.values()) {
                addPrefix(prefixes, prefix);
              }
              const found = new Map(previous.occurrences);
              for (const [id, condition] of line.occurrences) {
                found.set(id, conditions.or(found.get(id) ?? 0, condition));
              }
              joined.set(id, {
                condition: conditions.or(previous.condition, line.condition),
                pendingSpace: conditions.or(previous.pendingSpace, line.pendingSpace),
                prefixes,
                occurrences: found,
              });
            }
          }
        });
        lines = joined;
      } else if (item.type === "break") {
        let condition = 0;
        for (const line of lines.values()) {
          finishLine(line);
          condition = conditions.or(condition, line.condition);
        }
        lines = new Map([[++nextLine, emptyLine(condition)]]);
      } else if (item.type === "variable") {
        lines = new Map([...lines].map(([id, line]) => [id, { ...line, pendingSpace: 0, prefixes: new Map() }]));
      } else if (item.type === "space") {
        lines = new Map([...lines].map(([id, line]) => [id, { ...line, pendingSpace: line.condition }]));
      } else {
        if (item.type === "text" && !parts.has(item.source)) {
          parts.set(item.source, { ranges: [], fullMatch: false });
        }
        for (let index = 0; index < item.text.length; index++) {
          if (/\s/.test(item.text[index])) {
            lines = new Map([...lines].map(([id, line]) => [id, { ...line, pendingSpace: line.condition }]));
            continue;
          }
          const position = item.type === "text" ? { source: item.source, index } : undefined;
          for (const character of item.text[index].toLowerCase()) {
            let spaces = 0;
            let present = 0;
            for (const line of lines.values()) {
              spaces = conditions.or(spaces, line.pendingSpace);
              present = conditions.or(present, line.condition);
            }
            if (spaces) {
              const origin = origins.length;
              origins.push({ position, character: " ", condition: spaces });
              lines = new Map(
                [...lines].map(([id, line]) => [
                  id,
                  { ...characterAt(line, origin, line.pendingSpace), pendingSpace: 0 },
                ]),
              );
            }
            const origin = origins.length;
            origins.push({ position, character, condition: present });
            lines = new Map([...lines].map(([id, line]) => [id, characterAt(line, origin, line.condition)]));
          }
        }
      }
    }
    return lines;
  };
  for (const line of walk(flow, new Map([[0, emptyLine(1)]]))) {
    finishLine(line[1]);
  }
  for (const part of parts.values()) {
    part.ranges = mergeRanges(part.ranges);
  }
  return { parts, fullMatch };
}
