import { css } from "@emotion/react";
import { Bleed, CopyButton } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { Outlet } from "@tanstack/react-router";
import React from "react";

import { getFavoritter, getKontaktAdresse, getNavn, getPreferredLanguage } from "~/api/skribenten-api-endpoints";
import { getSak } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { SakDto } from "~/types/apiTypes";
import { SAK_TYPE_TO_TEXT } from "~/types/nameMappings";

export const Route = createFileRoute("/saksnummer/$sakId")({
  beforeLoad: ({ params: { sakId } }) => {
    const getSakQueryOptions = {
      queryKey: getSak.queryKey(sakId),
      queryFn: () => getSak.queryFn(sakId),
    };

    return { getSakQueryOptions };
  },
  loader: async ({ context: { queryClient, getSakQueryOptions } }) => {
    const sak = await queryClient.ensureQueryData(getSakQueryOptions);

    // Adresse is a slow query that will be needed later, therefore we prefetch it here as early as possible.
    queryClient.prefetchQuery({
      queryKey: getKontaktAdresse.queryKey(sak.sakId.toString()),
      queryFn: () => getKontaktAdresse.queryFn(sak.sakId.toString()),
    });

    queryClient.prefetchQuery(getFavoritter);
    queryClient.prefetchQuery({
      queryKey: getPreferredLanguage.queryKey(sak.sakId.toString()),
      queryFn: () => getPreferredLanguage.queryFn(sak.sakId.toString()),
    });

    return sak;
  },
  errorComponent: ({ error }) => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const { sakId } = Route.useParams();
    return <ApiError error={error} title={`Klarte ikke hente saksnummer ${sakId}`} />;
  },
  component: SakBreadcrumbsPage,
  validateSearch: (search: Record<string, unknown>): { vedtaksId?: string } => ({
    vedtaksId: search.vedtaksId?.toString(),
  }),
});

function SakBreadcrumbsPage() {
  const sak = Route.useLoaderData();

  return (
    <>
      <SakInfoBreadcrumbs sak={sak} />
      <Outlet />
    </>
  );
}

function SakInfoBreadcrumbs({ sak }: { sak?: SakDto }) {
  const { data: navn } = useQuery({
    queryKey: getNavn.queryKey(sak?.foedselsnr as string),
    queryFn: () => getNavn.queryFn(sak?.sakId?.toString() as string),
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
          border-bottom: 1px solid var(--a-gray-400);
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
          Saksnummer: {sak.sakId} <CopyButton copyText={sak.sakId.toString()} size="small" />
        </span>
        {vedtaksId && <span>vedtaksId: {vedtaksId}</span>}
      </div>
    </Bleed>
  );
}
