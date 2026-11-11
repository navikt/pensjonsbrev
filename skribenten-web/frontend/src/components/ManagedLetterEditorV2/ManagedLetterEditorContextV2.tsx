import { useQueryClient } from "@tanstack/react-query";
import isEqual from "lodash/isEqual";
import { createContext, type ReactNode, useCallback, useContext, useEffect, useRef } from "react";

import {
  brevV2Keys,
  lagreAttestertBrevtekstV2,
  oppdaterBrevV2,
  oppdaterRedigertBrevV2,
  tilbakestillBrevV2,
} from "~/api/brevV2-queries";
import { redigerbareVedleggKeys } from "~/api/redigerbareVedlegg-endpoints";
import { hentPdfForAttestering, hentPdfForBrev } from "~/api/sak-api-endpoints";
import { createLetterState, type LetterStateV2, type Updater } from "~/Brevredigering/EditorV2/state/editorState";
import { addHistoryEntry, emptyHistory, type HistoryEntry } from "~/Brevredigering/EditorV2/state/history";
import { useEditorAutosave } from "~/Brevredigering/EditorV2/state/useEditorAutosave";
import { useRedigeringsflate } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { type SaksbehandlerValg } from "~/types/brev";
import { type BrevResponseV2, type LetterV2 } from "~/types/brevV2";

import { sameDocument } from "./documentQueries";

type LetterHistoryEntry = HistoryEntry<LetterV2, SaksbehandlerValg>;

export type SaveOptionsV2 = {
  createHistoryEntry?: (previous: LetterStateV2, response: BrevResponseV2) => LetterHistoryEntry | null;
  preserveUnchangedValg?: boolean;
};

type ManagedLetterEditorContextV2Value = {
  editorState: LetterStateV2;
  setEditorState: Updater<LetterStateV2>;
  saveLetterOperation: (
    operation: (state: LetterStateV2) => Promise<BrevResponseV2>,
    options?: SaveOptionsV2,
  ) => Promise<BrevResponseV2>;
  saveFailed: boolean;
  savePendingChanges: () => Promise<void>;
  resetLetter: () => Promise<void>;
  resetting: boolean;
  registerSaveErrorReset: (reset: (() => void) | null) => void;
};

const ManagedLetterEditorContextV2 = createContext<ManagedLetterEditorContextV2Value | null>(null);

function historyAfterSave(
  state: LetterStateV2,
  response: BrevResponseV2,
  entry: LetterHistoryEntry | null | undefined,
): LetterStateV2["history"] {
  if (entry) return addHistoryEntry(state.history, entry);
  return sameDocument(state.redigertBrev, response.redigertBrev) ? state.history : emptyHistory();
}

const applySavedResponse = (
  state: LetterStateV2,
  response: BrevResponseV2,
  options?: SaveOptionsV2,
): LetterStateV2 => ({
  ...state,
  info: response.info,
  redigertBrev: response.redigertBrev,
  redigertBrevHash: response.redigertBrevHash,
  saksbehandlerValg:
    options?.preserveUnchangedValg && isEqual(state.saksbehandlerValg, response.saksbehandlerValg)
      ? state.saksbehandlerValg
      : response.saksbehandlerValg,
  history: historyAfterSave(state, response, options?.createHistoryEntry?.(state, response)),
});

export const ManagedLetterEditorContextV2Provider = (props: { brev: BrevResponseV2; children: ReactNode }) => {
  const queryClient = useQueryClient();
  const redigeringsflate = useRedigeringsflate();
  const brevId = props.brev.info.id;
  const saksId = props.brev.info.saksId;
  const savedBrev = useRef(props.brev);
  const observedBrev = useRef(props.brev);
  const saveErrorReset = useRef<(() => void) | null>(null);

  const registerSaveErrorReset = useCallback((reset: (() => void) | null) => {
    saveErrorReset.current = reset;
  }, []);

  const onSaved = (response: BrevResponseV2) => {
    savedBrev.current = response;
    queryClient.setQueryData(brevV2Keys.id(response.info.id), response);
    queryClient.setQueryData(brevV2Keys.attestering(response.info.id), response);
    const pdfQuery = redigeringsflate === "attestant-redigering" ? hentPdfForAttestering : hentPdfForBrev;
    void queryClient.resetQueries({ queryKey: pdfQuery.queryKey(response.info.id) });
  };

  const save = (state: LetterStateV2) => {
    saveErrorReset.current?.();
    const args = { saksId, brevId, redigertBrev: state.redigertBrev, frigiReservasjon: false };
    if (redigeringsflate === "attestant-redigering") return lagreAttestertBrevtekstV2(args);
    if (isEqual(state.saksbehandlerValg, savedBrev.current.saksbehandlerValg)) return oppdaterRedigertBrevV2(args);
    return oppdaterBrevV2({
      saksId,
      brevId,
      frigiReservasjon: false,
      request: { redigertBrev: state.redigertBrev, saksbehandlerValg: state.saksbehandlerValg },
    });
  };

  const { editorState, setEditorState, saveFailed, savePendingChanges, saveWith, reset, resetting } = useEditorAutosave<
    LetterStateV2,
    BrevResponseV2
  >({
    initialState: createLetterState(props.brev),
    save,
    applyLatestResponse: (state, response) => applySavedResponse(state, response, { preserveUnchangedValg: true }),
    onSaved,
  });

  const resetLetter = () =>
    reset(async () => {
      const response = await tilbakestillBrevV2(saksId, brevId);
      onSaved(response);
      void queryClient.invalidateQueries({ queryKey: redigerbareVedleggKeys.brev(brevId, redigeringsflate) });
      return createLetterState(response);
    });

  const saveLetterOperation = useCallback<ManagedLetterEditorContextV2Value["saveLetterOperation"]>(
    (operation, options) =>
      saveWith({
        save: async (state) => {
          const response = await operation(state);
          setEditorState((current) =>
            current.saksbehandlerValg === state.saksbehandlerValg
              ? { ...current, saksbehandlerValg: response.saksbehandlerValg }
              : current,
          );
          return response;
        },
        applyLatestResponse: (state, response) => applySavedResponse(state, response, options),
      }),
    [saveWith, setEditorState],
  );

  useEffect(() => {
    if (observedBrev.current === props.brev) return;
    observedBrev.current = props.brev;
    if (editorState.saveStatus === "SAVED" && savedBrev.current.redigertBrevHash !== props.brev.redigertBrevHash) {
      savedBrev.current = props.brev;
      void reset(async () => applySavedResponse(editorState, props.brev));
    }
  }, [editorState, props.brev, reset]);

  return (
    <ManagedLetterEditorContextV2.Provider
      value={{
        editorState,
        setEditorState,
        saveLetterOperation,
        saveFailed,
        savePendingChanges,
        resetLetter,
        resetting,
        registerSaveErrorReset,
      }}
    >
      {props.children}
    </ManagedLetterEditorContextV2.Provider>
  );
};

export const useManagedLetterEditorContextV2 = (): ManagedLetterEditorContextV2Value => {
  const context = useContext(ManagedLetterEditorContextV2);
  if (!context) {
    throw new Error("useManagedLetterEditorContextV2 must be used within a <ManagedLetterEditorContextV2Provider>");
  }
  return context;
};
