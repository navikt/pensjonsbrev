import { BodyShort, Button, Heading, Modal } from "@navikt/ds-react";

import type { AttestForbiddenReason } from "~/utils/parseAttest403";

function AttestForbiddenModal({ reason, onClose }: { reason: AttestForbiddenReason; onClose: () => void }) {
  let heading = "";
  let body = "";

  switch (reason) {
    case "MISSING_ATTESTANT_ROLE":
      heading = "Du kan ikke attestere dette brevet";
      body = "Du har ikke rollen som attestant.";
      break;
    case "SELF_ATTESTATION":
      heading = "Du kan ikke attestere dette brevet";
      body = "Du kan ikke attestere ditt eget brev.";
      break;
    case "ALREADY_ATTESTED":
      heading = "Brevet er allerede attestert";
      body = "Brevet er allerede attestert av en annen saksbehandler.";
      break;
    case "UNKNOWN_403":
      heading = "Ukjent 403-feil";
      body = "Kontakt systemansvarlig.";
  }

  return (
    <Modal aria-label={heading} onClose={onClose} open>
      <Modal.Header>
        <Heading size="large">{heading}</Heading>
      </Modal.Header>
      <Modal.Body>
        <BodyShort>{body}</BodyShort>
      </Modal.Body>
      <Modal.Footer>
        <Button onClick={onClose} variant="tertiary">
          Lukk
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default AttestForbiddenModal;
