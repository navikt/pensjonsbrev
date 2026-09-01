import { ArrowLeftIcon, ArrowRightIcon } from "@navikt/aksel-icons";
import { Alert, BodyShort, Box, Button, Heading, HStack, Label, Modal, Skeleton, VStack } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { type AxiosError } from "axios";
import { useState } from "react";

import { attesteringBrevKeys, getBrevAttestering } from "~/api/brev-queries";
import { hentPdfForAttestering, sendBrev } from "~/api/sak-api-endpoints";
import { SOFT_HYPHEN } from "~/Brevredigering/LetterEditor/model/utils";
import { ApiError } from "~/components/ApiError";
import { distribusjonstypeTilText } from "~/components/kvitterteBrev/KvitterteBrevUtils";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import ThreeSectionLayout from "~/components/ThreeSectionLayout";
import { type BestillBrevResponse, type BrevResponse } from "~/types/brev";
import { trackEvent } from "~/utils/umami";

import BrevForhåndsvisning from "../brevbehandler/-components/BrevForhåndsvisning";
import { useSendtBrev } from "../kvittering/-components/SendtBrevContext";

export const Route = createFileRoute("/saksnummer_/$saksId/attester/$brevId/forhandsvisning")({
  component: () => <VedtakForhåndsvisningWrapper />,
});

const VedtakForhåndsvisningWrapper = () => {
  const { saksId, brevId } = Route.useParams();
  const hentBrevQuery = useQuery(getBrevAttestering(saksId, Number(brevId)));

  // Vi rendrer layout og info til venstre med Aksel Skeleton og BrevForhåndsvisning til høyre med
  // egen spinner for pdf. Da unngår vi en fullside-loader etterfulgt av pdf-loader.
  if (hentBrevQuery.isError) {
    return <ApiError error={hentBrevQuery.error} title="En feil skjedde ved henting av vedtaksbrev" />;
  }

  return (
    <VedtaksForhåndsvisning brev={hentBrevQuery.data} isBrevFresh={hentBrevQuery.isFetchedAfterMount} saksId={saksId} />
  );
};

type PdfStatus = "ready" | "fetching" | "unavailable";

const VedtaksForhåndsvisning = (props: { saksId: string; brev?: BrevResponse; isBrevFresh: boolean }) => {
  const { brevId } = Route.useParams();
  const navigate = useNavigate({ from: Route.fullPath });
  const [vilSendeBrev, setVilSendeBrev] = useState(false);

  // Brevet hentes alltid på nytt ved mount, og vi venter på det ferske svaret før pdf-en hentes,
  // slik at pdf-en alltid samsvarer med gjeldende redigertBrevHash.
  //
  // Dette hviler på at spørringen bruker standard staleTime (0): dataen er stale med én gang, og
  // React Querys standard refetchOnMount henter derfor på nytt ved hver mount. staleTime løses per
  // observer, så `staleTime: Number.POSITIVE_INFINITY` i redigering.tsx - som deler queryKey med
  // denne - påvirker oss ikke her.
  //
  // ADVARSEL: setter noen en staleTime for brevspørringen (i getBrevAttestering,
  // queryClient.setQueryDefaults eller defaultOptions.queries på klienten i main.tsx), slutter
  // refetchOnMount å utløses. Da kan isBrevFresh bli true på cachet data, og vi risikerer å sende
  // en pdf som hører til en utdatert redigertBrevHash.
  const isBrevReady = props.brev !== undefined && props.isBrevFresh;
  const hentPdfQuery = useQuery({
    queryKey: hentPdfForAttestering.queryKey(Number(brevId), props.brev?.redigertBrevHash),
    queryFn: () => hentPdfForAttestering.queryFn(props.saksId, Number(brevId)),
    enabled: isBrevReady,
    refetchOnWindowFocus: false,
  });

  // !isBrevReady må sjekkes aller først. Uten hash blir nøkkelen ["hentPdfForAttestering", brevId], og
  // det kan allerede ligge en pdf i cachen der fra et tidligere besøk på denne siden.
  // `enabled: false` hindrer at vi henter, men ikke at hentPdfQuery.data leser den cachede verdien.
  // Deretter "fetching": ved en bakgrunns-refetch ligger forrige pdf fortsatt i data.
  // isSuccess trengs i tillegg til null-sjekken, fordi React Query beholder forrige data når en
  // refetch feiler (status blir "error") - da viser forhåndsvisningen feil, og vi skal ikke kunne
  // sende.
  // data === null betyr at brevet ikke har noen pdf (queryFn gir null ved 404).
  const pdfStatus: PdfStatus =
    !isBrevReady || hentPdfQuery.isFetching
      ? "fetching"
      : hentPdfQuery.isSuccess && hentPdfQuery.data !== null
        ? "ready"
        : "unavailable";

  return (
    <VStack height="100%">
      {vilSendeBrev && props.brev && (
        <SendBrevModal
          brevId={props.brev.info.id.toString()}
          onClose={() => setVilSendeBrev(false)}
          pdfStatus={pdfStatus}
          saksId={props.saksId}
          åpen={vilSendeBrev}
        />
      )}
      <ThreeSectionLayout
        bottom={
          <HStack gap="space-20">
            <Button
              disabled={props.brev === undefined}
              icon={<ArrowLeftIcon />}
              onClick={() => {
                if (!props.brev) return;
                navigate({
                  to: "/saksnummer/$saksId/attester/$brevId/redigering",
                  params: {
                    saksId: props.saksId,
                    brevId: props.brev.info.id.toString(),
                  },
                  search: {
                    vedtaksId: props.brev.info.vedtaksId?.toString() ?? undefined,
                    enhetsId: props.brev.info.avsenderEnhet.enhetNr.toString(),
                  },
                });
              }}
              size="small"
              type="button"
              variant="secondary"
            >
              Tilbake til redigering
            </Button>
            <Button
              disabled={pdfStatus !== "ready"}
              icon={<ArrowRightIcon />}
              iconPosition="right"
              // Kun spinner når pdf-en faktisk hentes. pdfStatus er "fetching" allerede fra første
              // paint mens brevet lastes, og da ville knappen snurret gjennom hele innlastingen.
              loading={isBrevReady && hentPdfQuery.isFetching}
              onClick={() => setVilSendeBrev(true)}
              size="small"
              type="button"
            >
              Send brev
            </Button>
          </HStack>
        }
        left={
          // Aksel-Skeleton setter aria-hidden internt, så venstre kolonne er usynlig for skjermlesere
          // mens den laster. aria-busy + status-regionen under gir den en stemme. Regionen må rendres
          // i begge tilstander, ellers finnes den ikke i DOM-en når teksten endres og annonseres ikke.
          <Box aria-busy={!isBrevReady}>
            <span className="aksel-sr-only" role="status">
              {isBrevReady ? "Brevinformasjon lastet" : "Henter brevinformasjon…"}
            </span>
            {!isBrevReady || props.brev === undefined ? (
              <VStack gap="space-12">
                <Skeleton height={28} variant="rectangle" width="80%" />
                <VStack gap="space-16">
                  <Skeleton height={96} variant="rectangle" width="100%" />
                  <VStack gap="space-4">
                    <Skeleton height={20} variant="rectangle" width="50%" />
                    <Skeleton height={20} variant="rectangle" width="70%" />
                  </VStack>
                </VStack>
              </VStack>
            ) : (
              <VStack gap="space-12">
                <Heading size="small">{props.brev.info.brevtittel}</Heading>
                <VStack gap="space-16">
                  <OppsummeringAvMottaker mottaker={props.brev.info.mottaker ?? null} saksId={props.saksId} withTitle />
                  <VStack gap="space-4">
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
            )}
          </Box>
        }
        right={
          <BrevForhåndsvisning
            brevId={Number(brevId)}
            pdfKilde="attestering"
            redigertBrevHash={props.brev?.redigertBrevHash}
            saksId={props.saksId}
            waitingForFreshBrev={!isBrevReady}
          />
        }
      />
    </VStack>
  );
};

const SendBrevModal = (props: {
  saksId: string;
  brevId: string;
  pdfStatus: PdfStatus;
  åpen: boolean;
  onClose: () => void;
}) => {
  const { setBrevResult } = useSendtBrev();
  const navigate = useNavigate({ from: Route.fullPath });

  const queryClient = useQueryClient();

  const cachedBrevData = queryClient.getQueryData<BrevResponse>(attesteringBrevKeys.id(Number(props.brevId)));

  const sendBrevMutation = useMutation<BestillBrevResponse, AxiosError>({
    mutationFn: () => {
      return sendBrev(props.saksId, props.brevId);
    },
    onSuccess: (response) => {
      trackEvent("brev sendt", {
        brevId: Number(props.brevId),
        brevkode: cachedBrevData?.info.brevkode,
        type: "attestering",
        enhetsId: cachedBrevData?.info.avsenderEnhet.enhetNr,
      });
      setBrevResult(props.brevId, {
        status: "success",
        brevInfo: cachedBrevData!.info,
        response,
      });
      props.onClose();
    },
    onError: (error) => {
      setBrevResult(props.brevId, {
        status: "error",
        brevInfo: cachedBrevData!.info,
        error,
      });
      props.onClose();
    },
    onSettled: () => {
      navigate({
        to: "/saksnummer/$saksId/attester/$brevId/kvittering",
        params: { saksId: props.saksId, brevId: props.brevId },
        search: {
          vedtaksId: cachedBrevData!.info.vedtaksId?.toString() ?? undefined,
          enhetsId: cachedBrevData!.info.avsenderEnhet.enhetNr.toString(),
        },
      });
    },
  });
  if (!cachedBrevData) {
    return (
      <Modal header={{ heading: "Vil du sende brevet?" }} onClose={props.onClose} open={props.åpen} portal width={450}>
        <Modal.Body>
          <BodyShort>Klarte ikke å hente brev{SOFT_HYPHEN}informasjon - prøv på nytt senere.</BodyShort>
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
        <HStack gap="space-16">
          <Button onClick={props.onClose} type="button" variant="tertiary">
            Avbryt
          </Button>
          <Button
            disabled={props.pdfStatus !== "ready"}
            loading={sendBrevMutation.isPending || props.pdfStatus === "fetching"}
            onClick={() => sendBrevMutation.mutate()}
            type="button"
          >
            Ja, send brevet
          </Button>
        </HStack>
      </Modal.Footer>
    </Modal>
  );
};
