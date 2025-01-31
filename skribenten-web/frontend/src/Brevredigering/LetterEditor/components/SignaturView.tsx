import type { SerializedStyles } from "@emotion/react";
import { css } from "@emotion/react";

import type { Signatur } from "~/types/brevbakerTypes";

const Saksbehandler = ({ rolleTekst, navn }: { rolleTekst: string; navn?: string }) => {
  if (!navn) {
    return <></>;
  }
  return (
    <div>
      <p
        css={css`
          margin-bottom: var(--a-spacing-1-alt);
        `}
        data-cy="brev-editor-saksbehandler"
      >
        {navn}
      </p>
      <i>{rolleTekst}</i>
    </div>
  );
};

export const SignaturView = ({ signatur, wrapperStyles }: { signatur: Signatur; wrapperStyles?: SerializedStyles }) => (
  <div
    css={css`
      opacity: 0.5;
      display: flex;
      flex-direction: column;
      gap: var(--a-spacing-6);
      font-size: 16.5px;
      line-height: var(--a-font-line-height-heading-xsmall);
      ${wrapperStyles}
    `}
  >
    <span>{signatur.hilsenTekst}</span>
    <div
      css={css`
        display: grid;
        grid-template-columns: 50% 50%;
        gap: var(--a-spacing-8);
      `}
    >
      <Saksbehandler navn={signatur.saksbehandlerNavn} rolleTekst={signatur.saksbehandlerRolleTekst} />
      <Saksbehandler navn={signatur.attesterendeSaksbehandlerNavn} rolleTekst={signatur.saksbehandlerRolleTekst} />
    </div>
    <div>{signatur.navAvsenderEnhet}</div>
  </div>
);
