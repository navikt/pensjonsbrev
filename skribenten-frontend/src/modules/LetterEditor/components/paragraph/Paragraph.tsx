import {ParagraphBlock} from "../../../../lib/model/skribenten"
import {FC} from "react"
import styles from "./Paragraph.module.css"
import ContentGroup from "../contentgroup/ContentGroup"
import {BlockProps} from "../../BlockProps"

const Paragraph: FC<BlockProps<ParagraphBlock>> =
    ({block, blockId, updateLetter, blockStealFocus, blockFocusStolen, onFocus}) => (
        <div className={styles.paragraph}>
            <ContentGroup id={{blockId}}
                          updateLetter={updateLetter}
                          content={block.content}
                          editable={block.editable}
                          stealFocus={blockStealFocus}
                          focusStolen={blockFocusStolen}
                          onFocus={onFocus}
            />
        </div>
    )
export default Paragraph