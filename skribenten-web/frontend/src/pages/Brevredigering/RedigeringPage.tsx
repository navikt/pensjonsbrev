import { css } from "@emotion/react";

import { ModelEditor } from "~/pages/Brevredigering/ModelEditor/ModelEditor";

export function RedigeringPage() {
  return (
    <div
      css={css`
        background: var(--a-white);
        display: grid;
        grid-template-columns: minmax(380px, 400px) minmax(432px, 720px);
        gap: var(--a-spacing-4);
        justify-content: space-between;
        flex: 1;

        > form:first-of-type {
          padding: var(--a-spacing-4);
          border-left: 1px solid var(--a-gray-400);
          border-right: 1px solid var(--a-gray-400);
        }
      `}
    >
      <ModelEditor />
    </div>
  );
}
