import { Tag } from "@navikt/ds-react";

import { BrevSystem, type LetterMetadata } from "~/types/apiTypes";

function LetterTemplateTags({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  return (
    <div>
      {(() => {
        switch (letterTemplate.brevsystem) {
          case BrevSystem.Brevbaker: {
            return (
              <Tag size="small" variant="alt2">
                Brevbaker
              </Tag>
            );
          }
          case BrevSystem.Exstream: {
            return (
              <Tag size="small" variant="alt1">
                Exstream
              </Tag>
            );
          }
          case BrevSystem.DokSys: {
            return (
              <Tag size="small" variant="alt3">
                Doksys
              </Tag>
            );
          }
        }
      })()}
    </div>
  );
}

export default LetterTemplateTags;
