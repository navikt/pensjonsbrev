import { Alert, BodyShort, Button, ExpansionCard, HStack, Loader, VStack } from "@navikt/ds-react";

import { useRedigerbareVedlegg } from "~/components/vedlegg/useRedigerbareVedlegg";
import { useVedleggEditor } from "~/components/vedlegg/VedleggEditorContext";

/**
 * The "Vedlegg" side panel: the letter's vedlegg, where opening a redigerbart vedlegg also makes it
 * the document shown in the editor. Only redigerbare vedlegg are listed for now — alltid valgbare
 * vedlegg and P1 are still managed from brevbehandler.
 */
export const VedleggPanel = (props: { saksId: string; brevId: number }) => {
  const { aktivtDokument, kanTilbakestille, tilbakestillAktivtVedlegg, velgBrev, velgVedlegg } = useVedleggEditor();
  const vedleggQuery = useRedigerbareVedlegg({ saksId: props.saksId, brevId: props.brevId });

  if (vedleggQuery.isPending) {
    return <Loader size="small" title="Henter vedlegg" />;
  }

  if (vedleggQuery.isError) {
    return (
      <Alert size="small" variant="error">
        Klarte ikke å hente vedleggene til brevet.
      </Alert>
    );
  }

  const vedlegg = vedleggQuery.data;

  return (
    <VStack gap="space-12">
      {vedlegg.map((v) => {
        const erAktivt = aktivtDokument.type === "vedlegg" && aktivtDokument.vedleggId === v.vedleggId;
        return (
          <ExpansionCard
            aria-label={v.tittel}
            key={v.vedleggId}
            onToggle={(open) => void (open ? velgVedlegg(v.vedleggId) : velgBrev())}
            open={erAktivt}
            size="small"
          >
            <ExpansionCard.Header>
              <ExpansionCard.Title size="small">{v.tittel}</ExpansionCard.Title>
            </ExpansionCard.Header>
            <ExpansionCard.Content>
              <VStack gap="space-32">
                <BodyShort size="small">
                  Dette vedlegget er redigerbart. Innholdet vises i redigeringsflaten og kan redigeres der.
                </BodyShort>
                {erAktivt && kanTilbakestille && (
                  <HStack>
                    <Button
                      data-color="danger"
                      onClick={tilbakestillAktivtVedlegg}
                      size="small"
                      type="button"
                      variant="secondary"
                    >
                      Tilbakestill vedlegg
                    </Button>
                  </HStack>
                )}
              </VStack>
            </ExpansionCard.Content>
          </ExpansionCard>
        );
      })}
    </VStack>
  );
};
