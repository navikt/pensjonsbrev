import {
  Bleed,
  BodyShort,
  Box,
  Button,
  HStack,
  Modal,
  Search,
  Select,
  Table,
  Tabs,
  Tag,
  VStack,
} from "@navikt/ds-react";
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
import { SamhandlerTypeSelect } from "~/components/select/SamhandlerSelect";

import { InnOgUtland, Søketype } from "./EndreMottakerUtils";
import { MottakerDetaljer, sortedColumnHeaderCss } from "./SamhandlerSearchResults";

export const SAMHANDLER_DISTRIBUTION_ANNOUNCEMENT_ID = SAMHANDLER_DISTRIBUTION_ANNOUNCEMENT.id;
export const SAMHANDLER_DISTRIBUTION_ANNOUNCEMENT_SHOW_UNTIL = automaticTourUntil(SAMHANDLER_DISTRIBUTION_ANNOUNCEMENT);
export const SAMHANDLER_DISTRIBUTION_ANNOUNCEMENT_REPLAYABLE_UNTIL = listedUntil(SAMHANDLER_DISTRIBUTION_ANNOUNCEMENT);

type Mottaker = Parameters<typeof MottakerDetaljer>[0]["mottaker"];

type ExampleSearch = {
  samhandlerType: string;
  hitsBefore: string[];
  selected: Mottaker;
  hitsAfter: string[];
};

const MED_ORGANISASJONSNUMMER: ExampleSearch = {
  samhandlerType: "X",
  hitsBefore: ["Statens Innkrevingssentral"],
  selected: {
    samhandler: {
      idType: "ORG",
      navn: "STATENS PENSJONSKASSE",
      offentligId: "974760967",
      organisasjonsnummer: "974760967",
      samhandlerType: "X",
    },
  },
  hitsAfter: ["Statens Pensjonskasse Forvaltning"],
};

const UTEN_ORGANISASJONSNUMMER: ExampleSearch = {
  samhandlerType: "TEPE",
  hitsBefore: [],
  selected: {
    samhandler: {
      idType: "TSS",
      navn: "STATENS PENSJONSKASSE",
      offentligId: "80000425840",
      organisasjonsnummer: null,
      samhandlerType: "TEPE",
    },
    adresse: {
      navn: "STATENS PENSJONSKASSE",
      linje1: "POSTBOKS 10 SKØYEN",
      postnr: "0212",
      poststed: "OSLO",
      land: "NOR",
    },
  },
  hitsAfter: ["Statens Pensjonskasse"],
};

// Resolves once React has rendered the new example, so Joyride measures the new target.
const nextPaint = () =>
  new Promise<void>((resolve) => requestAnimationFrame(() => requestAnimationFrame(() => resolve())));

/**
 * «Se hvordan» for digital utsending to samhandlere: opens an illustration of «Endre mottaker» with one search where the
 * samhandler has an organisasjonsnummer (sent digitally) and one where it does not (sent by post), and walks through them.
 * Never starts automatically. Mount it where the user can change the recipient.
 */
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
  const [example, setExample] = useState<ExampleSearch>(MED_ORGANISASJONSNUMMER);
  const targetRef = useRef<HTMLTableSectionElement>(null);
  const previousFocusRef = useRef<HTMLElement | null>(null);

  if (!modalOpen && example !== MED_ORGANISASJONSNUMMER) setExample(MED_ORGANISASJONSNUMMER);

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
        target: targetRef,
        title: "Digital utsending via Altinn",
        content:
          "Har samhandleren organisasjonsnummer i samhandlerregisteret, vises organisasjonsnummeret når du utvider treffet. Da sendes brevet digitalt via Altinn.",
        placement: "top",
        before: () => {
          setExample(MED_ORGANISASJONSNUMMER);
          return nextPaint();
        },
      },
      {
        target: targetRef,
        title: "Sendes i posten når organisasjonsnummer mangler",
        content:
          "Uten organisasjonsnummer i samhandlerregisteret vises postadressen i stedet, og brevet sendes i posten.",
        placement: "top",
        before: () => {
          setExample(UTEN_ORGANISASJONSNUMMER);
          return nextPaint();
        },
      },
    ],
    [],
  );

  const closeModal = () => endAnnouncementReplay(replayGeneration);

  return (
    <>
      <Modal
        // Joyride positions the tour with position: fixed, which an opening animation's transform would offset.
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
              {/* An example, not a form: it looks like «Endre mottaker», but cannot be focused or changed. */}
              <div inert>
                <Tabs size="small" value="samhandler">
                  <Tabs.List>
                    <Tabs.Tab label="Finn samhandler" value="samhandler" />
                    {skalKunOppdatereSamhandler ? null : <Tabs.Tab label="Legg til manuelt" value="manuellAdresse" />}
                  </Tabs.List>
                  <Box marginBlock="space-16 space-0">
                    <Tabs.Panel value="samhandler">
                      <ExampleSearchForm example={example} targetRef={targetRef} />
                    </Tabs.Panel>
                  </Box>
                </Tabs>
              </div>
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

const noop = () => {};

/** Same layout as SøkOgVelgSamhandlerForm and SamhandlerSearchResults after a search on organisasjonsnavn. */
function ExampleSearchForm({
  example,
  targetRef,
}: {
  example: ExampleSearch;
  targetRef: React.RefObject<HTMLTableSectionElement | null>;
}) {
  const hits = example.hitsBefore.length + 1 + example.hitsAfter.length;

  return (
    <VStack gap="space-24">
      <VStack align="start" gap="space-24">
        <Select label="Søketype" onChange={noop} size="small" value={Søketype.ORGANISASJONSNAVN}>
          <option value={Søketype.ORGANISASJONSNAVN}>Organisasjonsnavn</option>
        </Select>
        <VStack gap="space-24">
          <Select label="Inn-/utland" onChange={noop} size="small" value={InnOgUtland.ALLE}>
            <option value={InnOgUtland.ALLE}>Alle</option>
          </Select>
          <SamhandlerTypeSelect onChange={noop} value={example.samhandlerType} />
          <Search label="Navn" onChange={noop} size="small" value="Statens" variant="simple" />
        </VStack>
      </VStack>
      <HStack align="start">
        <Button size="small" type="button">
          Søk
        </Button>
      </HStack>
      <VStack gap="space-8">
        <BodyShort size="small" weight="semibold">
          {hits} treff
        </BodyShort>
        <Bleed marginInline="space-12">
          <Box marginInline="space-12">
            <Table size="small" sort={{ orderBy: "navn", direction: "ascending" }}>
              <Table.Header>
                <Table.Row>
                  <Table.HeaderCell />
                  <Table.ColumnHeader css={sortedColumnHeaderCss} sortable sortKey="navn">
                    Navn
                  </Table.ColumnHeader>
                  <Table.HeaderCell />
                  <Table.HeaderCell />
                </Table.Row>
              </Table.Header>
              {example.hitsBefore.length > 0 && (
                <Table.Body>
                  {example.hitsBefore.map((navn) => (
                    <ExampleHit key={navn} navn={navn} />
                  ))}
                </Table.Body>
              )}
              <Table.Body ref={targetRef}>
                <ExampleHit mottaker={example.selected} navn="Statens Pensjonskasse" />
              </Table.Body>
              {example.hitsAfter.length > 0 && (
                <Table.Body>
                  {example.hitsAfter.map((navn) => (
                    <ExampleHit key={navn} navn={navn} />
                  ))}
                </Table.Body>
              )}
            </Table>
          </Box>
        </Bleed>
      </VStack>
    </VStack>
  );
}

/** A hit like SamhandlerResultRow. Only the selected hit is expanded, as after the user has clicked it. */
function ExampleHit({ navn, mottaker }: { navn: string; mottaker?: Mottaker }) {
  const selected = mottaker !== undefined;

  return (
    <Table.ExpandableRow
      content={selected ? <MottakerDetaljer mottaker={mottaker} /> : null}
      onOpenChange={noop}
      open={selected}
      selected={selected}
      togglePlacement="right"
    >
      <Table.DataCell>
        <span className="aksel-radio aksel-radio--small">
          <input aria-label={navn} checked={selected} className="aksel-radio__input" readOnly type="radio" />
        </span>
      </Table.DataCell>
      <Table.DataCell>{navn}</Table.DataCell>
      <Table.DataCell>
        <Tag data-color="neutral" size="xsmall" variant="moderate">
          Samhandler
        </Tag>
      </Table.DataCell>
    </Table.ExpandableRow>
  );
}
