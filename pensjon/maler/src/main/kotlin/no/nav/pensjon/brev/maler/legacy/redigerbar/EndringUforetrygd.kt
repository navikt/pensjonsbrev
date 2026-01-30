package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.PesysDataSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.PesysDataSelectors.pe
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.adhoc.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.fraser.*
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
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
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate
import kotlin.and

@TemplateModelHelpers
object EndringUforetrygd : RedigerbarTemplate<InnvilgelseUfoeretrygdDto> {

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
                bokmal { +"Nav har innvilget søknaden din om uføretrygd" },
                nynorsk { +"Nav har innvilget søknaden din om uføretrygd " },
            )
        }
        outline {
            val pe = pesysData.pe

            val uforetidspunkt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt().ifNull(LocalDate.now())
            val virkningstidpunkt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt().ifNull(LocalDate.now())

            //IF(PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_uu" AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_ys" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "barn_endret_inntekt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "eps_endret_inntekt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "begge_for_end_inn" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "omgj_etter_klage" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "omgj_etter_anke" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravgjelder().notEqualTo("sok_uu") and pe.vedtaksdata_kravhode_kravgjelder().notEqualTo("sok_ys") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endret_inntekt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("barn_endret_inntekt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("eps_endret_inntekt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("begge_for_end_inn") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("omgj_etter_klage") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("omgj_etter_anke") and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()))){
                //[TBU2287EN, TBU2287, TBU2287NN]

                paragraph {
                    text (
                        bokmal { + "Vi har endret uføretrygden din fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "." },
                        nynorsk { + "Vi har endra uføretrygda di frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "." }
                    )
                }
            }

            //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) AND PE_Vedtaksdata_Kravhode_KravArsakType = "soknad_bt" ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_kravhode_kravarsaktype().equalTo("soknad_bt"))){
                //[TBU2288]
                paragraph {
                    text (
                        bokmal { + "Vi har innvilget søknaden din om barnetillegg som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har endret uføretrygden din fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "." },
                        nynorsk { + "Vi har innvilga søknaden din om barnetillegg som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har endra uføretrygda di frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "." },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND  (((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0)   AND   (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0))   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false))  ) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0))) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))))){
                        text (
                            bokmal { + " Tillegget blir ikke utbetalt fordi inntekten til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { + " Tillegget blir ikkje utbetalt, fordi inntekta til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din er over grensa for å få utbetalt barnetillegg." },
                        )
                    }

                    //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)  AND  (((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0)   AND   (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0))   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false))  ) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) and (((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0))) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))))){
                        text (
                            bokmal { + " Tillegget blir ikke utbetalt fordi inntekten din er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { + "Tillegget blir ikkje utbetalt fordi inntekta di er over grensa for å få utbetalt barnetillegg." }
                        )
                    }
                }
            }

            //IF(  (PE_Vedtaksdata_Kravhode_KravArsakType = "omgj_etter_klage" OR PE_Vedtaksdata_Kravhode_KravArsakType = "omgj_etter_anke")  ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("omgj_etter_klage") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("omgj_etter_anke")))){
                //[TBU3332EN, TBU3332, TBU3332NN]

                paragraph {
                    text (
                        bokmal { + "Vi har endret uføretrygden din fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + " fordi du har fått medhold i klagen din." },
                        nynorsk { + "Vi har endra uføretrygda di frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + " fordi du har fått medhald i klaga di." },
                    )
                }
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND  (PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt" OR  PE_Vedtaksdata_Kravhode_KravArsakType = "barn_endret_inntekt" OR PE_Vedtaksdata_Kravhode_KravArsakType = "eps_endret_inntekt" OR PE_Vedtaksdata_Kravhode_KravArsakType = "begge_for_end_inn")    ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and (pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("barn_endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("eps_endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("begge_for_end_inn")))){
                //[TBU2289EN, TBU2289, TBU2289NN]

                paragraph {
                    text (
                        bokmal { + "Vi har endret barnetillegget i uføretrygden din fordi du har meldt fra om inntektsendring." },
                        nynorsk { + "Vi har endra barnetillegget i uføretrygda di fordi du har meldt frå om inntektsendring." },
                    )
                }
            }

            //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
            showIf((FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_bt(),pe.ut_konst_vilkarsvedtakresultat_opphor()))){
                includePhrase(TBU2290_Generated(pe))
            }

            //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_et, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
            showIf((FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_et(),pe.ut_konst_vilkarsvedtakresultat_opphor()))){
                //[TBU2291EN, TBU2291, TBU2291NN]

                paragraph {
                    text (
                        bokmal { + "Vi har opphørt ektefelletillegget i uføretrygden din fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "." },
                        nynorsk { + "Vi har stansa ektefelletillegget i uføretrygda di frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "." },
                    )
                }
            }

            //PE_Vedtaksdata_Kravhode_KravGjelder = "sok_uu"
            showIf(pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_uu")){
                //[TBU2292EN, TBU2292, TBU2292NN]

                paragraph {
                    text (
                        bokmal { + "Vi har innvilget søknaden din om rettighet som ung ufør som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har endret uføretrygden din fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "." },
                        nynorsk { + "Vi har innvilga søknaden din om å få rettar som ung ufør som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har endra uføretrygda di frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "." },
                    )
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) = FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) AND PE_Vedtaksdata_Kravhode_KravGjelder = "sok_ys") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).equalTo((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad())) and pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_ys"))){
                //[TBU2293EN, TBU2293, TBU2293NN]

                paragraph {
                    text (
                        bokmal { + "Vi har innvilget søknaden din om uføretrygd etter særbestemmelsene for yrkesskade eller yrkessykdom som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har kommet fram til at hele uførheten din skyldes en godkjent yrkesskade eller yrkessykdom, og uføretrygden din er endret fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "." },
                        nynorsk { + "Vi har innvilga søknaden din om uføretrygd etter særreglane for yrkesskade eller yrkessjukdom som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har kome fram til at heile uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom, og uføretrygda di er endra frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "." },
                    )
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravGjelder = "sok_ys") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_ys"))){
                //[TBU2294EN, TBU2294, TBU2294NN]

                paragraph {
                    text (
                        bokmal { + "Vi har innvilget søknaden din om uføretrygd etter særbestemmelsene for yrkesskade eller yrkessykdom som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har kommet fram til at " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().format() + " prosent av uførheten din skyldes en godkjent yrkesskade eller yrkessykdom, og uføretrygden din er endret fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "." },
                        nynorsk { + "Vi har innvilga søknaden din om uføretrygd etter særreglane for yrkesskade eller yrkessjukdom som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har kome fram til at " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().format() + " prosent av uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom, og uføretrygda di er endra frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "." },
                    )
                }
            }

            //PE_Vedtaksdata_Kravhode_KravArsakType = "endring_ifu"
            showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endring_ifu")){
                //[TBU2295EN, TBU2295, TBU2295NN]

                paragraph {
                    text (
                        bokmal { + "Vi har innvilget søknaden din om endring av inntektsgrense." },
                        nynorsk { + "Vi har innvilga søknaden din om endring av inntektsgrense." },
                    )
                    text (
                        bokmal { + "Den nye inntektsgrensen din har økt til " + pe.ut_inntektsgrense_faktisk().format() + " kroner fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "." },
                        nynorsk { + "Den nye inntektsgrensa di har auka til " + pe.ut_inntektsgrense_faktisk().format() + " kroner frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                //[TBU2296EN, TBU2296, TBU2296NN]

                paragraph {
                    text (
                        bokmal { + "Vi har innvilget deg gjenlevenderettigheter i uføretrygden din. Vi har endret uføretrygden din fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "." },
                        nynorsk { + "Vi har innvilga deg attlevanderettar i uføretrygda di. Vi har endra uføretrygda di frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "." },
                    )
                }
            }

            //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_ut_gjt, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
            showIf((FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_ut_gjt(),pe.ut_konst_vilkarsvedtakresultat_opphor()))){
                //[TBU2297EN, TBU2297, TBU2297NN]

                paragraph {
                    text (
                        bokmal { + "Vi har opphørt gjenlevendetillegget i uføretrygden din fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "." },
                        nynorsk { + "Vi har stansa attlevandetillegget i uføretrygda di frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "instopphold" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("instopphold") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                //[TBU3333EN, TBU3333, TBU3333NN]

                paragraph {
                    text (
                        bokmal { + "Vi har endret uføretrygden din fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + " fordi du ble innlagt på institusjon." },
                        nynorsk { + "Vi har endra uføretrygda di frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + " fordi du ble innlagd på institusjon." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                //[TBU3334EN, TBU3334, TBU3334NN]

                paragraph {
                    text (
                        bokmal { + "Vi har mottatt opplysninger om at du er innlagt på institusjon. Dette får ikke betydning for uføretrygden din, og du vil få utbetalt det samme som før." },
                        nynorsk { + "Vi har fått opplysningar om at du er innlagd på institusjon. Dette får ikkje noko å seie for uføretrygda di, og du får utbetalt det same som før." },
                    )

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  AND  PE_UT_Forsorgeransvar_ingen_er_false()  AND PE_Vedtaksdata_Kravhode_KravArsakType ="instopphold" AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0)  THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksdata_kravhode_kravarsaktype().equalTo("instopphold") and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0))){
                        text (
                            bokmal { + " Fra og med måneden etter at du ble innlagt på institusjon vil du bli vurdert med sivilstand som enslig. Det betyr at barnetillegget kun vil bli beregnet ut fra den samlede inntekten din fra samme tidspunkt." },
                            nynorsk { + " Frå og med månaden etter at du blei innlagd på institusjon vil du bli vurdert med ein sivilstand som einsleg. Det betyr at barnetillegget berre blir berekna ut frå den samla inntekta di frå same tidspunkt. " },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "instopphold" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("instopphold") and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                //[TBU3363EN, TBU3363, TBU3363NN]

                paragraph {
                    text (
                        bokmal { + "Vi har mottatt opplysninger om at du er under straffegjennomføring. Dette får ikke betydning for uføretrygden din, og du vil få utbetalt det samme som før." },
                        nynorsk { + "Vi har fått opplysningar om at du er under straffegjennomføring. Dette får ikkje noko å seie for uføretrygda di, og du får utbetalt det same som før." },
                    )

                    // IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  AND  PE_UT_Forsorgeransvar_ingen_er_false()  AND PE_Vedtaksdata_Kravhode_KravArsakType ="instopphold" AND  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true))  THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksdata_kravhode_kravarsaktype().equalTo("instopphold") and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))){
                        text (
                            bokmal { + " Fra og med måneden etter at du er under straffegjennomføring vil du bli vurdert med sivilstand som enslig. Det betyr at barnetillegget kun vil bli beregnet ut fra den samlede inntekten din fra samme tidspunkt." },
                            nynorsk { + " Frå og med månaden etter at du er under straffegjennomføringa vil du bli vurdert med ein sivilstand som einsleg. Det betyr at barnetillegget berre blir berekna ut frå den samla inntekta di frå same tidspunkt." },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "instopphold" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("instopphold") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                //[TBU3335EN, TBU3335, TBU3335NN]

                paragraph {
                    text (
                        bokmal { + "Vi har endret utbetalingen av uføretrygden din fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + " fordi du er under straffegjennomføring." },
                        nynorsk { + "Vi har stansa utbetalinga av uføretrygda di frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + " fordi du er under straffegjennomføring." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "instopphold" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("instopphold") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo(""))){
                //[TBU3337EN, TBU3337, TBU3337NN]

                paragraph {
                    text (
                        bokmal { + "Vi har gjenopptatt utbetaling av uføretrygden din fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "." },
                        nynorsk { + "Vi har teke til att med å utbetale uføretrygda di frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + "." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false) THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                includePhrase(TBU1120_Generated(pe))
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                includePhrase(TBU1121_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false) THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                includePhrase(TBU1122_Generated(pe))
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                includePhrase(TBU1123_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETnetto > 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etnetto().greaterThan(0))){
                includePhrase(TBU1253_Generated(pe))
            }

            //IF(  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETnetto > 0  AND   ((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0))  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etnetto().greaterThan(0) and ((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().greaterThan(0)) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().greaterThan(0))) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                includePhrase(TBU1254_Generated(pe))
            }

            //IF(   PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad  AND  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and (not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                includePhrase(TBU2298_Generated(pe))
            }

            //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND   (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)   AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                includePhrase(TBU2299_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                includePhrase(TBU2300_Generated(pe))
            }

            //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad  AND   (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)   AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and (not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) or not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                includePhrase(TBU2301_Generated(pe))
            }

            //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                includePhrase(TBU2302_Generated(pe))
            }

            //IF(  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo" AND PE_UT_Forsorgeransvar_ingen_er_true()  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true   ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and pe.ut_forsorgeransvar_ingen_er_true() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt())){
                includePhrase(TBU3101_Generated(pe))
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt())){
                includePhrase(TBU3102_Generated(pe))
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  AND PE_UT_Forsorgeransvar_ingen_er_false()  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt())){
                includePhrase(TBU3331_Generated(pe))
            }

            //IF((FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) = "nor"  OR FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) = "")  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = true  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true)) THEN      INCLUDE ENDIF
            showIf((((pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("nor") or (pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("")) and (pe.vedtaksdata_beregningsdata_beregningufore_belopokt() or pe.vedtaksdata_beregningsdata_beregningufore_belopredusert()))){
                includePhrase(TBU2303mFRI_Generated)
            }

            //IF(FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) <> "nor" AND FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) <> "") THEN      INCLUDE ENDIF
            showIf(((pe.grunnlag_persongrunnlagsliste_personbostedsland()).notEqualTo("nor") and (pe.grunnlag_persongrunnlagsliste_personbostedsland()).notEqualTo(""))){
                //[TBU2304mFRI_EN, TBU2304mFRI, TBU2304mFRINN]

                paragraph {
                    text (
                        bokmal { + "Uføretrygden blir utbetalt senest den 20. hver måned. Mottar du uføretrygden på en utenlandsk bankkonto kan utbetalingen bli forsinket. Du får din første utbetaling i <FRITEKST: måned og år>." },
                        nynorsk { + "Uføretrygda blir utbetalt seinast den 20. i kvar månad. Får du uføretrygda på ein utanlandsk bankkonto, kan utbetalinga bli forseinka. Du får den første utbetalinga di i <FRITEKST: måned og år>." },
                    )
                }
            }

            //IF((PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = false) AND(FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) = "nor"  OR FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) = "")) THEN      INCLUDE ENDIF
            showIf(((not(pe.vedtaksdata_beregningsdata_beregningufore_belopokt()) and not(pe.vedtaksdata_beregningsdata_beregningufore_belopredusert())) and ((pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("nor") or (pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("")))){
                includePhrase(TBU2223_Generated)
            }
            includePhrase(TBU1128_Generated)
            includePhrase(TBU1092_Generated)

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold" AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_EktefelleGarantiTillegg_EGTinnvilget = false) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold") and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefellegarantitillegg_egtinnvilget()))){
                //[TBU2305EN, TBU2305, TBU2305NN]

                paragraph {
                    text (
                        bokmal { + "Du har uføretrygd med minsteytelse. Størrelsen på minsteytelsen er avhengig av sivilstanden din." },
                        nynorsk { + "Du har uføretrygd med minsteyting. Storleiken på minsteytinga er avhengig av sivilstanden din." },
                    )
                }
            }

            //IF(  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true  AND PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring" AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = true OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true)  ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse() and pe.vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring") and (pe.vedtaksdata_beregningsdata_beregningufore_belopokt() or pe.vedtaksdata_beregningsdata_beregningufore_belopredusert()))){
                //[TBU2305_1EN, TBU2305_1, TBU2305_1NN]

                paragraph {
                    text (
                        bokmal { + "Vi har mottatt opplysninger om at sivilstanden din har blitt endret. Utbetalingen din endres derfor til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().format() + " ganger folketrygdens grunnbeløp." },
                        nynorsk { + "Vi har fått opplysningar om at sivilstanden din har blitt endra. Utbetalinga av uføretrygda di blir derfor endra til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().format() + " gonger grunnbeløpet i folketrygda." },
                    )
                }
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = false OR (PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = false)) AND PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"  ) THEN      INCLUDE ENDIF
            showIf(((not(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse()) or (not(pe.vedtaksdata_beregningsdata_beregningufore_belopokt()) and not(pe.vedtaksdata_beregningsdata_beregningufore_belopredusert()))) and pe.vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring"))){
                //[TBU2305_2EN, TBU2305_2, TBU2305_2NN]

                paragraph {
                    text (
                        bokmal { + "Vi har mottatt opplysninger om at sivilstanden din har blitt endret. Dette får ikke betydning for uføretrygden din, og du vil få utbetalt det samme som før." },
                        nynorsk { + "Vi har fått opplysningar om at sivilstanden din har blitt endra. Dette får ikkje noko å seie for uføretrygda di, og du får utbetalt det same som før." },
                    )
                }
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5"  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2"  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed_ektefelle"  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed_registrert_partner")  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = false AND PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"   ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 1-5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed_ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed_registrert_partner")) and pe.vedtaksdata_beregningsdata_beregningufore_belopredusert() and not(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse()) and pe.vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring"))){
                //[TBU2321mFRI_EN, TBU2321mFRI, TBU2321mFRINN]

                paragraph {
                    text (
                        bokmal { + "Vi har mottatt opplysninger om at du <FRITEKST: sivilstandsendring>. Du har minsteytelse i uføretrygden din. Den endrede sivilstanden din medfører nå at du får uføretrygd på grunnlag av egen opptjening." },
                        nynorsk { + "Vi har fått opplysningar om at du <FRITEKST: sivilstandsendring>. Du har minsteyting i uføretrygda di. Den endra sivilstanden din fører no til at du får uføretrygd på grunnlag av di eiga opptening." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravGjelder = "sok_uu" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat) = "oppfylt") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_uu") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat()).equalTo("oppfylt"))){
                //[TBU2322EN, TBU2322, TBU2322NN]

                paragraph {
                    text (
                        bokmal { + "For å innvilges rettighet som ung ufør, må du være alvorlig og varig syk før du fylte 26 år. Uføretidspunktet ditt er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforetidspunkt().format() + ". Du er innvilget rettighet som ung ufør. Det betyr at uføretrygden din vil bli beregnet etter regler som gjelder ung ufør." },
                        nynorsk { + "For at du skal få innvilga rett som ung ufør, må du vere alvorleg og varig sjuk før du fylte 26 år. Uføretidspunktet ditt er sett til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforetidspunkt().format() + ". Du er innvilga rett som ung ufør. Det betyr at uføretrygda di blir berekna etter reglar som gjeld ung ufør." },
                    )
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                //[TBU2323EN, TBU2323, TBU2323NN]

                paragraph {
                    text (
                        bokmal { + "Du er innvilget uføretrygd etter regler for yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert at hele din nedsatte inntektsevne skyldes den godkjente yrkesskaden eller yrkessykdommen." },
                        nynorsk { + "Du er innvilga uføretrygd etter reglar for yrkesskade eller yrkessjukdom. Vi har ut frå opplysningane i saka vurdert at heile den nedsette inntektsevna di kjem av den godkjende yrkesskaden eller yrkessjukdommen." },
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false
                    showIf(not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup())){
                        text (
                            bokmal { + " Dette betyr at uføretrygden din vil bli beregnet etter særbestemmelser som gir deg en høyere uføretrygd." },
                            nynorsk { + " Dette betyr at uføretrygda di blir berekna etter særreglar, og gir deg ei høgare uføretrygd." },
                        )
                    }
                }
                includePhrase(TBU1167_Generated(pe))

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,1) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IVSBegrunnelse) = "stdbegr_12_17_3_2") THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat(),1).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsbegrunnelse()).equalTo("stdbegr_12_17_3_2"))){
                    includePhrase(TBU3241_Generated(pe))
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = false AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                //[TBU2324EN, TBU2324, TBU2324NN]

                paragraph {
                    text (
                        bokmal { + "Du er innvilget uføretrygd etter regler for yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert at hele din nedsatte inntektsevne skyldes den godkjente yrkesskaden eller yrkessykdommen." },
                        nynorsk { + "Du er innvilga uføretrygd etter reglar for yrkesskade eller yrkessjukdom. Vi har ut frå opplysningane i saka vurdert at heile den nedsette inntektsevna di kjem av den godkjende yrkesskaden eller yrkessjukdommen." },
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false
                    showIf(not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup())){
                        text (
                            bokmal { + " Dette betyr at uføretrygden din vil bli beregnet etter særbestemmelser, dersom dette er til fordel for deg." },
                            nynorsk { + " Dette betyr at uføretrygda di blir berekna etter særreglar dersom dette er til fordel for deg." },
                        )
                    }
                }
                includePhrase(TBU1167_Generated(pe))

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,1) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IVSBegrunnelse) = "stdbegr_12_17_3_2") THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat(),1).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsbegrunnelse()).equalTo("stdbegr_12_17_3_2"))){
                    includePhrase(TBU3241_Generated(pe))
                }

                //PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false
                showIf(not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup())){
                    //[TBU1270, TBU1270NN]

                    paragraph {
                        text (
                            bokmal { + "Inntekten din på skadetidspunktet er lavere enn beregningsgrunnlaget ditt, og uføretrygden din vil derfor ikke bli beregnet etter særbestemmelser for yrkesskade eller yrkessykdom." },
                            nynorsk { + "Inntekta di på skadetidspunktet er lågare enn berekningsgrunnlaget ditt, og uføretrygda di blir derfor ikkje berekna etter særreglar for yrkesskade eller yrkessjukdom." },
                        )
                    }
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) > 0 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).greaterThan(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                //[TBU2372EN, TBU2372, TBU2372NN]

                paragraph {
                    text (
                        bokmal { + "Du er innvilget uføretrygd etter regler for yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert om yrkesskaden eller yrkessykdommen din er årsak til uførheten din." },
                        nynorsk { + "Du er innvilga uføretrygd etter reglar for yrkesskade eller yrkessjukdom. Vi har ut frå opplysningane i saka vurdert om yrkesskaden eller yrkessjukdommen din er årsaka til uførleiken din." },
                    )
                }
                //[TBU2373EN, TBU2373, TBU2373NN]

                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().format() + " prosent av uførheten din skyldes godkjent yrkesskade eller yrkessykdom. <FRITEKST: konkret begrunnelse>" },
                        nynorsk { + "Vi har kome fram til at " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().format() + " prosent av uførleiken din kjem av godkjend yrkesskade eller yrkessjukdom. <FRITEKST: konkret begrunnelse>." },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = true AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest() and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))){
                    includePhrase(TBU1263_Generated)
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) > 0 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).greaterThan(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = false AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
                showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))){
                    includePhrase(TBU1264_Generated)
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) = "stdbegr_12_17_3_1") THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse()).equalTo("stdbegr_12_17_3_1"))){
                    includePhrase(TBU1167_Generated(pe))
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,1) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IVSBegrunnelse) = "stdbegr_12_17_3_2") THEN      INCLUDE ENDIF
                showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat(),1).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsbegrunnelse()).equalTo("stdbegr_12_17_3_2"))){
                    includePhrase(TBU3241_Generated(pe))
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = false AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
                showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))){
                    includePhrase(TBU1265_Generated)
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "endring_ifu" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100 AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_9") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endring_ifu") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().lessThan(100) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_9"))){

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "endring_ifu" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100 AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_9") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endring_ifu") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().lessThan(100) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_9"))){
                    //[TBU2326EN, TBU2326, TBU2326NN]

                    paragraph {
                        text (
                            bokmal { + "Inntekten i stillingen din har økt og du får derfor en høyere inntektsgrense. Dette gjør vi for at du skal få riktig utbetaling av uføretrygd." },
                            nynorsk { + "Inntekta i stillinga di har auka, og du får derfor ei høgare inntektsgrense. Dette gjer vi for at du skal få riktig utbetaling av uføretrygd." },
                        )
                    }
                }
                includePhrase(TBU1133_Generated)

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_UT_VirkningstidpunktStorreEnn01012016() = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.ut_virkningstidpunktstorreenn01012016())){
                    //[TBU3821EN, TBU3821, TBU3821_NN]

                    paragraph {
                        text (
                            bokmal { + "Fordi inntekten din har økt, blir størrelsen på barnetillegget ditt endret." },
                            nynorsk { + "Fordi inntekta di har auka, blir storleiken på barnetillegget ditt endra." },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                //[TBU2327EN, TBU2327, TBU2327NN]

                paragraph {
                    text (
                        bokmal { + "Du er innvilget gjenlevendetillegg i uføretrygden fordi du er gjenlevende ektefelle/samboer. Tillegget er beregnet etter ditt eget og den avdødes beregningsgrunnlag. " },
                        nynorsk { + "Du er innvilga attlevandetillegg i uføretrygda fordi du er attlevande ektefelle/sambuar. Tillegget er berekna etter ditt eiga og den avdøde sitt brekningsgrunnlag. " },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse() and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())){
                        text (
                            bokmal { + "Siden sivilstanden din har endret seg har dette også betydning for størrelsen på uføretrygden din. " },
                            nynorsk { + "Sidan sivilstanden din har endra seg har dette også noko å seie for storleiken på uføretrygda di. " },
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true   OR (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true        AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTnetto > 0       )    ) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse() or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtnetto().greaterThan(0)))){
                        text (
                            bokmal { + "Det betyr at uføretrygden din har økt. " },
                            nynorsk { + "Det betyr at uføretrygda di har auka. " },
                        )
                    }

                    //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_NyttGjenlevendetillegg = true
                    showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg()){
                        text (
                            bokmal { + "Tillegget er tidsbegrenset til fem år fra virkningstidspunktet. " },
                            nynorsk { + "Tillegget er tidsbegrensa til fem år frå verknadstidspunktet. " },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                //[TBU2328EN, TBU2328, TBU2328NN]

                paragraph {
                    text (
                        bokmal { + "Avdøde må også ha vært medlem i folketrygden, eller mottatt pensjon fra folketrygden, de siste <FRITEKST: tre/fem årene>  før dødsfallet." },
                        nynorsk { + "Avdøde må også ha vore medlem i folketrygda, eller fått pensjon frå folketrygda, dei siste <FRITEKST: tre/fem åra>  før dødsfallet." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningBenyttetSivilstand= "gift" AND PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_ut_gjt, PE_UT_KONST_VilkarsVedtakResultat_opphor)) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring") and pe.vedtaksdata_beregningsdata_beregning_beregningbenyttetsivilstand().equalTo("gift") and FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_ut_gjt(),pe.ut_konst_vilkarsvedtakresultat_opphor()))){
                //[TBU2329EN, TBU2329, TBU2329NN]

                paragraph {
                    text (
                        bokmal { + "Du er tidligere innvilget gjenlevendetillegg i uføretrygden din. Gjenlevendetillegget ditt opphører fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + " fordi du har giftet deg." },
                        nynorsk { + "Du er tidlegare innvilga attlevandetillegg i uføretrygda di. Attlevandetillegget ditt tek slutt frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + " fordi du har gifta deg." },
                    )
                }
            }

            //IF(  PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"  AND  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningBrukerSivilstand = "samboer1_5"  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningBrukerSivilstand = "samboer3_2"  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningBrukerSivilstand <> "gift")  AND PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_ut_gjt, PE_UT_KONST_VilkarsVedtakResultat_opphor)  ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring") and (pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().equalTo("samboer1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().equalTo("samboer3_2") and pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().notEqualTo("gift")) and FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_ut_gjt(),pe.ut_konst_vilkarsvedtakresultat_opphor()))){
                //[TBU2330EN, TBU2330, TBU2330NN]

                paragraph {
                    text (
                        bokmal { + "Du er tidligere innvilget gjenlevendetillegg i uføretrygden din. Gjenlevendetillegget ditt opphører fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + " fordi du er i et samboerforhold, og dere har felles barn." },
                        nynorsk { + "Du er tidlegare innvilga attlevandetillegg i uføretrygda di. Attlevandetillegget ditt tek slutt frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + " fordi du er i eit sambuarforhold og de har felles barn." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "sivilstandsendring"  AND  PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_et, PE_UT_KONST_VilkarsVedtakResultat_opphor) AND PE_Vedtaksdata_Kravhode_KravArsakType <> "sokand_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold" ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("sivilstandsendring") and FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_et(),pe.ut_konst_vilkarsvedtakresultat_opphor()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("sokand_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                //[TBU2331]
                paragraph {

                    //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bruker_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_opph_ikke_avt_land") > 0 ) THEN      EXCLUDE ENDIF
                    showIf(not((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"bruker_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"eps_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"eps_opph_ikke_avt_land").greaterThan(0)))){
                        text (
                            bokmal { + "Ektefelletillegg beholdes bare ut den perioden som vedtaket gjelder for. Vi har derfor opphørt ektefelletillegget." },
                            nynorsk { + "Ektefelletillegget beheld du bare ut perioden vedtaket gjeld for. Vi har derfor stansa ektefelletillegget ditt. " },
                        )
                    }

                    //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bruker_flyttet_ikke_avt_land") > 0) THEN      INCLUDE ENDIF
                    showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"bruker_flyttet_ikke_avt_land").greaterThan(0))){
                        text (
                            bokmal { + "Ifølge våre opplysninger er du bosatt i <Fritekst: bostedsland>. Derfor har du ikke lenger rett til ektefelletillegg." },
                            nynorsk { + "Ifølgje våre opplysningar er du busett i <Fritekst: bostedsland>. Da har du ikkje lenger rett til ektefelletillegg." },
                        )
                    }

                    //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "barn_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_opph_ikke_avt_land") > 0 ) THEN      INCLUDE ENDIF
                    showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"eps_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"barn_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"eps_opph_ikke_avt_land").greaterThan(0))){
                        text (
                            bokmal { + "Ifølge våre opplysninger er <Fritekst: ektefellen/partneren/samboeren din> bosatt i <Fritekst: bostedsland>. Derfor har du ikke lenger rett til ektefelletetillegg." },
                            nynorsk { + "Ifølgje våre opplysningar er <Fritekst: ektefelle/partner/sambuar> busett i <Fritekst: bostedsland>. Da har du ikkje lenger rett til ektefelletillegg. " },
                        )
                    }

                    //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bruker_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_opph_ikke_avt_land") > 0 ) THEN      INCLUDE ENDIF
                    showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"bruker_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"eps_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"eps_opph_ikke_avt_land").greaterThan(0))){
                        text (
                            bokmal { + "For å ha rett til ektefelletillegg fra 1. juli 2020 " },
                            nynorsk { + "For å ha rett til ektefelletillegg frå 1. juli 2020 " },
                        )
                    }

                    //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bruker_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_opph_ikke_avt_land") > 0 ) THEN      INCLUDE ENDIF
                    showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"bruker_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"eps_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"eps_opph_ikke_avt_land").greaterThan(0))){
                        text (
                            bokmal { + "må du enten bo i Norge, innenfor EØS-området eller i et annet land Norge har trygdeavtale med" },
                            nynorsk { + "må du enten bu i Noreg, innanfor EØS-området eller i eit anna land Noreg har trygdeavtale med" },
                        )
                    }

                    //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bruker_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "eps_opph_ikke_avt_land") > 0 ) THEN      INCLUDE ENDIF
                    showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"bruker_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"eps_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"eps_opph_ikke_avt_land").greaterThan(0))){
                        text (
                            bokmal { + "må også ektefellen/samboeren din være bosatt og oppholde seg i Norge, innenfor EØS-området eller et annet land Norge har trygdeavtale med" },
                            nynorsk { + "må også ektefellen/sambuaren vere busett og opphalde seg i Noreg, innanfor EØS-området eller eit anna land Noreg har trygdeavtale med " },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningBenyttetSivilstand = "skil"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningBenyttetSivilstand = "skpa")  AND  PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_et, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring") and (pe.vedtaksdata_beregningsdata_beregning_beregningbenyttetsivilstand().equalTo("skil") or pe.vedtaksdata_beregningsdata_beregning_beregningbenyttetsivilstand().equalTo("skpa")) and FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_et(),pe.ut_konst_vilkarsvedtakresultat_opphor()))){
                //[TBU2332EN, TBU2332, TBU2332NN]

                paragraph {
                    text (
                        bokmal { + "Vi har mottatt opplysninger om at du har blitt skilt. Vi har derfor opphørt ektefelletillegget." },
                        nynorsk { + "Vi har fått opplysningar om at du har blitt skilt. Vi har derfor stansa ektefelletillegget ditt." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningBenyttetSivilstand = "enke" AND  PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_et, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring") and pe.vedtaksdata_beregningsdata_beregning_beregningbenyttetsivilstand().equalTo("enke") and FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_et(),pe.ut_konst_vilkarsvedtakresultat_opphor()))){
                //[TBU2333EN, TBU2333, TBU2333NN]

                paragraph {
                    text (
                        bokmal { + "Vi har mottatt opplysninger om at du har blitt enke/enkemann. Vi har derfor opphørt ektefelletillegget." },
                        nynorsk { + "Vi har fått opplysningar om at du har blitt enkje/enkjemann. Vi har derfor stansa ektefelletillegget ditt." },
                    )
                }
            }

            //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
            showIf((FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_bt(),pe.ut_konst_vilkarsvedtakresultat_opphor()))){
                //[TBU3924_EN, TBU3924, TBU3924_NN]

                paragraph {
                    text (
                        bokmal { + "Vi har opphørt barnetillegget i uføretrygden din for barn født: " + pe.ut_fodselsdatobarn() },
                        nynorsk { + "Vi har stansa barnetillegget i uføretrygda for barn fødd: " + pe.ut_fodselsdatobarn() },
                    )
                }

                //IF( Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bt_over_18") > 1  )THEN      INCLUDE ENDIF
                showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"bt_over_18").greaterThan(1))){
                    includePhrase(TBU3920_Generated(pe))
                }

                //IF( Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "annen_forld_rett_bt") > 1  )THEN      INCLUDE ENDIF
                showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"annen_forld_rett_bt").greaterThan(1))){
                    //[TBU2607EN, TBU2607, TBU2607_NN]

                    paragraph {
                        text (
                            bokmal { + "<FRITEKST: slett det som ikke er aktuelt>" },
                            nynorsk { + "<Fritekst: slett det som ikke er aktuelt>" },
                        )
                    }
                }

                //IF( Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "annen_forld_rett_bt") > 1  )THEN      INCLUDE ENDIF
                showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"annen_forld_rett_bt").greaterThan(1))){
                    //[TBU3921_EN, TBU3921, TBU3921_NN]

                    paragraph {
                        text (
                            bokmal { + "Når " + pe.ut_barnet_barna_opphor() + " blir forsørget av begge foreldrene og begge mottar uføretrygd, skal barnetillegget gis til den som får det høyeste tillegget. " + pe.ut_barnet_barna_opphor_stor_forbokstav() + "s andre forelder har rett til et høyere barnetillegg enn det du vil få. Vi har derfor opphørt barnetillegget i uføretrygden din. " },
                            nynorsk { + "Når " + pe.ut_barnet_barna_opphor() + " blir " + pe.ut_barnet_barna_opphor_forsørga_forsørgde() + " av begge foreldra og begge får uføretrygd, blir barnetillegget gitt til den som får det høgaste tillegget. Den andre forelderen har rett til eit høgare barnetillegg enn det du vil få. Vi har derfor stansa barnetillegget i uføretrygda di." },
                        )
                    }
                }

                //IF( Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "annen_forld_rett_bt") > 1  )THEN      INCLUDE ENDIF
                showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"annen_forld_rett_bt").greaterThan(1))){
                    //[TBU3922_EN, TBU3922, TBU3922_NN]

                    paragraph {
                        text (
                            bokmal { + "Når " + pe.ut_barnet_barna_opphor() + " blir forsørget av foreldre som ikke bor sammen, blir barnetillegget gitt til den som har samme folkeregistrerte adresse som " + pe.ut_barnet_barna_opphor() + ". Du bor ikke på samme folkeregistrerte adresse som " + pe.ut_barnet_barna_opphor() + ". Vi har derfor opphørt barnetillegget i uføretrygden din." },
                            nynorsk { + "Når " + pe.ut_barnet_barna_opphor() + " blir " + pe.ut_barnet_barna_opphor_forsørga_forsørgde() + " av foreldre som ikkje bur saman, blir barnetillegget gitt til den som har same folkeregistrerte adresse som " + pe.ut_barnet_barna_opphor() + ". Du bur ikkje på same folkeregistrerte adresse som " + pe.ut_barnet_barna_opphor() + ". Vi har derfor stansa barnetillegget i uføretrygda di." },
                        )
                    }
                }

                //IF( Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "mindre_ett_ar_bt_flt") > 1 )  THEN INCLUDE ENDIF
                showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"mindre_ett_ar_bt_flt").greaterThan(1))){
                    //[TBU3925_EN, TBU3925, TBU3925_NN]

                    paragraph {
                        text (
                            bokmal { + "Det er mulig å flytte barnetillegget fra den ene til den andre forelderen. Det må imidlertid ha gått et år siden barnetillegget ble overført. I ditt tilfelle har det gått ett år og barnetillegget er overført til den andre forelderen. Vi har derfor opphørt barnetillegget i uføretrygden din." },
                            nynorsk { + "Det er mogleg å flytte barnetillegget frå den eine til den andre forelderen. Det må i det minste ha gått eit år sidan barnetillegget blei overført. I ditt tilfelle har det gått eit år og barnetillegget er overført til den andre forelderen. Vi har derfor stansa barnetillegget i uføretrygda di." },
                        )
                    }
                }

                //IF( Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bt_innt_over_1g") > 1  )THEN      INCLUDE ENDIF
                showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"bt_innt_over_1g").greaterThan(1))){
                    //[TBU3923_EN, TBU3923, TBU3923_NN]

                    paragraph {
                        text (
                            bokmal { + "Når " + pe.ut_barnet_barna_opphor() + " har inntekt over folketrygdens grunnbeløp på " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner, har du ikke rett til barnetillegg. " },
                            nynorsk { + "Når " + pe.ut_barnet_barna_opphor() + " har inntekt over grunnbeløpet i folketrygda på " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner, har du ikkje rett til barnetillegg." },
                        )
                        text (
                            bokmal { + "Det er opplyst at " + pe.ut_barnet_barna_opphor() + " " + pe.ut_barnet_barna_opphor_dit_dine() + " har inntekt over " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner. Vi har derfor opphørt barnetillegget i uføretrygden din." },
                            nynorsk { + "Det er opplyst at " + pe.ut_barnet_barna_opphor() + " " + pe.ut_barnet_barna_opphor_dit_dine() + " har inntekt over " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner. Vi har derfor stansa barnetillegget i uføretrygda di." },
                        )
                    }
                }

                //IF(PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor)) AND (Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bruker_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "barn_flyttet_ikke_avt_land") > 0 OR Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "barn_opph_ikke_avt_land") > 0 ) THEN      INCLUDE ENDIF
                showIf((FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_bt(),pe.ut_konst_vilkarsvedtakresultat_opphor())) and (FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"bruker_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"barn_flyttet_ikke_avt_land").greaterThan(0) or FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"barn_opph_ikke_avt_land").greaterThan(0))){

                    //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "barn_flyttet_ikke_avt_land") > 0) THEN      INCLUDE ENDIF
                    showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"barn_flyttet_ikke_avt_land").greaterThan(0))){
                        //[TBU5009_EN, TBU5009, TBU5009_NN]

                        paragraph {
                            text (
                                bokmal { + "Ifølge våre opplysninger er " + pe.ut_dine_ditt_barn_opphor() + " bosatt i <Fritekst: bostedsland>.  Derfor har du ikke lenger rett til barnetillegg" },
                                nynorsk { + "Ifølgje våre opplysningar er " + pe.ut_dine_ditt_barn_opphor() + " busett i <Fritekst: bostedsland>. Derfor har du ikkje lenger rett til barnetillegg." },
                            )
                        }
                    }

                    //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "barn_opph_ikke_avt_land") > 0) THEN      INCLUDE ENDIF
                    showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"barn_opph_ikke_avt_land").greaterThan(0))){
                        //[TBU5010_EN, TBU5010, TBU5010_NN]

                        paragraph {
                            text (
                                bokmal { + "Ifølge våre opplysninger er " + pe.ut_dine_ditt_barn_opphor() + " bosatt i <Fritekst: bostedsland>.  Derfor har du ikke lenger rett til barnetillegg." },
                                nynorsk { + "Ifølgje våre opplysningar er " + pe.ut_dine_ditt_barn_opphor() + " busett i <Fritekst: bostedsland>. Derfor har du ikkje lenger rett til barnetillegg." },
                            )
                        }
                    }

                    //IF(Contains(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, "bruker_flyttet_ikke_avt_land") > 0) THEN      INCLUDE ENDIF
                    showIf((FUNKSJON_Contains(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_begrunnelse(),"bruker_flyttet_ikke_avt_land").greaterThan(0))){
                        //[TBU5011_EN, TBU5011, TBU5011_NN]

                        paragraph {
                            text (
                                bokmal { + "Ifølge våre opplysninger er du bosatt i <Fritekst: bostedsland>. Derfor har du ikke lenger rett til barnetillegg." },
                                nynorsk { + "Ifølgje våre opplysningar er du busett i <Fritekst: bostedsland>. Da har du ikkje lenger rett til barnetillegg." },
                            )
                        }
                    }

                    //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor) ) THEN      INCLUDE ENDIF
                    showIf((FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_bt(),pe.ut_konst_vilkarsvedtakresultat_opphor()))){
                        //[TBU5006_EN, TBU5006, TBU5006_NN]

                        paragraph {
                            text (
                                bokmal { + "For å ha rett til barnetillegg fra 1. juli 2020" },
                                nynorsk { + "For å ha rett til forsørgingstillegg frå 1. juli 2020 " },
                            )
                            text (
                                bokmal { + "må du enten bo i Norge, innenfor EØS-området eller i et annet land Norge har trygdeavtale med" },
                                nynorsk { + "må du enten bu i Noreg, innanfor EØS-området eller i eit anna land Noreg har trygdeavtale med" },
                            )
                            text (
                                bokmal { + "må også barnet være bosatt og oppholde seg i Norge, innenfor EØS-området eller et annet land Norge har trygdeavtale med " },
                                nynorsk { + "må også barn vere busett og opphalde seg i Noreg, innanfor EØS-området eller eit anna land Noreg har trygdeavtale med " },
                            )
                            text (
                                bokmal { + "Dette går frem av folketrygdloven §12-15 som gjelder fra 1.juli 2020." },
                                nynorsk { + "Dette går fram av folketrygdlova § 12-15 som gjeld frå 1. juli 2020" },
                            )
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = false AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) = "false" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "sivilstandsendring") THEN      INCLUDE ENDIF
                    showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_belopokt()) and not(pe.vedtaksdata_beregningsdata_beregningufore_belopredusert()) and FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1).equalTo("false") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("sivilstandsendring"))){
                        includePhrase(TBU2222_Generated)
                    }

                    //IF(PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes_Er_Ulik(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor)) THEN     INCLUDE ENDIF
                    showIf((FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes_Er_Ulik(pe.ut_konst_kralinjekode_bt(),pe.ut_konst_vilkarsvedtakresultat_opphor()))){
                        //[TBU2335EN, TBU2335, TBU2335NN]

                        paragraph {

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())){
                                text (
                                    bokmal { + "Barnetillegg kan gis så lenge du forsørger barn. Det gis som et tillegg til uføretrygden din og opphører når barnet fyller 18 år. " },
                                    nynorsk { + "Barnetillegg kan bli gitt så lenge du forsørgjer barn. Det blir gitt som eit tillegg til uføretrygda di og blir stansa når barnet ditt fyller 18 år. " },
                                )
                            }

                            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) AND PE_Vedtaksdata_Kravhode_KravArsakType = "soknad_bt" ) THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_kravhode_kravarsaktype().equalTo("soknad_bt"))){
                                text (
                                    bokmal { + "Du er innvilget barnetillegg fordi du forsørger barn." },
                                    nynorsk { + "Du er innvilga barnetillegg fordi du forsørgjer barn." },
                                )
                            }
                        }
                    }

                    //IF(  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND (PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "barn_endret_inntekt" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "annen_for_end_in" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "begge_for_end_in")  ) THEN      INCLUDE ENDIF
                    showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and (pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("barn_endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("annen_for_end_in") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("begge_for_end_in")))){
                        //[TBU2336EN, TBU2336, TBU2336NN]

                        paragraph {
                            text (
                                bokmal { + "Vi har mottatt opplysninger om at inntekten din er endret. Barnetillegget er derfor beregnet på nytt." },
                                nynorsk { + "Vi har fått opplysningar om at inntekta di er endra. Barnetillegg er derfor berekna på nytt." },
                            )
                        }
                    }

                    //IF(  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND (PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "barn_endret_inntekt" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "annen_for_end_in" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "begge_for_end_in" OR  PE_Vedtaksdata_Kravhode_KravArsakType  = "eps_endret_inntekt")  ) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("barn_endret_inntekt") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("annen_for_end_in") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("begge_for_end_in") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("eps_endret_inntekt")))){
                        //[TBU2337EN, TBU2337, TBU2337NN]

                        paragraph {
                            text (
                                bokmal { + "Vi har mottatt opplysninger om at inntekten deres er endret. Barnetillegget er derfor beregnet på nytt." },
                                nynorsk { + "Vi har fått opplysningar om at inntekta dykkar er endra. Barnetillegg er derfor berekna på nytt." },
                            )
                        }
                    }

                    //PE_Vedtaksdata_Kravhode_KravArsakType = "omgj_etter_klage"
                    showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("omgj_etter_klage")){
                        includePhrase(TBU1136_Generated)
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        includePhrase(TBU3103_Generated)
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  OR  PE_UT_Forsorgeransvar_ingen_er_false() OR  PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = true)) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() or pe.ut_forsorgeransvar_ingen_er_false() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter()))){
                        //[TBU3104]
                        paragraph {
                            text (
                                bokmal { + "Vi reduserer ikke utbetalingen av uføretrygden din verken i innleggelsesmåneden eller de tre påfølgende månedene når du er innlagt på institusjon. Uføretrygden din blir deretter redusert og skal under oppholdet utgjøre 14 prosent av uføretrygden inntil institusjonsoppholdet avsluttes. Totalt sett skal utbetalingen likevel utgjøre minst 45 prosent av folketrygdens grunnbeløp, før skatt." },
                                nynorsk { + "Vi reduserer ikkje utbetalinga av uføretrygda di verken i innleggingsmånaden eller dei tre påfølgjande månadene når du er innlagd på institusjon. Uføretrygda di blir deretter redusert og skal under opphaldet utgjere 14 prosent av uføretrygda inntil institusjonsopphaldet blir avslutta. Totalt sett skal utbetalinga likevel utgjere minst 45 prosent av grunnbeløpet i folketrygda før skatt. " },
                            )

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() or pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())){
                                text (
                                    bokmal { + " Dersom du mottar " },
                                    nynorsk { + "Dersom du får" },
                                )
                            }

                            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()){
                                text (
                                    bokmal { + "ektefelletillegg" },
                                    nynorsk { + "ektefelletillegg" },
                                )
                            }

                            //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                            showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()){
                                text (
                                    bokmal { + "gjenlevendetillegg" },
                                    nynorsk { + "attlevandetillegg" },
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() or pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())){
                                text (
                                    bokmal { + " vil dette tillegget også bli redusert." },
                                    nynorsk { + "vil dette tillegget også bli redusert." },
                                )
                            }
                        }
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  OR  PE_UT_Forsorgeransvar_ingen_er_false() OR  PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = true)) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() or pe.ut_forsorgeransvar_ingen_er_false() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter()))){
                        includePhrase(TBU3105_Generated(pe))
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false AND PE_UT_Forsorgeransvar_ingen_er_true() AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
                    showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.ut_forsorgeransvar_ingen_er_true() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                        includePhrase(TBU3106_Generated)
                    }

                    //IF(PE_UT_Forsorgeransvar_ingen_er_false() AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0) THEN      INCLUDE ENDIF
                    showIf((pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0))){
                        includePhrase(TBU3107_Generated(pe))
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND PE_UT_Forsorgeransvar_ingen_er_true()) THEN      INCLUDE ENDIF
                    showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.ut_forsorgeransvar_ingen_er_true())){
                        includePhrase(TBU3108_Generated)
                    }

                    //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0 AND PE_UT_Forsorgeransvar_ingen_er_false() AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                        includePhrase(TBU3109_Generated(pe))
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                        includePhrase(TBU3110_Generated(pe))
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0  AND PE_UT_Forsorgeransvar_ingen_er_true()) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0) and pe.ut_forsorgeransvar_ingen_er_true())){
                        includePhrase(TBU3112_Generated(pe))
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        includePhrase(TBU3114_Generated)
                    }

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"
                    showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo")){
                        //[TBU3115]
                        paragraph {
                            text (
                                bokmal { + "Uføretrygden din er redusert fordi du er under straffegjennomføring." },
                                nynorsk { + "Uføretrygda di er redusert fordi du er under straffegjennomføring." },
                            )

                            //IF(FF_GetArrayElement_Boolean(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphReduksjonsperiodeListe_InstOpphReduksjonsperiode_Forsorgeransvar) = true) THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphreduksjonsperiodeliste_instopphreduksjonsperiode_forsorgeransvar()))){
                                text (
                                    bokmal { + " " },
                                    nynorsk { + " " },
                                )
                            }

                            //IF(PE_UT_Forsorgeransvar_siste_er_true()) THEN      INCLUDE ENDIF
                            showIf((pe.ut_forsorgeransvar_siste_er_true())){
                                text (
                                    bokmal { + "Da du forsørger barn" },
                                    nynorsk { + " Da du forsørgjer barn" },
                                )
                            }

                            //IF(FF_GetArrayElement_Boolean(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphReduksjonsperiodeListe_InstOpphReduksjonsperiode_Forsorgeransvar) = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true) THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphreduksjonsperiodeliste_instopphreduksjonsperiode_forsorgeransvar()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget())){
                                text (
                                    bokmal { + " og/eller ektefelle" },
                                    nynorsk { + " og/eller ektefelle" },
                                )
                            }

                            //IF(PE_UT_Forsorgeransvar_siste_er_true()) THEN      INCLUDE ENDIF
                            showIf((pe.ut_forsorgeransvar_siste_er_true())){
                                text (
                                    bokmal { + ", vil utbetaling av uføretrygden din reduseres med 50 prosent. " },
                                    nynorsk { + ", vil utbetalinga av uføretrygda di reduserast med 50 prosent. " },
                                )
                            }
                            text (
                                bokmal { + "Utbetalingen din er redusert fra andre måned etter at straffegjennomføring tok til. Når straffegjennomføring er avsluttet, vil vi ikke lenger redusere uføretrygden din. " },
                                nynorsk { + " Utbetalinga di er redusert frå den andre månaden etter at straffegjennomføringa tok til. Når straffegjennomføringa er avslutta, vil vi ikkje lenger redusere uføretrygda di. " },
                            )

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() or pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())){
                                text (
                                    bokmal { + "Dersom du mottar " },
                                    nynorsk { + "Dersom du mottar " },
                                )
                            }

                            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()){
                                text (
                                    bokmal { + "ektefelletillegg" },
                                    nynorsk { + "ektefelletillegg" },
                                )
                            }

                            //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                            showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()){
                                text (
                                    bokmal { + "gjenlevendetillegg" },
                                    nynorsk { + "attlevandetillegg" },
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() or pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())){
                                text (
                                    bokmal { + " vil dette tillegget også bli redusert." },
                                    nynorsk { + " vil dette tillegget også bli redusert." },
                                )
                            }
                        }
                    }

                    //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "instopphold" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("instopphold") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo(""))){
                        //[TBU3364EN, TBU3364, TBU3364NN]

                        paragraph {
                            text (
                                bokmal { + "Du er ikke lenger <FRITEKST: innlagt på institusjon/under straffegjennomføring>, og du får derfor tilbake utbetalingen av uføretrygden." },
                                nynorsk { + "Du er ikkje lenger <Fritekst: innlagt på institusjon/under straffegjennomføring>, og du får derfor tilbake utbetalinga av uføretrygda." },
                            )
                        }
                    }

                    //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_et, PE_UT_KONST_VilkarsVedtakResultat_opphor)) THEN      INCLUDE ENDIF
                    showIf((FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_et(),pe.ut_konst_vilkarsvedtakresultat_opphor()))){
                        //[TBU2500EN, TBU2500, TBU2500NN]

                        paragraph {
                            text (
                                bokmal { + "Vedtaket er gjort etter forskrift om overgangsregler ved innføringen av uføretrygd § 8." },
                                nynorsk { + "Vedtaket er gjort etter forskrift om overgangsreglar ved innføringa av uføretrygd § 8." },
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
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }
                            text (
                                bokmal { + "." },
                                nynorsk { + "." },
                            )
                        }
                    }

                    //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo" AND PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes_Er_Ulik(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor)  ) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo") and FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes_Er_Ulik(pe.ut_konst_kralinjekode_bt(),pe.ut_konst_vilkarsvedtakresultat_opphor()))){
                        //[TBU2343]
                        paragraph {
                            text (
                                bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-16 og " },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-16 og " },
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }

                            //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                            showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                                text (
                                    bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                                    nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                                )
                            }
                            text (
                                bokmal { + "." },
                                nynorsk { + "." },
                            )
                        }
                    }

                    //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo"  ) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))){
                        //[TBU2344]
                        paragraph {
                            text (
                                bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-17 og " },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-17 og " },
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }

                            //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                            showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                                text (
                                    bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                                    nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                                )
                            }
                            text (
                                bokmal { + "." },
                                nynorsk { + "." },
                            )
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo") THEN      INCLUDE ENDIF
                    showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))){
                        //[TBU2345EN, TBU2345, TBU2345NN]

                        paragraph {
                            text (
                                bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-14, 12-17 og " },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-14, 12-17 og " },
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }
                            text (
                                bokmal { + "." },
                                nynorsk { + "." },
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
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }
                            text (
                                bokmal { + "." },
                                nynorsk { + "." },
                            )
                        }
                    }

                    //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo"  ) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))){
                        //[TBU2347]
                        paragraph {
                            text (
                                bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-16, 12-18 og " },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-16, 12-18 og " },
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }

                            //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                            showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                                text (
                                    bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                                    nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                                )
                            }
                            text (
                                bokmal { + "." },
                                nynorsk { + "." },
                            )
                        }
                    }

                    //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo"  ) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))){
                        //[TBU2348]
                        paragraph {
                            text (
                                bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-18 og " },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-18 og " },
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }

                            //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                            showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                                text (
                                    bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                                    nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                                )
                            }
                            text (
                                bokmal { + "." },
                                nynorsk { + "." },
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
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13 " },
                                )
                            }
                            text (
                                bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden." },
                                nynorsk { + "og forskrift om overgangsreglar for barnetillegg i uføretrygda." },
                            )
                        }
                    }

                    //IF(  PE_UT_KravLinjeKode_Og_PaaFolgende_bt_ikkeInnv() AND ( FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5" ) AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo" AND PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor)  ) THEN      INCLUDE ENDIF
                    showIf((pe.ut_kravlinjekode_og_paafolgende_bt_ikkeinnv() and ((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5")) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo") and FUNKSJON_PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(pe.ut_konst_kralinjekode_bt(),pe.ut_konst_vilkarsvedtakresultat_opphor()))){
                        //[TBU2350EN, TBU2350, TBU2350NN]

                        paragraph {
                            text (
                                bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-15 og 22-12." },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-15 og 22-12." },
                            )
                        }
                    }

                    //IF( PE_UT_KravLinjeKode_Og_PaaFolgende_ut_gjt_ikkeInnv() AND ( FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5" ) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo"  ) THEN      INCLUDE ENDIF
                    showIf((pe.ut_kravlinjekode_og_paafolgende_ut_gjt_ikkeinnv() and ((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5")) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo"))){
                        //[TBU2351EN, TBU2351, TBU2351NN]

                        paragraph {
                            text (
                                bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-18 og 22-12." },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-18 og 22-12." },
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
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }
                            text (
                                bokmal { + "." },
                                nynorsk { + "." },
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
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }
                            text (
                                bokmal { + "." },
                                nynorsk { + "." },
                            )
                        }
                    }

                    //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  ) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                        //[TBU3354]
                        paragraph {
                            text (
                                bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-16, 12-19 og " },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-16, 12-19 og " },
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }

                            //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                            showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                                text (
                                    bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                                    nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                                )
                            }
                            text (
                                bokmal { + "." },
                                nynorsk { + "." },
                            )
                        }
                    }

                    //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  ) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                        //[TBU3355]
                        paragraph {
                            text (
                                bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-17, 12-19 og " },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-17, 12-19 og " },
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }

                            //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                            showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                                text (
                                    bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                                    nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                                )
                            }
                            text (
                                bokmal { + "." },
                                nynorsk { + "." },
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
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }
                            text (
                                bokmal { + "." },
                                nynorsk { + "." },
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
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }
                            text (
                                bokmal { + "." },
                                nynorsk { + "." },
                            )
                        }
                    }

                    //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  ) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                        //[TBU3358]
                        paragraph {
                            text (
                                bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-16, 12-20 og " },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-16, 12-20 og " },
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }

                            //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                            showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                                text (
                                    bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                                    nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                                )
                            }
                            text (
                                bokmal { + "." },
                                nynorsk { + "." },
                            )
                        }
                    }

                    //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  ) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                        //[TBU3359]
                        paragraph {
                            text (
                                bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-17, 12-20 og " },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-17, 12-20 og " },
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }

                            //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                            showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                                text (
                                    bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden." },
                                    nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                                )
                            }
                            text (
                                bokmal { + " " },
                                nynorsk { + " " },
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
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }
                            text (
                                bokmal { + "." },
                                nynorsk { + "." },
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
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }
                            text (
                                bokmal { + "." },
                                nynorsk { + ". " },
                            )
                        }
                    }

                    //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  ) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                        //[TBU3712]
                        paragraph {
                            text (
                                bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-16, 12-18, 12-19 og " },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-16, 12-18, 12-19 og " },
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }

                            //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                            showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                                text (
                                    bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                                    nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                                )
                            }
                            text (
                                bokmal { + ". " },
                                nynorsk { + ". " },
                            )
                        }
                    }

                    //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  ) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                        //[TBU3713]
                        paragraph {
                            text (
                                bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-19 og " },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-19 og " },
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }

                            //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                            showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                                text (
                                    bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                                    nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                                )
                            }
                            text (
                                bokmal { + ". " },
                                nynorsk { + ". " },
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
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }
                            text (
                                bokmal { + "." },
                                nynorsk { + "." },
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
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }
                            text (
                                bokmal { + "." },
                                nynorsk { + "." },
                            )
                        }
                    }

                    //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  ) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                        //[TBU3716]
                        paragraph {
                            text (
                                bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-16, 12-18, 12-20 og " },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-16, 12-18, 12-20 og " },
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }

                            //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                            showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                                text (
                                    bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                                    nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                                )
                            }

                            //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = ""overgangsregler_2016""
                            showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("")){
                                text (
                                    bokmal { + "." },
                                    nynorsk { + "." },
                                )
                            }
                        }
                    }

                    //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  ) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                        //[TBU3717]
                        paragraph {
                            text (
                                bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-8 til 12-18, 12-20 og " },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-8 til 12-18, 12-20 og " },
                            )

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-12" },
                                    nynorsk { + "22-12" },
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                                text (
                                    bokmal { + "22-13" },
                                    nynorsk { + "22-13" },
                                )
                            }

                            //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                            showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                                text (
                                    bokmal { + " og forskrift om overgangsregler for barnetillegg i uføretrygden" },
                                    nynorsk { + " og forskrift om overgangsreglar for barnetillegg i uføretrygda" },
                                )
                            }
                            text (
                                bokmal { + "." },
                                nynorsk { + "." },
                            )
                        }
                    }
                    includePhrase(TBU1174_Generated)

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "") THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo(""))){
                        //[TBU3361EN, TBU3361, TBU3361NN]

                        paragraph {
                            text (
                                bokmal { + "Uføretrygden din er endret fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + ". Dette kaller vi virkningstidspunktet. <Fritekst: Konkret begrunnelse for fastsatt virkningstidspunkt>." },
                                nynorsk { + "Uføretrygda di er endra frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + ". Dette kallar vi verknadstidspunktet. <Fritekst: Konkret begrunnelse for fastsatt virkningstidspunkt>." },
                            )
                        }
                    }

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneResultat) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_1") THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevneresultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_1"))){
                        includePhrase(TBU1175_Generated(pe))
                    }

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneResultat) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_2") THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevneresultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_2"))){
                        includePhrase(TBU1176_Generated(pe))
                    }

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_3") THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_3"))){
                        includePhrase(TBU1177_Generated(pe))
                    }

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_4") THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_4"))){
                        includePhrase(TBU1178_Generated(pe))
                    }

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                        includePhrase(TBU1179_Generated(pe))
                    }

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_12") THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_12"))){
                        //[TBU2528EN, TBU2528, TBU2528NN]

                        paragraph {
                            text (
                                bokmal { + "Uføretrygden din er endret fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + ". Dette kaller vi virkningstidspunktet. Du vil derfor få ny utbetaling fra og med måneden etter den måneden vilkårene er oppfylt." },
                                nynorsk { + "Uføretrygda di er endra frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + ". Dette kallar vi verknadstidspunktet. Du vil derfor få ny utbetaling frå og med månaden etter den månaden vilkåra er oppfylde." },
                            )
                        }
                    }

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_11") THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_11"))){
                        //[TBU2529EN, TBU2529, TBU2529NN]

                        paragraph {
                            text (
                                bokmal { + "Uføretrygden din er endret fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + ". Dette kaller vi virkningstidspunktet. Du vil derfor få ny utbetaling fra og med måneden vilkårene er oppfylt. " },
                                nynorsk { + "Uføretrygda di er endra frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + ". Dette kallar vi verknadstidspunktet. Du vil derfor få ny utbetaling frå og med månaden vilkåra er oppfylde." },
                            )
                        }
                    }

                    //IF(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse(1) = "stdbegr_22_12_1_14") THEN      INCLUDE ENDIF
                    showIf((FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse(1).equalTo("stdbegr_22_12_1_14"))){
                        //[TBU3024EN, TBU3024, TBU3024NN]

                        paragraph {
                            text (
                                bokmal { + "Virkningstidspunkt for opphør er satt til måneden vilkårene ikke lenger er oppfylt." },
                                nynorsk { + "Verknadstidspunktet for stans er satt til månaden vilkåra ikkje lenger er oppfylde." },
                            )
                        }
                    }

                    //IF(PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_ys") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_kravhode_kravgjelder().notEqualTo("sok_ys"))){

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "") THEN      INCLUDE ENDIF
                        showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("") or (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo(""))){
                            includePhrase(TBU2038_Generated)
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "") THEN      INCLUDE ENDIF
                        showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("") or (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo(""))){
                            includePhrase(TBU1289_Generated)
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_1" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "stdbegr_12_8_1_3") THEN      INCLUDE ENDIF
                        showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_1") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("stdbegr_12_8_1_3"))){
                            includePhrase(TBU1187_Generated(pe))
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "stdbegr_12_8_1_3") THEN      INCLUDE ENDIF
                        showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_3") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("stdbegr_12_8_1_3"))){
                            includePhrase(TBU1194_Generated(pe))
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse,1) = "stdbegr_12_8_2_5" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse,1) <> "stdbegr_12_8_1_3") THEN      INCLUDE ENDIF
                        showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse(),1).equalTo("stdbegr_12_8_2_5") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse(),1).notEqualTo("stdbegr_12_8_1_3"))){
                            includePhrase(TBU3022_Generated(pe))
                        }

                        //IF((FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat,1) = "oppfylt") AND (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse,1) = "stdbegr_12_8_2_4")  AND (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse,1) <> "stdbegr_12_8_1_3"))  THEN      INCLUDE ENDIF
                        showIf((((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat(),1).equalTo("oppfylt")) and ((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse(),1).equalTo("stdbegr_12_8_2_4")) and ((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse(),1).notEqualTo("stdbegr_12_8_1_3")))){
                            includePhrase(TBU2354_Generated(pe))
                        }

                        //IF((FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "") AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100  AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt) = 0  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "stdbegr_12_8_1_3"  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "stdbegr_12_8_2_2"  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "stdbegr_12_8_2_9"  ) THEN      INCLUDE ENDIF
                        showIf((((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo("") or (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("")) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().equalTo(100) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()).equalTo(0) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("stdbegr_12_8_1_3") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo("stdbegr_12_8_2_2") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo("stdbegr_12_8_2_9"))){
                            includePhrase(TBU1188_Generated(pe))
                        }

                        //IF( ( FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "" ) AND ((PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100) OR ( PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100 AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt) > 0 )) AND  FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "stdbegr_12_8_1_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "stdbegr_12_8_2_2"  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "stdbegr_12_8_2_9" ) THEN      INCLUDE ENDIF
                        showIf((((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo("") or (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("")) and ((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().lessThan(100)) or (pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().equalTo(100) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()).greaterThan(0))) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("stdbegr_12_8_1_3") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo("stdbegr_12_8_2_2") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo("stdbegr_12_8_2_9"))){
                            includePhrase(TBU1189_Generated(pe))
                        }

                        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_9" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_2") THEN      INCLUDE ENDIF
                        showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_9") or (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_2"))){
                            includePhrase(TBU1198_Generated(pe))
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = 100 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().equalTo(100) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(100) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        includePhrase(TBU1201_Generated)
                    }

                    //IF(  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0) OR (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad)) AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold"  ) THEN      INCLUDE ENDIF
                    showIf((((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().lessThan(100) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().greaterThan(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()))) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        includePhrase(TBU1203_Generated)
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        includePhrase(TBU1204_Generated)
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        includePhrase(TBU2251_Generated)
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> 60000 AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt(1) = 0 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().equalTo(100) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(60000) and FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt(1).equalTo(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        includePhrase(TBU1205_Generated(pe))
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense = 60000 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().equalTo(100) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().equalTo(60000) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        includePhrase(TBU1296_Generated)
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        includePhrase(TBU1206_Generated(pe))
                    }

                    //IF(  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> 60000 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop  AND  (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0)  OR  (FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt) > 0 AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100)  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold" ) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(60000) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()) and (pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().lessThan(100) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().greaterThan(0)) or ((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()).greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().equalTo(100)) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        includePhrase(TBU1207_Generated(pe))
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense = 60000 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().equalTo(60000) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().lessThan(100) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        includePhrase(TBU2357_Generated(pe))
                    }

                    //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        //[TBU1208_1]
                        paragraph {
                            text (
                                bokmal { + "Vi bruker en fastsatt prosentandel når vi justerer uføretrygden din ut fra inntekt. Denne prosentandelen kaller vi kompensasjonsgrad." },
                                nynorsk { + "Vi bruker ein fastsett prosentdel når vi justerer uføretrygda di ut frå inntekt. Denne prosentdelen kallar vi kompensasjonsgrad. " },
                            )
                            text (
                                bokmal { + "For deg utgjør kompensasjonsgraden " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " prosent. Det er bare den delen av inntekten din som overstiger " + pe.ut_inntektsgrense_faktisk().format() + " kroner, som vi justerer uføretrygden din ut fra. Det betyr at et beløp som tilsvarer " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " prosent av den inntekten du har over " + pe.ut_inntektsgrense_faktisk().format() + " kroner trekkes fra uføretrygden din." },
                                nynorsk { + "For deg utgjer kompensasjonsgraden " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " prosent. Det er berre den delen av inntekta di som overstig " + pe.ut_inntektsgrense_faktisk().format() + " kroner, som vi justerer uføretrygda di ut frå. Det betyr at eit beløp som svarer til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " prosent av inntekta du har over " + pe.ut_inntektsgrense_faktisk().format() + " kroner blir trekt frå uføretrygda di." },
                            )

                            //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                            showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()){
                                text (
                                    bokmal { + " " },
                                    nynorsk { + " " },
                                )
                            }

                            //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                            showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()){
                                text (
                                    bokmal { + "Tjener du mer enn inntektsgrensen din, reduserer vi gjenlevendetillegget ditt med samme prosent som vi reduserer uføretrygden din med." },
                                    nynorsk { + "Tener du meir enn inntektsgrensa di, reduserer vi attlevandetillegget ditt med same prosent som vi reduserer uføretrygda di med." },
                                )
                            }
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt().greaterThan(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        includePhrase(TBU2361_Generated(pe))
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_beregningsdata_beregningufore_belopredusert() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        includePhrase(TBU2362_Generated)
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_beregningsdata_beregningufore_belopokt() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        includePhrase(TBU2363_Generated)
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        //[TBU2261]
                        paragraph {
                            text (
                                bokmal { + "Ut fra den årlige inntekten din vil uføretrygden utgjøre " + pe.ut_nettoakk_pluss_nettorestar().format() + " kroner." },
                                nynorsk { + "På bakgrunn av den innmelde inntekta di utgjer uføretrygda di " + pe.ut_nettoakk_pluss_nettorestar().format() + " kroner." },
                            )

                            //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = false) THEN      INCLUDE ENDIF
                            showIf((not(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(pe.vedtaksdata_virkningfom())))){
                                text (
                                    bokmal { + " Hittil i år har du fått utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettoakk().format() + " kroner." },
                                    nynorsk { + " Hittil i år har du fått utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettoakk().format() + " kroner." },
                                )
                            }
                            text (
                                bokmal { + " Du har derfor rett til en utbetaling av uføretrygd på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_netto().format() + " kroner per måned for resten av året." },
                                nynorsk { + " Du har derfor rett til ei utbetaling av uføretrygd på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_netto().format() + " kroner per månad for resten av kalenderåret." },
                            )
                        }
                    }

                    //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        includePhrase(TBU1210_Generated(pe))
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        includePhrase(TBU2364_Generated)
                        includePhrase(TBU2365_Generated)
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        includePhrase(TBU2366_Generated)
                        includePhrase(TBU2367_Generated)
                        includePhrase(TBU2279_Generated(pe))
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        includePhrase(TBU2280_Generated(pe))
                    }

                    //IF( ( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true ) AND PE_Vedtaksdata_Kravhode_SokerBT = true ) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_kravhode_sokerbt())){
                        includePhrase(TBU3800_Generated)
                    }

                    //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) AND PE_Vedtaksdata_Kravhode_SokerBT = true AND ((PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB) OR (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT <> 0 ) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_kravhode_sokerbt() and ((pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggfellesyk_belopgammelbtfb().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggfellesyk_belopnybtfb())) or (pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggserkullyk_belopgammelbtsb().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggserkullyk_belopnybtsb()))) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().notEqualTo(0))){
                        includePhrase(TBU2379mFRI_Generated)
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0) THEN      EXCLUDE ENDIF
                    showIf(not((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0)))){

                        //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)   ) THEN      INCLUDE ENDIF
                        showIf(((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))){
                            includePhrase(TBU2338_Generated(pe))
                        }

                        //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                        showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()){
                            includePhrase(TBU2339_Generated(pe))
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())){
                            //[TBU3801]
                            paragraph {
                                text (
                                    bokmal { + "Endringer i " },
                                    nynorsk { + "Endringar i " },
                                )

                                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()){
                                    text (
                                        bokmal { + "inntektene til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_alle_spraak_entall() + " din " },
                                        nynorsk { + "inntektene til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din " },
                                    )
                                }

                                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                                showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())){
                                    text (
                                        bokmal { + "inntekten din " },
                                        nynorsk { + "inntekta di " },
                                    )
                                }
                                text (
                                    bokmal { + "kan ha betydning for barnetillegget ditt. Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» på $NAV_URL." },
                                    nynorsk { + "kan ha betydning for barnetillegget ditt. Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» på $NAV_URL." },
                                )
                            }
                        }

                        //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  ) THEN      INCLUDE ENDIF
                        showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()))){
                            //[TBU1284]
                            paragraph {
                                text (
                                    bokmal { + "Inntekten din er " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning().format() + " kroner og inntekten til " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_alle_spraak_entall() + " din er " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format() + " kroner. " },
                                    nynorsk { + "Inntekta di er " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning().format() + " kroner, og inntekta til " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_alle_spraak_entall() + " din er " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format() + " kroner. " },
                                )

                                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbelopFratrukketAnnenForeldersInntekt > 0) THEN      INCLUDE ENDIF
                                showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt().greaterThan(0))){
                                    text (
                                        bokmal { + "Folketrygdens grunnbeløp på inntil " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner er holdt utenfor inntekten til " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_alle_spraak_entall() + " din. " },
                                        nynorsk { + "Grunnbeløpet i folketrygda på inntil " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner er halde utanfor inntekta til " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_alle_spraak_entall() + " din. "}
                                    )
                                }

                                //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 )) THEN      INCLUDE ENDIF
                                showIf(((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0)))){
                                    text (
                                        bokmal { + "Til sammen er inntektene " + pe.ut_fradrag_høyere_lavere() + " enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                        nynorsk { + "Til saman er inntektene " + pe.ut_fradrag_høyere_lavere() + " enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                    )
                                }

                                //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0 )) THEN      INCLUDE ENDIF
                                showIf(((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)))){
                                    text (
                                        bokmal { + "ikke " },
                                        nynorsk { + "ikkje " },
                                    )
                                }

                                //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 )) THEN      INCLUDE ENDIF
                                showIf(((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0)))){
                                    text (
                                        bokmal { + "redusert. " },
                                        nynorsk { + "redusert. " },
                                    )
                                }

                                //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false ))THEN     INCLUDE ENDIF
                                showIf(((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())))){
                                    text (
                                        bokmal { + "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. " },
                                        nynorsk { + "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. " },
                                    )
                                }

                                //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto  = 0 ))THEN     INCLUDE ENDIF
                                showIf(((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0)))){
                                    text (
                                        bokmal { + "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                        nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                                    )
                                }
                            }
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()))){
                            //[TBU1285]
                            paragraph {

                                //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_UT_BTSBInnvilget_Ikke_BTFBInnvilget() )) THEN      INCLUDE ENDIF
                                showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.ut_btsbinnvilget_ikke_btfbinnvilget()))){
                                    text (
                                        bokmal { + "Inntekten din på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner er " + pe.ut_inntekt_høyere_lavere() + " enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                        nynorsk { + "Inntekta di på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner er " + pe.ut_inntekt_høyere_lavere() + " enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                    )
                                }

                                //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_UT_BTSBInnvilget_Ikke_BTFBInnvilget() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop )) THEN      INCLUDE ENDIF
                                showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.ut_btsbinnvilget_ikke_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().lessThanOrEqual(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop())))){
                                    text (
                                        bokmal { + "ikke " },
                                        nynorsk { + "ikkje " },
                                    )
                                }

                                //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_UT_BTSBInnvilget_Ikke_BTFBInnvilget() )) THEN      INCLUDE ENDIF
                                showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.ut_btsbinnvilget_ikke_btfbinnvilget()))){
                                    text (
                                        bokmal { + "redusert ut fra inntekt. " },
                                        nynorsk { + "redusert ut frå inntekt. " },
                                    )
                                }

                                //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_UT_BTSBInnvilget_Ikke_BTFBInnvilget() )) THEN      INCLUDE ENDIF
                                showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.ut_btsbinnvilget_ikke_btfbinnvilget()))){
                                    text (
                                        bokmal { + "Inntekten din er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner. " },
                                        nynorsk { + "Inntekta di er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner. " },
                                    )
                                }

                                //IF(( PE_UT_BTSBInnvilget_og_justBelopPA() )) THEN      INCLUDE ENDIF
                                showIf(((pe.ut_btsbinnvilget_og_justbeloppa()))){
                                    text (
                                        bokmal { + "Inntekten din er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner. Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. " },
                                        nynorsk { + "Inntekta di er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner. Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. " },
                                    )
                                }

                                //IF(( PE_UT_BTSBInnvilget_og_justBelopPA() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 )) THEN      INCLUDE ENDIF
                                showIf(((pe.ut_btsbinnvilget_og_justbeloppa() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)))){
                                    text (
                                        bokmal { + "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året." },
                                        nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                                    )
                                }
                            }
                        }

                        //IF(PE_UT_TBU1286_del1() = true OR PE_UT_TBU1286_del2() = true OR PE_UT_TBU1286_del3() = true) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() or pe.ut_tbu1286_del2() or pe.ut_tbu1286_del3())){
                            //[TBU1286]
                            paragraph {

                                //IF(PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                                showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                                    text (
                                        bokmal { + "Inntekten din er " + pe.ut_inntekt_høyere_lavere() + " enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene. " },
                                        nynorsk { + "Inntekta di er " + pe.ut_inntekt_høyere_lavere() + " enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra. " },
                                    )
                                }

                                //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0  AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)  OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0)) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                                showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                                    text (
                                        bokmal { + "Dette barnetillegget er derfor " + pe.ut_ikke() + "redusert ut fra inntekt. " },
                                        nynorsk { + "Dette barnetillegget er derfor " + pe.ut_ikke() + "redusert ut frå inntekt. " },
                                    )
                                }

                                //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                                showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                                    text (
                                        bokmal { + "Til sammen er " },
                                        nynorsk { + "Til saman er " },
                                    )
                                }

                                //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <>  0 AND ( (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0) ) AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                                showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                                    text (
                                        bokmal { + "også " },
                                        nynorsk { + "også " },
                                    )
                                }

                                //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                                showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                                    text (
                                        bokmal { + "inntektene til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_alle_spraak_entall() + " din " + pe.ut_bruttoetterreduksjonbt_høyere_lavere() + " enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre. " },
                                        nynorsk { + "inntektene til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_alle_spraak_entall() + " din " + pe.ut_bruttoetterreduksjonbt_høyere_lavere() + " enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine. " },
                                    )
                                }

                                //IF( PE_UT_TBU1286_del1() AND ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0))  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                                showIf((pe.ut_tbu1286_del1() and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                                    text (
                                        bokmal { + "Dette barnetillegget er derfor " },
                                        nynorsk { + "Dette barnetillegget er derfor " },
                                    )
                                }

                                //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBbrutto  AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0))  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                                showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbbrutto()) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                                    text (
                                        bokmal { + "ikke " },
                                        nynorsk { + "ikkje " },
                                    )
                                }

                                //IF( PE_UT_TBU1286_del1() AND ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0))  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                                showIf((pe.ut_tbu1286_del1() and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                                    text (
                                        bokmal { + "redusert ut fra inntekt. " },
                                        nynorsk { + "redusert ut frå inntekt. " },
                                    )
                                }

                                //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0  AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0)) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0  ) THEN      INCLUDE ENDIF
                                showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                                    text (
                                        bokmal { + "Barnetilleggene er derfor" },
                                        nynorsk { + "Desse barnetillegga er derfor" },
                                    )
                                }

                                //IF( PE_UT_TBU1286_del1() AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBbrutto  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBbrutto)  AND  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0)  AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0)) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                                showIf((pe.ut_tbu1286_del1() and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbbrutto()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbbrutto())) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0)) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                                    text (
                                        bokmal { + " ikke" },
                                        nynorsk { + " ikkje" },
                                    )
                                }

                                //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0  AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0)) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0  ) THEN      INCLUDE ENDIF
                                showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                                    text (
                                        bokmal { + " redusert ut fra inntekt. " },
                                        nynorsk { + " redusert ut frå inntekt. " },
                                    )
                                }

                                //IF( PE_UT_TBU1286_del1() AND  ( PE_UT_TBU1286_del2() OR PE_UT_TBU1286_del3() )) THEN      INCLUDE ENDIF
                                showIf((pe.ut_tbu1286_del1() and (pe.ut_tbu1286_del2() or pe.ut_tbu1286_del3()))){
                                    text (
                                        bokmal { + " " },
                                        nynorsk { + " " },
                                    )
                                }

                                //IF(( PE_UT_TBU1286_del2() )) THEN      INCLUDE ENDIF
                                showIf(((pe.ut_tbu1286_del2()))){
                                    text (
                                        bokmal { + "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre. " },
                                        nynorsk { + "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine. " },
                                    )
                                }

                                //IF(( PE_UT_TBU1286_del2() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 )) THEN      INCLUDE ENDIF
                                showIf(((pe.ut_tbu1286_del2() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0)))){
                                    text (
                                        bokmal { + "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                        nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                                    )
                                }

                                //IF(( PE_UT_TBU1286_del2() AND PE_UT_TBU1286_del3() )) THEN      INCLUDE ENDIF
                                showIf(((pe.ut_tbu1286_del2() and pe.ut_tbu1286_del3()))){
                                    text (
                                        bokmal { + " " },
                                        nynorsk { + " " },
                                    )
                                }

                                //IF(( PE_UT_TBU1286_del3() )) THEN      INCLUDE ENDIF
                                showIf(((pe.ut_tbu1286_del3()))){
                                    text (
                                        bokmal { + "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene. " },
                                        nynorsk { + "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra. " },
                                    )
                                }

                                //IF(( PE_UT_TBU1286_del3() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 )) THEN      INCLUDE ENDIF
                                showIf(((pe.ut_tbu1286_del3() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)))){
                                    text (
                                        bokmal { + "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                        nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                                    )
                                }
                            }
                        }

                        //IF( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) ) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) or not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())))){
                            includePhrase(TBU1286.1_Generated(pe))
                        }

                        //IF( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false) ) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) or not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())))){
                            includePhrase(TBU1286.2_Generated(pe))
                        }

                        //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0)  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0)  )  THEN      INCLUDE ENDIF
                        showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0)) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0)))){
                            includePhrase(TBU2490_Generated(pe))
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())){
                            includePhrase(TBU1288_Generated)
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())){

                        //IF (PE_UT_KravLinjeKode_VedtakResultat_forekomst_bt_innv()) THEN INCLUDE ENDIF
                        showIf((pe.ut_kravlinjekode_vedtakresultat_forekomst_bt_innv())){
                            includePhrase(TBU5005_Generated)
                        }

                        //IF (PE_UT_KravLinjeKode_VedtakResultat_forekomst_bt_innv()) THEN INCLUDE ENDIF
                        showIf((pe.ut_kravlinjekode_vedtakresultat_forekomst_bt_innv())){
                            includePhrase(TBU5007_Generated)
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        includePhrase(TBU2275_Generated)
                        includePhrase(TBU2369_Generated)
                    }

                    //IF(PE_SaksData_SakAPogUP = true AND PE_SaksData_SakAPStatus = "lopende" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold") THEN      INCLUDE ENDIF
                    showIf((pe.saksdata_sakapogup() and pe.saksdata_sakapstatus().equalTo("lopende") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("instopphold"))){
                        includePhrase(TBU2370_Generated)
                        includePhrase(TBU2371_Generated)
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_onsketVirkningsDato < PE_VedtakFattetDato_minus_1mnd) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_belopokt() and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_kravhode_onsketvirkningsdato().lessThan(pe.vedtakfattetdato_minus_1mnd()))){
                        includePhrase(TBU3032_Generated(pe))
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningAntallPerioder > 1 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_onsketVirkningsDato < PE_VedtakFattetDato_minus_1mnd) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningantallperioder().greaterThan(1) and pe.vedtaksdata_beregningsdata_beregningufore_belopredusert() and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_kravhode_onsketvirkningsdato().lessThan(pe.vedtakfattetdato_minus_1mnd()))){
                        includePhrase(TBU2530_Generated)
                    }
                    includePhrase(TBU2212_Generated)
                    includePhrase(TBU2213_Generated)
                    includePhrase(TBU1074_Generated)
                    includePhrase(TBU2242_Generated)
                    includePhrase(TBU1227_Generated)
                    includePhrase(TBU1228_Generated)

                    //IF(FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) <> "nor" AND FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) <> "") THEN      INCLUDE ENDIF
                    showIf(((pe.grunnlag_persongrunnlagsliste_personbostedsland()).notEqualTo("nor") and (pe.grunnlag_persongrunnlagsliste_personbostedsland()).notEqualTo(""))){
                        includePhrase(TBU3730_Generated)
                    }
                    includePhrase(TBU1076_MedMargin_Generated)
                    includePhrase(TBU1077_Generated)
                    includePhrase(TBU2455_Generated(pe))
                    //[TBU2247]
                    paragraph {
                        text (
                            bokmal { + "Vedlegg:" },
                            nynorsk { + "Vedlegg:" },
                        )

                        //IF((PE_Vedtaksdata_BeregningsData_BeregningUfore_Total <> PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Brutto  AND PE_Vedtaksdata_BeregningsData_BeregningAntallPerioder >= 1)  OR PE_Vedtaksdata_BeregningsData_BeregningAntallPerioder > 1) THEN      INCLUDE ENDIF
                        showIf(((pe.vedtaksdata_beregningsdata_beregningufore_total().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_brutto()) and pe.vedtaksdata_beregningsdata_beregningantallperioder().greaterThanOrEqual(1)) or pe.vedtaksdata_beregningsdata_beregningantallperioder().greaterThan(1))){
                            text (
                                bokmal { + "" },
                                nynorsk { + "" },
                            )
                        }

                        //IF((PE_Vedtaksdata_BeregningsData_BeregningUfore_Total <> PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Brutto  AND PE_Vedtaksdata_BeregningsData_BeregningAntallPerioder >= 1)  OR PE_Vedtaksdata_BeregningsData_BeregningAntallPerioder > 1) THEN      INCLUDE ENDIF
                        showIf(((pe.vedtaksdata_beregningsdata_beregningufore_total().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_brutto()) and pe.vedtaksdata_beregningsdata_beregningantallperioder().greaterThanOrEqual(1)) or pe.vedtaksdata_beregningsdata_beregningantallperioder().greaterThan(1))){
                            text (
                                bokmal { + "Dette er din månedlige uføretrygd før skatt" },
                                nynorsk { + "Dette er den månadlege uføretrygda di før skatt" },
                            )
                        }

                        //IF(PE_Vedtaksdata_Faktoromregnet = false) THEN      INCLUDE ENDIF
                        showIf((not(pe.vedtaksdata_faktoromregnet()))){
                            text (
                                bokmal { + "" },
                                nynorsk { + "" },
                            )
                        }

                        //IF(PE_Vedtaksdata_Faktoromregnet = false) THEN      INCLUDE ENDIF
                        showIf((not(pe.vedtaksdata_faktoromregnet()))){
                            text (
                                bokmal { + "Opplysninger om beregningen" },
                                nynorsk { + "Opplysningar om berekninga" },
                            )
                        }
                        text (
                            bokmal { + "Orientering om rettigheter og plikter" },
                            nynorsk { + "Orientering om rettar og plikter" },
                        )
                    }
                }
            }

            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, pesysData.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pesysData.pe, pesysData.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
