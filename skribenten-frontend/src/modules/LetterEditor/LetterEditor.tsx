import Title1 from "./components/title1/Title1"
import Paragraph from "./components/paragraph/Paragraph"
import styles from "./LetterEditor.module.css"
import {FC, useState} from "react"
import {AnyBlock, RenderedLetter} from "./model"
import {bindAction, BoundAction} from "../../lib/actions"
import {BlocksAction, MERGE_TARGET} from "./actions/blocks"
import {BlockAction} from "./actions/block"
import SakspartView from "./components/sakspart/SakspartView"
import SignaturView from "./components/signatur/SignaturView"
import {RenderedLetterAction} from "./actions/letter"
import {SplitBlockAtContent} from "./BlockProps"
import EditorMenu from "./components/editormenu/EditorMenu"
import {CursorPosition} from "./components/content/Content"

interface AnyBlockProps {
    block: AnyBlock,
    splitBlock: SplitBlockAtContent
    mergeWith: BoundAction<[target: MERGE_TARGET]>
    updateBlock: BoundAction<[block: AnyBlock]>
    stealFocus?: CursorPosition
    blockFocusStolen: BoundAction<[]>
}

const AnyBlock: FC<AnyBlockProps> = ({block, splitBlock, mergeWith, updateBlock, stealFocus, blockFocusStolen}) => {

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
            />
        case 'PARAGRAPH':
            return <Paragraph block={block}
                              doUnlock={doUnlock}
                              updateContent={updateBlockContent}
                              splitBlockAtContent={splitBlock}
                              mergeWith={mergeWith}
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

    const [stealFocus, setStealFocus] = useState<{[blockId: number]: CursorPosition | undefined}>({})
    const [currentBlock, setCurrentBlock] = useState(-1)

    const updateBlocks = bindAction(RenderedLetterAction.updateBlocks, updateLetter, letter)
    const updateBlock = bindAction(BlocksAction.updateBlock, updateBlocks, blocks)
    const mergeWith = bindAction(BlocksAction.mergeWith, updateBlocks, blocks)

    const mergeWithAndStealFocus: BoundAction<[blockId: number, target: MERGE_TARGET]> = (blockId, target) => {
        mergeWith(blockId, target)
        switch (target) {
            case MERGE_TARGET.PREVIOUS:
                const prev = blocks[blockId - 1]
                if (prev) {
                    const lastContentId = prev.content.length - 1
                    setStealFocus({[blockId - 1]: {contentId: lastContentId, startOffset: prev.content[lastContentId].text.length}})
                }
                break
            case MERGE_TARGET.NEXT:
                const current = blocks[blockId]
                if (current) {
                    const lastContentId = current.content.length - 1
                    setStealFocus({[blockId]: {contentId: lastContentId, startOffset: current.content[lastContentId].text.length}})
                }
                break
        }
    }

    const splitBlock = (blockId: number, block: AnyBlock, contentId: number, currentText: string, nextText: string) => {
        updateBlocks(BlocksAction.splitBlock(blocks, blockId, block, contentId, currentText, nextText))
        setStealFocus({[blockId + 1]: {contentId: 0, startOffset: 0}})
    }

    const switchType = bindAction(BlockAction.switchType, updateBlock.bind(null, currentBlock), blocks[currentBlock])

    return (
        <div className={styles.container}>
            <input type="number" value={currentBlock} onChange={e => setCurrentBlock(Number(e.target.value))}/>
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
                              blockFocusStolen={() => setStealFocus({})}
                    />
                )}
                <SignaturView signatur={letter.signatur}/>
            </div>
            <button onClick={() => console.log(blocks)}>Save</button>
        </div>
    )
}

export default LetterEditor