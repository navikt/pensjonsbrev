import type { SerializedStyles } from "@emotion/react";
import { css } from "@emotion/react";

import type { Sakspart } from "~/types/brevbakerTypes";

export const SakspartView = ({ sakspart, wrapperStyles }: { sakspart: Sakspart; wrapperStyles?: SerializedStyles }) => (
  <div
    css={css`
      display: grid;
      grid-template-columns: max-content 1fr min-content;
      gap: var(--a-spacing-1) var(--a-spacing-2);
      opacity: 0.5;
      font-size: 16.5px;
      line-height: var(--a-font-line-height-heading-xsmall);
      ${wrapperStyles}
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
  </div>
);
