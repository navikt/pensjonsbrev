import { css } from "@emotion/react";
import { Heading, VStack } from "@navikt/ds-react";

import LetterTemplateTags from "~/components/LetterTemplateTags";
import { type LetterMetadata } from "~/types/apiTypes";

export default function LetterTemplateHeading({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  return (
    <VStack gap="space-8">
      <Heading level="2" size="small">
        {letterTemplate.name}
      </Heading>
      <div
        css={css`
          display: flex;
          align-items: center;
          gap: var(--ax-space-8);
        `}
      >
        <LetterTemplateTags letterTemplate={letterTemplate} />
      </div>
    </VStack>
  );
}
