package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.OpphoersbegrunnelseSelectors.barn_flyttet_ikke_avt_land
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.OpphoersbegrunnelseSelectors.barn_opph_ikke_avt_land
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.OpphoersbegrunnelseSelectors.bruker_flyttet_ikke_avt_land
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.OpphoersbegrunnelseSelectors.eps_flyttet_ikke_avt_land
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.OpphoersbegrunnelseSelectors.eps_opph_ikke_avt_land
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.annen_forld_rett_bt
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.antallBarnOpphor
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.bt_innt_over_1g
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.bt_over_18
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.fodselsdatobarn
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.mindre_ett_ar_bt_flt
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.opphoersbegrunnelse
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.opphortBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.opphortEktefelletillegg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.opphortGjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.pe
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.adhoc.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.maler.fraser.common.Constants.KLAGE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.KONTAKT_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.SKATTEETATEN_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.UFOERETRYGD_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.fraser.*
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate

@TemplateModelHelpers
object EndringUforetrygd : RedigerbarTemplate<EndringUfoeretrygdDto> {

    override val featureToggle = FeatureToggles.brevmalUtInnvilgelse.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_ENDRING_UFOERETRYGD
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uføretrygd",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Nav har innvilget søknaden din om endring av uføretrygd" },
                nynorsk { +"Nav har innvilget søknaden din om endring av uføretrygd " },
            )
        }
        outline {
            val pe = pesysData.pe

            val uforetidspunkt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt().ifNull(LocalDate.now())
            val onsketvirkningsdato = pe.vedtaksdata_kravhode_onsketvirkningsdato().ifNull(LocalDate.now())
            val skadetidspunkt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_skadetidspunkt().ifNull(LocalDate.now())
            val virkningbegrunnelseStdbegr_22_12_1_5 = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse().equalTo("stdbegr_22_12_1_5")
            val paragraf22_12_or_22_13 = if (virkningbegrunnelseStdbegr_22_12_1_5.equals(true)) "22-13" else "22-12"
            val fodselsdatoBarn = pesysData.fodselsdatobarn.ifNull("")
            val barnet_barna_opphor = if (pesysData.antallBarnOpphor.greaterThan(1).equals(true)) "barna" else "barnet"
            val barnet_barna_opphor_forsørga_forsørgde = if (pesysData.antallBarnOpphor.greaterThan(1).equals(true)) "barna forsørgde" else "barnet forsørga"
            val dine_ditt_barn_opphor = if (pesysData.antallBarnOpphor.greaterThan(1).equals(true)) "dine barn" else "ditt barn"
            val barnet_barna_opphor_ditt_dine = if (pesysData.antallBarnOpphor.greaterThan(1).equals(true)) "barna dine" else "barnet ditt"

            //IF(PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_uu" AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_ys" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "barn_endret_inntekt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "eps_endret_inntekt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "begge_for_end_inn" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "omgj_etter_klage" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "omgj_etter_anke" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravgjelder().notEqualTo("sok_uu") and pe.vedtaksdata_kravhode_kravgjelder().notEqualTo("sok_ys") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endret_inntekt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("barn_endret_inntekt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("eps_endret_inntekt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("begge_for_end_inn") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("omgj_etter_klage") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("omgj_etter_anke") and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()))) {
                //[TBU2287EN, TBU2287, TBU2287NN]

                paragraph {
                    text(
                        bokmal { +"Vi har endret uføretrygden din fra " + onsketvirkningsdato.format() + "." },
                        nynorsk { +"Vi har endra uføretrygda di frå " + onsketvirkningsdato.format() + "." }
                    )
                }
            }

            //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) AND PE_Vedtaksdata_Kravhode_KravArsakType = "soknad_bt" ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_kravhode_kravarsaktype().equalTo("soknad_bt"))) {
                //[TBU2288]
                paragraph {
                    text(
                        bokmal { +"Vi har innvilget søknaden din om barnetillegg som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har endret uføretrygden din fra " + onsketvirkningsdato.format() + "." },
                        nynorsk { +"Vi har innvilga søknaden din om barnetillegg som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har endra uføretrygda di frå " + onsketvirkningsdato.format() + "." },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND  (((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0)   AND   (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0))   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false))  ) THEN      INCLUDE ENDIF
                    showIf(
                        (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0))) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto()
                            .equalTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))))
                    ) {
                        text(
                            bokmal { +" Tillegget blir ikke utbetalt fordi inntekten til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { +" Tillegget blir ikkje utbetalt, fordi inntekta til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din er over grensa for å få utbetalt barnetillegg." },
                        )
                    }

                    //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)  AND  (((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0)   AND   (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0))   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false))  ) THEN      INCLUDE ENDIF
                    showIf(
                        ((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) and (((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0))) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto()
                            .equalTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))))
                    ) {
                        text(
                            bokmal { +" Tillegget blir ikke utbetalt fordi inntekten din er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { +"Tillegget blir ikkje utbetalt fordi inntekta di er over grensa for å få utbetalt barnetillegg." }
                        )
                    }
                }
            }

            //IF(  (PE_Vedtaksdata_Kravhode_KravArsakType = "omgj_etter_klage" OR PE_Vedtaksdata_Kravhode_KravArsakType = "omgj_etter_anke")  ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("omgj_etter_klage") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("omgj_etter_anke")))) {
                //[TBU3332EN, TBU3332, TBU3332NN]

                paragraph {
                    text(
                        bokmal { +"Vi har endret uføretrygden din fra " + onsketvirkningsdato.format() + " fordi du har fått medhold i klagen din." },
                        nynorsk { +"Vi har endra uføretrygda di frå " + onsketvirkningsdato.format() + " fordi du har fått medhald i klaga di." },
                    )
                }
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND  (PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt" OR  PE_Vedtaksdata_Kravhode_KravArsakType = "barn_endret_inntekt" OR PE_Vedtaksdata_Kravhode_KravArsakType = "eps_endret_inntekt" OR PE_Vedtaksdata_Kravhode_KravArsakType = "begge_for_end_inn")    ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and (pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("barn_endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("eps_endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("begge_for_end_inn")))) {
                //[TBU2289EN, TBU2289, TBU2289NN]

                paragraph {
                    text(
                        bokmal { +"Vi har endret barnetillegget i uføretrygden din fordi du har meldt fra om inntektsendring." },
                        nynorsk { +"Vi har endra barnetillegget i uføretrygda di fordi du har meldt frå om inntektsendring." },
                    )
                }
            }

            //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
            showIf(pesysData.opphortBarnetillegg) {
                paragraph {
                    text(
                        bokmal { +"Vi har vedtatt at barnetillegget i uføretrygden din opphører fra " + onsketvirkningsdato.format() + " for barn født " + fodselsdatoBarn },
                        nynorsk { +"Vi har stansa barnetillegget i uføretrygda di frå " + onsketvirkningsdato.format() + " for barn fødd " + fodselsdatoBarn },
                    )
                }
            }

            //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_et, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
            showIf(pesysData.opphortEktefelletillegg) {
                //[TBU2291EN, TBU2291, TBU2291NN]

                paragraph {
                    text(
                        bokmal { +"Vi har opphørt ektefelletillegget i uføretrygden din fra " + onsketvirkningsdato.format() + "." },
                        nynorsk { +"Vi har stansa ektefelletillegget i uføretrygda di frå " + onsketvirkningsdato.format() + "." },
                    )
                }
            }

            //PE_Vedtaksdata_Kravhode_KravGjelder = "sok_uu"
            showIf(pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_uu")) {
                //[TBU2292EN, TBU2292, TBU2292NN]

                paragraph {
                    text(
                        bokmal { +"Vi har innvilget søknaden din om rettighet som ung ufør som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har endret uføretrygden din fra " + onsketvirkningsdato.format() + "." },
                        nynorsk { +"Vi har innvilga søknaden din om å få rettar som ung ufør som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har endra uføretrygda di frå " + onsketvirkningsdato.format() + "." },
                    )
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) = FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) AND PE_Vedtaksdata_Kravhode_KravGjelder = "sok_ys") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).equalTo((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad())) and pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_ys"))) {
                //[TBU2293EN, TBU2293, TBU2293NN]

                paragraph {
                    text(
                        bokmal { +"Vi har innvilget søknaden din om uføretrygd etter særbestemmelsene for yrkesskade eller yrkessykdom som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har kommet fram til at hele uførheten din skyldes en godkjent yrkesskade eller yrkessykdom, og uføretrygden din er endret fra " + onsketvirkningsdato.format() + "." },
                        nynorsk { +"Vi har innvilga søknaden din om uføretrygd etter særreglane for yrkesskade eller yrkessjukdom som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har kome fram til at heile uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom, og uføretrygda di er endra frå " + onsketvirkningsdato.format() + "." },
                    )
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravGjelder = "sok_ys") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_ys"))) {
                //[TBU2294EN, TBU2294, TBU2294NN]

                paragraph {
                    text(
                        bokmal { +"Vi har innvilget søknaden din om uføretrygd etter særbestemmelsene for yrkesskade eller yrkessykdom som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har kommet fram til at " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().format() + " prosent av uførheten din skyldes en godkjent yrkesskade eller yrkessykdom, og uføretrygden din er endret fra " + onsketvirkningsdato.format() + "." },
                        nynorsk { +"Vi har innvilga søknaden din om uføretrygd etter særreglane for yrkesskade eller yrkessjukdom som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har kome fram til at " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().format() + " prosent av uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom, og uføretrygda di er endra frå " + onsketvirkningsdato.format() + "." },
                    )
                }
            }

            //PE_Vedtaksdata_Kravhode_KravArsakType = "endring_ifu"
            showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endring_ifu")) {
                //[TBU2295EN, TBU2295, TBU2295NN]

                paragraph {
                    text(
                        bokmal { +"Vi har innvilget søknaden din om endring av inntektsgrense." },
                        nynorsk { +"Vi har innvilga søknaden din om endring av inntektsgrense." },
                    )
                    text(
                        bokmal { +"Den nye inntektsgrensen din har økt til " + pe.ut_inntektsgrense_faktisk().format() + " kroner fra " + onsketvirkningsdato.format() + "." },
                        nynorsk { +"Den nye inntektsgrensa di har auka til " + pe.ut_inntektsgrense_faktisk().format() + " kroner frå " + onsketvirkningsdato.format() + "." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                //[TBU2296EN, TBU2296, TBU2296NN]

                paragraph {
                    text(
                        bokmal { +"Vi har innvilget deg gjenlevenderettigheter i uføretrygden din. Vi har endret uføretrygden din fra " + onsketvirkningsdato.format() + "." },
                        nynorsk { +"Vi har innvilga deg attlevanderettar i uføretrygda di. Vi har endra uføretrygda di frå " + onsketvirkningsdato.format() + "." },
                    )
                }
            }

            //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_ut_gjt, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
            showIf(pesysData.opphortGjenlevendetillegg) {
                //[TBU2297EN, TBU2297, TBU2297NN]

                paragraph {
                    text(
                        bokmal { +"Vi har opphørt gjenlevendetillegget i uføretrygden din fra " + onsketvirkningsdato.format() + "." },
                        nynorsk { +"Vi har stansa attlevandetillegget i uføretrygda di frå " + onsketvirkningsdato.format() + "." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "instopphold" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("instopphold") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))) {
                //[TBU3333EN, TBU3333, TBU3333NN]

                paragraph {
                    text(
                        bokmal { +"Vi har endret uføretrygden din fra " + onsketvirkningsdato.format() + " fordi du ble innlagt på institusjon." },
                        nynorsk { +"Vi har endra uføretrygda di frå " + onsketvirkningsdato.format() + " fordi du ble innlagd på institusjon." },
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
                    )

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  AND  PE_UT_Forsorgeransvar_ingen_er_false()  AND PE_Vedtaksdata_Kravhode_KravArsakType ="instopphold" AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0)  THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksdata_kravhode_kravarsaktype().equalTo("instopphold") and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0))) {
                        text(
                            bokmal { +" Fra og med måneden etter at du ble innlagt på institusjon vil du bli vurdert med sivilstand som enslig. Det betyr at barnetillegget kun vil bli beregnet ut fra den samlede inntekten din fra samme tidspunkt." },
                            nynorsk { +" Frå og med månaden etter at du blei innlagd på institusjon vil du bli vurdert med ein sivilstand som einsleg. Det betyr at barnetillegget berre blir berekna ut frå den samla inntekta di frå same tidspunkt. " },
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
                    )

                    // IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  AND  PE_UT_Forsorgeransvar_ingen_er_false()  AND PE_Vedtaksdata_Kravhode_KravArsakType ="instopphold" AND  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true))  THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksdata_kravhode_kravarsaktype().equalTo("instopphold") and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))) {
                        text(
                            bokmal { +" Fra og med måneden etter at du er under straffegjennomføring vil du bli vurdert med sivilstand som enslig. Det betyr at barnetillegget kun vil bli beregnet ut fra den samlede inntekten din fra samme tidspunkt." },
                            nynorsk { +" Frå og med månaden etter at du er under straffegjennomføringa vil du bli vurdert med ein sivilstand som einsleg. Det betyr at barnetillegget berre blir berekna ut frå den samla inntekta di frå same tidspunkt." },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "instopphold" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("instopphold") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))) {
                //[TBU3335EN, TBU3335, TBU3335NN]

                paragraph {
                    text(
                        bokmal { +"Vi har endret utbetalingen av uføretrygden din fra " + onsketvirkningsdato.format() + " fordi du er under straffegjennomføring." },
                        nynorsk { +"Vi har stansa utbetalinga av uføretrygda di frå " + onsketvirkningsdato.format() + " fordi du er under straffegjennomføring." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "instopphold" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("instopphold") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo(""))) {
                //[TBU3337EN, TBU3337, TBU3337NN]

                paragraph {
                    text(
                        bokmal { +"Vi har gjenopptatt utbetaling av uføretrygden din fra " + onsketvirkningsdato.format() + "." },
                        nynorsk { +"Vi har teke til att med å utbetale uføretrygda di frå " + onsketvirkningsdato.format() + "." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false) THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                includePhrase(TBU1120_Generated(pe))
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                includePhrase(TBU1121_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false) THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                includePhrase(TBU1122_Generated(pe))
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                includePhrase(TBU1123_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETnetto > 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etnetto().greaterThan(0))) {
                includePhrase(TBU1253_Generated(pe))
            }

            //IF(  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETnetto > 0  AND   ((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0))  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etnetto().greaterThan(0) and ((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().greaterThan(0)) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().greaterThan(0))) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                includePhrase(TBU1254_Generated(pe))
            }

            //IF(   PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad  AND  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and (not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd per måned før skatt. Fordi du har inntekt ved siden av uføretrygden, vil utbetalingen din bli redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per måned før skatt." },
                        nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd per månad før skatt. Fordi du har inntekt ved sida av uføretrygda, blir utbetalinga di redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per månad før skatt." },
                    )
                }
            }

            //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND   (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)   AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd og barnetillegg per måned før skatt. Fordi du har inntekt ved siden av uføretrygden, vil utbetalingen din bli redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per måned før skatt." },
                        nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd og barnetillegg per månad før skatt. Fordi du har inntekt ved sida av uføretrygda, blir utbetalinga di redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per månad før skatt." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd og gjenlevendetillegg per måned før skatt. Fordi du har inntekt ved siden av uføretrygden, vil utbetalingen din bli redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per måned før skatt." },
                        nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd og attlevandetillegg per månad før skatt. Fordi du har inntekt ved sida av uføretrygda, blir utbetalinga di redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per månad før skatt." },
                    )
                }
            }

            //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad  AND   (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)   AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and (not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) or not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd og ektefelletillegg per måned før skatt. Fordi du har inntekt ved siden av uføretrygden, vil utbetalingen din bli redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per måned før skatt." },
                        nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd og ektefelletillegg per månad før skatt. Fordi du har inntekt ved sida av uføretrygda, blir utbetalinga di redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per månad før skatt." },
                    )
                }
            }

            //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd, barne- og gjenlevendetillegg per måned før skatt. Fordi du har inntekt ved siden av uføretrygden, vil utbetalingen din bli redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per måned før skatt." },
                        nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd, barne- og attlevandetillegg per månad før skatt. Fordi du har inntekt ved sida av uføretrygda, blir utbetalinga di redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per månad før skatt." },
                    )
                }
            }

            //IF(  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo" AND PE_UT_Forsorgeransvar_ingen_er_true()  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true   ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and pe.ut_forsorgeransvar_ingen_er_true() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt())) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men den kommer ikke til utbetaling fordi du er under straffegjennomføring." },
                        nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men ho blir ikkje betalt ut fordi du er under straffegjennomføring." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt())) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalingen er redusert fordi du er innlagt på institusjon. I denne perioden vil du få utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                        nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalinga er redusert fordi du er innlagd på institusjon. I denne perioden får du betalt ut " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  AND PE_UT_Forsorgeransvar_ingen_er_false()  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt())) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalingen er redusert fordi du er under straffegjennomføring. I denne perioden vil du få utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                        nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalinga er redusert fordi du er under straffegjennomføring. I denne perioden får du betalt ut " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                    )
                }
            }

            //IF((FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) = "nor"  OR FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) = "")  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = true  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true)) THEN      INCLUDE ENDIF
            showIf((((pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("nor") or (pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("")) and (pe.vedtaksdata_beregningsdata_beregningufore_belopokt() or pe.vedtaksdata_beregningsdata_beregningufore_belopredusert()))) {
                paragraph {
                    text(
                        bokmal { +"Uføretrygden blir utbetalt senest den 20. hver måned. Du får din første utbetaling med nytt beløp i " + fritekst("måned og år") + "." },
                        nynorsk { +"Uføretrygda blir utbetalt seinast den 20. i kvar månad. Du får den første utbetalinga di med nytt beløp i " + fritekst("måned og år") + "." },
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) <> "nor" AND FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) <> "") THEN      INCLUDE ENDIF
            showIf(((pe.grunnlag_persongrunnlagsliste_personbostedsland()).notEqualTo("nor") and (pe.grunnlag_persongrunnlagsliste_personbostedsland()).notEqualTo(""))) {
                //[TBU2304mFRI_EN, TBU2304mFRI, TBU2304mFRINN]

                paragraph {
                    text(
                        bokmal { +"Uføretrygden blir utbetalt senest den 20. hver måned. Mottar du uføretrygden på en utenlandsk bankkonto kan utbetalingen bli forsinket. Du får din første utbetaling i " + fritekst("måned og år") + "." },
                        nynorsk { +"Uføretrygda blir utbetalt seinast den 20. i kvar månad. Får du uføretrygda på ein utanlandsk bankkonto, kan utbetalinga bli forseinka. Du får den første utbetalinga di i " + fritekst("måned og år") + "." },
                    )
                }
            }

            //IF((PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = false) AND(FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) = "nor"  OR FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) = "")) THEN      INCLUDE ENDIF
            showIf(((not(pe.vedtaksdata_beregningsdata_beregningufore_belopokt()) and not(pe.vedtaksdata_beregningsdata_beregningufore_belopredusert())) and ((pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("nor") or (pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("")))) {
                includePhrase(TBU2223_Generated)
            }
            paragraph {
                text(
                    bokmal { +"I dette brevet forklarer vi hvilke rettigheter og plikter du har. Det er derfor viktig at du leser hele brevet." },
                    nynorsk { +"I dette brevet forklarer vi kva rettar og plikter du har. Det er derfor viktig at du les heile brevet." },
                )
            }

            title1 {
                text(
                    bokmal { +"Begrunnelse for vedtaket" },
                    nynorsk { +"Grunngiving for vedtaket" },
                )
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold" AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_EktefelleGarantiTillegg_EGTinnvilget = false) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold") and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefellegarantitillegg_egtinnvilget()))) {
                //[TBU2305EN, TBU2305, TBU2305NN]

                paragraph {
                    text(
                        bokmal { +"Du har uføretrygd med minsteytelse. Størrelsen på minsteytelsen er avhengig av sivilstanden din." },
                        nynorsk { +"Du har uføretrygd med minsteyting. Storleiken på minsteytinga er avhengig av sivilstanden din." },
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
                    )
                }
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5"  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2"  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed_ektefelle"  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed_registrert_partner")  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = false AND PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"   ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 1-5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed_ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed_registrert_partner")) and pe.vedtaksdata_beregningsdata_beregningufore_belopredusert() and not(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse()) and pe.vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring"))) {
                //[TBU2321mFRI_EN, TBU2321mFRI, TBU2321mFRINN]

                paragraph {
                    text(
                        bokmal { +"Vi har mottatt opplysninger om at du " + fritekst("sivilstandsendring") + ". Du har minsteytelse i uføretrygden din. Den endrede sivilstanden din medfører nå at du får uføretrygd på grunnlag av egen opptjening." },
                        nynorsk { +"Vi har fått opplysningar om at du " + fritekst("sivilstandsendring") + ". Du har minsteyting i uføretrygda di. Den endra sivilstanden din fører no til at du får uføretrygd på grunnlag av di eiga opptening." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravGjelder = "sok_uu" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat) = "oppfylt") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_uu") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat()).equalTo("oppfylt"))) {
                //[TBU2322EN, TBU2322, TBU2322NN]

                paragraph {
                    text(
                        bokmal { +"For å innvilges rettighet som ung ufør, må du være alvorlig og varig syk før du fylte 26 år. Uføretidspunktet ditt er fastsatt til " + uforetidspunkt.format() + ". Du er innvilget rettighet som ung ufør. Det betyr at uføretrygden din vil bli beregnet etter regler som gjelder ung ufør." },
                        nynorsk { +"For at du skal få innvilga rett som ung ufør, må du vere alvorleg og varig sjuk før du fylte 26 år. Uføretidspunktet ditt er sett til " + uforetidspunkt.format() + ". Du er innvilga rett som ung ufør. Det betyr at uføretrygda di blir berekna etter reglar som gjeld ung ufør." },
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
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false
                    showIf(not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup())) {
                        text(
                            bokmal { +" Dette betyr at uføretrygden din vil bli beregnet etter særbestemmelser som gir deg en høyere uføretrygd." },
                            nynorsk { +" Dette betyr at uføretrygda di blir berekna etter særreglar, og gir deg ei høgare uføretrygd." },
                        )
                    }
                }
                paragraph {
                    text(
                        bokmal { +"Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner." },
                        nynorsk { +"Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta di på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner." },
                    )
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,1) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IVSBegrunnelse) = "stdbegr_12_17_3_2") THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsbegrunnelse()).equalTo("stdbegr_12_17_3_2"))) {
                    paragraph {
                        text(
                            bokmal { +"Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner. Vi har justert denne inntekten opp fordi den skal tilsvare inntekten du ville hatt som frisk på skadetidspunktet." },
                            nynorsk { +"Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner. Vi har justert denne inntekta opp fordi den skal svare til inntekta du ville hatt som frisk på skadetidspunktet." },
                        )
                    }
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = false AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                //[TBU2324EN, TBU2324, TBU2324NN]

                paragraph {
                    text(
                        bokmal { +"Du er innvilget uføretrygd etter regler for yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert at hele din nedsatte inntektsevne skyldes den godkjente yrkesskaden eller yrkessykdommen." },
                        nynorsk { +"Du er innvilga uføretrygd etter reglar for yrkesskade eller yrkessjukdom. Vi har ut frå opplysningane i saka vurdert at heile den nedsette inntektsevna di kjem av den godkjende yrkesskaden eller yrkessjukdommen." },
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false
                    showIf(not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup())) {
                        text(
                            bokmal { +" Dette betyr at uføretrygden din vil bli beregnet etter særbestemmelser, dersom dette er til fordel for deg." },
                            nynorsk { +" Dette betyr at uføretrygda di blir berekna etter særreglar dersom dette er til fordel for deg." },
                        )
                    }
                }
                paragraph {
                    text(
                        bokmal { +"Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner." },
                        nynorsk { +"Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta di på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner." },
                    )
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,1) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IVSBegrunnelse) = "stdbegr_12_17_3_2") THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsbegrunnelse()).equalTo("stdbegr_12_17_3_2"))) {
                    paragraph {
                        text(
                            bokmal { +"Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner. Vi har justert denne inntekten opp fordi den skal tilsvare inntekten du ville hatt som frisk på skadetidspunktet." },
                            nynorsk { +"Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner. Vi har justert denne inntekta opp fordi den skal svare til inntekta du ville hatt som frisk på skadetidspunktet." },
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
                        )
                    }
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) > 0 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).greaterThan(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                //[TBU2372EN, TBU2372, TBU2372NN]

                paragraph {
                    text(
                        bokmal { +"Du er innvilget uføretrygd etter regler for yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert om yrkesskaden eller yrkessykdommen din er årsak til uførheten din." },
                        nynorsk { +"Du er innvilga uføretrygd etter reglar for yrkesskade eller yrkessjukdom. Vi har ut frå opplysningane i saka vurdert om yrkesskaden eller yrkessjukdommen din er årsaka til uførleiken din." },
                    )
                }
                //[TBU2373EN, TBU2373, TBU2373NN]

                paragraph {
                    text(
                        bokmal { +"Vi har kommet fram til at " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().format() + " prosent av uførheten din skyldes godkjent yrkesskade eller yrkessykdom. " + fritekst("konkret begrunnelse") + "" },
                        nynorsk { +"Vi har kome fram til at " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().format() + " prosent av uførleiken din kjem av godkjend yrkesskade eller yrkessjukdom. " + fritekst("konkret begrunnelse") + "." },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = true AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest() and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))) {
                    paragraph {
                        text(
                            bokmal { +"Derfor vil denne delen av uføretrygden din bli beregnet etter særbestemmelser som gir deg en høyere uføretrygd." },
                            nynorsk { +"Derfor vil denne delen av uføretrygda di bli rekna ut etter særreglar som gjer deg ei høgare uføretrygd." },
                        )
                    }
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) > 0 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).greaterThan(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = false AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
                showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))) {
                    paragraph {
                        text(
                            bokmal { +"Denne delen av uføretrygden din vil bli beregnet etter særbestemmelser, dersom dette er til fordel for deg." },
                            nynorsk { +"Denne delen av uføretrygda di blir rekna ut etter særreglar dersom det er til fordel for deg." },
                        )
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) = "stdbegr_12_17_3_1") THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse()).equalTo("stdbegr_12_17_3_1"))) {
                    paragraph {
                        text(
                            bokmal { +"Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner." },
                            nynorsk { +"Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta di på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner." },
                        )
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,1) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IVSBegrunnelse) = "stdbegr_12_17_3_2") THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsbegrunnelse()).equalTo("stdbegr_12_17_3_2"))) {
                    paragraph {
                        text(
                            bokmal { +"Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner. Vi har justert denne inntekten opp fordi den skal tilsvare inntekten du ville hatt som frisk på skadetidspunktet." },
                            nynorsk { +"Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner. Vi har justert denne inntekta opp fordi den skal svare til inntekta du ville hatt som frisk på skadetidspunktet." },
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = false AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
                showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))) {
                    includePhrase(TBU1265_Generated)
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "endring_ifu" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100 AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_9") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endring_ifu") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().lessThan(100) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_9"))) {

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "endring_ifu" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100 AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_9") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endring_ifu") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().lessThan(100) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_9"))) {
                    //[TBU2326EN, TBU2326, TBU2326NN]

                    paragraph {
                        text(
                            bokmal { +"Inntekten i stillingen din har økt og du får derfor en høyere inntektsgrense. Dette gjør vi for at du skal få riktig utbetaling av uføretrygd." },
                            nynorsk { +"Inntekta i stillinga di har auka, og du får derfor ei høgare inntektsgrense. Dette gjer vi for at du skal få riktig utbetaling av uføretrygd." },
                        )
                    }
                }
                includePhrase(TBU1133_Generated)

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_UT_VirkningstidpunktStorreEnn01012016() = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.ut_virkningstidpunktstorreenn01012016())) {
                    //[TBU3821EN, TBU3821, TBU3821_NN]

                    paragraph {
                        text(
                            bokmal { +"Fordi inntekten din har økt, blir størrelsen på barnetillegget ditt endret." },
                            nynorsk { +"Fordi inntekta di har auka, blir storleiken på barnetillegget ditt endra." },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                //[TBU2327EN, TBU2327, TBU2327NN]

                paragraph {
                    text(
                        bokmal { +"Du er innvilget gjenlevendetillegg i uføretrygden fordi du er gjenlevende ektefelle/samboer. Tillegget er beregnet etter ditt eget og den avdødes beregningsgrunnlag. " },
                        nynorsk { +"Du er innvilga attlevandetillegg i uføretrygda fordi du er attlevande ektefelle/sambuar. Tillegget er berekna etter ditt eiga og den avdøde sitt brekningsgrunnlag. " },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse() and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())) {
                        text(
                            bokmal { +"Siden sivilstanden din har endret seg har dette også betydning for størrelsen på uføretrygden din. " },
                            nynorsk { +"Sidan sivilstanden din har endra seg har dette også noko å seie for storleiken på uføretrygda di. " },
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true   OR (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true        AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTnetto > 0       )    ) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse() or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtnetto().greaterThan(0)))) {
                        text(
                            bokmal { +"Det betyr at uføretrygden din har økt. " },
                            nynorsk { +"Det betyr at uføretrygda di har auka. " },
                        )
                    }

                    //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_NyttGjenlevendetillegg = true
                    showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg()) {
                        text(
                            bokmal { +"Tillegget er tidsbegrenset til fem år fra virkningstidspunktet. " },
                            nynorsk { +"Tillegget er tidsbegrensa til fem år frå verknadstidspunktet. " },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                //[TBU2328EN, TBU2328, TBU2328NN]

                paragraph {
                    text(
                        bokmal { +"Avdøde må også ha vært medlem i folketrygden, eller mottatt pensjon fra folketrygden, de siste " + fritekst("tre/fem årene") + " før dødsfallet." },
                        nynorsk { +"Avdøde må også ha vore medlem i folketrygda, eller fått pensjon frå folketrygda, dei siste " + fritekst("tre/fem åra") + " før dødsfallet." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningBenyttetSivilstand= "gift" AND PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_ut_gjt, PE_UT_KONST_VilkarsVedtakResultat_opphor)) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring") and pe.vedtaksdata_beregningsdata_beregning_beregningbenyttetsivilstand().equalTo("gift") and pesysData.opphortGjenlevendetillegg)) {
                //[TBU2329EN, TBU2329, TBU2329NN]

                paragraph {
                    text(
                        bokmal { +"Du er tidligere innvilget gjenlevendetillegg i uføretrygden din. Gjenlevendetillegget ditt opphører fra " + onsketvirkningsdato.format() + " fordi du har giftet deg." },
                        nynorsk { +"Du er tidlegare innvilga attlevandetillegg i uføretrygda di. Attlevandetillegget ditt tek slutt frå " + onsketvirkningsdato.format() + " fordi du har gifta deg." },
                    )
                }
            }

            //IF(  PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"  AND  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningBrukerSivilstand = "samboer1_5"  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningBrukerSivilstand = "samboer3_2"  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningBrukerSivilstand <> "gift")  AND PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_ut_gjt, PE_UT_KONST_VilkarsVedtakResultat_opphor)  ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring") and (pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().equalTo("samboer1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().equalTo("samboer3_2") and pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().notEqualTo("gift")) and pesysData.opphortGjenlevendetillegg)) {
                //[TBU2330EN, TBU2330, TBU2330NN]

                paragraph {
                    text(
                        bokmal { +"Du er tidligere innvilget gjenlevendetillegg i uføretrygden din. Gjenlevendetillegget ditt opphører fra " + onsketvirkningsdato.format() + " fordi du er i et samboerforhold, og dere har felles barn." },
                        nynorsk { +"Du er tidlegare innvilga attlevandetillegg i uføretrygda di. Attlevandetillegget ditt tek slutt frå " + onsketvirkningsdato.format() + " fordi du er i eit sambuarforhold og de har felles barn." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "sivilstandsendring"  AND  PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_et, PE_UT_KONST_VilkarsVedtakResultat_opphor) AND PE_Vedtaksdata_Kravhode_KravArsakType <> "sokand_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold" ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("sivilstandsendring") and pesysData.opphortEktefelletillegg and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("sokand_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                //[TBU2331]
                paragraph {

                    //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bruker_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_opph_ikke_avt_land") > 0 ) THEN      EXCLUDE ENDIF
                    showIf(not(pesysData.opphoersbegrunnelse.bruker_flyttet_ikke_avt_land or pesysData.opphoersbegrunnelse.eps_flyttet_ikke_avt_land or pesysData.opphoersbegrunnelse.eps_opph_ikke_avt_land)) {
                        text(
                            bokmal { +"Ektefelletillegg beholdes bare ut den perioden som vedtaket gjelder for. Vi har derfor opphørt ektefelletillegget." },
                            nynorsk { +"Ektefelletillegget beheld du bare ut perioden vedtaket gjeld for. Vi har derfor stansa ektefelletillegget ditt. " },
                        )
                    }

                    //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bruker_flyttet_ikke_avt_land") > 0) THEN      INCLUDE ENDIF
                    showIf(pesysData.opphoersbegrunnelse.bruker_flyttet_ikke_avt_land) {
                        text(
                            bokmal { +"Ifølge våre opplysninger er du bosatt i " + fritekst("bostedsland") + ". Derfor har du ikke lenger rett til ektefelletillegg." },
                            nynorsk { +"Ifølgje våre opplysningar er du busett i " + fritekst("bostedsland") + ". Da har du ikkje lenger rett til ektefelletillegg." },
                        )
                    }

                    //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "barn_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_opph_ikke_avt_land") > 0 ) THEN      INCLUDE ENDIF
                    showIf((pesysData.opphoersbegrunnelse.eps_flyttet_ikke_avt_land or pesysData.opphoersbegrunnelse.barn_flyttet_ikke_avt_land or pesysData.opphoersbegrunnelse.eps_opph_ikke_avt_land)) {
                        text(
                            bokmal { +"Ifølge våre opplysninger er " + fritekst("ektefellen/partneren/samboeren din") + " bosatt i " + fritekst("bostedsland") + ". Derfor har du ikke lenger rett til ektefelletetillegg." },
                            nynorsk { +"Ifølgje våre opplysningar er " + fritekst("ektefelle/partner/sambuar") + " busett i " + fritekst("bostedsland") + ". Da har du ikkje lenger rett til ektefelletillegg. " },
                        )
                    }

                    //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bruker_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_opph_ikke_avt_land") > 0 ) THEN      INCLUDE ENDIF
                    showIf((pesysData.opphoersbegrunnelse.bruker_flyttet_ikke_avt_land or pesysData.opphoersbegrunnelse.eps_flyttet_ikke_avt_land or pesysData.opphoersbegrunnelse.eps_opph_ikke_avt_land)) {
                        text(
                            bokmal { +"For å ha rett til ektefelletillegg fra 1. juli 2020 må du og ektefellen/samboeren din enten bo i Norge, innenfor EØS-området eller i et annet land Norge har trygdeavtale med. " },
                            nynorsk { +"For å ha rett til ektefelletillegg frå 1. juli 2020 må du og ektefellen/sambuaren din enten bu i Noreg, innanfor EØS-området eller i eit anna land Noreg har trygdeavtale med. " },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningBenyttetSivilstand = "skil"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningBenyttetSivilstand = "skpa")  AND  PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_et, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring") and (pe.vedtaksdata_beregningsdata_beregning_beregningbenyttetsivilstand().equalTo("skil") or pe.vedtaksdata_beregningsdata_beregning_beregningbenyttetsivilstand().equalTo("skpa")) and pesysData.opphortEktefelletillegg)) {
                //[TBU2332EN, TBU2332, TBU2332NN]

                paragraph {
                    text(
                        bokmal { +"Vi har mottatt opplysninger om at du har blitt skilt. Vi har derfor opphørt ektefelletillegget." },
                        nynorsk { +"Vi har fått opplysningar om at du har blitt skilt. Vi har derfor stansa ektefelletillegget ditt." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningBenyttetSivilstand = "enke" AND  PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_et, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring") and pe.vedtaksdata_beregningsdata_beregning_beregningbenyttetsivilstand().equalTo("enke") and pesysData.opphortEktefelletillegg)) {
                //[TBU2333EN, TBU2333, TBU2333NN]

                paragraph {
                    text(
                        bokmal { +"Vi har mottatt opplysninger om at du har blitt enke/enkemann. Vi har derfor opphørt ektefelletillegget." },
                        nynorsk { +"Vi har fått opplysningar om at du har blitt enkje/enkjemann. Vi har derfor stansa ektefelletillegget ditt." },
                    )
                }
            }

            //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
            showIf(pesysData.opphortBarnetillegg) {
                //[TBU3924_EN, TBU3924, TBU3924_NN]

                paragraph {
                    text(
                        bokmal { +"Vi har opphørt barnetillegget i uføretrygden din for barn født: " + fodselsdatoBarn },
                        nynorsk { +"Vi har stansa barnetillegget i uføretrygda for barn fødd: " + fodselsdatoBarn },
                    )
                }

                //IF( Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bt_over_18") > 1  )THEN      INCLUDE ENDIF
                showIf(pesysData.bt_over_18) {
                    paragraph {
                        text(
                            bokmal { +"For å ha rett til barnetillegg må du forsørge barn under 18 år. Vi har vedtatt at barnetillegget i uføretrygden opphører fordi " + barnet_barna_opphor + " har fylt 18 år. " },
                            nynorsk { +"For å ha rett til barnetillegg må du forsørgje barn under 18 år. Vi har stansa barnetillegget i uføretrygda fordi " + barnet_barna_opphor + " har fylt 18 år." },
                        )
                    }
                }

                //IF( Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "annen_forld_rett_bt") > 1  )THEN      INCLUDE ENDIF
                showIf(pesysData.annen_forld_rett_bt) {
                    //[TBU2607EN, TBU2607, TBU2607_NN]

                    paragraph {
                        text(
                            bokmal { +"" + fritekst("slett det som ikke er aktuelt") + "" },
                            nynorsk { +"" + fritekst("slett det som ikke er aktuelt") + "" },
                        )
                    }
                }

                //IF( Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "annen_forld_rett_bt") > 1  )THEN      INCLUDE ENDIF
                showIf(pesysData.annen_forld_rett_bt) {
                    //[TBU3921_EN, TBU3921, TBU3921_NN]

                    paragraph {
                        text(
                            bokmal { +"Når " + barnet_barna_opphor + " blir forsørget av begge foreldrene og begge mottar uføretrygd, skal barnetillegget gis til den som får det høyeste tillegget. Den andre forelderen har rett til et høyere barnetillegg enn det du vil få. Vi har derfor opphørt barnetillegget i uføretrygden din. " },
                            nynorsk { +"Når " + barnet_barna_opphor + " blir " + barnet_barna_opphor_forsørga_forsørgde + " av begge foreldra og begge får uføretrygd, blir barnetillegget gitt til den som får det høgaste tillegget. Den andre forelderen har rett til eit høgare barnetillegg enn det du vil få. Vi har derfor stansa barnetillegget i uføretrygda di." },
                        )
                    }
                }

                //IF( Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "annen_forld_rett_bt") > 1  )THEN      INCLUDE ENDIF
                showIf(pesysData.annen_forld_rett_bt) {
                    //[TBU3922_EN, TBU3922, TBU3922_NN]

                    paragraph {
                        text(
                            bokmal { +"Når " + barnet_barna_opphor + " blir forsørget av foreldre som ikke bor sammen, blir barnetillegget gitt til den som har samme folkeregistrerte adresse som " + barnet_barna_opphor + ". Du bor ikke på samme folkeregistrerte adresse som " + barnet_barna_opphor + ". Vi har derfor opphørt barnetillegget i uføretrygden din." },
                            nynorsk { +"Når " + barnet_barna_opphor + " blir " + barnet_barna_opphor_forsørga_forsørgde + " av foreldre som ikkje bur saman, blir barnetillegget gitt til den som har same folkeregistrerte adresse som " + barnet_barna_opphor + ". Du bur ikkje på same folkeregistrerte adresse som " + barnet_barna_opphor + ". Vi har derfor stansa barnetillegget i uføretrygda di." },
                        )
                    }
                }

                //IF( Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "mindre_ett_ar_bt_flt") > 1 )  THEN INCLUDE ENDIF
                showIf(pesysData.mindre_ett_ar_bt_flt) {
                    //[TBU3925_EN, TBU3925, TBU3925_NN]

                    paragraph {
                        text(
                            bokmal { +"Det er mulig å flytte barnetillegget fra den ene til den andre forelderen. Det må imidlertid ha gått et år siden barnetillegget ble overført. I ditt tilfelle har det gått ett år og barnetillegget er overført til den andre forelderen. Vi har derfor opphørt barnetillegget i uføretrygden din." },
                            nynorsk { +"Det er mogleg å flytte barnetillegget frå den eine til den andre forelderen. Det må i det minste ha gått eit år sidan barnetillegget blei overført. I ditt tilfelle har det gått eit år og barnetillegget er overført til den andre forelderen. Vi har derfor stansa barnetillegget i uføretrygda di." },
                        )
                    }
                }

                //IF( Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bt_innt_over_1g") > 1  )THEN      INCLUDE ENDIF
                showIf(pesysData.bt_innt_over_1g) {
                    //[TBU3923_EN, TBU3923, TBU3923_NN]

                    paragraph {
                        text(
                            bokmal { +"Når " + barnet_barna_opphor + " har inntekt over folketrygdens grunnbeløp på " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner, har du ikke rett til barnetillegg. " },
                            nynorsk { +"Når " + barnet_barna_opphor + " har inntekt over grunnbeløpet i folketrygda på " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner, har du ikkje rett til barnetillegg." },
                        )
                        text(
                            bokmal { +"Det er opplyst at " + barnet_barna_opphor + " " + barnet_barna_opphor_ditt_dine + " har inntekt over " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner. Vi har derfor opphørt barnetillegget i uføretrygden din." },
                            nynorsk { +"Det er opplyst at " + barnet_barna_opphor + " " + barnet_barna_opphor_ditt_dine + " har inntekt over " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner. Vi har derfor stansa barnetillegget i uføretrygda di." },
                        )
                    }
                }
            }

            //IF(PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor)) AND (Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bruker_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "barn_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "barn_opph_ikke_avt_land") > 0 ) THEN      INCLUDE ENDIF
            showIf(pesysData.opphortBarnetillegg and (pesysData.opphoersbegrunnelse.bruker_flyttet_ikke_avt_land or pesysData.opphoersbegrunnelse.barn_flyttet_ikke_avt_land or pesysData.opphoersbegrunnelse.barn_opph_ikke_avt_land)) {

                //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "barn_flyttet_ikke_avt_land") > 0) THEN      INCLUDE ENDIF
                showIf(pesysData.opphoersbegrunnelse.barn_flyttet_ikke_avt_land) {
                    //[TBU5009_EN, TBU5009, TBU5009_NN]

                    paragraph {
                        text(
                            bokmal { +"Ifølge våre opplysninger er " +barnet_barna_opphor + " bosatt i " + fritekst("bostedsland") + ".  Derfor har du ikke lenger rett til barnetillegg" },
                            nynorsk { +"Ifølgje våre opplysningar er " + barnet_barna_opphor + " busett i " + fritekst("bostedsland") + ". Derfor har du ikkje lenger rett til barnetillegg." },
                        )
                    }
                }

                //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "barn_opph_ikke_avt_land") > 0) THEN      INCLUDE ENDIF
                showIf(pesysData.opphoersbegrunnelse.barn_opph_ikke_avt_land) {
                    //[TBU5010_EN, TBU5010, TBU5010_NN]

                    paragraph {
                        text(
                            bokmal { +"Ifølge våre opplysninger er " + dine_ditt_barn_opphor + " bosatt i " + fritekst("bostedsland") + ".  Derfor har du ikke lenger rett til barnetillegg." },
                            nynorsk { +"Ifølgje våre opplysningar er " + dine_ditt_barn_opphor + " busett i " + fritekst("bostedsland") + ". Derfor har du ikkje lenger rett til barnetillegg." },
                        )
                    }
                }

                //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bruker_flyttet_ikke_avt_land") > 0) THEN      INCLUDE ENDIF
                showIf(pesysData.opphoersbegrunnelse.bruker_flyttet_ikke_avt_land) {
                    //[TBU5011_EN, TBU5011, TBU5011_NN]

                    paragraph {
                        text(
                            bokmal { +"Ifølge våre opplysninger er du bosatt i " + fritekst("bostedsland") + ". Derfor har du ikke lenger rett til barnetillegg." },
                            nynorsk { +"Ifølgje våre opplysningar er du busett i " + fritekst("bostedsland") + ". Da har du ikkje lenger rett til barnetillegg." },
                        )
                    }
                }

                //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
                showIf(pesysData.opphortBarnetillegg) {
                    //[TBU5006_EN, TBU5006, TBU5006_NN]

                    paragraph {
                        text(
                            bokmal { +"For å ha rett til barnetillegg fra 1. juli 2020" },
                            nynorsk { +"For å ha rett til barnetillegg frå 1. juli 2020 " },
                        )
                        text(
                            bokmal { +"må du enten bo i Norge, innenfor EØS-området eller i et annet land Norge har trygdeavtale med" },
                            nynorsk { +"må du enten bu i Noreg, innanfor EØS-området eller i eit anna land Noreg har trygdeavtale med" },
                        )
                        text(
                            bokmal { +"må også barnet være bosatt og oppholde seg i Norge, innenfor EØS-området eller et annet land Norge har trygdeavtale med " },
                            nynorsk { +"må også barn vere busett og opphalde seg i Noreg, innanfor EØS-området eller eit anna land Noreg har trygdeavtale med " },
                        )
                        text(
                            bokmal { +"Dette går frem av folketrygdloven §12-15 som gjelder fra 1.juli 2020." },
                            nynorsk { +"Dette går fram av folketrygdlova § 12-15 som gjeld frå 1. juli 2020" },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = false AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) = "false" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "sivilstandsendring") THEN      INCLUDE ENDIF
            // TODO: Er YrkesskadeResultat her vesentlig for logikken? Eller er dette en mulig bug fra tidligere, "false" henger heller ikke sammen med "innvilget" brukt andre steder. Velger å fjerne sjekken på yrkesskade inntil videre.
            // showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_belopokt()) and not(pe.vedtaksdata_beregningsdata_beregningufore_belopredusert()) and FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1).equalTo("false") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("sivilstandsendring"))){
            showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_belopokt()) and not(pe.vedtaksdata_beregningsdata_beregningufore_belopredusert()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("sivilstandsendring"))) {
                paragraph {
                    text(
                        bokmal { +"Dette får ikke betydning for uføretrygden din, og du vil få utbetalt det samme som før." },
                        nynorsk { +"Dette får ikkje noko å seie for uføretrygda di, og du får utbetalt det same som før." },
                    )
                }
            }

            //IF(PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes_Er_Ulik(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor)) THEN     INCLUDE ENDIF
            showIf(not(pesysData.opphortBarnetillegg)) {
                //[TBU2335EN, TBU2335, TBU2335NN]

                paragraph {

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())) {
                        text(
                            bokmal { +"Barnetillegg kan gis så lenge du forsørger barn. Det gis som et tillegg til uføretrygden din og opphører når barnet fyller 18 år. " },
                            nynorsk { +"Barnetillegg kan bli gitt så lenge du forsørgjer barn. Det blir gitt som eit tillegg til uføretrygda di og blir stansa når barnet ditt fyller 18 år. " },
                        )
                    }

                    //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) AND PE_Vedtaksdata_Kravhode_KravArsakType = "soknad_bt" ) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_kravhode_kravarsaktype().equalTo("soknad_bt"))) {
                        text(
                            bokmal { +"Du er innvilget barnetillegg fordi du forsørger barn." },
                            nynorsk { +"Du er innvilga barnetillegg fordi du forsørgjer barn." },
                        )
                    }
                }
            }

            //IF(  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND (PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "barn_endret_inntekt" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "annen_for_end_in" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "begge_for_end_in")  ) THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and (pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("barn_endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("annen_for_end_in") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("begge_for_end_in")))) {
                //[TBU2336EN, TBU2336, TBU2336NN]

                paragraph {
                    text(
                        bokmal { +"Vi har mottatt opplysninger om at inntekten din er endret. Barnetillegget er derfor beregnet på nytt." },
                        nynorsk { +"Vi har fått opplysningar om at inntekta di er endra. Barnetillegg er derfor berekna på nytt." },
                    )
                }
            }

            //IF(  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND (PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "barn_endret_inntekt" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "annen_for_end_in" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "begge_for_end_in" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "eps_endret_inntekt")  ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("barn_endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("annen_for_end_in") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("begge_for_end_in") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("eps_endret_inntekt")))) {
                //[TBU2337EN, TBU2337, TBU2337NN]

                paragraph {
                    text(
                        bokmal { +"Vi har mottatt opplysninger om at inntekten deres er endret. Barnetillegget er derfor beregnet på nytt." },
                        nynorsk { +"Vi har fått opplysningar om at inntekta dykkar er endra. Barnetillegg er derfor berekna på nytt." },
                    )
                }
            }

            //PE_Vedtaksdata_Kravhode_KravArsakType = "omgj_etter_klage"
            showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("omgj_etter_klage")) {
                paragraph {
                    text(
                        bokmal { +"Søknaden din er innvilget etter klage og vi anser klagen som ferdig behandlet. Dersom du ønsker å opprettholde klagen, må du gi tilbakemelding til Nav innen 3 uker." },
                        nynorsk { +"Søknaden din er innvilga etter klage, og vi ser det slik at klaga er ferdig behandla. Dersom du ønskjer å halde fast på klaga, må du melde dette tilbake til Nav innan 3 veker." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                paragraph {
                    text(
                        bokmal { +"Utbetaling av uføretrygd for deg som er innlagt på institusjon" },
                        nynorsk { +"Utbetaling av uføretrygd når du er innlagd på institusjon" },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  OR  PE_UT_Forsorgeransvar_ingen_er_false() OR  PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = true)) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() or pe.ut_forsorgeransvar_ingen_er_false() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(Kroner(0))))) {
                //[TBU3104]
                paragraph {
                    text(
                        bokmal { +"Vi reduserer ikke utbetalingen av uføretrygden din verken i innleggelsesmåneden eller de tre påfølgende månedene når du er innlagt på institusjon. Uføretrygden din blir deretter redusert og skal under oppholdet utgjøre 14 prosent av uføretrygden inntil institusjonsoppholdet avsluttes. Totalt sett skal utbetalingen likevel utgjøre minst 45 prosent av folketrygdens grunnbeløp, før skatt." },
                        nynorsk { +"Vi reduserer ikkje utbetalinga av uføretrygda di verken i innleggingsmånaden eller dei tre påfølgjande månadene når du er innlagd på institusjon. Uføretrygda di blir deretter redusert og skal under opphaldet utgjere 14 prosent av uføretrygda inntil institusjonsopphaldet blir avslutta. Totalt sett skal utbetalinga likevel utgjere minst 45 prosent av grunnbeløpet i folketrygda før skatt. " },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() or pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())) {
                        text(
                            bokmal { +" Dersom du mottar " },
                            nynorsk { +"Dersom du får" },
                        )
                    }

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
                    showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) {
                        text(
                            bokmal { +"ektefelletillegg" },
                            nynorsk { +"ektefelletillegg" },
                        )
                    }

                    //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                    showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) {
                        text(
                            bokmal { +"gjenlevendetillegg" },
                            nynorsk { +"attlevandetillegg" },
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() or pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())) {
                        text(
                            bokmal { +" vil dette tillegget også bli redusert." },
                            nynorsk { +"vil dette tillegget også bli redusert." },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  OR  PE_UT_Forsorgeransvar_ingen_er_false() OR  PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = true)) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() or pe.ut_forsorgeransvar_ingen_er_false() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(Kroner(0))))) {
                paragraph {
                    text(
                        bokmal { +"Dersom du har faste og nødvendige utgifter til bolig, kan vi vurdere om uføretrygden din kan reduseres mindre. Du må sende inn dokumentasjon på dine utgifter til Nav. Forsørger du barn " },
                        nynorsk { +"Dersom du har faste og nødvendige utgifter til bustad, vil vi vurdere en lågare reduksjon av uføretrygda di. Du må sende inn dokumentasjon på utgiftene dine til Nav. Viss du forsørgjer barn" },
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
                    showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) {
                        text(
                            bokmal { +"og/eller ektefelle " },
                            nynorsk { +" og/eller ektefelle" },
                        )
                    }
                    text(
                        bokmal { +"under innleggelsen på institusjonen, vil vi ikke redusere uføretrygden din." },
                        nynorsk { +" mens du er lagd inn på institusjonen, reduserer vi ikkje uføretrygda di." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false AND PE_UT_Forsorgeransvar_ingen_er_true() AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.ut_forsorgeransvar_ingen_er_true() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))) {
                paragraph {
                    text(
                        bokmal { +"Uføretrygden din er lavere enn 45 prosent av folketrygdens grunnbeløp. Du vil derfor ikke få redusert utbetaling av uføretrygden din når du er innlagt på institusjon." },
                        nynorsk { +"Uføretrygda di er lågare enn 45 prosent av grunnbeløpet i folketrygda. Du får derfor ikkje redusert utbetaling av uføretrygda di når du er innlagd på institusjon." },
                    )
                }
            }

            //IF(PE_UT_Forsorgeransvar_ingen_er_false() AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0) THEN      INCLUDE ENDIF
            showIf((pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0))) {
                paragraph {
                    text(
                        bokmal { +"Du forsørger barn" },
                        nynorsk { +"Du forsørgjer barn " },
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
                    showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) {
                        text(
                            bokmal { +" og/eller" + " ektefelle" },
                            nynorsk { +"og/eller ektefelle " },
                        )
                    }
                    text(
                        bokmal { +" under oppholdet ditt i institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { +"under opphaldet ditt på institusjon. Vi har derfor kome fram til at utbetalinga di derfor ikkje skal reduserast." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND PE_UT_Forsorgeransvar_ingen_er_true()) THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.ut_forsorgeransvar_ingen_er_true())) {
                paragraph {
                    text(
                        bokmal { +"Du har dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { +"Du har dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har kome fram til at utbetalinga di derfor ikkje skal reduserast." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0 AND PE_UT_Forsorgeransvar_ingen_er_false() AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))) {
                paragraph {
                    text(
                        bokmal { +"Du har dokumentert at du har faste og nødvendige utgifter til bolig, og du forsørger barn " },
                        nynorsk { +"Du har dokumentert at du har faste og nødvendige utgifter til bustad, og du forsørgjer barn" },
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
                    showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) {
                        text(
                            bokmal { +"og/eller ektefelle " },
                            nynorsk { +" og/eller ektefelle" },
                        )
                    }
                    text(
                        bokmal { +"under oppholdet ditt i institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { +" under opphaldet ditt på institusjon. Vi har derfor kome fram til at utbetalinga di ikkje skal reduserast." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))) {
                paragraph {
                    text(
                        bokmal { +"Du har dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at utbetalingen din skal reduseres til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                        nynorsk { +"Du har dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har kome fram til at utbetalinga di skal reduserast til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0  AND PE_UT_Forsorgeransvar_ingen_er_true()) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0) and pe.ut_forsorgeransvar_ingen_er_true())) {
                paragraph {
                    text(
                        bokmal { +"Du forsørger ikke barn" },
                        nynorsk { +"Du forsørgjer ikkje barn" },
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
                    showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) {
                        text(
                            bokmal { +" og/eller ektefelle" },
                            nynorsk { +" og/eller ektefelle" },
                        )
                    }
                    text(
                        bokmal { +", og det er ikke dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at uføretrygden din skal reduseres til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                        nynorsk { +", og det er ikkje dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har derfor kome fram til at uføretrygda di skal reduserast til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                paragraph {
                    text(
                        bokmal { +"Utbetaling av uføretrygd for deg som er under straffegjennomføring" },
                        nynorsk { +"Utbetaling av uføretrygd når du er under straffegjennomføring" },
                    )
                }
            }

            //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"
            showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo")) {
                //[TBU3115]
                paragraph {
                    text(
                        bokmal { +"Uføretrygden din er redusert fordi du er under straffegjennomføring." },
                        nynorsk { +"Uføretrygda di er redusert fordi du er under straffegjennomføring." },
                    )

                    //IF(FF_GetArrayElement_Boolean(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphReduksjonsperiodeListe_InstOpphReduksjonsperiode_Forsorgeransvar) = true) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphreduksjonsperiodeliste_instopphreduksjonsperiode_forsorgeransvar()))) {
                        text(
                            bokmal { +" " },
                            nynorsk { +" " },
                        )
                    }

                    //IF(PE_UT_Forsorgeransvar_siste_er_true()) THEN      INCLUDE ENDIF
                    showIf((pe.ut_forsorgeransvar_siste_er_true())) {
                        text(
                            bokmal { +"Da du forsørger barn" },
                            nynorsk { +" Da du forsørgjer barn" },
                        )
                    }

                    //IF(FF_GetArrayElement_Boolean(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphReduksjonsperiodeListe_InstOpphReduksjonsperiode_Forsorgeransvar) = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphreduksjonsperiodeliste_instopphreduksjonsperiode_forsorgeransvar()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget())) {
                        text(
                            bokmal { +" og/eller ektefelle" },
                            nynorsk { +" og/eller ektefelle" },
                        )
                    }

                    //IF(PE_UT_Forsorgeransvar_siste_er_true()) THEN      INCLUDE ENDIF
                    showIf((pe.ut_forsorgeransvar_siste_er_true())) {
                        text(
                            bokmal { +", vil utbetaling av uføretrygden din reduseres med 50 prosent. " },
                            nynorsk { +", vil utbetalinga av uføretrygda di reduserast med 50 prosent. " },
                        )
                    }
                    text(
                        bokmal { +"Utbetalingen din er redusert fra andre måned etter at straffegjennomføring tok til. Når straffegjennomføring er avsluttet, vil vi ikke lenger redusere uføretrygden din. " },
                        nynorsk { +" Utbetalinga di er redusert frå den andre månaden etter at straffegjennomføringa tok til. Når straffegjennomføringa er avslutta, vil vi ikkje lenger redusere uføretrygda di. " },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() or pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())) {
                        text(
                            bokmal { +"Dersom du mottar " },
                            nynorsk { +"Dersom du mottar " },
                        )
                    }

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
                    showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) {
                        text(
                            bokmal { +"ektefelletillegg" },
                            nynorsk { +"ektefelletillegg" },
                        )
                    }

                    //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                    showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) {
                        text(
                            bokmal { +"gjenlevendetillegg" },
                            nynorsk { +"attlevandetillegg" },
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() or pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())) {
                        text(
                            bokmal { +" vil dette tillegget også bli redusert." },
                            nynorsk { +" vil dette tillegget også bli redusert." },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "instopphold" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("instopphold") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo(""))) {
                //[TBU3364EN, TBU3364, TBU3364NN]

                paragraph {
                    text(
                        bokmal { +"Du er ikke lenger " + fritekst("innlagt på institusjon/under straffegjennomføring") + ", og du får derfor tilbake utbetalingen av uføretrygden." },
                        nynorsk { +"Du er ikkje lenger " + fritekst("innlagt på institusjon/under straffegjennomføring") + ", og du får derfor tilbake utbetalinga av uføretrygda." },
                    )
                }
            }

            //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_et, PE_UT_KONST_VilkarsVedtakResultat_opphor)) THEN      INCLUDE ENDIF
            showIf(pesysData.opphortEktefelletillegg) {
                //[TBU2500EN, TBU2500, TBU2500NN]

                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter forskrift om overgangsregler ved innføringen av uføretrygd § 8." },
                        nynorsk { +"Vedtaket er gjort etter forskrift om overgangsreglar ved innføringa av uføretrygd § 8." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo") THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14 og " + paragraf22_12_or_22_13 + "." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14 og " + paragraf22_12_or_22_13 + "." },
                    )
                }
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo" AND PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes_Er_Ulik(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor)  ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo") and not(pesysData.opphortBarnetillegg))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-16 og " + paragraf22_12_or_22_13 },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-16 og " + paragraf22_12_or_22_13 },
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")) {
                        text(
                            bokmal { +" og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                            nynorsk { +" og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                        )
                    }
                    text(
                        bokmal { +"." },
                        nynorsk { +"." },
                    )
                }
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo"  ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-17 og " + paragraf22_12_or_22_13 },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-17 og " + paragraf22_12_or_22_13 },
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")) {
                        text(
                            bokmal { +" og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                            nynorsk { +" og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                        )
                    }
                    text(
                        bokmal { +"." },
                        nynorsk { +"." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo") THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-17 og " + paragraf22_12_or_22_13 + "." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-17 og " + paragraf22_12_or_22_13 + "." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo") THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-18 og " + paragraf22_12_or_22_13 + "." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-18 og " + paragraf22_12_or_22_13 + "." },
                    )
                }
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo"  ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-16, 12-18 og " + paragraf22_12_or_22_13 },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-16, 12-18 og " + paragraf22_12_or_22_13 },
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")) {
                        text(
                            bokmal { +" og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                            nynorsk { +" og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                        )
                    }
                    text(
                        bokmal { +"." },
                        nynorsk { +"." },
                    )
                }
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo"  ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-18 og " + paragraf22_12_or_22_13 },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-18 og " + paragraf22_12_or_22_13 },
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")) {
                        text(
                            bokmal { +" og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                            nynorsk { +" og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                        )
                    }
                    text(
                        bokmal { +"." },
                        nynorsk { +"." },
                    )
                }
            }

            //PE_Vedtaksdata_Kravhode_KravArsakType = "soknad_bt"
            showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("soknad_bt")) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-15 og 12-16 og " + paragraf22_12_or_22_13 + " og forskrift om overgangsregler for barnetillegg i uføretrygden." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-15 og 12-16 og " + paragraf22_12_or_22_13 + " og forskrift om overgangsreglar for barnetillegg i uføretrygda." },
                    )
                }
            }

            // TODO Hør med fageksperter hva logikken her egentlig skal være mtp PE_UT_KravLinjeKode_Og_PaaFolgende_bt_ikkeInnv og PE_UT_KravLinjeKode_Og_PaaFolgende_ut_gjt_ikkeInnv
//            //IF(  PE_UT_KravLinjeKode_Og_PaaFolgende_bt_ikkeInnv() AND ( FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5" ) AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo" AND PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor)  ) THEN      INCLUDE ENDIF
//            showIf((pe.ut_kravlinjekode_og_paafolgende_bt_ikkeinnv() and (not(virkningbegrunnelseStdbegr_22_12_1_5)) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo") and pesysData.opphortBarnetillegg)) {
//                //[TBU2350EN, TBU2350, TBU2350NN]
//
//                // 12-15 = barnetillegg
//                paragraph {
//                    text(
//                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-15 og 22-12." },
//                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-15 og 22-12." },
//                    )
//                }
//            }
//
//            //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_ut_gjt_ikkeInnv() AND ( FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5" ) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo"  ) THEN      INCLUDE ENDIF
//            showIf((pe.ut_kravlinjekode_og_paafolgende_ut_gjt_ikkeinnv() and (not(virkningbegrunnelseStdbegr_22_12_1_5)) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))) {
//                //[TBU2351EN, TBU2351, TBU2351NN]
//
//                // 12-18 = gjenlevendetillegg
//                paragraph {
//                    text(
//                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-18 og 22-12." },
//                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-18 og 22-12." },
//                    )
//                }
//            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "endring_ifu" AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse(1) = "stdbegr_12_8_2_9" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endring_ifu") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse().equalTo("stdbegr_12_8_2_9") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14 og forskrift om uføretrygd fra folketrygden § 2-3." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14 og forskrift om uføretrygd frå folketrygda § 2-3." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-19 og " + paragraf22_12_or_22_13 + "." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-19 og " + paragraf22_12_or_22_13 + "." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-17, 12-19 og " + paragraf22_12_or_22_13 + "." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-17, 12-19 og " + paragraf22_12_or_22_13 + "." },
                    )
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))) {
                paragraph {
                    showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")) {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-16, 12-19 og " + paragraf22_12_or_22_13 + " og forskrift om overgangsregler for barnetillegg i uføretrygden." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-16, 12-19 og " + paragraf22_12_or_22_13 + " og forskrift om overgangsreglar for barnetillegg i uføretrygda." },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-16, 12-19 og " + paragraf22_12_or_22_13 + "."},
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-16, 12-19 og " + paragraf22_12_or_22_13 + "."},
                        )
                    }
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))) {
                paragraph {
                    showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")) {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-17, 12-19 og " + paragraf22_12_or_22_13 + " og forskrift om overgangsregler for barnetillegg i uføretrygden." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-17, 12-19 og " + paragraf22_12_or_22_13 + " og forskrift om overgangsreglar for barnetillegg i uføretrygda." },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-17, 12-19 og " + paragraf22_12_or_22_13 + "."},
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-17, 12-19 og " + paragraf22_12_or_22_13 + "."},
                        )
                    }
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo") THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-20 og " + paragraf22_12_or_22_13 + "." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-20 og " + paragraf22_12_or_22_13 + "." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo") THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-17, 12-20 og " + paragraf22_12_or_22_13 + "." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-17, 12-20 og " + paragraf22_12_or_22_13 + "." },
                    )
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-16, 12-20 og " + paragraf22_12_or_22_13 },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-16, 12-20 og " + paragraf22_12_or_22_13 },
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")) {
                        text(
                            bokmal { +" og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                            nynorsk { +" og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                        )
                    }
                    text(
                        bokmal { +"." },
                        nynorsk { +"." },
                    )
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-17, 12-20 og " + paragraf22_12_or_22_13 },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-17, 12-20 og " + paragraf22_12_or_22_13 },
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")) {
                        text(
                            bokmal { +" og forskrift om overgangsregler for barnetillegg i uføretrygden." },
                            nynorsk { +" og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                        )
                    }
                    text(
                        bokmal { +" " },
                        nynorsk { +" " },
                    )
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  ) THEN      INCLUDE ENDIF
            showIf(((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-18, 12-19 og " + paragraf22_12_or_22_13 + "." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-18, 12-19 og " + paragraf22_12_or_22_13 + "." },
                    )
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  ) THEN      INCLUDE ENDIF
            showIf(((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-17 til 12-19 og " + paragraf22_12_or_22_13 + "." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-17 til 12-19 og " + paragraf22_12_or_22_13 + "." },
                    )
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-16, 12-18, 12-19 og" + paragraf22_12_or_22_13 },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-16, 12-18, 12-19 og " + paragraf22_12_or_22_13 },
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")) {
                        text(
                            bokmal { +" og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                            nynorsk { +" og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                        )
                    }
                    text(
                        bokmal { +". " },
                        nynorsk { +". " },
                    )
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-19 og " + paragraf22_12_or_22_13 },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-19 og " + paragraf22_12_or_22_13 },
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")) {
                        text(
                            bokmal { +" og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                            nynorsk { +" og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                        )
                    }
                    text(
                        bokmal { +". " },
                        nynorsk { +". " },
                    )
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  ) THEN      INCLUDE ENDIF
            showIf(((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-18, 12-20 og " + paragraf22_12_or_22_13 + "." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-18, 12-20 og " + paragraf22_12_or_22_13 + "." },
                    )
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  ) THEN      INCLUDE ENDIF
            showIf(((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-17, 12-18 og 12-20 og " + paragraf22_12_or_22_13 + "." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-17, 12-18 og 12-20 og " + paragraf22_12_or_22_13 + "." },
                    )
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-16, 12-18, 12-20 og " + paragraf22_12_or_22_13 },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-16, 12-18, 12-20 og " + paragraf22_12_or_22_13 },
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")) {
                        text(
                            bokmal { +" og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                            nynorsk { +" og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                        )
                    }

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = ""overgangsregler_2016""
                    showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("")) {
                        text(
                            bokmal { +"." },
                            nynorsk { +"." },
                        )
                    }
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-18, 12-20 og " + paragraf22_12_or_22_13 },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-18, 12-20 og " + paragraf22_12_or_22_13 },
                    )


                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")) {
                        text(
                            bokmal { +" og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                            nynorsk { +" og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                        )
                    }
                    text(
                        bokmal { +"." },
                        nynorsk { +"." },
                    )
                }
            }
            title1 {
                text(
                    bokmal { +"Dette er virkningstidspunktet ditt" },
                    nynorsk { +"Dette er verknadstidspunktet ditt" },
                )
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo(""))) {
                //[TBU3361EN, TBU3361, TBU3361NN]

                paragraph {
                    text(
                        bokmal { +"Uføretrygden din er endret fra " + onsketvirkningsdato.format() + ". Dette kaller vi virkningstidspunktet. " + fritekst("Konkret begrunnelse for fastsatt virkningstidspunkt") + "." },
                        nynorsk { +"Uføretrygda di er endra frå " + onsketvirkningsdato.format() + ". Dette kallar vi verknadstidspunktet. " + fritekst("Konkret begrunnelse for fastsatt virkningstidspunkt") + "." },
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneResultat) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_1") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevneresultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_1"))) {
                paragraph {
                    text(
                        bokmal { +"Du har fått innvilget uføretrygd fra " + onsketvirkningsdato.format() + ". Dette kaller vi virkningstidspunktet. Fram til dette vil du få arbeidsavklaringspenger." },
                        nynorsk { +"Du har fått innvilga uføretrygd frå " + onsketvirkningsdato.format() + ". Dette kallar vi verknadstidspunktet. Fram til dette kjem du til å få arbeidsavklaringspengar." },
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneResultat) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_2") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevneresultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_2"))) {
                paragraph {
                    text(
                        bokmal { +"Du har fått innvilget uføretrygd fra " + onsketvirkningsdato.format() + ". Dette kaller vi virkningstidspunktet. Arbeidsavklaringspengene utbetales fram til " + fritekst("dato for opphør") + " og uføretrygd utbetales for de gjenstående dagene i " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_mndvirkningstidpunkt().format() + "." },
                        nynorsk { +"Du har fått innvilga uføretrygd frå " + onsketvirkningsdato.format() + ". Dette kallar vi verknadstidspunktet. Arbeidsavklaringspengane blir betalte ut fram til " + fritekst("Dato for opphør") + ", og uføretrygd blir betalt ut for dei dagane som er att i " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_mndvirkningstidpunkt().format() + "." },
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_3") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_3"))) {
                paragraph {
                    text(
                        bokmal { +"Du har fått innvilget uføretrygd fra " + onsketvirkningsdato.format() + ". Dette kaller vi virkningstidspunktet. Du vil få sykepenger fram til " + fritekst("dato for opphør") + ". I denne måneden får du utbetalt den delen av sykepengene som overstiger uføretrygden." },
                        nynorsk { +"Du har fått innvilga uføretrygd frå " + onsketvirkningsdato.format() + ". Dette kallar vi verknadstidspunktet. Du får sjukepengar fram til " + fritekst("Dato for opphør") + ". I denne månaden får du utbetalt den delen av sjukepengane som overstig uføretrygda." },
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_4") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_4"))) {
                paragraph {
                    text(
                        bokmal { +"Du har fått innvilget uføretrygd fra " + onsketvirkningsdato.format() + ". Dette kaller vi virkningstidspunktet. For å ha rett til uføretrygd må du ha fylt 18 år. Du får derfor uføretrygd fra måneden etter at du fyller 18 år." },
                        nynorsk { +"Du har fått innvilga uføretrygd frå " + onsketvirkningsdato.format() + ". Dette kallar vi verknadstidspunktet. For å ha rett til uføretrygd må du ha fylt 18 år. Du får derfor uføretrygd frå månaden etter at du fyljer 18 år." },
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
            showIf(virkningbegrunnelseStdbegr_22_12_1_5) {
                paragraph {
                    text(
                        bokmal { +"Du har fått innvilget uføretrygd fra " + onsketvirkningsdato.format() + ". Dette kaller vi virkningstidspunktet. Vi mottok søknaden din " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Dersom vilkårene for rett til uføretrygd var oppfylt før dette, kan uføretrygden innvilges opptil tre måneder før denne datoen. <FRITEKST>" },
                        nynorsk { +"Du har fått innvilga uføretrygd frå " + onsketvirkningsdato.format() + ". Dette kallar vi verknadstidspunktet. Vi fekk søknaden din " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Dersom vilkåra for rett til uføretrygd var oppfylte før dette, kan vi innvilge uføretrygd opptil tre månader før denne datoen. <FRITEKST>" },
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_12") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_12"))) {
                //[TBU2528EN, TBU2528, TBU2528NN]

                paragraph {
                    text(
                        bokmal { +"Uføretrygden din er endret fra " + onsketvirkningsdato.format() + ". Dette kaller vi virkningstidspunktet. Du vil derfor få ny utbetaling fra og med måneden etter den måneden vilkårene er oppfylt." },
                        nynorsk { +"Uføretrygda di er endra frå " + onsketvirkningsdato.format() + ". Dette kallar vi verknadstidspunktet. Du vil derfor få ny utbetaling frå og med månaden etter den månaden vilkåra er oppfylde." },
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_11") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_11"))) {
                //[TBU2529EN, TBU2529, TBU2529NN]

                paragraph {
                    text(
                        bokmal { +"Uføretrygden din er endret fra " + onsketvirkningsdato.format() + ". Dette kaller vi virkningstidspunktet. Du vil derfor få ny utbetaling fra og med måneden vilkårene er oppfylt. " },
                        nynorsk { +"Uføretrygda di er endra frå " + onsketvirkningsdato.format() + ". Dette kallar vi verknadstidspunktet. Du vil derfor få ny utbetaling frå og med månaden vilkåra er oppfylde." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse(1) = "stdbegr_22_12_1_14") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_14"))) {
                //[TBU3024EN, TBU3024, TBU3024NN]

                paragraph {
                    text(
                        bokmal { +"Virkningstidspunkt for opphør er satt til måneden vilkårene ikke lenger er oppfylt." },
                        nynorsk { +"Verknadstidspunktet for stans er satt til månaden vilkåra ikkje lenger er oppfylde." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_ys") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravgjelder().notEqualTo("sok_ys"))) {

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "") THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("") or (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo(""))) {
                    paragraph {
                        text(
                            bokmal { +"Slik har vi fastsatt uføregraden din" },
                            nynorsk { +"Slik har vi fastsett uføregraden din" },
                        )
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "") THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("") or (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo(""))) {
                    paragraph {
                        text(
                            bokmal { +"Vi har sammenliknet inntektsmulighetene dine før og etter at du ble ufør, og vurdert hvor mye inntektsevnen din er varig nedsatt." },
                            nynorsk { +"Vi har samanlikna inntektsmoglegheitene dine før og etter at du blei ufør, og vurdert kor mykje inntektsevna di er varig nedsett." },
                        )
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_1" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "stdbegr_12_8_1_3") THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_1") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("stdbegr_12_8_1_3"))) {
                    paragraph {
                        text(
                            bokmal { +"Inntekten din før du ble ufør er fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. " + fritekst("begrunnelse for fastsatt IFU") + "." },
                            nynorsk { +"Inntekta di før du blei ufør er fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. " + fritekst("Begrunnelse for fastsatt IFU") + "." },
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu > FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt)) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oifu().greaterThan((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt())))) {
                            text(
                                bokmal { +" Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + pe.ut_oifuperiode_else_oifu().format() + " kroner." },
                                nynorsk { +" Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + pe.ut_oifuperiode_else_oifu().format() + " kroner." },
                            )
                        }
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "stdbegr_12_8_1_3") THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_3") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("stdbegr_12_8_1_3"))) {
                    paragraph {
                        text(
                            bokmal { +"Du hadde begrenset yrkesaktivitet og inntekt før du ble ufør. Inntekten din før du ble ufør er fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. Dette er for å garantere deg et minstenivå på inntekt før uførhet. Dette minstenivået skal tilsvare 3,3 ganger folketrygdens grunnbeløp." },
                            nynorsk { +"Du hadde avgrensa yrkesaktivitet og inntekt før du blei ufør. Inntekta di før du blei ufør, er fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. Dette er for å garantere deg eit minstenivå på inntekt før uførleik. Dette minstenivået skal svare til 3,3 gonger grunnbeløpet." },
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu > FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt)) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oifu().greaterThan((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt())))) {
                            text(
                                bokmal { +" Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + pe.ut_oifuperiode_else_oifu().format() + " kroner." },
                                nynorsk { +" Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + pe.ut_oifuperiode_else_oifu().format() + " kroner." },
                            )
                        }
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse,1) = "stdbegr_12_8_2_5" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse,1) <> "stdbegr_12_8_1_3") THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_5") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo("stdbegr_12_8_1_3"))) {
                    paragraph {
                        text(
                            bokmal { +"Du hadde begrenset yrkesaktivitet og inntekt før du ble ufør. Inntekten din før du ble ufør er fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. Dette er for å garantere deg et minstenivå på inntekt før uførhet. Dette minstenivået skal tilsvare 3,5 ganger folketrygdens grunnbeløp." },
                            nynorsk { +"Du hadde avgrensa yrkesaktivitet og inntekt før du blei ufør. Inntekta di før du blei ufør, er fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. Dette er for å garantere deg eit minstenivå på inntekt før uførleik. Dette minstenivået skal svare til 3,5 gonger grunnbeløpet." },
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu > FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt)) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oifu().greaterThan((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt())))) {
                            text(
                                bokmal { +" Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + pe.ut_oifuperiode_else_oifu().format() + " kroner." },
                                nynorsk { +" Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + pe.ut_oifuperiode_else_oifu().format() + " kroner." },
                            )
                        }
                    }
                }

                //IF((FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat,1) = "oppfylt") AND (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse,1) = "stdbegr_12_8_2_4")  AND (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse,1) <> "stdbegr_12_8_1_3"))  THEN      INCLUDE ENDIF
                showIf((((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat()).equalTo("oppfylt")) and ((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_4")) and ((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("stdbegr_12_8_1_3")))) {
                    paragraph {
                        text(
                            bokmal { +"Inntekten din før du ble ufør er fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. Vi har innvilget deg rettighet som ung ufør, og inntekten din før du ble ufør skal derfor tilsvare minst 4,5 ganger grunnbeløpet." },
                            nynorsk { +"Inntekta di før du blei ufør, er fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. Vi har innvilga deg rettar som ung ufør, og inntekta di før du blei ufør skal derfor svare til minst 4,5 gonger grunnbeløpet." },
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu > FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt)) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oifu().greaterThan((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt())))) {
                            text(
                                bokmal { +" Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + pe.ut_oifuperiode_else_oifu().format() + " kroner." },
                                nynorsk { +" Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + pe.ut_oifuperiode_else_oifu().format() + " kroner." },
                            )
                        }
                    }
                }

                //IF((FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "") AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100  AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt) = 0  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "stdbegr_12_8_1_3"  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "stdbegr_12_8_2_2"  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "stdbegr_12_8_2_9"  ) THEN      INCLUDE ENDIF
                showIf((((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo("") or (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("")) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().equalTo(100) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()).equalTo(0) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("stdbegr_12_8_1_3") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo("stdbegr_12_8_2_2") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo("stdbegr_12_8_2_9"))) {
                    paragraph {
                        text(
                            bokmal { +"Du har ikke inntekt i dag, og vi har derfor fastsatt uføregraden din til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent." },
                            nynorsk { +"Du har ikkje inntekt i dag, og vi har derfor fastsett uføregraden din til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent." },
                        )
                    }
                }

                //IF( ( FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "" ) AND ((PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100) OR ( PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100 AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt) > 0 )) AND  FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "stdbegr_12_8_1_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "stdbegr_12_8_2_2"  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "stdbegr_12_8_2_9" ) THEN      INCLUDE ENDIF
                showIf((((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo("") or (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("")) and ((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().lessThan(100)) or (pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().equalTo(100) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()).greaterThan(0))) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("stdbegr_12_8_1_3") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo("stdbegr_12_8_2_2") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo("stdbegr_12_8_2_9"))) {
                    paragraph {
                        text(
                            bokmal { +"Du har en inntekt på " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format() + " kroner, og vi har derfor fastsatt uføregraden din til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent." },
                            nynorsk { +"Du har ei inntekt på " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format() + " kroner, og vi har derfor fastsett uføregraden din til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent." },
                        )
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_9" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_2") THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_9") or (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_2"))) {
                    paragraph {
                        text(
                            bokmal { +"Vi har fastsatt inntekten din før du ble ufør til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. Inntekten din før du ble ufør er fastsatt ut fra stillingsandelen din, og forventet inntekt på " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format() + " kroner. Inntekten din etter at du ble ufør er derfor fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format() + " kroner og uføregraden din blir " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent." },
                            nynorsk { +"Vi har fastsett inntekta di før du blei ufør til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. Inntekta di før du blei ufør, er fastsett ut frå stillingsdelen din og forventa inntekt på " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format() + " kroner. Inntekta di etter at du blei ufør, er derfor fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format() + " kroner, og uføregraden din blir " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent." },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = 100 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().equalTo(100) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(100) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                includePhrase(TBU1201_Generated)
            }

            //IF(  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0) OR (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad)) AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold"  ) THEN      INCLUDE ENDIF
            showIf((((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().lessThan(100) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().greaterThan(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()))) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                includePhrase(TBU1203_Generated)
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                includePhrase(TBU1204_Generated)
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                includePhrase(TBU2251_Generated)
            }

            // TODO: Legg til sjekk og tekst når vi vet mer om dette
            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> 60000 AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt(1) = 0 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            //showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().equalTo(100) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(60000) and FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt(1).equalTo(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
            //    includePhrase(TBU1205_Generated(pe))
            //}

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense = 60000 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().equalTo(100) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().equalTo(60000) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                includePhrase(TBU1296_Generated)
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                includePhrase(TBU1206_Generated(pe))
            }

            //IF(  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> 60000 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop  AND  (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0)  OR  (FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt) > 0 AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100)  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold" ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(60000) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()) and (pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().lessThan(100) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().greaterThan(0)) or ((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()).greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().equalTo(100)) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                includePhrase(TBU1207_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense = 60000 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().equalTo(60000) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().lessThan(100) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                includePhrase(TBU2357_Generated(pe))
            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                //[TBU1208_1]
                paragraph {
                    text(
                        bokmal { +"Vi bruker en fastsatt prosentandel når vi justerer uføretrygden din ut fra inntekt. Denne prosentandelen kaller vi kompensasjonsgrad." },
                        nynorsk { +"Vi bruker ein fastsett prosentdel når vi justerer uføretrygda di ut frå inntekt. Denne prosentdelen kallar vi kompensasjonsgrad. " },
                    )
                    text(
                        bokmal { +"For deg utgjør kompensasjonsgraden " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " prosent. Det er bare den delen av inntekten din som overstiger " + pe.ut_inntektsgrense_faktisk().format() + " kroner, som vi justerer uføretrygden din ut fra. Det betyr at et beløp som tilsvarer " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " prosent av den inntekten du har over " + pe.ut_inntektsgrense_faktisk().format() + " kroner trekkes fra uføretrygden din." },
                        nynorsk { +"For deg utgjer kompensasjonsgraden " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " prosent. Det er berre den delen av inntekta di som overstig " + pe.ut_inntektsgrense_faktisk().format() + " kroner, som vi justerer uføretrygda di ut frå. Det betyr at eit beløp som svarer til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " prosent av inntekta du har over " + pe.ut_inntektsgrense_faktisk().format() + " kroner blir trekt frå uføretrygda di." },
                    )

                    //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                    showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) {
                        text(
                            bokmal { +" " },
                            nynorsk { +" " },
                        )
                    }

                    //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                    showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) {
                        text(
                            bokmal { +"Tjener du mer enn inntektsgrensen din, reduserer vi gjenlevendetillegget ditt med samme prosent som vi reduserer uføretrygden din med." },
                            nynorsk { +"Tener du meir enn inntektsgrensa di, reduserer vi attlevandetillegget ditt med same prosent som vi reduserer uføretrygda di med." },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt().greaterThan(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                includePhrase(TBU2361_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_beregningsdata_beregningufore_belopredusert() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                includePhrase(TBU2362_Generated)
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_beregningsdata_beregningufore_belopokt() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                includePhrase(TBU2363_Generated)
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                //[TBU2261]
                paragraph {
                    text(
                        bokmal { +"Ut fra den årlige inntekten din vil uføretrygden utgjøre " + pe.ut_nettoakk_pluss_nettorestar().format() + " kroner." },
                        nynorsk { +"På bakgrunn av den innmelde inntekta di utgjer uføretrygda di " + pe.ut_nettoakk_pluss_nettorestar().format() + " kroner." },
                    )

                    //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = false) THEN      INCLUDE ENDIF
                    showIf((not(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(pe.vedtaksdata_virkningfom())))) {
                        text(
                            bokmal { +" Hittil i år har du fått utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettoakk().format() + " kroner." },
                            nynorsk { +" Hittil i år har du fått utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettoakk().format() + " kroner." },
                        )
                    }
                    text(
                        bokmal { +" Du har derfor rett til en utbetaling av uføretrygd på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_netto().format() + " kroner per måned for resten av året." },
                        nynorsk { +" Du har derfor rett til ei utbetaling av uføretrygd på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_netto().format() + " kroner per månad for resten av kalenderåret." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                includePhrase(TBU1210_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                includePhrase(TBU2364_Generated)
                includePhrase(TBU2365_Generated)
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                includePhrase(TBU2366_Generated)
                includePhrase(TBU2367_Generated)
                includePhrase(TBU2279_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                includePhrase(TBU2280_Generated(pe))
            }

            //IF( ( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true ) AND PE_Vedtaksdata_Kravhode_SokerBT = true ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_kravhode_sokerbt())) {
                includePhrase(TBU3800_Generated)
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) AND PE_Vedtaksdata_Kravhode_SokerBT = true AND ((PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB) OR (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT <> 0 ) THEN      INCLUDE ENDIF
            showIf(
                ((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_kravhode_sokerbt() and ((pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggfellesyk_belopgammelbtfb().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggfellesyk_belopnybtfb())) or (pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggserkullyk_belopgammelbtsb().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggserkullyk_belopnybtsb()))) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt()
                    .greaterThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().notEqualTo(0))
            ) {
                paragraph {
                    text(
                        bokmal { +"Barnetillegget i uføretrygden din er endret fordi " + fritekst("årsak til endring") + "." },
                        nynorsk { +"Barnetillegget i uføretrygda di er endra fordi " + fritekst("årsak til endring") + "." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0) THEN      EXCLUDE ENDIF
            showIf(not((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0)))) {

                //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)   ) THEN      INCLUDE ENDIF
                showIf(((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))) {
                    includePhrase(TBU2338_Generated(pe))
                }

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                    includePhrase(TBU2339_Generated(pe))
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) {
                    //[TBU3801]
                    paragraph {
                        text(
                            bokmal { +"Endringer i " },
                            nynorsk { +"Endringar i " },
                        )

                        //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                        showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                            text(
                                bokmal { +"inntektene til deg og ektefellen, partneren eller samboeren din " },
                                nynorsk { +"inntektene til deg og ektefella, partnaren eller sambuaren din " },
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())) {
                            text(
                                bokmal { +"inntekten din " },
                                nynorsk { +"inntekta di " },
                            )
                        }
                        text(
                            bokmal { +"kan ha betydning for barnetillegget ditt. Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» på $NAV_URL." },
                            nynorsk { +"kan ha betydning for barnetillegget ditt. Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» på $NAV_URL." },
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  ) THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()))) {
                    //[TBU1284]
                    paragraph {
                        text(
                            bokmal { +"Inntekten din er " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning().format() + " kroner og inntekten til ektefellen, partneren eller samboeren din er " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format() + " kroner. " },
                            nynorsk { +"Inntekta di er " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning().format() + " kroner, og inntekta til ektefella, partnaren eller sambuaren din er " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format() + " kroner. " },
                        )

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbelopFratrukketAnnenForeldersInntekt > 0) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt().greaterThan(0))) {
                            text(
                                bokmal { +"Folketrygdens grunnbeløp på inntil " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner er holdt utenfor inntekten til ektefellen, partneren eller samboeren din. " },
                                nynorsk { +"Grunnbeløpet i folketrygda på inntil " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner er halde utanfor inntekta til ektefella, partnaren eller sambuaren din. " }
                            )
                        }

                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 )) THEN      INCLUDE ENDIF
                        showIf(((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0)))) {
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning().greaterThan(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop())) {
                                text(
                                    bokmal { +"Til sammen er inntektene høyere enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                    nynorsk { +"Til saman er inntektene høgare enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                )
                            }.orShow {
                                text(
                                    bokmal { +"Til sammen er inntektene lavere enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                    nynorsk { +"Til saman er inntektene lågare enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                )
                            }
                        }

                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0 )) THEN      INCLUDE ENDIF
                        showIf(((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)))) {
                            text(
                                bokmal { +"ikke " },
                                nynorsk { +"ikkje " },
                            )
                        }

                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 )) THEN      INCLUDE ENDIF
                        showIf(((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0)))) {
                            text(
                                bokmal { +"redusert. " },
                                nynorsk { +"redusert. " },
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false ))THEN     INCLUDE ENDIF
                        showIf(((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())))) {
                            text(
                                bokmal { +"Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. " },
                                nynorsk { +"Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. " },
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto  = 0 ))THEN     INCLUDE ENDIF
                        showIf(((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0)))) {
                            text(
                                bokmal { +"Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                nynorsk { +"Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()))) {
                    //[TBU1285]
                    paragraph {

                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_UT_BTSBInnvilget_Ikke_BTFBInnvilget() )) THEN      INCLUDE ENDIF
                        showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.ut_btsbinnvilget_ikke_btfbinnvilget()))) {
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().greaterThan(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop())) {
                                text(
                                    bokmal { +"Inntekten din på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner er høyere enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                    nynorsk { +"Inntekta di på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner er høgare enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                )
                            }.orShow {
                                text(
                                    bokmal { +"Inntekten din på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner er lavere enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                    nynorsk { +"Inntekta di på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner er lågare enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                )
                            }
                        }

                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_UT_BTSBInnvilget_Ikke_BTFBInnvilget() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop )) THEN      INCLUDE ENDIF
                        showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.ut_btsbinnvilget_ikke_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().lessThanOrEqual(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop())))) {
                            text(
                                bokmal { +"ikke " },
                                nynorsk { +"ikkje " },
                            )
                        }

                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_UT_BTSBInnvilget_Ikke_BTFBInnvilget() )) THEN      INCLUDE ENDIF
                        showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.ut_btsbinnvilget_ikke_btfbinnvilget()))) {
                            text(
                                bokmal { +"redusert ut fra inntekt. " },
                                nynorsk { +"redusert ut frå inntekt. " },
                            )
                        }

                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_UT_BTSBInnvilget_Ikke_BTFBInnvilget() )) THEN      INCLUDE ENDIF
                        showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.ut_btsbinnvilget_ikke_btfbinnvilget()))) {
                            text(
                                bokmal { +"Inntekten din er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner. " },
                                nynorsk { +"Inntekta di er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner. " },
                            )
                        }

                        //IF(( PE_UT_BTSBInnvilget_og_justBelopPA() )) THEN      INCLUDE ENDIF
                        showIf(((pe.ut_btsbinnvilget_og_justbeloppa()))) {
                            text(
                                bokmal { +"Inntekten din er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner. Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. " },
                                nynorsk { +"Inntekta di er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner. Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. " },
                            )
                        }

                        //IF(( PE_UT_BTSBInnvilget_og_justBelopPA() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 )) THEN      INCLUDE ENDIF
                        showIf(((pe.ut_btsbinnvilget_og_justbeloppa() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)))) {
                            text(
                                bokmal { +"Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året." },
                                nynorsk { +"Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                            )
                        }
                    }
                }

                //IF(PE_UT_TBU1286_del1() = true OR PE_UT_TBU1286_del2() = true OR PE_UT_TBU1286_del3() = true) THEN      INCLUDE ENDIF
                showIf((pe.ut_tbu1286_del1() or pe.ut_tbu1286_del2() or pe.ut_tbu1286_del3())) {
                    //[TBU1286]
                    paragraph {

                        //IF(PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0))) {
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().greaterThan(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop())) {
                                text(
                                    bokmal { +"Inntekten din er høyere enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene. " },
                                    nynorsk { +"Inntekta di er høgare enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra. " },
                                )
                            }.orShow {
                                text(
                                    bokmal { +"Inntekten din er lavere enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene. " },
                                    nynorsk { +"Inntekta di er lågare enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra. " },
                                )
                            }
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0  AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)  OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0)) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0))) {
                            text(
                                bokmal { +"Dette barnetillegget er derfor " + pe.ut_ikke() + "redusert ut fra inntekt. " },
                                nynorsk { +"Dette barnetillegget er derfor " + pe.ut_ikke() + "redusert ut frå inntekt. " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))) {
                            text(
                                bokmal { +"Til sammen er " },
                                nynorsk { +"Til saman er " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <>  0 AND ( (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0) ) AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))) {
                            text(
                                bokmal { +"også " },
                                nynorsk { +"også " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))) {
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning().greaterThan(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop())) {
                                text(
                                    bokmal { +"inntektene til deg og ektefellen, partneren eller samboeren din høyere enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre. " },
                                    nynorsk { +"inntektene til deg og ektefella, partnaren eller sambuaren din høgare enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine. " },
                                )
                            }.orShow {
                                text(
                                    bokmal { +"inntektene til deg og ektefellen, partneren eller samboeren din lavere enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre. " },
                                    nynorsk { +"inntektene til deg og ektefella, partnaren eller sambuaren din lågare enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine. " },
                                )
                            }
                        }

                        //IF( PE_UT_TBU1286_del1() AND ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0))  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))) {
                            text(
                                bokmal { +"Dette barnetillegget er derfor " },
                                nynorsk { +"Dette barnetillegget er derfor " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBbrutto  AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0))  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbbrutto()) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))) {
                            text(
                                bokmal { +"ikke " },
                                nynorsk { +"ikkje " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0))  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))) {
                            text(
                                bokmal { +"redusert ut fra inntekt. " },
                                nynorsk { +"redusert ut frå inntekt. " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0  AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0)) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0  ) THEN      INCLUDE ENDIF
                        showIf(
                            (pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                                .equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))
                        ) {
                            text(
                                bokmal { +"Barnetilleggene er derfor" },
                                nynorsk { +"Desse barnetillegga er derfor" },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBbrutto  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBbrutto)  AND  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0)  AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0)) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf(
                            (pe.ut_tbu1286_del1() and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbbrutto()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbbrutto())) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0)) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag()
                                .equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))
                        ) {
                            text(
                                bokmal { +" ikke" },
                                nynorsk { +" ikkje" },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0  AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0)) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0  ) THEN      INCLUDE ENDIF
                        showIf(
                            (pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                                .equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))
                        ) {
                            text(
                                bokmal { +" redusert ut fra inntekt. " },
                                nynorsk { +" redusert ut frå inntekt. " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND  ( PE_UT_TBU1286_del2() OR PE_UT_TBU1286_del3() )) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and (pe.ut_tbu1286_del2() or pe.ut_tbu1286_del3()))) {
                            text(
                                bokmal { +" " },
                                nynorsk { +" " },
                            )
                        }

                        //IF(( PE_UT_TBU1286_del2() )) THEN      INCLUDE ENDIF
                        showIf(((pe.ut_tbu1286_del2()))) {
                            text(
                                bokmal { +"Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre. " },
                                nynorsk { +"Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine. " },
                            )
                        }

                        //IF(( PE_UT_TBU1286_del2() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 )) THEN      INCLUDE ENDIF
                        showIf(((pe.ut_tbu1286_del2() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0)))) {
                            text(
                                bokmal { +"Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                nynorsk { +"Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                            )
                        }

                        //IF(( PE_UT_TBU1286_del2() AND PE_UT_TBU1286_del3() )) THEN      INCLUDE ENDIF
                        showIf(((pe.ut_tbu1286_del2() and pe.ut_tbu1286_del3()))) {
                            text(
                                bokmal { +" " },
                                nynorsk { +" " },
                            )
                        }

                        //IF(( PE_UT_TBU1286_del3() )) THEN      INCLUDE ENDIF
                        showIf(((pe.ut_tbu1286_del3()))) {
                            text(
                                bokmal { +"Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene. " },
                                nynorsk { +"Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra. " },
                            )
                        }

                        //IF(( PE_UT_TBU1286_del3() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 )) THEN      INCLUDE ENDIF
                        showIf(((pe.ut_tbu1286_del3() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)))) {
                            text(
                                bokmal { +"Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                nynorsk { +"Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                            )
                        }
                    }
                }

                //IF( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) ) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) or not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())))) {
                    paragraph {
                        text(
                            bokmal { +"Barnetillegget " },
                            nynorsk { +"Barnetillegget " },
                        )

                        //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                        showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                            text(
                                bokmal { +"for " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene, " },
                                nynorsk { +"for " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra sine, " },
                            )
                        }
                        text(
                            bokmal { +"blir ikke utbetalt fordi du " },
                            nynorsk { +"blir ikkje utbetalt fordi du " },
                        )

                        //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                        showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                            text(
                                bokmal { +"alene " },
                                nynorsk { +"åleine " },
                            )
                        }
                        text(
                            bokmal { +"har en samlet inntekt som er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + " kroner. Inntekten din er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { +"har ei samla inntekt som er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + " kroner. Inntekta di er over grensa for å få utbetalt barnetillegg. " },
                        )
                    }
                }

                //IF( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false) ) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) or not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())))) {
                    paragraph {
                        text(
                            bokmal { +"Barnetillegget" },
                            nynorsk { +"Barnetillegget " },
                        )

                        //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true
                        showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) {
                            text(
                                bokmal { +" for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre," },
                                nynorsk { +"for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine, " },
                            )
                        }
                        text(
                            bokmal { +" blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + " kroner. De samlede inntektene er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { +"blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + " kroner. Dei samla inntektene er over grensa for å få utbetalt barnetillegg." },
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0)  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0)  )  THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0)) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0)))) {
                    includePhrase(TBU2490_Generated(pe))
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) {
                    includePhrase(TBU1288_Generated)
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())) {

                showIf((pe.ut_kravlinjekode_vedtakresultat_forekomst_bt_innv().greaterThan(0))) {
                    includePhrase(TBU5005_Generated)
                    includePhrase(TBU5007_Generated)
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                includePhrase(TBU2275_Generated)
                paragraph {
                    text(
                        bokmal { +"Du mottar ektefelletillegg i uføretrygden din. Dette tillegget blir ikke endret som følge av endring i uføretrygden din." },
                        nynorsk { +"Du får ektefelletillegg i uføretrygda di. Dette tillegget blir ikkje endra som følgje av endring i uføretrygda di." },
                    )
                    text(
                        bokmal { +"For å ha rett til ektefelletillegg fra 1. juli 2020 " },
                        nynorsk { +"For å ha rett til ektefelletillegg frå 1. juli 2020 " },
                    )
                    text(
                        bokmal { +"må du enten bo i Norge, innenfor EØS-området eller i et annet land Norge har trygdeavtale med" },
                        nynorsk { +"må du enten bu i Noreg, innanfor EØS-området eller i eit anna land Noreg har trygdeavtale med" },
                    )
                    text(
                        bokmal { +"må også ektefellen/samboeren din være bosatt og oppholde seg i Norge, innenfor EØS-området eller et annet land Norge har trygdeavtale med" },
                        nynorsk { +"må også ektefellen/sambuaren din vere busett og opphalde seg i Noreg, innanfor EØS-området eller eit anna land Noreg har trygdeavtale med" },
                    )
                    text(
                        bokmal { +"Hvis du planlegger å flytte eller oppholde deg i et annet land, må du kontakte oss slik at vi kan ta stilling til om du fortsatt har rett til ektefelletillegg. Dette gjelder også hvis ektefellen/samboeren din du forsørger skal oppholde seg i et annet land." },
                        nynorsk { +"Om du planlegg å flytte eller opphalde deg i eit anna land må du kontakte oss slik at vi kan ta stilling til om du fortsatt har rett til ektefelletillegg. Dette gjeld også om ektefellen/sambuaren du forsørgjer skal opphalde seg i eit anna land." },
                    )
                }
            }

            //IF(PE_SaksData_SakAPogUP = true AND PE_SaksData_SakAPStatus = "lopende" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_harLopendealderspensjon() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))) {
                paragraph {
                    text(
                        bokmal { +"For deg som kombinerer uføretrygd og alderspensjon" },
                        nynorsk { +"For deg som kombinerer uføretrygd og alderspensjon" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du mottar alderspensjon fra folketrygden. Når du kombinerer uføretrygd og alderspensjon kan disse til sammen ikke utgjøre mer enn 100 prosent." },
                        nynorsk { +"Du mottar alderspensjon frå folketrygda. Viss du kombinerer uføretrygd og alderspensjon, kan den totale prosenten ikkje vere høgare enn 100 prosent." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_onsketVirkningsDato < PE_VedtakFattetDato_minus_1mnd) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_belopokt() and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and onsketvirkningsdato.lessThan(pe.vedtakfattetdato_minus_1mnd()))) {
                paragraph {
                    text(
                        bokmal { +"Etterbetaling av uføretrygd" },
                        nynorsk { +"Etterbetaling av uføretrygd" },
                    )
                    text(
                        bokmal { +"Du får etterbetalt uføretrygd fra " + onsketvirkningsdato.format() + ". Beløpet blir vanligvis utbetalt i løpet av sju virkedager. Det kan bli beregnet fradrag i etterbetalingen for skatt og ytelser du har mottatt fra Nav eller andre, som for eksempel tjenestepensjonsordninger. I disse tilfellene kan etterbetalingen bli forsinket med inntil ni uker. Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen." },
                        nynorsk { +"Du får etterbetalt uføretrygd frå " + onsketvirkningsdato.format() + ". Beløpet blir vanlegvis utbetalt innan sju vyrkedagar. Det kan bli rekna ut frådrag i etterbetalinga for skatt og ytingar du har fått frå Nav eller andre, som til dømes tenestepensjonsordningar. I desse tilfella kan etterbetalinga bli forseinka med inntil ni veker. Frådrag i etterbetalinga kjem fram av utbetalingsmeldinga." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningAntallPerioder > 1 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_onsketVirkningsDato < PE_VedtakFattetDato_minus_1mnd) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningantallperioder().greaterThan(1) and pe.vedtaksdata_beregningsdata_beregningufore_belopredusert() and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and onsketvirkningsdato.lessThan(pe.vedtakfattetdato_minus_1mnd()))) {
                includePhrase(TBU2530_Generated)
            }

            title1 {
                text(
                    bokmal { +"Du må melde fra om endringer" },
                    nynorsk { +"Du må melde frå om endringar" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Skjer det endringer, må du melde fra til oss med en gang. I vedlegget «Orientering om rettigheter og plikter» ser du hvilke endringer du må si fra om." },
                    nynorsk { +"Skjer det endringar, må du melde frå til oss med ein gong. I vedlegget «Orientering om rettar og plikter» ser du kva endringar du må seie frå om." },
                )
            }
            title1 {
                text(
                    bokmal { +"Du har rett til å klage " },
                    nynorsk { +"Du har rett til å klage " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du fikk vedtaket. I vedlegget " },
                    nynorsk { + "Dersom du meiner at vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen du fekk vedtaket. I vedlegget " },
                )
                namedReference(vedleggDineRettigheterOgPlikterUfoere)
                text(
                    bokmal { +" får du vite mer om hvordan du går fram. Du finner skjema og informasjon på $KLAGE_URL. " },
                    nynorsk { +" kan du lese meir om korleis du går fram. Du finn skjema og informasjon på $KLAGE_URL. " },
                )
            }
            title1 {
                text(
                    bokmal { +"Du har rett til innsyn" },
                    nynorsk { +"Du har rett til innsyn" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du har rett til å se dokumentene i saken din. Se vedlegg «Orientering om rettigheter og plikter» for informasjon om hvordan du går fram." },
                    nynorsk { +"Du har rett til å sjå dokumenta i saka di. Sjå vedlegg «Orientering om rettar og plikter» for informasjon om korleis du går fram." },
                )
            }
            title1 {
                text(
                    bokmal { +"Sjekk utbetalingene dine" },
                    nynorsk { +"Sjekk utbetalingane dine" },
                )
                text(
                    bokmal { +"Du får uføretrygd utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. Se alle utbetalinger du har mottatt: $UFOERETRYGD_URL. Her kan du også endre kontonummer." },
                    nynorsk { +"Du får uføretrygd betalt ut den 20. kvar månad eller seinast siste vyrkedag før denne datoen. Sjå alle utbetalingar du har fått: $UFOERETRYGD_URL. Her kan du også endre kontonummer." },
                )
            }
            title1 {
                text(
                    bokmal { +"Skattekort" },
                    nynorsk { +"Skattekort" },
                )
                text(
                    bokmal { +"Uføretrygd skattlegges som lønnsinntekt. Du trenger ikke levere skattekortet ditt til Nav fordi skatteopplysningene dine sendes elektronisk fra Skatteetaten. Du bør likevel sjekke at du har riktig skattekort. Skattekortet kan du endre på $SKATTEETATEN_URL. Under menyvalget «uføretrygd» når du logger deg inn på $NAV_URL, kan du se hvilket skattetrekk som er registrert hos Nav." },
                    nynorsk { +"Uføretrygd blir skattlagd som lønsinntekt. Du treng ikkje levere skattekortet ditt til Nav, fordi skatteopplysningane dine blir sende elektronisk frå Skatteetaten. Du bør likevel sjekke at du har rett skattekort. Skattekortet kan du endre på $SKATTEETATEN_URL. Under menyvalet «uføretrygd» når du logger deg inn på $NAV_URL, kan du sjå kva skattetrekk som er registrert hos Nav." },
                )
            }

            //IF(FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) <> "nor" AND FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) <> "") THEN      INCLUDE ENDIF
            showIf(((pe.grunnlag_persongrunnlagsliste_personbostedsland()).notEqualTo("nor") and (pe.grunnlag_persongrunnlagsliste_personbostedsland()).notEqualTo(""))) {
                title1 {
                    text(
                        bokmal { +"Skatt for deg som bor i utlandet" },
                        nynorsk { +"Skatt for deg som bur i utlandet" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt på $SKATTEETATEN_URL. Hvis du er bosatt i utlandet og betaler skatt i annet land enn Norge, kan du kontakte skattemyndighetene der du bor." },
                        nynorsk { +"Bur du i utlandet og betaler kjeldeskatt, finn du meir informasjon om kjeldeskatt på $SKATTEETATEN_URL. Viss du er busett i utlandet og betaler skatt i eit anna land enn Noreg, kan du kontakte skattemyndigheitene der du bur." },
                    )
                }
            }
            title1 {
                text(
                    bokmal { +"Har du spørsmål?" },
                    nynorsk { +"Har du spørsmål?" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du finner mer informasjon på $NAV_URL. Hvis du ikke finner svar på spørsmålet ditt, kontakt oss på $KONTAKT_URL" },
                    nynorsk { +"Du finn meir informasjon på $NAV_URL. Om du ikkje finn svar på spørsmålet ditt, kontakt oss på $KONTAKT_URL." },
                )
            }

            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, pesysData.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pesysData.pe, pesysData.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
