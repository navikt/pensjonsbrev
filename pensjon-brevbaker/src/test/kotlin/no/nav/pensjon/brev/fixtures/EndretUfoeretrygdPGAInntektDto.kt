package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year
import java.time.LocalDate

fun createEndretUfoeretrygdPGAInntektDto() =
    EndretUfoeretrygdPGAInntektDto(
        PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus = Kroner(0),
        PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus = Kroner(1),
        PE_UT_NettoAkk_pluss_NettoRestAr = Kroner(2),
        PE_UT_VirkningstidpunktArMinus1Ar = 50,
        PE_VedtaksData_VirkningFOM = LocalDate.of(2020, 1, 1),

        harTilleggForFlereBarn = true,
        barnetilleggReduksjonsgrunnlagHarRegelverkstypeOvergang2016 = true,

        barnetilleggFelles_InntektBruktIAvkortning = Kroner(3),
        barnetilleggFelles_avkortningsbeloepPerAar = Kroner(4),
        barnetilleggFelles_belopFratrukketAnnenForeldersInntekt = Kroner(5),
        barnetilleggFelles_brukersInntektTilAvkortning = Kroner(6),
        barnetilleggFelles_fribeloep = Kroner(7),
        barnetilleggFelles_fribelopPeriodisert = true,
        barnetilleggFelles_inntektAnnenForelder = Kroner(8),
        barnetilleggFelles_inntektPeriodisert = true,
        barnetilleggFelles_inntektstak = Kroner(9),
        barnetilleggFelles_innvilget = true,
        barnetilleggFelles_justeringsbelopPerAr = Kroner(10),
        barnetilleggFelles_netto = Kroner(11),
        beregningUfore_BelopGammelBTFB = Kroner(12),
        beregningUfore_BelopNyBTFB = Kroner(13),
        harFlereFellesBarn = true,

        barnetilleggSerkull_avkortingsbelopPerAr = Kroner(14),
        barnetilleggSerkull_fribeloep = Kroner(15),
        barnetilleggSerkull_fribelopPeriodisert = true,
        barnetilleggSerkull_inntektBruktIAvkortning = Kroner(16),
        barnetilleggSerkull_inntektPeriodisert = true,
        barnetilleggSerkull_inntektstak = Kroner(17),
        barnetilleggSerkull_innvilget = true,
        barnetilleggSerkull_justeringsbelopPerAr = Kroner(18),
        barnetilleggSerkull_netto = Kroner(19),
        beregningUfore_BelopGammelBTSB = Kroner(20),
        beregningUfore_BelopNyBTSB = Kroner(21),
        harFlereSaerkullsbarn = true,

        beregningUfore_BelopGammelUT = Kroner(22),
        beregningUfore_BelopNyUT = Kroner(23),
        beregningUfore_BelopOkt = true,
        beregningUfore_BelopRedusert = true,
        beregningUfore_grunnbelop = Kroner(24),
        beregningUfore_totalNetto = Kroner(25),
        beregningUfore_uforegrad = 51,
        beregningVirkFomErFoersteJanuar = true,
        borMedSivilstand = BorMedSivilstand.EKTEFELLE,
        brukerBorINorge = true,
        ektefelletillegg_ETinnvilget = true,
        fyller67iAar = true,
        gjenlevendetillegg_GTinnvilget = true,
        harInstitusjonsopphold = true,
        harKravaarsakSivilstandsendring = true,
        uforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = 50,
        uforetrygdOrdiner_avkortingsbelopPerAr = Kroner(26),
        uforetrygdOrdiner_forventetInntekt = Kroner(27),
        uforetrygdOrdiner_inntektsgrense = Kroner(28),
        uforetrygdOrdiner_inntektstak = Kroner(29),
        uforetrygdOrdiner_netto = Kroner(30),
        uforetrygdOrdiner_nettoAkk = Kroner(31),
        virkFomAar = Year(2020),
        virkFomAarMinusEn = Year(2021),
        virkFomErFoersteJanuar = true,
    )