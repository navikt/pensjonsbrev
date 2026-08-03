package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgPlikterUforeDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

data class VedtakOmEtterbetalingOpphor2026AutoDto(
    val etterbetaling: Kroner,
    val hjemler: Set<String>,
    val reduksjonsprosent: Double,
    val uforegrad: Int,
    val ifu: Kroner,
    val endringUforegrad: Boolean,
    val endringIfu: Boolean,
    val pe: PEgruppe10,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val dineRettigheterOgPlikterUfore: DineRettigheterOgPlikterUforeDto,
) : AutobrevData
