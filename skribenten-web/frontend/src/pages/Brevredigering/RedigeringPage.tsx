import { css } from "@emotion/react";
import { useMutation } from "@tanstack/react-query";

import { renderLetter } from "~/api/skribenten-api-endpoints";
import { LetterEditor } from "~/pages/Brevredigering/LetterEditor/LetterEditor";
import { ModelEditor } from "~/pages/Brevredigering/ModelEditor/ModelEditor";
import type { RenderedLetter } from "~/types/brevbakerTypes";

const VAL = {
  mottattSoeknad: "2023-09-11",
  ytelse: "test",
  land: "NO",
  inkluderVenterSvarAFP: {
    uttaksDato: "2023-12-12",
    uttakAlderspensjonProsent: "54",
  },
  svartidUker: 1,
};

const TEST_TEMPLATE = "INFORMASJON_OM_SAKSBEHANDLINGSTID";

export function RedigeringPage() {
  return (
    <div
      css={css`
        background: var(--a-white);
        display: grid;
        grid-template-columns: minmax(380px, 400px) minmax(432px, 720px);
        gap: var(--a-spacing-4);
        justify-content: space-between;
        flex: 1;

        > :nth-child(1) {
          padding: var(--a-spacing-4);
          border-left: 1px solid var(--a-gray-400);
          border-right: 1px solid var(--a-gray-400);
        }
      `}
    >
      <ModelEditor />
    </div>
  );
}
