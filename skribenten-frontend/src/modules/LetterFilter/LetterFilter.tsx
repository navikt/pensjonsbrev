import React, {FC} from 'react';
import styles from "./LetterFilter.module.css"
import { Button, Search, ExpansionCard} from "@navikt/ds-react";

let somethingTest = [
    "Brev med skjema",
    "Innhente opplysninger",
    "Informasjonsbrev",
    "Notat",
    "Øvrig",
    "Vedtak"
]
interface LetterFilterProps{
}
const LetterFilter: FC<LetterFilterProps> = ({}) => {
    return (
        <div className={styles.brevvelgerContainer}>
            <h1>Brevvelger</h1>
            <div className={styles.mottakerCard}>
                <div>
                    <h2>Mottaker</h2>
                    <Button variant="tertiary" size="xsmall">Endre mottaker</Button>
                </div>
                <p>Test Testerson</p>
            </div>
            <div>
                <h2>Favoritter</h2>
                <hr/>
                <p>Test1234</p>
                <p>Test1234</p>
                <p>Test1234</p>
            </div>
            <div>
                <h2>Brevlisten</h2>
                <hr/>
                <Search
                    label="Søk alle NAV sine sider"
                    variant="simple"
                    description={"Test"}
                    size="small"/>
                    {somethingTest.map(name =>
                        (<ExpansionCard size="small" aria-label="Small-variant" className={styles.letterGroup}>
                            <ExpansionCard.Header>
                                <ExpansionCard.Title as="h4" size="small">{name}</ExpansionCard.Title>
                            </ExpansionCard.Header>
                            <ExpansionCard.Content>
                                <p>Test brev 0</p>
                                <p>Test brev 1</p>
                                <p>Test brev 2</p>
                                <p>Test brev 3</p>
                                <p>Test brev 4</p>
                            </ExpansionCard.Content>
                        </ExpansionCard>)
                    )}
            </div>
        </div>
    )
}

export default LetterFilter