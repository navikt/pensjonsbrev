import React, {FC, FormEvent, useState} from "react"
import {Header} from "@navikt/ds-react-internal"
import styles from "../../pages/velgsak.module.css"
import {Button, TextField} from "@navikt/ds-react"
import {Simulate} from "react-dom/test-utils"

interface SelectSakIdProps {
    onSubmit:(sakId: number)=>void
    error: string | null
}

// TODO should this component be responsible for all sak selection?
const SelectSakid: FC<SelectSakIdProps> = ({onSubmit, error}) => {
    const [saksnummer, setSaksnummer] = useState<number | null>(null)
    const handleInput = (event: FormEvent<HTMLInputElement>) => {
        const formtext = event.currentTarget.value
        if (RegExp("^[0-9]{0,15}$",).test(formtext)) {
            const sakNumber = parseInt(formtext)
            setSaksnummer(sakNumber)
        }
    }
    return (
        <div>
            <Header>
                <Header.Title as="h1">Skribenten</Header.Title>
            </Header>
            <div className={styles.container}>
                <h1 className={styles.header}>Skribenten</h1>
                <TextField value={saksnummer || ""}
                           onInput={handleInput}
                           label={"Saksnummer"}
                           error={error && (<div>${error}</div>)}
                           type="text"/>
                <Button onClick={()=>saksnummer && onSubmit(saksnummer)}>Åpne brevvelger</Button>
            </div>
        </div>
    )
}

export default SelectSakid