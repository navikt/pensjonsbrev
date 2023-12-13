import { css } from "@emotion/react";
import type { ReactNode } from "react";

export function FullWidthDatePickerWrapper({ children }: { children: ReactNode }) {
  return (
    <div
      css={css`

        .navds-date__wrapper {
          width: 100%;
        }

        .navds-date__field-wrapper {
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
