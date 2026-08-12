// @vitest-environment node
import { renderToStaticMarkup } from "react-dom/server";
import { describe, expect, it } from "vitest";

import { type Expr, ExprType } from "~/api/brevbakerTypesV2";
import { ExprToText } from "~/components/TemplateDocumentationV2View";

function render(expr: Expr): string {
  return renderToStaticMarkup(<ExprToText expr={expr} />);
}

/** Fjerner HTML-tagger, slik at assertions kan sammenlignes mot den rene teksten som vises. */
function text(html: string): string {
  return html.replace(/<[^>]+>/g, "");
}

describe("ExprToText FieldPath med Computed-base", () => {
  it("rendrer getOrNull som array-access og segmentene som en tydelig avgrenset postfix-kjede", () => {
    // Eksakt strukturen fra legacy-selectoren
    // vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat: getOrNull(...)
    // etterfulgt av .vilkar.unguforresultat, pakket i en NullCoalesce med tom streng-fallback.
    const expr: Expr = {
      exprType: ExprType.NULL_COALESCE,
      value: {
        exprType: ExprType.FIELD_PATH,
        source: {
          dataSourceType: "COMPUTED",
          expr: {
            exprType: ExprType.FUNCTION_CALL,
            name: "getOrNull",
            args: [
              {
                exprType: ExprType.FIELD_PATH,
                source: { dataSourceType: "SCOPE", name: "argument" },
                segments: ["pesysData", "pe", "vedtaksbrev", "vedtaksdata", "vilkarsvedtaklist", "vilkarsvedtak"],
                leafType: null,
              },
              { exprType: ExprType.LITERAL, value: "0", kind: "NUMBER" },
            ],
          },
        },
        segments: ["vilkar", "unguforresultat"],
        leafType: null,
      },
      fallback: { exprType: ExprType.LITERAL, value: "", kind: "STRING" },
    };

    const html = render(expr);
    const rendered = text(html);

    // Listen som indekseres skal være tydelig avgrenset til getOrNull sitt første argument
    // (`...vilkarsvedtak[0]`) - de påfølgende `.vilkar.unguforresultat`-segmentene skal IKKE
    // se ut til å høre til selve listeuttrykket. "argument."-prefikset skal være skjult, siden
    // praktisk talt alle uttrykk starter der og det bare er støy.
    expect(rendered).toContain(
      "pesysData.pe.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak[0].vilkar.unguforresultat",
    );
    expect(rendered).not.toContain("argument.");
    // Ingen prosafrase ("element nr. ... i ...") som ikke tydeliggjør grensen mellom
    // funksjonskallet og de påfølgende segmentene.
    expect(rendered).not.toContain("element nr.");
    // Tom streng-fallback skal fortsatt være synlig (kvotert).
    expect(rendered).toContain("&quot;&quot;");
  });
});

describe("ExprToText FieldPath argument-scope", () => {
  it("skjuler 'argument.'-prefikset siden praktisk talt alle uttrykk starter der", () => {
    const expr: Expr = {
      exprType: ExprType.FIELD_PATH,
      source: { dataSourceType: "SCOPE", name: "argument" },
      segments: ["person", "navn"],
      leafType: null,
    };

    expect(text(render(expr))).toBe("person.navn");
  });

  it("beholder navnet til andre scopes enn 'argument' (f.eks. 'felles')", () => {
    const expr: Expr = {
      exprType: ExprType.FIELD_PATH,
      source: { dataSourceType: "SCOPE", name: "felles" },
      segments: ["dato"],
      leafType: null,
    };

    expect(text(render(expr))).toBe("felles.dato");
  });

  it("faller tilbake til 'argument' når det ikke er noen segmenter å vise (hele scopet brukt direkte)", () => {
    const expr: Expr = {
      exprType: ExprType.FIELD_PATH,
      source: { dataSourceType: "SCOPE", name: "argument" },
      segments: [],
      leafType: null,
    };

    expect(text(render(expr))).toBe("argument");
  });
});

function fieldPath(name: string): Expr {
  return {
    exprType: ExprType.FIELD_PATH,
    source: { dataSourceType: "SCOPE", name: "argument" },
    segments: [name],
    leafType: null,
  };
}

describe("ExprToText linjeskift og innrykk for AND/OR-kjeder", () => {
  it("rendrer en flat AND-kjede med flere ledd som en blokk med én rad per ledd, uten nøstet border/innrykk", () => {
    const expr: Expr = {
      exprType: ExprType.ASSOCIATIVE_OP,
      op: "AND",
      operands: [fieldPath("a"), fieldPath("b"), fieldPath("c")],
    };

    const html = render(expr);
    // Kun én blokk (ingen nøsting) siden alle tre ledd er "flate" FieldPath-er.
    expect((html.match(/class="expr-block"/g) ?? []).length).toBe(1);
    expect((html.match(/class="expr-block-row"/g) ?? []).length).toBe(3);
    expect(text(html)).toBe("aog bog c");
  });

  it("rendrer en nøstet OR inni en AND som sin egen innrykkede blokk, uten eksplisitte parenteser", () => {
    const expr: Expr = {
      exprType: ExprType.ASSOCIATIVE_OP,
      op: "AND",
      operands: [
        fieldPath("a"),
        { exprType: ExprType.ASSOCIATIVE_OP, op: "OR", operands: [fieldPath("b"), fieldPath("c")] },
      ],
    };

    const html = render(expr);
    // To nøstede blokker: den ytre AND-blokken og den indre OR-blokken.
    expect((html.match(/class="expr-block"/g) ?? []).length).toBe(2);
    // Ingen parenteser rundt den nøstede OR-en - innrykket/border kommuniserer grupperingen.
    expect(text(html)).not.toContain("(");
    expect(text(html)).toContain("eller");
  });

  it("holder CONCAT/PLUS-kjeder inline (ingen linjeskift/blokk-formattering)", () => {
    const expr: Expr = {
      exprType: ExprType.ASSOCIATIVE_OP,
      op: "CONCAT",
      operands: [{ exprType: ExprType.LITERAL, value: "Hei ", kind: "STRING" }, fieldPath("navn")],
    };

    const html = render(expr);
    expect(html).not.toContain("expr-block");
  });
});

describe("ExprToText linjeskift og innrykk for Conditional (if/then/else)", () => {
  it("rendrer if/then/else som tre separate rader i en blokk", () => {
    const expr: Expr = {
      exprType: ExprType.CONDITIONAL_EXPR,
      predicate: fieldPath("harBarn"),
      ifTrue: { exprType: ExprType.LITERAL, value: "ja", kind: "STRING" },
      ifElse: { exprType: ExprType.LITERAL, value: "nei", kind: "STRING" },
    };

    const html = render(expr);
    expect((html.match(/class="expr-block-row"/g) ?? []).length).toBe(3);
    const rendered = text(html);
    expect(rendered).toContain("if");
    expect(rendered).toContain("then");
    expect(rendered).toContain("else");
  });
});
