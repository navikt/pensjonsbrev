import { css } from "@emotion/react";
import { Button, TextField } from "@navikt/ds-react";
import React from "react";
import { useForm } from "react-hook-form";

export function VelgSakPage() {
  const { handleSubmit, register } = useForm();

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
      onSubmit={handleSubmit((values) => console.log(values))}
    >
      <TextField {...register("saksnummer")} autoComplete="off" label="Saksnummer" />
      <Button type="submit">Åpne brevvelger</Button>
    </form>
  );
}
