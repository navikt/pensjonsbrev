import Title1 from "./components/title1/Title1"
import Paragraph from "./components/paragraph/Paragraph"
import styles from "./LetterEditor.module.css"
import {FC} from "react"
import {AnyBlock, RenderedLetter} from "./model"
import {bindAction, BoundAction} from "../../lib/actions"
import {SplitBlockAtContent} from "./components/content/Content"
import {BlocksAction} from "./actions/blocks"
import {BlockAction} from "./actions/block"
import SakspartView from "./components/sakspart/SakspartView"
import SignaturView from "./components/signatur/SignaturView"
import {RenderedLetterAction} from "./actions/letter"

interface AnyBlockProps {
    block: AnyBlock,
    splitBlock: SplitBlockAtContent
    updateBlock: BoundAction<[block: AnyBlock]>
}

const AnyBlock: FC<AnyBlockProps> = ({block, splitBlock, updateBlock}) => {

    const doUnlock = bindAction(BlockAction.unlock, updateBlock, block)
    const updateBlockContent = bindAction(BlockAction.updateBlockContent, updateBlock, block)

    switch (block.type) {
        case 'TITLE1':
            return <Title1 block={block} doUnlock={doUnlock} updateContent={updateBlockContent} splitContent={splitBlock}/>
        case 'PARAGRAPH':
            return <Paragraph block={block} doUnlock={doUnlock} updateContent={updateBlockContent} splitContent={splitBlock}/>
    }
}

export interface LetterEditorProps {
    letter: RenderedLetter
    updateLetter: BoundAction<[letter: RenderedLetter]>
}

const LetterEditor: FC<LetterEditorProps> = ({letter, updateLetter}) => {
    const blocks = letter.blocks

    const updateBlocks = bindAction(RenderedLetterAction.updateBlocks, updateLetter, letter)
    const updateBlock = bindAction(BlocksAction.updateBlock, updateBlocks, blocks)
    const splitBlock = bindAction(BlocksAction.splitBlock, updateBlocks, blocks)

    return (
        <div className={styles.container}>
            <div className={styles.letter}>
                <SakspartView sakspart={letter.sakspart}/>
                <h1>{letter.title}</h1>
                {blocks.map((block, blockId) =>
                    <AnyBlock key={blockId}
                              block={block}
                              splitBlock={splitBlock.bind(null, blockId, block)}
                              updateBlock={updateBlock.bind(null, blockId)}
                    />
                )}
                <SignaturView signatur={letter.signatur}/>
            </div>
            <button onClick={() => console.log(blocks)}>Save</button>
        </div>
    )
}

export default LetterEditor