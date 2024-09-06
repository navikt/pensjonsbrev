import { css } from "@emotion/react";
import { PlusIcon } from "@navikt/aksel-icons";
import { Button, Heading, HStack, Label, Skeleton, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { useRef, useState } from "react";

import { hentAlleBrevForSak } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";

import BrevbehandlerMeny from "./-components/BrevbehandlerMeny";
import { BrevForhåndsvisning } from "./-components/BrevForhåndsvisning";
import { FerdigstillOgSendBrevButton, FerdigstillOgSendBrevModal } from "./-components/FerdigstillBrev";

export const Route = createFileRoute("/saksnummer/$saksId/brevbehandler")({
  component: Brevbehandler,
  validateSearch: (search: Record<string, unknown>): { brevId?: string } => ({
    brevId: search.brevId?.toString(),
  }),
});

function Brevbehandler() {
  const { saksId } = Route.useParams();
  const { brevId } = Route.useSearch();
  const navigate = useNavigate({ from: Route.fullPath });
  const [modalÅpen, setModalÅpen] = useState<boolean>(false);

  const brevPdfContainerReference = useRef<HTMLDivElement>(null);

  //vi henter data her istedenfor i route-loaderen fordi vi vil vise stort sett lik skjermbilde
  //Vi kan muligens gjøre en load i route-loader slik at brevene laster litt fortere
  const alleBrevForSak = useQuery({
    queryKey: hentAlleBrevForSak.queryKey(saksId),
    queryFn: () => hentAlleBrevForSak.queryFn(saksId),
  });

  return (
    <div
      css={css`
        display: flex;
        flex: 1;
      `}
    >
      {modalÅpen && <FerdigstillOgSendBrevModal onClose={() => setModalÅpen(false)} sakId={saksId} åpen={modalÅpen} />}
      <div
        css={css`
          display: grid;
          grid-template:
            "meny pdf" 1fr
            "footer footer" auto / 33% 66%;
          align-items: start;

          background-color: white;
          width: 1200px;
        `}
      >
        <VStack
          css={css`
            padding: var(--a-spacing-4);
            border-right: 1px solid var(--a-gray-200);
            height: var(--main-page-content-height);
            overflow-y: auto;
          `}
        >
          <Heading level="1" size="small">
            Brevbehandler
          </Heading>

          {alleBrevForSak.isPending && (
            <VStack
              css={css`
                padding: 1rem;
              `}
            >
              <Skeleton height={80} variant="rectangle" width="100%" />
            </VStack>
          )}
          {alleBrevForSak.isError && (
            <ApiError error={alleBrevForSak.error} title={"Klarte ikke å hente alle brev for saken"} />
          )}
          {alleBrevForSak.isSuccess && <BrevbehandlerMeny brevInfo={alleBrevForSak.data} sakId={saksId} />}
        </VStack>

        <div ref={brevPdfContainerReference}>{brevId && <BrevForhåndsvisning brevId={brevId} sakId={saksId} />}</div>

        <HStack
          css={css`
            padding: 8px 12px;
            grid-area: footer;
            border-top: 1px solid var(--a-gray-200);
          `}
          justify="space-between"
        >
          <Button
            onClick={() => {
              navigate({ to: "/saksnummer/$saksId/brevvelger", params: { saksId: saksId } });
            }}
            size="small"
            type="button"
            variant="secondary"
          >
            <HStack>
              <PlusIcon fontSize="1.5rem" title="pluss-ikon" />
              <Label>Lag nytt brev</Label>
            </HStack>
          </Button>
          {alleBrevForSak.isSuccess && (
            <FerdigstillOgSendBrevButton
              brevInfo={alleBrevForSak.data}
              sakId={saksId}
              valgtBrevId={brevId}
              åpneFerdigstillModal={() => setModalÅpen(true)}
            />
          )}
        </HStack>
      </div>
    </div>
  );
}
