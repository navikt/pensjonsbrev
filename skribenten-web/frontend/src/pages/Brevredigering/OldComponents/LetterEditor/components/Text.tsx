import type { TextContent } from "../lib/model/skribenten";
import { LITERAL, VARIABLE } from "../lib/model/skribenten";
import styles from "./Text.module.css";

export interface TextProperties {
  content: TextContent;
}

const Text = ({ content }: TextProperties) => {
  switch (content.type) {
    case LITERAL: {
      return <span>{content.text}</span>;
    }
    case VARIABLE: {
      return <span className={styles.variable}>{content.text}</span>;
    }
  }
};

export default Text;
