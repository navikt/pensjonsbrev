import {ParagraphBlock} from "../../model";
import {FC} from "react";
import styles from "./Paragraph.module.css"
import Content, {SplitContent, Unlock, UpdateContent} from "../content/Content";

export interface ParagraphProps {
    block: ParagraphBlock
    doUnlock: Unlock
    updateContent: UpdateContent
    splitContent: SplitContent
}

const Paragraph: FC<ParagraphProps> = ({ block, doUnlock, updateContent, splitContent }) => (
    <div className={styles.paragraph}>
        <Content block={block} doUnlock={doUnlock} updateContent={updateContent} splitContent={splitContent}/>
    </div>
)
export default Paragraph