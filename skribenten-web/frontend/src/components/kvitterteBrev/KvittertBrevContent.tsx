import { Accordion, BodyShort, Button, VStack } from "@navikt/ds-react";
import { useMutation, useQuery } from "@tanstack/react-query";

import { hentPdfForJournalpostQuery, sendBrev, sendBrevTilAttestering } from "~/api/sak-api-endpoints";
import { getNavn } from "~/api/skribenten-api-endpoints";
import Oppsummeringspar from "~/routes/saksnummer_/$saksId/kvittering/-components/Oppsummeringspar";
import type { BestillBrevResponse, BrevInfo, Mottaker } from "~/types/brev";
import { Distribusjonstype } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";
import { humanizeName } from "~/utils/stringUtils";
import { queryFold } from "~/utils/tanstackUtils";

import { ApiError } from "../ApiError";
import { distribusjonstypeTilText } from "./KvitterteBrevUtils";

const AccordionContent = (props: {
  saksId: string;
  brevId: number;
  apiStatus: "error" | "success";
  context: "sendBrev" | "attestering";
  distribusjonstype: Distribusjonstype;
  journalpostId: Nullable<number>;
  mottaker: Nullable<Mottaker>;
}) => {
  const sendBrevMutation = useMutation<BestillBrevResponse, Error>({
    mutationFn: () => sendBrev(props.saksId, props.brevId),
  });

  const sendBrevTilAttesteringMutation = useMutation<BrevInfo, Error>({
    mutationFn: () => sendBrevTilAttestering({ saksId: props.saksId, brevId: props.brevId }),
  });

  switch (props.apiStatus) {
    case "error": {
      switch (props.context) {
        case "sendBrev": {
          return (
            <AccordionContentError isPending={sendBrevMutation.isPending} onPrøvIgjenClick={sendBrevMutation.mutate} />
          );
        }
        case "attestering": {
          return (
            <AccordionContentError
              isPending={sendBrevTilAttesteringMutation.isPending}
              onPrøvIgjenClick={sendBrevTilAttesteringMutation.mutate}
            />
          );
        }
      }
      break;
    }
    case "success": {
      switch (props.context) {
        case "sendBrev": {
          return (
            <AccordionContentSuccess
              distribusjonstype={props.distribusjonstype}
              journalpostId={props.journalpostId}
              mottaker={props.mottaker}
              saksId={props.saksId}
            />
          );
        }
        case "attestering": {
          return (
            <AccordionContentSuccess
              distribusjonstype={props.distribusjonstype}
              journalpostId={null}
              mottaker={props.mottaker}
              saksId={props.saksId}
            />
          );
        }
      }
    }
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

  const hentNavnQuery = useQuery({
    queryKey: getNavn.queryKey(props.saksId),
    queryFn: () => getNavn.queryFn(props.saksId),
  });

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
