import {ParagraphBlock} from "../../model/api"
import {FC} from "react"
import styles from "./Paragraph.module.css"
import ContentGroup from "../contentgroup/ContentGroup"
import {BlockProps} from "../../BlockProps"

const Paragraph: FC<BlockProps<ParagraphBlock>> =
    ({block, updateContent, splitBlockAtContent, mergeWith, blockStealFocus, blockFocusStolen, onFocus}) => (
        <div className={styles.paragraph}>
            <ContentGroup content={block.content}
                          editable={block.editable}
                          updateContent={updateContent}
                          splitAtContent={splitBlockAtContent}
                          mergeWith={mergeWith}
                          stealFocus={blockStealFocus}
                          focusStolen={blockFocusStolen}
                          onFocus={onFocus}
            />
        </div>
    )
export default Paragraph