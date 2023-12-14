import { css } from "@emotion/react";

import type { Sakspart } from "~/types/brevbakerTypes";

export const SakspartView = ({ sakspart }: { sakspart: Sakspart }) => (
  <div
    css={css`
      display: grid;
      grid-template-columns: max-content 1fr min-content;
      column-gap: var(--a-spacing-2);
      row-gap: var(--a-spacing-1);
      border-radius: 4px;
      background: var(--Global-Gray-50, #f2f3f5);
      padding: 0 var(--a-spacing-3);
    `}
  >
    <span>Saken gjelder:</span>
    <span>{sakspart.gjelderNavn}</span>
    <span />
    <span>Fødselsnummer:</span>
    <span>{sakspart.gjelderFoedselsnummer}</span>
    <span />
    <span>Saksnummer:</span>
    <span>{sakspart.saksnummer}</span>
    <span>{sakspart.dokumentDato}</span>
  </div>
);
