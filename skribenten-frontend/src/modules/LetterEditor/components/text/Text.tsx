import {FC} from "react"
import {TextContent} from "../../model"
import ContentEditable, {ContentEditableEvent} from "react-contenteditable"
import styles from "./Text.module.css"
import {BoundAction} from "../../../../lib/actions"

export interface TextProps {
    content: TextContent
    updateText?: UpdateText
    splitBlockAtConentWithText?: SplitBlockAtContentWithText
}

export type UpdateText = BoundAction<[text: string]>
export type SplitBlockAtContentWithText = BoundAction<[currentText: string, nextText: string]>

function updateOrSplit(updateText: UpdateText, split: SplitBlockAtContentWithText): (e: ContentEditableEvent) => void {
    return (e: ContentEditableEvent) => {
        const newText = e.target.value
        const brIndex = newText.indexOf("<br>")
        if (brIndex >= 0) {
            split(newText.substring(0, brIndex), newText.substring(brIndex+4))
        } else {
            updateText(e.target.value)
        }
    }
}

const Text: FC<TextProps> = ({content, updateText, splitBlockAtConentWithText}) => {
    if (updateText && splitBlockAtConentWithText) {
        switch (content.type) {
            case "literal":
                return <ContentEditable html={content.text} tagName={'span'} onChange={updateOrSplit(updateText, splitBlockAtConentWithText)}/>
            case "variable":
                return <span className={styles.variable}>{content.text}</span>
        }
    } else {
        switch (content.type) {
            case "literal":
                return <span>{content.text}</span>
            case "variable":
                return <span className={styles.variable}>{content.text}</span>
        }
    }
}

export default Text