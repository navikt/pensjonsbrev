import { css } from "@emotion/react";
import { BodyShort, CopyButton, HStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { Outlet } from "@tanstack/react-router";

import { getFavoritter, getKontaktAdresse, getNavn, getPreferredLanguage } from "~/api/skribenten-api-endpoints";
import { getSakContext } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { SakDto } from "~/types/apiTypes";
import { SAK_TYPE_TO_TEXT } from "~/types/nameMappings";
import { queryFold } from "~/utils/tanstackUtils";

import { MottakerContextProvider } from "./brevvelger/-components/endreMottaker/MottakerContext";
import { FerdigstillResultatContextProvider } from "./kvittering/-components/FerdigstillResultatContext";

export const Route = createFileRoute("/saksnummer/$saksId")({
  beforeLoad: ({ params: { saksId }, search: { vedtaksId } }) => {
    const getSakContextQueryOptions = {
      ...getSakContext,
      queryKey: getSakContext.queryKey(saksId, vedtaksId),
      queryFn: () => getSakContext.queryFn(saksId, vedtaksId),
    };

    return { getSakContextQueryOptions };
  },
  loader: async ({ context: { queryClient, getSakContextQueryOptions } }) => {
    const sakContext = await queryClient.ensureQueryData(getSakContextQueryOptions);

    // Adresse is a slow query that will be needed later, therefore we prefetch it here as early as possible.
    queryClient.prefetchQuery({
      queryKey: getKontaktAdresse.queryKey(sakContext.sak.saksId.toString()),
      queryFn: () => getKontaktAdresse.queryFn(sakContext.sak.saksId.toString()),
    });

    queryClient.prefetchQuery(getFavoritter);
    queryClient.prefetchQuery({
      queryKey: getPreferredLanguage.queryKey(sakContext.sak.saksId.toString()),
      queryFn: () => getPreferredLanguage.queryFn(sakContext.sak.saksId.toString()),
    });

    return sakContext;
  },
  errorComponent: ({ error }) => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const { saksId } = Route.useParams();
    return <ApiError error={error} title={`Klarte ikke hente saksnummer ${saksId}`} />;
  },
  component: SakLayout,
  validateSearch: (search: Record<string, unknown>): { vedtaksId?: string; enhetsId?: string } => ({
    vedtaksId: search.vedtaksId?.toString(),
    enhetsId: search.enhetsId?.toString(),
  }),
});

function SakLayout() {
  const sakContext = Route.useLoaderData();

  return (
    <FerdigstillResultatContextProvider>
      <MottakerContextProvider>
        {sakContext && <Subheader sak={sakContext.sak} />}
        <div className="page-margins">
          <Outlet />
        </div>
      </MottakerContextProvider>
    </FerdigstillResultatContextProvider>
  );
}

function Subheader({ sak }: { sak: SakDto }) {
  const { fødselsdato, personnummer } = splitFødselsnummer(sak.foedselsnr);
  const hentNavnQuery = useQuery({
    queryKey: getNavn.queryKey(sak?.foedselsnr as string),
    queryFn: () => getNavn.queryFn(sak?.saksId?.toString() as string),
    enabled: !!sak,
  });

  return (
    <div
      css={css`
        position: sticky;
        top: 48px;
        z-index: var(--a-z-index-focus);
      `}
    >
      <div
        css={css`
          display: flex;
          padding: var(--a-spacing-2) var(--a-spacing-8);
          align-items: center;
          justify-content: space-between;
          border-bottom: 1px solid var(--a-gray-200);
          background: var(--a-surface-default);

          p {
            display: flex;
            align-items: center;
          }

          p::after {
            content: "/";
            margin: 0 var(--a-spacing-3);
          }

          p:last-child::after {
            content: none;
          }
        `}
      >
        <HStack>
          <BodyShort size="small">
            {fødselsdato} {personnummer} <CopyButton copyText={sak.foedselsnr} size="small" variant="action" />
          </BodyShort>
          {queryFold({
            query: hentNavnQuery,
            initial: () => null,
            pending: () => <BodyShort size="small">Henter navn...</BodyShort>,
            error: () => <BodyShort size="small">Feil ved henting av navn</BodyShort>,
            success: (navn) => <BodyShort size="small">{navn}</BodyShort>,
          })}
        </HStack>
        <HStack>
          <BodyShort size="small">{SAK_TYPE_TO_TEXT[sak.sakType]}</BodyShort>
          <BodyShort size="small">
            {sak.saksId} <CopyButton copyText={sak.saksId.toString()} size="small" variant="action" />
          </BodyShort>
        </HStack>
      </div>
    </div>
  );
}

const splitFødselsnummer = (fødselsnummer: string) => {
  const fødselsdato = fødselsnummer.slice(0, 6);
  const personnummer = fødselsnummer.slice(6);

  return { fødselsdato, personnummer };
};
