import { css } from "@emotion/react";
import { ArrowLeftIcon } from "@navikt/aksel-icons";
import { BodyShort, Button, HStack, VStack } from "@navikt/ds-react";

import { VerifySamhandler } from "../Adresse";

const HentOgVisSamhandlerAdresse = (properties: {
  id: string;
  onTilbakeTilSøk: () => void;
  onBekreftNyMottaker: (id: string) => void;
  onCloseIntent: () => void;
}) => {
  return (
    <VStack gap="4">
      <VStack gap="6">
        <BodyShort>TODO - et felt for mottakertype i tabellen</BodyShort>
        <VerifySamhandler idTSSEkstern={properties.id} />
        <Button
          css={css`
            width: fit-content;
            align-self: flex-start;
          `}
          icon={<ArrowLeftIcon />}
          onClick={properties.onTilbakeTilSøk}
          size="small"
          variant="tertiary"
        >
          Tilbake til søk
        </Button>
      </VStack>
      <HStack
        css={css`
          align-self: flex-end;
        `}
        gap="4"
      >
        <Button onClick={properties.onCloseIntent} size="small" type="button" variant="secondary">
          Avbryt
        </Button>
        <Button
          data-cy="bekreft-ny-mottaker"
          onClick={() => properties.onBekreftNyMottaker(properties.id)}
          size="small"
          variant="primary"
        >
          Bekreft ny mottaker
        </Button>
      </HStack>
    </VStack>
  );
};

export default HentOgVisSamhandlerAdresse;
