/**
 * Kast-bar test-UI for redigerbare vedlegg.
 *
 * Gjenbruker <LetterEditor /> for å redigere ett vedlegg (Edit.Attachment) slik at vi kan
 * teste back-end (skribenten + brevbaker). Hele frontend skrives på nytt av en annen
 * utvikler senere – derfor er dette bevisst minimalt.
 */

import { TrashIcon } from "@navikt/aksel-icons";
import { Alert, Button, Heading, HStack, Loader, Modal, VStack } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { type Dispatch, type SetStateAction, useEffect, useState } from "react";

import {
  getBrev,
  getRedigertVedlegg,
  lagreRedigertVedlegg,
  redigertVedleggKeys,
  slettRedigertVedlegg,
} from "~/api/brev-queries";
import { hentPdfForBrev } from "~/api/sak-api-endpoints";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { type BrevInfo } from "~/types/brev";
import { type EditAttachment, type EditedLetter, type Sakspart, type Signatur } from "~/types/brevbakerTypes";
import { getErrorMessage } from "~/utils/errorUtils";

const PLACEHOLDER_SAKSPART: Sakspart = {
  gjelderNavn: "",
  gjelderFoedselsnummer: "",
  saksnummer: "",
  dokumentDato: "",
};

const PLACEHOLDER_SIGNATUR: Signatur = {
  hilsenTekst: "",
  navAvsenderEnhet: "",
};

const attachmentToEditedLetter = (
  attachment: EditAttachment,
  sakspart: Sakspart,
  signatur: Signatur,
): EditedLetter => ({
  title: attachment.title,
  sakspart,
  blocks: attachment.blocks,
  signatur,
  deletedBlocks: attachment.deletedBlocks,
});

const editedLetterToAttachment = (letter: EditedLetter): EditAttachment => ({
  title: letter.title,
  blocks: letter.blocks,
  deletedBlocks: letter.deletedBlocks,
  includeSakspart: false,
});

const buildEditorState = (info: BrevInfo, redigertBrev: EditedLetter): LetterEditorState => ({
  info,
  redigertBrev,
  redigertBrevHash: "",
  saksbehandlerValg: {},
  saveStatus: "SAVED",
  focus: { blockIndex: 0, contentIndex: 0 },
  history: { entries: [], entryPointer: -1 },
});

type Props = {
  saksId: string;
  brev: BrevInfo;
  vedleggId: string;
  open: boolean;
  onClose: () => void;
};

export const RedigertVedleggEditModal = ({ saksId, brev, vedleggId, open, onClose }: Props) => {
  const queryClient = useQueryClient();
  const [editorState, setEditorState] = useState<LetterEditorState | null>(null);

  // reserver=true: skaff reservasjon for innlogget bruker slik at PUT/DELETE (som krever
  // reservasjon) går igjennom, på samme måte som å åpne den vanlige brevredigereren.
  const { data: brevData } = useQuery({
    queryKey: getBrev.queryKey(brev.id),
    queryFn: () => getBrev.queryFn(saksId, brev.id, open),
    enabled: open,
  });

  const {
    data: override,
    isPending: overridePending,
    isError,
    error,
  } = useQuery({
    queryKey: redigertVedleggKeys.id(brev.id, vedleggId),
    queryFn: () => getRedigertVedlegg.queryFn(saksId, brev.id, vedleggId),
    enabled: open,
  });

  // Nullstill editoren når modalen lukkes slik at den re-initialiseres ved neste åpning.
  useEffect(() => {
    if (!open) {
      setEditorState(null);
    }
  }, [open]);

  // Initialiser editoren når innholdet er ferdig lastet. Innholdet kommer alltid fra
  // back-end: enten en lagret overstyring, eller – ved første åpning / etter sletting –
  // vedlegget slik det produseres på nytt av brevbakeren. Ingen klient-seeding.
  // NB: en disabled react-query rapporterer isLoading=false, så vi venter på at spørringen
  // faktisk har resolvet (isPending=false) før vi initialiserer.
  useEffect(() => {
    if (!open || overridePending || isError || editorState || !override) {
      return;
    }
    const sakspart = brevData?.redigertBrev.sakspart ?? PLACEHOLDER_SAKSPART;
    const signatur = brevData?.redigertBrev.signatur ?? PLACEHOLDER_SIGNATUR;
    setEditorState(buildEditorState(brev, attachmentToEditedLetter(override, sakspart, signatur)));
  }, [open, overridePending, isError, editorState, override, brevData, brev]);

  const invalidateBrev = () => {
    queryClient.invalidateQueries({ queryKey: getBrev.queryKey(brev.id) });
    queryClient.invalidateQueries({ queryKey: hentPdfForBrev.queryKey(brev.id) });
    queryClient.invalidateQueries({ queryKey: redigertVedleggKeys.id(brev.id, vedleggId) });
  };

  const lagreMutation = useMutation({
    mutationFn: (state: LetterEditorState) =>
      lagreRedigertVedlegg(saksId, brev.id, vedleggId, editedLetterToAttachment(state.redigertBrev)),
    onSuccess: () => {
      invalidateBrev();
      onClose();
    },
  });

  const slettMutation = useMutation({
    mutationFn: () => slettRedigertVedlegg(saksId, brev.id, vedleggId),
    onSuccess: () => {
      invalidateBrev();
      onClose();
    },
  });

  const isBusy = lagreMutation.isPending || slettMutation.isPending;
  const freeze = isBusy || !editorState;

  return (
    <VStack asChild height="85vh" maxHeight="85vh" maxWidth="85vw" width="85vw">
      <Modal aria-label="Rediger testvedlegg" onClose={onClose} open={open} size="medium">
        <Modal.Header>
          <Heading size="small">Overstyring av redigerbart testvedlegg</Heading>
        </Modal.Header>

        <VStack asChild flexGrow="1" overflow="hidden">
          <Modal.Body>
            {isError ? (
              <Alert size="small" variant="error">
                {getErrorMessage(error)}
              </Alert>
            ) : !overridePending && !override ? (
              <Alert size="small" variant="error">
                Fant ikke et redigerbart vedlegg «{vedleggId}» for dette brevet.
              </Alert>
            ) : overridePending || !editorState ? (
              <VStack align="center" flexGrow="1" justify="center">
                <Loader size="3xlarge" title="Laster vedlegg..." />
              </VStack>
            ) : (
              <VStack gap="space-8">
                {(lagreMutation.error ?? slettMutation.error) && (
                  <Alert size="small" variant="error">
                    {getErrorMessage((lagreMutation.error ?? slettMutation.error) as Error)}
                  </Alert>
                )}
                <LetterEditor
                  editorState={editorState}
                  error={false}
                  freeze={freeze}
                  setEditorState={setEditorState as Dispatch<SetStateAction<LetterEditorState>>}
                  showDebug={false}
                />
              </VStack>
            )}
          </Modal.Body>
        </VStack>

        <HStack asChild justify="space-between">
          <Modal.Footer>
            <Button
              disabled={isBusy}
              icon={<TrashIcon aria-hidden />}
              loading={slettMutation.isPending}
              onClick={() => slettMutation.mutate()}
              type="button"
              variant="danger"
            >
              Slett overstyring
            </Button>
            <HStack gap="space-8">
              <Button disabled={isBusy} onClick={onClose} type="button" variant="tertiary">
                Avbryt
              </Button>
              <Button
                disabled={!editorState}
                loading={lagreMutation.isPending}
                onClick={() => editorState && lagreMutation.mutate(editorState)}
                type="button"
                variant="primary"
              >
                Lagre og lukk
              </Button>
            </HStack>
          </Modal.Footer>
        </HStack>
      </Modal>
    </VStack>
  );
};
