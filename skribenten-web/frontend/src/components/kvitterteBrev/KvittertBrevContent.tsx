import { css } from "@emotion/react";
import { Accordion, BodyShort, Button, VStack } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";

import { hentPdfForJournalpost } from "~/api/sak-api-endpoints";
import { useSakGjelderNavnFormatert } from "~/hooks/useSakGjelderNavn";
import Oppsummeringspar from "~/routes/saksnummer_/$saksId/kvittering/-components/Oppsummeringspar";
import type { BrevInfo } from "~/types/brev";
import { Distribusjonstype } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";

import { ApiError } from "../ApiError";
import { distribusjonstypeTilText } from "./KvitterteBrevUtils";

const AccordionContent = (props: {
  saksId: string;
  apiStatus: "error" | "success";
  isPending: boolean;
  onRetry: () => void;
  journalpostId: Nullable<number>;
  brev: BrevInfo;
  open: boolean;
}) => {
  switch (props.apiStatus) {
    case "error":
      return <AccordionContentError isPending={props.isPending} onPrøvIgjenClick={props.onRetry} open={props.open} />;

    case "success":
      return (
        <AccordionContentSuccess
          brev={props.brev}
          journalpostId={props.journalpostId}
          open={props.open}
          saksId={props.saksId}
        />
      );
  }
};

export default AccordionContent;

const AccordionContentSuccess = (props: {
  saksId: string;
  brev: BrevInfo;
  journalpostId: Nullable<number>;
  open: boolean;
}) => {
  const pdfForJournalpost = useMutation<Blob, Error, number>({
    mutationFn: (journalpostId) => hentPdfForJournalpost.queryFn(props.saksId, journalpostId),
    onSuccess: (pdf) => window.open(URL.createObjectURL(pdf), "_blank"),
  });

  const sakenGjelderNavn = useSakGjelderNavnFormatert({ saksId: props.saksId });
  const showOpenPdf =
    props.brev.distribusjonstype === Distribusjonstype.LOKALPRINT && props.brev.status.type !== "Attestering";

  return (
    <Accordion.Content
      css={css`
        box-shadow: none;
        margin: ${props.open ? "var(--ax-space-12)" : "0"} var(--ax-space-8);
        padding-block: 0;
        padding-inline: var(--ax-space-8);

        > div {
          padding-block: 0;
          padding-inline: 0;
        }
      `}
      data-cy={`journalpostId-${props.journalpostId}`}
    >
      <VStack align="start" gap="space-16">
        <Oppsummeringspar
          tittel="Mottaker"
          verdi={props.brev?.mottaker?.navn ?? sakenGjelderNavn ?? "Fant ikke mottakerens navn"}
        />

        <Oppsummeringspar tittel="Distribueres via" verdi={distribusjonstypeTilText(props.brev.distribusjonstype)} />
        {props.journalpostId && <Oppsummeringspar tittel="Journalpost ID" verdi={props.journalpostId!} />}
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
        {pdfForJournalpost.isError && <ApiError error={pdfForJournalpost.error} title="Klarte ikke å hente PDF" />}
      </VStack>
    </Accordion.Content>
  );
};

const AccordionContentError = (props: { onPrøvIgjenClick: () => void; isPending: boolean; open: boolean }) => {
  return (
    <Accordion.Content
      css={css`
        box-shadow: none;
        margin: ${props.open ? "var(--ax-space-12)" : "0"} var(--ax-space-8);
        padding-block: 0;
        padding-inline: var(--ax-space-8);

        > div {
          padding-block: 0;
          padding-inline: 0;
        }
      `}
    >
      <VStack align="start" gap="space-12">
        <VStack gap="space-20">
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
