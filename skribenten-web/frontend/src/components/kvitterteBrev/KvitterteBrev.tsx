import { css } from "@emotion/react";
import { Accordion } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";
import type { AxiosError } from "axios";

import { sendBrev } from "~/api/sak-api-endpoints";
import { useSendtBrev } from "~/routes/saksnummer_/$saksId/kvittering/-components/SendtBrevContext";
import type { BestillBrevResponse, BrevInfo } from "~/types/brev";
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
        justify-self: center;
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

/**
 * Ikke så veldig glad i at vi må skille både mellom api status og context...
 * ulik handling basert på context, og response fra backend er heller ikke det samme
 * dette vil skalere veldig dårlig dersom kvitteringen skal måtte håndtere et nytt endepunkt, med ny response type
 */
const AccordionItem = (props: {
  saksId: string;
  brevFørHandling: BrevInfo;
  apiStatus: "error" | "success";
  context: "sendBrev" | "attestering";
  journalpostId: Nullable<number>;
}) => {
  const { setBrevResult } = useSendtBrev();
  const key = String(props.brevFørHandling.id);

  const sendBrevMutation = useMutation<BestillBrevResponse, AxiosError>({
    mutationFn: () => sendBrev(props.saksId, props.brevFørHandling.id),
    onSuccess: (response) => {
      setBrevResult(key, {
        status: "success",
        brevInfo: props.brevFørHandling,
        response,
      });
    },
    onError: (error) => {
      setBrevResult(key, {
        status: "error",
        brevInfo: props.brevFørHandling,
        error,
      });
    },
  });

  return (
    <Accordion.Item>
      <AccordionHeader apiStatus={props.apiStatus} brevInfo={props.brevFørHandling} context={props.context} />
      <AccordionContent
        apiStatus={props.apiStatus}
        distribusjonstype={props.brevFørHandling.distribusjonstype}
        isPending={sendBrevMutation.isPending}
        journalpostId={props.journalpostId}
        mottaker={props.brevFørHandling.mottaker}
        onRetry={() => sendBrevMutation.mutate()}
        saksId={props.saksId}
      />
    </Accordion.Item>
  );
};
