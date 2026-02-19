package no.nav.pensjon.brev.alder.model.aldersovergang

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.alder.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner
import java.time.LocalDate


data class VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto(
    val virkFom: LocalDate,
    val alderspensjonVedVirk: AlderspensjonVedVirk,
    val beregnetPensjonPerMaaned: BeregnetPensjonPerMaaned,
    val avdod: Avdod,
    val bruker: Bruker,
    val inngangOgEksportVurdering: InngangOgEksportVurdering,
    val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
    val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto,
    val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
    val maanedligPensjonFoerSkattAP2025Dto: MaanedligPensjonFoerSkattAP2025Dto?,
) : AutobrevData {
    data class AlderspensjonVedVirk(
        val totalPensjon: Kroner,
        val uttaksgrad: Int,
        val gjenlevenderettAnvendt: Boolean,
        val erEksportBeregnet: Boolean,
        val regelverkType: AlderspensjonRegelverkType,
        val innvilgetFor67: Boolean?,
        val godkjentYrkesskade: Boolean?,
        val pensjonstilleggInnvilget: Boolean?,
        val garantipensjonInnvilget: Boolean?,
    )
    data class BeregnetPensjonPerMaaned(
        val antallBeregningsperioderPensjon: Int,
    )
    data class Avdod(
        val navn: String,
        val avdodFnr: String?
    )
    data class Bruker(
        val faktiskBostedsland: String,
        val borINorge: Boolean,
    )
    data class InngangOgEksportVurdering(
        val minst20ArTrygdetid: Boolean?,
        val eksportTrygdeavtaleAvtaleland: Boolean?,
        val eksportTrygdeavtaleEOS: Boolean?,
        val eksportBeregnetUtenGarantipensjon: Boolean?,
    )

    data class BeregnetPensjonPerManedVedVirk(
        val gjenlevendetillegg: Int?
    )
}
