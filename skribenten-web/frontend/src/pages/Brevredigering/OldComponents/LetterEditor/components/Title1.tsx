import type { Title1Block } from "~/types/brevbakerTypes";

import type { BlockProperties } from "../BlockProperties";
import ContentGroup from "./ContentGroup";
import styles from "./Title1.module.css";

const Title1 = ({
  block,
  blockId,
  updateLetter,
  blockStealFocus,
  blockFocusStolen,
  onFocus,
}: BlockProperties<Title1Block>) => (
  <h2 className={styles.container}>
    <ContentGroup
      content={block.content}
      editable={block.editable}
      focusStolen={blockFocusStolen}
      id={{ blockId }}
      onFocus={onFocus}
      stealFocus={blockStealFocus}
      updateLetter={updateLetter}
    />
  </h2>
);

export default Title1;
