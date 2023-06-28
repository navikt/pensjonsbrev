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
import NavBar from "../components/navbar/NavBar"
import CaseContextBar from "../components/casecontextbar/CaseContextBar"
import LetterPickerActionBar from "../modules/LetterPicker/components/ActionBar/ActionBar"
import {useSearchParams} from "next/navigation"

const Brevvelger: NextPage<SkribentenConfig> = (props) => {
    const [selectedLetter, setSelectedLetter] = useState<LetterSelection | null>(null)
    const [letterCategories, setLetterCategories] = useState<LetterCategory[] | null>(null)
    const [favourites, setFavourites] = useState<LetterSelection[] | null>(null)
    const [letterMetadata, setLetterMetadata] = useState<LetterSelection[] | null>()


    const letterSelectedHandler = (id: string | null) => {
        if(id === selectedLetter?.id) {
            setSelectedLetter(null)
        } else {
            const selectedLetterMetadata = letterMetadata?.find(el => el.id === id) || null
            setSelectedLetter(selectedLetterMetadata)
        }
    }

    const onOrderLetterHandler = (selectedLanguage: string) => {
        if(selectedLetter) {
            skribentApi.bestillExtreamBrev(
                msal,
                selectedLetter.id,
                selectedLanguage
            ).then((value: string)=> {console.log(value)})
        }
    }
    const skribentApi = new SkribentenAPI(props.api)
    const msal = useMsal()
    const queryParams = useSearchParams()
    const sakId = queryParams.get("sakId") || "" // TODO handle empty / invalid sakid

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


    useEffect(() => {
        Promise.all([
            skribentApi.getLetterTemplates(msal),
            skribentApi.getFavourites(msal),
            skribentApi.getSaksinfo(msal, sakId),
        ]).then(([categories, favourites, response]) => {
            setLetterCategories(categories)
            const metadata = categories.flatMap(cat => cat.templates)
            setLetterMetadata(metadata)
            if (favourites) {
                setFavourites(metadata.filter(m => favourites.includes(m.id)))
            } else {
                setFavourites([])
            }
        })
    }, [])

    //TODO extract universal page (header and background)
    return (
        <div className={styles.outerContainer}>
            <NavBar/>
            <CaseContextBar saksnummer={sakId || "1234123"}
                            foedselsnummer={"0101190012345"}
                            gjelderNavn={"Test Testerson"}
                            foedselsdato={"01.01.1900"}
                            sakstype={"Uføretrygd"}
            />
            <div className={styles.innterContainer}>
                <LetterFilter categories={letterCategories}
                              favourites={favourites}
                              onLetterSelected={letterSelectedHandler}
                              selectedLetter={selectedLetter?.id || null}/>
                <LetterPreview selectedLetter={selectedLetter}
                               selectedIsFavourite={selectedLetterIsFavourite()}
                               onAddToFavourites={addToFavouritesHandler}/>
            </div>
            <LetterPickerActionBar selectedLetter={selectedLetter}
                                   onOrderLetter={onOrderLetterHandler}/>
        </div>
    )
}


export default Brevvelger