import React, {FC, useState} from "react"
import {Button, Label, Select} from "@navikt/ds-react"
import BottomMenu from "../../../../components/bottom-menu/BottomMenu"
import {SpraakKode, Metadata} from "../../../../lib/model/skribenten"
import styles from "./ActionBar.module.css"

interface ActionBarProps {
    selectedLetter: Metadata | null
    preferredLanguage: SpraakKode | null
    onOrderLetter: (language: string) => void
}

const ActionBar: FC<ActionBarProps> = ({selectedLetter, onOrderLetter, preferredLanguage}) => {
    const [selectedLanguage, setSelectedLanguage] = useState(0)
    const disabled = selectedLetter == null
    const selection = selectedLetter?.spraak?.map(spraak => {
        if(spraak === preferredLanguage) {
            return (<option key={spraak}>{sprakTekst(spraak) + " (Foretrukket)"}</option>)
        }
        return (<option key={spraak}>{sprakTekst(spraak)}</option>)
    })

    const handleOrderLetter = () => {
        if (selectedLetter != null && selectedLanguage !== null) {
            onOrderLetter(selectedLetter.spraak[selectedLanguage])
        }
    }
    //TODO split dropdown into new private component.
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