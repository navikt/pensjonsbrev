import React, {FC, FunctionComponent} from "react"
import styles from "./NavBar.module.css"
import {Header} from "@navikt/ds-react-internal"

interface NavBarProps {

}
const NavBar: FC<NavBarProps> = () => {
    return (
        <Header>
            <Header.Title as="h1">Skribenten</Header.Title>
            <Header.Button>Brevvelger</Header.Button>
            <Header.Button>Brevbehandler</Header.Button>
        </Header>
    )
}

export default NavBar