package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.RegelverkType
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


@Suppress("unused")
data class AvslagFoer67AarDto(
    val avtalelandErEOSLand: Boolean,
    val avtalelandNavn: String?,
    val brukerBorINorge: Boolean,
    val kravDato: LocalDate,
    val regelverkType: RegelverkType,
    val simulertAlderspensjonVedVirk: SimulertAlderspensjonVedVirk
): BrevbakerBrevdata {
    data class SimulertAlderspensjonVedVirk(
        val minstePensjonsNivaa: Kroner,
        val privatAFPErBrukt: Boolean,
        val proRataErBrukt: Boolean,
        val totalPensjonMedAFP: Kroner?,
        val uttaksgrad: Int
    )
}
