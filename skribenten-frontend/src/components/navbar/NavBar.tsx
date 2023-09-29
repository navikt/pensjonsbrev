import React, {FC, FunctionComponent} from "react"
import {InternalHeader} from "@navikt/ds-react"

interface NavBarProps {

}
const NavBar: FC<NavBarProps> = () => {
    return (
        <InternalHeader>
            <InternalHeader.Title as="h1">Skribenten</InternalHeader.Title>
            <InternalHeader.Button>Brevvelger</InternalHeader.Button>
            <InternalHeader.Button>Brevbehandler</InternalHeader.Button>
        </InternalHeader>
    )
}

export default NavBar