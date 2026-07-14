package no.nav.pensjon.brev.ufore.api.model.maler.simulering

import no.nav.pensjon.brev.api.model.maler.AutobrevData
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
 */
data class SimuleringUforetrygdDto(
    val virkningstidspunkt: LocalDate,
    val aarligBeloep: Kroner,
    val maanedligBeloep: Kroner,
    val grunnbeloep: Kroner,
    val trygdetidAar: Int,
    val uforetidspunkt: LocalDate,
    val uforegrad: Percent,
    val snittInntektTreBesteAvFem: Kroner,
    val yrkesskade: Yrkesskade? = null,
    val inntektsgrunnlag: List<Inntektsaar>,
) : AutobrevData {

    data class Yrkesskade(
        val yrkesskadegrad: Percent,
        val inntektPaaSkadetidspunkt: Kroner,
    )

    data class Inntektsaar(
        val aar: Year,
        val pensjonsgivendeInntekt: Kroner,
        val inntektJustertMedGrunnbeloep: Kroner,
        val benyttetIBeregningen: Boolean,
        val merknad: String? = null,
    )
}
