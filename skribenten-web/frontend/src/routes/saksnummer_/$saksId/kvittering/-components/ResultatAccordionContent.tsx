import { css } from "@emotion/react";
import { XMarkOctagonFillIcon } from "@navikt/aksel-icons";
import { Accordion, BodyShort, Button, VStack } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";

import { sendBrev } from "~/api/sak-api-endpoints";
import type { BestillBrevResponse } from "~/types/brev";

import Oppsummeringspar from "./Oppsummeringspar";

const ResultatAccordionContentWrapper = (properties: {
  sakId: string;
  resultat: PromiseSettledResult<BestillBrevResponse>;
}) => {
  return (
    <Accordion.Content>
      <ResultatAccordionContent resultat={properties.resultat} sakId={properties.sakId} />
    </Accordion.Content>
  );
};

const ResultatAccordionContent = (properties: {
  sakId: string;
  resultat: PromiseSettledResult<BestillBrevResponse>;
}) => {
  switch (properties.resultat.status) {
    case "rejected": {
      return (
        <ResultatContentError resultat={properties.resultat} sakId={properties.sakId}>
          <BodyShort>Brevet ble ikke sendt pga en ukjent feil. Prøv igjen.</BodyShort>
          <BodyShort>Feilen var {JSON.stringify(properties.resultat.reason)}</BodyShort>
        </ResultatContentError>
      );
    }
    case "fulfilled": {
      if (properties.resultat.value.error != null) {
        return (
          <ResultatContentError resultat={properties.resultat} sakId={properties.sakId}>
            <BodyShort>
              Brevet ble ikke sendt pga {properties.resultat.value.error?.tekniskgrunn ?? "en ukjent teknisk grunn"}.
              Prøv igjen.
            </BodyShort>
            <BodyShort>{properties.resultat.value.error?.beskrivelse}</BodyShort>
          </ResultatContentError>
        );
      } else if (properties.resultat.value.journalpostId == null) {
        return (
          <ResultatContentError resultat={properties.resultat} sakId={properties.sakId}>
            <BodyShort>Brevet ble ikke sendt pga en ukjent feil. Prøv igjen.</BodyShort>
          </ResultatContentError>
        );
      } else {
        return (
          <VStack gap="4">
            {/* TODO <Oppsummeringspar tittel={"Mottaker"} verdi={""} /> */}
            {/* TODO <Oppsummeringspar tittel={"Distribueres via"} verdi={""} /> */}
            <Oppsummeringspar tittel={"Journalpost ID"} verdi={properties.resultat.value.journalpostId} />
          </VStack>
        );
      }
    }
  }
};

const ResultatContentError = (properties: {
  sakId: string;
  resultat: PromiseSettledResult<BestillBrevResponse>;
  children: React.ReactNode;
}) => {
  const mutation = useMutation<BestillBrevResponse, Error, number>({
    mutationFn: (brevId) => sendBrev(properties.sakId, brevId),
  });

  return (
    <VStack align="start" gap="3">
      <div
        css={css`
          display: grid;
          grid-template-columns: 1.5rem 1fr;
          gap: 1rem;
        `}
      >
        <XMarkOctagonFillIcon
          css={css`
            color: var(--a-nav-red);
          `}
          fontSize="1.5rem"
          title="error"
        />
        <div
          css={css`
            display: flex;
            flex-direction: column;
            gap: 1rem;
          `}
        >
          {properties.children}
        </div>
      </div>
      {/* TODO - brevId */}
      <Button onClick={() => mutation.mutate(11_111)} size="small" type="button" variant="primary-neutral">
        Prøv igjen
      </Button>
    </VStack>
  );
};

export default ResultatAccordionContentWrapper;
