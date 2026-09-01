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
import { ApiError } from "~/components/ApiError";
import { useAktivtDokument } from "~/components/brevOgVedlegg/AktivtDokumentContext";
import { CenteredLoader } from "~/components/CenteredLoader";
import TilbakestillVedleggModal from "~/components/vedlegg/TilbakestillVedleggModal";
import { type BrevResponse, type EditAttachment, type RedigerbartVedleggInfo } from "~/types/brev";
import { type EditedDocument } from "~/types/brevbakerTypes";
import { type Redigeringsflate } from "~/utils/editorTracking";

/** Mirrors how the backend renders the list title, so the cached title matches a refetched one. */
const formaterVedleggtittel = (vedlegg: EditAttachment): string =>
  vedlegg.title.text.map((innhold) => text(innhold) ?? "").join("");

/**
 * Editor session for one editable attachment. It reuses LetterEditorState so the existing
 * LetterEditor and every content action work unchanged: `redigertBrev` holds the vedlegg's editable
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

type VedleggEditorProps = {
  saksId: string;
  brev: BrevResponse;
  vedleggId: string;
  vedleggtittel: string;
  freeze: boolean;
  redigeringsflate: Redigeringsflate;
};

export const ManagedVedleggEditor = (props: VedleggEditorProps) => {
  const { saksId, brev, vedleggId, redigeringsflate } = props;

  const vedleggQuery = useQuery({
    queryKey: getRedigerbartVedlegg.queryKey(brev.info.id, vedleggId, redigeringsflate),
    queryFn: () => getRedigerbartVedlegg.queryFn(saksId, brev.info.id, vedleggId, redigeringsflate),
  });

  if (vedleggQuery.isPending) {
    return <CenteredLoader label="Henter vedlegg..." verticalStrategy="height" />;
  }
  if (vedleggQuery.isError) {
    return <ApiError error={vedleggQuery.error} title="Klarte ikke å hente vedlegget" />;
  }

  return <VedleggEditorSession {...props} key={vedleggId} vedlegg={vedleggQuery.data} />;
};

const VedleggEditorSession = (props: VedleggEditorProps & { vedlegg: EditAttachment }) => {
  const { saksId, brev, vedleggId, vedlegg, redigeringsflate } = props;
  const queryClient = useQueryClient();
  const { registrerVedleggslagring, registrerTilbakestilling } = useAktivtDokument();
  const [editorState, setEditorState] = useState<LetterEditorState>(() => createVedleggState(brev, vedlegg));
  const [vilTilbakestille, setVilTilbakestille] = useState(false);

  // `includeSakspart` is metadata the editor never touches, so it is kept out of the editor state
  // and folded back in when saving. That keeps the editor state a plain EditedDocument.
  const tilVedlegg = (dokument: EditedDocument): EditAttachment => ({
    ...dokument,
    includeSakspart: vedlegg.includeSakspart,
  });

  const settVedleggICache = (oppdatert: EditAttachment) =>
    queryClient.setQueryData(redigerbareVedleggKeys.vedlegg(brev.info.id, vedleggId, redigeringsflate), oppdatert);

  // The side panel list only carries the title, and fetching it makes the backend re-render every
  // stored vedlegg — far too heavy to repeat on each autosave. Patch the cached title instead.
  const settTittelICache = (oppdatert: EditAttachment) =>
    queryClient.setQueryData<RedigerbartVedleggInfo[]>(
      redigerbareVedleggKeys.liste(brev.info.id, redigeringsflate),
      (liste) =>
        liste?.map((v) => (v.vedleggId === vedleggId ? { ...v, tittel: formaterVedleggtittel(oppdatert) } : v)),
    );

  const pdfQuery = redigeringsflate === "attestant-redigering" ? hentPdfForAttestering : hentPdfForBrev;

  const { lagringFeilet, lagreNaa, medLagringPaaPause } = useDocumentAutosave<EditedDocument, EditAttachment>({
    content: editorState.redigertBrev,
    saveStatus: editorState.saveStatus,
    mutationFn: (dokument) =>
      lagreRedigerbartVedlegg(saksId, brev.info.id, vedleggId, tilVedlegg(dokument), redigeringsflate),
    onSaveStart: () => setEditorState((s) => ({ ...s, saveStatus: "SAVE_PENDING" })),
    onSaveSuccess: (lagretVedlegg) => {
      const lagretDokument: EditedDocument = {
        title: lagretVedlegg.title,
        blocks: lagretVedlegg.blocks,
        deletedBlocks: lagretVedlegg.deletedBlocks,
      };
      // Keep it DIRTY when the user typed again while the save was in flight: those edits are not
      // covered by this response, so the autosave must fire again instead of reporting SAVED.
      setEditorState((s) => {
        if (s.saveStatus === "DIRTY") return s;
        if (isEqual(normalizeDocumentForComparison(s.redigertBrev), normalizeDocumentForComparison(lagretDokument))) {
          return { ...s, saveStatus: "SAVED" };
        }
        return {
          ...s,
          redigertBrev: lagretDokument,
          saveStatus: "SAVED",
          history: { entries: [], entryPointer: -1 },
        };
      });
      settVedleggICache(lagretVedlegg);
      settTittelICache(lagretVedlegg);
      queryClient.resetQueries({ queryKey: pdfQuery.queryKey(brev.info.id) });
    },
    onSaveError: () => setEditorState((s) => ({ ...s, saveStatus: "DIRTY" })),
  });

  useEffect(() => {
    registrerVedleggslagring(lagreNaa);
    return () => registrerVedleggslagring(null);
  }, [registrerVedleggslagring, lagreNaa]);

  const aapneTilbakestilling = useCallback(() => setVilTilbakestille(true), []);

  useEffect(() => {
    registrerTilbakestilling(aapneTilbakestilling);
    return () => registrerTilbakestilling(null);
  }, [aapneTilbakestilling, registrerTilbakestilling]);

  const tilbakestill = () =>
    medLagringPaaPause(async () => {
      const tilbakestilt = await tilbakestillRedigerbartVedlegg(saksId, brev.info.id, vedleggId);
      settVedleggICache(tilbakestilt);
      settTittelICache(tilbakestilt);
      queryClient.resetQueries({ queryKey: hentPdfForBrev.queryKey(brev.info.id) });
      return tilbakestilt;
    });

  return (
    <>
      <LetterEditor
        editorState={editorState}
        error={lagringFeilet}
        freeze={props.freeze}
        redigeringsflate={props.redigeringsflate}
        setEditorState={setEditorState}
        showDebug={false}
      />
      {vilTilbakestille && (
        <TilbakestillVedleggModal
          onClose={() => setVilTilbakestille(false)}
          resetEditor={(tilbakestilt) => setEditorState(createVedleggState(brev, tilbakestilt))}
          tilbakestill={tilbakestill}
          vedleggtittel={props.vedleggtittel}
          åpen
        />
      )}
    </>
  );
};
