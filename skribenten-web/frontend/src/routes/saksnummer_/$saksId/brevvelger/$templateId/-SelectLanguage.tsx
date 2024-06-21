import { Select } from "@navikt/ds-react";
import { useEffect } from "react";
import { useFormContext } from "react-hook-form";

import { usePreferredLanguage } from "~/hooks/usePreferredLanguage";
import type { LetterMetadata } from "~/types/apiTypes";
import { SPRAAK_ENUM_TO_TEXT } from "~/types/nameMappings";

import { Route } from "./route";

function SelectLanguage({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  const { saksId } = Route.useParams();
  const { vedtaksId } = Route.useSearch();
  const { setValue, register } = useFormContext();
  const preferredLanguage = usePreferredLanguage(saksId, vedtaksId);

  // Update selected language if preferredLanguage was not loaded before form initialization.
  useEffect(() => {
    if (preferredLanguage && letterTemplate.spraak.includes(preferredLanguage)) {
      setValue("spraak", preferredLanguage);
    }
  }, [preferredLanguage, setValue, letterTemplate.spraak]);

  return (
    <Select {...register("spraak")} label="Språk" size="medium">
      {letterTemplate.spraak.map((spraak) => (
        <option key={spraak} value={spraak}>
          {SPRAAK_ENUM_TO_TEXT[spraak]} {preferredLanguage === spraak ? "(foretrukket språk)" : ""}
        </option>
      ))}
    </Select>
  );
}

export default SelectLanguage;
