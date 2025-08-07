package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate
import java.time.Year


@Suppress("unused")
data class EndringPgaOpptjeningAutoDto(
    val virkFom: LocalDate,
    val opptjening: Opptjening,
    val sisteGyldigeOpptjeningsAar: Int,
    val antallAarEndretOpptjening: Int,
    val endretOpptjeningsAar: Set<Int>,
    val belopEndring: String,
    val uforeKombinertMedAlder: Boolean,
    val beregnetPensjonPerMaanedGjeldende: BeregnetPensjonPerMaanedGjeldende,
) : BrevbakerBrevdata

enum class Opptjening {
    TILVEKST,
    KORRIGERING,
}

data class BeregnetPensjonPerMaanedGjeldende(
    val totalPensjon: Kroner,
    val virkFom: LocalDate,
)