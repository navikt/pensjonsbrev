import React, {FC, useCallback} from "react"
import {LiteralValue} from "../../../../lib/model/skribenten"
import ContentEditable, {ContentEditableEvent} from "react-contenteditable"
import {BoundAction} from "../../../../lib/actions"
import {isEmptyContent} from "../../model/utils"
import styles from "./Text.module.css"

export interface EditableTextProps {
    content: LiteralValue
    updateText: BoundAction<[text: string]>
    innerRef: BoundAction<[node: HTMLElement]>
}


function onChangeHandler(updateText: BoundAction<[text: string]>): (e: ContentEditableEvent) => void {
    return (e: ContentEditableEvent) => {
        updateText(e.target.value)
    }
}

const EditableText: FC<EditableTextProps> = ({content, updateText, innerRef}) => {
    // const updateText = bindAction(TextContentAction.updateText, updateContent, content)
    // Passing innerRef as a dependency has some weird consequences such as the cursor skipping to the end on every edit.
    /* eslint-disable react-hooks/exhaustive-deps */
    const ref = useCallback((node: HTMLElement) => {
        innerRef(node)
    }, [])
    /* eslint-enable react-hooks/exhaustive-deps */

    return <ContentEditable className={isEmptyContent(content) ? styles.empty : ""} innerRef={ref} html={content.text || "​"} tagName="span" onChange={onChangeHandler(updateText)}/>
}

export default EditableText