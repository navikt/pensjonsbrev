import {ITEM_LIST, LITERAL, VARIABLE} from "../model/api"
import {Action} from "../../../lib/actions"
import produce from "immer"
import {MergeTarget} from "./common"
import {ContentGroup, StealFocus} from "../model/state"
import {isEmptyContentGroup} from "../model/utils"

const onMerge: Action<StealFocus, [contentGroups: ContentGroup[], contentGroupId: number, target: MergeTarget]> =
    produce((draft, contentGroups, contentGroupId, target) => {
        switch (target) {
            case MergeTarget.PREVIOUS: {
                const prev = contentGroups[contentGroupId - 1]
                if (prev) {
                    const lastContentId = prev.content.length - 1
                    const lastContent = prev.content[lastContentId]

                    switch (lastContent.type) {
                        case LITERAL:
                            draft[contentGroupId - 1] = {contentId: lastContentId, startOffset: lastContent.text.length}
                            break
                        case VARIABLE:
                        case ITEM_LIST:
                            draft[contentGroupId - 1] = {contentId: lastContentId + 1, startOffset: 0}
                            break
                    }
                }
                break
            }
            case MergeTarget.NEXT: {
                const current = contentGroups[contentGroupId]
                if (current && contentGroupId + 1 < contentGroups.length) {
                    const lastContentId = current.content.length - 1
                    const lastContent = current.content[lastContentId]
                    switch (lastContent.type) {
                        case LITERAL:
                            draft[contentGroupId] = {contentId: lastContentId, startOffset: lastContent.text.length}
                            break
                    }
                }
                break
            }
        }
    })

const onSplit: Action<StealFocus, [contentGroups: ContentGroup[], contentGroupId: number]> =
    produce((draft, contentGroups, contentGroupId) => {
        if (!isEmptyContentGroup(contentGroups[contentGroupId])) {
            draft[contentGroupId + 1]= { contentId: 0, startOffset: 0 }
        }
    })

const focusStolen: Action<StealFocus, [contentGroupId: number]> =
    produce((draft, contentGroupId) => {
        delete draft[contentGroupId]
    })

export const StealFocusAction = {
    onMerge, onSplit, focusStolen,
}