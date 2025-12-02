import type { SerializedStyles } from "@emotion/react";
import { css } from "@emotion/react";
import { Accordion, Alert, BodyShort, Button, Heading, HStack, Label, Search, VStack } from "@navikt/ds-react";
import type { UseQueryResult } from "@tanstack/react-query";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import Fuse from "fuse.js";
import { groupBy, partition, sortBy } from "lodash";
import { useCallback, useMemo, useState } from "react";
import { z } from "zod";

import { getBrevmetadataQuery } from "~/api/brev-queries";
import { hentAlleBrevForSak } from "~/api/sak-api-endpoints";
import { getFavoritterQuery, getSakContextQuery } from "~/api/skribenten-api-endpoints";
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

const brevvelgerSearchSchema = z.object({
  brevId: z.coerce.number().optional(),
  idTSSEkstern: z.coerce.string().optional(),
  templateId: z.coerce.string().optional(),
});

type BrevvelgerSearch = z.infer<typeof brevvelgerSearchSchema>;

export const Route = createFileRoute("/saksnummer_/$saksId/brevvelger")({
  validateSearch: (search): BrevvelgerSearch => brevvelgerSearchSchema.parse(search),
  loaderDeps: ({ search: { vedtaksId } }) => ({ vedtaksId }),
  loader: async ({ context, params: { saksId }, deps: { vedtaksId } }) => {
    context.queryClient.prefetchQuery(getBrevmetadataQuery);
    return await context.queryClient.ensureQueryData(getSakContextQuery(saksId, vedtaksId));
  },
  errorComponent: ({ error }) => <ApiError error={error} title="Klarte ikke hente brevmaler for saken." />,
  component: BrevvelgerPage,
});

export interface SubmitTemplateOptions {
  onClick: () => void;
}

export function BrevvelgerPage() {
  const { saksId } = Route.useParams();
  const { brevmalKoder } = Route.useLoaderData();
  const brevmetadata = useQuery({ ...getBrevmetadataQuery, select: metadataMapFromList }).data ?? {};

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

        @media (width <= 1023px) {
          align-self: start;
        }
        width: 100%;
        min-width: 944px;
        max-width: 1104px;
        background-color: white;
      `}
    >
      <BrevvelgerMainContent
        alleSaksbrevQuery={alleSaksbrevQuery}
        brevmalKoder={brevmalKoder}
        brevmetadata={brevmetadata}
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
  saksId: string;
  brevmalKoder: string[];
  brevmetadata: Record<string, LetterMetadata>;
  alleSaksbrevQuery: UseQueryResult<BrevInfo[], Error>;
  setOnSubmitClick: (v: SubmitTemplateOptions) => void;
}) => {
  const { brevId, templateId, enhetsId } = Route.useSearch();
  const { brevmalKoder, brevmetadata } = props;
  const [openAccordions, setOpenAccordions] = useState<Record<string, boolean>>({});

  const closeAccordionWhereTemplateWasAdded = useCallback(
    (templateId: string) => {
      const kategori = brevmetadata[templateId]?.brevkategori ?? "Annet";

      setOpenAccordions((prev) => ({
        ...prev,
        [kategori]: false,
      }));
    },
    [brevmetadata, setOpenAccordions],
  );

  return (
    <div
      css={css`
        display: grid;
        grid-template-columns: minmax(640px, 720px) minmax(304px, 384px);
        background-color: white;
        height: var(--main-page-content-height);
      `}
    >
      <VStack
        css={css`
          border-left: 1px solid var(--ax-neutral-300);
          border-right: 1px solid var(--ax-neutral-300);
          padding: var(--ax-space-20) var(--ax-space-24);
          overflow-y: auto;
        `}
        gap="6"
      >
        <Heading level="1" size="small">
          Brevvelger
        </Heading>
        <Brevmaler
          alleSaksbrev={props.alleSaksbrevQuery}
          brevmalKoder={brevmalKoder}
          brevmetadata={brevmetadata}
          handleOpenAccordionChange={(categoryKey) =>
            setOpenAccordions((prev) => ({ ...prev, [categoryKey]: !prev[categoryKey] }))
          }
          openAccordions={openAccordions}
        />
      </VStack>
      <BrevmalPanel
        brevId={brevId}
        brevmetadata={brevmetadata}
        enhetsId={enhetsId ?? ""}
        onAddFavorittSuccess={(templateId) => closeAccordionWhereTemplateWasAdded(templateId)}
        saksId={props.saksId}
        setOnFormSubmitClick={props.setOnSubmitClick}
        templateId={templateId}
      />
    </div>
  );
};

function Brevmaler({
  brevmalKoder,
  brevmetadata,
  alleSaksbrev,
  openAccordions,
  handleOpenAccordionChange,
}: {
  brevmalKoder: string[];
  brevmetadata: Record<string, LetterMetadata>;
  alleSaksbrev: UseQueryResult<BrevInfo[], Error>;
  openAccordions: Record<string, boolean>;
  handleOpenAccordionChange: (categoryKey: string) => void;
}) {
  const navigate = useNavigate({ from: "/saksnummer/$saksId/brevvelger" });
  const { templateId } = Route.useSearch();
  const [searchTerm, setSearchTerm] = useState("");
  const favoritter = useQuery(getFavoritterQuery).data ?? [];

  const alleBrevmaler: LetterMetadata[] = useMemo(
    () => brevmalKoder.map((kode) => brevmetadata[kode]).filter((b): b is LetterMetadata => b !== undefined),
    [brevmalKoder, brevmetadata],
  );

  const fuse = useMemo(() => {
    const fuseOptions = {
      keys: ["name", "brevsystem", "brevkategori"],
      threshold: 0.4, // lower => stricter, less fuzzy (default is 0.6)
    };
    return new Fuse(alleBrevmaler, fuseOptions);
  }, [alleBrevmaler]);

  const brevmalerMatchingSearchTerm =
    searchTerm.trim().length === 0
      ? sortBy(alleBrevmaler, (template) => template.name)
      : fuse.search(searchTerm).map((result) => result.item);

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

          .aksel-accordion__content {
            padding: 0;
          }
        `}
        headingSize="xsmall"
        indent={false}
        size="small"
      >
        {alleSaksbrev.isSuccess && <Kladder alleBrevPåSaken={alleSaksbrev.data} brevmetadata={brevmetadata} />}

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
                              color: var(--ax-text-accent-contrast);
                              background-color: var(--ax-bg-accent-strong-hover);
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

const Kladder = (props: { alleBrevPåSaken: BrevInfo[]; brevmetadata: Record<string, LetterMetadata> }) => {
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
                  brev.id === brevId
                    ? css`
                        color: var(--ax-text-accent-contrast);
                        background-color: var(--ax-bg-accent-strong-hover);
                      `
                    : undefined
                }
                key={brev.id}
                onClick={() =>
                  navigate({
                    to: "/saksnummer/$saksId/brevvelger",
                    search: (s) => ({
                      ...s,
                      brevId: brev.id,
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
                    <BrevSystemIcon brevsystem={props.brevmetadata[brev.brevkode]?.brevsystem} />

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
          padding: var(--ax-space-8) var(--ax-space-12);
          border-radius: 0;

          span {
            font-weight: var(--ax-font-weight-regular);
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

function metadataMapFromList(letterMetadataList: LetterMetadata[]): Record<string, LetterMetadata> {
  return letterMetadataList.reduce((acc, b) => ({ ...acc, [b.id]: b }), {} as Record<string, LetterMetadata>);
}
