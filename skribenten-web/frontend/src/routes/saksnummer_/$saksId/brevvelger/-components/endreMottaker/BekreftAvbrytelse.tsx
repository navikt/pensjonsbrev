import { css } from "@emotion/react";
import { BodyShort, Button, HStack, VStack } from "@navikt/ds-react";

const BekreftAvbrytelse = (properties: { onBekreftAvbryt: () => void; onIkkeAvbryt: () => void }) => {
  return (
    <VStack>
      <BodyShort size="small">
        Infoen du har skrevet inn blir ikke lagret. Du kan ikke angre denne handlingen.{" "}
      </BodyShort>
      <HStack
        css={css`
          align-self: flex-end;
        `}
        gap="4"
      >
        <Button onClick={properties.onIkkeAvbryt} size="small" type="button" variant="secondary">
          Nei, ikke avbryt
        </Button>
        <Button onClick={properties.onBekreftAvbryt} size="small" type="button" variant="danger">
          Ja, avbryt
        </Button>
      </HStack>
    </VStack>
  );
};

export default BekreftAvbrytelse;
