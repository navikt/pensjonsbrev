package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year
import java.time.LocalDate

@Suppress("unused")
data class EndretUfoeretrygdPGAInntektDto(
    val PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus: Kroner,
    val PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus: Kroner,
    val PE_UT_NettoAkk_pluss_NettoRestAr: Kroner,
    val PE_UT_VirkningstidpunktArMinus1Ar: Int,
    val PE: PE,
    val opplysningerBruktIBeregningenLegacyDto: OpplysningerBruktIBeregningenLegacyDto?,
): BrevbakerBrevdata
