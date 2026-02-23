import "./editor.css";

import { Box, Heading, VStack } from "@navikt/ds-react";
import { applyPatches } from "immer";
import type { Dispatch, SetStateAction } from "react";
import { createContext, useCallback, useContext, useState } from "react";

import { DebugPanel } from "~/Brevredigering/LetterEditor/components/DebugPanel";
import { applyAction, type CallbackReceiver } from "~/Brevredigering/LetterEditor/lib/actions";
import { useDragSelectUnifier } from "~/hooks/useDragSelectUnifier";
import { useSelectionDeleteHotkey } from "~/hooks/useSelectionDeleteHotKey";
import { TITLE_INDEX } from "~/types/brevbakerTypes";

import Actions from "./actions";
import { ContentGroup } from "./components/ContentGroup";
import { EditorMenu } from "./components/EditorMenu";
import { SakspartView } from "./components/SakspartView";
import { SignaturView } from "./components/SignaturView";
import type { LetterEditorState } from "./model/state";
import { useEditorKeyboardShortcuts } from "./utils";

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
  const editorKeyboardShortcuts = useEditorKeyboardShortcuts(setEditorState);

  const [editorRoot, setEditorRoot] = useState<HTMLDivElement | null>(null);
  const editorRootRef = useCallback((el: HTMLDivElement | null) => setEditorRoot(el), []);

  useDragSelectUnifier(editorRoot, !freeze);

  useSelectionDeleteHotkey(editorRoot, (focus) => applyAction(Actions.deleteSelection, setEditorState, focus), !freeze);

  const canUndo = editorState.history.entryPointer >= 0;
  const canRedo = editorState.history.entryPointer < editorState.history.entries.length - 1;

  const undo = useCallback(() => {
    if (!canUndo) return;
    setEditorState((current) => {
      const { inversePatches } = current.history.entries[current.history.entryPointer];
      const previous = applyPatches(current, inversePatches);
      return {
        ...previous,
        saveStatus: "DIRTY",
        history: {
          ...previous.history,
          entryPointer: current.history.entryPointer - 1,
        },
      };
    });
  }, [canUndo, setEditorState]);

  const redo = useCallback(() => {
    if (!canRedo) return;
    setEditorState((current) => {
      const nextPointer = current.history.entryPointer + 1;
      const { patches } = current.history.entries[nextPointer];
      const next = applyPatches(current, patches);
      return {
        ...next,
        saveStatus: "DIRTY",
        history: {
          ...next.history,
          entryPointer: nextPointer,
        },
      };
    });
  }, [canRedo, setEditorState]);

  return (
    <VStack align="center" height={editorHeight ?? "auto"}>
      <EditorStateContext.Provider value={{ freeze, error, editorState, setEditorState, undo, redo }}>
        <EditorMenu canRedo={canRedo} canUndo={canUndo} redo={redo} undo={undo} />
        <Box className="editor" css={freeze ? { cursor: "wait" } : {}} flexGrow="1" overflowY="auto">
          <SakspartView sakspart={letter.sakspart} spraak={editorState.info.spraak} />
          <Heading
            className="letter-title"
            level="1"
            onDragOver={(e) => {
              e.preventDefault();
              e.dataTransfer.dropEffect = "none";
            }}
            onDragStart={(e) => e.preventDefault()}
            onDrop={(e) => e.preventDefault()}
            size="medium"
          >
            <ContentGroup literalIndex={{ blockIndex: TITLE_INDEX, contentIndex: 0 }} />
          </Heading>
          {/**
           * biome-ignore lint/a11y/noStaticElementInteractions: Redigeringsflaten inneholder
           * redigerbar, og derfor interaktiv, tekst
           */}
          <div
            className="editor-surface"
            data-editor-root
            onDragOver={(e) => {
              e.preventDefault();
              e.dataTransfer.dropEffect = "none";
            }}
            onDragStart={(e) => e.preventDefault()}
            onDrop={(e) => e.preventDefault()}
            onKeyDown={editorKeyboardShortcuts}
            ref={editorRootRef}
          >
            {blocks.map((block, blockIndex) => (
              <div className={block.type} key={blockIndex}>
                <ContentGroup literalIndex={{ blockIndex, contentIndex: 0 }} />
              </div>
            ))}
          </div>
          <SignaturView signatur={letter.signatur} />
        </Box>
        {showDebug && <DebugPanel />}
      </EditorStateContext.Provider>
    </VStack>
  );
};

export const EditorStateContext = createContext<{
  freeze: boolean;
  error: boolean;
  editorState: LetterEditorState;
  setEditorState: CallbackReceiver<LetterEditorState>;
  undo: () => void;
  redo: () => void;
}>({
  freeze: false,
  error: false,
  editorState: {} as LetterEditorState,
  setEditorState: () => {},
  undo: () => {},
  redo: () => {},
});
export const useEditor = () => {
  return useContext(EditorStateContext);
};
