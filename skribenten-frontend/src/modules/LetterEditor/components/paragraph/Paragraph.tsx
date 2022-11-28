import {ParagraphBlock} from "../../model"
import {FC} from "react"
import styles from "./Paragraph.module.css"
import Content, {SplitBlockAtContent, Unlock, UpdateContent} from "../content/Content"

export interface ParagraphProps {
    block: ParagraphBlock
    doUnlock: Unlock
    updateContent: UpdateContent
    splitContent: SplitBlockAtContent
}

const Paragraph: FC<ParagraphProps> = ({ block, doUnlock, updateContent, splitContent }) => (
    <div className={styles.paragraph}>
        <Content block={block} doUnlock={doUnlock} updateContent={updateContent} splitBlockAtContent={splitContent}/>
    </div>
)
export default Paragraph