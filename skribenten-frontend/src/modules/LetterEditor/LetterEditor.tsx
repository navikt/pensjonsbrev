import Title1 from "./components/title1/Title1"
import Title2 from "./components/title2/Title2"
import Paragraph from "./components/paragraph/Paragraph"
import styles from "./LetterEditor.module.css"
import {Dispatch, FC, SetStateAction, useState} from "react"
import {AnyBlock, PARAGRAPH, TITLE1, TITLE2} from "../../lib/model/skribenten"
import {bindActionWithCallback, BoundAction, CallbackReceiver} from "../../lib/actions"
import SakspartView from "./components/sakspart/SakspartView"
import SignaturView from "./components/signatur/SignaturView"
import EditorMenu from "./components/editormenu/EditorMenu"
import {CursorPosition, LetterEditorState} from "./model/state"
import Actions from "./actions"

interface AnyBlockProps {
    block: AnyBlock,
    blockId: number,
    updateLetter: CallbackReceiver<LetterEditorState>
    stealFocus?: CursorPosition
    blockFocusStolen: BoundAction<[]>
    onFocus: BoundAction<[]>
}

const AnyBlockView: FC<AnyBlockProps> = ({block, blockId, updateLetter, stealFocus, blockFocusStolen, onFocus}) => {
    switch (block.type) {
        case TITLE1:
            return <Title1 block={block}
                           blockId={blockId}
                           updateLetter={updateLetter}
                           blockStealFocus={stealFocus}
                           blockFocusStolen={blockFocusStolen}
                           onFocus={onFocus}
            />
        case TITLE2:
            return <Title2 block={block}
                           blockId={blockId}
                           updateLetter={updateLetter}
                           blockStealFocus={stealFocus}
                           blockFocusStolen={blockFocusStolen}
                           onFocus={onFocus}
            />
        case PARAGRAPH:
            return <Paragraph block={block}
                              blockId={blockId}
                              updateLetter={updateLetter}
                              blockStealFocus={stealFocus}
                              blockFocusStolen={blockFocusStolen}
                              onFocus={onFocus}
            />
    }
}

export interface LetterEditorProps {
    editorState: LetterEditorState
    updateState: Dispatch<SetStateAction<LetterEditorState>>
}

const LetterEditor: FC<LetterEditorProps> = ({editorState, updateState}) => {
    const blocks = editorState.editedLetter.letter.blocks

    const [currentBlock, setCurrentBlock] = useState(0)

    const focusStolen = bindActionWithCallback(Actions.focusStolen, updateState)
    const switchType = bindActionWithCallback(Actions.switchType, updateState, currentBlock)

    return (
        <div className={styles.container}>
            <EditorMenu switchType={switchType}/>
            <div className={styles.letter}>
                <SakspartView sakspart={editorState.editedLetter.letter.sakspart}/>
                <h1>{editorState.editedLetter.letter.title}</h1>
                {blocks.map((block, blockId) =>
                    <AnyBlockView key={blockId}
                                  block={block}
                                  blockId={blockId}
                                  updateLetter={updateState}
                                  stealFocus={editorState.stealFocus[blockId]}
                                  blockFocusStolen={focusStolen.bind(null, blockId)}
                                  onFocus={setCurrentBlock.bind(null, blockId)}
                    />
                )}
                <SignaturView signatur={editorState.editedLetter.letter.signatur}/>
            </div>
            <button onClick={() => console.log(editorState.editedLetter)}>Save</button>
        </div>
    )
}

export default LetterEditor