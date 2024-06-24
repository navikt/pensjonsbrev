import { css } from "@emotion/react";
import { Alert, VStack } from "@navikt/ds-react";
import { createFileRoute, notFound } from "@tanstack/react-router";

import { getPreferredLanguage } from "~/api/skribenten-api-endpoints";
import type { LetterMetadata } from "~/types/apiTypes";
import type { SpraakKode } from "~/types/apiTypes";
import { BrevSystem } from "~/types/apiTypes";

import BrevmalForDoksys from "./-BrevmalDoksys";
import BrevmalForExstream from "./-BrevmalExstream";
import Eblankett from "./-EBlankett";
import FavoriteButton from "./-FavoriteButton";

export const Route = createFileRoute("/saksnummer/$saksId/brevvelger/$templateId")({
  component: SelectedTemplate,
  loaderDeps: ({ search: { vedtaksId } }) => ({ vedtaksId: vedtaksId }),
  validateSearch: (search: Record<string, unknown>): { idTSSEkstern?: string; enhetsId?: string } => ({
    idTSSEkstern: search.idTSSEkstern?.toString(),
    enhetsId: search.enhetsId?.toString(),
  }),
  loader: async ({ context: { queryClient, getSakContextQueryOptions }, params: { templateId } }) => {
    const sakContext = await queryClient.ensureQueryData(getSakContextQueryOptions);
    const letterTemplate = sakContext.brevMetadata.find((letterMetadata) => letterMetadata.id === templateId);
    //vi vil gjerne ha preferredLanguage istedenfor å hente den i komponenten - da slipper vi at den kan være undefined i et par sekunder før den blir hentet
    const preferredLanguage =
      (await queryClient
        .fetchQuery({
          queryKey: getPreferredLanguage.queryKey(sakContext.sak.saksId.toString()),
          queryFn: () => getPreferredLanguage.queryFn(sakContext.sak.saksId.toString()),
        })
        .then((response) => response.spraakKode)) ?? null;

    if (!letterTemplate) {
      throw notFound();
    }

    return { letterTemplate, sak: sakContext.sak, preferredLanguage };
  },
  notFoundComponent: () => {
    // eslint-disable-next-line react-hooks/rules-of-hooks -- this works and is used as an example in the documentation: https://tanstack.com/router/latest/docs/framework/react/guide/not-found-errors#data-loading-inside-notfoundcomponent
    const { templateId } = Route.useParams();
    return (
      <Alert
        css={css`
          height: fit-content;
        `}
        size="small"
        variant="info"
      >
        Fant ikke brevmal med id {templateId}
      </Alert>
    );
  },
});

export function SelectedTemplate() {
  const { letterTemplate, preferredLanguage } = Route.useLoaderData();

  return (
    <VStack
      css={css`
        /* Override form elements to be same size as xsmall headings */
        label,
        legend {
          font-size: var(--a-font-size-heading-xsmall);
        }

        form {
          display: flex;
          flex-direction: column;
          height: 100%;
          justify-content: space-between;
        }
      `}
      gap="4"
    >
      <FavoriteButton />
      <Brevmal letterTemplate={letterTemplate} prefferedLanguage={preferredLanguage} />
    </VStack>
  );
}

function Brevmal({
  letterTemplate,
  prefferedLanguage,
}: {
  letterTemplate: LetterMetadata;
  prefferedLanguage: SpraakKode | null;
}) {
  if (letterTemplate.dokumentkategoriCode === "E_BLANKETT") {
    return <Eblankett letterTemplate={letterTemplate} />;
  }

  switch (letterTemplate.brevsystem) {
    case BrevSystem.DokSys: {
      return <BrevmalForDoksys letterTemplate={letterTemplate} preferredLanguage={prefferedLanguage} />;
    }
    case BrevSystem.Exstream: {
      return <BrevmalForExstream letterTemplate={letterTemplate} preferredLanguage={prefferedLanguage} />;
    }
    case BrevSystem.Brevbaker: {
      return <div>TODO</div>;
    }
  }
}
