package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.Grunnlag
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.Persongrunnlag
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagBilateral
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagEOS
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagListeBilateral
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagListeEOS
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagnorge.Trygdetidsgrunnlag
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagnorge.TrygdetidsgrunnlagListeNor
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.Vedtaksbrev
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.Vedtaksdata
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.BeregningsData
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.*
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.*
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.uforetrygdberegning.Uforetrygdberegning
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregninguforeperiode.BeregningUforePeriode
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.kravhode.Kravhode
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.VilkarsVedtakList
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.Vilkar
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.VilkarsVedtak
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.beregningsvilkar.BeregningsVilkar
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.beregningsvilkar.Trygdetid
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createPE() =
    PE(
        Vedtaksbrev = createVedtaksbrev(),
        functions = PE.ExstreamFunctions(
            PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT = "ektefelle",
            PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_EN_cohabiting_partner = "spouse",
            PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall = "ektefellen",
            PE_UT_NettoAkk_pluss_NettoRestAr = Kroner(8313),
            PE_UT_NettoAkk_pluss_NettoRestAr_pluss_ForventetInntekt = Kroner(8314),
            PE_UT_OpplyningerOmBergeningen_NettoPerAr = Kroner(8315),
            PE_UT_Overskytende = Kroner(8316),
            PE_UT_SUM_FaTTNorge_FramtidigTTNorge_DIV_12 = Kroner(8317),
            PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus = Kroner(1881),
            PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus = Kroner(1882),
            PE_UT_VirkningstidpunktArMinus1Ar = 2019
        )
    )

fun createVedtaksbrev(): Vedtaksbrev =
    Vedtaksbrev(
        Vedtaksdata = createVedtaksdata(),
        Grunnlag = createGrunnlag()
    )

fun createGrunnlag() =
    Grunnlag(
        listOf(createPersongrunnlag())
    )

fun createPersongrunnlag() =
    Persongrunnlag(
        BrukerFlyktning = true,
        PersonBostedsland = "nor",
        TrygdetidsgrunnlagListeBilateral = createTrygdetidsgrunnlagListeBilateral(),
        TrygdetidsgrunnlagListeEOS = createTrygdetidsgrunnlagListeEOS(),
        TrygdetidsgrunnlagListeNor = createTrygdetidsgrunnlagListeNor(),
    )

fun createTrygdetidsgrunnlagListeBilateral() =
    TrygdetidsgrunnlagListeBilateral(
        listOf(
            TrygdetidsgrunnlagBilateral(
                TrygdetidBilateralLand = "usa",
                TrygdetidFomBilateral = LocalDate.of(2020, 1, 5),
                TrygdetidTomBilateral = LocalDate.of(2020, 5, 5),
            )
        )
    )

fun createTrygdetidsgrunnlagListeEOS() =
    TrygdetidsgrunnlagListeEOS(
        listOf(
            TrygdetidsgrunnlagEOS(
                TrygdetidEOSLand = "swe",
                TrygdetidFomEOS = LocalDate.of(2020, 6, 5),
                TrygdetidTomEOS = LocalDate.of(2020, 8, 5),
            )
        )
    )

fun createTrygdetidsgrunnlagListeNor() =
    TrygdetidsgrunnlagListeNor(
        listOf(
            Trygdetidsgrunnlag(
                TrygdetidFom = LocalDate.of(2020, 8, 5),
                TrygdetidTom = LocalDate.of(2020, 10, 5),
            )
        )
    )

fun createVedtaksdata() = Vedtaksdata(
    BeregningsData = createBeregningsData(),
    Kravhode = createKravhode(),
    VilkarsVedtakList = createVilkarsVedtakList(),
    VirkningFOM = LocalDate.of(2020, 1, 1),
    Faktoromregnet = false,
)

fun createVilkarsVedtakList() =
    VilkarsVedtakList(
        listOf(createVilkarsVedtak())
    )

fun createVilkarsVedtak() =
    VilkarsVedtak(
        BeregningsVilkar = createBeregningsVilkar(),
        Vilkar = createVilkar(),
    )

fun createVilkar() =
    Vilkar(YrkesskadeResultat = null)

fun createBeregningsVilkar() =
    BeregningsVilkar(
        IEUBegrunnelse = null,
        IFUBegrunnelse = null,
        IFUInntekt = Kroner(9938),
        Trygdetid = createTrygdetid(),
        Uforegrad = 100,
    )

fun createTrygdetid() =
    Trygdetid(
        FaTTEOS = 30,
        FramtidigTTEOS = 31,
        FramtidigTTNorsk = 32,
        RedusertFramtidigTrygdetid = 33,
        FaTTNorge = 34,
    )

fun createKravhode() =
    Kravhode(
        BoddArbeidUtland = true,
        BrukerKonvertertUP = false,
        KravArsakType = "endring_ifu",
    )

fun createBeregningsData() =
    BeregningsData(
        BeregningUfore = createBeregningUfore(),
        BeregningAntallPerioder = 1,
        BeregningUforePeriode = createBeregningUforePeriode()
    )

fun createBeregningUforePeriode() =
    listOf(
        BeregningUforePeriode(
            createBeregningYtelsesKomp()
        )
    )

fun createBeregningUfore() =
    BeregningUfore(
        BeregningYtelsesKomp = createBeregningYtelsesKomp(),
        BelopRedusert = false,
        TotalNetto = Kroner(1231),
        Total = Kroner(1232),
        Reduksjonsgrunnlag = createReduksjonsgrunnlag(),
        Belopsendring = createBelopsendring(),
        Uforetrygdberegning = createUforetrygdberegning(),
        BeregningSivilstandAnvendt = "bormed 1-5",
        BelopOkt = true,
        BeregningVirkningDatoFom = LocalDate.of(2020, 1, 1),
    )

fun createUforetrygdberegning() =
    Uforetrygdberegning(
        AnvendtTrygdetid = 20,
        Mottarminsteytelse = false,
        Uforegrad = 100,
        Yrkesskadegrad = 0,
        Grunnbelop = Kroner(1010),
        InstOppholdType = null,
    )

fun createReduksjonsgrunnlag() =
    Reduksjonsgrunnlag(
        AndelYtelseAvOIFU = 25.5,
        BarnetilleggRegelverkType = "overgangsregler_2016",
        GradertOppjustertIFU = Kroner(1938),
        ProsentsatsOIFUForTak = 30,
    )

fun createBelopsendring() =
    Belopsendring(
        BarnetilleggFellesYK = createBarnetilleggFellesYK(),
        BarnetilleggSerkullYK = createBarnetilleggSerkullYK(),
        UforetrygdOrdinerYK = createUforetrygdOrdinerYK(),
    )

fun createBarnetilleggFellesYK() = BarnetilleggFellesYK(
    BelopGammelBTFB = Kroner(5850),
    BelopNyBTFB = Kroner(5851),
)

fun createBarnetilleggSerkullYK() = BarnetilleggSerkullYK(
    BelopGammelBTSB = Kroner(5140),
    BelopNyBTSB = Kroner(5141),
)

fun createUforetrygdOrdinerYK() = UforetrygdOrdinerYK(
    BelopGammelUT = Kroner(5810),
    BelopNyUT = Kroner(5811),
)

fun createBeregningYtelsesKomp() =
    BeregningYtelsesKomp(
        UforetrygdOrdiner = createUforetrygdOrdiner(),
        BarnetilleggFelles = createBarnetilleggFelles(),
        BarnetilleggSerkull = createBarnetilleggSerkull(),
        Ektefelletillegg = createEktefelletillegg(),
        Gjenlevendetillegg = createGjenlevendetillegg(),
    )

fun createEktefelletillegg() =
    Ektefelletillegg(true)

fun createGjenlevendetillegg() =
    Gjenlevendetillegg(true)

fun createBarnetilleggSerkull() =
    BarnetilleggSerkull(
        AvkortningsInformasjon = createAvkortningsInformasjonBT(),
        BTSBinnvilget = true,
        BTSBnetto = Kroner(8450),
        BTSBInntektBruktiAvkortning = Kroner(8451),
        BTSBfribelop = Kroner(8452),
    )

fun createBarnetilleggFelles() =
    BarnetilleggFelles(
        AvkortningsInformasjon = createAvkortningsInformasjonBT(),
        BTFBinnvilget = false,
        BTFBnetto = Kroner(1200),
        BTFBBrukersInntektTilAvkortning = Kroner(1201),
        BTFBInntektBruktiAvkortning = Kroner(1202),
        BTFBbelopFratrukketAnnenForeldersInntekt = Kroner(1203),
        BTFBfribelop = Kroner(1204),
        BTFBinntektAnnenForelder = Kroner(1205),
    )

fun createAvkortningsInformasjonBT() =
    AvkortningsInformasjonBT(
        JusteringsbelopPerAr = Kroner(1110),
        AvkortingsbelopPerAr = Kroner(1111),
        FribelopPeriodisert = true,
        InntektPeriodisert = true,
        Inntektstak = Kroner(1114),
    )

fun createUforetrygdOrdiner() =
    UforetrygdOrdiner(
        AvkortningsInformasjon = createAvkortningsInformasjon(),
        Fradrag = Kroner(3802),
        Netto = Kroner(1201),
        NettoAkk = Kroner(10134),
        Minsteytelse = createMinsteytelse(),
        AvkortingsbelopPerAr = Kroner(5819),
        Brutto = Kroner(9832),
        NettoRestAr = Kroner(5934),
        Ytelsesgrunnlag = createYtelsesgrunnlag()
    )

fun createYtelsesgrunnlag() =
    Ytelsesgrunnlag(
        BeregningsgrunnlagOrdinar = createBeregningsgrunnlagOrdinar()
    )

fun createBeregningsgrunnlagOrdinar() =
    BeregningsgrunnlagOrdinar(
        AntallArOver1G = 10,
        AntallArInntektIAvtaleland = 11,
    )

fun createAvkortningsInformasjon(): AvkortningsInformasjon =
    AvkortningsInformasjon(
        Oifu = Kroner(1910),
        Oieu = Kroner(1911),
        Belopsgrense = Kroner(1912),
        Inntektsgrense = Kroner(1913),
        Inntektstak = Kroner(1914),
        UgradertBruttoPerAr = Kroner(1915),
        Kompensasjonsgrad = 5.0,
        Utbetalingsgrad = 100,
        ForventetInntekt = Kroner(1918),
        InntektsgrenseNesteAr = Kroner(1919),
    )

fun createMinsteytelse() =
    Minsteytelse(3.5)