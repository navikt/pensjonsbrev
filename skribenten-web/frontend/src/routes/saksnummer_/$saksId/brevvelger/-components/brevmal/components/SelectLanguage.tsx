import { InlineMessage, Select, VStack } from "@navikt/ds-react";
import { useFormContext } from "react-hook-form";

import type { SpraakKode } from "~/types/apiTypes";
import { SPRAAK_ENUM_TO_TEXT } from "~/types/nameMappings";

function SelectLanguage({
  sorterteSpråk,
  preferredLanguage,
}: {
  sorterteSpråk: SpraakKode[];
  preferredLanguage: SpraakKode | null;
}) {
  const { register, watch } = useFormContext();
  const selectedLanguage = watch("spraak");

  return (
    <VStack gap="space-8">
      <Select {...register("spraak")} data-cy="språk-velger-select" label="Språk" size="small">
        {sorterteSpråk.map((spraak) => (
          <option key={spraak} value={spraak}>
            {SPRAAK_ENUM_TO_TEXT[spraak]} {preferredLanguage === spraak ? "(foretrukket språk)" : ""}
          </option>
        ))}
      </Select>
      {preferredLanguage && selectedLanguage !== preferredLanguage && (
        <InlineMessage size="medium" status="warning">
          Brukers foretrukne språk er {SPRAAK_ENUM_TO_TEXT[preferredLanguage].toLowerCase()}.
        </InlineMessage>
      )}
    </VStack>
  );
}

export default SelectLanguage;
