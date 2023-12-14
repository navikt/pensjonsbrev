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
      return <span>{content.text}</span>;
    }
  }
};
