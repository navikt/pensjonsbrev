import {FC} from "react"
import {Title1Block} from "../../model"
import Content from "../content/Content"
import styles from "./Title1.module.css"
import {BlockProps} from "../../BlockProps"

const Title1: FC<BlockProps<Title1Block>> = ({block, doUnlock, updateContent, splitBlockAtContent, mergeWithPrevious, blockStealFocus, blockFocusStolen}) => {
    return (
        <h2 className={styles.container}>
            <Content block={block}
                     doUnlock={doUnlock}
                     updateContent={updateContent}
                     splitBlockAtContent={splitBlockAtContent}
                     mergeWithPrevious={mergeWithPrevious}
                     blockStealFocus={blockStealFocus}
                     blockFocusStolen={blockFocusStolen}
            />
        </h2>
    )
}

export default Title1