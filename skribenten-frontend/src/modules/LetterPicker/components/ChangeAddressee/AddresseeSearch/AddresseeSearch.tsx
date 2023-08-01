import {Button, Radio, RadioGroup, Search} from "@navikt/ds-react"
import {FC, FormEvent, FormEventHandler, useState} from "react"
import styles from './AddresseeSearch.module.css'
import {PersonSoekResponse, SkribentServiceResult} from "../../../../LetterEditor/model/api"

export type RecipientType = 'PERSON' | 'SAMHANDLER'
export type Place = 'INNLAND' | 'UTLAND'

export type SearchRequest = {
    soeketekst: string,
    recipientType: RecipientType | null,
    place: Place | null,
}

interface AddresseeSearchProps {
    onSearchForRecipient: (request: SearchRequest) => Promise<SkribentServiceResult<PersonSoekResponse>>
}

const AddresseeSearch: FC<AddresseeSearchProps> = ({onSearchForRecipient}) => {
    const [showFilter, setShowFilter] = useState(false)
    const [searchText, setSearchText] = useState("")
    const [results, setResults] = useState<PersonSoekResponse | null>(null)
    const [recipientType, setRecipientType] = useState<RecipientType | null>(null)
    const [place, setPlace] = useState<Place | null>(null)

    const handleSearch = (form: FormEvent<HTMLFormElement>) => {
        form.preventDefault()
        onSearchForRecipient({
            recipientType: recipientType,
            place: place,
            soeketekst: searchText,
        }).then((res) => setResults(res.result))


    }

    return (
        <div className={styles.content}>
            <form role="search" className={styles.searchBar} onSubmit={handleSearch}>
                <Search size="small" label="søk mottaker" onChange={setSearchText}/>
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
                    </div>
                </div>
            )}
            <div>
                {results?.resultat.map(result =>(
                    <div>{result.navn}</div>
                ))}
            </div>
        </div>


    )
}

export default AddresseeSearch