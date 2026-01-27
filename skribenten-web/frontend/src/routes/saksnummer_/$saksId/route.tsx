import { css } from "@emotion/react";
import { FileIcon, ParagraphIcon } from "@navikt/aksel-icons";
import { BodyShort, BoxNew, CopyButton, HStack, Tag } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute, Outlet } from "@tanstack/react-router";
import { useMemo } from "react";
import { z } from "zod";

import {
  getBrukerStatus,
  getFavoritter,
  getKontaktAdresse,
  getPreferredLanguage,
  getSakContext,
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
    const getSakContextQueryOptions = getSakContext(saksId, vedtaksId);

    queryClient.prefetchQuery(getKontaktAdresse(saksId));
    queryClient.prefetchQuery(getFavoritter);
    queryClient.prefetchQuery(getPreferredLanguage(saksId));
    queryClient.prefetchQuery(getBrukerStatus(saksId));

    return await queryClient.ensureQueryData(getSakContextQueryOptions);
  },
  component: SakLayout,
  errorComponent: ({ error }) => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const { saksId } = Route.useParams();
    return <ApiError error={error} title={`Klarte ikke hente saksnummer ${saksId}`} />;
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
  const { data: brukerStatus } = useQuery(getBrukerStatus(sak.saksId.toString()));

  const dateOfBirth = useMemo(() => {
    if (!sak.foedselsdato) return undefined;
    const date = new Date(sak.foedselsdato);
    return Number.isNaN(date.getTime())
      ? undefined
      : date.toLocaleDateString("no-NO", { year: "numeric", month: "2-digit", day: "2-digit" });
  }, [sak.foedselsdato]);
  const dateOfDeath = useMemo(() => {
    if (!brukerStatus?.doedsfall) return undefined;
    const date = new Date(brukerStatus.doedsfall);
    return isNaN(date.valueOf())
      ? undefined
      : date.toLocaleDateString("no-NO", { year: "numeric", month: "2-digit", day: "2-digit" });
  }, [brukerStatus]);

  return (
    <BoxNew
      asChild
      background="default"
      borderColor="neutral-subtle"
      borderWidth="0 0 1 0"
      css={{ zIndex: 10 }}
      top="space-48"
    >
      <HStack
        align="center"
        css={css`
          p {
            display: flex;
            align-items: center;
          }

          p::after {
            content: "/";
            margin: 0 var(--ax-space-12);
          }

          p:last-child::after {
            content: none;
          }
        `}
        justify="space-between"
        paddingBlock="space-8"
        paddingInline="space-32"
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
          {brukerStatus?.erSkjermet && (
            <BodyShort>
              <Tag css={{ borderRadius: "var(--ax-radius-4)" }} icon={<FileIcon />} size="small" variant="neutral">
                Egen ansatt
              </Tag>
            </BodyShort>
          )}
          {brukerStatus?.vergemaal && (
            <BodyShort>
              <Tag css={{ borderRadius: "var(--ax-radius-4)" }} icon={<FileIcon />} size="small" variant="neutral">
                Vergemål
              </Tag>
            </BodyShort>
          )}
          {brukerStatus?.adressebeskyttelse && (
            <BodyShort>
              <Tag
                css={{ borderRadius: "var(--ax-radius-4)" }}
                icon={<ParagraphIcon />}
                size="small"
                variant="error-filled"
              >
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
      </HStack>
    </BoxNew>
  );
}

const splitFødselsnummer = (fødselsnummer: string) => {
  const fødselsdato = fødselsnummer.slice(0, 6);
  const personnummer = fødselsnummer.slice(6);

  return { fødselsdato, personnummer };
};
