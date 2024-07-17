import { css } from "@emotion/react";
import { Label, Loader, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";

import { hentPdfForBrev, hentPdfForBrevFunction } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";

import PDFViewer from "./-components/PDFViewer";

export const Route = createFileRoute("/saksnummer/$saksId/brevbehandler/$brevId")({
  component: BrevForhåndsvisning,
});

function BrevForhåndsvisning() {
  const { saksId, brevId } = Route.useParams();

  const hentPdfQuery = useQuery({
    queryKey: hentPdfForBrev.queryKey(brevId),
    queryFn: () => hentPdfForBrevFunction(saksId, brevId),
  });

  return (
    <VStack
      css={css`
        height: 100%;
      `}
      justify={"center"}
    >
      {hentPdfQuery.isPending && (
        <VStack align="center" justify="center">
          <Loader size="3xlarge" title="Henter..." />
          <Label>Henter brev...</Label>
        </VStack>
      )}
      {hentPdfQuery.isError && <ApiError error={hentPdfQuery.error} title={"Kunne ikke hente PDF"} />}
      {hentPdfQuery.isSuccess && <PDFViewer brevId={brevId} pdf={hentPdfQuery.data} sakId={saksId} />}
    </VStack>
  );
}
