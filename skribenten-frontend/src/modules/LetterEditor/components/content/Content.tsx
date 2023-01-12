import React, {MouseEventHandler} from "react"
import Text from "../text/Text"
import styles from "./Content.module.css"
import {AnyBlock} from "../../model"
import {BlockProps} from "../../BlockProps"
import EditableText from "../text/EditableText"
import {SelectionService} from "../../services/SelectionService"
import {MERGE_TARGET} from "../../actions/blocks"

const selectService = new SelectionService()

type ContentState = never

class Content extends React.Component<BlockProps<AnyBlock>, ContentState> {
    private childRefs: { [contentId: number]: HTMLElement } = {}

    constructor(props: BlockProps<AnyBlock>) {
        super(props)
    }

    // TODO: Bake denne inn i useCallback her istedenfor i EditableText?
    setChildRef(contentId: number, node: HTMLElement) {
        const prev = this.childRefs[contentId]

        if (contentId === 0) {
            // TODO: Skriver dette over emitchange i ContentEditable?
            if (prev) {
                prev.onkeydown = null
            }
            if (node) {
                node.onkeydown = this.backspaceHandler
            }
            if (this.props.blockStealFocus && node != null) {
                selectService.focusStartOfNode(node, this.props.blockFocusStolen)
            }
        } else if (contentId === this.props.block.content.length - 1) {
            if (prev) {
                prev.onkeydown = null
            }
            if (node) {
                node.onkeydown = this.deleteHandler
            }
        }

        this.childRefs[contentId] = node
    }

    componentDidUpdate() {
        const first = this.childRefs[0]
        if (this.props.blockStealFocus && first != null) {
            if (first) {
                selectService.focusStartOfNode(first, this.props.blockFocusStolen)
            } else {
                console.warn("Got stealBlockFocus, but first child is not EditableText (or spanref is missing)")
            }
        }
    }

    stealFocusHandler: MouseEventHandler<HTMLDivElement> = (e) => {
        if (e.target === e.currentTarget) {
            const closestContent = selectService.findClosestNodeId(this.childRefs, {x: e.clientX, y: e.clientY})
            const node = this.childRefs[closestContent]
            if (node) {
                selectService.focusEndOfClickedLine(node, e.clientY)
            } else {
                console.warn("Attempted to steal focus on click, but the closest EditableText contentId does not have a spanRef: ", closestContent)
            }
        }
    }

    backspaceHandler = (e: KeyboardEvent) => {
        if (e.key === "Backspace") {
            const sel = window.getSelection() as Selection
            const range = sel.getRangeAt(0)
            if (range != null && range.startOffset === 0 && range.collapsed) {
                e.preventDefault()
                this.props.mergeWith(MERGE_TARGET.PREVIOUS)
            }
        }
    }

    deleteHandler = (e: KeyboardEvent) => {
        if (e.key === "Delete") {
            const sel = window.getSelection() as Selection
            const range = sel && sel.getRangeAt(0)
            const lastContent = this.props.block.content[this.props.block.content.length - 1]
            if (range != null && range.startOffset >= lastContent.text.length && range.collapsed) {
                e.preventDefault()
                this.props.mergeWith(MERGE_TARGET.NEXT)
            }
        }
    }

    render() {
        const {block, doUnlock, updateContent, splitBlockAtContent} = this.props
        if (!block.editable) {
            return (
                <div className={styles.notEditable}>
                    {block.content.map((c, id) =>
                        <Text key={id} content={c}/>
                    )}
                </div>
            )
        } else if (block.locked) {
            return (
                <div className={styles.locked}>
                    <div className={styles.content}>
                        {block.content.map((c, id) =>
                            <Text key={id} content={c}/>
                        )}
                    </div>
                    <button className={styles.lock} onClick={doUnlock}/>
                </div>
            )
        } else {
            return (
                <div className={styles.content} onClick={this.stealFocusHandler}>
                    {block.content.map((c, contentId) => {
                            switch (c.type) {
                                case "LITERAL":
                                    return <EditableText key={contentId}
                                                         content={c}
                                                         updateContent={updateContent.bind(null, contentId)}
                                                         splitBlockAtContentWithText={splitBlockAtContent.bind(null, contentId)}
                                                         innerRef={this.setChildRef.bind(this, contentId)}
                                    />
                                case "VARIABLE":
                                    return <Text key={contentId} content={c}/>
                            }
                        }
                    )}
                </div>
            )
        }
    }
}

export default Content