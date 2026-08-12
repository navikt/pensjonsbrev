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
    // se ut til å høre til selve listeuttrykket.
    expect(rendered).toContain(
      "argument.pesysData.pe.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak[0].vilkar.unguforresultat",
    );
    // Ingen prosafrase ("element nr. ... i ...") som ikke tydeliggjør grensen mellom
    // funksjonskallet og de påfølgende segmentene.
    expect(rendered).not.toContain("element nr.");
    // Tom streng-fallback skal fortsatt være synlig (kvotert).
    expect(rendered).toContain("&quot;&quot;");
  });
});
