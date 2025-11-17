package no.nav.pensjon.brev.alder.model.aldersovergang

import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.alder.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


data class VedtakAldersovergang67AarGarantitilleggAutoDto(
    val virkFom: LocalDate,
    val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
    val alderspensjonVedVirk: AlderspensjonVedVirk,
    val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto,
    val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
    val maanedligPensjonFoerSkattAP2025Dto: MaanedligPensjonFoerSkattAP2025Dto?,
) : AutobrevData

data class BeregnetPensjonPerManedVedVirk(
    val garantitillegg: Kroner,
)

data class AlderspensjonVedVirk(
    val totalPensjon: Kroner,
    val uttaksgrad: Int,
)
