import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import type { Block, Content, LiteralValue, ParagraphBlock } from "~/types/brevbakerTypes";
import { FontType } from "~/types/brevbakerTypes";
import type { Nullable } from "~/types/Nullable";

const isParagraphBlock = (b: Block): b is ParagraphBlock => b.type === "PARAGRAPH";

const isContentLiteral = (c: Content): c is LiteralValue => c.type === "LITERAL";

/**
 * Merk at vi kan bare endre på fonttypen til literals
 * @returns fonttypen som alle literals i blocken har, ellers null hvis vi ikke kan finne ut av den
 */
export const getLiteralEditedFontTypeForBlock = (editorState: LetterEditorState): Nullable<FontType> => {
  const block = editorState.redigertBrev.blocks[editorState.focus.blockIndex];

  if (!isParagraphBlock(block)) {
    return null;
  }

  //vi kan bare endre fonttypen på literals
  const literals = block.content.filter(isContentLiteral);

  if (literals.length === 0) {
    return null;
  }

  const editedFontTypes: FontType[] = literals
    .map((literal) => literal.editedFontType)
    .filter((editedFontType) => !!editedFontType);

  if (editedFontTypes.length === 0) {
    return null;
  }

  if (editedFontTypes.every((editedFontType) => editedFontType === FontType.BOLD)) {
    return FontType.BOLD;
  }
  if (editedFontTypes.every((editedFontType) => editedFontType === FontType.ITALIC)) {
    return FontType.ITALIC;
  }
  if (editedFontTypes.every((editedFontType) => editedFontType === FontType.PLAIN)) {
    return FontType.PLAIN;
  }

  return null;
};
