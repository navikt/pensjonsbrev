package no.nav.pensjon.brev.maler.legacy.redigerbar.endringUfoeretrygd

import no.nav.pensjon.brev.api.model.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.grunnlag_persongrunnlagsliste_personbostedsland
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall
import no.nav.pensjon.brev.maler.legacy.ut_barnet_barna_opphor
import no.nav.pensjon.brev.maler.legacy.ut_barnet_barna_opphor_dit_dine
import no.nav.pensjon.brev.maler.legacy.ut_barnet_barna_opphor_forsorga_forsorgde
import no.nav.pensjon.brev.maler.legacy.ut_barnet_barna_opphor_has_have
import no.nav.pensjon.brev.maler.legacy.ut_barnet_barna_opphor_is_are
import no.nav.pensjon.brev.maler.legacy.ut_barnet_barna_opphor_stor_forbokstav
import no.nav.pensjon.brev.maler.legacy.ut_child_children_opphor
import no.nav.pensjon.brev.maler.legacy.ut_dine_ditt_barn_opphor
import no.nav.pensjon.brev.maler.legacy.ut_fodselsdatobarn
import no.nav.pensjon.brev.maler.legacy.ut_forsorgeransvar_ingen_er_false
import no.nav.pensjon.brev.maler.legacy.ut_forsorgeransvar_ingen_er_true
import no.nav.pensjon.brev.maler.legacy.ut_forsorgeransvar_siste_er_true
import no.nav.pensjon.brev.maler.legacy.ut_inntektsgrense_faktisk
import no.nav.pensjon.brev.maler.legacy.ut_konst_kralinjekode_bt
import no.nav.pensjon.brev.maler.legacy.ut_konst_kralinjekode_et
import no.nav.pensjon.brev.maler.legacy.ut_konst_kralinjekode_ut_gjt
import no.nav.pensjon.brev.maler.legacy.ut_konst_vilkarsvedtakresultat_opphor
import no.nav.pensjon.brev.maler.legacy.ut_virkningstidpunktstorreenn01012016
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphreduksjonsperiodeliste_instopphreduksjonsperiode_forsorgeransvar
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningbenyttetsivilstand
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand
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
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtnetto
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_total
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_totalnetto
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforetidspunkt
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravarsaktype
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravgjelder
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravmottatdato
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_onsketvirkningsdato
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsbegrunnelse
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_skadetidspunkt
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat
import no.nav.pensjon.brev.maler.redigerbar.InformasjonOmSaksbehandlingstid.fritekst
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dateFormatter
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
import no.nav.pensjon.brevbaker.api.model.ElementTags
import no.nav.pensjon.brevbaker.api.model.Kroner
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
                    paragraph {
                        text(
                            bokmal { +"Vi har endret uføretrygden din fra " + oensketVirkningsDato.format() + "." },
                            nynorsk { +"Vi har endra uføretrygda di frå " + oensketVirkningsDato.format() + "." },
                            english { +"We have changed your disability benefit effective as of " + oensketVirkningsDato.format() + "." },
                        )
                    }
                }

                //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) AND PE_Vedtaksdata_Kravhode_KravArsakType = "soknad_bt" ) THEN      INCLUDE ENDIF
                showIf(
                    ((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()
                            or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())
                            and pe.vedtaksdata_kravhode_kravarsaktype().equalTo("soknad_bt"))
                ) {
                    //[TBU2288]
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


                //IF(  (PE_Vedtaksdata_Kravhode_KravArsakType = "omgj_etter_klage" OR PE_Vedtaksdata_Kravhode_KravArsakType = "omgj_etter_anke")  ) THEN      INCLUDE ENDIF
                showIf(
                    ((pe.vedtaksdata_kravhode_kravarsaktype()
                        .equalTo("omgj_etter_klage") or pe.vedtaksdata_kravhode_kravarsaktype()
                        .equalTo("omgj_etter_anke")))
                ) {
                    //[TBU3332EN, TBU3332, TBU3332NN]
                    paragraph {
                        text(
                            bokmal { +"Vi har endret uføretrygden din fra " + oensketVirkningsDato.format() + " fordi du har fått medhold i klagen din." },
                            nynorsk { +"Vi har endra uføretrygda di frå " + oensketVirkningsDato.format() + " fordi du har fått medhald i klaga di." },
                            english { +"We have changed your disability benefit effective as of " + oensketVirkningsDato.format() + ", because your appeal has been successful." },
                        )
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
                //TODO
                showIf(
                    (FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(
                        pe.ut_konst_kralinjekode_bt(),
                        pe.ut_konst_vilkarsvedtakresultat_opphor()
                    ))
                ) {
                    //TBU2290EN, TBU2290, TBU2290NN
                    ifNotNull(pe.ut_fodselsdatobarn()) { foedselsdatobarn ->
                        paragraph {
                            text(
                                bokmal { +"Vi har vedtatt at barnetillegget i uføretrygden din opphører fra " + oensketVirkningsDato.format() + " for barn født " + foedselsdatobarn.format() + "." },
                                nynorsk { +"Vi har stansa barnetillegget i uføretrygda di frå " + oensketVirkningsDato.format() + " for barn fødd " + foedselsdatobarn.format() + "." },
                                english { +"The child supplement in your disability benefit has been discontinued, effective as of " + oensketVirkningsDato.format() + ", for child born " + foedselsdatobarn.format() + "." },
                            )
                        }
                    }
                }
//TODO
                //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_et, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
                showIf(
                    (FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(
                        pe.ut_konst_kralinjekode_et(),
                        pe.ut_konst_vilkarsvedtakresultat_opphor()
                    ))
                ) {
                    //[TBU2291EN, TBU2291, TBU2291NN]
                    paragraph {
                        text(
                            bokmal { +"Vi har opphørt ektefelletillegget i uføretrygden din fra " + oensketVirkningsDato.format() + "." },
                            nynorsk { +"Vi har stansa ektefelletillegget i uføretrygda di frå " + oensketVirkningsDato.format() + "." },
                            english { +"The spouse supplement in your disability benefit has been discontinued, effective as of " + oensketVirkningsDato.format() + "." },
                        )
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
                    (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad().equalTo
                        (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad())
                            and pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_ys"))
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
                    (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad().lessThan(
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
                                +oensketVirkningsDato.format() + "."
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
//TODO
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
                showIf(((pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("nor") or (pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("")) and (pe.vedtaksdata_beregningsdata_beregningufore_belopokt() or pe.vedtaksdata_beregningsdata_beregningufore_belopredusert())) {
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
                showIf((pe.grunnlag_persongrunnlagsliste_personbostedsland()).notEqualTo("nor") and (pe.grunnlag_persongrunnlagsliste_personbostedsland()).notEqualTo("")) {
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
                showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_belopokt()) and not(pe.vedtaksdata_beregningsdata_beregningufore_belopredusert())) and (pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("nor") or (pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("")) {
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
                showIf((pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_uu") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat()).equalTo("oppfylt"))) {
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
                showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
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
//TODO
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

    data class TBU2324tilTBU1270(
        val pe: Expression<PE>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            ifNotNull(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_skadetidspunkt()) { skadetidspunkt ->

                //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = false AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold")) {
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
                        text(
                            bokmal { +"Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner." },
                            nynorsk { +"Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta di på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner." },
                            english { +"The date of your injury has been determined to be " + skadetidspunkt.format() + ". Your annual income at this time has been determined to be NOK " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + "." },
                        )
                    }
//TODO
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

                    //PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false
                    showIf(not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup())) {
                        //[TBU1270, TBU1270NN]
                        paragraph {
                            text(
                                bokmal { +"Inntekten din på skadetidspunktet er lavere enn beregningsgrunnlaget ditt, og uføretrygden din vil derfor ikke bli beregnet etter særbestemmelser for yrkesskade eller yrkessykdom." },
                                nynorsk { +"Inntekta di på skadetidspunktet er lågare enn berekningsgrunnlaget ditt, og uføretrygda di blir derfor ikkje berekna etter særreglar for yrkesskade eller yrkessjukdom." },
                                english { +"Your income at the time of the injury is lower than your calculation basis, and your disability benefit will therefore not be calculated under the special rules for occupational injury or occupational illness." }
                            )
                        }
                    }
                }
            }
        }
    }

    data class TBU2372tilTBU1263(
        val pe: Expression<PE>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) > 0 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).greaterThan(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold")) {
                //[TBU2372EN, TBU2372, TBU2372NN]
                paragraph {
                    text(
                        bokmal { +"Du er innvilget uføretrygd etter regler for yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert om yrkesskaden eller yrkessykdommen din er årsak til uførheten din." },
                        nynorsk { +"Du er innvilga uføretrygd etter reglar for yrkesskade eller yrkessjukdom. Vi har ut frå opplysningane i saka vurdert om yrkesskaden eller yrkessjukdommen din er årsaka til uførleiken din." },
                        english { +"You have been granted disability benefit in accordance with the rules for occupational injury or occupational illness. On the basis of the information available in your case, we have assessed whether your occupational injury or occupational illness was the cause of your disability." },
                    )
                }
                //[TBU2373EN, TBU2373, TBU2373NN]
                paragraph {
                    text(
                        bokmal { +"Vi har kommet fram til at " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().format() + " prosent av uførheten din skyldes godkjent yrkesskade eller yrkessykdom. <FRITEKST: konkret begrunnelse>." },
                        nynorsk { +"Vi har kome fram til at " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().format() + " prosent av uførleiken din kjem av godkjend yrkesskade eller yrkessjukdom. <FRITEKST: konkret begrunnelse>." },
                        english { +"We have concluded that " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().format() + " percent of your disability was caused by a certified occupational injury or occupational illness. <FRITEKST: konkret begrunnelse>" },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = true AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest() and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))) {
                    //[TBU1263EN, TBU1263NN, TBU1263]
                    paragraph {
                        text(
                            bokmal { +"Derfor vil denne delen av uføretrygden din bli beregnet etter særbestemmelser som gir deg en høyere uføretrygd." },
                            nynorsk { +"Derfor vil denne delen av uføretrygda di bli rekna ut etter særreglar som gjer deg ei høgare uføretrygd." },
                            english { +"This means that this portion of your disability benefit will be calculated on the basis of special rules, which will result in a higher payment for you." },
                        )
                    }
                }
            }
        }
    }

    data class TBU1264tilTBU1265(
        val pe: Expression<PE>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            ifNotNull(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_skadetidspunkt()) { skadetidspunkt ->

                //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) > 0 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).greaterThan(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold")) {

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = false AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
                    showIf(not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup())) {
                        //[TBU1264EN, TBU1264NN, TBU1264]
                        paragraph {
                            text(
                                bokmal { +"Denne delen av uføretrygden din vil bli beregnet etter særbestemmelser, dersom dette er til fordel for deg." },
                                nynorsk { +"Denne delen av uføretrygda di blir rekna ut etter særreglar dersom det er til fordel for deg." },
                                english { +"This means that this portion of your disability benefit will be calculated on the basis of special rules, provided this is to your benefit." },
                            )
                        }
                    }

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) = "stdbegr_12_17_3_1") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse()).equalTo("stdbegr_12_17_3_1")) {
                        //[TBU1167EN, TBU1167, TBU1167NN]
                        paragraph {
                            text(
                                bokmal { +"Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner." },
                                nynorsk { +"Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta di på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner." },
                                english { +"The date of your injury has been determined to be " + skadetidspunkt.format() + ". Your annual income at this time has been determined to be NOK " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + "." },
                            )
                        }
                    }
//TODO
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

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = false AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
                    showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))) {
                        //[TBU1265EN, TBU1265NN, TBU1265]
                        paragraph {
                            text(
                                bokmal { +"Inntekten din på skadetidspunktet er lavere enn beregningsgrunnlaget ditt, og uføretrygden din vil derfor ikke bli beregnet etter særbestemmelser for yrkesskade eller yrkessykdom." },
                                nynorsk { +"Inntekta di på skadetidspunktet er lågare enn berekningsgrunnlaget ditt, og uføretrygda di blir derfor ikkje berekna etter særreglar for yrkesskade eller yrkessjukdom." },
                                english { +"Your income at the time of injury was lower than your basis for calculation, and your disability benefit will not be calculated in accordance with the special rules for occupational injury or occupational illness." },
                            )
                        }
                    }
                }
            }
        }
    }

    data class TBU2326til2337(
        val pe: Expression<PE>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            ifNotNull(pe.vedtaksdata_kravhode_onsketvirkningsdato(), pe.ut_fodselsdatobarn()) { oensketVirkningsDato, foedselsdatobarn ->

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "endring_ifu" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100 AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_9") THEN      INCLUDE ENDIF
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endring_ifu") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().lessThan(100) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_9")) {

                    //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "endring_ifu" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100 AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_9") THEN      INCLUDE ENDIF
                    showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endring_ifu") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().lessThan(100) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_9")) {
                        //[TBU2326EN, TBU2326, TBU2326NN]
                        paragraph {
                            text(
                                bokmal { +"Inntekten i stillingen din har økt og du får derfor en høyere inntektsgrense. Dette gjør vi for at du skal få riktig utbetaling av uføretrygd." },
                                nynorsk { +"Inntekta i stillinga di har auka, og du får derfor ei høgare inntektsgrense. Dette gjer vi for at du skal få riktig utbetaling av uføretrygd." },
                                english { +"The income for your position has increased and therefore you have been granted a higher income limit. We have done this so that you receive the correct disability benefit payment." },
                            )
                        }
                    }
                    paragraph {
                        text(
                            bokmal { +"Du kan lese mer om dette i vedlegget «Opplysninger om beregningen»." },
                            nynorsk { +"Du kan lese meir om dette i vedlegget «Opplysningar om berekninga»." },
                            english { +"You can read more about this in attachment “Information about calculations”. " },
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_UT_VirkningstidpunktStorreEnn01012016() = true) THEN      INCLUDE ENDIF
                    showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.ut_virkningstidpunktstorreenn01012016()) {
                        //[TBU3821EN, TBU3821, TBU3821_NN]
                        paragraph {
                            text(
                                bokmal { +"Fordi inntekten din har økt, blir størrelsen på barnetillegget ditt endret." },
                                nynorsk { +"Fordi inntekta di har auka, blir storleiken på barnetillegget ditt endra." },
                                english { +"As your income has increased, the size of your child supplement will be adjusted." },
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold")) {
                    //[TBU2327EN, TBU2327, TBU2327NN]

                    paragraph {
                        text(
                            bokmal { +"Du er innvilget gjenlevendetillegg i uføretrygden fordi du er gjenlevende ektefelle/samboer. Tillegget er beregnet etter ditt eget og den avdødes beregningsgrunnlag. " },
                            nynorsk { +"Du er innvilga attlevandetillegg i uføretrygda fordi du er attlevande ektefelle/sambuar. Tillegget er berekna etter ditt eiga og den avdøde sitt brekningsgrunnlag. " },
                            english { +"You have been granted disability benefit with a survivor's supplement. This supplement is calculated on the basis of your and your deceased spouse's bases for calculation and periods of national insurance coverage. " },
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse() and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) {
                            text(
                                bokmal { +"Siden sivilstanden din har endret seg har dette også betydning for størrelsen på uføretrygden din. " },
                                nynorsk { +"Sidan sivilstanden din har endra seg har dette også noko å seie for storleiken på uføretrygda di. " },
                                english { +"Your marital status has been changed and your disability benefit has been recalculated. " },
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true   OR (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true        AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTnetto > 0       )    ) THEN      INCLUDE ENDIF
                        showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse() or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtnetto().greaterThan(0))) {
                            text(
                                bokmal { +"Det betyr at uføretrygden din har økt. " },
                                nynorsk { +"Det betyr at uføretrygda di har auka. " },
                                english { +"We have therefore increased your disability benefit. " },
                            )
                        }

                        //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_NyttGjenlevendetillegg = true
                        showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg()) {
                            text(
                                bokmal { +"Tillegget er tidsbegrenset til fem år fra virkningstidspunktet. " },
                                nynorsk { +"Tillegget er tidsbegrensa til fem år frå verknadstidspunktet. " },
                                english { +"The supplement is time limited to five years from your effective date. " },
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                    //[TBU2328EN, TBU2328, TBU2328NN]
                    paragraph {
                        text(
                            bokmal { +"Avdøde må også ha vært medlem i folketrygden, eller mottatt pensjon fra folketrygden, de siste <FRITEKST: tre/fem årene>  før dødsfallet." },
                            nynorsk { +"Avdøde må også ha vore medlem i folketrygda, eller fått pensjon frå folketrygda, dei siste <FRITEKST: tre/fem åra>  før dødsfallet." },
                            english { +"The deceased must have had national insurance coverage, or been a recipient of a retirement pension from the national insurance, for the last <FRITEKST: three/five years>  prior to his or her death." },
                        )
                    }
                }
//TODO
                //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningBenyttetSivilstand= "gift" AND PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_ut_gjt, PE_UT_KONST_VilkarsVedtakResultat_opphor)) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring") and pe.vedtaksdata_beregningsdata_beregning_beregningbenyttetsivilstand().equalTo("gift") and FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_ut_gjt(), pe.ut_konst_vilkarsvedtakresultat_opphor()))) {
                    //[TBU2329EN, TBU2329, TBU2329NN]
                    paragraph {
                        text(
                            bokmal { +"Du er tidligere innvilget gjenlevendetillegg i uføretrygden din. Gjenlevendetillegget ditt opphører fra " + oensketVirkningsDato.format() + " fordi du har giftet deg." },
                            nynorsk { +"Du er tidlegare innvilga attlevandetillegg i uføretrygda di. Attlevandetillegget ditt tek slutt frå " + oensketVirkningsDato.format() + " fordi du har gifta deg." },
                            english { +"You have previously been granted a survivor's supplement in addition to your disability benefit. This survivor's supplement will cease from " + oensketVirkningsDato.format() + ", because you are now married." },
                        )
                    }
                }

                //IF(  PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"  AND  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningBrukerSivilstand = "samboer1_5"  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningBrukerSivilstand = "samboer3_2"  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningBrukerSivilstand <> "gift")  AND PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_ut_gjt, PE_UT_KONST_VilkarsVedtakResultat_opphor)  ) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring") and (pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().equalTo("samboer1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().equalTo("samboer3_2") and pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().notEqualTo("gift")) and FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_ut_gjt(), pe.ut_konst_vilkarsvedtakresultat_opphor()))) {
                    //[TBU2330EN, TBU2330, TBU2330NN]
                    paragraph {
                        text(
                            bokmal { +"Du er tidligere innvilget gjenlevendetillegg i uføretrygden din. Gjenlevendetillegget ditt opphører fra " + oensketVirkningsDato.format() + " fordi du er i et samboerforhold, og dere har felles barn." },
                            nynorsk { +"Du er tidlegare innvilga attlevandetillegg i uføretrygda di. Attlevandetillegget ditt tek slutt frå " + oensketVirkningsDato.format() + " fordi du er i eit sambuarforhold og de har felles barn." },
                            english { +"You have previously been granted a survivor's supplement in addition to your disability benefit. This survivor's supplement will cease from " + oensketVirkningsDato.format() + ", because you are in a cohabitant relationship, and you and your cohabitant have a joint child." },
                        )
                    }
                }
//TODO
                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "sivilstandsendring"  AND  PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_et, PE_UT_KONST_VilkarsVedtakResultat_opphor) AND PE_Vedtaksdata_Kravhode_KravArsakType <> "sokand_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold" ) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("sivilstandsendring") and FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_et(), pe.ut_konst_vilkarsvedtakresultat_opphor()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("sokand_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                    //[TBU2331EN, TBU2331, TBU2331NN]
                    paragraph {
                        //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bruker_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_opph_ikke_avt_land") > 0 ) THEN      EXCLUDE ENDIF
                        //TODO                            showIf(not((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "bruker_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "eps_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "eps_opph_ikke_avt_land").greaterThan(0)))) {
                        text(
                            bokmal { +"Ektefelletillegg beholdes bare ut den perioden som vedtaket gjelder for. Vi har derfor opphørt ektefelletillegget. " },
                            nynorsk { +"Ektefelletillegget beheld du bare ut perioden vedtaket gjeld for. Vi har derfor stansa ektefelletillegget ditt. " },
                            english { +"The spouse supplement in connection with your disability benefit has been discontinued, do to a limited time period for the supplement. " },
                        )
//TODO
                        //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bruker_flyttet_ikke_avt_land") > 0) THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "bruker_flyttet_ikke_avt_land").greaterThan(0))) {
                            text(
                                bokmal { +"Ifølge våre opplysninger er du bosatt i <Fritekst: bostedsland>. Derfor har du ikke lenger rett til ektefelletillegg." },
                                nynorsk { +"Ifølgje våre opplysningar er du busett i <Fritekst: bostedsland>. Da har du ikkje lenger rett til ektefelletillegg." },
                                english { +"According to our information you are living in <Fritekst:bostedsland>. Then you are not eligible for spouse supplement." },
                            )
                        }
//TODO
                        //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "barn_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_opph_ikke_avt_land") > 0 ) THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "eps_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "barn_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "eps_opph_ikke_avt_land").greaterThan(0))) {
                            text(
                                bokmal { +"Ifølge våre opplysninger er <Fritekst: ektefellen/partneren/samboeren din> bosatt i <Fritekst: bostedsland>. Derfor har du ikke lenger rett til ektefelletetillegg." },
                                nynorsk { +"Ifølgje våre opplysningar er <Fritekst: ektefelle/partner/sambuar> busett i <Fritekst: bostedsland>. Da har du ikkje lenger rett til ektefelletillegg." },
                                english { +"According to our information, your <Fritekst: spouse/partner/cohabiting partner> are living in <Fritekst:bostedsland>. Then you are not eligible for spouse supplement." },
                            )
                        }
//TODO
                        //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bruker_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_opph_ikke_avt_land") > 0 ) THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "bruker_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "eps_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "eps_opph_ikke_avt_land").greaterThan(0))) {
                            text(
                                bokmal { +"For å ha rett til ektefelletillegg fra 1. juli 2020 " },
                                nynorsk { +"For å ha rett til ektefelletillegg frå 1. juli 2020 " },
                                english { +" In order to be entitled to spouse supplement from 1 July 2020 " },
                            )
                        }
//TODO
                        //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bruker_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_opph_ikke_avt_land") > 0 ) THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "bruker_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "eps_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "eps_opph_ikke_avt_land").greaterThan(0))) {
                            text(
                                bokmal { +"må du enten bo i Norge, innenfor EØS-området eller i et annet land Norge har trygdeavtale med" },
                                nynorsk { +"må du enten bu i Noreg, innanfor EØS-området eller i eit anna land Noreg har trygdeavtale med" },
                                english { +"you must live either in Norway, within the EEA, or in another country that Norway has a social security agreement with" },

                                )
                        }
//TODO
                        //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bruker_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_opph_ikke_avt_land") > 0 ) THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "bruker_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "eps_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "eps_opph_ikke_avt_land").greaterThan(0))) {
                            text(
                                bokmal { +"må også ektefellen/samboeren din være bosatt og oppholde seg i Norge, innenfor EØS-området eller et annet land Norge har trygdeavtale med" },
                                nynorsk { +"må også ektefellen/sambuaren vere busett og opphalde seg i Noreg, innanfor EØS-området eller eit anna land Noreg har trygdeavtale med" },
                                english { +"your spouse must also be a resident of and currently reside in Norway, within the EEA, or in another country that Norway has a social security agreement with " },
                            )
                        }
                    }
//TODO
                    //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningBenyttetSivilstand = "skil"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningBenyttetSivilstand = "skpa")  AND  PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_et, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring") and (pe.vedtaksdata_beregningsdata_beregning_beregningbenyttetsivilstand().equalTo("skil") or pe.vedtaksdata_beregningsdata_beregning_beregningbenyttetsivilstand().equalTo("skpa")) and FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_et(), pe.ut_konst_vilkarsvedtakresultat_opphor())))
                    {
                        //[TBU2332EN, TBU2332, TBU2332NN]
                        paragraph {
                            text(
                                bokmal { +"Vi har mottatt opplysninger om at du har blitt skilt. Vi har derfor opphørt ektefelletillegget." },
                                nynorsk { +"Vi har fått opplysningar om at du har blitt skilt. Vi har derfor stansa ektefelletillegget ditt." },
                                english { +"We have received information about your divorce, we have therefore canceled the spouse supplement in connection with your disability benefit." },
                            )
                        }
                    }
//TODO
                    //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningBenyttetSivilstand = "enke" AND  PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_et, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring") and pe.vedtaksdata_beregningsdata_beregning_beregningbenyttetsivilstand().equalTo("enke") and FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_et(), pe.ut_konst_vilkarsvedtakresultat_opphor())))
                    {
                        //[TBU2333EN, TBU2333, TBU2333NN]
                        paragraph {
                            text(
                                bokmal { +"Vi har mottatt opplysninger om at du har blitt enke/enkemann. Vi har derfor opphørt ektefelletillegget." },
                                nynorsk { +"Vi har fått opplysningar om at du har blitt enkje/enkjemann. Vi har derfor stansa ektefelletillegget ditt." },
                                english { +"We have been informed that you have become a widow/widower. Because of this your spouse supplement has been discontinued." },
                            )
                        }
                    }
//TODO
                    //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
                    showIf((FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_bt(), pe.ut_konst_vilkarsvedtakresultat_opphor())))
                    {
                        //[TBU3924_EN, TBU3924, TBU3924_NN]
                        paragraph {
                            text(
                                bokmal { +"Vi har opphørt barnetillegget i uføretrygden din for barn født: " + foedselsdatobarn.format() },
                                nynorsk { +"Vi har stansa barnetillegget i uføretrygda for barn fødd: " + foedselsdatobarn.format() },
                                english { +"The child supplement in your disability benefit has been discontinued for the " + pe.ut_barnet_barna_opphor() + " born: " + foedselsdatobarn.format() },
                            )
                        }
//TODO
                        //IF( Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bt_over_18") > 1  )THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "bt_over_18").greaterThan(1))) {
                            //[TBU3920_EN, TBU3920, TBU3920_NN]
                            paragraph {
                                text(
                                    bokmal { +"For å ha rett til barnetillegg må du forsørge barn under 18 år. Vi har vedtatt at barnetillegget i uføretrygden opphører fordi " + pe.ut_barnet_barna_opphor() + " har fylt 18 år. " },
                                    nynorsk { +"For å ha rett til barnetillegg må du forsørgje barn under 18 år. Vi har stansa barnetillegget i uføretrygda fordi " + pe.ut_barnet_barna_opphor() + " har fylt 18 år." },
                                    english { +"To be eligible for child supplement, you must support children under 18 years of age. The child supplement in your disability benefit has been discontinued because your " + pe.ut_barnet_barna_opphor() + " " + pe.ut_barnet_barna_opphor_has_have() + " turned 18 years of age. " },
                                )
                            }
                        }
//TODO
                        //IF( Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "annen_forld_rett_bt") > 1  )THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "annen_forld_rett_bt").greaterThan(1))) {
                            //[TBU2607EN, TBU2607, TBU2607_NN]
                            paragraph {
                                text(
                                    bokmal { +"<FRITEKST: slett det som ikke er aktuelt>" },
                                    nynorsk { +"<Fritekst: slett det som ikke er aktuelt>" },
                                    english { +"<Fritekst: slett det som ikke er aktuelt>" },
                                )
                            }
                        }
//TODO
                        //IF( Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "annen_forld_rett_bt") > 1  )THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "annen_forld_rett_bt").greaterThan(1))) {
                            //[TBU3921_EN, TBU3921, TBU3921_NN]
                            paragraph {
                                text(
                                    bokmal { +"Når " + pe.ut_barnet_barna_opphor() + " blir forsørget av begge foreldrene og begge mottar uføretrygd, skal barnetillegget gis til den som får det høyeste tillegget. " + pe.ut_barnet_barna_opphor_stor_forbokstav() + "s andre forelder har rett til et høyere barnetillegg enn det du vil få. Vi har derfor opphørt barnetillegget i uføretrygden din. " },
                                    nynorsk { +"Når " + pe.ut_barnet_barna_opphor() + " blir " + pe.ut_barnet_barna_opphor_forsorga_forsorgde() + " av begge foreldra og begge får uføretrygd, blir barnetillegget gitt til den som får det høgaste tillegget. Den andre forelderen har rett til eit høgare barnetillegg enn det du vil få. Vi har derfor stansa barnetillegget i uføretrygda di." },
                                    english { +"When the " + pe.ut_barnet_barna_opphor() + " " + pe.ut_barnet_barna_opphor_is_are() + " supported by both parents and both are receiving disability benefit, the child supplement will be given to the parent that receives the highest supplement. The " + pe.ut_barnet_barna_opphor() + "'s other parent is entitled to a higher child supplement than you would receive. Therefore your child supplement in your disability benefit has been discontinued." },
                                )
                            }
                        }
//TODO
                        //IF( Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "annen_forld_rett_bt") > 1  )THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "annen_forld_rett_bt").greaterThan(1))) {
                            //[TBU3922_EN, TBU3922, TBU3922_NN]
                            paragraph {
                                text(
                                    bokmal { +"Når " + pe.ut_barnet_barna_opphor() + " blir forsørget av foreldre som ikke bor sammen, blir barnetillegget gitt til den som har samme folkeregistrerte adresse som " + pe.ut_barnet_barna_opphor() + ". Du bor ikke på samme folkeregistrerte adresse som " + pe.ut_barnet_barna_opphor() + ". Vi har derfor opphørt barnetillegget i uføretrygden din." },
                                    nynorsk { +"Når " + pe.ut_barnet_barna_opphor() + " blir " + pe.ut_barnet_barna_opphor_forsorga_forsorgde() + " av foreldre som ikkje bur saman, blir barnetillegget gitt til den som har same folkeregistrerte adresse som " + pe.ut_barnet_barna_opphor() + ". Du bur ikkje på same folkeregistrerte adresse som " + pe.ut_barnet_barna_opphor() + ". Vi har derfor stansa barnetillegget i uføretrygda di." },
                                    english { +"When the " + pe.ut_barnet_barna_opphor() + " " + pe.ut_barnet_barna_opphor_is_are() + " supported by parents who do not live together, the child supplement will be paid to the parent that has the same registered address as the " + pe.ut_barnet_barna_opphor() + ". You do not live at the same registered address as the " + pe.ut_barnet_barna_opphor() + ". Therefore your child supplement in your disability benefit has been discontinued." },
                                )
                            }
                        }
//TODO
                        //IF( Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "mindre_ett_ar_bt_flt") > 1 )  THEN INCLUDE ENDIF
                        showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "mindre_ett_ar_bt_flt").greaterThan(1))) {
                            //[TBU3925_EN, TBU3925, TBU3925_NN]
                            paragraph {
                                text(
                                    bokmal { +"Det er mulig å flytte barnetillegget fra den ene til den andre forelderen. Det må imidlertid ha gått et år siden barnetillegget ble overført. I ditt tilfelle har det gått ett år og barnetillegget er overført til den andre forelderen. Vi har derfor opphørt barnetillegget i uføretrygden din." },
                                    nynorsk { +"Det er mogleg å flytte barnetillegget frå den eine til den andre forelderen. Det må i det minste ha gått eit år sidan barnetillegget blei overført. I ditt tilfelle har det gått eit år og barnetillegget er overført til den andre forelderen. Vi har derfor stansa barnetillegget i uføretrygda di." },
                                    english { +"It is possible to transfer the child supplement between the parents, but it must have been at least one year since the last time it was transferred. In your case, one year has passed and the child supplement has been transferred to the other parent. Therefore your child supplement in your disability benefit has been discontinued." },
                                )
                            }
                        }
//TODO
                        //IF( Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bt_innt_over_1g") > 1  )THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "bt_innt_over_1g").greaterThan(1))) {
                            //[TBU3923_EN, TBU3923, TBU3923_NN]
                            paragraph {
                                text(
                                    bokmal { +"Når " + pe.ut_barnet_barna_opphor() + " har inntekt over folketrygdens grunnbeløp på " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner, har du ikke rett til barnetillegg. " },
                                    nynorsk { +"Når " + pe.ut_barnet_barna_opphor() + " har inntekt over grunnbeløpet i folketrygda på " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner, har du ikkje rett til barnetillegg." },
                                    english { +"When the " + pe.ut_barnet_barna_opphor() + " " + pe.ut_barnet_barna_opphor_has_have() + " an income above the national insurance basic amount of NOK " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + ", you are not eligible for child supplement. " },
                                )
                                text(
                                    bokmal { +"Det er opplyst at " + pe.ut_barnet_barna_opphor() + " " + pe.ut_barnet_barna_opphor_dit_dine() + " har inntekt over " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner. Vi har derfor opphørt barnetillegget i uføretrygden din." },
                                    nynorsk { +"Det er opplyst at " + pe.ut_barnet_barna_opphor() + " " + pe.ut_barnet_barna_opphor_dit_dine() + " har inntekt over " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner. Vi har derfor stansa barnetillegget i uføretrygda di." },
                                    english { +"We have been informed that your " + pe.ut_barnet_barna_opphor() + " " + pe.ut_barnet_barna_opphor_has_have() + " an income above NOK " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + ". Therefore the child supplement in your disability benefit has been discontinued." },
                                )
                            }
                        }
                    }
//TODO
                    //IF(PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor)) AND (Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bruker_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "barn_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "barn_opph_ikke_avt_land") > 0 ) THEN      INCLUDE ENDIF
                    showIf((FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_bt(), pe.ut_konst_vilkarsvedtakresultat_opphor())) and (FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "bruker_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "barn_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "barn_opph_ikke_avt_land").greaterThan(0)))
                    {

                        //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "barn_flyttet_ikke_avt_land") > 0) THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "barn_flyttet_ikke_avt_land").greaterThan(0))) {
                            //[TBU5009_EN, TBU5009, TBU5009_NN]
                            paragraph {
                                text(
                                    bokmal { +"Ifølge våre opplysninger er " + pe.ut_dine_ditt_barn_opphor() + " bosatt i <Fritekst: bostedsland>.  Derfor har du ikke lenger rett til barnetillegg" },
                                    nynorsk { +"Ifølgje våre opplysningar er " + pe.ut_dine_ditt_barn_opphor() + " busett i <Fritekst: bostedsland>. Derfor har du ikkje lenger rett til barnetillegg." },
                                    english { +"According to our information, your " + pe.ut_child_children_opphor() + " living in <Fritekst: bostedsland>. Then you are not eligible for child supplement." },
                                )
                            }
                        }
//TODO
                        //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "barn_opph_ikke_avt_land") > 0) THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "barn_opph_ikke_avt_land").greaterThan(0))) {
                            //[TBU5010_EN, TBU5010, TBU5010_NN]
                            paragraph {
                                text(
                                    bokmal { +"Ifølge våre opplysninger er " + pe.ut_dine_ditt_barn_opphor() + " bosatt i <Fritekst: bostedsland>.  Derfor har du ikke lenger rett til barnetillegg." },
                                    nynorsk { +"Ifølgje våre opplysningar er " + pe.ut_dine_ditt_barn_opphor() + " busett i <Fritekst: bostedsland>. Derfor har du ikkje lenger rett til barnetillegg." },
                                    english { +"According to our information, your " + pe.ut_child_children_opphor() + " living in <Fritekst: bostedsland>. Then you are not eligible for child supplement." },
                                )
                            }
                        }
//TODO
                        //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bruker_flyttet_ikke_avt_land") > 0) THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(), "bruker_flyttet_ikke_avt_land").greaterThan(0))) {
                            //[TBU5011_EN, TBU5011, TBU5011_NN]
                            paragraph {
                                text(
                                    bokmal { +"Ifølge våre opplysninger er du bosatt i <Fritekst: bostedsland>. Derfor har du ikke lenger rett til barnetillegg." },
                                    nynorsk { +"Ifølgje våre opplysningar er du busett i <Fritekst: bostedsland>. Da har du ikkje lenger rett til barnetillegg." },
                                    english { +"According to our information you are living in i <Fritekst: bostedsland>. Then you are not eligible for child supplement." },
                                )
                            }
                        }
//TODO
                        //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_bt(), pe.ut_konst_vilkarsvedtakresultat_opphor()))) {
                            //[TBU5006_EN, TBU5006, TBU5006_NN]
                            paragraph {
                                text(
                                    bokmal { +"For å ha rett til barnetillegg fra 1. juli 2020 " },
                                    nynorsk { +"For å ha rett til forsørgingstillegg frå 1. juli 2020 " },
                                    english { +"In order to be entitled to child supplement from 1 July 2020 " },
                                )
                                text(
                                    bokmal { +"må du enten bo i Norge, innenfor EØS-området eller i et annet land Norge har trygdeavtale med" },
                                    nynorsk { +"må du enten bu i Noreg, innanfor EØS-området eller i eit anna land Noreg har trygdeavtale med" },
                                    english { +"you must live either in Norway, within the EEA, or in another country that Norway has a social security agreement with" },
                                )
                                text(
                                    bokmal { +"må også barnet være bosatt og oppholde seg i Norge, innenfor EØS-området eller et annet land Norge har trygdeavtale med " },
                                    nynorsk { +"må også barn vere busett og opphalde seg i Noreg, innanfor EØS-området eller eit anna land Noreg har trygdeavtale med " },
                                    english { +"your child must also be a resident of and currently reside in Norway, within the EEA, or in another country that Norway has a social security agreement with " },
                                )
                                text(
                                    bokmal { +"Dette går frem av folketrygdloven §12-15 som gjelder fra 1.juli 2020." },
                                    nynorsk { +"Dette går fram av folketrygdlova § 12-15 som gjeld frå 1. juli 2020" },
                                    english { +"This is in accordance with the regulations of § 12-15 of the National Insurance Act, which apply from 1 July 2020." },
                                )
                            }
                        }
                    }
//TODO
                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = false AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) = "false" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "sivilstandsendring") THEN      INCLUDE ENDIF
                    showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_belopokt()) and not(pe.vedtaksdata_beregningsdata_beregningufore_belopredusert()) and FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1).equalTo("false") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("sivilstandsendring")))
                    {
                        //[TBU2222EN, TBU2222, TBU2222NN]
                        paragraph {
                            text(
                                bokmal { +"Dette får ikke betydning for uføretrygden din, og du vil få utbetalt det samme som før." },
                                nynorsk { +"Dette får ikkje noko å seie for uføretrygda di, og du får utbetalt det same som før." },
                                english { +"This will not affect your disability benefit, and you will be paid the same amount." },
                            )
                        }
                    }
//TODO
                    //IF(PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes_Er_Ulik(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor)) THEN     INCLUDE ENDIF
                    showIf((FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes_Er_Ulik(pe.ut_konst_kralinjekode_bt(), pe.ut_konst_vilkarsvedtakresultat_opphor())))
                    {
                        //[TBU2335EN, TBU2335, TBU2335NN]
                        paragraph {
                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())) {
                                text(
                                    bokmal { +"Barnetillegg kan gis så lenge du forsørger barn. Det gis som et tillegg til uføretrygden din og opphører når barnet fyller 18 år. " },
                                    nynorsk { +"Barnetillegg kan bli gitt så lenge du forsørgjer barn. Det blir gitt som eit tillegg til uføretrygda di og blir stansa når barnet ditt fyller 18 år. " },
                                    english { +"You can receive a child supplement as long as you are supporting a child. This is given as a supplement to your disability benefit, and will be discontinued when the child turns 18 years of age. " },
                                )
                            }

                            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) AND PE_Vedtaksdata_Kravhode_KravArsakType = "soknad_bt" ) THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_kravhode_kravarsaktype().equalTo("soknad_bt"))) {
                                text(
                                    bokmal { +"Du er innvilget barnetillegg fordi du forsørger barn." },
                                    nynorsk { +"Du er innvilga barnetillegg fordi du forsørgjer barn." },
                                    english { +"You have been granted child supplement because you are supporting children." },
                                )
                            }
                        }
                    }

                    //IF(  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND (PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "barn_endret_inntekt" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "annen_for_end_in" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "begge_for_end_in")  ) THEN      INCLUDE ENDIF
                    showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and (pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("barn_endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("annen_for_end_in") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("begge_for_end_in"))))
                    {
                        //[TBU2336EN, TBU2336, TBU2336NN]
                        paragraph {
                            text(
                                bokmal { +"Vi har mottatt opplysninger om at inntekten din er endret. Barnetillegget er derfor beregnet på nytt." },
                                nynorsk { +"Vi har fått opplysningar om at inntekta di er endra. Barnetillegg er derfor berekna på nytt." },
                                english { +"We have new information regarding your income. The child supplement has therefore been recalculated." },
                            )
                        }
                    }

                    //IF(  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND (PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "barn_endret_inntekt" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "annen_for_end_in" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "begge_for_end_in" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "eps_endret_inntekt")  ) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("barn_endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("annen_for_end_in") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("begge_for_end_in") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("eps_endret_inntekt"))))
                    {
                        //[TBU2337EN, TBU2337, TBU2337NN]
                        paragraph {
                            text(
                                bokmal { +"Vi har mottatt opplysninger om at inntekten deres er endret. Barnetillegget er derfor beregnet på nytt." },
                                nynorsk { +"Vi har fått opplysningar om at inntekta dykkar er endra. Barnetillegg er derfor berekna på nytt." },
                                english { +"We have new information regarding your income. The child supplement has therefore been recalculated." },
                            )
                        }
                    }
                }
            }

            data class TBU1136tilTBU3364(
                val pe: Expression<PE>
            ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
                override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

                    //PE_Vedtaksdata_Kravhode_KravArsakType = "omgj_etter_klage"
                    showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("omgj_etter_klage")) {
                        //[TBU1136EN, TBU1136, TBU1136NN, TBU3362NN]
                        paragraph {
                            text(
                                bokmal { +"Søknaden din er innvilget etter klage og vi anser klagen som ferdig behandlet. Dersom du ønsker å opprettholde klagen, må du gi tilbakemelding til NAV innen 3 uker." },
                                nynorsk { +"Søknaden din er innvilga etter klage, og vi ser det slik at klaga er ferdig behandla. Dersom du ønskjer å halde fast på klaga, må du melde dette tilbake til NAV innan 3 veker." },
                                english { +"Your application has been granted following an appeal and we consider the processing of the appeal to have been concluded. If you want to uphold your appeal, you must notify NAV of this within 3 weeks." },
                            )
                        }
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                        //[TBU3103EN, TBU3103NN, TBU3103]
                        paragraph {
                            text(
                                bokmal { +"Utbetaling av uføretrygd for deg som er innlagt på institusjon" },
                                nynorsk { +"Utbetaling av uføretrygd når du er innlagd på institusjon" },
                                english { +"Payment of disability benefit when you have been admitted to an institution" },
                            )
                        }
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  OR  PE_UT_Forsorgeransvar_ingen_er_false() OR  PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = true)) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() or pe.ut_forsorgeransvar_ingen_er_false() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter()))) {
                        //[TBU3104, TBU3104EN, TBU3104NN]
                        paragraph {
                            text(
                                bokmal { +"Vi reduserer ikke utbetalingen av uføretrygden din verken i innleggelsesmåneden eller de tre påfølgende månedene når du er innlagt på institusjon. Uføretrygden din blir deretter redusert og skal under oppholdet utgjøre 14 prosent av uføretrygden inntil institusjonsoppholdet avsluttes. Totalt sett skal utbetalingen likevel utgjøre minst 45 prosent av folketrygdens grunnbeløp, før skatt. " },
                                nynorsk { +"Vi reduserer ikkje utbetalinga av uføretrygda di verken i innleggingsmånaden eller dei tre påfølgjande månadene når du er innlagd på institusjon. Uføretrygda di blir deretter redusert og skal under opphaldet utgjere 14 prosent av uføretrygda inntil institusjonsopphaldet blir avslutta. Totalt sett skal utbetalinga likevel utgjere minst 45 prosent av grunnbeløpet i folketrygda før skatt. " },
                                english { +"We will not reduce payment of your disability benefit during the month of admission, and also not during the following three months after you are admitted to an institution. After that, your disability benefit will be reduced. Up until the end of your stay in the institution, the payments will be 14 percent of your disability benefit. Your disability benefit will nevertheless be at least 45 percent of the national insurance basic amount. " },
                            )

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() or pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())) {
                                text(
                                    bokmal { +"Dersom du mottar " },
                                    nynorsk { +"Dersom du får " },
                                    english { +"If you receive " },
                                )
                            }

                            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) {
                                text(
                                    bokmal { +"ektefelletillegg " },
                                    nynorsk { +"ektefelletillegg " },
                                    english { +"spouse supplement " }
                                )
                            }

                            //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                            showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) {
                                text(
                                    bokmal { +"gjenlevendetillegg " },
                                    nynorsk { +"attlevandetillegg " },
                                    english { +"survivor’s supplement " },
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() or pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())) {
                                text(
                                    bokmal { +"vil dette tillegget også bli redusert." },
                                    nynorsk { +"vil dette tillegget også bli redusert." },
                                    english { +"this will also be reduced." },
                                )
                            }
                        }
                    }
//TODO//AVKLARING - fasteutgifter er kroner beløp, Ikke boolean?
                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  OR  PE_UT_Forsorgeransvar_ingen_er_false() OR  PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = true)) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() or pe.ut_forsorgeransvar_ingen_er_false() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter()))) {
                        //[TBU3105EN, TBU3105NN, TBU3105]

                        paragraph {
                            text(
                                bokmal { +"Dersom du har faste og nødvendige utgifter til bolig, kan vi vurdere om uføretrygden din kan reduseres mindre. Du må sende inn dokumentasjon på dine utgifter til NAV. Forsørger du barn " },
                                nynorsk { +"Dersom du har faste og nødvendige utgifter til bustad, vil vi vurdere en lågare reduksjon av uføretrygda di. Du må sende inn dokumentasjon på utgiftene dine til NAV. Viss du forsørgjer barn" },
                                english { +"If you have fixed and necessary housing expenses, we will consider a lower reduction in your disability benefit. You must submit documentation of your housing expenses to NAV. If you support children " },
                            )

                            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) {
                                text(
                                    bokmal { +"og/eller ektefelle " },
                                    nynorsk { +" og/eller ektefelle" },
                                    english { +"and/or spouse " },
                                )
                            }
                            text(
                                bokmal { +"under innleggelsen på institusjonen, vil vi ikke redusere uføretrygden din." },
                                nynorsk { +" mens du er lagd inn på institusjonen, reduserer vi ikkje uføretrygda di." },
                                english { +"during your stay in the institution, we will not reduce your disability benefit." },
                            )
                        }
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false AND PE_UT_Forsorgeransvar_ingen_er_true() AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
                    showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.ut_forsorgeransvar_ingen_er_true() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))) {
                        //[TBU3106EN, TBU3106NN, TBU3106]
                        paragraph {
                            text(
                                bokmal { +"Uføretrygden din er lavere enn 45 prosent av folketrygdens grunnbeløp. Du vil derfor ikke få redusert utbetaling av uføretrygden din når du er innlagt på institusjon." },
                                nynorsk { +"Uføretrygda di er lågare enn 45 prosent av grunnbeløpet i folketrygda. Du får derfor ikkje redusert utbetaling av uføretrygda di når du er innlagd på institusjon." },
                                english { +"Your disability benefit is less than 45 percent of the national insurance basic amount. Therefore, your disability benefit payments will not be reduced when you are admitted to an institution." },
                            )
                        }
                    }

                    //IF(PE_UT_Forsorgeransvar_ingen_er_false() AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0) THEN      INCLUDE ENDIF
                    showIf((pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0))) {
                        //[TBU3107EN, TBU3107NN, TBU3107]
                        paragraph {
                            text(
                                bokmal { +"Du forsørger barn" },
                                nynorsk { +"Du forsørgjer barn " },
                                english { +"You support children" },
                            )

                            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) {
                                text(
                                    bokmal { +" og/eller" + " ektefelle" },
                                    nynorsk { +"og/eller ektefelle " },
                                    english { +" and/or spouse" },
                                )
                            }
                            text(
                                bokmal { +" under oppholdet ditt i institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                                nynorsk { +"under opphaldet ditt på institusjon. Vi har derfor kome fram til at utbetalinga di derfor ikkje skal reduserast." },
                                english { +" during your stay in an institution. Therefore we have concluded that your disability benefit payments will not be reduced." },
                            )
                        }
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND PE_UT_Forsorgeransvar_ingen_er_true()) THEN      INCLUDE ENDIF
                    showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.ut_forsorgeransvar_ingen_er_true())) {
                        //[TBU3108EN, TBU3108NN, TBU3108]
                        paragraph {
                            text(
                                bokmal { +"Du har dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                                nynorsk { +"Du har dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har kome fram til at utbetalinga di derfor ikkje skal reduserast." },
                                english { +"You have documented that you have fixed and necessary housing expenses during your stay in the institution. We have concluded that your disability benefit payments will not be reduced." },
                            )
                        }
                    }

                    //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0 AND PE_UT_Forsorgeransvar_ingen_er_false() AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))) {
                        //[TBU3109EN, TBU3109NN, TBU3109]
                        paragraph {
                            text(
                                bokmal { +"Du har dokumentert at du har faste og nødvendige utgifter til bolig, og du forsørger barn " },
                                nynorsk { +"Du har dokumentert at du har faste og nødvendige utgifter til bustad, og du forsørgjer barn" },
                                english { +"You have documented that you have fixed and necessary housing expenses and that you support children" },
                            )

                            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) {
                                text(
                                    bokmal { +"og/eller ektefelle " },
                                    nynorsk { +" og/eller ektefelle" },
                                    english { +" and/or" + " spouse" },
                                )
                            }
                            text(
                                bokmal { +"under oppholdet ditt i institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                                nynorsk { +" under opphaldet ditt på institusjon. Vi har derfor kome fram til at utbetalinga di ikkje skal reduserast." },
                                english { +" during your stay in the institution. Therefore, we have concluded that your disability benefit payment will not be reduced." },
                            )
                        }
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))) {
                        //[TBU3110EN, TBU3110NN, TBU3110]
                        paragraph {
                            text(
                                bokmal { +"Du har dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at utbetalingen din skal reduseres til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                                nynorsk { +"Du har dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har kome fram til at utbetalinga di skal reduserast til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                                english { +"You have documented that you have fixed and necessary housing expenses during your stay in the institution. We have concluded that your disability benefit payments will be reduced by NOK " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + "." },
                            )
                        }
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0  AND PE_UT_Forsorgeransvar_ingen_er_true()) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0) and pe.ut_forsorgeransvar_ingen_er_true())) {
                        //[TBU3112EN, TBU3112NN, TBU3112]
                        paragraph {
                            text(
                                bokmal { +"Du forsørger ikke barn" },
                                nynorsk { +"Du forsørgjer ikkje barn" },
                                english { +"You do not support children" },
                            )

                            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) {
                                text(
                                    bokmal { +" og/eller ektefelle" },
                                    nynorsk { +" og/eller ektefelle" },
                                    english { +" and/or spouse" },
                                )
                            }
                            text(
                                bokmal { +", og det er ikke dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at uføretrygden din skal reduseres til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                                nynorsk { +", og det er ikkje dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har derfor kome fram til at uføretrygda di skal reduserast til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                                english { +" and you have not documented that you have fixed and necessary housing expenses during your stay in the institution. We have concluded that your disability benefit payments will be reduced to NOK " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + "." },
                            )
                        }
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                        //[TBU3114EN, TBU3114NN, TBU3114]
                        paragraph {
                            text(
                                bokmal { +"Utbetaling av uføretrygd for deg som er under straffegjennomføring" },
                                nynorsk { +"Utbetaling av uføretrygd når du er under straffegjennomføring" },
                                english { +"Payment of disability benefit while you are serving a sentence" },
                            )
                        }
                    }

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"
                    showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo")) {
                        //[TBU3115, TBU3115EN, TBU3115NN]
                        paragraph {
                            text(
                                bokmal { +"Uføretrygden din er redusert fordi du er under straffegjennomføring." },
                                nynorsk { +"Uføretrygda di er redusert fordi du er under straffegjennomføring." },
                                english { +"We have reduced your disability benefit, because you are serving a sentence." }
                            )

                            //IF(PE_UT_Forsorgeransvar_siste_er_true()) THEN      INCLUDE ENDIF
                            showIf((pe.ut_forsorgeransvar_siste_er_true())) {
                                text(
                                    bokmal { +" Da du forsørger barn" },
                                    nynorsk { +" Da du forsørgjer barn" },
                                    english { +" Because you support children" }
                                )
                            }
//TODO
                            //IF(FF_GetArrayElement_Boolean(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphReduksjonsperiodeListe_InstOpphReduksjonsperiode_Forsorgeransvar) = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true) THEN      INCLUDE ENDIF
                            showIf((FUNKSJON_FF_GetArrayElement_Boolean(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphreduksjonsperiodeliste_instopphreduksjonsperiode_forsorgeransvar()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget())) {
                                text(
                                    bokmal { +" og/eller ektefelle" },
                                    nynorsk { +" og/eller ektefelle" },
                                    english { +" and/or spouse" }
                                )
                            }

                            //IF(PE_UT_Forsorgeransvar_siste_er_true()) THEN      INCLUDE ENDIF
                            showIf((pe.ut_forsorgeransvar_siste_er_true())) {
                                text(
                                    bokmal { +", vil utbetaling av uføretrygden din reduseres med 50 prosent." },
                                    nynorsk { +", vil utbetalinga av uføretrygda di reduserast med 50 prosent." },
                                    english { +", your payment is reduced to 50 percent." }
                                )
                            }
                            text(
                                bokmal { +" Utbetalingen din er redusert fra andre måned etter at straffegjennomføring tok til. Når straffegjennomføring er avsluttet, vil vi ikke lenger redusere uføretrygden din. " },
                                nynorsk { +" Utbetalinga di er redusert frå den andre månaden etter at straffegjennomføringa tok til. Når straffegjennomføringa er avslutta, vil vi ikkje lenger redusere uføretrygda di. " },
                                english { +" Your payment is reduced from the second month after you started serving your sentence. We will resume payments when you have served your sentence. " }
                            )

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() or pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())) {
                                text(
                                    bokmal { +"Dersom du mottar " },
                                    nynorsk { +"Dersom du mottar " },
                                    english { +"If you receive " }
                                )
                            }

                            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) {
                                text(
                                    bokmal { +"ektefelletillegg" },
                                    nynorsk { +"ektefelletillegg" },
                                    english { +"spouse supplement" }
                                )
                            }

                            //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                            showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) {
                                text(
                                    bokmal { +"gjenlevendetillegg" },
                                    nynorsk { +"attlevandetillegg" },
                                    english { +"survivor's supplement" }
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() or pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())) {
                                text(
                                    bokmal { +" vil dette tillegget også bli redusert." },
                                    nynorsk { +" vil dette tillegget også bli redusert." },
                                    english { +" this will also be reduced." }
                                )
                            }
                        }

                        //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "instopphold" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "") THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("instopphold") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo(""))) {
                            //[TBU3364EN, TBU3364, TBU3364NN]
                            paragraph {
                                text(
                                    bokmal { +"Du er ikke lenger <FRITEKST: innlagt på institusjon/under straffegjennomføring>, og du får derfor tilbake utbetalingen av uføretrygden." },
                                    nynorsk { +"Du er ikkje lenger <Fritekst: innlagt på institusjon/under straffegjennomføring>, og du får derfor tilbake utbetalinga av uføretrygda." },
                                    english { +"As you no longer are <FRITEKST: innlagt på institusjon/under straffegjennomføring>, we have reinstated payment of your disability benefit." },
                                )
                            }
                        }
                    }
                }
            }
        }

        data class TBU2500tilTBU3717(
            val pe: Expression<PE>
        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
            override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
                //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_et, PE_UT_KONST_VilkarsVedtakResultat_opphor)) THEN      INCLUDE ENDIF
                showIf((FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_et(),pe.ut_konst_vilkarsvedtakresultat_opphor()))){
                    //[TBU2500EN, TBU2500, TBU2500NN]

                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter forskrift om overgangsregler ved innføringen av uføretrygd § 8." },
                            nynorsk { + "Vedtaket er gjort etter forskrift om overgangsreglar ved innføringa av uføretrygd § 8." },
                            english { + "The decision has been made pursuant to Section 8 of the transitional provisions for the implementation of disability benefit." },
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo") THEN      INCLUDE ENDIF
                showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))){
                    //[TBU2342EN, TBU2342, TBU2342NN]

                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14 og " },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14 og " },
                            english { + "The decision has been made pursuant to Section 12-8 to 12-14 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                                nynorsk { + "22-12" },
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                                nynorsk { + "22-13" },
                                english { + "22-13" },
                            )
                        }
                        text (
                            bokmal { + "." },
                            nynorsk { + "." },
                            english { + " of the Norwegian National Insurance Act." },
                        )
                    }
                }

                //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo" AND PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes_Er_Ulik(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor)  ) THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo") and FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes_Er_Ulik(pe.ut_konst_kralinjekode_bt(),pe.ut_konst_vilkarsvedtakresultat_opphor()))){
                    //[TBU2343, TBU2343EN, TBU2343NN]

                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-16 og " },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-16 og " },
                            english { + "The decision has been made pursuant to Section 12-8 to 12-16 and " }
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                                nynorsk { + "22-12" },
                                english { + "22-12 of the Norwegian National Insurance Act." }
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                                nynorsk { + "22-13" },
                                english { + "22-13 of the Norwegian National Insurance Act." }
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                                nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                                english { + " and the transitional provisions for the child supplement in your disability benefit" }
                            )
                        }
                        text (
                            bokmal { + "." },
                            nynorsk { + "." },
                            english { + "." }
                        )
                    }

                //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo"  ) THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))){
                    //[TBU2344, TBU2344EN, TBU2344NN]

                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-17 og " },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-17 og " },
                            english { + "The decision has been made pursuant to Section 12-8 to 12-17 and " }
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                                nynorsk { + "22-12" },
                                english { + "22-12 of the Norwegian National Insurance Act." },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                                nynorsk { + "22-13" },
                                english { + "22-13 of the Norwegian National Insurance Act." }
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                                nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                                english { + " and the transitional provisions for the child supplement in your disability benefit" }
                            )
                        }
                        text (
                            bokmal { + "." },
                            nynorsk { + "." },
                            english { + "." }
                        )
                    }


                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo") THEN      INCLUDE ENDIF
                showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))){
                    //[TBU2345EN, TBU2345, TBU2345NN]

                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-17 og " },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-17 og " },
                            english { + "The decision has been made pursuant to Section 12-8 to 12-14, 12-17 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                                nynorsk { + "22-12" },
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                                nynorsk { + "22-13" },
                                english { + "22-13" },
                            )
                        }
                        text (
                            bokmal { + "." },
                            nynorsk { + "." },
                            english { + " of the Norwegian National Insurance Act." },
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo") THEN      INCLUDE ENDIF
                showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))){
                    //[TBU2346EN, TBU2346, TBU2346NN]

                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-18 og " },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-18 og " },
                            english { + "The decision has been made pursuant to Section 12-8 to 12-14, 12-18 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                                nynorsk { + "22-12" },
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                                nynorsk { + "22-13" },
                                english { + "22-13" },
                            )
                        }
                        text (
                            bokmal { + "." },
                            nynorsk { + "." },
                            english { + " of the Norwegian National Insurance Act." },
                        )
                    }
                }

                //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo"  ) THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))){
                    //[TBU2347]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-16, 12-18 og " },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-16, 12-18 og " },
                            english { + "The decision has been made pursuant to Section 12-8 to 12-16, 12-18 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                                nynorsk { + "22-12" },
                                english { + "22-12 of the Norwegian National Insurance Act" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                                nynorsk { + "22-13" },
                                english { + "22-13 of the Norwegian National Insurance Act" },
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                                nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                                english { + " and the transitional provisions for the child supplement in your disability benefit" },
                            )
                        }
                        text (
                            bokmal { + "." },
                            nynorsk { + "." },
                            english { + "." },
                        )
                    }


                //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo"  ) THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))){
                    //[TBU2348]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-18 og " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                            )
                        }
                        text (
                            bokmal { + "." },
                        )
                    }
                    //[TBU2348]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-18 og " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                nynorsk { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                nynorsk { + "22-13" },
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                            )
                        }
                        text (
                            nynorsk { + "." },
                        )
                    }
                    //[TBU2348]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            english { + "The decision has been made pursuant to Section 12-8 to 12-18 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                english { + "22-13" },
                            )
                        }
                        text (
                            english { + " of the Norwegian National Insurance Act" },
                        )

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                english { + " and the transitional provisions for the child supplement in your disability benefit" },
                            )
                        }
                        text (
                            english { + "." },
                        )
                    }
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "soknad_bt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("soknad_bt")){
                    //[TBU2349EN, TBU2349, TBU2349NN]

                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-15 og 12-16 og " },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-15 og 12-16 og " },
                            english { + "The decision has been made pursuant to Section 12-15 and 12-16 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                                nynorsk { + "22-12" },
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                                nynorsk { + "22-13 " },
                                english { + "22-13" },
                            )
                        }
                        text (
                            bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden." },
                            nynorsk { + "og forskrift om overgangsreglar for barnetillegg i uføretrygda." },
                            english { + " of the Norwegian National Insurance Act." },
                        )
                    }
                }

                //IF(  PE_UT_KravLinjeKode_Og_PaaFolgende_bt_ikkeInnv() AND ( FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5" ) AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo" AND PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor)  ) THEN      INCLUDE ENDIF
                showIf((pe.ut_kravlinjekode_og_paafolgende_bt_ikkeinnv() and (FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5")) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo") and FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_bt(),pe.ut_konst_vilkarsvedtakresultat_opphor()))){
                    //[TBU2350EN, TBU2350, TBU2350NN]

                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-15 og 22-12." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-15 og 22-12." },
                            english { + "The decision has been made pursuant to Section 12-15 and 22-12 of the Norwegian National Insurance Act." },
                        )
                    }
                }

                //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_ut_gjt_ikkeInnv() AND ( FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5" ) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo"  ) THEN      INCLUDE ENDIF
                showIf((pe.ut_kravlinjekode_og_paafolgende_ut_gjt_ikkeinnv() and (FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5")) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))){
                    //[TBU2351EN, TBU2351, TBU2351NN]

                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-18 og 22-12." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-18 og 22-12." },
                            english { + "The decision has been made pursuant to Section 12-18 and 22-12 of the Norwegian National Insurance Act." },
                        )
                    }
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "endring_ifu" AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse(1) = "stdbegr_12_8_2_9" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endring_ifu") and FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse(1).equalTo("stdbegr_12_8_2_9") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))){
                    //[TBU2352EN, TBU2352, TBU2352NN]

                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14 og forskrift om uføretrygd fra folketrygden § 2-3." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14 og forskrift om uføretrygd frå folketrygda § 2-3." },
                            english { + "The decision has been made pursuant to Section 12-8 to 12-14 of the Norwegian National Insurance Act, as well as the Regulations pertaining to disability benefit pursuant to Section 2-3 of the Norwegian National Insurance Act." },
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
                showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                    //[TBU3352EN, TBU3352, TBU3352NN]

                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-19 og " },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-19 og " },
                            english { + "The decision has been made pursuant to Section 12-8 to 12-14, 12-19 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                                nynorsk { + "22-12" },
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                                nynorsk { + "22-13" },
                                english { + "22-13" },
                            )
                        }
                        text (
                            bokmal { + "." },
                            nynorsk { + "." },
                            english { + " of the Norwegian National Insurance Act." },
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
                showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                    //[TBU3353EN, TBU3353, TBU3353NN]

                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-17, 12-19 og " },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-17, 12-19 og " },
                            english { + "The decision has been made pursuant to Section 12-8 to 12-14, 12-17, 12-19 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                                nynorsk { + "22-12" },
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                                nynorsk { + "22-13" },
                                english { + "22-13" },
                            )
                        }
                        text (
                            bokmal { + "." },
                            nynorsk { + "." },
                            english { + " of the Norwegian National Insurance Act." },
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  ) THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                    //[TBU3354]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-16, 12-19 og " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                            )
                        }
                        text (
                            bokmal { + "." },
                        )
                    }
                    //[TBU3354]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-16, 12-19 og " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                nynorsk { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                nynorsk { + "22-13" },
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                            )
                        }
                        text (
                            nynorsk { + "." },
                        )
                    }
                    //[TBU3354]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            english { + "The decision has been made pursuant to Section 12-8 to 12-16, 12-19 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                english { + "22-13" },
                            )
                        }
                        text (
                            english { + " of the Norwegian National Insurance Act" },
                        )

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                english { + " and the transitional provisions for the child supplement in your disability benefit" },
                            )
                        }
                        text (
                            english { + "." },
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  ) THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                    //[TBU3355]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-17, 12-19 og " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                            )
                        }
                        text (
                            bokmal { + "." },
                        )
                    }
                    //[TBU3355]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-17, 12-19 og " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                nynorsk { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                nynorsk { + "22-13" },
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                            )
                        }
                        text (
                            nynorsk { + "." },
                        )
                    }
                    //[TBU3355]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            english { + "The decision has been made pursuant to Section 12-8 to 12-17, 12-19 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                english { + "22-13" },
                            )
                        }
                        text (
                            english { + " of the Norwegian National Insurance Act" },
                        )

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                english { + " and the transitional provisions for the child supplement in your disability benefit" },
                            )
                        }
                        text (
                            english { + "." },
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo") THEN      INCLUDE ENDIF
                showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                    //[TBU3356EN, TBU3356, TBU3356NN]

                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-20 og " },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-20 og " },
                            english { + "The decision has been made pursuant to Section 12-8 to 12-14, 12-20 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                                nynorsk { + "22-12" },
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                                nynorsk { + "22-13" },
                                english { + "22-13" },
                            )
                        }
                        text (
                            bokmal { + "." },
                            nynorsk { + "." },
                            english { + " of the Norwegian National Insurance Act." },
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo") THEN      INCLUDE ENDIF
                showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                    //[TBU3357EN, TBU3357, TBU3357NN]

                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-17, 12-20 og " },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-17, 12-20 og " },
                            english { + "The decision has been made pursuant to Section 12-8 to 12-14, 12-17, 12-20 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                                nynorsk { + "22-12" },
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                                nynorsk { + "22-13" },
                                english { + "22-13" },
                            )
                        }
                        text (
                            bokmal { + "." },
                            nynorsk { + "." },
                            english { + " of the Norwegian National Insurance Act." },
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  ) THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                    //[TBU3358]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-16, 12-20 og " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                            )
                        }
                        text (
                            bokmal { + "." },
                        )
                    }
                    //[TBU3358]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-16, 12-20 og " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                nynorsk { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                nynorsk { + "22-13" },
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                            )
                        }
                        text (
                            nynorsk { + "." },
                        )
                    }
                    //[TBU3358]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            english { + "The decision has been made pursuant to Section 12-8 to 12-16, 12-20 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                english { + "22-13" },
                            )
                        }
                        text (
                            english { + " of the Norwegian National Insurance Act" },
                        )

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                english { + " and the transitional provisions for the child supplement in your disability benefit" },
                            )
                        }
                        text (
                            english { + "." },
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  ) THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                    //[TBU3359]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-17, 12-20 og " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden." },
                            )
                        }
                        text (
                            bokmal { + " " },
                        )
                    }
                    //[TBU3359]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-17, 12-20 og " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                nynorsk { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                nynorsk { + "22-13" },
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                            )
                        }
                        text (
                            nynorsk { + "." },
                        )
                    }
                    //[TBU3359]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            english { + "The decision has been made pursuant to Section 12-8 to 12-17, 12-20 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                english { + "22-13" },
                            )
                        }
                        text (
                            english { + " of the Norwegian National Insurance Act" },
                        )

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                english { + " and the transitional provisions for the child supplement in your disability benefit" },
                            )
                        }
                        text (
                            english { + "." },
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  ) THEN      INCLUDE ENDIF
                showIf(((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                    //[TBU3710EN, TBU3710, TBU3710NN]

                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-18, 12-19 og " },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-18, 12-19 og " },
                            english { + "The decision has been made pursuant to Section 12-8 to 12-14, 12-18, 12-19 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                                nynorsk { + "22-12" },
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                                nynorsk { + "22-13" },
                                english { + "22-13" },
                            )
                        }
                        text (
                            bokmal { + "." },
                            nynorsk { + "." },
                            english { + " of the Norwegian National Insurance Act." },
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  ) THEN      INCLUDE ENDIF
                showIf(((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                    //[TBU3711EN, TBU3711, TBU3711NN]

                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-17 til 12-19 og " },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-17 til 12-19 og " },
                            english { + "The decision has been made pursuant to Section 12-8 to 12-14, 12-17 to 12-19 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                                nynorsk { + "22-12" },
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                                nynorsk { + "22-13" },
                                english { + "22-13" },
                            )
                        }
                        text (
                            bokmal { + "." },
                            nynorsk { + ". " },
                            english { + " of the Norwegian National Insurance Act." },
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  ) THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                    //[TBU3712]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-16, 12-18, 12-19 og " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                            )
                        }
                        text (
                            bokmal { + ". " },
                        )
                    }
                    //[TBU3712]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-16, 12-18, 12-19 og " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                nynorsk { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                nynorsk { + "22-13" },
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                            )
                        }
                        text (
                            nynorsk { + "." },
                        )
                    }
                    //[TBU3712]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            english { + "The decision has been made pursuant to Section 12-8 to 12-16, 12-18, 12-19 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                english { + "22-13" },
                            )
                        }
                        text (
                            english { + " of the Norwegian National Insurance Act" },
                        )

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                english { + " and the transitional provisions for the child supplement in your disability benefit" },
                            )
                        }
                        text (
                            english { + "." },
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  ) THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                    //[TBU3713]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-19 og " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                            )
                        }
                        text (
                            bokmal { + ". " },
                        )
                    }
                    //[TBU3713]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-19 og " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                nynorsk { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                nynorsk { + "22-13" },
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                            )
                        }
                        text (
                            nynorsk { + "." },
                        )
                    }
                    //[TBU3713]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            english { + "The decision has been made pursuant to Section 12-8 to 12-19 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                english { + "22-13" },
                            )
                        }
                        text (
                            english { + " of the Norwegian National Insurance Act" },
                        )

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                english { + " and the transitional provisions for the child supplement in your disability benefit" },
                            )
                        }
                        text (
                            english { + "." },
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  ) THEN      INCLUDE ENDIF
                showIf(((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                    //[TBU3714EN, TBU3714, TBU3714NN]

                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-18, 12-20 og " },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-18, 12-20 og " },
                            english { + "The decision has been made pursuant to Section 12-8 to 12-14, 12-18, 12-20 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                                nynorsk { + "22-12" },
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                                nynorsk { + "22-13" },
                                english { + "22-13" },
                            )
                        }
                        text (
                            bokmal { + "." },
                            nynorsk { + "." },
                            english { + " of the Norwegian National Insurance Act." },
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  ) THEN      INCLUDE ENDIF
                showIf(((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                    //[TBU3715EN, TBU3715, TBU3715NN]

                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-17, 12-18 og 12-20 og " },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-17, 12-18 og 12-20 og " },
                            english { + "The decision has been made pursuant to Section 12-8 to 12-14, 12-17, 12-18, 12-20 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                                nynorsk { + "22-12" },
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                                nynorsk { + "22-13" },
                                english { + "22-13" },
                            )
                        }
                        text (
                            bokmal { + "." },
                            nynorsk { + "." },
                            english { + " of the Norwegian National Insurance Act." },
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  ) THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                    //[TBU3716]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-16, 12-18, 12-20 og " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = ""overgangsregler_2016""
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("")){
                            text (
                                bokmal { + "." },
                            )
                        }
                    }
                    //[TBU3716]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-16, 12-18, 12-20 og " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                nynorsk { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                nynorsk { + "22-13" },
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                            )
                        }
                        text (
                            nynorsk { + "." },
                        )
                    }
                    //[TBU3716]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            english { + "The decision has been made pursuant to Section 12-8 to 12-16, 12-18, 12-20 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                english { + "22-13" },
                            )
                        }
                        text (
                            english { + " of the Norwegian National Insurance Act" },
                        )

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                english { + " and the transitional provisions for the child supplement in your disability benefit" },
                            )
                        }
                        text (
                            english { + "." },
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  ) THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                    //[TBU3717]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-18, 12-20 og " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                bokmal { + "22-13" },
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                            )
                        }
                        text (
                            bokmal { + "." },
                        )
                    }
                    //[TBU3717]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-18, 12-20 og " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                nynorsk { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                nynorsk { + "22-13" },
                            )
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                            )
                        }
                        text (
                            nynorsk { + "." },
                        )
                    }
                    //[TBU3717]
                    // TODO Failure while merging. Expected conditional for all language variants. Needs manual merge.
                    paragraph {
                        text (
                            english { + "The decision has been made pursuant to Section 12-8 to 12-18, 12-20 and " },
                        )

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                            text (
                                english { + "22-12" },
                            )
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                            text (
                                english { + "22-13" },
                            )
                        }
                        text (
                            english { + " of the Norwegian National Insurance Act" },
                        )

                        //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                        showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                            text (
                                english { + " and the transitional provisions for the child supplement in your disability benefit" },
                            )
                        }
                        text (
                            english { + "." },
                        )
                    }
                }
            }
        }
    }
}




