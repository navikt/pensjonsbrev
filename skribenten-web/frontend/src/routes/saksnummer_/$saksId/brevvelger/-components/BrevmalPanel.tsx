import { css } from "@emotion/react";

import type { LetterMetadata } from "~/types/apiTypes";

import type { SubmitTemplateOptions } from "../route";
import { BrevmalBrevbakerKladd } from "./BrevmalBrevbakerKladd";
import { TemplateLoader } from "./TemplateLoader";

const BrevmalPanel = (props: {
  saksId: number;
  templateId?: string;
  brevId?: string;
  letterTemplates: LetterMetadata[];
  setOnFormSubmitClick: (v: SubmitTemplateOptions) => void;
  enhetsId: string;
}) => {
  return (
    <div>
      {(props.templateId || props.brevId) && (
        <div
          css={css`
            display: flex;
            max-width: 389px;
            border-right: 1px solid var(--a-gray-200);
            padding: var(--a-spacing-6);
            height: 100%;
          `}
        >
          {props.templateId ? (
            <TemplateLoader
              enhetsId={props.enhetsId}
              letterTemplate={props.letterTemplates.find((template) => template.id === props.templateId)!}
              saksId={props.saksId}
              setOnFormSubmitClick={props.setOnFormSubmitClick}
              templateId={props.templateId}
            />
          ) : (
            <BrevmalBrevbakerKladd
              //linje 18 garanterer at vi ikke får undefined
              brevId={props.brevId!}
              letterTemplates={props.letterTemplates}
              saksId={props.saksId.toString()}
              setOnFormSubmitClick={props.setOnFormSubmitClick}
            />
          )}
        </div>
      )}
    </div>
  );
};

export default BrevmalPanel;
