import { css } from "@emotion/react";
import { useQuery } from "@tanstack/react-query";
import { useMemo } from "react";

import { getPreferredLanguage } from "~/api/skribenten-api-endpoints";
import type { LetterMetadata } from "~/types/apiTypes";
import type { SpraakKode } from "~/types/apiTypes";
import { BrevSystem } from "~/types/apiTypes";
import { SPRAAK_ENUM_TO_TEXT } from "~/types/nameMappings";

import type { SubmitTemplateOptions } from "../route";
import BrevmalBrevbaker from "./brevmal/BrevmalBrevbaker";
import BrevmalForDoksys from "./brevmal/BrevmalDoksys";
import BrevmalForExstream from "./brevmal/BrevmalExstream";
import Eblankett from "./brevmal/EBlankett";
import { hentDefaultValueForSpråk } from "./brevmal/TemplateUtils";
import FavoriteButton from "./FavoriteButton";

export const TemplateLoader = (props: {
  saksId: number;
  templateId: string;
  letterTemplate: LetterMetadata;
  setOnFormSubmitClick: (v: SubmitTemplateOptions) => void;
  enhetsId: string;
  onAddFavorittSuccess?: (templateId: string) => void;
}) => {
  const preferredLanguage =
    useQuery({
      queryKey: getPreferredLanguage.queryKey(props.saksId.toString()),
      queryFn: () => getPreferredLanguage.queryFn(props.saksId.toString()),
    })?.data?.spraakKode ?? null;

  return (
    <div
      css={css`
        display: flex;
        flex-direction: column;
        gap: 1rem;
        overflow-y: auto;
      `}
    >
      <FavoriteButton onAddFavorittSuccess={props.onAddFavorittSuccess} templateId={props.templateId} />
      <Brevmal
        enhetsId={props.enhetsId}
        letterTemplate={props.letterTemplate}
        preferredLanguage={preferredLanguage}
        saksId={props.saksId.toString()}
        setOnFormSubmitClick={props.setOnFormSubmitClick}
        templateId={props.templateId}
      />
    </div>
  );
};

function Brevmal({
  letterTemplate,
  preferredLanguage,
  saksId,
  templateId,
  setOnFormSubmitClick,
  enhetsId,
}: {
  letterTemplate: LetterMetadata;
  preferredLanguage: SpraakKode | null;
  saksId: string;
  templateId: string;
  enhetsId: string;
  setOnFormSubmitClick: (v: SubmitTemplateOptions) => void;
}) {
  /*
    språk i brevene kan komme i forskjellige rekkefølge, siden de også har forskjellige verdier.
    Dette er for at alle language-inputtene skal ha samme rekkefølge.
    Samtidig, er det behov å bruke denne for å sjekke hva defaultValue skal være
    */
  const displayLanguages = letterTemplate.spraak.toSorted((a, b) =>
    SPRAAK_ENUM_TO_TEXT[a].localeCompare(SPRAAK_ENUM_TO_TEXT[b]),
  );

  const defaultValues = useMemo(() => {
    return {
      isSensitive: undefined,
      brevtittel: "",
      spraak: hentDefaultValueForSpråk(preferredLanguage, displayLanguages),
      enhetsId: enhetsId,
    };
  }, [displayLanguages, preferredLanguage, enhetsId]);

  if (letterTemplate.dokumentkategoriCode === "E_BLANKETT") {
    return <Eblankett letterTemplate={letterTemplate} setOnFormSubmitClick={setOnFormSubmitClick} />;
  }

  switch (letterTemplate.brevsystem) {
    case BrevSystem.DokSys: {
      return (
        <BrevmalForDoksys
          defaultValues={defaultValues}
          displayLanguages={displayLanguages}
          letterTemplate={letterTemplate}
          preferredLanguage={preferredLanguage}
          saksId={saksId}
          setOnFormSubmitClick={setOnFormSubmitClick}
          templateId={templateId}
        />
      );
    }
    case BrevSystem.Exstream: {
      return (
        <BrevmalForExstream
          defaultValues={defaultValues}
          displayLanguages={displayLanguages}
          letterTemplate={letterTemplate}
          preferredLanguage={preferredLanguage}
          saksId={saksId}
          setOnFormSubmitClick={setOnFormSubmitClick}
          templateId={templateId}
        />
      );
    }
    case BrevSystem.Brevbaker: {
      return (
        <BrevmalBrevbaker
          defaultValues={defaultValues}
          displayLanguages={displayLanguages}
          letterTemplate={letterTemplate}
          preferredLanguage={preferredLanguage}
          saksId={saksId}
          setOnFormSubmitClick={setOnFormSubmitClick}
        />
      );
    }
  }
}
