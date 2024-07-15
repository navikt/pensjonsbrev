import { BodyShort } from "@navikt/ds-react";
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

  /*
  //TODO - vil vi alltid kanskje lage PDF'en ved switchen i parent route, og bare hente den her?
  const lagPdf = useMutation({
    mutationFn: () => lagPdfForBrev(saksId, brevId),
    onSuccess: (pdfData) => {
      window.open(URL.createObjectURL(pdfData));
    },
  });*/

  const hentPdfQuery = useQuery({
    queryKey: hentPdfForBrev.queryKey,
    queryFn: () => hentPdfForBrevFunction(saksId, brevId),
  });

  return (
    <div>
      {hentPdfQuery.isPending && <BodyShort size="small">Henter...</BodyShort>}
      {hentPdfQuery.isError && <ApiError error={hentPdfQuery.error} title={"Kunne ikke hente PDF"} />}
      {hentPdfQuery.isSuccess && <PDFViewer brevId={brevId} pdf={hentPdfQuery.data} sakId={saksId} />}
    </div>
  );
}
