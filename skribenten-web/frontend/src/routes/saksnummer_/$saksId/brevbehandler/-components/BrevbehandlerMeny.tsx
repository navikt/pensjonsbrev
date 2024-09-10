import { css } from "@emotion/react";
import { XMarkOctagonFillIcon } from "@navikt/aksel-icons";
import { Accordion, Alert, BodyShort, Label, Loader, Radio, RadioGroup, Switch, Tag, VStack } from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { Link, useNavigate } from "@tanstack/react-router";
import { useMemo, useState } from "react";

import { delvisOppdaterBrev, hentAlleBrevForSak } from "~/api/sak-api-endpoints";
import type { BrevStatus, DelvisOppdaterBrevResponse } from "~/types/brev";
import { type BrevInfo, Distribusjonstype } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";
import { erBrevKlar } from "~/utils/brevUtils";
import { formatStringDate, formatStringDateWithTime, isDateToday } from "~/utils/dateUtils";

import { EndreMottakerModal } from "../../brevvelger/$templateId/-components/endreMottaker/EndreMottaker";
import { brevStatusTypeToTextAndTagVariant } from "../-BrevbehandlerUtils";
import { Route } from "../route";

const BrevbehandlerMeny = (properties: { sakId: string; brevInfo: BrevInfo[] }) => {
  return (
    <VStack
      css={css`
        padding: var(--a-spacing-4);
      `}
    >
      <Saksbrev brev={properties.brevInfo} sakId={properties.sakId} />
    </VStack>
  );
};

export default BrevbehandlerMeny;

const Saksbrev = (properties: { sakId: string; brev: BrevInfo[] }) => {
  const { brevId } = Route.useSearch();
  const [åpenBrevItem, setÅpenBrevItem] = useState<Nullable<string>>(brevId ?? null);
  const navigate = useNavigate({ from: Route.fullPath });

  const handleOpenChange = (brevId: string) => (isOpen: boolean) => {
    setÅpenBrevItem(isOpen ? brevId : null);

    if (isOpen) {
      navigate({
        to: "/saksnummer/$saksId/brevbehandler",
        params: { saksId: properties.sakId },
        search: (s) => ({ ...s, brevId: brevId.toString() }),
        replace: true,
      });
    } else {
      navigate({
        to: "/saksnummer/$saksId/brevbehandler",
        params: { saksId: properties.sakId },
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
      {properties.brev
        .toSorted((a, b) => a.id - b.id)
        .map((brev) => (
          <BrevItem
            brev={brev}
            key={brev.id}
            onOpenChange={handleOpenChange(brev.id.toString())}
            open={åpenBrevItem === brev.id.toString()}
            sakId={properties.sakId}
          />
        ))}
    </Accordion>
  );
};

const BrevItem = (properties: {
  sakId: string;
  brev: BrevInfo;
  open: boolean;
  onOpenChange: (isOpen: boolean) => void;
}) => {
  const [modalÅpen, setModalÅpen] = useState<boolean>(false);
  const queryClient = useQueryClient();

  const låsForRedigeringMutation = useMutation<DelvisOppdaterBrevResponse, Error, boolean, unknown>({
    mutationFn: (låst) =>
      delvisOppdaterBrev({
        sakId: properties.sakId,
        brevId: properties.brev.id,
        laastForRedigering: låst,
      }),
    onSuccess: (response) => {
      queryClient.setQueryData(hentAlleBrevForSak.queryKey(properties.sakId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brev) => (brev.id === properties.brev.id ? response.info : brev)),
      );
    },
  });

  const distribusjonstypeMutation = useMutation<DelvisOppdaterBrevResponse, Error, Distribusjonstype, unknown>({
    mutationFn: (distribusjonstype) =>
      delvisOppdaterBrev({
        sakId: properties.sakId,
        brevId: properties.brev.id,
        distribusjonstype: distribusjonstype,
      }),
    onSuccess: (response) => {
      queryClient.setQueryData(hentAlleBrevForSak.queryKey(properties.sakId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brev) => (brev.id === properties.brev.id ? response.info : brev)),
      );
    },
  });

  const erLåst = useMemo(() => erBrevKlar(properties.brev), [properties.brev]);

  return (
    <>
      {modalÅpen && (
        <EndreMottakerModal
          onBekreftNyMottaker={() => {
            setModalÅpen(false);
            //TODO - bekreft ny mottaker
          }}
          onClose={() => setModalÅpen(false)}
          åpen={modalÅpen}
        />
      )}
      <Accordion.Item onOpenChange={() => properties.onOpenChange(!properties.open)} open={properties.open}>
        <Accordion.Header>
          <VStack gap="2">
            <Brevtilstand status={properties.brev.status} />
            <Label size="small">{properties.brev.brevtittel}</Label>
          </VStack>
        </Accordion.Header>
        <Accordion.Content>
          <VStack gap="8">
            <VStack gap="4">
              {/* 
              TODO - Implementer endring av mottaker
              1. Vise hvem mottakeren er. Brevet starter default til brukeren, så kan dem endre til sahmandler / manuell adresse
              2. Gjør et kall til backend for å endre mottakeren
              <div>
                <BodyShort
                  css={css`
                    color: var(--a-grayalpha-700);
                  `}
                >
                  Mottaker
                </BodyShort>
                <HStack gap="2">
                  <BodyShort>En mottaker</BodyShort>
                  {!erLåst && (
                    <Button
                      css={css`
                        padding: 0;
                      `}
                      onClick={() => setModalÅpen(true)}
                      size="small"
                      type="button"
                      variant="tertiary"
                    >
                      <PencilIcon fontSize="24px" />
                    </Button>
                  )}
                </HStack>
              </div>
              */}

              <Switch
                checked={erLåst}
                // TODO - finn en måte å gi feedback på dersom kallet gir error. Jeg antar at switcehn ikke blir endret dersom det er en error
                loading={låsForRedigeringMutation.isPending}
                onChange={(event) => låsForRedigeringMutation.mutate(event.target.checked)}
              >
                Lås for redigering
              </Switch>

              {!erLåst && (
                <VStack
                  css={css`
                    align-items: flex-start;
                  `}
                  gap="4"
                >
                  {/*
                  TODO - Implementer oppdatering av data
                  <Button
                    css={css`
                      color: #23262a;
                      border-color: #23262a;
                      box-shadow: inset 0 0 0 2px #23262a;
                    `}
                    onClick={() => {
                      //TODO: Implementer oppdatering av data
                      console.log("Oppdaterer data");
                    }}
                    size="small"
                    type="button"
                    variant="secondary"
                  >
                    Oppdater data
                  </Button>
                  */}
                  <Link
                    className="navds-button navds-button--small navds-label navds-label--small"
                    css={css`
                      color: #23262a;
                      border-color: #23262a;
                      box-shadow: inset 0 0 0 2px #23262a;
                    `}
                    params={{ saksId: properties.sakId, brevId: properties.brev.id }}
                    to="/saksnummer/$saksId/brev/$brevId"
                  >
                    Fortsett redigering
                  </Link>
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
                  value={properties.brev.distribusjonstype}
                >
                  <Radio value={Distribusjonstype.SENTRALPRINT}>Sentralprint</Radio>
                  <Radio value={Distribusjonstype.LOKALPRINT}>Lokalprint</Radio>
                </RadioGroup>
              )}
            </VStack>

            <div>
              <BodyShort
                css={css`
                  color: var(--a-grayalpha-700);
                `}
              >
                Sist endret:{" "}
                {isDateToday(properties.brev.sistredigert)
                  ? formatStringDateWithTime(properties.brev.sistredigert)
                  : formatStringDate(properties.brev.sistredigert)}{" "}
                av {properties.brev.sistredigertAv}
              </BodyShort>
              <BodyShort
                css={css`
                  color: var(--a-grayalpha-700);
                `}
              >
                Brev opprettet: {formatStringDate(properties.brev.opprettet)}
              </BodyShort>
            </div>
          </VStack>
        </Accordion.Content>
      </Accordion.Item>
    </>
  );
};

const Brevtilstand = (properties: { status: BrevStatus }) => {
  const { variant, text } = brevStatusTypeToTextAndTagVariant(properties.status);

  return (
    <Tag
      css={css`
        align-self: flex-start;
      `}
      size="small"
      variant={variant}
    >
      <BodyShort>{text}</BodyShort>
    </Tag>
  );
};
