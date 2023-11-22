import { css } from "@emotion/react";
import { Accordion, Button, Heading, Search, Tabs } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { Link, Outlet, useNavigate, useParams, useRouteContext, useSearch } from "@tanstack/react-router";
import { useState } from "react";

import { getFavoritter, getLetterTemplate } from "../../api/skribenten-api-endpoints";
import { brevvelgerRoute } from "../../tanStackRoutes";
import type { LetterCategory } from "../../types/apiTypes";
import type { LetterMetadata } from "../../types/apiTypes";

export enum BrevvelgerTabOptions {
  BREVMALER = "brevmaler",
  E_BLANKETTER = "e-blanketter",
}

export function BrevvelgerPage() {
  const { fane } = useSearch({ from: brevvelgerRoute.id });
  const { sakId } = useParams({ from: brevvelgerRoute.id });
  const navigate = useNavigate();
  const { getSakQueryOptions } = useRouteContext({ from: brevvelgerRoute.id });
  const sak = useQuery(getSakQueryOptions).data;

  const getLetterTemplateQuery = useQuery({
    queryKey: getLetterTemplate.queryKey(sak?.sakType as string),
    queryFn: () => getLetterTemplate.queryFn(sak?.sakType as string),
    enabled: !!sak,
  });

  return (
    <div
      css={css`
        background: var(--a-white);
        display: flex;
        gap: var(--a-spacing-4);
        justify-content: space-between;

        > :first-of-type {
          flex: 1;
          padding: var(--a-spacing-6);
        }
      `}
    >
      <Tabs
        onChange={(value) =>
          navigate({
            to: brevvelgerRoute.id,
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
          <Brevmaler kategorier={getLetterTemplateQuery.data?.kategorier ?? []} />
        </Tabs.Panel>
        <Tabs.Panel value={BrevvelgerTabOptions.E_BLANKETTER}>
          <Eblanketter eblanketter={getLetterTemplateQuery.data?.eblanketter ?? []} />
        </Tabs.Panel>
      </Tabs>
      <Outlet />
    </div>
  );
}

function Brevmaler({ kategorier }: { kategorier: LetterCategory[] }) {
  const [searchTerm, setSearchTerm] = useState("");

  const favoritter = useQuery(getFavoritter).data ?? [];

  const matchingLetterCategories = kategorier.map((category) => ({
    ...category,
    templates: category.templates.filter((template) => template.name.toLowerCase().includes(searchTerm.toLowerCase())),
  }));

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
        value={searchTerm}
        variant="simple"
      />
      <Heading level="2" size="xsmall">
        Favoritter
      </Heading>
      <div>
        {favoritter.map((favoritt) => (
          <span key={favoritt}>{favoritt}</span>
        ))}
      </div>
      <Heading level="2" size="xsmall">
        Brevmaler
      </Heading>
      <Accordion headingSize="xsmall" size="small">
        {matchingLetterCategories.map((letterCategory) => (
          <Accordion.Item key={letterCategory.name} open={searchTerm.length > 0 ? true : undefined}>
            <Accordion.Header>{CATEGORY_TRANSLATIONS[letterCategory.name] ?? "Annet"}</Accordion.Header>
            <Accordion.Content>
              <div
                css={css`
                  display: flex;
                  flex-direction: column;
                `}
              >
                {letterCategory.templates.map((template) => (
                  <BrevmalButton key={template.id} letterMetadata={template} />
                ))}
              </div>
            </Accordion.Content>
          </Accordion.Item>
        ))}
      </Accordion>
    </div>
  );
}

function BrevmalButton({ letterMetadata }: { letterMetadata: LetterMetadata }) {
  const { sakId } = useParams({ from: brevvelgerRoute.id });
  return (
    // @ts-expect-error -- Aksel Buttons "as" typing clashes when using css-emotion as it also has an "as" override. Not ideal: https://aksel.nav.no/grunnleggende/kode/overridablecomponent
    <Button
      as={Link}
      css={css`
        justify-content: flex-start;

        &[data-status="active"] {
          color: var(--ac-button-tertiary-active-text, var(--a-text-on-action));
          background-color: var(--ac-button-tertiary-active-bg, var(--a-surface-action-active));
        }
      `}
      from={brevvelgerRoute.id}
      params={{ sakId, brevmalId: letterMetadata.id }}
      search={(search: unknown) => search}
      to="$brevmalId"
      variant="tertiary"
    >
      {letterMetadata.name}
    </Button>
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

const CATEGORY_TRANSLATIONS: Record<string, string> = {
  BREV_MED_SKJEMA: "Brev med skjema",
  INFORMASJON: "Informasjon",
  INNHENTE_OPPL: "Innhente opplysninger",
  NOTAT: "Notat",
  OVRIG: "Øvrig",
  VARSEL: "Varsel",
  VEDTAK: "Vedtak",
};
