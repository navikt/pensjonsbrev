import { css } from "@emotion/react";
import { Button, TextField } from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import React from "react";
import { useForm } from "react-hook-form";

import { getSak } from "../../api/skribenten-api-endpoints";
import { sakRoute } from "../../tanStackRoutes";
import type { SakDto } from "../../types/apiTypes";
import { BrevvelgerTabOptions } from "./BrevvelgerPage";

export function VelgSakPage() {
  const queryClient = useQueryClient();
  const navigate = useNavigate({ from: sakRoute.id });
  const { handleSubmit, register } = useForm({
    defaultValues: {
      saksnummer: "22972355",
    },
  });

  const hentSakMutation = useMutation<SakDto, AxiosError<unknown>, { saksnummer: string }>({
    mutationFn: (values) => getSak.queryFn(values.saksnummer),
    onSuccess: (sak, values) => {
      queryClient.setQueryData(getSak.queryKey(values.saksnummer), sak);
      navigate({
        to: "/saksnummer/$sakId/brevvelger",
        params: { sakId: sak.sakId.toString() },
        search: { fane: BrevvelgerTabOptions.BREVMALER },
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
        width: 290px;
      `}
      onSubmit={handleSubmit((values) => hentSakMutation.mutate(values))}
    >
      <TextField
        {...register("saksnummer")}
        autoComplete="off"
        error={hentSakMutation.isError ? "Finner ikke saksnummer" : undefined}
        label="Saksnummer"
      />
      <Button loading={hentSakMutation.isPending} type="submit">
        Åpne brevvelger
      </Button>
    </form>
  );
}
