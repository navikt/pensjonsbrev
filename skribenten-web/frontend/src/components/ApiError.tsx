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
          margin-top: var(--a-spacing-4);
          width: fit-content;
          align-self: center;
        `}
        variant="error"
      >
        <Heading level="2" size="small" spacing>
          Oops
        </Heading>
        <div>{text}</div>
        <div>
          <span>Prøv på nytt om litt. Hvis problemet vedvarer rapporter feil og oppgi følgende id: </span>
          <HStack align="center" gap="0">
            {correlationId}
            <CopyButton copyText={correlationId} />
          </HStack>
        </div>
      </Alert>
    );
  }

  // If the error is not an axios error, then there is a programming error. Fallback to default Router Error
  return <ErrorComponent error={error} />;
}
