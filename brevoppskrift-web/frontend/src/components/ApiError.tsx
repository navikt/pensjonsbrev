import { CopyButton, GlobalAlert, HStack } from "@navikt/ds-react";
import { AxiosError } from "axios";

/**
 * An error that stops the whole page, e.g. a failed route loader. Errors from
 * the backend include the request id, so the user can report it.
 */
export function ApiError({ error, text }: { error: unknown; text: string }) {
  const correlationId = error instanceof AxiosError ? error.response?.headers["x-request-id"] : undefined;
  const details = error instanceof AxiosError ? undefined : error instanceof Error ? error.message : undefined;
  return (
    <GlobalAlert role="alert" size="small" status="error">
      <GlobalAlert.Header>
        <GlobalAlert.Title>{text}</GlobalAlert.Title>
      </GlobalAlert.Header>
      {correlationId || details ? (
        <GlobalAlert.Content>
          {correlationId ? (
            <HStack align="center" gap="space-0">
              <span>
                Hvis problemet vedvarer rapporter feil og oppgi følgende id: <b>{correlationId}</b>
              </span>
              <CopyButton copyText={correlationId} />
            </HStack>
          ) : (
            details
          )}
        </GlobalAlert.Content>
      ) : null}
    </GlobalAlert>
  );
}
