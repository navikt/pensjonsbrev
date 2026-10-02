package no.nav.pensjon.brev.ufore.api.model.maler.simulering

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Percent
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

/**
 * Simulering (serviceberegning) av uføretrygd.
 *
 * Modellen speiler den gamle JSF-utskriften i pensjon-psak
 * (presentation.simulering.uforetrygd.utskrift + beregningsresultat), som gir bruker en foreløpig,
 * ikke juridisk bindende beregning av hvor mye uføretrygd før skatt hen kan forvente.
 *
 * Beløp er brutto (før skatt).
 *
 * Brevet er redigerbart i Skribenten. Simuleringsresultatet kan ikke hentes på nytt fra PEN i etterkant,
 * så [SimuleringUforetrygdData] sendes inn som statisk fagsystembrevdata når brevet opprettes via
 * Skribentens eksterne API (`statiskFagsystemBrevdata`), og lagres sammen med brevet.
 */
data class SimuleringUforetrygdDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: SimuleringUforetrygdData,
) : RedigerbarBrevdata<SimuleringUforetrygdData>

data class SimuleringUforetrygdData(
    val virkningstidspunkt: LocalDate,
    val aarligBeloep: Kroner,
    val maanedligBeloep: Kroner,
    val grunnbeloep: Kroner,
    val trygdetidAar: Int,
    val uforetidspunkt: LocalDate,
    val uforegrad: Percent,
    val snittInntektTreBesteAvFem: Kroner,
    val yrkesskade: Yrkesskade?,
    val inntektsgrunnlag: List<Inntektsaar>,
) : FagsystemBrevdata {

    data class Yrkesskade(
        val yrkesskadegrad: Percent,
        val inntektPaaSkadetidspunkt: Kroner,
    )

    data class Inntektsaar(
        val aar: Year,
        val pensjonsgivendeInntekt: Kroner,
        val inntektJustertMedGrunnbeloep: Kroner,
        val benyttetIBeregningen: Boolean,
        val merknad: String?,
    )
}
