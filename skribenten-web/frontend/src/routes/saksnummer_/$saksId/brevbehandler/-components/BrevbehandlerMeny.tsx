import { css } from "@emotion/react";
import { PencilIcon, PlusCircleIcon, XMarkOctagonFillIcon } from "@navikt/aksel-icons";
import {
  Accordion,
  Alert,
  BodyShort,
  BoxNew,
  Button,
  Checkbox,
  CheckboxGroup,
  Detail,
  HStack,
  Label,
  Loader,
  Modal,
  Radio,
  RadioGroup,
  Switch,
  Tag,
  VStack,
} from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { useMemo, useState } from "react";
import { Controller, useForm } from "react-hook-form";

import { type UserInfo } from "~/api/bff-endpoints";
import { getBrev } from "~/api/brev-queries";
import { delvisOppdaterBrev, getBrevVedlegg, hentAlleBrevForSak } from "~/api/sak-api-endpoints";
import EndreMottakerMedOppsummeringOgApiHåndtering from "~/components/EndreMottakerMedApiHåndtering";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import { P1EditModal } from "~/components/P1/P1EditModal";
import { useUserInfo } from "~/hooks/useUserInfo";
import type { BrevStatus, DelvisOppdaterBrevResponse } from "~/types/brev";
import { type BrevInfo, Distribusjonstype, VEDLEGG_KODE_TO_TEXT, VedleggKode } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";
import { erBrevArkivert, erBrevKlar, erBrevLaastForRedigering, erVedtaksbrev } from "~/utils/brevUtils";
import { formatStringDate, formatStringDateWithTime, isDateToday } from "~/utils/dateUtils";
import { getErrorMessage } from "~/utils/errorUtils";

import { brevStatusTypeToTextAndTagVariant, forkortetSaksbehandlernavn, sortBrev } from "../-BrevbehandlerUtils";
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

  const laasForRedigeringMutation = useMutation<DelvisOppdaterBrevResponse, Error, boolean, unknown>({
    mutationFn: (laast) => delvisOppdaterBrev(props.saksId, props.brev.id, { laastForRedigering: laast }),
    onSuccess: (response) => {
      queryClient.setQueryData(hentAlleBrevForSak.queryKey(props.saksId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brev) => (brev.id === props.brev.id ? response.info : brev)),
      );
      queryClient.invalidateQueries({ queryKey: getBrev.queryKey(props.brev.id) });
    },
  });

  const distribusjonstypeMutation = useMutation<DelvisOppdaterBrevResponse, Error, Distribusjonstype, unknown>({
    mutationFn: (distribusjonstype) =>
      delvisOppdaterBrev(props.saksId, props.brev.id, { distribusjonstype: distribusjonstype }),
    onSuccess: (response) => {
      queryClient.setQueryData(hentAlleBrevForSak.queryKey(props.saksId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brev) => (brev.id === props.brev.id ? response.info : brev)),
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
            onClick={() =>
              navigate({
                to: "/saksnummer/$saksId/brev/$brevId",
                params: { brevId: props.brev.id, saksId: props.saksId },
                search: { enhetsId, vedtaksId },
              })
            }
            size="small"
            variant="secondary-neutral"
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

type VedleggFormData = {
  valgteVedlegg: VedleggKode[];
};

const getVedleggLabel = (kode: string): string => {
  if (kode in VedleggKode) {
    return VEDLEGG_KODE_TO_TEXT[kode as VedleggKode];
  }
  return kode;
};

const Vedlegg = (props: { saksId: string; brev: BrevInfo; erLaast: boolean }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isP1ModalOpen, setIsP1ModalOpen] = useState(false);
  const [savedVedlegg, setSavedVedlegg] = useState<VedleggKode[]>([]);

  const isP1Brev = props.brev.brevkode === "P1_SAMLET_MELDING_OM_PENSJONSVEDTAK_V2";

  const form = useForm<VedleggFormData>({
    defaultValues: { valgteVedlegg: [] },
  });

  const {
    data: vedleggKoder,
    isLoading,
    isError,
    error,
  } = useQuery({
    queryKey: getBrevVedlegg.queryKey(props.saksId, props.brev.id),
    queryFn: () => getBrevVedlegg.queryFn(props.saksId, props.brev.id),
  });

  const leggTilVedleggMutation = useMutation({
    mutationFn: (vedlegg: VedleggKode[]) =>
      delvisOppdaterBrev(props.saksId, props.brev.id, { alltidValgbareVedlegg: vedlegg }),
    onSuccess: (response) => {
      setSavedVedlegg((response.valgteVedlegg as VedleggKode[]) ?? []);
      handleCloseModal();
    },
  });

  const handleOpenModal = () => {
    form.reset({ valgteVedlegg: savedVedlegg });
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    form.reset({ valgteVedlegg: [] });
  };

  const handleLeggTil = form.handleSubmit((data) => {
    leggTilVedleggMutation.mutate(data.valgteVedlegg);
  });

  // Don't show vedlegg section if not P1 and no saved vedlegg
  if (!isP1Brev && savedVedlegg.length === 0 && !isLoading && !isError) {
    return null;
  }

  if (isLoading) {
    return (
      <div>
        <HStack align="center" gap="space-8">
          <BodyShort size="small" weight="semibold">
            Vedlegg
          </BodyShort>
        </HStack>
        <Loader size="small" />
      </div>
    );
  }

  if (isError) {
    return (
      <div>
        <HStack align="center" gap="space-8">
          <BodyShort size="small" weight="semibold">
            Vedlegg
          </BodyShort>
        </HStack>
        <Alert size="small" variant="error">
          {getErrorMessage(error)}
        </Alert>
      </div>
    );
  }

  return (
    <VStack gap="space-8">
      <HStack align="center" gap="space-8">
        <BodyShort size="small" weight="semibold">
          Vedlegg
        </BodyShort>
        <Button
          css={css`
            margin-left: auto;
          `}
          icon={<PlusCircleIcon aria-hidden />}
          onClick={handleOpenModal}
          size="xsmall"
          title="Legg til vedlegg"
          variant="tertiary"
        />
      </HStack>

      {/* P1 vedlegg with its own edit button */}
      {isP1Brev && (
        <HStack align="center" justify="space-between">
          <BodyShort size="small">P1</BodyShort>
          {!props.erLaast && (
            <BoxNew asChild borderRadius="4">
              <Button
                data-cy="p1-edit-button"
                icon={<PencilIcon />}
                onClick={() => setIsP1ModalOpen(true)}
                size="xsmall"
                type="button"
                variant="tertiary"
              />
            </BoxNew>
          )}
        </HStack>
      )}

      {/* Other saved vedlegg */}
      {savedVedlegg.length > 0 && (
        <VStack gap="space-4">
          {savedVedlegg.map((kode) => (
            <BodyShort key={kode} size="small">
              {getVedleggLabel(kode)}
            </BodyShort>
          ))}
        </VStack>
      )}

      {/* P1 Edit Modal */}
      {isP1ModalOpen && (
        <P1EditModal
          brevId={props.brev.id}
          onClose={() => setIsP1ModalOpen(false)}
          open={isP1ModalOpen}
          saksId={props.saksId}
        />
      )}

      {/* Add vedlegg modal */}
      <Modal
        css={css`
          width: 700px;
        `}
        header={{ heading: "Legg til vedlegg/skjema" }}
        onClose={handleCloseModal}
        open={isModalOpen}
      >
        <Modal.Body>
          {vedleggKoder && vedleggKoder.length > 0 ? (
            <Controller
              control={form.control}
              name="valgteVedlegg"
              render={({ field }) => (
                <CheckboxGroup hideLegend legend="Velg vedlegg" onChange={field.onChange} value={field.value}>
                  {vedleggKoder.map((kode) => (
                    <Checkbox key={kode} value={kode}>
                      {getVedleggLabel(kode)}
                    </Checkbox>
                  ))}
                </CheckboxGroup>
              )}
            />
          ) : (
            <BodyShort>Ingen vedlegg tilgjengelig</BodyShort>
          )}
          {leggTilVedleggMutation.isError && (
            <Alert size="small" variant="error">
              {getErrorMessage(leggTilVedleggMutation.error)}
            </Alert>
          )}
        </Modal.Body>
        <Modal.Footer>
          <HStack gap="space-8" justify="space-between" width="100%">
            <Button onClick={handleCloseModal} variant="tertiary">
              Avbryt
            </Button>
            <Button loading={leggTilVedleggMutation.isPending} onClick={handleLeggTil} variant="primary">
              Legg til
            </Button>
          </HStack>
        </Modal.Footer>
      </Modal>
    </VStack>
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
