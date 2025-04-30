import { Accordion, BodyShort, Button, VStack } from "@navikt/ds-react";
import { useMutation, useQuery } from "@tanstack/react-query";
import type { AxiosError } from "axios";

import { hentPdfForJournalpostQuery, sendBrev } from "~/api/sak-api-endpoints";
import { getNavnQuery } from "~/api/skribenten-api-endpoints";
import Oppsummeringspar from "~/routes/saksnummer_/$saksId/kvittering/-components/Oppsummeringspar";
import type {
  SendtBrevErrorResponse,
  SendtBrevSuccessResponse,
} from "~/routes/saksnummer_/$saksId/kvittering/-components/SendtBrevResultatContext";
import { useSendtBrevResultatContext } from "~/routes/saksnummer_/$saksId/kvittering/-components/SendtBrevResultatContext";
import type { BestillBrevResponse, Mottaker } from "~/types/brev";
import { Distribusjonstype } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";
import { humanizeName } from "~/utils/stringUtils";
import { queryFold } from "~/utils/tanstackUtils";

import { ApiError } from "../ApiError";
import { distribusjonstypeTilText } from "./KvitterteBrevUtils";

const AccordionContent = (props: { saksId: string; brevId: number }) => {
  const { resultat, setResultat } = useSendtBrevResultatContext();
  const filtered = resultat.find((brev) => brev.brevInfo.id === props.brevId);

  const sendBrevMutation = useMutation<BestillBrevResponse, Error>({
    mutationFn: () => sendBrev(props.saksId, props.brevId),
    onSuccess: (response) => {
      if (response.error) {
        const errorEntry: SendtBrevErrorResponse = {
          status: "error",
          error: response.error as unknown as AxiosError<unknown, unknown>,
          brevInfo: filtered!.brevInfo,
        };
        setResultat([...resultat.filter((brev) => brev.brevInfo.id !== props.brevId), errorEntry]);
        return;
      }
      const successEntry: SendtBrevSuccessResponse = {
        status: "success",
        response,
        brevInfo: filtered!.brevInfo,
      };
      setResultat([...resultat.filter((brev) => brev.brevInfo.id !== props.brevId), successEntry]);
    },
  });

  if (!filtered || filtered.status === "error") {
    return (
      <AccordionContentError
        isPending={sendBrevMutation.isPending}
        onPrøvIgjenClick={() => {
          sendBrevMutation.mutate();
        }}
      />
    );
  }
  if (filtered.status === "success") {
    return (
      <AccordionContentSuccess
        distribusjonstype={filtered.brevInfo.distribusjonstype}
        journalpostId={filtered.response.journalpostId}
        mottaker={filtered.brevInfo.mottaker}
        saksId={props.saksId}
      />
    );
  }
};
export default AccordionContent;

const AccordionContentSuccess = (props: {
  saksId: string;
  distribusjonstype: Distribusjonstype;
  journalpostId: Nullable<number>;
  /**
   * defaulter til brukeren
   */
  mottaker: Nullable<Mottaker>;
}) => {
  const pdfForJournalpost = useMutation<Blob, Error, number>({
    mutationFn: (journalpostId) => hentPdfForJournalpostQuery.queryFn(props.saksId, journalpostId),
    onSuccess: (pdf) => window.open(URL.createObjectURL(pdf), "_blank"),
  });

  const hentNavnQuery = useQuery(getNavnQuery(props.saksId.toString()));

  return (
    <Accordion.Content>
      <VStack align={"start"} gap="4">
        {props.mottaker ? (
          <Oppsummeringspar tittel={"Mottaker"} verdi={props.mottaker.navn ?? "Fant ikke mottakerens navn"} />
        ) : (
          queryFold({
            query: hentNavnQuery,
            initial: () => <></>,
            pending: () => <BodyShort>Henter mottaker navn...</BodyShort>,
            error: (error) => <ApiError error={error} title={"Klarte ikke å hente mottaker"} />,
            success: (navn) => <Oppsummeringspar tittel={"Mottaker"} verdi={humanizeName(navn)} />,
          })
        )}

        <Oppsummeringspar tittel={"Distribueres via"} verdi={distribusjonstypeTilText(props.distribusjonstype)} />
        {props.journalpostId && <Oppsummeringspar tittel={"Journalpost ID"} verdi={props.journalpostId!} />}
        {props.distribusjonstype === Distribusjonstype.LOKALPRINT && (
          <Button
            loading={pdfForJournalpost.isPending}
            onClick={() => pdfForJournalpost.mutate(props.journalpostId!)}
            size="small"
            type="button"
          >
            Åpne utskrivbar fil i ny fane
          </Button>
        )}
        {pdfForJournalpost.isError && <ApiError error={pdfForJournalpost.error} title={"Klarte ikke å hente PDF"} />}
      </VStack>
    </Accordion.Content>
  );
};

const AccordionContentError = (props: { onPrøvIgjenClick: () => void; isPending: boolean }) => {
  return (
    <Accordion.Content>
      <VStack align="start" gap="3">
        <VStack gap="5">
          <BodyShort size="small">Skribenten klarte ikke å sende brevet.</BodyShort>
          <BodyShort size="small">Brevet ligger lagret i brevbehandler til brevet er sendt.</BodyShort>
        </VStack>

        <Button loading={props.isPending} onClick={props.onPrøvIgjenClick} size="small" type="button">
          Prøv å sende igjen
        </Button>
      </VStack>
    </Accordion.Content>
  );
};
