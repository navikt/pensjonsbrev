import React, {useEffect, useState} from 'react'
import {NextPage} from "next"
import {SkribentenConfig} from "./_app"
import {Header} from "@navikt/ds-react-internal"

import "@navikt/ds-css"
import "@navikt/ds-css-internal"
import LetterFilter from "../modules/LetterFilter/LetterFilter"
import styles from './brevvelger.module.css'
import LetterPreview from "../modules/LetterFilter/components/LetterPreview/LetterPreview"
import SkribentenAPI from "../lib/services/skribenten"
import {useMsal} from "@azure/msal-react"
import {LetterCategory, LetterMetadata} from "../modules/LetterFilter/model/skribenten"

const Brevvelger: NextPage<SkribentenConfig> = (props) => {
    const [selectedLetter, setSelectedLetter] = useState<string | null>(null)
    const [letterCategories, setLetterCategories] = useState<LetterCategory[] | null>(null)
    const [favourites, setFavourites] = useState<LetterMetadata[] | null>(null)
    const [letterMetadata, setLetterMetadata] = useState<LetterMetadata[] | null>()


    const letterSelectedHandler = (id: string | null) => setSelectedLetter(selectedLetter === id ? null : id)
    const skribentApi = new SkribentenAPI(props.api)
    const msal = useMsal()

    const addToFavouritesHandler = async () => {
        if (selectedLetter) {
            const selectedLetterMetadata = letterMetadata?.find(el => el.id === selectedLetter)
            if (selectedLetterMetadata) {
                const isFavourite = favourites?.includes(selectedLetterMetadata)
                if (isFavourite) {
                    setFavourites(fav => fav ? fav.filter(f => f.id !== selectedLetter):null)
                    await skribentApi.removeFavourite(msal, selectedLetter)
                } else {
                    setFavourites(fav => fav ? [...fav, selectedLetterMetadata] : [selectedLetterMetadata])
                    await skribentApi.addFavourite(msal, selectedLetter)
                }
            }

        }
    }

    const selectedLetterIsFavourite = (): boolean => {
        if (selectedLetter && favourites) {
            return favourites.some(f => f.id == selectedLetter)
        } else return false
    }


    useEffect(() => {
        Promise.all([
            skribentApi.getLetterTemplates(msal),
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
    }, [])

    //TODO extract universal page (header and background)
    return (
        <div className={styles.outerContainer}>
            <Header>
                <Header.Title as="h1">Skribenten</Header.Title>
                <Header.Button>Brevvelger</Header.Button>
                <Header.Button>Brevbehandler</Header.Button>
                <Header.User name="Test Testerson" className="ml-auto"/>
            </Header>
            <div className={styles.innterContainer}>
                <LetterFilter categories={letterCategories}
                              favourites={favourites}
                              onLetterSelected={letterSelectedHandler}
                              selectedLetter={selectedLetter}/>
                <LetterPreview selectedLetter={selectedLetter}
                               selectedIsFavourite={selectedLetterIsFavourite()}
                               onAddToFavourites={addToFavouritesHandler}/>
            </div>
        </div>
    )
}

export default Brevvelger