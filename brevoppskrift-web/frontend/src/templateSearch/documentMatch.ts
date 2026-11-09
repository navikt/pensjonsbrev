import {
  type ContentOrControlStructure,
  ContentOrControlStructureType,
  type Element,
  ElementType,
  type Row,
  type TextLiteral,
} from "~/api/templateDocumentationTypes";
import { matchTextFlow, type TextFlow } from "~/search/matchTextFlow";

/** Literals receive visible ranges; table rows only receive full matches from their invisible cell separators. */
export type DocumentSource = TextLiteral | Row;

function isVisibleCell(flow: TextFlow<DocumentSource>[]): boolean {
  return flow.some(
    (item) =>
      (item.type === "text" && item.text.trim().length > 0) ||
      item.type === "variable" ||
      (item.type === "branches" && item.branches.some(isVisibleCell)),
  );
}

/** The same flow matcher supplies both match eligibility and ranges; React only renders its plan. */
export function matchDocument<E extends Element>(
  items: ContentOrControlStructure<E>[],
  needle: string,
  exact: boolean,
) {
  if (!needle.trim()) {
    return matchTextFlow<DocumentSource>([], needle, exact);
  }
  const flowOf = (items: ContentOrControlStructure<Element>[]): TextFlow<DocumentSource>[] =>
    items.flatMap((item): TextFlow<DocumentSource>[] => {
      if (item.controlStructureType === ContentOrControlStructureType.CONDITIONAL) {
        return [
          {
            type: "branches",
            branches: [item.showIf, ...item.elseIf.map((branch) => branch.showIf), item.showElse].map(flowOf),
          },
        ];
      }
      if (item.controlStructureType === ContentOrControlStructureType.FOR_EACH) {
        return flowOf(item.body);
      }
      const content = item.content;
      const isolated = (body: TextFlow<DocumentSource>[]): TextFlow<DocumentSource>[] => [
        { type: "break" },
        ...body,
        { type: "break" },
      ];
      switch (content.elementType) {
        case ElementType.PARAGRAPH_TEXT_LITERAL:
          return [{ type: "text", source: content, text: content.text }];
        case ElementType.PARAGRAPH_TEXT_EXPRESSION:
          return [{ type: "variable" }];
        case ElementType.PARAGRAPH:
          return isolated(flowOf(content.paragraph));
        case ElementType.TITLE1:
        case ElementType.TITLE2:
        case ElementType.TITLE3:
        case ElementType.PARAGRAPH_ITEMLIST_ITEM:
          return isolated(flowOf(content.text));
        case ElementType.PARAGRAPH_ITEMLIST:
          return isolated(flowOf(content.items));
        case ElementType.PARAGRAPH_TABLE:
          return isolated(
            flowOf([
              { controlStructureType: ContentOrControlStructureType.CONTENT, content: content.header },
              ...content.rows,
            ]),
          );
        case ElementType.PARAGRAPH_TABLE_ROW:
          return isolated(
            content.cells
              .map((cell) => flowOf(cell.text))
              .filter(isVisibleCell)
              .flatMap((cell, index) => [
                ...(index > 0 ? [{ type: "separator", text: " | ", source: content } as const] : []),
                ...cell,
              ]),
          );
      }
      const unsupported: never = content;
      throw new Error(`Unsupported document element: ${unsupported}`);
    });
  return matchTextFlow(flowOf(items), needle, exact);
}
