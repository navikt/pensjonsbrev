import { css } from "@emotion/react";
import { Button, TextField } from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import React from "react";
import { useForm } from "react-hook-form";

import { getSakContextQuery } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { SakContextDto } from "~/types/apiTypes";

type RootSearch = { enhetsId: string | undefined };

export const Route = createFileRoute("/saksnummer/")({
  validateSearch: (search: Record<string, unknown>): RootSearch => ({ enhetsId: search.enhetsId as string }),
  component: SaksnummerPage,
});

function SaksnummerPage() {
  const queryClient = useQueryClient();
  const navigate = useNavigate({ from: Route.fullPath });
  const { handleSubmit, register } = useForm({
    defaultValues: {
      saksnummer: "",
    },
  });
  const search = Route.useSearch();

  const hentSakContextMutation = useMutation<SakContextDto, AxiosError<unknown>, { saksnummer: string }>({
    mutationFn: ({ saksnummer }) => getSakContextQuery(saksnummer, undefined).queryFn(),
    onSuccess: (sakContext, { saksnummer }) => {
      queryClient.setQueryData(getSakContextQuery(saksnummer, undefined).queryKey, sakContext);
      return navigate({
        to: "/saksnummer/$saksId/brevvelger",
        params: { saksId: sakContext.sak.saksId.toString() },
        search: { ...search, vedtaksId: undefined },
      });
    },
  });

  return (
    //Saksnummer har ingen children som den skal dele styles med, men er likevel brukt som en parent node i route-strukturen
    //derfor styler vi komponenten selv her, og ikke i parent.
    //merk at saksnummer/$saksId, også har håndtering for styles og sine children.
    <form className="page-margins" onSubmit={handleSubmit((values) => hentSakContextMutation.mutate(values))}>
      <div
        css={css`
          display: flex;
          flex-direction: column;
          align-self: center;
          gap: var(--a-spacing-6);

          margin-top: var(--a-spacing-8);

          width: 340px;
        `}
      >
        <TextField {...register("saksnummer")} autoComplete="off" label="Saksnummer" />
        {hentSakContextMutation.error && (
          <ApiError
            error={hentSakContextMutation.error}
            title={hentSakContextMutation.error?.response?.data?.toString() || "Finner ikke saksnummer"}
          />
        )}
        <Button loading={hentSakContextMutation.isPending} type="submit">
          Åpne brevvelger
        </Button>
      </div>
    </form>
  );
}
