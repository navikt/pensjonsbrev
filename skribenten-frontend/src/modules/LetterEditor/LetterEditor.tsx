import Title1 from "./components/title1/Title1"
import Paragraph from "./components/paragraph/Paragraph"
import styles from "./LetterEditor.module.css"
import {FC, useState} from "react"
import {AnyBlock, StealFocus, CursorPosition, RenderedLetter} from "./model"
import {bindAction, BoundAction, combine} from "../../lib/actions"
import {BlocksAction, MergeTarget} from "./actions/blocks"
import {BlockAction} from "./actions/block"
import SakspartView from "./components/sakspart/SakspartView"
import SignaturView from "./components/signatur/SignaturView"
import {RenderedLetterAction} from "./actions/letter"
import {SplitBlockAtContent} from "./BlockProps"
import EditorMenu from "./components/editormenu/EditorMenu"
import {StealFocusAction} from "./actions/stealfocus"

interface AnyBlockProps {
    block: AnyBlock,
    splitBlock: SplitBlockAtContent
    mergeWith: BoundAction<[target: MergeTarget]>
    updateBlock: BoundAction<[block: AnyBlock]>
    stealFocus?: CursorPosition
    blockFocusStolen: BoundAction<[]>
    onFocus: BoundAction<[]>
}

const AnyBlock: FC<AnyBlockProps> = ({block, splitBlock, mergeWith, updateBlock, stealFocus, blockFocusStolen, onFocus}) => {

    const doUnlock = bindAction(BlockAction.unlock, updateBlock, block)
    const updateBlockContent = bindAction(BlockAction.updateBlockContent, updateBlock, block)

    switch (block.type) {
        case 'TITLE1':
            return <Title1 block={block}
                           doUnlock={doUnlock}
                           updateContent={updateBlockContent}
                           splitBlockAtContent={splitBlock}
                           mergeWith={mergeWith}
                           blockStealFocus={stealFocus}
                           blockFocusStolen={blockFocusStolen}
                           onFocus={onFocus}
            />
        case 'PARAGRAPH':
            return <Paragraph block={block}
                              doUnlock={doUnlock}
                              updateContent={updateBlockContent}
                              splitBlockAtContent={splitBlock}
                              mergeWith={mergeWith}
                              blockStealFocus={stealFocus}
                              blockFocusStolen={blockFocusStolen}
                              onFocus={onFocus}
            />
    }
}

export interface LetterEditorProps {
    letter: RenderedLetter
    updateLetter: BoundAction<[letter: RenderedLetter]>
}

const LetterEditor: FC<LetterEditorProps> = ({letter, updateLetter}) => {
    const blocks = letter.blocks

    const [stealFocus, setStealFocus] = useState<StealFocus>({})
    const [currentBlock, setCurrentBlock] = useState(0)

    const updateBlocks = bindAction(RenderedLetterAction.updateBlocks, updateLetter, letter)
    const updateBlock = bindAction(BlocksAction.updateBlock, updateBlocks, blocks)

    const mergeWithAndStealFocus = combine(
        bindAction(BlocksAction.mergeWith, updateBlocks),
        bindAction(StealFocusAction.onMerge, setStealFocus, stealFocus)
    ).bind(null, blocks)

    const splitBlock = (blockId: number, block: AnyBlock, contentId: number, currentText: string, nextText: string) => {
        updateBlocks(BlocksAction.splitBlock(blocks, blockId, block, contentId, currentText, nextText))
        setStealFocus(StealFocusAction.onSplit(stealFocus, blockId))
    }

    const focusStolen = bindAction(StealFocusAction.focusStolen, setStealFocus, stealFocus)

    const switchType = bindAction(BlockAction.switchType, updateBlock.bind(null, currentBlock), blocks[currentBlock])

    return (
        <div className={styles.container}>
            <EditorMenu switchType={switchType} />
            <div className={styles.letter}>
                <SakspartView sakspart={letter.sakspart}/>
                <h1>{letter.title}</h1>
                {blocks.map((block, blockId) =>
                    <AnyBlock key={blockId}
                              block={block}
                              splitBlock={splitBlock.bind(null, blockId, block)}
                              mergeWith={mergeWithAndStealFocus.bind(null, blockId)}
                              updateBlock={updateBlock.bind(null, blockId)}
                              stealFocus={stealFocus[blockId]}
                              blockFocusStolen={focusStolen.bind(null, blockId)}
                              onFocus={setCurrentBlock.bind(null, blockId)}
                    />
                )}
                <SignaturView signatur={letter.signatur}/>
            </div>
            <button onClick={() => console.log(blocks)}>Save</button>
        </div>
    )
}

export default LetterEditor