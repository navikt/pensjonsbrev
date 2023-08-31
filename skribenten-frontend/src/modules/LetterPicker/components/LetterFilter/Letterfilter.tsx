import React, {FC, useContext, useEffect, useState} from "react"
import styles from "./Letterfilter.module.css"
import {Loader, Search, Select, Tabs, TextField} from "@navikt/ds-react"
import LetterCategories from "../LetterCategories/LetterCategories"
import LetterPicker from "../LetterPicker/LetterPicker"
import {LetterCategory, LetterMetadata} from "../../model/skribenten"
import {SkribentContext} from "../../../../pages/brevvelger"
import {Avtaleland} from "../ChangeAddressee/AddresseeSearch/AddresseeSearch"

interface LetterfilterProps {
    categories: LetterCategory[] | null
    eblanketter: LetterMetadata[] | null
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
    const {msal, skribentApi} = useContext(SkribentContext) as SkribentContext
    const [avtalelandError, setAvtalelandError] = useState<null | string>(null)
    const [mottakerError, setMottakerError] = useState<null | string>(null)

    const [searchFilter, setSearchFilter] = useState("")
    const [expandCategories, setExpandCategories] = useState(false)

    const [avtalelandOptions, setAvtalelandOptions] = useState<Avtaleland[] | null>(null)
    const [avtaleland, setAvtaleland] = useState<string | null>(null)
    const [mottakerText, setMottakerText] = useState<null | string>(null)

    const handleAvtalelandChanged = (avtalelandKode: string) =>{
        setAvtalelandError(avtalelandKode ? null : "Vennligst velg avtaleland")
        setAvtaleland(avtalelandKode)
    }

    const handleMottakerTextChanged = (mottakerText: string) => {
        setMottakerError(mottakerText && mottakerText.length > 0 ? null : "Vennligst skriv inn mottaker")
        setMottakerText(mottakerText)
    }

    useEffect(() => {
        if (eblanketter && eblanketter.length > 0) {
            skribentApi.hentAvtaleland(msal)
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

    let letterPickerPanel =
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
        letterPickerPanel =
            <Tabs defaultValue="brevmaler">
                <Tabs.List>
                    <Tabs.Tab value="brevmaler" label="Brevmaler"/>
                    <Tabs.Tab value="eblanketter" label="E-blanketter"/>
                </Tabs.List>
                <Tabs.Panel value="brevmaler">
                    {letterPickerPanel}
                </Tabs.Panel>
                <Tabs.Panel value="eblanketter">
                    <div className={styles.eblanketter}>
                        <Select label="Land"
                                size="small"
                                error={avtalelandError}
                                hideLabel
                                onChange={event => handleAvtalelandChanged(event.target.value)}>
                            <option value="">Velg land</option>
                            {avtalelandOptions && avtalelandOptions?.map(land => <option key={land.kode}
                                                                                         value={land.kode}>{land.navn}</option>)}
                        </Select>
                        <TextField label="Mottaker" size="small" hideLabel
                                   onChange={el => handleMottakerTextChanged(el.target.value)}
                                   error={mottakerError}/>
                        <LetterPicker letters={eblanketter} selectedLetter={selectedLetter}
                                      onLetterSelected={handleEblankettSelected}/>
                    </div>
                </Tabs.Panel>
            </Tabs>
    }
    return (
        <>
            {letterPickerPanel}
        </>
    )
}

export default Letterfilter