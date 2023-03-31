import React, {useEffect, useState} from 'react'
import {NextPage} from "next"
import {SkribentenConfig} from "./_app"
import { Header } from "@navikt/ds-react-internal"

import "@navikt/ds-css"
import "@navikt/ds-css-internal"
import LetterFilter from "../modules/LetterFilter/LetterFilter"
import styles from './brevvelger.module.css'
import LetterPreview from "../modules/LetterFilter/components/LetterPreview/LetterPreview"
import SkribentenAPI from "../lib/services/skribenten"
import {LetterTemplateInfo} from "../modules/LetterFilter/model/skribenten"
import {useMsal} from "@azure/msal-react"

const Brevvelger:NextPage<SkribentenConfig> = (props) => {
    const [selectedLetter, setSelectedLetter] = useState<string | null>(null)
    const [ letterTemplateInfo, setLetterTemplateInfo ] = useState<LetterTemplateInfo | null>(null)
    const letterSelectedHandler = (id: string | null) => setSelectedLetter(selectedLetter === id ? null : id)
    const api = new SkribentenAPI(props.api)
    const msal = useMsal()

    useEffect(()=>{
        api.getLetterTemplates(msal).then(templates => {
            setLetterTemplateInfo(templates)
            //TODO error handling with message
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
                <LetterFilter letterTemplateInfo={letterTemplateInfo}
                              onLetterSelected={letterSelectedHandler}
                              selectedLetter={selectedLetter}/>
                <LetterPreview selectedLetter={selectedLetter}/>
            </div>
        </div>
    )
}

export default Brevvelger