import { css } from "@emotion/react";
import { Alert, BodyLong, BodyShort, Button, Heading, HStack, Loader, VStack } from "@navikt/ds-react";
import { useMutation, useQuery } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { useState } from "react";

import { getBrev } from "~/api/brev-queries";
import { delvisOppdaterBrev, hentPdfForBrev } from "~/api/sak-api-endpoints";
import { queryFold } from "~/utils/tanstackUtils";

import PDFViewer from "../../-components/PDFViewer";
import PDFViewerTopBar from "../../-components/PDFViewerTopBar";

const BrevForhåndsvisning = (properties: { saksId: string; brevId: number }) => {
  const [showBrevDataEndringAlert, setShowBrevDataEndringAlert] = useState(true);
  const [oppdaterError, setOppdaterError] = useState<string | null>(null);
  const navigate = useNavigate();

  const hentPdfQuery = useQuery({
    queryKey: hentPdfForBrev.queryKey(properties.brevId),
    queryFn: () => hentPdfForBrev.queryFn(properties.saksId, properties.brevId),
  });

  const brev = useQuery({
    queryKey: getBrev.queryKey(properties.brevId),
    queryFn: () => getBrev.queryFn(properties.saksId, properties.brevId),
  });

  const navigateToBrevRedigering = () =>
    navigate({
      to: "/saksnummer/$saksId/brev/$brevId",
      params: { saksId: properties.saksId, brevId: properties.brevId },
    });

  const erKlarEllerAttestering =
    brev.data?.info.status.type === "Klar" || brev.data?.info.status.type === "Attestering";

  const oppdaterBrevTilKladd = useMutation({
    mutationFn: () =>
      delvisOppdaterBrev(properties.saksId, properties.brevId, {
        laastForRedigering: false,
      }),
    onSuccess: () => {
      setShowBrevDataEndringAlert(false);
      navigateToBrevRedigering();
    },
    onError: () => {
      setOppdaterError("Klarte ikke oppdatere brevet. Prøv igjen.");
    },
  });

  const handleOppdater = () => {
    if (brev.isLoading) return;

    if (erKlarEllerAttestering) {
      oppdaterBrevTilKladd.mutate();
    } else {
      setShowBrevDataEndringAlert(false);
      navigateToBrevRedigering();
    }
  };

  const handleIgnorer = () => {
    setShowBrevDataEndringAlert(false);
  };

  return queryFold({
    query: hentPdfQuery,
    initial: () => <></>,
    pending: () => (
      <VStack align="center" gap="space-4" justify="center">
        <Loader size="3xlarge" title="henter brev..." />
        <Heading size="large">Henter brev....</Heading>
      </VStack>
    ),
    error: () => (
      <>
        <PDFViewerTopBar brevId={properties.brevId} sakId={properties.saksId} utenSlettKnapp={false} />
        <Alert
          css={css`
            border-left: none;
            border-right: none;
          `}
          fullWidth
          variant="error"
        >
          <Heading size="xsmall">Klarte ikke åpne pdf</Heading>
          <BodyLong>Dette kan skje hvis du f.eks. har gjort endringer i saken i pesys.</BodyLong>
        </Alert>
      </>
    ),
    success: (pdfResponse) =>
      pdfResponse === null ? (
        <VStack align="center" justify="center">
          Fant ikke PDF for brev med id {properties.brevId}
        </VStack>
      ) : (
        <PDFViewer brevId={properties.brevId} pdf={pdfResponse.pdf} sakId={properties.saksId}>
          {pdfResponse.rendretBrevErEndret && showBrevDataEndringAlert ? (
            <Alert fullWidth variant="warning">
              <Heading size="xsmall">Brevet må oppdateres</Heading>
              <BodyShort spacing>
                Endringer i Pesys påvirker innholdet i brevet. Oppdater brevet for å sikre korrekt innhold.
              </BodyShort>
              <HStack gap="space-12">
                <Button
                  loading={oppdaterBrevTilKladd.isPending}
                  onClick={handleOppdater}
                  size="small"
                  variant="primary"
                >
                  Oppdater
                </Button>
                <Button onClick={handleIgnorer} size="small" variant="tertiary">
                  Ignorer
                </Button>
              </HStack>
              {oppdaterError && <BodyLong>{oppdaterError}</BodyLong>}
            </Alert>
          ) : null}
        </PDFViewer>
      ),
  });
};

export default BrevForhåndsvisning;
