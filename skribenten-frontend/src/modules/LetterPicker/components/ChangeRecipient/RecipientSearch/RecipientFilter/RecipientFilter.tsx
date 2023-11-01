import React, {FC} from "react"
import styles from "./RecipientFilter.module.css"
import {Button, Radio, RadioGroup, Select} from "@navikt/ds-react"
import {Location, RecipientType} from "../../../../../../lib/model/skribenten"
import {KommuneResult} from "../RecipientSearch"

export type RecipientSearchFilter = {
    kommunenavn: string | null,
    kommunenummer: number[],
    location: Location | null,
    showFilter: boolean,
    recipientType: RecipientType | null,
}

interface RecipientFilterProps {
    value: RecipientSearchFilter,
    kommuner: KommuneResult[]
    onFilterChanged: (recipientFilter: RecipientSearchFilter) => void
}

const RecipientFilter: FC<RecipientFilterProps> = ({value, onFilterChanged, kommuner}) => {
    const {kommunenavn, location, showFilter, recipientType}: RecipientSearchFilter = value

    const handleKommuneSelected = (option: string) => {
        const kommune = kommuner.find((kommune) => kommune.kommunenavn.toLowerCase() == option.toLowerCase())

        onFilterChanged({
            ...value,
            kommunenummer: kommune?.kommunenummer?.map(parseInt) || [],
            kommunenavn: kommune?.kommunenavn || null,
        })
    }

    return (
        <>
            <Button size="xsmall"
                    variant="secondary-neutral"
                    onClick={() => onFilterChanged({...value, showFilter: !showFilter})}
                    className={styles.filterButton}>Vis filter</Button>

            {showFilter && (
                <div className={styles.filters}>
                    <div className={styles.filterGroup}>
                        <RadioGroup
                            onChange={(recipientType) => onFilterChanged({...value, recipientType: recipientType})}
                            size="small"
                            value={recipientType}
                            legend="Person/samhandler">
                            <Radio value="PERSON">Person</Radio>
                            <Radio value="SAMHANDLER">Samhandler</Radio>
                        </RadioGroup>
                    </div>
                    <div className={styles.filterGroup}>
                        <RadioGroup onChange={(location) => onFilterChanged({...value, location: location})}
                                    value={location}
                                    size="small"
                                    legend="Inn-/utland">
                            <Radio value="INNLAND">Innland</Radio>
                            <Radio value="UTLAND">Utland</Radio>
                        </RadioGroup>
                        {location == "INNLAND" &&
                            <Select
                                label="Kommune"
                                size="small"
                                value={kommunenavn || undefined}
                                onChange={(event: React.ChangeEvent<HTMLSelectElement>) => handleKommuneSelected(event.target.value)}>
                                {kommuner.map(opt => (
                                    <option key={opt.kommunenavn}>{opt.kommunenavn}</option>))}</Select>
                        }
                    </div>
                </div>
            )}
        </>
    )
}

export default RecipientFilter