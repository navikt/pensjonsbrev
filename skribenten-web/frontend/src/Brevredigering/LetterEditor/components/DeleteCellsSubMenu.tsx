import { css } from "@emotion/react";
import { Button, Radio, RadioGroup, VStack } from "@navikt/ds-react";
import * as CM from "@radix-ui/react-context-menu";
import { useState } from "react";

export default function DeleteCellsSubMenu({ onDelete }: { onDelete: (choice: "row" | "col" | "table") => void }) {
  const [choice, setChoice] = useState<"row" | "col" | "table">("row");

  return (
    <VStack
      css={css`
        width: 220px;
        padding: 0.5rem 0.75rem;
      `}
    >
      <RadioGroup
        hideLegend
        legend="Hva vil du slette?"
        onChange={(selectedValue) => setChoice(selectedValue as "row" | "col" | "table")}
        size="small"
        value={choice}
      >
        <Radio value="row">Hele raden</Radio>
        <Radio value="col">Hele kolonnen</Radio>
        <Radio value="table">Hele tabellen</Radio>
      </RadioGroup>
      <CM.Item asChild onSelect={() => onDelete(choice)}>
        <Button
          css={css`
            align-self: flex-end;
            width: auto;
          `}
          onClick={() => onDelete(choice)}
          size="small"
        >
          Slett
        </Button>
      </CM.Item>
    </VStack>
  );
}
