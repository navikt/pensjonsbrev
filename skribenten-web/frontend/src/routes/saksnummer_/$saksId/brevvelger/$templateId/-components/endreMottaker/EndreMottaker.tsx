import { css } from "@emotion/react";
import { BodyShort, Button, Modal, Tabs } from "@navikt/ds-react";
import { useNavigate } from "@tanstack/react-router";
import { useState } from "react";

import type { Nullable } from "~/types/Nullable";

import { Route } from "../../route";
import BekreftAvbrytelse from "./BekreftAvbrytelse";
import type { FinnSamhandlerFormData } from "./EndreMottakerUtils";
import HentOgVisSamhandlerAdresse from "./HentOgVisSamhandlerAdresse";
import SøkOgVelgSamhandlerForm from "./SøkOgVelgSamhandlerForm";

const EndreMottaker = () => {
  const [modalÅpen, setModalÅpen] = useState<boolean>(false);
  const navigate = useNavigate({ from: Route.fullPath });

  return (
    <div>
      <h1>Endre mottaker</h1>
      {modalÅpen && (
        <EndreMottakerModal
          onBekreftNyMottaker={(id) => {
            setModalÅpen(false);
            navigate({
              search: (s) => ({ ...s, idTSSEkstern: id }),
              replace: true,
            });
          }}
          onClose={() => setModalÅpen(false)}
          åpen={modalÅpen}
        />
      )}

      <Button onClick={() => setModalÅpen(true)} size="small" type="button" variant="secondary">
        Endre mottaker
      </Button>
    </div>
  );
};

export default EndreMottaker;

type SamhandlerModalState = Nullable<{ id: Nullable<string>; values: FinnSamhandlerFormData }>;

/*
kjent bug: dersom man gjør formet dirty, og avbryter, vil bekreftelsen poppe opp. Dersom du så trykker "ikke avbryt", og deretter avbryt igje, vil modalen lukke seg.
            man forventer kanskje alltid at så lenge der er noe info lagt inn, så skal bekreftelsen poppe opp?
            Uansett, det skjer fordi vi setter inn nytt sett med default values etter "ikke avbryt" - og RHF bruker denne nye default-valuen til å sjekke om formen er dirty.
*/
const EndreMottakerModal = (properties: {
  åpen: boolean;
  onBekreftNyMottaker: (id: string) => void;
  onClose: () => void;
}) => {
  //dette er for å markere SB intensjon om å avbryte. Dette brukes for å gi oss en ekstra guard dersom SB har skrevet inn noe info
  const [vilAvbryte, setVilAvbryte] = useState<boolean>(false);

  //dette er for å markere om SB har gjort endringer i samhandler-steget. brukes i kombinasjon med vilAvbryte
  const [harSamhandlerEndringer, setHarSamhandlerEndringer] = useState<boolean>(false);

  //verdiene til samhandler steget dersom SBH har trykker på avbryt, eller har valgt en samhandler
  const [samhandlerValues, setSamhandlerValues] = useState<SamhandlerModalState>(null);

  //dersom SBH har gjort endringer og har trykket på avbryt, vil vi ikke lukke modalen med en gang - vi vil spørre om bekreftelse
  const onAvbrytClick = (v: SamhandlerModalState) => {
    setVilAvbryte(true);
    setSamhandlerValues(v);
    if (!harSamhandlerEndringer) {
      properties.onClose();
    }
  };

  return (
    <Modal
      header={{
        heading: vilAvbryte && harSamhandlerEndringer ? "Vil du avbryte endring av mottaker?" : "Endre mottaker",
      }}
      onClose={properties.onClose}
      open={properties.åpen}
      /*
        ved å ha modalen som en portal vil ikke browseren klage på at vi kommer til å ha en form inni en annen form.
        vi vil likevel få litt andre tekniske problemer som event propagation
        En løsning for dette ville være å flytte EndreMottaker-formen til parent formen - men det vil jo komme med sine nedsider også. 
      */
      portal
      width={600}
    >
      <Modal.Body>
        {vilAvbryte && harSamhandlerEndringer ? (
          <BekreftAvbrytelse onBekreftAvbryt={properties.onClose} onIkkeAvbryt={() => setVilAvbryte(false)} />
        ) : (
          <ModalTabs
            onAvbrytClick={onAvbrytClick}
            onBekreftNyMottaker={properties.onBekreftNyMottaker}
            samhandlerValues={samhandlerValues}
            setHarSamhandlerEndringer={setHarSamhandlerEndringer}
            setSamhandler={setSamhandlerValues}
          />
        )}
      </Modal.Body>
    </Modal>
  );
};

type ModalTabs = "samhandler" | "manuellAdresse" | "oppsummering";

const ModalTabs = (properties: {
  samhandlerValues: SamhandlerModalState;
  setSamhandler: (values: SamhandlerModalState) => void;
  setHarSamhandlerEndringer: (b: boolean) => void;
  onBekreftNyMottaker: (id: string) => void;
  onAvbrytClick: (v: SamhandlerModalState) => void;
}) => {
  //fordi oppsummering ikke er en tab som skal vises i tab-listen, så har vi en egen state for dette
  const [tab, setTab] = useState<ModalTabs>("samhandler");

  return (
    <div>
      {tab === "oppsummering" ? (
        <OppsummeringsTab
          onAvbrytClick={properties.onAvbrytClick}
          onBekreftNyMottaker={properties.onBekreftNyMottaker}
          onTilbake={(s) => setTab(s)}
          setSamhandler={properties.setSamhandler}
          values={properties.samhandlerValues}
        />
      ) : (
        <Tabs onChange={(s) => setTab(s as ModalTabs)} value={tab}>
          <Tabs.List>
            <Tabs.Tab label="Finn samhandler" value="samhandler" />
            <Tabs.Tab label="Legg til manuelt" value="manuellAdresse" />
          </Tabs.List>
          <div
            css={css`
              margin-top: 1rem;
            `}
          >
            <Tabs.Panel value="samhandler">
              <SøkOgVelgSamhandlerForm
                defaultValues={properties.samhandlerValues?.values ?? null}
                harEndringer={properties.setHarSamhandlerEndringer}
                onCloseIntent={(v) =>
                  properties.onAvbrytClick({ id: properties.samhandlerValues?.id ?? null, values: v })
                }
                onSamhandlerValg={(id, values) => {
                  properties.setSamhandler({ id, values });
                  setTab("oppsummering");
                }}
              />
            </Tabs.Panel>
            <Tabs.Panel value="manuellAdresse">
              {/*<UtfyllingAvManuellAdresseForm control={properties.control} onClose={properties.onClose} /> */}
              <BodyShort>Her kommer det innhold</BodyShort>
            </Tabs.Panel>
          </div>
        </Tabs>
      )}
    </div>
  );
};

const OppsummeringsTab = (properties: {
  values: SamhandlerModalState;
  setSamhandler: (values: SamhandlerModalState) => void;
  onBekreftNyMottaker: (id: string) => void;
  onAvbrytClick: (v: SamhandlerModalState) => void;
  onTilbake: (from: "samhandler") => void;
}) => {
  if (!properties.values?.id) {
    throw new Error("Teknisk feil - SamhandlerId er ikke satt");
  }

  return (
    <HentOgVisSamhandlerAdresse
      id={properties.values?.id}
      onBekreftNyMottaker={() => properties.onBekreftNyMottaker(properties.values!.id!)}
      //kan ikke være et case der values er null når man avbryter ved oppsummeringen, vel?
      onCloseIntent={() => properties.onAvbrytClick(properties.values)}
      //bug(?) - mutation er blitt reset, og dem må tykke på søk på nytt. SB forventer kanskje at søket er der fortsatt?
      onTilbakeTilSøk={() => properties.onTilbake("samhandler")}
      typeMottaker={properties.values!.values.samhandlerType!}
    />
  );
};
