import { css } from "@emotion/react";

import type { LetterMetadata } from "~/types/apiTypes";

import type { SubmitTemplateOptions } from "../route";
import { BrevmalBrevbakerKladd } from "./BrevmalBrevbakerKladd";
import { TemplateLoader } from "./TemplateLoader";

const BrevmalPanel = (props: {
  saksId: string;
  templateId?: string;
  brevId?: number;
  brevmetadata: Record<string, LetterMetadata>;
  setOnFormSubmitClick: (v: SubmitTemplateOptions) => void;
  enhetsId: string;
  onAddFavorittSuccess?: (templateId: string) => void;
}) => {
  const visPanel = (props.templateId && props.brevmetadata[props.templateId]) || props.brevId;
  return (
    <>
      {visPanel && (
        <div
          css={css`
            display: flex;
            border-right: 1px solid var(--a-gray-200);
            padding: var(--a-spacing-6);
            height: 100%;
            overflow-y: auto;
          `}
        >
          {props.templateId ? (
            <TemplateLoader
              enhetsId={props.enhetsId}
              letterTemplate={props.brevmetadata[props.templateId]}
              onAddFavorittSuccess={props.onAddFavorittSuccess}
              saksId={props.saksId}
              setOnFormSubmitClick={props.setOnFormSubmitClick}
              templateId={props.templateId}
            />
          ) : (
            <BrevmalBrevbakerKladd
              //visPanel garanterer at vi ikke får undefined
              brevId={props.brevId!}
              brevmetadata={props.brevmetadata}
              saksId={props.saksId.toString()}
              setOnFormSubmitClick={props.setOnFormSubmitClick}
            />
          )}
        </div>
      )}
    </>
  );
};

export default BrevmalPanel;
