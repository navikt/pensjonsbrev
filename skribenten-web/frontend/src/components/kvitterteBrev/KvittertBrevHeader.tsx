import { Accordion, Label, Tag, VStack } from "@navikt/ds-react";

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
    <Accordion.Header>
      <VStack align="start" justify="start">
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
        <Tag data-color="danger" size="small" variant="outline">
          Kunne ikke sende brev
        </Tag>
      );
      return { tag, tittel: args.brevInfo.brevtittel };
    }
    case "success": {
      if (args.context === "attestering") {
        const tag = (
          <Tag data-color="meta-lime" size="small" variant="outline">
            Klar til attestering
          </Tag>
        );
        return { tag, tittel: args.brevInfo.brevtittel };
      } else {
        switch (args.brevInfo.distribusjonstype) {
          case Distribusjonstype.SENTRALPRINT: {
            const tag = (
              <Tag data-color="success" size="small" variant="outline">
                Sendt til mottaker
              </Tag>
            );
            return { tag, tittel: args.brevInfo.brevtittel };
          }
          case Distribusjonstype.LOKALPRINT: {
            const tag = (
              <Tag data-color="info" size="small" variant="outline">
                Lokalprint - sendt til joark
              </Tag>
            );
            return { tag, tittel: args.brevInfo.brevtittel };
          }
        }
      }
    }
  }
};
