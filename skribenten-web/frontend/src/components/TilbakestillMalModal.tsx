import { BodyLong, Button, HStack, Modal } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";

const TilbakestillMalModal = (props: { åpen: boolean; onClose: () => void; resetLetter: () => Promise<void> }) => {
  const tilbakestillMutation = useMutation<void, Error>({
    mutationFn: props.resetLetter,
    onSuccess: props.onClose,
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
