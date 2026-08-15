import { PadlockLockedIcon, PlusIcon } from "@navikt/aksel-icons";
import { Button, HStack, Tabs } from "@navikt/ds-react";

import { type EditorTab } from "./types";

const TabLabel = (props: { tab: EditorTab }) => (
  <HStack align="center" gap="space-4">
    {props.tab.locked && <PadlockLockedIcon aria-hidden fontSize="1rem" />}
    <span>{props.tab.label}</span>
  </HStack>
);

/**
 * Presentational top navigation for the editor's documents. It knows about active state, labels,
 * lock state, selection, and "add vedlegg" — and nothing about how documents are fetched, saved,
 * reset, or edited. Those concerns belong to the active document's renderer and persistence adapter.
 */
export const EditorTabsBar = (props: {
  tabs: EditorTab[];
  activeTabId: string;
  onSelectTab: (tabId: string) => void;
  onAddVedlegg: () => void;
}) => (
  <HStack align="center" justify="space-between" paddingInline="space-8">
    <Tabs onChange={(value) => props.onSelectTab(value)} size="small" value={props.activeTabId}>
      <Tabs.List>
        {props.tabs.map((tab) => (
          <Tabs.Tab key={tab.id} label={<TabLabel tab={tab} />} value={tab.id} />
        ))}
      </Tabs.List>
    </Tabs>
    <Button icon={<PlusIcon aria-hidden />} onClick={props.onAddVedlegg} size="small" type="button" variant="tertiary">
      Vedlegg
    </Button>
  </HStack>
);
