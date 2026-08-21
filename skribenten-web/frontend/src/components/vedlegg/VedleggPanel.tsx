import { css } from "@emotion/react";
import { PlusIcon } from "@navikt/aksel-icons";
import { Accordion, Alert, BodyShort, Button, Loader, VStack } from "@navikt/ds-react";

import { useAktivtDokument } from "~/components/vedlegg/AktivtDokumentContext";
import { useRedigerbareVedlegg } from "~/components/vedlegg/useRedigerbareVedlegg";

const aktivtVedleggStyle = css`
  & > .aksel-accordion__header {
    background: var(--ax-bg-accent-soft);
  }
`;

/**
 * The "Vedlegg" side panel: the letter's vedlegg, where opening a redigerbart vedlegg also makes it
 * the document shown in the editor. Only redigerbare vedlegg are listed for now — alltid valgbare
 * vedlegg and P1 are still managed from brevbehandler.
 */
export const VedleggPanel = (props: { saksId: string; brevId: number }) => {
  const { aktivtDokument, velgBrev, velgVedlegg } = useAktivtDokument();
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
      <Button disabled icon={<PlusIcon aria-hidden />} size="small" type="button" variant="secondary">
        Legg til vedlegg
      </Button>

      {vedlegg.length === 0 ? (
        <BodyShort size="small">Dette brevet har ingen vedlegg.</BodyShort>
      ) : (
        <Accordion headingSize="xsmall" indent={false} size="small">
          {vedlegg.map((v) => {
            const erAktivt = aktivtDokument.type === "vedlegg" && aktivtDokument.vedleggId === v.vedleggId;
            return (
              <Accordion.Item
                css={erAktivt ? aktivtVedleggStyle : undefined}
                key={v.vedleggId}
                onOpenChange={(open) => (open ? velgVedlegg(v.vedleggId) : velgBrev())}
                open={erAktivt}
              >
                <Accordion.Header>{v.tittel}</Accordion.Header>
                <Accordion.Content>
                  <BodyShort size="small">Vedlegget vises i redigeringsfeltet, og kan redigeres der.</BodyShort>
                </Accordion.Content>
              </Accordion.Item>
            );
          })}
        </Accordion>
      )}
    </VStack>
  );
};
