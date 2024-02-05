import { css } from "@emotion/react";
import { Bleed, CopyButton } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { Outlet } from "@tanstack/react-router";
import React from "react";

import { getNavn } from "~/api/skribenten-api-endpoints";
import { getSak } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import { BrevvelgerTabOptions } from "~/routes/saksnummer_.$sakId.brevvelger";
import type { SakDto } from "~/types/apiTypes";
import { SAK_TYPE_TO_TEXT } from "~/types/nameMappings";
import { formatDateWithoutTimezone } from "~/utils/dateUtils";

export const Route = createFileRoute("/saksnummer/$sakId")({
  beforeLoad: ({ params: { sakId } }) => {
    const getSakQueryOptions = {
      queryKey: getSak.queryKey(sakId),
      queryFn: () => getSak.queryFn(sakId),
    };

    return { getSakQueryOptions };
  },
  loader: ({ context: { queryClient, getSakQueryOptions } }) => queryClient.ensureQueryData(getSakQueryOptions),
  errorComponent: ({ error }) => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const { sakId } = Route.useParams();
    return <ApiError error={error} text={`Klarte ikke hente saksnummer ${sakId}`} />;
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
    queryFn: () => getNavn.queryFn(sak?.foedselsnr as string),
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
          border-bottom: 1px solid var(--global-gray-400, #aab0ba);
          background: var(--surface-default, #fff);

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
        <span>Født: {formatDateWithoutTimezone(new Date(...sak.foedselsdato))}</span>
        <span>Sakstype: {SAK_TYPE_TO_TEXT[sak.sakType]}</span>
        <span>
          Saksnummer: {sak.sakId} <CopyButton copyText={sak.sakId.toString()} size="small" />
        </span>
        {vedtaksId && <span>VedtaksId: {vedtaksId}</span>}
      </div>
    </Bleed>
  );
}
