import type { SerializedStyles } from "@emotion/react";
import { css } from "@emotion/react";
import { ChevronDownIcon, ChevronUpIcon, ZoomMinusIcon, ZoomPlusIcon } from "@navikt/aksel-icons";
import { BodyShort, Button, HStack, TextField } from "@navikt/ds-react";
import { useNavigate } from "@tanstack/react-router";
import React, { useEffect, useState } from "react";

import { VerticalDivider } from "~/components/Divider";
import { SlettBrev } from "~/components/SlettBrev";

import { Route } from "../route";

const PDFViewerTopBar = (properties: {
  sakId: string;
  brevId: number;
  totalNumberOfPages: number;
  scale: number;
  setScale: (n: number) => void;
  currentPageNumber: number;
  setCurrentPageNumber: (n: number) => void;
  utenSlettKnapp?: boolean;
}) => {
  const navigate = useNavigate({ from: Route.fullPath });
  const { enhetsId, vedtaksId } = Route.useSearch();
  return (
    <HStack
      align="center"
      css={css`
        background-color: white;
        border-bottom: 1px solid var(--a-gray-200);
        padding: var(--a-spacing-2) var(--a-spacing-4);
        height: 48px;
        position: sticky;
        top: 0;
        z-index: 3;
      `}
      justify="space-between"
    >
      <HStack align="center" gap="4">
        <TopBarNavigation
          currentPageNumber={properties.currentPageNumber}
          setCurrentPageNumber={properties.setCurrentPageNumber}
          totalNumberOfPages={properties.totalNumberOfPages}
        />
        <VerticalDivider />
        <TopBarZoom scale={properties.scale} setScale={properties.setScale} />
      </HStack>
      {!properties.utenSlettKnapp && (
        <SlettBrev
          brevId={properties.brevId}
          buttonText="Slett"
          onSlettSuccess={() =>
            navigate({
              to: "/saksnummer/$saksId/brevbehandler",
              params: { saksId: properties.sakId },
              search: { enhetsId, vedtaksId },
            })
          }
          sakId={properties.sakId}
        />
      )}
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
    <HStack align="center" css={css``} gap="2">
      <HStack
        align="center"
        css={css`
          gap: 6px;
        `}
      >
        <BasicPDFViewerButton
          cssOveride={css`
            padding: 0;
            height: 24px;
            width: 24px;
          `}
          disabled={properties.currentPageNumber === 1}
          icon={<ChevronUpIcon fontSize="24px" title="forrige side" />}
          onClick={goToPreviousPage}
        />
        <BasicPDFViewerButton
          cssOveride={css`
            padding: 0;
            height: 24px;
            width: 24px;
          `}
          disabled={properties.currentPageNumber === properties.totalNumberOfPages}
          icon={<ChevronDownIcon fontSize="24px" title="neste side" />}
          onClick={goToNextPage}
        />
      </HStack>
      <HStack gap="1">
        <TextField
          css={css`
            input {
              width: 27px;
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
            width: 27px;
            align-self: center;
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
        cssOveride={css`
          height: 32px;
          width: 32px;
        `}
        icon={<ZoomPlusIcon fontSize="20px" title="zoom-in" />}
        onClick={() => {
          if (properties.scale <= 1.5) {
            properties.setScale(properties.scale + 0.1);
          }
        }}
      />

      <BasicPDFViewerButton
        cssOveride={css`
          height: 32px;
          width: 32px;
        `}
        icon={<ZoomMinusIcon fontSize="20px" title="zoom-ut" />}
        onClick={() => {
          if (properties.scale >= 1) {
            properties.setScale(properties.scale - 0.1);
          }
        }}
      />
    </HStack>
  );
};

const BasicPDFViewerButton = (properties: {
  cssOveride?: SerializedStyles;
  disabled?: boolean;
  onClick: () => void;
  icon: React.ReactNode;
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
      icon={properties.icon}
      onClick={properties.onClick}
      type="button"
      variant="tertiary-neutral"
    />
  );
};
