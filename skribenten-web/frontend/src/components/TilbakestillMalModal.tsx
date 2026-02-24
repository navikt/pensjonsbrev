import { BodyLong, Button, HStack, Modal } from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";

import { getBrev, tilbakestillBrev } from "~/api/brev-queries";
import type { BrevResponse } from "~/types/brev";

const TilbakestillMalModal = (props: {
  brevId: number;
  åpen: boolean;
  onClose: () => void;
  resetEditor: (brevResponse: BrevResponse) => void;
}) => {
  const queryClient = useQueryClient();
  const tilbakestillMutation = useMutation<BrevResponse, Error>({
    mutationFn: () => tilbakestillBrev(props.brevId),
    onSuccess: (response) => {
      queryClient.setQueryData(getBrev.queryKey(props.brevId), response);
      props.resetEditor(response);
      props.onClose();
    },
  });

  return (
    <Modal
      header={{
        heading: "Vil du tilbakestille brevmalen?",
      }}
      onClose={props.onClose}
      open={props.åpen}
      portal
      width={600}
    >
      <Modal.Body>
        <BodyLong>Innholdet du har endret eller lagt til i brevet vil bli slettet.</BodyLong>
        <BodyLong>Du kan ikke angre denne handlingen.</BodyLong>
      </Modal.Body>
      <Modal.Footer>
        <HStack gap="space-16">
          <Button onClick={props.onClose} type="button" variant="tertiary">
            Nei, behold brevet
          </Button>

          <Button
            data-color="danger"
            loading={tilbakestillMutation.isPending}
            onClick={() => tilbakestillMutation.mutate()}
            type="button"
            variant="primary"
          >
            Ja, tilbakestill malen
          </Button>
        </HStack>
      </Modal.Footer>
    </Modal>
  );
};

export default TilbakestillMalModal;
