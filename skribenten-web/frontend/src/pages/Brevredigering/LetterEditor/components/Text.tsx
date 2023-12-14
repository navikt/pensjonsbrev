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
            border: 1px solid var(--Border-Default, rgba(2, 20, 49, 0.49));
            background: var(--Global-Gray-50, #f2f3f5);
            padding: 1px 4px;
            cursor: default;
          `}
        >
          {content.text}
        </span>
      );
    }
  }
};
