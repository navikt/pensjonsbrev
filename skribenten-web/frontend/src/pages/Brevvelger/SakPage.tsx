import { css } from "@emotion/react";
import { Bleed, CopyButton } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { useRouteContext } from "@tanstack/react-router";

import { getNavn } from "../../api/skribenten-api-endpoints";
import { sakRoute } from "../../tanStackRoutes";
import type { SakDto } from "../../types/apiTypes";

export function SakPage() {
  const { queryOptions } = useRouteContext({ from: sakRoute.id });
  const sak = useQuery(queryOptions);

  return (
    <>
      <SakInfoBreadcrumbs sak={sak.data} />
    </>
  );
}

function SakInfoBreadcrumbs({ sak }: { sak?: SakDto }) {
  const { data: navn } = useQuery({
    queryKey: getNavn.queryKey(sak?.foedselsnr as string),
    queryFn: () => getNavn.queryFn(sak?.foedselsnr as string),
    enabled: !!sak,
  });

  if (!sak) {
    return <></>;
  }

  return (
    <Bleed asChild marginInline="full">
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
        <span>Født: {sak.foedselsdato}</span>
        <span>Sakstype: {sak.sakType}</span>
        <span>
          Saksnummer: {sak.sakId} <CopyButton copyText={sak.sakId.toString()} size="small" />
        </span>
      </div>
    </Bleed>
  );
}
