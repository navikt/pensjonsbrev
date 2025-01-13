import { ArrowLeftIcon, ArrowRightIcon } from "@navikt/aksel-icons";
import { Alert, BodyShort, Button, Heading, HStack, Label, Modal, VStack } from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { useState } from "react";

import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import type { BrevResponse } from "~/types/brev";

import { nyBrevResponse } from "../../../../../../cypress/utils/brevredigeringTestUtils";
import PDFViewer from "../../brevbehandler/-components/PDFViewer";
import { useSendVedtakContext } from "../kvittering/-SendVedtakContext";
import { ThreeSectionLayout } from "../redigering/route";

export const Route = createFileRoute("/saksnummer/$saksId/vedtak/$vedtakId/forhandsvisning")({
  component: () => <VedtaksForhåndsvisning />,
});

const SendBrevModal = (props: { saksId: string; vedtakId: string; åpen: boolean; onClose: () => void }) => {
  const queryClient = useQueryClient();
  const { setVedtakResponse } = useSendVedtakContext();
  const navigate = useNavigate({ from: Route.fullPath });
  const vedtak = queryClient.getQueryData<BrevResponse>(["vedtak", 1]) ?? nyBrevResponse({});

  const sendBrevMutation = useMutation({
    mutationFn: () =>
      Promise.resolve(
        setVedtakResponse({ status: "success", vedtak: vedtak, response: { journalpostId: 1, error: null } }),
      ),
    onSettled: () =>
      navigate({
        to: "/saksnummer/$saksId/vedtak/$vedtakId/kvittering",
        params: { saksId: props.saksId, vedtakId: props.vedtakId },
      }),
  });

  return (
    <Modal header={{ heading: "Vil du sende brevet?" }} onClose={props.onClose} open={props.åpen} portal width={450}>
      <Modal.Body>
        <BodyShort>Du kan ikke angre denne handlingen.</BodyShort>
      </Modal.Body>
      <Modal.Footer>
        <HStack gap="4">
          <Button onClick={props.onClose} type="button" variant="tertiary">
            Avbryt
          </Button>
          <Button loading={sendBrevMutation.isPending} onClick={() => sendBrevMutation.mutate()} type="button">
            Ja, send brevet
          </Button>
        </HStack>
      </Modal.Footer>
    </Modal>
  );
};

const VedtaksForhåndsvisning = () => {
  const queryClient = useQueryClient();
  const { saksId, vedtakId } = Route.useParams();
  const navigate = useNavigate({ from: Route.fullPath });
  const [vilSendeBrev, setVilSendeBrev] = useState(false);
  const vedtak = queryClient.getQueryData<BrevResponse>(["vedtak", 1]) ?? nyBrevResponse({});

  return (
    <>
      {vilSendeBrev && (
        <SendBrevModal onClose={() => setVilSendeBrev(false)} saksId={saksId} vedtakId={vedtakId} åpen={vilSendeBrev} />
      )}
      <ThreeSectionLayout
        bottom={
          <HStack gap="5">
            <Button
              icon={<ArrowLeftIcon />}
              onClick={() =>
                navigate({
                  to: "/saksnummer/$saksId/vedtak/$vedtakId/redigering",
                  params: { saksId, vedtakId: vedtakId },
                })
              }
              size="small"
              type="button"
              variant="secondary"
            >
              Tilbake til redigering
            </Button>
            <Button
              icon={<ArrowRightIcon />}
              iconPosition="right"
              onClick={() => setVilSendeBrev(true)}
              size="small"
              type="button"
            >
              Send brev
            </Button>
          </HStack>
        }
        left={
          <VStack gap="3">
            <Heading size="small">{vedtak.redigertBrev.title}</Heading>
            <VStack gap="4">
              <OppsummeringAvMottaker mottaker={vedtak.info.mottaker} saksId={saksId} withTitle />
              <VStack gap="1">
                <Label size="small">Distribusjonstype</Label>
                <BodyShort size="small">{vedtak.info.distribusjonstype}</BodyShort>
                <Alert size="small" variant="warning">
                  Du må åpne PDF og skrive ut brevet etter du har trykket på send brev.
                </Alert>
              </VStack>
            </VStack>
          </VStack>
        }
        right={
          <PDFViewer
            brevId={Number.parseInt(vedtakId)}
            pdf={new Blob([])}
            sakId={saksId}
            utenSlettKnapp
            viewerHeight={"var(--main-page-content-height)"}
          />
        }
      />
    </>
  );
};
