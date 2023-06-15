import React, {FC} from "react"
import styles from "./CaseContextBar.module.css"
import { CopyButton } from "@navikt/ds-react"


interface CaseContextBarProps {
    saksnummer: string,
    foedselsnummer: string,
    gjelderNavn: string,
    foedselsdato: string,
    sakstype: string,
}

const CaseContextBar: FC<CaseContextBarProps> = ({saksnummer, foedselsnummer, gjelderNavn, foedselsdato, sakstype}) => {
    return (
        <div className={styles.bar}>
            <div> {foedselsnummer} </div>
            <CopyButton copyText={foedselsnummer} className={styles.copyButton}/>
            <div>/</div>
            <div> {gjelderNavn} </div>
            <div>/</div>
            <div> Født: {foedselsdato} </div>
            <div>/</div>
            <div> Sakstype: {sakstype} </div>
            <div>/</div>
            <div> Saksnummer: {saksnummer} </div>
            <CopyButton copyText={saksnummer} className={styles.copyButton} />
        </div>
    )
}

export default CaseContextBar