import { useQuery, useQueryClient } from "@tanstack/react-query";
import { useCallback, useEffect, useRef, useState } from "react";

import { redigerbareVedleggKeys } from "~/api/redigerbareVedlegg-endpoints";
import {
  getRedigerbartVedleggV2,
  lagreRedigerbartVedleggV2,
  redigerbareVedleggV2Keys,
  tilbakestillRedigerbartVedleggV2,
} from "~/api/redigerbareVedleggV2-endpoints";
import { hentPdfForAttestering, hentPdfForBrev } from "~/api/sak-api-endpoints";
import { plainText } from "~/Brevredigering/EditorV2/model/text";
import { createAttachmentState, type EditorStateV2 } from "~/Brevredigering/EditorV2/state/editorState";
import { emptyHistory } from "~/Brevredigering/EditorV2/state/history";
import { useEditorAutosave } from "~/Brevredigering/EditorV2/state/useEditorAutosave";
import { EditorV2 } from "~/Brevredigering/EditorV2/view/EditorV2";
import { type Redigeringsflate } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { ApiError } from "~/components/ApiError";
import { useActiveDocument } from "~/components/brevOgVedlegg/ActiveDocumentContext";
import { CenteredLoader } from "~/components/CenteredLoader";
import { countMissingFromTemplate, sameDocument } from "~/components/ManagedLetterEditorV2/documentQueries";
import TilbakestillVedleggModal from "~/components/vedlegg/TilbakestillVedleggModal";
import { type BrevResponse, type RedigerbartVedleggInfo } from "~/types/brev";
import { type AttachmentV2, type DocumentV2 } from "~/types/brevV2";

type AttachmentEditorV2Props = {
  saksId: string;
  brev: Pick<BrevResponse, "info">;
  vedleggId: string;
  vedleggTitle: string;
  freeze: boolean;
  redigeringsflate: Redigeringsflate;
};

type VedleggMeta = Pick<AttachmentV2, "inkluderSaksinformasjon" | "version">;

const metaOf = (vedlegg: AttachmentV2): VedleggMeta => ({
  inkluderSaksinformasjon: vedlegg.inkluderSaksinformasjon,
  version: vedlegg.version,
});

const documentOf = (vedlegg: AttachmentV2): DocumentV2 => ({
  title: vedlegg.title,
  blocks: vedlegg.blocks,
  deletedBlocks: vedlegg.deletedBlocks,
});

export const ManagedAttachmentEditorV2 = (props: AttachmentEditorV2Props) => {
  const { saksId, brev, vedleggId, redigeringsflate } = props;

  const vedleggQuery = useQuery({
    ...getRedigerbartVedleggV2(saksId, brev.info.id, vedleggId, redigeringsflate),
    staleTime: Number.POSITIVE_INFINITY,
    refetchOnMount: "always",
  });

  if (vedleggQuery.isPending || !vedleggQuery.isFetchedAfterMount) {
    return <CenteredLoader label="Henter vedlegg..." verticalStrategy="height" />;
  }
  if (vedleggQuery.isError) {
    return <ApiError error={vedleggQuery.error} title="Klarte ikke å hente vedlegget" />;
  }

  return <AttachmentEditorSessionV2 {...props} initialVedlegg={vedleggQuery.data} key={vedleggId} />;
};

const AttachmentEditorSessionV2 = (props: AttachmentEditorV2Props & { initialVedlegg: AttachmentV2 }) => {
  const { saksId, brev, vedleggId, redigeringsflate } = props;
  const queryClient = useQueryClient();
  const { registerVedleggSave, registerReset, registerVedleggMissingFromTemplate } = useActiveDocument();
  const [resetModalOpen, setResetModalOpen] = useState(false);
  const vedleggMeta = useRef(metaOf(props.initialVedlegg));

  const setVedleggInCache = (vedlegg: AttachmentV2) => {
    vedleggMeta.current = metaOf(vedlegg);
    queryClient.setQueryData(redigerbareVedleggV2Keys.vedlegg(brev.info.id, vedleggId, redigeringsflate), vedlegg);
    queryClient.setQueryData<RedigerbartVedleggInfo[]>(
      redigerbareVedleggKeys.liste(brev.info.id, redigeringsflate),
      (list) => list?.map((v) => (v.vedleggId === vedleggId ? { ...v, tittel: plainText(vedlegg.title.text) } : v)),
    );
  };

  const pdfQuery = redigeringsflate === "attestant-redigering" ? hentPdfForAttestering : hentPdfForBrev;

  const { editorState, setEditorState, saveFailed, savePendingChanges, reset, resetting } = useEditorAutosave<
    EditorStateV2,
    AttachmentV2
  >({
    initialState: createAttachmentState(brev.info, props.initialVedlegg),
    save: (state) =>
      lagreRedigerbartVedleggV2(
        saksId,
        brev.info.id,
        vedleggId,
        { ...state.redigertBrev, ...vedleggMeta.current },
        redigeringsflate,
      ),
    applyLatestResponse: (state, vedlegg) => {
      const saved = documentOf(vedlegg);
      return sameDocument(state.redigertBrev, saved)
        ? state
        : { ...state, redigertBrev: saved, history: emptyHistory() };
    },
    onSaved: (vedlegg) => {
      setVedleggInCache(vedlegg);
      void queryClient.resetQueries({ queryKey: pdfQuery.queryKey(brev.info.id) });
    },
  });

  const missingFromTemplateCount = countMissingFromTemplate(editorState.redigertBrev);
  useEffect(() => {
    registerVedleggMissingFromTemplate(vedleggId, missingFromTemplateCount);
    return () => registerVedleggMissingFromTemplate(vedleggId, null);
  }, [registerVedleggMissingFromTemplate, vedleggId, missingFromTemplateCount]);

  useEffect(() => {
    registerVedleggSave(savePendingChanges);
    return () => registerVedleggSave(null);
  }, [registerVedleggSave, savePendingChanges]);

  const openResetModal = useCallback(() => setResetModalOpen(true), []);

  useEffect(() => {
    registerReset(openResetModal);
    return () => registerReset(null);
  }, [openResetModal, registerReset]);

  const resetVedlegg = () =>
    reset(async () => {
      const freshVedlegg = await tilbakestillRedigerbartVedleggV2(saksId, brev.info.id, vedleggId);
      setVedleggInCache(freshVedlegg);
      void queryClient.resetQueries({ queryKey: hentPdfForBrev.queryKey(brev.info.id) });
      return createAttachmentState(brev.info, freshVedlegg);
    });

  return (
    <>
      <EditorV2
        editorState={editorState}
        error={saveFailed}
        freeze={props.freeze || resetting}
        setEditorState={setEditorState}
        showDebug={false}
      />
      {resetModalOpen && (
        <TilbakestillVedleggModal
          onClose={() => setResetModalOpen(false)}
          open
          reset={resetVedlegg}
          vedleggTitle={props.vedleggTitle}
        />
      )}
    </>
  );
};
