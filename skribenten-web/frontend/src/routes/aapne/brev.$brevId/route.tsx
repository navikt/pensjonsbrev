import { Alert, BodyLong, Button, Heading, Modal } from "@navikt/ds-react";
import { createFileRoute, redirect } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { useState } from "react";

import { getBrevInfoQuery } from "~/api/brev-queries";
import { queryClient } from "~/routes/__root";
import type { AttestForbiddenReason } from "~/utils/parseAttest403";

export const Route = createFileRoute("/aapne/brev/$brevId")({
  loader: async ({ params: { brevId } }) => {
    const brevIdNum = Number(brevId);

    let brevInfo;
    try {
      brevInfo = await queryClient.ensureQueryData(getBrevInfoQuery(brevIdNum));
    } catch (error) {
      const axiosError = error as AxiosError & { forbidReason?: AttestForbiddenReason };

      if (axiosError.response?.status === 403 && axiosError.forbidReason) {
        return { reason: axiosError.forbidReason };
      }
      throw error;
    }

    switch (brevInfo.status.type) {
      case "Kladd":
      case "UnderRedigering":
        throw redirect({
          to: "/saksnummer/$saksId/brev/$brevId",
          params: { saksId: String(brevInfo.saksId), brevId: brevIdNum },
        });

      case "Attestering": {
        throw redirect({
          to: "/saksnummer/$saksId/attester/$brevId/redigering",
          params: { saksId: String(brevInfo.saksId), brevId: String(brevIdNum) },
        });
      }

      case "Klar":
      case "Arkivert":
        throw redirect({
          to: "/saksnummer/$saksId/brevbehandler",
          params: { saksId: String(brevInfo.saksId) },
          search: { brevId: brevIdNum },
        });
    }
  },

  component: () => {
    return <ForbiddenModal />;
  },

  errorComponent: ({ error }) => (
    <Alert size="small" variant="error">
      Kunne ikke åpne brevet: {String((error as AxiosError).message)}
    </Alert>
  ),
});

function ForbiddenModal() {
  const [open, setOpen] = useState(true);
  const data = Route.useLoaderData() as { reason?: AttestForbiddenReason };

  if (!data?.reason) return null;

  let heading: string;
  let body: string;

  switch (data.reason) {
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
    case "UNKNOWN_403": {
      throw new Error("Ukjent 403-feil");
    }
  }

  return (
    <Modal aria-label={heading} onClose={() => setOpen(false)} open={open}>
      <Modal.Header>
        <Heading size="large">{heading}</Heading>
      </Modal.Header>

      <Modal.Body>
        <BodyLong>{body}</BodyLong>
      </Modal.Body>

      <Modal.Footer>
        <Button onClick={() => setOpen(false)} variant="tertiary">
          Lukk
        </Button>
      </Modal.Footer>
    </Modal>
  );
}
