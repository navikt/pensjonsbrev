import { css } from "@emotion/react";
import { XMarkOctagonFillIcon } from "@navikt/aksel-icons";
import { Accordion, BodyShort, Button, Label, Tag, VStack } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";

import { hentPdfForJournalpostQuery, sendBrev } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";
import { type BestillBrevResponse, Distribusjonstype } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";

import type {
  FerdigstillResponse,
  FerdigstillResponser,
  FerdigstillSuccessResponse,
} from "./FerdigstillResultatContext";
import { distribusjonstypeTilText } from "./KvitteringUtils";
import Oppsummeringspar from "./Oppsummeringspar";

const KvitterteBrev = (properties: { sakId: string; resultat: FerdigstillResponser }) => {
  return (
    <Accordion>
      {properties.resultat.map((result, index) => (
        <KvittertBrev key={`resultat-${index}`} resultat={result} sakId={properties.sakId} />
      ))}
    </Accordion>
  );
};

export default KvitterteBrev;

const KvittertBrev = (properties: { sakId: string; resultat: FerdigstillResponse }) => {
  const mutation = useMutation<BestillBrevResponse, Error, number>({
    mutationFn: (brevId) => sendBrev(properties.sakId, brevId),
  });

  return (
    <Accordion.Item>
      <KvittertBrevHeader
        resultat={
          mutation.isSuccess
            ? {
                status: "fulfilledWithSuccess",
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
                status: "fulfilledWithSuccess",
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

const KvittertBrevHeader = (properties: { sakId: string; resultat: FerdigstillResponse }) => {
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

const hentTagOgTittelForHeader = (resultat: FerdigstillResponse) => {
  switch (resultat.status) {
    case "fulfilledWithError": {
      const tag = (
        <Tag size="small" variant={"error"}>
          Kunne ikke sende brev
        </Tag>
      );

      return { tag, tittel: resultat.brevInfo.brevtittel };
    }
    case "fulfilledWithSuccess": {
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
  resultat: FerdigstillResponse;
  onPrøvIgjenClick: () => void;
  isPending: boolean;
  error: Nullable<Error>;
}) => {
  return (
    <Accordion.Content>
      <KvittertBrevContent
        error={properties.error}
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
  resultat: FerdigstillResponse;
  onPrøvIgjenClick: () => void;
  isPending: boolean;
  error: Nullable<Error>;
}) => {
  switch (properties.resultat.status) {
    case "fulfilledWithError": {
      const error = properties.resultat.error;
      const correlationId = error.response?.headers["x-request-id"] ?? null;

      return (
        <KvittertBrevContentError
          error={properties.error}
          isPending={properties.isPending}
          onPrøvIgjenClick={properties.onPrøvIgjenClick}
          sakId={properties.sakId}
        >
          <BodyShort>Brevet ble ikke sendt pga en ukjent feil. Prøv igjen.</BodyShort>
          <BodyShort>
            Feilen var {error.code} - {error.message}
          </BodyShort>
          <BodyShort>Id for feilsøking: {correlationId ?? "Fant ikke feilsøkings-id"}</BodyShort>
        </KvittertBrevContentError>
      );
    }
    case "fulfilledWithSuccess": {
      if (properties.resultat.response.error != null) {
        return (
          <KvittertBrevContentError
            error={properties.error}
            isPending={properties.isPending}
            onPrøvIgjenClick={properties.onPrøvIgjenClick}
            sakId={properties.sakId}
          >
            <BodyShort>
              Brevet ble ikke sendt pga {properties.resultat.response.error?.tekniskgrunn ?? "en ukjent teknisk grunn"}.
              Prøv igjen.
            </BodyShort>
            <BodyShort>{properties.resultat.response.error?.beskrivelse}</BodyShort>
          </KvittertBrevContentError>
        );
      } else if (properties.resultat.response.journalpostId == null) {
        return (
          <KvittertBrevContentError
            error={properties.error}
            isPending={properties.isPending}
            onPrøvIgjenClick={properties.onPrøvIgjenClick}
            sakId={properties.sakId}
          >
            <BodyShort>Brevet ble ikke sendt pga en ukjent feil. Prøv igjen.</BodyShort>
          </KvittertBrevContentError>
        );
      } else {
        return <KvittertBrevContentSuccess resultat={properties.resultat} sakId={properties.sakId} />;
      }
    }
  }
};

const KvittertBrevContentSuccess = (properties: { sakId: string; resultat: FerdigstillSuccessResponse }) => {
  const pdfForJournalpost = useMutation<Blob, Error>({
    /* den er brukt i contexten av at journalpostId'en er sjekket til å være not null */
    mutationFn: () => hentPdfForJournalpostQuery.queryFn(properties.sakId, properties.resultat.response.journalpostId!),
    onSuccess: (pdf) => window.open(URL.createObjectURL(pdf), "_blank"),
  });

  return (
    <VStack align={"start"} gap="4">
      {/* TODO <Oppsummeringspar tittel={"Mottaker"} verdi={""} /> */}

      <Oppsummeringspar
        tittel={"Distribueres via"}
        verdi={distribusjonstypeTilText(properties.resultat.brevInfo.distribusjonstype)}
      />
      {/* den er brukt i contexten av at journalpostId'en er sjekket til å være not null */}
      <Oppsummeringspar tittel={"Journalpost ID"} verdi={properties.resultat.response.journalpostId!} />
      {properties.resultat.brevInfo.distribusjonstype === Distribusjonstype.LOKALPRINT && (
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

export const KvittertBrevContentError = (properties: {
  sakId: string;
  children: React.ReactNode;
  onPrøvIgjenClick: () => void;
  isPending: boolean;
  error: Nullable<Error>;
}) => {
  return (
    <VStack align="start" gap="3">
      <div
        css={css`
          display: grid;
          grid-template-columns: 1.5rem 1fr;
          gap: 1rem;
        `}
      >
        <XMarkOctagonFillIcon
          css={css`
            color: var(--a-nav-red);
          `}
          fontSize="1.5rem"
          title="error"
        />
        <div
          css={css`
            display: flex;
            flex-direction: column;
            gap: 1rem;
          `}
        >
          {properties.children}
        </div>
      </div>

      {properties.error && <ApiError error={properties.error} title={"Klarte ikke å sende brevet"} />}

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
