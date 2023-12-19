import { Heading } from "@navikt/ds-react";

import type { Title1Block } from "~/types/brevbakerTypes";

import type { BlockProperties } from "../BlockProperties";
import { ContentGroup } from "./ContentGroup";

export const Title1 = ({
  block,
  blockId,
  updateLetter,
  blockStealFocus,
  blockFocusStolen,
  onFocus,
}: BlockProperties<Title1Block>) => (
  <Heading level="2" size="medium">
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
