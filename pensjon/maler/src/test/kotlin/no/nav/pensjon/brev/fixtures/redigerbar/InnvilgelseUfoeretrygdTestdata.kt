package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Barnetillegg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Beregning
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Grunnlag
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Institusjonsopphold
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Kravhode
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Reduksjonsgrunnlag
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Tillegg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.UforeVedtaksinfo
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Vedtak
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.VilkarResultat
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.VilkarsvurderingFullstendig
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BarnDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BarnetilleggMedSammeBegrunnelsePaSammeTidDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BtBegrunnelseCode
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BarnetilleggUTDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUforetrygdBosattNorgeEtterUtlandDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUforetrygdMedEndringDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdMellombehandlingDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdUtlandDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.OkningUforegradDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.OkningUforegradV2Dto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.PeriodisertInntektBarnetillegg
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned
import no.nav.pensjon.brev.fixtures.createDineRettigheterOgPlikterUforeDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate
import java.time.Month

fun createInnvilgelseUfoeretrygdDto() =
    InnvilgelseUfoeretrygdDto(
        saksbehandlerValg = lagSaksbehandlervalg(
            "barnetilleggInfo" to true,
            "periodisertInntekt" to PeriodisertInntektBarnetillegg.PERIODISERT_INNTEKT.name,
        ),
        pesysData = InnvilgelseUfoeretrygdDto.PesysData(
            pe = createPEgruppe10(),
            vedtaksinfo = UforeVedtaksinfo(false, LocalDate.now()),
            kravFremsattDato = LocalDate.of(2020, Month.JANUARY, 1),
            oifuVedVirkningstidspunkt = Kroner(10000),
            maanedligUfoeretrygdFoerSkatt = MaanedligUfoeretrygdFoerSkattDto(
                ufoeretrygdPerioder = listOf(
                    UfoeretrygdPerMaaned(
                        annetBelop = Kroner(-500),
                        erAvkortet = false,
                        grunnbeloep = Kroner(10000),
                        ordinaerUTBeloepBrutto = Kroner(9500),
                        ordinaerUTBeloepNetto = Kroner(9000),
                        totalUTBeloepBrutto = Kroner(9500),
                        totalUTBeloepNetto = Kroner(9000),
                        virkningFraOgMed = LocalDate.of(2020, Month.JANUARY, 1),
                        barnetilleggSaerkullsbarnBrutto = null,
                        barnetilleggSaerkullsbarnNetto = null,
                        barnetilleggFellesbarnBrutto = null,
                        barnetilleggFellesbarnNetto = null,
                        gjenlevendetilleggBrutto = null,
                        gjenlevendetilleggNetto = null,
                        ektefelletilleggBrutto = null,
                        ektefelletilleggNetto = null,
                        dekningFasteUtgifter = null,
                        garantitilleggNordisk27Brutto = null,
                        garantitilleggNordisk27Netto = null,
                        virkningTilOgMed = null,
                    )
                )
            ),
            dineRettigheterOgPlikterUfore = createDineRettigheterOgPlikterUforeDto(),
            nyeInnvilgedeBarnetillegg = listOf(
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1990, Month.JANUARY, 1), fom = LocalDate.of(1990, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 2, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2), fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31)),
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1992, Month.MARCH, 3), fom = LocalDate.of(1992, Month.JANUARY, 1)),
            ),
            avslagBarnetilleggNye = listOf(
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.ANNET_AVSLAG, fom = LocalDate.of(1990, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1990, Month.APRIL, 1)))
                ),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.BT_GITT_TIL_ANNEN, fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31),
                    barn = listOf(BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1991, Month.MAY, 2)))
                ),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.BARN_FLYTTET_IKKE_AVT_LAND, fom = LocalDate.of(1992, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 3, fodselsdato = LocalDate.of(1992, Month.JUNE, 3)))
                ),
            ),
            sisteTrygdetidsgrunnlag = InnvilgelseUfoeretrygdDto.Trygdetidsgrunnlag(fom = LocalDate.of(2020, Month.JANUARY, 1), tom = LocalDate.of(2020, Month.JUNE, 30)),
            hjemler = setOf("12-2", "12-3", "12-4", "12-5", "12-6", "12-7", "12-8", "12-9", "12-10", "12-11", "12-12", "12-13", "12-14"),
            harVTA = false
        ),
    )

fun createInnvilgelseUfoeretrygdUtlandDto() =
    InnvilgelseUfoeretrygdUtlandDto(
        saksbehandlerValg = lagSaksbehandlervalg(
            "refusjon" to true,
            "barnetilleggInfo" to true,
            "innvilgetEtter12_2Andreledd" to true,
            "innvilgetEtter12_2Tredjeledd" to true,
            "periodisertInntekt" to PeriodisertInntektBarnetillegg.PERIODISERT_INNTEKT.name,
        ),
        pesysData = InnvilgelseUfoeretrygdUtlandDto.PesysData(
            pe = createPEgruppe10(),
            vedtaksinfo = UforeVedtaksinfo(false, LocalDate.now()),
            oifuVedVirkningstidspunkt = Kroner(10000),
            maanedligUfoeretrygdFoerSkatt = MaanedligUfoeretrygdFoerSkattDto(
                ufoeretrygdPerioder = listOf(
                    UfoeretrygdPerMaaned(
                        annetBelop = Kroner(-500),
                        erAvkortet = false,
                        grunnbeloep = Kroner(10000),
                        ordinaerUTBeloepBrutto = Kroner(9500),
                        ordinaerUTBeloepNetto = Kroner(9000),
                        totalUTBeloepBrutto = Kroner(9500),
                        totalUTBeloepNetto = Kroner(9000),
                        virkningFraOgMed = LocalDate.of(2020, Month.JANUARY, 1),
                        barnetilleggSaerkullsbarnBrutto = null,
                        barnetilleggSaerkullsbarnNetto = null,
                        barnetilleggFellesbarnBrutto = null,
                        barnetilleggFellesbarnNetto = null,
                        gjenlevendetilleggBrutto = null,
                        gjenlevendetilleggNetto = null,
                        ektefelletilleggBrutto = null,
                        ektefelletilleggNetto = null,
                        dekningFasteUtgifter = null,
                        garantitilleggNordisk27Brutto = null,
                        garantitilleggNordisk27Netto = null,
                        virkningTilOgMed = null,
                    )
                )
            ),
            dineRettigheterOgPlikterUfore = createDineRettigheterOgPlikterUforeDto(),
            nyeInnvilgedeBarnetillegg = listOf(
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1990, Month.JANUARY, 1), fom = LocalDate.of(1990, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 2, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2), fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31)),
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1992, Month.MARCH, 3), fom = LocalDate.of(1992, Month.JANUARY, 1)),
            ),
            avslagBarnetilleggNye = listOf(
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.ANNET_AVSLAG, fom = LocalDate.of(1990, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1990, Month.APRIL, 1)))
                ),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.BT_GITT_TIL_ANNEN, fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31),
                    barn = listOf(BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1991, Month.MAY, 2)))
                ),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.BARN_FLYTTET_IKKE_AVT_LAND, fom = LocalDate.of(1992, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 3, fodselsdato = LocalDate.of(1992, Month.JUNE, 3)))
                ),
            ),
            sisteTrygdetidsgrunnlag = InnvilgelseUfoeretrygdUtlandDto.Trygdetidsgrunnlag(fom = LocalDate.of(2020, Month.JANUARY, 1), tom = LocalDate.of(2020, Month.JUNE, 30)),
            hjemler = setOf("12-2", "12-3", "12-4", "12-5", "12-6", "12-7", "12-8", "12-9", "12-10", "12-11", "12-12", "12-13", "12-14"),
            harVTA = false
        ),
    )

fun createInnvilgelseUforetrygdBosattNorgeEtterUtlandDto() =
    InnvilgelseUforetrygdBosattNorgeEtterUtlandDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = InnvilgelseUforetrygdBosattNorgeEtterUtlandDto.PesysData(
            pe = createPEgruppe10(),
            vedtaksinfo = UforeVedtaksinfo(false, LocalDate.now()),
            oifuVedVirkningstidspunkt = Kroner(10000),
            maanedligUfoeretrygdFoerSkatt = MaanedligUfoeretrygdFoerSkattDto(
                ufoeretrygdPerioder = listOf(
                    UfoeretrygdPerMaaned(
                        annetBelop = Kroner(-500),
                        erAvkortet = false,
                        grunnbeloep = Kroner(10000),
                        ordinaerUTBeloepBrutto = Kroner(9500),
                        ordinaerUTBeloepNetto = Kroner(9000),
                        totalUTBeloepBrutto = Kroner(9500),
                        totalUTBeloepNetto = Kroner(9000),
                        virkningFraOgMed = LocalDate.of(2020, Month.JANUARY, 1),
                        barnetilleggSaerkullsbarnBrutto = null,
                        barnetilleggSaerkullsbarnNetto = null,
                        barnetilleggFellesbarnBrutto = null,
                        barnetilleggFellesbarnNetto = null,
                        gjenlevendetilleggBrutto = null,
                        gjenlevendetilleggNetto = null,
                        ektefelletilleggBrutto = null,
                        ektefelletilleggNetto = null,
                        dekningFasteUtgifter = null,
                        garantitilleggNordisk27Brutto = null,
                        garantitilleggNordisk27Netto = null,
                        virkningTilOgMed = null,
                    )
                )
            ),
            dineRettigheterOgPlikterUfore = createDineRettigheterOgPlikterUforeDto(),
            nyeInnvilgedeBarnetillegg = listOf(
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1990, Month.JANUARY, 1), fom = LocalDate.of(1990, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 2, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2), fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31)),
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1992, Month.MARCH, 3), fom = LocalDate.of(1992, Month.JANUARY, 1)),
            ),
            nyeAvslagBarnetillegg = listOf(
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.ANNET_AVSLAG, fom = LocalDate.of(1990, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1990, Month.APRIL, 1)))
                ),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.BT_GITT_TIL_ANNEN, fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31),
                    barn = listOf(BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1991, Month.MAY, 2)))
                ),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.BARN_FLYTTET_IKKE_AVT_LAND, fom = LocalDate.of(1992, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 3, fodselsdato = LocalDate.of(1992, Month.JUNE, 3)))
                ),
            ),
            sisteTrygdetidsgrunnlag = InnvilgelseUforetrygdBosattNorgeEtterUtlandDto.Trygdetidsgrunnlag(fom = LocalDate.of(2020, Month.JANUARY, 1), tom = LocalDate.of(2020, Month.JUNE, 30)),
            hjemler = setOf("12-2", "12-3", "12-4", "12-5", "12-6", "12-7", "12-8", "12-9", "12-10", "12-11", "12-12", "12-13", "12-14"),
            harVTA = false
        ),
    )

fun createInnvilgelseUforetrygdMellombehandlingDto() =
    InnvilgelseUfoeretrygdMellombehandlingDto(
        saksbehandlerValg = lagSaksbehandlervalg(
            "barnetilleggInfo" to true,
            "innvilgetEtter12_2Andreledd" to true,
            "innvilgetEtter12_2Tredjeledd" to true,
            "periodisertInntekt" to PeriodisertInntektBarnetillegg.PERIODISERT_INNTEKT.name,
        ),
        pesysData = InnvilgelseUfoeretrygdMellombehandlingDto.PesysData(
            pe = createPEgruppe10(),
            vedtaksinfo = UforeVedtaksinfo(false, LocalDate.now()),
            oifuVedVirkningstidspunkt = Kroner(10000),
            maanedligUfoeretrygdFoerSkatt = MaanedligUfoeretrygdFoerSkattDto(
                ufoeretrygdPerioder = listOf(
                    UfoeretrygdPerMaaned(
                        annetBelop = Kroner(-500),
                        erAvkortet = false,
                        grunnbeloep = Kroner(10000),
                        ordinaerUTBeloepBrutto = Kroner(9500),
                        ordinaerUTBeloepNetto = Kroner(9000),
                        totalUTBeloepBrutto = Kroner(9500),
                        totalUTBeloepNetto = Kroner(9000),
                        virkningFraOgMed = LocalDate.of(2020, Month.JANUARY, 1),
                        barnetilleggSaerkullsbarnBrutto = null,
                        barnetilleggSaerkullsbarnNetto = null,
                        barnetilleggFellesbarnBrutto = null,
                        barnetilleggFellesbarnNetto = null,
                        gjenlevendetilleggBrutto = null,
                        gjenlevendetilleggNetto = null,
                        ektefelletilleggBrutto = null,
                        ektefelletilleggNetto = null,
                        dekningFasteUtgifter = null,
                        garantitilleggNordisk27Brutto = null,
                        garantitilleggNordisk27Netto = null,
                        virkningTilOgMed = null,
                    )
                )
            ),
            dineRettigheterOgPlikterUfore = createDineRettigheterOgPlikterUforeDto(),
            nyeInnvilgedeBarnetillegg = listOf(
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1990, Month.JANUARY, 1), fom = LocalDate.of(1990, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 2, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2), fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31)),
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1992, Month.MARCH, 3), fom = LocalDate.of(1992, Month.JANUARY, 1)),
            ),
            avslagBarnetilleggNye = listOf(
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.ANNET_AVSLAG, fom = LocalDate.of(1990, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1990, Month.APRIL, 1)))
                ),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.BT_GITT_TIL_ANNEN, fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31),
                    barn = listOf(BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1991, Month.MAY, 2)))
                ),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.BARN_FLYTTET_IKKE_AVT_LAND, fom = LocalDate.of(1992, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 3, fodselsdato = LocalDate.of(1992, Month.JUNE, 3)))
                ),
            ),
            sisteTrygdetidsgrunnlag = InnvilgelseUfoeretrygdMellombehandlingDto.Trygdetidsgrunnlag(fom = LocalDate.of(2020, Month.JANUARY, 1), tom = LocalDate.of(2020, Month.JUNE, 30)),
            hjemler = setOf("12-2", "12-3", "12-4", "12-5", "12-6", "12-7", "12-8", "12-9", "12-10", "12-11", "12-12", "12-13", "12-14"),
            harVTA = false
        ),
    )

fun createInnvilgelseUforetrygdMedEndringDto() =
    InnvilgelseUforetrygdMedEndringDto(
        saksbehandlerValg = lagSaksbehandlervalg(
            "periodisertInntekt" to PeriodisertInntektBarnetillegg.PERIODISERT_INNTEKT.name,
        ),
        pesysData = InnvilgelseUforetrygdMedEndringDto.PesysData(
            pe = createPEgruppe10(),
            vedtaksinfo = UforeVedtaksinfo(false, LocalDate.now()),
            oifuVedVirkningstidspunkt = Kroner(10000),
            maanedligUfoeretrygdFoerSkatt = MaanedligUfoeretrygdFoerSkattDto(
                ufoeretrygdPerioder = listOf(
                    UfoeretrygdPerMaaned(
                        annetBelop = Kroner(-500),
                        erAvkortet = false,
                        grunnbeloep = Kroner(10000),
                        ordinaerUTBeloepBrutto = Kroner(9500),
                        ordinaerUTBeloepNetto = Kroner(9000),
                        totalUTBeloepBrutto = Kroner(9500),
                        totalUTBeloepNetto = Kroner(9000),
                        virkningFraOgMed = LocalDate.of(2020, Month.JANUARY, 1),
                        barnetilleggSaerkullsbarnBrutto = null,
                        barnetilleggSaerkullsbarnNetto = null,
                        barnetilleggFellesbarnBrutto = null,
                        barnetilleggFellesbarnNetto = null,
                        gjenlevendetilleggBrutto = null,
                        gjenlevendetilleggNetto = null,
                        ektefelletilleggBrutto = null,
                        ektefelletilleggNetto = null,
                        dekningFasteUtgifter = null,
                        garantitilleggNordisk27Brutto = null,
                        garantitilleggNordisk27Netto = null,
                        virkningTilOgMed = null,
                    )
                )
            ),
            dineRettigheterOgPlikterUfore = createDineRettigheterOgPlikterUforeDto(),
            nyeInnvilgedeBarnetillegg = listOf(
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1990, Month.JANUARY, 1), fom = LocalDate.of(1990, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 2, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2), fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31)),
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1992, Month.MARCH, 3), fom = LocalDate.of(1992, Month.JANUARY, 1)),
            ),
            nyeAvslagBarnetillegg = listOf(
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.ANNET_AVSLAG, fom = LocalDate.of(1990, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1990, Month.APRIL, 1)))
                ),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.BT_GITT_TIL_ANNEN, fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31),
                    barn = listOf(BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1991, Month.MAY, 2)))
                ),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.BARN_FLYTTET_IKKE_AVT_LAND, fom = LocalDate.of(1992, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 3, fodselsdato = LocalDate.of(1992, Month.JUNE, 3)))
                ),
            ),
            sisteTrygdetidsgrunnlag = InnvilgelseUforetrygdMedEndringDto.Trygdetidsgrunnlag(fom = LocalDate.of(2020, Month.JANUARY, 1), tom = LocalDate.of(2020, Month.JUNE, 30)),
            hjemler = setOf("12-2", "12-3", "12-4", "12-5", "12-6", "12-7", "12-8", "12-9", "12-10", "12-11", "12-12", "12-13", "12-14"),
            harVTA = false,
        ),
    )

fun createOkningUforegradDto() =
    OkningUforegradDto(
        saksbehandlerValg = lagSaksbehandlervalg(
            "periodisertInntekt" to PeriodisertInntektBarnetillegg.PERIODISERT_INNTEKT.name,
        ),
        pesysData = OkningUforegradDto.PesysData(
            pe = createPEgruppe10(),
            vedtaksinfo = UforeVedtaksinfo(false, LocalDate.now()),
            kravFremsattDato = LocalDate.of(2020, Month.JANUARY, 1),
            oifuVedVirkningstidspunkt = Kroner(10000),
            maanedligUfoeretrygdFoerSkatt = MaanedligUfoeretrygdFoerSkattDto(
                ufoeretrygdPerioder = listOf(
                    UfoeretrygdPerMaaned(
                        annetBelop = Kroner(-500),
                        erAvkortet = false,
                        grunnbeloep = Kroner(10000),
                        ordinaerUTBeloepBrutto = Kroner(9500),
                        ordinaerUTBeloepNetto = Kroner(9000),
                        totalUTBeloepBrutto = Kroner(9500),
                        totalUTBeloepNetto = Kroner(9000),
                        virkningFraOgMed = LocalDate.of(2020, Month.JANUARY, 1),
                        barnetilleggSaerkullsbarnBrutto = null,
                        barnetilleggSaerkullsbarnNetto = null,
                        barnetilleggFellesbarnBrutto = null,
                        barnetilleggFellesbarnNetto = null,
                        gjenlevendetilleggBrutto = null,
                        gjenlevendetilleggNetto = null,
                        ektefelletilleggBrutto = null,
                        ektefelletilleggNetto = null,
                        dekningFasteUtgifter = null,
                        garantitilleggNordisk27Brutto = null,
                        garantitilleggNordisk27Netto = null,
                        virkningTilOgMed = null,
                    )
                )
            ),
            dineRettigheterOgPlikterUfore = createDineRettigheterOgPlikterUforeDto(),
            nyeInnvilgedeBarnetillegg = listOf(
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1990, Month.JANUARY, 1), fom = LocalDate.of(1990, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 2, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2), fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31)),
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1992, Month.MARCH, 3), fom = LocalDate.of(1992, Month.JANUARY, 1)),
            ),
            nyeAvslagBarnetillegg = listOf(
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.ANNET_AVSLAG, fom = LocalDate.of(1990, Month.JANUARY, 1),
                    barn = listOf(
                        BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1990, Month.APRIL, 1)),
                        BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1995, Month.APRIL, 1))
                    )
                ),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.BT_GITT_TIL_ANNEN, fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31),
                    barn = listOf(BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1991, Month.MAY, 2)))
                ),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.BARN_FLYTTET_IKKE_AVT_LAND, fom = LocalDate.of(1992, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 3, fodselsdato = LocalDate.of(1992, Month.JUNE, 3)))
                ),
            ),
            sisteTrygdetidsgrunnlag = OkningUforegradDto.Trygdetidsgrunnlag(fom = LocalDate.of(2020, Month.JANUARY, 1), tom = LocalDate.of(2020, Month.JUNE, 30)),
            hjemler = setOf("12-2", "12-3", "12-4", "12-5", "12-6", "12-7", "12-8", "12-9", "12-10", "12-11", "12-12", "12-13", "12-14"),
            fribelopsperioder = listOf(
                OkningUforegradDto.Fribelopsperiode(
                    fom = LocalDate.of(2026, 1, 1),
                    tom = LocalDate.of(2026, 12, 31),
                    faktor = 0.4,
                    grunnbelop = Kroner(136000),
                    venteperiodeStartDato = LocalDate.of(2026, 7, 1),
                    gradsokning = true
                )
            ),
            harVTA = false,
            fribelop = Kroner(5000),
            forrigeUforegrad = 50,
            forrigeUforetidspunkt = LocalDate.of(2025, 12, 1),
        ),
    )

fun createOkningUforegradV2Dto() =
    OkningUforegradV2Dto(
        saksbehandlerValg = lagSaksbehandlervalg(
            "periodisertInntekt" to PeriodisertInntektBarnetillegg.PERIODISERT_INNTEKT.name,
        ),
        pesysData = OkningUforegradV2Dto.PesysData(
            pe = createPEgruppe10(),
            vedtaksinfo = UforeVedtaksinfo(false, LocalDate.now()),
            kravFremsattDato = LocalDate.of(2020, Month.JANUARY, 1),
            oifuVedVirkningstidspunkt = Kroner(10000),
            maanedligUfoeretrygdFoerSkatt = MaanedligUfoeretrygdFoerSkattDto(
                ufoeretrygdPerioder = listOf(
                    UfoeretrygdPerMaaned(
                        annetBelop = Kroner(-500),
                        erAvkortet = false,
                        grunnbeloep = Kroner(10000),
                        ordinaerUTBeloepBrutto = Kroner(9500),
                        ordinaerUTBeloepNetto = Kroner(9000),
                        totalUTBeloepBrutto = Kroner(9500),
                        totalUTBeloepNetto = Kroner(9000),
                        virkningFraOgMed = LocalDate.of(2020, Month.JANUARY, 1),
                        barnetilleggSaerkullsbarnBrutto = null,
                        barnetilleggSaerkullsbarnNetto = null,
                        barnetilleggFellesbarnBrutto = null,
                        barnetilleggFellesbarnNetto = null,
                        gjenlevendetilleggBrutto = null,
                        gjenlevendetilleggNetto = null,
                        ektefelletilleggBrutto = null,
                        ektefelletilleggNetto = null,
                        dekningFasteUtgifter = null,
                        garantitilleggNordisk27Brutto = null,
                        garantitilleggNordisk27Netto = null,
                        virkningTilOgMed = null,
                    )
                )
            ),
            dineRettigheterOgPlikterUfore = createDineRettigheterOgPlikterUforeDto(),
            nyeInnvilgedeBarnetillegg = listOf(
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1990, Month.JANUARY, 1), fom = LocalDate.of(1990, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 2, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2), fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31)),
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1992, Month.MARCH, 3), fom = LocalDate.of(1992, Month.JANUARY, 1)),
            ),
            nyeAvslagBarnetillegg = listOf(
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.ANNET_AVSLAG, fom = LocalDate.of(1990, Month.JANUARY, 1),
                    barn = listOf(
                        BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1990, Month.APRIL, 1)),
                        BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1995, Month.APRIL, 1))
                    )
                ),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.BT_GITT_TIL_ANNEN, fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31),
                    barn = listOf(BarnDto(antallBarn = 1, fodselsdato = LocalDate.of(1991, Month.MAY, 2)))
                ),
                BarnetilleggMedSammeBegrunnelsePaSammeTidDto(
                    begrunnelse = BtBegrunnelseCode.BARN_FLYTTET_IKKE_AVT_LAND, fom = LocalDate.of(1992, Month.JANUARY, 1),
                    barn = listOf(BarnDto(antallBarn = 3, fodselsdato = LocalDate.of(1992, Month.JUNE, 3)))
                ),
            ),
            sisteTrygdetidsgrunnlag = OkningUforegradDto.Trygdetidsgrunnlag(fom = LocalDate.of(2020, Month.JANUARY, 1), tom = LocalDate.of(2020, Month.JUNE, 30)),
            hjemler = setOf("12-2", "12-3", "12-4", "12-5", "12-6", "12-7", "12-8", "12-9", "12-10", "12-11", "12-12", "12-13", "12-14"),
            fribelopsperioder = listOf(
                OkningUforegradDto.Fribelopsperiode(
                    fom = LocalDate.of(2026, 1, 1),
                    tom = LocalDate.of(2026, 12, 31),
                    faktor = 0.4,
                    grunnbelop = Kroner(136000),
                    venteperiodeStartDato = LocalDate.of(2026, 7, 1),
                    gradsokning = true
                )
            ),
            harVTA = false,
            fribelop = Kroner(5000),
            forrigeUforegrad = 50,
            forrigeUforetidspunkt = LocalDate.of(2025, 12, 1),
            harAlderspensjonLopendeEllerTilBehandling = true,
            krav = Kravhode(
                virkFom = LocalDate.of(2020, Month.JANUARY, 1),
                mottattDato = LocalDate.of(2020, Month.JANUARY, 1),
                arsak = Kravhode.Arsak.ANNEN,
                konvertertFraUforepensjon = false,
                ),
            vedtak = Vedtak(
                virkFom = LocalDate.of(2024, Month.JANUARY, 1),
                virkningsdatoTidligereMnd = false,
                vedtakFattetDatoEllerIdag = LocalDate.now(),
            ),
            beregning = Beregning(
                virkFom = LocalDate.of(2020, Month.JANUARY, 1),
                uforegrad = 100,
                uforetidspunkt = LocalDate.of(2020, Month.JANUARY, 1),
                utbetalingsgrad = 100,
                totalNetto = Kroner(1231),
                belopHarOkt = true,
                beregningsresultatTilRevurderingTotalNetto = Kroner(1000),
                bunnfradrag = Kroner(1913),
                bunnfradragNesteAr = Kroner(1919),
                reduksjonsprosent = 5.0,
                yrkesskadegrad = 0,
                yrkesskadeBest = false,
                inntektSkadetidspunkt = Kroner(1010),
                grunnbelop = Kroner(1010),
            ),
            grunnlag = Grunnlag(
                borIUtlandet = false,
                epsBrukerBorMed = Grunnlag.Eps.EKTEFELLE,
            ),
            institusjonsopphold = Institusjonsopphold(
                erAnvendt = false,
                type = null,
                fasteUtgifter = null,
                reduksjonsperioder = listOf(
                    Institusjonsopphold.Reduksjonsperiode(
                        forsorgeransvar = true,
                        fomDato = LocalDate.of(2020, Month.JANUARY, 1),
                    ),
                ),
                allePerioderHarForsorgeransvar = true,
                ingenPerioderHarForsorgeransvar = false,
            ),
            reduksjonsgrunnlag = Reduksjonsgrunnlag(
                sumUtBt = Kroner(8511),
                gradertOppjustertIFU = Kroner(1938),
                barnetilleggRegelverkType = Reduksjonsgrunnlag.BarnetilleggRegelverkTypeCode.OVERGANGSREGLER_2016,
                sumBruttoFoerReduksjonBt = Kroner(8752),
                sumBruttoEtterReduksjonBt = Kroner(8510),
                andelYtelseAvOIFU = 25.5,
            ),
            tillegg = Tillegg(
                ektefelletilleggInnvilget = true,
                gjenlevendetilleggInnvilget = false,
                nyttGjenlevendetillegg = false,
            ),
            barnetillegg = Barnetillegg(
                btFellesbarn = Barnetillegg.BarnetilleggFellesbarn(
                    innvilget = true,
                    netto = Kroner(1200),
                    brutto = Kroner(9832),
                    justeringsbelopPerAr = Kroner(1110),
                    inntektAnnenForelder = Kroner(1205),
                    brukersInntektTilAvkorting = Kroner(1201),
                    inntektstak = Kroner(1114),
                    fradrag = Kroner(3802),
                    fribelop = Kroner(1204),
                    inntektBruktIAvkorting = Kroner(1202),
                    belopFratrukketAnnenForeldersInntekt = Kroner(1203),
                    antallBarn = 2,
                ),
                btSerkullsbarn = Barnetillegg.BarnetilleggSerkullsbarn(
                    innvilget = false,
                    netto = Kroner(8450),
                    brutto = Kroner(19113),
                    justeringsbelopPerAr = Kroner(1110),
                    inntektstak = Kroner(1114),
                    fradrag = Kroner(19112),
                    fribelop = Kroner(8452),
                    inntektBruktIAvkorting = Kroner(8451),
                    antallBarn = 2,
                ),
                antallForekomsterAvBarnetillegg = 1,
            ),
            vilkarsvurdering = VilkarsvurderingFullstendig(
                virkningstidspunkt = LocalDate.of(2020, Month.FEBRUARY, 12),
                virkningstidspunktBegrunnelse = VilkarsvurderingFullstendig.VirkningstidspunktBegrunnelse.STDBEGR_22_12_1_1,
                uforetidspunkt = vilkaarligDato.minusYears(5),
                uforegrad = 100,
                ifuInntekt = Kroner(9938),
                ifuBegrunnelse = null,
                ieuInntekt = Kroner(9939),
                ieuBegrunnelse = null,
                ungUforResultat = VilkarResultat.IKKE_OPPFYLT,
                nedsattInntektsevneResultat = VilkarResultat.OPPFYLT,
                yrkesskadeResultat = VilkarResultat.IKKE_OPPFYLT,
                yrkesskadeBegrunnelse = VilkarsvurderingFullstendig.YrkesskadeBegrunnelse.STDBEGR_12_17_1_O_1,
                yrkesskade = VilkarsvurderingFullstendig.Yrkesskade(
                    skadetidspunkt = LocalDate.of(2020, Month.JANUARY, 1),
                    yrkesskadegrad = 29,
                    inntektVedSkadetidspunkt = Kroner(1010),
                    inntektVedSkadetidspunktBegrunnelse = null,
                ),
            ),
        ),
    )
