import { css } from "@emotion/react";
import { Accordion, ExpansionCard, HStack } from "@navikt/ds-react";
import type { Dispatch } from "react";
import React from "react";
import { useEffect, useState } from "react";

import { isItemContentIndex, isNew, text as textOf } from "~/Brevredigering/LetterEditor/actions/common";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import type { Focus, LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { isFritekst, isLiteral, isTextContent } from "~/Brevredigering/LetterEditor/model/utils";
import { getCaretRect, getRange } from "~/Brevredigering/LetterEditor/services/caretUtils";
import type { AnyBlock, Block, Content, Item } from "~/types/brevbakerTypes";

export function DebugPanel() {
  const { freeze, editorState } = useEditor();
  const [mousePosition, setMousePosition] = useState({ x: 0, y: 0 });
  const [caretOffset, setCaretOffset] = useState(0);
  const [caretRect, setCaretRect] = useState<DOMRect>();

  const mouseMoveEventListener = (event: MouseEvent) => {
    setMousePosition({ x: event.pageX, y: event.pageY });
  };

  const keyboardEventListener = () => {
    setCaretOffset(getRange()?.startOffset ?? 0);
    setCaretRect(getCaretRect());
  };

  useEffect(() => {
    document.addEventListener("mousemove", mouseMoveEventListener);
    document.addEventListener("keyup", keyboardEventListener);

    return () => {
      document.removeEventListener("mousemove", mouseMoveEventListener);
      document.removeEventListener("keyup", keyboardEventListener);
    };
  }, []);

  return (
    <div
      css={css`
        padding: 1rem;
        margin-top: 1rem;
        align-self: flex-start;
        background: var(--a-blue-200);
        width: 100%;
      `}
    >
      <HStack gap={"4"}>
        FOCUS:
        {Object.entries(editorState.focus).map(([key, value]) => (
          <div key={key}>
            <b>{key}: </b>
            <span>{value}</span>
          </div>
        ))}
      </HStack>
      <HStack gap={"4"}>
        FREEZE: <b>{freeze.toString()}</b>
      </HStack>
      <HStack gap={"4"}>
        MOUSE:
        <b>X: {mousePosition.x}</b>
        <b>Y: {mousePosition.y}</b>
      </HStack>
      <HStack gap={"4"}>
        CARET:
        <b>offset: {caretOffset}</b>
        <b>X: {caretRect?.x}</b>
        <b>Y: {caretRect?.y}</b>
      </HStack>
      <LetterTree state={editorState} />
    </div>
  );
}

const LetterTree = ({ state: { focus, redigertBrev } }: { state: LetterEditorState }) => {
  return (
    <>
      {redigertBrev.blocks.map((b, index) => (
        <Block block={b} focus={focus} index={index} key={index} />
      ))}
    </>
  );
};

const useToggleWithFocusUpdate = (
  currentFocus: number | undefined,
  thisIndex: number,
): [boolean, Dispatch<React.SetStateAction<boolean>>] => {
  const [isOpen, setIsOpen] = useState(currentFocus === thisIndex);
  const [prevFocus, setPrevFocus] = useState(currentFocus);
  if (currentFocus !== prevFocus) {
    setPrevFocus(currentFocus);
    setIsOpen(currentFocus === thisIndex);
  }
  return [isOpen, setIsOpen];
};

const Block = ({ block, focus, index }: { block: AnyBlock; focus: Focus; index: number }) => {
  const blockText = block.content
    .map((c) => (isTextContent(c) ? textOf(c) : null))
    .filter((c) => c !== null)
    .join("");
  const highlightColor = getHighlightColor(isNew(block), isEdited(block));

  const [isOpen, setIsOpen] = useToggleWithFocusUpdate(focus.blockIndex, index);

  return (
    <ExpansionCard
      aria-label={`${block.type} - ${index} - ${block.id}`}
      css={css`
        background: var(${highlightColor});
      `}
      onToggle={setIsOpen}
      open={isOpen}
      size={"small"}
    >
      <ExpansionCard.Header>
        <ExpansionCard.Title>
          <HStack gap={"4"}>
            <span>{block.type}</span>
            <span>Index: {index}</span>
            <span>ID: {block.id ?? "NEW"}</span>
          </HStack>
        </ExpansionCard.Title>
        <ExpansionCard.Description>{textExtract(blockText)}</ExpansionCard.Description>
      </ExpansionCard.Header>
      <ExpansionCard.Content>
        <Accordion headingSize={"xsmall"} size={"small"}>
          {block.content.map((c, index) => (
            <Content content={c} focus={focus} index={index} key={index} />
          ))}
        </Accordion>
      </ExpansionCard.Content>
    </ExpansionCard>
  );
};

const Content = ({ content, focus, index }: { content: Content; focus?: Focus; index: number }) => {
  const extract = isTextContent(content) ? textExtract(textOf(content)) : "";
  const highlightColor = getHighlightColor(isNew(content), isEdited(content));

  const [isOpen, setIsOpen] = useToggleWithFocusUpdate(focus?.contentIndex, index);

  return (
    <Accordion.Item
      css={css`
        background: var(${highlightColor});
      `}
      onOpenChange={setIsOpen}
      open={isOpen}
    >
      <Accordion.Header>
        <HStack gap={"4"}>
          <span>{content.type}</span>
          <span>Index: {index}</span>
          <span>ID: {content.id ?? "NEW"}</span>
          <span>
            {isLiteral(content) && isFritekst(content) && "Fritekst: "}
            {extract}
          </span>
        </HStack>
      </Accordion.Header>
      <Accordion.Content>
        <ContentBody content={content} focus={focus} />
      </Accordion.Content>
    </Accordion.Item>
  );
};

const ContentBody = ({ content, focus }: { content: Content; focus?: Focus }) => {
  switch (content.type) {
    case "NEW_LINE":
      return "--new line--";
    case "ITEM_LIST":
      return (
        <Accordion headingSize={"xsmall"} size={"small"}>
          {content.items.map((i, index) => (
            <ItemBody focus={focus} index={index} item={i} key={index} />
          ))}
        </Accordion>
      );
    case "LITERAL":
    case "VARIABLE":
      return textOf(content);
  }
};

const ItemBody = ({ focus, index, item }: { focus?: Focus; index: number; item: Item }) => {
  const itemContentFocus =
    focus && isItemContentIndex(focus)
      ? { blockIndex: focus.blockIndex, contentIndex: focus.itemContentIndex }
      : undefined;

  const [isOpen, setIsOpen] = useToggleWithFocusUpdate(
    isItemContentIndex(focus) ? focus.itemContentIndex : undefined,
    index,
  );

  const itemText = item.content.map(textOf).join("");
  const highlightColor = getHighlightColor(isNew(item), isEditedItem(item));

  return (
    <Accordion.Item
      css={css`
        background: var(${highlightColor});
      `}
      onOpenChange={setIsOpen}
      open={isOpen}
    >
      <Accordion.Header>
        <HStack gap={"4"}>
          <span>Index: {index}</span>
          <span>ID: {item.id ?? "NEW"}</span>
          <span>{textExtract(itemText)}</span>
        </HStack>
      </Accordion.Header>
      <Accordion.Content>
        <Accordion headingSize={"xsmall"} size={"small"}>
          {item.content.map((c, index) => (
            <Content content={c} focus={itemContentFocus} index={index} key={index} />
          ))}
        </Accordion>
      </Accordion.Content>
    </Accordion.Item>
  );
};

function getHighlightColor(isNew: boolean, isEdited: boolean): string {
  return isNew ? "--a-surface-success-subtle" : isEdited ? "--a-surface-info-subtle" : "--a-surface-default";
}

function isEdited(content: Content | AnyBlock): boolean {
  switch (content.type) {
    case "NEW_LINE":
      return isNew(content);
    case "ITEM_LIST":
      return isNew(content) || content.deletedItems.length > 0 || content.items.some(isEditedItem);
    case "LITERAL":
      return isNew(content) || content.editedText !== null || content.editedFontType !== null;
    case "VARIABLE":
      return false;
    case "TITLE1":
    case "TITLE2":
    case "PARAGRAPH":
      return (
        isNew(content) ||
        (content.originalType !== null ? content.type !== content.originalType : false) ||
        content.deletedContent.length > 0 ||
        content.content.some(isEdited)
      );
  }
  return false;
}

function isEditedItem(item: Item): boolean {
  return isNew(item) || item.deletedContent.length > 0 || item.content.some(isEdited);
}

function textExtract(str: string, maxLength: number = 65): string {
  return str.length > maxLength ? str.slice(0, 62) + "..." : str;
}
