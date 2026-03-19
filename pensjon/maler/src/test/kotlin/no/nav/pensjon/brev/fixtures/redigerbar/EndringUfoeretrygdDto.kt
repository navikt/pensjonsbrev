package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BarnetilleggResultatCode
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BarnetilleggUTDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDto
import no.nav.pensjon.brev.fixtures.createOrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate
import java.time.Month
import kotlin.collections.listOf

fun createEndringUfoeretrygdDto() =
    EndringUfoeretrygdDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = EndringUfoeretrygdDto.PesysData(
            pe = createPEgruppe10(),
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

            oifuVedVirkningstidspunkt = Kroner(10000),

            maanedligUfoeretrygdFoerSkatt = null,
            orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto(),

            barnetilleggInnvilget = listOf(
                BarnetilleggUTDto(LocalDate.of(1990, Month.JANUARY, 1), BarnetilleggResultatCode.INNVILGET),
                BarnetilleggUTDto(LocalDate.of(1991, Month.FEBRUARY, 2), BarnetilleggResultatCode.INNVILGET),
                BarnetilleggUTDto(LocalDate.of(1992, Month.MARCH, 3), BarnetilleggResultatCode.INNVILGET),
            ),
            barnetilleggAvslatt = listOf(
                BarnetilleggUTDto(LocalDate.of(1990, Month.APRIL, 4), BarnetilleggResultatCode.BT_INNT_OVER_1G),
                BarnetilleggUTDto(LocalDate.of(1991, Month.MAY, 5), BarnetilleggResultatCode.BT_GITT_TIL_ANNEN),
                BarnetilleggUTDto(LocalDate.of(1992, Month.JUNE, 5), BarnetilleggResultatCode.BARN_FLYTTET_IKKE_AVT_LAND)
            ),
        ),
    )
