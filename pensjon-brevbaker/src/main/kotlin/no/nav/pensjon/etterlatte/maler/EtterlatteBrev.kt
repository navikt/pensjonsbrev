package no.nav.pensjon.etterlatte.maler

import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class BarnepensjonEtterbetaling(
    val inneholderKrav: Boolean?,
    val frivilligSkattetrekk: Boolean?,
    val etterbetalingPeriodeValg: EtterbetalingPeriodeValg?,
)

enum class EtterbetalingPeriodeValg {
    UNDER_3_MND,
    FRA_3_MND,
}

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
    val bruktTrygdetid: Trygdetid, // Fra og med siste åpne periode
    val trygdetid: List<Trygdetid>,
    val erForeldreloes: Boolean = false,
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
    val beregningsperioder: List<OmstillingsstoenadBeregningsperiode>,
    val sisteBeregningsperiode: OmstillingsstoenadBeregningsperiode,
    val trygdetid: Trygdetid,
) : BrevDTO

data class OmstillingsstoenadBeregningsperiode(
    val datoFOM: LocalDate,
    val datoTOM: LocalDate?,
    val inntekt: Kroner,
    val aarsinntekt: Kroner,
    val fratrekkInnAar: Kroner,
    val relevantMaanederInnAar: Int,
    val grunnbeloep: Kroner,
    val ytelseFoerAvkorting: Kroner,
    val restanse: Kroner,
    val utbetaltBeloep: Kroner,
    val trygdetid: Int,
    val sanksjon: Boolean,
)

data class Trygdetid(
    val navnAvdoed: String,
    val trygdetidsperioder: List<Trygdetidsperiode>,
    val beregnetTrygdetidAar: Int,
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

enum class FeilutbetalingType {
    FEILUTBETALING_UTEN_VARSEL,
    FEILUTBETALING_MED_VARSEL,
    INGEN_FEILUTBETALING
}

data class IntBroek(val teller: Int, val nevner: Int)
