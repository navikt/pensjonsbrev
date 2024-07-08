import { css } from "@emotion/react";
import { BodyShort, Button, HStack, VStack } from "@navikt/ds-react";

const BekreftAvbrytelse = (properties: { onBekreftAvbryt: () => void; onIkkeAvbryt: () => void }) => {
  return (
    <VStack>
      <BodyShort>Infoen du har skrevet inn blir ikke lagret. Du kan ikke angre denne handlingen. </BodyShort>
      <HStack
        css={css`
          align-self: flex-end;
        `}
        gap="4"
      >
        <Button onClick={properties.onIkkeAvbryt} type="button" variant="secondary">
          Ikke avbryt
        </Button>
        <Button onClick={properties.onBekreftAvbryt} type="button" variant="danger">
          Avbryt
        </Button>
      </HStack>
    </VStack>
  );
};

export default BekreftAvbrytelse;
