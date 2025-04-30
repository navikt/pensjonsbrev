import { BodyLong, Button, Modal } from "@navikt/ds-react";

import type { ReservasjonResponse } from "~/types/brev";

const ReservertBrevError = ({
  reservasjon,
  doRetry,
  onNeiClick,
}: {
  reservasjon?: ReservasjonResponse;
  doRetry: () => void;
  onNeiClick: () => void;
}) => {
  if (reservasjon) {
    return (
      <Modal
        header={{ heading: "Brevet redigeres av noen andre", closeButton: false }}
        onClose={() => {}}
        open={!reservasjon.vellykket}
        width={478}
      >
        <Modal.Body>
          <BodyLong>
            Brevet er utilgjengelig for deg fordi {reservasjon.reservertAv.navn} har brevet åpent. Ønsker du å forsøke å
            åpne brevet på nytt?
          </BodyLong>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={doRetry} type="button">
            Ja, åpne på nytt
          </Button>
          <Button onClick={onNeiClick} type="button" variant="tertiary">
            Nei, gå til brevbehandler
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }
};

export default ReservertBrevError;
