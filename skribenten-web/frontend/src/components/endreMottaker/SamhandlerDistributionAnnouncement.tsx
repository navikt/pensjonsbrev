import { BodyShort, Box, Button, HStack, Modal, Table, Tabs, Tag, VStack } from "@navikt/ds-react";
import { useEffect, useMemo, useRef, useState } from "react";

import {
  FeatureAnnouncement,
  type FeatureAnnouncementStep,
} from "~/components/featureAnnouncement/FeatureAnnouncement";
import {
  endAnnouncementReplay,
  useAnnouncementReplay,
} from "~/components/featureAnnouncement/featureAnnouncementState";
import {
  automaticTourUntil,
  listedUntil,
  SAMHANDLER_DISTRIBUTION_ANNOUNCEMENT,
} from "~/components/notifications/productAnnouncements";

import { MottakerDetaljer } from "./SamhandlerSearchResults";

export const SAMHANDLER_DISTRIBUTION_ANNOUNCEMENT_ID = SAMHANDLER_DISTRIBUTION_ANNOUNCEMENT.id;
export const SAMHANDLER_DISTRIBUTION_ANNOUNCEMENT_SHOW_UNTIL = automaticTourUntil(SAMHANDLER_DISTRIBUTION_ANNOUNCEMENT);
export const SAMHANDLER_DISTRIBUTION_ANNOUNCEMENT_REPLAYABLE_UNTIL = listedUntil(SAMHANDLER_DISTRIBUTION_ANNOUNCEMENT);

const MED_ORGANISASJONSNUMMER = {
  samhandler: {
    idType: "ORG",
    navn: "STATENS PENSJONSKASSE",
    offentligId: "974760967",
    organisasjonsnummer: "974760967",
    samhandlerType: "DIVE",
  },
};

const UTEN_ORGANISASJONSNUMMER = {
  samhandler: {
    idType: "TSS",
    navn: "STATENS PENSJONSKASSE",
    offentligId: "80000425840",
    organisasjonsnummer: null,
    samhandlerType: "TPOR",
  },
  adresse: {
    navn: "STATENS PENSJONSKASSE",
    linje1: "POSTBOKS 10 SKØYEN",
    postnr: "0212",
    poststed: "OSLO",
    land: "NOR",
  },
};

export function SamhandlerDistributionAnnouncement({
  when = true,
  skalKunOppdatereSamhandler = false,
}: {
  when?: boolean;
  skalKunOppdatereSamhandler?: boolean;
}) {
  const replayGeneration = useAnnouncementReplay(SAMHANDLER_DISTRIBUTION_ANNOUNCEMENT_ID);
  const modalOpen = when && replayGeneration > 0;
  const [dialog, setDialog] = useState<HTMLDialogElement | null>(null);
  const digitalTargetRef = useRef<HTMLTableSectionElement>(null);
  const postalTargetRef = useRef<HTMLTableSectionElement>(null);
  const previousFocusRef = useRef<HTMLElement | null>(null);

  useEffect(() => {
    if (!modalOpen) return;
    previousFocusRef.current = document.activeElement instanceof HTMLElement ? document.activeElement : null;
    const dialogAtOpen = dialog;
    return () => {
      queueMicrotask(() => {
        const active = document.activeElement;
        if (!active || active === document.body || !active.isConnected || dialogAtOpen?.contains(active)) {
          previousFocusRef.current?.focus();
        }
      });
    };
  }, [modalOpen, dialog]);

  const steps = useMemo<FeatureAnnouncementStep[]>(
    () => [
      {
        target: digitalTargetRef,
        title: "Digital utsending via Altinn",
        content:
          "Har samhandleren organisasjonsnummer i samhandlerregisteret, vises organisasjonsnummeret når du utvider treffet. Da sendes brevet digitalt via Altinn.",
        placement: "bottom",
      },
      {
        target: postalTargetRef,
        title: "Sendes i posten når organisasjonsnummer mangler",
        content:
          "Uten organisasjonsnummer i samhandlerregisteret vises postadressen i stedet, og brevet sendes i posten.",
        placement: "top",
      },
    ],
    [],
  );

  const closeModal = () => endAnnouncementReplay(replayGeneration);

  return (
    <>
      <Modal
        css={{ "&[open]": { animation: "none" } }}
        data-testid="samhandler-tour-modal"
        header={{ closeButton: false, heading: "Endre mottaker" }}
        onClose={closeModal}
        open={modalOpen}
        portal
        ref={setDialog}
        width={700}
      >
        <Modal.Body>
          {!modalOpen ? null : (
            <VStack gap="space-16">
              <Tabs size="small" value="samhandler">
                <Tabs.List>
                  <Tabs.Tab label="Finn samhandler" value="samhandler" />
                  {skalKunOppdatereSamhandler ? null : <Tabs.Tab label="Legg til manuelt" value="manuellAdresse" />}
                </Tabs.List>
                <Box marginBlock="space-16 space-0">
                  <Tabs.Panel value="samhandler">
                    <VStack gap="space-8">
                      <BodyShort size="small" weight="semibold">
                        2 treff
                      </BodyShort>
                      <Table size="small">
                        <Table.Header>
                          <Table.Row>
                            <Table.HeaderCell />
                            <Table.HeaderCell scope="col">Navn</Table.HeaderCell>
                            <Table.HeaderCell />
                            <Table.HeaderCell />
                          </Table.Row>
                        </Table.Header>
                        <Table.Body ref={digitalTargetRef}>
                          <ExampleSamhandlerRow
                            ariaLabel="Statens Pensjonskasse med organisasjonsnummer"
                            mottaker={MED_ORGANISASJONSNUMMER}
                            selected
                          />
                        </Table.Body>
                        <Table.Body ref={postalTargetRef}>
                          <ExampleSamhandlerRow
                            ariaLabel="Statens Pensjonskasse med postadresse"
                            mottaker={UTEN_ORGANISASJONSNUMMER}
                            selected={false}
                          />
                        </Table.Body>
                      </Table>
                    </VStack>
                  </Tabs.Panel>
                </Box>
              </Tabs>
              <HStack justify="space-between">
                <Button onClick={closeModal} size="small" type="button" variant="tertiary">
                  Avbryt
                </Button>
                <Button disabled size="small" type="button">
                  Lagre og lukk
                </Button>
              </HStack>
            </VStack>
          )}
        </Modal.Body>
      </Modal>
      <FeatureAnnouncement
        automatic={false}
        dialog={dialog}
        id={SAMHANDLER_DISTRIBUTION_ANNOUNCEMENT_ID}
        replayableUntil={SAMHANDLER_DISTRIBUTION_ANNOUNCEMENT_REPLAYABLE_UNTIL}
        showFrom={SAMHANDLER_DISTRIBUTION_ANNOUNCEMENT.publishedAt}
        showUntil={SAMHANDLER_DISTRIBUTION_ANNOUNCEMENT_SHOW_UNTIL}
        steps={steps}
        when={when}
      />
    </>
  );
}

function ExampleSamhandlerRow({
  ariaLabel,
  mottaker,
  selected,
}: {
  ariaLabel: string;
  mottaker: Parameters<typeof MottakerDetaljer>[0]["mottaker"];
  selected: boolean;
}) {
  return (
    <Table.ExpandableRow
      content={<MottakerDetaljer mottaker={mottaker} />}
      onOpenChange={() => {}}
      open
      selected={selected}
      togglePlacement="right"
    >
      <Table.DataCell>
        <span className="aksel-radio aksel-radio--small">
          <input
            aria-label={ariaLabel}
            checked={selected}
            className="aksel-radio__input"
            name="samhandler-tour-eksempel"
            readOnly
            tabIndex={-1}
            type="radio"
          />
        </span>
      </Table.DataCell>
      <Table.DataCell>Statens Pensjonskasse</Table.DataCell>
      <Table.DataCell>
        <Tag data-color="neutral" size="xsmall" variant="moderate">
          Samhandler
        </Tag>
      </Table.DataCell>
    </Table.ExpandableRow>
  );
}
