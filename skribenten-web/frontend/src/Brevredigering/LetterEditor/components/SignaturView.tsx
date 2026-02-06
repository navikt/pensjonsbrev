import type { SerializedStyles } from "@emotion/react";
import { css } from "@emotion/react";
import { BoxNew, HGrid, VStack } from "@navikt/ds-react";

import type { Signatur } from "~/types/brevbakerTypes";

const Saksbehandler = ({ navn }: { navn?: string }) =>
  navn ? <BoxNew data-cy="brev-editor-saksbehandler">{navn}</BoxNew> : <></>;

export const SignaturView = ({ signatur, wrapperStyles }: { signatur: Signatur; wrapperStyles?: SerializedStyles }) => (
  <VStack
    css={css`
      opacity: 0.5;
      font-size: 16.5px;
      line-height: var(--ax-font-line-height-large);
      ${wrapperStyles}
    `}
  >
    <span>{signatur.hilsenTekst}</span>
    <HGrid columns="1fr 1fr" gap="space-32" marginBlock="0 space-24">
      <Saksbehandler navn={signatur.attesterendeSaksbehandlerNavn} />
      <Saksbehandler navn={signatur.saksbehandlerNavn} />
    </HGrid>
    <div>{signatur.navAvsenderEnhet}</div>
  </VStack>
);
