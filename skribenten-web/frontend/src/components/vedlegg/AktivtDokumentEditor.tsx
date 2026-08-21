import { type ReactNode } from "react";

import { useAktivtDokument } from "~/components/vedlegg/AktivtDokumentContext";
import { ManagedVedleggEditor } from "~/components/vedlegg/ManagedVedleggEditor";
import { useRedigerbareVedlegg } from "~/components/vedlegg/useRedigerbareVedlegg";
import { type BrevResponse } from "~/types/brev";

/**
 * Renders the editor surface for whichever document is active. The brev renderer is passed in
 * because it owns the brev's editor session; a vedlegg gets its own session here.
 */
export const AktivtDokumentEditor = (props: {
  saksId: string;
  brev: BrevResponse;
  freeze: boolean;
  renderBrev: () => ReactNode;
}) => {
  const { aktivtDokument } = useAktivtDokument();
  const vedleggQuery = useRedigerbareVedlegg({ saksId: props.saksId, brevId: props.brev.info.id });

  if (aktivtDokument.type === "brev") {
    return props.renderBrev();
  }

  const vedlegg = vedleggQuery.data?.find((v) => v.vedleggId === aktivtDokument.vedleggId);
  // The route clears an unknown ?vedlegg= value, so this only shows while the list is loading.
  if (!vedlegg) {
    return props.renderBrev();
  }

  return (
    <ManagedVedleggEditor
      brev={props.brev}
      freeze={props.freeze}
      saksId={props.saksId}
      vedleggId={vedlegg.vedleggId}
      vedleggtittel={vedlegg.tittel}
    />
  );
};
