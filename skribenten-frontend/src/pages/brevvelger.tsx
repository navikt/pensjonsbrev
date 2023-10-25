import React, {createContext, useState} from 'react'
import {NextPage} from "next"
import {SkribentenConfig} from "./_app"

import "@navikt/ds-css"
import LetterMenu, {Recipient} from "../modules/LetterPicker/LetterMenu"
import styles from './brevvelger.module.css'
import LetterPreview from "../modules/LetterPicker/components/LetterPreview/LetterPreview"
import SkribentenAPI from "../lib/services/skribenten"
import {useMsal} from "@azure/msal-react"
import {ForetrukketSpraakOgMaalForm, LetterCategory, Metadata} from "../lib/model/skribenten"
import LetterPickerActionBar from "../modules/LetterPicker/components/ActionBar/ActionBar"
import SakContext, {Sak} from "../components/casecontextpage/CaseContextPage"

import {LetterSelectionEvent} from "../modules/LetterPicker/components/LetterFilter/Letterfilter"

export type SkribentContext = {
    skribentApi: SkribentenAPI
}

export const SkribentContext = createContext<SkribentContext | null>(null)

export type LetterSelection = {
    metadata: Metadata,
    landkode?: string,
    mottakerText?: string,
}

const Brevvelger: NextPage<SkribentenConfig> = (props) => {
    const [selectedLetter, setSelectedLetter] = useState<LetterSelection | undefined>()
    const [letterCategories, setLetterCategories] = useState<LetterCategory[] | null>(null)
    const [eblanketter, setEblanketter] = useState<Metadata[] | null>(null)
    const [favourites, setFavourites] = useState<Metadata[] | null>(null)
    const [letterMetadata, setLetterMetadata] = useState<Metadata[] | null>()
    const [sak, setSak] = useState<Sak | null>(null)
    const [recipient, setRecipient] = useState<Recipient | null>(null)
    const [recipientOverride, setRecipientOverride] = useState<Recipient | null>(null)
    const [languagePreference, setLanguagePreference] = useState<ForetrukketSpraakOgMaalForm | null>(null)

    const msal = useMsal()
    const skribentApi = new SkribentenAPI(props.api, msal)


    const onChangeSakHandler = (newSak: Sak | null) => {
        setSak(newSak)
        if (newSak && newSak.sakType && newSak.foedselsnr) {
            if (newSak.sakId !== sak?.sakId ) {
                Promise.all([
                    skribentApi.getLetterTemplates(newSak.sakType),
                    skribentApi.getFavourites(),
                    skribentApi.hentForetrukketSpraaklag("29129319060"),
                ]).then(([letterMetadata, favourites, kontaktinfo]) => {
                    setLetterCategories(letterMetadata.kategorier)
                    console.log(kontaktinfo)
                    setLanguagePreference(kontaktinfo.spraakKode)
                    const metadata = letterMetadata.kategorier.flatMap(cat => cat.templates)
                    setLetterMetadata(metadata.concat(letterMetadata.eblanketter))
                    setEblanketter(letterMetadata.eblanketter)
                    if (favourites) {
                        setFavourites(metadata.filter(m => favourites.includes(m.id)))
                    } else {
                        setFavourites([])
                    }
                })

                if (newSak.navn) {
                    setRecipient({
                        recipientName: newSak.navn,
                        addressLines: ["TODO brukers egen adresse linje 1", "TODO brukers egen adresse linje 2"],
                    })
                } else {
                    setRecipient(null)
                }
                // TODO get addresses for the user themselves.
            }
        }

    }

    const letterSelectedHandler = (selectionEvent: LetterSelectionEvent) => {
        if (selectionEvent.letterCode === selectedLetter?.metadata.id) {
            setSelectedLetter(undefined)
        } else {
            const selectedLetterMetadata = letterMetadata?.find(el => el.id === selectionEvent.letterCode)
            if (selectedLetterMetadata) {
                setSelectedLetter({
                    metadata: selectedLetterMetadata,
                    landkode: selectionEvent.countryCode,
                    mottakerText: selectionEvent.recipientText,
                })
            }
        }
    }

    const onOrderLetterHandler = (selectedLanguage: string) => {
        if (selectedLetter && sak && sak.sakId && sak.foedselsnr) {
            const metadata = selectedLetter.metadata
            if (metadata.brevsystem == "EXTERAM") {
                skribentApi.bestillExtreamBrev(selectedLetter.metadata, sak, sak.foedselsnr, selectedLanguage, selectedLetter.landkode, selectedLetter.mottakerText)
                    .then((url: string) => {
                        console.log(`Fikk URL tilbake fra extream ${url}`)
                        window.open(url)
                    })
            } else if (metadata.brevsystem == "DOKSYS") {
                skribentApi.bestillDoksysBrev(metadata.id, sak.sakId.toString(), sak.foedselsnr, selectedLanguage)
                    .then((url: string) => {
                        console.log(`Fikk URL tilbake fra doksys ${url}`)
                        window.open(url)
                    })
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
                await skribentApi.removeFavourite(metadata.id)
            } else {
                setFavourites(fav => fav ? [...fav, metadata] : [metadata])
                await skribentApi.addFavourite(metadata.id)
            }
        }
    }

    const selectedLetterIsFavourite = (): boolean => {
        if (selectedLetter && favourites) {
            return favourites.some(f => f.id == selectedLetter.metadata.id)
        } else return false
    }

    return (
        <SkribentContext.Provider value={{skribentApi: skribentApi}}>
            <SakContext onSakChanged={onChangeSakHandler} sak={sak}>
                <div className={styles.outerContainer}>
                    <div className={styles.innterContainer}>
                        <LetterMenu categories={letterCategories}
                                    favourites={favourites}
                                    eblanketter={eblanketter}
                                    recipient={recipientOverride? recipientOverride : recipient}
                                    onRecipientChanged={setRecipientOverride}
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