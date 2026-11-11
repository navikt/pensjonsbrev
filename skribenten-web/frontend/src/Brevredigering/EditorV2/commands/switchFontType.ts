import { type DocumentV2, type FontTypeV2 } from "~/types/brevV2";

import { insertAt } from "../model/collection";
import { type ContainerPath, caret, resolve } from "../model/paths";
import { contentCollection, fontTypeOf, isLiteral, newLiteral, setFontType, setText, textOf } from "../model/text";
import { type Command } from "../state/editorState";

export const switchFontType: Command<[fontType: FontTypeV2]> = (draft, fontType) => {
  const at = draft.focus;
  const container = allowsFontType(draft.redigertBrev, at.path) ? resolve(draft.redigertBrev, at.path) : undefined;
  const literal = container?.content[at.content];
  if (!container || !isLiteral(literal)) return;

  const text = textOf(literal);
  const word = wordAt(text, at.offset);
  if (!word) return;

  const original = fontTypeOf(literal);
  const font = original === fontType ? "PLAIN" : fontType;
  const before = text.slice(0, word.start);
  const after = text.slice(word.end);
  const items = contentCollection(container);

  if (before.length > 0) {
    setText(literal, before);
    insertAt(items, at.content + 1, newLiteral({ text: text.slice(word.start, word.end), fontType: font }));
    if (after.length > 0) insertAt(items, at.content + 2, newLiteral({ text: after, fontType: original }));
    draft.focus = caret(at.path, at.content + 1, at.offset - word.start);
  } else {
    setFontType(literal, font);
    if (after.length > 0) {
      setText(literal, text.slice(0, word.end));
      insertAt(items, at.content + 1, newLiteral({ text: after, fontType: original }));
    }
    draft.focus = caret(at.path, at.content, at.offset);
  }
};

function allowsFontType(doc: DocumentV2, path: ContainerPath): boolean {
  switch (path.kind) {
    case "title":
      return false;
    case "block":
      return doc.blocks[path.block]?.type === "PARAGRAPH";
    case "item":
    case "header":
    case "cell":
      return true;
  }
}

function isWordCharacter(character: string | undefined): boolean {
  return character !== undefined && character.trim() !== "";
}

function wordAt(text: string, offset: number): { start: number; end: number } | undefined {
  if (offset < 0 || text[offset] === " ") return undefined;
  let start = offset;
  while (start > 0 && isWordCharacter(text[start - 1])) start--;
  let end = offset;
  while (end < text.length && isWordCharacter(text[end])) end++;
  return { start, end };
}
