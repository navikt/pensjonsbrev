import {FC} from "react"
import {Title2Block} from "../../model/api"
import ContentGroup from "../contentgroup/ContentGroup"
import styles from "./Title2.module.css"
import {BlockProps} from "../../BlockProps"

const Title2: FC<BlockProps<Title2Block>> =
    ({block, blockId, updateLetter, blockStealFocus, blockFocusStolen, onFocus}) => (
        <h3 className={styles.container}>
            <ContentGroup id={{blockId}}
                          updateLetter={updateLetter}
                          content={block.content}
                          editable={block.editable}
                          stealFocus={blockStealFocus}
                          focusStolen={blockFocusStolen}
                          onFocus={onFocus}
            />
        </h3>
    )

export default Title2