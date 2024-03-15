import { css } from "@emotion/react";
import { HStack } from "@navikt/ds-react";
import { useEffect, useState } from "react";

import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { getCaretRect, getRange } from "~/Brevredigering/LetterEditor/services/caretUtils";

export function DebugPanel() {
  const { editorState } = useEditor();
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
        MODEL:
        {Object.entries(editorState.focus).map(([key, value]) => (
          <div key={key}>
            <b>{key}: </b>
            <span>{value}</span>
          </div>
        ))}
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
    </div>
  );
}
