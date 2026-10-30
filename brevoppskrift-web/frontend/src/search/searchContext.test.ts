import { describe, expect, it } from "vitest";

import {
  criteriaOf,
  formatResultRef,
  parseResultRef,
  type ResultRef,
  toTemplateSearch,
  toTemplatesSearch,
  validateTemplateSearchContext,
  validateTemplatesSearch,
} from "~/search/searchContext";

const ref: ResultRef = { malType: "autobrev", id: "UT_VEDTAK", language: "BOKMAL" };

describe("searchContext", () => {
  it("round-trips a result reference", () => {
    expect(parseResultRef(formatResultRef(ref))).toEqual(ref);
  });

  it.each([
    ["missing parts", "autobrev/UT_VEDTAK"],
    ["unknown malType", "brev/UT_VEDTAK/BOKMAL"],
    ["empty brevkode", "autobrev//BOKMAL"],
    ["not a string", 12],
  ])("rejects a result reference with %s", (_, value) => {
    expect(parseResultRef(value)).toBeUndefined();
  });

  it("validates /templates params and drops defaults and junk", () => {
    expect(
      validateTemplatesSearch({
        q: "  pensjon ",
        exact: "true",
        tab: "innhold",
        page: "3",
        selected: formatResultRef(ref),
      }),
    ).toEqual({ q: "pensjon", exact: true, tab: "innhold", page: 3, selected: "autobrev/UT_VEDTAK/BOKMAL" });

    expect(validateTemplatesSearch({ q: " ", exact: "nei", tab: "brev", page: "0", selected: "x" })).toEqual({
      q: undefined,
      exact: undefined,
      tab: undefined,
      page: undefined,
      selected: undefined,
    });
  });

  // The router parses search values as JSON where it can.
  it("accepts a query the router parsed as a number or boolean", () => {
    expect(validateTemplatesSearch({ q: 19 }).q).toBe("19");
    expect(validateTemplateSearchContext({ q: true }).q).toBe("true");
  });

  it("has no criteria when nothing was searched for", () => {
    expect(criteriaOf({})).toBeUndefined();
    expect(criteriaOf({ q: "pensjon" })).toEqual({ q: "pensjon", exact: false, tab: "brev" });
  });

  it("builds the template link and the way back from the same criteria", () => {
    const criteria = { q: "pensjon", exact: true, tab: "innhold" as const };

    const toTemplate = toTemplateSearch(criteria, ref, 4);
    expect(toTemplate).toEqual({
      q: "pensjon",
      exact: true,
      tab: "innhold",
      language: "BOKMAL",
      index: 4,
    });
    expect(criteriaOf(validateTemplateSearchContext(toTemplate))).toEqual(criteria);

    expect(toTemplatesSearch(criteria, ref)).toEqual({
      q: "pensjon",
      exact: true,
      tab: "innhold",
      selected: "autobrev/UT_VEDTAK/BOKMAL",
    });
  });

  it("leaves default criteria out of the URL", () => {
    expect(toTemplatesSearch({ q: "pensjon", exact: false, tab: "brev" })).toEqual({
      q: "pensjon",
      exact: undefined,
      tab: undefined,
      selected: undefined,
    });
  });
});
