import { ArrowRightIcon } from "@navikt/aksel-icons";
import { Box, Button, HStack } from "@navikt/ds-react";
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
    <Box asChild borderColor="neutral-subtle" borderWidth="1 0 0 0">
      <HStack gap="space-8" justify="end" paddingBlock="space-8" paddingInline="space-16">
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
          variant="secondary"
        >
          {harBrevKlarTilSending
            ? `Du har ${props.antallBrevKlarTilSending} brev klar til sending. Gå til brevbehandler`
            : "Gå til brevbehandler"}
        </Button>
        {props.onSubmitClick && (
          <Button
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
    </Box>
  );
};

export default BrevvelgerFooter;
