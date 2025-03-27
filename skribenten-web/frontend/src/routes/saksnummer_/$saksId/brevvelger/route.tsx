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
import { getFavoritter, getSakContext } from "~/api/skribenten-api-endpoints";
import { BrevbakerIcon, DoksysIcon, ExstreamIcon } from "~/assets/icons";
import { ApiError } from "~/components/ApiError";
import type { LetterMetadata } from "~/types/apiTypes";
import { BrevSystem } from "~/types/apiTypes";
import type { BrevInfo } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";
import { erBrevKladdEllerUnderRedigering, erBrevKlar } from "~/utils/brevUtils";
import { formatStringDate } from "~/utils/dateUtils";

import BrevmalPanel from "./-components/BrevmalPanel";
import BrevvelgerFooter from "./-components/BrevvelgerFooter";

export const Route = createFileRoute("/saksnummer_/$saksId/brevvelger")({
  validateSearch: (
    search: Record<string, unknown>,
  ): { idTSSEkstern?: string; brevId?: string; templateId?: string; enhetsId?: string } => ({
    idTSSEkstern: search.idTSSEkstern?.toString(),
    brevId: search.brevId?.toString(),
    templateId: search.templateId?.toString(),
    enhetsId: search.enhetsId?.toString(),
  }),
  loaderDeps: ({ search: { vedtaksId } }) => ({ vedtaksId }),
  loader: async ({ context: { queryClient, getSakContextQueryOptions }, params: { saksId }, deps: { vedtaksId } }) => {
    // TODO: Dette er en work-around fordi getSakContextQueryOptions av en eller annen grunn er undefined når brukeren redirectes pga. encoding av search-parameters.
    const queryOptions = getSakContextQueryOptions ?? {
      ...getSakContext,
      queryKey: getSakContext.queryKey(saksId, vedtaksId),
      queryFn: () => getSakContext.queryFn(saksId, vedtaksId),
    };
    const sakContext = await queryClient.ensureQueryData(queryOptions);
    return { saksId: sakContext.sak.saksId, letterTemplates: sakContext.brevMetadata };
  },
  errorComponent: ({ error }) => <ApiError error={error} title="Klarte ikke hente brevmaler for saken." />,
  component: BrevvelgerPage,
});

export interface SubmitTemplateOptions {
  onClick: () => void;
}

export function BrevvelgerPage() {
  const { saksId, letterTemplates } = Route.useLoaderData();
  const [onSubmitClick, setOnSubmitClick] = useState<Nullable<SubmitTemplateOptions>>(null);

  const alleSaksbrevQuery = useQuery({
    queryKey: hentAlleBrevForSak.queryKey(saksId.toString()),
    queryFn: () => hentAlleBrevForSak.queryFn(saksId.toString()),
  });

  return (
    <div
      css={css`
        display: flex;
        flex-direction: column;
        align-self: center;
        background-color: white;
      `}
    >
      <BrevvelgerMainContent
        alleSaksbrevQuery={alleSaksbrevQuery}
        letterTemplates={letterTemplates}
        saksId={saksId}
        setOnSubmitClick={setOnSubmitClick}
      />

      <BrevvelgerFooter
        antallBrevKlarTilSending={alleSaksbrevQuery.data?.filter(erBrevKlar)?.length ?? 0}
        onSubmitClick={onSubmitClick}
        saksId={saksId}
      />
    </div>
  );
}

const BrevvelgerMainContent = (props: {
  saksId: number;
  letterTemplates: LetterMetadata[];
  alleSaksbrevQuery: UseQueryResult<BrevInfo[], Error>;
  setOnSubmitClick: (v: SubmitTemplateOptions) => void;
}) => {
  const { brevId, templateId, enhetsId } = Route.useSearch();
  const [openAccordions, setOpenAccordions] = useState<Record<string, boolean>>({});

  const closeAccordionWhereTemplateWasAdded = (templateId: string) => {
    const kategori = props.letterTemplates.find((template) => template.id === templateId)?.brevkategori ?? "Annet";

    setOpenAccordions((prev) => ({
      ...prev,
      [kategori]: !prev[kategori],
    }));
  };

  return (
    <div
      css={css`
        display: flex;
        background-color: white;
        height: var(--main-page-content-height);
      `}
    >
      <VStack
        css={css`
          width: 720px;
          border-left: 1px solid var(--a-gray-200);
          border-right: 1px solid var(--a-gray-200);
          padding: var(--a-spacing-5) var(--a-spacing-6);
          overflow-y: scroll;
        `}
        gap="6"
      >
        <Heading level="5" size="small">
          Brevmeny
        </Heading>
        <Brevmaler
          alleSaksbrev={props.alleSaksbrevQuery}
          handleOpenAccordionChange={(categoryKey) =>
            setOpenAccordions((prev) => ({ ...prev, [categoryKey]: !prev[categoryKey] }))
          }
          letterTemplates={props.letterTemplates}
          openAccordions={openAccordions}
        />
      </VStack>
      <BrevmalPanel
        brevId={brevId}
        enhetsId={enhetsId ?? ""}
        letterTemplates={props.letterTemplates}
        onAddFavorittSuccess={(templateId) => closeAccordionWhereTemplateWasAdded(templateId)}
        saksId={props.saksId}
        setOnFormSubmitClick={props.setOnSubmitClick}
        templateId={templateId}
      />
    </div>
  );
};

function Brevmaler({
  letterTemplates,
  alleSaksbrev,
  openAccordions,
  handleOpenAccordionChange,
}: {
  letterTemplates: LetterMetadata[];
  alleSaksbrev: UseQueryResult<BrevInfo[], Error>;
  openAccordions: Record<string, boolean>;
  handleOpenAccordionChange: (categoryKey: string) => void;
}) {
  const navigate = useNavigate({ from: "/saksnummer/$saksId/brevvelger" });
  const { templateId } = Route.useSearch();
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
        {alleSaksbrev.isSuccess && <Kladder alleBrevPåSaken={alleSaksbrev.data} letterTemplates={letterTemplates} />}

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
              onOpenChange={() => handleOpenAccordionChange(type)}
              open={searchTerm.length > 0 ? true : openAccordions[type]}
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
                      onClick={() =>
                        navigate({
                          to: "/saksnummer/$saksId/brevvelger",
                          search: (s) => ({
                            ...s,
                            templateId: template.id,
                            brevId: undefined,
                          }),
                        })
                      }
                      title={
                        <HStack
                          align={"center"}
                          css={css`
                            overflow: hidden;
                          `}
                          gap="2"
                          wrap={false}
                        >
                          <BrevSystemIcon brevsystem={template.brevsystem} />
                          <BodyShort
                            css={css`
                              overflow: hidden;
                              text-overflow: ellipsis;
                              max-width: 95%;
                            `}
                            size="small"
                          >
                            {template.name}
                          </BodyShort>
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

const Kladder = (props: { alleBrevPåSaken: BrevInfo[]; letterTemplates: LetterMetadata[] }) => {
  const { brevId } = Route.useSearch();
  const navigate = useNavigate({ from: Route.fullPath });
  const kladder = props.alleBrevPåSaken.filter(erBrevKladdEllerUnderRedigering);

  if (kladder.length > 0) {
    return (
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
            {kladder.map((brev) => (
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
                onClick={() =>
                  navigate({
                    to: "/saksnummer/$saksId/brevvelger",
                    search: (s) => ({
                      ...s,
                      brevId: brev.id.toString(),
                      templateId: undefined,
                    }),
                  })
                }
                title={
                  <HStack
                    align={"center"}
                    css={css`
                      overflow: hidden;
                    `}
                    gap="2"
                    wrap={false}
                  >
                    <BrevSystemIcon
                      brevsystem={props.letterTemplates.find((template) => template.id === brev.brevkode)?.brevsystem}
                    />

                    <BodyShort
                      css={css`
                        overflow: hidden;
                        text-overflow: ellipsis;
                        max-width: 90%;
                      `}
                      size="small"
                    >
                      {brev.brevtittel}
                    </BodyShort>
                  </HStack>
                }
              />
            ))}
          </div>
        </Accordion.Content>
      </Accordion.Item>
    );
  }

  return null;
};

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
      <HStack justify={"space-between"} wrap={false}>
        {props.title}
        {props.description && <BodyShort size="small">{props.description}</BodyShort>}
      </HStack>
    </Button>
  );
};
