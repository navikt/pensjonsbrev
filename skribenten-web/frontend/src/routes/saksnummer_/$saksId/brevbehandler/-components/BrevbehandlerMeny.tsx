import { css } from "@emotion/react";
import { XMarkOctagonFillIcon } from "@navikt/aksel-icons";
import {
  Accordion,
  Alert,
  BodyShort,
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
import { useMemo, useState } from "react";

import { delvisOppdaterBrev, hentAlleBrevForSak } from "~/api/sak-api-endpoints";
import { getNavn } from "~/api/skribenten-api-endpoints";
import EndreMottakerMedOppsummeringOgApiHåndtering from "~/components/EndreMottakerMedApiHåndtering";
import type { BrevStatus, DelvisOppdaterBrevResponse, Mottaker } from "~/types/brev";
import { type BrevInfo, Distribusjonstype } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";
import { erBrevKlar } from "~/utils/brevUtils";
import { formatStringDate, formatStringDateWithTime, isDateToday } from "~/utils/dateUtils";
import { humanizeName } from "~/utils/stringUtils";

import { brevStatusTypeToTextAndTagVariant } from "../-BrevbehandlerUtils";
import { Route } from "../route";

const BrevbehandlerMeny = (properties: { saksId: string; brevInfo: BrevInfo[] }) => {
  return (
    <VStack
      css={css`
        padding: var(--a-spacing-4);
      `}
    >
      <Saksbrev brev={properties.brevInfo} saksId={properties.saksId} />
    </VStack>
  );
};

export default BrevbehandlerMeny;

const Saksbrev = (properties: { saksId: string; brev: BrevInfo[] }) => {
  const { brevId } = Route.useSearch();
  const [åpenBrevItem, setÅpenBrevItem] = useState<Nullable<string>>(brevId ?? null);
  const navigate = useNavigate({ from: Route.fullPath });

  const handleOpenChange = (brevId: string) => (isOpen: boolean) => {
    setÅpenBrevItem(isOpen ? brevId : null);

    if (isOpen) {
      navigate({
        to: "/saksnummer/$saksId/brevbehandler",
        params: { saksId: properties.saksId },
        search: (s) => ({ ...s, brevId: brevId.toString() }),
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
      {properties.brev
        .toSorted((a, b) => a.id - b.id)
        .map((brev) => (
          <BrevItem
            brev={brev}
            key={brev.id}
            onOpenChange={handleOpenChange(brev.id.toString())}
            open={åpenBrevItem === brev.id.toString()}
            saksId={properties.saksId}
          />
        ))}
    </Accordion>
  );
};

const MottakerNavn = (properties: { mottaker: Mottaker }) => {
  switch (properties.mottaker.type) {
    case "Samhandler": {
      return <BodyShort>{properties.mottaker.navn ?? `Fant ikke navn for ${properties.mottaker.tssId}`}</BodyShort>;
    }
    case "NorskAdresse": {
      return <BodyShort>{properties.mottaker.navn}</BodyShort>;
    }
    case "UtenlandskAdresse": {
      return <BodyShort>{properties.mottaker.navn}</BodyShort>;
    }
  }
};

const BrevItem = (properties: {
  saksId: string;
  brev: BrevInfo;
  open: boolean;
  onOpenChange: (isOpen: boolean) => void;
}) => {
  const queryClient = useQueryClient();
  const sakContext = Route.useLoaderData();

  const { data: navn } = useQuery({
    queryKey: getNavn.queryKey(sakContext.sak.foedselsnr as string),
    queryFn: () => getNavn.queryFn(sakContext.sak.saksId),
  });

  const låsForRedigeringMutation = useMutation<DelvisOppdaterBrevResponse, Error, boolean, unknown>({
    mutationFn: (låst) =>
      delvisOppdaterBrev({
        saksId: properties.saksId,
        brevId: properties.brev.id,
        laastForRedigering: låst,
      }),
    onSuccess: (response) => {
      queryClient.setQueryData(hentAlleBrevForSak.queryKey(properties.saksId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brev) => (brev.id === properties.brev.id ? response.info : brev)),
      );
    },
  });

  const distribusjonstypeMutation = useMutation<DelvisOppdaterBrevResponse, Error, Distribusjonstype, unknown>({
    mutationFn: (distribusjonstype) =>
      delvisOppdaterBrev({
        saksId: properties.saksId,
        brevId: properties.brev.id,
        distribusjonstype: distribusjonstype,
      }),
    onSuccess: (response) => {
      queryClient.setQueryData(hentAlleBrevForSak.queryKey(properties.saksId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brev) => (brev.id === properties.brev.id ? response.info : brev)),
      );
    },
  });

  const erLåst = useMemo(() => erBrevKlar(properties.brev), [properties.brev]);

  return (
    <>
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
              <EndreMottakerMedOppsummeringOgApiHåndtering
                brev={properties.brev}
                endreAsIcon
                kanTilbakestilleMottaker={!erLåst}
                overrideOppsummering={(edit) => (
                  <div>
                    <BodyShort
                      css={css`
                        color: var(--a-grayalpha-700);
                      `}
                    >
                      Mottaker
                    </BodyShort>
                    {properties.brev.mottaker ? (
                      <HStack align={"center"} gap="2">
                        <MottakerNavn mottaker={properties.brev.mottaker} /> {edit}
                      </HStack>
                    ) : (
                      <HStack align={"center"} gap="2">
                        <BodyShort>{navn ? humanizeName(navn) : "Bruker"}</BodyShort> {edit}
                      </HStack>
                    )}
                  </div>
                )}
                saksId={properties.saksId}
              />

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
                    params={{ saksId: properties.saksId, brevId: properties.brev.id }}
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
                av {properties.brev.sistredigertAv.navn ?? properties.brev.sistredigertAv.id}
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
