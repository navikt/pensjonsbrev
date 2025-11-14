package no.nav.pensjon.brev.alder.model.sivilstand

import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.alder.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class EndringAvAlderspensjonGarantitilleggDto(
    override val pesysData: PesysData,
    override val saksbehandlerValg: EmptySaksbehandlerValg,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, EndringAvAlderspensjonGarantitilleggDto.PesysData> {
    data class PesysData(
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
        val kravVirkDatoFom: LocalDate, // v3.Krav
        val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
        val maanedligPensjonFoerSkattAP2025Dto: MaanedligPensjonFoerSkattAP2025Dto?,
        val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto,
    ) : BrevbakerBrevdata

    data class AlderspensjonVedVirk(
        val innvilgetFor67: Boolean,
        val ufoereKombinertMedAlder: Boolean,
        val uttaksgrad: Int,
    )

    data class BeregnetPensjonPerManedVedVirk(
        val garantitillegg: Kroner?, // beregnetPensjonPerManedVedVirk <- v1.ALderspensjon
        val totalPensjon: Kroner, // beregnetPensjonPerManedVedVirk
    )
}
