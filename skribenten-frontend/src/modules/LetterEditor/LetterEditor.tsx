import Title1 from "./components/title1/Title1"
import Paragraph from "./components/paragraph/Paragraph"
import styles from "./LetterEditor.module.css"
import {FC, useState} from "react"
import {AnyBlock, RenderedLetter} from "./model"
import {bindAction, BoundAction} from "../../lib/actions"
import {BlocksAction} from "./actions/blocks"
import {BlockAction} from "./actions/block"
import SakspartView from "./components/sakspart/SakspartView"
import SignaturView from "./components/signatur/SignaturView"
import {RenderedLetterAction} from "./actions/letter"
import {SplitBlockAtContent} from "./BlockProps"

interface AnyBlockProps {
    block: AnyBlock,
    splitBlock: SplitBlockAtContent
    mergeWithPrevious: BoundAction<[]>
    updateBlock: BoundAction<[block: AnyBlock]>
    stealFocus: boolean
    blockFocusStolen: BoundAction<[]>
}

const AnyBlock: FC<AnyBlockProps> = ({block, splitBlock, mergeWithPrevious, updateBlock, stealFocus, blockFocusStolen}) => {

    const doUnlock = bindAction(BlockAction.unlock, updateBlock, block)
    const updateBlockContent = bindAction(BlockAction.updateBlockContent, updateBlock, block)

    switch (block.type) {
        case 'TITLE1':
            return <Title1 block={block}
                           doUnlock={doUnlock}
                           updateContent={updateBlockContent}
                           splitBlockAtContent={splitBlock}
                           mergeWithPrevious={mergeWithPrevious}
                           blockStealFocus={stealFocus}
                           blockFocusStolen={blockFocusStolen}
            />
        case 'PARAGRAPH':
            return <Paragraph block={block}
                              doUnlock={doUnlock}
                              updateContent={updateBlockContent}
                              splitBlockAtContent={splitBlock}
                              mergeWithPrevious={mergeWithPrevious}
                              blockStealFocus={stealFocus}
                              blockFocusStolen={blockFocusStolen}
            />
    }
}

export interface LetterEditorProps {
    letter: RenderedLetter
    updateLetter: BoundAction<[letter: RenderedLetter]>
}

const LetterEditor: FC<LetterEditorProps> = ({letter, updateLetter}) => {
    const blocks = letter.blocks

    const [stealFocusBlockId, setStealFocusBlockId] = useState<number | null>(null)

    const updateBlocks = bindAction(RenderedLetterAction.updateBlocks, updateLetter, letter)
    const updateBlock = bindAction(BlocksAction.updateBlock, updateBlocks, blocks)
    const mergeWithPrevious = bindAction(BlocksAction.mergeWithPreviousBlock, updateBlocks, blocks)

    const splitBlock = (blockId: number, block: AnyBlock, contentId: number, currentText: string, nextText: string) => {
        updateBlocks(BlocksAction.splitBlock(blocks, blockId, block, contentId, currentText, nextText))
        setStealFocusBlockId(blockId + 1)
    }


    return (
        <div className={styles.container}>
            <div className={styles.letter}>
                <SakspartView sakspart={letter.sakspart}/>
                <h1>{letter.title}</h1>
                {blocks.map((block, blockId) =>
                    <AnyBlock key={blockId}
                              block={block}
                              splitBlock={splitBlock.bind(null, blockId, block)}
                              mergeWithPrevious={mergeWithPrevious.bind(null, blockId)}
                              updateBlock={updateBlock.bind(null, blockId)}
                              stealFocus={stealFocusBlockId === blockId}
                              blockFocusStolen={() => setStealFocusBlockId(null)}
                    />
                )}
                <SignaturView signatur={letter.signatur}/>
            </div>
            <button onClick={() => console.log(blocks)}>Save</button>
        </div>
    )
}

export default LetterEditor