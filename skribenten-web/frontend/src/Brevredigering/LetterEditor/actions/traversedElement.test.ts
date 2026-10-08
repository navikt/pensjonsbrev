import { describe, expect, test } from "vitest";

import {
  mergeNeighbouringText,
  type Text,
  type TraversedElement,
} from "~/Brevredigering/LetterEditor/actions/traversedElement";
import { FontType } from "~/types/brevbakerTypes";

const text = (value: string, font: FontType = FontType.PLAIN): Text => ({ type: "TEXT", font, text: value });

describe("mergeNeighbouringText", () => {
  test("merges neighbouring runs with the same font and collapses whitespace", () => {
    expect(
      mergeNeighbouringText([text("a "), text(" b\n"), text("c"), text("d", FontType.BOLD), text("e", FontType.BOLD)]),
    ).toEqual([text("a b c"), text("de", FontType.BOLD)]);
  });

  test("leaves single runs and other elements untouched", () => {
    const paragraph: TraversedElement = { type: "P", content: [text("p")] };

    expect(mergeNeighbouringText([text("a  b"), paragraph, text("c"), text("d")])).toEqual([
      text("a  b"),
      paragraph,
      text("cd"),
    ]);
  });

  test("does not merge across other elements or different fonts", () => {
    expect(mergeNeighbouringText([text("a"), text("b", FontType.ITALIC), text("c")])).toEqual([
      text("a"),
      text("b", FontType.ITALIC),
      text("c"),
    ]);
  });

  test("returns an empty list for no elements", () => {
    expect(mergeNeighbouringText([])).toEqual([]);
  });

  // A quadratic merge took over half a second for 20 000 runs; RTF from Word easily produces that many.
  test.each([
    ["alternating fonts", (i: number) => text("x", i % 2 === 0 ? FontType.PLAIN : FontType.BOLD), 50_000],
    ["the same font", () => text("x "), 1],
  ])("is linear for 50 000 runs with %s", (_, run, expectedLength) => {
    const elements = Array.from({ length: 50_000 }, (__, i) => run(i));

    const start = performance.now();
    const merged = mergeNeighbouringText(elements);
    const elapsed = performance.now() - start;

    expect(merged).toHaveLength(expectedLength);
    expect(elapsed).toBeLessThan(500);
  });
});
