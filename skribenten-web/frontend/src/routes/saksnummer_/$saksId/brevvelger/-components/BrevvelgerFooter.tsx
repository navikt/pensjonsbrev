import { css } from "@emotion/react";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { Button, HStack } from "@navikt/ds-react";
import { useMutationState } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";

import type { Nullable } from "~/types/Nullable";

import type { SubmitTemplateOptions } from "../route";
import { Route } from "../route";

const BrevvelgerFooter = (props: {
  saksId: string;
  antallBrevKlarTilSending: number;
  onSubmitClick: Nullable<SubmitTemplateOptions>;
}) => {
  const navigate = useNavigate({ from: Route.fullPath });
  const { enhetsId, vedtaksId } = Route.useSearch();
  const harBrevKlarTilSending = props.antallBrevKlarTilSending > 0;
  //henter mutations som er pending for å vise loading på knapp - vi har kun en mutation som kan være pending
  const mutationState = useMutationState({ filters: { status: "pending" } });

  return (
    <HStack
      css={css`
        padding: 8px 12px;
        border-top: 1px solid var(--a-gray-200);
      `}
      justify={"end"}
    >
      <Button
        onClick={() =>
          navigate({
            to: "/saksnummer/$saksId/brevbehandler",
            params: { saksId: props.saksId.toString() },
            search: { enhetsId, vedtaksId },
          })
        }
        size="small"
        type="button"
        variant="tertiary"
      >
        {harBrevKlarTilSending
          ? `Du har ${props.antallBrevKlarTilSending} brev klar til sending. Gå til brevbehandler`
          : "Gå til brevbehandler"}
      </Button>
      {props.onSubmitClick && (
        <Button
          css={css`
            width: fit-content;
          `}
          data-cy="order-letter"
          icon={<ArrowRightIcon />}
          iconPosition="right"
          loading={mutationState.at(-1)?.status === "pending"}
          onClick={props.onSubmitClick.onClick}
          size="small"
          variant="primary"
        >
          Åpne brev
        </Button>
      )}
    </HStack>
  );
};

export default BrevvelgerFooter;
