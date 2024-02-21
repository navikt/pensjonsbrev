import { css } from "@emotion/react";

export function Divider() {
  return (
    <div
      css={css`
        width: 100%;
        height: 1px;
        background: var(--a-grayalpha-50);
      `}
    />
  );
}
