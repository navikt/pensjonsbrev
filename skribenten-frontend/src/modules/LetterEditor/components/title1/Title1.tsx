import {FC} from "react"
import {Title1Block} from "../../../../lib/model/skribenten"
import ContentGroup from "../contentgroup/ContentGroup"
import styles from "./Title1.module.css"
import {BlockProps} from "../../BlockProps"

const Title1: FC<BlockProps<Title1Block>> =
    ({block, blockId, updateLetter, blockStealFocus, blockFocusStolen, onFocus}) => (
        <h2 className={styles.container}>
            <ContentGroup id={{blockId}}
                          updateLetter={updateLetter}
                          content={block.content}
                          editable={block.editable}
                          stealFocus={blockStealFocus}
                          focusStolen={blockFocusStolen}
                          onFocus={onFocus}
            />
        </h2>
    )

export default Title1