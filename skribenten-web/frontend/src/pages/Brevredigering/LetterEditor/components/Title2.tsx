import { Heading } from "@navikt/ds-react";

import type { Title2Block } from "~/types/brevbakerTypes";

import type { BlockProperties } from "../BlockProperties";
import { ContentGroup } from "./ContentGroup";

export const Title2 = ({
  block,
  blockId,
  updateLetter,
  blockStealFocus,
  blockFocusStolen,
  onFocus,
}: BlockProperties<Title2Block>) => (
  <Heading level="3" size="small">
    <ContentGroup
      content={block.content}
      editable={block.editable}
      focusStolen={blockFocusStolen}
      id={{ blockId }}
      onFocus={onFocus}
      stealFocus={blockStealFocus}
      updateLetter={updateLetter}
    />
  </Heading>
);
