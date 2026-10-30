import { HStack, Tag } from "@navikt/ds-react";

import { BrevSystem, type LetterMetadata } from "~/types/apiTypes";

function LetterTemplateTags({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  return (
    <HStack gap="space-8">
      {(() => {
        switch (letterTemplate.brevsystem) {
          case BrevSystem.Brevbaker: {
            return (
              <Tag data-color="meta-lime" size="small" variant="moderate">
                Skribenten
              </Tag>
            );
          }
          case BrevSystem.Exstream: {
            return (
              <Tag data-color="meta-purple" size="small" variant="moderate">
                Exstream
              </Tag>
            );
          }
        }
      })()}
      {letterTemplate.dokumentkategoriCode === "E_BLANKETT" && (
        <Tag data-color="neutral" size="small" variant="moderate">
          E-blankett
        </Tag>
      )}
    </HStack>
  );
}

export default LetterTemplateTags;
