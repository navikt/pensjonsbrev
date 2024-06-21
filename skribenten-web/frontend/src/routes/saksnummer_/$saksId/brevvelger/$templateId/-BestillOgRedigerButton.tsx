import { css } from "@emotion/react";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { Alert, Button, Heading, Link, VStack } from "@navikt/ds-react";
import type { UseMutationResult } from "@tanstack/react-query";
import type { AxiosError } from "axios";

import { ApiError } from "~/components/ApiError";

export default function BestillOgRedigerButton({
  orderMutation,
}: {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any -- request type is not relevant for this component
  orderMutation: UseMutationResult<string, AxiosError<Error> | Error, any>;
}) {
  return (
    <VStack gap="4">
      {orderMutation.error && <ApiError error={orderMutation.error} title="Bestilling feilet" />}
      {orderMutation.isSuccess ? (
        <Alert data-cy="order-letter-success-message" size="small" variant="success">
          <Heading level="3" size="xsmall">
            Brev bestilt
          </Heading>
          <span>
            Åpnet ikke brevet seg? <Link href={orderMutation.data}>Klikk her for å prøve igjen</Link>
          </span>
        </Alert>
      ) : (
        <Button
          css={css`
            width: fit-content;
          `}
          data-cy="order-letter"
          icon={<ArrowRightIcon />}
          iconPosition="right"
          loading={orderMutation.isPending}
          size="small"
          type="submit"
          variant="primary"
        >
          Åpne brev
        </Button>
      )}
    </VStack>
  );
}
