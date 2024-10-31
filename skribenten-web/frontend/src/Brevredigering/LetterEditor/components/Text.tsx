import { css } from "@emotion/react";

import type { TextContent } from "~/types/brevbakerTypes";
import { FontType, LITERAL, VARIABLE } from "~/types/brevbakerTypes";

export type TextProperties = {
  content: TextContent;
};

export const Text = ({ content }: TextProperties) => {
  const fontStyle = css`
    ${content?.fontType === FontType.BOLD && "font-weight: bold;"}
    ${content.fontType === FontType.ITALIC && "font-style: italic;"}
  `;

  switch (content.type) {
    case LITERAL: {
      return <span css={fontStyle}>{content.text}</span>;
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
            ${fontStyle}
          `}
        >
          lol{content.text}
        </span>
      );
    }
  }
};
