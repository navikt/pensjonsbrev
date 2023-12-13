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
  const renderLetterMutation = useMutation<RenderedLetter, unknown, string>({
    mutationFn: async (id) => {
      return await renderLetter(id, { letterData: VAL, editedLetter: undefined });
    },
  });

  const renderedLetter = renderLetterMutation.data;

  return (
    <div>
      <button onClick={() => renderLetterMutation.mutate(TEST_TEMPLATE)}>test</button>
      <ModelEditor />
      <div>{renderedLetter && <LetterEditor initialState={renderedLetter} />}</div>
    </div>
  );
}
