import "./editor.css";

import { CheckmarkIcon, XMarkIcon } from "@navikt/aksel-icons";
import { Box, Button, Heading, HStack, VStack } from "@navikt/ds-react";
import { applyPatches } from "immer";
import React, { createContext, type Dispatch, type SetStateAction, useCallback, useContext, useState } from "react";

import { applyAction, type CallbackReceiver } from "~/Brevredigering/LetterEditor/lib/actions";
import TilbakestillMalModal from "~/components/TilbakestillMalModal";
import { useDragSelectUnifier } from "~/hooks/useDragSelectUnifier";
import { useSelectionDeleteHotkey } from "~/hooks/useSelectionDeleteHotKey";
import { TITLE_INDEX } from "~/types/brevbakerTypes";

import Actions from "./actions";
import { getBlockClassName } from "./actions/common";
import { ContentGroup } from "./components/ContentGroup";
import { EditorMenu } from "./components/EditorMenu";
import { SakspartView } from "./components/SakspartView";
import { SignaturView } from "./components/SignaturView";
import { useAttestantDiff } from "./diff/AttestantDiffContext";
import { isTekstValgHighlighted, useInsertedTekstValgHighlight } from "./InsertedTekstValgHighlight";
import { type LetterEditorState } from "./model/state";
import { useEditorKeyboardShortcuts } from "./utils";

const DebugPanel = React.lazy(() => import("./components/DebugPanel"));

export const LetterEditor = ({
  freeze,
  error,
  editorState,
  setEditorState,
  showDebug,
}: {
  freeze: boolean;
  error: boolean;
  editorState: LetterEditorState;
  setEditorState: Dispatch<SetStateAction<LetterEditorState>>;
  showDebug: boolean;
}) => {
  const letter = editorState.redigertBrev;
  const blocks = letter.blocks;
  const editorKeyboardShortcuts = useEditorKeyboardShortcuts(setEditorState);
  const highlightedIds = useInsertedTekstValgHighlight();
  const { diffHash, invalidateDiff } = useAttestantDiff();

  const [editorRoot, setEditorRoot] = useState<HTMLDivElement | null>(null);
  const editorRootRef = useCallback((el: HTMLDivElement | null) => setEditorRoot(el), []);

  useDragSelectUnifier(editorRoot, !freeze);

  useSelectionDeleteHotkey(
    editorRoot,
    (focus) => {
      if (diffHash) {
        invalidateDiff(diffHash);
      }
      applyAction(Actions.deleteSelection, setEditorState, focus);
    },
    !freeze,
  );

  const [vilTilbakestilleMal, setVilTilbakestilleMal] = useState(false);

  const canUndo = !freeze && editorState.history.entryPointer >= 0;
  const canRedo = !freeze && editorState.history.entryPointer < editorState.history.entries.length - 1;

  const undo = useCallback(() => {
    if (diffHash) {
      invalidateDiff(diffHash);
    }
    setEditorState((current) => {
      if (freeze || current.history.entryPointer < 0) return current;
      const entry = current.history.entries[current.history.entryPointer];
      const previous =
        entry.type === "SAKSBEHANDLERVALG_ENDRET"
          ? {
              ...current,
              redigertBrev: structuredClone(entry.before.redigertBrev),
              redigertBrevHash: entry.before.redigertBrevHash,
              saksbehandlerValg: structuredClone(entry.before.saksbehandlerValg),
            }
          : applyPatches(current, entry.inversePatches);
      return {
        ...previous,
        saveStatus: "DIRTY",
        history: {
          ...previous.history,
          entryPointer: current.history.entryPointer - 1,
        },
      };
    });
  }, [diffHash, freeze, invalidateDiff, setEditorState]);

  const redo = useCallback(() => {
    if (diffHash) {
      invalidateDiff(diffHash);
    }
    setEditorState((current) => {
      if (freeze || current.history.entryPointer >= current.history.entries.length - 1) return current;
      const nextPointer = current.history.entryPointer + 1;
      const entry = current.history.entries[nextPointer];
      const next =
        entry.type === "SAKSBEHANDLERVALG_ENDRET"
          ? {
              ...current,
              redigertBrev: structuredClone(entry.after.redigertBrev),
              redigertBrevHash: entry.after.redigertBrevHash,
              saksbehandlerValg: structuredClone(entry.after.saksbehandlerValg),
            }
          : applyPatches(current, entry.patches);
      return {
        ...next,
        saveStatus: "DIRTY",
        history: {
          ...next.history,
          entryPointer: nextPointer,
        },
      };
    });
  }, [diffHash, freeze, invalidateDiff, setEditorState]);

  return (
    <VStack overflowY="hidden">
      <EditorStateContext.Provider value={{ freeze, error, editorState, setEditorState, undo, redo }}>
        <EditorMenu
          canRedo={canRedo}
          canUndo={canUndo}
          redo={redo}
          setVilTilbakestilleMal={setVilTilbakestilleMal}
          undo={undo}
        />
        <VStack align="center" overflowY="auto">
          <Box className="editor" css={freeze ? { cursor: "wait" } : {}} height="100%">
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
                <div
                  className={getBlockClassName(block, isTekstValgHighlighted(highlightedIds, block))}
                  key={blockIndex}
                >
                  {block.missingFromTemplate && (
                    <HStack className="missing-from-template-actions" gap="space-4" justify="end">
                      <Button
                        icon={<CheckmarkIcon aria-hidden />}
                        onClick={() => applyAction(Actions.keepMissingFromTemplateBlock, setEditorState, blockIndex)}
                        size="xsmall"
                        type="button"
                        variant="secondary"
                      >
                        Behold
                      </Button>
                      <Button
                        icon={<XMarkIcon aria-hidden />}
                        onClick={() => applyAction(Actions.removeMissingFromTemplateBlock, setEditorState, blockIndex)}
                        size="xsmall"
                        type="button"
                        variant="secondary"
                      >
                        Slett
                      </Button>
                    </HStack>
                  )}
                  <ContentGroup literalIndex={{ blockIndex, contentIndex: 0 }} />
                </div>
              ))}
            </div>
            <SignaturView signatur={letter.signatur} />
          </Box>
        </VStack>
        {showDebug && <DebugPanel />}
        {/* Åpner modal, tar ikke plass i DOM her */}
        {vilTilbakestilleMal && (
          <TilbakestillMalModal
            brevId={editorState.info.id}
            onClose={() => setVilTilbakestilleMal(false)}
            resetEditor={(brevResponse) => setEditorState(Actions.create(brevResponse))}
            åpen={vilTilbakestilleMal}
          />
        )}
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
