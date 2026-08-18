import { css } from "@emotion/react";
import { Tabs } from "@navikt/ds-react";

import { type EditorTab } from "./types";

const tabsBarStyle = css`
  background: var(--ax-bg-accent-soft);
  max-height: 48px;
  border-bottom: 1px solid var(--ax-border-neutral-subtleA);

  && .aksel-tabs__tab--small {
    min-height: 3rem;
  }
`;

/**
 * Presentational top navigation for the editor's tabs (the brev and its redigerbare vedlegg). It
 * knows about active state, labels, and selection — and nothing about how documents are fetched,
 * saved, reset, or edited. Those concerns belong to the active tab's renderer.
 */
export const EditorTabsBar = (props: {
  tabs: EditorTab[];
  activeTabId: string;
  onSelectTab: (tabId: string) => void;
}) => (
  <Tabs css={tabsBarStyle} onChange={(value) => props.onSelectTab(value)} size="small" value={props.activeTabId}>
    <Tabs.List>
      {props.tabs.map((tab) => (
        <Tabs.Tab key={tab.id} label={tab.label} value={tab.id} />
      ))}
    </Tabs.List>
  </Tabs>
);
