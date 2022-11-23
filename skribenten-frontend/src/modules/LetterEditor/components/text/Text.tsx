import {FC} from "react";
import {TextContent} from "../../model";
import ContentEditable, {ContentEditableEvent} from "react-contenteditable";
import styles from "./Text.module.css"

export interface TextProps {
    content: TextContent
    updateText?: UpdateText
    splitParagraph?: SplitParagraph
}

export type UpdateText = (text: string) => void
export type SplitParagraph = (currentText: string, nextText: string) => void

function updateOrSplit(updateText: UpdateText, splitParagraph: SplitParagraph): (e: ContentEditableEvent) => void {
    return (e: ContentEditableEvent) => {
        const newText = e.target.value
        const brIndex = newText.indexOf("<br>")
        if (brIndex >= 0) {
            splitParagraph(newText.substring(0, brIndex), newText.substring(brIndex+4))
        } else {
            updateText(e.target.value)
        }
    }
}

const Text: FC<TextProps> = ({content, updateText, splitParagraph}) => {
    if (updateText && splitParagraph) {
        switch (content.type) {
            case "literal":
                return <ContentEditable html={content.text} tagName={'span'} onChange={updateOrSplit(updateText, splitParagraph)}/>
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