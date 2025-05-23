package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto


@Suppress("unused")
data class InnvilgelseAvAlderspensjonDto(
    override val pesysData: PesysData,
    override val saksbehandlerValg,
) : RedigerbarBrevdata<InnvilgelseAvAlderspensjonDto.SaksbehandlerValg, InnvilgelseAvAlderspensjonDto.PesysData> {

    data class SaksbehandlerValg(
        val abc: String,
    )

    data class PesysData(
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
    )

    data class AlderspensjonVedVirk(
        val uttaksgrad: Int,
        val regelverkType: AlderspensjonRegelverkType,
    )

    data class DineRettigheterOgMulighet

    data class EktefelletilleggVedVirk(
        val innvilegetEktefelletillegg: Boolean,

    )
}
