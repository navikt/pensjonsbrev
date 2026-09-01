import { css } from "@emotion/react";
import { Tabs } from "@navikt/ds-react";
import { type ReactNode, useEffect, useState } from "react";

import { useBrevOgVedleggEditor } from "~/components/vedlegg/BrevOgVedleggEditorContext";
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
  }

  /* Aksel sizes tabs by content — with two tabs they split the panel width evenly instead. */
  & .aksel-tabs__tab--small {
    width: 50%;
  }

  & > .aksel-tabs__tabpanel {
    padding-top: var(--ax-space-16);
  }
`;

export const BrevEditorSidepanel = (props: { saksId: string; brevId: number; brevmalPanel: ReactNode }) => {
  const { aktivtDokument, velgBrev, velgVedlegg } = useBrevOgVedleggEditor();
  const vedleggQuery = useRedigerbareVedlegg({ saksId: props.saksId, brevId: props.brevId });
  const [aktivTab, setAktivTab] = useState(aktivtDokument.type === "vedlegg" ? VEDLEGG_TAB : BREVMAL_TAB);

  // Keep the tab aligned with URL-driven document changes, including normalization of an unknown vedlegg.
  useEffect(() => {
    setAktivTab(aktivtDokument.type === "vedlegg" ? VEDLEGG_TAB : BREVMAL_TAB);
  }, [aktivtDokument.type]);

  // Switching tabs also switches the active document shown in the editor.
  const velgTab = async (tab: string) => {
    if (tab === BREVMAL_TAB) {
      if (await velgBrev()) {
        setAktivTab(tab);
      }
      return;
    }

    const foersteVedlegg = vedleggQuery.data?.[0];
    if (!foersteVedlegg) {
      setAktivTab(tab);
    } else if (await velgVedlegg(foersteVedlegg.vedleggId)) {
      setAktivTab(tab);
    }
  };

  // Show the tabs only when the letter has editable attachments, but keep them visible on error so the issue can be shown.
  const visFaner = (vedleggQuery.data?.length ?? 0) > 0 || vedleggQuery.isError;
  if (!visFaner) {
    return props.brevmalPanel;
  }

  return (
    <Tabs css={sidepanelStyle} onChange={(tab) => void velgTab(tab)} size="small" value={aktivTab}>
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
