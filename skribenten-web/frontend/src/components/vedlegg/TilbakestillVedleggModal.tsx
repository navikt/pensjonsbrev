import { BodyLong, Button, HStack, Modal } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";

import { type EditAttachment } from "~/types/brev";

const TilbakestillVedleggModal = (props: {
  vedleggtittel: string;
  åpen: boolean;
  onClose: () => void;
  tilbakestill: () => Promise<EditAttachment>;
  resetEditor: (vedlegg: EditAttachment) => void;
}) => {
  const tilbakestillMutation = useMutation<EditAttachment, Error>({
    mutationFn: () => props.tilbakestill(),
    onSuccess: (vedlegg) => {
      props.resetEditor(vedlegg);
      props.onClose();
    },
  });

  return (
    <Modal header={{ heading: "Vil du tilbakestille vedlegget?" }} onClose={props.onClose} open={props.åpen} portal>
      <Modal.Body>
        <BodyLong>
          Innholdet du har endret eller lagt til i {props.vedleggtittel} vil bli slettet. Vedlegget blir liggende i
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
            loading={tilbakestillMutation.isPending}
            onClick={() => tilbakestillMutation.mutate()}
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
