import { BodyLong, Button, Modal } from "@navikt/ds-react";
import type React from "react";

export type WarnModalKind = "fritekst" | "tekstValg" | "fritekstOgTekstValg" | "avsnittIkkeIMal";

// Used discriminated props so that only relevant props are required for each kind.
// here in this case "count" is only relevant for "fritekst", "fritekstOgTekstValg" and "avsnittIkkeIMal" kinds.
// "fortsettLabel" is optional and defaults to "Fortsett til brevbehandler".
type WarnModalCommonProps = {
  open: boolean;
  onClose: () => void;
  onFortsett: () => void;
  fortsettLabel?: string;
};

type WarnModalProps = WarnModalCommonProps &
  (
    | { kind: "tekstValg" }
    | { kind: "fritekst"; count: number }
    | { kind: "fritekstOgTekstValg"; count: number }
    | { kind: "avsnittIkkeIMal"; count: number }
  );

export const WarnModal: React.FC<WarnModalProps> = ({ kind, open, onClose, onFortsett, fortsettLabel, ...rest }) => {
  // Early return so the modal unmounts when open is false
  if (!open) {
    return null;
  }
  // normalize count when available
  const count = "count" in rest ? Math.max(0, rest.count) : undefined;

  // Heading text
  const heading = (() => {
    switch (kind) {
      case "tekstValg":
        return "Du må velge tekst";
      case "fritekst":
        return `Du må fylle ut ${count} fritekstfelt`;
      case "fritekstOgTekstValg":
        return `Du må fylle ut ${count} fritekstfelt og velge tekst`;
      case "avsnittIkkeIMal":
        return `Du må velge om du vil beholde eller slette ${count} avsnitt`;
      default:
        return "";
    }
  })();

  // Body text
  const body = (() => {
    switch (kind) {
      case "tekstValg":
        return "Du kan fortsette, men brevet kan ikke sendes før du har valgt et eller flere obligatoriske tekstvalg.";
      case "fritekst":
        return "Du kan fortsette, men brevet kan ikke sendes før alle fritekstfelter er fylt ut.";
      case "fritekstOgTekstValg":
        return "Du kan fortsette, men brevet kan ikke sendes før alle fritekstfelter er fylt ut og du har valgt et eller flere obligatoriske tekstvalg.";
      case "avsnittIkkeIMal":
        return count === 1
          ? "Dette avsnittet er markert i brevet. Velg «Behold» eller «Slett». Du kan fortsette, men brevet kan ikke sendes før dette er gjort."
          : "Disse avsnittene er markert i brevet. Velg «Behold» eller «Slett» for hvert av dem. Du kan fortsette, men brevet kan ikke sendes før dette er gjort.";
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
            onFortsett();
          }}
          type="button"
          variant="tertiary"
        >
          {fortsettLabel ?? "Fortsett til brevbehandler"}
        </Button>
      </Modal.Footer>
    </Modal>
  );
};
