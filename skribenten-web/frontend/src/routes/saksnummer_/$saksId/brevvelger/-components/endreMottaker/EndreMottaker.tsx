import { Button, HStack } from "@navikt/ds-react";
import { useNavigate } from "@tanstack/react-router";
import { useState } from "react";

import { EndreMottakerModal } from "~/components/endreMottaker/EndreMottakerModal";
import type { Adresse } from "~/types/apiTypes";
import type { Nullable } from "~/types/Nullable";

import { Route } from "../../route";

const EndreMottaker = (properties: {
  //kan være undefined fordi vi ikke kan gjøre noe manuellAdresse enda
  onManuellAdresseBekreft?: (a: Nullable<Adresse>) => void;
}) => {
  const [modalÅpen, setModalÅpen] = useState<boolean>(false);
  const navigate = useNavigate({ from: Route.fullPath });
  const { idTSSEkstern } = Route.useSearch();

  return (
    <div>
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
      <HStack>
        <Button
          data-cy="toggle-endre-mottaker-modal"
          onClick={() => setModalÅpen(true)}
          size="small"
          type="button"
          variant="secondary"
        >
          Endre mottaker
        </Button>
        {idTSSEkstern && (
          <Button
            onClick={() =>
              navigate({
                search: (s) => ({ ...s, idTSSEkstern: undefined }),
                replace: true,
              })
            }
            size="small"
            type="button"
            variant="tertiary"
          >
            Tilbakestill mottaker
          </Button>
        )}
      </HStack>
    </div>
  );
};

export default EndreMottaker;
