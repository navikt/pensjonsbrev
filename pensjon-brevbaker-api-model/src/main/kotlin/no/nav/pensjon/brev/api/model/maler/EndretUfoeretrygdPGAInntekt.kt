package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto

@Suppress("unused")
data class EndretUfoeretrygdPGAInntektDto(
    val pe: PE,
    val opplysningerBruktIBeregningenLegacyDto: OpplysningerBruktIBeregningenLegacyDto?,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
): BrevbakerBrevdata
