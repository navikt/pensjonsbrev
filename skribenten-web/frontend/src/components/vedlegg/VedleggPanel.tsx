import { Alert, BodyShort, Button, ExpansionCard, HStack, Loader, VStack } from "@navikt/ds-react";
import { type UseQueryResult } from "@tanstack/react-query";

import { useActiveDocument } from "~/components/brevOgVedlegg/ActiveDocumentContext";
import { type RedigerbartVedleggInfo } from "~/types/brev";

/**
 * Lists editable attachments and switches the editor to the selected one.
 */
export const VedleggPanel = (props: { vedleggQuery: UseQueryResult<RedigerbartVedleggInfo[], Error> }) => {
  const { activeDocument, canReset, resetActiveVedlegg, selectBrev, selectVedlegg } = useActiveDocument();
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
    <VStack gap="space-12" paddingBlock="space-16 space-0">
      {vedlegg.map((v) => {
        const isActive = activeDocument.type === "vedlegg" && activeDocument.vedleggId === v.vedleggId;
        return (
          <ExpansionCard
            aria-label={v.tittel}
            key={v.vedleggId}
            onToggle={(open) => void (open ? selectVedlegg(v.vedleggId) : selectBrev())}
            open={isActive}
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
                {isActive && canReset && (
                  <HStack>
                    <Button
                      data-color="danger"
                      onClick={resetActiveVedlegg}
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
