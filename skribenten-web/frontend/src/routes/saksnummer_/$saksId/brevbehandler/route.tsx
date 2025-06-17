import { css } from "@emotion/react";
import { PlusIcon } from "@navikt/aksel-icons";
import { Button, Heading, HStack, Label, Skeleton, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { useRef, useState } from "react";
import { z } from "zod";

import { hentAlleBrevForSak } from "~/api/sak-api-endpoints";
import { getNavnQuery, getSakContextQuery } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";

import BrevbehandlerMeny from "./-components/BrevbehandlerMeny";
import BrevForhåndsvisning from "./-components/BrevForhåndsvisning";
import { FerdigstillOgSendBrevButton, FerdigstillOgSendBrevModal } from "./-components/FerdigstillBrev";

const brevbehandlerSearchSchema = z.object({
  brevId: z.coerce.number().optional(),
});
type BrevbehandlerSearch = z.infer<typeof brevbehandlerSearchSchema>;

export const Route = createFileRoute("/saksnummer_/$saksId/brevbehandler")({
  validateSearch: (search): BrevbehandlerSearch => brevbehandlerSearchSchema.parse(search),
  loaderDeps: ({ search }) => ({ vedtaksId: search.vedtaksId }),
  loader: async ({ context, params: { saksId }, deps: { vedtaksId } }) => {
    const getSakContextQueryOptions = getSakContextQuery(saksId, vedtaksId);
    context.queryClient.prefetchQuery(getNavnQuery(saksId));
    return await context.queryClient.ensureQueryData(getSakContextQueryOptions);
  },
  component: Brevbehandler,
});

function Brevbehandler() {
  const { saksId } = Route.useParams();
  const { brevId, enhetsId, vedtaksId } = Route.useSearch();
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
            padding: var(--a-spacing-6);
            border-right: 1px solid var(--a-gray-200);
            height: var(--main-page-content-height);
            overflow-y: auto;
          `}
          gap="3"
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
          {alleBrevForSak.isSuccess && <BrevbehandlerMeny brevInfo={alleBrevForSak.data} saksId={saksId} />}
        </VStack>

        <div ref={brevPdfContainerReference}>{brevId && <BrevForhåndsvisning brevId={brevId} saksId={saksId} />}</div>

        <HStack
          css={css`
            padding: 8px 12px;
            grid-area: footer;
            border-top: 1px solid var(--a-gray-200);
          `}
          justify="space-between"
        >
          <Button
            onClick={() =>
              navigate({
                to: "/saksnummer/$saksId/brevvelger",
                params: { saksId: saksId },
                search: { enhetsId, vedtaksId },
              })
            }
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
