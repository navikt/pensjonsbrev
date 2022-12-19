import {Signatur} from "../../model"
import {FC} from "react"
import styles from "./SignaturView.module.css"

export interface SignaturViewProps {
    signatur: Signatur
}

const Saksbehandler: FC<{rolleTekst: string, navn?: string}> = ({rolleTekst, navn}) => {
    if(navn) {
        return (
            <div>
                <div>{navn}</div>
                <div>{rolleTekst}</div>
            </div>
        )
    } else return null
}

const SignaturView: FC<SignaturViewProps> = ({signatur}) => (
    <div className={styles.container}>
        <div>{signatur.hilsenTekst}</div>
        <div className={styles.saksbehandlere}>
            <Saksbehandler rolleTekst={signatur.saksbehandlerRolleTekst} navn={signatur.saksbehandlerNavn}/>
            <Saksbehandler rolleTekst={signatur.saksbehandlerRolleTekst} navn={signatur.attesterendeSaksbehandlerNavn}/>
        </div>
        <div>{signatur.navAvsenderEnhet}</div>
    </div>
)
export default SignaturView