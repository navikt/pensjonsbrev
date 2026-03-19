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
                BarnetilleggUTDto(resultat = BarnetilleggResultatCode.INNVILGET, fodselsdato = LocalDate.of(1990, Month.JANUARY, 1), fom = LocalDate.of(1990, Month.JANUARY, 1)),
                BarnetilleggUTDto(resultat = BarnetilleggResultatCode.INNVILGET, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2), fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31)),
                BarnetilleggUTDto(resultat = BarnetilleggResultatCode.INNVILGET, fodselsdato = LocalDate.of(1992, Month.MARCH, 3), fom = LocalDate.of(1992, Month.JANUARY, 1)),
            ),
            barnetilleggAvslatt = listOf(
                BarnetilleggUTDto(resultat = BarnetilleggResultatCode.BT_INNT_OVER_1G, fodselsdato = LocalDate.of(1990, Month.APRIL, 1), fom = LocalDate.of(1990, Month.JANUARY, 1)),
                BarnetilleggUTDto(resultat = BarnetilleggResultatCode.BT_GITT_TIL_ANNEN, fodselsdato = LocalDate.of(1991, Month.MAY, 2), fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31)),
                BarnetilleggUTDto(resultat = BarnetilleggResultatCode.BARN_FLYTTET_IKKE_AVT_LAND, fodselsdato = LocalDate.of(1992, Month.JUNE, 3), fom = LocalDate.of(1992, Month.JANUARY, 1)),
            ),
        ),
    )
