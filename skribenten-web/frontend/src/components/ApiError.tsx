import { css } from "@emotion/react";
import { Alert, CopyButton, Heading, HStack } from "@navikt/ds-react";
import { ErrorComponent } from "@tanstack/react-router";
import { AxiosError } from "axios";

export function ApiError({ error, title }: { error: unknown; title: string }) {
  if (error instanceof AxiosError) {
    const correlationId = error.response?.headers["x-request-id"];
    return (
      <Alert
        css={css`
          margin-top: var(--a-spacing-4);
          width: fit-content;
          align-self: center;
        `}
        size="small"
        variant="error"
      >
        <Heading level="2" size="small" spacing>
          {title}
        </Heading>
        <div>
          {correlationId && (
            <>
              <HStack align="center" gap="0">
                <span>Hvis det skjer igjen, rapporter feilen og oppgi følgende ID:</span>
                <CopyButton
                  copyText={correlationId}
                  css={css`
                    border: 1px solid black;

                    span:not(.navds-copybutton__icon) {
                      white-space: nowrap;
                      overflow: hidden;
                      text-overflow: ellipsis;
                    }
                  `}
                  text={correlationId}
                />
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
