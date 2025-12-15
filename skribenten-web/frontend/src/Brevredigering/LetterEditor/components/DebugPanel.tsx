import { css, Global } from "@emotion/react";
import { Accordion, BodyShort, BoxNew, ExpansionCard, HStack, VStack } from "@navikt/ds-react";
import type { Dispatch } from "react";
import React from "react";
import { useEffect, useState } from "react";

import { isItemContentIndex, isNew, text as textOf } from "~/Brevredigering/LetterEditor/actions/common";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import type { Focus, LetterEditorState, SelectionIndex } from "~/Brevredigering/LetterEditor/model/state";
import { isFritekst, isLiteral, isTextContent } from "~/Brevredigering/LetterEditor/model/utils";
import { getCaretRect, getRange, getSelectionFocus } from "~/Brevredigering/LetterEditor/services/caretUtils";
import type { AnyBlock, Block, Content, Item } from "~/types/brevbakerTypes";

export function DebugPanel() {
  const { freeze, editorState } = useEditor();
  const [mousePosition, setMousePosition] = useState({ x: 0, y: 0 });
  const [caretOffset, setCaretOffset] = useState(0);
  const [caretRect, setCaretRect] = useState<DOMRect>();

  const [mappedSelection, setMappedSelection] = useState<SelectionIndex | null>(null);

  const mouseMoveEventListener = (event: MouseEvent) => {
    setMousePosition({ x: event.pageX, y: event.pageY });
  };

  const keyboardEventListener = () => {
    setCaretOffset(getRange()?.startOffset ?? 0);
    setCaretRect(getCaretRect());
  };

  const selectionChangeListener = () => {
    const root = document.querySelector<HTMLElement>("[data-editor-root]");
    const mapped = root ? getSelectionFocus(root) : undefined;
    if (mapped) setMappedSelection(mapped);
  };

  useEffect(() => {
    document.addEventListener("mousemove", mouseMoveEventListener);
    document.addEventListener("keyup", keyboardEventListener);
    document.addEventListener("selectionchange", selectionChangeListener);

    return () => {
      document.removeEventListener("mousemove", mouseMoveEventListener);
      document.removeEventListener("keyup", keyboardEventListener);
      document.removeEventListener("selectionchange", selectionChangeListener);
    };
  }, []);

  return (
    <>
      <Global
        styles={css`
          .editor {
            [contenteditable] {
              &:focus-within {
                outline: 1px solid var(--ax-border-brand-magenta-subtle);
              }
              outline: 1px solid var(--ax-border-brand-magenta-subtle);
            }
          }
        `}
      />
      <BoxNew background="neutral-soft" marginBlock="space-16 0">
        <VStack padding="space-16">
          <HStack gap="space-16">
            {mappedSelection ? (
              <>
                <VStack>
                  <HStack gap="space-16">
                    SELECTION &lt;:
                    <Focus focus={mappedSelection.start} />
                  </HStack>
                  <HStack gap="space-16">
                    SELECTION &gt;:
                    <Focus focus={mappedSelection.end} />
                  </HStack>
                </VStack>
              </>
            ) : null}
          </HStack>
          <HStack gap="space-16">
            FOCUS:
            <Focus focus={editorState.focus} />
          </HStack>
          <HStack gap="space-16">
            FREEZE:{" "}
            <BodyShort color={freeze ? "red" : "black"} weight="semibold">
              {freeze.toString()}
            </BodyShort>
            SAVESTATUS: <BodyShort weight="semibold">{editorState.saveStatus}</BodyShort>
          </HStack>
          <HStack gap="space-16">
            MOUSE:
            <BodyShort weight="semibold">X: {mousePosition.x}</BodyShort>
            <BodyShort weight="semibold">Y: {mousePosition.y}</BodyShort>
          </HStack>
          <HStack gap="space-16">
            CARET:
            <BodyShort weight="semibold">offset: {caretOffset}</BodyShort>
            <BodyShort weight="semibold">X: {caretRect?.x}</BodyShort>
            <BodyShort weight="semibold">Y: {caretRect?.y}</BodyShort>
          </HStack>
          <LetterTree state={editorState} />
        </VStack>
      </BoxNew>
    </>
  );
}

const Focus = ({ focus }: { focus: Focus }) => {
  return (
    <>
      {Object.entries(focus).map(([key, value]) => (
        <div key={key}>
          <BodyShort as="span" weight="semibold">
            {key.replaceAll("Index", "")}:{" "}
          </BodyShort>
          <span>{value}</span>
        </div>
      ))}
    </>
  );
};

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

  const [isOpen, setIsOpen] = useToggleWithFocusUpdate(focus.blockIndex, index);

  return (
    <ExpansionCard
      aria-label={`${block.type} - ${index} - ${block.id}`}
      data-color={getHighlightColor(isNew(block), isEdited(block))}
      onToggle={setIsOpen}
      open={isOpen}
      size="small"
    >
      <ExpansionCard.Header>
        <ExpansionCard.Title>
          <HStack gap="space-16">
            <span>{block.type}</span>
            <span>Index: {index}</span>
            <span>ID: {block.id ?? "NEW"}</span>
          </HStack>
        </ExpansionCard.Title>
        <ExpansionCard.Description>{textExtract(blockText)}</ExpansionCard.Description>
      </ExpansionCard.Header>
      <BoxNew asChild background="default">
        <ExpansionCard.Content>
          <Accordion size="small">
            {block.content.map((c, index) => (
              <Content content={c} focus={focus} index={index} key={index} />
            ))}
          </Accordion>
        </ExpansionCard.Content>
      </BoxNew>
    </ExpansionCard>
  );
};

const Content = ({ content, focus, index }: { content: Content; focus?: Focus; index: number }) => {
  const extract = isTextContent(content) ? textExtract(textOf(content)) : "";

  const [isOpen, setIsOpen] = useToggleWithFocusUpdate(focus?.contentIndex, index);

  return (
    <Accordion.Item
      data-color={getHighlightColor(isNew(content), isEdited(content))}
      onOpenChange={setIsOpen}
      open={isOpen}
    >
      <Accordion.Header>
        <HStack gap="space-16">
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
        <Accordion size="small">
          {content.items.map((i, index) => (
            <ItemBody focus={focus} index={index} item={i} key={index} />
          ))}
        </Accordion>
      );
    case "LITERAL":
    case "VARIABLE":
      return textOf(content);
    case "TABLE":
      return null;
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

  return (
    <Accordion.Item
      data-color={getHighlightColor(isNew(item), isEditedItem(item))}
      onOpenChange={setIsOpen}
      open={isOpen}
    >
      <Accordion.Header>
        <HStack gap="space-16">
          <span>Index: {index}</span>
          <span>ID: {item.id ?? "NEW"}</span>
          <span>{textExtract(itemText)}</span>
        </HStack>
      </Accordion.Header>
      <Accordion.Content>
        <Accordion size="small">
          {item.content.map((c, index) => (
            <Content content={c} focus={itemContentFocus} index={index} key={index} />
          ))}
        </Accordion>
      </Accordion.Content>
    </Accordion.Item>
  );
};

function getHighlightColor(isNew: boolean, isEdited: boolean): string {
  return isNew ? "success" : isEdited ? "accent" : "neutral";
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
    case "TITLE3":
    case "PARAGRAPH":
      return (
        isNew(content) ||
        (content.originalType !== null ? content.type !== content.originalType : false) ||
        content.deletedContent.length > 0 ||
        content.content.some(isEdited)
      );
    case "TABLE":
      return false;
  }
  return false;
}

function isEditedItem(item: Item): boolean {
  return isNew(item) || item.deletedContent.length > 0 || item.content.some(isEdited);
}

function textExtract(str: string, maxLength: number = 65): string {
  return str.length > maxLength ? str.slice(0, 62) + "..." : str;
}
