import { css } from "@emotion/react";
import { Box, HStack } from "@navikt/ds-react";

const ThreeSectionLayout = (props: { left: React.ReactNode; right: React.ReactNode; bottom: React.ReactNode }) => {
  return (
    <Box
      background="bg-default"
      css={css`
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        flex: 1;
      `}
    >
      <div
        css={css`
          display: grid;
          grid-template-columns: 27% 73%;
          flex: 1;

          > :first-of-type {
            padding: 16px 24px;
            border-right: 1px solid var(--a-gray-200);
          }

          @media (width <= 1024px) {
            > :first-of-type {
              padding: var(--a-spacing-3);
            }
          }
        `}
      >
        <div>{props.left}</div>
        <div>{props.right}</div>
      </div>
      <HStack
        css={css`
          position: sticky;
          bottom: 0;
          left: 0;
          width: 100%;
          background: var(--a-white);

          border-top: 1px solid var(--a-gray-200);
          padding: var(--a-spacing-2) var(--a-spacing-4);
        `}
        justify={"end"}
      >
        {props.bottom}
      </HStack>
    </Box>
  );
};

export default ThreeSectionLayout;
