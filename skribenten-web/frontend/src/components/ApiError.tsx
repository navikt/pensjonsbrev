import { FilesIcon } from "@navikt/aksel-icons";
import { Alert, BodyShort, Box, BoxNew, CopyButton, Heading, HGrid, HStack, VStack } from "@navikt/ds-react";
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
        <HStack align="center" justify="center" marginBlock="space-12">
          <Alert
            css={{ width: "100%", maxWidth: "512px" }}
            data-cy="functional-error-alert"
            size="medium"
            variant="error"
          >
            <Heading level="2" size="small">
              {tittel ?? title}
            </Heading>
            {melding && <Box marginBlock="space-4 0">{melding}</Box>}
          </Alert>
        </HStack>
      );
    }

    const correlationId = error.response?.headers["x-request-id"];
    return (
      <HStack align="center" justify="center" marginBlock="space-12">
        <HStack maxWidth="512px" width="100%">
          <Alert size="small" variant="error">
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
                <BoxNew asChild background="default" borderColor="neutral" borderRadius="4" borderWidth="1">
                  <HGrid align="center" columns="auto max-content" paddingInline="space-8 0">
                    <BodyShort truncate>{correlationId + "aksdjflka sdlkja sdlkj alkjsad lfkaj"}</BodyShort>
                    <BoxNew asChild borderRadius="4">
                      <CopyButton
                        copyText={correlationId}
                        data-color="accent"
                        icon={<FilesIcon />}
                        size="small"
                        text="Kopier ID"
                      />
                    </BoxNew>
                  </HGrid>
                </BoxNew>
              </VStack>
            )}
          </Alert>
        </HStack>
      </HStack>
    );
  }

  // If the error is not an axios error, then there is a programming error. Fallback to simple handling
  return (
    <HStack align="center" justify="center" marginBlock="space-12">
      <Alert variant="error">En feil har oppstått og blitt logget. Prøv igjen litt senere.</Alert>
    </HStack>
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
