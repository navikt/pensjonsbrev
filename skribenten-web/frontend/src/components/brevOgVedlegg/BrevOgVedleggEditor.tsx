import { type ReactNode } from "react";

import { ApiError } from "~/components/ApiError";
import { useActiveDocument } from "~/components/brevOgVedlegg/ActiveDocumentContext";
import { CenteredLoader } from "~/components/CenteredLoader";
import { ManagedAttachmentEditor } from "~/components/vedlegg/ManagedAttachmentEditor";
import { useRedigerbareVedlegg } from "~/components/vedlegg/useRedigerbareVedlegg";
import { type BrevResponse } from "~/types/brev";

/**
 * Renders either the letter editor or the active attachment editor.
 */
export const BrevOgVedleggEditor = (props: {
  saksId: string;
  brev: BrevResponse;
  freeze: boolean;
  renderBrev: () => ReactNode;
}) => {
  const { activeDocument, redigeringsflate } = useActiveDocument();
  const vedleggQuery = useRedigerbareVedlegg({
    saksId: props.saksId,
    brevId: props.brev.info.id,
    redigeringsflate,
  });

  if (activeDocument.type === "brev") {
    return props.renderBrev();
  }

  // The attachment's title comes from the vedlegg-list, so a deep link has to wait for it rather than flash
  // the letter on the way to the attachment.
  if (vedleggQuery.isPending) {
    return <CenteredLoader label="Henter vedlegg..." verticalStrategy="height" />;
  }
  if (vedleggQuery.isError) {
    return <ApiError error={vedleggQuery.error} title="Klarte ikke å hente vedlegg" />;
  }

  const vedlegg = vedleggQuery.data.find((v) => v.vedleggId === activeDocument.vedleggId);
  if (!vedlegg) {
    return props.renderBrev();
  }

  return (
    <ManagedAttachmentEditor
      brev={props.brev}
      freeze={props.freeze}
      // Keying by the full editing identity forces a fresh query owner and activation lifecycle on
      // every direct attachment switch (A -> B -> A included), not just a fresh inner editor: without
      // this key, switching attachments would reuse the mounted query owner and its already-fetched,
      // now stale, cached data instead of fetching the newly active attachment.
      key={`${props.saksId}-${props.brev.info.id}-${redigeringsflate}-${vedlegg.vedleggId}`}
      redigeringsflate={redigeringsflate}
      saksId={props.saksId}
      vedleggId={vedlegg.vedleggId}
      vedleggTitle={vedlegg.tittel}
    />
  );
};
