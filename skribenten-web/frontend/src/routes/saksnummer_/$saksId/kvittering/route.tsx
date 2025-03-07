import { css } from "@emotion/react";
import { Button, Heading, VStack } from "@navikt/ds-react";
import { createFileRoute, useNavigate } from "@tanstack/react-router";

import KvitterteBrev from "~/components/kvitterteBrev/KvitterteBrev";
import type { KvittertBrev } from "~/components/kvitterteBrev/KvitterteBrevUtils";
import { toKvittertBrev } from "~/components/kvitterteBrev/KvitterteBrevUtils";

import { useSendBrevAttesteringContext } from "./-components/SendBrevTilAttesteringResultatContext";
import { useSendtBrevResultatContext } from "./-components/SendtBrevResultatContext";

export const Route = createFileRoute("/saksnummer/$saksId/kvittering")({
  component: Kvittering,
});

function Kvittering() {
  const { saksId } = Route.useParams();
  const navigate = useNavigate({ from: Route.fullPath });
  const ferdigstillBrevContext = useSendtBrevResultatContext();
  const brevTilAttesteringContext = useSendBrevAttesteringContext();

  const kvitterteBrev: KvittertBrev[] = [
    ...ferdigstillBrevContext.resultat.map((resultat) =>
      toKvittertBrev({
        status: resultat.status,
        context: "sendBrev",
        brevFørHandling: resultat.brevInfo,
        bestillBrevResponse: resultat.status === "success" ? resultat.response : null,
        attesterResponse: null,
      }),
    ),
    ...brevTilAttesteringContext.resultat.map((resultat) =>
      toKvittertBrev({
        status: resultat.status,
        context: "attestering",
        brevFørHandling: resultat.brevInfo,
        bestillBrevResponse: null,
        attesterResponse: resultat.status === "success" ? resultat.response : null,
      }),
    ),
  ];

  return (
    <div
      css={css`
        display: grid;
        flex-grow: 1;
        grid-template-columns: 40% 1% 40%;
        gap: 3rem;
        padding: var(--a-spacing-5) 0;
        background: var(--a-white);
      `}
    >
      <VStack
        css={css`
          justify-self: flex-end;
        `}
        gap="4"
      >
        <Heading size="medium">Hva vil du gjøre nå?</Heading>
        <Button
          css={css`
            width: fit-content;
          `}
          onClick={() => navigate({ to: "/saksnummer" })}
          size="small"
          type="button"
          variant="secondary"
        >
          Åpne annen sak
        </Button>
        <Button
          css={css`
            width: fit-content;
          `}
          onClick={() => navigate({ to: "/saksnummer/$saksId/brevvelger", params: { saksId } })}
          size="small"
          type="button"
          variant="secondary"
        >
          Lage nytt brev på denne saken
        </Button>
        <Button
          css={css`
            width: fit-content;
          `}
          onClick={() => navigate({ to: "/saksnummer/$saksId/brevbehandler", params: { saksId } })}
          size="small"
          type="button"
          variant="secondary"
        >
          Gå til brevbehandler
        </Button>
      </VStack>
      <div
        // This is a vertical line
        css={css`
          background: var(--a-gray-200);
          width: 1px;
        `}
      ></div>
      <KvitterteBrev kvitterteBrev={kvitterteBrev} sakId={saksId} />
    </div>
  );
}
