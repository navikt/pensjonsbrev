import { css } from "@emotion/react";
import { Button, Heading, VStack } from "@navikt/ds-react";
import { createFileRoute, useNavigate } from "@tanstack/react-router";

import KvitterteBrev from "~/components/kvitterteBrev/KvitterteBrev";
import type { KvittertBrev } from "~/components/kvitterteBrev/KvitterteBrevUtils";
import { toKvittertBrev } from "~/components/kvitterteBrev/KvitterteBrevUtils";

// import { useAttesteringResultat } from "./-components/AttesteringResultatContext";
import { useSendtBrevResultatContext } from "./-components/SendtBrevResultatContext";

export const Route = createFileRoute("/saksnummer_/$saksId/kvittering")({
  component: Kvittering,
});

function Kvittering() {
  const { saksId } = Route.useParams();
  const { enhetsId, vedtaksId } = Route.useSearch();
  const navigate = useNavigate({ from: Route.fullPath });
  const ferdigstillBrevContext = useSendtBrevResultatContext();
  // const brevTilAttesteringContext = useAttesteringResultat();

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
    // ...brevTilAttesteringContext.resultat.map((resultat) =>
    //   toKvittertBrev({
    //     status: resultat.status,
    //     context: "attestering",
    //     brevFørHandling: resultat.brevInfo,
    //     bestillBrevResponse: null,
    //     attesterResponse: null,
    //   }),
    // ),
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
      <KvitterteBrev kvitterteBrev={kvitterteBrev} sakId={saksId} />
      <div
        // This is a vertical line
        css={css`
          background: var(--a-gray-200);
          width: 1px;
        `}
      ></div>
      <VStack
        css={css`
          justify-self: anchor-center;
        `}
        gap="4"
      >
        <Heading size="medium">Hva vil du gjøre nå?</Heading>
        <Button
          css={css`
            width: fit-content;
          `}
          onClick={() => navigate({ to: "/saksnummer", search: { enhetsId } })}
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
          css={css`
            width: fit-content;
          `}
          onClick={() =>
            navigate({ to: "/saksnummer/$saksId/brevbehandler", params: { saksId }, search: { enhetsId, vedtaksId } })
          }
          size="small"
          type="button"
          variant="secondary"
        >
          Gå til brevbehandler
        </Button>
      </VStack>
    </div>
  );
}
