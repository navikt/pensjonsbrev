import type { Sakspart } from "~/types/brevbakerTypes";

import styles from "./SakspartView.module.css";

export type SakspartViewProperties = {
  sakspart: Sakspart;
};
export const SakspartView = ({ sakspart }: SakspartViewProperties) => (
  <div className={styles.container}>
    <div className={styles.sakspartContainer}>
      <div>Saken gjelder:</div>
      <div>{sakspart.gjelderNavn}</div>
      <div>Fødselsnummer:</div>
      <div>{sakspart.gjelderFoedselsnummer}</div>
      <div>Saksnummer:</div>
      <div>{sakspart.saksnummer}</div>
    </div>
    <div className={styles.dato}>
      <div>{sakspart.dokumentDato}</div>
    </div>
  </div>
);
