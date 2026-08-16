import { useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";

import {
  getRedigerbartVedlegg,
  lagreRedigerbartVedlegg,
  redigerbareVedleggKeys,
} from "~/api/redigerbareVedlegg-endpoints";
import { hentPdfForBrev } from "~/api/sak-api-endpoints";
import { SakspartView } from "~/Brevredigering/LetterEditor/components/SakspartView";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { ApiError } from "~/components/ApiError";
import { CenteredLoader } from "~/components/CenteredLoader";
import { useVedleggAutosave } from "~/components/editorTabs/documents/useVedleggAutosave";
import { type BrevResponse, type EditAttachment } from "~/types/brev";

/**
 * Builds the editor session for a redigerbart vedlegg. It reuses the brev's LetterEditorState shape
 * (redigertBrev holds the EditAttachment; info comes from the parent brev for spraak; the hash and
 * saksbehandlerValg are unused stand-ins). This lets the existing LetterEditor and all its content
 * components work unchanged via useEditor().
 */
const createVedleggState = (brev: BrevResponse, vedlegg: EditAttachment): LetterEditorState => ({
  info: brev.info,
  redigertBrev: vedlegg,
  redigertBrevHash: "",
  saksbehandlerValg: {},
  saveStatus: "SAVED",
  focus: { blockIndex: 0, contentIndex: 0 },
  history: { entries: [], entryPointer: -1 },
});

/**
 * Editor for one redigerbart vedlegg. Fetches the vedlegg content on mount (the backend falls back
 * to the template attachment when no override exists) and autosaves edits through the vedlegg
 * persistence adapter. Sakspart is shown only when the vedlegg's includeSakspart flag is set; there
 * is no signatur. The brev reservation (owned by the page) covers vedlegg saves — no separate lock.
 */
export const ManagedVedleggEditor = (props: { saksId: string; brev: BrevResponse; vedleggId: string }) => {
  const { saksId, brev, vedleggId } = props;
  const queryClient = useQueryClient();

  const vedleggQuery = useQuery({
    queryKey: getRedigerbartVedlegg.queryKey(brev.info.id, vedleggId),
    queryFn: () => getRedigerbartVedlegg.queryFn(saksId, brev.info.id, vedleggId),
    staleTime: Number.POSITIVE_INFINITY,
  });

  if (vedleggQuery.isPending) {
    return <CenteredLoader label="Henter vedlegg..." />;
  }
  if (vedleggQuery.isError) {
    return <ApiError error={vedleggQuery.error} title="Klarte ikke å hente vedlegg" />;
  }

  return (
    <ManagedVedleggEditorReady
      brev={brev}
      key={vedleggId}
      queryClient={queryClient}
      saksId={saksId}
      vedlegg={vedleggQuery.data}
      vedleggId={vedleggId}
    />
  );
};

const ManagedVedleggEditorReady = (props: {
  saksId: string;
  brev: BrevResponse;
  vedleggId: string;
  vedlegg: EditAttachment;
  queryClient: ReturnType<typeof useQueryClient>;
}) => {
  const { saksId, brev, vedleggId, vedlegg, queryClient } = props;
  const [editorState, setEditorState] = useState<LetterEditorState>(() => createVedleggState(brev, vedlegg));

  // The vedlegg owns its save flow: the PUT body is just the edited attachment (no
  // saksbehandlerValg / document hash like the brev). The save response is the parent BrevResponse,
  // which does not contain the saved vedlegg, so we keep the editor's own state as the source of
  // truth and only use the response to invalidate the affected queries.
  const { isError } = useVedleggAutosave<EditAttachment, BrevResponse>({
    content: editorState.redigertBrev as EditAttachment,
    saveStatus: editorState.saveStatus,
    mutationFn: (redigertVedlegg) => lagreRedigerbartVedlegg(saksId, brev.info.id, vedleggId, redigertVedlegg),
    onSaveStart: () => setEditorState((s) => ({ ...s, saveStatus: "SAVE_PENDING" })),
    onSaveSuccess: (response) => {
      // If the user typed again while the save was in flight, the state is DIRTY and the newer
      // edits are not covered by this response. Keep it DIRTY so the autosave fires again instead of
      // incorrectly marking it SAVED (same guard the brev's onSaveSuccess uses).
      setEditorState((s) => (s.saveStatus === "DIRTY" ? s : { ...s, saveStatus: "SAVED" }));
      // Keep the content query in sync so a remount does not refetch stale content.
      queryClient.setQueryData(redigerbareVedleggKeys.vedlegg(brev.info.id, vedleggId), editorState.redigertBrev);
      // The edited title may have changed, and the vedlegg is part of the rendered letter PDF.
      queryClient.invalidateQueries({ queryKey: redigerbareVedleggKeys.liste(brev.info.id) });
      queryClient.resetQueries({ queryKey: hentPdfForBrev.queryKey(response.info.id) });
    },
    onSaveError: () => setEditorState((s) => ({ ...s, saveStatus: "DIRTY" })),
  });

  const vedleggDoc = editorState.redigertBrev as EditAttachment;

  return (
    <LetterEditor
      editorState={editorState}
      error={isError}
      freeze={false}
      renderSakspart={
        vedleggDoc.includeSakspart
          ? () => <SakspartView sakspart={brev.redigertBrev.sakspart} spraak={brev.info.spraak} />
          : () => null
      }
      renderSignatur={() => null}
      setEditorState={setEditorState}
      showDebug={false}
    />
  );
};
