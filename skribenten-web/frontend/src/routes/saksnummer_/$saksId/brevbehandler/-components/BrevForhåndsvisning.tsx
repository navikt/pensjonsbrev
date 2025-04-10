import { Alert, Label, Loader, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";

import { hentPdfForBrev, hentPdfForBrevFunction } from "~/api/sak-api-endpoints";
import { queryFold } from "~/utils/tanstackUtils";

import PDFViewer from "../../-components/PDFViewer";

const BrevForhåndsvisning = (properties: { saksId: string; brevId: number }) => {
  const hentPdfQuery = useQuery({
    queryKey: hentPdfForBrev.queryKey(properties.brevId),
    queryFn: () => hentPdfForBrevFunction(properties.saksId, properties.brevId),
  });

  return queryFold({
    query: hentPdfQuery,
    initial: () => <></>,
    pending: () => (
      <VStack align="center" justify="center">
        <Loader size="3xlarge" title="Henter brev..." />
        <Label>Henter brev...</Label>
      </VStack>
    ),
    error: (error) => <Alert variant="error">{error.message}</Alert>,
    success: (pdf) =>
      pdf === null ? (
        <VStack align="center">Fant ikke PDF</VStack>
      ) : (
        <PDFViewer
          brevId={properties.brevId}
          pdf={pdf}
          sakId={properties.saksId}
          utenSlettKnapp
          viewerHeight={"var(--main-page-content-height)"}
        />
      ),
  });
};

export default BrevForhåndsvisning;
