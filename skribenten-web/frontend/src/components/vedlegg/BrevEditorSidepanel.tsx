import { css } from "@emotion/react";
import { Tabs } from "@navikt/ds-react";
import { type ReactNode, useEffect, useState } from "react";

import { useAktivtDokument } from "~/components/vedlegg/AktivtDokumentContext";
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

/**
 * The letter editor's side panel: brevmal options for the letter, and the letter's attachments. The
 * brevmal tab is the default because the vast majority of letters are edited without ever touching
 * an attachment.
 */
export const BrevEditorSidepanel = (props: { saksId: string; brevId: number; brevmalPanel: ReactNode }) => {
  const { aktivtDokument, velgBrev, velgVedlegg } = useAktivtDokument();
  const vedleggQuery = useRedigerbareVedlegg({ saksId: props.saksId, brevId: props.brevId });
  const [aktivTab, setAktivTab] = useState(aktivtDokument.type === "vedlegg" ? VEDLEGG_TAB : BREVMAL_TAB);

  // A deep link straight to a vedlegg (?vedlegg=…) must open on the tab that shows it.
  useEffect(() => {
    if (aktivtDokument.type === "vedlegg") {
      setAktivTab(VEDLEGG_TAB);
    }
  }, [aktivtDokument.type]);

  // The brevmal controls edit the letter, so going back to that tab also brings the letter back into
  // the editor — otherwise they would be changing a document the user cannot see.
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

  // Fanene finnes bare når brevet faktisk har redigerbare vedlegg. Ved feil viser vi dem likevel,
  // slik at feilen blir synlig i stedet for at funksjonen forsvinner i stillhet.
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
