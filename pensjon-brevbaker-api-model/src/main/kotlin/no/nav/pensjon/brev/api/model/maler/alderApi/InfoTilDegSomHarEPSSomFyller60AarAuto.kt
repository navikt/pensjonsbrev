package no.nav.pensjon.brev.api.model.maler.alderApi


import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata


@Suppress("unused")
data class InfoTilDegSomHarEPSSomFyller60AarAutoDto(
    val sakstype: Sakstype
) : BrevbakerBrevdata
