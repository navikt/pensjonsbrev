import React, {FC} from "react"
import styles from "./CaseContextBar.module.css"
import {CopyButton, Loader} from "@navikt/ds-react"


interface CaseContextBarProps {
    saksnummer: string | undefined,
    foedselsnummer: string | undefined,
    gjelderNavn: string | undefined,
    foedselsdato: string | undefined,
    sakstype: string | undefined,
}

const CaseContextBar: FC<CaseContextBarProps> = ({saksnummer, foedselsnummer, gjelderNavn, foedselsdato, sakstype}) => {
    return (
        <div className={styles.bar}>
            <div> {foedselsnummer} </div>
            <CopyButton disabled={foedselsnummer == null}
                        copyText={foedselsnummer || ""}
                        className={styles.copyButton}/>
            <div>/</div>
            <div> {gjelderNavn || <Loader/>} </div>
            <div>/</div>
            <div> Født: {foedselsdato|| <Loader/>} </div>
            <div>/</div>
            <div> Sakstype: {sakstypeToName(sakstype)|| <Loader/>} </div>
            <div>/</div>
            <div> Saksnummer: {saksnummer|| <Loader/>} </div>
            <CopyButton disabled={saksnummer == null}
                        copyText={saksnummer || ""}
                        className={styles.copyButton}/>
        </div>
    )
}

export default CaseContextBar

function sakstypeToName(sakstype: string | undefined): string | undefined {
    switch (sakstype) {
        case "AFP":
            return "AFP"
        case "AFP_PRIVAT":
            return "AFP Privat"
        case "ALDER":
            return "Alderspensjon"
        case "BARNEP":
            return "Barnepensjon"
        case "FAM_PL":
            return "Familiepleierytelse"
        case "GAM_YRK":
            return "Gammel yrkesskade"
        case "GENRL":
            return "Generell"
        case "GJENLEV":
            return "Gjenlevendeytelse"
        case "GRBL":
            return "Grunnblanketter"
        case "KRIGSP":
            return "Krigspensjon"
        case "OMSORG":
            return "Omsorgsopptjening"
        case "UFOREP":
            return "Uføretrygd"
        default:
            return sakstype
    }
}