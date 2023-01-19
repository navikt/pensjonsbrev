import {FC} from "react"
import {Title1Block} from "../../model"
import Content from "../content/Content"
import styles from "./Title1.module.css"
import {BlockProps} from "../../BlockProps"

const Title1: FC<BlockProps<Title1Block>> = ({block, doUnlock, updateContent, splitBlockAtContent, mergeWith, blockStealFocus, blockFocusStolen, onFocus}) => {
    return (
        <h2 className={styles.container}>
            <Content block={block}
                     doUnlock={doUnlock}
                     updateContent={updateContent}
                     splitBlockAtContent={splitBlockAtContent}
                     mergeWith={mergeWith}
                     blockStealFocus={blockStealFocus}
                     blockFocusStolen={blockFocusStolen}
                     onFocus={onFocus}
            />
        </h2>
    )
}

export default Title1