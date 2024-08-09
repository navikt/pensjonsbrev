@file:Suppress("FunctionName")

package no.nav.pensjon.brev.maler.fraser

import kotlinx.serialization.descriptors.PrimitiveKind
import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.maler.legacy.PESelectors.vedtaksbrev_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.VedtaksbrevSelectors.vedtaksdata_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.VedtaksdataSelectors.beregningsdata_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.VedtaksdataSelectors.kravhode_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.BeregningsDataSelectors.beregningufore_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BarnetilleggFellesYKSelectors.belopgammelbtfb_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BarnetilleggFellesYKSelectors.belopnybtfb_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BarnetilleggSerkullYKSelectors.belopgammelbtsb_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BarnetilleggSerkullYKSelectors.belopnybtsb_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BelopsendringSelectors.barnetilleggfellesyk_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BelopsendringSelectors.barnetilleggserkullyk_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BeregningUforeSelectors.belopokt_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BeregningUforeSelectors.belopsendring_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BeregningUforeSelectors.beregningytelseskomp_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BeregningUforeSelectors.uforetrygdberegning_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.AvkortningsInformasjonSelectors.utbetalingsgrad_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.BarnetilleggFellesSelectors.antallbarnfelles_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.BarnetilleggFellesSelectors.btfbnetto_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.BarnetilleggSerkullSelectors.antallbarnserkull_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.BarnetilleggSerkullSelectors.btsbnetto_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.BeregningYtelsesKompSelectors.barnetilleggfelles_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.BeregningYtelsesKompSelectors.barnetilleggserkull_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.BeregningYtelsesKompSelectors.uforetrygdordiner_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.UforetrygdOrdinerSelectors.avkortningsinformasjon_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.uforetrygdberegning.UforetrygdberegningSelectors.mottarminsteytelse_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.kravhode.KravhodeSelectors.kravarsaktype_safe
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


fun FUNKSJON_PE_UT_Trygdetid(
    PE_pebrevkode: Expression<String>,
    PE_Vedtaksdata_Kravhode_KravArsakType: Expression<String>,
    PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid: Expression<Int>,
): Expression<Boolean> =
    PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("soknad_bt") and
            PE_pebrevkode.notEqualTo("PE_UT_04_108") and
            PE_pebrevkode.notEqualTo("PE_UT_04_109") and
            PE_pebrevkode.notEqualTo("PE_UT_07_200") and
            PE_pebrevkode.notEqualTo("PE_UT_06_300") and
            (
                    (PE_pebrevkode.equalTo("PE_UT_04_101") or PE_pebrevkode.equalTo("PE_UT_04_114")) or
                            (PE_pebrevkode.notEqualTo("PE_UT_05_100") and PE_pebrevkode.notEqualTo("PE_UT_07_100")
                                    and PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid.lessThan(40))
                    )



fun FUNKSJON_PE_UT_TBU056V(
    PE_pebrevkode: Expression<String>,
    PE_Vedtaksdata_Kravhode_KravArsakType: Expression<String>,
    PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT: Expression<Kroner>,
    PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT: Expression<Kroner>,
    PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense: Expression<Kroner>,
    PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak: Expression<Kroner>,
) = (
        PE_pebrevkode.equalTo("PE_UT_04_102")
                or PE_pebrevkode.equalTo("PE_UT_04_116")
                or PE_pebrevkode.equalTo("PE_UT_04_101")
                or PE_pebrevkode.equalTo("PE_UT_04_114")
                or PE_pebrevkode.equalTo("PE_UT_04_300")
                or PE_pebrevkode.equalTo("PE_UT_14_300")
                or PE_pebrevkode.equalTo("PE_UT_04_500")
                or (PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("endret_inntekt")
                and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.notEqualTo(
            PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT
        )
                )
        ) and PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("soknad_bt") and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.lessThan(
    PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak
)

fun FUNKSJON_PE_UT_TBU056V_51(
    PE_pebrevkode: Expression<String>,
    PE_Vedtaksdata_Kravhode_KravArsakType: Expression<String>,
    PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT: Expression<Kroner>,
    PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT: Expression<Kroner>,
    PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense: Expression<Kroner>,
    PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak: Expression<Kroner>,
    PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP: Expression<Boolean>,
) = (
        PE_pebrevkode.equalTo("PE_UT_04_102")
                or PE_pebrevkode.equalTo("PE_UT_04_116")
                or PE_pebrevkode.equalTo("PE_UT_04_101")
                or PE_pebrevkode.equalTo("PE_UT_04_114")
                or PE_pebrevkode.equalTo("PE_UT_04_300")
                or PE_pebrevkode.equalTo("PE_UT_14_300")
                or PE_pebrevkode.equalTo("PE_UT_04_500")
                or (PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("endret_inntekt")
                and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.notEqualTo(
            PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT
        )
                )
        ) and PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("soknad_bt") and
        PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP and
        PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.lessThan(
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak
        )


fun Expression<PE>.pe_ut_tbu601v_tbu604v(): Expression.BinaryInvoke<Boolean, Boolean, Boolean> {
    val belopsendring = vedtaksbrev_safe.vedtaksdata_safe.beregningsdata_safe.beregningufore_safe.belopsendring_safe
    return vedtaksbrev_safe.vedtaksdata_safe.kravhode_safe.kravarsaktype_safe.equalTo("endret_inntekt") and
            (belopsendring.barnetilleggfellesyk_safe.belopgammelbtfb_safe.notEqualTo(belopsendring.barnetilleggfellesyk_safe.belopnybtfb_safe) or
                    belopsendring.barnetilleggserkullyk_safe.belopgammelbtsb_safe.notEqualTo(belopsendring.barnetilleggserkullyk_safe.belopnybtsb_safe))
}

fun FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(date: Expression<LocalDate?>): Expression<Boolean> =
    date.ifNull(LocalDate.of(2020, 2, 2)).month.equalTo(1) and
            date.ifNull(LocalDate.of(2020, 2, 2)).day.equalTo(1)

fun Expression<PE>.pe_ut_barnet_barna_felles(): Expression<String> {
    val erEngelsk = Expression.FromScope.Language.equalTo(Language.English.expr())
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

fun Expression<PE>.pe_ut_barnet_barna_serkull(): Expression<String> {
    val erEngelsk = Expression.FromScope.Language.equalTo(Language.English.expr())
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
    val erEngelsk = Expression.FromScope.Language.equalTo(Language.English.expr())
    val beregningytelseskomp = vedtaksbrev_safe.vedtaksdata_safe.beregningsdata_safe.beregningufore_safe.beregningytelseskomp_safe

    val barnetilleggserkull = beregningytelseskomp.barnetilleggserkull_safe
    val barnetilleggfelles = beregningytelseskomp.barnetilleggfelles_safe

    val erEttBarn = (barnetilleggfelles.antallbarnfelles_safe.ifNull(0).equalTo(1) and
            barnetilleggfelles.btfbnetto_safe.ifNull(0).equalTo(0)) or
            (barnetilleggserkull.antallbarnserkull_safe.ifNull(0).equalTo(1) and
                    barnetilleggserkull.btsbnetto_safe.ifNull(0).equalTo(0))

    val erFlereBarn = (barnetilleggfelles.antallbarnfelles_safe.ifNull(0).greaterThan(1) and
            barnetilleggfelles.btfbnetto_safe.ifNull(0).equalTo(0)) or
            (barnetilleggserkull.antallbarnserkull_safe.ifNull(0).greaterThan(1) and
                    barnetilleggserkull.btsbnetto_safe.ifNull(0).equalTo(0))
    return ifElse(
        erEttBarn, ifElse(erEngelsk, "child", "barnet"),
        ifElse(
            erFlereBarn,
            ifElse(erEngelsk, "children", "barna"),
            "".expr()
        )
    )
}


//doInclude = false
//IF(
//
//(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true-
//OR-
//PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true
//)
//AND
//(PE_pebrevkode = "PE_UT_04_101"-
//OR (PE_pebrevkode = "PE_UT_04_102" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "tilst_dod")
//OR PE_pebrevkode = "PE_UT_04_114"-
//OR PE_pebrevkode = "PE_UT_04_300"-
//OR PE_pebrevkode = "PE_UT_14_300"
//OR PE_pebrevkode = "PE_UT_06_100"-
//OR PE_pebrevkode = "PE_UT_04_103"-
//OR PE_pebrevkode = "PE_UT_04_106"
//OR PE_pebrevkode = "PE_UT_04_108"-
//OR PE_pebrevkode = "PE_UT_04_109"
//OR PE_pebrevkode = "PE_UT_07_200"
//OR PE_pebrevkode = "PE_UT_06_300")
//AND PE_UT_Etteroppgjor_BT_Utbetalt() = true
//) THEN
//    doInclude = true
//ENDIF

fun Expression<PE>.pe_ut_tbu069v() = vedtaksbrev_safe.vedtaksdata_safe.beregningsdata_safe.beregningufore_safe.beregningytelseskomp_safe((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget or PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget) and (PE_pebrevkode.equalTo("PE_UT_04_101") or (PE_pebrevkode.equalTo("PE_UT_04_102") and PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("tilst_dod")) or PE_pebrevkode.equalTo("PE_UT_04_114") or PE_pebrevkode.equalTo("PE_UT_04_300") or PE_pebrevkode.equalTo("PE_UT_14_300") or PE_pebrevkode.equalTo("PE_UT_06_100") or PE_pebrevkode.equalTo("PE_UT_04_103") or PE_pebrevkode.equalTo("PE_UT_04_106") or PE_pebrevkode.equalTo("PE_UT_04_108") or PE_pebrevkode.equalTo("PE_UT_04_109") or PE_pebrevkode.equalTo("PE_UT_07_200") or PE_pebrevkode.equalTo("PE_UT_06_300")) and PE_UT_Etteroppgjor_BT_Utbetalt())

