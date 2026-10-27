package no.nav.pensjon.brev.api.model.maler.ufoerApi.felles

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class Beregning(
    val virkFom: LocalDate,
    val uforegrad: Int,
    val uforetidspunkt: LocalDate,
    val utbetalingsgrad: Int,

    val totalNetto: Kroner,
    val belopHarOkt: Boolean,
    val beregningsresultatTilRevurderingTotalNetto: Kroner?,

    val bunnfradrag: Kroner,
    val bunnfradragNesteAr: Kroner,
    val reduksjonsprosent: Double,

    val yrkesskadegrad: Int?,
    val yrkesskadeBest: Boolean,
    val inntektSkadetidspunkt: Kroner?,

    val grunnbelop: Kroner,

    val anvendtTrygdetid: Int
)