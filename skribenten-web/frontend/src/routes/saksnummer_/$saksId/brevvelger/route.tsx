import type { SerializedStyles } from "@emotion/react";
import { css } from "@emotion/react";
import { Accordion, Alert, BodyShort, Button, Heading, HStack, Label, Search, VStack } from "@navikt/ds-react";
import type { UseQueryResult } from "@tanstack/react-query";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { useNavigate } from "@tanstack/react-router";
import { groupBy, partition, sortBy } from "lodash";
import { useState } from "react";

import { hentAlleBrevForSak } from "~/api/sak-api-endpoints";
import { getFavoritter } from "~/api/skribenten-api-endpoints";
import { BrevbakerIcon, DoksysIcon, ExstreamIcon } from "~/assets/icons";
import { ApiError } from "~/components/ApiError";
import type { LetterMetadata } from "~/types/apiTypes";
import { BrevSystem } from "~/types/apiTypes";
import type { BrevInfo } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";
import { erBrevKladdEllerUnderRedigering, erBrevKlar } from "~/utils/brevUtils";
import { formatStringDate } from "~/utils/dateUtils";

import { BrevmalBrevbakerKladd } from "./-components/BrevmalBrevbakerKladd";
import { TemplateLoader } from "./-components/TemplateLoader";

export const Route = createFileRoute("/saksnummer/$saksId/brevvelger")({
  validateSearch: (
    search: Record<string, unknown>,
  ): { idTSSEkstern?: string; brevId?: string; templateId?: string; enhetsId?: string } => ({
    idTSSEkstern: search.idTSSEkstern?.toString(),
    brevId: search.brevId?.toString(),
    templateId: search.templateId?.toString(),
    enhetsId: search.enhetsId?.toString(),
  }),
  loaderDeps: ({ search: { vedtaksId } }) => ({ includeVedtak: !!vedtaksId }),
  loader: async ({ context: { queryClient, getSakContextQueryOptions } }) => {
    const sakContext = await queryClient.ensureQueryData(getSakContextQueryOptions);
    return { saksId: sakContext.sak.saksId, letterTemplates: sakContext.brevMetadata };
  },
  errorComponent: ({ error }) => <ApiError error={error} title="Klarte ikke hente brevmaler for saken." />,
  component: BrevvelgerPage,
});

export function BrevvelgerPage() {
  const { brevId, templateId } = Route.useSearch();
  const navigate = useNavigate({ from: Route.fullPath });
  const { saksId, letterTemplates } = Route.useLoaderData();
  const [nesteButton, setNesteButton] = useState<Nullable<React.ReactNode>>(null);

  const alleSaksbrevQuery = useQuery({
    queryKey: hentAlleBrevForSak.queryKey(saksId.toString()),
    queryFn: () => hentAlleBrevForSak.queryFn(saksId.toString()),
  });

  const harBrevKlarTilSending = alleSaksbrevQuery.isSuccess && alleSaksbrevQuery.data.some(erBrevKlar);
  const antallBrevKlarTilSending = harBrevKlarTilSending ? alleSaksbrevQuery.data.filter(erBrevKlar).length : 0;

  return (
    <div
      css={css`
        display: flex;
        flex-direction: column;
        align-self: center;
        background-color: white;
      `}
    >
      <div
        css={css`
          display: flex;
          background-color: white;
          height: var(--main-page-content-height);
        `}
      >
        <div
          css={css`
            width: 720px;
            border-left: 1px solid var(--a-gray-200);
            border-right: 1px solid var(--a-gray-200);
            padding: var(--a-spacing-6);
            overflow-y: scroll;
          `}
        >
          <Brevmaler alleSaksbrev={alleSaksbrevQuery} letterTemplates={letterTemplates} />
        </div>
        {(templateId || brevId) && (
          <div
            css={css`
              display: flex;
              width: 389px;
              border-right: 1px solid var(--a-gray-200);
              padding: var(--a-spacing-6);
            `}
          >
            {templateId && (
              <TemplateLoader
                letterTemplate={letterTemplates.find((template) => template.id === templateId)!}
                saksId={saksId}
                setNestebutton={setNesteButton}
                templateId={templateId}
              />
            )}
            {brevId && (
              <BrevmalBrevbakerKladd
                brevId={brevId}
                letterTemplates={letterTemplates}
                saksId={saksId.toString()}
                setNestebutton={setNesteButton}
              />
            )}
          </div>
        )}
      </div>

      <HStack
        css={css`
          padding: 8px 12px;
          border-top: 1px solid var(--a-gray-200);
        `}
        justify={"end"}
      >
        <Button
          onClick={() => {
            navigate({
              to: "/saksnummer/$saksId/brevbehandler",
              params: { saksId: saksId.toString() },
            });
          }}
          size="small"
          type="button"
          variant="tertiary"
        >
          {harBrevKlarTilSending
            ? `Du har ${antallBrevKlarTilSending} brev klar til sending. Gå til brevbehandler`
            : "Gå til brevbehandler"}
        </Button>
        {nesteButton}
      </HStack>
    </div>
  );
}

function Brevmaler({
  letterTemplates,
  alleSaksbrev,
}: {
  letterTemplates: LetterMetadata[];
  alleSaksbrev: UseQueryResult<BrevInfo[], Error>;
}) {
  const navigate = useNavigate({ from: "/saksnummer/$saksId/brevvelger" });
  const { templateId, brevId } = Route.useSearch();
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
          overflow-y: scroll;

          .navds-accordion__content {
            padding: 0;
          }
        `}
        headingSize="xsmall"
        indent={false}
        size="small"
      >
        {alleSaksbrev.isSuccess && alleSaksbrev.data.some(erBrevKladdEllerUnderRedigering) && (
          <Accordion.Item defaultOpen>
            <Accordion.Header
              css={css`
                flex-direction: row-reverse;
                justify-content: space-between;
              `}
            >
              <HStack gap="2">
                <Label size="small">Kladder</Label>
              </HStack>
            </Accordion.Header>
            <Accordion.Content>
              <div
                css={css`
                  display: flex;
                  flex-direction: column;
                `}
              >
                {alleSaksbrev.data.filter(erBrevKladdEllerUnderRedigering).map((brev) => (
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
                        to: "/saksnummer/$saksId/brevvelger",
                        search: (s) => ({ ...s, brevId: brev.id.toString(), templateId: undefined }),
                      });
                    }}
                    title={
                      <HStack align={"center"} gap="2">
                        <BrevSystemIcon
                          brevsystem={letterTemplates.find((template) => template.id === brev.brevkode)?.brevsystem}
                        />
                        <BodyShort size="small">{brev.brevtittel}</BodyShort>
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
                <Label size="small">{type}</Label>
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
                          to: "/saksnummer/$saksId/brevvelger",
                          search: (s) => ({ ...s, templateId: template.id, brevId: undefined }),
                        });
                      }}
                      title={
                        <HStack align="center" gap="2">
                          <BrevSystemIcon brevsystem={template.brevsystem} />{" "}
                          <BodyShort size="small">{template.name}</BodyShort>
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

          > :first-of-type {
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
        {props.title}
        {props.description && (
          <BodyShort
            css={css`
              color: var(--a-gray-600);
            `}
            size="small"
          >
            {props.description}
          </BodyShort>
        )}
      </HStack>
    </Button>
  );
};
