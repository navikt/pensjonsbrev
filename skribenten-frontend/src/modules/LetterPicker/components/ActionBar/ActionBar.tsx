import React, {FC, useState} from "react"
import {Button, Label, Select} from "@navikt/ds-react"
import BottomMenu from "../../../../components/bottom-menu/BottomMenu"
import {ForetrukketSpraakOgMaalForm, LanguageCode, Metadata} from "../../../../lib/model/skribenten"
import styles from "./ActionBar.module.css"

interface ActionBarProps {
    selectedLetter: Metadata | undefined
    preferredLanguage: ForetrukketSpraakOgMaalForm | null
    onOrderLetter: (language: string) => void
}

const ActionBar: FC<ActionBarProps> = ({selectedLetter, onOrderLetter}) => {
    const [selectedLanguage, setSelectedLanguage] = useState(0)
    const disabled = !selectedLetter
    const selection = selectedLetter?.spraak?.map(sprak => {
        return (<option key={sprak}>{sprakTekst(sprak)}</option>)
    })

    const handleOrderLetter = () => {
        if (selectedLetter?.spraak && selectedLanguage !== null) {
            onOrderLetter(selectedLetter.spraak[selectedLanguage])
        }
    }

    return (
        <BottomMenu>
            <div className={styles.actionBar}>
                <div className={styles.spraakVelger}>
                    <Label>
                        Språklag:
                    </Label>
                    <Select disabled={disabled}
                            label="Språklag"
                            className={styles.languageSelection}
                            hideLabel
                            onChange={(event: React.ChangeEvent<HTMLSelectElement>) => setSelectedLanguage(event.target.selectedIndex)}
                            size="small">
                        {selection}
                    </Select>
                </div>
                <div>
                    <Button variant="secondary"
                            size="small"
                            className={styles.editLetterButton}
                            onClick={handleOrderLetter}
                            disabled={disabled}>Rediger brev</Button>

                    {/*<Button size="small"
                            className={styles.submitLetterButton}*
                            disabled={disabled}>Ferdigstill brev og gå videre</Button>*/}
                </div>
            </div>
        </BottomMenu>
    )
}


//TODO i18n?
function sprakTekst(sprak: string) {
    switch (sprak) {
        case "NN":
            return "Nynorsk"
        case "NB":
            return "Bokmål"
        case "EN":
            return "Engelsk"
        case "SE":
            return "Nordsamisk"
        case "FR":
            return "Fransk"
    }
}

export default ActionBar