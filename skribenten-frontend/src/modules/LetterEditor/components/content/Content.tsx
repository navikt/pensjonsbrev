import {FC} from "react";
import Text, {SplitParagraph, UpdateText} from "../text/Text";
import styles from "./Content.module.css"
import {AnyBlock, TextContent} from "../../model";
import {Updater} from "../../../../lib/state";

export interface ContentProps {
    block: AnyBlock
    doUnlock: Unlock
    updateContent: UpdateContent
    splitContent: SplitContent
}

export type Unlock = () => void
export type UpdateContent = (contentId: number, mapper: Updater<TextContent>) => void
export type SplitContent = (contentId: number) => SplitParagraph

const Content: FC<ContentProps> = ({block, doUnlock, updateContent, splitContent}) => {
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
                    <Text key={id} content={c} updateText={text => updateContent(id, c => ({...c, text}))} splitParagraph={splitContent(id)}/>
                )}
            </div>
        )
    }
}

export default Content