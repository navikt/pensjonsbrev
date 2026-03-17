import { PencilIcon } from "@navikt/aksel-icons";
import { Button, HStack, Label, VStack } from "@navikt/ds-react";
import { useNavigate } from "@tanstack/react-router";
import { useState } from "react";

import { EndreMottakerModal } from "~/components/endreMottaker/EndreMottakerModal";
import HentOgVisAdresse from "~/components/endreMottaker/HentOgVisAdresse";
import { type Adresse } from "~/types/apiTypes";
import { type Nullable } from "~/types/Nullable";

import { Route } from "../../route";

const EndreMottaker = (properties: {
  //kan være undefined fordi vi ikke kan gjøre noe manuellAdresse enda
  onManuellAdresseBekreft?: (a: Nullable<Adresse>) => void;
  saksId: string;
}) => {
  const [modalÅpen, setModalÅpen] = useState<boolean>(false);
  const navigate = useNavigate({ from: Route.fullPath });
  const { idTSSEkstern } = Route.useSearch();

  return (
    <VStack gap="space-8">
      {modalÅpen && (
        <EndreMottakerModal
          error={null}
          isPending={false}
          onBekreftNyMottaker={(bekreftetMottaker) => {
            setModalÅpen(false);
            //hvis mottaker er en string, betyr det at det er en samhandler
            if (typeof bekreftetMottaker === "string") {
              properties.onManuellAdresseBekreft?.(null);
              return navigate({
                search: (s) => ({ ...s, idTSSEkstern: bekreftetMottaker }),
                replace: true,
              });
            } else {
              //hvis mottaker er en adresse, betyr det at det er en manuell adresse
              properties.onManuellAdresseBekreft?.(bekreftetMottaker);
              return navigate({
                search: (s) => ({ ...s, idTSSEkstern: undefined }),
                replace: true,
              });
            }
          }}
          onClose={() => setModalÅpen(false)}
          resetOnBekreftState={() => {}}
          skalKunOppdatereSamhandler
          åpen={modalÅpen}
        />
      )}
      <HStack align="center" justify="space-between">
        <Label size="small">Mottaker</Label>
        <Button
          data-cy="toggle-endre-mottaker-modal"
          icon={<PencilIcon />}
          iconPosition="right"
          onClick={() => setModalÅpen(true)}
          size="xsmall"
          type="button"
          variant="tertiary"
        >
          Endre
        </Button>
      </HStack>
      <HentOgVisAdresse sakId={properties.saksId} samhandlerId={idTSSEkstern} />
      {idTSSEkstern && (
        <HStack>
          <Button
            css={{ margin: "0 calc(-1 * var(--ax-space-8))" }}
            onClick={() =>
              navigate({
                search: (s) => ({ ...s, idTSSEkstern: undefined }),
                replace: true,
              })
            }
            size="xsmall"
            type="button"
            variant="tertiary"
          >
            Tilbakestill mottaker
          </Button>
        </HStack>
      )}
    </VStack>
  );
};

export default EndreMottaker;
