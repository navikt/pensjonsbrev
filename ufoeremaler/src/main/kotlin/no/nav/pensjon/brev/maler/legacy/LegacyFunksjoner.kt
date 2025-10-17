@file:Suppress("FunctionName", "DuplicatedCode")

package no.nav.pensjon.brev.maler.legacy

import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.VedtaksdataSelectors.beregningsdata_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.VedtaksdataSelectors.kravhode_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.VedtaksdataSelectors.vilkarsvedtaklist_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.BeregningsDataSelectors.beregningufore_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BarnetilleggFellesYKSelectors.belopgammelbtfb_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BarnetilleggFellesYKSelectors.belopnybtfb_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BarnetilleggSerkullYKSelectors.belopgammelbtsb_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BarnetilleggSerkullYKSelectors.belopnybtsb_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BelopsendringSelectors.barnetilleggfellesyk_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BelopsendringSelectors.barnetilleggserkullyk_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BeregningUforeSelectors.belopsendring_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BeregningUforeSelectors.beregningytelseskomp_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.BarnetilleggFellesSelectors.antallbarnfelles_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.BarnetilleggFellesSelectors.btfbnetto_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.BarnetilleggSerkullSelectors.antallbarnserkull_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.BarnetilleggSerkullSelectors.btsbnetto_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.BeregningYtelsesKompSelectors.barnetilleggfelles_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.BeregningYtelsesKompSelectors.barnetilleggserkull_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.kravhode.KravhodeSelectors.kravarsaktype_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.kravhode.KravhodeSelectors.kravmottattdato_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.VilkarsVedtakListSelectors.vilkarsvedtak_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.VilkarsVedtakSelectors.beregningsvilkar_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.beregningsvilkar.BeregningsVilkarSelectors.virkningstidpunkt_safe
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.ufore.api.model.maler.legacy.PE
import no.nav.pensjon.brev.ufore.api.model.maler.legacy.PESelectors.vedtaksbrev
import no.nav.pensjon.brev.ufore.api.model.maler.legacy.PESelectors.vedtaksbrev_safe
import no.nav.pensjon.brev.ufore.api.model.maler.legacy.vedtaksbrev.VedtaksbrevSelectors.vedtaksdata
import no.nav.pensjon.brev.ufore.api.model.maler.legacy.vedtaksbrev.VedtaksbrevSelectors.vedtaksdata_safe
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


fun Expression<PE>.ut_trygdetid(): Expression<Boolean> =
    vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and
            pebrevkode().notEqualTo("PE_UT_04_108") and
            pebrevkode().notEqualTo("PE_UT_04_109") and
            pebrevkode().notEqualTo("PE_UT_07_200") and
            pebrevkode().notEqualTo("PE_UT_06_300") and
            (
                    (pebrevkode().equalTo("PE_UT_04_101") or pebrevkode().equalTo("PE_UT_04_114")) or
                            (pebrevkode().notEqualTo("PE_UT_05_100") and pebrevkode().notEqualTo("PE_UT_07_100")
                                    and vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().lessThan(40))
                    )



fun Expression<PE>.ut_tbu056v() = (
        pebrevkode().equalTo("PE_UT_04_102")
                or pebrevkode().equalTo("PE_UT_04_116")
                or pebrevkode().equalTo("PE_UT_04_101")
                or pebrevkode().equalTo("PE_UT_04_114")
                or pebrevkode().equalTo("PE_UT_04_300")
                or pebrevkode().equalTo("PE_UT_14_300")
                or pebrevkode().equalTo("PE_UT_04_500")
                or (vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")
                and vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopgammelut().notEqualTo(
            vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut()
        )
                )
        ) and vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().lessThan(
    vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()
)

fun Expression<PE>.ut_tbu056v_51() = (
        pebrevkode().equalTo("PE_UT_04_102")
                or pebrevkode().equalTo("PE_UT_04_116")
                or pebrevkode().equalTo("PE_UT_04_101")
                or pebrevkode().equalTo("PE_UT_04_114")
                or pebrevkode().equalTo("PE_UT_04_300")
                or pebrevkode().equalTo("PE_UT_14_300")
                or pebrevkode().equalTo("PE_UT_04_500")
                or (vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")
                and vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopgammelut().notEqualTo(
            vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut()
        )
                )
        ) and vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and
        vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup() and
        vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().lessThan(
            vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()
        )


fun Expression<PE>.pe_ut_tbu601v_tbu604v(): Expression<Boolean> {
    val belopsendring = vedtaksbrev_safe.vedtaksdata_safe.beregningsdata_safe.beregningufore_safe.belopsendring_safe
    return vedtaksbrev_safe.vedtaksdata_safe.kravhode_safe.kravarsaktype_safe.equalTo("endret_inntekt") and
            (belopsendring.barnetilleggfellesyk_safe.belopgammelbtfb_safe.notEqualTo(belopsendring.barnetilleggfellesyk_safe.belopnybtfb_safe) or
                    belopsendring.barnetilleggserkullyk_safe.belopgammelbtsb_safe.notEqualTo(belopsendring.barnetilleggserkullyk_safe.belopnybtsb_safe))
}

fun FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(date: Expression<LocalDate?>): Expression<Boolean> =
    date.ifNull(LocalDate.of(2020, 2, 2)).month.equalTo(1) and
            date.ifNull(LocalDate.of(2020, 2, 2)).day.equalTo(1)


fun FUNKSJON_Year(date: Expression<LocalDate?>): Expression<Int> =
    date.ifNull(LocalDate.of(1000,1,1)).year // I exstream er Year(null) = 1000

fun FUNKSJON_Month(date: Expression<LocalDate?>): Expression<Int> =
    date.ifNull(LocalDate.of(1000,1,1)).month

fun Expression<PE>.ut_trygdetid_avdod() = (vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pebrevkode().notEqualTo("PE_UT_07_100") and vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd") and pebrevkode().notEqualTo("PE_UT_05_100") and vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg() and pebrevkode().notEqualTo("PE_UT_04_108") and pebrevkode().notEqualTo("PE_UT_04_109") and pebrevkode().notEqualTo("PE_UT_04_500") and pebrevkode().notEqualTo("PE_UT_07_200") and pebrevkode().notEqualTo("PE_UT_06_300"))

fun Expression<PE>.ut_barnet_barna_felles(): Expression<String> {
    val erEngelsk = Expression.FromScope.Language.equalTo(English.expr())
    val erEttBarn = vedtaksbrev_safe.vedtaksdata_safe.beregningsdata_safe.beregningufore_safe.beregningytelseskomp_safe.barnetilleggfelles_safe.antallbarnfelles_safe.ifNull(0).equalTo(1)
    val erFlereBarn = vedtaksbrev_safe.vedtaksdata_safe.beregningsdata_safe.beregningufore_safe.beregningytelseskomp_safe.barnetilleggfelles_safe.antallbarnfelles_safe.ifNull(0).greaterThan(1)
    return ifElse(
        erEttBarn, ifElse(erEngelsk, "child", "barnet"),
        ifElse(
            erFlereBarn,
            ifElse(erEngelsk, "children", "barna"),
            "".expr()
        )
    )
}

fun Expression<PE>.ut_barnet_barna_serkull(): Expression<String> {
    val erEngelsk = Expression.FromScope.Language.equalTo(English.expr())
    val erEttBarn = vedtaksbrev_safe.vedtaksdata_safe.beregningsdata_safe.beregningufore_safe.beregningytelseskomp_safe.barnetilleggserkull_safe.antallbarnserkull_safe.ifNull(0).equalTo(1)
    val erFlereBarn = vedtaksbrev_safe.vedtaksdata_safe.beregningsdata_safe.beregningufore_safe.beregningytelseskomp_safe.barnetilleggserkull_safe.antallbarnserkull_safe.ifNull(0).greaterThan(1)
    return ifElse(
        erEttBarn, ifElse(erEngelsk, "child", "barnet"),
        ifElse(
            erFlereBarn,
            ifElse(erEngelsk, "children", "barna"),
            "".expr()
        )
    )
}

fun Expression<PE>.pe_ut_barnet_barna_felles_serkull(): Expression<String> {
    val erEngelsk = Expression.FromScope.Language.equalTo(English.expr())
    val beregningytelseskomp = vedtaksbrev_safe.vedtaksdata_safe.beregningsdata_safe.beregningufore_safe.beregningytelseskomp_safe

    val barnetilleggserkull = beregningytelseskomp.barnetilleggserkull_safe
    val barnetilleggfelles = beregningytelseskomp.barnetilleggfelles_safe

    val erEttBarn = (barnetilleggfelles.antallbarnfelles_safe.ifNull(0).equalTo(1) and
            barnetilleggfelles.btfbnetto_safe.ifNull(Kroner(0)).equalTo(0)) or
            (barnetilleggserkull.antallbarnserkull_safe.ifNull(0).equalTo(1) and
                    barnetilleggserkull.btsbnetto_safe.ifNull(Kroner(0)).equalTo(0))

    val erFlereBarn = (barnetilleggfelles.antallbarnfelles_safe.ifNull(0).greaterThan(1) and
            barnetilleggfelles.btfbnetto_safe.ifNull(Kroner(0)).equalTo(0)) or
            (barnetilleggserkull.antallbarnserkull_safe.ifNull(0).greaterThan(1) and
                    barnetilleggserkull.btsbnetto_safe.ifNull(Kroner(0)).equalTo(0))
    return ifElse(
        erEttBarn, ifElse(erEngelsk, "child", "barnet"),
        ifElse(
            erFlereBarn,
            ifElse(erEngelsk, "children", "barna"),
            "".expr()
        )
    )
}

fun Expression<PE>.ut_inntektsgrense_faktisk() =
    ifElse(
        vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrensenestear().equalTo(0),
        vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense(),
        vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrensenestear()
    )

//IF FF_GetArrayElement_Date(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Virkningstidpunkt) >= DateValue("01/01/2016") THEN
//   isGrater = true
//ENDIF
fun Expression<PE>.ut_virkningstidpunktstorreenn01012016() = vedtaksbrev.vedtaksdata_safe.vilkarsvedtaklist_safe.vilkarsvedtak_safe.getOrNull().beregningsvilkar_safe.virkningstidpunkt_safe.legacyGreaterThan(LocalDate.of(2016,1,1))

fun Expression<PE>.ut_periodefomstorre0101() = vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom().legacyGreaterThan(ut_firstday())

fun Expression<PE>.ut_periodetommindre3112() = vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodetom().legacyLessThan(ut_lastday())

fun Expression<PE>.ut_sum_inntekterbt_totalbeloput(): Expression<Kroner> = vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterbt().ifNull(Kroner(0)) + vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput()

fun Expression<PE>.ut_firstday(): Expression<LocalDate?> = vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom().ifNull(LocalDate.of(2014,10,14)).firstDayOfYear
fun Expression<PE>.ut_lastday(): Expression<LocalDate?> = vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom().ifNull(LocalDate.of(2014,10,14)).lastDayOfYear

fun Expression<PE>.aarstall_trygdetid(): Expression<Int> =
    vedtaksbrev.vedtaksdata.kravhode_safe.kravmottattdato_safe.ifNull(LocalDate.now()).year

fun Expression<PE>.aars_trygdetid(): Expression<String> {
    val erEngelsk = Expression.FromScope.Language.equalTo(English)

    return ifElse(aarstall_trygdetid() lessThan 2021, ifElse(erEngelsk, "three", "tre"), ifElse(erEngelsk, "five", "fem"))
}


fun Expression<PE>.ut_ikke(): Expression<String> {
    val erEngelsk = Expression.FromScope.Language.equalTo(English.expr())
    val erBokmal = Expression.FromScope.Language.equalTo(Nynorsk.expr())
    val erNynorsk = Expression.FromScope.Language.equalTo(Bokmal.expr())
    return ifElse(
        vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbbrutto()),
        ifElse(erEngelsk,"not ".expr(), ifElse(erBokmal,"ikke ".expr(), ifElse(erNynorsk,"ikkje ".expr(), "".expr()))),
        "".expr()
    )
}

fun Expression<PE>.ut_sumutbt_hoyere_lavere(): Expression<String> =
    hoyereLavere(vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumutbt().greaterThan(vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu()))

fun Expression<PE>.ut_inntekt_hoyere_lavere(): Expression<String> =
    hoyereLavere(this.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().greaterThan(this.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop()))

private fun hoyereLavere(hoyereCondition: Expression<Boolean>): Expression<String> {
    val erEngelsk = Expression.FromScope.Language.equalTo(English.expr())
    val erBokmal = Expression.FromScope.Language.equalTo(Nynorsk.expr())
    val erNynorsk = Expression.FromScope.Language.equalTo(Bokmal.expr())
    val hoyere = hoyereCondition
    return ifElse(
        hoyere,
        ifElse(erBokmal, "høyere".expr(), ifElse(erNynorsk, "høgare".expr(), ifElse(erEngelsk, "higher".expr(), "".expr()))),
        ifElse(erEngelsk, "lavere".expr(), ifElse(erBokmal, "lågare".expr(), ifElse(erNynorsk, "lower".expr(), "".expr())))
    )
}

fun Expression<PE>.ut_barnet_barna_serkull_en_entall_flertall(): Expression<String> =
    ifElse(vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_antallbarnserkull().equalTo(1) and Expression.FromScope.Language.equalTo(English.expr()), "s".expr(), "".expr())

fun Expression<PE>.ut_barnet_barna_felles_en_entall_flertall(): Expression<String> =
    ifElse(vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().equalTo(1) and Expression.FromScope.Language.equalTo(English.expr()), "s".expr(), "".expr())

fun Expression<PE>.ut_virkningstidpunkttilprosent(): Expression<Int> {
    val year = vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().ifNull(LocalDate.of(1000,1,1)).year
    return ifElse(year.equalTo(2017), 107.expr(),
            ifElse(year.equalTo(2018), 104.expr(),
                ifElse(year.equalTo(2019), 101.expr(),
                    ifElse(year.equalTo(2020), 98.expr(), 110.expr())))
    )
}

fun Expression<PE>.ut_virkningstidpunktar(): Expression<Int> =
    vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().ifNull(LocalDate.of(1000,1,1)).year

//IF(PE_UT_KravLinjeKode_VedtakResultat_Antall(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_innv) = 1)
//THEN IF UCase(PE_XML_brev_spraak) ='EN'  THEN value = "child"
//          ELSE value = "barnet"
//          END IF
//ELSEIF (PE_UT_KravLinjeKode_VedtakResultat_Antall(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_innv) > 1)
//THEN IF UCase(PE_XML_brev_spraak) ='EN'  THEN value = "children"
//          ELSE value = "barna"
//          END IF
//
//ENDIF
fun Expression<PE>.ut_barnet_barna_innvilget(): Expression<String> {
    val erEngelsk = Expression.FromScope.Language.equalTo(English.expr())
    val erEttBarn = foedselsdatoTilBarnTilleggErInnvilgetFor().size().ifNull(0).equalTo(1)
    val erFlereBarn = foedselsdatoTilBarnTilleggErInnvilgetFor().size().ifNull(0).greaterThan(1)
    return ifElse(
        erEttBarn, ifElse(erEngelsk, "child", "barnet"),
        ifElse(
            erFlereBarn,
            ifElse(erEngelsk, "children", "barna"),
            "".expr()
        )
    )
}

// GENERATED
fun Expression<PE>.inkluderopplysningerbruktiberegningen() = ((not(vedtaksdata_faktoromregnet()) and pebrevkode().notEqualTo("PE_UT_04_102")) or (pebrevkode().equalTo("PE_UT_04_102") and (vedtaksdata_beregningsdata_beregningufore_belopokt() or (vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse() and vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(0)) or vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse().equalTo("stdbegr_12_8_2_5")) or vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod")))
fun Expression<PE>.ut_etteroppgjor_bt_utbetalt(): Expression<Boolean> = not((vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0)))
fun Expression<PE>.ut_tbu605(): Expression<Boolean> = ((vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and not(vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().greaterThan(0) or (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0) and vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0)))) or (not(vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().greaterThan(0) or (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0) and vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0)))) and ut_etteroppgjor_bt_utbetalt())
fun Expression<PE>.ut_tbu605v_eller_til_din(): Expression<Boolean> = (vedtaksdata_kravhode_kravarsaktype().notEqualTo("endret_inntekt"))
fun Expression<PE>.ut_tbu606v_tbu608v(): Expression<Boolean> = (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and ut_etteroppgjor_bt_utbetalt())
fun Expression<PE>.ut_tbu606v_tbu611v(): Expression<Boolean> = ((vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and (pebrevkode().equalTo("PE_UT_04_101") or (pebrevkode().equalTo("PE_UT_04_102") and vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod")) or pebrevkode().equalTo("PE_UT_04_114") or pebrevkode().equalTo("PE_UT_04_300") or pebrevkode().equalTo("PE_UT_14_300") or pebrevkode().equalTo("PE_UT_06_100") or pebrevkode().equalTo("PE_UT_04_103") or pebrevkode().equalTo("PE_UT_04_106") or pebrevkode().equalTo("PE_UT_04_108") or pebrevkode().equalTo("PE_UT_04_109") or pebrevkode().equalTo("PE_UT_07_200") or pebrevkode().equalTo("PE_UT_06_300")) or (vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt") and (vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggfellesyk_belopgammelbtfb().notEqualTo(vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggfellesyk_belopnybtfb()) or vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggserkullyk_belopgammelbtsb().notEqualTo(vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggserkullyk_belopnybtsb()))) and ut_etteroppgjor_bt_utbetalt())
fun Expression<PE>.ut_tbu608_far_ikke(): Expression<Boolean> = (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0) and vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))
fun Expression<PE>.ut_tbu609v_tbu611v(): Expression<Boolean> = (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and ut_etteroppgjor_bt_utbetalt())
fun Expression<PE>.ut_tbu611_far_ikke(): Expression<Boolean> = (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0) and vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0))
fun Expression<PE>.ut_tbu613v(): Expression<Boolean> = (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and ut_etteroppgjor_bt_utbetalt())
fun Expression<PE>.ut_tbu613v_1_3(): Expression<Boolean> = (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().greaterThan(0) or (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0) and vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0)))
fun Expression<PE>.ut_tbu613v_4_5(): Expression<Boolean> = (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().greaterThan(0) or (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0) and vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0)))
fun Expression<PE>.ut_tbu069v(): Expression<Boolean> = ((vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and (pebrevkode().equalTo("PE_UT_04_101") or (pebrevkode().equalTo("PE_UT_04_102") and vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod")) or pebrevkode().equalTo("PE_UT_04_114") or pebrevkode().equalTo("PE_UT_04_300") or pebrevkode().equalTo("PE_UT_14_300") or pebrevkode().equalTo("PE_UT_06_100") or pebrevkode().equalTo("PE_UT_04_103") or pebrevkode().equalTo("PE_UT_04_106") or pebrevkode().equalTo("PE_UT_04_108") or pebrevkode().equalTo("PE_UT_04_109") or pebrevkode().equalTo("PE_UT_07_200") or pebrevkode().equalTo("PE_UT_06_300")) and ut_etteroppgjor_bt_utbetalt())
fun Expression<PE>.ut_periodetomstorrelik3112(): Expression<Boolean> = (vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodetom().legacyGreaterThanOrEqual(ut_lastday()))
fun Expression<PE>.ut_btsbinnvilget_og_justbeloppa() = (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0))
fun Expression<PE>.ut_btsbinnvilget_ikke_btfbinnvilget() = (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and not(vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()))
fun Expression<PE>.ut_periodefommindrelik0101(): Expression<Boolean> = (vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom().legacyLessThanOrEqual(ut_firstday()))
fun Expression<PE>.ut_tbu4050(): Expression<Boolean> = (vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() and vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb() and vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) and vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().equalTo(0))
fun Expression<PE>.ut_tbu4051(): Expression<Boolean> = (vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() and vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb() and vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().equalTo(0) and vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
fun Expression<PE>.ut_periodefomstorre0101periodetomlik3112(): Expression<Boolean> = (vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom().legacyGreaterThan(ut_firstday()) and vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodetom().equalTo(ut_lastday()))
fun Expression<PE>.ut_periodetommindre3112periodefomlik0101(): Expression<Boolean> = (vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodetom().legacyLessThan(ut_lastday()) and vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom().equalTo(ut_firstday()))
fun Expression<PE>.ut_periodetommindre3112periodefomstorre0101(): Expression<Boolean> = (vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodetom().legacyLessThan(ut_lastday()) and vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom().legacyGreaterThan(ut_firstday()))
fun Expression<PE>.ut_etteroppgjoravviksbeloptsbogtfbuliknull(): Expression<Boolean> = (vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
fun Expression<PE>.ut_avvikbtikkeut(): Expression<Boolean> = ((vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0) or vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0)) and vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().equalTo(0))
fun Expression<PE>.inkludervedleggopplysningerometteroppgjoeret() = not(pebrevkode().equalTo("PE_UT_04_401") and (vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("ikke_avvik") or vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("etterbet_tolgr") or vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr_tolgr")))
fun Expression<PE>.ut_tbu1286_del1() = (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and ((vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) and vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0)) or (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))))
fun Expression<PE>.ut_tbu1286_del2() = ((vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0)))
fun Expression<PE>.ut_tbu1286_del3() = ((vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0)))
fun Expression<PE>.ut_avdod() = ((vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pebrevkode().notEqualTo("PE_UT_07_100") and vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd") and pebrevkode().notEqualTo("PE_UT_05_100") and not(vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()) and pebrevkode().notEqualTo("PE_UT_04_114") and vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodordiner_opptjeningutliste_opptjeningut_ar().notEqualTo(0)) and pebrevkode().notEqualTo("PE_UT_04_108") and pebrevkode().notEqualTo("PE_UT_04_109") and pebrevkode().notEqualTo("PE_UT_07_200") and pebrevkode().notEqualTo("PE_UT_04_500") and pebrevkode().notEqualTo("PE_UT_07_200") and pebrevkode().notEqualTo("PE_UT_06_300") and (pebrevkode().notEqualTo("PE_UT_04_102") or (pebrevkode().equalTo("PE_UT_04_102") and vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod"))))
fun Expression<PE>.ut_tbu501v() = ((vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and (pebrevkode().equalTo("PE_UT_04_101") or (pebrevkode().equalTo("PE_UT_04_102") and vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod")) or pebrevkode().equalTo("PE_UT_04_114") or pebrevkode().equalTo("PE_UT_04_300") or pebrevkode().equalTo("PE_UT_14_300") or pebrevkode().equalTo("PE_UT_06_100") or pebrevkode().equalTo("PE_UT_04_103") or pebrevkode().equalTo("PE_UT_04_106") or pebrevkode().equalTo("PE_UT_04_108") or pebrevkode().equalTo("PE_UT_04_109") or pebrevkode().equalTo("PE_UT_07_200")))