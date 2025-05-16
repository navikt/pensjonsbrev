import { css } from "@emotion/react";
import { ArrowCirclepathIcon } from "@navikt/aksel-icons";
import { BodyLong, Button, HStack, Modal } from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import React, { useState } from "react";

import { getBrev, tilbakestillBrev } from "~/api/brev-queries";
import type { BrevResponse } from "~/types/brev";

const TilbakestillBrev = (props: { brevId: number; resetEditor: (brevResponse: BrevResponse) => void }) => {
  const [vilTilbakestilleMal, setVilTilbakestilleMal] = useState(false);

  return (
    <>
      {vilTilbakestilleMal && (
        <TilbakestillMalModal
          brevId={props.brevId}
          onClose={() => setVilTilbakestilleMal(false)}
          resetEditor={props.resetEditor}
          åpen={vilTilbakestilleMal}
        />
      )}
      <Button onClick={() => setVilTilbakestilleMal(true)} size="small" type="button" variant="danger">
        <HStack align={"center"} gap="1">
          <ArrowCirclepathIcon
            css={css`
              transform: scaleX(-1);
            `}
            fontSize="1.5rem"
            title="Tilbakestill mal"
          />
          Tilbakestill malen
        </HStack>
      </Button>
    </>
  );
};

export default TilbakestillBrev;

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
      css={css`
        border-radius: 0.25rem;
      `}
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
        <HStack gap="4">
          <Button onClick={props.onClose} type="button" variant="tertiary">
            Nei, behold brevet
          </Button>

          <Button
            loading={tilbakestillMutation.isPending}
            onClick={() => tilbakestillMutation.mutate()}
            type="button"
            variant="danger"
          >
            Ja, tilbakestill malen
          </Button>
        </HStack>
      </Modal.Footer>
    </Modal>
  );
};
