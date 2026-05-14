package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

data class VedtakOmIFUReduksjonsprosentData(
    val nettoUforetrygdUtenTillegg: Kroner,
    val nettoBarnetillegg: Kroner?,
    val nettoGjenlevendetillegg: Kroner?,
    val etterbetalingJuli: Kroner,
    val reduksjonsprosent: Double,
    val bunnfradrag: Kroner,
    val inntektstak: Kroner,
    val ifu: Kroner,
    val tillegg: Collection<Tillegg>,
    val endringNettoUforetrygdUtenTillegg: Boolean,
    val endringNettoBarnetillegg: Boolean,
    val endringNettoGjenlevendetillegg: Boolean,
    val endringReduksjonsprosent: Boolean,
    val endringBunnfradrag: Boolean,
    val endringInntektstak: Boolean,
    val endringIfu: Boolean,
    val hjemmeltekst: String,
    val pe: PEgruppe10,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
)
{
    enum class Tillegg(val bokmal: String, val nynorsk: String) { BT("Barnetillegg", "Barnetillegg"), GJT("Gjenlevendetillegg", "Attlevandetillegg") }
}

