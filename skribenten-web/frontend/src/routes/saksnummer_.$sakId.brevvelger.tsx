import { css } from "@emotion/react";
import { Accordion, Button, Search, Tabs } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { Outlet, useNavigate, useParams } from "@tanstack/react-router";
import { groupBy } from "lodash";
import { useState } from "react";

import { getEblanketter, getFavoritter, getLetterTemplate } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { LetterMetadata } from "~/types/apiTypes";

export const Route = createFileRoute("/saksnummer/$sakId/brevvelger")({
  validateSearch: (search: Record<string, unknown>): { enhetsId?: string; fane: BrevvelgerTabOptions } => ({
    fane:
      search.fane === BrevvelgerTabOptions.E_BLANKETTER
        ? BrevvelgerTabOptions.E_BLANKETTER
        : BrevvelgerTabOptions.BREVMALER,
    enhetsId: search.enhetsId?.toString(),
  }),
  loader: async ({ context: { queryClient, getSakQueryOptions } }) => {
    const { sakType } = await queryClient.ensureQueryData(getSakQueryOptions);

    const eblanketter = await queryClient.ensureQueryData(getEblanketter);

    const getLetterTemplateQuery = {
      queryKey: getLetterTemplate.queryKey(sakType),
      queryFn: () => getLetterTemplate.queryFn(sakType),
    };

    const letterTemplates = await queryClient.ensureQueryData(getLetterTemplateQuery);

    return { letterTemplates, eblanketter };
  },
  errorComponent: ({ error }) => <ApiError error={error} text="Klarte ikke hente brevmaler for saken." />,
  component: BrevvelgerPage,
});

export enum BrevvelgerTabOptions {
  BREVMALER = "brevmaler",
  E_BLANKETTER = "e-blanketter",
}

export function BrevvelgerPage() {
  const { fane } = Route.useSearch();
  const { sakId } = Route.useParams();
  const navigate = useNavigate({ from: Route.fullPath });
  const { letterTemplates, eblanketter } = Route.useLoaderData();

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
      `}
    >
      <Tabs
        onChange={(value) =>
          navigate({
            to: Route.fullPath,
            params: { sakId },
            search: { fane: value as BrevvelgerTabOptions },
          })
        }
        value={fane}
      >
        <Tabs.List>
          <Tabs.Tab label="Brevmaler" value={BrevvelgerTabOptions.BREVMALER} />
          <Tabs.Tab label="E-blanketter" value={BrevvelgerTabOptions.E_BLANKETTER} />
        </Tabs.List>
        <Tabs.Panel value={BrevvelgerTabOptions.BREVMALER}>
          <Brevmaler kategorier={letterTemplates ?? []} />
        </Tabs.Panel>
        <Tabs.Panel value={BrevvelgerTabOptions.E_BLANKETTER}>
          <Eblanketter eblanketter={eblanketter ?? []} />
        </Tabs.Panel>
      </Tabs>
      <Outlet />
    </div>
  );
}

function Brevmaler({ kategorier }: { kategorier: LetterMetadata[] }) {
  const [searchTerm, setSearchTerm] = useState("");

  const favoritter = useQuery(getFavoritter).data ?? [];

  const matchingFavoritter = kategorier.filter(({ id }) => favoritter.includes(id));
  const brevmalerMatchingSearchTerm = kategorier.filter((template) =>
    template.name.toLowerCase().includes(searchTerm.toLowerCase()),
  );

  const brevmalerGroupedByType = {
    FAVORITTER: matchingFavoritter,
    ...groupBy(brevmalerMatchingSearchTerm, (brevmal) => brevmal.brevkategoriCode),
  };

  return (
    <div
      css={css`
        display: flex;
        flex-direction: column;
        gap: var(--a-spacing-6);
        margin: var(--a-spacing-6) 0;
      `}
    >
      <Search
        label="Filtrer brevmaler"
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
        {Object.entries(brevmalerGroupedByType).map(([type, brevmaler]) => {
          return (
            <Accordion.Item key={type} open={searchTerm.length > 0 ? true : undefined}>
              <Accordion.Header
                css={css`
                  flex-direction: row-reverse;
                  justify-content: space-between;
                `}
              >
                {CATEGORY_TRANSLATIONS[type] ?? "Annet"}
              </Accordion.Header>
              <Accordion.Content>
                <div
                  css={css`
                    display: flex;
                    flex-direction: column;
                  `}
                >
                  {brevmaler.map((template) => (
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

function Eblanketter({ eblanketter }: { eblanketter: LetterMetadata[] }) {
  return (
    <div
      css={css`
        margin-top: var(--a-spacing-6);
        display: flex;
        flex-direction: column;
      `}
    >
      {eblanketter.map((template) => (
        <BrevmalButton key={template.id} letterMetadata={template} />
      ))}
    </div>
  );
}

function BrevmalButton({ letterMetadata }: { letterMetadata: LetterMetadata }) {
  const { sakId, templateId } = useParams({ from: "/saksnummer/$sakId/brevvelger/$templateId" });
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
          params: { sakId, templateId: letterMetadata.id },
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
};
