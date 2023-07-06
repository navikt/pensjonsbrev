package no.nav.pensjon.etterlatte.maler

import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


data class Avkortingsinfo(
    val grunnbeloep: Kroner,
    val inntekt: Kroner,
    val virkningsdato: LocalDate,
    val beregningsperioder: List<AvkortetBeregningsperiode>
)

enum class EndringIUtbetaling {
    OEKES, REDUSERES, SAMME
}

data class Navn(val fornavn: String, val mellomnavn: String? = null, val etternavn: String)

data class AvkortetBeregningsperiode(
    val datoFOM: LocalDate,
    val datoTOM: LocalDate?,
    val inntekt: Kroner,
    val utbetaltBeloep: Kroner
)
data class Utbetalingsinfo(
    val antallBarn: Int,
    val beloep: Kroner,
    val soeskenjustering: Boolean,
    val virkningsdato: LocalDate,
    val beregningsperioder: List<Beregningsperiode>,
)

data class Beregningsperiode(
    val datoFOM: LocalDate,
    val datoTOM: LocalDate?,
    val grunnbeloep: Kroner,
    val antallBarn: Int,
    var utbetaltBeloep: Kroner,
)

data class Avdoed(
    val navn: String,
    val doedsdato: LocalDate,
)
