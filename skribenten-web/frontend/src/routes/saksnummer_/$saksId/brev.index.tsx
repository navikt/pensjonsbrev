import { css } from "@emotion/react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { z } from "zod";

import { createBrev, getBrev, oppdaterSignatur } from "~/api/brev-queries";
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
      //selv om vi navigerer enhetsId som string | null, så vil tanstack gjøre om null til undefined
      .parse(search.enhetsId || null),
  }),
  component: OpprettBrevPage,
});

function OpprettBrevPage() {
  const { saksId } = Route.useParams();
  const queryClient = useQueryClient();
  const navigate = useNavigate({ from: Route.fullPath });
  const { brevkode, spraak, enhetsId } = Route.useSearch();
  const signaturMutation = useMutation<BrevResponse, AxiosError, { brevId: string; signatur: string }>({
    mutationFn: (args) => oppdaterSignatur(args.brevId, args.signatur),
  });
  const createBrevMutation = useCreateLetterMutation(saksId, brevkode, spraak, enhetsId);

  const handleOnSubmit = (saksbehandlerValg: SaksbehandlerValg, signatur: string) => {
    createBrevMutation.mutate(
      { saksbehandlerValg },
      {
        onSuccess: async (createBrevResponse) => {
          queryClient.setQueryData(getBrev.queryKey(createBrevResponse.info.id), createBrevResponse);
          await signaturMutation.mutateAsync(
            { brevId: createBrevResponse.info.id.toString(), signatur },
            {
              onSuccess: (signaturResponse) => {
                queryClient.setQueryData(getBrev.queryKey(signaturResponse.info.id), signaturResponse);
              },
            },
          );

          return navigate({
            to: "/saksnummer/$saksId/brev/$brevId",
            params: { brevId: createBrevResponse.info.id },
          });
        },
      },
    );
  };

  return (
    <div
      css={css`
        background: var(--a-white);
        display: grid;
        grid-template: "modelEditor ." 1fr / 30% 70%;
        flex: 1;
        border-left: 1px solid var(--a-gray-200);
        border-right: 1px solid var(--a-gray-200);

        > form:first-of-type {
          padding: var(--a-spacing-4);
          border-right: 1px solid var(--a-gray-200);
        }
      `}
    >
      <ModelEditor
        brevId={null}
        brevkode={brevkode}
        disableSubmit={createBrevMutation.isPending || createBrevMutation.isSuccess}
        onSubmit={handleOnSubmit}
        saksId={saksId}
        vedtaksId={undefined}
      />
    </div>
  );
}

function useCreateLetterMutation(saksId: string, brevkode: string, språk: SpraakKode, enhetsId: Nullable<string>) {
  return useMutation<BrevResponse, Error, { saksbehandlerValg: SaksbehandlerValg }>({
    mutationFn: async (values) =>
      createBrev(saksId, {
        brevkode: brevkode,
        spraak: språk,
        avsenderEnhetsId: enhetsId,
        saksbehandlerValg: values.saksbehandlerValg,
      }),
  });
}
