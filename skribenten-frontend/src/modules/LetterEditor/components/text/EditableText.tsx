import React, {FC, useCallback} from "react"
import {LiteralValue} from "../../model/api"
import ContentEditable, {ContentEditableEvent} from "react-contenteditable"
import {bindAction, BoundAction} from "../../../../lib/actions"
import {TextContentAction} from "../../actions/textcontent"

export interface EditableTextProps {
    content: LiteralValue
    updateContent: BoundAction<[content: LiteralValue]>
    innerRef: BoundAction<[node: HTMLElement]>
}


function onChangeHandler(updateText: BoundAction<[text: string]>): (e: ContentEditableEvent) => void {
    return (e: ContentEditableEvent) => {
        updateText(e.target.value)
    }
}

const EditableText: FC<EditableTextProps> = ({content, updateContent, innerRef}) => {
    const updateText = bindAction(TextContentAction.updateText, updateContent, content)
    const ref = useCallback((node: HTMLElement) => {
        innerRef(node)
    }, [innerRef])
    return <ContentEditable innerRef={ref} html={content.text} tagName="span" onChange={onChangeHandler(updateText)}/>
}

export default EditableText