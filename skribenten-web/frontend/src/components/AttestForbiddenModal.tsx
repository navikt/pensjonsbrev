import { BodyShort, Button, Heading, Modal } from "@navikt/ds-react";
import { useEffect } from "react";

import type { AttestForbiddenReason } from "~/utils/parseAttest403";
import { trackEvent } from "~/utils/umami";

function AttestForbiddenModal({ reason, onClose }: { reason: AttestForbiddenReason; onClose: () => void }) {
  useEffect(() => {
    const reasonLabels: Record<AttestForbiddenReason, string> = {
      MISSING_ATTESTANT_ROLE: "mangler attestant-rolle",
      SELF_ATTESTATION: "egen attestering",
      ALREADY_ATTESTED: "allerede attestert",
      UNKNOWN_403: "ukjent 403",
    };

    trackEvent("attestering blokkert", {
      grunn: reasonLabels[reason],
      grunnKode: reason,
    });
  }, [reason]);

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
