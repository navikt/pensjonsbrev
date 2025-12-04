import { css } from "@emotion/react";
import { FilesIcon } from "@navikt/aksel-icons";
import { Alert, CopyButton, Heading, VStack } from "@navikt/ds-react";
import { AxiosError } from "axios";
import { useEffect } from "react";

import type { FailureType } from "~/types/apiTypes";
import { FAILURE_TYPES } from "~/types/apiTypes";
import { logError } from "~/utils/logger";

interface FunctionalErrorPayload {
  tittel?: string;
  melding?: string;
}

function isFunctionalError(error: AxiosError<unknown>): error is AxiosError<FunctionalErrorPayload> {
  const data = error.response?.data;
  return (
    error.response?.status === 422 &&
    data !== null &&
    typeof data === "object" &&
    ("tittel" in data || "melding" in data)
  );
}

export function ApiError({ error, title }: { error: unknown; title: string }) {
  useEffect(() => {
    if (error) {
      // eslint-disable-next-line no-console
      console.log(error);
      const originalStatus = error instanceof AxiosError ? error.status : undefined;
      // eslint-disable-next-line no-console
      logError(error, originalStatus).catch(() => console.error("Unable to log error message"));
    }
  }, [error]);

  if (error instanceof AxiosError) {
    if (isFunctionalError(error)) {
      const { tittel, melding } = error.response!.data;

      return (
        <Alert
          css={css`
            align-self: center;
            width: 100%;
            max-width: 512px;
          `}
          data-cy="functional-error-alert"
          size="medium"
          variant="error"
        >
          <Heading level="2" size="small">
            {tittel ?? title}
          </Heading>
          {melding && (
            <div
              css={css`
                margin-top: 4px;
              `}
            >
              {melding}
            </div>
          )}
        </Alert>
      );
    }

    const correlationId = error.response?.headers["x-request-id"];
    return (
      <Alert
        css={css`
          align-self: center;
          width: 100%;
          max-width: 512px;
        `}
        size="small"
        variant="error"
      >
        <Heading level="2" size="small">
          {title}
        </Heading>
        {correlationId && (
          <VStack gap="space-4">
            <div>
              <span>{mapErrorMessage(error.message)}</span>
              <span>
                Hvis det skjer igjen, trykk på knappen <i>Kopier ID</i> nedenfor og meld feil til oss i Teams.
              </span>
            </div>
            <CopyButton
              copyText={correlationId}
              css={css`
                border-radius: var(--ax-radius-4);
                background: var(--ax-bg-default);
                width: fit-content;
                box-shadow: inset 0 0 0 2px var(--ax-border-neutral);
              `}
              icon={<FilesIcon />}
              size="small"
              text="Kopier ID"
              variant="neutral"
            />
          </VStack>
        )}
      </Alert>
    );
  }

  // If the error is not an axios error, then there is a programming error. Fallback to simple handling
  return (
    <div>
      <Alert variant={"error"}>En feil har oppstått og blitt logget. Prøv igjen litt senere.</Alert>
    </div>
  );
}

function mapErrorMessage(errorMessage: string) {
  if (isOfFailureType(errorMessage)) {
    return mapFailureTypes(errorMessage);
  }

  return "";
}

function isOfFailureType(error: string): error is FailureType {
  return FAILURE_TYPES.includes(error as FailureType);
}

function mapFailureTypes(failureType: FailureType) {
  // eslint-disable-next-line @typescript-eslint/switch-exhaustiveness-check
  switch (failureType) {
    case "DOKSYS_BESTILLING_ADDRESS_NOT_FOUND":
    case "EXSTREAM_BESTILLING_ADRESSE_MANGLER": {
      return "Fant ikke adresse på brukeren.";
    }
    case "DOKSYS_BESTILLING_PERSON_NOT_FOUND": {
      return "Fant ikke bruker.";
    }
    case "EXSTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT": {
      return "Mangler i data-grunnlaget til brukeren for å kunne bestille brevet.";
    }
    case "DOKSYS_BESTILLING_UNAUTHORIZED": {
      return "Har ikke tilgang til å produsere brev på brukeren.";
    }
    case "DOKSYS_REDIGERING_IKKE_TILGANG": {
      return "Prøver å redigere et brev hvor man ikke har tilgang til bruker/enhet.";
    }
    case "DOKSYS_REDIGERING_UNDER_REDIGERING": {
      return "Brevet er allerede åpent for redigering. Det kan hende at du eller noen andre allerede har åpnet brevet.";
    }
    default: {
      return "Teknisk feil.";
    }
  }
}
