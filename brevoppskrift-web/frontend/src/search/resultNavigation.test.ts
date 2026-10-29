import { describe, expect, it } from "vitest";

import { isSelectedResult, pageOfResult, resultPosition, templatesInOrder } from "~/search/resultNavigation";
import { type ResultRef } from "~/search/searchContext";

function result(id: string, language: string): ResultRef {
  return { malType: "autobrev", id, language };
}

const aBokmal = result("A", "BOKMAL");
const aNynorsk = result("A", "NYNORSK");
const bBokmal = result("B", "BOKMAL");
const cNynorsk = result("C", "NYNORSK");
const bEnglish = result("B", "ENGLISH");
const ranked = [aBokmal, bBokmal, aNynorsk, cNynorsk, bEnglish];

describe("templatesInOrder", () => {
  it("groups the languages of each template, in template rank order", () => {
    expect(templatesInOrder(ranked)).toEqual([[aBokmal, aNynorsk], [bBokmal, bEnglish], [cNynorsk]]);
  });
});

describe("resultPosition", () => {
  it("counts templates, not language versions", () => {
    expect(resultPosition(ranked, bEnglish)).toMatchObject({ position: 2, total: 3 });
    expect(resultPosition(ranked, result("B", "NYNORSK"))).toMatchObject({ position: 2, total: 3 });
  });

  it("opens neighbours in the current language if they matched there, else in their best-ranked language", () => {
    expect(resultPosition(ranked, bBokmal)).toMatchObject({ previous: aBokmal, next: cNynorsk });
    expect(resultPosition(ranked, result("B", "NYNORSK"))).toMatchObject({ previous: aNynorsk, next: cNynorsk });
  });

  it("has no previous at the start and no next at the end", () => {
    expect(resultPosition(ranked, aBokmal)).toMatchObject({ previous: undefined, next: bBokmal });
    expect(resultPosition(ranked, cNynorsk)).toMatchObject({ previous: bBokmal, next: undefined });
  });

  it("is undefined for a template that is not among the results", () => {
    expect(resultPosition(ranked, result("X", "BOKMAL"))).toBeUndefined();
  });
});

describe("selection in the result list", () => {
  it("matches one language per row for content results, and any language for grouped rows", () => {
    expect(isSelectedResult(aBokmal, aNynorsk, false)).toBe(false);
    expect(isSelectedResult(aBokmal, aNynorsk, true)).toBe(true);
    expect(isSelectedResult(aBokmal, undefined, true)).toBe(false);
  });

  it("finds the page that shows the selected result", () => {
    expect(pageOfResult(ranked, cNynorsk, 2, false)).toBe(2);
    expect(pageOfResult(ranked, bEnglish, 2, false)).toBe(3);
    expect(pageOfResult(ranked, result("A", "ENGLISH"), 2, true)).toBe(1);
    expect(pageOfResult(ranked, result("X", "BOKMAL"), 2, false)).toBeUndefined();
  });
});
