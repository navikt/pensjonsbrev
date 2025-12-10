import { BoxNew, Button, Heading, HGrid, HStack, VStack } from "@navikt/ds-react";
import { createFileRoute, useNavigate } from "@tanstack/react-router";

import { VerticalDivider } from "~/components/Divider";
import KvitterteBrev from "~/components/kvitterteBrev/KvitterteBrev";
import type { KvittertBrev } from "~/components/kvitterteBrev/KvitterteBrevUtils";
import { toKvittertBrev } from "~/components/kvitterteBrev/KvitterteBrevUtils";

import { useBrevInfoKlarTilAttestering } from "./-components/KlarTilAttesteringContext";
import { useSendtBrev } from "./-components/SendtBrevContext";

export const Route = createFileRoute("/saksnummer_/$saksId/kvittering")({
  component: Kvittering,
});

function Kvittering() {
  const { saksId } = Route.useParams();
  const { enhetsId, vedtaksId } = Route.useSearch();
  const navigate = useNavigate({ from: Route.fullPath });

  const { sendteBrev } = useSendtBrev();
  const { brevListKlarTilAttestering } = useBrevInfoKlarTilAttestering();

  const sendtBrevList: KvittertBrev[] = Object.values(sendteBrev).map((resultat) =>
    toKvittertBrev({
      status: resultat.status,
      context: "sendBrev",
      brevFørHandling: resultat.brevInfo,
      bestillBrevResponse: resultat.status === "success" ? resultat.response! : null,
      attesterResponse: null,
    }),
  );

  const attestList: KvittertBrev[] = brevListKlarTilAttestering.map((brev) =>
    toKvittertBrev({
      status: "success",
      context: "attestering",
      brevFørHandling: brev,
      bestillBrevResponse: null,
      attesterResponse: null,
    }),
  );

  const kvitterteBrev: KvittertBrev[] = [...sendtBrevList, ...attestList];

  return (
    <BoxNew
      asChild
      background="default"
      height="calc(var(--main-page-content-height) + 48px)"
      marginInline="auto"
      width="fit-content"
    >
      <HGrid columns="minmax(304px, 384px) 1px minmax(640px, 720px)">
        <BoxNew overflow="auto" paddingBlock="space-20" paddingInline="space-16">
          <KvitterteBrev kvitterteBrev={kvitterteBrev} sakId={saksId} />
        </BoxNew>
        <VerticalDivider />
        <HStack justify="center" overflow="auto" paddingBlock="space-20" paddingInline="0">
          <VStack gap="space-16">
            <Heading size="medium">Hva vil du gjøre nå?</Heading>
            <Button
              css={{ width: "fit-content" }}
              onClick={() => navigate({ to: "/saksnummer", search: { enhetsId } })}
              size="small"
              type="button"
              variant="secondary"
            >
              Åpne annen sak
            </Button>
            <Button
              css={{ width: "fit-content" }}
              onClick={() =>
                navigate({ to: "/saksnummer/$saksId/brevvelger", params: { saksId }, search: { enhetsId, vedtaksId } })
              }
              size="small"
              type="button"
              variant="secondary"
            >
              Lage nytt brev på denne saken
            </Button>
            <Button
              css={{ width: "fit-content" }}
              onClick={() =>
                navigate({
                  to: "/saksnummer/$saksId/brevbehandler",
                  params: { saksId },
                  search: { enhetsId, vedtaksId },
                })
              }
              size="small"
              type="button"
              variant="secondary"
            >
              Gå til brevbehandler
            </Button>
          </VStack>
        </HStack>
      </HGrid>
    </BoxNew>
  );
}
