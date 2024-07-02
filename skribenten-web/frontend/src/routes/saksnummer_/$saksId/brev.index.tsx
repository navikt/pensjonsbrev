import { css } from "@emotion/react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { z } from "zod";

import { createBrev, getBrev } from "~/api/brev-queries";
import { ModelEditor } from "~/Brevredigering/ModelEditor/ModelEditor";
import type { SpraakKode } from "~/types/apiTypes";
import type { BrevResponse, SaksbehandlerValg } from "~/types/brev";

export const Route = createFileRoute("/saksnummer/$saksId/brev/")({
  validateSearch: (search: Record<string, unknown>): { brevkode: string } => ({
    brevkode: z.string().parse(search.brevkode),
  }),
  component: OpprettBrevPage,
});

function OpprettBrevPage() {
  const { saksId } = Route.useParams();
  const { brevkode } = Route.useSearch();
  const createBrevMutation = useCreateLetterMutation(saksId, brevkode);
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
        brevkode={brevkode}
        disableSubmit={createBrevMutation.isPending || createBrevMutation.isSuccess}
        onSubmit={(submittedValues) =>
          createBrevMutation.mutate({
            spraak: submittedValues.spraak,
            avsenderEnhet: submittedValues.avsenderEnhet,
            saksbehandlerValg: submittedValues.saksbehandlerValg,
          })
        }
        sakId={saksId}
      />
    </div>
  );
}

function useCreateLetterMutation(saksId: string, brevkode: string) {
  const navigate = useNavigate({ from: Route.fullPath });
  const queryClient = useQueryClient();
  return useMutation<
    BrevResponse,
    Error,
    { spraak: SpraakKode; avsenderEnhet: string; saksbehandlerValg: SaksbehandlerValg }
  >({
    mutationFn: async (values) =>
      createBrev(saksId, {
        brevkode: brevkode,
        spraak: values.spraak,
        avsenderEnhet: values.avsenderEnhet,
        saksbehandlerValg: values.saksbehandlerValg,
      }),
    onSuccess: async (response) => {
      queryClient.setQueryData(getBrev.queryKey(response.info.id), response);
      return navigate({
        to: "/saksnummer/$saksId/brev/$brevId",
        params: { brevId: response.info.id },
      });
    },
  });
}
