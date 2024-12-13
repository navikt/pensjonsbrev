package no.nav.pensjon.brev.api.model.maler


import no.nav.pensjon.brev.api.model.Sakstype


@Suppress("unused")
data class InfoTilDegSomHarEPSSomFyller60AarAutoDto(
    val sakstype: Sakstype
) : BrevbakerBrevdata
