import { BodyShort, Button, ExpansionCard, VStack } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";

import { hentPdfForJournalpost } from "~/api/sak-api-endpoints";
import { useSakGjelderNavnFormatert } from "~/hooks/useSakGjelderNavn";
import Oppsummeringspar from "~/routes/saksnummer_/$saksId/kvittering/-components/Oppsummeringspar";
import { type BrevInfo, Distribusjonstype } from "~/types/brev";
import { type Nullable } from "~/types/Nullable";

import { ApiError } from "../ApiError";
import { distribusjonstypeTilText } from "./KvitterteBrevUtils";

const KvittertBrevContent = (props: {
  saksId: string;
  apiStatus: "error" | "success";
  isPending: boolean;
  onRetry: () => void;
  journalpostId: Nullable<number>;
  brev: BrevInfo;
}) => {
  switch (props.apiStatus) {
    case "error":
      return <KvittertBrevContentError isPending={props.isPending} onPrøvIgjenClick={props.onRetry} />;

    case "success":
      return <KvittertBrevContentSuccess brev={props.brev} journalpostId={props.journalpostId} saksId={props.saksId} />;
  }
};

export default KvittertBrevContent;

const KvittertBrevContentSuccess = (props: { saksId: string; brev: BrevInfo; journalpostId: Nullable<number> }) => {
  const pdfForJournalpost = useMutation<Blob, Error, number>({
    mutationFn: (journalpostId) => hentPdfForJournalpost.queryFn(props.saksId, journalpostId),
    onSuccess: (pdf) => window.open(URL.createObjectURL(pdf), "_blank"),
  });

  const sakenGjelderNavn = useSakGjelderNavnFormatert({ saksId: props.saksId });
  const showOpenPdf =
    props.brev.distribusjonstype === Distribusjonstype.LOKALPRINT && props.brev.status.type !== "Attestering";

  return (
    <ExpansionCard.Content data-testid={`journalpostId-${props.journalpostId}`}>
      <VStack align="start" gap="space-16">
        <Oppsummeringspar
          size="small"
          tittel="Mottaker"
          verdi={props.brev?.mottaker?.navn ?? sakenGjelderNavn ?? "Fant ikke mottakerens navn"}
        />

        <Oppsummeringspar
          size="small"
          tittel="Distribusjon"
          verdi={distribusjonstypeTilText(props.brev.distribusjonstype)}
        />
        {props.journalpostId && <Oppsummeringspar size="small" tittel="Journalpost" verdi={props.journalpostId!} />}
        {showOpenPdf && (
          <Button
            loading={pdfForJournalpost.isPending}
            onClick={() => pdfForJournalpost.mutate(props.journalpostId!)}
            size="small"
            type="button"
            variant="secondary"
          >
            Åpne PDF
          </Button>
        )}
        {pdfForJournalpost.isError && <ApiError error={pdfForJournalpost.error} title="Klarte ikke å hente PDF" />}
      </VStack>
    </ExpansionCard.Content>
  );
};

const KvittertBrevContentError = (props: { onPrøvIgjenClick: () => void; isPending: boolean }) => {
  return (
    <ExpansionCard.Content>
      <VStack align="start" gap="space-12">
        <VStack gap="space-20">
          <BodyShort size="small">Skribenten klarte ikke å sende brevet.</BodyShort>
          <BodyShort size="small">Brevet ligger lagret i brevbehandler til brevet er sendt.</BodyShort>
        </VStack>

        <Button loading={props.isPending} onClick={props.onPrøvIgjenClick} size="small" type="button">
          Prøv å sende igjen
        </Button>
      </VStack>
    </ExpansionCard.Content>
  );
};
