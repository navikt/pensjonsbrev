import "react-pdf/dist/Page/TextLayer.css";
import "react-pdf/dist/Page/AnnotationLayer.css";

import { Alert, BodyLong, Box, Heading, HStack, VStack } from "@navikt/ds-react";
import { CatchBoundary } from "@tanstack/react-router";
import { Suspense, useCallback, useEffect, useRef, useState } from "react";
import { Document, Page as PDFPage, pdfjs } from "react-pdf";

import { CenteredLoader } from "~/components/CenteredLoader";

import PDFViewerTopBar from "./PDFViewerTopBar";

pdfjs.GlobalWorkerOptions.workerSrc = new URL("pdfjs-dist/build/pdf.worker.min.mjs", import.meta.url).toString();

const PDFViewer = (properties: {
  sakId: string;
  brevId: number;
  pdf?: Blob;
  utenSlettKnapp?: boolean;
  children?: React.ReactNode;
}) => {
  const [scale, setScale] = useState<number>(1);
  // Antall sider er knyttet til fila det ble lest fra. Når pdf-en byttes (f.eks. ved å slå av førsteside) kan den nye
  // ha færre sider, og vi må ikke be om sider som ikke finnes før den nye er lastet - da kaster react-pdf feil.
  const [loadedDocument, setLoadedDocument] = useState<{ file: Blob; numPages: number } | null>(null);
  const totalNumberOfPages = loadedDocument?.numPages ?? 1;
  const numberOfPagesToRender = loadedDocument && loadedDocument.file === properties.pdf ? loadedDocument.numPages : 0;

  const [currentPageNumber, setCurrentPageNumber] = useState(1);
  const pdfContainerReference = useRef<HTMLDivElement>(null);

  const handleScroll = useCallback(() => {
    const pdfContainer = pdfContainerReference.current;
    if (!pdfContainer) return;

    //dette er antall pixler som er scrollet ned fra containerens topp punkt.
    //det vil si at om man er scrollet til toppen av containeren, vil denne være 0, og når man scroller nedover, vil denne øke.
    const scrollTop = pdfContainer.scrollTop;
    //dette er høyden til containeren, alstå innholdet som er under PDF-vieweren sin toppbar.
    const pdfContainerHeight = pdfContainer.clientHeight;
    //dette er midten av containeren, altså midten av PDF-vieweren sin viewport.
    const pdfContainerMiddle = scrollTop + pdfContainerHeight / 2;

    //her henter vi bare alle <Page> elementene som er i PDF'en - vi har definert en class .pdf-page på alle disse elementene
    const pages = [...pdfContainer.querySelectorAll<HTMLDivElement>(".pdf-page")];

    for (const [index, page] of pages.entries()) {
      const pageTop = page.offsetTop;
      const pageBottom = page.offsetTop + page.offsetHeight;

      //sjekker om den midtre delen av containeren er innenfor Pagen.
      if (pdfContainerMiddle >= pageTop && pdfContainerMiddle <= pageBottom) {
        const newVisiblePage = index + 1;
        if (currentPageNumber !== newVisiblePage) {
          setCurrentPageNumber(newVisiblePage);
        }
        break;
      }
    }
  }, [currentPageNumber]);

  /*
   * Når vi scroller i PDF'en, vil vi oppdatere hvilken side vi er på.
   * Vi legger på scroll events på containeren som holder PDF'en, som kaller handleScroll() funksjonen når man scroller gjennom.
   *
   * Vi cleaner eventlisteneren når komponenten blir unmounted.
   */
  useEffect(() => {
    const container = pdfContainerReference.current;
    if (!container) return;
    container.addEventListener("scroll", handleScroll);

    return () => {
      container.removeEventListener("scroll", handleScroll);
    };
  }, [totalNumberOfPages, handleScroll]);

  return (
    <Box asChild background="neutral-soft" height="100%" minHeight="0" ref={pdfContainerReference}>
      <VStack>
        <PDFViewerTopBar
          brevId={properties.brevId}
          sakId={properties.sakId}
          utenSlettKnapp={properties.utenSlettKnapp}
          viewerControls={{
            currentPageNumber,
            setCurrentPageNumber,
            totalNumberOfPages,
            scale,
            setScale,
          }}
        />
        {properties.children}
        <HStack flexGrow="1" justify="space-around" overflow="auto" padding="space-12">
          <CatchBoundary errorComponent={PDFRenderError} getResetKey={() => properties.pdf}>
            <Suspense fallback={<CenteredLoader label="Henter brev..." verticalStrategy="height" />}>
              <Document
                css={{ display: "flex", flexDirection: "column", "> div": { flexGrow: "1" } }}
                file={properties.pdf}
                noData={<CenteredLoader label="Henter brev..." verticalStrategy="height" />}
                onLoadSuccess={(pdf) => {
                  if (properties.pdf) setLoadedDocument({ file: properties.pdf, numPages: pdf.numPages });
                }}
              >
                {Array.from({ length: numberOfPagesToRender }, (_, index) => (
                  <Box
                    className={`pdf-page`}
                    id={`page_${index + 1}`}
                    key={`page_${index + 1}`}
                    marginBlock="space-0 space-16"
                  >
                    {/* Egen Suspense per side, slik at sider som lastes ikke skjuler sider som allerede vises */}
                    <Suspense fallback={null}>
                      <PDFPage pageNumber={index + 1} scale={scale} />
                    </Suspense>
                  </Box>
                ))}
              </Document>
            </Suspense>
          </CatchBoundary>
        </HStack>
      </VStack>
    </Box>
  );
};

const PDFRenderError = () => (
  <Alert variant="error">
    <Heading size="xsmall">Klarte ikke vise pdf</Heading>
    <BodyLong>Prøv å laste siden på nytt.</BodyLong>
  </Alert>
);

export default PDFViewer;
