import { createContext, type ReactNode, useContext, useMemo } from "react";

import { BREV_TAB_ID, type EditorTab } from "./types";

type EditorTabsContextValue = {
  tabs: EditorTab[];
  activeTabId: string;
  activeTab: EditorTab | undefined;
  selectTab: (tabId: string) => void;
};

const EditorTabsContext = createContext<EditorTabsContextValue | null>(null);

/**
 * Owns "which document is active" for the editor and exposes selection. It is route-agnostic: the
 * caller supplies the current active tab id (derived from the ?vedlegg= search param) and an
 * onActiveTabChange that navigates. Keeping the URL in the route — not here — is what lets the
 * attest route adopt the same tabs later with its own persistence.
 *
 * Per-document editor session state (brev/vedlegg) is intentionally NOT held here; each document
 * manages its own session. This provider only tracks selection.
 */
export const EditorTabsProvider = (props: {
  tabs: EditorTab[];
  activeTabId: string | undefined;
  onActiveTabChange: (tabId: string) => void;
  children: ReactNode;
}) => {
  const activeTabId = props.activeTabId ?? BREV_TAB_ID;

  const value = useMemo<EditorTabsContextValue>(() => {
    const activeTab = props.tabs.find((tab) => tab.id === activeTabId);

    return {
      tabs: props.tabs,
      activeTabId,
      activeTab,
      selectTab: (tabId) => {
        // Selecting the brev clears the ?vedlegg= param; selecting a vedlegg sets it.
        props.onActiveTabChange(tabId);
      },
    };
  }, [props.tabs, activeTabId, props.onActiveTabChange]);

  return <EditorTabsContext.Provider value={value}>{props.children}</EditorTabsContext.Provider>;
};

export const useEditorTabsContext = (): EditorTabsContextValue => {
  const context = useContext(EditorTabsContext);
  if (!context) {
    throw new Error("useEditorTabsContext must be used within a <EditorTabsProvider>");
  }
  return context;
};
