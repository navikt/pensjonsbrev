import { css } from "@emotion/react";
import { Accordion, Button, Heading, VStack } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";
import { createFileRoute, useNavigate } from "@tanstack/react-router";

import { sendBrev } from "~/api/sak-api-endpoints";
import type { BestillBrevResponse } from "~/types/brev";

import type { FerdigstillResponse, FerdigstillResponser } from "./-components/FerdigstillResultatContext";
import { useFerdigstillResultatContext } from "./-components/FerdigstillResultatContext";
import ResultatAccordionContent from "./-components/ResultatAccordionContent";
import ResultatAccordionHeader from "./-components/ResultatAccordionHeader";

export const Route = createFileRoute("/saksnummer/$saksId/kvittering")({
  component: Kvittering,
});

function Kvittering() {
  const { saksId } = Route.useParams();
  const navigate = useNavigate({ from: Route.fullPath });
  const ferdigstillBrevContext = useFerdigstillResultatContext();

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
          onClick={() => navigate({ to: "/saksnummer" })}
          size="small"
          type="button"
        >
          Åpne annen sak
        </Button>
        <Button
          css={css`
            width: fit-content;
          `}
          onClick={() => navigate({ to: "/saksnummer/$saksId/brevvelger", params: { saksId } })}
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
          onClick={() => {
            //hva er dokumentoversikten? Pesys? Isåfall trenger vi en eller annen url
            console.log("åpner dokumentoversikt");
          }}
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
          onClick={() => {
            //hva skal egentlig skje her?
            console.log("avslutte brevbehandler");
          }}
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
      <FerdigstillingsResultat resultat={ferdigstillBrevContext.resultat} sakId={saksId} />
    </div>
  );
}

const FerdigstillingsResultat = (properties: { sakId: string; resultat: FerdigstillResponser }) => {
  return (
    <Accordion>
      {properties.resultat.map((result, index) => (
        <ResultatAccordionItem key={`resultat-${index}`} resultat={result} sakId={properties.sakId} />
      ))}
    </Accordion>
  );
};

const ResultatAccordionItem = (properties: { sakId: string; resultat: FerdigstillResponse }) => {
  const mutation = useMutation<BestillBrevResponse, Error, number>({
    mutationFn: (brevId) => sendBrev(properties.sakId, brevId),
  });

  return (
    <Accordion.Item>
      <ResultatAccordionHeader
        resultat={
          mutation.isSuccess
            ? {
                status: "fulfilledWithSuccess",
                brevInfo: properties.resultat.brevInfo,
                response: mutation.data,
              }
            : properties.resultat
        }
        sakId={properties.sakId}
      />
      <ResultatAccordionContent
        error={mutation.isError ? mutation.error : null}
        isPending={mutation.isPending}
        onPrøvIgjenClick={() => mutation.mutate(properties.resultat.brevInfo.id)}
        resultat={
          mutation.isSuccess
            ? {
                status: "fulfilledWithSuccess",
                brevInfo: properties.resultat.brevInfo,
                response: mutation.data,
              }
            : properties.resultat
        }
        sakId={properties.sakId}
      />
    </Accordion.Item>
  );
};
