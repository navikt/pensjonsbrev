import { css } from "@emotion/react";
import { useEffect, useState } from "react";

import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";

export function DebugPanel() {
  const { editorState } = useEditor();
  const [mousePosition, setMousePosition] = useState({ x: 0, y: 0 });

  const mouseMoveEventListener = (event: MouseEvent) => {
    setMousePosition({ x: event.pageX, y: event.pageY });
  };

  useEffect(() => {
    document.addEventListener("mousemove", mouseMoveEventListener);

    return () => {
      document.removeEventListener("mousemove", mouseMoveEventListener);
    };
  }, []);

  return (
    <div
      css={css`
        display: flex;
        gap: 1rem;
        padding: 1rem;
        margin-top: 1rem;
        align-self: flex-start;
        background: var(--a-blue-200);
        width: 100%;
      `}
    >
      {Object.entries(editorState.focus).map(([key, value]) => (
        <div key={key}>
          <b>{key}: </b>
          <span>{value}</span>
        </div>
      ))}
      <b>X: {mousePosition.x}</b>
      <b>Y: {mousePosition.y}</b>
    </div>
  );
}
