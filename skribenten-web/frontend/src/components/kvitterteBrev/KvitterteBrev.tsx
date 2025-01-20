import { css } from "@emotion/react";
import { Accordion } from "@navikt/ds-react";

import type { BrevInfo } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";

import AccordionContent from "./KvittertBrevContent";
import AccordionHeader from "./KvittertBrevHeader";
import { getPriorityKey, kvitteringSorteringsPrioritet, type KvittertBrev } from "./KvitterteBrevUtils";

const KvitterteBrev = (properties: { sakId: string; kvitterteBrev: KvittertBrev[] }) => {
  const sorted = properties.kvitterteBrev.toSorted(
    (a, b) =>
      kvitteringSorteringsPrioritet[getPriorityKey(a.apiStatus, a.context, a.brevFørHandling)] -
      kvitteringSorteringsPrioritet[getPriorityKey(b.apiStatus, b.context, b.brevFørHandling)],
  );

  return (
    <Accordion
      css={css`
        /* fastsetter bredden slik at innholdet ikke 'hopper' når man åpner/lukker. Dette skjer når parent container er flex */
        width: 350px;
      `}
    >
      {sorted.map((kvittertBrev, index) => {
        return (
          <AccordionItem
            apiStatus={kvittertBrev.apiStatus}
            brevFørHandling={kvittertBrev.brevFørHandling}
            context={kvittertBrev.context}
            journalpostId={kvittertBrev.sendtBrevResponse?.journalpostId ?? null}
            key={`resultat-${index}`}
            saksId={properties.sakId}
          />
        );
      })}
    </Accordion>
  );
};

export default KvitterteBrev;

const AccordionItem = (props: {
  saksId: string;
  brevFørHandling: BrevInfo;
  apiStatus: "error" | "success";
  context: "sendBrev" | "attestering";
  journalpostId: Nullable<number>;
}) => {
  return (
    <Accordion.Item>
      <AccordionHeader apiStatus={props.apiStatus} brevInfo={props.brevFørHandling} context={props.context} />
      <AccordionContent
        apiStatus={props.apiStatus}
        brevId={props.brevFørHandling.id}
        context={props.context}
        distribusjonstype={props.brevFørHandling.distribusjonstype}
        journalpostId={props.journalpostId}
        mottaker={props.brevFørHandling.mottaker}
        saksId={props.saksId}
      />
    </Accordion.Item>
  );
};
