import "~/Brevredigering/LetterEditor/editor.css";

import { Box, Heading, VStack } from "@navikt/ds-react";
import React, { type DragEvent, useCallback, useMemo, useRef, useState } from "react";

import { useDragSelectUnifier } from "~/hooks/useDragSelectUnifier";
import { type SaksinformasjonV2, type SignaturV2 } from "~/types/brevV2";

import { DeletedBlocksAtV2 } from "../diff/DeletedMarkupV2";
import { TITLE } from "../model/paths";
import { apply, type EditorStateV2, type Updater } from "../state/editorState";
import { redo, undo } from "../state/history";
import { BlockView } from "./BlockView";
import { type Dispatch, EditorContextV2, useEditorV2 } from "./EditorContextV2";
import { SaksinformasjonView, SignaturView } from "./LetterChrome";
import { EditorMenuV2 } from "./menu/EditorMenuV2";
import { TextContainerView } from "./text/TextContainerView";
import { useEditorShortcuts } from "./useEditorShortcuts";

const DebugPanelV2 = React.lazy(() => import("./DebugPanelV2"));

export type EditorV2Props<S extends EditorStateV2> = {
  freeze: boolean;
  error: boolean;
  editorState: S;
  setEditorState: Updater<S>;
  showDebug?: boolean;
  renderTilbakestillModal?: (args: { open: boolean; onClose: () => void }) => React.ReactNode;
  diffToggleMenu?: React.ReactNode;
  saksinformasjon?: SaksinformasjonV2;
  signatur?: SignaturV2;
};

const preventDrag = {
  onDragOver: (event: DragEvent) => {
    event.preventDefault();
    event.dataTransfer.dropEffect = "none";
  },
  onDragStart: (event: DragEvent) => event.preventDefault(),
  onDrop: (event: DragEvent) => event.preventDefault(),
};

export const EditorV2 = <S extends EditorStateV2>({
  freeze,
  error,
  editorState,
  setEditorState,
  showDebug,
  renderTilbakestillModal,
  diffToggleMenu,
  saksinformasjon,
  signatur,
}: EditorV2Props<S>) => {
  const dispatch = useCallback<Dispatch>(
    (command, ...args) => {
      if (!freeze) apply(setEditorState, command, ...args);
    },
    [freeze, setEditorState],
  );
  const undoLast = useCallback(() => !freeze && setEditorState(undo), [freeze, setEditorState]);
  const redoNext = useCallback(() => !freeze && setEditorState(redo), [freeze, setEditorState]);
  const context = useMemo(
    () => ({ freeze, error, state: editorState, dispatch, undo: undoLast, redo: redoNext }),
    [freeze, error, editorState, dispatch, undoLast, redoNext],
  );
  const [tilbakestill, setTilbakestill] = useState(false);

  return (
    <VStack height="100%" overflowY="hidden">
      <EditorContextV2.Provider value={context}>
        <EditorMenuV2 onTilbakestill={renderTilbakestillModal ? () => setTilbakestill(true) : undefined} />
        {diffToggleMenu}
        <VStack align="center" flexGrow="1" minHeight="0" overflowY="auto">
          <EditorSurface saksinformasjon={saksinformasjon} signatur={signatur} />
        </VStack>
        {showDebug && <DebugPanelV2 />}
        {tilbakestill && renderTilbakestillModal?.({ open: true, onClose: () => setTilbakestill(false) })}
      </EditorContextV2.Provider>
    </VStack>
  );
};

function EditorSurface({
  saksinformasjon,
  signatur,
}: Pick<EditorV2Props<EditorStateV2>, "saksinformasjon" | "signatur">) {
  const { state, freeze } = useEditorV2();
  const document = state.redigertBrev;
  const shortcuts = useEditorShortcuts();
  const [root, setRoot] = useState<HTMLDivElement | null>(null);
  const [showFocusRing, setShowFocusRing] = useState(false);
  const tabHandled = useRef(false);

  useDragSelectUnifier(root, !freeze);

  return (
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
        tabHandled.current = event.key === "Tab" && event.defaultPrevented;
      }}
      onKeyUp={(event) => {
        if (event.key === "Tab" && !tabHandled.current) setShowFocusRing(true);
        tabHandled.current = false;
      }}
      onPointerDown={() => setShowFocusRing(false)}
    >
      {saksinformasjon && <SaksinformasjonView info={saksinformasjon} spraak={state.info.spraak} />}
      <Heading className="letter-title" level="1" size="medium" {...preventDrag}>
        <TextContainerView content={document.title.text} path={TITLE} />
      </Heading>
      {/* biome-ignore lint/a11y/noStaticElementInteractions: Redigeringsflaten inneholder redigerbar, og derfor interaktiv, tekst */}
      <div className="editor-surface" data-editor-root onKeyDown={shortcuts} ref={setRoot} {...preventDrag}>
        {document.blocks.map((block, index) => (
          <React.Fragment key={index}>
            <DeletedBlocksAtV2 block={index} />
            <BlockView block={block} index={index} />
          </React.Fragment>
        ))}
        <DeletedBlocksAtV2 block={document.blocks.length} trailing />
      </div>
      {signatur && <SignaturView signatur={signatur} />}
    </Box>
  );
}
