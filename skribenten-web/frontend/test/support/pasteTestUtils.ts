import { fontTypeOf, text } from "~/Brevredigering/LetterEditor/actions/common";
import { type Text, type TraversedElement } from "~/Brevredigering/LetterEditor/actions/traversedElement";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { type Content, FontType, ListType, type LiteralValue, type TextContent } from "~/types/brevbakerTypes";

/** Compact text projection of the letter: `H1: …`, `P: …`, `• …`/`1. …`, `TABLE`/`th:`/`tr:`, `**bold**`, `_italic_`. */
export function projectLetter(state: LetterEditorState): string[] {
  const runs = (content: TextContent[]) =>
    content
      .map((c) => {
        const value = text(c as LiteralValue);
        if (value.length === 0) return "";
        const font = fontTypeOf(c);
        if (font === FontType.BOLD) return `**${value}**`;
        if (font === FontType.ITALIC) return `_${value}_`;
        return value;
      })
      .join("");

  return state.redigertBrev.blocks.flatMap((block) => {
    const prefix = block.type === "PARAGRAPH" ? "P" : block.type.replace("TITLE", "H");
    const lines: string[] = [];
    let textRun: TextContent[] = [];
    const flushText = () => {
      const line = runs(textRun);
      if (line.length > 0) lines.push(`${prefix}: ${line}`);
      textRun = [];
    };

    for (const content of block.content as Content[]) {
      if (content.type === "ITEM_LIST") {
        flushText();
        const marker = content.listType === ListType.NUMMERERT_LISTE ? "1." : "•";
        lines.push(...content.items.map((listItem) => `${marker} ${runs(listItem.content)}`));
      } else if (content.type === "TABLE") {
        flushText();
        const row = (cells: TextContent[][]) => cells.map(runs).join(" | ");
        lines.push(
          "TABLE",
          `  th: ${row(content.header.colSpec.map((spec) => spec.headerContent.text))}`,
          ...content.rows.map((tableRow) => `  tr: ${row(tableRow.cells.map((cell) => cell.text))}`),
        );
      } else {
        textRun.push(content);
      }
    }
    flushText();
    return lines;
  });
}

/** A clipboard with the given formats. `getData` returns "" for missing formats, like the browser. */
export class MockDataTransfer implements DataTransfer {
  private data: Record<string, string> = {};

  get types(): string[] {
    return Object.keys(this.data);
  }
  getData(format: string): string {
    return this.data[format] ?? "";
  }
  setData(format: string, data: string): void {
    this.data[format] = data;
  }

  constructor(data: Record<string, string>) {
    Object.assign(this.data, data);
  }

  dropEffect: "none" | "copy" | "link" | "move" = "none";
  effectAllowed: "none" | "copy" | "link" | "move" | "all" | "copyLink" | "copyMove" | "linkMove" | "uninitialized" =
    "uninitialized";
  files = [] as unknown as FileList;
  items = [] as unknown as DataTransferItemList;

  clearData(): void {
    throw new Error("Method not implemented.");
  }
  setDragImage(): void {
    throw new Error("Method not implemented.");
  }
}

/** The same projection as `projectLetter`, for parser output; nested list items are marked `(n)`. */
export function projectElements(elements: TraversedElement[]): string[] {
  const runs = (content: Text[]) =>
    content
      .map((run) => {
        if (run.font === FontType.BOLD) return `**${run.text}**`;
        if (run.font === FontType.ITALIC) return `_${run.text}_`;
        return run.text;
      })
      .join("");

  return elements.flatMap((element) => {
    switch (element.type) {
      case "TEXT": {
        return [`TEXT: ${runs([element])}`];
      }
      case "ITEM": {
        const marker = element.listType === ListType.NUMMERERT_LISTE ? "1." : "•";
        return [`${marker}${element.nested ? " (n)" : ""} ${runs(element.content)}`];
      }
      case "TABLE": {
        const row = (cells: { content: Text[] }[]) => cells.map((cell) => runs(cell.content)).join(" | ");
        return [
          "TABLE",
          ...(element.headerCells ? [`  th: ${row(element.headerCells)}`] : []),
          ...element.rows.map((tableRow) => `  tr: ${row(tableRow.cells)}`),
        ];
      }
      default: {
        return [`${element.type}: ${runs(element.content)}`];
      }
    }
  });
}
