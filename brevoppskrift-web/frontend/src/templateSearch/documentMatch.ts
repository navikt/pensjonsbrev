import {
  type ContentOrControlStructure,
  ContentOrControlStructureType,
  type Element,
  ElementType,
  type TextLiteral,
} from "~/api/templateDocumentationTypes";
import { matchTextFlow, type TextFlow } from "~/search/matchTextFlow";

/** The same flow matcher supplies both match eligibility and ranges; React only renders its plan. */
export function matchDocument<E extends Element>(
  items: ContentOrControlStructure<E>[],
  needle: string,
  exact: boolean,
) {
  if (!needle.trim()) {
    return matchTextFlow<TextLiteral>([], needle, exact);
  }
  const flowOf = (items: ContentOrControlStructure<Element>[]): TextFlow<TextLiteral>[] =>
    items.flatMap((item): TextFlow<TextLiteral>[] => {
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
      const isolated = (body: TextFlow<TextLiteral>[]): TextFlow<TextLiteral>[] => [
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
            content.cells.flatMap((cell, index) => [
              ...(index > 0 ? [{ type: "cell" } as const] : []),
              ...flowOf(cell.text),
            ]),
          );
      }
      const unsupported: never = content;
      throw new Error(`Unsupported document element: ${unsupported}`);
    });
  return matchTextFlow(flowOf(items), needle, exact);
}
