import {FC} from "react"
import {LITERAL, TextContent, VARIABLE} from "../../model/api"
import styles from "./Text.module.css"

export interface TextProps {
    content: TextContent
}

const Text: FC<TextProps> = ({content}) => {
    switch (content.type) {
        case LITERAL:
            return <span>{content.text}</span>
        case VARIABLE:
            return <span className={styles.variable}>{content.text}</span>
    }
}

export default Text