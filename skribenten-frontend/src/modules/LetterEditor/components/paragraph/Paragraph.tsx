import {ParagraphBlock} from "../../model"
import {FC} from "react"
import styles from "./Paragraph.module.css"
import Content from "../content/Content"
import {BlockProps} from "../../BlockProps"

const Paragraph: FC<BlockProps<ParagraphBlock>> = ({block, doUnlock, updateContent, splitBlockAtContent, mergeWith, blockStealFocus, blockFocusStolen}) => (
    <div className={styles.paragraph}>
        <Content block={block}
                 doUnlock={doUnlock}
                 updateContent={updateContent}
                 splitBlockAtContent={splitBlockAtContent}
                 mergeWith={mergeWith}
                 blockStealFocus={blockStealFocus}
                 blockFocusStolen={blockFocusStolen}
        />
    </div>
)
export default Paragraph