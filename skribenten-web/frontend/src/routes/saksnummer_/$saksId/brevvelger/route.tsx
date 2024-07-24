import { css } from "@emotion/react";
import { Accordion, Alert, Button, Heading, Search, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { Outlet, useNavigate, useParams } from "@tanstack/react-router";
import { groupBy, partition, sortBy } from "lodash";
import { useState } from "react";

import { getFavoritter } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { LetterMetadata } from "~/types/apiTypes";

export const Route = createFileRoute("/saksnummer/$saksId/brevvelger")({
  loaderDeps: ({ search: { vedtaksId } }) => ({ includeVedtak: !!vedtaksId }),
  loader: async ({ context: { queryClient, getSakContextQueryOptions } }) => {
    const sakContext = await queryClient.ensureQueryData(getSakContextQueryOptions);
    return { letterTemplates: sakContext.brevMetadata };
  },
  errorComponent: ({ error }) => <ApiError error={error} title="Klarte ikke hente brevmaler for saken." />,
  component: BrevvelgerPage,
});

export function BrevvelgerPage() {
  const { letterTemplates } = Route.useLoaderData();

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
      <Brevmaler letterTemplates={letterTemplates ?? []} />
      <Outlet />
    </div>
  );
}

function Brevmaler({ letterTemplates }: { letterTemplates: LetterMetadata[] }) {
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
                    <BrevmalButton key={template.id} letterMetadata={template} />
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

function BrevmalButton({ letterMetadata }: { letterMetadata: LetterMetadata }) {
  const { templateId } = useParams({ from: "/saksnummer/$saksId/brevvelger/$templateId" });
  const navigate = useNavigate({ from: "/saksnummer/$saksId/brevvelger/$templateId" });

  // Ideally we would use the Link component as it gives native <a/> features.
  // However, when we render as many links as we do it slows down drastically. Try again when Tanstack Router has developed further

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
        `,
        templateId === letterMetadata.id &&
          css`
            color: var(--a-text-on-action);
            background-color: var(--a-surface-action-active);
          `,
      )}
      data-cy="brevmal-button"
      onClick={() =>
        navigate({
          to: "/saksnummer/$saksId/brevvelger/$templateId",
          params: { templateId: letterMetadata.id },
          search: (s) => s,
        })
      }
      variant="tertiary"
    >
      {letterMetadata.name}
    </Button>
  );
}
