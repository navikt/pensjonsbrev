package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.maler.legacy.PESelectors.ExstreamFunctionsSelectors.pe_ut_nettoakk_pluss_nettorestar_pluss_forventetinntekt
import no.nav.pensjon.brev.api.model.maler.legacy.PESelectors.functions
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class TBU052V_TBU073V_SlikBlirDinUtbetalingFoerSkatt(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
//IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_pebrevkode <> "PE_UT_06_300") THEN      INCLUDE ENDIF

        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_06_300" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode().notEqualTo("PE_UT_04_109") and pe.pebrevkode().notEqualTo("PE_UT_06_300") and pe.pebrevkode().notEqualTo("PE_UT_07_200") and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()) and pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut().greaterThan(0))){
            //[TBU052V-TBU073V]

            paragraph {
                table(
                    header = {
                        column(columnSpan = 3) {
                            text (
                                Bokmal to "Slik blir din utbetaling før skatt",
                                Nynorsk to "Slik blir den månadlege utbetalinga di før skatt",
                                English to "This is your monthly payment before tax",
                                FontType.BOLD
                            )
                        }
                        column(columnSpan = 1, ColumnAlignment.RIGHT) {
                            text(
                                Bokmal to "",
                                Nynorsk to "",
                                English to "",
                            )
                        }
                    }
                ){
                    row {
                        cell {
                            text (
                                Bokmal to "Brutto beregnet uføretrygd som følge av innmeldt inntekt",
                                Nynorsk to "Brutto berekna uføretrygd som følgje av innmeld inntekt",
                                English to "Gross estimated disability benefit corresponding to a reported income",
                            )
                        }
                        cell {
                            textExpr (
                                Bokmal to pe.ut_nettoakk_pluss_nettorestar().format() + " kr",
                                Nynorsk to pe.ut_nettoakk_pluss_nettorestar().format() + " kr",
                                English to pe.ut_nettoakk_pluss_nettorestar().format() + " NOK",
                            )
                        }
                    }
                    row {
                        cell {
                            text (
                                Bokmal to "- Utbetalt uføretrygd hittil i år",
                                Nynorsk to "- Utbetalt uføretrygd hittil i år",
                                English to "- Disability benefit payments so far this year",
                            )
                        }
                        cell {
                            textExpr (
                                Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettoakk()
                                    .format() + " kr",
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettoakk()
                                    .format() + " kr",
                                English to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettoakk()
                                    .format() + " NOK",
                            )
                        }
                    }
                    row {
                        cell {
                            text (
                                Bokmal to "= Utbetaling av uføretrygd for resterende måneder i året",
                                Nynorsk to "= Utbetaling av uføretrygd for resterande månader i året",
                                English to "= Disability benefit payments for the remaining months of the year",
                            )
                        }
                        cell {
                            textExpr (
                                Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettorestar()
                                    .format() + " kr",
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettorestar()
                                    .format() + " kr",
                                English to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettorestar()
                                    .format() + " NOK",
                            )
                        }
                    }
                }
            }
        }.orShowIf(
            (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad()
                .lessThan(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad()) and pe.vedtaksdata_kravhode_kravarsaktype()
                .notEqualTo(
                    "soknad_bt"
                ) and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode()
                .notEqualTo("PE_UT_04_109") and pe.pebrevkode().notEqualTo("PE_UT_07_200") and pe.pebrevkode().notEqualTo("PE_UT_06_300"))){
            //[TBU052V-TBU073V]

            title1 {
                text (
                    Bokmal to "Slik blir din utbetaling før skatt",
                    Nynorsk to "Slik blir den månadlege utbetalinga di før skatt",
                    English to "This is your monthly payment before tax",
                )
            }
        }

        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_06_300" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
        showIf(
            (pe.vedtaksdata_beregningsdata_beregningufore_belopredusert() and pe.vedtaksdata_beregningsdata_beregningufore_totalnetto()
                .greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad()
                .lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_kravhode_kravarsaktype()
                .notEqualTo(
                    "soknad_bt"
                ) and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode()
                .notEqualTo("PE_UT_06_300") and pe.pebrevkode().notEqualTo("PE_UT_04_109") and pe.pebrevkode()
                .notEqualTo("PE_UT_07_200") and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()
                .lessThan(
                    pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()
                ) and pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut().greaterThan(0))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Du vil få en månedlig reduksjon i uføretrygden din på ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_fradrag()
                        .format() + " kroner i resterende måneder i kalenderåret.",
                    Nynorsk to "Du får ein månadleg reduksjon i uføretrygda di på ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_fradrag()
                        .format() + " kroner i resterande månader i kalenderåret.",
                    English to "Your monthly disability benefit payments will be reduced by NOK ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_fradrag()
                        .format() + " for the remaining months of the calendar year.",
                )
            }
        }

        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto > 0 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_06_300" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
        showIf(
            (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad()
                .lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_beregningsdata_beregningufore_totalnetto()
                .greaterThan(
                    0
                ) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.pebrevkode()
                .notEqualTo("PE_UT_04_108") and pe.pebrevkode().notEqualTo("PE_UT_04_109") and pe.pebrevkode()
                .notEqualTo("PE_UT_06_300") and pe.pebrevkode()
                .notEqualTo("PE_UT_07_200") and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()
                .lessThan(
                    pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()
                ) and pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut().greaterThan(0))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Uføretrygden og inntekten din vil ut fra dette til sammen utgjøre ".expr() + pe.functions.pe_ut_nettoakk_pluss_nettorestar_pluss_forventetinntekt.format() + " kroner for dette året.",
                    Nynorsk to "Uføretrygda di og inntekta di utgjer til saman ".expr() + pe.functions.pe_ut_nettoakk_pluss_nettorestar_pluss_forventetinntekt.format() + " kroner i dette året.",
                    English to "Your disability benefit and income together will total NOK ".expr() + pe.functions.pe_ut_nettoakk_pluss_nettorestar_pluss_forventetinntekt.format() + " for this year.",
                )
            }
        }

        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense >= PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_pebrevkode <> "PE_UT_06_300" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT = 0) THEN      INCLUDE ENDIF
        showIf(
            (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()
                .greaterThanOrEqual(
                    pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()
                ) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad()
                .lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_kravhode_kravarsaktype()
                .notEqualTo(
                    "soknad_bt"
                ) and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode()
                .notEqualTo("PE_UT_04_109") and pe.pebrevkode().notEqualTo("PE_UT_07_200") and pe.pebrevkode()
                .notEqualTo("PE_UT_06_300") and pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut().equalTo(0))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Utbetalingen av uføretrygden din er redusert, fordi du har inntekt. Den innmeldte inntekten er høyere enn inntektsgrensen din på ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()
                        .format() + " kroner og uføretrygden blir derfor ikke utbetalt. ",
                    Nynorsk to "Utbetalinga av uføretrygda di er redusert, fordi du har inntekt. Den innmelde inntekta er høgare enn inntektsgrensa di på ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()
                        .format() + " kroner og uføretrygda blir derfor ikkje utbetalt.",
                    English to "Your payment have been reduced because you have an income. The reported income is higher then your income cap of NOK ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()
                        .format() + ". Your disability benefit will therefore not be paid.",
                )
            }
        }

        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT = 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) THEN      INCLUDE ENDIF
        showIf(
            (pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut()
                .equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()
                .lessThan(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt()
                .greaterThan(
                    pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()
                ))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Du får ikke utbetalt uføretrygd siden inntekten din er høyere enn 80 prosent av inntekten du hadde før du ble ufør, det vil si ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()
                        .format() + " kroner.",
                    Nynorsk to "Du får ikkje utbetalt uføretrygd fordi inntekta di er høgare enn 80 prosent av inntekta du hadde før du blei ufør, det vil si ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()
                        .format() + " kroner.",
                    English to "You will not receive disability benefit because your income exceeds 80 percent of your income prior to disability, which is NOK ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()
                        .format() + ". ",
                )
            }
        }

        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_pebrevkode <> "PE_UT_06_300") THEN      INCLUDE ENDIF
        showIf(
            (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad()
                .lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_kravhode_kravarsaktype()
                .notEqualTo(
                    "soknad_bt"
                ) and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode()
                .notEqualTo("PE_UT_04_109") and pe.pebrevkode().notEqualTo("PE_UT_07_200") and pe.pebrevkode().notEqualTo("PE_UT_06_300"))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Du vil få tilbake ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()
                        .format() + " prosent uføretrygd uten søknad, dersom du tjener mindre enn inntektsgrensen din. Hvis du allerede har fått utbetalt det du har rett til i uføretrygd for kalenderåret, vil du ikke få utbetalt uføretrygd med den opprinnelige uføregraden din før neste kalenderår.",
                    Nynorsk to "Du får tilbake ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()
                        .format() + " prosent uføretrygd utan søknad dersom du tener mindre enn inntektsgrensa di. Dersom du allereie har fått utbetalt det du har rett til i uføretrygd for kalenderåret, får du ikkje utbetalt uføretrygd med den opphavlege uføregraden din før neste kalenderår.",
                    English to "You will get payed the full amount of your disability benefit if your income falls below your income limit. If you already have been paid what you are entitled to in disability benefits this calendar year, you will not receive any disability benefit payments at your original degree of disability until the next calendar year.".expr(),
                )
            }
        }
    }
}
