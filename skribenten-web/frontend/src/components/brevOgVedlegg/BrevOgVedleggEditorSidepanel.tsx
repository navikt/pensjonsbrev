import { Box, Tabs } from "@navikt/ds-react";
import { type ReactNode, useEffect, useId, useState } from "react";

import { useActiveDocument } from "~/components/brevOgVedlegg/ActiveDocumentContext";
import { useRedigerbareVedlegg } from "~/components/vedlegg/useRedigerbareVedlegg";
import { VedleggPanel } from "~/components/vedlegg/VedleggPanel";

const BREVMAL_TAB = "brevmal";
const VEDLEGG_TAB = "vedlegg";

export const BrevOgVedleggEditorSidepanel = (props: { saksId: string; brevId: number; brevmalPanel: ReactNode }) => {
  const { activeDocument, redigeringsflate, selectBrev, selectVedlegg } = useActiveDocument();
  const vedleggQuery = useRedigerbareVedlegg({
    saksId: props.saksId,
    brevId: props.brevId,
    redigeringsflate,
  });
  const id = useId();
  const brevmalTabId = `${id}-brevmal-tab`;
  const brevmalPanelId = `${id}-brevmal-panel`;
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
  const showBrevmal = !showTabs || activeTab === BREVMAL_TAB;

  return (
    <Box
      asChild
      paddingBlock={{ xs: "space-0 space-12", lg: "space-0 space-16" }}
      paddingInline={{ xs: "space-12", lg: "space-24" }}
    >
      <Tabs
        className="brev-og-vedlegg-editor-sidepanel"
        css={
          showTabs
            ? {
                "> div:first-child": {
                  position: "sticky",
                  top: 0,
                  zIndex: 1,
                  height: "var(--ax-space-48)",
                  background: "var(--ax-bg-default)",
                },
              }
            : undefined
        }
        fill
        onChange={(tab) => void handleSelectTab(tab)}
        size="small"
        value={activeTab}
      >
        {showTabs && (
          <Tabs.List>
            <Tabs.Tab aria-controls={brevmalPanelId} id={brevmalTabId} label="Brevmal" value={BREVMAL_TAB} />
            <Tabs.Tab label="Vedlegg" value={VEDLEGG_TAB} />
          </Tabs.List>
        )}

        {/* Keep the panel mounted when tabs appear so focused fields are not replaced. */}
        {/* biome-ignore lint/a11y/useAriaPropsSupportedByRole: The label is present only with the tabpanel role. */}
        <div
          aria-labelledby={showTabs ? brevmalTabId : undefined}
          className={showTabs ? "aksel-tabs__tabpanel" : undefined}
          data-state={showTabs ? (showBrevmal ? "active" : "inactive") : undefined}
          hidden={!showBrevmal}
          id={showTabs ? brevmalPanelId : undefined}
          role={showTabs ? "tabpanel" : undefined}
          tabIndex={showTabs ? 0 : undefined}
        >
          {showBrevmal ? props.brevmalPanel : null}
        </div>
        {showTabs && (
          <Tabs.Panel value={VEDLEGG_TAB}>
            <VedleggPanel vedleggQuery={vedleggQuery} />
          </Tabs.Panel>
        )}
      </Tabs>
    </Box>
  );
};
