import { BodyShort, ExpansionCard, Tag, VStack } from "@navikt/ds-react";
import { AxiosError } from "axios";
import { useState } from "react";

import HentOgVisAdresse from "~/components/HentOgVisAdresse";
import Oppsummeringspar from "~/routes/saksnummer_/$saksId/kvittering/-components/Oppsummeringspar";
import { type DelautomatiskBrevResultat } from "~/routes/saksnummer_/$saksId/kvittering/-components/SendtBrevContext";

import { ApiError } from "../ApiError";
import { KvittertBrevHeaderLayout } from "./KvittertBrevHeader";

const KvittertDelautomatiskBrev = (props: { saksId: string; resultat: DelautomatiskBrevResultat }) => {
  const [open, setOpen] = useState(true);
  const { resultat } = props;

  return (
    <ExpansionCard aria-label={resultat.brevtittel} onToggle={setOpen} open={open}>
      <KvittertBrevHeaderLayout
        tag={
          resultat.status === "success" ? (
            <Tag data-color="success" size="small" variant="moderate">
              Sendt til mottaker
            </Tag>
          ) : (
            <Tag data-color="danger" size="small" variant="moderate">
              Kunne ikke sende brev
            </Tag>
          )
        }
        tittel={resultat.brevtittel}
      />
      <ExpansionCard.Content data-testid="kvittert-delautomatisk-brev">
        {resultat.status === "success" ? (
          <VStack align="start" gap="space-16">
            <VStack gap="space-4">
              <BodyShort size="small" weight="semibold">
                Mottaker
              </BodyShort>
              <HentOgVisAdresse
                sakId={props.saksId}
                samhandlerId={resultat.idTSSEkstern ?? undefined}
                visTags={false}
              />
            </VStack>
            {resultat.journalpostId && (
              <Oppsummeringspar size="small" tittel="Journalpost" verdi={resultat.journalpostId} />
            )}
          </VStack>
        ) : resultat.error instanceof AxiosError ? (
          <ApiError error={resultat.error} title="Kunne ikke sende brev" />
        ) : (
          <BodyShort size="small">Skribenten klarte ikke å sende brevet.</BodyShort>
        )}
      </ExpansionCard.Content>
    </ExpansionCard>
  );
};

export default KvittertDelautomatiskBrev;
