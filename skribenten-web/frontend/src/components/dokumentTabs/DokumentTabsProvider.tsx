import { createContext, type ReactNode, useContext, useMemo } from "react";

import { BREV_TAB_ID, type DokumentTab } from "./types";

type DokumentTabsContextValue = {
  tabs: DokumentTab[];
  activeTabId: string;
  activeTab: DokumentTab | undefined;
  selectTab: (tabId: string) => void;
};

const DokumentTabsContext = createContext<DokumentTabsContextValue | null>(null);

/**
 * Owns "which document is active" for the editor and exposes selection. It is route-agnostic: the
 * caller supplies the current active tab id (derived from the ?vedlegg= search param) and an
 * onActiveTabChange that navigates. Keeping the URL in the route — not here — is what lets the
 * attest route adopt the same tabs later with its own persistence.
 *
 * Per-document editor session state (brev/vedlegg) is intentionally NOT held here; each document
 * manages its own session. This provider only tracks selection.
 */
export const DokumentTabsProvider = (props: {
  tabs: DokumentTab[];
  activeTabId: string | undefined;
  onActiveTabChange: (tabId: string) => void;
  children: ReactNode;
}) => {
  const activeTabId = props.activeTabId ?? BREV_TAB_ID;

  const value = useMemo<DokumentTabsContextValue>(() => {
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

  return <DokumentTabsContext.Provider value={value}>{props.children}</DokumentTabsContext.Provider>;
};

export const useDokumentTabsContext = (): DokumentTabsContextValue => {
  const context = useContext(DokumentTabsContext);
  if (!context) {
    throw new Error("useDokumentTabsContext must be used within a <DokumentTabsProvider>");
  }
  return context;
};
