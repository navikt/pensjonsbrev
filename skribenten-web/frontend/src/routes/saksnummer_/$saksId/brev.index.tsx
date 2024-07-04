import { css } from "@emotion/react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { z } from "zod";

import { createBrev, getBrev } from "~/api/brev-queries";
import { ModelEditor } from "~/Brevredigering/ModelEditor/ModelEditor";
import { SpraakKode } from "~/types/apiTypes";
import type { BrevResponse, SaksbehandlerValg } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";

export const Route = createFileRoute("/saksnummer/$saksId/brev/")({
  validateSearch: (
    search: Record<string, unknown>,
  ): { brevkode: string; spraak: SpraakKode; enhetsId: Nullable<string> } => ({
    brevkode: z.string().parse(search.brevkode),
    spraak: z.nativeEnum(SpraakKode).parse(search.spraak),
    enhetsId: z
      .string()
      .nullable()
      //er noe kødd med zod her som fører til error hvis vi enhetsId er null, og man bare gjør search.enhetsId.
      //men det fungerer som forventet med dette her
      .parse(search.enhetsId || null),
  }),
  component: OpprettBrevPage,
});

function OpprettBrevPage() {
  const { saksId } = Route.useParams();
  const { brevkode, spraak, enhetsId } = Route.useSearch();
  const createBrevMutation = useCreateLetterMutation(saksId, brevkode, spraak, enhetsId);
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
        onSubmit={(saksbehandlerValg) => createBrevMutation.mutate({ saksbehandlerValg })}
      />
    </div>
  );
}

function useCreateLetterMutation(saksId: string, brevkode: string, språk: SpraakKode, enhetsId: Nullable<string>) {
  const navigate = useNavigate({ from: Route.fullPath });
  const queryClient = useQueryClient();
  return useMutation<BrevResponse, Error, { saksbehandlerValg: SaksbehandlerValg }>({
    mutationFn: async (values) =>
      createBrev(saksId, {
        brevkode: brevkode,
        spraak: språk,
        avsenderEnhetsId: enhetsId,
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
