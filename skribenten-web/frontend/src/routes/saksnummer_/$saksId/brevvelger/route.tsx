import type { SerializedStyles } from "@emotion/react";
import { css } from "@emotion/react";
import { Accordion, Alert, BodyShort, Button, Heading, HStack, Search, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { Outlet, useNavigate, useParams } from "@tanstack/react-router";
import { groupBy, partition, sortBy } from "lodash";
import { useState } from "react";

import { hentAlleBrevForSak } from "~/api/sak-api-endpoints";
import { getFavoritter } from "~/api/skribenten-api-endpoints";
import { BrevbakerIcon, DoksysIcon, ExstreamIcon } from "~/assets/icons";
import { ApiError } from "~/components/ApiError";
import type { LetterMetadata } from "~/types/apiTypes";
import { BrevSystem } from "~/types/apiTypes";
import { erBrevKladdEllerUnderRedigering } from "~/utils/brevUtils";
import { formatStringDate } from "~/utils/dateUtils";

export const Route = createFileRoute("/saksnummer/$saksId/brevvelger")({
  loaderDeps: ({ search: { vedtaksId } }) => ({ includeVedtak: !!vedtaksId }),
  loader: async ({ context: { queryClient, getSakContextQueryOptions } }) => {
    const sakContext = await queryClient.ensureQueryData(getSakContextQueryOptions);
    return { saksId: sakContext.sak.saksId, letterTemplates: sakContext.brevMetadata };
  },
  errorComponent: ({ error }) => <ApiError error={error} title="Klarte ikke hente brevmaler for saken." />,
  component: BrevvelgerPage,
});

export function BrevvelgerPage() {
  const { saksId, letterTemplates } = Route.useLoaderData();

  return (
    <div
      css={css`
        display: flex;
        flex: 1;
        justify-content: center;

        > :first-of-type {
          background: white;
          min-width: 432px;
          max-width: 720px;
          flex: 1;
          border-left: 1px solid var(--a-gray-200);
          border-right: 1px solid var(--a-gray-200);
          padding: var(--a-spacing-6);
        }

        > :nth-of-type(2) {
          background: white;
          min-width: 336px;
          max-width: 388px;
          border-right: 1px solid var(--a-gray-200);
          padding: var(--a-spacing-4);
          flex: 1;
        }
      `}
    >
      <Brevmaler letterTemplates={letterTemplates ?? []} saksId={saksId} />
      <Outlet />
    </div>
  );
}

function Brevmaler({ saksId, letterTemplates }: { saksId: number; letterTemplates: LetterMetadata[] }) {
  const navigate = useNavigate({ from: "/saksnummer/$saksId/brevvelger" });

  //dette funker som vist https://tanstack.com/router/latest/docs/framework/react/guide/path-params#path-params-outside-of-routes
  const { templateId, brevId } = useParams({ strict: false }) as { templateId?: string; brevId?: string };
  const [searchTerm, setSearchTerm] = useState("");

  const favoritter = useQuery(getFavoritter).data ?? [];

  const brevmalerMatchingSearchTerm = sortBy(
    letterTemplates.filter((template) => template.name.toLowerCase().includes(searchTerm.toLowerCase())),
    (template) => template.name,
  );

  const matchingFavoritter = brevmalerMatchingSearchTerm.filter(({ id }) => favoritter.includes(id));
  const [eblanketter, brevmaler] = partition(
    brevmalerMatchingSearchTerm,
    (brevmal) => brevmal.dokumentkategoriCode === "E_BLANKETT",
  );

  const groupedBrevmaler = groupBy(brevmaler, (brevmal) => brevmal.brevkategori ?? "Annet");

  const brevmalerGroupedByType: Record<string, LetterMetadata[]> = {
    ...(matchingFavoritter.length > 0 ? { Favoritter: matchingFavoritter } : {}),
    ...groupedBrevmaler,
    ...(eblanketter.length > 0 ? { "E-blanketter": eblanketter } : {}),
  };

  const sortedCategoryKeys = [
    matchingFavoritter.length > 0 ? ["Favoritter"] : [],
    sortBy(Object.keys(groupedBrevmaler), (type) => type),
    eblanketter.length > 0 ? ["E-blanketter"] : [],
  ].flat();

  const alleSaksbrevQuery = useQuery({
    queryKey: hentAlleBrevForSak.queryKey(saksId.toString()),
    queryFn: () => hentAlleBrevForSak.queryFn(saksId.toString()),
  });

  return (
    <VStack gap="6">
      <Heading level="1" size="small">
        Brevmeny
      </Heading>
      <Search
        data-cy="brevmal-search"
        hideLabel={false}
        label="Søk etter brevmal"
        onChange={(value) => setSearchTerm(value)}
        size="small"
        value={searchTerm}
        variant="simple"
      />
      <Accordion
        css={css`
          max-height: calc(100vh - var(--header-height) - var(--breadcrumbs-height) - 180px);
          overflow-y: scroll;

          .navds-accordion__content {
            padding: 0;
          }
        `}
        headingSize="xsmall"
        indent={false}
        size="small"
      >
        {alleSaksbrevQuery.isSuccess && alleSaksbrevQuery.data.some(erBrevKladdEllerUnderRedigering) && (
          <Accordion.Item defaultOpen>
            <Accordion.Header
              css={css`
                flex-direction: row-reverse;
                justify-content: space-between;
              `}
            >
              <HStack gap="2">Kladder</HStack>
            </Accordion.Header>
            <Accordion.Content>
              <div
                css={css`
                  display: flex;
                  flex-direction: column;
                `}
              >
                {alleSaksbrevQuery.data.filter(erBrevKladdEllerUnderRedigering).map((brev) => (
                  <BrevmalButton
                    description={`Opprettet ${formatStringDate(brev.opprettet)}`}
                    extraStyles={
                      brev.id.toString() === brevId
                        ? css`
                            color: var(--a-text-on-action);
                            background-color: var(--a-surface-action-selected-hover);
                          `
                        : undefined
                    }
                    key={brev.id}
                    onClick={() => {
                      navigate({
                        to: "/saksnummer/$saksId/brevvelger/kladd/$brevId",
                        params: { saksId: saksId.toString(), brevId: brev.id.toString() },
                        search: (s) => s,
                      });
                    }}
                    title={
                      <HStack align={"center"} gap="2">
                        <BrevSystemIcon
                          brevsystem={letterTemplates.find((template) => template.id === brev.brevkode)?.brevsystem}
                        />
                        {brev.brevtittel}
                      </HStack>
                    }
                  />
                ))}
              </div>
            </Accordion.Content>
          </Accordion.Item>
        )}

        {Object.keys(brevmalerGroupedByType).length === 0 && (
          <Alert data-cy="ingen-treff-alert" size="small" variant="info">
            Ingen treff
          </Alert>
        )}

        {sortedCategoryKeys.map((type) => {
          return (
            <Accordion.Item
              data-cy="category-item"
              defaultOpen={type === "Favoritter"}
              key={type}
              open={searchTerm.length > 0 ? true : undefined}
            >
              <Accordion.Header
                css={css`
                  flex-direction: row-reverse;
                  justify-content: space-between;
                `}
              >
                {type}
              </Accordion.Header>
              <Accordion.Content>
                <div
                  css={css`
                    display: flex;
                    flex-direction: column;
                  `}
                >
                  {brevmalerGroupedByType[type].map((template) => (
                    <BrevmalButton
                      extraStyles={
                        template.id === templateId
                          ? css`
                              color: var(--a-text-on-action);
                              background-color: var(--a-surface-action-selected-hover);
                            `
                          : undefined
                      }
                      key={template.id}
                      onClick={() => {
                        navigate({
                          to: "/saksnummer/$saksId/brevvelger/$templateId",
                          params: { templateId: template.id },
                          search: (s) => s,
                        });
                      }}
                      title={
                        <HStack align="center" gap="2">
                          <BrevSystemIcon brevsystem={template.brevsystem} /> <BodyShort>{template.name}</BodyShort>
                        </HStack>
                      }
                    />
                  ))}
                </div>
              </Accordion.Content>
            </Accordion.Item>
          );
        })}
      </Accordion>
    </VStack>
  );
}

const BrevSystemIcon = (props: { brevsystem?: BrevSystem }) => {
  switch (props.brevsystem) {
    case BrevSystem.Exstream: {
      return <ExstreamIcon />;
    }
    case BrevSystem.DokSys: {
      return <DoksysIcon />;
    }
    case BrevSystem.Brevbaker: {
      return <BrevbakerIcon />;
    }
    case undefined: {
      return null;
    }
  }
};

const BrevmalButton = (props: {
  onClick: () => void;
  title: React.ReactNode;
  extraStyles?: SerializedStyles;
  description?: string;
}) => {
  return (
    <Button
      css={css(
        css`
          color: black;
          justify-content: flex-start;
          padding: var(--a-spacing-2) var(--a-spacing-3);
          border-radius: 0;

          span {
            font-weight: var(--a-font-weight-regular);
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }

          > :first-child {
            width: 100%;
          }
        `,
        props.extraStyles,
      )}
      data-cy="brevmal-button"
      onClick={props.onClick}
      variant="tertiary"
    >
      <HStack justify={"space-between"}>
        <div>{props.title}</div>
        {props.description && (
          <BodyShort
            css={css`
              color: var(--a-gray-600);
            `}
          >
            {props.description}
          </BodyShort>
        )}
      </HStack>
    </Button>
  );
};
