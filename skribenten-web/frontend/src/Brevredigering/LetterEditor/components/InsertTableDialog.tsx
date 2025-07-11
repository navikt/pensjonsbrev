import { css } from "@emotion/react";
import { Button, HStack, Label, Modal, TextField, VStack } from "@navikt/ds-react";
import { useState } from "react";

interface InsertTableDialogProps {
  open: boolean;
  onCancel: () => void;
  onInsert: (cols: number, rows: number) => void;
}

export default function InsertTableDialog({ open, onCancel, onInsert }: InsertTableDialogProps) {
  const [columnCount, setColumnCount] = useState(3);
  const [rowCount, setRowCount] = useState(2);

  return (
    <Modal header={{ heading: "Sett inn tabell" }} onClose={onCancel} open={open} width={360}>
      <Modal.Body style={{ paddingTop: "0.5rem" }}>
        <VStack gap="4">
          <HStack align="center" gap="4">
            <Label
              css={css`
                min-width: 8rem;
                text-align: right;
                font-weight: 400;
              `}
              htmlFor="num-cols"
              size="small"
            >
              Antall kolonner:
            </Label>
            <TextField
              css={css`
                width: 5rem;
              `}
              hideLabel
              label="Antall kolonner"
              max={20}
              min={1}
              onChange={(e) => setColumnCount(Number(e.target.value))}
              size="small"
              step={1}
              type="number"
              value={columnCount}
            />
          </HStack>
          <HStack gap="4">
            <Label
              css={css`
                min-width: 8rem;
                text-align: right;
                font-weight: 400;
                align-content: center;
              `}
              htmlFor="num-rows"
              size="small"
            >
              Antall rader:
            </Label>
            <TextField
              css={css`
                width: 5rem;
              `}
              hideLabel
              label="Antall rader"
              max={20}
              min={1}
              onChange={(e) => setRowCount(Number(e.target.value))}
              size="small"
              step={1}
              type="number"
              value={rowCount}
            />
          </HStack>
        </VStack>
      </Modal.Body>

      <Modal.Footer>
        <HStack gap="4">
          <Button onClick={onCancel} size="small" type="button" variant="secondary">
            Avbryt
          </Button>
          <Button onClick={() => onInsert(columnCount, rowCount)} size="small" type="button">
            Sett inn tabell
          </Button>
        </HStack>
      </Modal.Footer>
    </Modal>
  );
}
