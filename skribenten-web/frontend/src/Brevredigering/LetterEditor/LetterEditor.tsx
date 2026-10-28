import "./editor.css";

import { CheckmarkIcon, XMarkIcon } from "@navikt/aksel-icons";
import { Box, Button, Heading, HStack, VStack } from "@navikt/ds-react";
import { applyPatches } from "immer";
import isEqual from "lodash/isEqual";
import React, {
  createContext,
  type Dispatch,
  type SetStateAction,
  useCallback,
  useContext,
  useLayoutEffect,
  useRef,
  useState,
} from "react";

import { isLetterDocument } from "~/Brevredigering/LetterEditor/actions/common";
import { applyAction, type CallbackReceiver } from "~/Brevredigering/LetterEditor/lib/actions";
import { useDragSelectUnifier } from "~/hooks/useDragSelectUnifier";
import { useSelectionDeleteHotkey } from "~/hooks/useSelectionDeleteHotKey";
import { TITLE_INDEX } from "~/types/brevbakerTypes";
import { type MissingFromTemplateEventName, trackMissingFromTemplateAction } from "~/utils/editorTracking";

import Actions from "./actions";
import { countMissingFromTemplateBlocks, getBlockClassName } from "./actions/common";
import { ContentGroup } from "./components/ContentGroup";
import { EditorMenu } from "./components/EditorMenu";
import { SakspartView } from "./components/SakspartView";
import { SignaturView } from "./components/SignaturView";
import { useAttestantDiff } from "./diff/AttestantDiffContext";
import { DeletedBlocksAt } from "./diff/DeletedMarkup";
import { isTekstValgHighlighted, useInsertedTekstValgHighlight } from "./InsertedTekstValgHighlight";
import { type LetterEditorState } from "./model/state";
import { useRedigeringsflate } from "./RedigeringsflateContext";
import { selectSelectionIndex } from "./services/caretUtils";
import { useEditorKeyboardShortcuts } from "./utils";

const DebugPanel = React.lazy(() => import("./components/DebugPanel"));

export const LetterEditor = ({
  freeze,
  error,
  editorState,
  setEditorState,
  showDebug,
  renderTilbakestillModal,
  diffToggleMenu,
}: {
  freeze: boolean;
  error: boolean;
  editorState: LetterEditorState;
  setEditorState: Dispatch<SetStateAction<LetterEditorState>>;
  showDebug: boolean;
  /** Owned by the caller because what "tilbakestill" resets depends on the document being edited. */
  renderTilbakestillModal?: (args: { open: boolean; onClose: () => void }) => React.ReactNode;
  diffToggleMenu?: React.ReactNode;
}) => {
  const redigeringsflate = useRedigeringsflate();
  const letter = editorState.redigertBrev;
  const blocks = letter.blocks;
  const editorKeyboardShortcuts = useEditorKeyboardShortcuts(setEditorState);
  const highlightedIds = useInsertedTekstValgHighlight();

  const [editorRoot, setEditorRoot] = useState<HTMLDivElement | null>(null);
  const [showFocusRing, setShowFocusRing] = useState(false);
  const tabHandledByEditor = useRef(false);
  const editorRootRef = useCallback((el: HTMLDivElement | null) => setEditorRoot(el), []);

  useDragSelectUnifier(editorRoot, !freeze);

  const { disableDiffMode } = useAttestantDiff();
  useSelectionDeleteHotkey(
    editorRoot,
    (focus) => applyAction(Actions.deleteSelection, setEditorState, focus),
    !freeze,
    disableDiffMode,
  );

  // Restores a caret/selection requested by an action (e.g. after a font type change). Runs after the literals' own
  // layout effects, so the range can span several literals. Focus is reset to the start of the range afterwards, overriding
  // the focus event fired while restoring, so the literals don't move the caret again.
  const pendingSelection = editorState.focus.pendingSelection;
  useLayoutEffect(() => {
    if (!pendingSelection || !editorRoot || freeze) return;
    selectSelectionIndex(editorRoot, pendingSelection);
    const { start, end } = pendingSelection;
    const collapsed = isEqual(start, end);
    // Som ved en markering gjort av brukeren: uten cursorPosition respekterer EditableText DOM-markeringen.
    setEditorState((state) => ({ ...state, focus: collapsed ? start : { ...start, cursorPosition: undefined } }));
  }, [pendingSelection, editorRoot, freeze, setEditorState]);

  const sporMissingFromTemplateHandling = useCallback(
    (eventName: MissingFromTemplateEventName, blockIndex: number) =>
      trackMissingFromTemplateAction(eventName, redigeringsflate, {
        brevId: editorState.info.id,
        brevkode: editorState.info.brevkode,
        brevtype: editorState.info.brevtype,
        spraak: editorState.info.spraak,
        antallGjenstaaende: countMissingFromTemplateBlocks(letter) - 1,
        blockIndex,
      }),
    [redigeringsflate, editorState.info, letter],
  );

  const [vilTilbakestilleMal, setVilTilbakestilleMal] = useState(false);

  const showMissingFromTemplate = redigeringsflate !== "attestant-redigering";

  const canUndo = !freeze && editorState.history.entryPointer >= 0;
  const canRedo = !freeze && editorState.history.entryPointer < editorState.history.entries.length - 1;

  const undo = useCallback(() => {
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
  }, [freeze, setEditorState]);

  const redo = useCallback(() => {
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
  }, [freeze, setEditorState]);

  return (
    <VStack height="100%" overflowY="hidden">
      <EditorStateContext.Provider value={{ freeze, error, editorState, setEditorState, undo, redo }}>
        <EditorMenu
          canRedo={canRedo}
          canUndo={canUndo}
          redo={redo}
          setVilTilbakestilleMal={renderTilbakestillModal ? setVilTilbakestilleMal : undefined}
          undo={undo}
        />
        {diffToggleMenu}
        <VStack align="center" flexGrow="1" minHeight="0" overflowY="auto">
          <Box
            className="editor"
            css={freeze ? { cursor: "wait" } : {}}
            data-focus-ring={showFocusRing}
            flexShrink="0"
            minHeight="100%"
            onBlur={(event) => {
              if (!event.currentTarget.contains(event.relatedTarget)) setShowFocusRing(false);
            }}
            onKeyDown={(event) => {
              tabHandledByEditor.current = event.key === "Tab" && event.defaultPrevented;
            }}
            onKeyUp={(event) => {
              if (event.key === "Tab" && !tabHandledByEditor.current) setShowFocusRing(true);
              tabHandledByEditor.current = false;
            }}
            onPointerDown={() => setShowFocusRing(false)}
          >
            {isLetterDocument(letter) && <SakspartView sakspart={letter.sakspart} spraak={editorState.info.spraak} />}
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
                <React.Fragment key={blockIndex}>
                  <DeletedBlocksAt blockIndex={blockIndex} />
                  <div
                    className={getBlockClassName(
                      block,
                      isTekstValgHighlighted(highlightedIds, block),
                      showMissingFromTemplate,
                    )}
                  >
                    {block.missingFromTemplate && showMissingFromTemplate && (
                      <HStack className="missing-from-template-actions" gap="space-4" justify="end">
                        <Button
                          icon={<CheckmarkIcon aria-hidden />}
                          onClick={() => {
                            sporMissingFromTemplateHandling("blokk beholdt", blockIndex);
                            applyAction(Actions.keepMissingFromTemplateBlock, setEditorState, blockIndex);
                          }}
                          size="xsmall"
                          type="button"
                          variant="secondary"
                        >
                          Behold
                        </Button>
                        <Button
                          icon={<XMarkIcon aria-hidden />}
                          onClick={() => {
                            sporMissingFromTemplateHandling("blokk slettet", blockIndex);
                            applyAction(Actions.removeMissingFromTemplateBlock, setEditorState, blockIndex);
                          }}
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
                </React.Fragment>
              ))}
              <DeletedBlocksAt blockIndex={blocks.length} trailing />
            </div>
            {isLetterDocument(letter) && <SignaturView signatur={letter.signatur} />}
          </Box>
        </VStack>
        {showDebug && <DebugPanel />}
        {vilTilbakestilleMal && renderTilbakestillModal?.({ open: true, onClose: () => setVilTilbakestilleMal(false) })}
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
