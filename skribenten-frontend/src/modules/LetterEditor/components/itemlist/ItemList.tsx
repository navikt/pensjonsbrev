import {FC} from "react"
import * as Model from "../../model/api"
import ContentGroup, {ItemID} from "../contentgroup/ContentGroup"
import {BoundAction, CallbackReceiver} from "../../../../lib/actions"
import {CursorPosition, LetterEditorState} from "../../model/state"

interface ItemProps {
    id: ItemID
    updateLetter: CallbackReceiver<LetterEditorState>
    item: Model.Item
    editable: boolean | undefined
    stealFocus?: CursorPosition
    focusStolen: BoundAction<[]>
    onFocus: BoundAction<[]>
}
const Item: FC<ItemProps> = ({id, updateLetter, item, editable, stealFocus, focusStolen, onFocus}) => (
    <li>
        <ContentGroup content={item.content}
                      id={id}
                      updateLetter={updateLetter}
                      editable={editable}
                      stealFocus={stealFocus}
                      focusStolen={focusStolen}
                      onFocus={onFocus}/>
    </li>
)

interface ItemListProps {
    id: { blockId: number, contentId: number }
    itemList: Model.ItemList
    editable: boolean | undefined
    updateLetter: CallbackReceiver<LetterEditorState>
    stealFocus?: CursorPosition
    focusStolen: BoundAction<[]>
    onFocus: BoundAction<[]>
}

const ItemList: FC<ItemListProps> = ({ id, itemList, editable, updateLetter, stealFocus, focusStolen, onFocus}) => {
    return (
        <ul>{itemList.items.map((item, itemId) =>
            <Item key={itemId}
                  id={{...id, itemId}}
                  updateLetter={updateLetter}
                  editable={editable}
                  item={item}
                  stealFocus={stealFocus?.item?.id === itemId ? {contentId: stealFocus.item.contentId, startOffset: stealFocus.startOffset} : undefined}
                  focusStolen={focusStolen}
                  onFocus={onFocus}
            />
        )}</ul>
    )
}

export default ItemList