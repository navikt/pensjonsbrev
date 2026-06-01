import { css } from "@emotion/react";
import { BodyLong, Heading, Select, VStack } from "@navikt/ds-react";
import { createFileRoute, Link, notFound, redirect, useNavigate } from "@tanstack/react-router";
import { useEffect, useRef } from "react";

import {
  getBrevkoder,
  getTemplateDescription,
  getTemplateDocumentation,
  type MalType,
} from "~/api/brevbaker-api-endpoints";
import {
  type Attachment,
  type Conditional,
  type ContentOrControlStructure,
  ContentOrControlStructureType,
  type Element,
  ElementType,
  type ElseIf,
  type Expression,
  type ForEach,
  type TemplateDocumentation,
} from "~/api/brevbakerTypes";
import { DataClasses, trimClassName } from "~/components/DataClasses";

export const Route = createFileRoute("/template/$malType/$templateId")({
  loaderDeps: ({ search: { language } }) => ({ language }),
  params: {
    parse: (raw: Record<string, string>) => ({
      templateId: raw.templateId,
      malType: raw.malType as MalType,
    }),
  },
  loader: async ({ context, deps, params, preload }) => {
    await context.queryClient.ensureQueryData({
      queryKey: getBrevkoder.queryKey(params.malType),
      queryFn: () => getBrevkoder.queryFn(params.malType),
    });

    const description = await context.queryClient.ensureQueryData({
      queryKey: getTemplateDescription.queryKey(params.malType, params.templateId),
      queryFn: () => getTemplateDescription.queryFn(params.malType, params.templateId),
    });

    const defaultLanguage = description.languages[0];
    if (!defaultLanguage) {
      throw notFound();
    }

    if (!deps.language && !preload) {
      redirect({
        to: "/template/$malType/$templateId",
        search: { language: defaultLanguage },
        params,
      });
    }

    const language = deps.language ?? defaultLanguage;

    const documentation = await context.queryClient.ensureQueryData({
      queryKey: getTemplateDocumentation.queryKey(params.malType, params.templateId, language),
      queryFn: () => getTemplateDocumentation.queryFn(params.malType, params.templateId, language),
    });

    return { documentation, description };
  },
  validateSearch: (
    search: Record<string, unknown>,
  ): {
    language?: string;
    highlightedDataClass?: string;
    highlightedDataField?: string;
    q?: string;
    qi?: number;
  } => {
    const qiRaw = search.qi !== undefined ? Number(search.qi) : Number.NaN;
    return {
      language: search.language?.toString(),
      highlightedDataClass: search.highlightedDataClass?.toString(),
      highlightedDataField: search.highlightedDataField?.toString(),
      q: search.q?.toString(),
      qi: Number.isInteger(qiRaw) && qiRaw >= 0 ? qiRaw : undefined,
    };
  },
  component: TemplateExplorer,
});

/** Removes any previous search highlight by unwrapping the inserted <mark>s. */
function clearSearchHighlights(container: HTMLElement): void {
  for (const mark of container.querySelectorAll("mark.search-target")) {
    const parent = mark.parentNode;
    if (!parent) {
      continue;
    }
    while (mark.firstChild) {
      parent.insertBefore(mark.firstChild, mark);
    }
    parent.removeChild(mark);
    parent.normalize();
  }
}

/** True when a text node lives inside a variable/control-structure subtree
 * (`.expression` wrappers or `<code>` labels). Those are excluded from the
 * search index, so the highlighter must skip them to keep occurrence ordinals
 * aligned with the indexed literal text. */
function isExcludedTextNode(node: Node, container: HTMLElement): boolean {
  let element = node.parentElement;
  while (element && element !== container) {
    if (element.tagName === "CODE" || element.classList.contains("expression")) {
      return true;
    }
    element = element.parentElement;
  }
  return false;
}

/** Collapses whitespace the same way the search index does (`\s+` → " ", then
 * trim), while keeping a map from each collapsed-string index back to the raw
 * offset in the node. This lets multi-word/whitespace-containing queries match
 * the same way they were indexed, then resolve to a precise DOM range. */
function collapseWithMap(raw: string): { text: string; map: number[] } {
  const chars: string[] = [];
  const map: number[] = [];
  let pendingSpace = false;
  for (let i = 0; i < raw.length; i++) {
    const ch = raw[i];
    if (/\s/.test(ch)) {
      if (chars.length > 0) {
        pendingSpace = true;
      }
      continue;
    }
    if (pendingSpace) {
      chars.push(" ");
      map.push(i); // the space maps to the start of the following word
      pendingSpace = false;
    }
    chars.push(ch);
    map.push(i);
  }
  return { text: chars.join(""), map };
}

type Occurrence = { node: Text; start: number; end: number };

/** Wraps a single-text-node range in a <mark> and scrolls it into view. */
function markOccurrence(occurrence: Occurrence): void {
  const range = document.createRange();
  range.setStart(occurrence.node, occurrence.start);
  range.setEnd(occurrence.node, occurrence.end);
  const mark = document.createElement("mark");
  mark.className = "search-target";
  range.surroundContents(mark);
  mark.scrollIntoView({ behavior: "smooth", block: "center" });
}

/** Highlights the `ordinal`-th (0-based) occurrence of the query within the
 * indexed literal text, and scrolls it into view. Occurrences are counted in
 * document order over text nodes, skipping variable/control-structure subtrees,
 * with per-node whitespace collapsing so counting matches the index. If the
 * ordinal is out of range we clamp to the last occurrence (deterministic and
 * close), so a click always lands somewhere sensible. */
function highlightOccurrence(container: HTMLElement, query: string, ordinal: number): void {
  const needle = query.trim().toLowerCase();
  if (!needle) {
    return;
  }
  clearSearchHighlights(container);
  const walker = document.createTreeWalker(container, NodeFilter.SHOW_TEXT, {
    acceptNode: (node) => (isExcludedTextNode(node, container) ? NodeFilter.FILTER_REJECT : NodeFilter.FILTER_ACCEPT),
  });

  let seen = 0;
  let last: Occurrence | null = null;
  let node = walker.nextNode() as Text | null;
  while (node) {
    const raw = node.nodeValue ?? "";
    const { text, map } = collapseWithMap(raw);
    const haystack = text.toLowerCase();
    let at = haystack.indexOf(needle);
    while (at >= 0) {
      const start = map[at];
      const end = map[at + needle.length - 1] + 1;
      const occurrence: Occurrence = { node, start, end };
      if (seen === ordinal) {
        markOccurrence(occurrence);
        return;
      }
      last = occurrence;
      seen++;
      at = haystack.indexOf(needle, at + needle.length);
    }
    node = walker.nextNode() as Text | null;
  }

  // Requested ordinal was out of range: clamp to the last occurrence found.
  if (last) {
    markOccurrence(last);
  }
}

function TemplateExplorer() {
  const { documentation } = Route.useLoaderData();
  const { templateId } = Route.useParams();
  const { q, qi } = Route.useSearch();
  const previewRef = useRef<HTMLDivElement>(null);

  // biome-ignore lint/correctness/useExhaustiveDependencies: documentation is an intentional trigger so we re-highlight when navigating between templates with the same query.
  useEffect(() => {
    const container = previewRef.current;
    if (!container) {
      return;
    }
    if (!q) {
      clearSearchHighlights(container);
      return;
    }
    // Wait a frame so the freshly rendered document is laid out before scrolling.
    const raf = requestAnimationFrame(() => highlightOccurrence(container, q, qi ?? 0));
    return () => cancelAnimationFrame(raf);
  }, [q, qi, documentation]);

  return (
    <>
      <DataClasses templateModelSpecification={documentation.templateModelSpecification} />
      <VStack align="center" gap="space-16">
        <Heading size="medium" spacing>
          Oppskrift for {templateId}
        </Heading>
        <SelectLanguage />
        <div
          css={css`
            width: 100%;

            mark.search-target {
              background: var(--ax-warning-300);
              color: inherit;
              scroll-margin-top: var(--ax-space-64);
            }
          `}
          ref={previewRef}
        >
          <Document templateDocumentation={documentation} />
          {documentation.attachments.map((attachment, index) => (
            <Document key={index} templateDocumentation={attachment} />
          ))}
        </div>
      </VStack>
    </>
  );
}

function SelectLanguage() {
  const { description } = Route.useLoaderData();
  const { language } = Route.useSearch();
  const navigate = useNavigate({ from: Route.fullPath });

  return (
    <Select
      css={css`
        width: 200px;
        margin-bottom: var(--ax-space-32);
      `}
      label="Språk"
      onChange={(event) => navigate({ search: { language: event.target.value }, replace: true })}
      size="medium"
      value={language}
    >
      {description.languages.map((language) => (
        <option key={language} value={language}>
          {language}
        </option>
      ))}
    </Select>
  );
}

function Document({ templateDocumentation }: { templateDocumentation: TemplateDocumentation | Attachment }) {
  return (
    <div className="preview">
      <div>
        {templateDocumentation.title.map((cocs, index) => {
          return <ContentOrControlStructureComponent cocs={cocs} key={index} />;
        })}
      </div>
      <div>
        {templateDocumentation.outline.map((cocs, index) => {
          return <ContentOrControlStructureComponent cocs={cocs} key={index} />;
        })}
      </div>
    </div>
  );
}

function ContentOrControlStructureComponent<E extends Element>({ cocs }: { cocs: ContentOrControlStructure<E> }) {
  switch (cocs.controlStructureType) {
    case ContentOrControlStructureType.CONDITIONAL: {
      return <ConditionalComponent conditional={cocs} />;
    }
    case ContentOrControlStructureType.CONTENT: {
      return <ContentComponent content={cocs.content} />;
    }
    case ContentOrControlStructureType.FOR_EACH: {
      return <ForEachComponent content={cocs} />;
    }
  }
}

function ForEachComponent({ content }: { content: ForEach<Element> }) {
  return (
    <>
      <div className="expression">
        <code>For hver X i:</code> <ExpressionToText expression={content.items} />
      </div>
      {content.body.map((b, index) => (
        <ContentOrControlStructureComponent cocs={b} key={index} />
      ))}
    </>
  );
}

function ContentComponent({ content }: { content: Element }) {
  switch (content.elementType) {
    case ElementType.TITLE1: {
      return (
        <Heading size="medium" spacing>
          {content.text.map((cocs, index) => (
            <ContentOrControlStructureComponent cocs={cocs} key={index} />
          ))}
        </Heading>
      );
    }
    case ElementType.TITLE2: {
      return (
        <Heading size="small" spacing>
          {content.text.map((cocs, index) => (
            <ContentOrControlStructureComponent cocs={cocs} key={index} />
          ))}
        </Heading>
      );
    }
    case ElementType.TITLE3: {
      return (
        <Heading size="xsmall" spacing>
          {content.text.map((cocs, index) => (
            <ContentOrControlStructureComponent cocs={cocs} key={index} />
          ))}
        </Heading>
      );
    }
    case ElementType.PARAGRAPH_TEXT_LITERAL: {
      return <span className="paragraph-text-literal">{content.text}</span>;
    }
    case ElementType.PARAGRAPH_TEXT_EXPRESSION: {
      return (
        <span className="expression">
          <ExpressionToText expression={content.expression} />
        </span>
      );
    }
    case ElementType.PARAGRAPH: {
      return (
        <BodyLong as="div" spacing>
          {content.paragraph.map((cocs, index) => (
            <ContentOrControlStructureComponent cocs={cocs} key={index} />
          ))}
        </BodyLong>
      );
    }
    case ElementType.PARAGRAPH_TABLE: {
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

            /* Indent cells that are conditional to an expression */

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
                <ContentOrControlStructureComponent cocs={t} key={index} />
              ))}
            </b>
          ))}
          {content.rows.map((r, index) => (
            <ContentOrControlStructureComponent cocs={r} key={index} />
          ))}
        </div>
      );
    }
    case ElementType.PARAGRAPH_TABLE_ROW: {
      return content.cells.map((cell, index) => (
        <span className="cell" key={index}>
          {cell.text.map((t, index) => (
            <ContentOrControlStructureComponent cocs={t} key={index} />
          ))}
        </span>
      ));
    }
    case ElementType.PARAGRAPH_ITEMLIST: {
      return (
        <ul>
          {content.items.map((cocs, index) => (
            <ContentOrControlStructureComponent cocs={cocs} key={index} />
          ))}
        </ul>
      );
    }
    case ElementType.PARAGRAPH_ITEMLIST_ITEM: {
      return (
        <li>
          {content.text.map((cocs, index) => (
            <ContentOrControlStructureComponent cocs={cocs} key={index} />
          ))}
        </li>
      );
    }
  }
}

function ConditionalComponent<E extends Element>({ conditional }: { conditional: Conditional<E> }) {
  return (
    <div className="conditional">
      <div className="show-if">
        <div className="expression">
          <code>If </code>
          <ExpressionToText expression={conditional.predicate} />
        </div>
        <ShowIf cocs={conditional.showIf} />
      </div>
      {conditional.elseIf.map((elseIf, index) => (
        <ShowElseIf elseIf={elseIf} key={index} />
      ))}
      <ShowElse cocs={conditional.showElse} />
    </div>
  );
}

function ShowElseIf<E extends Element>({ elseIf }: { elseIf: ElseIf<E> }) {
  return (
    <div className="show-if">
      <div className="expression">
        <code>Else If </code>
        <ExpressionToText expression={elseIf.predicate} />
      </div>
      {elseIf.showIf.map((cocs, index) => (
        <ContentOrControlStructureComponent cocs={cocs} key={index} />
      ))}
    </div>
  );
}

function ShowIf<E extends Element>({ cocs }: { cocs: ContentOrControlStructure<E>[] }) {
  return (
    <>
      {cocs.map((a, index) => (
        <ContentOrControlStructureComponent cocs={a} key={index} />
      ))}
    </>
  );
}

function ShowElse<E extends Element>({ cocs }: { cocs: ContentOrControlStructure<E>[] }) {
  if (cocs.length === 0) {
    return null;
  }
  return (
    <div className="show-else">
      <div className="expression">
        <code>Else</code>
      </div>
      {cocs.map((a, index) => (
        <ContentOrControlStructureComponent cocs={a} key={index} />
      ))}
    </div>
  );
}

function ExpressionToText({ expression }: { expression: Expression }) {
  if ("scopeName" in expression) {
    switch (expression.scopeName) {
      case "forEach_item": {
        return "X"; // TODO: forskjellig navn for nøstede forEach
      }
      case "argument": {
        return "";
      }
      default: {
        return expression.scopeName;
      }
    }
  }
  if ("value" in expression) return expression.value;

  const firstExpressionResolved = <ExpressionToText expression={expression.first} />;
  const secondExpressionResolved = expression.second ? <ExpressionToText expression={expression.second} /> : "";
  switch (expression.operator.syntax) {
    case "FUNCTION": {
      return (
        <span>
          {expression.operator.text}
          <span
            css={css`
              color: purple;
            `}
          >
            (
          </span>
          {firstExpressionResolved}
          {secondExpressionResolved}
          <span
            css={css`
              color: purple;
            `}
          >
            )
          </span>
        </span>
      );
    }
    case "POSTFIX": {
      const isDeepestPostFix = "scopeName" in expression.first && expression.first?.scopeName === "argument";
      const content = (
        <>
          {firstExpressionResolved}
          {expression.operator.text}
        </>
      );

      if (isDeepestPostFix) {
        const isPrimitive = expression.type?.includes("kotlin") || expression.type?.includes("java"); // TODO: too basic?
        return (
          <Link
            from={Route.fullPath}
            preload={false}
            replace
            search={(s) => ({
              ...s,
              highlightedDataClass: isPrimitive ? undefined : trimClassName(expression.type ?? "").replace("?", ""),
              highlightedDataField: isPrimitive ? expression.operator.text.replace(".", "") : undefined,
            })}
          >
            {content}
          </Link>
        );
      }

      return <span>{content}</span>;
    }
    case "INFIX": {
      return (
        <span>
          {firstExpressionResolved}{" "}
          <span
            css={css`
              color: red;
            `}
          >
            {expression.operator.text}
          </span>{" "}
          {secondExpressionResolved}
        </span>
      );
    }
    case "PREFIX": {
      return (
        <span>
          {expression.operator.text}
          {firstExpressionResolved}
          {secondExpressionResolved}
        </span>
      );
    }
  }
}
