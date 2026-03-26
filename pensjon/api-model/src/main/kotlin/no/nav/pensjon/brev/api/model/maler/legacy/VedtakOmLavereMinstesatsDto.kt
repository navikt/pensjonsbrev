package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

data class VedtakOmLavereMinstesatsDto(
    val nettoUforetrygdUtenTillegg: Kroner,
    val nettoBarnetillegg: Kroner?,
    val nettoGjenlevendetillegg: Kroner?,
    val egenopptjentUforetrygd: Kroner,
    val reduksjonsprosent: Double,
    val harMinstesats: Boolean,
    val tidligereMinstesats: Kroner,
    val nyMinstesats: Kroner,
    val harRedusertTrygdetid: Boolean,
    val harGradertUfoeretrygd: Boolean,
    val tillegg: Collection<Tillegg>,
    val endringNettoUforetrygdUtenTillegg: Boolean,
    val endringNettoBarnetillegg: Boolean,
    val endringNettoGjenlevendetillegg: Boolean,
    val endringReduksjonsprosent: Boolean,
    val hjemmeltekst: String,
    //for vedlegg
    val pe: PEgruppe10,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto
): AutobrevData

enum class Tillegg(val bokmal: String, val nynorsk: String) { BT("Barnetillegg", "Barnetillegg"), GJT("Gjenlevendetillegg", "Gjenlevandetillegg") }