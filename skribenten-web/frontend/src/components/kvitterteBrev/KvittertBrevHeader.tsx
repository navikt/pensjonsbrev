import { css } from "@emotion/react";
import { ExpansionCard, Tag, VStack } from "@navikt/ds-react";

import { type BrevInfo, Distribusjonstype } from "~/types/brev";

const AccordionHeader = (props: {
  apiStatus: "error" | "success";
  context: "sendBrev" | "attestering";
  brevInfo: BrevInfo;
}) => {
  const { tag, tittel } = hentTagOgTittelForHeader({
    apiStatus: props.apiStatus,
    context: props.context,
    brevInfo: props.brevInfo,
  });

  return (
    <ExpansionCard.Header>
      <ExpansionCard.Title
        as="h2"
        css={css`
          font-size: var(--ax-font-size-heading-xsmall);
        `}
        size="small"
      >
        <VStack align="start" gap="space-8" justify="start">
          {tag}
          {tittel}
        </VStack>
      </ExpansionCard.Title>
    </ExpansionCard.Header>
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
        <Tag data-color="danger" size="small" variant="moderate">
          Kunne ikke sende brev
        </Tag>
      );
      return { tag, tittel: args.brevInfo.brevtittel };
    }
    case "success": {
      if (args.context === "attestering") {
        const tag = (
          <Tag data-color="meta-lime" size="small" variant="moderate">
            Klar til attestering
          </Tag>
        );
        return { tag, tittel: args.brevInfo.brevtittel };
      } else {
        switch (args.brevInfo.distribusjonstype) {
          case Distribusjonstype.SENTRALPRINT: {
            const tag = (
              <Tag data-color="success" size="small" variant="moderate">
                Sendt til mottaker
              </Tag>
            );
            return { tag, tittel: args.brevInfo.brevtittel };
          }
          case Distribusjonstype.LOKALPRINT: {
            const tag = (
              <Tag data-color="info" size="small" variant="moderate">
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
