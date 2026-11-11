import DOMPurify from "dompurify";

import {
  cleansePastedText,
  type ItemElement,
  mergeNeighbouringText,
  type ParagraphElement,
  type Table,
  type TableCell,
  type Text,
  type TraversedElement,
} from "~/Brevredigering/LetterEditor/actions/traversedElement";
import { type FontType, type ListType } from "~/types/brevbakerTypes";

const ALLOWED_TAGS = [
  "p",
  "br",
  "strong",
  "b",
  "em",
  "i",
  "ul",
  "ol",
  "li",
  "table",
  "thead",
  "tbody",
  "tr",
  "td",
  "th",
  "span",
  "h1",
  "h2",
  "h3",
];

export function htmlToElements(html: string): TraversedElement[] {
  const sanitized = DOMPurify.sanitize(html, { ALLOWED_TAGS, ALLOWED_ATTR: ["rowspan", "colspan"] });
  const body = new DOMParser().parseFromString(sanitized, "text/html").body;
  return moveLeadingTextIntoParagraphs(traverseChildren(body, "PLAIN"));
}

function moveLeadingTextIntoParagraphs(elements: TraversedElement[]): TraversedElement[] {
  const result: TraversedElement[] = [];
  for (const element of elements.toReversed()) {
    const next = result.at(-1);
    if (element.type === "TEXT" && next?.type === "P") {
      result[result.length - 1] = { ...next, content: mergeNeighbouringText([element, ...next.content]) };
    } else {
      result.push(element);
    }
  }
  return result.toReversed();
}

function textRun(font: FontType, text: string): Text {
  return { type: "TEXT", font, text };
}

function ownText(element: Element): string {
  return cleansePastedText(element.textContent ?? "");
}

function isOnlyWhitespace(node: Node): boolean {
  return !!node.textContent && !/[^\t\n\r ]/.test(node.textContent);
}

function traverseNode(node: Node, font: FontType): TraversedElement[] {
  if (node instanceof Element) return traverse(node, font);
  if (node.nodeType !== Node.TEXT_NODE || isOnlyWhitespace(node)) return [];
  const text = cleansePastedText(node.textContent ?? "");
  return text.length > 0 ? [textRun(font, text)] : [];
}

function traverseChildren(element: Element, font: FontType): TraversedElement[] {
  return mergeNeighbouringText([...element.childNodes].flatMap((node) => traverseNode(node, font)));
}

function emphasis(font: FontType, emphasised: FontType): FontType {
  return font === "PLAIN" ? emphasised : font;
}

function traverse(element: Element, font: FontType): TraversedElement[] {
  switch (element.tagName) {
    case "SPAN":
      return traverseInline(element, font);
    case "STRONG":
    case "B":
      return traverseInline(element, emphasis(font, "BOLD"));
    case "EM":
    case "I":
      return traverseInline(element, emphasis(font, "ITALIC"));
    case "P":
      return traverseParagraph(element, font);
    case "UL":
      return traverseList(element, font, "PUNKTLISTE");
    case "OL":
      return traverseList(element, font, "NUMMERERT_LISTE");
    case "LI":
      return traverseItem(element, font);
    case "H1":
      return [{ type: "H1", content: textContainerContent(element, font) }];
    case "H2":
      return [{ type: "H2", content: textContainerContent(element, font) }];
    case "H3":
      return [{ type: "H3", content: textContainerContent(element, font) }];
    case "TABLE":
      return [traverseTable(element, font)];
    default:
      return traverseChildren(element, font);
  }
}

function traverseInline(element: Element, font: FontType): TraversedElement[] {
  if (element.children.length > 0) return traverseChildren(element, font);
  const text = ownText(element);
  return text.length > 0 ? [textRun(font, text)] : [];
}

function textContainerContent(element: Element, font: FontType): Text[] {
  if (element.children.length === 0) return [textRun(font, ownText(element))];
  return traverseChildren(element, font).flatMap((child) => {
    switch (child.type) {
      case "TEXT":
        return [child];
      case "TABLE":
        return [];
      default:
        return child.content;
    }
  });
}

function traverseParagraph(element: Element, font: FontType): TraversedElement[] {
  if (element.children.length === 0) return [{ type: "P", content: [textRun(font, ownText(element))] }];
  const result: TraversedElement[] = [];
  let buffer: Text[] = [];
  const flush = () => {
    if (buffer.length > 0) result.push({ type: "P", content: buffer } satisfies ParagraphElement);
    buffer = [];
  };
  for (const child of traverseChildren(element, font)) {
    if (child.type === "TEXT") {
      buffer.push(child);
    } else {
      flush();
      if (child.type !== "TABLE") result.push(child);
    }
  }
  flush();
  return result;
}

function traverseList(element: Element, font: FontType, listType: ListType): TraversedElement[] {
  return traverseChildren(element, font).map((child) =>
    child.type === "ITEM" && child.listType == null ? { ...child, listType } : child,
  );
}

function traverseItem(element: Element, font: FontType): ItemElement[] {
  if (element.children.length === 0) return [{ type: "ITEM", content: [textRun(font, ownText(element))] }];
  const result: ItemElement[] = [];
  let direct: Text[] = [];
  const flush = () => {
    if (direct.length > 0) result.push({ type: "ITEM", content: direct });
    direct = [];
  };
  for (const child of traverseChildren(element, font)) {
    switch (child.type) {
      case "TEXT":
        direct.push(child);
        break;
      case "ITEM":
        flush();
        result.push({ ...child, nested: true });
        break;
      case "TABLE":
        break;
      default:
        direct.push(...child.content);
    }
  }
  flush();
  return result.length > 0 ? result : [{ type: "ITEM", content: [textRun(font, "")] }];
}

function traverseTable(element: Element, font: FontType): Table {
  const toCell = (cell: Element): TableCell => ({ content: cellContent(cell, font) });
  let rows = [...element.querySelectorAll("tr")];
  let headerCells: TableCell[] | undefined;

  const theadRow = element.querySelector("thead tr");
  const theadCells = theadRow ? [...theadRow.querySelectorAll("th")] : [];
  if (theadCells.length > 0) {
    headerCells = theadCells.map(toCell);
    rows = rows.filter((row) => row !== theadRow);
  } else {
    const firstRowHeaders = rows.length > 0 ? [...rows[0].querySelectorAll("th")] : [];
    if (firstRowHeaders.length > 0) {
      headerCells = firstRowHeaders.map(toCell);
      rows = rows.slice(1);
    }
  }

  return {
    type: "TABLE",
    rows: rows
      .map((row) => ({ cells: [...row.querySelectorAll("td,th")].map(toCell) }))
      .filter((row) => row.cells.length > 0),
    headerCells,
  };
}

function cellContent(cell: Element, font: FontType): Text[] {
  return traverseChildren(cell, font)
    .flatMap((child) => {
      switch (child.type) {
        case "TEXT":
          return [child];
        case "P":
        case "ITEM":
          return child.content;
        default:
          return [];
      }
    })
    .filter((run) => run.text.trim().length > 0);
}
