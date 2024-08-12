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
import no.nav.pensjon.brev.api.model.maler.legacy.personsak.PSfnr
import no.nav.pensjon.brev.api.model.maler.legacy.personsak.PersonSak
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
        vedtaksbrev = createVedtaksbrev(),
        functions = PE.ExstreamFunctions(
            pe_sivilstand_ektefelle_partner_samboer_bormed_ut = "ektefelle",
            pe_sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner = "spouse",
            pe_sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall = "ektefellen",
            pe_ut_nettoakk_pluss_nettorestar = Kroner(8313),
            pe_ut_nettoakk_pluss_nettorestar_pluss_forventetinntekt = Kroner(8314),
            pe_ut_opplyningerombergeningen_nettoperar = Kroner(8315),
            pe_ut_overskytende = Kroner(8316),
            pe_ut_sum_fattnorge_framtidigttnorge_div_12 = 20,
            pe_ut_virkningstidpunktarminus1ar = 2019,
            pe_ut_vilfylle67ivirkningfomar = true,
            pe_ut_virkningfomar = 2020,
            pe_sivilstand_ektefelle_partner_samboer_bormed_ut_en = "spouse",
            pe_ut_btfbinntektbruktiavkortningminusbtfbfribelop = Kroner(1058),
            pe_ut_btsbinntektbruktiavkortningminusbtsbfribelop = Kroner(1087),
        ),
        pebrevkode = "PE_UT_05_100",
        personsak = PersonSak(PSfnr("01019878910"))
    )

fun createVedtaksbrev(): Vedtaksbrev =
    Vedtaksbrev(
        vedtaksdata = createVedtaksdata(),
        grunnlag = createGrunnlag()
    )

fun createGrunnlag() =
    Grunnlag(
        listOf(createPersongrunnlag())
    )

fun createPersongrunnlag() =
    Persongrunnlag(
        brukerflyktning = true,
        personbostedsland = "nor",
        trygdetidsgrunnlaglistebilateral = createTrygdetidsgrunnlagListeBilateral(),
        trygdetidsgrunnlaglisteeos = createTrygdetidsgrunnlagListeEOS(),
        trygdetidsgrunnlaglistenor = createTrygdetidsgrunnlagListeNor(),
    )

fun createTrygdetidsgrunnlagListeBilateral() =
    TrygdetidsgrunnlagListeBilateral(
        listOf(
            TrygdetidsgrunnlagBilateral(
                trygdetidbilateralland = "usa",
                trygdetidfombilateral = LocalDate.of(2020, 1, 5),
                trygdetidtombilateral = LocalDate.of(2020, 5, 5),
            )
        )
    )

fun createTrygdetidsgrunnlagListeEOS() =
    TrygdetidsgrunnlagListeEOS(
        listOf(
            TrygdetidsgrunnlagEOS(
                trygdetideosland = "swe",
                trygdetidfomeos = LocalDate.of(2020, 6, 5),
                trygdetidtomeos = LocalDate.of(2020, 8, 5),
            )
        )
    )

fun createTrygdetidsgrunnlagListeNor() =
    TrygdetidsgrunnlagListeNor(
        listOf(
            Trygdetidsgrunnlag(
                trygdetidfom = LocalDate.of(2020, 8, 5),
                trygdetidtom = LocalDate.of(2020, 10, 5),
            )
        )
    )

fun createVedtaksdata() = Vedtaksdata(
    beregningsdata = createBeregningsData(),
    kravhode = createKravhode(),
    vilkarsvedtaklist = createVilkarsVedtakList(),
    virkningfom = LocalDate.of(2020, 1, 1),
    faktoromregnet = false,
)

fun createVilkarsVedtakList() =
    VilkarsVedtakList(
        listOf(createVilkarsVedtak())
    )

fun createVilkarsVedtak() =
    VilkarsVedtak(
        beregningsvilkar = createBeregningsVilkar(),
        vilkar = createVilkar(),
        vilkarVirkningFom = LocalDate.of(2020,1,1)
    )

fun createVilkar() =
    Vilkar(yrkesskaderesultat = null)

fun createBeregningsVilkar() =
    BeregningsVilkar(
        ieubegrunnelse = null,
        ifubegrunnelse = null,
        ifuinntekt = Kroner(9938),
        trygdetid = createTrygdetid(),
        uforegrad = 100,
        virkningstidpunkt = LocalDate.of(2020,2,12)
    )

fun createTrygdetid() =
    Trygdetid(
        fatteos = 30,
        framtidigtteos = 31,
        framtidigttnorsk = 32,
        redusertframtidigtrygdetid = 33,
        fattnorge = 34,
    )

fun createKravhode() =
    Kravhode(
        boddarbeidutland = true,
        brukerkonvertertup = false,
        kravarsaktype = "endring_ifu",
    )

fun createBeregningsData() =
    BeregningsData(
        beregningufore = createBeregningUfore(),
        beregningantallperioder = 1,
        beregninguforeperiode = createBeregningUforePeriode()
    )

fun createBeregningUforePeriode() =
    listOf(
        BeregningUforePeriode(
            createBeregningYtelsesKomp()
        )
    )

fun createBeregningUfore() =
    BeregningUfore(
        beregningytelseskomp = createBeregningYtelsesKomp(),
        belopredusert = false,
        totalnetto = Kroner(1231),
        total = Kroner(1232),
        reduksjonsgrunnlag = createReduksjonsgrunnlag(),
        belopsendring = createBelopsendring(),
        uforetrygdberegning = createUforetrygdberegning(),
        beregningsivilstandanvendt = "bormed 1-5",
        belopokt = true,
        beregningvirkningdatofom = LocalDate.of(2020, 1, 1),
    )

fun createUforetrygdberegning() =
    Uforetrygdberegning(
        anvendttrygdetid = 20,
        mottarminsteytelse = false,
        uforegrad = 100,
        yrkesskadegrad = 0,
        grunnbelop = Kroner(1010),
        instoppholdtype = null,
    )

fun createReduksjonsgrunnlag() =
    Reduksjonsgrunnlag(
        andelytelseavoifu = 25.5,
        barnetilleggregelverktype = "overgangsregler_2016",
        gradertoppjustertifu = Kroner(1938),
        prosentsatsoifufortak = 30,
        sumbruttoforreduksjonbt = Kroner(8752),
        sumbruttoetterreduksjonbt = Kroner(8510),
    )

fun createBelopsendring() =
    Belopsendring(
        barnetilleggfellesyk = createBarnetilleggFellesYK(),
        barnetilleggserkullyk = createBarnetilleggSerkullYK(),
        uforetrygdordineryk = createUforetrygdOrdinerYK(),
    )

fun createBarnetilleggFellesYK() = BarnetilleggFellesYK(
    belopgammelbtfb = Kroner(5850),
    belopnybtfb = Kroner(5851),
)

fun createBarnetilleggSerkullYK() = BarnetilleggSerkullYK(
    belopgammelbtsb = Kroner(5140),
    belopnybtsb = Kroner(5141),
)

fun createUforetrygdOrdinerYK() = UforetrygdOrdinerYK(
    belopgammelut = Kroner(5810),
    belopnyut = Kroner(5811),
)

fun createBeregningYtelsesKomp() =
    BeregningYtelsesKomp(
        uforetrygdordiner = createUforetrygdOrdiner(),
        barnetilleggfelles = createBarnetilleggFelles(),
        barnetilleggserkull = createBarnetilleggSerkull(),
        ektefelletillegg = createEktefelletillegg(),
        gjenlevendetillegg = createGjenlevendetillegg(),
    )

fun createEktefelletillegg() =
    Ektefelletillegg(true)

fun createGjenlevendetillegg() =
    Gjenlevendetillegg(true)

fun createBarnetilleggSerkull() =
    BarnetilleggSerkull(
        avkortningsinformasjon = createAvkortningsInformasjonBT(),
        btsbinnvilget = true,
        btsbnetto = Kroner(8450),
        btsbinntektbruktiavkortning = Kroner(8451),
        btsbfribelop = Kroner(8452),
        antallbarnserkull = 2,
        btsbbruttoperar = Kroner(8590),
        btsbnettoperar = Kroner(19111),
    )

fun createBarnetilleggFelles() =
    BarnetilleggFelles(
        avkortningsinformasjon = createAvkortningsInformasjonBT(),
        btfbinnvilget = false,
        btfbnetto = Kroner(1200),
        btfbbrukersinntekttilavkortning = Kroner(1201),
        btfbinntektbruktiavkortning = Kroner(1202),
        btfbbelopfratrukketannenforeldersinntekt = Kroner(1203),
        btfbfribelop = Kroner(1204),
        btfbinntektannenforelder = Kroner(1205),
        antallbarnfelles = 2,
        btfbbruttoperar = Kroner(5819),
        btfbnettoperar = Kroner(10085)
    )

fun createAvkortningsInformasjonBT() =
    AvkortningsInformasjonBT(
        justeringsbelopperar = Kroner(1110),
        avkortingsbelopperar = Kroner(1111),
        fribelopperiodisert = true,
        inntektperiodisert = true,
        inntektstak = Kroner(1114),
    )

fun createUforetrygdOrdiner() =
    UforetrygdOrdiner(
        avkortningsinformasjon = createAvkortningsInformasjon(),
        fradrag = Kroner(3802),
        netto = Kroner(1201),
        nettoakk = Kroner(10134),
        minsteytelse = createMinsteytelse(),
        avkortingsbelopperar = Kroner(5819),
        brutto = Kroner(9832),
        nettorestar = Kroner(5934),
        ytelsesgrunnlag = createYtelsesgrunnlag()
    )

fun createYtelsesgrunnlag() =
    Ytelsesgrunnlag(
        beregningsgrunnlagordinar = createBeregningsgrunnlagOrdinar()
    )

fun createBeregningsgrunnlagOrdinar() =
    BeregningsgrunnlagOrdinar(
        antallarover1g = 10,
        antallarinntektiavtaleland = 11,
    )

fun createAvkortningsInformasjon(): AvkortningsInformasjon =
    AvkortningsInformasjon(
        oifu = Kroner(1910),
        oieu = Kroner(1911),
        belopsgrense = Kroner(1912),
        inntektsgrense = Kroner(1913),
        inntektstak = Kroner(1914),
        ugradertbruttoperar = Kroner(1915),
        kompensasjonsgrad = 5.0,
        utbetalingsgrad = 100,
        forventetinntekt = Kroner(1918),
        inntektsgrensenestear = Kroner(1919),
    )

fun createMinsteytelse() =
    Minsteytelse(3.5)