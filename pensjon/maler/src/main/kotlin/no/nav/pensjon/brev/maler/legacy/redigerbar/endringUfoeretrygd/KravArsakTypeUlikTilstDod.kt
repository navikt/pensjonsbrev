package no.nav.pensjon.brev.maler.legacy.redigerbar.endringUfoeretrygd

import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.oensketVirkningsDato
import no.nav.pensjon.brev.api.model.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall
import no.nav.pensjon.brev.maler.legacy.ut_inntektsgrense_faktisk
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravarsaktype
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravgjelder
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravmottatdato
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_onsketvirkningsdato
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text
import kotlin.text.format

object KravArsakTypeUlikTilstDod {

    data class TBU2287til2297(
        val pe: Expression<PE>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            ifNotNull(pe.vedtaksdata_kravhode_onsketvirkningsdato()) { oensketVirkningsDato ->
                //IF(PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_uu" AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_ys" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "barn_endret_inntekt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "eps_endret_inntekt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "begge_for_end_inn" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "omgj_etter_klage" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "omgj_etter_anke" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_kravhode_kravgjelder()
                        .notEqualTo("sok_uu") and pe.vedtaksdata_kravhode_kravgjelder()
                        .notEqualTo("sok_ys") and pe.vedtaksdata_kravhode_kravarsaktype()
                        .notEqualTo("endring_ifu") and pe.vedtaksdata_kravhode_kravarsaktype()
                        .notEqualTo("endret_inntekt") and pe.vedtaksdata_kravhode_kravarsaktype()
                        .notEqualTo("barn_endret_inntekt") and pe.vedtaksdata_kravhode_kravarsaktype()
                        .notEqualTo("eps_endret_inntekt") and pe.vedtaksdata_kravhode_kravarsaktype()
                        .notEqualTo("begge_for_end_inn") and pe.vedtaksdata_kravhode_kravarsaktype()
                        .notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype()
                        .notEqualTo("instopphold") and pe.vedtaksdata_kravhode_kravarsaktype()
                        .notEqualTo("omgj_etter_klage") and pe.vedtaksdata_kravhode_kravarsaktype()
                        .notEqualTo("omgj_etter_anke") and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()))
                ) {
                    //[TBU2287EN, TBU2287, TBU2287NN]
                    ifNotNull(pe.vedtaksdata_kravhode_onsketvirkningsdato()) { oensketVirkningsDato ->
                        paragraph {
                            text(
                                bokmal { +"Vi har endret uføretrygden din fra " + oensketVirkningsDato.format() + "." },
                                nynorsk { +"Vi har endra uføretrygda di frå " + oensketVirkningsDato.format() + "." },
                                english { +"We have changed your disability benefit effective as of " + oensketVirkningsDato.format() + "." },
                            )
                        }
                    }
                }

                //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) AND PE_Vedtaksdata_Kravhode_KravArsakType = "soknad_bt" ) THEN      INCLUDE ENDIF
                showIf(
                    ((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()
                            or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())
                            and pe.vedtaksdata_kravhode_kravarsaktype().equalTo("soknad_bt"))
                ) {
                    //[TBU2288]
                    ifNotNull(pe.vedtaksdata_kravhode_onsketvirkningsdato()) { oensketVirkningsDato ->
                        paragraph {
                            text(
                                bokmal {
                                    +"Vi har innvilget søknaden din om barnetillegg som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato()
                                        .format() +
                                            ". Vi har endret uføretrygden din fra " + oensketVirkningsDato.format() + "."
                                },
                                nynorsk {
                                    +"Vi har innvilga søknaden din om barnetillegg som vi fekk "
                                    +pe.vedtaksdata_kravhode_kravmottatdato()
                                        .format() + ". Vi har endra uføretrygda di frå "
                                    +oensketVirkningsDato.format() + "."
                                },
                                english {
                                    +"We have granted your application for child supplement, received by us on "
                                    +pe.vedtaksdata_kravhode_kravmottatdato()
                                        .format() + ". We have changed your disability benefit, effective as of "
                                    +oensketVirkningsDato.format() + ". "
                                },
                            )

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND  (((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0)   AND   (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0))   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false))  ) THEN      INCLUDE ENDIF
                            showIf(
                                (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto()
                                    .equalTo(0)) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                                    .equalTo(0))) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto()
                                    .equalTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                                    .equalTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))))
                            ) {
                                text(
                                    bokmal { +" Tillegget blir ikke utbetalt fordi inntekten til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " er over grensen for å få utbetalt barnetillegg." },
                                    nynorsk { +" Tillegget blir ikkje utbetalt, fordi inntekta til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din er over grensa for å få utbetalt barnetillegg." },
                                    english { +"You will not receive child supplement because your income and your " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner() + "'s income exceeds the income limit. " },
                                )
                            }

                            //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)  AND  (((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0)   AND   (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0))   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false))  ) THEN      INCLUDE ENDIF
                            showIf(
                                ((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and not(
                                    pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()
                                )) and (((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto()
                                    .equalTo(0)) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                                    .equalTo(0))) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto()
                                    .equalTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                                    .equalTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))))
                            ) {
                                text(
                                    bokmal { +" Tillegget blir ikke utbetalt fordi inntekten din er over grensen for å få utbetalt barnetillegg." },
                                    nynorsk { +" Tillegget blir ikkje utbetalt fordi inntekta di er over grensa for å få utbetalt barnetillegg." },
                                    english { +"You will not receive child supplement because your income exceeds the income limit." },
                                )
                            }
                        }
                    }
                }

                //IF(  (PE_Vedtaksdata_Kravhode_KravArsakType = "omgj_etter_klage" OR PE_Vedtaksdata_Kravhode_KravArsakType = "omgj_etter_anke")  ) THEN      INCLUDE ENDIF
                showIf(
                    ((pe.vedtaksdata_kravhode_kravarsaktype()
                        .equalTo("omgj_etter_klage") or pe.vedtaksdata_kravhode_kravarsaktype()
                        .equalTo("omgj_etter_anke")))
                ) {
                    //[TBU3332EN, TBU3332, TBU3332NN]
                    ifNotNull(pe.vedtaksdata_kravhode_onsketvirkningsdato()) { oensketVirkningsDato ->
                        paragraph {
                            text(
                                bokmal { +"Vi har endret uføretrygden din fra " + oensketVirkningsDato.format() + " fordi du har fått medhold i klagen din." },
                                nynorsk { +"Vi har endra uføretrygda di frå " + oensketVirkningsDato.format() + " fordi du har fått medhald i klaga di." },
                                english { +"We have changed your disability benefit effective as of " + oensketVirkningsDato.format() + ", because your appeal has been successful." },
                            )
                        }
                    }
                }

                //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND  (PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt" OR  PE_Vedtaksdata_Kravhode_KravArsakType = "barn_endret_inntekt" OR PE_Vedtaksdata_Kravhode_KravArsakType = "eps_endret_inntekt" OR PE_Vedtaksdata_Kravhode_KravArsakType = "begge_for_end_inn")    ) THEN      INCLUDE ENDIF
                showIf(
                    ((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and (pe.vedtaksdata_kravhode_kravarsaktype()
                        .equalTo("endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype()
                        .equalTo("barn_endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype()
                        .equalTo("eps_endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype()
                        .equalTo("begge_for_end_inn")))
                ) {
                    //[TBU2289EN, TBU2289, TBU2289NN]

                    paragraph {
                        text(
                            bokmal { +"Vi har endret barnetillegget i uføretrygden din fordi du har meldt fra om inntektsendring." },
                            nynorsk { +"Vi har endra barnetillegget i uføretrygda di fordi du har meldt frå om inntektsendring." },
                            english { +"We have changed the child supplement in your disability benefit, because you have reported a change in income." },
                        )
                    }
                }

                //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
                showIf(
                    (FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(
                        pe.ut_konst_kralinjekode_bt(),
                        pe.ut_konst_vilkarsvedtakresultat_opphor()
                    ))
                ) {
                    //TBU2290,
                    paragraph {
                        text(
                            bokmal { +"Vi har vedtatt at barnetillegget i uføretrygden din opphører fra " + oensketVirkningsDato.format() + " for barn født " + pe.ut_fodselsdatobarn() },
                            nynorsk { +"Vi har stansa barnetillegget i uføretrygda di frå " + oensketVirkningsDato.format() + " for barn fødd " + pe.ut_fodselsdatobarn() },
                            english { +"The child supplement in your disability benefit has been discontinued, effective as of " + oensketVirkningsDato.format() + ", for child born " + pe.ut_fodselsdatobarn_en() },
                        )
                    }
                }

                //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_et, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
                showIf(
                    (FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(
                        pe.ut_konst_kralinjekode_et(),
                        pe.ut_konst_vilkarsvedtakresultat_opphor()
                    ))
                ) {
                    //[TBU2291EN, TBU2291, TBU2291NN]
                    ifNotNull(pe.vedtaksdata_kravhode_onsketvirkningsdato()) { oensketVirkningsDato ->
                        paragraph {
                            text(
                                bokmal { +"Vi har opphørt ektefelletillegget i uføretrygden din fra " + oensketVirkningsDato.format() + "." },
                                nynorsk { +"Vi har stansa ektefelletillegget i uføretrygda di frå " + oensketVirkningsDato.format() + "." },
                                english { +"The spouse supplement in your disability benefit has been discontinued, effective as of " + oensketVirkningsDato.format() + "." },
                            )
                        }
                    }
                }

//PE_Vedtaksdata_Kravhode_KravGjelder = "sok_uu"
                showIf(pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_uu")) {
                    //[TBU2292EN, TBU2292, TBU2292NN]

                    paragraph {
                        text(
                            bokmal {
                                +"Vi har innvilget søknaden din om rettighet som ung ufør som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato()
                                    .format() + ". Vi har endret uføretrygden din fra " + oensketVirkningsDato.format() + "."
                            },
                            nynorsk {
                                +"Vi har innvilga søknaden din om å få rettar som ung ufør som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato()
                                    .format() + ". Vi har endra uføretrygda di frå " + oensketVirkningsDato.format() + "."
                            },
                            english {
                                +"We have granted your application for your disability benefit to be calculated in accordance with special rights for young disabled individuals, which we received on " + pe.vedtaksdata_kravhode_kravmottatdato()
                                    .format() + ". We have changed your disability benefit effective as of " + oensketVirkningsDato.format() + "."
                            },
                        )
                    }
                }

                //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) = FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) AND PE_Vedtaksdata_Kravhode_KravGjelder = "sok_ys") THEN      INCLUDE ENDIF
                showIf(
                    (FUNKSJON_FF_GetArrayElement_Float(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).equalTo(
                        FUNKSJON_FF_GetArrayElement_Float(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad())
                    ) and pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_ys"))
                ) {
                    //[TBU2293EN, TBU2293, TBU2293NN]

                    paragraph {
                        text(
                            bokmal {
                                +"Vi har innvilget søknaden din om uføretrygd etter særbestemmelsene for yrkesskade eller yrkessykdom som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato()
                                    .format() + ". Vi har kommet fram til at hele uførheten din skyldes en godkjent yrkesskade eller yrkessykdom, og uføretrygden din er endret fra " + oensketVirkningsDato.format() + "."
                            },
                            nynorsk {
                                +"Vi har innvilga søknaden din om uføretrygd etter særreglane for yrkesskade eller yrkessjukdom som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato()
                                    .format() + ". Vi har kome fram til at heile uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom, og uføretrygda di er endra frå " + oensketVirkningsDato.format() + "."
                            },
                            english {
                                +"We have granted your application for disability benefit in accordance with special rules in connection with occupational injury or occupational illness, which we received on " + pe.vedtaksdata_kravhode_kravmottatdato()
                                    .format() + ". We have concluded that your disability in its entirety was caused by a certified occupational injury or occupational illness, and your disability benefit has been changed accordingly, effective as of " + oensketVirkningsDato.format() + "."
                            },
                        )
                    }
                }

                //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravGjelder = "sok_ys") THEN      INCLUDE ENDIF
                showIf(
                    (FUNKSJON_FF_GetArrayElement_Float(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).lessThan(
                        pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()
                    ) and pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_ys"))
                ) {
                    //[TBU2294EN, TBU2294, TBU2294NN]

                    paragraph {
                        text(
                            bokmal {
                                +"Vi har innvilget søknaden din om uføretrygd etter særbestemmelsene for yrkesskade eller yrkessykdom som vi mottok "
                                +pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har kommet fram til at "
                                +pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad()
                                    .format() + " prosent av uførheten din skyldes en godkjent yrkesskade eller yrkessykdom, og uføretrygden din er endret fra "
                                +pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "."
                            },
                            nynorsk {
                                +"Vi har innvilga søknaden din om uføretrygd etter særreglane for yrkesskade eller yrkessjukdom som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato()
                                    .format() + ". Vi har kome fram til at " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad()
                                    .format() + " prosent av uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom, og uføretrygda di er endra frå " + oensketVirkningsDato.format() + "."
                            },
                            english {
                                +"We have granted your application for disability benefit in accordance with special rules in connection with occupational injury or occupational illness, which we received on " + pe.vedtaksdata_kravhode_kravmottatdato()
                                    .format() + ". We have concluded that " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad()
                                    .format() + " percent of your disability was caused by a certified occupational injury or occupational illness, and your disability benefit has been changed accordingly, effective as of " + oensketVirkningsDato.format() + "."
                            },
                        )
                    }
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endring_ifu"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endring_ifu")) {
                    //[TBU2295EN, TBU2295, TBU2295NN]
                    ifNotNull(pe.ut_inntektsgrense_faktisk()) { inntektsgrense ->
                        paragraph {
                            text(
                                bokmal { +"Vi har innvilget søknaden din om endring av inntektsgrense." },
                                nynorsk { +"Vi har innvilga søknaden din om endring av inntektsgrense." },
                                english { +"We have granted your application for alteration of your income limit." },
                            )
                            text(
                                bokmal {
                                    +"Den nye inntektsgrensen din har økt til " + inntektsgrense.format() + " kroner fra " + oensketVirkningsDato.format() + "."
                                },
                                nynorsk {
                                    +"Den nye inntektsgrensa di har auka til " + inntektsgrense.format() + " kroner frå " + oensketVirkningsDato.format() + "."
                                },
                                english {
                                    +"Your new income limit has increased to NOK " + inntektsgrense.format() + " from " + oensketVirkningsDato.format() + "."
                                },
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_kravhode_kravarsaktype()
                        .notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))
                ) {
                    //[TBU2296EN, TBU2296, TBU2296NN]

                    paragraph {
                        text(
                            bokmal {
                                +"Vi har innvilget deg gjenlevenderettigheter i uføretrygden din. Vi har endret uføretrygden din fra " + oensketVirkningsDato.format() + "."
                            },
                            nynorsk {
                                +"Vi har innvilga deg attlevanderettar i uføretrygda di. Vi har endra uføretrygda di frå " + oensketVirkningsDato.format() + "."
                            },
                            english {
                                +"You have been granted survivor's rights in connection with your disability benefit. We have changed your disability benefit, effective as of " + oensketVirkningsDato.format() + "."
                            },
                        )
                    }
                }

                //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_ut_gjt, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
                showIf(
                    (FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(
                        pe.ut_konst_kralinjekode_ut_gjt(),
                        pe.ut_konst_vilkarsvedtakresultat_opphor()
                    ))
                ) {
                    //[TBU2297EN, TBU2297, TBU2297NN]

                    paragraph {
                        text(
                            bokmal {
                                +"Vi har opphørt gjenlevendetillegget i uføretrygden din fra " + oensketVirkningsDato.format() + "."
                            },
                            nynorsk {
                                +"Vi har stansa attlevandetillegget i uføretrygda di frå " + oensketVirkningsDato.format() + "."
                            },
                            english {
                                +"The survivor's supplement in your disability benefit has been discontinued, effective as of " + oensketVirkningsDato.format() + "."
                            },
                        )
                    }
                }
            }
        }

        data class TBU3333til2223(
            val pe: Expression<PE>
        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
            override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
                ifNotNull(pe.vedtaksdata_kravhode_onsketvirkningsdato()) { oensketVirkningsDato ->
                    //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "instopphold" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
                    showIf(
                        (pe.vedtaksdata_kravhode_kravarsaktype()
                            .equalTo("instopphold") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype()
                            .equalTo("reduksjon_hs"))
                    )
                    {
                        //[TBU3333EN, TBU3333, TBU3333NN]
                        paragraph {
                            text(
                                bokmal { +"Vi har endret uføretrygden din fra " + oensketVirkningsDato.format() + " fordi du ble innlagt på institusjon." },
                                nynorsk { +"Vi har endra uføretrygda di frå " + oensketVirkningsDato.format() + " fordi du ble innlagd på institusjon." },
                                english { +"We have changed your disability benefit effective as of " + oensketVirkningsDato.format() + ", because you have been admitted to an institution." },
                            )
                        }
                    }
                }
            }
        }
    }
}