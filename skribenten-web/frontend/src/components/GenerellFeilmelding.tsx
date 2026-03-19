import { css } from "@emotion/react";
import { FilesIcon } from "@navikt/aksel-icons";
import { BodyShort, Box, CopyButton, Heading, HGrid, HStack, Link, VStack } from "@navikt/ds-react";
import { AxiosError } from "axios";
import { useEffect } from "react";

import feedbackUrl from "~/utils/feedbackUrl";
import { logError } from "~/utils/logger";

export function GenerellFeilmelding({ error }: { error: unknown }) {
  useEffect(() => {
    if (error) {
      console.error(error);
      const status = error instanceof AxiosError ? error.status : undefined;
      logError(error, status).catch(() => console.error("Unable to log error message"));
    }
  }, [error]);

  const correlationId = error instanceof AxiosError ? error.response?.headers["x-request-id"] : undefined;

  return (
    <HStack justify="center" paddingBlock="space-24" paddingInline="space-24">
      <VStack gap="space-4" maxWidth="512px" width="100%">
        <Heading css={css`color: var(--ax-text-danger-subtle);`} level="1" size="xlarge">
          Skribenten kunne ikke åpnes
        </Heading>
        <BodyShort>
          Dette skyldes en teknisk feil. Vennligst kopier feilmeldingen nedenfor og{" "}
          <Link href={feedbackUrl}>meld fra i Teams</Link>.
        </BodyShort>
        {correlationId && (
          <VStack gap="space-2" marginBlock="space-4 space-0">
            <BodyShort weight="semibold">Feilmelding</BodyShort>
            <Box asChild background="default" borderColor="neutral" borderRadius="4" borderWidth="1">
              <HGrid align="center" columns="auto max-content" paddingInline="space-8 space-0">
                <BodyShort truncate>{correlationId}</BodyShort>
                <Box asChild borderRadius="4">
                  <CopyButton
                    copyText={correlationId}
                    icon={<FilesIcon />}
                    iconPosition="left"
                    size="small"
                    text="Kopier"
                  />
                </Box>
              </HGrid>
            </Box>
          </VStack>
        )}
      </VStack>
    </HStack>
  );
}
