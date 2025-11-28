import { css } from "@emotion/react";
import { PencilIcon, XMarkOctagonFillIcon } from "@navikt/aksel-icons";
import {
  Accordion,
  Alert,
  BodyShort,
  Button,
  Detail,
  Heading,
  HStack,
  Label,
  Loader,
  Modal,
  Radio,
  RadioGroup,
  Switch,
  Tabs,
  Tag,
  Textarea,
  VStack,
} from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { useMemo, useState } from "react";

import { type UserInfo } from "~/api/bff-endpoints";
import { getBrev } from "~/api/brev-queries";
import { delvisOppdaterBrev, hentAlleBrevForSak } from "~/api/sak-api-endpoints";
import EndreMottakerMedOppsummeringOgApiHåndtering from "~/components/EndreMottakerMedApiHåndtering";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import { useUserInfo } from "~/hooks/useUserInfo";
import type { BrevStatus, DelvisOppdaterBrevResponse } from "~/types/brev";
import { type BrevInfo, Distribusjonstype } from "~/types/brev";
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
          <VStack gap="2">
            <Brevtilstand gjeldendeBruker={gjeldendeBruker} status={properties.brev.status} />
            <Label size="small">{properties.brev.brevtittel}</Label>
          </VStack>
        </Accordion.Header>
        <Accordion.Content>
          <VStack gap="4">
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
    <VStack
      css={css`
        gap: 18px;
      `}
    >
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

  const [modalopen, setModalopen] = useState<boolean>(false);

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
          kanTilbakestilleMottaker={!erLaast}
          overrideOppsummering={(edit) => (
            <div>
              <Detail textColor="subtle">Mottaker</Detail>
              <HStack align="start" gap="8" wrap={false}>
                <OppsummeringAvMottaker
                  mottaker={props.brev.mottaker ?? null}
                  saksId={props.saksId}
                  withTitle={false}
                />
                {!erLaast && edit}
              </HStack>
            </div>
          )}
          saksId={props.saksId}
        />

        <VStack gap="space-8">
          <BodyShort size="small" weight="semibold">
            Vedlegg
          </BodyShort>

          <HStack align="center">
            <BodyShort
              css={css`
                margin-right: 7rem;
              `}
              size="small"
            >
              1. P1
            </BodyShort>
            {!erLaast && (
              <Button
                css={css`
                  padding: 0;
                `}
                icon={<PencilIcon fontSize="24px" />}
                onClick={() => setModalopen(true)}
                size="xsmall"
                type="button"
                variant="tertiary"
              />
            )}
          </HStack>
        </VStack>

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
          <VStack
            css={css`
              align-items: flex-start;
            `}
            gap="4"
          >
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

        {props.brev.distribusjonstype === Distribusjonstype.LOKALPRINT && erLaast && <LokalPrintInfoAlerts />}
      </div>

      {modalopen && (
        <P1EditingModal
          brevId={props.brev.id}
          onClose={() => setModalopen(false)}
          open={modalopen}
          saksId={props.saksId}
        />
      )}
    </div>
  );
};

/*
 * Modal for editing P1
 */

type P1TabKey = "innehaver" | "forsikret" | "innvilget" | "avslag" | "institusjon";

const P1EditingModal = (props: { brevId: number; saksId: string; open: boolean; onClose: () => void }) => {
  const [activeTab, setActiveTab] = useState<P1TabKey>("innehaver");

  const [innehaverText, setInnehaverText] = useState("");
  const [forsikretText, setForsikretText] = useState("");
  const [innvilgetText, setInnvilgetText] = useState("");
  const [avslagText, setAvslagText] = useState("");
  const [institusjonText, setInstitusjonText] = useState("");

  const queryClient = useQueryClient();
  const lagreMutation = useMutation({
    mutationFn: async () => {
      // TODO: hook up to actual API for P1-vedlegg
      const payload = {
        p1Vedlegg: {
          innehaver: innehaverText,
          forsikret: forsikretText,
          innvilget: innvilgetText,
          avslag: avslagText,
          institusjon: institusjonText,
        },
      };
      return delvisOppdaterBrev(props.saksId, props.brevId, payload);
    },
    onSuccess: () => {
      // Refresh brev data when saved
      queryClient.invalidateQueries({
        queryKey: getBrev.queryKey(props.brevId),
      });
      props.onClose();
    },
  });

  const handleCancel = () => {
    // Optional: reset state here if we want to discard edits explicitly
    props.onClose();
  };

  const handleSave = () => {
    lagreMutation.mutate();
  };

  const p1ModalOverride = css`
    && {
      max-width: 85vw;
      width: 85vw;
      max-height: 80vh;
      height: 80vh;
      display: flex;
      flex-direction: column;
    }
  `;

  const tabsPanelStyle = css`
    margin-top: 2rem;
  `;

  return (
    <Modal aria-label="Rediger vedlegg P1" css={p1ModalOverride} onClose={handleCancel} open={props.open} size="medium">
      <Modal.Header>
        <Heading size="medium">Overstyring av vedlegg – P1 samlet melding om pensjonsvedtak</Heading>
      </Modal.Header>

      <Modal.Body
        css={css`
          flex: 1 1 auto;
          overflow: auto;
        `}
      >
        <Tabs onChange={(v) => setActiveTab(v as P1TabKey)} value={activeTab}>
          <Tabs.List>
            <Tabs.Tab label="1. Personopplysninger om innehaveren" value="innehaver" />
            <Tabs.Tab label="2. Personopplysninger om den forsikrede" value="forsikret" />
            <Tabs.Tab label="3. Innvilget pensjon" value="innvilget" />
            <Tabs.Tab label="4. Avslag på pensjon" value="avslag" />
            <Tabs.Tab label="5. Institusjonen som har fylt ut skjemaet" value="institusjon" />
          </Tabs.List>

          <Tabs.Panel css={tabsPanelStyle} value="innehaver">
            <Textarea
              label="Personopplysninger om innehaveren"
              minRows={6}
              onChange={(e) => setInnehaverText(e.target.value)}
              value={innehaverText}
            />
          </Tabs.Panel>

          <Tabs.Panel css={tabsPanelStyle} value="forsikret">
            <Textarea
              label="Personopplysninger om den forsikrede"
              minRows={6}
              onChange={(e) => setForsikretText(e.target.value)}
              value={forsikretText}
            />
          </Tabs.Panel>

          <Tabs.Panel css={tabsPanelStyle} value="innvilget">
            <Textarea
              label="Innvilget pensjon"
              minRows={6}
              onChange={(e) => setInnvilgetText(e.target.value)}
              value={innvilgetText}
            />
          </Tabs.Panel>

          <Tabs.Panel css={tabsPanelStyle} value="avslag">
            <Textarea
              label="Avslag på pensjon"
              minRows={6}
              onChange={(e) => setAvslagText(e.target.value)}
              value={avslagText}
            />
          </Tabs.Panel>

          <Tabs.Panel css={tabsPanelStyle} value="institusjon">
            <Textarea
              label="Institusjonen som har fylt ut skjemaet"
              minRows={6}
              onChange={(e) => setInstitusjonText(e.target.value)}
              value={institusjonText}
            />
          </Tabs.Panel>
        </Tabs>

        {lagreMutation.isError && (
          <Alert
            css={css`
              margin-top: 1rem;
            `}
            size="small"
            variant="error"
          >
            Noe gikk galt ved lagring av vedlegg P1.
          </Alert>
        )}
      </Modal.Body>

      <Modal.Footer
        css={css`
          justify-content: space-between;
          flex-shrink: 0;
        `}
      >
        <Button loading={lagreMutation.isPending} onClick={handleSave} size="medium" type="button" variant="primary">
          Lagre
        </Button>
        <Button
          css={css`
            && {
              margin-left: 0;
            }
          `}
          disabled={lagreMutation.isPending}
          onClick={handleCancel}
          type="button"
          variant="tertiary"
        >
          Avbryt
        </Button>
      </Modal.Footer>
    </Modal>
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
