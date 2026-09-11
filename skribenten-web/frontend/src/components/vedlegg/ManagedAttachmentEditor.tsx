import { useQuery, useQueryClient } from "@tanstack/react-query";
import isEqual from "lodash/isEqual";
import { useCallback, useEffect, useState } from "react";

import {
  getRedigerbartVedlegg,
  lagreRedigerbartVedlegg,
  redigerbareVedleggKeys,
  tilbakestillRedigerbartVedlegg,
} from "~/api/redigerbareVedlegg-endpoints";
import { hentPdfForAttestering, hentPdfForBrev } from "~/api/sak-api-endpoints";
import { normalizeDocumentForComparison, text } from "~/Brevredigering/LetterEditor/actions/common";
import { useDocumentAutosave } from "~/Brevredigering/LetterEditor/hooks/useDocumentAutosave";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { type Redigeringsflate } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { ApiError } from "~/components/ApiError";
import { useActiveDocument } from "~/components/brevOgVedlegg/ActiveDocumentContext";
import { CenteredLoader } from "~/components/CenteredLoader";
import TilbakestillVedleggModal from "~/components/vedlegg/TilbakestillVedleggModal";
import { type BrevResponse, type EditAttachment, type RedigerbartVedleggInfo } from "~/types/brev";
import { type EditedDocument } from "~/types/brevbakerTypes";

/** Mirrors how the backend renders the list title, so the cached title matches a refetched one. */
const formatVedleggTitle = (vedlegg: EditAttachment): string =>
  vedlegg.title.text.map((item) => text(item) ?? "").join("");

/**
 * Editor session for one editable attachment. It reuses LetterEditorState so the existing
 * LetterEditor and every content action work unchanged: `redigertBrev` holds the attachment's editable
 * body, `info` comes from the parent brev (for spraak), and hash/saksbehandlerValg are unused here
 * because an attachment is saved through its own endpoint.
 */
const createVedleggState = (brev: BrevResponse, vedlegg: EditAttachment): LetterEditorState => ({
  info: brev.info,
  redigertBrev: { title: vedlegg.title, blocks: vedlegg.blocks, deletedBlocks: vedlegg.deletedBlocks },
  redigertBrevHash: "",
  saksbehandlerValg: {},
  saveStatus: "SAVED",
  focus: { blockIndex: 0, contentIndex: 0 },
  history: { entries: [], entryPointer: -1 },
});

type AttachmentEditorProps = {
  saksId: string;
  brev: BrevResponse;
  vedleggId: string;
  vedleggTitle: string;
  freeze: boolean;
  redigeringsflate: Redigeringsflate;
};

export const ManagedAttachmentEditor = (props: AttachmentEditorProps) => {
  const { saksId, brev, vedleggId, redigeringsflate } = props;

  const vedleggQuery = useQuery({
    queryKey: getRedigerbartVedlegg.queryKey(brev.info.id, vedleggId, redigeringsflate),
    queryFn: ({ signal }) => getRedigerbartVedlegg.queryFn(saksId, brev.info.id, vedleggId, redigeringsflate, signal),
    // Pinned: only a save or reset response should ever update this query's data once the session
    // has activated with it — a background refetch (e.g. on window focus) that resolves after an
    // autosave must not put pre-edit content back in the cache for a later remount to pick up.
    staleTime: Number.POSITIVE_INFINITY,
    // Every mount of this component is a fresh activation of the attachment (see the key in
    // BrevOgVedleggEditor), and must read the latest server content rather than a cache entry left
    // over from — or shared with — a previous activation.
    refetchOnMount: "always",
  });

  // Require a fetch that completed *for this activation* before handing anything to the editor.
  // `isFetchedAfterMount` (rather than `isPending`/`isSuccess` alone) is what rules out an editable
  // fallback built from stale cached data while the mandatory refetch above is still in flight.
  if (vedleggQuery.isPending || !vedleggQuery.isFetchedAfterMount) {
    return <CenteredLoader label="Henter vedlegg..." verticalStrategy="height" />;
  }
  if (vedleggQuery.isError) {
    return <ApiError error={vedleggQuery.error} title="Klarte ikke å hente vedlegget" />;
  }

  return <AttachmentEditorSession {...props} initialVedlegg={vedleggQuery.data} key={vedleggId} />;
};

const AttachmentEditorSession = (props: AttachmentEditorProps & { initialVedlegg: EditAttachment }) => {
  const { saksId, brev, vedleggId, redigeringsflate } = props;
  const queryClient = useQueryClient();
  const { registerVedleggSave, registerReset } = useActiveDocument();
  // Captured once from the activation fetch above. Later cache writes for this query key (a
  // window-focus refetch racing a save, for instance) are deliberately not observed here — this
  // session owns its document from here on, and only local edits, its own save responses, and its
  // own reset responses are allowed to change it.
  const [editorState, setEditorState] = useState<LetterEditorState>(() =>
    createVedleggState(brev, props.initialVedlegg),
  );
  const [resetModalOpen, setResetModalOpen] = useState(false);
  const [vedleggMeta, setVedleggMeta] = useState({ includeSakspart: props.initialVedlegg.includeSakspart });

  // `includeSakspart` is metadata the editor never touches, so it is kept out of the editor state
  // and folded back in when saving. That keeps the editor state a plain EditedDocument.
  const toVedlegg = (doc: EditedDocument): EditAttachment => ({
    ...doc,
    includeSakspart: vedleggMeta.includeSakspart,
  });

  const setVedleggInCache = (vedleggResponse: EditAttachment) =>
    queryClient.setQueryData(
      redigerbareVedleggKeys.vedlegg(brev.info.id, vedleggId, redigeringsflate),
      vedleggResponse,
    );

  // The side panel list only carries the title, and fetching it makes the backend re-render every
  // stored attachment — far too heavy to repeat on each autosave. Patch the cached title instead.
  const setTitleInCache = (vedleggResponse: EditAttachment) =>
    queryClient.setQueryData<RedigerbartVedleggInfo[]>(
      redigerbareVedleggKeys.liste(brev.info.id, redigeringsflate),
      (list) =>
        list?.map((v) => (v.vedleggId === vedleggId ? { ...v, tittel: formatVedleggTitle(vedleggResponse) } : v)),
    );

  const pdfQuery = redigeringsflate === "attestant-redigering" ? hentPdfForAttestering : hentPdfForBrev;

  const { saveFailed, saveNow, withSavingPaused } = useDocumentAutosave<EditedDocument, EditAttachment>({
    document: editorState.redigertBrev,
    saveStatus: editorState.saveStatus,
    mutationFn: (doc) => lagreRedigerbartVedlegg(saksId, brev.info.id, vedleggId, toVedlegg(doc), redigeringsflate),
    onSaveStart: () => setEditorState((s) => ({ ...s, saveStatus: "SAVE_PENDING" })),
    onSaveSuccess: (vedleggResponse) => {
      const savedDocument: EditedDocument = {
        title: vedleggResponse.title,
        blocks: vedleggResponse.blocks,
        deletedBlocks: vedleggResponse.deletedBlocks,
      };
      // Keep it DIRTY when the user typed again while the save was in flight: those edits are not
      // covered by this response, so the autosave must fire again instead of reporting SAVED.
      setEditorState((s) => {
        if (s.saveStatus === "DIRTY") return s;
        if (isEqual(normalizeDocumentForComparison(s.redigertBrev), normalizeDocumentForComparison(savedDocument))) {
          return { ...s, saveStatus: "SAVED" };
        }
        return {
          ...s,
          redigertBrev: savedDocument,
          saveStatus: "SAVED",
          history: { entries: [], entryPointer: -1 },
        };
      });
      setVedleggInCache(vedleggResponse);
      setTitleInCache(vedleggResponse);
      setVedleggMeta({ includeSakspart: vedleggResponse.includeSakspart });
      queryClient.resetQueries({ queryKey: pdfQuery.queryKey(brev.info.id) });
    },
    onSaveError: () => setEditorState((s) => ({ ...s, saveStatus: "DIRTY" })),
  });

  useEffect(() => {
    registerVedleggSave(saveNow);
    return () => registerVedleggSave(null);
  }, [registerVedleggSave, saveNow]);

  const openResetModal = useCallback(() => setResetModalOpen(true), []);

  useEffect(() => {
    registerReset(openResetModal);
    return () => registerReset(null);
  }, [openResetModal, registerReset]);

  const resetVedlegg = () =>
    withSavingPaused(async () => {
      const freshVedlegg = await tilbakestillRedigerbartVedlegg(saksId, brev.info.id, vedleggId);
      setVedleggInCache(freshVedlegg);
      setTitleInCache(freshVedlegg);
      setVedleggMeta({ includeSakspart: freshVedlegg.includeSakspart });
      queryClient.resetQueries({ queryKey: hentPdfForBrev.queryKey(brev.info.id) });
      return freshVedlegg;
    });

  return (
    <>
      <LetterEditor
        editorState={editorState}
        error={saveFailed}
        freeze={props.freeze}
        setEditorState={setEditorState}
        showDebug={false}
      />
      {resetModalOpen && (
        <TilbakestillVedleggModal
          onClose={() => setResetModalOpen(false)}
          open
          reset={resetVedlegg}
          resetEditor={(freshVedlegg) => setEditorState(createVedleggState(brev, freshVedlegg))}
          vedleggTitle={props.vedleggTitle}
        />
      )}
    </>
  );
};
