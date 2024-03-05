import { css } from "@emotion/react";
import { Button, TextField } from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import React from "react";
import { useForm } from "react-hook-form";

import { getSak } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { SakDto } from "~/types/apiTypes";

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

  const hentSakMutation = useMutation<SakDto, AxiosError<unknown>, { saksnummer: string }>({
    mutationFn: (values) => getSak.queryFn(values.saksnummer),
    onSuccess: (sak, values) => {
      queryClient.setQueryData(getSak.queryKey(values.saksnummer), sak);
      navigate({
        to: "/saksnummer/$sakId/brevvelger",
        params: { sakId: sak.sakId.toString() },
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
      onSubmit={handleSubmit((values) => hentSakMutation.mutate(values))}
    >
      <TextField {...register("saksnummer")} autoComplete="off" label="Saksnummer" />
      {hentSakMutation.error && (
        <ApiError
          error={hentSakMutation.error}
          title={hentSakMutation.error?.response?.data?.toString() || "Finner ikke saksnummer"}
        />
      )}
      <Button loading={hentSakMutation.isPending} type="submit">
        Åpne brevvelger
      </Button>
      <Button onClick={e => { e.preventDefault(); window.open("https://nrk.no");  }}>test link</Button>
    </form>
  );
}
