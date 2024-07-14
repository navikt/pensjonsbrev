import "react-pdf/dist/Page/TextLayer.css";
import "react-pdf/dist/Page/AnnotationLayer.css";

import { css } from "@emotion/react";
import { BodyShort, Button, HStack, Modal, TextField, VStack } from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { useRef, useState } from "react";
import { Outline, pdfjs } from "react-pdf";
import { Document, Page } from "react-pdf";
import { i } from "vitest/dist/reporters-P7C2ytIv";
import { number } from "zod";

import { hentAlleBrevForSak, lagPdfForBrev, slettBrev } from "~/api/sak-api-endpoints";
import type { BrevInfo } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";

import pdf from "./-multipage.pdf";

pdfjs.GlobalWorkerOptions.workerSrc = new URL("pdfjs-dist/build/pdf.worker.min.mjs", import.meta.url).toString();

export const Route = createFileRoute("/saksnummer/$saksId/brevbehandler/$brevId")({
  component: BrevForhåndsvisning,
});

function BrevForhåndsvisning() {
  const { saksId, brevId } = Route.useParams();
  const [scale, setScale] = useState<number>(1);
  const [numberPages, setNumberPages] = useState<Nullable<number>>(null);
  const [currentPage, setCurrentPage] = useState(1);

  const inputReference = useRef(null);

  function onDocumentLoadSuccess({ numPages }: { numPages: number }): void {
    setNumberPages(numPages);
  }

  //TODO - vil vi alltid kanskje lage PDF'en ved switchen i parent route, og bare hente den her?
  const lagPdf = useMutation({
    mutationFn: () => lagPdfForBrev(saksId, brevId),
    onSuccess: (pdfData) => {
      //console.log(pdfData);
      //window.open(URL.createObjectURL(new Blob([pdfData], { type: "application/pdf" })), "_blank");
    },
  });

  const scrollToPage = (pageNumber) => {
    const pageElement = document.querySelector(`#page_${pageNumber}`);
    if (pageElement) {
      pageElement.scrollIntoView({ behavior: "smooth" });
    }
  };

  const handlePageInputChange = (event) => {
    const newPageNumber = Number(event.target.value);
    if (!Number.isNaN(newPageNumber) && newPageNumber >= 1 && newPageNumber <= numberPages) {
      setCurrentPage(newPageNumber);
      scrollToPage(newPageNumber);
    }
  };

  const handleKeyDown = (event) => {
    if (event.key === "Enter") {
      handlePageInputChange(event);
    }
  };

  const goToNextPage = () => {
    if (currentPage < numberPages) {
      setCurrentPage(currentPage + 1);
      scrollToPage(currentPage + 1);
    }
  };

  const goToPreviousPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
      scrollToPage(currentPage - 1);
    }
  };

  return (
    <div
      css={css`
        max-height: 85vh;
      `}
    >
      <VStack>
        <HStack>
          <Button loading={lagPdf.isPending} onClick={() => lagPdf.mutate()} type="button">
            Lag pdf
          </Button>
        </HStack>
      </VStack>
      -------------------
      <div>
        <HStack>
          <Button
            onClick={() => {
              if (scale <= 2) {
                setScale(scale + 0.1);
              }
            }}
            size="small"
            type="button"
          >
            +
          </Button>
          <Button
            onClick={() => {
              if (scale >= 1) {
                setScale(scale - 0.1);
              }
            }}
            size="small"
            type="button"
          >
            -
          </Button>
          <SlettBrev brevId={brevId} sakId={saksId} />
          <HStack>
            <TextField
              css={css`
                width: 50px;
              `}
              hideLabel
              label="Side"
              onKeyDown={handleKeyDown}
              ref={inputReference}
            />
            / {numberPages}
          </HStack>
          <button disabled={currentPage === 1} onClick={goToPreviousPage}>
            Previous Page
          </button>
          <button disabled={currentPage === numberPages} onClick={goToNextPage}>
            Next Page
          </button>
        </HStack>
        {/* 
        <div
          css={css`
            overflow: auto;
            max-height: 75vh;
          `}
        >
          <Document
            css={css`
              background-color: var(--a-gray-300);
              padding: 1rem 3rem;
            `}
            file={pdf}
            onLoadSuccess={onDocumentLoadSuccess}
          >
            {Array.from(new Array(totalPages), (element, index) => (
              <Page
                css={css`
                  margin: 2rem 0;
                `}
                key={`page_${index + 1}`}
                pageNumber={index + 1}
                scale={scale}
              />
            ))}
          </Document>
        </div>
*/}
        <div
          css={css`
            overflow: auto;
            max-height: 75vh;
          `}
        >
          <Document
            css={css`
              background-color: var(--a-gray-300);
              padding: 1rem 3rem;
            `}
            file={pdf}
            onLoadSuccess={onDocumentLoadSuccess}
          >
            {Array.from(new Array(numberPages), (element, index) => (
              <div id={`page_${index + 1}`} key={`page_${index + 1}`}>
                <Page
                  css={css`
                    margin: 2rem 0;
                  `}
                  pageNumber={index + 1}
                />
              </div>
            ))}
          </Document>
        </div>
      </div>
      -------------------
    </div>
  );
}

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
        Slett brev
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
