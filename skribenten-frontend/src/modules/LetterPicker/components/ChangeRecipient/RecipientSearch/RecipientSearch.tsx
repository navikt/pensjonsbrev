import {Button, Search, Table} from "@navikt/ds-react"
import React, {FC, FormEvent, useCallback, useContext, useEffect, useState} from "react"
import styles from './RecipientSearch.module.css'
import {PersonSoekResponse, SearchRequest} from "../../../../../lib/model/skribenten"
import {SkribentContext} from "../../../../../pages/brevvelger"
import {RecipientChange} from "../ChangeRecipient"
import RecipientFilter, {RecipientSearchFilter} from "./RecipientFilter/RecipientFilter"

export type KommuneResult = {
    kommunenummer: string[],
    kommunenavn: string,
}

export type Avtaleland = {
    navn: string,
    kode: string,
}

interface RecipientSearchProps {
    onMottakerChosen: (addressChange: RecipientChange) => void,
}

const RecipientSearch: FC<RecipientSearchProps> = ({onMottakerChosen}) => {
    const [searchText, setSearchText] = useState("")
    const [results, setResults] = useState<PersonSoekResponse | null>(null)

    const [isSearching, setIsSearching] = useState(false)
    const [latestSearch, setlatestSearch] = useState<SearchRequest | null>(null)
    const [errorMessage, setErrorMessage] = useState<string | null>(null)
    const [kommuner, setKommuner] = useState<KommuneResult[]>([])
    const [recipientFilter, setRecipientFilter] = useState<RecipientSearchFilter>({
        recipientType: null,
        location: null,
        showFilter: false,
        kommunenavn: null,
        kommunenummer: [],
    })
    const {skribentApi} = useContext(SkribentContext) as SkribentContext

    useEffect(() => {
        skribentApi.hentKommuneForslag().then(
            res => setKommuner(res))
    }, [skribentApi])

    const handleSearch = useCallback(() => {
        if (isSearching) return
        if (searchText.length < 3) {
            setErrorMessage("Søketeksten må være minst 3 bokstaver lang")
            return
        }

        setErrorMessage(null)
        setIsSearching(true)
        const request: SearchRequest = {
            recipientType: recipientFilter.recipientType,
            location: recipientFilter.location,
            soeketekst: searchText,
            kommunenummer: recipientFilter.kommunenummer,
            land: null,
        }
        skribentApi.soekEtterMottaker(request).then(
            res => {
                setResults(res.result)
                setlatestSearch(request)
                setIsSearching(false)
            }
        )// TODO error handling
    }, [isSearching, recipientFilter, searchText, skribentApi])

    const handleFormSubmit = (form: FormEvent<HTMLFormElement>) => {
        form.preventDefault()
        handleSearch()
    }

    const handleMottakerChosen = (pid: string, navn: string) => {
        skribentApi.hentAdresse(pid).then(result => {
                onMottakerChosen({
                    recipientName: navn,
                    addressContext: "recipientsearch",
                    addressLines: result.adresselinjer,
                })
            }
        )
    }

    const error = (errorMessage && <div>{errorMessage}</div>)

    return (
        <div className={styles.content}>
            <form role="search" className={styles.searchBar} onSubmit={handleFormSubmit}>
                <Search size="small" label="søk mottaker" onChange={setSearchText} error={error}>
                    <Search.Button type="button" loading={isSearching} onClick={handleSearch}/>
                </Search>
            </form>
            <RecipientFilter value={recipientFilter} kommuner={kommuner} onFilterChanged={setRecipientFilter}/>

            {latestSearch && results && results.resultat.length > 0 &&
                <div className={styles.lastSearchedText}>Søkeresultater for “{latestSearch.soeketekst}”</div>}
            {results && results.resultat.length > 0 && <Table size="small">
                <Table.Header>
                    <Table.Row>
                        <Table.HeaderCell scope="col">Navn</Table.HeaderCell>
                        {(latestSearch?.recipientType == "PERSON" &&
                            <Table.HeaderCell scope="col" align="right">Fødselsdato</Table.HeaderCell>)}
                        {(!latestSearch?.recipientType &&
                            <Table.HeaderCell scope="col" align="left">Samhandlertype</Table.HeaderCell>)}
                        <Table.HeaderCell scope="col" align="right"/>
                    </Table.Row>
                </Table.Header>

                <Table.Body>
                    {results?.resultat.map((result) => (
                        <Table.Row key={result.id}>
                            <Table.DataCell scope="row">{result.navn}</Table.DataCell>

                            {(latestSearch?.recipientType == "PERSON" &&
                                <Table.DataCell scope="row" align="right">{result.foedselsdato}</Table.DataCell>)}

                            {(!latestSearch?.recipientType &&
                                <Table.DataCell scope="row" align="left">Person</Table.DataCell>)}

                            <Table.DataCell scope="row" align="right">
                                <Button size="xsmall"
                                        variant="secondary"
                                        onClick={() => handleMottakerChosen(result.id, result.navn)}>Velg</Button></Table.DataCell>
                        </Table.Row>
                    ))}
                </Table.Body>
            </Table>
            }
        </div>
    )
}

export default RecipientSearch