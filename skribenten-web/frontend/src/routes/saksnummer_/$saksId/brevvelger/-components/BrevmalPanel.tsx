import { css } from "@emotion/react";
import React from "react";

import type { LetterMetadata } from "~/types/apiTypes";

import type { SubmitBrevmalButtonOptions } from "../route";
import { BrevmalBrevbakerKladd } from "./BrevmalBrevbakerKladd";
import { TemplateLoader } from "./TemplateLoader";

const BrevmalPanel = (props: {
  saksId: number;
  templateId?: string;
  brevId?: string;
  letterTemplates: LetterMetadata[];
  setSubmitBrevmalButtonOptions: (s: SubmitBrevmalButtonOptions) => void;
}) => {
  return (
    <div>
      {(props.templateId || props.brevId) && (
        <div
          css={css`
            display: flex;
            width: 389px;
            border-right: 1px solid var(--a-gray-200);
            padding: var(--a-spacing-6);
          `}
        >
          {props.templateId && props.brevId ? (
            <TemplateLoader
              letterTemplate={props.letterTemplates.find((template) => template.id === props.templateId)!}
              saksId={props.saksId}
              setSubmitBrevmalButtonOptions={props.setSubmitBrevmalButtonOptions}
              templateId={props.templateId}
            />
          ) : (
            <>
              {props.templateId && (
                <TemplateLoader
                  letterTemplate={props.letterTemplates.find((template) => template.id === props.templateId)!}
                  saksId={props.saksId}
                  setSubmitBrevmalButtonOptions={props.setSubmitBrevmalButtonOptions}
                  templateId={props.templateId}
                />
              )}
              {props.brevId && (
                <BrevmalBrevbakerKladd
                  brevId={props.brevId}
                  letterTemplates={props.letterTemplates}
                  saksId={props.saksId.toString()}
                  setSubmitBrevmalButtonOptions={props.setSubmitBrevmalButtonOptions}
                />
              )}
            </>
          )}
        </div>
      )}
    </div>
  );
};

export default BrevmalPanel;
