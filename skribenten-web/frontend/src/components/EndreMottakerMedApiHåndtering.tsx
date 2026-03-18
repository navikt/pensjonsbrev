import { PencilIcon, XMarkOctagonFillIcon } from "@navikt/aksel-icons";
import { Button, HStack, Label, VStack } from "@navikt/ds-react";

import { EndreMottakerModal } from "~/components/endreMottaker/EndreMottakerModal";
import { useEndreMottaker } from "~/hooks/useEndreMottaker";
import type { BrevInfo } from "~/types/brev";

import OppsummeringAvMottaker from "./OppsummeringAvMottaker";

const EndreMottakerMedOppsummeringOgApiHåndtering = (props: {
  saksId: string;
  brev: BrevInfo;
  withOppsummeringTitle?: boolean;
  kanTilbakestilleMottaker?: boolean;
  withGap?: boolean;
}) => {
  const {
    modalÅpen,
    åpneModal,
    lukkModal,
    endreMottaker,
    resetEndreMottaker,
    endreMottakerError,
    endreMottakerIsPending,
    fjernMottaker,
    fjernMottakerIsPending,
    fjernMottakerIsError,
  } = useEndreMottaker(props.saksId, props.brev.id);

  return (
    <VStack gap={props.withGap ? "space-8" : "space-0"}>
      {modalÅpen && (
        <EndreMottakerModal
          error={endreMottakerError}
          isPending={endreMottakerIsPending}
          onBekreftNyMottaker={endreMottaker}
          onClose={lukkModal}
          resetOnBekreftState={resetEndreMottaker}
          åpen={modalÅpen}
        />
      )}
      <VStack gap="space-8">
        {props.withOppsummeringTitle && (
          <HStack align="center" justify="space-between">
            <Label size="small">Mottaker</Label>
            <Button
              icon={<PencilIcon />}
              iconPosition="right"
              onClick={åpneModal}
              size="xsmall"
              type="button"
              variant="tertiary"
            >
              Endre
            </Button>
          </HStack>
        )}
        {!props.withOppsummeringTitle && (
          <Button
            icon={<PencilIcon />}
            iconPosition="right"
            onClick={åpneModal}
            size="xsmall"
            type="button"
            variant="tertiary"
          >
            Endre
          </Button>
        )}
        <OppsummeringAvMottaker mottaker={props.brev.mottaker} saksId={props.saksId} withTitle={false} />
      </VStack>
      {props.brev.mottaker && props.kanTilbakestilleMottaker && (
        <HStack>
          <Button
            css={{ margin: "0 calc(-1 * var(--ax-space-8))" }}
            loading={fjernMottakerIsPending}
            onClick={fjernMottaker}
            size="xsmall"
            type="button"
            variant="tertiary"
          >
            Tilbakestill mottaker
          </Button>
          {fjernMottakerIsError && (
            <XMarkOctagonFillIcon
              css={{
                alignSelf: "center",
                color: "var(--ax-text-logo)",
              }}
              title="error"
            />
          )}
        </HStack>
      )}
    </VStack>
  );
};

export default EndreMottakerMedOppsummeringOgApiHåndtering;
