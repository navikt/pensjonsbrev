import type {NextPage} from 'next'
import "@navikt/ds-css"
import "@navikt/ds-css-internal"
import {Header} from "@navikt/ds-react-internal"
import React, {FormEvent, useState} from "react"
import {Button, TextField} from "@navikt/ds-react"
import styles from './velgsak.module.css'

const VelgSak: NextPage = () => {
    const [saksnummer, setSaksnummer] = useState<number | null>(null)
    const handleInput = (event: FormEvent<HTMLInputElement>) => {
        const formtext = event.currentTarget.value
        if (RegExp("^[0-9]{0,15}$",).test(formtext)) {
            setSaksnummer(parseInt(formtext))
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
                           type="text"/>
                <Button>Åpne brevvelger</Button>
            </div>
        </div>
    )
}

export default VelgSak
