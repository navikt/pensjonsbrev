import { useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";

import { getRedigerbartVedlegg, redigerbareVedleggKeys } from "~/api/redigerbareVedlegg-endpoints";
import { SakspartView } from "~/Brevredigering/LetterEditor/components/SakspartView";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { ApiError } from "~/components/ApiError";
import { CenteredLoader } from "~/components/CenteredLoader";
import { useManagedDocument } from "~/components/ManagedDocumentEditor/useManagedDocument";
import { createVedleggPersistence } from "~/components/ManagedDocumentEditor/vedleggPersistence";
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
export const VedleggEditor = (props: { saksId: string; brev: BrevResponse; vedleggId: string }) => {
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
    <VedleggEditorReady
      brev={brev}
      key={vedleggId}
      queryClient={queryClient}
      saksId={saksId}
      vedlegg={vedleggQuery.data}
      vedleggId={vedleggId}
    />
  );
};

const VedleggEditorReady = (props: {
  saksId: string;
  brev: BrevResponse;
  vedleggId: string;
  vedlegg: EditAttachment;
  queryClient: ReturnType<typeof useQueryClient>;
}) => {
  const { saksId, brev, vedleggId, vedlegg, queryClient } = props;
  const [editorState, setEditorState] = useState<LetterEditorState>(() => createVedleggState(brev, vedlegg));

  const persistence = createVedleggPersistence({ saksId, brevId: brev.info.id, vedleggId, queryClient });

  const { isError } = useManagedDocument<EditAttachment, BrevResponse>({
    content: editorState.redigertBrev as EditAttachment,
    saveStatus: editorState.saveStatus,
    persistence,
    onSaveStart: () => setEditorState((s) => ({ ...s, saveStatus: "SAVE_PENDING" })),
    onSaveSuccess: () => {
      setEditorState((s) => ({ ...s, saveStatus: "SAVED" }));
      // Keep the content query in sync so a remount does not refetch stale content.
      queryClient.setQueryData(redigerbareVedleggKeys.vedlegg(brev.info.id, vedleggId), editorState.redigertBrev);
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
