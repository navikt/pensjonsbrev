import { css } from "@emotion/react";
import { BodyShort, Button, Modal, Tabs } from "@navikt/ds-react";
import { useState } from "react";

import SøkOgVelgSamhandlerForm from "./SøkOgVelgSamhandlerForm";

const EndreMottaker = () => {
  const [modalÅpen, setModalÅpen] = useState<boolean>(false);
  return (
    <div>
      <h1>Endre mottaker</h1>
      {modalÅpen && <EndreMottakerModal onClose={() => setModalÅpen(false)} åpen={modalÅpen} />}

      <Button onClick={() => setModalÅpen(true)} size="small" type="button" variant="secondary">
        Endre mottaker
      </Button>
    </div>
  );
};

export default EndreMottaker;

const EndreMottakerModal = (properties: { åpen: boolean; onClose: () => void }) => {
  //dette er for å markere SB intensjon om å avbryte. Dette brukes for å gi oss en ekstra guard dersom SB har skrevet inn noe info
  //const [vilAvbryte, setVilAvbryte] = useState<boolean>(false);
  const [tab, setActiveTab] = useState<"samhandler" | "manuellAdresse">("samhandler");

  return (
    <Modal
      header={{
        heading: "Endre mottaker",
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
        <ModalTabs onClose={properties.onClose} setActiveTab={(v) => setActiveTab(v)} tab={tab} />
      </Modal.Body>
    </Modal>
  );
};

const ModalTabs = (properties: {
  tab: "samhandler" | "manuellAdresse";
  setActiveTab: (tab: "samhandler" | "manuellAdresse") => void;
  onClose: () => void;
}) => {
  return (
    <Tabs onChange={(v) => properties.setActiveTab(v as "samhandler" | "manuellAdresse")} value={properties.tab}>
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
          <SøkOgVelgSamhandlerForm onClose={properties.onClose} />
        </Tabs.Panel>
        <Tabs.Panel value="manuellAdresse">
          {/*<UtfyllingAvManuellAdresseForm control={properties.control} onClose={properties.onClose} /> */}
          <BodyShort>Her kommer det innhold</BodyShort>
        </Tabs.Panel>
      </div>
    </Tabs>
  );
};
