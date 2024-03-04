import { css } from "@emotion/react";
import { FilesIcon } from "@navikt/aksel-icons";
import { Alert, CopyButton, Heading, Link, VStack } from "@navikt/ds-react";
import { ErrorComponent } from "@tanstack/react-router";
import { AxiosError } from "axios";

import type { FailureType } from "~/types/apiTypes";
import { FAILURE_TYPES } from "~/types/apiTypes";

const PORTEN_URL = "https://jira.adeo.no/plugins/servlet/desk/portal/541";
export function ApiError({ error, title }: { error: unknown; title: string }) {
  if (error instanceof AxiosError) {
    const correlationId = error.response?.headers["x-request-id"] ?? "asd";
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
          <VStack gap="1">
            <div>
              <span>{mapErrorMessage(error.message)}</span>
              <span>
                Hvis det skjer igjen, kopier ID nedenfor og{" "}
                <Link href={PORTEN_URL} target="_blank">
                  meld feil i Porten
                </Link>
              </span>
            </div>
            <CopyButton
              copyText={correlationId}
              css={css`
                border-radius: 4px;
                background: white;
                width: fit-content;
                box-shadow: inset 0 0 0 2px var(--a-border-default);
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

  // If the error is not an axios error, then there is a programming error. Fallback to default Router Error
  return <ErrorComponent error={error} />;
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
