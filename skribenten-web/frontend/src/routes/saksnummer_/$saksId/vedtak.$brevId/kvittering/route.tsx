import { css } from "@emotion/react";
import { BodyShort, Box, Button, Heading, VStack } from "@navikt/ds-react";
import { createFileRoute, Link } from "@tanstack/react-router";

import KvitterteBrev from "~/components/kvitterteBrev/KvitterteBrev";
import { toKvittertBrev } from "~/components/kvitterteBrev/KvitterteBrevUtils";

import { useSendtBrevResultatContext } from "../../kvittering/-components/SendtBrevResultatContext";

export const Route = createFileRoute("/saksnummer/$saksId/vedtak/$brevId/kvittering")({
  component: () => <Kvittering />,
});

const Kvittering = () => {
  const { resultat } = useSendtBrevResultatContext();
  const { saksId, brevId } = Route.useParams();

  const sendteBrev = resultat.map((resultat) =>
    toKvittertBrev({
      status: resultat.status,
      context: "sendBrev",
      brevFørHandling: resultat.brevInfo,
      bestillBrevResponse: resultat.status === "success" ? resultat.response : null,
      attesterResponse: null,
    }),
  );

  if (resultat.length === 0) {
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
        <BodyShort>Ingen informasjon om brevsending</BodyShort>
        <Link params={{ saksId, brevId }} to={"/saksnummer/$saksId/vedtak/$brevId/forhandsvisning"}>
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
        <KvitterteBrev kvitterteBrev={sendteBrev} sakId={saksId} />
      </VStack>
      <VStack gap="2">
        <Heading size="medium">Hva vil du gjøre nå?</Heading>
        <VStack align={"start"} gap="3">
          <ButtonLink as={"a"} href={`https://psak/dokument/saksoversikt.jsf?&sakId=${saksId}`}>
            Gå til brukeroversikt
          </ButtonLink>
          <ButtonLink as={"a"} href="https://www.nav.no">
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
