import { type FontTypeV2, type LiteralV2, type NewLineV2, type TextV2, type VariableV2 } from "~/types/brevV2";

import { type Collection, collection, insertAt, isFromTemplate, isNew, removeAt } from "./collection";
import { type TextContainer } from "./paths";

export function newLiteral(args: { text?: string; fontType?: FontTypeV2 } = {}): LiteralV2 {
  return {
    type: "LITERAL",
    id: null,
    parentId: null,
    text: "",
    editedText: args.text ?? "",
    fontType: args.fontType ?? "PLAIN",
    editedFontType: null,
    editBehaviour: null,
  };
}

export function newNewLine(): NewLineV2 {
  return { type: "NEW_LINE", id: null, parentId: null, text: "", fontType: "PLAIN" };
}

export function isLiteral(text: TextV2 | undefined): text is LiteralV2 {
  return text?.type === "LITERAL";
}

export function isVariable(text: TextV2 | undefined): text is VariableV2 {
  return text?.type === "VARIABLE";
}

export function isNewLine(text: TextV2 | undefined): text is NewLineV2 {
  return text?.type === "NEW_LINE";
}

export function isFritekst(text: TextV2 | undefined): boolean {
  return (isLiteral(text) || isVariable(text)) && text.editBehaviour === "FRITEKST";
}

export function isRedigerbarData(text: TextV2 | undefined): boolean {
  return (isLiteral(text) || isVariable(text)) && text.editBehaviour === "REDIGERBAR_DATA";
}

export function isUneditedFritekst(text: TextV2 | undefined): boolean {
  return isLiteral(text) && isFritekst(text) && (text.editedText ?? null) === null;
}

export function textOf(text: TextV2): string {
  return isLiteral(text) ? (text.editedText ?? text.text) : text.text;
}

export function fontTypeOf(text: TextV2): FontTypeV2 {
  switch (text.type) {
    case "LITERAL":
      return text.editedFontType ?? text.fontType;
    case "VARIABLE":
      return text.fontType;
    case "NEW_LINE":
      return "PLAIN";
  }
}

export function cleanseText(text: string): string {
  return text.replaceAll("<br>", "").replaceAll("&nbsp;", " ").replaceAll("\n", " ").replaceAll("\r", "");
}

export function setText(literal: LiteralV2, text: string): void {
  const cleansed = cleanseText(text);
  literal.editedText = cleansed === literal.text ? null : cleansed;
}

export function setFontType(literal: LiteralV2, fontType: FontTypeV2): void {
  literal.editedFontType = fontType === literal.fontType ? null : fontType;
}

export function isEmptyText(text: TextV2): boolean {
  return text.type !== "NEW_LINE" && textOf(text).length === 0;
}

export function isEmptyContent(content: TextV2[]): boolean {
  return content.every((text) => isLiteral(text) && isEmptyText(text));
}

export function plainText(content: TextV2[]): string {
  return content.map((text) => (isNewLine(text) ? "\n" : textOf(text))).join("");
}

export function lengthOf(text: TextV2 | undefined): number {
  return text ? textOf(text).length : 0;
}

export function contentCollection(container: TextContainer): Collection<TextV2> {
  return collection(container.content, container.deletedContent, container);
}

export function splitLiteral(literal: LiteralV2, offset: number): LiteralV2 {
  const original = textOf(literal);
  setText(literal, original.slice(0, Math.max(0, offset)));
  return newLiteral({ text: cleanseText(original.slice(Math.max(0, offset))), fontType: fontTypeOf(literal) });
}

function canMerge(first: TextV2, second: TextV2): first is LiteralV2 {
  return (
    isLiteral(first) &&
    isLiteral(second) &&
    !isFritekst(first) &&
    !isFritekst(second) &&
    fontTypeOf(first) === fontTypeOf(second) &&
    !(isFromTemplate(first) && isFromTemplate(second))
  );
}

export function mergeAdjacentLiterals(container: TextContainer): void {
  const target = contentCollection(container);
  for (let i = 0; i < target.items.length - 1; ) {
    const first = target.items[i];
    const second = target.items[i + 1];
    if (canMerge(first, second) && isLiteral(second)) {
      const merged = textOf(first) + textOf(second);
      if (isNew(first)) {
        setText(second, merged);
        removeAt(target, i);
      } else {
        setText(first, merged);
        removeAt(target, i + 1);
      }
    } else {
      i++;
    }
  }
}

export function ensureEditable(container: TextContainer): void {
  if (container.content.length === 0 || !isLiteral(container.content.at(-1))) {
    insertAt(contentCollection(container), container.content.length, newLiteral());
  }
}

export type Position = { content: number; offset: number };

export function span(text: TextV2): number {
  return isNewLine(text) ? 1 : lengthOf(text);
}

export function linearOffset(content: TextV2[], position: Position): number {
  return content.slice(0, position.content).reduce((sum, text) => sum + span(text), 0) + position.offset;
}

export function linearLength(content: TextV2[]): number {
  return content.reduce((sum, text) => sum + span(text), 0);
}

export function positionAt(content: TextV2[], linear: number): Position {
  let start = 0;
  let fallback: Position | undefined;
  for (const [index, text] of content.entries()) {
    const end = start + span(text);
    if (isLiteral(text) && linear >= start && linear <= end) {
      return { content: index, offset: linear - start };
    }
    if (linear >= start && linear <= end && fallback === undefined) {
      fallback = { content: index, offset: isNewLine(text) ? 0 : Math.min(linear - start, end - start) };
    }
    start = end;
  }
  return fallback ?? { content: Math.max(0, content.length - 1), offset: lengthOf(content.at(-1)) };
}
