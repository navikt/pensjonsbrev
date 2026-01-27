import { HGrid } from "@navikt/ds-react";
import { intlFormat } from "date-fns";

import { SpraakKode } from "~/types/apiTypes";
import type { Sakspart } from "~/types/brevbakerTypes";

export const SakspartView = ({ sakspart, spraak }: { sakspart: Sakspart; spraak: SpraakKode }) => {
  const dokumentDato = Date.parse(sakspart.dokumentDato);
  return (
    <HGrid
      columns="minmax(10rem, max-content) 1fr min-content"
      css={{
        opacity: "0.5",
        fontSize: "16px",
        lineHeight: "1.3125rem",
        marginBottom: "var(--ax-space-40)",
      }}
      gap="space-4 space-8"
    >
      {sakspart.annenMottakerNavn && (
        <>
          <span>Mottaker:</span>
          <span>{sakspart.annenMottakerNavn}</span>
          <span />
        </>
      )}
      {sakspart.annenMottakerNavn ? <span>Saken gjelder:</span> : <span>Navn:</span>}
      <span>{sakspart.gjelderNavn}</span>
      <span />
      <span>Fødselsnummer:</span>
      <span>{sakspart.gjelderFoedselsnummer}</span>
      <span />
      <span>Saksnummer:</span>
      <span>{sakspart.saksnummer}</span>
      <span css={{ alignSelf: "end", textWrap: "nowrap" }}>
        {!Number.isNaN(dokumentDato) &&
          intlFormat(
            dokumentDato,
            { year: "numeric", month: "long", day: "numeric" },
            { locale: spraak === SpraakKode.Engelsk ? "en-GB" : spraak },
          )}
      </span>
    </HGrid>
  );
};
