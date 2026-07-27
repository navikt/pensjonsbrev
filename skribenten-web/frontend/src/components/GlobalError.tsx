import { FilesIcon } from "@navikt/aksel-icons";
import {
  BodyLong,
  BodyShort,
  Box,
  CopyButton,
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
        <VStack gap="space-6" maxWidth="512px" width="100%">
          <VStack gap="space-4">
            <Heading level="1" size="xlarge" css={{ color: "var(--ax-text-danger-subtle)" }}>
              {title}
            </Heading>
            <BodyLong size="large">
              Noe gikk galt. Prøv igjen litt senere. Hvis problemet vedvarer, kan du melde fra
              til oss.
            </BodyLong>
          </VStack>

          <VStack gap="space-4" css={{ marginTop: "var(--ax-space-40)" }}>
            <BodyShort weight="semibold">Vil du melde fra om dette?</BodyShort>
            <BodyLong>
              Kopier ID-en nedenfor og{" "}
              <Link href="https://jira.adeo.no/plugins/servlet/desk/portal/541" target="_blank">
                meld fra i Porten
              </Link>
              .
            </BodyLong>
            <Box asChild background="default" borderColor="neutral" borderRadius="4" borderWidth="1" padding="space-4">
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
          </VStack>

          <VStack gap="space-2">
            <BodyShort textColor="subtle">{formatStringDateWithTime(new Date().toISOString())}</BodyShort>
            <ReadMore header="Tekniske detaljer" size="small">
              <BodyShort truncate>{correlationId}</BodyShort>
            </ReadMore>
          </VStack>
        </VStack>
      </HStack>
    </Box>
  );
}
