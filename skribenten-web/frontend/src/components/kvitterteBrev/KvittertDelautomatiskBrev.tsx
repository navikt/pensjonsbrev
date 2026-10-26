import { BodyShort, ExpansionCard, Tag, VStack } from "@navikt/ds-react";
import { AxiosError } from "axios";
import { useState } from "react";

import { useSakGjelderNavnFormatert } from "~/hooks/useSakGjelderNavn";
import { useSamhandlerMottaker } from "~/hooks/useSamhandlerMottaker";
import Oppsummeringspar from "~/routes/saksnummer_/$saksId/kvittering/-components/Oppsummeringspar";
import { type DelautomatiskBrevResult } from "~/routes/saksnummer_/$saksId/kvittering/-components/SendtBrevContext";

import { ApiError } from "../ApiError";
import { KvittertBrevHeaderLayout } from "./KvittertBrevHeader";

const KvittertDelautomatiskBrev = (props: { saksId: string; result: DelautomatiskBrevResult }) => {
  const [open, setOpen] = useState(true);
  const { result } = props;
  const sakGjelderNavn = useSakGjelderNavnFormatert({ saksId: props.saksId });
  const samhandlerMottaker = useSamhandlerMottaker(result.idTSSEkstern ?? undefined);
  const recipientName = result.idTSSEkstern ? samhandlerMottaker.data?.samhandler.navn : sakGjelderNavn;

  return (
    <ExpansionCard aria-label={result.brevtittel} onToggle={setOpen} open={open}>
      <KvittertBrevHeaderLayout
        tag={
          result.status === "success" ? (
            <Tag data-color="success" size="small" variant="moderate">
              Sendt til mottaker
            </Tag>
          ) : (
            <Tag data-color="danger" size="small" variant="moderate">
              Kunne ikke sende brev
            </Tag>
          )
        }
        title={result.brevtittel}
      />
      <ExpansionCard.Content data-testid="kvittert-delautomatisk-brev">
        {result.status === "success" ? (
          <VStack align="start" gap="space-16">
            <Oppsummeringspar size="small" tittel="Mottaker" verdi={recipientName ?? "Fant ikke mottakerens navn"} />
            {result.journalpostId && (
              <Oppsummeringspar size="small" tittel="Journalpost" verdi={result.journalpostId} />
            )}
          </VStack>
        ) : result.error instanceof AxiosError ? (
          <ApiError error={result.error} title="Kunne ikke sende brev" />
        ) : (
          <BodyShort size="small">Skribenten klarte ikke å sende brevet.</BodyShort>
        )}
      </ExpansionCard.Content>
    </ExpansionCard>
  );
};

export default KvittertDelautomatiskBrev;
