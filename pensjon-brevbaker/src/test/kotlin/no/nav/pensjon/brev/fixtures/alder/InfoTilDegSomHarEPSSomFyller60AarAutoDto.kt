package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.InfoTilDegSomHarEPSSomFyller60AarAutoDto


fun createInfoTilDegSomHarEPSSomFyller60AarAutoDto() =
    InfoTilDegSomHarEPSSomFyller60AarAutoDto(
        sakstype = Sakstype.AFP,
    )
