import { Box, Heading, VStack } from "@navikt/ds-react";
import { type ReactNode } from "react";

import { type BrevResponse } from "~/types/brev";

import { useDokumentTabsContext } from "./DokumentTabsProvider";
import { VedleggEditor } from "./documents/VedleggEditor";
import { vedleggIdFromTabId } from "./types";

const Placeholder = (props: { title: string; description: string }) => (
  <Box asChild padding="space-24">
    <VStack align="center" gap="space-8">
      <Heading level="2" size="small">
        {props.title}
      </Heading>
      <p>{props.description}</p>
    </VStack>
  </Box>
);

/**
 * Decides what to render in the editor surface based on the active document, so the route stays
 * orchestration rather than a chain of if/else. The brev renderer is passed in (it already exists
 * and is brev-session-specific); a redigerbart vedlegg renders a real VedleggEditor; the rest are
 * placeholders introduced in their own phases.
 */
export const EditorDocumentSurface = (props: { saksId: string; brev: BrevResponse; renderBrev: () => ReactNode }) => {
  const { activeTab } = useDokumentTabsContext();

  if (!activeTab || activeTab.type === "brev") {
    return props.renderBrev();
  }

  switch (activeTab.type) {
    case "redigerbartVedlegg": {
      return <VedleggEditor brev={props.brev} saksId={props.saksId} vedleggId={vedleggIdFromTabId(activeTab.id)} />;
    }
    case "alltidValgbartVedlegg": {
      return (
        <Placeholder
          description="Skrivebeskyttet forhåndsvisning av vedlegg kommer i en senere fase."
          title={activeTab.label}
        />
      );
    }
    case "p1": {
      return <Placeholder description="P1-skjemaet flyttes hit i en senere fase." title="P1" />;
    }
    // "brev" is handled by the early return above; the default keeps the switch exhaustive and is a
    // safe fallback to the brev renderer.
    default: {
      return props.renderBrev();
    }
  }
};
