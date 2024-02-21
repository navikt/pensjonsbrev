import { css } from "@emotion/react";
import { Accordion, Alert, Button, Heading, Search } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { Outlet, useNavigate, useParams } from "@tanstack/react-router";
import { groupBy, partition, sortBy } from "lodash";
import { useState } from "react";

import { getFavoritter, getLetterTemplate } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { LetterMetadata } from "~/types/apiTypes";

export const Route = createFileRoute("/saksnummer/$sakId/brevvelger")({
  loaderDeps: ({ search: { vedtaksId } }) => ({ includeVedtak: !!vedtaksId }),
  loader: async ({ context: { queryClient, getSakQueryOptions }, deps: { includeVedtak } }) => {
    const sak = await queryClient.ensureQueryData(getSakQueryOptions);

    const letterTemplates = await queryClient.ensureQueryData({
      queryKey: getLetterTemplate.queryKey({ sakType: sak.sakType, includeVedtak }),
      queryFn: () => getLetterTemplate.queryFn(sak.sakType, { includeVedtak }),
    });

    return { letterTemplates };
  },
  errorComponent: ({ error }) => <ApiError error={error} title="Klarte ikke hente brevmaler for saken." />,
  component: BrevvelgerPage,
});

export function BrevvelgerPage() {
  const { letterTemplates } = Route.useLoaderData();

  return (
    <div
      css={css`
        background: var(--a-white);
        display: grid;
        align-self: center;
        max-width: 1108px;
        grid-template-columns: minmax(432px, 720px) minmax(336px, 388px);
        gap: var(--a-spacing-4);
        justify-content: space-between;
        flex: 1;

        > :first-of-type {
          padding: var(--a-spacing-4);
          border-left: 1px solid var(--a-gray-400);
          border-right: 1px solid var(--a-gray-400);
        }

        /* When no template is selected, let the brevvelger use entire width */
        > :only-child {
          grid-column: span 2;
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

  const groupedBrevmaler = groupBy(brevmaler, (brevmal) => brevmal.brevkategoriCode ?? "OTHER");

  const brevmalerGroupedByType: Record<string, LetterMetadata[]> = {
    ...(matchingFavoritter.length > 0 ? { FAVORITTER: matchingFavoritter } : {}),
    ...groupedBrevmaler,
    ...(eblanketter.length > 0 ? { E_BLANKETT: eblanketter } : {}),
  };

  const sortedCategoryKeys = [
    "FAVORITTER",
    ...sortBy(Object.keys(groupedBrevmaler), (type) => CATEGORY_TRANSLATIONS[type]),
    "E_BLANKETT",
  ];

  return (
    <div
      css={css`
        display: flex;
        flex-direction: column;
        padding: var(--a-spacing-6) var(--a-spacing-4);
        gap: var(--a-spacing-6);
      `}
    >
      <Heading level="1" size="medium">
        Brevmeny
      </Heading>
      <Search
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
          <Alert size="small" variant="info">
            Ingen treff
          </Alert>
        )}
        {sortedCategoryKeys.map((type) => {
          return (
            <Accordion.Item
              defaultOpen={type === "FAVORITTER"}
              key={type}
              open={searchTerm.length > 0 ? true : undefined}
            >
              <Accordion.Header
                css={css`
                  flex-direction: row-reverse;
                  justify-content: space-between;
                `}
              >
                {CATEGORY_TRANSLATIONS[type]}
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
    </div>
  );
}

function BrevmalButton({ letterMetadata }: { letterMetadata: LetterMetadata }) {
  const { templateId } = useParams({ from: "/saksnummer/$sakId/brevvelger/$templateId" });
  const navigate = useNavigate({ from: "/saksnummer/$sakId/brevvelger/$templateId" });

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
      onClick={() =>
        navigate({
          to: "/saksnummer/$sakId/brevvelger/$templateId",
          params: { templateId: letterMetadata.id },
          search: (s) => s,
        })
      }
      size="small"
      variant="tertiary"
    >
      {letterMetadata.name}
    </Button>
  );
}

const CATEGORY_TRANSLATIONS: Record<string, string> = {
  BREV_MED_SKJEMA: "Brev med skjema",
  INFORMASJON: "Informasjon",
  INNHENTE_OPPL: "Innhente opplysninger",
  NOTAT: "Notat",
  OVRIG: "Øvrig",
  VARSEL: "Varsel",
  VEDTAK: "Vedtak",
  FAVORITTER: "Favoritter",
  E_BLANKETT: "E-blanketter",
  OTHER: "Annet",
};
