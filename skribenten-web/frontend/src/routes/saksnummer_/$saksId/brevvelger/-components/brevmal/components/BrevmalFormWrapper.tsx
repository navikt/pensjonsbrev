import { css } from "@emotion/react";
import { Alert, BodyShort, Heading, Link } from "@navikt/ds-react";
import type { AxiosError } from "axios";

import { ApiError } from "~/components/ApiError";
import type { BestillOgRedigerBrevResponse } from "~/types/apiTypes";

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
  data: BestillOgRedigerBrevResponse | undefined;
}) => {
  return (
    <div>
      {props.error && <ApiError error={props.error} title="Bestilling feilet" />}
      {props.data && (
        <Alert data-cy="order-letter-success-message" size="small" variant="success">
          <Heading level="3" size="xsmall">
            Brev bestilt
          </Heading>
          {props.data.journalpostId && (
            <BodyShort>Brevet er bestilt og sendt til mottaker med journalpostId {props.data.journalpostId}</BodyShort>
          )}
          {props.data.url && (
            <span>
              Åpnet ikke brevet seg? <Link href={props.data.url}>Klikk her for å prøve igjen</Link>
            </span>
          )}
        </Alert>
      )}
    </div>
  );
};
