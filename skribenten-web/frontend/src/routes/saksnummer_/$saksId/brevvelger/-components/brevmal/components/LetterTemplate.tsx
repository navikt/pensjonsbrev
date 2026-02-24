import { Heading, VStack } from "@navikt/ds-react";

import LetterTemplateTags from "~/components/LetterTemplateTags";
import type { LetterMetadata } from "~/types/apiTypes";

export default function LetterTemplateHeading({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  return (
    <VStack align="start" gap="space-8">
      <Heading level="2" size="small">
        {letterTemplate.name}
      </Heading>
      <LetterTemplateTags letterTemplate={letterTemplate} />
    </VStack>
  );
}
