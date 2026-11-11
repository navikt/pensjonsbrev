import { BodyShort, Box, ExpansionCard, VStack } from "@navikt/ds-react";
import { useEffect, useState } from "react";

import { caretAtDomSelection } from "../caret/domCaret";
import { type Caret, pathKey, resolve } from "../model/paths";
import { useEditorV2 } from "./EditorContextV2";

const describeCaret = (caret: Caret | undefined) =>
  caret ? `${pathKey(caret.path)} · innhold ${caret.content} · offset ${caret.offset}` : "–";

export default function DebugPanelV2() {
  const { state } = useEditorV2();
  const [domCaret, setDomCaret] = useState<Caret>();

  useEffect(() => {
    const update = () => setDomCaret(caretAtDomSelection());
    document.addEventListener("selectionchange", update);
    return () => document.removeEventListener("selectionchange", update);
  }, []);

  return (
    <Box
      background="raised"
      borderColor="neutral-subtle"
      borderWidth="1 0 0 0"
      maxHeight="40vh"
      overflowY="auto"
      padding="space-8"
    >
      <VStack gap="space-4">
        <BodyShort size="small">Fokus: {describeCaret(state.focus)}</BodyShort>
        <BodyShort size="small">DOM-markør: {describeCaret(domCaret)}</BodyShort>
        <BodyShort size="small">
          Lagring: {state.saveStatus} · historikk {state.history.entryPointer + 1}/{state.history.entries.length}
        </BodyShort>
        <ExpansionCard aria-label="Fokusert beholder" size="small">
          <ExpansionCard.Header>
            <ExpansionCard.Title size="small">Fokusert beholder</ExpansionCard.Title>
          </ExpansionCard.Header>
          <ExpansionCard.Content>
            <pre>{JSON.stringify(resolve(state.redigertBrev, state.focus.path), null, 2)}</pre>
          </ExpansionCard.Content>
        </ExpansionCard>
        <ExpansionCard aria-label="Dokument" size="small">
          <ExpansionCard.Header>
            <ExpansionCard.Title size="small">Dokument</ExpansionCard.Title>
          </ExpansionCard.Header>
          <ExpansionCard.Content>
            <pre>{JSON.stringify(state.redigertBrev, null, 2)}</pre>
          </ExpansionCard.Content>
        </ExpansionCard>
      </VStack>
    </Box>
  );
}
