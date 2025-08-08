package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


@Suppress("unused")
data class EndringPgaOpptjeningAutoDto(
    val virkFom: LocalDate,
    val opptjening: Opptjening,
    val sisteGyldigeOpptjeningsAar: Int,
    val antallAarEndretOpptjening: Int,
    val endretOpptjeningsAar: Set<Int>,
    val belopEndring: String,
    val uforeKombinertMedAlder: Boolean,
    val beregnetPensjonPerMaanedGjeldende: BeregnetPensjonPerMaaned,
    val beregnetPensjonPerMaaned: BeregnetPensjonPerMaaned,
    val beregnetPensjonPerMaanedVedVirk: BeregnetPensjonPerMaaned,
    val regelverkType: AlderspensjonRegelverkType,
    val erFoerstegangsbehandling: Boolean,
) : BrevbakerBrevdata

enum class Opptjening {
    TILVEKST,
    KORRIGERING,
}

data class BeregnetPensjonPerMaaned(
    val totalPensjon: Kroner,
    val virkFom: LocalDate,
    val antallBeregningsperioderPensjon: Int,
    val pensjonstilleggInnvilget: Boolean,
    val minstenivaPensjonistParInnvilget: Boolean,
    val minstenivaIndividuellInnvilget: Boolean,
    val gjenlevenderettAnvendt: Boolean,
    val garantipensjonInnvilget: Boolean,
)