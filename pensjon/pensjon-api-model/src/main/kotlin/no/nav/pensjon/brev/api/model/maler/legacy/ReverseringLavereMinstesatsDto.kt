package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class ReverseringLavereMinstesatsDto(
    val lopendeYtelse: LopendeYtelse?,
    val opphortYtelse: OpphortYtelse?,
    val etterbetaling: Kroner,
    val hjemmeltekst: String,
    val pe: PEgruppe10,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
)

data class LopendeYtelse(
    val nettoTotal: Kroner,
    val nettoUforetrygd: Kroner,
    val nettoBarnetillegg: Kroner?,
    val nettoGjenlevendetillegg: Kroner?,
    val reduksjonsprosent: Double,
    val brukersMinstesats: Double,
    val avkortetPgaRedusertTrygdetid: Boolean,
    val harGradertUfoeretrygd: Boolean,
    val endringBt: Boolean = true,
    )

data class OpphortYtelse(
    val opphorsdato: LocalDate
)