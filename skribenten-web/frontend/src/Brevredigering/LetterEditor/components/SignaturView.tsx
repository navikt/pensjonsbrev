import { BoxNew, HGrid, VStack } from "@navikt/ds-react";

import type { Signatur } from "~/types/brevbakerTypes";

const Saksbehandler = ({ navn }: { navn?: string }) =>
  navn ? (
    <BoxNew as="span" data-cy="brev-editor-saksbehandler">
      {navn}
    </BoxNew>
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
    <BoxNew as="span" marginBlock="0 space-24">
      {signatur.hilsenTekst}
    </BoxNew>
    <HGrid columns="1fr 1fr" gap="space-8 space-32" marginBlock="0 space-24">
      <Saksbehandler navn={signatur.attesterendeSaksbehandlerNavn} />
      <Saksbehandler navn={signatur.saksbehandlerNavn} />
    </HGrid>
    <span>{signatur.navAvsenderEnhet}</span>
  </VStack>
);
