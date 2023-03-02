import {Content, Item, ITEM_LIST, LITERAL, VARIABLE} from "../model/api"
import produce from "immer"
import {ContentGroup} from "../model/state"

export enum MergeTarget { PREVIOUS = "PREVIOUS", NEXT = "NEXT" }

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
            return content.text.length === 0
        case ITEM_LIST:
            return content.items.length === 1 && isEmptyItem(content.items[0])
    }
}

export function isEmptyContentGroup(group: ContentGroup) {
    return group.content.length === 1 && isEmptyContent(group.content[0])
}
export function mergeContentArrays<T extends Content>(first: T[], second: T[]) {
    return produce(first, draft => {
        const lastContentOfFirst = draft[first.length - 1]
        const firstContentOfSecond = second[0]

        if (lastContentOfFirst.type === LITERAL && firstContentOfSecond.type === LITERAL) {
            // merge adjoining literals
            lastContentOfFirst.text += firstContentOfSecond.text
            // @ts-ignore
            draft.splice(first.length, 0, ...second.slice(1))
        } else {
            // @ts-ignore
            draft.splice(first.length, 0, ...second)
        }
    })
}

export function isEmptyItem(item: Item): boolean {
    return item.content.length === 0 || (item.content.length === 1 && isEmptyContent(item.content[0]))
}