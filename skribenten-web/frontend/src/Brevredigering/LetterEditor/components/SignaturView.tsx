import { Box, HGrid, VStack } from "@navikt/ds-react";

import type { Signatur } from "~/types/brevbakerTypes";

const Saksbehandler = ({ navn }: { navn?: string }) =>
  navn ? (
    <Box as="span" data-cy="brev-editor-saksbehandler">
      {navn}
    </Box>
  ) : (
    <></>
  );

export const SignaturView = ({ signatur }: { signatur: Signatur }) => (
  <VStack
    css={{
      fontSize: "16px",
      color: "var(--ax-text-neutral-subtle)",
      lineHeight: "var(--ax-font-line-height-large)",
      marginBottom: "48px",
    }}
  >
    <Box as="span" marginBlock="space-0 space-24">
      {signatur.hilsenTekst}
    </Box>
    <HGrid columns="1fr 1fr" gap="space-8 space-32" marginBlock="space-0 space-24">
      <Saksbehandler navn={signatur.attesterendeSaksbehandlerNavn} />
      <Saksbehandler navn={signatur.saksbehandlerNavn} />
    </HGrid>
    <span>{signatur.navAvsenderEnhet}</span>
  </VStack>
);
