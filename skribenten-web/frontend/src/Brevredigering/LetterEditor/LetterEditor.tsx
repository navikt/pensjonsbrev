import "./editor.css";

import { css } from "@emotion/react";
import { Heading } from "@navikt/ds-react";
import type { Dispatch, SetStateAction } from "react";
import { createContext, useContext } from "react";

import { DebugPanel } from "~/Brevredigering/LetterEditor/components/DebugPanel";
import { type CallbackReceiver } from "~/Brevredigering/LetterEditor/lib/actions";
import type { Nullable } from "~/types/Nullable";

import { ContentGroup } from "./components/ContentGroup";
import { EditorMenu } from "./components/EditorMenu";
import { SakspartView } from "./components/SakspartView";
import { SignaturView } from "./components/SignaturView";
import type { LetterEditorState } from "./model/state";
import { getCursorOffset } from "./services/caretUtils";
import { useEditorKeyboardShortcuts } from "./utils";

const onKeyDown = (e: React.KeyboardEvent<HTMLSpanElement>) => {
  if (e.key === "Backspace") {
    handleBackspace(e);
  }

  if (e.key === "Delete") {
    handleDelete(e);
  }
};

const getAdjacentNodes = (): {
  currentNode: Nullable<Node>;
  previousNode: Nullable<Node>;
  nextNode: Nullable<Node>;
} => {
  const selection = window.getSelection();
  if (!selection || !selection.anchorNode) {
    return { currentNode: null, previousNode: null, nextNode: null };
  }
  const spanElement = selection.anchorNode.parentElement as HTMLSpanElement | null;
  if (!spanElement) {
    return { currentNode: null, previousNode: null, nextNode: null };
  }

  const previousNode = spanElement.previousSibling;
  const nextNode = spanElement.nextSibling;

  return { currentNode: spanElement, previousNode, nextNode };
};

const handleDelete = (e: React.KeyboardEvent<HTMLSpanElement>) => {
  const cursorPosition = getCursorOffset();
  const { currentNode, nextNode } = getAdjacentNodes();

  if (!currentNode && !nextNode) {
    return;
  }

  const isProtected = (nextNode as HTMLElement).dataset.protected === "true";

  if (isProtected && cursorPosition === currentNode?.textContent?.length) {
    // Prevent deleting protected variable
    e.preventDefault();
  }
};

const handleBackspace = (e: React.KeyboardEvent<HTMLSpanElement>) => {
  const cursorPosition = getCursorOffset();
  const { previousNode } = getAdjacentNodes();

  if (!previousNode) {
    return;
  }

  const isProtected = (previousNode as HTMLElement).dataset.protected === "true";

  if (isProtected && cursorPosition === 0) {
    // Prevent deleting protected variable
    e.preventDefault();
  }
};

export const LetterEditor = ({
  freeze,
  error,
  editorState,
  setEditorState,
  editorHeight,
  showDebug,
}: {
  freeze: boolean;
  error: boolean;
  editorState: LetterEditorState;
  setEditorState: Dispatch<SetStateAction<LetterEditorState>>;
  editorHeight?: string;
  showDebug: boolean;
}) => {
  const letter = editorState.redigertBrev;
  const blocks = letter.blocks;
  const editorKeyboardShortcuts = useEditorKeyboardShortcuts(editorState, setEditorState);

  return (
    <div
      css={css`
        display: flex;
        flex-direction: column;
        align-items: center;
        height: ${editorHeight ?? "auto"};
        overflow-y: auto;
      `}
    >
      <EditorStateContext.Provider value={{ freeze, error, editorState, setEditorState }}>
        <div
          css={css`
            position: sticky;
            top: 0;
            width: 100%;
            z-index: 1;
          `}
        >
          <EditorMenu />
        </div>
        <div className="editor">
          <SakspartView sakspart={letter.sakspart} />
          <Heading
            css={css`
              margin: var(--a-spacing-8) 0;
            `}
            level="1"
            size="large"
          >
            {letter.title}
          </Heading>
          <div
            // NOTE: ideally this would be "plaintext-only", and it works in practice.
            // However, the tests will not work if set to plaintext-only. For some reason focus/input and other events will not be triggered by userEvent as expected.
            // This is not documented anywhere I could find and caused a day of frustration, beware
            contentEditable={!freeze}
            onKeyDown={(e) => {
              onKeyDown(e);
              editorKeyboardShortcuts(e);
            }}
          >
            {blocks.map((block, blockIndex) => (
              <div className={block.type} key={blockIndex}>
                <ContentGroup literalIndex={{ blockIndex, contentIndex: 0 }} />
              </div>
            ))}
          </div>
          <SignaturView signatur={letter.signatur} />
        </div>
        {showDebug && <DebugPanel />}
      </EditorStateContext.Provider>
    </div>
  );
};

export const EditorStateContext = createContext<{
  freeze: boolean;
  error: boolean;
  editorState: LetterEditorState;
  setEditorState: CallbackReceiver<LetterEditorState>;
}>({
  freeze: false,
  error: false,
  editorState: {} as LetterEditorState,
  setEditorState: () => {},
});
export const useEditor = () => {
  return useContext(EditorStateContext);
};
