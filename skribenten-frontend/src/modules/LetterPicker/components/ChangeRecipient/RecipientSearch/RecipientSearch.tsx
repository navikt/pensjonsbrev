import {Button, Radio, RadioGroup, Search, Select, Table} from "@navikt/ds-react"
import React, {FC, FormEvent, useCallback, useContext, useEffect, useState} from "react"
import styles from './RecipientSearch.module.css'
import {PersonSoekResponse, RecipientType, SearchRequest, Location} from "../../../../../lib/model/skribenten"
import {SkribentContext} from "../../../../../pages/brevvelger"

export type AddressResult = {
    addressName: string,
    addressLines: string[]
}

export type KommuneResult = {
    kommunenummer: string[],
    kommunenavn: string,
}

export type Avtaleland = {
    navn: string,
    kode: string,
}

interface RecipientSearchProps {
    onMottakerChosen: (pid: string) => void,
}

const RecipientSearch: FC<RecipientSearchProps> = ({onMottakerChosen}) => {
    const [showFilter, setShowFilter] = useState(false)
    const [searchText, setSearchText] = useState("")
    const [isSearching, setIsSearching] = useState(false)
    const [latestSearch, setlatestSearch] = useState<SearchRequest | null>(null)
    const [results, setResults] = useState<PersonSoekResponse | null>(null)
    const [recipientType, setRecipientType] = useState<RecipientType | null>(null)
    const [location, setLocation] = useState<Location | null>(null)
    const [errorMessage, setErrorMessage] = useState<string | null>(null)
    const [kommuner, setKommuner] = useState<KommuneResult[]>([])
    const [selectedKommune, setSelectedKommune] = useState<string[] | null>(null)
    const {skribentApi} = useContext(SkribentContext) as SkribentContext

    useEffect(() => {
        skribentApi.hentKommuneForslag().then(
            res => setKommuner(res))
    }, [])

    const handleSearch = useCallback(() => {
        if (isSearching) return
        if (searchText.length < 3) {
            setErrorMessage("Søketeksten må være minst 3 bokstaver lang")
            return
        }

        setErrorMessage(null)
        setIsSearching(true)
        const request: SearchRequest = {
            recipientType: recipientType,
            location: location,
            soeketekst: searchText,
            kommunenummer: selectedKommune,
            land: null,
        }
        skribentApi.soekEtterMottaker(request).then(
            res => {
                setResults(res.result)
                setlatestSearch(request)
                setIsSearching(false)
            }
        )// TODO error handling
    },[isSearching, location, recipientType, searchText, selectedKommune, skribentApi])

    const handleFormSubmit = (form: FormEvent<HTMLFormElement>) => {
        form.preventDefault()
        handleSearch()
    }

    const handleKommuneSelected = (option: string) => {
        const kommune = kommuner.find((value) => value.kommunenavn.toLowerCase() == option.toLowerCase())
        setSelectedKommune(kommune?.kommunenummer || null)
        console.log(kommune)
    }

    const handleMottakerChosen = (pid: string ) => {
        console.log(skribentApi.hentAdresse(pid))
    }

    const error = (errorMessage && <div>{errorMessage}</div>)

    // TODO split out filters into a component? It's getting stuffy in here.
    return (
        <div className={styles.content}>
            <form role="search" className={styles.searchBar} onSubmit={handleFormSubmit}>
                <Search size="small" label="søk mottaker" onChange={setSearchText} error={error}>
                    <Search.Button type="button" loading={isSearching} onClick={handleSearch}/>
                </Search>
            </form>
            <Button size="xsmall"
                    variant="secondary-neutral"
                    onClick={() => setShowFilter(!showFilter)}
                    className={styles.filterButton}>Vis filter</Button>

            {showFilter && (
                <div className={styles.filters}>
                    <div className={styles.filterGroup}>
                        <RadioGroup onChange={setRecipientType} size="small" legend="Person/samhandler">
                            <Radio value="PERSON">Person</Radio>
                            <Radio value="SAMHANDLER">Samhandler</Radio>
                        </RadioGroup>
                    </div>
                    <div className={styles.filterGroup}>
                        <RadioGroup onChange={setLocation} size="small" legend="Inn-/utland">
                            <Radio value="INNLAND">Innland</Radio>
                            <Radio value="UTLAND">Utland</Radio>
                        </RadioGroup>
                        {location == "INNLAND" &&
                            <Select
                                label="Kommune"
                                size="small"
                                onChange={(event: React.ChangeEvent<HTMLSelectElement>) => handleKommuneSelected(event.target.value)}>
                                {kommuner.map(opt => (<option key={opt.kommunenavn}>{opt.kommunenavn}</option>))}</Select>
                        }
                    </div>
                </div>
            )}

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
                                        onClick={() => handleMottakerChosen(result.id)}>Velg</Button></Table.DataCell>
                        </Table.Row>
                    ))}
                </Table.Body>
            </Table>
            }
        </div>
    )
}

export default RecipientSearch