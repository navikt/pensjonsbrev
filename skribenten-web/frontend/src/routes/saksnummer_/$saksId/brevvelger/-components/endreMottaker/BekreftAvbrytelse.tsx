import { BodyShort, Button, HStack, VStack } from "@navikt/ds-react";

const BekreftAvbrytelse = (properties: { onBekreftAvbryt: () => void; onIkkeAvbryt: () => void }) => {
  return (
    <VStack gap="space-20">
      <BodyShort size="medium">
        Infoen du har skrevet inn blir ikke lagret. Du kan ikke angre denne handlingen.{" "}
      </BodyShort>
      <HStack gap="space-16" justify="end">
        <Button onClick={properties.onIkkeAvbryt} type="button" variant="secondary">
          Nei, ikke avbryt
        </Button>
        <Button onClick={properties.onBekreftAvbryt} type="button" variant="danger">
          Ja, avbryt
        </Button>
      </HStack>
    </VStack>
  );
};

export default BekreftAvbrytelse;
