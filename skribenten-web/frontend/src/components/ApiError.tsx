import { css } from "@emotion/react";
import { FilesIcon } from "@navikt/aksel-icons";
import { Alert, CopyButton, Heading, HStack, Link } from "@navikt/ds-react";
import { ErrorComponent } from "@tanstack/react-router";
import { AxiosError } from "axios";

const PORTEN_URL = "https://jira.adeo.no/plugins/servlet/desk/portal/541";
export function ApiError({ error, title }: { error: unknown; title: string }) {
  if (error instanceof AxiosError) {
    const correlationId = error.response?.headers["x-request-id"];
    return (
      <Alert
        css={css`
          align-self: center;
          width: 100%;
        `}
        size="small"
        variant="error"
      >
        <Heading level="2" size="small">
          {title}
        </Heading>
        <div>
          {correlationId && (
            <>
              <HStack align="center" gap="1">
                <span>
                  Hvis det skjer igjen, kopier ID nedenfor og{" "}
                  <Link href={PORTEN_URL} target="_blank">
                    meld feil i Porten
                  </Link>
                </span>
                <CopyButton
                  copyText={correlationId}
                  css={css`
                    border-radius: 4px;
                    border: 2px solid var(--a-border-default);
                  `}
                  icon={<FilesIcon />}
                  size="small"
                  text="Kopier ID"
                  variant="action"
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
