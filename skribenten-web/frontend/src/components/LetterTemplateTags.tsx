import { Tag } from "@navikt/ds-react";

import { BrevSystem, type LetterMetadata } from "~/types/apiTypes";

function LetterTemplateTags({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  return (
    <>
      {(() => {
        switch (letterTemplate.brevsystem) {
          case BrevSystem.Brevbaker: {
            return (
              <Tag data-color="meta-lime" size="small" variant="outline">
                Skribenten
              </Tag>
            );
          }
          case BrevSystem.Exstream: {
            return (
              <Tag data-color="meta-purple" size="small" variant="outline">
                Exstream
              </Tag>
            );
          }
          case BrevSystem.DokSys: {
            return (
              <Tag data-color="info" size="small" variant="outline">
                Doksys
              </Tag>
            );
          }
        }
      })()}
    </>
  );
}

export default LetterTemplateTags;
