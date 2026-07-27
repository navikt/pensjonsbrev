import { FilesIcon } from "@navikt/aksel-icons";
import {
  BodyLong,
  BodyShort,
  Box,
  CopyButton,
  Detail,
  Heading,
  HGrid,
  HStack,
  Link,
  ReadMore,
  VStack,
} from "@navikt/ds-react";
import { AxiosError } from "axios";
import { useEffect } from "react";

import { formatStringDateWithTime } from "~/utils/dateUtils";
import { logError } from "~/utils/logger";

export function GlobalError({ error, title }: { error: unknown; title: string }) {
  useEffect(() => {
    if (error) {
      console.error(error);
      const originalStatus = error instanceof AxiosError ? error.status : undefined;
      logError(error, originalStatus).catch(() => console.error("Unable to log error message"));
    }
  }, [error]);

  const correlationId = error instanceof AxiosError ? error.response?.headers["x-request-id"] : undefined;

  return (
    <Box background="default" minHeight="100vh" paddingBlock="space-24">
      <HStack align="center" justify="center">
        <VStack gap="space-20" maxWidth="512px" width="100%">
          <VStack gap="space-4">
            <Heading css={{ color: "var(--ax-text-danger-subtle)" }} level="1" size="xlarge">
              {title}
            </Heading>
            <BodyLong size="large">
              Noe gikk galt. Prøv igjen litt senere. Hvis problemet vedvarer, kan du melde fra til oss.
            </BodyLong>
          </VStack>

          <VStack gap="space-4" paddingBlock="space-20 space-0">
            <BodyShort weight="semibold">Vil du melde fra om dette?</BodyShort>
            <BodyLong>
              Kopier ID-en nedenfor og{" "}
              <Link href="https://jira.adeo.no/plugins/servlet/desk/portal/541" rel="noopener noreferrer" target="_blank">
                meld fra i Porten
              </Link>
              .
            </BodyLong>
            {correlationId ? (
              <Box
                asChild
                background="default"
                css={{
                  borderRadius: "var(--ax-radius-4)",
                  border: "1px solid var(--ax-border-neutral-subtle)",
                  padding: "var(--ax-space-12)",
                }}
              >
                <HGrid align="center" columns="auto max-content" paddingInline="space-6 space-0">
                  <BodyShort truncate>{correlationId}</BodyShort>
                  <CopyButton
                    copyText={correlationId}
                    data-color="accent"
                    icon={<FilesIcon />}
                    size="small"
                    text="Kopier"
                  />
                </HGrid>
              </Box>
            ) : (
              <BodyShort>Ingen sporings-ID tilgjengelig for denne feilen.</BodyShort>
            )}
          </VStack>

          {correlationId && (
            <ReadMore header="Tekniske detaljer" size="small">
              <BodyShort truncate>{correlationId}</BodyShort>
            </ReadMore>
          )}

          <Detail css={{ color: "var(--ax-text-neutral-subtle)" }}>
            {formatStringDateWithTime(new Date().toISOString())}
          </Detail>
        </VStack>
      </HStack>
    </Box>
  );
}
