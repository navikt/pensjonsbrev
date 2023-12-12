import type { ParagraphBlock } from "~/types/brevbakerTypes";

import type { BlockProperties } from "../BlockProperties";
import ContentGroup from "./ContentGroup";
import styles from "./Paragraph.module.css";

const Paragraph = ({
  block,
  blockId,
  updateLetter,
  blockStealFocus,
  blockFocusStolen,
  onFocus,
}: BlockProperties<ParagraphBlock>) => (
  <div className={styles.paragraph}>
    <ContentGroup
      content={block.content}
      editable={block.editable}
      focusStolen={blockFocusStolen}
      id={{ blockId }}
      onFocus={onFocus}
      stealFocus={blockStealFocus}
      updateLetter={updateLetter}
    />
  </div>
);
export default Paragraph;
