import { css } from "@emotion/react";
import { Button, HStack, Modal, TextField, VStack } from "@navikt/ds-react";
import { useState } from "react";

interface InsertTableDialogProps {
  open: boolean;
  onCancel: () => void;
  onInsert: (cols: number, rows: number) => void;
}

function InsertTableDialog({ open, onCancel, onInsert }: InsertTableDialogProps) {
  const [columnCount, setColumnCount] = useState("3");
  const [rowCount, setRowCount] = useState("2");

  const numCols = Number(columnCount) || 0;
  const numRows = Number(rowCount) || 0;

  return (
    <Modal
      data-cy="insert-table-modal"
      header={{ heading: "Sett inn tabell" }}
      onClose={onCancel}
      open={open}
      width={360}
    >
      <Modal.Body style={{ paddingTop: "0.5rem" }}>
        <VStack gap="space-16">
          {[
            {
              value: columnCount,
              updateValue: setColumnCount,
              label: "Antall kolonner:",
              id: "num-cols",
              cyTag: "input-cols",
            },
            {
              value: rowCount,
              updateValue: setRowCount,
              label: "Antall rader:",
              id: "num-rows",
              cyTag: "input-rows",
            },
          ].map(({ value, updateValue, label, id, cyTag }) => (
            <HStack align="center" gap="space-16" justify="space-between" key={id} paddingInline="space-40">
              <TextField
                css={css`
                  align-items: center;
                  display: flex;
                  flex-grow: 1;
                  justify-content: space-between;

                  input {
                    width: 5rem;
                  }

                  label {
                    font-weight: var(--ax-font-weight-regular);
                  }
                `}
                data-cy={cyTag}
                id={id}
                inputMode="numeric"
                label={label}
                max={20}
                min={1}
                onChange={(e) => {
                  const value = e.target.value;
                  if (value === "" || (/^\d+$/.test(value) && Number(value) <= 20)) {
                    updateValue(value);
                  }
                }}
                size="small"
                step={1}
                type="number"
                value={value}
              />
            </HStack>
          ))}
        </VStack>
      </Modal.Body>
      <Modal.Footer>
        <HStack gap="space-16">
          <Button data-cy="insert-table-cancel-btn" onClick={onCancel} size="small" type="button" variant="secondary">
            Avbryt
          </Button>
          <Button
            data-cy="insert-table-confirm-btn"
            disabled={numCols < 1 || numRows < 1}
            onClick={() => onInsert(Math.max(1, numCols), Math.max(1, numRows))}
            size="small"
            type="button"
          >
            Sett inn tabell
          </Button>
        </HStack>
      </Modal.Footer>
    </Modal>
  );
}
export default InsertTableDialog;
