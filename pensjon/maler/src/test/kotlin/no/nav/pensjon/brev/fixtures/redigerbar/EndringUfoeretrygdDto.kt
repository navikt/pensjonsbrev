package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BarnDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BarnetilleggMedSammeBegrunnelsePaSammeTidDto
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
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = EndringUfoeretrygdDto.PesysData(
            pe = createPEgruppe10(),
            kravFremsattDato = LocalDate.of(2020, Month.JANUARY, 1),
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
            avslagBarnetilleggNye = listOf(
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(begrunnelse = BtBegrunnelseCode.ANNEN_FORLD_RETT_BT, fom = LocalDate.of(2020, Month.JANUARY, 1), tom = LocalDate.of(2020, Month.MARCH, 31),
                    barn = listOf(BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2)))),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(begrunnelse = BtBegrunnelseCode.BT_GITT_TIL_ANNEN, fom = LocalDate.of(2021, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 2, fodselsdato = LocalDate.of(1992, Month.MARCH, 3)))),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(begrunnelse = BtBegrunnelseCode.MINDRE_ETT_AR_BT_FLT, fom = LocalDate.of(2022, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 3, fodselsdato = LocalDate.of(1990, Month.JANUARY, 1)))),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(begrunnelse = BtBegrunnelseCode.BT_INNT_OVER_1G, fom = LocalDate.of(2023, Month.JANUARY, 1), tom = LocalDate.of(2023, Month.MARCH, 31),
                    barn = listOf(BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2)))),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(begrunnelse = BtBegrunnelseCode.BRK_FORSO_IKKE_BARN, fom = LocalDate.of(2024, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 2, fodselsdato = LocalDate.of(1992, Month.MARCH, 3)))),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(begrunnelse = BtBegrunnelseCode.BRUKER_FLYTTET_IKKE_AVT_LAND, fom = LocalDate.of(2025, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 3, fodselsdato = LocalDate.of(1990, Month.JANUARY, 1)))),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(begrunnelse = BtBegrunnelseCode.BARN_FLYTTET_IKKE_AVT_LAND, fom = LocalDate.of(2026, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31),
                    barn = listOf(BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2)))),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(begrunnelse = BtBegrunnelseCode.BARN_OPPH_IKKE_AVT_LAND, fom = LocalDate.of(2025, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 2, fodselsdato = LocalDate.of(1992, Month.MARCH, 3)))),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(begrunnelse = BtBegrunnelseCode.IKKE_MOTTATT_DOK, fom = LocalDate.of(2024, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 3, fodselsdato = LocalDate.of(1992, Month.MARCH, 3)), BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1996, Month.DECEMBER, 3)))),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(begrunnelse = BtBegrunnelseCode.ANNET_AVSLAG, fom = LocalDate.of(2023, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1992, Month.MARCH, 3)))),
            ),
            opphorteBarnetilleggNye = listOf(
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(begrunnelse = BtBegrunnelseCode.OPPHOR_ANNEN_FORLD_RETT_BT, fom = LocalDate.of(2020, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1990, Month.JANUARY, 1)))),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(begrunnelse = BtBegrunnelseCode.OPPHOR_BT_GITT_TIL_ANNEN, fom = LocalDate.of(2021, Month.JANUARY, 1), tom = LocalDate.of(2021, Month.MARCH, 31),
                    barn = listOf(BarnDto(antallBarn = 2, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2)))),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(begrunnelse = BtBegrunnelseCode.OPPHOR_BT_INNT_OVER_1G, fom = LocalDate.of(2022, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1990, Month.JANUARY, 1)))),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(begrunnelse = BtBegrunnelseCode.OPPHOR_BRK_FORSO_IKKE_BARN, fom = LocalDate.of(2023, Month.JANUARY, 1), tom = LocalDate.of(2023, Month.MARCH, 31),
                    barn = listOf(BarnDto(antallBarn = 2, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2)))),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(begrunnelse = BtBegrunnelseCode.OPPHOR_BRUKER_FLYTTET_IKKE_AVT_LAND, fom = LocalDate.of(2024, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 3, fodselsdato = LocalDate.of(1992, Month.MARCH, 3)))),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(begrunnelse = BtBegrunnelseCode.OPPHOR_BARN_FLYTTET_IKKE_AVT_LAND, fom = LocalDate.of(2025, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1990, Month.JANUARY, 1)))),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(begrunnelse = BtBegrunnelseCode.OPPHOR_BARN_OPPH_IKKE_AVT_LAND, fom = LocalDate.of(2026, Month.JANUARY, 1), tom = LocalDate.of(2026, Month.MARCH, 31),
                    barn = listOf(BarnDto(antallBarn = 2, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2)))),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(begrunnelse = BtBegrunnelseCode.OPPHOR_IKKE_MOTTATT_DOK, fom = LocalDate.of(2025, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 3, fodselsdato = LocalDate.of(1992, Month.MARCH, 3)))),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(begrunnelse = BtBegrunnelseCode.OPPHOR_BT_OVER_18, fom = LocalDate.of(2024, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1992, Month.MARCH, 3)))),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(begrunnelse = BtBegrunnelseCode.ANNET_OPPHOR, fom = LocalDate.of(2023, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 2, fodselsdato = LocalDate.of(1992, Month.MARCH, 3)), BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1997, Month.DECEMBER, 3)))),
            ),
            hjemler = setOf("12-8", "12-9", "12-10", "12-11", "12-12", "12-13", "12-14", "12-15", "12-16")
        ),
    )
