package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


@Suppress("unused")
data class EndringPgaOpptjeningAutoDto(
    val virkFom: LocalDate,
    val opptjeningType: OpptjeningType,
    val opptjening: Opptjening,
    val belopEndring: BeloepEndring,
    val uforeKombinertMedAlder: Boolean,
    val beregnetPensjonPerMaanedGjeldende: BeregnetPensjonPerMaanedGjeldende,
    val beregnetPensjonPerMaaned: BeregnetPensjonPerMaaned,
    val beregnetPensjonPerMaanedVedVirk: BeregnetPensjonPerMaanedVedVirk,
    val regelverkType: AlderspensjonRegelverkType,
    val erFoerstegangsbehandling: Boolean,
    val borINorge: Boolean,
) : BrevbakerBrevdata

enum class OpptjeningType {
    TILVEKST,
    KORRIGERING,
}

data class Opptjening(
    val sisteGyldigeOpptjeningsAar: Int?,
    val antallAarEndretOpptjening: Int,
    val endretOpptjeningsAar: Set<Int>,
)

data class BeregnetPensjonPerMaaned(
    val antallBeregningsperioderPensjon: Int,
)

data class BeregnetPensjonPerMaanedGjeldende(
    val totalPensjon: Kroner,
    val virkFom: LocalDate,
)

data class BeregnetPensjonPerMaanedVedVirk(
    val totalPensjon: Kroner,
    val virkFom: LocalDate,
    val pensjonstilleggInnvilget: Boolean,
    val minstenivaPensjonistParInnvilget: Boolean,
    val minstenivaIndividuellInnvilget: Boolean,
    val gjenlevenderettAnvendt: Boolean,
    val garantipensjonInnvilget: Boolean,
    val uttaksgrad: Int,
)