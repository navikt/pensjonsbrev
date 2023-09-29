import React, {FC, useContext, useEffect, useState} from 'react'

import "@navikt/ds-css"
import {Sak, SkribentServiceResult} from "../../modules/LetterEditor/model/api"
import SelectSakid, {SAKID_REGEX} from "../../modules/SelectSakId/SelectSakid"
import {useSearchParams} from "next/navigation"
import {useRouter} from "next/router"
import NavBar from "../navbar/NavBar"
import CaseContextBar from "../casecontextbar/CaseContextBar"
import {SkribentContext} from "../../pages/brevvelger"
import {InternalHeader} from "@navikt/ds-react"

interface CaseContextPageProps {
    children?: React.ReactNode,
    onSakChanged?: (sak: Sak | null) => void,
}


const CaseContextPage: FC<CaseContextPageProps> = ({children, onSakChanged}) => {
    const [sak, setSak] = useState<Sak | null>(null)
    const [navn, setNavn] = useState<string | null>(null)
    const [isLoadingSak, setIsLoadingSak] = useState<boolean>(false)
    const [errorMessage, setErrorMessage] = useState<string | null>()
    const queryParams = useSearchParams()

    const {msal, skribentApi} = useContext(SkribentContext) as SkribentContext

    const router = useRouter()

    const handleSakIdChanged = (sakId: number) => {
        skribentApi.getSaksinfo(msal, sakId).then((response: SkribentServiceResult<Sak>) => {
                if (response.result) {
                    setSak(response.result)
                    if(onSakChanged !== undefined) {
                        onSakChanged(response.result)
                    }
                    skribentApi.hentNavn(msal, response.result.foedselsnr).then(setNavn)
                    setErrorMessage(null)
                    router.push(`${router.basePath}?sakId=${sakId}`, undefined, {shallow: true})
                    setIsLoadingSak(false)
                } else {
                    setIsLoadingSak(false)
                    setErrorMessage(response.errorMessage)
                }
            }
        )
    }
    useEffect(()=>{
        const querySakId = queryParams.get("sakId")

        if(querySakId && SAKID_REGEX.test(querySakId)) {
            setIsLoadingSak(true)
            handleSakIdChanged(parseInt(querySakId))
        }
    },[])

    if (isLoadingSak || sak ) {
        return (
            <div>
                <NavBar/>
                <CaseContextBar saksnummer={sak?.sakId.toString()}
                                foedselsnummer={sak?.foedselsnr}
                                gjelderNavn={navn || undefined} // TODO få inn navn fra pdl
                                foedselsdato={sak?.foedselsdato}
                                sakstype={sak?.sakType} //TODO legg in map for forskjellige sakstyper
                />
                {children}
            </div>
        )
    } else return (
        <div>
            <InternalHeader>
                <InternalHeader.Title as="h1">Skribenten</InternalHeader.Title>
            </InternalHeader>
            <SelectSakid
                error={errorMessage}
                onSubmit={handleSakIdChanged}/>
        </div>
    )
}


export default CaseContextPage