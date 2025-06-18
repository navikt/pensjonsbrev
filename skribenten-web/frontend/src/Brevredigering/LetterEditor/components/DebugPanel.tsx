import { css } from "@emotion/react";
import { HStack } from "@navikt/ds-react";
import { useEffect, useState } from "react";

import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import type { LiteralIndex } from "~/Brevredigering/LetterEditor/model/state";
import { getCaretRect, getRange } from "~/Brevredigering/LetterEditor/services/caretUtils";
import type { AnyBlock, Content, Item } from "~/types/brevbakerTypes";

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
      <HStack gap={"4"}>
        Edited:
        <ul>
          {findEdits(editorState.redigertBrev.blocks).map((litIndex, index) => (
            <li key={index}>
              {Object.entries(litIndex).map(([key, value]) => (
                <div key={key}>
                  <b>{key}: </b>
                  <span>{value}</span>
                </div>
              ))}
            </li>
          ))}
        </ul>
      </HStack>
    </div>
  );
}

function findEdits(blocks: AnyBlock[]): LiteralIndex[] {
  return blocks.flatMap(findEditsBlock);
}

function findEditsBlock(block: AnyBlock): LiteralIndex[] {
  return block.content.flatMap(findEditsContent).map((ind) => ({ ...ind, blockIndex: block.id ?? -1 }));
}

function findEditsContent(content: Content): { contentIndex: number; itemIndex?: number; itemContentIndex?: number }[] {
  switch (content.type) {
    case "VARIABLE": {
      return [];
    }
    case "ITEM_LIST": {
      return content.items.flatMap(findEditsItem).map((ind) => ({ ...ind, contentIndex: content.id ?? -1 }));
    }
    case "LITERAL": {
      return content.editedText ? [{ contentIndex: content.id ?? -1 }] : [];
    }
    case "NEW_LINE": {
      return content?.id === null ? [] : [{ contentIndex: content.id }];
    }
    case "TABLE": {
      // A table element itself doesn’t carry edited text;
      // any edits happen inside its cells so we return empty.
      return [];
    }
  }
  return [];
}

function findEditsItem(item: Item): { itemIndex: number; itemContentIndex: number }[] {
  return item.content
    .flatMap(findEditsContent)
    .map((ind) => ({ itemIndex: item.id ?? -1, itemContentIndex: ind.contentIndex }));
}
