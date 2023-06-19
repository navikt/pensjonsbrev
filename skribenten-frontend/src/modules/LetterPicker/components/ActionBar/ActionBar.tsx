import React, {FC} from "react"
import {Button, Label, Select} from "@navikt/ds-react"
import BottomMenu from "../../../../components/bottom-menu/BottomMenu"
import {LetterSelection} from "../../model/skribenten"
import styles from "./ActionBar.module.css"

interface ActionBarProps {
    selectedLetter: LetterSelection | null
}

const ActionBar: FC<ActionBarProps> = ({selectedLetter}) => {
    const disabled = selectedLetter == null
    const selection = selectedLetter?.spraak?.map( sprak => (<option key={sprak}>{sprakTekst(sprak)}</option>))

    return (
        <BottomMenu>
            <div className={styles.actionBar}>
                <div className={styles.spraakVelger}>
                    <Label>
                        Språklag:
                    </Label>
                    <Select disabled={disabled} label="Språklag" className={styles.languageSelection} hideLabel size="small">
                        {selection}
                    </Select>
                </div>
                <div>
                    <Button variant="secondary" size="small" className={styles.editLetterButton} disabled={disabled}>Rediger brev</Button>
                    <Button size="small" className={styles.submitLetterButton} disabled={disabled}>Ferdigstill brev og gå videre</Button>
                </div>
            </div>
        </BottomMenu>
    )
}


//TODO i18n?
function sprakTekst (sprak:string){
    switch (sprak) {
        case "NN": return "Nynorsk"
        case "NB": return "Norsk Bokmål"
        case "EN": return "Engelsk"
        case "SE": return "Nordsamisk"
        case "FR": return "Fransk"
    }
}

export default ActionBar