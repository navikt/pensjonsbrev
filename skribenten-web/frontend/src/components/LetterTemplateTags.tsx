import { BoxNew, Tag } from "@navikt/ds-react";

import { BrevSystem, type LetterMetadata } from "~/types/apiTypes";

function LetterTemplateTags({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  return (
    <>
      {(() => {
        switch (letterTemplate.brevsystem) {
          case BrevSystem.Brevbaker: {
            return (
              <BoxNew asChild width="fit-content">
                <Tag size="small" variant="alt2">
                  Skribenten
                </Tag>
              </BoxNew>
            );
          }
          case BrevSystem.Exstream: {
            return (
              <BoxNew asChild width="fit-content">
                <Tag size="small" variant="alt1">
                  Exstream
                </Tag>
              </BoxNew>
            );
          }
          case BrevSystem.DokSys: {
            return (
              <BoxNew asChild width="fit-content">
                <Tag size="small" variant="alt3">
                  Doksys
                </Tag>
              </BoxNew>
            );
          }
        }
      })()}
    </>
  );
}

export default LetterTemplateTags;
