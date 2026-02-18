package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmInnvilgelseAvOmsorgspoengDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.fixtures.createOrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.fixtures.createPE

fun createEndringUfoeretrygdDto() =
    EndringUfoeretrygdDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = EndringUfoeretrygdDto.PesysData(
            pe = createPE(),
            opphortEktefelletillegg = true,
            opphortBarnetillegg = true,
            opphortGjenlevendetillegg = true,
            bt_innt_over_1g = true,
            bt_over_18 = true,
            annen_forld_rett_bt = true,
            mindre_ett_ar_bt_flt = true,
            opphoersbegrunnelse = EndringUfoeretrygdDto.Opphoersbegrunnelse(
                bruker_flyttet_ikke_avt_land = true,
                eps_flyttet_ikke_avt_land = true,
                eps_opph_ikke_avt_land = true,
                barn_flyttet_ikke_avt_land = true,
                barn_opph_ikke_avt_land = true,
            ),
            antallBarnOpphor = 1,

            maanedligUfoeretrygdFoerSkatt = null,
            orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto()
        ),
    )
