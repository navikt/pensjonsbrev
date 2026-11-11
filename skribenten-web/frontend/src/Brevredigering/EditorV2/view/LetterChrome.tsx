import { Box, HGrid, VStack } from "@navikt/ds-react";
import { intlFormat } from "date-fns";

import { SpraakKode } from "~/types/apiTypes";
import { type SaksinformasjonV2, type SignaturV2 } from "~/types/brevV2";

export const SaksinformasjonView = ({ info, spraak }: { info: SaksinformasjonV2; spraak: SpraakKode }) => {
  const dato = Date.parse(info.dokumentDato);
  return (
    <HGrid
      columns="minmax(10rem, max-content) 1fr min-content"
      css={{
        color: "var(--ax-text-neutral-subtle)",
        fontSize: "16px",
        lineHeight: "1.3125rem",
        marginBottom: "var(--ax-space-40)",
      }}
      gap="space-4 space-8"
    >
      {info.annenMottakerNavn && (
        <>
          <span>Mottaker:</span>
          <span>{info.annenMottakerNavn}</span>
          <span />
        </>
      )}
      <span>{info.annenMottakerNavn ? "Saken gjelder:" : "Navn:"}</span>
      <span>{info.gjelderNavn}</span>
      <span />
      <span>Fødselsnummer:</span>
      <span>{info.gjelderPersonidentifikator}</span>
      <span />
      <span>Saksnummer:</span>
      <span>{info.saksnummer}</span>
      <span css={{ alignSelf: "end", textWrap: "nowrap" }}>
        {!Number.isNaN(dato) &&
          intlFormat(
            dato,
            { year: "numeric", month: "long", day: "numeric" },
            { locale: spraak === SpraakKode.Engelsk ? "en-GB" : spraak },
          )}
      </span>
    </HGrid>
  );
};

const Saksbehandler = ({ navn }: { navn?: string | null }) =>
  navn ? (
    <Box as="span" data-testid="brev-editor-saksbehandler">
      {navn}
    </Box>
  ) : null;

export const SignaturView = ({ signatur }: { signatur: SignaturV2 }) => (
  <VStack
    css={{
      fontSize: "16px",
      color: "var(--ax-text-neutral-subtle)",
      lineHeight: "var(--ax-font-line-height-large)",
      marginBottom: "48px",
    }}
  >
    <HGrid columns="1fr 1fr" gap="space-8 space-32" marginBlock="space-0 space-24">
      <Saksbehandler navn={signatur.attesterendeSaksbehandlerNavn} />
      <Saksbehandler navn={signatur.saksbehandlerNavn} />
    </HGrid>
    <span>{signatur.navAvsenderEnhet}</span>
  </VStack>
);
