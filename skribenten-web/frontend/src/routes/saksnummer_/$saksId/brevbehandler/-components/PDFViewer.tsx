import "react-pdf/dist/Page/TextLayer.css";
import "react-pdf/dist/Page/AnnotationLayer.css";

import { css } from "@emotion/react";
import { useCallback, useEffect, useRef, useState } from "react";
import { pdfjs } from "react-pdf";
import { Document, Page } from "react-pdf";

import PDFViewerTopBar from "./PDFViewerTopBar";

pdfjs.GlobalWorkerOptions.workerSrc = new URL("pdfjs-dist/build/pdf.worker.min.mjs", import.meta.url).toString();

const PDFViewer = (properties: { sakId: string; brevId: string; pdf: Blob; viewerHeight?: string }) => {
  const [scale, setScale] = useState<number>(1);
  const [totalNumberOfPages, setTotalNumberOfPages] = useState<number>(1);

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
        height: ${properties.viewerHeight ? `${properties.viewerHeight}` : "auto"};
        overflow: scroll;
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
          display: flex;
          justify-content: center;
        `}
        ref={pdfContainerReference}
      >
        <Document
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
