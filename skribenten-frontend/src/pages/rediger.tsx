import LetterEditor from "../modules/LetterEditor/LetterEditor"
import {NextPage} from "next"
import React, {Dispatch, SetStateAction, useEffect, useState} from "react"
import {initObjectFromSpec, ObjectValue} from "../modules/ModelEditor/model"
import {RedigerbarTemplateDescription} from "../modules/LetterEditor/model/api"
import {useMsal} from "@azure/msal-react"
import SkribentenAPI from "../lib/services/skribenten"
import {SkribentenConfig} from "./_app"
import ModelEditor from "../modules/ModelEditor/ModelEditor"
import styles from "./rediger.module.css"
import Actions from "../modules/LetterEditor/actions"
import {LetterEditorState} from "../modules/LetterEditor/model/state"

const BREVKODE = "INFORMASJON_OM_SAKSBEHANDLINGSTID"

const RedigerBrev: NextPage<SkribentenConfig> = (props) => {
    const api = new SkribentenAPI(props.api)

    const [modelSpec, setModelSpec] = useState<RedigerbarTemplateDescription | null>(null)
    const [modelValue, setModelValue] = useState<ObjectValue>({})
    const [editorState, setEditorState] = useState<LetterEditorState | null>(null)

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
        api.renderLetter(msal, BREVKODE, modelValue, editorState?.editedLetter).then(letter => {
            if(editorState === null) {
                setEditorState(Actions.create(letter))
            } else {
                setEditorState(Actions.updateLetter(editorState, letter))
            }
        })
    }

    const updateState: Dispatch<SetStateAction<LetterEditorState>> = (update) => {
        if (typeof update === 'function') {
            setEditorState((prevState) => {
                if (prevState !== null) {
                    return update(prevState)
                } else {
                    console.error("cannot update letterEditorState: received null prevState ")
                    return null
                }
            })
        } else {
            setEditorState(update)
        }
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
                {editorState !== null ? <LetterEditor editorState={editorState} updateState={updateState}/> : <div/>}
            </div>
        )
    }
}

export default RedigerBrev