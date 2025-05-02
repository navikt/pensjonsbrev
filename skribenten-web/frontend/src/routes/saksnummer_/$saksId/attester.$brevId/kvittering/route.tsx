import { css } from "@emotion/react";
import { BodyShort, Box, Button, Heading, VStack } from "@navikt/ds-react";
import { createFileRoute, Link } from "@tanstack/react-router";
import { useEffect } from "react";

import KvitterteBrev from "~/components/kvitterteBrev/KvitterteBrev";
import { toKvittertBrev } from "~/components/kvitterteBrev/KvitterteBrevUtils";

import { useSendtBrev } from "../../kvittering/-components/SendtBrevContext";

export const Route = createFileRoute("/saksnummer_/$saksId/attester/$brevId/kvittering")({
  component: () => <Kvittering />,
});

const Kvittering = () => {
  const isProd = import.meta.env.PROD;
  const { saksId, brevId } = Route.useParams();
  const { vedtaksId, enhetsId } = Route.useSearch();

  const { sendteBrev } = useSendtBrev();

  const brukeroversiktQ2Url = `https://pensjon-psak-q2.dev.adeo.no/psak/bruker/brukeroversikt.jsf?sakId=${saksId}`;
  const dokumentoversiktQ2Url = `https://pensjon-psak-q2.dev.adeo.no/psak/dokument/saksoversikt.jsf?sakId=${saksId}`;
  const brukeroversiktProdUrl = `https://pensjon-psak.nais.adeo.no/psak/bruker/brukeroversikt.jsf?sakId=${saksId}`;
  const dokumentoversiktProdUrl = `https://pensjon-psak.nais.adeo.no/psak/dokument/saksoversikt.jsf?sakId=${saksId}`;

  const brukeroversiktUrl = isProd ? brukeroversiktProdUrl : brukeroversiktQ2Url;
  const dokumentoversiktUrl = isProd ? dokumentoversiktProdUrl : dokumentoversiktQ2Url;

  const sendteBrevLista = Object.values(sendteBrev).map((brevResult) =>
    toKvittertBrev({
      status: brevResult.status,
      context: "sendBrev",
      brevFørHandling: brevResult.brevInfo,
      bestillBrevResponse: brevResult.status === "success" ? brevResult.response! : null,
      attesterResponse: null,
    }),
  );

  if (sendteBrevLista.length === 0) {
    return (
      <Box
        background="bg-default"
        css={css`
          display: flex;
          flex-direction: column;
          flex: 1;
          align-items: center;
          padding-top: var(--a-spacing-8);
        `}
      >
        <VStack gap="2">
          <Heading size="medium">Hva vil du gjøre nå?</Heading>
          <VStack align={"start"} gap="3">
            <ButtonLink as={"a"} href={brukeroversiktUrl}>
              Gå til brukeroversikt
            </ButtonLink>
            <ButtonLink as={"a"} href={dokumentoversiktUrl}>
              Gå til dokumentoversikt
            </ButtonLink>
          </VStack>
        </VStack>
        <BodyShort>Ingen informasjon om brevsending</BodyShort>
        <Link
          params={{ saksId, brevId }}
          search={{ vedtaksId, enhetsId }}
          to={"/saksnummer/$saksId/attester/$brevId/forhandsvisning"}
        >
          Tilbake til forhåndsvisning
        </Link>
      </Box>
    );
  }

  return (
    <Box
      background="bg-default"
      css={css`
        display: flex;
        flex: 1;
        align-self: center;
        gap: 5rem;
        justify-content: center;
        padding: var(--a-spacing-8) var(--a-spacing-24);
      `}
    >
      <VStack gap="5">
        <Heading size="medium">Kvittering</Heading>
        <KvitterteBrev kvitterteBrev={sendteBrevLista} sakId={saksId} />
      </VStack>
      <VStack gap="2">
        <Heading size="medium">Hva vil du gjøre nå?</Heading>
        <VStack align={"start"} gap="3">
          <ButtonLink as={"a"} href={brukeroversiktUrl}>
            Gå til brukeroversikt
          </ButtonLink>
          <ButtonLink as={"a"} href={dokumentoversiktUrl}>
            Gå til dokumentoversikt
          </ButtonLink>
        </VStack>
      </VStack>
    </Box>
  );
};

const ButtonLink = (props: React.ComponentPropsWithRef<typeof Button>) => {
  return <Button size="small" variant="secondary" {...props} as="a" />;
};
