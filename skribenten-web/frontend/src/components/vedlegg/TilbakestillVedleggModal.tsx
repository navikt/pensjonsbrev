import { BodyLong, Button, HStack, Modal } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";

const TilbakestillVedleggModal = (props: {
  vedleggTitle: string;
  open: boolean;
  onClose: () => void;
  reset: () => Promise<void>;
}) => {
  const resetMutation = useMutation<void, Error>({
    mutationFn: () => props.reset(),
    onSuccess: props.onClose,
  });

  return (
    <Modal header={{ heading: "Vil du tilbakestille vedlegget?" }} onClose={props.onClose} open={props.open} portal>
      <Modal.Body>
        <BodyLong>
          Innholdet du har endret eller lagt til i {props.vedleggTitle} vil bli slettet. Vedlegget blir liggende i
          brevet. Du kan ikke angre denne handlingen.
        </BodyLong>
      </Modal.Body>
      <Modal.Footer>
        <HStack gap="space-16">
          <Button onClick={props.onClose} type="button" variant="tertiary">
            Nei, behold vedlegget
          </Button>
          <Button
            data-color="danger"
            loading={resetMutation.isPending}
            onClick={() => resetMutation.mutate()}
            type="button"
            variant="primary"
          >
            Ja, tilbakestill vedlegget
          </Button>
        </HStack>
      </Modal.Footer>
    </Modal>
  );
};

export default TilbakestillVedleggModal;
