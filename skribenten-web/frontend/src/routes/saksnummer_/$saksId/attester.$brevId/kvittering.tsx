import { BodyShort, Box, Button, Heading, HGrid, HStack, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute, Link } from "@tanstack/react-router";

import { getBaseUrls } from "~/api/bff-endpoints";
import { VerticalDivider } from "~/components/Divider";
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
        asChild
        background="default"
        height="calc(var(--main-page-content-height) + 48px)"
        marginInline="auto"
        width="fit-content"
      >
        <HGrid columns="minmax(304px, 384px) 1px minmax(640px, 720px)">
          <HStack justify="start" overflow="auto" paddingBlock="space-20" paddingInline="space-16">
            <VStack>
              <BodyShort>Ingen informasjon om brevsending</BodyShort>
              <Link
                params={{ saksId, brevId }}
                search={{ vedtaksId, enhetsId }}
                to="/saksnummer/$saksId/attester/$brevId/forhandsvisning"
              >
                Tilbake til forhåndsvisning
              </Link>
            </VStack>
          </HStack>
          <VerticalDivider />
          {urlReady && (
            <HStack justify="center" overflow="auto" paddingBlock="space-20" paddingInline="space-0">
              <VStack gap="space-8" marginInline="auto">
                <Heading size="medium">Hva vil du gjøre nå?</Heading>
                <NavButtons psak={psak} saksId={saksId} />
              </VStack>
            </HStack>
          )}
        </HGrid>
      </Box>
    );
  }

  return (
    <Box
      asChild
      background="default"
      height="calc(var(--main-page-content-height) + 48px)"
      marginInline="auto"
      width="fit-content"
    >
      <HGrid columns="minmax(304px, 384px) 1px minmax(640px, 720px)">
        <HStack justify="start" overflow="auto" paddingBlock="space-20" paddingInline="space-16">
          <VStack gap="space-20">
            <Heading size="medium">Kvittering</Heading>
            <KvitterteBrev kvitterteBrev={sendteBrevLista} sakId={saksId} />
          </VStack>
        </HStack>
        <VerticalDivider />
        {urlReady && (
          <HStack justify="center" overflow="auto" paddingBlock="space-20" paddingInline="space-0">
            <VStack gap="space-8" marginInline="auto">
              <Heading size="medium">Hva vil du gjøre nå?</Heading>
              <NavButtons psak={psak} saksId={saksId} />
            </VStack>
          </HStack>
        )}
      </HGrid>
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
    <VStack align="start" gap="space-12">
      <ButtonLink as="a" href={urls.bruker}>
        Gå til brukeroversikt
      </ButtonLink>
      <ButtonLink as="a" href={urls.dokument}>
        Gå til dokumentoversikt
      </ButtonLink>
    </VStack>
  );
};

const ButtonLink = (props: React.ComponentPropsWithRef<typeof Button>) => {
  return <Button size="small" variant="secondary" {...props} as="a" />;
};
