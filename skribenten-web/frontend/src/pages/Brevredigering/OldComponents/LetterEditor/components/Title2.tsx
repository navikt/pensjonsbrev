import type { Title2Block } from "~/types/brevbakerTypes";

import type { BlockProperties } from "../BlockProperties";
import ContentGroup from "./ContentGroup";
import styles from "./Title2.module.css";

const Title2 = ({
  block,
  blockId,
  updateLetter,
  blockStealFocus,
  blockFocusStolen,
  onFocus,
}: BlockProperties<Title2Block>) => (
  <h3 className={styles.container}>
    <ContentGroup
      content={block.content}
      editable={block.editable}
      focusStolen={blockFocusStolen}
      id={{ blockId }}
      onFocus={onFocus}
      stealFocus={blockStealFocus}
      updateLetter={updateLetter}
    />
  </h3>
);

export default Title2;
