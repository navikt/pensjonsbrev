import { css } from "@emotion/react";
import { TextField } from "@navikt/ds-react";

import type { Signatur } from "~/types/brevbakerTypes";

const Saksbehandler = ({ rolleTekst, navn }: { rolleTekst: string; navn?: string }) => {
  if (!navn) {
    return <></>;
  }
  return (
    <div>
      <TextField
        css={css`
          margin-bottom: var(--a-spacing-1);
        `}
        data-cy="brev-editor-saksbehandler-input"
        hideLabel
        label=""
        readOnly
        tabIndex={-1}
        value={navn}
      />
      <i>{rolleTekst}</i>
    </div>
  );
};

export const SignaturView = ({ signatur }: { signatur: Signatur }) => (
  <div
    css={css`
      background: var(--a-gray-50);
      padding: var(--a-spacing-3);
      display: flex;
      flex-direction: column;
      gap: var(--a-spacing-6);
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
