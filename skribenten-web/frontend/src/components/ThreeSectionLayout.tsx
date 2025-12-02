import { css } from "@emotion/react";
import { BoxNew, HStack } from "@navikt/ds-react";

const ThreeSectionLayout = (props: { left: React.ReactNode; right: React.ReactNode; bottom: React.ReactNode }) => {
  return (
    <BoxNew
      background="default"
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
          grid-template-columns: minmax(min-content, 25%) auto;
          flex: 1;

          > :first-of-type {
            padding: 16px 24px;
            border-right: 1px solid var(--ax-neutral-300);
          }

          @media (width <= 1024px) {
            > :first-of-type {
              padding: var(--ax-space-12);
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
          background: var(--ax-bg-default);

          border-top: 1px solid var(--ax-neutral-300);
          padding: var(--ax-space-8) var(--ax-space-16);
        `}
        justify={"end"}
      >
        {props.bottom}
      </HStack>
    </BoxNew>
  );
};

export default ThreeSectionLayout;
