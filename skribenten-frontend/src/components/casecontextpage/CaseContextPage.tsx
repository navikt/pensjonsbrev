import React, {FC, useCallback, useContext, useEffect, useState} from 'react'

import "@navikt/ds-css"
import {SakDto, SakType, SkribentServiceResult} from "../../lib/model/skribenten"
import SelectSakid, {SAKID_REGEX} from "../../modules/SelectSakId/SelectSakid"
import {useSearchParams} from "next/navigation"
import {useRouter} from "next/router"
import NavBar from "../navbar/NavBar"
import CaseContextBar from "../casecontextbar/CaseContextBar"
import {SkribentContext} from "../../pages/brevvelger"
import {InternalHeader} from "@navikt/ds-react"

interface CaseContextPageProps {
    children?: React.ReactNode,
    onSakChanged: (sak: Sak | null) => void,
    sak: Sak | null,
}

export type Sak = {
    readonly sakId: number | undefined,
    readonly foedselsnr: string | undefined,
    readonly foedselsdato: string | undefined,
    readonly navn: string | undefined,
    readonly sakType: SakType | undefined,
}


const CaseContextPage: FC<CaseContextPageProps> = ({children, onSakChanged, sak}) => {
    const [isLoadingSak, setIsLoadingSak] = useState<boolean>(false)
    const [errorMessage, setErrorMessage] = useState<string | null>()
    const queryParams = useSearchParams()

    const {skribentApi} = useContext(SkribentContext) as SkribentContext

    const router = useRouter()

    const handleSakSubmitted = useCallback((sakId: number) => {
        router.push(`${router.basePath}?sakId=${sakId}`, undefined, {shallow: true})
    }, [router])

    useEffect(() => {
        const querySakId = queryParams.get("sakId")

        if (querySakId && SAKID_REGEX.test(querySakId) && parseInt(querySakId) !== sak?.sakId) {
            setIsLoadingSak(true)
            skribentApi.getSaksinfo(parseInt(querySakId)).then((response: SkribentServiceResult<SakDto>) => {
                    const sakResponse = response.result
                    if (sakResponse !== null) {
                        const newSak = {
                            sakId: sakResponse?.sakId,
                            navn: sak?.navn,
                            sakType: sakResponse.sakType,
                            foedselsdato: sakResponse.foedselsdato,
                            foedselsnr: sakResponse.foedselsnr,
                        }
                        if (onSakChanged !== undefined) {
                            onSakChanged(newSak)
                            if (newSak?.foedselsnr !== sak?.foedselsnr) {
                                skribentApi.hentNavn(sakResponse.foedselsnr).then(navn => {
                                        if (sak) {
                                            onSakChanged({...sak, navn: navn})
                                        } else {
                                            onSakChanged({
                                                ...newSak,
                                                navn: navn,
                                            })
                                        }
                                    }
                                )
                            }
                        }
                        setErrorMessage(null)
                        setIsLoadingSak(false)
                    } else {
                        setIsLoadingSak(false)
                        setErrorMessage(response.errorMessage)
                    }
                }
            )
        } else {
            setErrorMessage(`Ugyldig saksnummer: ${querySakId}`)
        }
    }, [skribentApi, queryParams, sak, onSakChanged])

    if (isLoadingSak || sak) {
        return (
            <div>
                <NavBar/>
                <CaseContextBar saksnummer={sak?.sakId?.toString()}
                                foedselsnummer={sak?.foedselsnr}
                                gjelderNavn={sak?.navn} // TODO få inn navn fra pdl
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
                onSubmit={handleSakSubmitted}/>
        </div>
    )
}


export default CaseContextPage