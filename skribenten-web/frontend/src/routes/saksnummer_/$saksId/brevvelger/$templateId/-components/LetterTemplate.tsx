import { css } from "@emotion/react";
import { Heading, Tag } from "@navikt/ds-react";

import { BrevSystem, type LetterMetadata } from "~/types/apiTypes";

export default function LetterTemplateHeading({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  return (
    <div>
      <Heading level="2" size="small">
        {letterTemplate.name}
      </Heading>
      <div
        css={css`
          display: flex;
          align-items: center;
          gap: var(--a-spacing-3);
          margin-top: var(--a-spacing-2);
        `}
      >
        <LetterTemplateTags letterTemplate={letterTemplate} />
      </div>
    </div>
  );
}

export function LetterTemplateTags({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  return (
    <div>
      {(() => {
        switch (letterTemplate.brevsystem) {
          case BrevSystem.Brevbaker: {
            return (
              <Tag size="small" variant="alt2-moderate">
                Brevbaker
              </Tag>
            );
          }
          case BrevSystem.Exstream: {
            return (
              <Tag size="small" variant="alt1-moderate">
                Exstream
              </Tag>
            );
          }
          case BrevSystem.DokSys: {
            return (
              <Tag size="small" variant="alt3-moderate">
                Doksys
              </Tag>
            );
          }
        }
      })()}
    </div>
  );
}
