import { Box, Button, Label, VStack } from "@navikt/ds-react";

const ArkivertBrev = (props: { onGaTilBrevbehandler: () => void }) => {
  return (
    <Box background="default" flexGrow="1" padding="space-24">
      <VStack align="start" gap="space-8">
        <Label size="small">Brevet er arkivert, og kan derfor ikke redigeres.</Label>
        <Button onClick={props.onGaTilBrevbehandler} size="small" variant="secondary">
          Gå til brevbehandler
        </Button>
      </VStack>
    </Box>
  );
};

export default ArkivertBrev;
