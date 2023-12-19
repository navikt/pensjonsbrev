package no.nav.pensjon.etterlatte.maler

import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate
import java.time.Period

data class EtterbetalingDTO(
    val fraDato: LocalDate,
    val tilDato: LocalDate,
    val etterbetalingsperioder: List<Beregningsperiode> = listOf()
)

data class Avkortingsinfo(
    val grunnbeloep: Kroner,
    val inntekt: Kroner,
    val virkningsdato: LocalDate,
    val beregningsperioder: List<AvkortetBeregningsperiode>,
)

enum class EndringIUtbetaling {
    OEKES, REDUSERES, SAMME
}

data class Navn(val fornavn: String, val mellomnavn: String? = null, val etternavn: String)

data class AvkortetBeregningsperiode(
    val datoFOM: LocalDate,
    val datoTOM: LocalDate?,
    val inntekt: Kroner,
    val ytelseFoerAvkorting: Kroner,
    val utbetaltBeloep: Kroner,
    val trygdetid: Int,
)

data class Beregningsinfo(
    override val innhold: List<Element>,
    val grunnbeloep: Kroner,
    val beregningsperioder: List<NyBeregningsperiode>,
    val trygdetidsperioder: List<Trygdetidsperiode>,
) : BrevDTO

data class NyBeregningsperiode(
    val inntekt: Kroner,
    val trygdetid: Int,
    val stoenadFoerReduksjon: Kroner,
    var utbetaltBeloep: Kroner,
)

data class Trygdetidsperiode(
    val datoFOM: LocalDate,
    val datoTOM: LocalDate?,
    val land: String,
    val opptjeningsperiode: Periode?,
)

data class Periode(
    val aar: Int,
    val maaneder: Int,
    val dager: Int,
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
