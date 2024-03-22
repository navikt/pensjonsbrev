import { css } from "@emotion/react";

import type { TextContent } from "~/types/brevbakerTypes";
import { LITERAL, VARIABLE } from "~/types/brevbakerTypes";

export type TextProperties = {
  content: TextContent;
};

export const Text = ({ content }: TextProperties) => {
  switch (content.type) {
    case LITERAL: {
      return <span>{content.text}</span>;
    }
    case VARIABLE: {
      return (
        <span
          css={css`
            border-radius: 4px;
            border: 1px solid var(--a-border-default);
            background: var(--a-gray-50);
            padding: 1px 4px;
            display: inline-block;
            margin: 0 1px;
            cursor: default;
          `}
        >
          {content.text}
        </span>
      );
    }
  }
};
