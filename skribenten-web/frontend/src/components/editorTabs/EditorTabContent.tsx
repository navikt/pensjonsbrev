import { type ReactNode } from "react";

import { type BrevResponse } from "~/types/brev";

import { ManagedVedleggEditor } from "./documents/ManagedVedleggEditor";
import { useEditorTabsContext } from "./EditorTabsProvider";
import { vedleggIdFromTabId } from "./types";

/**
 * Decides what to render in the editor surface based on the active tab, so the route stays
 * orchestration rather than a chain of if/else. The brev renderer is passed in (it already exists
 * and is brev-session-specific); a redigerbart vedlegg renders a real ManagedVedleggEditor.
 */
export const EditorTabContent = (props: { saksId: string; brev: BrevResponse; renderBrev: () => ReactNode }) => {
  const { activeTab } = useEditorTabsContext();

  if (!activeTab || activeTab.type === "brev") {
    return props.renderBrev();
  }

  switch (activeTab.type) {
    case "redigerbartVedlegg": {
      return (
        <ManagedVedleggEditor brev={props.brev} saksId={props.saksId} vedleggId={vedleggIdFromTabId(activeTab.id)} />
      );
    }
    // "brev" is handled by the early return above; the default keeps the switch exhaustive and is a
    // safe fallback to the brev renderer.
    default: {
      return props.renderBrev();
    }
  }
};
