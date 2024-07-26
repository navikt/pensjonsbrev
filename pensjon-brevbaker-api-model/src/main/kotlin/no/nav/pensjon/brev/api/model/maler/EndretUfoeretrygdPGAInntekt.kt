package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDto

@Suppress("unused")
data class EndretUfoeretrygdPGAInntektDto(
    val pe: PE,
    val opplysningerBruktIBeregningenLegacyDto: OpplysningerBruktIBeregningenLegacyDto?,
): BrevbakerBrevdata
