import {FC} from "react";
import {Title1Block} from "../../model";
import Content, {SplitContent, Unlock, UpdateContent} from "../content/Content";

export interface Title1Props {
    block: Title1Block
    doUnlock: Unlock
    updateContent: UpdateContent
    splitContent: SplitContent
}

const Title1: FC<Title1Props> = ({block, doUnlock, updateContent, splitContent}) => {
    return (
        <h2>
            <Content block={block} doUnlock={doUnlock} updateContent={updateContent} splitContent={splitContent}/>
        </h2>
    )
}

export default Title1