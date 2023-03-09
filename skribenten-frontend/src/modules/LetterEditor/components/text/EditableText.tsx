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
    // Passing innerRef as a dependency has some weird consequences such as the cursor skipping to the end on every edit.
    /* eslint-disable react-hooks/exhaustive-deps */
    const ref = useCallback((node: HTMLElement) => {
        innerRef(node)
    }, [])
    /* eslint-enable react-hooks/exhaustive-deps */

    return <ContentEditable innerRef={ref} html={content.text} tagName="span" onChange={onChangeHandler(updateText)}/>
}

export default EditableText