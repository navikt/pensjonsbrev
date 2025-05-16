import { css } from "@emotion/react";
import { BodyShort, Box, Button, Heading, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute, Link } from "@tanstack/react-router";

import { getBaseUrls } from "~/api/bff-endpoints";
import KvitterteBrev from "~/components/kvitterteBrev/KvitterteBrev";
import { toKvittertBrev } from "~/components/kvitterteBrev/KvitterteBrevUtils";

import { useSendtBrev } from "../kvittering/-components/SendtBrevContext";

export const Route = createFileRoute("/saksnummer_/$saksId/attester/$brevId/kvittering")({
  component: () => <Kvittering />,
});

const Kvittering = () => {
  const { saksId, brevId } = Route.useParams();
  const { vedtaksId, enhetsId } = Route.useSearch();

  const { sendteBrev } = useSendtBrev();
  const { data: baseUrls, isLoading, error } = useQuery(getBaseUrls);

  const psak = baseUrls?.psak;
  const urlReady = !isLoading && !error && !!psak;

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
        {urlReady && (
          <VStack gap="2">
            <Heading size="medium">Hva vil du gjøre nå?</Heading>
            <NavButtons psak={psak} saksId={saksId} />
          </VStack>
        )}
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
      {urlReady && (
        <VStack gap="2">
          <Heading size="medium">Hva vil du gjøre nå?</Heading>
          <NavButtons psak={psak} saksId={saksId} />
        </VStack>
      )}
    </Box>
  );
};

const NavButtons = ({ psak, saksId }: { psak?: string; saksId: string }) => {
  if (!psak) return null;

  const urls = {
    bruker: `${psak}/psak/bruker/brukeroversikt.jsf?sakId=${saksId}`,
    dokument: `${psak}/psak/dokument/saksoversikt.jsf?sakId=${saksId}`,
  };

  return (
    <VStack align={"start"} gap="3">
      <ButtonLink as={"a"} href={urls.bruker}>
        Gå til brukeroversikt
      </ButtonLink>
      <ButtonLink as={"a"} href={urls.dokument}>
        Gå til dokumentoversikt
      </ButtonLink>
    </VStack>
  );
};

const ButtonLink = (props: React.ComponentPropsWithRef<typeof Button>) => {
  return <Button size="small" variant="secondary" {...props} as="a" />;
};
