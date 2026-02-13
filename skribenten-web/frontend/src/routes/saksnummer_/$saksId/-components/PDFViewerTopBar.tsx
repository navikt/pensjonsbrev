import type { SerializedStyles } from "@emotion/react";
import { css } from "@emotion/react";
import { ChevronDownIcon, ChevronUpIcon, ZoomMinusIcon, ZoomPlusIcon } from "@navikt/aksel-icons";
import { BodyShort, Box, Button, HStack, TextField } from "@navikt/ds-react";
import { useNavigate } from "@tanstack/react-router";
import type React from "react";
import { useEffect, useState } from "react";

import { VerticalDivider } from "~/components/Divider";
import { SlettBrev } from "~/components/SlettBrev";

import { Route } from "../route";

type ViewerControls = {
  totalNumberOfPages: number;
  currentPageNumber: number;
  setCurrentPageNumber: (n: number) => void;
  scale: number;
  setScale: (n: number) => void;
};

type PDFViewerTopBarProps = {
  sakId: string;
  brevId: number;
  utenSlettKnapp?: boolean;
  viewerControls?: ViewerControls;
};

const PDFViewerTopBar = ({ sakId, brevId, utenSlettKnapp, viewerControls }: PDFViewerTopBarProps) => {
  const navigate = useNavigate();
  const { enhetsId, vedtaksId } = Route.useSearch();
  return (
    <Box asChild background="default" borderColor="neutral-subtle" borderWidth="0 0 1 0">
      <HStack
        align="center"
        css={{ zIndex: 3 }}
        height="48px"
        justify="space-between"
        paddingBlock="space-8"
        paddingInline="space-16"
        position="sticky"
        top="space-0"
      >
        {viewerControls && (
          <HStack align="center" gap="space-16">
            <TopBarNavigation
              currentPageNumber={viewerControls.currentPageNumber}
              setCurrentPageNumber={viewerControls.setCurrentPageNumber}
              totalNumberOfPages={viewerControls.totalNumberOfPages}
            />
            <VerticalDivider />
            <TopBarZoom scale={viewerControls.scale} setScale={viewerControls.setScale} />
          </HStack>
        )}
        {!utenSlettKnapp && (
          <SlettBrev
            brevId={brevId}
            buttonText="Slett"
            onSlettSuccess={() =>
              navigate({
                to: "/saksnummer/$saksId/brevbehandler",
                params: { saksId: sakId },
                search: { enhetsId, vedtaksId },
              })
            }
            sakId={sakId}
          />
        )}
      </HStack>
    </Box>
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
    <HStack align="center" gap="space-8">
      <HStack align="center" gap="space-6">
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
      <HStack gap="space-4">
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

        <BodyShort css={{ width: "27px", alignSelf: "center" }}>/ {properties.totalNumberOfPages}</BodyShort>
      </HStack>
    </HStack>
  );
};

const TopBarZoom = (properties: { scale: number; setScale: (n: number) => void }) => {
  return (
    <HStack gap="space-8">
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
      data-color="neutral"
      disabled={properties.disabled}
      icon={properties.icon}
      onClick={properties.onClick}
      type="button"
      variant="tertiary"
    />
  );
};
