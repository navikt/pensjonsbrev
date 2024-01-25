package no.nav.pensjon.etterlatte.maler

import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class BarnepensjonEtterbetaling(
    val fraDato: LocalDate,
    val tilDato: LocalDate,
    val etterbetalingsperioder: List<BarnepensjonBeregningsperiode> = listOf()
)

data class OmstillingsstoenadEtterbetaling(
    val fraDato: LocalDate,
    val tilDato: LocalDate,
    val etterbetalingsperioder: List<OmstillingsstoenadBeregningsperiode> = listOf()
)

data class BarnepensjonBeregning(
    override val innhold: List<Element>,
    val antallBarn: Int,
    val virkningsdato: LocalDate,
    val grunnbeloep: Kroner,
    val beregningsperioder: List<BarnepensjonBeregningsperiode>,
    val sisteBeregningsperiode: BarnepensjonBeregningsperiode,
    val trygdetid: Trygdetid,
) : BrevDTO

data class BarnepensjonBeregningsperiode(
    val datoFOM: LocalDate,
    val datoTOM: LocalDate?,
    val grunnbeloep: Kroner,
    val antallBarn: Int,
    var utbetaltBeloep: Kroner,
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

enum class BeregningsMetode {
    NASJONAL,
    PRORATA,
    BEST
}

enum class EndringIUtbetaling {
    OEKES, REDUSERES, SAMME
}

data class Navn(val fornavn: String, val mellomnavn: String? = null, val etternavn: String)

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

data class Avdoed(
    val navn: String,
    val doedsdato: LocalDate,
)

@Deprecated("Denne utgår når vi får ryddet bort søskenjustering")
data class Utbetalingsinfo(
    val antallBarn: Int,
    val beloep: Kroner,
    val soeskenjustering: Boolean,
    val virkningsdato: LocalDate,
    val beregningsperioder: List<BarnepensjonBeregningsperiode>,
)

@Deprecated("Denne utgår når vi får ryddet opp i omstillingsstønad revurdering")
data class NyBeregningsperiode(
    val inntekt: Kroner,
    val trygdetid: Int,
    val stoenadFoerReduksjon: Kroner,
    var utbetaltBeloep: Kroner,
)

@Deprecated("Denne utgår når vi får ryddet opp i omstillingsstønad revurdering")
data class Beregningsinfo(
    override val innhold: List<Element>,
    val grunnbeloep: Kroner,
    val beregningsperioder: List<NyBeregningsperiode>,
    val trygdetidsperioder: List<Trygdetidsperiode>,
) : BrevDTO
