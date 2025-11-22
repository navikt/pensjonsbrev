import type { SerializedStyles } from "@emotion/react";
import { css } from "@emotion/react";

import type { Signatur } from "~/types/brevbakerTypes";

const Saksbehandler = ({ navn }: { navn?: string }) => {
  if (!navn) {
    return <></>;
  }
  return (
    <div
      css={css`
        flex: 1;
        margin-bottom: var(--a-spacing-6);
      `}
      data-cy="brev-editor-saksbehandler"
    >
      {navn}
    </div>
  );
};

export const SignaturView = ({ signatur, wrapperStyles }: { signatur: Signatur; wrapperStyles?: SerializedStyles }) => (
  <div
    css={css`
      opacity: 0.5;
      display: flex;
      flex-direction: column;
      font-size: 16.5px;
      line-height: var(--a-font-line-height-heading-xsmall);
      ${wrapperStyles}
    `}
  >
    <div>{signatur.hilsenTekst}</div>
    <div
      css={css`
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        gap: var(--a-spacing-8);
      `}
    >
      <Saksbehandler navn={signatur.attesterendeSaksbehandlerNavn} />
      <Saksbehandler navn={signatur.saksbehandlerNavn} />
    </div>
    <div>{signatur.navAvsenderEnhet}</div>
  </div>
);
