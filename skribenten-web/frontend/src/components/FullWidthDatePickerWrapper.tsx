import { css } from "@emotion/react";
import { type ReactNode } from "react";

export function FullWidthDatePickerWrapper({ children }: { children: ReactNode }) {
  return (
    <div
      css={css`
        >div {
          width: 100%;
        }

        div:has(>input) {
          width: 100%;
        }

        input {
          width: 100% !important;
        }
      `}
    >
      {children}
    </div>
  );
}
