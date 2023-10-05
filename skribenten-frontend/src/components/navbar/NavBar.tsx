import React, {FC} from "react"
import {InternalHeader} from "@navikt/ds-react"

const NavBar: FC = () => {
    return (
        <InternalHeader>
            <InternalHeader.Title as="h1">Skribenten</InternalHeader.Title>
            <InternalHeader.Button>Brevvelger</InternalHeader.Button>
            <InternalHeader.Button>Brevbehandler</InternalHeader.Button>
        </InternalHeader>
    )
}

export default NavBar