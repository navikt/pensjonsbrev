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
      <Modal.Body>
        <VStack gap="4">
          <div>
            <Label>Antall kolonner:</Label>
            <TextField
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
          </div>
          <div>
            <Label>Antall rader:</Label>
            <TextField
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
          </div>
        </VStack>
      </Modal.Body>

      <Modal.Footer>
        <HStack gap="4">
          <Button onClick={onCancel} type="button" variant="tertiary">
            Avbryt
          </Button>
          <Button onClick={() => onInsert(columnCount, rowCount)} type="button">
            Sett inn tabell
          </Button>
        </HStack>
      </Modal.Footer>
    </Modal>
  );
}
