import { describe, expect, it } from "vitest";

import {
  type HighlightParts,
  matchTextFlow,
  mergeRanges,
  type TextFlow,
  textFlowHasMatch,
} from "~/search/matchTextFlow";

function text(source: string): TextFlow<string> {
  return { type: "text", source, text: source };
}

describe("incremental text-flow matching", () => {
  const eligibilityCases: {
    description: string;
    flow: TextFlow<string>[];
    query: string;
    exact: boolean;
    expected: boolean;
  }[] = [
    {
      description: "fuzzy terms across a variable",
      flow: [text("barnetilleg"), { type: "variable" }, text(" uføretrygd")],
      query: "barnetillegg uføretrygd",
      exact: false,
      expected: true,
    },
    {
      description: "an exact phrase across a variable",
      flow: [text("barnetillegg"), { type: "variable" }, text(" uføretrygd")],
      query: "barnetillegg uføretrygd",
      exact: true,
      expected: false,
    },
    {
      description: "fuzzy terms across a table cell",
      flow: [text("barnetillegg"), { type: "cell" }, text("uføretrygd")],
      query: "barnetillegg uføretrygd",
      exact: false,
      expected: true,
    },
    {
      description: "an exact phrase across a table cell",
      flow: [text("barnetillegg"), { type: "cell" }, text("uføretrygd")],
      query: "barnetillegg uføretrygd",
      exact: true,
      expected: false,
    },
    {
      description: "mutually exclusive branch terms",
      flow: [{ type: "branches", branches: [[text("barnetillegg")], [text("uføretrygd")]] }],
      query: "barnetillegg uføretrygd",
      exact: false,
      expected: false,
    },
    {
      description: "normalized exact whitespace",
      flow: [text("Du får    barnetillegg")],
      query: "får barnetillegg",
      exact: true,
      expected: true,
    },
    {
      description: "symbol-only exact matching",
      flow: [text("Henvisning til § 12")],
      query: "§",
      exact: false,
      expected: true,
    },
  ];

  it.each(eligibilityCases)("range-free matching preserves $description", ({ flow, query, exact, expected }) => {
    const planned = matchTextFlow(flow, query, exact);

    expect(planned.fullMatch).toBe(expected);
    expect(textFlowHasMatch(flow, query, exact)).toBe(planned.fullMatch);
  });

  it.each([false, true])(
    "handles 35 optional siblings without expanding their combinations (exact: %s)",
    (exact) => {
      const flow: TextFlow<string>[] = [
        text("begin "),
        ...Array.from(
          { length: 35 },
          (_, index): TextFlow<string> => ({
            type: "branches",
            branches: [[text(`optional${index} `)], []],
          }),
        ),
        text("end"),
      ];

      const result = matchTextFlow(flow, "begin end", exact);

      expect(result.fullMatch).toBe(true);
      expect([...result.parts].filter(([, parts]) => parts.ranges.length > 0).map(([source]) => source)).toEqual([
        "begin ",
        "end",
      ]);
    },
    2000,
  );

  it("supports more than 32 terms without enumerating coverage subsets", () => {
    const terms = Array.from({ length: 40 }, (_, index) => String.fromCharCode(0x410 + index));
    const result = matchTextFlow(
      terms.map((term) => ({ type: "branches", branches: [[text(term)], []] })),
      terms.join(" "),
    );

    expect(result.fullMatch).toBe(true);
    expect([...result.parts.values()].every((parts) => parts.fullMatch)).toBe(true);
  });

  it("cannot assemble those terms from mutually exclusive alternatives", () => {
    const terms = Array.from({ length: 40 }, (_, index) => String.fromCharCode(0x410 + index));
    const result = matchTextFlow([{ type: "branches", branches: terms.map((term) => [text(term)]) }], terms.join(" "));

    expect(result.fullMatch).toBe(false);
    expect([...result.parts.values()].every((parts) => !parts.fullMatch)).toBe(true);
  });

  it("does not attribute a complete match to an incompatible branch", () => {
    const result = matchTextFlow(
      [
        {
          type: "branches",
          branches: [[text("A")], [text("B")]],
        },
        text(" C"),
      ],
      "A C",
    );

    expect(result.fullMatch).toBe(true);
    expect(result.parts.get("A")?.fullMatch).toBe(true);
    expect(result.parts.get("B")?.fullMatch).toBe(false);
    expect(result.parts.get(" C")?.fullMatch).toBe(true);
  });

  it("handles typo-tolerant terms across a conditional boundary", () => {
    const result = matchTextFlow(
      [text("barne"), { type: "branches", branches: [[text("tilleg")], [text("penger")]] }],
      "barnetillegg",
    );

    expect(result.fullMatch).toBe(true);
    expect(result.parts.get("barne")?.ranges).toEqual([{ start: 0, end: 5, terms: [0] }]);
    expect(result.parts.get("tilleg")?.ranges).toEqual([{ start: 0, end: 6, terms: [0] }]);
    expect(result.parts.get("penger")?.ranges).toEqual([]);
  });

  it("does not let a structural boundary in one branch interrupt another branch", () => {
    const flow: TextFlow<string>[] = [
      text("Du får "),
      {
        type: "branches",
        branches: [[text("barnetillegg.")], [{ type: "break" }, text("annen linje"), { type: "break" }]],
      },
    ];

    const result = matchTextFlow(flow, "får barnetillegg", true);
    expect(result.fullMatch).toBe(true);
    expect(result.parts.get("Du får ")?.ranges).toEqual([{ start: 3, end: 6, terms: [0] }]);
    expect(result.parts.get("barnetillegg.")?.fullMatch).toBe(true);
    expect(result.parts.get("annen linje")?.ranges).toEqual([]);
  });

  it.each([false, true])("agrees with independently matching small compatible variants (exact: %s)", (exact) => {
    let source = 0;
    const literal = (text: string): TextFlow<number> => ({ type: "text", source: source++, text });
    const examples: TextFlow<number>[][] = [
      [
        literal("Du får "),
        { type: "branches", branches: [[literal("barnetillegg ")], [{ type: "break" }, literal("Annet")], []] },
        literal("uføretrygd"),
      ],
      [
        literal("barne"),
        { type: "branches", branches: [[literal("til")], [literal("pen")], []] },
        { type: "branches", branches: [[literal("legg")], [literal("leg")], [literal("sjon")]] },
      ],
      [
        literal("barnetillegg "),
        {
          type: "branches",
          branches: [
            [{ type: "variable" }, literal(" uføretrygd")],
            [literal(" og "), { type: "branches", branches: [[literal("uføretrygd")], []] }],
          ],
        },
      ],
    ];
    const variantsOf = (flow: TextFlow<number>[]): TextFlow<number>[][] => {
      let variants: TextFlow<number>[][] = [[]];
      for (const item of flow) {
        const alternatives = item.type === "branches" ? item.branches.flatMap(variantsOf) : [[item]];
        variants = variants.flatMap((variant) => alternatives.map((alternative) => [...variant, ...alternative]));
      }
      return variants;
    };
    for (const flow of examples) {
      for (const query of ["får barnetillegg", "barnetillegg uføretrygd", "barnetillegg", "barnetileg"]) {
        const expected = new Map<number, HighlightParts>();
        let fullMatch = false;
        for (const variant of variantsOf(flow)) {
          const result = matchTextFlow(variant, query, exact);
          fullMatch ||= result.fullMatch;
          for (const [source, parts] of result.parts) {
            const previous = expected.get(source);
            expected.set(source, {
              ranges: mergeRanges([...(previous?.ranges ?? []), ...parts.ranges]),
              fullMatch: !!previous?.fullMatch || parts.fullMatch,
            });
          }
        }
        expect(matchTextFlow(flow, query, exact), query).toEqual({ parts: expected, fullMatch });
      }
    }
  });
});
