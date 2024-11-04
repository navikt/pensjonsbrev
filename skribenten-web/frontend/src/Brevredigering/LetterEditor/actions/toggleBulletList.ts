import type { Draft } from "immer";
import { produce } from "immer";

import type { Content, ItemList, LiteralValue, ParagraphBlock, VariableValue } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import type { LiteralIndex } from "./model";

export const toggleBulletList: Action<LetterEditorState, [literalIndex: LiteralIndex]> = produce(
  (draft, literalIndex) => {
    const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
    if (block.type !== "PARAGRAPH") {
      return;
    }

    draft.isDirty = true;

    return block.content.every((contentItem) => contentItem.type === "ITEM_LIST")
      ? toggleBulletListOff(draft, literalIndex.blockIndex)
      : toggleBulletListOn(draft, literalIndex.blockIndex);
  },
);

const toggleBulletListOn = (draft: Draft<LetterEditorState>, blockPosition: number) => {
  const block = draft.redigertBrev.blocks[blockPosition];
  const theBlocksContent = block.content;

  const result: Array<Array<LiteralValue | VariableValue> | ItemList> = [];
  let currentSentence: Array<LiteralValue | VariableValue> = [];

  /*
    en block kan inneholde flere vars/literls som utgjør en setning, og punktliste.
    vi bygger opp setningen ved å legge til vars/literals til currentSentence-arrayet, og så legger det som et innslag
    i resultatet når vi kommer til en punktliste / er ferdig med innholdet i blocken.
    Det vil også bevare rekkefølgen på elementene i blocken.
  */
  for (const content of theBlocksContent) {
    if (content.type === "VARIABLE" || content.type === "LITERAL") {
      // Add literals and variables to the current sentence
      currentSentence.push(content);
    } else if (content.type === "ITEM_LIST") {
      if (currentSentence.length > 0) {
        result.push(currentSentence);
        currentSentence = [];
      }
      result.push(content);
    }
  }

  if (currentSentence.length > 0) {
    result.push(currentSentence);
  }

  /**
   * konstruerer innholdet som skal være i ny block, som inneholder elementene i punktlisten.
   */
  const newBlockContent: Content[] = result.flatMap((content) => {
    //hvis det er en array, er dette en setning av vars/literals, ellers er det allerede en punktliste
    return Array.isArray(content)
      ? {
          type: "ITEM_LIST",
          id: null,
          deletedItems: [],
          items: [
            {
              id: null,
              content: content,
            },
          ],
        }
      : content;
  });

  const newBlock: ParagraphBlock = {
    ...block,
    type: "PARAGRAPH",
    content: newBlockContent,
  };

  draft.redigertBrev.blocks[blockPosition] = newBlock;
};

const toggleBulletListOff = (draft: Draft<LetterEditorState>, blockPosition: number) => {
  const block = draft.redigertBrev.blocks[blockPosition];
  const blockContent = block.content as Array<Draft<ItemList>>;

  //2D array. Hver item i item-listen er en liste av literals og vars
  const theContentsOfBulletList = blockContent.flatMap((itemList) => itemList.items.map((item) => item.content));

  //erstatter nåvæerende content i block (som er punktliste) med innholdet som var i første punkt i punktlisten
  draft.redigertBrev.blocks[blockPosition].content = theContentsOfBulletList[0];

  //resterende punkter i punktlisten
  const restOfTheContent = theContentsOfBulletList.slice(1);

  //lager nye content-blocks for de resterende punktene i punktlisten
  const newBlocks: ParagraphBlock[] = restOfTheContent.map((content) => ({
    ...block,
    type: "PARAGRAPH",
    content: content,
    deletedContent: [],
  }));
  //setter inn de nye content-blocksene etter den originale blocken
  insertAfterIndex(draft.redigertBrev.blocks, blockPosition, ...newBlocks);
};

function insertAfterIndex<T>(array: T[], index: number, ...elements: T[]): T[] {
  array.splice(index + 1, 0, ...elements); // Insert elements after the specified index
  return array;
}
