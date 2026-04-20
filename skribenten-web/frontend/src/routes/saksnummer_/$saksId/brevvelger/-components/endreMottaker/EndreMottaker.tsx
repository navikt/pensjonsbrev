import { ArrowCirclepathReverseIcon, PencilIcon } from "@navikt/aksel-icons";
import { Button, HStack, Label, Spacer, VStack } from "@navikt/ds-react";
import { useNavigate } from "@tanstack/react-router";
import { useState } from "react";

import { EndreMottakerModal } from "~/components/endreMottaker/EndreMottakerModal";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import { type Adresse } from "~/types/apiTypes";
import { type Mottaker } from "~/types/brev";
import { type Nullable } from "~/types/Nullable";
import { truncatedSha256Hash } from "~/utils/hashUtils";
import { trackEvent } from "~/utils/umami";

import { Route } from "../../route";

const EndreMottaker = (properties: {
  //kan være undefined fordi vi ikke kan gjøre noe manuellAdresse enda
  onManuellAdresseBekreft?: (a: Nullable<Adresse>) => void;
  saksId: string;
}) => {
  const [modalÅpen, setModalÅpen] = useState<boolean>(false);
  const navigate = useNavigate({ from: Route.fullPath });
  const { idTSSEkstern } = Route.useSearch();

  const mottaker: Nullable<Mottaker> = idTSSEkstern ? { type: "Samhandler", tssId: idTSSEkstern, navn: null } : null;

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
      <HStack align="center">
        <Label size="small">Mottaker</Label>
        <Spacer />
        {idTSSEkstern && (
          <Button
            icon={<ArrowCirclepathReverseIcon />}
            iconPosition="right"
            onClick={async () => {
              trackEvent("tilbakestill mottaker klikket", {
                kontekst: "exstream mal",
                saksId: await truncatedSha256Hash(properties.saksId),
              });
              navigate({
                search: (s) => ({ ...s, idTSSEkstern: undefined }),
                replace: true,
              });
            }}
            size="xsmall"
            type="button"
            variant="tertiary"
          >
            Tilbakestill
          </Button>
        )}
        <Button
          data-cy="toggle-endre-mottaker-modal"
          icon={<PencilIcon />}
          iconPosition="right"
          onClick={() => {
            trackEvent("endre mottaker klikket", { kontekst: "exstream mal", saksId: properties.saksId });
            setModalÅpen(true);
          }}
          size="xsmall"
          type="button"
          variant="tertiary"
        >
          Endre
        </Button>
      </HStack>
      <OppsummeringAvMottaker mottaker={mottaker} saksId={properties.saksId} withTitle={false} />
    </VStack>
  );
};

export default EndreMottaker;
