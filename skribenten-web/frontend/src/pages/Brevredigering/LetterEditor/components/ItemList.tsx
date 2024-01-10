import type { Item, Item as ItemType, ItemList as ItemListType } from "~/types/brevbakerTypes";

import type { BoundAction, CallbackReceiver } from "../lib/actions";
import type { CursorPosition, LetterEditorState } from "../model/state";

type ItemID = { blockId: number; contentId: number; itemId: number };
type ItemProperties = {
  id: ItemID;
  updateLetter: CallbackReceiver<LetterEditorState>;
  item: ItemType;
  editable: boolean | undefined;
  stealFocus?: CursorPosition;
  focusStolen: BoundAction<[]>;
  onFocus: BoundAction<[]>;
};
const ItemComp = ({ id, updateLetter, item, editable, stealFocus, focusStolen, onFocus }: ItemProperties) => (
  <li>
    {/*@ts-expect-error -- TODO will be reimplented later*/}
    <ContentGroup
      content={item.content}
      editable={editable}
      focusStolen={focusStolen}
      id={id}
      onFocus={onFocus}
      stealFocus={stealFocus}
      updateLetter={updateLetter}
    />
  </li>
);

type ItemListProperties = {
  id: { blockId: number; contentId: number };
  itemList: ItemListType;
  editable: boolean | undefined;
  updateLetter: CallbackReceiver<LetterEditorState>;
  stealFocus?: CursorPosition;
  focusStolen: BoundAction<[]>;
  onFocus: BoundAction<[]>;
};

export const ItemList = ({
  id,
  itemList,
  editable,
  updateLetter,
  stealFocus,
  focusStolen,
  onFocus,
}: ItemListProperties) => {
  return (
    <ul>
      {itemList.items.map((item, itemId) => (
        <ItemComp
          editable={editable}
          focusStolen={focusStolen}
          id={{ ...id, itemId }}
          item={item}
          key={itemId}
          onFocus={onFocus}
          stealFocus={
            stealFocus?.item?.id === itemId
              ? { contentId: stealFocus.item.contentId, startOffset: stealFocus.startOffset }
              : undefined
          }
          updateLetter={updateLetter}
        />
      ))}
    </ul>
  );
};

export function NewItemList({ items }: { items: Item[] }) {
  return (
    <ul>
      {items.map((item, itemIndex) => (
        <li key={itemIndex}>{item.content.map((content) => content.text)}</li>
      ))}
    </ul>
  );
}
