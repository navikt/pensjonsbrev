import { css } from "@emotion/react";
import { ArrowLeftIcon, ArrowRightIcon } from "@navikt/aksel-icons";
import { Alert, BodyShort, Box, Button, Heading, HStack, Label, Loader, Modal, VStack } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { useState } from "react";

import { attesteringBrevKeys, getBrevAttesteringQuery } from "~/api/brev-queries";
import { sendBrev } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";
import { distribusjonstypeTilText } from "~/components/kvitterteBrev/KvitterteBrevUtils";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import ThreeSectionLayout from "~/components/ThreeSectionLayout";
import type { BrevResponse } from "~/types/brev";
import { queryFold } from "~/utils/tanstackUtils";

import BrevForhåndsvisning from "../../brevbehandler/-components/BrevForhåndsvisning";
import { useSendtBrevResultatContext } from "../../kvittering/-components/SendtBrevResultatContext";

export const Route = createFileRoute("/saksnummer_/$saksId/attester/$brevId/forhandsvisning")({
  component: () => <VedtakForhåndsvisningWrapper />,
});

const VedtakForhåndsvisningWrapper = () => {
  const { saksId, brevId } = Route.useParams();
  const hentBrevQuery = useQuery(getBrevAttesteringQuery(saksId, Number(brevId)));

  return queryFold({
    query: hentBrevQuery,
    initial: () => null,
    pending: () => (
      <Box
        background="bg-default"
        css={css`
          display: flex;
          flex-direction: column;
          flex: 1;
          align-items: center;
          padding-top: var(--a-spacing-8);
        `}
      >
        <VStack align="center" gap="1">
          <Loader size="3xlarge" title="henter brev..." />
          <Heading size="large">Henter brev....</Heading>
        </VStack>
      </Box>
    ),
    error: (err) => (
      <Box
        background="bg-default"
        css={css`
          display: flex;
          flex-direction: column;
          flex: 1;
          align-items: center;
          padding-top: var(--a-spacing-8);
        `}
      >
        <ApiError error={err} title={"En feil skjedde ved henting av vedtaksbrev"} />
      </Box>
    ),
    success: (brev) => <VedtaksForhåndsvisning brev={brev} saksId={saksId} />,
  });
};

const VedtaksForhåndsvisning = (props: { saksId: string; brev: BrevResponse }) => {
  const navigate = useNavigate({ from: Route.fullPath });
  const [vilSendeBrev, setVilSendeBrev] = useState(false);

  return (
    <>
      {vilSendeBrev && (
        <SendBrevModal
          brevId={props.brev.info.id.toString()}
          onClose={() => setVilSendeBrev(false)}
          saksId={props.saksId}
          åpen={vilSendeBrev}
        />
      )}
      <ThreeSectionLayout
        bottom={
          <HStack gap="5">
            <Button
              icon={<ArrowLeftIcon />}
              onClick={() =>
                navigate({
                  to: "/saksnummer/$saksId/attester/$brevId/redigering",
                  params: { saksId: props.saksId, brevId: props.brev.info.id.toString() },
                  search: {
                    vedtaksId: props.brev.info.vedtaksId?.toString() ?? undefined,
                    enhetsId: props.brev.info.avsenderEnhet?.enhetNr.toString() ?? undefined,
                  },
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
            <Heading size="small">{props.brev.redigertBrev.title}</Heading>
            <VStack gap="4">
              <OppsummeringAvMottaker mottaker={props.brev.info.mottaker} saksId={props.saksId} withTitle />
              <VStack gap="1">
                <Label size="small">Distribusjonstype</Label>
                <BodyShort size="small">{distribusjonstypeTilText(props.brev.info.distribusjonstype)}</BodyShort>
                {props.brev.info.distribusjonstype === "LOKALPRINT" && (
                  <Alert size="small" variant="warning">
                    Du må åpne PDF og skrive ut brevet etter du har trykket på send brev.
                  </Alert>
                )}
              </VStack>
            </VStack>
          </VStack>
        }
        right={<BrevForhåndsvisning brevId={props.brev.info.id} saksId={props.saksId} />}
      />
    </>
  );
};

const SendBrevModal = (props: { saksId: string; brevId: string; åpen: boolean; onClose: () => void }) => {
  const { setResultat } = useSendtBrevResultatContext();
  const navigate = useNavigate({ from: Route.fullPath });

  const queryClient = useQueryClient();

  const cachedBrevData = queryClient.getQueryData<BrevResponse>(
    attesteringBrevKeys.id(Number(props.brevId), props.saksId),
  );

  const sendBrevMutation = useMutation({
    mutationFn: () => {
      return sendBrev(props.saksId, props.brevId);
    },
    onError: (error: AxiosError) => {
      setResultat([{ status: "error", brevInfo: cachedBrevData!.info, error: error }]);
      props.onClose();
    },
    onSuccess: (res) => {
      setResultat([{ status: "success", brevInfo: cachedBrevData!.info, response: res }]);
      props.onClose();
    },
    onSettled: () => {
      navigate({
        to: "/saksnummer/$saksId/attester/$brevId/kvittering",
        params: { saksId: props.saksId, brevId: props.brevId },
        search: {
          vedtaksId: cachedBrevData!.info.vedtaksId?.toString() ?? undefined,
          enhetsId: cachedBrevData!.info.avsenderEnhet?.enhetNr.toString() ?? undefined,
        },
      });
    },
  });
  if (!cachedBrevData) {
    return (
      <Modal header={{ heading: "Vil du sende brevet?" }} onClose={props.onClose} open={props.åpen} portal width={450}>
        <Modal.Body>
          <BodyShort>Klarte ikke å hente brev­informasjon – prøv på nytt senere.</BodyShort>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={props.onClose}>Lukk</Button>
        </Modal.Footer>
      </Modal>
    );
  }
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
