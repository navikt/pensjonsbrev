import { css } from "@emotion/react";
import { XMarkOctagonFillIcon } from "@navikt/aksel-icons";
import {
  Alert,
  BodyShort,
  Button,
  Detail,
  ExpansionCard,
  HStack,
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
import { type BrevInfo, type BrevStatus, Distribusjonstype } from "~/types/brev";
import { type Nullable } from "~/types/Nullable";
import { erBrevArkivert, erBrevKlar, erBrevLaastForRedigering, erVedtaksbrev } from "~/utils/brevUtils";
import { formatStringDate, formatStringDateWithTime, isDateToday } from "~/utils/dateUtils";
import { getErrorMessage } from "~/utils/errorUtils";
import { trackEvent } from "~/utils/umami";

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
    <VStack gap="space-8">
      {sortBrev(properties.brev).map((brev) => (
        <BrevItem
          brev={brev}
          key={brev.id}
          onOpenChange={handleOpenChange(brev.id)}
          open={åpenBrevItem === brev.id}
          saksId={properties.saksId}
        />
      ))}
    </VStack>
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
    <ExpansionCard
      aria-label={properties.brev.brevtittel}
      onToggle={() => properties.onOpenChange(!properties.open)}
      open={properties.open}
      size="small"
    >
      <ExpansionCard.Header
        css={css`
          gap: var(--ax-space-6);
        `}
      >
        <Brevtilstand gjeldendeBruker={gjeldendeBruker} status={properties.brev.status} />
        <ExpansionCard.Title
          as="h4"
          css={css`
            font-size: var(--ax-font-size-heading-xsmall);
          `}
          size="small"
        >
          <VStack align="start" gap="space-8" justify="start">
            {properties.brev.brevtittel}
          </VStack>
        </ExpansionCard.Title>
      </ExpansionCard.Header>
      <ExpansionCard.Content>
        <VStack>
          {erBrevArkivert(properties.brev) ? (
            <ArkivertBrev brev={properties.brev} />
          ) : (
            <ActiveBrev brev={properties.brev} saksId={properties.saksId} />
          )}
          <div
            css={css`
              margin-top: var(--ax-space-20);
            `}
          >
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
      </ExpansionCard.Content>
    </ExpansionCard>
  );
};

const ArkivertBrev = (props: { brev: BrevInfo }) => {
  const sakContext = Route.useLoaderData();

  return (
    <VStack>
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
    onSuccess: (response, isKlar) => {
      const brevType = erVedtaksbrev(response) ? "vedtaksbrev" : "informasjonsbrev";
      const klarStatus = isKlar
        ? erVedtaksbrev(response)
          ? "klart for attestering"
          : "klart for sending"
        : "ikke klar";
      trackEvent("brev klar status endret", {
        brevId: response.id,
        brevType,
        klarStatus,
        erKlar: isKlar,
      });

      queryClient.setQueryData(hentAlleBrevInfoForSak.queryKey(props.saksId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brev) => (brev.id === response.id ? response : brev)),
      );
      queryClient.invalidateQueries({
        queryKey: getBrev.queryKey(props.brev.id),
      });
    },
  });

  const distribusjonstypeMutation = useMutation<BrevInfo, Error, Distribusjonstype, unknown>({
    mutationFn: (distribusjonstype) =>
      endreDistribusjonstype(props.saksId, props.brev.id, {
        distribusjon: distribusjonstype,
      }),
    onSuccess: (response, distribusjonstype) => {
      trackEvent("brev distribusjonstype endret", {
        brevId: response.id,
        distribusjonstype: distribusjonstype === Distribusjonstype.SENTRALPRINT ? "sentralprint" : "lokalprint",
      });
      queryClient.setQueryData(hentAlleBrevInfoForSak.queryKey(props.saksId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brevInfo) => (brevInfo.id === response.id ? response : brevInfo)),
      );
    },
  });

  const erLaast = useMemo(() => erBrevLaastForRedigering(props.brev), [props.brev]);

  return (
    <VStack gap="space-20">
      <EndreMottakerMedOppsummeringOgApiHåndtering
        brev={props.brev}
        endreAsIcon
        kanTilbakestilleMottaker={!erLaast}
        overrideOppsummering={(edit) => (
          <VStack flexGrow="1" gap="space-8">
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
        onChange={(event) => {
          laasForRedigeringMutation.mutate(event.target.checked);
        }}
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
        <VStack align="start">
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
            <HStack align="center" gap="space-20">
              <BodyShort color="text-neutral" size="small" weight="semibold">
                Distribusjon
              </BodyShort>
              {distribusjonstypeMutation.isPending && <Loader size="small" />}
              {distribusjonstypeMutation.isError && (
                <XMarkOctagonFillIcon color="var(--ax-text-danger-decoration)" title="error" />
              )}
            </HStack>
          }
          legend=""
          onChange={(v) => {
            distribusjonstypeMutation.mutate(v);
          }}
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
  const { color, text } = brevStatusTypeToTextAndTagVariant(status, gjeldendeBruker);

  return (
    <Tag data-color={color} size="small" variant="moderate">
      {text}
    </Tag>
  );
};

const LokalPrintInfoAlerts = () => {
  return (
    <VStack gap="space-20">
      <Alert size="small" variant="warning">
        Husk å åpne PDF og skriv ut brevet.
      </Alert>
    </VStack>
  );
};
