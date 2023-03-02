import {FC, useState} from "react"
import * as Model from "../../model/api"
import ContentGroup from "../contentgroup/ContentGroup"
import {bindAction, BoundAction, combine} from "../../../../lib/actions"
import {ItemListAction} from "../../actions/itemlist"
import {MergeTarget} from "../../actions/common"
import {StealFocusAction} from "../../actions/stealfocus"
import {CursorPosition, StealFocus} from "../../model/state"

interface ItemProps {
    item: Model.Item
    editable: boolean | undefined
    stealFocus: CursorPosition | undefined
    focusStolen: BoundAction<[]>
    updateItem: BoundAction<[item: Model.Item]>
    mergeWith: BoundAction<[target: MergeTarget]>
    splitAtContent: BoundAction<[contentId: number, offset: number]>
}
const Item: FC<ItemProps> = ({item, editable, stealFocus, focusStolen, updateItem, mergeWith, splitAtContent}) => (
    <li>
        <ContentGroup content={item.content}
                      editable={editable}
                      updateContent={bindAction(ItemListAction.updateItemContent, updateItem, item)}
                      mergeWith={mergeWith}
                      splitAtContent={splitAtContent}
                      stealFocus={stealFocus}
                      focusStolen={focusStolen}
                      onFocus={() => {}}/>
    </li>
)

interface ItemListProps {
    itemList: Model.ItemList
    editable: boolean | undefined
    updateList: BoundAction<[list: Model.ItemList]>
}

const ItemList: FC<ItemListProps> = ({itemList, editable, updateList}) => {
    const [stealFocus, setStealFocus] = useState<StealFocus>({})

    const updateItems = bindAction(ItemListAction.updateItems, updateList, itemList)

    const mergeWithAndStealFocus = combine(
        bindAction(ItemListAction.mergeWith, updateItems),
        bindAction(StealFocusAction.onMerge, setStealFocus, stealFocus),
    ).bind(null, itemList.items)


    const splitAndStealFocus = combine(
        bindAction(ItemListAction.splitItem, updateItems),
        bindAction(StealFocusAction.onSplit, setStealFocus, stealFocus)
    ).bind(null, itemList.items)

    return (
        <ul>{itemList.items.map((item, itemId) =>
            <Item key={itemId}
                  editable={editable}
                  item={item}
                  stealFocus={stealFocus[itemId]}
                  focusStolen={bindAction(StealFocusAction.focusStolen, setStealFocus, stealFocus, itemId)}
                  updateItem={bindAction(ItemListAction.updateItem, updateList, itemList, itemId)}
                  mergeWith={mergeWithAndStealFocus.bind(null, itemId)}
                  splitAtContent={splitAndStealFocus.bind(null, itemId)}
            />
        )}</ul>
    )
}

export default ItemList