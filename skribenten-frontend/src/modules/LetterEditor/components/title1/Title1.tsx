import {FC} from "react"
import {Title1Block} from "../../model"
import Content, {SplitBlockAtContent, Unlock, UpdateContent} from "../content/Content"
import styles from "./Title1.module.css"

export interface Title1Props {
    block: Title1Block
    doUnlock: Unlock
    updateContent: UpdateContent
    splitContent: SplitBlockAtContent
}

const Title1: FC<Title1Props> = ({block, doUnlock, updateContent, splitContent}) => {
    return (
        <h2 className={styles.container}>
            <Content block={block} doUnlock={doUnlock} updateContent={updateContent} splitBlockAtContent={splitContent}/>
        </h2>
    )
}

export default Title1