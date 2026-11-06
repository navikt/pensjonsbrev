// @vitest-environment node
import { renderToStaticMarkup } from "react-dom/server";
import { describe, expect, it, vi } from "vitest";

import {
  type Conditional,
  type Content,
  type ContentOrControlStructure,
  ContentOrControlStructureType,
  ElementType,
  type Expr,
  type ExprFieldPath,
  ExprType,
  type OutlineContent,
  type ParagraphContent,
  type Row,
  type Text,
} from "~/api/templateDocumentationTypes";
import { Document, ExprToText, isLeafPrimitive } from "~/components/TemplateDocumentationView";
import * as documentMatching from "~/templateSearch/documentMatch";
import { SearchHighlightProvider } from "~/templateSearch/SearchHighlightContext";

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
    // ExprToText er felles for predikater og popovere, der fallbacken påvirker meningen.
    expect(rendered).toContain("?:");
    expect(rendered).toContain("&quot;&quot;");
  });

  it("viser en ikke-tom fallback fortsatt eksplisitt", () => {
    const expr: Expr = {
      exprType: ExprType.NULL_COALESCE,
      value: fieldPath("navn"),
      fallback: { exprType: ExprType.LITERAL, value: "ukjent", kind: "STRING" },
    };
    expect(text(render(expr))).toContain("navn ?: &quot;ukjent&quot;");
  });
});

describe("ExprToText eksempelverdi for tekstfelt", () => {
  const avtaleland: Expr = {
    exprType: ExprType.NULL_COALESCE,
    value: { ...(fieldPath("avtaleland") as ExprFieldPath), exampleText: "Sverige" },
    fallback: { exprType: ExprType.LITERAL, value: "", kind: "STRING" },
  };

  function paragraphText(expression: Expr): string {
    return renderToStaticMarkup(
      <Document
        templateDocumentation={{
          title: [],
          outline: [
            {
              controlStructureType: ContentOrControlStructureType.CONTENT,
              content: {
                elementType: ElementType.PARAGRAPH,
                index: 0,
                paragraph: [
                  {
                    controlStructureType: ContentOrControlStructureType.CONTENT,
                    content: { elementType: ElementType.PARAGRAPH_TEXT_EXPRESSION, expression },
                  },
                ],
              },
            },
          ],
          include: fieldPath("dummy"),
          attachmentData: fieldPath("dummy"),
        }}
      />,
    );
  }

  it("viser eksempelverdien i tekstposisjon, med feltstien tilgjengelig via popover", () => {
    const html = paragraphText(avtaleland);
    expect(buttonText(html)).toBe("Sverige");
    expect(html).toContain('aria-label="Vis fullt uttrykk"');
    expect(text(html)).toContain("avtaleland");
    // Fallbacken skjules i selve teksten, men popoveren viser det fulle uttrykket.
    expect(buttonText(html)).not.toContain("?:");
    expect(html).toContain("?:");
  });

  it("skjuler tom fallback i tekstposisjon også uten eksempelverdi, men ikke i predikater", () => {
    const utenEksempel: Expr = { ...avtaleland, value: fieldPath("avtaleland") };
    const html = paragraphText(utenEksempel);
    expect(html).not.toContain("?:");
    expect(text(html)).toContain("avtaleland");
    const predicate = render({ exprType: ExprType.COMPARISON, left: utenEksempel, op: "EQUAL", right: fieldPath("x") });
    expect(text(predicate)).toContain("avtaleland ?: &quot;&quot;");
  });

  it("viser ikke eksempelverdien i et predikat", () => {
    const html = render({ exprType: ExprType.COMPARISON, left: avtaleland, op: "EQUAL", right: fieldPath("x") });
    expect(html).not.toContain("Sverige");
    expect(html).not.toContain("expr-popover-trigger");
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
  it("rendrer if/then/else som tre separate rader i en blokk (når grenene IKKE begge er literaler)", () => {
    const expr: Expr = {
      exprType: ExprType.CONDITIONAL_EXPR,
      predicate: fieldPath("harBarn"),
      ifTrue: fieldPath("navn"),
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

/** Innholdet i selve `<button>`-markøren, uten den (skjulte) popover-innholdet ved siden av. */
function buttonText(html: string): string {
  const match = /<button[^>]*>(.*?)<\/button>/s.exec(html);
  return match ? text(match[1]) : "";
}

describe("ExprToText kompakt Conditional-visning for rene literal-grener", () => {
  it("rendrer [ifTrue] når ifElse er tom streng, uten if/then/else-blokk", () => {
    const expr: Expr = {
      exprType: ExprType.CONDITIONAL_EXPR,
      predicate: fieldPath("harBarn"),
      ifTrue: { exprType: ExprType.LITERAL, value: "med barnetillegg", kind: "STRING" },
      ifElse: { exprType: ExprType.LITERAL, value: "", kind: "STRING" },
    };

    const html = render(expr);
    expect(html).not.toContain("expr-block");
    expect(html).toContain("expr-popover-trigger");
    expect(buttonText(html)).toBe("[med barnetillegg]");
    // Predikatet skal IKKE vises direkte i markøren (kun tilgjengelig via popover).
    expect(buttonText(html)).not.toContain("harBarn");
    // Popover-innholdet finnes i DOM-en (for tilgjengelighet), men er skjult som standard -
    // vi asserter på VÅR egen kontrakt (popover-triggeren og dens aria-label) i stedet for
    // Aksels interne klassenavn, som kan endre seg ved oppgradering.
    expect(html).toContain('aria-label="Vis betingelse"');
    expect(html).toContain("Hvis");
    expect(html).toContain("harBarn");
  });

  it("rendrer ifTrue|ifElse når begge grener har innhold, uten if/then/else-blokk", () => {
    const expr: Expr = {
      exprType: ExprType.CONDITIONAL_EXPR,
      predicate: fieldPath("erGift"),
      ifTrue: { exprType: ExprType.LITERAL, value: "ja", kind: "STRING" },
      ifElse: { exprType: ExprType.LITERAL, value: "nei", kind: "STRING" },
    };

    const html = render(expr);
    expect(html).not.toContain("expr-block");
    expect(buttonText(html)).toBe("ja|nei");
    expect(buttonText(html)).not.toContain("erGift");
    expect(html).toContain('aria-label="Vis betingelse"');
    expect(html).toContain("erGift");
  });
});

describe("ExprToText kompakt Conditional-visning for formatterte grener", () => {
  it("rendrer eksempeltekst1|eksempeltekst2 når begge grener er Format med exampleText", () => {
    // Strukturen fra OpplysningerBruktIBeregningTabell sin Uføretidspunkt-rad:
    // ifElse(erMndEtterFoedsel, foedselsdato.formatMonthYear(), uforetidspunkt.format())
    const expr: Expr = {
      exprType: ExprType.CONDITIONAL_EXPR,
      predicate: fieldPath("erMndEtterFoedsel"),
      ifTrue: {
        exprType: ExprType.FORMAT,
        value: fieldPath("foedselsdato"),
        formatterName: "MonthYearFormatter",
        exampleText: "mars 2024",
      },
      ifElse: {
        exprType: ExprType.FORMAT,
        value: fieldPath("uforetidspunkt"),
        formatterName: "ShortDateFormat",
        exampleText: "17.03.2024",
      },
    };

    const html = render(expr);
    expect(buttonText(html)).toBe("mars 2024|17.03.2024");
    // Verken predikatet eller feltstiene skal ta plass i selve raden.
    expect(buttonText(html)).not.toContain("erMndEtterFoedsel");
    expect(buttonText(html)).not.toContain("foedselsdato");
    expect(html).toContain('aria-label="Vis betingelse"');
    // Med uttrykks-grener må popoveren vise HELE if/then/else-strukturen, slik at leseren ser
    // hvilket felt hver eksempelverdi stammer fra - ikke bare predikatet.
    expect(html).toContain("expr-block");
    expect(text(html)).toContain("erMndEtterFoedsel");
    expect(text(html)).toContain("foedselsdato");
    expect(text(html)).toContain("formatert som måned og år");
    // Format-grenene inni popoveren skal rendres i full form, ikke som nye popover-markører.
    expect((html.match(/expr-popover-trigger/g) ?? []).length).toBe(1);
  });

  it("rendrer [eksempeltekst] når else-grenen er tom streng", () => {
    const expr: Expr = {
      exprType: ExprType.CONDITIONAL_EXPR,
      predicate: fieldPath("harUforetidspunkt"),
      ifTrue: {
        exprType: ExprType.FORMAT,
        value: fieldPath("uforetidspunkt"),
        formatterName: "ShortDateFormat",
        exampleText: "17.03.2024",
      },
      ifElse: { exprType: ExprType.LITERAL, value: "", kind: "STRING" },
    };

    expect(buttonText(render(expr))).toBe("[17.03.2024]");
  });

  it("faller tilbake til if/then/else-blokk når en gren mangler kompakt tekst", () => {
    const expr: Expr = {
      exprType: ExprType.CONDITIONAL_EXPR,
      predicate: fieldPath("erGift"),
      ifTrue: {
        exprType: ExprType.FORMAT,
        value: fieldPath("ukjentFelt"),
        formatterName: "UkjentFormatter",
        exampleText: null,
      },
      ifElse: { exprType: ExprType.LITERAL, value: "nei", kind: "STRING" },
    };

    const html = render(expr);
    expect(html).not.toContain("expr-popover-trigger");
    expect((html.match(/class="expr-block-row"/g) ?? []).length).toBe(3);
  });
});

describe("ExprToText Format med eksempeltekst", () => {
  it("viser exampleText kompakt med full uttrykk+formatterer tilgjengelig via popover", () => {
    const expr: Expr = {
      exprType: ExprType.FORMAT,
      value: fieldPath("fodselsdato"),
      formatterName: "DateFormat",
      exampleText: "17. mars 2024",
    };

    const html = render(expr);
    expect(html).toContain("expr-popover-trigger");
    expect(buttonText(html)).toBe("17. mars 2024");
    // Den abstrakte "formatert som"-frasen skal IKKE vises i markøren, kun i popoveren.
    expect(buttonText(html)).not.toContain("fodselsdato");
    expect(html).toContain('aria-label="Vis fullt uttrykk"');
    expect(html).toContain("fodselsdato");
  });

  it("faller tilbake til <uttrykk> formatert som <formatterer> når exampleText er null", () => {
    const expr: Expr = {
      exprType: ExprType.FORMAT,
      value: fieldPath("ukjentFelt"),
      formatterName: "UkjentFormatter",
      exampleText: null,
    };

    const html = render(expr);
    expect(html).not.toContain("expr-popover-trigger");
    expect(text(html)).toContain("ukjentFelt");
  });
});

function textLiteralParagraph(text: string): ContentOrControlStructure<OutlineContent> {
  return {
    controlStructureType: ContentOrControlStructureType.CONTENT,
    content: {
      elementType: ElementType.PARAGRAPH,
      index: 0,
      paragraph: [
        {
          controlStructureType: ContentOrControlStructureType.CONTENT,
          content: { elementType: ElementType.PARAGRAPH_TEXT_LITERAL, text },
        },
      ],
    },
  };
}

describe("Document search indexes", () => {
  it("exposes the block index on the rendered paragraph", () => {
    const html = renderToStaticMarkup(
      <Document
        templateDocumentation={{
          title: [],
          outline: [textLiteralParagraph("Søkbar tekst")],
          attachments: [],
          templateModelSpecification: { types: {}, letterModelTypeName: "" },
        }}
      />,
    );

    expect(html).toContain('data-block-index="0"');
  });
});

describe("Document search marking", () => {
  function paragraph(index: number, text: string): ContentOrControlStructure<OutlineContent> {
    const block = textLiteralParagraph(text);
    return block.controlStructureType === ContentOrControlStructureType.CONTENT
      ? { ...block, content: { ...block.content, index } }
      : block;
  }
  const documentation = {
    title: [],
    outline: [
      paragraph(0, "Du får barnetillegg."),
      paragraph(1, "Om barnetillegg og annet."),
      paragraph(2, "Noe helt annet."),
    ],
    attachments: [],
    templateModelSpecification: { types: {}, letterModelTypeName: "" },
  };

  it("marks hits only in matching blocks, and the active block apart from the others", () => {
    const html = renderToStaticMarkup(
      <SearchHighlightProvider
        highlight={{ needle: "barnetillegg", exact: false, matchingBlocks: new Set([1, 2]), activeBlock: 2 }}
      >
        <Document templateDocumentation={documentation} />
      </SearchHighlightProvider>,
    );

    expect(html).not.toMatch(/data-block-index="0"[^>]*data-search-hit/);
    expect(html).toMatch(/data-block-index="1"[^>]*data-search-hit="match"/);
    expect(html).toMatch(/data-block-index="2"[^>]*data-search-hit="active"/);
    // Block 0 has the word too, but is not a hit, so only block 1's is marked.
    expect(html.match(/<mark[^>]*>barnetillegg<\/mark>/g)).toHaveLength(1);
  });

  // E.g. a block kept from another language, where it matched: still easy to
  // find when switching back and forth, but nothing in it is marked.
  it("outlines the active block even when it is not a hit in this language, without marking its words", () => {
    const html = renderToStaticMarkup(
      <SearchHighlightProvider
        highlight={{ needle: "barnetillegg", exact: false, matchingBlocks: new Set(), activeBlock: 0 }}
      >
        <Document templateDocumentation={documentation} />
      </SearchHighlightProvider>,
    );

    expect(html).toMatch(/data-block-index="0"[^>]*data-search-hit="active"/);
    expect(html).not.toMatch(/data-block-index="1"[^>]*data-search-hit/);
    expect(html).not.toContain("<mark");
  });

  it.each([
    {
      name: "highlights punctuation directly after an adjacent literal",
      literals: ["Du får barnetillegg", "."],
      needle: "barnetillegg.",
      expectedMarks: ["barnetillegg", "."],
    },
    {
      name: "highlights a word split across adjacent literals",
      literals: ["Du får barne", "tillegg."],
      needle: "barnetillegg",
      expectedMarks: ["barne", "tillegg"],
    },
    {
      name: "preserves and normalizes whitespace already present between literals",
      literals: ["Du får \n", "\tbarnetillegg."],
      needle: "får barnetillegg",
      expectedMarks: ["får", "barnetillegg"],
    },
    {
      name: "does not invent whitespace between adjacent literals",
      literals: ["Du får barne", "tillegg."],
      needle: "barne tillegg",
      expectedMarks: [],
    },
  ])("$name", ({ literals, needle, expectedMarks }) => {
    const html = renderToStaticMarkup(
      <SearchHighlightProvider highlight={{ needle, exact: true, matchingBlocks: new Set([0]), activeBlock: 0 }}>
        <Document
          templateDocumentation={{
            ...documentation,
            outline: [
              {
                controlStructureType: ContentOrControlStructureType.CONTENT,
                content: {
                  elementType: ElementType.PARAGRAPH,
                  index: 0,
                  paragraph: literals.map((text) => ({
                    controlStructureType: ContentOrControlStructureType.CONTENT,
                    content: { elementType: ElementType.PARAGRAPH_TEXT_LITERAL, text },
                  })),
                },
              },
            ],
          }}
        />
      </SearchHighlightProvider>,
    );

    expect([...html.matchAll(/<mark[^>]*>(.*?)<\/mark>/gs)].map(([, text]) => text)).toEqual(expectedMarks);
  });

  it("renders exactly as without search when there is no highlight", () => {
    const plain = renderToStaticMarkup(<Document templateDocumentation={documentation} />);
    const withoutHighlight = renderToStaticMarkup(
      <SearchHighlightProvider highlight={undefined}>
        <Document templateDocumentation={documentation} />
      </SearchHighlightProvider>,
    );

    expect(withoutHighlight).toBe(plain);
    expect(plain).not.toContain("<mark");
    expect(plain).not.toContain("data-search-hit");
  });

  it.each([
    { needle: "", matchingBlocks: new Set([0]) },
    { needle: "barnetillegg", matchingBlocks: new Set<number>() },
  ])("skips match planning when no block can show marks: %j", (highlight) => {
    const plan = vi.spyOn(documentMatching, "matchDocument");
    try {
      renderToStaticMarkup(
        <SearchHighlightProvider highlight={{ ...highlight, exact: false, activeBlock: 0 }}>
          <Document templateDocumentation={documentation} />
        </SearchHighlightProvider>,
      );
      expect(plan).not.toHaveBeenCalled();
    } finally {
      plan.mockRestore();
    }
  });
});

describe("Document full-query coverage", () => {
  function literal(text: string): ContentOrControlStructure<Text> {
    return {
      controlStructureType: ContentOrControlStructureType.CONTENT,
      content: { elementType: ElementType.PARAGRAPH_TEXT_LITERAL, text },
    };
  }

  function conditional(
    showIf: ContentOrControlStructure<Text>[],
    showElse: ContentOrControlStructure<Text>[] = [],
  ): Conditional<Text> {
    return {
      controlStructureType: ContentOrControlStructureType.CONDITIONAL,
      predicate: fieldPath("harBarn"),
      showIf,
      elseIf: [],
      showElse,
    };
  }

  function row(...cells: (string | ContentOrControlStructure<Text>[])[]): Content<Row> {
    return {
      controlStructureType: ContentOrControlStructureType.CONTENT,
      content: {
        elementType: ElementType.PARAGRAPH_TABLE_ROW,
        cells: cells.map((cell) => ({ text: typeof cell === "string" ? [literal(cell)] : cell })),
      },
    };
  }

  function table(
    rows: ContentOrControlStructure<Row>[],
    header = row("A", "B").content,
  ): ContentOrControlStructure<ParagraphContent> {
    return {
      controlStructureType: ContentOrControlStructureType.CONTENT,
      content: { elementType: ElementType.PARAGRAPH_TABLE, header, rows },
    };
  }

  const variable: ContentOrControlStructure<Text> = {
    controlStructureType: ContentOrControlStructureType.CONTENT,
    content: { elementType: ElementType.PARAGRAPH_TEXT_EXPRESSION, expression: fieldPath("antall") },
  };

  function renderParagraph(
    items: ContentOrControlStructure<ParagraphContent>[],
    exact = false,
    needle = "barnetillegg uføretrygd",
  ): string {
    return renderToStaticMarkup(
      <SearchHighlightProvider
        highlight={{
          needle,
          exact,
          matchingBlocks: new Set([0]),
          activeBlock: 0,
        }}
      >
        <Document
          templateDocumentation={{
            title: [],
            outline: [
              {
                controlStructureType: ContentOrControlStructureType.CONTENT,
                content: { elementType: ElementType.PARAGRAPH, index: 0, paragraph: items },
              },
            ],
            attachments: [],
            templateModelSpecification: { types: {}, letterModelTypeName: "" },
          }}
        />
      </SearchHighlightProvider>,
    );
  }

  it.each([
    { structure: "list", exact: false },
    { structure: "list", exact: true },
    { structure: "table", exact: false },
    { structure: "table", exact: true },
  ])(
    "keeps a textual branch connected when another branch contains a $structure (exact: $exact)",
    ({ structure, exact }) => {
      const other: ContentOrControlStructure<ParagraphContent> =
        structure === "table"
          ? table([row("Annet")])
          : {
              controlStructureType: ContentOrControlStructureType.CONTENT,
              content: {
                elementType: ElementType.PARAGRAPH_ITEMLIST,
                type: "PUNKTLISTE",
                items: [
                  {
                    controlStructureType: ContentOrControlStructureType.CONTENT,
                    content: { elementType: ElementType.PARAGRAPH_ITEMLIST_ITEM, text: [literal("Annet")] },
                  },
                ],
              },
            };
      const branch: Conditional<ParagraphContent> = {
        ...conditional([literal("barnetillegg.")]),
        showElse: [other],
      };
      const html = renderParagraph([literal("Du får "), branch], exact, "får barnetillegg");

      expect(
        [...html.matchAll(/<mark([^>]*)>(.*?)<\/mark>/g)].map(([, attributes, text]) => [
          text,
          attributes.includes("data-search-full-match"),
        ]),
      ).toEqual([
        ["får", true],
        ["barnetillegg", true],
      ]);
    },
  );

  it("reuses the block plan inside nested branches and table cells", () => {
    const plan = vi.spyOn(documentMatching, "matchDocument");
    try {
      renderParagraph([table([row([literal("Du får "), conditional([literal("barnetillegg")])], "uføretrygd")])]);
      expect(plan).toHaveBeenCalledTimes(1);
    } finally {
      plan.mockRestore();
    }
  });

  it.each([false, true])("highlights an exact phrase across an inline conditional (table cell: %s)", (inTable) => {
    const items = [literal("Du får "), conditional([literal("barnetillegg")], [literal("Annet")])];
    const html = renderParagraph(inTable ? [table([row(items)])] : items, true, "får barnetillegg");

    expect([...html.matchAll(/<mark[^>]*>(.*?)<\/mark>/g)].map(([, text]) => text)).toEqual(["får", "barnetillegg"]);
  });

  it("highlights a word across surrounding literals and compatible nested branches", () => {
    const html = renderParagraph(
      [
        literal("Du får barne"),
        conditional([literal("til"), conditional([literal("legg")], [literal("pensjon")])], [literal("penger")]),
        literal("."),
      ],
      true,
      "barnetillegg.",
    );

    expect([...html.matchAll(/<mark[^>]*>(.*?)<\/mark>/g)].map(([, text]) => text)).toEqual([
      "barne",
      "til",
      "legg",
      ".",
    ]);
  });

  it("merges overlapping ranges from compatible alternatives in a shared literal", () => {
    const html = renderParagraph([conditional([literal("ab")], [literal("a")]), literal("aba")], true, "aba");

    expect([...html.matchAll(/<mark[^>]*>(.*?)<\/mark>/g)].map(([, text]) => text)).toEqual(["ab", "aba"]);
  });

  it("combines compatible sibling conditionals, including an else-if branch", () => {
    const first = conditional([literal("trygd")], [literal("penger")]);
    first.elseIf = [{ predicate: fieldPath("harBarnetillegg"), showIf: [literal("til")] }];
    const html = renderParagraph(
      [literal("Du får barne"), first, conditional([literal("legg")], [literal("skatt")]), literal(".")],
      true,
      "barnetillegg.",
    );

    expect([...html.matchAll(/<mark[^>]*>(.*?)<\/mark>/g)].map(([, text]) => text)).toEqual([
      "barne",
      "til",
      "legg",
      ".",
    ]);
  });

  it("allows an empty branch between the surrounding literals of an exact phrase", () => {
    const html = renderParagraph(
      [literal("Du får "), conditional([literal("ikke ")]), literal("barnetillegg.")],
      true,
      "får barnetillegg",
    );

    expect([...html.matchAll(/<mark[^>]*>(.*?)<\/mark>/g)].map(([, text]) => text)).toEqual(["får", "barnetillegg"]);
  });

  it("prefers full coverage through an inline branch over an earlier partial-match paragraph", () => {
    const html = renderParagraph(
      [conditional([literal("får")], [literal("Du får "), conditional([literal("barnetillegg")], [literal("Annet")])])],
      false,
      "får barnetillegg",
    );

    expect(
      [...html.matchAll(/<mark([^>]*)>(.*?)<\/mark>/g)].map(([, attributes, text]) => [
        text,
        attributes.includes("data-search-full-match"),
      ]),
    ).toEqual([
      ["får", false],
      ["får", true],
      ["barnetillegg", true],
    ]);
  });

  it.each([false, true])("does not combine mutually exclusive inline branches (exact: %s)", (exact) => {
    const html = renderParagraph(
      [literal("Du får "), conditional([literal("barnetillegg ")], [literal("uføretrygd")])],
      exact,
    );

    expect([...html.matchAll(/<mark[^>]*>(.*?)<\/mark>/g)].map(([, text]) => text)).toEqual(
      exact ? [] : ["barnetillegg", "uføretrygd"],
    );
    expect(html).not.toContain("data-search-full-match");
  });

  it.each([false, true])("keeps a variable inside an inline branch unmarked (exact: %s)", (exact) => {
    const html = renderParagraph(
      [literal("Du får "), conditional([variable, literal(" barnetillegg")])],
      exact,
      "får barnetillegg",
    );

    const marks = [...html.matchAll(/<mark([^>]*)>(.*?)<\/mark>/g)];
    expect(marks.map(([, , text]) => text)).toEqual(exact ? [] : ["får", "barnetillegg"]);
    expect(marks.every(([, attributes]) => attributes.includes("data-search-full-match"))).toBe(true);
    expect(text(html)).toContain("antall");
  });

  it("combines a term spanning an inline conditional with another table cell", () => {
    const html = renderParagraph([
      table([row([literal("barne"), conditional([literal("tillegg")], [literal("X")])], "uføretrygd")]),
    ]);

    expect(
      [...html.matchAll(/<mark([^>]*)>(.*?)<\/mark>/g)].map(([, attributes, text]) => [
        text,
        attributes.includes("data-search-full-match"),
      ]),
    ).toEqual([
      ["barne", true],
      ["tillegg", true],
      ["uføretrygd", true],
    ]);
  });

  it("prefers full term coverage across a variable over an earlier partial-match branch", () => {
    const html = renderParagraph([
      {
        controlStructureType: ContentOrControlStructureType.CONDITIONAL,
        predicate: fieldPath("harBarn"),
        showIf: [literal("barnetillegg")],
        elseIf: [],
        showElse: [literal("barnetillegg "), variable, literal(" uføretrygd")],
      },
    ]);

    const marks = [...html.matchAll(/<mark([^>]*)>(.*?)<\/mark>/g)];
    expect(marks.map(([, attributes, text]) => [text, attributes.includes("data-search-full-match")])).toEqual([
      ["barnetillegg", false],
      ["barnetillegg", true],
      ["uføretrygd", true],
    ]);
    expect(text(html)).toContain("antall");
  });

  it("does not combine term coverage from alternative branches", () => {
    const html = renderParagraph([
      {
        controlStructureType: ContentOrControlStructureType.CONDITIONAL,
        predicate: fieldPath("harBarn"),
        showIf: [literal("barnetillegg")],
        elseIf: [],
        showElse: [literal("uføretrygd")],
      },
    ]);

    expect(html.match(/<mark/g)).toHaveLength(2);
    expect(html).not.toContain("data-search-full-match");
  });

  it("does not combine term coverage across separate searchable lines in a paragraph", () => {
    const html = renderParagraph([
      literal("barnetillegg"),
      {
        controlStructureType: ContentOrControlStructureType.CONTENT,
        content: {
          elementType: ElementType.PARAGRAPH_ITEMLIST,
          type: "PUNKTLISTE",
          items: [
            {
              controlStructureType: ContentOrControlStructureType.CONTENT,
              content: { elementType: ElementType.PARAGRAPH_ITEMLIST_ITEM, text: [literal("Annen tekst")] },
            },
          ],
        },
      },
      literal("uføretrygd"),
    ]);

    expect(html.match(/<mark/g)).toHaveLength(2);
    expect(html).not.toContain("data-search-full-match");
  });

  it("does not treat terms separated by a variable as an exact phrase", () => {
    const html = renderParagraph([literal("barnetillegg "), variable, literal(" uføretrygd")], true);

    expect(html).not.toContain("<mark");
  });

  it("marks full-query coverage across table cells instead of an earlier partial-match branch", () => {
    const html = renderParagraph([
      {
        controlStructureType: ContentOrControlStructureType.CONDITIONAL,
        predicate: fieldPath("harBarn"),
        showIf: [literal("barnetillegg")],
        elseIf: [],
        showElse: [table([row("barnetillegg", "uføretrygd")])],
      },
    ]);

    expect(
      [...html.matchAll(/<mark([^>]*)>(.*?)<\/mark>/g)].map(([, attributes, text]) => [
        text,
        attributes.includes("data-search-full-match"),
      ]),
    ).toEqual([
      ["barnetillegg", false],
      ["barnetillegg", true],
      ["uføretrygd", true],
    ]);
  });

  it("combines header cells without leaking coverage into body rows", () => {
    const html = renderParagraph([table([row("barnetillegg", "Annet")], row("barnetillegg", "uføretrygd").content)]);

    expect(
      [...html.matchAll(/<mark([^>]*)>(.*?)<\/mark>/g)].map(([, attributes, text]) => [
        text,
        attributes.includes("data-search-full-match"),
      ]),
    ).toEqual([
      ["barnetillegg", true],
      ["uføretrygd", true],
      ["barnetillegg", false],
    ]);
  });

  it.each([
    {
      name: "separate table rows",
      rows: [row("barnetillegg", "Annet"), row("Annet", "uføretrygd")],
    },
    {
      name: "alternative table-row branches",
      rows: [
        {
          controlStructureType: ContentOrControlStructureType.CONDITIONAL,
          predicate: fieldPath("harBarn"),
          showIf: [row("barnetillegg", "Annet")],
          elseIf: [],
          showElse: [row("Annet", "uføretrygd")],
        } satisfies ContentOrControlStructure<Row>,
      ],
    },
  ])("does not combine coverage across $name", ({ rows }) => {
    const html = renderParagraph([table(rows)]);

    expect(html.match(/<mark/g)).toHaveLength(2);
    expect(html).not.toContain("data-search-full-match");
  });

  it.each([
    { otherCell: "Annet", fullMatch: false },
    { otherCell: "uføretrygd", fullMatch: true },
  ])("keeps cell alternatives separate when another cell contains $otherCell", ({ otherCell, fullMatch }) => {
    const html = renderParagraph([
      table([
        row(
          [
            {
              controlStructureType: ContentOrControlStructureType.CONDITIONAL,
              predicate: fieldPath("harBarn"),
              showIf: [literal("barnetillegg")],
              elseIf: [],
              showElse: [literal("uføretrygd")],
            },
          ],
          otherCell,
        ),
      ]),
    ]);

    const marks = [...html.matchAll(/<mark([^>]*)>(.*?)<\/mark>/g)];
    expect(marks.map(([, attributes]) => attributes.includes("data-search-full-match"))).toEqual(
      fullMatch ? [true, false, true] : [false, false],
    );
  });

  it("highlights adjacent literals inside a loop without changing conditional rendering", () => {
    const html = renderParagraph(
      [
        {
          controlStructureType: ContentOrControlStructureType.FOR_EACH,
          items: fieldPath("barn"),
          body: [
            literal("Du får barne"),
            literal("tillegg."),
            {
              controlStructureType: ContentOrControlStructureType.CONDITIONAL,
              predicate: fieldPath("harBarn"),
              showIf: [literal("Annen tekst")],
              elseIf: [],
              showElse: [],
            },
          ],
        },
      ],
      true,
      "barnetillegg",
    );

    expect([...html.matchAll(/<mark[^>]*>(.*?)<\/mark>/g)].map(([, text]) => text)).toEqual(["barne", "tillegg"]);
    expect(html).toContain('class="conditional"');
    expect(html).not.toContain('class="inline-conditional"');
    expect(text(html)).toContain("For hver X i:");
  });
});

describe("Document block-level Conditional (If/Else If)", () => {
  it("skjuler predikatet bak en lukket <details>/<summary>-disclosure, men viser selve den betingede teksten direkte", () => {
    const html = renderToStaticMarkup(
      <Document
        templateDocumentation={{
          title: [],
          outline: [
            {
              controlStructureType: ContentOrControlStructureType.CONDITIONAL,
              predicate: fieldPath("harBarn"),
              showIf: [textLiteralParagraph("Du har barnetillegg.")],
              elseIf: [],
              showElse: [],
            },
          ],
          include: fieldPath("dummy"),
          attachmentData: fieldPath("dummy"),
        }}
      />,
    );

    // <details> uten `open`-attributt er kollapset som standard.
    expect(html).toContain("<details");
    expect(html).not.toContain("<details open");
    expect(html).toContain("<summary");
    // Selve den betingede TEKSTEN (utfallet leseren faktisk vil se i brevet) er alltid synlig,
    // uavhengig av om disclosure-en er åpen - den er ikke skjult bak popover/details.
    expect(text(html)).toContain("Du har barnetillegg.");
    // Predikatet finnes fortsatt i DOM-en (for tilgjengelighet/søk), men bare inni <details>.
    const detailsMatch = /<details[^>]*>(.*?)<\/details>/s.exec(html);
    expect(detailsMatch).not.toBeNull();
    expect(text(detailsMatch?.[1] ?? "")).toContain("harBarn");
  });

  it("rendrer 'Else If' med samme kollapsede disclosure-mønster", () => {
    const html = renderToStaticMarkup(
      <Document
        templateDocumentation={{
          title: [],
          outline: [
            {
              controlStructureType: ContentOrControlStructureType.CONDITIONAL,
              predicate: fieldPath("harBarn"),
              showIf: [textLiteralParagraph("Med barnetillegg.")],
              elseIf: [
                { predicate: fieldPath("harEktefelle"), showIf: [textLiteralParagraph("Med ektefelletillegg.")] },
              ],
              showElse: [],
            },
          ],
          include: fieldPath("dummy"),
          attachmentData: fieldPath("dummy"),
        }}
      />,
    );

    expect((html.match(/<summary/g) ?? []).length).toBe(2);
    expect(text(html)).toContain("Else If");
    expect(text(html)).toContain("Med ektefelletillegg.");
  });
});

describe("isLeafPrimitive", () => {
  const knownDataClasses = new Set(["UngUfoerAutoDto", "Barnetillegg"]);

  it("behandler kortnavn-typer fra håndskrevne selectors som primitive når de ikke finnes i modellen", () => {
    // `propertyType` er ikke garantert fullt kvalifisert - Date.kt/Number.kt setter "Int"
    // og "LocalDate". Den gamle "kotlin"/"java"-heuristikken ville feilaktig klassifisert
    // disse som data-klasser og lenket til en klasse som ikke finnes i panelet.
    expect(isLeafPrimitive("LocalDate", knownDataClasses)).toBe(true);
    expect(isLeafPrimitive("Int", knownDataClasses)).toBe(true);
  });

  it("gjenkjenner modellens egne data-klasser, både fullt kvalifisert og med nullability-suffiks", () => {
    expect(isLeafPrimitive("no.nav.pensjon.brevbaker.api.model.UngUfoerAutoDto", knownDataClasses)).toBe(false);
    expect(isLeafPrimitive("Barnetillegg?", knownDataClasses)).toBe(false);
  });

  it("faller tilbake til navne-heuristikken når modellspesifikasjonen ikke er kjent", () => {
    expect(isLeafPrimitive("kotlin.String", undefined)).toBe(true);
    expect(isLeafPrimitive("no.nav.pensjon.brevbaker.api.model.UngUfoerAutoDto", undefined)).toBe(false);
  });
});

describe("Document lister", () => {
  function itemListParagraph(type: "PUNKTLISTE" | "NUMMERERT_LISTE"): ContentOrControlStructure<OutlineContent> {
    return {
      controlStructureType: ContentOrControlStructureType.CONTENT,
      content: {
        elementType: ElementType.PARAGRAPH,
        index: 0,
        paragraph: [
          {
            controlStructureType: ContentOrControlStructureType.CONTENT,
            content: {
              elementType: ElementType.PARAGRAPH_ITEMLIST,
              type,
              items: [
                {
                  controlStructureType: ContentOrControlStructureType.CONTENT,
                  content: {
                    elementType: ElementType.PARAGRAPH_ITEMLIST_ITEM,
                    text: [
                      {
                        controlStructureType: ContentOrControlStructureType.CONTENT,
                        content: { elementType: ElementType.PARAGRAPH_TEXT_LITERAL, text: "Et punkt" },
                      },
                    ],
                  },
                },
              ],
            },
          },
        ],
      },
    };
  }

  function renderList(type: "PUNKTLISTE" | "NUMMERERT_LISTE"): string {
    return renderToStaticMarkup(
      <Document
        templateDocumentation={{
          title: [],
          outline: [itemListParagraph(type)],
          include: fieldPath("dummy"),
          attachmentData: fieldPath("dummy"),
        }}
      />,
    );
  }

  it("rendrer NUMMERERT_LISTE som <ol>", () => {
    const html = renderList("NUMMERERT_LISTE");
    expect(html).toContain("<ol>");
    expect(html).not.toContain("<ul>");
    expect(text(html)).toContain("Et punkt");
  });

  it("rendrer PUNKTLISTE som <ul>", () => {
    const html = renderList("PUNKTLISTE");
    expect(html).toContain("<ul>");
    expect(html).not.toContain("<ol>");
  });
});

describe("Document betingelser i avsnitt", () => {
  const lit = (text: string): ContentOrControlStructure<ParagraphContent> => ({
    controlStructureType: ContentOrControlStructureType.CONTENT,
    content: { elementType: ElementType.PARAGRAPH_TEXT_LITERAL, text },
  });

  function renderParagraph(paragraph: ContentOrControlStructure<ParagraphContent>[]): string {
    return renderToStaticMarkup(
      <Document
        templateDocumentation={{
          title: [],
          outline: [
            {
              controlStructureType: ContentOrControlStructureType.CONTENT,
              content: { elementType: ElementType.PARAGRAPH, index: 0, paragraph },
            },
          ],
          include: fieldPath("dummy"),
          attachmentData: fieldPath("dummy"),
        }}
      />,
    );
  }

  it("rendrer en tekst-betingelse midt i et avsnitt inline, uten blokk som bryter linjen", () => {
    const html = renderParagraph([
      lit("Du får "),
      {
        controlStructureType: ContentOrControlStructureType.CONDITIONAL,
        predicate: fieldPath("harBarn"),
        showIf: [lit("barnetillegg")],
        elseIf: [],
        showElse: [lit("ingenting")],
      },
      lit(" fra oss."),
    ]);

    expect(html).toContain("inline-conditional");
    expect(html).not.toContain('class="conditional');
    expect(html).not.toContain("<details");
    expect(text(html)).toContain("barnetillegg");
    expect(text(html)).toContain("ingenting");
    // Betingelsene ligger i popover-innholdet, ikke som synlig tekst i flyten.
    expect(html).toContain('aria-label="If"');
    expect(html).toContain('aria-label="Else"');
    expect(html).toContain("harBarn");
  });

  it("behandler et avsnitt med en betingelse som eneste barn som en betinget avsnitt-blokk", () => {
    const html = renderParagraph([
      {
        controlStructureType: ContentOrControlStructureType.CONDITIONAL,
        predicate: fieldPath("harBarn"),
        showIf: [lit("Med barn.")],
        elseIf: [],
        showElse: [lit("Uten barn.")],
      },
    ]);

    expect(html).not.toContain("inline-conditional");
    expect(html).toContain('class="conditional conditional-multi-branch"');
    // Hver gren er sitt eget avsnitt inni den betingede blokken.
    const showIf = /<div class="show-if">(.*?)<\/div><div class="show-else">/s.exec(html);
    expect(showIf).not.toBeNull();
    expect(showIf?.[1]).toContain("Med barn.");
    expect(html).toMatch(/class="show-else">.*Uten barn\./s);
  });

  it("beholder blokkvisning når en gren inneholder annet enn løpende tekst", () => {
    const html = renderParagraph([
      lit("Se: "),
      {
        controlStructureType: ContentOrControlStructureType.CONDITIONAL,
        predicate: fieldPath("harBarn"),
        showIf: [
          {
            controlStructureType: ContentOrControlStructureType.CONTENT,
            content: { elementType: ElementType.PARAGRAPH_ITEMLIST, type: "PUNKTLISTE", items: [] },
          },
        ],
        elseIf: [],
        showElse: [],
      },
    ]);

    expect(html).not.toContain("inline-conditional");
    expect(html).toContain('class="conditional"');
  });
});
