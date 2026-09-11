import { createContext, type ReactNode, useCallback, useContext, useMemo, useRef } from "react";

import { type Redigeringsflate } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";

/**
 * Which document the editor surface is currently showing. The letter is the default;
 * an editable attachment is identified by its vedleggId.
 */
export type ActiveDocument = { type: "brev" } | { type: "vedlegg"; vedleggId: string };

type ActiveDocumentContextValue = {
  activeDocument: ActiveDocument;
  redigeringsflate: Redigeringsflate;
  canReset: boolean;
  selectBrev: () => Promise<boolean>;
  selectVedlegg: (vedleggId: string) => Promise<boolean>;
  resetActiveVedlegg: () => void;
  registerReset: (reset: (() => void) | null) => void;
  /**
   * The active attachment editor registers its save function so navigation and submission can wait
   * for unsaved changes before leaving the editing session.
   */
  registerVedleggSave: (saveNow: (() => Promise<void>) | null) => void;
};

const ActiveDocumentContext = createContext<ActiveDocumentContextValue | null>(null);

/**
 * Coordinates switching between the letter and its editable attachment. The route owns activeVedleggId,
 * keeping the URL as the source of truth across reload and browser navigation.
 */
export const ActiveDocumentProvider = (props: {
  activeVedleggId: string | undefined;
  redigeringsflate: Redigeringsflate;
  onSelectDocument: (vedleggId: string | undefined) => Promise<boolean>;
  registerVedleggSave: (saveNow: (() => Promise<void>) | null) => void;
  children: ReactNode;
}) => {
  const { activeVedleggId, redigeringsflate, onSelectDocument, registerVedleggSave } = props;
  const resetActiveVedleggRef = useRef<(() => void) | null>(null);

  const selectBrev = useCallback(() => onSelectDocument(undefined), [onSelectDocument]);
  const selectVedlegg = useCallback((vedleggId: string) => onSelectDocument(vedleggId), [onSelectDocument]);
  const resetActiveVedlegg = useCallback(() => resetActiveVedleggRef.current?.(), []);
  const registerReset = useCallback((reset: (() => void) | null) => {
    resetActiveVedleggRef.current = reset;
  }, []);

  const value = useMemo<ActiveDocumentContextValue>(
    () => ({
      activeDocument:
        activeVedleggId === undefined ? { type: "brev" } : { type: "vedlegg", vedleggId: activeVedleggId },
      redigeringsflate: redigeringsflate,
      canReset: redigeringsflate === "saksbehandler-redigering",
      selectBrev: selectBrev,
      selectVedlegg: selectVedlegg,
      resetActiveVedlegg: resetActiveVedlegg,
      registerReset: registerReset,
      registerVedleggSave: registerVedleggSave,
    }),
    [
      activeVedleggId,
      redigeringsflate,
      selectBrev,
      selectVedlegg,
      resetActiveVedlegg,
      registerReset,
      registerVedleggSave,
    ],
  );

  return <ActiveDocumentContext.Provider value={value}>{props.children}</ActiveDocumentContext.Provider>;
};

export const useActiveDocument = (): ActiveDocumentContextValue => {
  const context = useContext(ActiveDocumentContext);
  if (!context) {
    throw new Error("useActiveDocument must be used within an <ActiveDocumentProvider>");
  }
  return context;
};
