import { css } from "@emotion/react";
import { Accordion, BodyShort, Box, Button, Heading, Label, Tag, VStack } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";
import { createFileRoute, Link } from "@tanstack/react-router";

import { hentPdfForJournalpostQuery } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";

import { distribusjonstypeTilText } from "../../kvittering/-components/KvitteringUtils";
import { KvittertBrevContentError } from "../../kvittering/-components/KvitterteBrev";
import Oppsummeringspar from "../../kvittering/-components/Oppsummeringspar";
import type { SendVedtakSuccessResponse } from "./-SendVedtakContext";
import { useSendVedtakContext } from "./-SendVedtakContext";

export const Route = createFileRoute("/saksnummer/$saksId/vedtak/$vedtakId/kvittering")({
  component: () => <Kvittering />,
});

const Kvittering = () => {
  const { response } = useSendVedtakContext();
  const { saksId, vedtakId } = Route.useParams();

  if (response.status === "initial") {
    return (
      <Box
        background="bg-default"
        css={css`
          display: flex;
          flex-direction: column;
          flex: 1;
          align-items: center;
          padding-top: var(--a-spacing-8);
        `}
      >
        <BodyShort>Ingen informasjon om brevsending</BodyShort>
        <Link params={{ saksId, vedtakId }} to={"/saksnummer/$saksId/vedtak/$vedtakId/forhandsvisning"}>
          Tilbake til forhåndsvisning
        </Link>
      </Box>
    );
  }

  return (
    <Box
      background="bg-default"
      css={css`
        display: flex;
        flex: 1;
        align-self: center;
        gap: 5rem;
        justify-content: center;
        padding: var(--a-spacing-8) var(--a-spacing-24);
      `}
    >
      <VStack>
        <Heading size="medium">Kvittering</Heading>
        <Accordion>
          <Accordion.Item>
            <Accordion.Header>
              <VStack align="start">
                {response.status === "success" && (
                  <Tag size="small" variant="info">
                    Lokalprint - sendt til joark
                  </Tag>
                )}
                <Label size="small">{response.vedtak.redigertBrev.title}</Label>
              </VStack>
            </Accordion.Header>
            <Accordion.Content>
              <VStack align={"start"} gap="4">
                {response.status === "success" && <SuccessDisplayer response={response} saksId={saksId} />}
              </VStack>
            </Accordion.Content>
          </Accordion.Item>
        </Accordion>
      </VStack>
      <VStack gap="2">
        <Heading size="medium">Hva vil du gjøre nå?</Heading>
        <VStack align={"start"} gap="3">
          <Button size="small" type="button" variant="secondary">
            Gå til brukeroversikt
          </Button>
          <Button size="small" type="button" variant="secondary">
            Gå til dokumentoversikt
          </Button>
        </VStack>
      </VStack>
    </Box>
  );
};

const SuccessDisplayer = (props: { saksId: string; response: SendVedtakSuccessResponse }) => {
  const pdfForJournalpost = useMutation<Blob, Error, number>({
    /* den er brukt i contexten av at journalpostId'en er sjekket til å være not null */
    mutationFn: (journalpostId) => hentPdfForJournalpostQuery.queryFn(props.saksId, journalpostId),
    onSuccess: (pdf) => window.open(URL.createObjectURL(pdf), "_blank"),
  });

  if (props.response.response.error != null) {
    return (
      <KvittertBrevContentError
        error={props.response.response.error}
        isPending={false}
        onPrøvIgjenClick={() => {}}
        sakId={props.saksId}
      >
        <BodyShort>
          Brevet ble ikke sendt pga {props.response.response.error?.tekniskgrunn ?? "en ukjent teknisk grunn"}. Prøv
          igjen.
        </BodyShort>
        <BodyShort>{props.response.response.error?.beskrivelse}</BodyShort>
      </KvittertBrevContentError>
    );
  } else if (props.response.response.journalpostId == null) {
    return (
      <KvittertBrevContentError
        error={props.response.response.error}
        isPending={false}
        onPrøvIgjenClick={() => {}}
        sakId={props.saksId}
      >
        <BodyShort>Brevet ble ikke sendt pga en ukjent feil. Prøv igjen.</BodyShort>
      </KvittertBrevContentError>
    );
  } else {
    return (
      <VStack align={"start"} gap="4">
        {/* TODO <Oppsummeringspar tittel={"Mottaker"} verdi={""} /> */}

        <Oppsummeringspar
          tittel={"Distribueres via"}
          verdi={distribusjonstypeTilText(props.response.vedtak.info.distribusjonstype)}
        />

        {/* den er brukt i contexten av at journalpostId'en er sjekket til å være not null */}
        <Oppsummeringspar tittel={"Journalpost ID"} verdi={props.response.response.journalpostId} />

        <Button
          loading={pdfForJournalpost.isPending}
          // den er brukt i contexten av at journalpostId'en er sjekket til å være not null
          onClick={() => pdfForJournalpost.mutate(props.response.response.journalpostId!)}
          size="small"
          type="button"
        >
          Åpne PDF i ny fane
        </Button>

        {pdfForJournalpost.isError && <ApiError error={pdfForJournalpost.error} title={"Klarte ikke å hente PDF"} />}
      </VStack>
    );
  }
};
