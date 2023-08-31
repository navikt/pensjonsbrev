import React, {createContext, useState} from 'react'
import {NextPage} from "next"
import {SkribentenConfig} from "./_app"

import "@navikt/ds-css"
import "@navikt/ds-css-internal"
import LetterMenu from "../modules/LetterPicker/LetterMenu"
import styles from './brevvelger.module.css'
import LetterPreview from "../modules/LetterPicker/components/LetterPreview/LetterPreview"
import SkribentenAPI from "../lib/services/skribenten"
import {useMsal} from "@azure/msal-react"
import {LetterCategory, LetterMetadata} from "../modules/LetterPicker/model/skribenten"
import LetterPickerActionBar from "../modules/LetterPicker/components/ActionBar/ActionBar"
import SakContext from "../components/casecontextpage/CaseContextPage"

import {Sak} from "../modules/LetterEditor/model/api"
import {IMsalContext} from "@azure/msal-react/dist/MsalContext"
import {LetterSelectionEvent} from "../modules/LetterPicker/components/LetterFilter/Letterfilter"

export type SkribentContext = {
    skribentApi: SkribentenAPI,
    msal: IMsalContext
}

export const SkribentContext = createContext<SkribentContext | null>(null)

export type LetterSelection = {
    metadata: LetterMetadata,
    landkode?: string,
    mottakerText?: string,
}

const Brevvelger: NextPage<SkribentenConfig> = (props) => {
    const [selectedLetter, setSelectedLetter] = useState<LetterSelection | undefined>()
    const [letterCategories, setLetterCategories] = useState<LetterCategory[] | null>(null)
    const [eblanketter, setEblanketter] = useState<LetterMetadata[] | null>(null)
    const [favourites, setFavourites] = useState<LetterMetadata[] | null>(null)
    const [letterMetadata, setLetterMetadata] = useState<LetterMetadata[] | null>()
    const [sak, setSak] = useState<Sak | null>(null)

    const skribentApi = new SkribentenAPI(props.api)
    const msal = useMsal()

    const onChangeSakHandler = (newSak: Sak | null) => {
        setSak(newSak)
        if (newSak) {
            Promise.all([
                skribentApi.getLetterTemplates(msal, newSak.sakType),
                skribentApi.getFavourites(msal),
            ]).then(([letterMetadata, favourites]) => {
                setLetterCategories(letterMetadata.kategorier)
                const metadata = letterMetadata.kategorier.flatMap(cat => cat.templates)
                setLetterMetadata(metadata.concat(letterMetadata.eblanketter))
                setEblanketter(letterMetadata.eblanketter)
                if (favourites) {
                    setFavourites(metadata.filter(m => favourites.includes(m.id)))
                } else {
                    setFavourites([])
                }
            })
        }
    }

    const letterSelectedHandler = (selectionEvent: LetterSelectionEvent) => {
        if (selectionEvent.letterCode === selectedLetter?.metadata.id) {
            setSelectedLetter(undefined)
        } else {
            const selectedLetterMetadata = letterMetadata?.find(el => el.id === selectionEvent.letterCode)
            if(selectedLetterMetadata) {
                setSelectedLetter({metadata: selectedLetterMetadata, landkode: selectionEvent.countryCode, mottakerText: selectionEvent.recipientText})
            }
        }
    }

    const onOrderLetterHandler = (selectedLanguage: string) => {
        if (selectedLetter && sak) {
            const metadata = selectedLetter.metadata
            if (metadata.brevsystem == "EXTERAM") {
                skribentApi.bestillExtreamBrev(msal, selectedLetter, sak, selectedLanguage)
                    .then((url: string) => {
                        console.log(url)
                        window.open(url)
                    })
            } else if (metadata.brevsystem == "DOKSYS") {
                skribentApi.bestillDoksysBrev(msal, metadata.id, sak.sakId.toString(), sak.foedselsnr, selectedLanguage,)
            }
        }
    }

    const addToFavouritesHandler = async () => {
        const metadata = selectedLetter?.metadata
        if (metadata && !metadata.isEblankett) {
            console.log(selectedLetter)
            const isFavourite = favourites?.includes(metadata)
            if (isFavourite) {
                setFavourites(fav => fav ? fav.filter(f => f.id !== metadata.id) : null)
                await skribentApi.removeFavourite(msal, metadata.id)
            } else {
                setFavourites(fav => fav ? [...fav, metadata] : [metadata])
                await skribentApi.addFavourite(msal, metadata.id)
            }
        }
    }

    const selectedLetterIsFavourite = (): boolean => {
        if (selectedLetter && favourites) {
            return favourites.some(f => f.id == selectedLetter.metadata.id)
        } else return false
    }

    return (
        <SkribentContext.Provider value={{skribentApi: skribentApi, msal: msal}}>
            <SakContext onSakChanged={onChangeSakHandler}>
                <div className={styles.outerContainer}>
                    <div className={styles.innterContainer}>
                        <LetterMenu categories={letterCategories}
                                    favourites={favourites}
                                    eblanketter={eblanketter}
                                    onLetterSelected={letterSelectedHandler}
                                    selectedLetter={selectedLetter?.metadata?.id || null}/>
                        <LetterPreview selectedLetter={selectedLetter?.metadata}
                                       selectedIsFavourite={selectedLetterIsFavourite()}
                                       onAddToFavourites={addToFavouritesHandler}/>
                    </div>
                    <LetterPickerActionBar selectedLetter={selectedLetter?.metadata}
                                           onOrderLetter={onOrderLetterHandler}/>
                </div>
            </SakContext>
        </SkribentContext.Provider>
    )
}


export default Brevvelger