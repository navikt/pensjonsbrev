import React, {FC, FormEvent, useState} from "react"
import styles from "../../pages/velgsak.module.css"
import {Button, TextField} from "@navikt/ds-react"

interface SelectSakIdProps {
    onSubmit:(sakId: number)=>void
    error?: string | null
}

// TODO should this component be responsible for all sak selection?
const SelectSakid: FC<SelectSakIdProps> = ({onSubmit, error = null}) => {
    const [saksnummer, setSaksnummer] = useState<number | null>(null)
    const handleInput = (event: FormEvent<HTMLInputElement>) => {
        const formtext = event.currentTarget.value
        if (SAKID_REGEX.test(formtext)) {
            const sakNumber = parseInt(formtext)
            setSaksnummer(sakNumber)
        }
    }
    return (
        <div className={styles.container}>
            <h1 className={styles.header}>Skribenten</h1>
            <TextField value={saksnummer || ""}
                       onInput={handleInput}
                       label={"Saksnummer"}
                       error={error}
                       type="text"/>
            <Button onClick={()=>saksnummer && onSubmit(saksnummer)}>Åpne brevvelger</Button>
        </div>
    )
}

export const SAKID_REGEX = RegExp("^[0-9]{0,15}$",)

export default SelectSakid