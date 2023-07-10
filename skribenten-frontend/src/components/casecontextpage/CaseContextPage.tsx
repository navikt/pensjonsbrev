import React, {FC, useEffect, useState} from 'react'

import "@navikt/ds-css"
import "@navikt/ds-css-internal"
import {Sak, SkribentServiceResult} from "../../modules/LetterEditor/model/api"
import SkribentenAPI from "../../lib/services/skribenten"
import SelectSakid, {SAKID_REGEX} from "../../modules/SelectSakId/SelectSakid"
import {IMsalContext} from "@azure/msal-react/dist/MsalContext"
import {useSearchParams} from "next/navigation"
import {Header} from "@navikt/ds-react-internal"
import {useRouter} from "next/router"
import NavBar from "../navbar/NavBar"
import CaseContextBar from "../casecontextbar/CaseContextBar"


interface CaseContextPageProps {
    children?: React.ReactNode,
    skribentApi: SkribentenAPI,
    msal: IMsalContext,
    onSakChanged?: (sak: Sak | null) => void,
}

const CaseContextPage: FC<CaseContextPageProps> = ({children, skribentApi, msal, onSakChanged}) => {
    const [sak, setSak] = useState<Sak | null>(null)
    const [navn, setNavn] = useState<string | null>(null)
    // TODO
    const [isLoadingSak, setIsLoadingSak] = useState<boolean>(false)
    const [errorMessage, setErrorMessage] = useState<string | null>()
    const queryParams = useSearchParams()

    const router = useRouter()

    const handleSakIdChanged = (sakId: number) => {
        skribentApi.getSaksinfo(msal, sakId).then((response: SkribentServiceResult<Sak>) => {
                if (response.result) {
                    setSak(response.result)
                    if(onSakChanged !== undefined) {
                        onSakChanged(response.result)
                    }
                    skribentApi.hentNavn(msal, response.result.foedselsnr).then(setNavn) // TODO kommenter inn når tilgang til PDL er i orden.
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

    //TODO extract universal page (header and background)
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
            <Header>
                <Header.Title as="h1">Skribenten</Header.Title>
            </Header>

            <SelectSakid
                error={errorMessage}
                onSubmit={handleSakIdChanged}/>
        </div>
    )
}


export default CaseContextPage