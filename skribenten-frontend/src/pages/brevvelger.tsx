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
import {Sak, SkribentServiceResult} from "../modules/LetterEditor/model/api"
import SelectSakid from "../modules/SelectSakId/SelectSakid"
import {useRouter} from "next/router"

const Brevvelger: NextPage<SkribentenConfig> = (props) => {
    const [selectedLetter, setSelectedLetter] = useState<LetterSelection | null>(null)
    const [letterCategories, setLetterCategories] = useState<LetterCategory[] | null>(null)
    const [favourites, setFavourites] = useState<LetterSelection[] | null>(null)
    const [letterMetadata, setLetterMetadata] = useState<LetterSelection[] | null>()
    const [sak, setSak] = useState<Sak | null>(null)
    const [errorMessage, setErrorMessage] = useState<string | null>()
    const queryParams = useSearchParams()
    const sakId = queryParams.get("sakId") || null // TODO handle empty / invalid sakid
    const router = useRouter()

    const letterSelectedHandler = (id: string | null) => {
        if (id === selectedLetter?.id) {
            setSelectedLetter(null)
        } else {
            const selectedLetterMetadata = letterMetadata?.find(el => el.id === id) || null
            setSelectedLetter(selectedLetterMetadata)
        }
    }

    const onOrderLetterHandler = (selectedLanguage: string) => {
        if (selectedLetter) {
            skribentApi.bestillExtreamBrev(
                msal,
                selectedLetter.id,
                selectedLanguage
            ).then((value: string) => {
                console.log(value)
            })
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

    useEffect(() => {
        sakId && skribentApi.getSaksinfo(msal, sakId).then((response: SkribentServiceResult<Sak>) => {
                if (response.result) {
                    setSak(response.result)
                } else {
                    console.log(response.errorMessage)
                    setErrorMessage(response.errorMessage)
                }
            }
        )
    }, [sakId])

    //TODO extract universal page (header and background)
    if (sakId && !errorMessage) {
        return (<div className={styles.outerContainer}>
                <NavBar/>
                <CaseContextBar saksnummer={sak?.sakId.toString()}
                                foedselsnummer={sak?.foedselsnr}
                                gjelderNavn={"TODO test testerson"} // TODO få inn navn fra pdl
                                foedselsdato={sak?.foedselsdato}
                                sakstype={sak?.sakType} //TODO legg in map for forskjellige sakstyper
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
    } else return (
        <SelectSakid onSubmit={(sakId: number) => router.push(`brevvelger?sakId=${sakId}`,
            undefined,
            {shallow: true})}/>
    )
}


export default Brevvelger