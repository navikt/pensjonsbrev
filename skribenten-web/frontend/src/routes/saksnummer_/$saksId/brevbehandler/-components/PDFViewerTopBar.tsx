import type { SerializedStyles } from "@emotion/react";
import { css } from "@emotion/react";
import { ChevronDownIcon, ChevronUpIcon, TrashIcon, ZoomMinusIcon, ZoomPlusIcon } from "@navikt/aksel-icons";
import { BodyShort, Button, HStack, Modal, TextField } from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import React, { useEffect, useState } from "react";

import { hentAlleBrevForSak, slettBrev } from "~/api/sak-api-endpoints";
import type { BrevInfo } from "~/types/brev";

import { Route } from "../route";

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

export default PDFViewerTopBar;

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
          width: 24px;
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
          width: 24px;
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

              /* sentrerer teksten i inputfeltet dersom feltet har et tegn, eller fler */
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
          if (properties.scale <= 1.5) {
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
      queryClient.setQueryData(hentAlleBrevForSak.queryKey(properties.sakId), (currentBrevInfo: BrevInfo[]) =>
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
