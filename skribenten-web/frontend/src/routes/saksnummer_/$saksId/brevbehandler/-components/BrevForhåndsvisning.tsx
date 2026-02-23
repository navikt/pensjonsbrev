import { css } from "@emotion/react";
import { Alert, BodyLong, BodyShort, Button, Heading, HStack, VStack } from "@navikt/ds-react";
import { useMutation, useQuery } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { useState } from "react";

import { getBrevInfo } from "~/api/brev-queries";
import { hentPdfForBrev, veksleKlarStatus } from "~/api/sak-api-endpoints";
import { CenteredLoader } from "~/components/CenteredLoader";
import { getErrorMessage, getErrorTitle } from "~/utils/errorUtils";
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
    refetchOnWindowFocus: false,
  });

  const brevInfo = useQuery(getBrevInfo(properties.brevId));

  const navigateToBrevRedigering = () =>
    navigate({
      to: "/saksnummer/$saksId/brev/$brevId",
      params: { saksId: properties.saksId, brevId: properties.brevId },
    });

  const erKlarEllerAttestering =
    brevInfo?.data?.status?.type === "Klar" || brevInfo?.data?.status?.type === "Attestering";

  const oppdaterBrevTilKladd = useMutation({
    mutationFn: () => veksleKlarStatus(properties.saksId, properties.brevId, { klar: false }),
    onSuccess: () => {
      setShowBrevDataEndringAlert(false);
      navigateToBrevRedigering();
    },
    onError: () => {
      setOppdaterError("Klarte ikke oppdatere brevet. Prøv igjen.");
    },
  });

  const handleOppdater = () => {
    if (brevInfo.isLoading) return;

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
    pending: () => <CenteredLoader label="Henter brev..." />,
    error: (error) => {
      const is422 = error?.response?.status === 422;
      const errorTitle = is422 ? getErrorTitle(error) : undefined;
      const errorMessage = is422 ? getErrorMessage(error) : undefined;

      return (
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
            <Heading size="xsmall">{errorTitle ?? "Klarte ikke åpne pdf"}</Heading>
            <BodyLong>{errorMessage ?? "Dette kan skje hvis du f.eks. har gjort endringer i saken i pesys."}</BodyLong>
          </Alert>
        </>
      );
    },
    success: (pdfResponse) =>
      pdfResponse === null ? (
        <VStack align="center" justify="center">
          Fant ikke PDF for brev med id {properties.brevId}
        </VStack>
      ) : (
        <PDFViewer
          brevId={properties.brevId}
          pdf={hentPdfQuery.isRefetching ? undefined : pdfResponse.pdf}
          sakId={properties.saksId}
        >
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
