import { Alert, Heading, Link, VStack } from "@navikt/ds-react";
import type { AxiosError } from "axios";
import React from "react";

import { ApiError } from "~/components/ApiError";

const BrevmalFormWrapper = (props: {
  onSubmit: (e: React.FormEvent<HTMLFormElement>) => void;
  formRef: React.RefObject<HTMLFormElement | null>;
  children: React.ReactNode;
}) => {
  const { onSubmit, formRef, children } = props;
  return (
    <VStack asChild gap="space-32">
      <form onSubmit={onSubmit} ref={formRef}>
        {children}
      </form>
    </VStack>
  );
};

export default BrevmalFormWrapper;

export const OrderLetterResult = (props: {
  error: Error | AxiosError<Error, unknown> | null;
  data: string | undefined;
}) => {
  return (
    <>
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
    </>
  );
};
