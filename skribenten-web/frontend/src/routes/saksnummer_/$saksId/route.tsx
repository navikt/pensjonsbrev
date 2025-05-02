import { css } from "@emotion/react";
import { BodyShort, CopyButton, HStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute, Outlet } from "@tanstack/react-router";

import {
  getFavoritterQuery,
  getKontaktAdresseQuery,
  getNavnQuery,
  getPreferredLanguageQuery,
  getSakContextQuery,
} from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { SakDto } from "~/types/apiTypes";
import { SAK_TYPE_TO_TEXT } from "~/types/nameMappings";
import { queryFold } from "~/utils/tanstackUtils";

import { MottakerContextProvider } from "./brevvelger/-components/endreMottaker/MottakerContext";
import { BrevInfoKlarTilAttesteringProvider } from "./kvittering/-components/KlarTilAttesteringContext";
import { SendtBrevProvider } from "./kvittering/-components/SendtBrevContext";

// Typer er deklarert som `: string | undefined` heller enn `?: string` for å kreve at disse parametrene overføres i lenker.
type SaksnummerSearch = { vedtaksId: string | undefined; enhetsId: string | undefined };

export const Route = createFileRoute("/saksnummer_/$saksId")({
  beforeLoad: ({ params: { saksId }, search: { vedtaksId } }) => ({
    getSakContextQueryOptions: getSakContextQuery(saksId, vedtaksId),
  }),
  loader: async ({ context: { queryClient, getSakContextQueryOptions }, params: { saksId } }) => {
    // Adresse is a slow query that will be needed later, therefore we prefetch it here as early as possible.
    queryClient.prefetchQuery(getKontaktAdresseQuery(saksId));
    queryClient.prefetchQuery(getFavoritterQuery);
    queryClient.prefetchQuery(getPreferredLanguageQuery(saksId));

    return await queryClient.ensureQueryData(getSakContextQueryOptions);
  },
  errorComponent: ({ error }) => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const { saksId } = Route.useParams();
    return <ApiError error={error} title={`Klarte ikke hente saksnummer ${saksId}`} />;
  },
  component: SakLayout,
  validateSearch: (search: Record<string, unknown>): SaksnummerSearch => ({
    vedtaksId: search.vedtaksId as string,
    enhetsId: search.enhetsId as string,
  }),
});

function SakLayout() {
  const sakContext = Route.useLoaderData();
  return (
    <BrevInfoKlarTilAttesteringProvider>
      <SendtBrevProvider>
        <MottakerContextProvider>
          {sakContext && <Subheader sak={sakContext.sak} />}
          <div className="page-margins">
            <Outlet />
          </div>
        </MottakerContextProvider>
      </SendtBrevProvider>
    </BrevInfoKlarTilAttesteringProvider>
  );
}

function Subheader({ sak }: { sak: SakDto }) {
  const { fødselsdato, personnummer } = splitFødselsnummer(sak.foedselsnr);
  const hentNavnQuery = useQuery(getNavnQuery(sak.saksId.toString()));

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
