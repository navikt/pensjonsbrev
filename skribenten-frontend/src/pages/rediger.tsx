import LetterEditor from "../modules/LetterEditor/LetterEditor"
import {NextPage} from "next"
import React, {useEffect, useState} from "react"
import {initObjectFromSpec, ObjectValue} from "../modules/ModelEditor/model"
import {EditedLetter, RedigerbarTemplateDescription} from "../modules/LetterEditor/model/api"
import {useMsal} from "@azure/msal-react"
import SkribentenAPI from "../lib/services/skribenten"
import {SkribentenConfig} from "./_app"
import ModelEditor from "../modules/ModelEditor/ModelEditor"
import styles from "./rediger.module.css"
import {EditedLetterAction} from "../modules/LetterEditor/actions/letter"

const BREVKODE = "INFORMASJON_OM_SAKSBEHANDLINGSTID"

const RedigerBrev: NextPage<SkribentenConfig> = (props) => {
    const api = new SkribentenAPI(props.api)

    const [modelSpec, setModelSpec] = useState<RedigerbarTemplateDescription | null>(null)
    const [modelValue, setModelValue] = useState<ObjectValue>({})
    const [editedLetter, setEditedLetter] = useState<EditedLetter | null>(null)

    const msal = useMsal()

    // We only want this effect to trigger once, so we don't pass any deps.
    /* eslint-disable react-hooks/exhaustive-deps */
    useEffect(() => {
        api.getRedigerbarTemplateDescription(msal, BREVKODE).then(d => {
            const inited = initObjectFromSpec(d.modelSpecification.types, d.modelSpecification.types[d.modelSpecification.letterModelTypeName])
            setModelValue({...inited, mottattSoeknad: "2023-03-01", ytelse: "alderspensjon"})
            setModelSpec(d)
        })
    }, [])
    /* eslint-enable react-hooks/exhaustive-deps */

    const renderLetter = () => {
        api.renderLetter(msal, BREVKODE, modelValue, editedLetter).then(letter => {
            if(editedLetter === null) {
                setEditedLetter(EditedLetterAction.create(letter))
            } else {
                setEditedLetter(EditedLetterAction.updateLetter(editedLetter, letter))
            }
        })
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
                {editedLetter !== null ? <LetterEditor editedLetter={editedLetter} updateLetter={setEditedLetter}/> : <div/>}
            </div>
        )
    }
}

export default RedigerBrev