import { css } from "@emotion/react";
import { Accordion, BodyShort, Button, Label, Tag, VStack } from "@navikt/ds-react";
import { useMutation, useQuery } from "@tanstack/react-query";

import { hentPdfForJournalpostQuery, sendBrev } from "~/api/sak-api-endpoints";
import { getNavn } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { Mottaker } from "~/types/brev";
import { type BestillBrevResponse, Distribusjonstype } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";
import { humanizeName } from "~/utils/stringUtils";
import { queryFold } from "~/utils/tanstackUtils";

import { distribusjonstypeTilText } from "./KvitteringUtils";
import Oppsummeringspar from "./Oppsummeringspar";
import type { SendtBrevResponse, SendtBrevResponser } from "./SendtBrevResultatContext";

//TODO - komponenten er nå i bruk i 2 forskjellige steder. flytt til /components
const KvitterteBrev = (properties: { sakId: string; resultat: SendtBrevResponser }) => {
  return (
    <Accordion>
      {properties.resultat.map((result, index) => (
        <KvittertBrev key={`resultat-${index}`} resultat={result} sakId={properties.sakId} />
      ))}
    </Accordion>
  );
};

export default KvitterteBrev;

const KvittertBrev = (properties: { sakId: string; resultat: SendtBrevResponse }) => {
  const mutation = useMutation<BestillBrevResponse, Error, number>({
    mutationFn: (brevId) => sendBrev(properties.sakId, brevId),
  });

  return (
    <Accordion.Item>
      <KvittertBrevHeader
        resultat={
          mutation.isSuccess
            ? {
                status: "success",
                brevInfo: properties.resultat.brevInfo,
                response: mutation.data,
              }
            : properties.resultat
        }
        sakId={properties.sakId}
      />
      <KvittertBrevContentWrapper
        error={mutation.isError ? mutation.error : null}
        isPending={mutation.isPending}
        onPrøvIgjenClick={() => mutation.mutate(properties.resultat.brevInfo.id)}
        resultat={
          mutation.isSuccess
            ? {
                status: "success",
                brevInfo: properties.resultat.brevInfo,
                response: mutation.data,
              }
            : properties.resultat
        }
        sakId={properties.sakId}
      />
    </Accordion.Item>
  );
};

const KvittertBrevHeader = (properties: { sakId: string; resultat: SendtBrevResponse }) => {
  const { tag, tittel } = hentTagOgTittelForHeader(properties.resultat);

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

const hentTagOgTittelForHeader = (resultat: SendtBrevResponse) => {
  switch (resultat.status) {
    case "error": {
      const tag = (
        <Tag size="small" variant={"error"}>
          Kunne ikke sende brev
        </Tag>
      );

      return { tag, tittel: resultat.brevInfo.brevtittel };
    }
    case "success": {
      const tag = resultat.response.journalpostId ? (
        <Tag
          size="small"
          variant={resultat.brevInfo.distribusjonstype === Distribusjonstype.LOKALPRINT ? "info" : "success"}
        >
          {resultat.brevInfo.distribusjonstype === Distribusjonstype.LOKALPRINT
            ? "Lokalprint - sendt til joark"
            : "Sendt til mottaker"}
        </Tag>
      ) : (
        <Tag size="small" variant={"error"}>
          Kunne ikke sende brev
        </Tag>
      );

      return { tag, tittel: resultat.brevInfo.brevtittel };
    }
  }
};

const KvittertBrevContentWrapper = (properties: {
  sakId: string;
  resultat: SendtBrevResponse;
  onPrøvIgjenClick: () => void;
  isPending: boolean;
  error: Nullable<Error>;
}) => {
  return (
    <Accordion.Content>
      <KvittertBrevContent
        isPending={properties.isPending}
        onPrøvIgjenClick={properties.onPrøvIgjenClick}
        resultat={properties.resultat}
        sakId={properties.sakId}
      />
    </Accordion.Content>
  );
};

const KvittertBrevContent = (properties: {
  sakId: string;
  resultat: SendtBrevResponse;
  onPrøvIgjenClick: () => void;
  isPending: boolean;
}) => {
  switch (properties.resultat.status) {
    case "error": {
      return (
        <BrevIkkeSendtKvittering isPending={properties.isPending} onPrøvIgjenClick={properties.onPrøvIgjenClick} />
      );
    }
    case "success": {
      if (properties.resultat.response.error != null) {
        return (
          <BrevIkkeSendtKvittering isPending={properties.isPending} onPrøvIgjenClick={properties.onPrøvIgjenClick} />
        );
      } else if (properties.resultat.response.journalpostId == null) {
        return (
          <BrevIkkeSendtKvittering isPending={properties.isPending} onPrøvIgjenClick={properties.onPrøvIgjenClick} />
        );
      } else {
        return (
          <BrevSendtKvittering
            distribusjonstype={properties.resultat.brevInfo.distribusjonstype}
            journalpostId={properties.resultat.response.journalpostId}
            mottaker={properties.resultat.brevInfo.mottaker}
            saksId={properties.sakId}
          />
        );
      }
    }
  }
};

export const BrevSendtKvittering = (props: {
  saksId: string;
  distribusjonstype: Distribusjonstype;
  journalpostId: number;
  /**
   * defaulter til brukeren
   */
  mottaker: Nullable<Mottaker>;
}) => {
  const pdfForJournalpost = useMutation<Blob, Error>({
    mutationFn: () => hentPdfForJournalpostQuery.queryFn(props.saksId, props.journalpostId),
    onSuccess: (pdf) => window.open(URL.createObjectURL(pdf), "_blank"),
  });

  const hentNavnQuery = useQuery({
    queryKey: getNavn.queryKey(props.saksId),
    queryFn: () => getNavn.queryFn(props.saksId),
  });

  return (
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
      <Oppsummeringspar tittel={"Journalpost ID"} verdi={props.journalpostId} />
      {props.distribusjonstype === Distribusjonstype.LOKALPRINT && (
        <Button
          loading={pdfForJournalpost.isPending}
          onClick={() => pdfForJournalpost.mutate()}
          size="small"
          type="button"
        >
          Åpne utskrivbar fil i ny fane
        </Button>
      )}
      {pdfForJournalpost.isError && <ApiError error={pdfForJournalpost.error} title={"Klarte ikke å hente PDF"} />}
    </VStack>
  );
};

export const BrevIkkeSendtKvittering = (properties: { onPrøvIgjenClick: () => void; isPending: boolean }) => {
  return (
    <VStack align="start" gap="3">
      <BodyShort size="small">Skribenten klarte ikke å sende brevet.</BodyShort>
      <BodyShort size="small">Brevet ligger lagret i brevbehandler til brevet er sendt.</BodyShort>

      <Button
        loading={properties.isPending}
        onClick={properties.onPrøvIgjenClick}
        size="small"
        type="button"
        variant="primary-neutral"
      >
        Prøv igjen
      </Button>
    </VStack>
  );
};
