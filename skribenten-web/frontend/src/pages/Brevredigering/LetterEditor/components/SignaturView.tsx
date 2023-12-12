import type { Signatur } from "~/types/brevbakerTypes";

import styles from "./SignaturView.module.css";

export type SignaturViewProperties = {
  signatur: Signatur;
};

const Saksbehandler = ({ rolleTekst, navn }: { rolleTekst: string; navn?: string }) => {
  return navn ? (
    <div>
      <div>{navn}</div>
      <div>{rolleTekst}</div>
    </div>
  ) : null;
};

export const SignaturView = ({ signatur }: SignaturViewProperties) => (
  <div className={styles.container}>
    <div>{signatur.hilsenTekst}</div>
    <div className={styles.saksbehandlere}>
      <Saksbehandler navn={signatur.saksbehandlerNavn} rolleTekst={signatur.saksbehandlerRolleTekst} />
      <Saksbehandler navn={signatur.attesterendeSaksbehandlerNavn} rolleTekst={signatur.saksbehandlerRolleTekst} />
    </div>
    <div>{signatur.navAvsenderEnhet}</div>
  </div>
);
