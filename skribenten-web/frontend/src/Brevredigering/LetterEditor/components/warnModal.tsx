import { BodyLong, Button, Modal } from "@navikt/ds-react";
import React from "react";

export type WarnModalKind = "fritekst" | "requiredSaksbehandlerValg" | "both";

// Used discriminated props so that only relevant props are required for each kind.
// here in this case "count" is only relevant for "fritekst" and "both" kinds.
type WarnModalProps =
  | {
      kind: "fritekst";
      count: number;
      open: boolean;
      onClose: () => void;
      onFortsett: () => void;
    }
  | {
      kind: "both";
      count: number;
      open: boolean;
      onClose: () => void;
      onFortsett: () => void;
    }
  | {
      kind: "requiredSaksbehandlerValg";
      open: boolean;
      onClose: () => void;
      onFortsett: () => void;
    };

export const WarnModal: React.FC<WarnModalProps> = ({ kind, open, onClose, onFortsett, ...rest }) => {
  // normalize count when available
  const count = "count" in rest ? Math.max(0, rest.count) : undefined;

  // Heading text
  const heading = (() => {
    switch (kind) {
      case "requiredSaksbehandlerValg":
        return "Du må velge tekst";
      case "fritekst":
        return `Du må fylle ut ${count} fritekstfelt`;
      case "both":
        return `Du må fylle ut ${count} fritekstfelt og velge tekst`;
      default:
        return "";
    }
  })();

  // Body text
  const body = (() => {
    switch (kind) {
      case "requiredSaksbehandlerValg":
        return "Du kan fortsette til brevbehandler, men brevet kan ikke sendes før du har valgt et obligatorisk tekstalternativ.";
      case "fritekst":
        return "Du kan fortsette til brevbehandler, men brevet kan ikke sendes før alle fritekstfelter er fylt ut.";
      case "both":
        return "Du kan fortsette til brevbehandler, men brevet kan ikke sendes før alle fritekstfelter er fylt ut og du har valgt et obligatorisk tekstalternativ.";
    }
  })();

  return (
    <Modal header={{ heading, closeButton: true }} onClose={onClose} open={open} width={600}>
      <Modal.Body>
        <BodyLong>{body}</BodyLong>
      </Modal.Body>
      <Modal.Footer>
        <Button onClick={onClose} type="button">
          Bli her
        </Button>
        <Button
          onClick={() => {
            onClose();
            onFortsett();
          }}
          type="button"
          variant="tertiary"
        >
          Fortsett til brevbehandler
        </Button>
      </Modal.Footer>
    </Modal>
  );
};
