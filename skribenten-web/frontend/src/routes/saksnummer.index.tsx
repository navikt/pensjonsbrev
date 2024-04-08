import { css } from "@emotion/react";
import { Button, TextField } from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import React from "react";
import { useForm } from "react-hook-form";

import { getSakContext } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { SakContextDto } from "~/types/apiTypes";

export const Route = createFileRoute("/saksnummer/")({
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

  const hentSakContextMutation = useMutation<SakContextDto, AxiosError<unknown>, { saksnummer: string }>({
    mutationFn: (values) => getSakContext.queryFn(values.saksnummer, undefined),
    onSuccess: (sakContext, values) => {
      queryClient.setQueryData(getSakContext.queryKey(values.saksnummer), sakContext);
      navigate({
        to: "/saksnummer/$saksId/brevvelger",
        params: { saksId: sakContext.sak.saksId.toString() },
      });
    },
  });

  return (
    <form
      css={css`
        display: flex;
        gap: var(--a-spacing-6);
        flex-direction: column;
        align-self: center;
        margin-top: var(--a-spacing-8);
        width: 340px;
      `}
      onSubmit={handleSubmit((values) => hentSakContextMutation.mutate(values))}
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
    </form>
  );
}
