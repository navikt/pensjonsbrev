import React, {FC, useState} from 'react'
import styles from "./LetterFilter.module.css"
import {Button, Heading, Loader, Search} from "@navikt/ds-react"
import LetterCategories from "./components/LetterCategories/LetterCategories"
import LetterPicker from "./components/LetterPicker/LetterPicker"
import {LetterCategory, LetterSelection} from "./model/skribenten"
import ChangeAddressee from "./components/ChangeAddressee/ChangeAddressee"

export interface LetterFilterProps {
    categories: LetterCategory[] | null
    favourites: LetterSelection[] | null
    selectedLetter: string | null,
    onLetterSelected: (id: string | null) => void,
}

function filterCategories(categories: LetterCategory[], searchText: string): LetterCategory[] {
    return categories.map(c => ({
            ...c, templates: c.templates.filter(l => l.name.toLowerCase().includes(searchText.toLowerCase())),
        })
    )
}

const LetterFilter: FC<LetterFilterProps> = ({categories, favourites, selectedLetter, onLetterSelected}) => {
    const [searchFilter, setSearchFilter] = useState("")
    const [expandCategories, setExpandCategories] = useState(false)
    const [changeAddresseeIsOpen, setChangeAddresseeIsOpen] = useState(false)

    const searchUpdatedHandler = (text: string) => {
        setSearchFilter(text)
        setExpandCategories(text.length > 0)
    }

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
                                                     onLetterSelected={onLetterSelected}/>)
                    : (<Loader/>)}
            </div>
            <div>
                <h2 className={styles.sectionHeading}>Brevlisten</h2>
                <hr/>
                <Search
                    label="Søk alle NAV sine sider"
                    variant="simple"
                    description={"Test"}
                    onChange={searchUpdatedHandler}
                    size="small"/>

                {categories != null ? (<LetterCategories categories={filterCategories(categories, searchFilter)}
                                                         expanded={expandCategories}
                                                         selectedLetter={selectedLetter}
                                                         onLetterSelected={onLetterSelected}/>)
                    : (<Loader/>)}
            </div>
        </div>
    )
}

export default LetterFilter