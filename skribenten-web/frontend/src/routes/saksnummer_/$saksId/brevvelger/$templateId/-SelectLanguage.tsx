import { Select } from "@navikt/ds-react";
import { useFormContext } from "react-hook-form";

import { usePreferredLanguage } from "~/hooks/usePreferredLanguage";
import type { LetterMetadata } from "~/types/apiTypes";
import { SPRAAK_ENUM_TO_TEXT } from "~/types/nameMappings";

import { Route } from "./route";

function SelectLanguage({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  const { saksId } = Route.useParams();
  const { vedtaksId } = Route.useSearch();
  const { register } = useFormContext();
  const preferredLanguage = usePreferredLanguage(saksId, vedtaksId);

  return (
    <Select {...register("spraak")} label="Språk" size="medium">
      {letterTemplate.spraak.toSorted().map((spraak) => (
        <option key={spraak} value={spraak}>
          {SPRAAK_ENUM_TO_TEXT[spraak]} {preferredLanguage === spraak ? "(foretrukket språk)" : ""}
        </option>
      ))}
    </Select>
  );
}

export default SelectLanguage;
