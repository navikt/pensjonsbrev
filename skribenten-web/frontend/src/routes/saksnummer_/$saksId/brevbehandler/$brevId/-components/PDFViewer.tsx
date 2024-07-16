import "react-pdf/dist/Page/TextLayer.css";
import "react-pdf/dist/Page/AnnotationLayer.css";

import type { SerializedStyles } from "@emotion/react";
import { css } from "@emotion/react";
import { ChevronDownIcon, ChevronUpIcon, TrashIcon, ZoomMinusIcon, ZoomPlusIcon } from "@navikt/aksel-icons";
import { BodyShort, Button, HStack, Modal, TextField } from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import React, { useCallback, useEffect, useRef, useState } from "react";
import { pdfjs } from "react-pdf";
import { Document, Page } from "react-pdf";

import { hentAlleBrevForSak, slettBrev } from "~/api/sak-api-endpoints";
import type { BrevInfo } from "~/types/brev";

import { Route } from "../route";
import { usePDFViewerContext } from "./PDFViewerContext";

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
    const container = pdfContainerReference.current;
    if (!container) return;

    //dette er antall pixler som er scrollet ned fra containerens topp punkt.
    //det vil si at om man er scrollet til til toppen av containeren, vil denne være 0.
    //og når man scroller nedover, vil denne øke.
    const scrollTop = container.scrollTop;
    //dette er høyden til containeren, alstå innholdet som er under PDF-vieweren sin toppbar.
    const containerHeight = container.clientHeight;
    //dette er midten av containeren, altså midten av PDF-vieweren sin viewport.
    const containerMiddle = scrollTop + containerHeight / 2;

    //her henter vi bare alle <Page> elementene som er i PDF'en - vi har definert en class .pdf-page på alle disse elementene
    const pages = [...container.querySelectorAll<HTMLDivElement>(".pdf-page")];

    for (const [index, page] of pages.entries()) {
      const pageTop = page.offsetTop;
      const pageBottom = page.offsetTop + page.offsetHeight;

      //sjekker om den midtre delen av containeren er innenfor Pagen.
      if (containerMiddle >= pageTop && containerMiddle <= pageBottom) {
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
          // 48px er høyden på topbaren
          height: ${pdfHeightContext.height ? `${pdfHeightContext.height - 48}px` : "100%"};
          overflow: auto;
        `}
        ref={pdfContainerReference}
      >
        <Document
          css={css`
            background-color: var(--a-gray-300);
            padding: 0 3rem 1rem 3rem;
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

const SlettBrev = (properties: { sakId: string; brevId: string }) => {
  const [vilSletteBrev, setVilSletteBrev] = useState(false);

  return (
    <div>
      {vilSletteBrev && (
        <SlettBrevModal
          brevId={properties.brevId}
          onClose={() => setVilSletteBrev(false)}
          sakId={properties.sakId}
          åpen={vilSletteBrev}
        />
      )}
      <Button onClick={() => setVilSletteBrev(true)} size="small" type="button" variant="danger">
        <HStack gap="1">
          <TrashIcon fontSize="1.5rem" title="slett-ikon" /> <BodyShort>Slett</BodyShort>
        </HStack>
      </Button>
    </div>
  );
};

const SlettBrevModal = (properties: { sakId: string; brevId: string; åpen: boolean; onClose: () => void }) => {
  const queryClient = useQueryClient();
  const navigate = useNavigate({ from: Route.fullPath });

  const slett = useMutation({
    mutationFn: () => slettBrev(properties.sakId, properties.brevId),
    onSuccess: () => {
      queryClient.setQueryData(hentAlleBrevForSak.queryKey, (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.filter((brev) => brev.id.toString() !== properties.brevId),
      );
      navigate({ to: "/saksnummer/$saksId/brevbehandler", params: { saksId: properties.sakId } });
    },
  });

  return (
    <Modal
      header={{ heading: "Vil du slette brevet?" }}
      onClose={properties.onClose}
      open={properties.åpen}
      portal
      width={450}
    >
      <Modal.Body>
        <BodyShort>Brevet vil bli slettet, og du kan ikke angre denne handlingen.</BodyShort>
      </Modal.Body>
      <Modal.Footer>
        <HStack gap="4">
          <Button onClick={properties.onClose} type="button" variant="tertiary">
            Nei, behold brevet
          </Button>
          <Button loading={slett.isPending} onClick={() => slett.mutate()} type="button" variant="danger">
            Ja, slett brevet
          </Button>
        </HStack>
      </Modal.Footer>
    </Modal>
  );
};

const PDFViewerTopBar = (properties: {
  sakId: string;
  brevId: string;
  totalNumberOfPages: number;
  scale: number;
  setScale: (n: number) => void;
  currentPageNumber: number;
  setCurrentPageNumber: (n: number) => void;
}) => {
  return (
    <HStack
      align="center"
      css={css`
        background-color: white;
        border-top: 1px solid var(--a-gray-200);
        border-bottom: 1px solid var(--a-gray-200);
        padding: 0 1rem;
        height: 48px;
      `}
      justify="space-between"
    >
      <HStack align="center" gap="2">
        <TopBarNavigation
          currentPageNumber={properties.currentPageNumber}
          setCurrentPageNumber={properties.setCurrentPageNumber}
          totalNumberOfPages={properties.totalNumberOfPages}
        />
        <TopBarZoom scale={properties.scale} setScale={properties.setScale} />
      </HStack>
      <SlettBrev brevId={properties.brevId} sakId={properties.sakId} />
    </HStack>
  );
};

const TopBarNavigation = (properties: {
  totalNumberOfPages: number;
  currentPageNumber: number;
  setCurrentPageNumber: (n: number) => void;
}) => {
  const [textFieldValue, setTextFieldValue] = useState("1");

  useEffect(() => {
    setTextFieldValue(properties.currentPageNumber.toString());
  }, [properties.currentPageNumber]);

  const scrollToPage = (pageNumber: number) => {
    const pageElement = document.querySelector(`#page_${pageNumber}`);
    if (pageElement) {
      pageElement.scrollIntoView();
      properties.setCurrentPageNumber(pageNumber);
    }
  };

  const handlePageInputChange = () => {
    const parsedValue = Number.parseInt(textFieldValue, 10);
    if (parsedValue >= 1 && parsedValue <= properties.totalNumberOfPages) {
      properties.setCurrentPageNumber(parsedValue);
      scrollToPage(parsedValue);
    } else {
      setTextFieldValue(properties.currentPageNumber.toString());
    }
  };

  const goToNextPage = () => {
    if (properties.currentPageNumber < properties.totalNumberOfPages) {
      properties.setCurrentPageNumber(properties.currentPageNumber + 1);
      scrollToPage(properties.currentPageNumber + 1);
    }
  };

  const goToPreviousPage = () => {
    if (properties.currentPageNumber > 1) {
      properties.setCurrentPageNumber(properties.currentPageNumber - 1);
      scrollToPage(properties.currentPageNumber - 1);
    }
  };

  return (
    <HStack
      align="end"
      css={css`
        border-right: 1px solid var(--a-gray-200);
        padding-right: 1rem;
      `}
      gap="2"
    >
      <BasicPDFViewerButton
        cssOveride={css`
          padding: 0;
          height: 24px;
          widht: 24px;
        `}
        disabled={properties.currentPageNumber === 1}
        onClick={goToPreviousPage}
      >
        <ChevronUpIcon fontSize="24px" title="forrige side" />
      </BasicPDFViewerButton>
      <BasicPDFViewerButton
        cssOveride={css`
          padding: 0;
          height: 24px;
          widht: 24px;
        `}
        disabled={properties.currentPageNumber === properties.totalNumberOfPages}
        onClick={goToNextPage}
      >
        <ChevronDownIcon fontSize="24px" title="neste side" />
      </BasicPDFViewerButton>
      <HStack gap="1">
        <TextField
          css={css`
            input {
              width: 28px;
              height: 32px;
              min-height: 32px;
              //sentrerer teksten i inputfeltet dersom feltet har et tegn, eller fler
              padding: 0 ${textFieldValue.length > 1 ? "4px" : "8px"};
            }
          `}
          hideLabel
          label="Side"
          onChange={(event) => {
            const value = event.target.value;
            const parsedValue = Number.parseInt(value, 10);
            if (!Number.isNaN(parsedValue) || value === "") {
              setTextFieldValue(value);
            }
          }}
          onKeyDown={(event) => {
            if (event.key === "Enter") {
              handlePageInputChange();
            }
          }}
          value={textFieldValue}
        />
        <BodyShort
          css={css`
            align-self: center;
            font-size: 20px;
          `}
        >
          / {properties.totalNumberOfPages}
        </BodyShort>
      </HStack>
    </HStack>
  );
};

const TopBarZoom = (properties: { scale: number; setScale: (n: number) => void }) => {
  return (
    <HStack gap="2">
      <BasicPDFViewerButton
        onClick={() => {
          if (properties.scale <= 2) {
            properties.setScale(properties.scale + 0.1);
          }
        }}
      >
        <ZoomPlusIcon fontSize="24px" title="zoom-in" />
      </BasicPDFViewerButton>

      <BasicPDFViewerButton
        onClick={() => {
          if (properties.scale >= 1) {
            properties.setScale(properties.scale - 0.1);
          }
        }}
      >
        <ZoomMinusIcon fontSize="24px" title="zoom-ut" />
      </BasicPDFViewerButton>
    </HStack>
  );
};

const BasicPDFViewerButton = (properties: {
  cssOveride?: SerializedStyles;
  disabled?: boolean;
  onClick: () => void;
  children: React.ReactNode;
}) => {
  return (
    <Button
      css={
        properties.cssOveride ??
        css`
          padding: 0;
          height: fit-content;
        `
      }
      disabled={properties.disabled}
      onClick={properties.onClick}
      type="button"
      variant="tertiary-neutral"
    >
      {properties.children}
    </Button>
  );
};
