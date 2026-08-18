package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto

data class EndringUfoeretrygdFlyttingUtlandDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
    ) : RedigerbarBrevdataMedSaksbehandlerValg<EndringUfoeretrygdFlyttingUtlandDto.PesysData> {
    data class PesysData(
        val pe: PEgruppe10,

        val opphortEktefelletillegg: Boolean,
        val opphortBarnetillegg: Boolean,

        val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?
    ) : FagsystemBrevdata
}