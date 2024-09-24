import { css } from "@emotion/react";
import { Bleed, CopyButton } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { Outlet } from "@tanstack/react-router";

import { getFavoritter, getKontaktAdresse, getNavn, getPreferredLanguage } from "~/api/skribenten-api-endpoints";
import { getSakContext } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { SakDto } from "~/types/apiTypes";
import { SAK_TYPE_TO_TEXT } from "~/types/nameMappings";

import { MottakerContextProvider } from "./brevvelger/$templateId/-components/MottakerContext";
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
  component: SakBreadcrumbsPage,
  validateSearch: (search: Record<string, unknown>): { vedtaksId?: string; enhetsId?: string } => ({
    vedtaksId: search.vedtaksId?.toString(),
    enhetsId: search.enhetsId?.toString(),
  }),
});

function SakBreadcrumbsPage() {
  const sakContext = Route.useLoaderData();

  return (
    <FerdigstillResultatContextProvider>
      <MottakerContextProvider>
        <SakInfoBreadcrumbs sak={sakContext?.sak} />
        <Outlet />
      </MottakerContextProvider>
    </FerdigstillResultatContextProvider>
  );
}

function SakInfoBreadcrumbs({ sak }: { sak?: SakDto }) {
  const { data: navn } = useQuery({
    queryKey: getNavn.queryKey(sak?.foedselsnr as string),
    queryFn: () => getNavn.queryFn(sak?.saksId?.toString() as string),
    enabled: !!sak,
  });

  const { vedtaksId } = Route.useSearch();

  if (!sak) {
    return <></>;
  }

  return (
    <Bleed
      asChild
      css={css`
        position: sticky;
        top: 48px;
        z-index: var(--a-z-index-focus);
      `}
      marginInline="full"
    >
      <div
        css={css`
          display: flex;
          padding: var(--a-spacing-2) var(--a-spacing-8);
          align-items: center;
          border-bottom: 1px solid var(--a-gray-200);
          background: var(--a-surface-default);

          span {
            display: flex;
            align-items: center;
          }

          span::after {
            content: "/";
            margin: 0 var(--a-spacing-3);
          }

          span:last-child::after {
            content: none;
          }
        `}
      >
        <span>
          {sak.foedselsnr} <CopyButton copyText={sak.foedselsnr} size="small" />
        </span>
        <span>{navn ?? ""}</span>
        <span>Sakstype: {SAK_TYPE_TO_TEXT[sak.sakType]}</span>
        <span>
          Saksnummer: {sak.saksId} <CopyButton copyText={sak.saksId.toString()} size="small" />
        </span>
        {vedtaksId && <span>vedtaksId: {vedtaksId}</span>}
      </div>
    </Bleed>
  );
}
