import { css } from "@emotion/react";
import { Box, Button, Label, VStack } from "@navikt/ds-react";
import { Link } from "@tanstack/react-router";

import { Route } from "~/routes/saksnummer_/$saksId/attester.$brevId/redigering";

const ArkivertBrev = (props: { saksId: string }) => {
  const navigate = Route.useNavigate();
  const { vedtaksId, enhetsId } = Route.useSearch();

  return (
    <Box
      background="surface-default"
      css={css`
        display: flex;
        flex: 1;
      `}
      padding="6"
    >
      <VStack align="start" gap="2">
        <Label size="small">Brevet er arkivert, og kan derfor ikke redigeres.</Label>
        <Button
          as={Link}
          css={css`
            padding: 4px 0;
          `}
          onClick={() =>
            navigate({
              to: "/saksnummer/$saksId/brevbehandler",
              params: { saksId: props.saksId },
              search: { vedtaksId, enhetsId },
            })
          }
          size="small"
          variant="tertiary"
        >
          Gå til brevbehandler
        </Button>
      </VStack>
    </Box>
  );
};

export default ArkivertBrev;
