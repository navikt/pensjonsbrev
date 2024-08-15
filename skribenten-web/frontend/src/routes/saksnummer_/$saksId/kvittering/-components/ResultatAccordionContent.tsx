import { css } from "@emotion/react";
import { XMarkOctagonFillIcon } from "@navikt/aksel-icons";
import { Accordion, BodyShort, Button, VStack } from "@navikt/ds-react";

import { ApiError } from "~/components/ApiError";
import type { Nullable } from "~/types/Nullable";

import type { FerdigstillResponse } from "./FerdigstillResultatContext";
import Oppsummeringspar from "./Oppsummeringspar";

const ResultatAccordionContentWrapper = (properties: {
  sakId: string;
  resultat: FerdigstillResponse;
  onPrøvIgjenClick: () => void;
  isPending: boolean;
  error: Nullable<Error>;
}) => {
  return (
    <Accordion.Content>
      <ResultatAccordionContent
        error={properties.error}
        isPending={properties.isPending}
        onPrøvIgjenClick={properties.onPrøvIgjenClick}
        resultat={properties.resultat}
        sakId={properties.sakId}
      />
    </Accordion.Content>
  );
};

const ResultatAccordionContent = (properties: {
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
        <ResultatContentError
          error={properties.error}
          isPending={properties.isPending}
          onPrøvIgjenClick={properties.onPrøvIgjenClick}
          resultat={properties.resultat}
          sakId={properties.sakId}
        >
          <BodyShort>Brevet ble ikke sendt pga en ukjent feil. Prøv igjen.</BodyShort>
          <BodyShort>
            Feilen var {error.code} - {error.message}
          </BodyShort>
          <BodyShort>Id for feilsøking: {correlationId ?? "Fant ikke feilsøkings-id"}</BodyShort>
        </ResultatContentError>
      );
    }
    case "fulfilledWithSuccess": {
      if (properties.resultat.response.error != null) {
        return (
          <ResultatContentError
            error={properties.error}
            isPending={properties.isPending}
            onPrøvIgjenClick={properties.onPrøvIgjenClick}
            resultat={properties.resultat}
            sakId={properties.sakId}
          >
            <BodyShort>
              Brevet ble ikke sendt pga {properties.resultat.response.error?.tekniskgrunn ?? "en ukjent teknisk grunn"}.
              Prøv igjen.
            </BodyShort>
            <BodyShort>{properties.resultat.response.error?.beskrivelse}</BodyShort>
          </ResultatContentError>
        );
      } else if (properties.resultat.response.journalpostId == null) {
        return (
          <ResultatContentError
            error={properties.error}
            isPending={properties.isPending}
            onPrøvIgjenClick={properties.onPrøvIgjenClick}
            resultat={properties.resultat}
            sakId={properties.sakId}
          >
            <BodyShort>Brevet ble ikke sendt pga en ukjent feil. Prøv igjen.</BodyShort>
          </ResultatContentError>
        );
      } else {
        return (
          <VStack gap="4">
            {/* TODO <Oppsummeringspar tittel={"Mottaker"} verdi={""} /> */}
            {/* TODO <Oppsummeringspar tittel={"Distribueres via"} verdi={""} /> */}
            <Oppsummeringspar tittel={"Journalpost ID"} verdi={properties.resultat.response.journalpostId} />
          </VStack>
        );
      }
    }
  }
};

const ResultatContentError = (properties: {
  sakId: string;
  resultat: FerdigstillResponse;
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

export default ResultatAccordionContentWrapper;
