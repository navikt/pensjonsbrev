import { css } from "@emotion/react";

export function Divider() {
  return (
    <div
      css={css`
        width: 100%;
        height: 1px;
        background: var(--ax-neutral-100A);
      `}
    />
  );
}

export const VerticalDivider = () => {
  return (
    <div
      css={css`
        width: 1px;
        background: var(--ax-neutral-400A);
        align-self: stretch;
      `}
    />
  );
};
