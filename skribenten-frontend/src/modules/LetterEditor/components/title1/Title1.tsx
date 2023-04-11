import {FC} from "react"
import {Title1Block} from "../../model/api"
import ContentGroup from "../contentgroup/ContentGroup"
import styles from "./Title1.module.css"
import {BlockProps} from "../../BlockProps"

const Title1: FC<BlockProps<Title1Block>> =
    ({block, updateContent, splitBlockAtContent, mergeWith, blockStealFocus, blockFocusStolen, onFocus}) => {
        return (
            <h2 className={styles.container}>
                <ContentGroup content={block.content}
                              editable={block.editable}
                              updateContent={updateContent}
                              splitAtContent={splitBlockAtContent}
                              mergeWith={mergeWith}
                              stealFocus={blockStealFocus}
                              focusStolen={blockFocusStolen}
                              onFocus={onFocus}
                />
            </h2>
        )
    }

export default Title1