import { BoxNew, Button, Label, VStack } from "@navikt/ds-react";

import { Route } from "~/routes/saksnummer_/$saksId/attester.$brevId/redigering";

const ArkivertBrev = (props: { saksId: string }) => {
  const navigate = Route.useNavigate();
  const { vedtaksId, enhetsId } = Route.useSearch();

  return (
    <BoxNew background="default" flexGrow="1" padding="space-24">
      <VStack align="start" gap="space-8">
        <Label size="small">Brevet er arkivert, og kan derfor ikke redigeres.</Label>
        <Button
          onClick={() =>
            navigate({
              to: "/saksnummer/$saksId/brevbehandler",
              params: { saksId: props.saksId },
              search: { vedtaksId, enhetsId },
            })
          }
          size="small"
          variant="secondary"
        >
          Gå til brevbehandler
        </Button>
      </VStack>
    </BoxNew>
  );
};

export default ArkivertBrev;
