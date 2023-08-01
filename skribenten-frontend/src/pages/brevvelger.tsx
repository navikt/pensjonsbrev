import React, {useEffect, useState} from 'react'
import {NextPage} from "next"
import {SkribentenConfig} from "./_app"

import "@navikt/ds-css"
import "@navikt/ds-css-internal"
import LetterFilter from "../modules/LetterPicker/LetterFilter"
import styles from './brevvelger.module.css'
import LetterPreview from "../modules/LetterPicker/components/LetterPreview/LetterPreview"
import SkribentenAPI from "../lib/services/skribenten"
import {useMsal} from "@azure/msal-react"
import {LetterCategory, LetterSelection} from "../modules/LetterPicker/model/skribenten"
import LetterPickerActionBar from "../modules/LetterPicker/components/ActionBar/ActionBar"
import SakContext from "../components/casecontextpage/CaseContextPage"
import {PersonSoekResponse, Sak, SkribentServiceResult} from "../modules/LetterEditor/model/api"
import {SearchRequest} from "../modules/LetterPicker/components/ChangeAddressee/AddresseeSearch/AddresseeSearch"

const Brevvelger: NextPage<SkribentenConfig> = (props) => {
    const [selectedLetter, setSelectedLetter] = useState<LetterSelection | null>(null)
    const [letterCategories, setLetterCategories] = useState<LetterCategory[] | null>(null)
    const [favourites, setFavourites] = useState<LetterSelection[] | null>(null)
    const [letterMetadata, setLetterMetadata] = useState<LetterSelection[] | null>()
    const [sak, setSak] = useState<Sak | null>(null)

    const onChangeSakHandler = (newSak: Sak | null) => {
        setSak(newSak)
        if (newSak) {
            Promise.all([
                skribentApi.getLetterTemplates(msal, newSak.sakType),
                skribentApi.getFavourites(msal),
            ]).then(([categories, favourites]) => {
                setLetterCategories(categories)
                const metadata = categories.flatMap(cat => cat.templates)
                setLetterMetadata(metadata)
                if (favourites) {
                    setFavourites(metadata.filter(m => favourites.includes(m.id)))
                } else {
                    setFavourites([])
                }
            })
        }
    }

    const onSearchForRecipient = (request: SearchRequest): Promise<SkribentServiceResult<PersonSoekResponse>> =>
        skribentApi.searchForRecipient(msal, request)

    const letterSelectedHandler = (id: string | null) => {
        if (id === selectedLetter?.id) {
            setSelectedLetter(null)
        } else {
            const selectedLetterMetadata = letterMetadata?.find(el => el.id === id) || null
            setSelectedLetter(selectedLetterMetadata)
        }
    }

    const onOrderLetterHandler = (selectedLanguage: string) => {
        if (selectedLetter && sak) {
            if (selectedLetter.brevsystem == "EXTERAM") {
                skribentApi.bestillExtreamBrev(msal, selectedLetter.id, sak.sakId.toString(), sak.foedselsnr, selectedLanguage,)
                    .then((url: string) => {
                        console.log(url)
                        window.open(url)
                    })
            } else if (selectedLetter.brevsystem == "DOKSYS") {
                skribentApi.bestillDoksysBrev(msal, selectedLetter.id, sak.sakId.toString(), sak.foedselsnr, selectedLanguage,)
            }

        }
    }
    const skribentApi = new SkribentenAPI(props.api)
    const msal = useMsal()

    const addToFavouritesHandler = async () => {
        if (selectedLetter) {
            const isFavourite = favourites?.includes(selectedLetter)
            if (isFavourite) {
                setFavourites(fav => fav ? fav.filter(f => f.id !== selectedLetter.id) : null)
                await skribentApi.removeFavourite(msal, selectedLetter.id)
            } else {
                setFavourites(fav => fav ? [...fav, selectedLetter] : [selectedLetter])
                await skribentApi.addFavourite(msal, selectedLetter.id)
            }
        }
    }

    const selectedLetterIsFavourite = (): boolean => {
        if (selectedLetter && favourites) {
            return favourites.some(f => f.id == selectedLetter.id)
        } else return false
    }

    //TODO extract universal page (header and background)
    return (<SakContext skribentApi={skribentApi} msal={msal} onSakChanged={onChangeSakHandler}>
        <div className={styles.outerContainer}>
            <div className={styles.innterContainer}>
                <LetterFilter categories={letterCategories}
                              favourites={favourites}
                              onLetterSelected={letterSelectedHandler}
                              onSearchForRecipient={onSearchForRecipient}
                              selectedLetter={selectedLetter?.id || null}/>
                <LetterPreview selectedLetter={selectedLetter}
                               selectedIsFavourite={selectedLetterIsFavourite()}
                               onAddToFavourites={addToFavouritesHandler}/>
            </div>
            <LetterPickerActionBar selectedLetter={selectedLetter}
                                   onOrderLetter={onOrderLetterHandler}/>
        </div>
    </SakContext>)
}


export default Brevvelger