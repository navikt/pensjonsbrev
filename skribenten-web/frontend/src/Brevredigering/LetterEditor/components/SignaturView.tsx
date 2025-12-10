import type { SerializedStyles } from "@emotion/react";
import { css } from "@emotion/react";
import { Box, HStack, VStack } from "@navikt/ds-react";

import type { Signatur } from "~/types/brevbakerTypes";

const Saksbehandler = ({ navn }: { navn?: string }) => {
  if (!navn) {
    return <></>;
  }
  return (
    <Box data-cy="brev-editor-saksbehandler" flexGrow="1" marginBlock="0 space-24">
      {navn}
    </Box>
  );
};

export const SignaturView = ({ signatur, wrapperStyles }: { signatur: Signatur; wrapperStyles?: SerializedStyles }) => (
  <VStack
    css={css`
      opacity: 0.5;
      font-size: 16.5px;
      line-height: var(--ax-font-line-height-large);
      ${wrapperStyles}
    `}
  >
    <div>{signatur.hilsenTekst}</div>
    <HStack gap="space-32" justify="space-between">
      <Saksbehandler navn={signatur.attesterendeSaksbehandlerNavn} />
      <Saksbehandler navn={signatur.saksbehandlerNavn} />
    </HStack>
    <div>{signatur.navAvsenderEnhet}</div>
  </VStack>
);
