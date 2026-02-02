import { css } from "@emotion/react";
import { XMarkOctagonFillIcon } from "@navikt/aksel-icons";
import {
  Accordion,
  Alert,
  BodyShort,
  Button,
  Detail,
  HStack,
  Label,
  Loader,
  Radio,
  RadioGroup,
  Switch,
  Tag,
  VStack,
} from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { useMemo, useState } from "react";

import { type UserInfo } from "~/api/bff-endpoints";
import { getBrev } from "~/api/brev-queries";
import { endreDistribusjonstype, hentAlleBrevInfoForSak, veksleKlarStatus } from "~/api/sak-api-endpoints";
import EndreMottakerMedOppsummeringOgApiHåndtering from "~/components/EndreMottakerMedApiHåndtering";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import { useUserInfo } from "~/hooks/useUserInfo";
import type { BrevStatus } from "~/types/brev";
import { type BrevInfo, Distribusjonstype } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";
import { erBrevArkivert, erBrevKlar, erBrevLaastForRedigering, erVedtaksbrev } from "~/utils/brevUtils";
import { formatStringDate, formatStringDateWithTime, isDateToday } from "~/utils/dateUtils";
import { getErrorMessage } from "~/utils/errorUtils";

import { brevStatusTypeToTextAndTagVariant, forkortetSaksbehandlernavn, sortBrev } from "../-BrevbehandlerUtils";
import { Route } from "../route";
import { Vedlegg } from "./Vedlegg";

const BrevbehandlerMeny = (properties: { saksId: string; brevInfo: BrevInfo[] }) => {
  return (
    <VStack>
      <Saksbrev brev={properties.brevInfo} saksId={properties.saksId} />
    </VStack>
  );
};

export default BrevbehandlerMeny;

const Saksbrev = (properties: { saksId: string; brev: BrevInfo[] }) => {
  const { brevId } = Route.useSearch();
  const [åpenBrevItem, setÅpenBrevItem] = useState<Nullable<number>>(brevId ?? null);
  const navigate = useNavigate({ from: Route.fullPath });

  const handleOpenChange = (brevId: number) => (isOpen: boolean) => {
    setÅpenBrevItem(isOpen ? brevId : null);

    if (isOpen) {
      return navigate({
        to: "/saksnummer/$saksId/brevbehandler",
        params: { saksId: properties.saksId },
        search: (s) => ({ ...s, brevId: brevId }),
        replace: true,
      });
    } else {
      return navigate({
        to: "/saksnummer/$saksId/brevbehandler",
        params: { saksId: properties.saksId },
        search: (s) => ({ ...s, brevId: undefined }),
        replace: true,
      });
    }
  };

  if (properties.brev.length === 0) {
    return <Alert variant="info">Fant ingen brev som er under behandling</Alert>;
  }

  return (
    <Accordion>
      {sortBrev(properties.brev).map((brev) => (
        <BrevItem
          brev={brev}
          key={brev.id}
          onOpenChange={handleOpenChange(brev.id)}
          open={åpenBrevItem === brev.id}
          saksId={properties.saksId}
        />
      ))}
    </Accordion>
  );
};

const BrevItem = (properties: {
  saksId: string;
  brev: BrevInfo;
  open: boolean;
  onOpenChange: (isOpen: boolean) => void;
}) => {
  const gjeldendeBruker = useUserInfo();

  return (
    <>
      <Accordion.Item onOpenChange={() => properties.onOpenChange(!properties.open)} open={properties.open}>
        <Accordion.Header>
          <VStack gap="space-8">
            <Brevtilstand gjeldendeBruker={gjeldendeBruker} status={properties.brev.status} />
            <Label size="small">{properties.brev.brevtittel}</Label>
          </VStack>
        </Accordion.Header>
        <Accordion.Content>
          <VStack gap="space-16">
            {erBrevArkivert(properties.brev) ? (
              <ArkivertBrev brev={properties.brev} />
            ) : (
              <ActiveBrev brev={properties.brev} saksId={properties.saksId} />
            )}
            <div>
              <Detail textColor="subtle">
                Sist endret:{" "}
                {isDateToday(properties.brev.sistredigert)
                  ? formatStringDateWithTime(properties.brev.sistredigert)
                  : formatStringDate(properties.brev.sistredigert)}{" "}
                av {forkortetSaksbehandlernavn(properties.brev.sistredigertAv, gjeldendeBruker)}
              </Detail>
              <Detail textColor="subtle">Brev opprettet: {formatStringDate(properties.brev.opprettet)}</Detail>
            </div>
          </VStack>
        </Accordion.Content>
      </Accordion.Item>
    </>
  );
};

const ArkivertBrev = (props: { brev: BrevInfo }) => {
  const sakContext = Route.useLoaderData();

  return (
    <VStack gap="space-16">
      {/* TODO - copy-pasted fra <ÅpentBrev /> - Ha denne biten som en del av <OppsummeringAvMottaker /> */}
      <div>
        <Detail textColor="subtle">Mottaker</Detail>
        <OppsummeringAvMottaker
          mottaker={props.brev.mottaker ?? null}
          saksId={sakContext.sak.saksId.toString()}
          withTitle={false}
        />
      </div>

      <BodyShort size="small">
        Brevet er journalført med id {props.brev.journalpostId}. Brevet kan ikke endres.
      </BodyShort>
      <BodyShort size="small">Brevet har ikke blitt sendt. Du kan prøve å sende brevet på nytt.</BodyShort>
    </VStack>
  );
};

const ActiveBrev = (props: { saksId: string; brev: BrevInfo }) => {
  const queryClient = useQueryClient();
  const navigate = Route.useNavigate();
  const { enhetsId, vedtaksId } = Route.useSearch();

  const laasForRedigeringMutation = useMutation<BrevInfo, Error, boolean, unknown>({
    mutationFn: (klar) => veksleKlarStatus(props.saksId, props.brev.id, { klar: klar }),
    onSuccess: (response) => {
      queryClient.setQueryData(hentAlleBrevInfoForSak.queryKey(props.saksId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brev) => (brev.id === response.id ? response : brev)),
      );
      queryClient.invalidateQueries({ queryKey: getBrev.queryKey(props.brev.id) });
    },
  });

  const distribusjonstypeMutation = useMutation<BrevInfo, Error, Distribusjonstype, unknown>({
    mutationFn: (distribusjonstype) =>
      endreDistribusjonstype(props.saksId, props.brev.id, { distribusjon: distribusjonstype }),
    onSuccess: (response) => {
      queryClient.setQueryData(hentAlleBrevInfoForSak.queryKey(props.saksId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brevInfo) => (brevInfo.id === response.id ? response : brevInfo)),
      );
    },
  });

  const erLaast = useMemo(() => erBrevLaastForRedigering(props.brev), [props.brev]);

  return (
    <VStack gap="space-16">
      <EndreMottakerMedOppsummeringOgApiHåndtering
        brev={props.brev}
        endreAsIcon
        kanTilbakestilleMottaker={!erLaast}
        overrideOppsummering={(edit) => (
          <VStack flexGrow="1">
            <HStack justify="space-between" wrap={false}>
              <BodyShort size="small" weight="semibold">
                Mottaker
              </BodyShort>
              {!erLaast && edit}
            </HStack>
            <OppsummeringAvMottaker mottaker={props.brev.mottaker ?? null} saksId={props.saksId} withTitle={false} />
          </VStack>
        )}
        saksId={props.saksId}
      />
      <Vedlegg brev={props.brev} erLaast={erLaast} saksId={props.saksId} />
      <Switch
        checked={erLaast}
        loading={laasForRedigeringMutation.isPending}
        onChange={(event) => laasForRedigeringMutation.mutate(event.target.checked)}
        size="small"
      >
        {erVedtaksbrev(props.brev) && !erBrevKlar(props.brev)
          ? "Brevet er klart for attestering"
          : "Brevet er klart for sending"}
      </Switch>
      {laasForRedigeringMutation.isError && (
        <Alert size="small" variant="error">
          {getErrorMessage(laasForRedigeringMutation.error)}
        </Alert>
      )}
      {!erLaast && (
        <VStack align="start" gap="space-16">
          <Button
            data-color="neutral"
            onClick={() =>
              navigate({
                to: "/saksnummer/$saksId/brev/$brevId",
                params: { brevId: props.brev.id, saksId: props.saksId },
                search: { enhetsId, vedtaksId },
              })
            }
            size="small"
            variant="secondary"
          >
            Fortsett redigering
          </Button>
        </VStack>
      )}
      {erLaast && (
        <RadioGroup
          data-cy="brevbehandler-distribusjonstype"
          description={
            <HStack align="center">
              Distribusjon
              {distribusjonstypeMutation.isPending && <Loader size="small" />}
              {distribusjonstypeMutation.isError && (
                <XMarkOctagonFillIcon color="var(--ax-text-danger-decoration)" title="error" />
              )}
            </HStack>
          }
          legend=""
          onChange={(v) => distribusjonstypeMutation.mutate(v)}
          size="small"
          value={props.brev.distribusjonstype}
        >
          <Radio value={Distribusjonstype.SENTRALPRINT}>Sentralprint</Radio>
          <Radio value={Distribusjonstype.LOKALPRINT}>Lokalprint</Radio>
        </RadioGroup>
      )}
      {props.brev.distribusjonstype === Distribusjonstype.LOKALPRINT && erLaast && <LokalPrintInfoAlerts />}
    </VStack>
  );
};

const Brevtilstand = ({ status, gjeldendeBruker }: { status: BrevStatus; gjeldendeBruker?: UserInfo }) => {
  const { variant, text } = brevStatusTypeToTextAndTagVariant(status, gjeldendeBruker);

  return (
    <Tag
      css={css`
        align-self: flex-start;
      `}
      size="xsmall"
      variant={variant}
    >
      {text}
    </Tag>
  );
};

const LokalPrintInfoAlerts = () => {
  return (
    <VStack gap="space-20">
      <Alert size="small" variant="warning">
        Du må åpne PDF og skrive ut brevet etter du har ferdigstilt.
      </Alert>
      <Alert size="small" variant="info">
        Skribenten-brev som skal til samhandler kan sendes via sentralprint.
      </Alert>
    </VStack>
  );
};
