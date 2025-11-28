import { css } from "@emotion/react";
import { FileIcon, ParagraphIcon } from "@navikt/aksel-icons";
import { BodyShort, CopyButton, HStack, Tag } from "@navikt/ds-react";
import { createFileRoute, Outlet } from "@tanstack/react-router";
import { useMemo } from "react";
import { z } from "zod";

import {
  getFavoritterQuery,
  getKontaktAdresseQuery,
  getPreferredLanguageQuery,
  getSakContextQuery,
} from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { SakContextDto } from "~/types/apiTypes";
import { SAK_TYPE_TO_TEXT } from "~/types/nameMappings";
import { humanizeName } from "~/utils/stringUtils";

import { MottakerContextProvider } from "./brevvelger/-components/endreMottaker/MottakerContext";
import { BrevInfoKlarTilAttesteringProvider } from "./kvittering/-components/KlarTilAttesteringContext";
import { SendtBrevProvider } from "./kvittering/-components/SendtBrevContext";

export const baseSearchSchema = z.object({
  vedtaksId: z.coerce.string().optional(),
  enhetsId: z.coerce.string().optional(),
});
type BaseSearchParamsSchema = z.infer<typeof baseSearchSchema>;

export const Route = createFileRoute("/saksnummer_/$saksId")({
  validateSearch: (search): BaseSearchParamsSchema => baseSearchSchema.parse(search),

  loaderDeps: ({ search }) => ({ vedtaksId: search.vedtaksId }),
  loader: async ({ context: { queryClient }, params: { saksId }, deps: { vedtaksId } }) => {
    const getSakContextQueryOptions = getSakContextQuery(saksId, vedtaksId);

    queryClient.prefetchQuery(getKontaktAdresseQuery(saksId));
    queryClient.prefetchQuery(getFavoritterQuery);
    queryClient.prefetchQuery(getPreferredLanguageQuery(saksId));

    return await queryClient.ensureQueryData(getSakContextQueryOptions);
  },
  component: SakLayout,
  errorComponent: ({ error }) => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const { saksId } = Route.useParams();
    return (
      <div
        css={css`
          display: flex;
          margin: var(--a-spacing-4);
          justify-content: space-around;
        `}
      >
        <ApiError error={error} title={`Klarte ikke hente saksnummer ${saksId}`} />
      </div>
    );
  },
});

function SakLayout() {
  const sakContext = Route.useLoaderData();
  return (
    <BrevInfoKlarTilAttesteringProvider>
      <SendtBrevProvider>
        <MottakerContextProvider>
          {sakContext && <Subheader sakContext={sakContext} />}
          <div className="page-margins">
            <Outlet />
          </div>
        </MottakerContextProvider>
      </SendtBrevProvider>
    </BrevInfoKlarTilAttesteringProvider>
  );
}

function Subheader({ sakContext }: { sakContext: SakContextDto }) {
  const sak = sakContext.sak;
  const { fødselsdato, personnummer } = splitFødselsnummer(sak.foedselsnr);
  const dateOfBirth = useMemo(() => {
    if (!sak.foedselsdato) return undefined;
    const date = new Date(sak.foedselsdato);
    return isNaN(date.valueOf())
      ? undefined
      : date.toLocaleDateString("no-NO", { year: "numeric", month: "2-digit", day: "2-digit" });
  }, [sak.foedselsdato]);
  const dateOfDeath = useMemo(() => {
    if (!sakContext.doedsfall) return undefined;
    const date = new Date(sakContext.doedsfall);
    return isNaN(date.valueOf())
      ? undefined
      : date.toLocaleDateString("no-NO", { year: "numeric", month: "2-digit", day: "2-digit" });
  }, [sakContext.doedsfall]);

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
          <BodyShort size="small">
            {sak.navn.etternavn}, {humanizeName(sak.navn.fornavn)} {humanizeName(sak.navn.mellomnavn ?? "")}
          </BodyShort>
          {/* Vil ikke vises for ugyldig dato, f.eks. dummy pnr med ugyldig månedsledd */}
          {dateOfBirth && <BodyShort size="small">Født: {dateOfBirth}</BodyShort>}
          {dateOfDeath && <BodyShort size="small">Død: {dateOfDeath}</BodyShort>}
          {sakContext.erSkjermet && (
            <BodyShort>
              <Tag css={css({ borderRadius: "4px" })} icon={<FileIcon />} size="small" variant="neutral">
                Egen ansatt
              </Tag>
            </BodyShort>
          )}
          {sakContext.vergemaal && (
            <BodyShort>
              <Tag css={css({ borderRadius: "4px" })} icon={<FileIcon />} size="small" variant="neutral">
                Vergemål
              </Tag>
            </BodyShort>
          )}
          {sakContext.adressebeskyttelse && (
            <BodyShort>
              <Tag css={css({ borderRadius: "4px" })} icon={<ParagraphIcon />} size="small" variant="error-filled">
                Diskresjon
              </Tag>
            </BodyShort>
          )}
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
