import { css } from "@emotion/react";
import { Box, Button, Label, VStack } from "@navikt/ds-react";
import { Link } from "@tanstack/react-router";

const ArkivertBrev = (props: { saksId: string }) => {
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
          params={{ saksId: props.saksId }}
          size="small"
          to="/saksnummer/$saksId/brevbehandler"
          variant="tertiary"
        >
          Gå til brevbehandler
        </Button>
      </VStack>
    </Box>
  );
};

export default ArkivertBrev;
