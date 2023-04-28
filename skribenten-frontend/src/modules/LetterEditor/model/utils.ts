import {AnyBlock, Content, Item, ITEM_LIST, LITERAL, TextContent, VARIABLE} from "./api"
import {ContentGroup} from "./state"
import produce from "immer"

import {MergeTarget} from "../actions/model"

export function isTextContent(content: Content): content is TextContent {
    return content.type === LITERAL || content.type === VARIABLE
}

export function getMergeIds(srcId: number, target: MergeTarget): [number, number] {
    switch (target) {
        case MergeTarget.PREVIOUS:
            return [srcId - 1, srcId]
        case MergeTarget.NEXT:
            return [srcId, srcId + 1]
    }
}

export function isEmptyContent(content: Content) {
    switch (content.type) {
        case VARIABLE:
        case LITERAL:
            return content.text.trim().length === 0
        case ITEM_LIST:
            return content.items.length === 1 && isEmptyItem(content.items[0])
    }
}

export function isEmptyContentGroup(group: ContentGroup) {
    return group.content.length === 1 && isEmptyContent(group.content[0])
}

export function mergeContentArrays<T extends Content>(first: T[], second: T[]): T[]
export function mergeContentArrays(first: Content[], second: Content[]) {
    return produce(first, draft => {
        const lastContentOfFirst = draft[first.length - 1]
        const firstContentOfSecond = second[0]

        if (lastContentOfFirst.type === LITERAL && firstContentOfSecond.type === LITERAL) {
            // merge adjoining literals
            lastContentOfFirst.text += firstContentOfSecond.text
            draft.splice(first.length, 0, ...second.slice(1))
        } else {
            draft.splice(first.length, 0, ...second)
        }
    })
}

export function isEmptyItem(item: Item): boolean {
    return item.content.length === 0 || (item.content.length === 1 && isEmptyContent(item.content[0]))
}

export function isEmptyContentList(content: Content[]) {
    return content.length === 0 || (content.length === 1 && isEmptyContent(content[0]))
}
export function isEmptyBlock(block: AnyBlock): boolean {
    return isEmptyContentList(block.content)
}