import { type ReactNode } from "react";

import { useAktivtDokument } from "~/components/brevOgVedlegg/AktivtDokumentContext";
import { CenteredLoader } from "~/components/CenteredLoader";
import { ManagedVedleggEditor } from "~/components/vedlegg/ManagedVedleggEditor";
import { useRedigerbareVedlegg } from "~/components/vedlegg/useRedigerbareVedlegg";
import { type BrevResponse } from "~/types/brev";

/**
 * Renders either the letter editor or the active vedlegg editor.
 */
export const BrevOgVedleggEditor = (props: {
  saksId: string;
  brev: BrevResponse;
  freeze: boolean;
  renderBrev: () => ReactNode;
}) => {
  const { aktivtDokument, redigeringsflate } = useAktivtDokument();
  const vedleggQuery = useRedigerbareVedlegg({
    saksId: props.saksId,
    brevId: props.brev.info.id,
    redigeringsflate,
  });

  if (aktivtDokument.type === "brev") {
    return props.renderBrev();
  }

  // The attachment's title comes from the vedlegg-list, so a deep link has to wait for it rather than flash
  // the letter on the way to the attachment.
  if (vedleggQuery.isPending) {
    return <CenteredLoader label="Henter vedlegg..." verticalStrategy="height" />;
  }

  const vedlegg = vedleggQuery.data?.find((v) => v.vedleggId === aktivtDokument.vedleggId);
  if (!vedlegg) {
    return props.renderBrev();
  }

  return (
    <ManagedVedleggEditor
      brev={props.brev}
      freeze={props.freeze}
      redigeringsflate={redigeringsflate}
      saksId={props.saksId}
      vedleggId={vedlegg.vedleggId}
      vedleggtittel={vedlegg.tittel}
    />
  );
};
