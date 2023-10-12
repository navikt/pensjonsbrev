import React, {FC, useState} from 'react'
import styles from "./LetterMenu.module.css"
import {Button, Heading, Loader} from "@navikt/ds-react"
import LetterPicker from "./components/LetterPicker/LetterPicker"
import {LetterCategory, Metadata} from "../../lib/model/skribenten"
import ChangeRecipient from "./components/ChangeRecipient/ChangeRecipient"
import Letterfilter, {LetterSelectionEvent} from "./components/LetterFilter/Letterfilter"

export interface LetterMenuProps {
    categories: LetterCategory[] | null,
    favourites: Metadata[] | null,
    eblanketter: Metadata[] | null,
    selectedLetter: string | null,
    onLetterSelected: (letterSelectionEvent: LetterSelectionEvent) => void,
}

const LetterMenu: FC<LetterMenuProps> =
    ({
         categories,
         favourites,
         eblanketter,
         selectedLetter,
         onLetterSelected,
     }) => {
        const [showRecipientModal, setShowRecipientModal] = useState(false)

        return (
            <div className={styles.brevvelgerContainer}>
                <h1>Brevvelger</h1>
                <div className={styles.mottakerCard}>
                    <div>
                        <Heading level="2" size="small" className={styles.mottakerCardHeading}>Mottaker</Heading>
                        <Button variant="tertiary"
                                size="xsmall"
                                onClick={() => setShowRecipientModal(true)}>Endre mottaker</Button>
                        <ChangeRecipient open={showRecipientModal}
                                         onExit={() => setShowRecipientModal(false)}/>
                    </div>
                    <p>Test Saksbehandlerson</p>
                </div>
                <div>
                    <h2 className={styles.sectionHeading}>Favoritter</h2>
                    <hr/>
                    {favourites != null ? (<LetterPicker letters={favourites}
                                                         selectedLetter={selectedLetter}
                                                         onLetterSelected={id => {onLetterSelected({letterCode: id})}}
                        />)
                        : (<Loader/>)}
                </div>
                <Letterfilter categories={categories}
                              eblanketter={eblanketter}
                              selectedLetter={selectedLetter}
                              onLetterSelected={onLetterSelected}/>
            </div>
        )
    }

export default LetterMenu