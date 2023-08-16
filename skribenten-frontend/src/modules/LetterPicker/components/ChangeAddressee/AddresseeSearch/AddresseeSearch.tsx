import {Button, Radio, RadioGroup, Search, Table, UNSAFE_Combobox} from "@navikt/ds-react"
import {FC, FormEvent, useContext, useState} from "react"
import styles from './AddresseeSearch.module.css'
import {PersonSoekResponse} from "../../../../LetterEditor/model/api"
import {SkribentContext} from "../../../../../pages/brevvelger"
import {Combobox} from "@navikt/ds-react/src/form/combobox"

export type RecipientType = 'PERSON' | 'SAMHANDLER'
export type Place = 'INNLAND' | 'UTLAND'

export type SearchRequest = {
    soeketekst: string,
    recipientType: RecipientType | null,
    place: Place | null,
    kommuneNummer: string | null,
    land: string | null,
}


export type AddressResult = {
    addressName: string,
    addressLines: string[]
}

interface AddresseeSearchProps {
    onMottakerChosen: (pid: string) => void,
}

const AddresseeSearch: FC<AddresseeSearchProps> = ({onMottakerChosen}) => {
    const [showFilter, setShowFilter] = useState(false)
    const [searchText, setSearchText] = useState("")
    const [isSearching, setIsSearching] = useState(false)
    const [latestSearch, setlatestSearch] = useState<SearchRequest | null>(null)
    const [results, setResults] = useState<PersonSoekResponse | null>(null)
    const [recipientType, setRecipientType] = useState<RecipientType | null>(null)
    const [place, setPlace] = useState<Place | null>(null)
    const [errorMessage, setErrorMessage] = useState<string | null>(null)
    const {msal, skribentApi} = useContext(SkribentContext) as SkribentContext

    const handleSearch = () => {
        if (isSearching) return
        if (searchText.length < 3) {
            setErrorMessage("Søk må være minst 3 lang")
            return
        }

        setErrorMessage(null)
        setIsSearching(true)
        const request = {
            recipientType: recipientType,
            place: place,
            soeketekst: searchText,
            kommuneNummer: "0301",
            land: null,
        }
        skribentApi.soekEtterMottaker(msal, request).then(
            res => {
                setResults(res.result)
                setlatestSearch(request)
                setIsSearching(false)
            }
        )
    }

    const handleFormSubmit = (form: FormEvent<HTMLFormElement>) => {
        form.preventDefault()
        handleSearch()
    }

    const error = (errorMessage && <div>{errorMessage}</div>)

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
                        <RadioGroup onChange={setPlace} size="small" legend="Inn-/utland">
                            <Radio value="INNLAND">Innland</Radio>
                            <Radio value="UTLAND">Utland</Radio>
                        </RadioGroup>
                        {place == "INNLAND" &&
                            <UNSAFE_Combobox
                                label="Kommune"
                                options={[]}
                                shouldAutocomplete={true}
                            />
                        }
                    </div>

                </div>
            )}

            {latestSearch && results && results.resultat.length > 0  &&
                <div className={styles.lastSearchedText}>Søkeresultater for “{latestSearch.soeketekst}”</div>}
            {results && results.resultat.length > 0 && <Table size="small">
                <Table.Header>
                    <Table.Row>
                        <Table.HeaderCell scope="col">Navn</Table.HeaderCell>
                        {(latestSearch?.recipientType == "PERSON" &&
                            <Table.HeaderCell scope="col" align="right">Fødselsdato</Table.HeaderCell>)}
                        {(!latestSearch?.recipientType &&
                            <Table.HeaderCell scope="col" align="right">Samhandlertype</Table.HeaderCell>)}
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
                                <Table.DataCell scope="row" align="right">Person</Table.DataCell>)}

                            <Table.DataCell scope="row" align="right">
                                <Button size="xsmall"
                                        variant="secondary"
                                        onClick={() => onMottakerChosen(result.id)}>Velg</Button></Table.DataCell>
                        </Table.Row>
                    ))}
                </Table.Body>
            </Table>
            }
        </div>
    )
}

export default AddresseeSearch