/* eslint-disable react-hooks/rules-of-hooks */
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
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Link, useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { useMemo, useState } from "react";

import type { UserInfo } from "~/api/bff-endpoints";
import { delvisOppdaterBrev, hentAlleBrevForSak } from "~/api/sak-api-endpoints";
import { getNavn } from "~/api/skribenten-api-endpoints";
import EndreMottakerMedOppsummeringOgApiHåndtering from "~/components/EndreMottakerMedApiHåndtering";
import { useUserInfo } from "~/hooks/useUserInfo";
import type { BrevStatus, DelvisOppdaterBrevResponse, Mottaker } from "~/types/brev";
import { type BrevInfo, Distribusjonstype } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";
import { erBrevArkivert, erBrevKlar, erBrevTilAttestering, skalBrevAttesteres } from "~/utils/brevUtils";
import { formatStringDate, formatStringDateWithTime, isDateToday } from "~/utils/dateUtils";
import { humanizeName } from "~/utils/stringUtils";

import { brevStatusTypeToTextAndTagVariant, forkortetSaksbehandlernavn, sortBrevmeny } from "../-BrevbehandlerUtils";
import { Route } from "../route";

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
      navigate({
        to: "/saksnummer/$saksId/brevbehandler",
        params: { saksId: properties.saksId },
        search: (s) => ({ ...s, brevId: brevId }),
        replace: true,
      });
    } else {
      navigate({
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
      {sortBrevmeny(properties.brev).map((brev) => (
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

const MottakerNavn = (properties: { mottaker: Mottaker }) => {
  switch (properties.mottaker.type) {
    case "Samhandler": {
      return (
        <BodyShort size="small">
          {properties.mottaker.navn ?? `Fant ikke navn for ${properties.mottaker.tssId}`}
        </BodyShort>
      );
    }
    case "NorskAdresse": {
      return <BodyShort size="small">{properties.mottaker.navn}</BodyShort>;
    }
    case "UtenlandskAdresse": {
      return <BodyShort size="small">{properties.mottaker.navn}</BodyShort>;
    }
  }
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
          <VStack gap="2">
            <Brevtilstand
              attesteres={erBrevTilAttestering(properties.brev)}
              gjeldendeBruker={gjeldendeBruker}
              status={properties.brev.status}
            />
            <Label size="small">{properties.brev.brevtittel}</Label>
          </VStack>
        </Accordion.Header>
        <Accordion.Content>
          <VStack gap="4">
            {erBrevArkivert(properties.brev) ? (
              <ArkivertBrev brev={properties.brev} />
            ) : (
              <ÅpentBrev brev={properties.brev} saksId={properties.saksId} />
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
            {properties.brev.opprettetAv.id !== gjeldendeBruker?.navident && (
              <Link
                params={{ saksId: properties.saksId, brevId: properties.brev.id.toString() }}
                to="/saksnummer/$saksId/vedtak/$brevId/redigering"
              >
                Attester brev
              </Link>
            )}
          </VStack>
        </Accordion.Content>
      </Accordion.Item>
    </>
  );
};

const ArkivertBrev = (props: { brev: BrevInfo }) => {
  const sakContext = Route.useLoaderData();
  const { data: navn } = useQuery({
    queryKey: getNavn.queryKey(sakContext.sak.foedselsnr as string),
    queryFn: () => getNavn.queryFn(sakContext.sak.saksId),
  });

  return (
    <VStack
      css={css`
        gap: 18px;
      `}
    >
      {/* TODO - copy-pasted fra <ÅpentBrev /> - Ha denne biten som en del av <OppsummeringAvMottaker /> */}
      <div>
        <Detail textColor="subtle">Mottaker</Detail>
        {props.brev.mottaker ? (
          <HStack align={"center"} gap="2">
            <MottakerNavn mottaker={props.brev.mottaker} />
          </HStack>
        ) : (
          <HStack align={"center"} gap="2">
            <BodyShort size="small">{navn ? humanizeName(navn) : "Bruker"}</BodyShort>
          </HStack>
        )}
      </div>

      <BodyShort size="small">
        Brevet er journalført med id {props.brev.journalpostId}. Brevet kan ikke endres.
      </BodyShort>
      <BodyShort size="small">Brevet har ikke blitt sendt. Du kan prøve å sende brevet på nytt.</BodyShort>
    </VStack>
  );
};

const ÅpentBrev = (props: { saksId: string; brev: BrevInfo }) => {
  const queryClient = useQueryClient();
  const sakContext = Route.useLoaderData();

  const { data: navn } = useQuery({
    queryKey: getNavn.queryKey(sakContext.sak.foedselsnr as string),
    queryFn: () => getNavn.queryFn(sakContext.sak.saksId),
  });

  const låsForRedigeringMutation = useMutation<DelvisOppdaterBrevResponse, Error, boolean, unknown>({
    mutationFn: (låst) =>
      delvisOppdaterBrev({
        saksId: props.saksId,
        brevId: props.brev.id,
        laastForRedigering: låst,
      }),
    onSuccess: (response) => {
      queryClient.setQueryData(hentAlleBrevForSak.queryKey(props.saksId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brev) => (brev.id === props.brev.id ? response.info : brev)),
      );
    },
  });

  const distribusjonstypeMutation = useMutation<DelvisOppdaterBrevResponse, Error, Distribusjonstype, unknown>({
    mutationFn: (distribusjonstype) =>
      delvisOppdaterBrev({
        saksId: props.saksId,
        brevId: props.brev.id,
        distribusjonstype: distribusjonstype,
      }),
    onSuccess: (response) => {
      queryClient.setQueryData(hentAlleBrevForSak.queryKey(props.saksId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brev) => (brev.id === props.brev.id ? response.info : brev)),
      );
    },
  });

  const erLåst = useMemo(() => erBrevKlar(props.brev), [props.brev]);

  return (
    <div>
      <div
        css={css`
          display: flex;
          flex-direction: column;
          gap: 18px;
        `}
      >
        <EndreMottakerMedOppsummeringOgApiHåndtering
          brev={props.brev}
          endreAsIcon
          kanTilbakestilleMottaker={!erLåst}
          overrideOppsummering={(edit) => (
            <div>
              <Detail textColor="subtle">Mottaker</Detail>
              {props.brev.mottaker ? (
                <HStack align={"center"} gap="2">
                  <MottakerNavn mottaker={props.brev.mottaker} /> {!erLåst && edit}
                </HStack>
              ) : (
                <HStack align={"center"} gap="2">
                  <BodyShort size="small">{navn ? humanizeName(navn) : "Bruker"}</BodyShort> {!erLåst && edit}
                </HStack>
              )}
            </div>
          )}
          saksId={props.saksId}
        />

        <Switch
          checked={erLåst}
          loading={låsForRedigeringMutation.isPending}
          onChange={(event) => låsForRedigeringMutation.mutate(event.target.checked)}
          size="small"
        >
          {skalBrevAttesteres(props.brev) ? "Brevet er klart for attestering" : "Brevet er klart for sending"}
        </Switch>

        {låsForRedigeringMutation.isError && (
          <Alert size="small" variant="error">
            {typeof (låsForRedigeringMutation.error as AxiosError).response?.data === "string"
              ? ((låsForRedigeringMutation.error as AxiosError).response?.data as string)
              : "Noe gikk galt"}
          </Alert>
        )}

        {!erLåst && (
          <VStack
            css={css`
              align-items: flex-start;
            `}
            gap="4"
          >
            <Button
              as={Link}
              params={{ saksId: props.saksId, brevId: props.brev.id }}
              size="small"
              to="/saksnummer/$saksId/brev/$brevId"
              variant="secondary-neutral"
            >
              Fortsett redigering
            </Button>
          </VStack>
        )}

        {erLåst && (
          <RadioGroup
            data-cy="brevbehandler-distribusjonstype"
            description={
              <div
                css={css`
                  display: flex;
                  gap: 0.5rem;
                `}
              >
                Distribusjon
                <span
                  css={css`
                    display: flex;
                  `}
                >
                  {distribusjonstypeMutation.isPending && <Loader size="small" />}
                  {distribusjonstypeMutation.isError && (
                    <XMarkOctagonFillIcon
                      css={css`
                        align-self: center;
                        color: var(--a-nav-red);
                      `}
                      title="error"
                    />
                  )}
                </span>
              </div>
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

        {props.brev.distribusjonstype === Distribusjonstype.LOKALPRINT && erLåst && <LokalPrintInfoAlerts />}
      </div>
    </div>
  );
};

const Brevtilstand = ({
  status,
  attesteres,
  gjeldendeBruker,
}: {
  status: BrevStatus;
  attesteres: boolean;
  gjeldendeBruker?: UserInfo;
}) => {
  const { variant, text } = brevStatusTypeToTextAndTagVariant(status, attesteres, gjeldendeBruker);

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
    <div
      css={css`
        display: flex;
        flex-direction: column;
        gap: 18px;
      `}
    >
      <Alert size="small" variant="warning">
        Du må åpne PDF og skrive ut brevet etter du har ferdigstilt.
      </Alert>
      <Alert size="small" variant="info">
        Skribenten-brev som skal til samhandler kan sendes via sentralprint.
      </Alert>
    </div>
  );
};
