import { css } from "@emotion/react";
import { ArrowLeftIcon } from "@navikt/aksel-icons";
import { Button, HStack, VStack } from "@navikt/ds-react";

import type { SamhandlerTypeCode } from "~/types/apiTypes";
import { SAMHANDLER_ENUM_TO_TEXT } from "~/types/nameMappings";

import { VerifySamhandler } from "../Adresse";

const HentOgVisSamhandlerAdresse = (properties: {
  id: string;
  typeMottaker?: SamhandlerTypeCode;
  onTilbakeTilSøk: () => void;
  onBekreftNyMottaker: (id: string) => void;
  onCloseIntent: () => void;
}) => {
  return (
    <VStack gap="4">
      <VStack gap="6">
        <VerifySamhandler
          idTSSEkstern={properties.id}
          typeMottaker={properties.typeMottaker ? SAMHANDLER_ENUM_TO_TEXT[properties.typeMottaker] : undefined}
        />
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
