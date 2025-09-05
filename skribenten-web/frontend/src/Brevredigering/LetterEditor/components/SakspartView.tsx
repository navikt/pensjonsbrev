import type { SerializedStyles } from "@emotion/react";
import { css } from "@emotion/react";
import { intlFormat } from "date-fns";

import type { SpraakKode } from "~/types/apiTypes";
import type { Sakspart } from "~/types/brevbakerTypes";

export const SakspartView = ({
  sakspart,
  spraak,
  wrapperStyles,
}: {
  sakspart: Sakspart;
  spraak: SpraakKode;
  wrapperStyles?: SerializedStyles;
}) => (
  <div
    css={css`
      display: grid;
      grid-template-columns: minmax(10rem, max-content) 1fr min-content;
      gap: var(--a-spacing-1) var(--a-spacing-2);
      opacity: 0.5;
      font-size: 16.5px;
      line-height: var(--a-font-line-height-heading-xsmall);
      ${wrapperStyles}
    `}
  >
    {sakspart.vergeNavn && (
      <>
        <span>Verge:</span>
        <span>{sakspart.vergeNavn}</span>
        <span />
      </>
    )}
    {sakspart.vergeNavn ? <span>Saken gjelder:</span> : <span>Navn:</span>}
    <span>{sakspart.gjelderNavn}</span>
    <span />
    <span>Fødselsnummer:</span>
    <span>{sakspart.gjelderFoedselsnummer}</span>
    <span />
    <span>Saksnummer:</span>
    <span>{sakspart.saksnummer}</span>
    <span css={css({ alignSelf: "end", textWrap: "nowrap" })}>
      {intlFormat(
        sakspart.dokumentDato,
        { year: "numeric", month: "long", day: "numeric" },
        { locale: spraak === "EN" ? "en-GB" : spraak },
      )}
    </span>
  </div>
);
