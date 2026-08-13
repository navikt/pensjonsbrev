import { css } from "@emotion/react";
import { BodyLong, Box, Heading, Popover, Tag } from "@navikt/ds-react";
import { Link } from "@tanstack/react-router";
import { createContext, type ReactNode, useContext, useRef, useState } from "react";

import {
  type AssocOp,
  type AttachmentV2,
  type ConditionalV2,
  ContentOrControlStructureTypeV2,
  type ContentOrControlStructureV2,
  type DataSource,
  ElementTypeV2,
  type ElementV2,
  type ElseIfV2,
  type Expr,
  type ExprFieldPath,
  type ExprLiteral,
  ExprType,
  type ForEachV2,
  type RowV2,
  type TemplateDocumentationV2,
} from "~/api/brevbakerTypesV2";
import { trimClassName } from "~/components/DataClasses";

/**
 * Signaliserer at vi rendrer INNI en PARAGRAPH_TABLE sine rader (E = RowV2), og at gridet har
 * en ekstra, reservert "markør-kolonne" (kolonne 1) satt av fordi tabellen har minst én
 * betinget rad (se `hasConditionalRows`). Brukes av ConditionalComponentV2/ShowElseIfV2/
 * ShowElseV2/ForEachComponentV2/PARAGRAPH_TABLE_ROW til å vite om de skal forskyve sine egne
 * celler +1 kolonne og gi plass til en kompakt klikkbar markør i stedet for den fulle
 * "If <predikat>"-blokk-visningen som brukes utenfor tabeller. Nullstilles eksplisitt til
 * `null` når vi rendrer en celles TEKST-innhold (`cell.text`), siden inline tekst-uttrykk kan
 * ha SINE EGNE (Text-nivå) betingelser som ikke skal få tabell-radbehandling.
 */
const TableGridContext = createContext<{ hasMarkerColumn: boolean } | null>(null);

/**
 * Sjekker om en tabells rader (rekursivt gjennom FOR_EACH) inneholder minst én CONDITIONAL, som
 * avgjør om vi må reservere en ekstra markør-kolonne (kolonne 1) i gridet for kompakte
 * klikkbare If/Else If/Else-markører.
 */
function hasConditionalRows(rows: ContentOrControlStructureV2<RowV2>[]): boolean {
  return rows.some((row) => {
    if (row.controlStructureType === ContentOrControlStructureTypeV2.CONDITIONAL) {
      return true;
    }
    if (row.controlStructureType === ContentOrControlStructureTypeV2.FOR_EACH) {
      return hasConditionalRows(row.body);
    }
    return false;
  });
}

/**
 * Det vanlige/enkle tilfellet - en gren (showIf/showIf i en Else If/showElse) som består av
 * nøyaktig én direkte rad (CONTENT), uten nøstet FOR_EACH/CONDITIONAL. Da kan vi rendre
 * markøren og radens celler som søsken i det samme grid-elementet, uten koordinering på tvers
 * av komponenter. Nøstede/sammensatte grener faller tilbake til den eldre, alltid synlige
 * disclosure-raden (se `ConditionalPredicateDisclosure`).
 */
function singleDirectRow<E>(branch: ContentOrControlStructureV2<E>[]): E | null {
  return branch.length === 1 && branch[0].controlStructureType === ContentOrControlStructureTypeV2.CONTENT
    ? branch[0].content
    : null;
}

/**
 * Rendering for TemplateDocumentationRendererV2 ("v2"). Speiler strukturen i
 * routes/template.$malType.$templateId.tsx sin v1-rendering (samme CSS-klasser:
 * .preview/.expression/.conditional/.show-if/.show-else), men uttrykk (Expr) er
 * et generalisert sealed-hierarki i stedet for v1s løse operator+first+second-tre.
 *
 * `FieldPath.leafType` er en fullt kvalifisert Kotlin-type-streng for det siste
 * segmentet i feltstien (f.eks. "no.nav.pensjon.brevbaker.api.model.SomeDto?" eller
 * "kotlin.String"), satt av backend-rendereren. Vi bruker den til å lenke feltstier
 * til DataClasses-sidepanelet, på samme måte som v1 gjør for postfix-uttrykk.
 */
export function DocumentV2({
  templateDocumentation,
}: {
  templateDocumentation: TemplateDocumentationV2 | AttachmentV2;
}) {
  return (
    <Box background="default" className="preview letter-page" padding="space-48">
      <Heading level="1" size="large" spacing>
        {templateDocumentation.title.map((cocs, index) => (
          <ContentOrControlStructureComponentV2 cocs={cocs} key={index} />
        ))}
      </Heading>
      <div>
        {templateDocumentation.outline.map((cocs, index) => (
          <ContentOrControlStructureComponentV2 cocs={cocs} key={index} />
        ))}
      </div>
    </Box>
  );
}

function ContentOrControlStructureComponentV2<E extends ElementV2>({ cocs }: { cocs: ContentOrControlStructureV2<E> }) {
  switch (cocs.controlStructureType) {
    case ContentOrControlStructureTypeV2.CONDITIONAL: {
      return <ConditionalComponentV2 conditional={cocs} />;
    }
    case ContentOrControlStructureTypeV2.CONTENT: {
      return <ContentComponentV2 content={cocs.content} />;
    }
    case ContentOrControlStructureTypeV2.FOR_EACH: {
      return <ForEachComponentV2 content={cocs} />;
    }
  }
}

function ForEachComponentV2({ content }: { content: ForEachV2<ElementV2> }) {
  return (
    <>
      <div className="expression">
        <code>For hver X i:</code> <ExprToText expr={content.items} />
      </div>
      {content.body.map((b, index) => (
        <ContentOrControlStructureComponentV2 cocs={b} key={index} />
      ))}
    </>
  );
}

/** Tom, hvit fylle-celle for kolonne 1 (markør-kolonnen) - brukes av rader som IKKE selv er den
 * første raden i en betinget gren, slik at kolonne 1 alltid har et grid-element med hvit
 * bakgrunn (i stedet for en fullstendig ubrukt grid-celle, som ville vist gridets sorte
 * "linje"-bakgrunn som en hel svart rute for den raden). */
function TableMarkerGutterCell() {
  return <span className="cell" style={{ gridColumn: 1 }} />;
}

function ContentComponentV2({ content }: { content: ElementV2 }) {
  // Kalles ubetinget her (i stedet for inni PARAGRAPH_TABLE_ROW-caset under) for å følge
  // "rules of hooks" - denne komponenten har ett React-hook-kall uansett hvilket case som
  // trigges, selv om resultatet kun brukes av PARAGRAPH_TABLE_ROW.
  const tableGrid = useContext(TableGridContext);
  switch (content.elementType) {
    case ElementTypeV2.TITLE1: {
      return (
        <Heading level="2" size="medium" spacing>
          {content.text.map((cocs, index) => (
            <ContentOrControlStructureComponentV2 cocs={cocs} key={index} />
          ))}
        </Heading>
      );
    }
    case ElementTypeV2.TITLE2: {
      return (
        <Heading level="3" size="small" spacing>
          {content.text.map((cocs, index) => (
            <ContentOrControlStructureComponentV2 cocs={cocs} key={index} />
          ))}
        </Heading>
      );
    }
    case ElementTypeV2.TITLE3: {
      return (
        <Heading level="4" size="xsmall" spacing>
          {content.text.map((cocs, index) => (
            <ContentOrControlStructureComponentV2 cocs={cocs} key={index} />
          ))}
        </Heading>
      );
    }
    case ElementTypeV2.PARAGRAPH_TEXT_LITERAL: {
      return <span className="paragraph-text-literal">{content.text}</span>;
    }
    case ElementTypeV2.PARAGRAPH_TEXT_EXPRESSION: {
      return (
        <span className="expression">
          <ExprToText expr={content.expression} />
        </span>
      );
    }
    case ElementTypeV2.PARAGRAPH: {
      return (
        <BodyLong as="div" spacing>
          {content.paragraph.map((cocs, index) => (
            <ContentOrControlStructureComponentV2 cocs={cocs} key={index} />
          ))}
        </BodyLong>
      );
    }
    case ElementTypeV2.PARAGRAPH_TABLE: {
      // Reserverer en ekstra, smal markør-kolonne (kolonne 1) KUN hvis tabellen faktisk har
      // minst én betinget rad - unngår en unødvendig tom gutter-kolonne for det store flertallet
      // av tabeller som ikke har noen Conditional-rader i det hele tatt.
      const hasMarkerColumn = hasConditionalRows(content.rows);
      return (
        <div
          css={css`
            display: grid;
            gap: 1px;
            grid-template-columns: ${hasMarkerColumn ? "auto " : ""}repeat(${content.header.cells.length}, 1fr);
            border: 1px solid black;
            background: black;
            overflow: auto;

            .cell {
              background: white;
            }

            .expression + .cell,
            .table-conditional-marker + .cell {
              padding-left: var(--ax-space-16);
            }

            .conditional,
            .show-if,
            .show-else {
              display: contents;
            }

            /*
             * "1 / -1" spenner alltid fra første til siste gridlinje, uansett om
             * markør-kolonnen finnes eller ikke - slipper å regne ut antall kolonner på nytt
             * her for hver ny slik full-bredde-rad (For hver X i:, den valgfrie
             * If/Else If-predikat-raden når den er ekspandert, osv). .conditional-predicate
             * (native <details>) er fallback-visningen for sammensatte/nøstede grener (se
             * TableConditionalBranch) som ikke kan rendres kompakt - må også få hvit bakgrunn,
             * siden bare selve <summary>-en er synlig når den er kollapset.
             */
            .expression,
            .table-conditional-predicate,
            .conditional-predicate {
              grid-column: 1 / -1;
            }

            .conditional-predicate {
              background: white;
            }

            .table-conditional-predicate {
              background: var(--ax-meta-purple-300);
            }

            /* Den kompakte klikkbare ▸/▾-markøren (If/Else If) eller det statiske
             * Else-merket, plassert i den reserverte markør-kolonnen (kolonne 1) på SAMME
             * grid-rad som den første raden i grenen den hører til - i stedet for en egen,
             * alltid synlig rad. Se ConditionalComponentV2/ShowElseIfV2/ShowElseV2. */
            .table-conditional-marker {
              display: flex;
              align-items: center;
              justify-content: center;
              background: white;
              border: none;
              cursor: pointer;
              font: inherit;
              padding: 0 var(--ax-space-4);
            }

            .table-conditional-marker[aria-expanded="true"] {
              color: var(--ax-text-danger);
              font-weight: 600;
            }
          `}
        >
          {hasMarkerColumn && <b className="cell" style={{ gridColumn: 1 }} />}
          {content.header.cells.map((cell, index) => (
            <b className="cell" key={index} style={hasMarkerColumn ? { gridColumn: index + 2 } : undefined}>
              {cell.text.map((t, index) => (
                <ContentOrControlStructureComponentV2 cocs={t} key={index} />
              ))}
            </b>
          ))}
          <TableGridContext.Provider value={{ hasMarkerColumn }}>
            {content.rows.map((r, index) => (
              <ContentOrControlStructureComponentV2 cocs={r} key={index} />
            ))}
          </TableGridContext.Provider>
        </div>
      );
    }
    case ElementTypeV2.PARAGRAPH_TABLE_ROW: {
      return (
        <>
          {tableGrid?.hasMarkerColumn && <TableMarkerGutterCell />}
          {content.cells.map((cell, index) => (
            <span
              className="cell"
              key={index}
              style={tableGrid?.hasMarkerColumn ? { gridColumn: index + 2 } : undefined}
            >
              {/* Nullstiller kontekst: en celles TEKST-innhold kan ha SINE EGNE (Text-nivå)
               * betingelser, som skal rendres med den vanlige blokk-stilen - ikke tabell-radens
               * kompakte markør-stil. */}
              <TableGridContext.Provider value={null}>
                {cell.text.map((t, index) => (
                  <ContentOrControlStructureComponentV2 cocs={t} key={index} />
                ))}
              </TableGridContext.Provider>
            </span>
          ))}
        </>
      );
    }
    case ElementTypeV2.PARAGRAPH_ITEMLIST: {
      return (
        <ul>
          {content.items.map((cocs, index) => (
            <ContentOrControlStructureComponentV2 cocs={cocs} key={index} />
          ))}
        </ul>
      );
    }
    case ElementTypeV2.PARAGRAPH_ITEMLIST_ITEM: {
      return (
        <li>
          {content.text.map((cocs, index) => (
            <ContentOrControlStructureComponentV2 cocs={cocs} key={index} />
          ))}
        </li>
      );
    }
  }
}

function ConditionalComponentV2<E extends ElementV2>({ conditional }: { conditional: ConditionalV2<E> }) {
  const tableGrid = useContext(TableGridContext);
  if (tableGrid?.hasMarkerColumn) {
    return (
      <>
        <TableConditionalBranch branch={conditional.showIf} label="If" predicate={conditional.predicate} />
        {conditional.elseIf.map((elseIf, index) => (
          <TableConditionalBranch branch={elseIf.showIf} key={index} label="Else If" predicate={elseIf.predicate} />
        ))}
        <TableConditionalElseBranch cocs={conditional.showElse} />
      </>
    );
  }
  return (
    <div className="conditional">
      <div className="show-if">
        <ConditionalPredicateDisclosure label="If" predicate={conditional.predicate} />
        {conditional.showIf.map((cocs, index) => (
          <ContentOrControlStructureComponentV2 cocs={cocs} key={index} />
        ))}
      </div>
      {conditional.elseIf.map((elseIf, index) => (
        <ShowElseIfV2 elseIf={elseIf} key={index} />
      ))}
      <ShowElseV2 cocs={conditional.showElse} />
    </div>
  );
}

function ShowElseIfV2<E extends ElementV2>({ elseIf }: { elseIf: ElseIfV2<E> }) {
  return (
    <div className="show-if">
      <ConditionalPredicateDisclosure label="Else If" predicate={elseIf.predicate} />
      {elseIf.showIf.map((cocs, index) => (
        <ContentOrControlStructureComponentV2 cocs={cocs} key={index} />
      ))}
    </div>
  );
}

/**
 * Rendrer en If/Else If-gren for TABELL-rader (E = RowV2). Det vanlige/enkle tilfellet - grenen
 * består av nøyaktig én direkte rad - rendres kompakt: en klikkbar ▸/▾-markør i markør-kolonnen
 * (kolonne 1) på SAMME grid-rad som selve radens celler, i stedet for en alltid synlig egen
 * rad. Kun når disclosure-en er ekspandert settes det inn en EKSTRA, full-bredde rad over med
 * selve predikatet - se TableConditionalToggle. Sammensatte/nøstede grener (f.eks. FOR_EACH av
 * rader) faller tilbake til den eldre, alltid synlige disclosure-raden, siden markøren da ikke
 * entydig kan knyttes til én bestemt rad.
 */
function TableConditionalBranch<E extends ElementV2>({
  label,
  predicate,
  branch,
}: {
  label: string;
  predicate: Expr;
  branch: ContentOrControlStructureV2<E>[];
}) {
  const directRow = singleDirectRow(branch);
  if (directRow) {
    return (
      <TableConditionalToggle label={label} predicate={predicate}>
        <ContentOrControlStructureComponentV2
          cocs={{ controlStructureType: ContentOrControlStructureTypeV2.CONTENT, content: directRow }}
        />
      </TableConditionalToggle>
    );
  }
  return (
    <>
      <ConditionalPredicateDisclosure label={label} predicate={predicate} />
      {branch.map((cocs, index) => (
        <ContentOrControlStructureComponentV2 cocs={cocs} key={index} />
      ))}
    </>
  );
}

function TableConditionalElseBranch<E extends ElementV2>({ cocs }: { cocs: ContentOrControlStructureV2<E>[] }) {
  if (cocs.length === 0) {
    return null;
  }
  const directRow = singleDirectRow(cocs);
  if (directRow) {
    return (
      <>
        <span className="table-conditional-marker" style={{ gridColumn: 1 }} title="Else">
          <code>?</code>
        </span>
        <ContentOrControlStructureComponentV2
          cocs={{ controlStructureType: ContentOrControlStructureTypeV2.CONTENT, content: directRow }}
        />
      </>
    );
  }
  return (
    <>
      <div className="expression">
        <code>Else</code>
      </div>
      {cocs.map((a, index) => (
        <ContentOrControlStructureComponentV2 cocs={a} key={index} />
      ))}
    </>
  );
}

/**
 * Kompakt If/Else If-markør for tabellrader: en liten klikkbar ▸/▾-knapp i markør-kolonnen
 * (kolonne 1), på SAMME grid-rad som `children` (den betingede radens celler) - i stedet for en
 * alltid synlig, full-bredde "If <predikat>"-rad. Kun når `open` er `true` settes det inn en
 * EKSTRA, full-bredde rad OVER selve raden med det fulle predikatet - dette er den eneste
 * gangen en betinget tabellrad legger til en ekstra grid-rad.
 */
function TableConditionalToggle({
  label,
  predicate,
  children,
}: {
  label: string;
  predicate: Expr;
  children: ReactNode;
}) {
  const [open, setOpen] = useState(false);
  return (
    <>
      {open && (
        <div className="table-conditional-predicate">
          <code>{label}</code> <ExprToText expr={predicate} />
        </div>
      )}
      <button
        aria-expanded={open}
        className="table-conditional-marker"
        onClick={() => setOpen((o) => !o)}
        style={{ gridColumn: 1 }}
        title={`${label}${open ? " (klikk for å skjule betingelsen)" : ": klikk for å vise betingelsen"}`}
        type="button"
      >
        {open ? "▾" : "▸"}
      </button>
      {children}
    </>
  );
}

/**
 * "If <predikat>"/"Else If <predikat>"-raden kan bli svært lang (nøstede AND/OR-kjeder, lange
 * feltstier) og dominerer plassen når man leser gjennom brevoppskriften - selv om det som regel
 * er selve den betingede TEKSTEN (innholdet under raden) man er ute etter, ikke predikatet. Vises
 * derfor kollapset som standard bak en native <details>/<summary>-disclosure man kan klikke -
 * eller taste Enter/Space på, siden <summary> er fokuserbar og har innebygd tastaturstøtte - for
 * å vise selve predikatet. <summary> viser KUN en liten ▸/▾-ikon-markør (label som "If"/"Else
 * If" er kun tilgjengelig via `title`-attributtet, og vises i sin helhet inni predikat-teksten
 * når ekspandert) - holder markøren smal nok til å plasseres i selve BREVETS marg (se
 * .conditional-predicate i appStyles.css) uten å dekke over den betingede teksten ved siden av.
 */
function ConditionalPredicateDisclosure({ label, predicate }: { label: string; predicate: Expr }) {
  return (
    <details className="conditional-predicate" title={label}>
      <summary aria-label={label} />
      <div className="expression">
        <code>{label}</code> <ExprToText expr={predicate} />
      </div>
    </details>
  );
}

function ShowElseV2<E extends ElementV2>({ cocs }: { cocs: ContentOrControlStructureV2<E>[] }) {
  if (cocs.length === 0) {
    return null;
  }
  return (
    <div className="show-else">
      <div className="expression">
        <code>Else</code>
      </div>
      {cocs.map((a, index) => (
        <ContentOrControlStructureComponentV2 cocs={a} key={index} />
      ))}
    </div>
  );
}

function compareOpPhrase(op: string): string {
  switch (op) {
    case "EQUAL": {
      return "er lik";
    }
    case "NOT_EQUAL": {
      return "er ulik";
    }
    case "GREATER_THAN": {
      return "er større enn";
    }
    case "GREATER_THAN_OR_EQUAL": {
      return "er større enn eller lik";
    }
    case "LESS_THAN": {
      return "er mindre enn";
    }
    case "LESS_THAN_OR_EQUAL": {
      return "er mindre enn eller lik";
    }
    default: {
      return op;
    }
  }
}

function assocOpSymbol(op: string): string {
  switch (op) {
    case "AND": {
      return " og ";
    }
    case "OR": {
      return " eller ";
    }
    case "PLUS": {
      return " + ";
    }
    case "CONCAT": {
      return "";
    }
    default: {
      return ` ${op} `;
    }
  }
}

function dataSourceLabel(source: DataSource): string {
  switch (source.dataSourceType) {
    case "SCOPE": {
      // Praktisk talt alle felt-uttrykk starter i "argument"-scopet (brevets Dto-rot), så det
      // gir bare støy å skrive det ut hver gang – samme konvensjon som v1s ExpressionToText,
      // som returnerer "" for scopeName "argument". "felles"/"language" er egne, mindre
      // brukte scopes og beholder navnet sitt for å unngå tvetydighet.
      return source.name === "argument" ? "" : source.name;
    }
    case "FOR_EACH_VAR": {
      return source.depth > 0 ? `${source.label}₍${source.depth}₎` : source.label;
    }
    case "COMPUTED": {
      // Brukes kun som fallback her — FieldPathLink håndterer COMPUTED eksplisitt som en
      // postfix-kjede (<uttrykk>.segment) i stedet for å slå denne sammen med et punktum.
      return "";
    }
  }
}

/**
 * Samme som dataSourceLabel, men uten "argument"-skjulingen — brukt kun som fallback-tekst
 * i FieldPathLink for det sjeldne tilfellet der en FieldPath ikke har noen segmenter igjen
 * (selve scope-objektet brukes direkte), slik at vi aldri viser en tom lenke-tekst.
 */
function dataSourceLabelFallback(source: DataSource): string {
  return source.dataSourceType === "SCOPE" ? source.name : dataSourceLabel(source);
}

function editableKindLabel(kind: string): string {
  switch (kind) {
    case "FRITEKST": {
      return "Fritekst";
    }
    case "REDIGERBAR_DATA": {
      return "Redigerbar data";
    }
    case "BREVDATA_ELLER_FRITEKST": {
      return "Brevdata eller fritekst";
    }
    default: {
      return kind;
    }
  }
}

/**
 * Strukturell humanisering (4.A) av kjente, innebygde operasjoner (den lukkede listen
 * `UnaryOperation`/`BinaryOperation` i `Operations.kt`) til faste norske frasemaler, f.eks.
 * "{X} er tom" for `isEmpty`, "antall {X}" for `size`. Returnerer `null` for ukjente
 * funksjonsnavn, slik at `ExprToText` faller tilbake til den generiske `navn(args)`-visningen.
 */
function functionCallPhrase(name: string, args: Expr[]): ReactNode | null {
  switch (name) {
    case "isEmpty": {
      return args[0] ? (
        <span>
          <MaybeParens expr={args[0]} /> er tom
        </span>
      ) : null;
    }
    case "enabled": {
      return args[0] ? (
        <span>
          funksjonsbryteren <MaybeParens expr={args[0]} /> er aktivert
        </span>
      ) : null;
    }
    case "size": {
      return args[0] ? (
        <span>
          antall <MaybeParens expr={args[0]} />
        </span>
      ) : null;
    }
    case "today": {
      return <span>dagens dato</span>;
    }
    case "abs": {
      // UnaryOperation.AbsoluteValue / AbsoluteValueKroner
      return args[0] ? (
        <span>
          absoluttverdien av <MaybeParens expr={args[0]} />
        </span>
      ) : null;
    }
    case "str": {
      // UnaryOperation.ToString
      return args[0] ? (
        <span>
          <MaybeParens expr={args[0]} /> som tekst
        </span>
      ) : null;
    }
    case "fulltNavn": {
      // UnaryOperation.BrukerFulltNavn
      return args[0] ? (
        <span>
          <MaybeParens expr={args[0]} /> sitt fulle navn
        </span>
      ) : null;
    }
    case "isOneOf": {
      // BinaryOperation.EnumInList / IsOneOf: (verdi, liste)
      return args[0] && args[1] ? (
        <span>
          <MaybeParens expr={args[0]} /> er en av <MaybeParens expr={args[1]} />
        </span>
      ) : null;
    }
    case "getOrNull": {
      // BinaryOperation.GetElementOrNull: (liste, indeks). Rendres som array-access-syntaks
      // (`liste[indeks]`) i stedet for en prosa-frase, slik at en påfølgende feltaksess-kjede
      // på resultatet (Computed-base i FieldPath) leses naturlig som `liste[indeks].felt` i
      // stedet for at kjeden tvetydig ser ut til å høre til selve listen.
      return args[0] && args[1] ? (
        <span>
          <MaybeParens expr={args[0]} />[<ExprToText expr={args[1]} />]
        </span>
      ) : null;
    }
    case "-": {
      // BinaryOperation.IntMinus, eneste gjenværende INFIX-doc-operasjon som ikke
      // allerede flates ut/spesialhåndteres av renderBinaryInvoke (i motsetning til +,
      // som flates ut som en AssociativeOp).
      return args[0] && args[1] ? (
        <span>
          <MaybeParens expr={args[0]} /> - <MaybeParens expr={args[1]} />
        </span>
      ) : null;
    }
    case "IntToKroner": {
      // UnaryOperation.MapValue(IntToKroner) — heltall pakket om til et Kroner-beløp
      // (selve valutaformateringen skjer separat via en LocalizedFormatter/Format-node).
      return args[0] ? (
        <span>
          <MaybeParens expr={args[0]} /> som kronebeløp
        </span>
      ) : null;
    }
    case "IntToYear": {
      // UnaryOperation.MapValue(IntToYear)
      return args[0] ? (
        <span>
          <MaybeParens expr={args[0]} /> som årstall
        </span>
      ) : null;
    }
    case "SakstypeNavn": {
      // pensjon/maler sin BinaryOperation<Sakstype, Language, String?> — andre argument er
      // alltid Expression.FromScope.Language og gir ingen leservediendig informasjon.
      return args[0] ? (
        <span>
          navnet på sakstypen <MaybeParens expr={args[0]} />
        </span>
      ) : null;
    }
    default: {
      return null;
    }
  }
}

/**
 * Norske fraser for de vanligste `LocalizedFormatter`-implementasjonene (fra
 * `brevbaker/dsl/.../LocalizedFormatter.kt`). Ukjente/parametriserte formatterere
 * (f.eks. `DoubleFormat(scale)`, `CurrencyFormatKroner(denominator)`) faller tilbake til
 * "formatert med <formatterName>" siden parameterverdien ikke er tilgjengelig i `Expr`-treet.
 */
function formatterPhrase(formatterName: string): string {
  switch (formatterName) {
    case "ShortDateFormat": {
      return "formatert som kort dato";
    }
    case "DateFormat": {
      return "formatert som dato";
    }
    case "MonthYearFormatter": {
      return "formatert som måned og år";
    }
    case "MonthFormatter": {
      return "formatert som måned";
    }
    case "MonthFormatterShort": {
      return "formatert som kort måned";
    }
    case "IntFormat": {
      return "formatert som tall";
    }
    case "CurrencyFormat":
    case "CurrencyFormatKroner": {
      return "formatert som kroner";
    }
    case "TelefonnummerFormat": {
      return "formatert som telefonnummer";
    }
    case "FoedselsnummerFormat": {
      return "formatert som fødselsnummer";
    }
    case "CollectionFormat": {
      return "formatert som liste";
    }
    case "LandnavnFormat": {
      return "formatert som landnavn";
    }
    default: {
      return `formatert med ${formatterName}`;
    }
  }
}

/**
 * Er den fullt kvalifiserte Kotlin-type-strengen fra `leafType` en primitiv/innebygd
 * type (starter med "kotlin." eller "java.") i stedet for en av modellens egne
 * data-klasser? Samme konvensjon som v1 sin ExpressionToText bruker for postfix-uttrykk.
 */
function isPrimitiveType(leafType: string): boolean {
  return leafType.includes("kotlin") || leafType.includes("java");
}

function FieldPathLink({ expr }: { expr: ExprFieldPath }) {
  // For en Computed-base (feltaksess på et vilkårlig beregnet uttrykk, f.eks.
  // `getOrNull(...)[0].felt`) rendres kilden som selve uttrykket etterfulgt av segmentene som
  // en postfix-kjede (`<uttrykk>.segment1.segment2`), fremfor et prefiks-aktig punktum-join.
  // Selve uttrykket parentetiseres med mindre det allerede er en FieldPath eller et
  // FunctionCall/array-access (disse binder tett nok til at kjeding uten parentes er
  // utvetydig, f.eks. `liste[0].felt` eller `fn(x).felt`) — for f.eks. Comparison,
  // AssociativeOp, Conditional eller NullCoalesce som Computed-base er parentes nødvendig
  // for at segmentene tydelig skal tilhøre HELE det beregnede uttrykket, ikke bare siste ledd.
  const path: ReactNode =
    expr.source.dataSourceType === "COMPUTED" ? (
      <span className="expr-variable">
        <MaybeParensForPostfix expr={expr.source.expr} />
        {expr.segments.map((segment) => `.${segment}`).join("")}
      </span>
    ) : (
      // dataSourceLabel kan returnere "" (skjult "argument"-scope) — filtrer bort tomme
      // strenger før vi join'er, ellers ville vi fått en misvisende leading dot (".felt").
      // Om det ikke er noen segmenter igjen etter filtreringen (dvs. selve argument-objektet
      // brukes direkte uten feltaksess), faller vi tilbake til det opprinnelige scope-navnet
      // så vi aldri viser en tom lenke-tekst.
      <span className="expr-variable">
        {[dataSourceLabel(expr.source), ...expr.segments].filter(Boolean).join(".") ||
          dataSourceLabelFallback(expr.source)}
      </span>
    );
  const lastSegment = expr.segments.at(-1);

  if (!expr.leafType || !lastSegment) {
    return path;
  }

  const primitive = isPrimitiveType(expr.leafType);
  // `leafOwnerType` er eierklassen feltet er deklarert i. Ved å sende den med som
  // highlightedDataFieldOwner unngår vi at felt med samme navn i andre data-klasser
  // highlightes ved en feiltakelse (se DataClasses.tsx sin DataField).
  const ownerClassName = expr.leafOwnerType ? trimClassName(expr.leafOwnerType).replace("?", "") : undefined;
  return (
    <Link
      from="/template/$malType/$templateId"
      preload={false}
      replace
      search={(s) => ({
        ...s,
        highlightedDataClass: primitive ? undefined : trimClassName(expr.leafType ?? "").replace("?", ""),
        highlightedDataField: primitive ? lastSegment : undefined,
        highlightedDataFieldOwner: primitive ? ownerClassName : undefined,
      })}
    >
      {path}
    </Link>
  );
}

/**
 * Om et uttrykk trenger parentes når det vises som operand i en annen node (f.eks. en
 * AssociativeOp eller Comparison nøstet i en annen), for lesbarhet.
 */
function needsParens(expr: Expr): boolean {
  return (
    expr.exprType === ExprType.ASSOCIATIVE_OP ||
    expr.exprType === ExprType.COMPARISON ||
    expr.exprType === ExprType.CONDITIONAL_EXPR
  );
}

function MaybeParens({ expr }: { expr: Expr }) {
  if (needsParens(expr)) {
    return (
      <>
        (<ExprToText expr={expr} />)
      </>
    );
  }
  return <ExprToText expr={expr} />;
}

/**
 * En AND/OR-kjede med flere ledd (f.eks. lange showIf-predikater) rendres som en egen
 * innrykket blokk (linjeskift per ledd) i stedet for én lang linje - se `.expr-block` i
 * appStyles.css. CONCAT/PLUS holdes fortsatt inline: de bygger sammenhengende tekst/tall
 * (f.eks. brevtekst-konkatenering), og linjeskift der ville fragmentert lesbar prosa.
 * En enkeltstående operand (operands.length <= 1, som i praksis ikke skal forekomme siden
 * AssociativeOp alltid flates ut av backend fra minst to ledd) trenger heller ingen blokk.
 */
function isMultilineAssocOp(op: AssocOp, operandCount: number): boolean {
  return (op === "AND" || op === "OR") && operandCount > 1;
}

/**
 * Et AND/OR-ledd som selv er en nøstet AND/OR-kjede (f.eks. en OR nøstet inni en AND) rendres
 * som sin egen innrykkede blokk (se `.expr-block`) i stedet for med eksplisitte parenteser -
 * innrykket/border-left kommuniserer allerede grupperingen tydelig og uten dobbel visuell støy.
 * Alle andre leddtyper beholder den vanlige parentes-logikken (`MaybeParens`).
 */
function AssocOperandNode({ expr }: { expr: Expr }) {
  if (expr.exprType === ExprType.ASSOCIATIVE_OP && isMultilineAssocOp(expr.op, expr.operands.length)) {
    return <ExprToText expr={expr} />;
  }
  return <MaybeParens expr={expr} />;
}

/**
 * Trengs parentes rundt Computed-basen i en FieldPath (`<uttrykk>.segment1.segment2`)? En
 * `FieldPath` uten Computed-base kjeder naturlig (`x.y.z`), og `getOrNull` rendres som
 * array-access-syntaks (`liste[0]`) som binder tett nok til at etterfølgende segmenter
 * utvetydig tilhører resultatet (`liste[0].felt`). Alle andre `FunctionCall`-er rendres som
 * prosafraser (f.eks. "X er tom") som IKKE kjeder naturlig, så de – i likhet med Comparison,
 * AssociativeOp, Conditional, NullCoalesce, Not, Format, EditableField, Literal – trenger
 * parentes for at segmentene ikke skal se ut til å tilhøre bare siste ledd av frasen.
 */
function needsParensForPostfix(expr: Expr): boolean {
  if (expr.exprType === ExprType.FIELD_PATH) {
    return false;
  }
  if (expr.exprType === ExprType.FUNCTION_CALL) {
    return expr.name !== "getOrNull";
  }
  return true;
}

function MaybeParensForPostfix({ expr }: { expr: Expr }) {
  if (needsParensForPostfix(expr)) {
    return (
      <>
        (<ExprToText expr={expr} />)
      </>
    );
  }
  return <ExprToText expr={expr} />;
}

/**
 * Er dette literal-noden for Kotlin `null` (fra `Expression.Literal(null)`, brukt internt av
 * DSL-ens `isNull()`/`notNull()`)? Kjennetegnes ved at verdien er strengen "null" og `kind` er
 * ukjent (`inferScalarKind(null)` gir ingen treff), til forskjell fra en faktisk strengliteral
 * med innholdet "null".
 */
function isNullLiteral(expr: Expr): boolean {
  return expr.exprType === ExprType.LITERAL && expr.value === "null" && expr.kind === null;
}

function isSimpleLiteral(expr: Expr): expr is ExprLiteral {
  return expr.exprType === ExprType.LITERAL;
}

/**
 * Et `Conditional` der begge grener bare er tekst-/verdiliteraler (svært vanlig - malforfattere
 * bruker ofte `ifElse(pred, "en tekst", "")` eller `ifElse(pred, "ja", "nei")` for kortfattede
 * setningsvarianter) trenger ikke den fulle if/then/else-blokkvisningen: selve predikatet er
 * ofte bare "hvorfor akkurat denne varianten", ikke noe leseren trenger foran seg hele tiden.
 * Vi viser i stedet kun de(n) faktiske teksten(e), med predikatet tilgjengelig i en popover.
 */
function isCompactLiteralConditional(
  expr: Expr,
): expr is { exprType: ExprType.CONDITIONAL_EXPR; predicate: Expr; ifTrue: ExprLiteral; ifElse: ExprLiteral } {
  return expr.exprType === ExprType.CONDITIONAL_EXPR && isSimpleLiteral(expr.ifTrue) && isSimpleLiteral(expr.ifElse);
}

/**
 * Rå literal-tekst (uten anførselstegn) - brukes KUN i den kompakte Conditional-visningen, der
 * `[`/`]`/`|`-markørene allerede avgrenser verdien tydelig nok, og selve verdien som regel er
 * brevtekst som skal se ut som brevtekst (ikke en generisk kodeverdi som trenger kvotering).
 */
function compactLiteralText(literal: ExprLiteral): string {
  return literal.value;
}

/**
 * Viser `children` (en kompakt tekst-markør, f.eks. Conditional sin `[tekst]`/`tekst1|tekst2`
 * eller Format sin eksempeltekst) med en hover/fokus-popover som avslører `content` (den fulle
 * strukturen bak - predikat, eller uttrykk+formatterer) - full struktur er alltid tilgjengelig,
 * bare skjult som standard.
 */
function ExprPopover({ ariaLabel, content, children }: { ariaLabel: string; content: ReactNode; children: ReactNode }) {
  const [open, setOpen] = useState(false);
  const anchorRef = useRef<HTMLButtonElement>(null);

  return (
    <>
      <button
        aria-label={ariaLabel}
        className="expr-popover-trigger"
        onBlur={() => setOpen(false)}
        onFocus={() => setOpen(true)}
        onMouseEnter={() => setOpen(true)}
        onMouseLeave={() => setOpen(false)}
        ref={anchorRef}
        type="button"
      >
        {children}
      </button>
      <Popover anchorEl={anchorRef.current} onClose={() => setOpen(false)} open={open} placement="top">
        <Popover.Content>
          <span className="expression">{content}</span>
        </Popover.Content>
      </Popover>
    </>
  );
}

export function ExprToText({ expr }: { expr: Expr }) {
  switch (expr.exprType) {
    case ExprType.LITERAL: {
      // Strengliteraler vises med anførselstegn, slik at f.eks. en tom streng-fallback i
      // `ifNull("")` fortsatt er synlig i teksten (NullCoalesce sin fallback ville ellers
      // fremstått som et usynlig/tomt felt).
      if (expr.kind === "STRING") {
        return <span className="expr-literal">"{expr.value}"</span>;
      }
      return <span className="expr-literal">{expr.value}</span>;
    }
    case ExprType.FIELD_PATH: {
      return <FieldPathLink expr={expr} />;
    }
    case ExprType.ASSOCIATIVE_OP: {
      const symbol = assocOpSymbol(expr.op);
      if (isMultilineAssocOp(expr.op, expr.operands.length)) {
        const operatorWord = symbol.trim();
        return (
          <div className="expr-block">
            {expr.operands.map((operand, index) => (
              <div className="expr-block-row" key={index}>
                {index > 0 && <span className="expr-operator">{operatorWord} </span>}
                <AssocOperandNode expr={operand} />
              </div>
            ))}
          </div>
        );
      }
      return (
        <span>
          {expr.operands.map((operand, index) => (
            <span key={index}>
              {index > 0 && <span className="expr-operator">{symbol}</span>}
              <MaybeParens expr={operand} />
            </span>
          ))}
        </span>
      );
    }
    case ExprType.COMPARISON: {
      // Strukturell humanisering (4.A): `isNull()`/`notNull()` i DSL-en representeres internt
      // som en EQUAL/NOT_EQUAL-comparison mot literal null (se Base.kt). Vis dette som "har X" /
      // "mangler X" i stedet for "X == null" / "X != null" for bedre lesbarhet — krever ingen
      // maldata utover det som allerede finnes i Expr-treet.
      if ((expr.op === "EQUAL" || expr.op === "NOT_EQUAL") && isNullLiteral(expr.right)) {
        return (
          <span>
            <span className="expr-operator">{expr.op === "NOT_EQUAL" ? "har " : "mangler "}</span>
            <MaybeParens expr={expr.left} />
          </span>
        );
      }
      return (
        <span>
          <MaybeParens expr={expr.left} /> <span className="expr-operator">{compareOpPhrase(expr.op)}</span>{" "}
          <MaybeParens expr={expr.right} />
        </span>
      );
    }
    case ExprType.NOT: {
      return (
        <span>
          <span className="expr-operator">ikke</span> <MaybeParens expr={expr.term} />
        </span>
      );
    }
    case ExprType.FUNCTION_CALL: {
      const phrase = functionCallPhrase(expr.name, expr.args);
      if (phrase) {
        return <span className="expr-function">{phrase}</span>;
      }
      return (
        <span>
          <span className="expr-function">{expr.name}</span>(
          {expr.args.map((arg, index) => (
            <span key={index}>
              {index > 0 && ", "}
              <ExprToText expr={arg} />
            </span>
          ))}
          )
        </span>
      );
    }
    case ExprType.FORMAT: {
      const phrase = formatterPhrase(expr.formatterName);
      const fullExpr = (
        <span>
          <MaybeParens expr={expr.value} /> <span className="expr-function">{phrase}</span>
        </span>
      );
      // Når backend har klart å produsere et ekte, korrekt formattert eksempel (via den
      // faktiske `LocalizedFormatter`), vis det i stedet for den abstrakte
      // "<uttrykk> formatert som <formatterer>"-frasen - eksempelet ligner det leseren faktisk
      // vil se i et ferdig brev. Full struktur er fortsatt tilgjengelig via popover.
      if (expr.exampleText !== null) {
        return (
          <ExprPopover ariaLabel="Vis fullt uttrykk" content={fullExpr}>
            <span className="expr-literal">{expr.exampleText}</span>
          </ExprPopover>
        );
      }
      return fullExpr;
    }
    case ExprType.CONDITIONAL_EXPR: {
      // Svært vanlig mønster: begge grener er bare literaler (`ifElse(pred, "tekst", "")` eller
      // `ifElse(pred, "ja", "nei")`), der predikatet er mindre interessant enn selve teksten(e)
      // som faktisk vises. Vis da en kompakt markør (`[tekst]` når den ene grenen er tom
      // streng, ellers `tekst1|tekst2`) med predikatet tilgjengelig via popover i stedet for
      // den fulle if/then/else-blokken.
      if (isCompactLiteralConditional(expr)) {
        const ifTrueText = compactLiteralText(expr.ifTrue);
        const ifElseText = compactLiteralText(expr.ifElse);
        const compact = ifElseText === "" ? `[${ifTrueText}]` : `${ifTrueText}|${ifElseText}`;
        return (
          <ExprPopover
            ariaLabel="Vis betingelse"
            content={
              <>
                <code>Hvis </code>
                <ExprToText expr={expr.predicate} />
              </>
            }
          >
            <span className="expr-literal">{compact}</span>
          </ExprPopover>
        );
      }
      // if/then/else har naturlig tre "ledd" og drar samme nytte av linjeskift+innrykk som en
      // AND/OR-kjede - se `.expr-block` i appStyles.css. Prediktatet parentetiseres fortsatt
      // eksplisitt (i motsetning til AND/OR-ledd) siden det ikke er et likestilt ledd i samme
      // kjede, men selve betingelsen for grenene under.
      return (
        <div className="expr-block">
          <div className="expr-block-row">
            <span className="expr-operator">if</span> (<ExprToText expr={expr.predicate} />)
          </div>
          <div className="expr-block-row">
            <span className="expr-operator">then</span> <MaybeParens expr={expr.ifTrue} />
          </div>
          <div className="expr-block-row">
            <span className="expr-operator">else</span> <MaybeParens expr={expr.ifElse} />
          </div>
        </div>
      );
    }
    case ExprType.NULL_COALESCE: {
      return (
        <span>
          <MaybeParens expr={expr.value} /> <span className="expr-operator">?:</span>{" "}
          <MaybeParens expr={expr.fallback} />
        </span>
      );
    }
    case ExprType.EDITABLE_FIELD: {
      return (
        <Tag size="small" variant="alt3">
          {editableKindLabel(expr.kind)}
          {expr.value && (
            <>
              : <ExprToText expr={expr.value} />
            </>
          )}
          {expr.fallback && (
            <>
              {" "}
              (fallback: <ExprToText expr={expr.fallback} />)
            </>
          )}
        </Tag>
      );
    }
  }
}
