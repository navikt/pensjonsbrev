import { Box, Button, Heading, HGrid, HStack, VStack } from "@navikt/ds-react";
import { createFileRoute, useNavigate } from "@tanstack/react-router";

import { VerticalDivider } from "~/components/Divider";
import KvittertDelautomatiskBrev from "~/components/kvitterteBrev/KvittertDelautomatiskBrev";
import KvitterteBrev from "~/components/kvitterteBrev/KvitterteBrev";
import { type KvittertBrev, toKvittertBrev } from "~/components/kvitterteBrev/KvitterteBrevUtils";

import { useBrevInfoKlarTilAttestering } from "./-components/KlarTilAttesteringContext";
import { useSendtBrev } from "./-components/SendtBrevContext";

export const Route = createFileRoute("/saksnummer_/$saksId/kvittering")({
  component: Kvittering,
});

function Kvittering() {
  const { saksId } = Route.useParams();
  const { enhetsId, vedtaksId } = Route.useSearch();
  const navigate = useNavigate({ from: Route.fullPath });

  const { sendteBrev, delautomatiskBrevResults } = useSendtBrev();
  const { brevListKlarTilAttestering } = useBrevInfoKlarTilAttestering();

  const sendtBrevList: KvittertBrev[] = Object.values(sendteBrev).map((result) =>
    toKvittertBrev({
      status: result.status,
      context: "sendBrev",
      brevFørHandling: result.brevInfo,
      bestillBrevResponse: result.status === "success" ? result.response! : null,
      bestillBrevError: result.status === "error" ? (result.error ?? null) : null,
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
    <Box asChild background="default" height="100%" marginInline="auto" width="fit-content">
      <HGrid columns="minmax(304px, 384px) 1px minmax(640px, 720px)">
        <Box overflow="auto" paddingBlock="space-20" paddingInline="space-16">
          <VStack gap="space-8">
            {delautomatiskBrevResults.map((result, index) => (
              <KvittertDelautomatiskBrev
                initiallyOpen={index === delautomatiskBrevResults.length - 1}
                key={`delautomatisk-${index}`}
                result={result}
                saksId={saksId}
              />
            ))}
            <KvitterteBrev kvitterteBrev={kvitterteBrev} sakId={saksId} />
          </VStack>
        </Box>
        <VerticalDivider />
        <HStack justify="center" overflow="auto" paddingBlock="space-20" paddingInline="space-0">
          <VStack align="start" gap="space-16">
            <Heading size="medium">Hva vil du gjøre nå?</Heading>
            <Button
              onClick={() => navigate({ to: "/saksnummer", search: { enhetsId } })}
              size="small"
              type="button"
              variant="secondary"
            >
              Åpne annen sak
            </Button>
            <Button
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
    </Box>
  );
}
