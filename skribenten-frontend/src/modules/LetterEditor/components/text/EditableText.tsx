import React, {FC, useCallback} from "react"
import {TextContent} from "../../model"
import ContentEditable, {ContentEditableEvent} from "react-contenteditable"
import {bindAction, BoundAction} from "../../../../lib/actions"
import {TextContentAction} from "../../actions/content"

export interface EditableTextProps {
    content: TextContent
    updateContent: UpdateContent
    splitBlockAtContentWithText: SplitBlockAtContentWithText
    innerRef: BoundAction<[node: HTMLElement]>
}

export type UpdateContent = BoundAction<[content: TextContent]>
export type SplitBlockAtContentWithText = BoundAction<[currentText: string, nextText: string]>

function updateOrSplit(updateText: BoundAction<[text: string]>, split: SplitBlockAtContentWithText): (e: ContentEditableEvent) => void {
    return (e: ContentEditableEvent) => {
        const newText = e.target.value
        const brIndex = newText.indexOf("<br>")
        if (brIndex >= 0) {
            split(newText.substring(0, brIndex), newText.substring(brIndex + 4))
        } else {
            updateText(e.target.value)
        }
    }
}

const EditableText: FC<EditableTextProps> = ({content, updateContent, splitBlockAtContentWithText, innerRef}) => {
        const updateText = bindAction(TextContentAction.updateText, updateContent, content)
        const ref = useCallback((node: HTMLElement) => {
            innerRef(node)
        }, [])
        return <ContentEditable innerRef={ref} html={content.text} tagName={'span'} onChange={updateOrSplit(updateText, splitBlockAtContentWithText)}/>
}

export default EditableText