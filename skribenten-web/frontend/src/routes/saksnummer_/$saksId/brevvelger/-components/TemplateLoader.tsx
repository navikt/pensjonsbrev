import { css } from "@emotion/react";
import { useQuery } from "@tanstack/react-query";
import { useMemo } from "react";

import { getPreferredLanguage } from "~/api/skribenten-api-endpoints";
import type { LetterMetadata } from "~/types/apiTypes";
import type { SpraakKode } from "~/types/apiTypes";
import { BrevSystem } from "~/types/apiTypes";
import { SPRAAK_ENUM_TO_TEXT } from "~/types/nameMappings";

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
  setNestebutton: (el: React.ReactNode) => void;
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
      `}
    >
      <FavoriteButton templateId={props.templateId} />
      <Brevmal
        letterTemplate={props.letterTemplate}
        preferredLanguage={preferredLanguage}
        saksId={props.saksId.toString()}
        setNestebutton={props.setNestebutton}
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
  setNestebutton,
}: {
  letterTemplate: LetterMetadata;
  preferredLanguage: SpraakKode | null;
  saksId: string;
  templateId: string;
  setNestebutton: (el: React.ReactNode) => void;
}) {
  /*
    språk i brevene kan komme i forskjellige rekkefølge, siden de også har forskjellige verdier.
    Dette er for at alle language-inputtene skal ha samme rekkefølge.
    Samtidig, er det behov å bruke denne for å sjekke hva defaultValue skal være
    */
  const displayLanguages = useMemo(() => {
    return letterTemplate.spraak.toSorted((a, b) => SPRAAK_ENUM_TO_TEXT[a].localeCompare(SPRAAK_ENUM_TO_TEXT[b]));
  }, [letterTemplate.spraak]);

  const defaultValues = useMemo(() => {
    return {
      isSensitive: undefined,
      brevtittel: "",
      spraak: hentDefaultValueForSpråk(preferredLanguage, displayLanguages),
      enhetsId: "",
    };
  }, [preferredLanguage, displayLanguages]);

  if (letterTemplate.dokumentkategoriCode === "E_BLANKETT") {
    return <Eblankett letterTemplate={letterTemplate} setNestebutton={setNestebutton} />;
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
          setNestebutton={setNestebutton}
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
          setNestebutton={setNestebutton}
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
          setNestebutton={setNestebutton}
        />
      );
    }
  }
}
