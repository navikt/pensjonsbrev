import React, {FC, useState} from 'react';
import styles from "./LetterFilter.module.css"
import {Button, Heading, Search} from "@navikt/ds-react";
import LetterCategories from "./components/LetterCategories/LetterCategories";
import LetterPicker from "./components/LetterPicker/LetterPicker";

export interface LetterFilterProps {
    favourites: LetterMetaData[]
    categories: LetterCategory[]
}

export type LetterCategory = {
    name: string,
    letters: LetterMetaData[]
}
export type LetterMetaData = {
    id: string,
    name: string,
}

function filterCategories(categories: LetterCategory[], searchText: string): LetterCategory[] {
    return categories.map(c => ({
            ...c, letters: c.letters.filter(l => l.name.toLowerCase().includes(searchText.toLowerCase()))
        })
    )
}

const LetterFilter: FC<LetterFilterProps> = ({favourites, categories}) => {
    const [searchFilter, setSearchFilter] = useState("")
    const [expandCategories, setExpandCategories] = useState(false)

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
                    <Button variant="tertiary" size="xsmall">Endre mottaker</Button>
                </div>
                <p>Test Testerson</p>
            </div>
            <div>
                <h2 className={styles.sectionHeading}>Favoritter</h2>
                <hr/>
                <LetterPicker letters={favourites}/>
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

                <LetterCategories categories={filterCategories(categories, searchFilter)}
                                  expanded={expandCategories}/>
            </div>
        </div>
    )
}

export default LetterFilter