import { css } from "@emotion/react";
import { Accordion, Button, Heading, Label, VStack } from "@navikt/ds-react";
import { createFileRoute } from "@tanstack/react-router";

import type { BestillBrevResponse } from "~/types/brev";

import ResultatAccordionContent from "./-components/ResultatAccordionContent";
import ResultatTag from "./-components/ResultatTag";

export const Route = createFileRoute("/saksnummer/$saksId/kvittering")({
  component: Kvittering,
});

function Kvittering() {
  const { saksId } = Route.useParams();

  return (
    <div
      css={css`
        display: grid;
        flex-grow: 1;
        grid-template-columns: 40% 1% 40%;
        gap: 3rem;
        padding: var(--a-spacing-5) 0;
        background: var(--a-white);
      `}
    >
      <VStack
        css={css`
          justify-self: flex-end;
        `}
        gap="4"
      >
        <Heading size="medium">Hva vil du gjøre nå?</Heading>
        <Button
          css={css`
            width: fit-content;
          `}
          size="small"
          type="button"
        >
          Åpne annen sak
        </Button>
        <Button
          css={css`
            width: fit-content;
          `}
          size="small"
          type="button"
          variant="secondary"
        >
          Lage nytt brev på denne brukeren
        </Button>
        <Button
          css={css`
            width: fit-content;
          `}
          size="small"
          type="button"
          variant="secondary"
        >
          Åpne dokumentoversikt
        </Button>
        <Button
          css={css`
            width: fit-content;
          `}
          size="small"
          type="button"
          variant="secondary"
        >
          Avslutte brevbehandler
        </Button>
      </VStack>
      <div
        css={css`
          background: var(--a-gray-200);
          width: 1px;
        `}
      ></div>
      <div css={css``}>
        <FerdigstillingsResultat
          resultat={[
            { status: "fulfilled", value: { error: null, journalpostId: 1 } },
            {
              status: "fulfilled",
              value: {
                error: {
                  brevIkkeStoettet: "brev er ikke støttet",
                  beskrivelse:
                    "en beskrivelse for å forklare hvorfor vi fikk denne feilen. Dette skyldes en teknisk grunn - grunnen er fordi brevet ikke er støttet",
                  tekniskgrunn: "den tekniske grunnen",
                },
                journalpostId: null,
              },
            },
            { status: "fulfilled", value: { error: null, journalpostId: null } },
            { status: "rejected", reason: { something: "lol" } },
          ]}
          sakId={saksId}
        />
      </div>
    </div>
  );
}

const FerdigstillingsResultat = (properties: {
  sakId: string;
  resultat: PromiseSettledResult<BestillBrevResponse>[];
}) => {
  return (
    <Accordion>
      {properties.resultat.map((result, index) => (
        <ResultatAccordionItem key={`resultat-${index}`} resultat={result} sakId={properties.sakId} />
      ))}
    </Accordion>
  );
};

const ResultatAccordionItem = (properties: { sakId: string; resultat: PromiseSettledResult<BestillBrevResponse> }) => {
  return (
    <Accordion.Item>
      <Accordion.Header>
        <div
          css={css`
            display: flex;
            flex-direction: column;
            align-items: flex-start;
          `}
        >
          <ResultatTag resultat={properties.resultat} />
          <Label>Her må vi ha brevtittel</Label>
        </div>
      </Accordion.Header>
      <ResultatAccordionContent resultat={properties.resultat} sakId={properties.sakId} />
    </Accordion.Item>
  );
};
