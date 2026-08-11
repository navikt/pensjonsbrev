// @vitest-environment node
import { describe, expect, it } from "vitest";

import { buildIndex, type Line, search, type TemplateText } from "~/search/textSearch";

function textLine(value: string): Line {
  return [{ type: "text", value }];
}

function template(
  overrides: Partial<Omit<TemplateText, "lines">> & { id: string; title: string; lines: string[] },
): TemplateText {
  return {
    malType: "autobrev",
    language: "BOKMAL",
    indexes: overrides.lines.map((_, i) => i),
    ...overrides,
    lines: overrides.lines.map(textLine),
  };
}

describe("search", () => {
  it("finds an exact substring match in the content", () => {
    const templates = [
      template({ id: "A1", title: "Alderspensjon", lines: ["Vi har beregnet din alderspensjon"] }),
      template({ id: "A2", title: "Uføretrygd", lines: ["Vi har vurdert din søknad om uføretrygd"] }),
    ];
    const index = buildIndex(templates);

    const { content } = search(index, "alderspensjon");

    expect(content).toHaveLength(1);
    expect(content[0].template.id).toBe("A1");
  });

  it("tolerates a single typo per term", () => {
    const templates = [template({ id: "A1", title: "Alderspensjon", lines: ["Vi har beregnet din alderspensjon"] })];
    const index = buildIndex(templates);

    const { content } = search(index, "alderspenjson"); // transposed letters

    expect(content).toHaveLength(1);
    expect(content[0].template.id).toBe("A1");
  });

  it("requires every term to match (AND), regardless of order", () => {
    const templates = [
      template({ id: "BOTH", title: "Begge deler", lines: ["Termin for utbetaling er satt til den 20."] }),
      template({ id: "ONLY_ONE", title: "Bare ett", lines: ["Vi har beregnet terminbeløpet ditt."] }),
    ];
    const index = buildIndex(templates);

    const forward = search(index, "termin utbetaling");
    const reversed = search(index, "utbetaling termin");

    expect(forward.content.map((hit) => hit.template.id)).toEqual(["BOTH"]);
    expect(reversed.content.map((hit) => hit.template.id)).toEqual(["BOTH"]);
  });

  it("ranks an exact match above a fuzzy (typo) match of the same term", () => {
    const templates = [
      // "etterbetaling" with one letter swapped - still within the typo threshold.
      template({ id: "FUZZY", title: "Fuzzy treff", lines: ["Dette gjelder etterbetlaing av pensjon."] }),
      template({ id: "EXACT", title: "Eksakt treff", lines: ["Dette gjelder etterbetaling av pensjon."] }),
    ];
    const index = buildIndex(templates);

    const { content } = search(index, "etterbetaling");

    expect(content.map((hit) => hit.template.id)).toEqual(["EXACT", "FUZZY"]);
  });

  it("counts every matching line per template", () => {
    const templates = [
      template({
        id: "MULTI",
        title: "Flere avsnitt",
        lines: ["Første avsnitt om barnetillegg.", "Noe uavhengig tekst.", "Andre avsnitt om barnetillegg òg."],
      }),
    ];
    const index = buildIndex(templates);

    const { content } = search(index, "barnetillegg");

    expect(content).toHaveLength(1);
    expect(content[0].matchCount).toBe(2);
  });

  it("matches on title and brevkode for brev hits", () => {
    const templates = [
      template({ id: "PE_ETTER_01", title: "Etterbetaling av alderspensjon", lines: ["Uinteressant tekst."] }),
      template({ id: "PE_INNV_01", title: "Innvilgelse av alderspensjon", lines: ["Uinteressant tekst."] }),
    ];
    const index = buildIndex(templates);

    const byTitle = search(index, "etterbetaling");
    const byBrevkode = search(index, "PE_INNV_01");

    expect(byTitle.brev.map((hit) => hit.template.id)).toEqual(["PE_ETTER_01"]);
    expect(byBrevkode.brev.map((hit) => hit.template.id)).toEqual(["PE_INNV_01"]);
  });

  it("returns no results for an empty query", () => {
    const templates = [template({ id: "A1", title: "Alderspensjon", lines: ["Litt tekst her."] })];
    const index = buildIndex(templates);

    const { content, brev } = search(index, "   ");

    expect(content).toEqual([]);
    expect(brev).toEqual([]);
  });
});
