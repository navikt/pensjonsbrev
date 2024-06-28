import { css } from "@emotion/react";
import { BodyShort, Button, Modal, Tabs } from "@navikt/ds-react";
import { useNavigate } from "@tanstack/react-router";
import { useState } from "react";

import type { Nullable } from "~/types/Nullable";

import { Route } from "../../route";
import BekreftAvbrytelse from "./BekreftAvbrytelse";
import HentOgVisSamhandlerAdresse from "./EndreMottakerOppsummering";
import type { FinnSamhandlerFormData } from "./EndreMottakerUtils";
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

  const [harSamhandlerEndringer, setHarSamhandlerEndringer] = useState<boolean>(false);
  const [samhandlerValues, setSamhandlerValues] = useState<Nullable<FinnSamhandlerFormData>>(null);
  const [samhandlerId, setSamhandlerId] = useState<Nullable<string>>(null);

  const onAvbrytClick = () => {
    setVilAvbryte(true);
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
            defaultValuesSamhandler={samhandlerValues}
            onAvbrytClick={(v) => {
              setSamhandlerValues(v);
              onAvbrytClick();
            }}
            onBekreftNyMottaker={properties.onBekreftNyMottaker}
            samhandlerId={samhandlerId}
            setHarSamhandlerEndringer={setHarSamhandlerEndringer}
            setSamhandler={(id, values) => {
              setSamhandlerId(id);
              setSamhandlerValues(values);
            }}
          />
        )}
      </Modal.Body>
    </Modal>
  );
};

const ModalTabs = (properties: {
  samhandlerId: Nullable<string>;
  setSamhandler: (id: Nullable<string>, formValues: Nullable<FinnSamhandlerFormData>) => void;
  onAvbrytClick: (v: FinnSamhandlerFormData) => void;
  setHarSamhandlerEndringer: (b: boolean) => void;
  onBekreftNyMottaker: (id: string) => void;
  defaultValuesSamhandler: Nullable<FinnSamhandlerFormData>;
}) => {
  return (
    <div>
      {properties.samhandlerId ? (
        <HentOgVisSamhandlerAdresse
          id={properties.samhandlerId}
          onBekreftNyMottaker={properties.onBekreftNyMottaker}
          //kan ikke være et case der values er null når man avbryter ved oppsummeringen, vel?
          onCloseIntent={() => properties.onAvbrytClick(properties.defaultValuesSamhandler!)}
          //bug(?) - mutation er blitt reset, og dem må tykke på søk på nytt. SB forventer kanskje at søket er der fortsatt?
          onTilbakeTilSøk={() => properties.setSamhandler(null, properties.defaultValuesSamhandler)}
          typeMottaker={properties.defaultValuesSamhandler?.samhandlerType ?? undefined}
        />
      ) : (
        <Tabs defaultValue="samhandler">
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
                defaultValues={properties.defaultValuesSamhandler}
                harEndringer={(b) => {
                  if (b) {
                    properties.setHarSamhandlerEndringer(true);
                  } else {
                    properties.setHarSamhandlerEndringer(false);
                  }
                }}
                onCloseIntent={(v) => properties.onAvbrytClick(v)}
                onSamhandlerValg={(id, values) => properties.setSamhandler(id, values)}
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
