import React, {MouseEventHandler} from "react"
import Text from "../text/Text"
import styles from "./Content.module.css"
import {Content, ITEM_LIST, LITERAL, TextContent, VARIABLE} from "../../model/api"
import EditableText from "../text/EditableText"
import {SelectionService} from "../../services/SelectionService"
import {applyAction, bindActionWithCallback, BoundAction, CallbackReceiver, combine} from "../../../../lib/actions"
import ItemList from "../itemlist/ItemList"
import {isTextContent} from "../../model/utils"
import {CursorPosition, LetterEditorState} from "../../model/state"
import {ContentId, MergeTarget} from "../../actions/model"
import Actions from "../../actions"

const selectService = new SelectionService(true)

type ContentGroupState = never

export type BlockID = { blockId: number }
export type ItemID = { blockId: number, contentId: number, itemId: number }

function toContentId(id: BlockID | ItemID, contentId: number): ContentId {
    return "itemId" in id ? {...id, itemContentId: contentId} : {...id, contentId: contentId}
}

export interface ContentGroupProps<T extends Content> {
    id: BlockID | ItemID
    updateLetter: CallbackReceiver<LetterEditorState>
    content: T[]
    editable: boolean | undefined
    stealFocus: CursorPosition | undefined
    focusStolen: BoundAction<[]>
    onFocus: BoundAction<[]>
}

type ContentRefs = { [nodeId: number]: HTMLElement | undefined | null }

class ContentGroup<T extends Content | TextContent> extends React.Component<ContentGroupProps<T>, ContentGroupState> {
    private childRefs: ContentRefs = {}

    constructor(props: ContentGroupProps<T>) {
        super(props)
    }

    setChildRef(contentId: number, node: HTMLElement | null) {
        const prev = this.childRefs[contentId]

        if (prev) {
            prev.onkeydown = null
        }

        if (node != null) {
            let keyHandler = this.enterHandler.bind(this, contentId)

            // Første node skal ha backspaceHandler og siste node skal ha deleteHandler. Dette kan være samme node.
            if (contentId === 0) {
                keyHandler = combine(keyHandler, this.backspaceHandler.bind(this, 0))
            }
            if (contentId === this.props.content.length - 1) {
                keyHandler = combine(keyHandler, this.deleteHandler)
            }
            if (this.props.content[contentId - 1]?.type === ITEM_LIST) {
                keyHandler = combine(keyHandler, this.backspaceHandler.bind(this, contentId))
            }

            node.onkeydown = keyHandler

            if (this.props.stealFocus && this.props.stealFocus.contentId === contentId) {
                if (node.childNodes.length === 0) {
                    selectService.focusStartOfNode(node, this.props.focusStolen)
                } else {
                    selectService.focusAtOffset(node.childNodes[0], this.props.stealFocus.startOffset, this.props.focusStolen)
                }
            }
        }

        this.childRefs[contentId] = node
    }

    componentDidUpdate() {
        const span = this.props.stealFocus != null ? this.childRefs[this.props.stealFocus.contentId] : null
        if (this.props.stealFocus && span) {
            if (span.childNodes.length === 0) {
                selectService.focusStartOfNode(span, this.props.focusStolen)
            } else {
                selectService.focusAtOffset(span.childNodes[0], this.props.stealFocus.startOffset, this.props.focusStolen)
            }
        }
    }

    stealFocusHandler: MouseEventHandler<HTMLDivElement> = (e) => {
        if (e.target === e.currentTarget) {
            const nodes = Object.entries(this.childRefs).map(e => e[1]).filter((n): n is HTMLElement => n != null)

            if (nodes.length > 0) {
                selectService.focusEndOfClickedLine(nodes, {x: e.clientX, y: e.clientY}, this.props.focusStolen)
            } else {
                console.warn("Cannot steal focus for click because we don't have any childRefs.")
            }
        }
    }

    enterHandler = (contentId: number, e: KeyboardEvent) => {
        if (e.key === "Enter") {
            e.preventDefault()
            const offset = selectService.getCursorOffset()

            applyAction(Actions.split, this.props.updateLetter, toContentId(this.props.id, contentId), offset)
        }
    }

    backspaceHandler = (contentId: number, e: KeyboardEvent) => {
        if (e.key === "Backspace") {
            const span = this.childRefs[contentId]
            const cursorPosition = selectService.getCursorOffset()
            // If the cursor is at the beginning of the content (while we ignore any ZWSP)
            if (cursorPosition === 0 || (span?.textContent?.startsWith("​") && cursorPosition === 1)) {
                e.preventDefault()
                applyAction(Actions.merge, this.props.updateLetter, toContentId(this.props.id, contentId), MergeTarget.PREVIOUS)
            }
        }
    }

    deleteHandler = (e: KeyboardEvent) => {
        if (e.key === "Delete") {
            const lastContentId = this.props.content.length - 1
            const lastContent = this.props.content[lastContentId]
            if (isTextContent(lastContent) && selectService.getCursorOffset() >= lastContent.text.length) {
                e.preventDefault()
                applyAction(Actions.merge, this.props.updateLetter, toContentId(this.props.id, lastContentId), MergeTarget.NEXT)
            }
        }
    }

    render() {
        const {id, content, editable, onFocus, updateLetter, focusStolen} = this.props
        if (!editable) {
            return (
                <div className={styles.notEditable}>
                    {content.map((c, contentId) => {
                        switch (c.type) {
                            case LITERAL:
                            case VARIABLE:
                                return <Text key={contentId} content={c}/>
                            case ITEM_LIST:
                                return <ItemList key={contentId} id={{...id, contentId}} editable={false} itemList={c} updateLetter={updateLetter} focusStolen={focusStolen} onFocus={onFocus}/>
                        }
                    })}
                </div>
            )
        } else {
            return (
                <div className={styles.content} onClick={this.stealFocusHandler} onFocus={onFocus}>
                    {content.map((c, contentId) => {
                            switch (c.type) {
                                case LITERAL:
                                    return <EditableText key={contentId}
                                                         content={c}
                                                         updateText={bindActionWithCallback(Actions.updateContentText, updateLetter, toContentId(id, contentId))}
                                                         innerRef={this.setChildRef.bind(this, contentId)}
                                    />
                                case VARIABLE:
                                    return <Text key={contentId} content={c}/>
                                case ITEM_LIST:
                                    return <ItemList key={contentId}
                                                     id={{...id, contentId}}
                                                     editable={editable}
                                                     itemList={c}
                                                     updateLetter={updateLetter}
                                                     stealFocus={this.props.stealFocus?.contentId === contentId ? this.props.stealFocus : undefined}
                                                     focusStolen={focusStolen}
                                                     onFocus={onFocus}
                                    />
                            }
                        }
                    )}
                </div>
            )
        }
    }
}

export default ContentGroup