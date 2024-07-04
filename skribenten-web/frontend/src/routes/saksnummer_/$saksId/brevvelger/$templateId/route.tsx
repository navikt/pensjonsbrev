import { css } from "@emotion/react";
import { Alert, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute, notFound } from "@tanstack/react-router";
import { useMemo } from "react";

import { getPreferredLanguage } from "~/api/skribenten-api-endpoints";
import type { LetterMetadata } from "~/types/apiTypes";
import type { SpraakKode } from "~/types/apiTypes";
import { BrevSystem } from "~/types/apiTypes";

import BrevmalBrevbaker from "./-components/BrevmalBrevbaker";
import BrevmalForDoksys from "./-components/BrevmalDoksys";
import BrevmalForExstream from "./-components/BrevmalExstream";
import Eblankett from "./-components/EBlankett";
import FavoriteButton from "./-components/FavoriteButton";

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
    await queryClient.ensureQueryData({
      queryKey: getPreferredLanguage.queryKey(sakContext.sak.saksId.toString()),
      queryFn: () => getPreferredLanguage.queryFn(sakContext.sak.saksId.toString()),
    });

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
      <Brevmal letterTemplate={letterTemplate} preferredLanguage={preferredLanguage} />
    </VStack>
  );
}

function Brevmal({
  letterTemplate,
  preferredLanguage,
}: {
  letterTemplate: LetterMetadata;
  preferredLanguage: SpraakKode | null;
}) {
  /*
    språk i brevene kan komme i forskjellige rekkefølge, siden de også har forskjellige verdier.
    Dette er for at alle language-inputtene skal ha samme rekkefølge.
    Samtidig, er det behov å bruke denne for å sjekke hva defaultValue skal være
    */
  const displayLanguages = useMemo(() => {
    return letterTemplate.spraak.toSorted();
  }, [letterTemplate.spraak]);

  const defaultValuesDoksysOgExstream = useMemo(() => {
    return {
      isSensitive: undefined,
      brevtittel: "",
      // preferredLanguage finnes ikke nødvendigvis akkurat ved side last - Når vi får den lastet, vil vi ha den forhåndsvalgt, hvis brevet også støtter på språket.
      spraak:
        preferredLanguage && displayLanguages.includes(preferredLanguage) ? preferredLanguage : displayLanguages[0],
      enhetsId: "",
    };
  }, [preferredLanguage, displayLanguages]);

  if (letterTemplate.dokumentkategoriCode === "E_BLANKETT") {
    return <Eblankett letterTemplate={letterTemplate} />;
  }

  switch (letterTemplate.brevsystem) {
    case BrevSystem.DokSys: {
      return (
        <BrevmalForDoksys
          defaultValues={defaultValuesDoksysOgExstream}
          displayLanguages={displayLanguages}
          letterTemplate={letterTemplate}
          preferredLanguage={preferredLanguage}
        />
      );
    }
    case BrevSystem.Exstream: {
      return (
        <BrevmalForExstream
          defaultValues={defaultValuesDoksysOgExstream}
          displayLanguages={displayLanguages}
          letterTemplate={letterTemplate}
          preferredLanguage={preferredLanguage}
        />
      );
    }
    case BrevSystem.Brevbaker: {
      return (
        <BrevmalBrevbaker
          defaultValues={defaultValuesDoksysOgExstream}
          displayLanguages={displayLanguages}
          letterTemplate={letterTemplate}
          preferredLanguage={preferredLanguage}
        />
      );
    }
  }
}
