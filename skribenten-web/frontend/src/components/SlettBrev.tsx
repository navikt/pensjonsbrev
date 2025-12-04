import { css } from "@emotion/react";
import { TrashIcon } from "@navikt/aksel-icons";
import { BodyLong, Button, ErrorMessage, HStack, Modal } from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";

import { hentAlleBrevForSak, slettBrev } from "~/api/sak-api-endpoints";
import type { BrevInfo } from "~/types/brev";

export const SlettBrev = (properties: {
  sakId: string;
  brevId: number;
  buttonText: string;
  onSlettSuccess: () => void;
  modalTexts?: {
    heading?: string;
    body?: string;
    buttonYes?: string;
    buttonNo?: string;
  };
}) => {
  const [vilSletteBrev, setVilSletteBrev] = useState(false);

  return (
    <div
      css={css`
        height: 32px;
      `}
    >
      {vilSletteBrev && (
        <SlettBrevModal
          brevId={properties.brevId}
          onClose={() => setVilSletteBrev(false)}
          onSlettSuccess={properties.onSlettSuccess}
          sakId={properties.sakId}
          texts={properties.modalTexts}
          åpen={vilSletteBrev}
        />
      )}
      <Button onClick={() => setVilSletteBrev(true)} size="small" type="button" variant="danger">
        <HStack align="center" gap="space-4">
          <TrashIcon fontSize="1.5rem" title="slett-ikon" /> {properties.buttonText}
        </HStack>
      </Button>
    </div>
  );
};

const SlettBrevModal = (properties: {
  sakId: string;
  brevId: number;
  åpen: boolean;
  onClose: () => void;
  onSlettSuccess: () => void;
  texts?: {
    heading?: string;
    body?: string;
    buttonYes?: string;
    buttonNo?: string;
  };
}) => {
  const queryClient = useQueryClient();

  const slett = useMutation({
    mutationFn: () => slettBrev(properties.sakId, properties.brevId),
    onSuccess: () => {
      queryClient.setQueryData(hentAlleBrevForSak.queryKey(properties.sakId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo?.filter((brev) => brev.id !== properties.brevId),
      );
    },
  });

  return (
    <Modal
      header={{
        heading: !slett.isSuccess ? (properties.texts?.heading ?? "Vil du slette brevet?") : "Brevet er slettet",
      }}
      onClose={properties.onClose}
      open={properties.åpen}
      portal
      width={450}
    >
      <Modal.Body>
        <BodyLong>
          {!slett.isSuccess
            ? (properties.texts?.body ?? "Brevet vil bli slettet, og du kan ikke angre denne handlingen.")
            : `Brevet med id ${properties.brevId} er slettet. Vil du gå til brevbehandler?`}
        </BodyLong>
        {slett.isError && <ErrorMessage>Kunne ikke slette brev {properties.brevId}. Vil du prøve igjen?</ErrorMessage>}
      </Modal.Body>
      <Modal.Footer>
        <HStack gap="space-16">
          <Button disabled={slett.isPending} onClick={properties.onClose} type="button" variant="tertiary">
            {slett.isSuccess ? "Avbryt" : (properties.texts?.buttonNo ?? "Nei, behold brevet")}
          </Button>
          {!slett.isSuccess ? (
            <Button loading={slett.isPending} onClick={() => slett.mutate()} type="button" variant="danger">
              {properties.texts?.buttonYes ?? "Ja, slett brevet"}
            </Button>
          ) : (
            <Button onClick={() => properties.onSlettSuccess()} type="button" variant="primary">
              {"Gå til brevbehandler"}
            </Button>
          )}
        </HStack>
      </Modal.Footer>
    </Modal>
  );
};
