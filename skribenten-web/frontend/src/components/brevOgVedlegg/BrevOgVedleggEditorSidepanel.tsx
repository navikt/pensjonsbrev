import { css } from "@emotion/react";
import { Tabs } from "@navikt/ds-react";
import { type ReactNode, useEffect, useState } from "react";

import { useActiveDocument } from "~/components/brevOgVedlegg/ActiveDocumentContext";
import { useRedigerbareVedlegg } from "~/components/vedlegg/useRedigerbareVedlegg";
import { VedleggPanel } from "~/components/vedlegg/VedleggPanel";

const BREVMAL_TAB = "brevmal";
const VEDLEGG_TAB = "vedlegg";

const sidepanelStyle = css`
  /* The side panel scrolls, so the tabs stay put instead of scrolling away with the content. */
  & > .aksel-tabs__tablist-wrapper {
    position: sticky;
    top: 0;
    z-index: 1;
    background: var(--ax-bg-default);
    height: var(--ax-space-48);
  }

  /* Aksel sizes tabs by content — with two tabs they split the panel width evenly instead. */
  & .aksel-tabs__tab--small {
    width: 50%;
  }

  & > .aksel-tabs__tabpanel {
    padding-top: var(--ax-space-16);
  }
`;

export const BrevOgVedleggEditorSidepanel = (props: { saksId: string; brevId: number; brevmalPanel: ReactNode }) => {
  const { activeDocument, redigeringsflate, selectBrev, selectVedlegg } = useActiveDocument();
  const vedleggQuery = useRedigerbareVedlegg({
    saksId: props.saksId,
    brevId: props.brevId,
    redigeringsflate,
  });
  const [activeTab, setActiveTab] = useState(activeDocument.type === "vedlegg" ? VEDLEGG_TAB : BREVMAL_TAB);

  // Keep the tab aligned with URL-driven document changes, including normalization of an unknown vedlegg.
  useEffect(() => {
    setActiveTab(activeDocument.type === "vedlegg" ? VEDLEGG_TAB : BREVMAL_TAB);
  }, [activeDocument.type]);

  // Switching tabs also switches the active document shown in the editor.
  const handleSelectTab = async (tab: string) => {
    if (tab === BREVMAL_TAB) {
      if (await selectBrev()) {
        setActiveTab(tab);
      }
      return;
    }

    if (vedleggQuery.isError) {
      setActiveTab(tab);
      return;
    }

    const firstVedlegg = vedleggQuery.data?.[0];
    if (firstVedlegg && (await selectVedlegg(firstVedlegg.vedleggId))) {
      setActiveTab(tab);
    }
  };

  // Show the tabs only when the letter has editable attachments, but keep them visible on error so the issue can be shown.
  const showTabs = (vedleggQuery.data?.length ?? 0) > 0 || vedleggQuery.isError;
  if (!showTabs) {
    return props.brevmalPanel;
  }

  return (
    <Tabs
      className="brev-og-vedlegg-editor-sidepanel"
      css={sidepanelStyle}
      onChange={(tab) => void handleSelectTab(tab)}
      size="small"
      value={activeTab}
    >
      <Tabs.List>
        <Tabs.Tab label="Brevmal" value={BREVMAL_TAB} />
        <Tabs.Tab label="Vedlegg" value={VEDLEGG_TAB} />
      </Tabs.List>
      <Tabs.Panel value={BREVMAL_TAB}>{props.brevmalPanel}</Tabs.Panel>
      <Tabs.Panel value={VEDLEGG_TAB}>
        <VedleggPanel vedleggQuery={vedleggQuery} />
      </Tabs.Panel>
    </Tabs>
  );
};
