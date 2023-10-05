import React, {ChangeEvent, FC, useContext, useEffect, useState} from "react"
import styles from "./Letterfilter.module.css"
import {Loader, Search, Select, Tabs, TextField} from "@navikt/ds-react"
import LetterCategories from "../LetterCategories/LetterCategories"
import LetterPicker from "../LetterPicker/LetterPicker"
import {LetterCategory, Metadata} from "../../../../lib/model/skribenten"
import {SkribentContext} from "../../../../pages/brevvelger"
import {Avtaleland} from "../ChangeRecipient/RecipientSearch/RecipientSearch"

interface LetterfilterProps {
    categories: LetterCategory[] | null
    eblanketter: Metadata[] | null
    selectedLetter: string | null,
    onLetterSelected: (id: LetterSelectionEvent) => void,
}

export interface LetterSelectionEvent {
    letterCode: string | null,
    countryCode?: string,
    recipientText?: string,
}

function filterCategories(categories: LetterCategory[], searchText: string): LetterCategory[] {
    return categories.map(c => ({
            ...c, templates: c.templates.filter(l => l.name.toLowerCase().includes(searchText.toLowerCase())),
        })
    )
}

const Letterfilter: FC<LetterfilterProps> = ({categories, eblanketter, selectedLetter, onLetterSelected}) => {
    const {skribentApi} = useContext(SkribentContext) as SkribentContext

    const [avtalelandError, setAvtalelandError] = useState<string| null>(null)
    const [mottakerError, setMottakerError] = useState<string| null>(null)

    const [searchFilter, setSearchFilter] = useState("")
    const [expandCategories, setExpandCategories] = useState(false)

    const [avtalelandOptions, setAvtalelandOptions] = useState<Avtaleland[] | null>(null)
    const [avtaleland, setAvtaleland] = useState<string | null>(null)
    const [mottakerText, setMottakerText] = useState<string| null>(null)

    const handleAvtalelandChanged = (event: ChangeEvent<HTMLSelectElement>) =>{
        setAvtalelandError(event ? null : "Vennligst velg avtaleland")
        setAvtaleland(event.target.value)
    }

    const handleMottakerTextChanged = (mottakerText: string) => {
        setMottakerError(mottakerText && mottakerText.length > 0 ? null : "Vennligst skriv inn mottaker")
        setMottakerText(mottakerText)
    }

    useEffect(() => {
        if (eblanketter && eblanketter.length > 0) {
            skribentApi.hentAvtaleland()
                .then(setAvtalelandOptions)
        }
    }, [eblanketter])

    const searchUpdatedHandler = (text: string) => {
        setSearchFilter(text)
        setExpandCategories(text.length > 0)
    }

    const handleEblankettSelected = (id: string) => {
        if (avtaleland && mottakerText && mottakerText.length > 0 && id) {
            setAvtalelandError(null)
            setMottakerError(null)
            onLetterSelected({letterCode: id, countryCode: avtaleland, recipientText: mottakerText})
        }
        setAvtalelandError(avtaleland ? null : "Vennligst velg avtaleland")
        setMottakerError(mottakerText && mottakerText.length > 0 ? null : "Vennligst skriv inn mottaker")
    }

    let letterCategoriesPanel =
        <><Search
            label="Søk alle NAV sine sider"
            variant="simple"
            description={"Test"}
            className={styles.search}
            onChange={searchUpdatedHandler}
            value={searchFilter}
            size="small"/>

            {categories != null ? (
                    <LetterCategories categories={filterCategories(categories, searchFilter)}
                                      expanded={expandCategories}
                                      selectedLetter={selectedLetter}
                                      onLetterSelected={(id) => {
                                          onLetterSelected({letterCode: id})
                                      }}/>)
                : (<Loader/>)}
        </>

    if (eblanketter && eblanketter.length > 0) {
        letterCategoriesPanel =
            <Tabs defaultValue="brevmaler">
                <Tabs.List>
                    <Tabs.Tab value="brevmaler" label="Brevmaler"/>
                    <Tabs.Tab value="eblanketter" label="E-blanketter"/>
                </Tabs.List>
                <Tabs.Panel value="brevmaler">
                    {letterCategoriesPanel}
                </Tabs.Panel>
                <Tabs.Panel value="eblanketter">
                    <div className={styles.eblanketter}>
                        <Select label="Land"
                                size="small"
                                defaultValue={avtaleland || undefined}
                                error={avtalelandError}
                                hideLabel
                                onChange={handleAvtalelandChanged}>
                            <option value="">Velg land</option>
                            {avtalelandOptions && avtalelandOptions
                                ?.map(land => <option key={land.kode} value={land.kode}>{land.navn}</option>)}
                        </Select>

                        <TextField label="Mottaker" size="small" hideLabel
                                   onChange={el => handleMottakerTextChanged(el.target.value)}
                                   value={mottakerText || "" }
                                   error={mottakerError}/>
                        <LetterPicker letters={eblanketter} selectedLetter={selectedLetter}
                                      onLetterSelected={handleEblankettSelected}/>
                    </div>
                </Tabs.Panel>
            </Tabs>
    }
    return <>{letterCategoriesPanel}</>
}

export default Letterfilter