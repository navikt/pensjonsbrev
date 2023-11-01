import {Sakspart} from "../../../../lib/model/skribenten"
import {FC} from "react"
import styles from "./SakspartView.module.css"

export interface SakspartViewProps {
    sakspart: Sakspart
}
const SakspartView: FC<SakspartViewProps> = ({sakspart}) => (
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
)
export default SakspartView