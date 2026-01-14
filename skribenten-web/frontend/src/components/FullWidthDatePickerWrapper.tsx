import { css } from "@emotion/react";
import type { ReactNode } from "react";

export function FullWidthDatePickerWrapper({ children }: { children: ReactNode }) {
  return (
    <div
      css={css`
        .aksel-date__wrapper {
          width: 100%;
        }

        .aksel-date__field-wrapper {
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
