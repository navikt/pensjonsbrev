import { css } from "@emotion/react";
import { Tabs } from "@navikt/ds-react";
import { type ReactNode, useEffect, useState } from "react";

import { useAktivtDokument } from "~/components/vedlegg/AktivtDokumentContext";
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
  }

  & > .aksel-tabs__tabpanel {
    padding-top: var(--ax-space-16);
  }
`;

/**
 * The brev editor's side panel: brevmal options for the letter, and the letter's vedlegg. The
 * brevmal tab is the default because the vast majority of letters are edited without ever touching
 * a vedlegg.
 */
export const BrevSidepanel = (props: { saksId: string; brevId: number; brevmalPanel: ReactNode }) => {
  const { aktivtDokument, velgBrev } = useAktivtDokument();
  const [aktivTab, setAktivTab] = useState(aktivtDokument.type === "vedlegg" ? VEDLEGG_TAB : BREVMAL_TAB);

  // A deep link straight to a vedlegg (?vedlegg=…) must open on the tab that shows it.
  useEffect(() => {
    if (aktivtDokument.type === "vedlegg") {
      setAktivTab(VEDLEGG_TAB);
    }
  }, [aktivtDokument.type]);

  // The brevmal controls edit the letter, so going back to that tab also brings the letter back into
  // the editor — otherwise they would be changing a document the user cannot see.
  const velgTab = (tab: string) => {
    setAktivTab(tab);
    if (tab === BREVMAL_TAB) {
      velgBrev();
    }
  };

  return (
    <Tabs css={sidepanelStyle} onChange={velgTab} size="small" value={aktivTab}>
      <Tabs.List>
        <Tabs.Tab label="Brevmal" value={BREVMAL_TAB} />
        <Tabs.Tab label="Vedlegg" value={VEDLEGG_TAB} />
      </Tabs.List>
      <Tabs.Panel value={BREVMAL_TAB}>{props.brevmalPanel}</Tabs.Panel>
      <Tabs.Panel value={VEDLEGG_TAB}>
        <VedleggPanel brevId={props.brevId} saksId={props.saksId} />
      </Tabs.Panel>
    </Tabs>
  );
};
