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
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor.*
import no.nav.pensjon.brev.api.model.maler.legacy.personsak.PSfnr
import no.nav.pensjon.brev.api.model.maler.legacy.personsak.PersonSak
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.Vedtaksbrev
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.Vedtaksdata
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.BeregningsData
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.*
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.*
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.uforetrygdberegning.Uforetrygdberegning
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregninguforeperiode.BeregningUforePeriode
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.etteroppgjorresultat.Etteroppgjoerresultat
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.forrigeetteroppgjor.EoEndringBruker
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.forrigeetteroppgjor.EoEndringEps
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.forrigeetteroppgjor.ForrigeEtteroppgjor
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.kravhode.Kravhode
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.kravhode.Kravlinje
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.VilkarsVedtakList
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.Vilkar
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.VilkarsVedtak
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.beregningsvilkar.BeregningsVilkar
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.beregningsvilkar.Trygdetid
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.beregningsvilkar.ttutlandtrygdeavtaleliste.TTUtlandTrygdeAvtale
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.beregningsvilkar.ttutlandtrygdeavtaleliste.TTUtlandTrygdeAvtaleListe
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year
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
            pe_ut_sum_fattnorge_fatteos = 10,
            pe_ut_sum_fattnorge_fatt_a10_netto = 10,
            pe_ut_sum_fattnorge_fattbilateral = 11,
            pe_ut_antallbarnserkullogfelles = 12,
            fratrekkliste_inntektsgrunnlag_grunnikkereduksjon_har_etterslepsinnt_avslt_akt = true,
            fratrekkliste_inntektsgrunnlag_grunnikkereduksjon_har_erstatning_innttap_erstoppgj = true,
            pe_ut_etteroppgjorfratrekklistebrukeretterbetaling = true,
            pe_ut_inntekt_trukket_fra_personinntekt = Kroner(1088),
            pe_ut_grunnikkereduksjon_lik_erstatning_innttap_ertstoppgj_finnes = false,
            pe_ut_kravlinjekode_vedtakresultat_forekomst_bt_innv = 1
        ),
        pebrevkode = "PE_UT_06_300",
        personsak = PersonSak(PSfnr("01019878910"), LocalDate.of(1998, 1, 1))
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
        omsorgAar = listOf(Year(1990), Year(1989)),
        personbostedsland = "nor",
        trygdetidsgrunnlaglistebilateral = createTrygdetidsgrunnlagListeBilateral(),
        trygdetidsgrunnlaglisteeos = createTrygdetidsgrunnlagListeEOS(),
        trygdetidsgrunnlaglistenor = createTrygdetidsgrunnlagListeNor(),
        uforetrygdetteroppgjor = createUforetrygdEtteroppgjor(),
    )

fun createUforetrygdEtteroppgjor(): UforetrygdEtteroppgjor =
    UforetrygdEtteroppgjor(
        barnetilleggfb = true,
        barnetilleggsb = true,
        periodefom = LocalDate.of(2020, 1, 1),
        periodetom = LocalDate.of(2020, 1, 1),
        uforetrygdetteroppgjordetaljbruker = createUforetrygdEtteroppgjorDetaljBruker(),
        uforetrygdetteroppgjordetaljeps = createUforetrygdEtteroppgjorDetaljEps()
    )

private fun createUforetrygdEtteroppgjorDetaljBruker() = UforetrygdEtteroppgjorDetaljBruker(
    fratrekkliste = createFratrekkListe(),
    sumfratrekkut = Kroner(22),
    suminntekterut = Kroner(23),
    inntektliste = createInntektsListe(),
    suminntekterbt = Kroner(12),
    sumfratrekkbt = Kroner(14)
)

private fun createInntektsListe() =
    InntektListe(
        inntektsgrunnlag = listOf(
            createInntektsgrunnlag()
        )
    )

private fun createUforetrygdEtteroppgjorDetaljEps() = UforetrygdEtteroppgjorDetaljEPS(

    inntektliste = createInntektsListe(),
    fratrekkListe = createFratrekkListe(),
    suminntekterbt = Kroner(11),
    sumfratrekkbt = Kroner(13)
)

private fun createFratrekkListe() = FratrekkListe(
    inntektsgrunnlag = listOf(
        createInntektsgrunnlag()
    )
)

private fun createInntektsgrunnlag(): Inntektsgrunnlag = Inntektsgrunnlag(
    grunnikkereduksjon = "grunnikkereduksjon",
    belop = Kroner(1234),
    inntekttype = "inntekttype",
    registerkilde = "registerkilde"
)

fun createTrygdetidsgrunnlagListeBilateral() =
    TrygdetidsgrunnlagListeBilateral(
        listOf(
            createTrygdetidsgrunnlagBilateral()
        )
    )

private fun createTrygdetidsgrunnlagBilateral() = TrygdetidsgrunnlagBilateral(
    trygdetidbilateralland = "usa",
    trygdetidfombilateral = LocalDate.of(2020, 1, 5),
    trygdetidtombilateral = LocalDate.of(2020, 5, 5),
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
    etteroppgjorresultat = Etteroppgjoerresultat(
        avviksbelop = Kroner(5450),
        avviksbeloptfb = Kroner(5451),
        avviksbeloptsb = Kroner(5452),
        avviksbeloput = Kroner(5453),
        etteroppgjorresultattype = "etteroppgjorresultattype",
        inntektut = Kroner(6464),
        tidligerebeloptfb = Kroner(5454),
        tidligerebeloptsb = Kroner(5455),
        tidligerebeloput = Kroner(5456),
        totalbeloptfb = Kroner(5457),
        totalbeloptsb = Kroner(5458),
        totalbeloput = Kroner(5459),
    ),
    forrigeetteroppgjor = ForrigeEtteroppgjor(
        eoendringeps = EoEndringEps(
            endretpensjonogandreytelser = true,
            endretpgi = true
        ),
        eoendringbruker = EoEndringBruker(
            endretpensjonogandreytelser = true,
            endretpgi = true
        ),
        resultatforrigeeo = "",
        tidligereeoiverksatt = true
    ),
)

fun createVilkarsVedtakList() =
    VilkarsVedtakList(
        listOf(createVilkarsVedtak())
    )

fun createVilkarsVedtak() =
    VilkarsVedtak(
        beregningsvilkar = createBeregningsVilkar(),
        vilkar = createVilkar(),
        vilkarvirkningfom = LocalDate.of(2020, 1, 1),
        vilkarVirkningFom = LocalDate.of(2020, 1, 1), // TODO: fjern
        vilkarkravlinjekode = "bt",
        vilkarvedtakresultat = "avsl",
    )

fun createVilkar() =
    Vilkar(
        alderbegrunnelse = "stdbegr_12_4_1_i_1",
        alderresultat = "oppfylt",
        fortsattmedlemsskapresultat = "oppfylt",
        forutgaendemedlemskapresultat = "oppfylt",
        hensiktsmessigarbeidsrettedetiltakbegrunnelse = "stdbegr_12_5_2_i_3",
        hensiktsmessigarbeidsrettedetiltakresultat = "ikke_oppfylt",
        hensiktsmessigbehandlingbegrunnelse = "stdbegr_12_5_2_i_3",
        hensiktsmessigbehandlingresultat = "oppfylt",
        nedsattinntektsevnebegrunnelse = "stdbegr_12_7_2_i_2",
        nedsattinntektsevneresultat = "ikke_oppfylt",
        sykdomskadelytebegrunnelse = "stdbegr_12_5_2_i_3",
        sykdomskadelyteresultat = "ikke_oppfylt",
        unguforbegrunnelse = "stdbegr_12_13_1_i_3",
        unguforresultat = "unguforresultat",
        yrkesskadebegrunnelse = "stdbegr_12_17_1_o_1",
        yrkesskaderesultat = "ikke_oppfylt",
    )

fun createBeregningsVilkar() =
    BeregningsVilkar(
        ieubegrunnelse = null,
        ieuinntekt = Kroner(9939),
        ifubegrunnelse = null,
        ifuinntekt = Kroner(9938),
        skadetidspunkt = LocalDate.of(2020, 1, 1),
        trygdetid = createTrygdetid(),
        uforegrad = 100,
        uforetidspunkt = LocalDate.now().minusYears(5),
        virkningstidpunkt = LocalDate.of(2020, 2, 12),
        yrkesskadegrad = 29,
    )

fun createTrygdetid() =
    Trygdetid(
        fatteos = 30,
        framtidigtteos = 31,
        framtidigttnorsk = 32,
        redusertframtidigtrygdetid = true,
        fattnorge = 34,
        tttellereos = 50,
        ttnevnereos = 51,
        ttnordisk = 52,
        tttellernordisk = 53,
        ttnevnernordisk = 54,
        faTTA10Netto = 55,
        ttutlandtrygdeavtaleliste = TTUtlandTrygdeAvtaleListe(
            ttutlandtrygdeavtale = listOf(
                TTUtlandTrygdeAvtale(
                    fattbilateral = 100
                )
            )
        ),
    )

fun createKravhode() =
    Kravhode(
        boddarbeidutland = true,
        brukerkonvertertup = false,
        kravarsaktype = "endring_ifu",
        kravgjelder = "kravgjelder",
        kravmottattdato = LocalDate.of(2020, 1, 1),
        kravlinjeliste = listOf(createKravlinje()),
        vurderetrygdeavtale = false,
    )

fun createKravlinje() = Kravlinje(kravlinjetype = "ut")

fun createBeregningsData() =
    BeregningsData(
        beregningufore = createBeregningUfore(),
        beregningantallperioder = 1,
        beregninguforeperiode = createBeregningUforePeriode()
    )

fun createBeregningUforePeriode() =
    listOf(
        BeregningUforePeriode(
            beregningytelseskomp = createBeregningYtelsesKomp(),
            uforetrygdberegning = createUforetrygdberegning(),
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
        beregningbrukersivilstand = "gift",
    )

fun createUforetrygdberegning() =
    Uforetrygdberegning(
        anvendttrygdetid = 20,
        mottarminsteytelse = false,
        uforegrad = 100,
        yrkesskadegrad = 0,
        grunnbelop = Kroner(1010),
        instoppholdtype = "instoppholdtype",
        beregningsmetode = "beregningsmetode",
        uforetidspunkt = LocalDate.of(2020,1,1),
        proratabrokteller = 10,
        proratabroknevner = 11,
        instopphanvendt = true
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
    Gjenlevendetillegg(
        gtinnvilget = true,
        nyttgjenlevendetillegg = true
    )

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
        btsbfradrag = Kroner(19112),
        btsbbrutto = Kroner(19113),
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
        btfbnettoperar = Kroner(10085),
        btfbbrutto = Kroner(9832),
        btfbfradrag = Kroner(3802),
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
        beregningsgrunnlagordinar = createBeregningsgrunnlagOrdinar(),
        beregningsgrunnlagyrkesskadearsbelop = Kroner(5810),
        inntektvedskadetidspunktet = Kroner(1010),
    )

fun createBeregningsgrunnlagOrdinar() =
    BeregningsgrunnlagOrdinar(
        antallarover1g = 10,
        antallarinntektiavtaleland = 11,
        beregningsgrunnlagordinerarsbelop = Kroner(10589),
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