import { useQuery, useQueryClient } from "@tanstack/react-query";
import { useEffect, useState } from "react";

import {
  getRedigerbartVedlegg,
  lagreRedigerbartVedlegg,
  redigerbareVedleggKeys,
  tilbakestillRedigerbartVedlegg,
} from "~/api/redigerbareVedlegg-endpoints";
import { hentPdfForBrev } from "~/api/sak-api-endpoints";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { ApiError } from "~/components/ApiError";
import { CenteredLoader } from "~/components/CenteredLoader";
import { useAktivtDokument } from "~/components/vedlegg/AktivtDokumentContext";
import TilbakestillVedleggModal from "~/components/vedlegg/TilbakestillVedleggModal";
import { useDocumentAutosave } from "~/components/vedlegg/useDocumentAutosave";
import { type BrevResponse, type EditAttachment } from "~/types/brev";
import { type EditedDocument } from "~/types/brevbakerTypes";

/**
 * Editor session for one redigerbart vedlegg. It reuses LetterEditorState so the existing
 * LetterEditor and every content action work unchanged: `redigertBrev` holds the vedlegg's editable
 * body, `info` comes from the parent brev (for spraak), and hash/saksbehandlerValg are unused here
 * because a vedlegg is saved through its own endpoint.
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

type VedleggEditorProps = {
  saksId: string;
  brev: BrevResponse;
  vedleggId: string;
  vedleggtittel: string;
  freeze: boolean;
};

export const ManagedVedleggEditor = (props: VedleggEditorProps) => {
  const { saksId, brev, vedleggId } = props;

  const vedleggQuery = useQuery({
    queryKey: getRedigerbartVedlegg.queryKey(brev.info.id, vedleggId),
    queryFn: () => getRedigerbartVedlegg.queryFn(saksId, brev.info.id, vedleggId),
  });

  if (vedleggQuery.isPending) {
    return <CenteredLoader label="Henter vedlegg..." />;
  }
  if (vedleggQuery.isError) {
    return <ApiError error={vedleggQuery.error} title="Klarte ikke å hente vedlegget" />;
  }

  return <VedleggEditorSession {...props} key={vedleggId} vedlegg={vedleggQuery.data} />;
};

const VedleggEditorSession = (props: VedleggEditorProps & { vedlegg: EditAttachment }) => {
  const { saksId, brev, vedleggId, vedlegg } = props;
  const queryClient = useQueryClient();
  const { registrerLagring } = useAktivtDokument();
  const [editorState, setEditorState] = useState<LetterEditorState>(() => createVedleggState(brev, vedlegg));

  // `includeSakspart` is metadata the editor never touches, so it is kept out of the editor state
  // and folded back in when saving. That keeps the editor state a plain EditedDocument.
  const tilVedlegg = (dokument: EditedDocument): EditAttachment => ({
    ...dokument,
    includeSakspart: vedlegg.includeSakspart,
  });

  const settVedleggICache = (oppdatert: EditAttachment) =>
    queryClient.setQueryData(redigerbareVedleggKeys.vedlegg(brev.info.id, vedleggId), oppdatert);

  const { lagringFeilet, lagreNaa, medLagringPaaPause } = useDocumentAutosave<EditedDocument, EditAttachment>({
    content: editorState.redigertBrev,
    saveStatus: editorState.saveStatus,
    mutationFn: (dokument) => lagreRedigerbartVedlegg(saksId, brev.info.id, vedleggId, tilVedlegg(dokument)),
    onSaveStart: () => setEditorState((s) => ({ ...s, saveStatus: "SAVE_PENDING" })),
    onSaveSuccess: (lagretVedlegg) => {
      // Keep it DIRTY when the user typed again while the save was in flight: those edits are not
      // covered by this response, so the autosave must fire again instead of reporting SAVED.
      setEditorState((s) => (s.saveStatus === "DIRTY" ? s : { ...s, saveStatus: "SAVED" }));
      settVedleggICache(lagretVedlegg);
      // The edited title may have changed, and the vedlegg is part of the rendered letter PDF.
      queryClient.invalidateQueries({ queryKey: redigerbareVedleggKeys.liste(brev.info.id) });
      queryClient.resetQueries({ queryKey: hentPdfForBrev.queryKey(brev.info.id) });
    },
    onSaveError: () => setEditorState((s) => ({ ...s, saveStatus: "DIRTY" })),
  });

  useEffect(() => {
    registrerLagring(lagreNaa);
    return () => registrerLagring(null);
  }, [registrerLagring, lagreNaa]);

  const tilbakestill = () =>
    medLagringPaaPause(async () => {
      await tilbakestillRedigerbartVedlegg(saksId, brev.info.id, vedleggId);
      const tilbakestilt = await getRedigerbartVedlegg.queryFn(saksId, brev.info.id, vedleggId);
      settVedleggICache(tilbakestilt);
      // The title reverts to the template's, which the side panel list also shows.
      queryClient.invalidateQueries({ queryKey: redigerbareVedleggKeys.liste(brev.info.id) });
      queryClient.resetQueries({ queryKey: hentPdfForBrev.queryKey(brev.info.id) });
      return tilbakestilt;
    });

  return (
    <LetterEditor
      editorState={editorState}
      error={lagringFeilet}
      freeze={props.freeze}
      redigeringsflate="saksbehandler-redigering"
      renderTilbakestillModal={({ åpen, onClose }) => (
        <TilbakestillVedleggModal
          onClose={onClose}
          resetEditor={(tilbakestilt) => setEditorState(createVedleggState(brev, tilbakestilt))}
          tilbakestill={tilbakestill}
          vedleggtittel={props.vedleggtittel}
          åpen={åpen}
        />
      )}
      setEditorState={setEditorState}
      showDebug={false}
    />
  );
};
