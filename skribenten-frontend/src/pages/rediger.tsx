import LetterEditor from "../modules/LetterEditor/LetterEditor"
import {NextPage} from "next"
import React, {useEffect, useState} from "react"
import {initObjectFromSpec, ObjectValue} from "../modules/ModelEditor/model"
import {RedigerbarTemplateDescription, RenderedLetter} from "../modules/LetterEditor/model/api"
import {useMsal} from "@azure/msal-react"
import SkribentenAPI from "../lib/services/skribenten"
import {SkribentenConfig} from "./_app"
import ModelEditor from "../modules/ModelEditor/ModelEditor"
import styles from "./rediger.module.css"

const BREVKODE = "INFORMASJON_OM_SAKSBEHANDLINGSTID"

const RedigerBrev: NextPage<SkribentenConfig> = (props) => {
    const api = new SkribentenAPI(props.api)

    const [modelSpec, setModelSpec] = useState<RedigerbarTemplateDescription | null>(null)
    const [modelValue, setModelValue] = useState<ObjectValue>({})
    const [letter, setLetter] = useState<RenderedLetter | null>(null)

    const msal = useMsal()

    useEffect(() => {
        api.getRedigerbarTemplateDescription(msal, BREVKODE).then(d => {
            setModelValue(initObjectFromSpec(d.modelSpecification.types, d.modelSpecification.types[d.modelSpecification.letterModelTypeName]))
            setModelSpec(d)
        })
    }, [])

    const renderLetter = () => {
        api.renderLetter(msal, BREVKODE, modelValue, letter).then(setLetter)
    }

    if (modelSpec === null) {
        return (<div>Laster mal...</div>)
    } else {
        return (
            <div className={styles.container}>
                <div>
                    <ModelEditor spec={modelSpec.modelSpecification} value={modelValue} updateValue={setModelValue}/>
                    <button type="button" onClick={renderLetter}>Oppdater variabler</button>
                </div>
                {letter !== null ? <LetterEditor letter={letter} updateLetter={setLetter}/> : <div/>}
            </div>
        )
    }
}

export default RedigerBrev