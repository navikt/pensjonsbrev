import { type ReactNode } from "react";

import { type AnyBlock } from "~/types/brevbakerTypes";

// The letter title is the page's h1, so title blocks continue the outline from h2.
const HEADING_TAGS = { TITLE1: "h2", TITLE2: "h3", TITLE3: "h4" } as const;

/**
 * Wraps title block content in a heading of the matching level. Paragraph blocks stay unwrapped, since they can also
 * hold lists and tables, which can't be nested in a heading or <p>.
 */
export const BlockHeading = ({ type, children }: { type: AnyBlock["type"]; children: ReactNode }) => {
  if (type === "PARAGRAPH") return children;
  const Heading = HEADING_TAGS[type];
  return <Heading>{children}</Heading>;
};
