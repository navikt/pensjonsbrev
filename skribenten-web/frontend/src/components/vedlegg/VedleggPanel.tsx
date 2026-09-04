import { Alert, BodyShort, Button, ExpansionCard, HStack, Loader, VStack } from "@navikt/ds-react";
import { type UseQueryResult } from "@tanstack/react-query";

import { useAktivtDokument } from "~/components/brevOgVedlegg/AktivtDokumentContext";
import { type RedigerbartVedleggInfo } from "~/types/brev";

/**
 * Lists editable vedlegg and switches the editor to the selected one.
 */
export const VedleggPanel = (props: { vedleggQuery: UseQueryResult<RedigerbartVedleggInfo[], Error> }) => {
  const { aktivtDokument, kanTilbakestille, tilbakestillAktivtVedlegg, velgBrev, velgVedlegg } = useAktivtDokument();
  const { vedleggQuery } = props;

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
