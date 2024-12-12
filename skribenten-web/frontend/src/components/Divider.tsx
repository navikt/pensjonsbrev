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

export const VerticalDivider = () => {
  return (
    <div
      css={css`
        width: 1px;
        background: var(--a-grayalpha-300);
        align-self: stretch;
      `}
    />
  );
};
