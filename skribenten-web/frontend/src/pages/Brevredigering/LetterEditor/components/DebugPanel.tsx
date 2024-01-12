import { css } from "@emotion/react";

import { useEditor } from "~/pages/Brevredigering/LetterEditor/LetterEditor";

export function DebugPanel() {
  const { editorState } = useEditor();

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
    </div>
  );
}
