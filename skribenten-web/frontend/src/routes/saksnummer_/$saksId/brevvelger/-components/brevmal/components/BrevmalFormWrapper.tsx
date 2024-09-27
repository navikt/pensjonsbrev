import { css } from "@emotion/react";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { Alert, Button, Heading, Link } from "@navikt/ds-react";
import type { AxiosError } from "axios";
import { useEffect, useMemo } from "react";

import { ApiError } from "~/components/ApiError";

const BrevmalFormWrapper = (props: {
  onSubmit: () => void;
  formRef: React.RefObject<HTMLFormElement>;
  children: React.ReactNode;
}) => {
  return (
    <form
      css={css`
        display: flex;
        flex-direction: column;
        flex: 1;
      `}
      onSubmit={props.onSubmit}
      ref={props.formRef}
    >
      <div
        css={css`
          display: flex;
          flex-direction: column;
          flex: 1;
          gap: 2rem;
        `}
      >
        {props.children}
      </div>
    </form>
  );
};

export default BrevmalFormWrapper;

export const OrderLetterResult = (props: {
  error: Error | AxiosError<Error, unknown> | null;
  data: string | undefined;
}) => {
  return (
    <div>
      {props.error && <ApiError error={props.error} title="Bestilling feilet" />}
      {props.data && (
        <Alert data-cy="order-letter-success-message" size="small" variant="success">
          <Heading level="3" size="xsmall">
            Brev bestilt
          </Heading>
          <span>
            Åpnet ikke brevet seg? <Link href={props.data}>Klikk her for å prøve igjen</Link>
          </span>
        </Alert>
      )}
    </div>
  );
};

export const useSubmitBrevmalButton = (args: {
  onClick: () => void;
  onMount: (el: React.ReactNode) => void;
  status: "error" | "pending" | "idle" | "success" | null;
}) => {
  const submitButton = useMemo(() => {
    return (
      <Button
        css={css`
          width: fit-content;
        `}
        data-cy="order-letter"
        icon={<ArrowRightIcon />}
        iconPosition="right"
        loading={args.status === "pending"}
        onClick={args.onClick}
        size="small"
        type="submit"
        variant="primary"
      >
        Åpne brev
      </Button>
    );
    // https://github.com/TanStack/query/issues/1858
    // useMutation returnerer ny objekt for hver render, selv om vi er bare interessert i en liten del av det som har endret seg
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [args.status]);

  useEffect(() => {
    args.onMount(submitButton);
  }, [submitButton, args]);

  return submitButton;
};
