import React, {MouseEventHandler} from "react"
import Text from "../text/Text"
import styles from "./Content.module.css"
import {Content, ITEM_LIST, LITERAL, TextContent, VARIABLE, ItemList as ItemListModel, LiteralValue} from "../../model/api"
import {SplitAtContent, UpdateContent} from "../../BlockProps"
import EditableText from "../text/EditableText"
import {SelectionService} from "../../services/SelectionService"
import {BoundAction, combine} from "../../../../lib/actions"
import ItemList from "../itemlist/ItemList"
import {MergeTarget} from "../../actions/common"
import {isTextContent} from "../../model/utils"
import {CursorPosition} from "../../model/state"

const selectService = new SelectionService()

type ContentGroupState = never

export interface ContentGroupProps<T extends Content> {
    content: T[]
    editable: boolean | undefined
    updateContent: UpdateContent<T>
    mergeWith: BoundAction<[target: MergeTarget]>
    splitAtContent: SplitAtContent
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
                keyHandler = combine(keyHandler, this.backspaceHandler)
            }
            if (contentId === this.props.content.length - 1) {
                keyHandler = combine(keyHandler, this.deleteHandler)
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
            selectService.focusAtOffset(span.childNodes[0], this.props.stealFocus.startOffset, this.props.focusStolen)
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
            this.props.splitAtContent(contentId, selectService.getCursorOffset())
        }
    }

    backspaceHandler = (e: KeyboardEvent) => {
        if (e.key === "Backspace") {
            if (selectService.getCursorOffset() === 0) {
                e.preventDefault()
                this.props.mergeWith(MergeTarget.PREVIOUS)
            }
        }
    }

    deleteHandler = (e: KeyboardEvent) => {
        if (e.key === "Delete") {
            const lastContent = this.props.content[this.props.content.length - 1]
            if (isTextContent(lastContent) && selectService.getCursorOffset() >= lastContent.text.length) {
                e.preventDefault()
                this.props.mergeWith(MergeTarget.NEXT)
            }
        }
    }

    render() {
        const {content, editable, updateContent, onFocus} = this.props
        if (!editable) {
            return (
                <div className={styles.notEditable}>
                    {content.map((c, contentId) => {
                        switch (c.type) {
                            case LITERAL:
                            case VARIABLE:
                                return <Text key={contentId} content={c}/>
                            case ITEM_LIST:
                                return <ItemList key={contentId} editable={false} itemList={c} updateList={(l: ItemListModel) => updateContent(contentId, l as T)}/>
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
                                                         updateContent={(c: LiteralValue) => updateContent(contentId, c as T)}
                                                         innerRef={this.setChildRef.bind(this, contentId)}
                                    />
                                case VARIABLE:
                                    return <Text key={contentId} content={c}/>
                                case ITEM_LIST:
                                    return <ItemList key={contentId} editable={editable} itemList={c} updateList={(l: ItemListModel) => updateContent(contentId, l as T)}/>
                            }
                        }
                    )}
                </div>
            )
        }
    }
}

export default ContentGroup