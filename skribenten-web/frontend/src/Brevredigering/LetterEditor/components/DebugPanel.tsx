import { Global } from "@emotion/react";
import { Accordion, BodyShort, Box, ExpansionCard, HStack, VStack } from "@navikt/ds-react";
import { type AkselColor } from "@navikt/ds-react/types/theme";
import type React from "react";
import { type Dispatch, useEffect, useRef, useState } from "react";

import { isItemContentIndex, isNew, text as textOf } from "~/Brevredigering/LetterEditor/actions/common";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { type Focus, type LetterEditorState, type SelectionIndex } from "~/Brevredigering/LetterEditor/model/state";
import { isFritekst, isLiteral, isTextContent } from "~/Brevredigering/LetterEditor/model/utils";
import { getCaretRect, getRange, getSelectionFocus } from "~/Brevredigering/LetterEditor/services/caretUtils";
import { type AnyBlock, type Content, type Item, TITLE_INDEX } from "~/types/brevbakerTypes";

const MIN_HEIGHT = 120;
const RESIZE_STEP = 16;

export default function DebugPanel() {
  const { freeze, editorState } = useEditor();
  const [mousePosition, setMousePosition] = useState({ x: 0, y: 0 });
  const [caretOffset, setCaretOffset] = useState(0);
  const [caretRect, setCaretRect] = useState<DOMRect>();
  const [height, setHeight] = useState<number>();
  const dragRef = useRef<{ pointerId: number; startY: number; startHeight: number } | null>(null);
  const [mappedSelection, setMappedSelection] = useState<SelectionIndex | null>(null);

  const getMaxHeight = () => Math.max(MIN_HEIGHT, Math.floor(globalThis.innerHeight * 0.8));
  const getInitialHeight = () => Math.round(globalThis.innerHeight * 0.3);
  const clampHeight = (nextHeight: number) => Math.min(getMaxHeight(), Math.max(MIN_HEIGHT, nextHeight));

  const resizeWithKeyboard = (event: React.KeyboardEvent<HTMLDivElement>) => {
    const currentHeight = height ?? getInitialHeight();
    let nextHeight: number | undefined;

    switch (event.key) {
      case "ArrowUp":
        nextHeight = currentHeight + RESIZE_STEP;
        break;
      case "ArrowDown":
        nextHeight = currentHeight - RESIZE_STEP;
        break;
      case "Home":
        nextHeight = MIN_HEIGHT;
        break;
      case "End":
        nextHeight = getMaxHeight();
        break;
    }

    if (nextHeight !== undefined) {
      event.preventDefault();
      setHeight(clampHeight(nextHeight));
    }
  };

  useEffect(() => {
    const onPointerMove = (event: PointerEvent) => {
      const drag = dragRef.current;
      if (!drag || event.pointerId !== drag.pointerId) return;
      setHeight(clampHeight(drag.startHeight + drag.startY - event.clientY));
    };
    const onPointerUp = (event: PointerEvent) => {
      if (event.pointerId === dragRef.current?.pointerId) dragRef.current = null;
    };
    const onWindowResize = () => {
      setHeight((currentHeight) => (currentHeight === undefined ? currentHeight : clampHeight(currentHeight)));
    };

    globalThis.addEventListener("pointermove", onPointerMove);
    globalThis.addEventListener("pointerup", onPointerUp);
    globalThis.addEventListener("pointercancel", onPointerUp);
    globalThis.addEventListener("resize", onWindowResize);
    return () => {
      globalThis.removeEventListener("pointermove", onPointerMove);
      globalThis.removeEventListener("pointerup", onPointerUp);
      globalThis.removeEventListener("pointercancel", onPointerUp);
      globalThis.removeEventListener("resize", onWindowResize);
    };
  }, []);

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
    const tree = document.querySelector<HTMLElement>(".debug-panel-tree");
    const target = tree?.querySelector<HTMLElement>("[data-debug-focus-target]");
    if (!tree || !target) return;

    const treeRect = tree.getBoundingClientRect();
    const targetRect = target.getBoundingClientRect();
    const visibleTop = treeRect.top + tree.clientTop;
    const visibleBottom = visibleTop + tree.clientHeight;

    if (targetRect.top < visibleTop) {
      tree.scrollTop -= visibleTop - targetRect.top;
    } else if (targetRect.bottom > visibleBottom) {
      tree.scrollTop += targetRect.bottom - visibleBottom;
    }
  }, [editorState.focus]);

  useEffect(() => {
    document.addEventListener("mousemove", mouseMoveEventListener);
    document.addEventListener("keyup", keyboardEventListener);
    document.addEventListener("selectionchange", selectionChangeListener);

    return () => {
      document.removeEventListener("mousemove", mouseMoveEventListener);
      document.removeEventListener("keyup", keyboardEventListener);
      document.removeEventListener("selectionchange", selectionChangeListener);
    };
  }, [keyboardEventListener, mouseMoveEventListener, selectionChangeListener]);

  return (
    <>
      <Global
        styles={{
          ".debug-panel": {
            position: "relative",
          },
          ".debug-panel-resize-handle": {
            cursor: "ns-resize",
            height: "8px",
            left: 0,
            position: "absolute",
            right: 0,
            touchAction: "none",
            top: 0,
            zIndex: 1,
            "&::after": {
              backgroundColor: "var(--ax-border-neutral-subtle)",
              borderRadius: "2px",
              content: '""',
              height: "2px",
              left: "calc(50% - 16px)",
              position: "absolute",
              top: "3px",
              width: "32px",
            },
            "&:hover::after": {
              backgroundColor: "var(--ax-border-neutral)",
            },
            "&:focus-visible": {
              outline: "2px solid var(--ax-border-focus)",
              outlineOffset: "-2px",
            },
          },
          ".editor": {
            "[contenteditable]": {
              "&:focus-within": {
                outline: "1px dotted var(--ax-border-brand-magenta-subtle)",
              },
              outline: "1px dotted var(--ax-border-brand-magenta-subtle)",
              outlineOffset: "-1px",
            },
          },
        }}
      />
      <Box
        asChild
        background="neutral-soft"
        borderColor="neutral-subtle"
        borderWidth={"1 0 0 0"}
        className="debug-panel"
        flexShrink="0"
        height={height === undefined ? "30vh" : `${height}px`}
        overflowY="hidden"
      >
        <VStack>
          {/* biome-ignore lint/a11y/useSemanticElements: This adjustable separator exposes resize values and keyboard controls. */}
          <div
            aria-label="Endre høyden på debugpanelet"
            aria-orientation="horizontal"
            aria-valuemax={getMaxHeight()}
            aria-valuemin={MIN_HEIGHT}
            aria-valuenow={height ?? getInitialHeight()}
            className="debug-panel-resize-handle"
            onKeyDown={resizeWithKeyboard}
            onPointerDown={(event) => {
              if (event.button !== 0) return;
              event.preventDefault();
              dragRef.current = {
                pointerId: event.pointerId,
                startY: event.clientY,
                startHeight:
                  event.currentTarget.parentElement?.getBoundingClientRect().height || height || getInitialHeight(),
              };
            }}
            role="separator"
            tabIndex={0}
          />
          <VStack data-debug-panel-stats flexShrink="0" padding="space-16">
            <HStack gap="space-16">
              {mappedSelection ? (
                <VStack>
                  <HStack gap="space-16">
                    SELECTION &lt;:
                    <FocusInfo focus={mappedSelection.start} />
                  </HStack>
                  <HStack gap="space-16">
                    SELECTION &gt;:
                    <FocusInfo focus={mappedSelection.end} />
                  </HStack>
                </VStack>
              ) : null}
            </HStack>
            <HStack gap="space-16">
              FOCUS:
              <FocusInfo focus={editorState.focus} />
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
          </VStack>
          <Box asChild className="debug-panel-tree" flexGrow="1" minHeight="0" overflowY="auto">
            <VStack padding="space-16">
              <LetterTree state={editorState} />
            </VStack>
          </Box>
        </VStack>
      </Box>
    </>
  );
}

const FocusInfo = ({ focus }: { focus: Focus }) => {
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
  const focusedTitleContent =
    focus.blockIndex === TITLE_INDEX ? redigertBrev.title.text[focus.contentIndex] : undefined;

  return (
    <>
      {focusedTitleContent && (
        <div data-debug-focus-target="true">
          <HStack gap="space-16">
            <BodyShort weight="semibold">{focusedTitleContent.type}</BodyShort>
            <BodyShort>{textOf(focusedTitleContent)}</BodyShort>
          </HStack>
        </div>
      )}
      {redigertBrev.blocks.map((block, index) => (
        <BlockInfo block={block} focus={focus} index={index} key={index} />
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

const BlockInfo = ({ block, focus, index }: { block: AnyBlock; focus: Focus; index: number }) => {
  const blockText = block.content
    .map((content) => (isTextContent(content) ? textOf(content) : null))
    .filter((content) => content !== null)
    .join("");

  const [isOpen, setIsOpen] = useToggleWithFocusUpdate(focus.blockIndex, index);
  const blockFocus = focus.blockIndex === index ? focus : undefined;

  return (
    <ExpansionCard
      aria-label={`${block.type} - ${index} - ${block.id}`}
      data-color={getHighlightColor(isNew(block), isEdited(block))}
      onToggle={setIsOpen}
      open={isOpen}
      size="small"
    >
      <ExpansionCard.Header>
        <ExpansionCard.Title size="small">
          <HStack gap="space-16">
            <span>{block.type}</span>
            <span>Index: {index}</span>
            <span>ID: {block.id ?? "NEW"}</span>
          </HStack>
        </ExpansionCard.Title>
        <ExpansionCard.Description>{textExtract(blockText)}</ExpansionCard.Description>
      </ExpansionCard.Header>
      <Box asChild background="default">
        <ExpansionCard.Content>
          <Accordion size="small">
            {block.content.map((content, index) => (
              <ContentInfo
                content={content}
                focus={blockFocus}
                index={index}
                isFocused={blockFocus?.contentIndex === index && !isItemContentIndex(blockFocus)}
                key={index}
              />
            ))}
          </Accordion>
        </ExpansionCard.Content>
      </Box>
    </ExpansionCard>
  );
};

const ContentInfo = ({
  content,
  focus,
  index,
  isFocused,
}: {
  content: Content;
  focus?: Focus;
  index: number;
  isFocused: boolean;
}) => {
  const extract = isTextContent(content) ? textExtract(textOf(content)) : "";

  const [isOpen, setIsOpen] = useToggleWithFocusUpdate(focus?.contentIndex, index);

  return (
    <Accordion.Item
      data-color={getHighlightColor(isNew(content), isEdited(content))}
      onOpenChange={setIsOpen}
      open={isOpen}
    >
      <Accordion.Header data-debug-focus-target={isFocused ? "true" : undefined}>
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
          {content.items.map((item, index) => (
            <ItemBody focus={focus} index={index} item={item} key={index} />
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
  const focusedItem = isItemContentIndex(focus) && focus.itemIndex === index;
  const itemContentFocus = focusedItem
    ? { blockIndex: focus.blockIndex, contentIndex: focus.itemContentIndex }
    : undefined;

  const [isOpen, setIsOpen] = useToggleWithFocusUpdate(isItemContentIndex(focus) ? focus.itemIndex : undefined, index);

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
          {item.content.map((content, index) => (
            <ContentInfo
              content={content}
              focus={itemContentFocus}
              index={index}
              isFocused={itemContentFocus?.contentIndex === index}
              key={index}
            />
          ))}
        </Accordion>
      </Accordion.Content>
    </Accordion.Item>
  );
};

function getHighlightColor(isNew: boolean, isEdited: boolean): AkselColor {
  return isNew ? "success" : isEdited ? "accent" : "neutral";
}

function isEdited(content: Content | AnyBlock): boolean {
  switch (content.type) {
    case "NEW_LINE":
      return isNew(content);
    case "ITEM_LIST":
      return isNew(content) || content.deletedItems?.length > 0 || content.items.some(isEditedItem);
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
  return isNew(item) || item.deletedContent?.length > 0 || item.content.some(isEdited);
}

function textExtract(str: string, maxLength: number = 65): string {
  return str.length > maxLength ? `${str.slice(0, 62)}...` : str;
}
