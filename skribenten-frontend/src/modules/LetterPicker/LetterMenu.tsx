import React, {FC, useState} from 'react'
import styles from "./LetterMenu.module.css"
import {Button, Heading, Loader, Search, Tabs} from "@navikt/ds-react"
import LetterPicker from "./components/LetterPicker/LetterPicker"
import {LetterCategory, LetterMetadata} from "./model/skribenten"
import ChangeAddressee from "./components/ChangeAddressee/ChangeAddressee"
import Letterfilter, {LetterSelectionEvent} from "./components/LetterFilter/Letterfilter"

export interface LetterMenuProps {
    categories: LetterCategory[] | null
    favourites: LetterMetadata[] | null
    eblanketter: LetterMetadata[] | null
    selectedLetter: string | null,
    onLetterSelected: (id: LetterSelectionEvent) => void,
}

const LetterMenu: FC<LetterMenuProps> =
    ({
         categories,
         favourites,
         eblanketter,
         selectedLetter,
         onLetterSelected,
     }) => {
        const [changeAddresseeIsOpen, setChangeAddresseeIsOpen] = useState(false)

        return (
            <div className={styles.brevvelgerContainer}>
                <h1>Brevvelger</h1>
                <div className={styles.mottakerCard}>
                    <div>
                        <Heading level="2" size="small" className={styles.mottakerCardHeading}>Mottaker</Heading>
                        <Button variant="tertiary"
                                size="xsmall"
                                onClick={() => setChangeAddresseeIsOpen(true)}>Endre mottaker</Button>
                        <ChangeAddressee open={changeAddresseeIsOpen}
                                         onExit={() => setChangeAddresseeIsOpen(false)}/>
                    </div>
                    <p>Test Saksbehandlerson</p>
                </div>
                <div>
                    <h2 className={styles.sectionHeading}>Favoritter</h2>
                    <hr/>
                    {favourites != null ? (<LetterPicker letters={favourites} selectedLetter={selectedLetter}
                                                         onLetterSelected={id => id && {id: id}}/>)
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