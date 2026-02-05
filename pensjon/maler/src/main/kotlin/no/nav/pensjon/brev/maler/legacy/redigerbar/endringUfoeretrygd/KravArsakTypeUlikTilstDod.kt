package no.nav.pensjon.brev.maler.legacy.redigerbar.endringUfoeretrygd

import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.oensketVirkningsDato
import no.nav.pensjon.brev.api.model.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.grunnlag_persongrunnlagsliste_personbostedsland
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall
import no.nav.pensjon.brev.maler.legacy.ut_forsorgeransvar_ingen_er_false
import no.nav.pensjon.brev.maler.legacy.ut_forsorgeransvar_ingen_er_true
import no.nav.pensjon.brev.maler.legacy.ut_inntektsgrense_faktisk
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefellegarantitillegg_egtinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etnetto
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_belopokt
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_belopredusert
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_total
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_totalnetto
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforetidspunkt
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravarsaktype
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravgjelder
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravmottatdato
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_onsketvirkningsdato
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsbegrunnelse
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_skadetidspunkt
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text
import kotlin.and
import kotlin.or
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
                            bokmal { +"Vi har vedtatt at barnetillegget i uføretrygden din opphører fra " + oensketVirkningsDato.format() + " for barn født " + pe.ut_fodselsdato.format() + "." },
                            nynorsk { +"Vi har stansa barnetillegget i uføretrygda di frå " + oensketVirkningsDato.format() + " for barn fødd " + pe.ut_fodselsdato.format + "." },
                            english { +"The child supplement in your disability benefit has been discontinued, effective as of " + oensketVirkningsDato.format() + ", for child born " + pe.ut_fodselsdatobarn_en() + "." },
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

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
                    showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))) {
                        //[TBU3334EN, TBU3334, TBU3334NN]
                        paragraph {
                            text(
                                bokmal { +"Vi har mottatt opplysninger om at du er innlagt på institusjon. Dette får ikke betydning for uføretrygden din, og du vil få utbetalt det samme som før." },
                                nynorsk { +"Vi har fått opplysningar om at du er innlagd på institusjon. Dette får ikkje noko å seie for uføretrygda di, og du får utbetalt det same som før." },
                                english { +"We have been informed that you have been admitted to an institution. This will not affect your disability benefit, and you will be paid the same amount." },
                            )

                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  AND  PE_UT_Forsorgeransvar_ingen_er_false()  AND PE_Vedtaksdata_Kravhode_KravArsakType ="instopphold" AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0)  THEN      INCLUDE ENDIF
                            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksdata_kravhode_kravarsaktype().equalTo("instopphold") and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0))) {
                                text(
                                    bokmal { +" Fra og med måneden etter at du ble innlagt på institusjon vil du bli vurdert med sivilstand som enslig. Det betyr at barnetillegget kun vil bli beregnet ut fra den samlede inntekten din fra samme tidspunkt." },
                                    nynorsk { +" Frå og med månaden etter at du blei innlagd på institusjon vil du bli vurdert med ein sivilstand som einsleg. Det betyr at barnetillegget berre blir berekna ut frå den samla inntekta di frå same tidspunkt. " },
                                    english { +" You are considered to be single from the month after you were admitted to an institution. This means that the child supplement is calculated on the base of a single parent." },
                                )
                            }
                        }
                    }

                    //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "instopphold" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("instopphold") and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))) {
                        //[TBU3363EN, TBU3363, TBU3363NN]
                        paragraph {
                            text(
                                bokmal { +"Vi har mottatt opplysninger om at du er under straffegjennomføring. Dette får ikke betydning for uføretrygden din, og du vil få utbetalt det samme som før." },
                                nynorsk { +"Vi har fått opplysningar om at du er under straffegjennomføring. Dette får ikkje noko å seie for uføretrygda di, og du får utbetalt det same som før." },
                                english { +"We have been informed that you are serving a criminal sentence. This will not affect your disability benefit, and you will be paid the same amount." },
                            )

                            // IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  AND  PE_UT_Forsorgeransvar_ingen_er_false()  AND PE_Vedtaksdata_Kravhode_KravArsakType ="instopphold" AND  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true))  THEN      INCLUDE ENDIF
                            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksdata_kravhode_kravarsaktype().equalTo("instopphold") and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))) {
                                text(
                                    bokmal { +" Fra og med måneden etter at du er under straffegjennomføring vil du bli vurdert med sivilstand som enslig. Det betyr at barnetillegget kun vil bli beregnet ut fra den samlede inntekten din fra samme tidspunkt." },
                                    nynorsk { +" Frå og med månaden etter at du er under straffegjennomføringa vil du bli vurdert med ein sivilstand som einsleg. Det betyr at barnetillegget berre blir berekna ut frå den samla inntekta di frå same tidspunkt." },
                                    english { +" You are considered to be single from the month after you started serving a criminal sentence. This means that the child supplement is calculated on the base of a single parent." },
                                )
                            }
                        }
                    }

                    //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "instopphold" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("instopphold") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))) {
                        //[TBU3335EN, TBU3335, TBU3335NN]
                        paragraph {
                            text(
                                bokmal { +"Vi har endret utbetalingen av uføretrygden din fra " + oensketVirkningsDato.format() + " fordi du er under straffegjennomføring." },
                                nynorsk { +"Vi har stansa utbetalinga av uføretrygda di frå " + oensketVirkningsDato.format() + " fordi du er under straffegjennomføring." },
                                english { +"We have ceased payment of your disability benefit, effective as of " + oensketVirkningsDato.format() + ", because you are a serving a criminal sentence." },
                            )
                        }
                    }

                    //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "instopphold" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("instopphold") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo(""))) {
                        //[TBU3337EN, TBU3337, TBU3337NN]
                        paragraph {
                            text(
                                bokmal { +"Vi har gjenopptatt utbetaling av uføretrygden din fra " + oensketVirkningsDato.format() + "." },
                                nynorsk { +"Vi har teke til att med å utbetale uføretrygda di frå " + oensketVirkningsDato.format() + "." },
                                english { +"We have resumed payment of your disability benefit, effective as of " + oensketVirkningsDato.format() + "." },
                            )
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false) THEN      INCLUDE ENDIF
                    showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                        //[TBU1120, TBU1120NN, TBU1120EN]
                        paragraph {
                            text(
                                bokmal { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd per måned før skatt." },
                                nynorsk { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd per månad før skatt." },
                                english { +"Your monthly disability benefit payment will be NOK " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " before tax." },
                            )
                        }
                    }

                    //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  ) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                        //[TBU1121, TBU1121NN, TBU1121EN]
                        paragraph {
                            text(
                                bokmal { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd og barnetillegg per måned før skatt." },
                                nynorsk { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd og barnetillegg per månad før skatt." },
                                english { +"Your monthly disability benefit and child supplement payment will be NOK " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " before tax." },
                            )
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false) THEN      INCLUDE ENDIF
                    showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                        //[TBU1122, TBU1122NN, TBU1122EN]
                        paragraph {
                            text(
                                bokmal { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd og gjenlevendetillegg per måned før skatt." },
                                nynorsk { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd og attlevandetillegg per månad før skatt." },
                                english { +"Your monthly disability benefit and survivor's supplement payment will be NOK " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " before tax." },
                            )
                        }
                    }

                    //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false ) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                        //[TBU1123, TBU1123NN, TBU1123EN]
                        paragraph {
                            text(
                                bokmal { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd, barne- og gjenlevendetillegg per måned før skatt." },
                                nynorsk { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd, barne- og attlevandetillegg per månad før skatt." },
                                english { +"Your monthly disability benefit, child supplement and survivor's supplement payment will be NOK " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " before tax." },
                            )
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETnetto > 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etnetto().greaterThan(0))) {
                        //[TBU1253, TBU1253EN, TBU1253NN]
                        paragraph {
                            text(
                                bokmal { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd og ektefelletillegg per måned før skatt." },
                                nynorsk { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd og ektefelletillegg per månad før skatt." },
                                english { +"Your monthly disability benefit and spouse supplement payment will be NOK " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " before tax." },
                            )
                        }
                    }

                    //IF(  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETnetto > 0  AND   ((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0))  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  ) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etnetto().greaterThan(0) and ((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().greaterThan(0)) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().greaterThan(0))) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                        //[TBU1254, TBU1254EN, TBU1254NN]
                        paragraph {
                            text(
                                bokmal { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd, barne- og ektefelletillegg per måned før skatt." },
                                nynorsk { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd, barne- og ektefelletillegg per månad før skatt." },
                                english { +"Your monthly disability benefit, child supplement and spouse supplement payment will be NOK " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " before tax." },
                            )
                        }
                    }

                    //IF(   PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad  AND  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  ) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and (not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                        //[TBU2298EN, TBU2298, TBU2298NN]
                        paragraph {
                            text(
                                bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd per måned før skatt. Fordi du har inntekt ved siden av uføretrygden, vil utbetalingen din bli redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per måned før skatt." },
                                nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd per månad før skatt. Fordi du har inntekt ved sida av uføretrygda, blir utbetalinga di redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per månad før skatt." },
                                english { +"You are entitled to a monthly disability benefit payment of NOK " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " before tax. Because you earn an income in addition to your disability benefit, your payment will be reduced to NOK " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " per month before tax." },
                            )
                        }
                    }

                    //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND   (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)   AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  ) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                        //[TBU2299EN, TBU2299, TBU2299NN]
                        paragraph {
                            text(
                                bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd og barnetillegg per måned før skatt. Fordi du har inntekt ved siden av uføretrygden, vil utbetalingen din bli redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per måned før skatt." },
                                nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd og barnetillegg per månad før skatt. Fordi du har inntekt ved sida av uføretrygda, blir utbetalinga di redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per månad før skatt." },
                                english { +"You are entitled to a monthly disability benefit and child supplement payment of NOK " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " before tax. Because you earn an income in addition to your disability benefit, your payment will be reduced to NOK " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " per month before tax." },
                            )
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                        //[TBU2300EN, TBU2300, TBU2300NN]
                        paragraph {
                            text(
                                bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd og gjenlevendetillegg per måned før skatt. Fordi du har inntekt ved siden av uføretrygden, vil utbetalingen din bli redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per måned før skatt." },
                                nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd og attlevandetillegg per månad før skatt. Fordi du har inntekt ved sida av uføretrygda, blir utbetalinga di redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per månad før skatt." },
                                english { +"You are entitled to a monthly disability benefit and survivor's supplement payment of NOK " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " before tax. Because you earn an income in addition to your disability benefit, your payment will be reduced to NOK " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " per month before tax." },
                            )
                        }
                    }

                    //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad  AND   (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)   AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  ) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and (not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) or not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                        //[TBU2301EN, TBU2301, TBU2301NN]
                        paragraph {
                            text(
                                bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd og ektefelletillegg per måned før skatt. Fordi du har inntekt ved siden av uføretrygden, vil utbetalingen din bli redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per måned før skatt." },
                                nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd og ektefelletillegg per månad før skatt. Fordi du har inntekt ved sida av uføretrygda, blir utbetalinga di redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per månad før skatt." },
                                english { +"You are entitled to a monthly disability benefit and spouse supplement payment of NOK " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " before tax. Because you earn an income in addition to your disability benefit, your payment will be reduced to NOK " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " per month before tax." },
                            )
                        }
                    }

                    //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false ) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                        //[TBU2302EN, TBU2302, TBU2302NN]
                        paragraph {
                            text(
                                bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd, barne- og gjenlevendetillegg per måned før skatt. Fordi du har inntekt ved siden av uføretrygden, vil utbetalingen din bli redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per måned før skatt." },
                                nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd, barne- og attlevandetillegg per månad før skatt. Fordi du har inntekt ved sida av uføretrygda, blir utbetalinga di redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per månad før skatt." },
                                english { +"You are entitled to a monthly disability benefit, child supplement and survivor's supplement payment of NOK " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " before tax. Because you earn an income in addition to your disability benefit, your payment will be reduced to NOK " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " per month before tax." },
                            )
                        }
                    }

                    //IF(  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo" AND PE_UT_Forsorgeransvar_ingen_er_true()  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true   ) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and pe.ut_forsorgeransvar_ingen_er_true() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt())) {
                        //[TBU3101EN, TBU3101NN, TBU3101]
                        paragraph {
                            text(
                                bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men den kommer ikke til utbetaling fordi du er under straffegjennomføring." },
                                nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men ho blir ikkje betalt ut fordi du er under straffegjennomføring." },
                                english { +"You are entitled to receive a disability benefit of NOK " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + ", but this will not be paid out because you are serving a sentence." },
                            )
                        }
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt())) {
                        //[TBU3102EN, TBU3102NN, TBU3102]
                        paragraph {
                            text(
                                bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalingen er redusert fordi du er innlagt på institusjon. I denne perioden vil du få utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                                nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalinga er redusert fordi du er innlagd på institusjon. I denne perioden får du betalt ut " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                                english { +"You are entitled to receive NOK " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " in disability benefit, but the payment has been reduced because you have been admitted to an institution. During your stay in the institution you will receive NOK " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + "." },
                            )
                        }
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  AND PE_UT_Forsorgeransvar_ingen_er_false()  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt())) {
                        //[TBU3331EN, TBU3331NN, TBU3331]
                        paragraph {
                            text(
                                bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalingen er redusert fordi du er under straffegjennomføring. I denne perioden vil du få utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                                nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalinga er redusert fordi du er under straffegjennomføring. I denne perioden får du betalt ut " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                                english { +"You are entitled to receive NOK " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " in disability benefit, but the payment has been reduced because you are serving a criminal sentence. While you are serving your sentence you will receive NOK " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + "." },
                            )
                        }
                    }

                    //IF((FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) = "nor"  OR FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) = "")  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = true  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true)) THEN      INCLUDE ENDIF
                    showIf(((FUNKSJON_FF_GetArrayElement_String(pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("nor") or FUNKSJON_FF_GetArrayElement_String(pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("")) and (pe.vedtaksdata_beregningsdata_beregningufore_belopokt() or pe.vedtaksdata_beregningsdata_beregningufore_belopredusert()))) {
                        //[TBU2303mFRI_EN, TBU2303mFRI, TBU2303mFRINN]
                        paragraph {
                            text(
                                bokmal { +"Uføretrygden blir utbetalt senest den 20. hver måned. Du får din første utbetaling med nytt beløp i <FRITEKST: måned og år>." },
                                nynorsk { +"Uføretrygda blir utbetalt seinast den 20. i kvar månad. Du får den første utbetalinga di med nytt beløp i <FRITEKST: måned og år>." },
                                english { +"Your disability benefit will be paid no later than the 20th of every month. Your first payment with the changed amount will be made in <FRITEKST: måned og år>." },
                            )
                        }
                    }

                    //IF(FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) <> "nor" AND FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) <> "") THEN      INCLUDE ENDIF
                    showIf((FUNKSJON_FF_GetArrayElement_String(pe.grunnlag_persongrunnlagsliste_personbostedsland()).notEqualTo("nor") and FUNKSJON_FF_GetArrayElement_String(pe.grunnlag_persongrunnlagsliste_personbostedsland()).notEqualTo(""))) {
                        //[TBU2304mFRI_EN, TBU2304mFRI, TBU2304mFRINN]
                        paragraph {
                            text(
                                bokmal { +"Uføretrygden blir utbetalt senest den 20. hver måned. Mottar du uføretrygden på en utenlandsk bankkonto kan utbetalingen bli forsinket. Du får din første utbetaling i <FRITEKST: måned og år>." },
                                nynorsk { +"Uføretrygda blir utbetalt seinast den 20. i kvar månad. Får du uføretrygda på ein utanlandsk bankkonto, kan utbetalinga bli forseinka. Du får den første utbetalinga di i <FRITEKST: måned og år>." },
                                english { +"Your disability benefit will be paid no later than the 20th of every month. If the disability benefit is paid to a foreign bank account, the payment may be somewhat delayed. Your first payment will be made in <FRITEKST: måned og år>." },
                            )
                        }
                    }

                    //IF((PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = false) AND(FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) = "nor"  OR FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) = "")) THEN      INCLUDE ENDIF
                    showIf(((not(pe.vedtaksdata_beregningsdata_beregningufore_belopokt()) and not(pe.vedtaksdata_beregningsdata_beregningufore_belopredusert())) and (FUNKSJON_FF_GetArrayElement_String(pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("nor") or FUNKSJON_FF_GetArrayElement_String(pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("")))) {
                        //[TBU2223, TBU2223EN, TBU2223NN]
                        paragraph {
                            text(
                                bokmal { +"Uføretrygden blir fortsatt utbetalt senest den 20. hver måned." },
                                nynorsk { +"Uføretrygda blir framleis utbetalt seinast den 20. i kvar månad." },
                                english { +"Your disability benefit will still be paid no later than the 20th of every month." },
                            )
                        }
                    }
                }
            }
        }

        data class TBU2305til23052(
            val pe: Expression<PE>
        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
            override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold" AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_EktefelleGarantiTillegg_EGTinnvilget = false) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold") and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefellegarantitillegg_egtinnvilget()))) {
                    //[TBU2305EN, TBU2305, TBU2305NN]

                    paragraph {
                        text(
                            bokmal { +"Du har uføretrygd med minsteytelse. Størrelsen på minsteytelsen er avhengig av sivilstanden din." },
                            nynorsk { +"Du har uføretrygd med minsteyting. Storleiken på minsteytinga er avhengig av sivilstanden din." },
                            english { +"You are receiving the minimum disability benefit. The size of the minimum benefit is dependent on your marital status." },
                        )
                    }
                }

                //IF(  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true  AND PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring" AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = true OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true)  ) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse() and pe.vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring") and (pe.vedtaksdata_beregningsdata_beregningufore_belopokt() or pe.vedtaksdata_beregningsdata_beregningufore_belopredusert()))) {
                    //[TBU2305_1EN, TBU2305_1, TBU2305_1NN]

                    paragraph {
                        text(
                            bokmal { +"Vi har mottatt opplysninger om at sivilstanden din har blitt endret. Utbetalingen din endres derfor til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().format() + " ganger folketrygdens grunnbeløp." },
                            nynorsk { +"Vi har fått opplysningar om at sivilstanden din har blitt endra. Utbetalinga av uføretrygda di blir derfor endra til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().format() + " gonger grunnbeløpet i folketrygda." },
                            english { +"We have been informed that your marital status has been changed. We have changed your disability benefit to " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().format() + " times the national insurance basic amount." },
                        )
                    }
                }

                //IF(  (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = false OR (PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = false)) AND PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"  ) THEN      INCLUDE ENDIF
                showIf(((not(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse()) or (not(pe.vedtaksdata_beregningsdata_beregningufore_belopokt()) and not(pe.vedtaksdata_beregningsdata_beregningufore_belopredusert()))) and pe.vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring"))) {
                    //[TBU2305_2EN, TBU2305_2, TBU2305_2NN]

                    paragraph {
                        text(
                            bokmal { +"Vi har mottatt opplysninger om at sivilstanden din har blitt endret. Dette får ikke betydning for uføretrygden din, og du vil få utbetalt det samme som før." },
                            nynorsk { +"Vi har fått opplysningar om at sivilstanden din har blitt endra. Dette får ikkje noko å seie for uføretrygda di, og du får utbetalt det same som før." },
                            english { +"We have been informed that your marital status has been changed. This will not affect your disability benefit, and you will be paid the same amount." },
                        )
                    }
                }
            }
        }

        data class TBU2321til3241(
            val pe: Expression<PE>
        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
            override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
                ifNotNull(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforetidspunkt(), pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_skadetidspunkt()) { ufoeretidspunkt, skadetidspunkt ->

                    //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5"  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2"  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed_ektefelle"  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed_registrert_partner")  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = false AND PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"   ) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 1-5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed_ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed_registrert_partner")) and pe.vedtaksdata_beregningsdata_beregningufore_belopredusert() and not(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse()) and pe.vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring"))) {
                        //[TBU2321mFRI_EN, TBU2321mFRI, TBU2321mFRINN]
                        paragraph {
                            text(
                                bokmal { +"Vi har mottatt opplysninger om at du <FRITEKST: sivilstandsendring>. Du har minsteytelse i uføretrygden din. Den endrede sivilstanden din medfører nå at du får uføretrygd på grunnlag av egen opptjening." },
                                nynorsk { +"Vi har fått opplysningar om at du <FRITEKST: sivilstandsendring>. Du har minsteyting i uføretrygda di. Den endra sivilstanden din fører no til at du får uføretrygd på grunnlag av di eiga opptening." },
                                english { +"We have been informed that you <FRITEKST sivilstandsendring>. You are receiving the minimum disability benefit. Your changed marital status now entails that your disability benefit is calculated on the basis of your own accumulated rights." },
                            )
                        }
                    }

                    //IF(PE_Vedtaksdata_Kravhode_KravGjelder = "sok_uu" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat) = "oppfylt") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_uu") and FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat()).equalTo("oppfylt"))) {
                        //[TBU2322EN, TBU2322, TBU2322NN]
                        paragraph {
                            text(
                                bokmal { +"For å innvilges rettighet som ung ufør, må du være alvorlig og varig syk før du fylte 26 år. Uføretidspunktet ditt er fastsatt til " + ufoeretidspunkt.format() + ". Du er innvilget rettighet som ung ufør. Det betyr at uføretrygden din vil bli beregnet etter regler som gjelder ung ufør." },
                                nynorsk { +"For at du skal få innvilga rett som ung ufør, må du vere alvorleg og varig sjuk før du fylte 26 år. Uføretidspunktet ditt er sett til " + ufoeretidspunkt.format() + ". Du er innvilga rett som ung ufør. Det betyr at uføretrygda di blir berekna etter reglar som gjeld ung ufør." },
                                english { +"In order to be entitled to special rights as a young disabled individual, you must have suffered from a serious and permanent illness before the age of 26. Following a new evaluation, your date of disability has been determined to be " + ufoeretidspunkt.format() + ". You have been granted rights as a young disabled individual. This means your disability benefit will be calculated in accordance with special rules for young disabled individuals." },
                            )
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((FUNKSJON_FF_GetArrayElement_Float(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                        //[TBU2323EN, TBU2323, TBU2323NN]
                        paragraph {
                            text(
                                bokmal { +"Du er innvilget uføretrygd etter regler for yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert at hele din nedsatte inntektsevne skyldes den godkjente yrkesskaden eller yrkessykdommen." },
                                nynorsk { +"Du er innvilga uføretrygd etter reglar for yrkesskade eller yrkessjukdom. Vi har ut frå opplysningane i saka vurdert at heile den nedsette inntektsevna di kjem av den godkjende yrkesskaden eller yrkessjukdommen." },
                                english { +"You have been granted disability benefit in accordance with the rules for occupational injury or occupational illness. On the basis of the information available in your case, we have concluded that your disability in its entirety was caused by your certified occupational injury or occupational illness." },
                            )

                            //PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false
                            showIf(not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup())) {
                                text(
                                    bokmal { +" Dette betyr at uføretrygden din vil bli beregnet etter særbestemmelser som gir deg en høyere uføretrygd." },
                                    nynorsk { +" Dette betyr at uføretrygda di blir berekna etter særreglar, og gir deg ei høgare uføretrygd." },
                                    english { +" This means your disability benefit will be calculated on the basis of special rules, which will result in a higher payment for you." },
                                )
                            }
                        }
                        //[TBU1167EN, TBU1167, TBU1167NN]
                        paragraph {
                            text(
                                bokmal { +"Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner." },
                                nynorsk { +"Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta di på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner." },
                                english { +"The date of your injury has been determined to be " + skadetidspunkt.format() + ". Your annual income at this time has been determined to be NOK " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + "." },
                            )
                        }
                    }

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,1) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IVSBegrunnelse) = "stdbegr_12_17_3_2") THEN      INCLUDE ENDIF
                    showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat(), 1).equalTo("oppfylt") and FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsbegrunnelse()).equalTo("stdbegr_12_17_3_2"))) {
                        //[TBU3241EN, TBU3241NN, TBU3241]
                        paragraph {
                            text(
                                bokmal { +"Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner. Vi har justert denne inntekten opp fordi den skal tilsvare inntekten du ville hatt som frisk på skadetidspunktet." },
                                nynorsk { +"Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner. Vi har justert denne inntekta opp fordi den skal svare til inntekta du ville hatt som frisk på skadetidspunktet." },
                                english { +"Your date of injury has been established as " + skadetidspunkt.format() + ". Your annual income at this time was established as NOK " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + ". We have adjusted your income to the level of income you would have had if you worked full time at the date of injury." },
                            )
                        }
                    }
                }
            }
        }

        data class TBU2324til1270(
            val pe: Expression<PE>
        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
            override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
                ifNotNull(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_skadetidspunkt()) { skadetidspunkt ->

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = false AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                showIf((FUNKSJON_FF_GetArrayElement_Float(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                    //[TBU2324EN, TBU2324, TBU2324NN]
                    paragraph {
                        text(
                            bokmal { +"Du er innvilget uføretrygd etter regler for yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert at hele din nedsatte inntektsevne skyldes den godkjente yrkesskaden eller yrkessykdommen." },
                            nynorsk { +"Du er innvilga uføretrygd etter reglar for yrkesskade eller yrkessjukdom. Vi har ut frå opplysningane i saka vurdert at heile den nedsette inntektsevna di kjem av den godkjende yrkesskaden eller yrkessjukdommen." },
                            english { +"You have been granted disability benefit in accordance with the rules for occupational injury or occupational illness. On the basis of the information available in your case, we have concluded that your disability in its entirety was caused by your certified occupational injury or occupational illness." },
                        )
                        //PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false
                        showIf(not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup())) {
                            text(
                                bokmal { +" Dette betyr at uføretrygden din vil bli beregnet etter særbestemmelser, dersom dette er til fordel for deg." },
                                nynorsk { +" Dette betyr at uføretrygda di blir berekna etter særreglar dersom dette er til fordel for deg." },
                                english { +" This means your disability benefit will be calculated on the basis of special rules, provided this is to your benefit." },
                            )
                        }
                    }
                    //[TBU1167EN, TBU1167, TBU1167NN]
                    paragraph {
                        text (
                            bokmal { + "Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner." },
                            nynorsk { + "Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta di på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner." },
                            english { + "The date of your injury has been determined to be " + skadetidspunkt.format() + ". Your annual income at this time has been determined to be NOK " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + "." },
                        )
                    }

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,1) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IVSBegrunnelse) = "stdbegr_12_17_3_2") THEN      INCLUDE ENDIF
                    showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat(), 1).equalTo("oppfylt") and FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsbegrunnelse()).equalTo("stdbegr_12_17_3_2"))) {
                        //[TBU3241EN, TBU3241NN, TBU3241]
                        paragraph {
                            text (
                                bokmal { + "Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner. Vi har justert denne inntekten opp fordi den skal tilsvare inntekten du ville hatt som frisk på skadetidspunktet." },
                                nynorsk { + "Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner. Vi har justert denne inntekta opp fordi den skal svare til inntekta du ville hatt som frisk på skadetidspunktet." },
                                english { + "Your date of injury has been established as " + skadetidspunkt.format() + ". Your annual income at this time was established as NOK " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + ". We have adjusted your income to the level of income you would have had if you worked full time at the date of injury." },
                            )
                        }
                    }

                    //PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false
                    showIf(not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup())) {
                        //[TBU1270, TBU1270NN]
                        paragraph {
                            text(
                                bokmal { +"Inntekten din på skadetidspunktet er lavere enn beregningsgrunnlaget ditt, og uføretrygden din vil derfor ikke bli beregnet etter særbestemmelser for yrkesskade eller yrkessykdom." },
                                nynorsk { +"Inntekta di på skadetidspunktet er lågare enn berekningsgrunnlaget ditt, og uføretrygda di blir derfor ikkje berekna etter særreglar for yrkesskade eller yrkessjukdom." },
                                english { +"" }
                            )
                        }
                    }
                }
            }
        }
    }
}