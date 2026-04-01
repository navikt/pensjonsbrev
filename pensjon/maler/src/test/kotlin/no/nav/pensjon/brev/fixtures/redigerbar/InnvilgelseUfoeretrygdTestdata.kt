package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BtBegrunnelseCode
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BarnetilleggUTDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned
import no.nav.pensjon.brev.fixtures.createOrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate
import java.time.Month

fun createInnvilgelseUfoeretrygdDto() =
    InnvilgelseUfoeretrygdDto(
        saksbehandlerValg = InnvilgelseUfoeretrygdDto.Saksbehandlervalg(refusjon = true),
        pesysData = InnvilgelseUfoeretrygdDto.PesysData(
            pe = createPEgruppe10(),
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
            createOrienteringOmRettigheterUfoereDto(),
            nyeInnvilgedeBarnetillegg = listOf(
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1990, Month.JANUARY, 1), fom = LocalDate.of(1990, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 2, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1991, Month.FEBRUARY, 2), fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31)),
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.INNVILGET, fodselsdato = LocalDate.of(1992, Month.MARCH, 3), fom = LocalDate.of(1992, Month.JANUARY, 1)),
            ),
            nyeAvslagBarnetillegg = listOf(
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.ANNET, fodselsdato = LocalDate.of(1990, Month.APRIL, 1), fom = LocalDate.of(1990, Month.JANUARY, 1)),
                BarnetilleggUTDto(antallBarn = 1, begrunnelse = BtBegrunnelseCode.BT_GITT_TIL_ANNEN, fodselsdato = LocalDate.of(1991, Month.MAY, 2), fom = LocalDate.of(1991, Month.JANUARY, 1), tom = LocalDate.of(1991, Month.MARCH, 31)),
                BarnetilleggUTDto(antallBarn = 3, begrunnelse = BtBegrunnelseCode.BARN_FLYTTET_IKKE_AVT_LAND, fodselsdato = LocalDate.of(1992, Month.JUNE, 3), fom = LocalDate.of(1992, Month.JANUARY, 1)),
            ),
        ),
    )
