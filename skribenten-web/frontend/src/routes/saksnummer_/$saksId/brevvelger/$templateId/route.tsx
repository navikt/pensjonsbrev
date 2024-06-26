import { css } from "@emotion/react";
import { Alert, VStack } from "@navikt/ds-react";
import { queryOptions, useQuery } from "@tanstack/react-query";
import { createFileRoute, notFound } from "@tanstack/react-router";

import { getPreferredLanguage } from "~/api/skribenten-api-endpoints";
import type { LetterMetadata } from "~/types/apiTypes";
import type { SpraakKode } from "~/types/apiTypes";
import { BrevSystem } from "~/types/apiTypes";

import BrevmalForDoksys from "./-components/BrevmalDoksys";
import BrevmalForExstream from "./-components/BrevmalExstream";
import Eblankett from "./-components/EBlankett";
import FavoriteButton from "./-components/FavoriteButton";

const postsQueryOptions = (sakId: string) =>
  queryOptions({
    queryKey: getPreferredLanguage.queryKey(sakId),
    queryFn: () => getPreferredLanguage.queryFn(sakId),
  });

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
    await queryClient.ensureQueryData(postsQueryOptions(sakContext.sak.saksId.toString()));

    if (!letterTemplate) {
      throw notFound();
    }

    return { letterTemplate, sak: sakContext.sak };
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
  const { sak, letterTemplate } = Route.useLoaderData();

  const preferredLanguage =
    useQuery({
      queryKey: getPreferredLanguage.queryKey(sak.saksId.toString()),
      queryFn: () => getPreferredLanguage.queryFn(sak.saksId.toString()),
    })?.data?.spraakKode ?? null;

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
