package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto

data class EndringUfoeretrygdDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
    ) : RedigerbarBrevdata<EmptySaksbehandlerValg, EndringUfoeretrygdDto.PesysData> {
    data class PesysData(
        val pe: PE,

        val opphortEktefelletillegg: Boolean,
        val opphortBarnetillegg: Boolean,
        val opphortGjenlevendetillegg: Boolean,

        val bt_innt_over_1g: Boolean,
        val bt_over_18: Boolean,
        val annen_forld_rett_bt: Boolean,
        val mindre_ett_ar_bt_flt: Boolean,
        val opphoersbegrunnelse: Opphoersbegrunnelse,

        val antallBarnOpphor: Int,

        val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
        val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
    ) : FagsystemBrevdata

    data class Opphoersbegrunnelse(
        val bruker_flyttet_ikke_avt_land: Boolean,
        val eps_flyttet_ikke_avt_land: Boolean,
        val eps_opph_ikke_avt_land: Boolean,
        val barn_flyttet_ikke_avt_land: Boolean,
        val barn_opph_ikke_avt_land: Boolean,
    )
}