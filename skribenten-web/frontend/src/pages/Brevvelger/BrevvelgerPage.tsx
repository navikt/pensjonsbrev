import { css } from "@emotion/react";
import { Accordion, Button, Heading, Search, Tabs } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { useNavigate, useRouteContext, useSearch } from "@tanstack/react-router";
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
        padding: var(--a-spacing-6);
        background: var(--a-white);
      `}
    >
      <Tabs onChange={(value) => navigate({ search: { fane: value as BrevvelgerTabOptions } })} value={fane}>
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
      {/*<div*/}
      {/*  css={css`*/}
      {/*    width: 100%;*/}
      {/*    height: 1px;*/}
      {/*    background: var(--a-grayalpha-300);*/}
      {/*  `}*/}
      {/*/>*/}
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
  return (
    <Button
      css={css`
        justify-content: flex-start;
      `}
      variant="tertiary"
    >
      {letterMetadata.name}
    </Button>
  );
}

function Eblanketter({ eblanketter }: { eblanketter: LetterMetadata[] }) {
  return (
    <ul>
      {eblanketter.map((template) => (
        <li key={template.id}>{template.name}</li>
      ))}
    </ul>
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
