import "react-pdf/dist/Page/TextLayer.css";
import "react-pdf/dist/Page/AnnotationLayer.css";

import { css } from "@emotion/react";
import { useCallback, useEffect, useRef, useState } from "react";
import { pdfjs } from "react-pdf";
import { Document, Page } from "react-pdf";

import { usePDFViewerContext } from "./PDFViewerContext";
import PDFViewerTopBar from "./PDFViewerTopBar";

pdfjs.GlobalWorkerOptions.workerSrc = new URL("pdfjs-dist/build/pdf.worker.min.mjs", import.meta.url).toString();

/**
 * Brukt med [PDFViewerHeightContextProvider](./PDFViewerContext.tsx) for å gi en spesifikk høyden til PDFVieweren.
 * I fleste tilfeller vil vi vise PDF'en inni en scrollable div, og ikke la hele pagen være scrollable.
 * For at vi skal få det til å fungere må man bruke overflow: auto på div'en som holder PDF'en.
 * Merk at for å få det til må det settes en høyde på div'en. Denne høyden bestemmer parenten, siden den vet best i hvilken context layouten skal se ut
 *
 * Dersom man unnlater å bruke provideren og setter høyden, vil vi anta at man vil ha hele PDF'en rendret uten scrolling.
 */
const PDFViewer = (properties: { sakId: string; brevId: string; pdf: Blob }) => {
  const [scale, setScale] = useState<number>(1);
  const [totalNumberOfPages, setTotalNumberOfPages] = useState<number>(1);
  const pdfHeightContext = usePDFViewerContext();
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
    <div
      css={css`
        background: var(--a-gray-300);
      `}
    >
      <PDFViewerTopBar
        brevId={properties.brevId}
        currentPageNumber={currentPageNumber}
        sakId={properties.sakId}
        scale={scale}
        setCurrentPageNumber={setCurrentPageNumber}
        setScale={setScale}
        totalNumberOfPages={totalNumberOfPages}
      />
      <div
        css={css`
          /* 48px er høyden på topbaren */
          height: ${pdfHeightContext.height ? `${pdfHeightContext.height - 48}px` : "100%"};
          overflow: auto;
          display: flex;
          justify-content: center;
        `}
        ref={pdfContainerReference}
      >
        <Document
          css={css`
            background-color: var(--a-gray-300);
            padding: 0 3rem 1rem;
          `}
          file={properties.pdf}
          loading="Henter brev..."
          onLoadSuccess={(pdf) => setTotalNumberOfPages(pdf.numPages)}
        >
          {Array.from({ length: totalNumberOfPages }, (_, index) => (
            <div className={`pdf-page`} id={`page_${index + 1}`} key={`page_${index + 1}`}>
              <Page
                css={css`
                  margin-bottom: 1rem;
                `}
                pageNumber={index + 1}
                scale={scale}
              />
            </div>
          ))}
        </Document>
      </div>
    </div>
  );
};

export default PDFViewer;
