import type { SerializedStyles } from "@emotion/react";
import { css } from "@emotion/react";
import {
  Accordion,
  Alert,
  Bleed,
  BodyShort,
  Box,
  Button,
  Heading,
  HStack,
  Label,
  Search,
  VStack,
} from "@navikt/ds-react";
import type { UseQueryResult } from "@tanstack/react-query";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import Fuse from "fuse.js";
import { groupBy, partition, sortBy } from "lodash";
import { useCallback, useMemo, useState } from "react";
import { z } from "zod";

import { getBrevmetadata } from "~/api/brev-queries";
import { hentAlleBrevInfoForSak } from "~/api/sak-api-endpoints";
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
    context.queryClient.prefetchQuery(getBrevmetadata);
    return await context.queryClient.ensureQueryData(getSakContext(saksId, vedtaksId));
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
  const brevmetadata = useQuery({ ...getBrevmetadata, select: metadataMapFromList }).data ?? {};

  const [onSubmitClick, setOnSubmitClick] = useState<Nullable<SubmitTemplateOptions>>(null);

  const alleSaksbrevQuery = useQuery({
    queryKey: hentAlleBrevInfoForSak.queryKey(saksId.toString()),
    queryFn: () => hentAlleBrevInfoForSak.queryFn(saksId.toString()),
  });

  return (
    <Box asChild background="default">
      <VStack marginInline={{ sm: "space-0", lg: "auto" }} width="fit-content">
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
      </VStack>
    </Box>
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
    <Box asChild height="calc(var(--main-page-content-height)">
      <HStack wrap={false}>
        {/* Brevmal-liste */}
        <Box
          asChild
          borderColor="neutral-subtle"
          borderWidth="0 1 0 0"
          minWidth="640px"
          paddingBlock="space-20 space-0"
          paddingInline="space-24"
        >
          <VStack gap="space-24" height="100%">
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
        </Box>
        <BrevmalPanel
          brevId={brevId}
          brevmetadata={brevmetadata}
          enhetsId={enhetsId ?? ""}
          onAddFavorittSuccess={(templateId) => closeAccordionWhereTemplateWasAdded(templateId)}
          saksId={props.saksId}
          setOnFormSubmitClick={props.setOnSubmitClick}
          templateId={templateId}
        />
      </HStack>
    </Box>
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
  const favoritter = useQuery(getFavoritter).data ?? [];

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
    <VStack gap="space-24" height="calc(100% - 51px)">
      <Search
        data-cy="brevmal-search"
        hideLabel={false}
        label="Søk etter brevmal"
        onChange={(value) => setSearchTerm(value)}
        size="small"
        value={searchTerm}
        variant="simple"
      />
      <Bleed asChild marginInline="space-24">
        <Box asChild overflowY="auto" paddingInline="space-24">
          <Accordion
            css={css`
              .aksel-accordion__content {
                margin: 0;
              }
            `}
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
                      color: var(--ax-text-neutral);
                    `}
                  >
                    <Label size="small">{type}</Label>
                  </Accordion.Header>
                  {/* overflowX: hidden bidrar til ellipse på overflow i indre BodyShort med truncate */}
                  <Accordion.Content css={{ ".aksel-accordion__content-inner": { overflowX: "hidden" } }}>
                    <VStack>
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
                            <HStack flexGrow="1" gap="space-8" overflowX="hidden" wrap={false}>
                              <BrevSystemIcon brevsystem={template.brevsystem} />
                              <Box asChild maxWidth="calc(100% - var(--ax-space-24)">
                                <BodyShort size="small" truncate>
                                  {template.name}
                                </BodyShort>
                              </Box>
                            </HStack>
                          }
                        />
                      ))}
                    </VStack>
                  </Accordion.Content>
                </Accordion.Item>
              );
            })}
          </Accordion>
        </Box>
      </Bleed>
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
            color: var(--ax-text-neutral);
          `}
        >
          <HStack gap="space-8">
            <Label size="small">Kladder</Label>
          </HStack>
        </Accordion.Header>
        <Accordion.Content css={{ ".aksel-accordion__content-inner": { overflowX: "hidden" } }}>
          <VStack>
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
                  <HStack flexGrow="1" gap="space-8" overflowX="hidden" wrap={false}>
                    <BrevSystemIcon brevsystem={props.brevmetadata[brev.brevkode]?.brevsystem} />
                    <Box asChild maxWidth="calc(100% - var(--ax-space-24)">
                      <BodyShort size="small" truncate>
                        {brev.brevtittel}
                      </BodyShort>
                    </Box>
                  </HStack>
                }
              />
            ))}
          </VStack>
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
          color: var(--ax-text-neutral);
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
      <HStack gap="space-8" justify="space-between" wrap={false}>
        {props.title}
        {props.description && <BodyShort size="small">{props.description}</BodyShort>}
      </HStack>
    </Button>
  );
};

function metadataMapFromList(letterMetadataList: LetterMetadata[]): Record<string, LetterMetadata> {
  return letterMetadataList.reduce((acc, b) => ({ ...acc, [b.id]: b }), {} as Record<string, LetterMetadata>);
}
