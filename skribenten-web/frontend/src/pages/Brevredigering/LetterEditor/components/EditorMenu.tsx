import { css } from "@emotion/react";
import { Button } from "@navikt/ds-react";

import type { BoundAction } from "../lib/actions";

export type EditorMenuProperties = {
  switchType: BoundAction<[type: "PARAGRAPH" | "TITLE1" | "TITLE2"]>;
};

export const EditorMenu = ({ switchType }: EditorMenuProperties) => {
  return (
    <div
      css={css`
        border-bottom: 1px solid var(--a-border-divider);
        background: var(--a-blue-50);
        padding: var(--a-spacing-3) var(--a-spacing-4);
        display: flex;
        gap: var(--a-spacing-2);
        align-self: stretch;
      `}
    >
      <Button onClick={switchType.bind(null, "TITLE1")} size="xsmall" type="button" variant="secondary-neutral">
        Overskift 1
      </Button>
      <Button onClick={switchType.bind(null, "TITLE2")} size="xsmall" type="button" variant="secondary-neutral">
        Overskrift 2
      </Button>
      <Button onClick={switchType.bind(null, "PARAGRAPH")} size="xsmall" type="button" variant="secondary-neutral">
        Normal
      </Button>
    </div>
  );
};
