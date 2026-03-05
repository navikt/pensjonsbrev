import { css } from "@emotion/react";
import { Accordion, Label, Tag, VStack } from "@navikt/ds-react";

import { type BrevInfo, Distribusjonstype } from "~/types/brev";

const AccordionHeader = (props: {
  apiStatus: "error" | "success";
  context: "sendBrev" | "attestering";
  brevInfo: BrevInfo;
  open: boolean;
}) => {
  const { tag, tittel } = hentTagOgTittelForHeader({
    apiStatus: props.apiStatus,
    context: props.context,
    brevInfo: props.brevInfo,
  });

  return (
    <Accordion.Header
      css={css`
        ${!props.open ? "border-radius: var(--ax-radius-12); border: 1px solid var(--ax-border-neutral);" : ""}

        &::before,
        &::after {
          content: none;
          display: none;
        }

        > span:first-of-type {
          order: 2;
          margin-left: auto;
          background: none;
          color: var(--ax-text-neutral);
        }

        > span:last-of-type {
          order: 1;
          color: var(--ax-text-neutral);
        }

        &:hover > span:first-of-type,
        &:focus-visible > span:first-of-type {
          color: var(--ax-text-neutral);
        }
      `}
    >
      <VStack align="start" gap="space-8" justify="start">
        {tag}
        <Label>{tittel}</Label>
      </VStack>
    </Accordion.Header>
  );
};

export default AccordionHeader;

const hentTagOgTittelForHeader = (args: {
  apiStatus: "error" | "success";
  context: "sendBrev" | "attestering";
  brevInfo: BrevInfo;
}) => {
  switch (args.apiStatus) {
    case "error": {
      const tag = (
        <Tag
          css={css`
            box-shadow: none;
          `}
          data-color="danger"
          size="small"
          variant="outline"
        >
          Kunne ikke sende brev
        </Tag>
      );
      return { tag, tittel: args.brevInfo.brevtittel };
    }
    case "success": {
      if (args.context === "attestering") {
        const tag = (
          <Tag
            css={css`
              box-shadow: none;
            `}
            data-color="meta-lime"
            size="small"
            variant="outline"
          >
            Klar til attestering
          </Tag>
        );
        return { tag, tittel: args.brevInfo.brevtittel };
      } else {
        switch (args.brevInfo.distribusjonstype) {
          case Distribusjonstype.SENTRALPRINT: {
            const tag = (
              <Tag
                css={css`
                  box-shadow: none;
                `}
                data-color="success"
                size="small"
                variant="outline"
              >
                Sendt til mottaker
              </Tag>
            );
            return { tag, tittel: args.brevInfo.brevtittel };
          }
          case Distribusjonstype.LOKALPRINT: {
            const tag = (
              <Tag
                css={css`
                  box-shadow: none;
                `}
                data-color="info"
                size="small"
                variant="outline"
              >
                Lokalprint – arkivert
              </Tag>
            );
            return { tag, tittel: args.brevInfo.brevtittel };
          }
        }
      }
    }
  }
};
