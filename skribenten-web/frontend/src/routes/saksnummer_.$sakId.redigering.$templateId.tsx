import { css } from "@emotion/react";
import { createFileRoute } from "@tanstack/react-router";

import { ModelEditor } from "~/Brevredigering/ModelEditor/ModelEditor";

export const Route = createFileRoute("/saksnummer/$sakId/redigering/$templateId")({
  component: () => (
    <div
      css={css`
        background: var(--a-white);
        display: grid;
        grid-template-columns: minmax(380px, 400px) 1fr;
        flex: 1;
        border-left: 1px solid var(--a-gray-400);
        border-right: 1px solid var(--a-gray-400);

        > form:first-of-type {
          padding: var(--a-spacing-4);
          border-right: 1px solid var(--a-gray-400);
        }
      `}
    >
      <ModelEditor />
    </div>
  ),
});
