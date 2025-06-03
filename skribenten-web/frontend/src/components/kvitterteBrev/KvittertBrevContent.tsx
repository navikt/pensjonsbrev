import { Accordion, BodyShort, Button, VStack } from "@navikt/ds-react";
import { useMutation, useQuery } from "@tanstack/react-query";

import { hentPdfForJournalpostQuery } from "~/api/sak-api-endpoints";
import { getNavnQuery } from "~/api/skribenten-api-endpoints";
import Oppsummeringspar from "~/routes/saksnummer_/$saksId/kvittering/-components/Oppsummeringspar";
import type { BrevInfo } from "~/types/brev";
import { Distribusjonstype } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";
import { humanizeName } from "~/utils/stringUtils";
import { queryFold } from "~/utils/tanstackUtils";

import { ApiError } from "../ApiError";
import { distribusjonstypeTilText } from "./KvitterteBrevUtils";

const AccordionContent = (props: {
  saksId: string;
  apiStatus: "error" | "success";
  isPending: boolean;
  onRetry: () => void;
  journalpostId: Nullable<number>;
  brev: BrevInfo;
}) => {
  switch (props.apiStatus) {
    case "error":
      return <AccordionContentError isPending={props.isPending} onPrøvIgjenClick={props.onRetry} />;

    case "success":
      return <AccordionContentSuccess brev={props.brev} journalpostId={props.journalpostId} saksId={props.saksId} />;
  }
};

export default AccordionContent;

const AccordionContentSuccess = (props: { saksId: string; brev: BrevInfo; journalpostId: Nullable<number> }) => {
  const pdfForJournalpost = useMutation<Blob, Error, number>({
    mutationFn: (journalpostId) => hentPdfForJournalpostQuery.queryFn(props.saksId, journalpostId),
    onSuccess: (pdf) => window.open(URL.createObjectURL(pdf), "_blank"),
  });

  const hentNavnQuery = useQuery(getNavnQuery(props.saksId.toString()));
  const showOpenPdf =
    props.brev.distribusjonstype === Distribusjonstype.LOKALPRINT && props.brev.status.type !== "Attestering";

  return (
    <Accordion.Content data-cy={`journalpostId-${props.journalpostId}`}>
      <VStack align={"start"} gap="4">
        {props.brev.mottaker ? (
          <Oppsummeringspar tittel={"Mottaker"} verdi={props.brev.mottaker.navn ?? "Fant ikke mottakerens navn"} />
        ) : (
          queryFold({
            query: hentNavnQuery,
            initial: () => <></>,
            pending: () => <BodyShort>Henter mottaker navn...</BodyShort>,
            error: (error) => <ApiError error={error} title={"Klarte ikke å hente mottaker"} />,
            success: (navn) => <Oppsummeringspar tittel={"Mottaker"} verdi={humanizeName(navn)} />,
          })
        )}

        <Oppsummeringspar tittel={"Distribueres via"} verdi={distribusjonstypeTilText(props.brev.distribusjonstype)} />
        {props.journalpostId && <Oppsummeringspar tittel={"Journalpost ID"} verdi={props.journalpostId!} />}
        {showOpenPdf && (
          <Button
            loading={pdfForJournalpost.isPending}
            onClick={() => pdfForJournalpost.mutate(props.journalpostId!)}
            size="small"
            type="button"
          >
            Åpne PDF i ny fane
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
