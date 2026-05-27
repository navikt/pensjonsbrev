package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class VedtakOmIFUReduksjonsprosentData(
    val beregningFomDato: LocalDate,
    val nettoUforetrygdUtenTillegg: Kroner,
    val nettoBarnetillegg: Kroner?,
    val nettoGjenlevendetillegg: Kroner?,
    val totalbelop: Kroner,
    val etterbetalingJuli: Kroner,
    val reduksjonsprosent: Double,
    val inntektstak: Kroner,
    val ifu: Kroner,
    val tillegg: Collection<UTTillegg>,
    val endringNettoUforetrygdUtenTillegg: Boolean,
    val endringNettoBarnetillegg: Boolean,
    val endringNettoGjenlevendetillegg: Boolean,
    val endringInntektstak: Boolean,
    val erInntektsavkortet: Boolean,
    val hjemler: Set<String>,
    val pe: PEgruppe10,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
)
