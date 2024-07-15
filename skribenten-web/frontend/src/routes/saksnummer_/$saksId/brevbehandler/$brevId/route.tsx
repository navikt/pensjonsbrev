import { useMutation } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { useEffect, useRef, useState } from "react";

import { hentPdfForBrevFunction, lagPdfForBrev } from "~/api/sak-api-endpoints";
import type { Nullable } from "~/types/Nullable";

import PDFViewer from "./-components/PDFViewer";
import pdf from "./-multipage.pdf";

export const Route = createFileRoute("/saksnummer/$saksId/brevbehandler/$brevId")({
  component: BrevForhåndsvisning,
});

function BrevForhåndsvisning() {
  const { saksId, brevId } = Route.useParams();
  const pageContainerReference = useRef<HTMLDivElement>(null);
  const [containerHeight, setContainerHeight] = useState<Nullable<number>>(null);

  //TODO - vil vi alltid kanskje lage PDF'en ved switchen i parent route, og bare hente den her?
  const lagPdf = useMutation({
    mutationFn: () => lagPdfForBrev(saksId, brevId),
    onSuccess: (pdfData) => {
      window.open(URL.createObjectURL(pdfData));
    },
  });

  const hentPdf = useMutation({
    mutationFn: () => hentPdfForBrevFunction(saksId, brevId),
  });

  useEffect(() => {
    setContainerHeight(pageContainerReference?.current?.getBoundingClientRect().height ?? null);
  }, [pageContainerReference]);

  return (
    <div ref={pageContainerReference}>
      {/*       <VStack>
        <HStack>
          <Button loading={lagPdf.isPending} onClick={() => lagPdf.mutate()} type="button">
            Lag pdf
          </Button>
          <Button loading={hentPdf.isPending} onClick={() => hentPdf.mutate()} type="button">
            Hent pdf
          </Button>
        </HStack>
      </VStack>
      -------------------*/}
      {hentPdf.isSuccess && (
        <PDFViewer brevId={brevId} maxHeightLimit={containerHeight ?? undefined} pdf={hentPdf.data} sakId={saksId} />
      )}
      {hentPdf.isIdle && (
        <PDFViewer brevId={brevId} maxHeightLimit={containerHeight ?? undefined} pdf={pdf} sakId={saksId} />
      )}
    </div>
  );
}
