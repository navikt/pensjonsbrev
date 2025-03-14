import { css } from "@emotion/react";
import { Accordion, Label, Tag } from "@navikt/ds-react";

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
      <div
        css={css`
          display: flex;
          flex-direction: column;
          align-items: flex-start;
        `}
      >
        {tag}
        <Label>{tittel}</Label>
      </div>
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
        <Tag size="small" variant={"error"}>
          Kunne ikke sende brev
        </Tag>
      );
      return { tag, tittel: args.brevInfo.brevtittel };
    }
    case "success": {
      if (args.context === "attestering") {
        const tag = (
          <Tag size="small" variant={"alt2"}>
            Klar til attestering
          </Tag>
        );
        return { tag, tittel: args.brevInfo.brevtittel };
      } else {
        switch (args.brevInfo.distribusjonstype) {
          case Distribusjonstype.SENTRALPRINT: {
            const tag = (
              <Tag size="small" variant={"success"}>
                Sendt til mottaker
              </Tag>
            );
            return { tag, tittel: args.brevInfo.brevtittel };
          }
          case Distribusjonstype.LOKALPRINT: {
            const tag = (
              <Tag size="small" variant={"info"}>
                Lokalprint - arkivert
              </Tag>
            );
            return { tag, tittel: args.brevInfo.brevtittel };
          }
        }
      }
    }
  }
};
