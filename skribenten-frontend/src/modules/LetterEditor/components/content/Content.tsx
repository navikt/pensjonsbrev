import {FC} from "react"
import Text from "../text/Text"
import styles from "./Content.module.css"
import {AnyBlock, TextContent} from "../../model"
import {bindAction, BoundAction} from "../../../../lib/actions"
import {TextContentAction} from "../../actions/content"

export interface ContentProps {
    block: AnyBlock
    doUnlock: Unlock
    updateContent: UpdateContent
    splitBlockAtContent: SplitBlockAtContent
}

export type Unlock = BoundAction<[]>
export type UpdateContent = BoundAction<[contentId: number, content: TextContent]>
export type SplitBlockAtContent = BoundAction<[contentId: number, currentText: string, nextText: string]>

const Content: FC<ContentProps> = ({block, doUnlock, updateContent, splitBlockAtContent}) => {
    if (block.locked) {
        return (
            <div className={styles.locked}>
                <div className={styles.content}>
                    {block.content.map((c, id) =>
                        <Text key={id} content={c}/>
                    )}
                </div>
                <button className={styles.lock} onClick={doUnlock}/>
            </div>
        )
    } else {
        return (
            <div className={styles.content}>
                {block.content.map((c, id) =>
                    <Text key={id}
                          content={c}
                          updateText={bindAction(TextContentAction.updateText, updateContent.bind(null, id), c)}
                          splitBlockAtConentWithText={splitBlockAtContent.bind(null, id)}/>
                )}
            </div>
        )
    }
}

export default Content