import React, { useCallback } from "react";
import type { ContentEditableEvent } from "react-contenteditable";
import ContentEditable from "react-contenteditable";

import type { LiteralValue } from "~/types/brevbakerTypes";

import type { BoundAction } from "../lib/actions";
import { isEmptyContent } from "../model/utils";
import styles from "./Text.module.css";

export type EditableTextProperties = {
  content: LiteralValue;
  updateText: BoundAction<[text: string]>;
  innerRef: BoundAction<[node: HTMLElement]>;
};

function onChangeHandler(updateText: BoundAction<[text: string]>): (event: ContentEditableEvent) => void {
  return (event: ContentEditableEvent) => {
    updateText(event.target.value);
  };
}

export const EditableText = ({ content, updateText, innerRef }: EditableTextProperties) => {
  // const updateText = bindAction(TextContentAction.updateText, updateContent, content)
  // Passing innerRef as a dependency has some weird consequences such as the cursor skipping to the end on every edit.
  const reference = useCallback((node: HTMLElement) => {
    innerRef(node);
  }, []);

  return (
    <ContentEditable
      className={isEmptyContent(content) ? styles.empty : ""}
      html={content.text || "​"}
      innerRef={reference}
      onChange={onChangeHandler(updateText)}
      tagName="span"
    />
  );
};
