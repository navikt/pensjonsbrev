import { css } from "@emotion/react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { useState } from "react";
import { z } from "zod";

import { getBrev, updateBrev } from "~/api/brev-queries";
import Actions from "~/Brevredigering/LetterEditor/actions";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { ModelEditor } from "~/Brevredigering/ModelEditor/ModelEditor";
import type { BrevResponse, SaksbehandlerValg } from "~/types/brev";

export const Route = createFileRoute("/saksnummer/$saksId/brev/$brevId")({
  parseParams: ({ brevId }) => ({ brevId: z.coerce.number().parse(brevId) }),
  component: RedigerBrevPage,
});

function RedigerBrevPage() {
  const { brevId, saksId } = Route.useParams();
  const brevQuery = useQuery({
    queryKey: getBrev.queryKey(brevId),
    queryFn: () => getBrev.queryFn(saksId, brevId),
    staleTime: Number.POSITIVE_INFINITY,
  });

  return brevQuery.data ? <RedigerBrev brev={brevQuery.data} saksId={saksId} /> : <div></div>;
}

const RedigerBrev = ({ brev, saksId }: { brev: BrevResponse; saksId: string }) => {
  const [editorState, setEditorState] = useState<LetterEditorState>(Actions.create(brev.redigertBrev));
  const queryClient = useQueryClient();
  const oppdaterBrevMutation = useMutation<BrevResponse, unknown, SaksbehandlerValg>({
    mutationFn: async (saksbehandlerValg) =>
      updateBrev(saksId, brev.info.id, {
        saksbehandlerValg,
        redigertBrev: editorState.redigertBrev,
      }),
    onSuccess: (response) => {
      queryClient.setQueryData(getBrev.queryKey(response.info.id), response);
      setEditorState({ focus: editorState!.focus, redigertBrev: response.redigertBrev });
    },
  });

  return (
    <div
      css={css`
        background: var(--a-white);
        display: grid;
        grid-template-columns: minmax(380px, 400px) 1fr;
        flex: 1;
        border-left: 1px solid var(--a-gray-400);
        border-right: 1px solid var(--a-gray-400);

        > form:first-of-type {
          padding: var(--a-spacing-4);
          border-right: 1px solid var(--a-gray-400);
        }
      `}
    >
      <ModelEditor
        brevkode={brev.info.brevkode}
        defaultValues={brev.saksbehandlerValg}
        disableSubmit={oppdaterBrevMutation.isPending}
        onSubmit={oppdaterBrevMutation.mutate}
      />
      <LetterEditor editorState={editorState} setEditorState={setEditorState} />
    </div>
  );
};
