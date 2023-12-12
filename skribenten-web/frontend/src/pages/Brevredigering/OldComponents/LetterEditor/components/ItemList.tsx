import type { Item as ItemType, ItemList as ItemListType } from "~/types/brevbakerTypes";

import type { BoundAction, CallbackReceiver } from "../lib/actions";
import type { CursorPosition, LetterEditorState } from "../model/state";
import type { ItemID } from "./ContentGroup";
import ContentGroup from "./ContentGroup";

interface ItemProperties {
  id: ItemID;
  updateLetter: CallbackReceiver<LetterEditorState>;
  item: ItemType;
  editable: boolean | undefined;
  stealFocus?: CursorPosition;
  focusStolen: BoundAction<[]>;
  onFocus: BoundAction<[]>;
}
const Item = ({ id, updateLetter, item, editable, stealFocus, focusStolen, onFocus }: ItemProperties) => (
  <li>
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

interface ItemListProperties {
  id: { blockId: number; contentId: number };
  itemList: ItemListType;
  editable: boolean | undefined;
  updateLetter: CallbackReceiver<LetterEditorState>;
  stealFocus?: CursorPosition;
  focusStolen: BoundAction<[]>;
  onFocus: BoundAction<[]>;
}

const ItemList = ({ id, itemList, editable, updateLetter, stealFocus, focusStolen, onFocus }: ItemListProperties) => {
  return (
    <ul>
      {itemList.items.map((item, itemId) => (
        <Item
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

export default ItemList;
