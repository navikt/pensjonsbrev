package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto

data class EndringUfoeretrygdFlyttingUtlandDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
    ) : RedigerbarBrevdata<EmptySaksbehandlerValg, EndringUfoeretrygdFlyttingUtlandDto.PesysData> {
    data class PesysData(
        val pe: PEgruppe10,

        val opphortEktefelletillegg: Boolean,
        val opphortBarnetillegg: Boolean,

        val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?
    ) : FagsystemBrevdata
}