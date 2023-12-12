import { useMutation, useQuery } from "@tanstack/react-query";
import React, { useState } from "react";

import { getTemplate, renderLetter } from "~/api/skribenten-api-endpoints";
import { LetterEditor } from "~/pages/Brevredigering/LetterEditor/LetterEditor";
import type { ObjectValue } from "~/pages/Brevredigering/ModelEditor/model";
import { ModelEditor } from "~/pages/Brevredigering/ModelEditor/ModelEditor";
import type { RenderedLetter } from "~/types/brevbakerTypes";

const VAL = {
  mottattSoeknad: "2023-09-11",
  ytelse: "test",
  land: "NO",
  inkluderVenterSvarAFP: null,
  svartidUker: 1,
};

const TEST_TEMPLATE = "INFORMASJON_OM_SAKSBEHANDLINGSTID";

export function RedigeringPage() {
  const [modelValue, setModelValue] = useState<ObjectValue>({});

  const modelSpec = useQuery({
    queryKey: getTemplate.queryKey(TEST_TEMPLATE),
    queryFn: () => getTemplate.queryFn(TEST_TEMPLATE),
  }).data;

  const renderLetterMutation = useMutation<RenderedLetter, unknown, string>({
    mutationFn: async (id) => {
      return await renderLetter(id, { letterData: VAL, editedLetter: undefined });
    },
  });
  if (!modelSpec) {
    return <></>;
  }

  const renderedLetter = renderLetterMutation.data;

  return (
    <div>
      <button onClick={() => renderLetterMutation.mutate(TEST_TEMPLATE)}>test</button>
      <ModelEditor spec={modelSpec.modelSpecification} updateValue={setModelValue} value={modelValue} />
      <div>{renderedLetter && <LetterEditor initialState={renderedLetter} />}</div>
    </div>
  );
}
