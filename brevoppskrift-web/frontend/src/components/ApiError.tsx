import { css } from "@emotion/react";
import { Alert, CopyButton, Heading, HStack } from "@navikt/ds-react";
import { ErrorComponent } from "@tanstack/react-router";
import { AxiosError } from "axios";

export function ApiError({ error, text }: { error: unknown; text: string }) {
  if (error instanceof AxiosError) {
    const correlationId = error.response?.headers["x-request-id"];
    return (
      <Alert
        css={css`
          margin-top: var(--ax-space-16);
          width: fit-content;
          align-self: center;
        `}
        size="small"
        variant="error"
      >
        <Heading level="2" size="small" spacing>
          Oops
        </Heading>
        <div>{text}</div>
        <div>
          {correlationId && (
            <>
              <HStack align="center" gap="space-0">
                <span>
                  Hvis problemet vedvarer rapporter feil og oppgi følgende id: <b>{correlationId}</b>
                </span>
                <CopyButton copyText={correlationId} />
              </HStack>
            </>
          )}
        </div>
      </Alert>
    );
  }

  // If the error is not an axios error, then there is a programming error. Fallback to default Router Error
  return <ErrorComponent error={error} />;
}
