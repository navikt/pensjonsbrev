package no.nav.pensjon.etterlatte.maler

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BeregningsMetode
import java.time.LocalDate

data class Etterbetaling(
    val fraDato: LocalDate,
    val tilDato: LocalDate,
    val etterbetalingsperioder: List<Beregningsperiode> = listOf()
)

data class OmstillingsstoenadEtterbetaling(
    val fraDato: LocalDate,
    val tilDato: LocalDate,
    val beregningsperioder: List<OmstillingsstoenadBeregningsperiode> = listOf()
)

data class OmstillingsstoenadBeregning(
    override val innhold: List<Element>,
    val virkningsdato: LocalDate,
    val inntekt: Kroner,
    val grunnbeloep: Kroner,
    val beregningsperioder: List<OmstillingsstoenadBeregningsperiode>,
    val sisteBeregningsperiode: OmstillingsstoenadBeregningsperiode,
    val trygdetid: Trygdetid,
) : BrevDTO

data class OmstillingsstoenadBeregningsperiode(
    val datoFOM: LocalDate,
    val datoTOM: LocalDate?,
    val inntekt: Kroner,
    val ytelseFoerAvkorting: Kroner,
    val utbetaltBeloep: Kroner,
    val trygdetid: Int,
)

data class Trygdetid(
    val trygdetidsperioder: List<Trygdetidsperiode>,
    val beregnetTrygdetidAar: Int,
    val beregnetTrygdetidMaaneder: Int,
    val prorataBroek: IntBroek?,
    val beregningsMetodeAnvendt: BeregningsMetode,
    val beregningsMetodeFraGrunnlag: BeregningsMetode,
    val mindreEnnFireFemtedelerAvOpptjeningstiden: Boolean,
)

enum class EndringIUtbetaling {
    OEKES, REDUSERES, SAMME
}

data class Navn(val fornavn: String, val mellomnavn: String? = null, val etternavn: String)

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
    val type: TrygdetidType,
)

enum class TrygdetidType {
    FREMTIDIG,
    FAKTISK
}

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
