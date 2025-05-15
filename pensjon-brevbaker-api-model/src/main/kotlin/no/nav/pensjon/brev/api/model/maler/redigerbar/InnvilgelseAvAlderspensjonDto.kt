package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto


@Suppress("unused")
data class InnvilgelseAvAlderspensjonDto(
    override val pesysData: PesysData, override val saksbehandlerValg: EmptyBrevdata
) : RedigerbarBrevdata<EmptyBrevdata, InnvilgelseAvAlderspensjonDto, InnvilgelseAvAlderspensjonDto.PesysData> {
    data class PesysData(
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
    )
}
