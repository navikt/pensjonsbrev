package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BtBegrunnelseCode
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BarnetilleggUTDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDto
import no.nav.pensjon.brev.fixtures.createDineRettigheterOgPlikterUforeDto
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
            opphortGjenlevendetillegg = true,
            opphoersbegrunnelseEktefelletillegg = EndringUfoeretrygdDto.Opphoersbegrunnelse(
                bruker_flyttet_ikke_avt_land = true,
                eps_flyttet_ikke_avt_land = true,
                eps_opph_ikke_avt_land = true,
                barn_flyttet_ikke_avt_land = true,
                barn_opph_ikke_avt_land = true,
            ),

            oifuVedVirkningstidspunkt = Kroner(10000),

            maanedligUfoeretrygdFoerSkatt = null,
            orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto(),
            dineRettigheterOgPlikterUfore = createDineRettigheterOgPlikterUforeDto(),

            nyeInnvilgedeBarnetillegg = listOf(
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1990, Month.JANUARY, 1), fom = LocalDate.of(1990, Month.JANUARY, 1)),
            ),
            nyeAvslagBarnetillegg = listOf(
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.ANNEN_FORLD_RETT_BT, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2), fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31)),
                BarnetilleggUTDto(antallBarn = 2, begrunnelse = BtBegrunnelseCode.BT_GITT_TIL_ANNEN, fodselsdato = LocalDate.of(1992, Month.MARCH, 3), fom = LocalDate.of(1992, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 3, begrunnelse = BtBegrunnelseCode.MINDRE_ETT_AR_BT_FLT, fodselsdato = LocalDate.of(1990, Month.JANUARY, 1), fom = LocalDate.of(1990, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.BT_INNT_OVER_1G, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2), fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31)),
                BarnetilleggUTDto(antallBarn = 2, begrunnelse = BtBegrunnelseCode.BRK_FORSO_IKKE_BARN, fodselsdato = LocalDate.of(1992, Month.MARCH, 3), fom = LocalDate.of(1992, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 3, begrunnelse = BtBegrunnelseCode.BRUKER_FLYTTET_IKKE_AVT_LAND, fodselsdato = LocalDate.of(1990, Month.JANUARY, 1), fom = LocalDate.of(1990, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.BARN_FLYTTET_IKKE_AVT_LAND, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2), fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31)),
                BarnetilleggUTDto(antallBarn = 2, begrunnelse = BtBegrunnelseCode.BARN_OPPH_IKKE_AVT_LAND, fodselsdato = LocalDate.of(1992, Month.MARCH, 3), fom = LocalDate.of(1992, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 3, begrunnelse = BtBegrunnelseCode.IKKE_MOTTATT_DOK, fodselsdato = LocalDate.of(1992, Month.MARCH, 3), fom = LocalDate.of(1992, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.ANNET_AVSLAG, fodselsdato = LocalDate.of(1992, Month.MARCH, 3), fom = LocalDate.of(1992, Month.JANUARY, 1)),
            ),
            nyeOpphorteBarnetillegg = listOf(
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.OPPHOR_ANNEN_FORLD_RETT_BT, fodselsdato = LocalDate.of(1990, Month.JANUARY, 1), fom = LocalDate.of(1990, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 2, begrunnelse = BtBegrunnelseCode.OPPHOR_BT_GITT_TIL_ANNEN, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2), fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31)),
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.OPPHOR_BT_INNT_OVER_1G, fodselsdato = LocalDate.of(1990, Month.JANUARY, 1), fom = LocalDate.of(1990, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 2, begrunnelse = BtBegrunnelseCode.OPPHOR_BRK_FORSO_IKKE_BARN, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2), fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31)),
                BarnetilleggUTDto(antallBarn = 3, begrunnelse = BtBegrunnelseCode.OPPHOR_BRUKER_FLYTTET_IKKE_AVT_LAND, fodselsdato = LocalDate.of(1992, Month.MARCH, 3), fom = LocalDate.of(1992, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.OPPHOR_BARN_FLYTTET_IKKE_AVT_LAND, fodselsdato = LocalDate.of(1990, Month.JANUARY, 1), fom = LocalDate.of(1990, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 2, begrunnelse = BtBegrunnelseCode.OPPHOR_BARN_OPPH_IKKE_AVT_LAND, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2), fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31)),
                BarnetilleggUTDto(antallBarn = 3, begrunnelse = BtBegrunnelseCode.OPPHOR_IKKE_MOTTATT_DOK, fodselsdato = LocalDate.of(1992, Month.MARCH, 3), fom = LocalDate.of(1992, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.OPPHOR_BT_OVER_18, fodselsdato = LocalDate.of(1992, Month.MARCH, 3), fom = LocalDate.of(1992, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 2, begrunnelse = BtBegrunnelseCode.ANNET_OPPHOR, fodselsdato = LocalDate.of(1992, Month.MARCH, 3), fom = LocalDate.of(1992, Month.JANUARY, 1)),
            ),
            hjemler = setOf("12-8", "12-9", "12-10", "12-11", "12-12", "12-13", "12-14", "12-15", "12-16")
        ),
    )
