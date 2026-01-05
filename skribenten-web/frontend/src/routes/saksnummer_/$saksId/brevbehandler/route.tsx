import { PlusIcon } from "@navikt/aksel-icons";
import { BoxNew, Button, Heading, HGrid, HStack, Label, Skeleton, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { useState } from "react";
import { z } from "zod";

import { hentAlleBrevForSak } from "~/api/sak-api-endpoints";
import { getSakContextQuery } from "~/api/skribenten-api-endpoints";
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
    return await context.queryClient.ensureQueryData(getSakContextQueryOptions);
  },
  component: Brevbehandler,
});

function Brevbehandler() {
  const { saksId } = Route.useParams();
  const { brevId, enhetsId, vedtaksId } = Route.useSearch();
  const navigate = useNavigate({ from: Route.fullPath });
  const [modalÅpen, setModalÅpen] = useState<boolean>(false);

  //vi henter data her istedenfor i route-loaderen fordi vi vil vise stort sett lik skjermbilde
  //Vi kan muligens gjøre en load i route-loader slik at brevene laster litt fortere
  const alleBrevForSak = useQuery({
    queryKey: hentAlleBrevForSak.queryKey(saksId),
    queryFn: () => hentAlleBrevForSak.queryFn(saksId),
  });

  return (
    <BoxNew asChild background="default">
      <VStack height="calc(var(--main-page-content-height) + 48px)" marginInline="auto">
        {modalÅpen && (
          <FerdigstillOgSendBrevModal onClose={() => setModalÅpen(false)} sakId={saksId} åpen={modalÅpen} />
        )}
        <HGrid columns="minmax(304px, 384px) minmax(640px, 720px)" height="calc(100% - 48px)">
          {/* Meny */}
          <BoxNew
            asChild
            borderColor="neutral-subtle"
            borderWidth="0 1 0 0"
            height="100%"
            overflowY="auto"
            padding="space-24"
          >
            <VStack gap="space-12">
              <Heading level="1" size="small">
                Brevbehandler
              </Heading>

              {alleBrevForSak.isPending && (
                <VStack padding="space-16">
                  <Skeleton height={80} variant="rectangle" width="100%" />
                </VStack>
              )}
              {alleBrevForSak.isError && (
                <ApiError error={alleBrevForSak.error} title="Klarte ikke å hente alle brev for saken" />
              )}
              {alleBrevForSak.isSuccess && <BrevbehandlerMeny brevInfo={alleBrevForSak.data} saksId={saksId} />}
            </VStack>
          </BoxNew>

          {/* Pdf */}
          {brevId && <BrevForhåndsvisning brevId={brevId} saksId={saksId} />}
        </HGrid>

        {/* Footer */}
        <BoxNew
          asChild
          borderColor="neutral-subtle"
          borderWidth="1 0 0 0"
          paddingBlock="space-8"
          paddingInline="space-12"
        >
          <HStack gridColumn="footer" justify="space-between">
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
        </BoxNew>
      </VStack>
    </BoxNew>
  );
}
