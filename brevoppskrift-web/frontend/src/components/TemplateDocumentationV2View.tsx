import { css } from "@emotion/react";
import { BodyLong, Heading, Tag } from "@navikt/ds-react";

import {
  type AttachmentV2,
  type ConditionalV2,
  ContentOrControlStructureTypeV2,
  type ContentOrControlStructureV2,
  type DataSource,
  ElementTypeV2,
  type ElementV2,
  type ElseIfV2,
  type Expr,
  ExprType,
  type ForEachV2,
  isDataSourceScope,
  type TemplateDocumentationV2,
} from "~/api/brevbakerTypesV2";

/**
 * Rendering for TemplateDocumentationRendererV2 ("v2"). Speiler strukturen i
 * routes/template.$malType.$templateId.tsx sin v1-rendering (samme CSS-klasser:
 * .preview/.expression/.conditional/.show-if/.show-else), men uttrykk (Expr) er
 * et generalisert sealed-hierarki i stedet for v1s løse operator+first+second-tre.
 *
 * NB: v2s FieldPath.leafType er alltid null i nåværende backend-renderer, så vi har
 * ingen presis feltype-info å lenke feltnavn til DataClasses-highlighting med (slik
 * v1 gjør). Feltstier vises derfor som ren tekst her.
 */
export function DocumentV2({
  templateDocumentation,
}: {
  templateDocumentation: TemplateDocumentationV2 | AttachmentV2;
}) {
  return (
    <div className="preview">
      <div>
        {templateDocumentation.title.map((cocs, index) => (
          <ContentOrControlStructureComponentV2 cocs={cocs} key={index} />
        ))}
      </div>
      <div>
        {templateDocumentation.outline.map((cocs, index) => (
          <ContentOrControlStructureComponentV2 cocs={cocs} key={index} />
        ))}
      </div>
    </div>
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

function ContentComponentV2({ content }: { content: ElementV2 }) {
  switch (content.elementType) {
    case ElementTypeV2.TITLE1: {
      return (
        <Heading size="medium" spacing>
          {content.text.map((cocs, index) => (
            <ContentOrControlStructureComponentV2 cocs={cocs} key={index} />
          ))}
        </Heading>
      );
    }
    case ElementTypeV2.TITLE2: {
      return (
        <Heading size="small" spacing>
          {content.text.map((cocs, index) => (
            <ContentOrControlStructureComponentV2 cocs={cocs} key={index} />
          ))}
        </Heading>
      );
    }
    case ElementTypeV2.TITLE3: {
      return (
        <Heading size="xsmall" spacing>
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
      return (
        <div
          css={css`
            display: grid;
            gap: 1px;
            grid-template-columns: repeat(${content.header.cells.length}, 1fr);
            border: 1px solid black;
            background: black;
            overflow: auto;

            .cell {
              background: white;
            }

            .expression + .cell {
              padding-left: var(--ax-space-16);
            }

            .conditional,
            .show-if,
            .show-else {
              display: contents;
            }

            .expression {
              grid-column: span ${content.header.cells.length};
            }
          `}
        >
          {content.header.cells.map((cell, index) => (
            <b className="cell" key={index}>
              {cell.text.map((t, index) => (
                <ContentOrControlStructureComponentV2 cocs={t} key={index} />
              ))}
            </b>
          ))}
          {content.rows.map((r, index) => (
            <ContentOrControlStructureComponentV2 cocs={r} key={index} />
          ))}
        </div>
      );
    }
    case ElementTypeV2.PARAGRAPH_TABLE_ROW: {
      return content.cells.map((cell, index) => (
        <span className="cell" key={index}>
          {cell.text.map((t, index) => (
            <ContentOrControlStructureComponentV2 cocs={t} key={index} />
          ))}
        </span>
      ));
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
  return (
    <div className="conditional">
      <div className="show-if">
        <div className="expression">
          <code>If </code>
          <ExprToText expr={conditional.predicate} />
        </div>
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
      <div className="expression">
        <code>Else If </code>
        <ExprToText expr={elseIf.predicate} />
      </div>
      {elseIf.showIf.map((cocs, index) => (
        <ContentOrControlStructureComponentV2 cocs={cocs} key={index} />
      ))}
    </div>
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

function compareOpSymbol(op: string): string {
  switch (op) {
    case "EQUAL": {
      return "==";
    }
    case "NOT_EQUAL": {
      return "!=";
    }
    case "GREATER_THAN": {
      return ">";
    }
    case "GREATER_THAN_OR_EQUAL": {
      return ">=";
    }
    case "LESS_THAN": {
      return "<";
    }
    case "LESS_THAN_OR_EQUAL": {
      return "<=";
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
  if (isDataSourceScope(source)) {
    return source.name;
  }
  return source.depth > 0 ? `${source.label}₍${source.depth}₎` : source.label;
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

export function ExprToText({ expr }: { expr: Expr }) {
  switch (expr.exprType) {
    case ExprType.LITERAL: {
      return <span>{expr.value}</span>;
    }
    case ExprType.FIELD_PATH: {
      const path = [dataSourceLabel(expr.source), ...expr.segments].join(".");
      return <span>{path}</span>;
    }
    case ExprType.ASSOCIATIVE_OP: {
      const symbol = assocOpSymbol(expr.op);
      return (
        <span>
          {expr.operands.map((operand, index) => (
            <span key={index}>
              {index > 0 && symbol}
              <MaybeParens expr={operand} />
            </span>
          ))}
        </span>
      );
    }
    case ExprType.COMPARISON: {
      return (
        <span>
          <MaybeParens expr={expr.left} /> {compareOpSymbol(expr.op)} <MaybeParens expr={expr.right} />
        </span>
      );
    }
    case ExprType.NOT: {
      return (
        <span>
          !<MaybeParens expr={expr.term} />
        </span>
      );
    }
    case ExprType.FUNCTION_CALL: {
      return (
        <span>
          {expr.name}(
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
      return (
        <span>
          {expr.formatterName}(<ExprToText expr={expr.value} />)
        </span>
      );
    }
    case ExprType.CONDITIONAL_EXPR: {
      return (
        <span>
          if (<ExprToText expr={expr.predicate} />) then <MaybeParens expr={expr.ifTrue} /> else{" "}
          <MaybeParens expr={expr.ifElse} />
        </span>
      );
    }
    case ExprType.NULL_COALESCE: {
      return (
        <span>
          <MaybeParens expr={expr.value} /> ?: <MaybeParens expr={expr.fallback} />
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
