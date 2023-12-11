import type { Sakspart } from "../../lib/model/skribenten";
import styles from "./SakspartView.module.css";

export interface SakspartViewProperties {
  sakspart: Sakspart;
}
const SakspartView: SakspartViewProperties = ({ sakspart }: SakspartViewProperties) => (
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
export default SakspartView;
