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
import no.nav.pensjon.brev.maler.fraser.common.Constants.KLAGE_URL
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
import kotlin.and
import kotlin.or
import kotlin.text.format

@TemplateModelHelpers
object OkningUforegrad : RedigerbarTemplate<InnvilgelseUfoeretrygdDto> {

    override val featureToggle = FeatureToggles.brevmalUtInnvilgelse.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_OKNING_UFOREGRAD
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - økning av uføregrad",
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
            val onsketvirkningsdato = pe.vedtaksdata_kravhode_onsketvirkningsdato().ifNull(LocalDate.now())
            val bostedutland = (pe.grunnlag_persongrunnlagsliste_personbostedsland().notEqualTo("nor") and (pe.grunnlag_persongrunnlagsliste_personbostedsland()).notEqualTo(""))

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat) <> "oppfylt") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat()).notEqualTo("oppfylt"))){
                //[TBU1251NN, TBU1251]

                paragraph {
                    text (
                        bokmal { + "Vi har innvilget søknaden din om økt uføretrygd som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Uføregraden din øker fra <FRITEKST: Forrige Uforegrad> til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent fra " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt().format() + "." },
                        nynorsk { + "Vi har innvilga søknaden din om auka uføretrygd som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Uføregraden din aukar frå <FRITEKST: Forrige Uforegrad> til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent frå " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt().format() + "." },
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat) = "oppfylt") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat()).equalTo("oppfylt"))){
                //[TBU1252NN, TBU1252]

                paragraph {
                    text (
                        bokmal { + "Vi har innvilget søknaden din om økt uføretrygd som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Uføregraden din øker fra <FRITEKST: Forrige Uforegrad> til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent fra " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt().format() + ". Hele uføretrygden din er innvilget med rettighet som ung ufør." },
                        nynorsk { + "Vi har innvilga søknaden din om auka uføretrygd som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Uføregraden din aukar <FRITEKST: Forrige Uforegrad> til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent frå " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt().format() + ". Heile uføretrygda di er innvilga med rett som ung ufør. " },
                    )
                }
            }

            //IF((PE_Vedtaksdata_Kravhode_KravArsakType = "omgj_etter_klage" OR PE_Vedtaksdata_Kravhode_KravArsakType = "omgj_etter_anke") AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat(1) <> "oppfylt") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("omgj_etter_klage") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("omgj_etter_anke")) and FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat(1).notEqualTo("oppfylt"))){
                //[TBU1299NN, TBU1299]

                paragraph {
                    text (
                        bokmal { + "Vi har innvilget søknaden din om økt uføretrygd som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Du har fått medhold i klagen din, og får " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent uføretrygd fra " + pe.vedtaksdata_virkningfom().format() + "." },
                        nynorsk { + "Vi har innvilga søknaden din om uføretrygd som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Du har fått medhald i klaga di, og får " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent uføretrygd frå " + pe.vedtaksdata_virkningfom().format() + "." },
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat) = "oppfylt" AND (PE_Vedtaksdata_Kravhode_KravArsakType = "omgj_etter_klage" OR PE_Vedtaksdata_Kravhode_KravArsakType = "omgj_etter_anke")) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat()).equalTo("oppfylt") and (pe.vedtaksdata_kravhode_kravarsaktype().equalTo("omgj_etter_klage") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("omgj_etter_anke")))){
                //[TBU1300NN, TBU1300]

                paragraph {
                    text (
                        bokmal { + "Vi har innvilget søknaden din om økt uføretrygd som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Du har fått medhold i klagen din, og får " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent uføretrygd med rettighet som ung ufør fra " + pe.vedtaksdata_virkningfom().format() + "." },
                        nynorsk { + "Vi har innvilga søknaden din om uføretrygd som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Du har fått medhald i klaga di, og får " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent uføretrygd med rett som ung ufør frå " + pe.vedtaksdata_virkningfom().format() + "." },
                    )
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad,1) = FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad,1)) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).equalTo((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad())))){
                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at hele uførheten din skyldes en godkjent yrkesskade eller yrkessykdom." },
                        nynorsk { + "Vi har kome fram til at heile uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom." },
                    )
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) < FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) > 0 AND (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "ikke_oppfylt") ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).lessThan((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad())) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).greaterThan(0) and ((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt") or (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("ikke_oppfylt")))){
                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad().format() + " prosent av uførheten din skyldes en godkjent yrkesskade eller yrkessykdom." },
                        nynorsk { + "Vi har kome fram til at " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad().format() + " prosent av uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom." },
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "ikke_oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) = "stdbegr_12_17_1_i_1") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("ikke_oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse()).equalTo("stdbegr_12_17_1_i_1"))){
                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at uførheten din ikke skyldes en godkjent yrkesskade eller yrkessykdom." },
                        nynorsk { + "Vi har kome fram til at uførleiken din ikkje kjem av ein godkjend yrkesskade eller yrkessjukdom." },
                    )
                }
            }

            //IF( FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat, 1) = "ikke_oppfylt")  AND (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse, 1) = "stdbegr_12_17_1_i_2")  THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("ikke_oppfylt")) and ((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse()).equalTo("stdbegr_12_17_1_i_2"))){
                //[TBU1249NN, TBU1249]

                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at det er andre sykdomsforhold enn din godkjente yrkesskade eller yrkessykdom som er årsak til den økte uførheten din." },
                        nynorsk { + "Vi har kome fram til at det er andre sjukdomsforhold enn godkjend yrkesskade eller yrkessjukdom som er årsak til den auka uførleiken din." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())){
                //[TBU1117]
                paragraph {

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())){
                        text (
                            bokmal { + "Du er innvilget barnetillegg i uføretrygden din. " },
                            nynorsk { + "Du er innvilga barnetillegg i uføretrygda di. " },
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND  (((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0)   AND   (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0))   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false))  ) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0))) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))))){
                        text (
                            bokmal { + "Tillegget blir ikke utbetalt fordi inntekten til deg og din ektefelle er over grensen for å få utbetalt barnetillegg. " },
                            nynorsk { + "Tillegget blir ikkje utbetalt fordi inntekta di og inntekta til ektefellen din er over grensa for å få utbetalt barnetillegg. " },
                        )
                    }

                    //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)  AND  (((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0)   AND   (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0))   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false))  ) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) and (((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0))) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))))){
                        text (
                            bokmal { + "Tillegget blir ikke utbetalt fordi inntekten din er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { + "Tillegget blir ikkje betalt ut fordi inntekta di er over grensa for å få utbetalt barnetillegg." },
                        )
                    }
                }
            }

            //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
            showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()){
                paragraph {
                    text (
                        bokmal { + "Du er innvilget gjenlevendetillegg i uføretrygden din." },
                        nynorsk { + "Du er innvilga attlevandetillegg i uføretrygda di." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt())){
                paragraph {
                    text (
                        bokmal { + "Vi har redusert utbetalingen av uføretrygden din, fordi du er innlagt på institusjon." },
                        nynorsk { + "Vi har redusert utbetalinga av uføretrygda di, fordi du er innlagd på institusjon." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                paragraph {
                    text (
                        bokmal { + "Du er innlagt på institusjon, men vi kommer likevel ikke til å redusere utbetalingen av uføretrygden din." },
                        nynorsk { + "Du er innlagd på institusjon, men vi kjem likevel ikkje til å redusere utbetalinga av uføretrygda di." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt())){
                paragraph {
                    text (
                        bokmal { + "Vi har redusert utbetalingen av uføretrygden din, fordi du er under straffegjennomføring." },
                        nynorsk { + "Vi har redusert utbetalinga av uføretrygda di, fordi du er under straffegjennomføring." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                paragraph {
                    text (
                        bokmal { + "Du er under straffegjennomføring, men vi kommer likevel ikke til å redusere utbetalingen av uføretrygden din." },
                        nynorsk { + "Du er under straffegjennomføring, men vi kjem likevel ikkje til å redusere utbetalinga av uføretrygda di." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false) THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                includePhrase(TBU1120_Generated(pe))
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                includePhrase(TBU1121_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false) THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                includePhrase(TBU1122_Generated(pe))
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                includePhrase(TBU1123_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                includePhrase(TBU1253_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                includePhrase(TBU1254_Generated(pe))
            }

            //IF(  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo" AND PE_UT_Forsorgeransvar_ingen_er_true()  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true   ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and pe.ut_forsorgeransvar_ingen_er_true() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt())){
                paragraph {
                    text (
                        bokmal { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men den kommer ikke til utbetaling fordi du er under straffegjennomføring." },
                        nynorsk { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men ho blir ikkje betalt ut fordi du er under straffegjennomføring." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt())){
                paragraph {
                    text (
                        bokmal { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalingen er redusert fordi du er innlagt på institusjon. I denne perioden vil du få utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                        nynorsk { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalinga er redusert fordi du er innlagd på institusjon. I denne perioden får du betalt ut " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                    )
                }
            }

            //IF(  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo" AND PE_UT_Forsorgeransvar_ingen_er_false()  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true   ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt())){
                paragraph {
                    text (
                        bokmal { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalingen er redusert fordi du er under straffegjennomføring. I denne perioden vil du få utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                        nynorsk { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalinga er redusert fordi du er under straffegjennomføring. I denne perioden får du betalt ut " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                    )
                }
            }

            //IF(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland(1) = "" OR PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland(1) = "nor") THEN      INCLUDE ENDIF
            showIf(bostedutland) {
                paragraph {
                    text (
                        bokmal { + "Uføretrygden blir utbetalt senest den 20. hver måned. Mottar du uføretrygden på en utenlandsk bankkonto kan utbetalingen bli forsinket. Du får din første utbetaling i " + fritekst("måned og år") + "." },
                        nynorsk { + "Uføretrygda blir betalt ut seinast den 20. kvar månad. Får du uføretrygda på ein utanlandsk bankkonto, kan utbetalinga bli forseinka. Du får den første utbetalinga di i " + fritekst("månad og år") + "." },
                    )
                }
            }.orShow {
                //[TBU1255NN, TBU1255]
                paragraph {
                    text (
                        bokmal { + "Uføretrygden blir utbetalt senest den 20. hver måned. Du får din første utbetaling med nytt beløp i " + fritekst("måned og år") + "." },
                        nynorsk { + "Uføretrygda blir utbetalt seinast den 20. i kvar månad. Du får den første utbetalinga di med nytt beløp i " + fritekst("Månad og år") + "." },
                    )
                }
            }

            paragraph {
                text (
                    bokmal { + "I dette brevet forklarer vi hvilke rettigheter og plikter du har. Det er derfor viktig at du leser hele brevet." },
                    nynorsk { + "I dette brevet forklarer vi kva rettar og plikter du har. Det er derfor viktig at du les heile brevet." },
                )
            }

            title1 {
                text (
                    bokmal { + "Begrunnelse for vedtaket" },
                    nynorsk { + "Grunngiving for vedtaket" },
                )
            }
            //[TBU1256NN, TBU1256]

            paragraph {
                text (
                    bokmal { + "For å ha rett til økt uføretrygd må du oppfylle disse vilkårene:" },
                    nynorsk { + "For å ha rett til auka uføretrygd må du oppfylle desse vilkåra:" },
                )
                text (
                    bokmal { + "Du må være mellom 18 og 67 år." },
                    nynorsk { + "Du må vere mellom 18 og 67 år." },
                )
                text (
                    bokmal { + "Du må ha vært medlem av folketrygden i de siste tre årene fram til uføretidspunktet, eller oppfylle en av unntaksreglene." },
                    nynorsk { + "Du må ha vore medlem av folketrygda i dei siste tre åra fram til uføretidspunktet, eller oppfylle ein av unntaksreglane." },
                )
                text (
                    bokmal { + "Inntektsevnen din må ha blitt ytterligere nedsatt, og denne nedsettelsen må være varig." },
                    nynorsk { + "Inntektsevna di må ha blitt ytterlegare sett ned, og denne nedsetjinga må vere varig." },
                )
                text (
                    bokmal { + "Sykdom eller skade må være hovedårsak til at inntektsevnen din har blitt ytterligere nedsatt." },
                    nynorsk { + "Sjukdom eller skade må vere hovudårsak til at inntektsevna di er blitt ytterlegare sett ned." },
                )
                text (
                    bokmal { + "Du må ha gjennomført hensiktsmessig behandling og arbeidsrettede tiltak." },
                    nynorsk { + "Du må ha gjennomført formålstenleg behandling og arbeidsretta tiltak." },
                )
            }
            //[TBU1257NN, TBU1257]

            paragraph {
                text (
                    bokmal { + "Vi har kommet fram til at du oppfyller disse vilkårene." },
                    nynorsk { + "Vi har kome fram til at du oppfyller desse vilkåra." },
                )
            }

            //PE_Vedtaksdata_Kravhode_KravArsakType = "omgj_etter_klage"
            showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("omgj_etter_klage")){
                //[TBU3232NN, TBU3232]

                paragraph {
                    text (
                        bokmal { + "Søknaden din om økt uføretrygd er innvilget etter klage og vi anser klagen som ferdig behandlet. Dersom du ønsker å opprettholde klagen, må du gi tilbakemelding til Nav innen 3 uker." },
                        nynorsk { + "Søknaden din om auka uføretrygd er innvilga etter klage, og vi ser det slik at klaga er ferdig behandla. Dersom du ønskjer å halde fast på klaga, må du melde dette tilbake til Nav innan 3 veker." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false) AND  (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat, 1) = "ikke_oppfylt"  OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat, 1) = "oppfylt")  AND (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo") AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())) and ((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("ikke_oppfylt") or (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt")) and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo")) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())){
                includePhrase(TBU1138_Generated(pe))
            }

            //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)  AND  FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,1) = "ikke_vurdert"  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo") )  THEN      INCLUDE ENDIF
            showIf(((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("ikke_vurdert") and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo")))){
                includePhrase(TBU1139_Generated(pe))
            }

            //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND  FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,1) = "ikke_vurdert"  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo") )  THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("ikke_vurdert") and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo")))){
                //[TBU1140]
                paragraph {
                    text (
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-16 og " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-16 og " },
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
                            bokmal { + " og forskrift om barnetillegg i uføretrygden" },
                            nynorsk { + " og forskrift om barnetillegg i uføretrygda" },
                        )
                    }
                    text (
                        bokmal { + "." },
                        nynorsk { + "." },
                    )
                }
            }

            //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND ( FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,1) = "ikke_oppfylt"  OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,1) = "oppfylt" ) AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo") )  THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and ((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("ikke_oppfylt") or (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt")) and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo")))){
                //[TBU1141]
                paragraph {
                    text (
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-17 og " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-17 og " },
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
                            bokmal { + " og forskrift om barnetillegg i uføretrygden" },
                            nynorsk { + " og forskrift om barnetillegg i uføretrygda" },
                        )
                    }
                    text (
                        bokmal { + "." },
                        nynorsk { + "." },
                    )
                }
            }

            //IF( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false  AND  FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,1) = "ikke_vurdert"  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo") )  THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat(),1).equalTo("ikke_vurdert") and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo")))){
                includePhrase(TBU1142_Generated(pe))
            }

            //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)  AND  (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,1) = "ikke_oppfylt"  OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,1) = "oppfylt")  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo") )  THEN      INCLUDE ENDIF
            showIf(((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) and ((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat(),1).equalTo("ikke_oppfylt") or FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat(),1).equalTo("oppfylt")) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo")))){
                includePhrase(TBU1143_Generated(pe))
            }

            //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND  FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,1) = "ikke_vurdert"  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo") )  THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("ikke_vurdert") and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo")))){
                //[TBU1144]
                paragraph {
                    text (
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-16, 12-18 og " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-16, 12-18 og " },
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
                            bokmal { + " og forskrift om barnetillegg i uføretrygden" },
                            nynorsk { + " og forskrift om barnetillegg i uføretrygda" },
                        )
                    }
                    text (
                        bokmal { + "." },
                        nynorsk { + "." },
                    )
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND  (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,1) = "ikke_oppfylt"  OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,1) = "oppfylt") AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_hs"  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType <> "reduksjon_fo") )  THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and (FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat(),1).equalTo("ikke_oppfylt") or FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat(),1).equalTo("oppfylt")) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().notEqualTo("reduksjon_fo")))){
                //[TBU1145]
                paragraph {
                    text (
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-18 og " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-18 og " },
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
                            bokmal { + " og forskrift om barnetillegg i uføretrygden" },
                            nynorsk { + " og forskrift om barnetillegg i uføretrygda" },
                        )
                    }
                    text (
                        bokmal { + "." },
                        nynorsk { + "." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                includePhrase(TBU3233_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                includePhrase(TBU3234_Generated(pe))
            }

            //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" )  THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                //[TBU3235]
                paragraph {
                    text (
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-16, 12-19 og " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-16, 12-19 og " },
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
                            bokmal { + " og forskrift om barnetillegg i uføretrygden" },
                            nynorsk { + " og forskrift om barnetillegg i uføretrygda" },
                        )
                    }
                    text (
                        bokmal { + "." },
                        nynorsk { + "." },
                    )
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" )  THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                //[TBU3236]
                paragraph {
                    text (
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-17, 12-19 og " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-17, 12-19 og " },
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
                            bokmal { + " og forskrift om barnetillegg i uføretrygden" },
                            nynorsk { + " og forskrift om barnetillegg i uføretrygda" },
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
                includePhrase(TBU3237_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo") THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                includePhrase(TBU3238_Generated(pe))
            }

            //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo" )  THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                //[TBU3239]
                paragraph {
                    text (
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-16, 12-20 og " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-16, 12-20 og " },
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
                            bokmal { + " og forskrift om barnetillegg i uføretrygden" },
                            nynorsk { + " og forskrift om barnetillegg i uføretrygda" },
                        )
                    }
                    text (
                        bokmal { + "." },
                        nynorsk { + "." },
                    )
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo" )  THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                //[TBU3240]
                paragraph {
                    text (
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-17, 12-20 og " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-17, 12-20 og " },
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
                            bokmal { + " og forskrift om barnetillegg i uføretrygden" },
                            nynorsk { + " og forskrift om barnetillegg i uføretrygda" },
                        )
                    }
                    text (
                        bokmal { + "." },
                        nynorsk { + "." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                includePhrase(TBU3720_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                includePhrase(TBU3721_Generated(pe))
            }

            //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                //[TBU3722]
                paragraph {
                    text (
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-16, 12-18, 12-19 og " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-16, 12-18, 12-19 og " },
                    )

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse,1) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                        text (
                            bokmal { + "22-12" },
                            nynorsk { + "22-12" },
                        )
                    }

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse,1) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                        text (
                            bokmal { + "22-13" },
                            nynorsk { + "22-13" },
                        )
                    }

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                        text (
                            bokmal { + " og forskrift om barnetillegg i uføretrygden" },
                            nynorsk { + " og forskrift om barnetillegg i uføretrygda" },
                        )
                    }
                    text (
                        bokmal { + "." },
                        nynorsk { + "." },
                    )
                }
            }

            //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                //[TBU3723]
                paragraph {
                    text (
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-17 til 12-19 og " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-17 til 12-19 og " },
                    )

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse,1) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                        text (
                            bokmal { + "22-12" },
                            nynorsk { + "22-12" },
                        )
                    }

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse,1) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                        text (
                            bokmal { + "22-13" },
                            nynorsk { + "22-13" },
                        )
                    }

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                        text (
                            bokmal { + " og forskrift om barnetillegg i uføretrygden" },
                            nynorsk { + " og forskrift om barnetillegg i uføretrygda" },
                        )
                    }
                    text (
                        bokmal { + "." },
                        nynorsk { + "." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo") THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                includePhrase(TBU3724_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo") THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                includePhrase(TBU3725_Generated(pe))
            }

            //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo" ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                //[TBU3726]
                paragraph {
                    text (
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-16, 12-18, 12-20 og " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-16, 12-18, 12-20 og " },
                    )

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse,1) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                        text (
                            bokmal { + "22-12" },
                            nynorsk { + "22-12" },
                        )
                    }

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse,1) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                        text (
                            bokmal { + "22-13" },
                            nynorsk { + "22-13" },
                        )
                    }

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                        text (
                            bokmal { + " og forskrift om barnetillegg i uføretrygden" },
                            nynorsk { + " og forskrift om barnetillegg i uføretrygda" },
                        )
                    }
                    text (
                        bokmal { + "." },
                        nynorsk { + "." },
                    )
                }
            }

            //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo" ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo"))){
                //[TBU3727]
                paragraph {
                    text (
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-17, 12-18, 12-20 og " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-17, 12-18, 12-20 og " },
                    )

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse,1) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
                        text (
                            bokmal { + "22-12" },
                            nynorsk { + "22-12" },
                        )
                    }

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse,1) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                        text (
                            bokmal { + "22-13" },
                            nynorsk { + "22-13" },
                        )
                    }

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")){
                        text (
                            bokmal { + " og forskrift om barnetillegg i uføretrygden" },
                            nynorsk { + " og forskrift om barnetillegg i uføretrygda" },
                        )
                    }
                    text (
                        bokmal { + "." },
                        nynorsk { + "." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) = "oppfylt" OR PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) = "ikke_oppfylt") THEN      INCLUDE ENDIF
            showIf((FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1).equalTo("oppfylt") or FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1).equalTo("ikke_oppfylt"))){
                paragraph {
                    text (
                        bokmal { + "Uførhet som skyldes yrkesskade eller yrkessykdom" },
                        nynorsk { + "Uførleik som kjem av yrkesskade eller yrkessjukdom" },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse(1) = "stdbegr_12_17_1_o_1") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse(1).equalTo("stdbegr_12_17_1_o_1"))){
                //[TBU1258NN, TBU1258]

                paragraph {
                    text (
                        bokmal { + "Du har en godkjent yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert at hele din nedsatte inntektsevne skyldes den godkjente yrkesskaden eller yrkessykdommen." },
                        nynorsk { + "Du har ein godkjend yrkesskade eller yrkessjukdom. Vi har ut frå opplysningane i saka vurdert at heile den nedsette inntektsevna di kjem av den godkjende yrkesskaden eller yrkessjukdommen." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = true AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse(1) = "stdbegr_12_17_1_o_1" AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest() and FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse(1).equalTo("stdbegr_12_17_1_o_1") and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))){
                paragraph {
                    text (
                        bokmal { + "Dette betyr at uføretrygden din vil bli beregnet etter særbestemmelser som gir deg en høyere uføretrygd." },
                        nynorsk { + "Dette betyr at uføretrygda di blir rekna ut etter særreglar som gir deg ei høgare uføretrygd." },
                    )
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) = FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = false AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse(1) = "stdbegr_12_17_1_o_1" AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).equalTo((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad())) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse(1).equalTo("stdbegr_12_17_1_o_1") and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))){
                paragraph {
                    text (
                        bokmal { + "Dette betyr at uføretrygden din vil bli beregnet etter særbestemmelser, dersom dette er til fordel for deg." },
                        nynorsk { + "Dette betyr at uføretrygda di blir rekna ut etter særreglar, dersom dette er til fordel for deg." },
                    )
                }
                includePhrase(TBU1265_Generated)
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) < FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) > 0 AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse(1) = "stdbegr_12_17_1_o_2") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).lessThan((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad())) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()).greaterThan(0) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt") and FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse(1).equalTo("stdbegr_12_17_1_o_2"))){
                paragraph {
                    text (
                        bokmal { + "Du har en godkjent yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert om yrkesskaden eller yrkessykdommen er årsak til den økte uførheten din." },
                        nynorsk { + "Du har ein godkjend yrkesskade eller yrkessjukdom. Vi har ut frå opplysningane i saka vurdert om yrkesskaden eller yrkessjukdommen din er årsak til den auka uførleiken din." },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad().format() + " prosent av uførheten din skyldes godkjent yrkesskade eller yrkessykdom. <FRITEKST: Konkret begrunnelse>." },
                        nynorsk { + "Vi har kome fram til at " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad().format() + " prosent av uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom. <FRITEKST: Konkret begrunnelse>." },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = true AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest() and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))){
                    paragraph {
                        text (
                            bokmal { + "Derfor vil denne delen av uføretrygden din bli beregnet etter særbestemmelser som gir deg en høyere uføretrygd." },
                            nynorsk { + "Derfor vil denne delen av uføretrygda di bli rekna ut etter særreglar som gjer deg ei høgare uføretrygd." },
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = false AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
                showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))){
                    paragraph {
                        text (
                            bokmal { + "Denne delen av uføretrygden din vil bli beregnet etter særbestemmelser, dersom dette er til fordel for deg." },
                            nynorsk { + "Denne delen av uføretrygda di blir rekna ut etter særreglar dersom det er til fordel for deg." },
                        )
                    }
                    includePhrase(TBU1265_Generated)
                }
            }

            //IF(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) = "stdbegr_12_17_1_o_3") THEN      INCLUDE ENDIF
            showIf((FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse()).equalTo("stdbegr_12_17_1_o_3"))){
                //[TBU1266NN, TBU1266]

                paragraph {
                    text (
                        bokmal { + "Du er tidligere innvilget uføretrygd etter særbestemmelser for yrkesskade eller yrkessykdom. Vi har ut fra opplysninger i saken din vurdert om yrkesskaden eller yrkessykdommen din også er årsak til at uførheten din har økt, eller om dette skyldes andre sykdomsforhold." },
                        nynorsk { + "Du er tidlegare innvilga uføretrygd etter særreglar for yrkesskade eller yrkessjukdom. Vi har ut frå opplysningar i saka di vurdert om yrkesskaden eller yrkessjukdommen din også er årsak til at uførleiken din har auka, eller om dette kjem av andre sjukdomsforhold." },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at det er andre sykdomsforhold som er årsak til den økte uførheten din. <Fritekst: Konkret begrunnelse>" },
                        nynorsk { + "Vi har kome fram til at det er andre sjukdomsforhold som er årsak til den auka uførleiken din. <Fritekst: Konkret begrunnelse>" },
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,1) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IVSBegrunnelse) = "stdbegr_12_17_3_1") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsbegrunnelse()).equalTo("stdbegr_12_17_3_1"))){
                //[TBU1271NN, TBU1271]

                paragraph {
                    text (
                        bokmal { + "Skadetidspunktet ditt har vi fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_skadetidspunkt().format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsinntekt().format() + " kroner." },
                        nynorsk { + "Vi har fastsett skadetidspunktet ditt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_skadetidspunkt().format() + ". Den årlege inntekta di på dette tidspunktet er fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsinntekt().format() + " kroner." },
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,1) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IVSBegrunnelse) = "stdbegr_12_17_3_2") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsbegrunnelse()).equalTo("stdbegr_12_17_3_2"))){
                paragraph {
                    text (
                        bokmal { + "Skadetidspunktet ditt har vi fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_skadetidspunkt().format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner. Vi har justert denne inntekten opp fordi den skal tilsvare inntekten du ville hatt som frisk på skadetidspunktet." },
                        nynorsk { + "Skadetidspunktet ditt har vi fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_skadetidspunkt().format() + ". Den årlege inntekta på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner. Vi har justert denne inntekta opp fordi den skal svare til inntekta du ville hatt som frisk på skadetidspunktet." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) = "ikke_oppfylt" AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse(1) = "stdbegr_12_17_1_i_2") THEN      INCLUDE ENDIF
            showIf((FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1).equalTo("ikke_oppfylt") and FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse(1).equalTo("stdbegr_12_17_1_i_2"))){
                paragraph {
                    text (
                        bokmal { + "Du har en godkjent yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert om yrkesskaden eller yrkessykdommen er årsak til den økte uførheten din." },
                        nynorsk { + "Du har ein godkjend yrkesskade eller yrkessjukdom. Vi har ut frå opplysningane i saka vurdert om yrkesskaden eller yrkessjukdommen din er årsak til den auka uførleiken din." },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at det er andre sykdomsforhold som er årsak til den økte uførheten din. <Fritekst: Konkret begrunnelse>" },
                        nynorsk { + "Vi har kome fram til at det er andre sjukdomsforhold som er årsak til den auka uførleiken din. <Fritekst: Konkret begrunnelse>" },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = true AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) = "stdbegr_12_17_1_o_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest() and FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse()).equalTo("stdbegr_12_17_1_o_3") and FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt") and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))){
                //[TBU3731NN, TBU3731]

                paragraph {
                    text (
                        bokmal { + "Deler av uføretrygden din vil fortsatt bli beregnet etter særbestemmelser, noe som gir deg en høyere uføretrygd." },
                        nynorsk { + "Deler av uføretrygda di vil fortsatt bli rekna ut etter særreglar, noko som gir deg ei høgare uføretrygd." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = false AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) = "stdbegr_12_17_1_o_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse()).equalTo("stdbegr_12_17_1_o_3") and FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt") and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))){
                //[TBU3732NN, TBU3732]

                paragraph {
                    text (
                        bokmal { + "Deler av uføretrygden din vil fortsatt bli beregnet etter særbestemmelser, dersom dette er til fordel for deg." },
                        nynorsk { + "Deler av uføretrygda di vil fortsatt bli rekna ut etter særreglar, dersom dette er til fordel for deg." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = false AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) = "stdbegr_12_17_1_o_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse()).equalTo("stdbegr_12_17_1_o_3") and FUNKSJON_FF_GetArrayElement_String(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt"))){
                includePhrase(TBU1265_Generated)
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "ikke_oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) = "stdbegr_12_17_1_i_1") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("ikke_oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse()).equalTo("stdbegr_12_17_1_i_1"))){
                paragraph {
                    text (
                        bokmal { + "Du har ikke en godkjent yrkesskade eller yrkessykdom. Uføretrygden din vil derfor ikke bli beregnet etter særbestemmelser for yrkesskade eller yrkessykdom. <FRITEKST: Konkret begrunnelse>." },
                        nynorsk { + "Du har ikkje ein godkjend yrkesskade eller yrkessjukdom. Uføretrygda di blir derfor ikkje innvilga etter reglar for yrkesskade eller yrkessjukdom. <Fritekst: Konkret begrunnelse>." },
                    )
                }
            }

            title1 {
                text (
                    bokmal { + "Dette er virkningstidspunktet ditt" },
                    nynorsk { + "Dette er verknadstidspunktet ditt" },
                )
            }


            //IF(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneResultat(1) = "oppfylt" AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse(1) = "stdbegr_22_12_1_1") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevneresultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_1"))){
                //[TBU1301NN, TBU1301]
                paragraph {
                    text (
                        bokmal { + "Du har fått innvilget økt uføretrygd fra " + pe.vedtaksdata_virkningfom().format() + ". Dette kaller vi virkningstidspunktet. Fram til dette vil du få arbeidsavklaringspenger." },
                        nynorsk { + "Du har fått innvilga auka uføretrygd frå " + pe.vedtaksdata_virkningfom().format() + ". Dette kallar vi verknadstidspunktet. Fram til dette kjem du til å få arbeidsavklaringspengar." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneResultat(1) = "oppfylt" AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse(1) = "stdbegr_22_12_1_2") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevneresultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_2"))){
                //[TBU1302NN, TBU1302]
                paragraph {
                    text (
                        bokmal { + "Du har fått innvilget økt uføretrygd fra " + pe.vedtaksdata_virkningfom().format() + ". Dette kaller vi virkningstidspunktet. Arbeidsavklaringspengene utbetales fram til <FRITEKST: dato for opphør> og uføretrygd utbetales for de gjenstående dagene i " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_mndvirkningstidpunkt().format() + "." },
                        nynorsk { + "Du har fått innvilga auka uføretrygd frå " + pe.vedtaksdata_virkningfom().format() + ". Dette kallar vi verknadstidspunktet. Arbeidsavklaringspengane blir betalte ut fram til <FRITEKST: Dato for opphør>, og uføretrygd blir betalt ut for dei dagane som er att i " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_mndvirkningstidpunkt().format() + "." },
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_3") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_3"))){
                //[TBU1303NN, TBU1303]

                paragraph {
                    text (
                        bokmal { + "Du har fått innvilget økt uføretrygd fra " + pe.vedtaksdata_virkningfom().format() + ". Dette kaller vi virkningstidspunktet. Du vil få sykepenger fram til <FRITEKST: dato for opphør>. I denne måneden får du utbetalt den delen av sykepengene med det beløp som overstiger uføretrygden." },
                        nynorsk { + "Du har fått innvilga auka uføretrygd frå " + pe.vedtaksdata_virkningfom().format() + ". Dette kallar vi verknadstidspunktet. Du får sjukepengar fram til <FRITEKST: Dato for opphør>. I denne månaden får du utbetalt den delen av sjukepengane som overstig uføretrygda." },
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_4") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_4"))){
                //[TBU1304NN, TBU1304]

                paragraph {
                    text (
                        bokmal { + "Du har fått innvilget økt uføretrygd fra " + pe.vedtaksdata_virkningfom().format() + ". Dette kaller vi virkningstidspunktet. For å ha rett til uføretrygd må du ha fylt 18 år. Du får derfor uføretrygd fra måneden etter at du fyller 18 år." },
                        nynorsk { + "Du har fått innvilga auka uføretrygd frå " + pe.vedtaksdata_virkningfom().format() + ". Dette kallar vi verknadstidspunktet. For å ha rett til uføretrygd må du ha fylt 18 år. Du får derfor uføretrygd frå månaden etter at du fyller 18 år." },
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                //[TBU1305NN, TBU1305]

                paragraph {
                    text (
                        bokmal { + "Du har fått innvilget økt uføretrygd fra " + pe.vedtaksdata_virkningfom().format() + ". Dette kaller vi virkningstidspunktet. Vi mottok søknaden din " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Dersom vilkårene for rett til uføretrygd var oppfylt før dette, kan uføretrygden innvilges opptil tre måneder før denne datoen.<FRITEKST>." },
                        nynorsk { + "Du har fått innvilga auka uføretrygd frå " + pe.vedtaksdata_virkningfom().format() + ". Dette kallar vi verknadstidspunktet. Vi fekk søknaden din " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Dersom vilkåra for rett til uføretrygd var oppfylte før dette, kan vi innvilge uføretrygd for opptil tre månader før denne datoen.<FRITEKST>." },
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse,1) = "stdbegr_22_12_1_13") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_13"))){
                //[TBU3021NNx, TBU3021x]

                paragraph {
                    text (
                        bokmal { + "Du har fått innvilget økt uføretrygd fra " + pe.vedtaksdata_virkningfom().format() + ". Dette kaller vi virkningstidspunktet. Du har rett til uføretrygd fra den måneden vilkårene er oppfylt." },
                        nynorsk { + "Du har fått innvilga auka uføretrygd frå " + pe.vedtaksdata_virkningfom().format() + ". Dette kallar vi verknadstidpunktet. Du har rett til uføretrygd frå den månaden vilkåra er oppfylde." },
                    )
                }
            }

            title1 {
                text (
                    bokmal { + "Dette er uføretidspunktet ditt" },
                    nynorsk { + "Dette er uføretidspunktet ditt" },
                )
            }
            //[TBU1273NN, TBU1273]

            paragraph {
                text (
                    bokmal { + "Vi har tidligere fastsatt uføretidspunktet ditt til <FRITEKST: Første Uforetidspunkt>. Når uføregraden øker, fastsetter vi et nytt uføretidspunkt. Det nye uføretidspunktet ditt er " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt().format() + "." },
                    nynorsk { + "Vi har tidlegare fastsett uføretidspunktet ditt til <Fritekst Første Uforetidspunkt>. Når uføregraden aukar, fastset vi eit nytt uføretidspunkt. Det nye uføretidspunktet ditt er " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt().format() + "." },
                )
            }
            //[TBU1274NN, TBU1274]

            paragraph {
                text (
                    bokmal { + "Vi sammenligner beregningen av uføretrygden ut fra det tidspunktet som først ble fastsatt, og det nye uføretidspunktet. Du får det alternativet som gir deg høyest uføretrygd. Det er uføretidspunktet som bestemmer hvilke inntektsår vi legger til grunn for beregningen din." },
                    nynorsk { + "Vi samanliknar berekninga av uføretrygda ut frå det tidspunktet som først blei fastsett, og det nye uføretidspunktet. Du får det alternativet som gir deg høgast uføretrygd. Det er uføretidspunktet som bestemmer kva inntektsår vi legg til grunn for berekninga di." },
                )
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforetidspunkt <> FF_GetArrayElement_Date(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt)) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforetidspunkt().notEqualTo(FUNKSJON_FF_GetArrayElement_Date(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt())))){
                //[TBU1275NN, TBU1275]

                paragraph {
                    text (
                        bokmal { + "Du får fortsatt beregnet uføretrygden din ut fra tidligere uføretidspunkt, fordi dette gir deg en høyere uføretrygd." },
                        nynorsk { + "Du får framleis rekna ut uføretrygda di ut frå tidlegare uføretidspunkt, fordi dette gir deg ei høgare uføretrygd." },
                    )
                }
            }

            //IF(DateDiff("d", PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforetidspunkt,   FF_GetArrayElement_Date(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt)) = 0) THEN      INCLUDE ENDIF
            showIf((FUNKSJON_DateDiff("d",pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforetidspunkt(),FUNKSJON_FF_GetArrayElement_Date(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt())).equalTo(0))){
                //[TBU1276NN, TBU1276]

                paragraph {
                    text (
                        bokmal { + "Du får beregnet uføretrygden din ut fra nytt uføretidspunkt, fordi dette gir deg en høyere uføretrygd." },
                        nynorsk { + "Du får rekna ut uføretrygda di ut frå nytt uføretidspunkt, fordi dette gir deg ei høgare uføretrygd." },
                    )
                }
            }
            //[TBU1185NN, TBU1185]

            title1 {
                text (
                    bokmal { + "Slik har vi fastsatt uføregraden din" },
                    nynorsk { + "Slik har vi fastsett uføregraden din" },
                )
            }
            paragraph {
                text (
                    bokmal { + "Vi har sammenliknet inntektsmulighetene dine før og etter at du ble ufør, og vurdert hvor mye inntektsevnen din er varig nedsatt." },
                    nynorsk { + "Vi har samanlikna inntektsmoglegheitene dine før og etter at du blei ufør, og vurdert kor mykje inntektsevna di er varig nedsett." },
                )
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_1" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "stdbegr_12_8_1_3") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_1") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("stdbegr_12_8_1_3"))){
                //[TBU1290NN, TBU1290]

                paragraph {
                    text (
                        bokmal { + "Inntekten din før du ble ufør er fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner på uføretidspunktet i " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt().format() + ". <Fritekst: begrunnelse for fastsatt IFU>. " },
                        nynorsk { + "Inntekta di før du blei ufør, er fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner på uføretidspunktet i " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt().format() + ". <Fritekst: begrunnelse for fastsatt IFU>. " },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu > FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt)) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oifu().greaterThan((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt())))){
                        text (
                            bokmal { + "Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + pe.ut_oifuperiode_else_oifu().format() + " kroner." },
                            nynorsk { + "Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + pe.ut_oifuperiode_else_oifu().format() + " kroner." },
                        )
                    }
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "stdbegr_12_8_1_3") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_3") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("stdbegr_12_8_1_3"))){
                //[TBU1292_1NN, TBU1292_1]

                paragraph {
                    text (
                        bokmal { + "Du hadde begrenset yrkesaktivitet og inntekt før du ble ufør. Inntekten din før du ble ufør er derfor tidligere fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner på uføretidspunktet ditt. Dette er for å garantere deg et minstenivå på inntekt før uførhet. Dette minstenivået skal tilsvare 3,3 ganger grunnbeløpet." },
                        nynorsk { + "Du hadde avgrensa yrkesaktivitet og inntekt før du blei ufør. Inntekta di før du blei ufør, er fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner på uføretidspunktet i " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt().format() + ". Dette er for å garantere deg eit minstenivå på inntekt før uførleik. Dette minstenivået skal svare til 3,3 gonger grunnbeløpet." },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu > PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt(1)) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oifu().greaterThan(FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt(1)))){
                        text (
                            bokmal { + " Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + pe.ut_oifuperiode_else_oifu().format() + " kroner." },
                            nynorsk { + " Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + pe.ut_oifuperiode_else_oifu().format() + " kroner." },
                        )
                    }
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_5" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "stdbegr_12_8_1_3") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_5") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("stdbegr_12_8_1_3"))){
                //[TBU1292_2NN, TBU1292_2]

                paragraph {
                    text (
                        bokmal { + "Du hadde begrenset yrkesaktivitet og inntekt før du ble ufør. Inntekten din før du ble ufør er derfor tidligere fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner på uføretidspunktet i " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforetidspunkt().format() + ". Dette er for å garantere deg et minstenivå på inntekt før uførhet. Dette minstenivået skal tilsvare 3,5 ganger folketrygdens grunnbeløp." },
                        nynorsk { + "Du hadde avgrensa yrkesaktivitet og inntekt før du blei ufør. Inntekta di før du blei ufør, er fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner på uføretidspunktet i " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt().format() + ". Dette er for å garantere deg eit minstenivå på inntekt før uførleik. Dette minstenivået skal svare til 3,5 gonger grunnbeløpet." },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu > PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt(1)) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oifu().greaterThan(FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt(1)))){
                        text (
                            bokmal { + " Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + pe.ut_oifuperiode_else_oifu().format() + " kroner." },
                            nynorsk { + " Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + pe.ut_oifuperiode_else_oifu().format() + " kroner." },
                        )
                    }
                }
            }

            //IF((FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat,1) = true) AND (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse,1) = "stdbegr_12_8_2_4")  AND (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse,1) <> "stdbegr_12_8_1_3"))  THEN      INCLUDE ENDIF
            showIf((((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat())) and ((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_4")) and ((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("stdbegr_12_8_1_3")))){
                paragraph {
                    text (
                        bokmal { + "Inntekten din før du ble ufør er fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. Vi har innvilget deg rettighet som ung ufør, og inntekten din før du ble ufør skal derfor tilsvare minst 4,5 ganger grunnbeløpet." },
                        nynorsk { + "Inntekta di før du blei ufør, er fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. Vi har innvilga deg rettar som ung ufør, og inntekta di før du blei ufør skal derfor svare til minst 4,5 gonger grunnbeløpet." },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu > FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt)) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oifu().greaterThan((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt())))){
                        text (
                            bokmal { + " Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + pe.ut_oifuperiode_else_oifu().format() + " kroner." },
                            nynorsk { + " Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + pe.ut_oifuperiode_else_oifu().format() + " kroner." },
                        )
                    }
                }
            }

            //IF((FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "") AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100  AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt) = 0  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "stdbegr_12_8_1_3"  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "stdbegr_12_8_2_2"  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "stdbegr_12_8_2_9"  ) THEN      INCLUDE ENDIF
            showIf((((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo("") or (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("")) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().equalTo(100) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()).equalTo(0) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("stdbegr_12_8_1_3") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo("stdbegr_12_8_2_2") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo("stdbegr_12_8_2_9"))){
                paragraph {
                    text (
                        bokmal { + "Du har ikke inntekt i dag, og vi har derfor fastsatt uføregraden din til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent." },
                        nynorsk { + "Du har ikkje inntekt i dag, og vi har derfor fastsett uføregraden din til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent." },
                    )
                }
            }

            //IF( ( FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "" ) AND ((PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100) OR ( PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100 AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt) > 0 )) AND  FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "stdbegr_12_8_1_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "stdbegr_12_8_2_2"  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "stdbegr_12_8_2_9" ) THEN      INCLUDE ENDIF
            showIf((((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo("") or (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("")) and ((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().lessThan(100)) or (pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().equalTo(100) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()).greaterThan(0))) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()).notEqualTo("stdbegr_12_8_1_3") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo("stdbegr_12_8_2_2") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).notEqualTo("stdbegr_12_8_2_9"))){
                paragraph {
                    text (
                        bokmal { + "Du har en inntekt på " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format() + " kroner, og vi har derfor fastsatt uføregraden din til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent." },
                        nynorsk { + "Du har ei inntekt på " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format() + " kroner, og vi har derfor fastsett uføregraden din til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse(1) = "stdbegr_12_8_1_3") THEN      INCLUDE ENDIF
            showIf((FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse(1).equalTo("stdbegr_12_8_1_3"))){
                //[TBU1294NN, TBU1294]

                paragraph {
                    text (
                        bokmal { + "Vi har fastsatt inntekten din før du ble ufør til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner på uføretidspunktet i " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt().format() + ". <Fritekst: begrunnelse for fastsatt IFU>." },
                        nynorsk { + "Vi har fastsett inntekta di før du blei ufør til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner på uføretidspunktet i " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt().format() + ". <Fritekst: begrunnelse for fastsatt IFU>." },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu > PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt(1)) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oifu().greaterThan(FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt(1)))){
                        text (
                            bokmal { + " Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + pe.ut_oifuperiode_else_oifu().format() + " kroner." },
                            nynorsk { + " Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + pe.ut_oifuperiode_else_oifu().format() + " kroner." },
                        )
                    }
                }
                paragraph {
                    text (
                        bokmal { + "Det er dokumentert at du har inntektsmuligheter som du ikke benytter. Disse tar vi med når vi fastsetter inntekten din etter at du ble ufør. Inntekten din etter at du ble ufør er fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format() + " kroner, og uføregraden din er derfor fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent." },
                        nynorsk { + "Det er dokumentert at du har inntektsmoglegheiter som du ikkje nyttar. Desse tek vi med når vi fastset inntekta di etter at du blei ufør. Inntekta di etter at du blei ufør, er fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format() + " kroner, og uføregraden din er derfor fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent." },
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_2" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_9") THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_2") or (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()).equalTo("stdbegr_12_8_2_9"))){
                paragraph {
                    text (
                        bokmal { + "Vi har fastsatt inntekten din før du ble ufør til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. Inntekten din før du ble ufør er fastsatt ut fra stillingsandelen din, og forventet inntekt på " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format() + " kroner. Inntekten din etter at du ble ufør er derfor fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format() + " kroner og uføregraden din blir " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent." },
                        nynorsk { + "Vi har fastsett inntekta di før du blei ufør til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. Inntekta di før du blei ufør, er fastsett ut frå stillingsdelen din og forventa inntekt på " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format() + " kroner. Inntekta di etter at du blei ufør, er derfor fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format() + " kroner, og uføregraden din blir " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent." },
                    )
                }
            }
            includePhrase(TBU1133_Generated)

            //PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100
            showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().equalTo(100)){
                includePhrase(TBU1201_Generated)
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().lessThan(100))){
                includePhrase(TBU1203_Generated)
            }
            includePhrase(TBU1204_Generated)

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) = 100 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense = (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop*0.4)) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad()).equalTo(100) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().equalTo((/* TODO multiplication */)))){
                includePhrase(TBU1205_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense = 60000) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().equalTo(100) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().equalTo(60000))){
                includePhrase(TBU1296_Generated)
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense()))){
                includePhrase(TBU1206_Generated(pe))
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad,1) < 100 AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad,1) > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> 60000 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad()).lessThan(100) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad()).greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(60000) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()))){
                includePhrase(TBU1207_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense = 60000 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().equalTo(60000) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().lessThan(100))){
                includePhrase(TBU2357_Generated(pe))
            }
            includePhrase(TBU1208_Generated(pe))
            includePhrase(TBU1210_Generated(pe))

            //IF(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland(1) <> "nor" AND PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland(1) <> "") THEN      INCLUDE ENDIF
            showIf(bostedutland){
                paragraph {
                    text (
                        bokmal { + "Inntektsgrensen gjelder bare for den norske uføretrygden din. Har du spørsmål om inntektsgrensen i et annet land, må du kontakte trygdemyndighetene i det landet det gjelder." },
                        nynorsk { + "Inntektsgrensa gjeld berre for den norske uføretrygda di. Har du spørsmål om inntektsgrensa i eit anna land, må du kontakte trygdestyresmaktene i det landet det gjeld." },
                    )
                }
            }
            includePhrase(TBU2364_Generated)
            includePhrase(TBU2365_Generated)

            //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"
            showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs")){
                title1 {
                    text (
                        bokmal { + "Utbetaling av uføretrygd for deg som er innlagt på institusjon" },
                        nynorsk { + "Utbetaling av uføretrygd når du er innlagd på institusjon" },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  OR  PE_UT_Forsorgeransvar_ingen_er_false() OR  PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = true)) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() or pe.ut_forsorgeransvar_ingen_er_false() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(Kroner(0))))){
                //[TBU3104]
                paragraph {
                    text (
                        bokmal { + "Vi reduserer ikke utbetalingen av uføretrygden din verken i innleggelsesmåneden eller de tre påfølgende månedene når du er innlagt på institusjon. Uføretrygden din blir deretter redusert og skal under oppholdet utgjøre 14 prosent av uføretrygden inntil institusjonsoppholdet avsluttes. Totalt sett skal utbetalingen likevel utgjøre minst 45 prosent av folketrygdens grunnbeløp, før skatt." },
                        nynorsk { + "Vi reduserer ikkje utbetalinga av uføretrygda di verken i innleggingsmånaden eller dei tre påfølgjande månadene når du er innlagd på institusjon. Uføretrygda di blir deretter redusert og skal under opphaldet utgjere 14 prosent av uføretrygda inntil institusjonsopphaldet blir avslutta. Totalt sett skal utbetalinga likevel utgjere minst 45 prosent av grunnbeløpet i folketrygda før skatt." },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() or pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())){
                        text (
                            bokmal { + " Dersom du mottar " },
                            nynorsk { + " Dersom du får " },
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

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  OR  PE_UT_Forsorgeransvar_ingen_er_false() OR  PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = true)) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() or pe.ut_forsorgeransvar_ingen_er_false() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(Kroner(0))))){
                paragraph {
                    text (
                        bokmal { + "Dersom du har faste og nødvendige utgifter til bolig, kan vi vurdere om uføretrygden din kan reduseres mindre. Du må sende inn dokumentasjon på dine utgifter til Nav. Forsørger du barn " },
                        nynorsk { + "Dersom du har faste og nødvendige utgifter til bustad, vil vi vurdere en lågare reduksjon av uføretrygda di. Du må sende inn dokumentasjon på utgiftene dine til Nav. Viss du forsørgjer barn" },
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
                    showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()){
                        text (
                            bokmal { + "og/eller ektefelle " },
                            nynorsk { + " og/eller ektefelle" },
                        )
                    }
                    text (
                        bokmal { + "under innleggelsen på institusjonen, vil vi ikke redusere uføretrygden din." },
                        nynorsk { + " mens du er lagd inn på institusjonen, reduserer vi ikkje uføretrygda di." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false AND PE_UT_Forsorgeransvar_ingen_er_true() AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.ut_forsorgeransvar_ingen_er_true() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                paragraph {
                    text (
                        bokmal { + "Uføretrygden din er lavere enn 45 prosent av folketrygdens grunnbeløp. Du vil derfor ikke få redusert utbetaling av uføretrygden din når du er innlagt på institusjon." },
                        nynorsk { + "Uføretrygda di er lågare enn 45 prosent av grunnbeløpet i folketrygda. Du får derfor ikkje redusert utbetaling av uføretrygda di når du er innlagd på institusjon." },
                    )
                }
            }

            //IF(PE_UT_Forsorgeransvar_ingen_er_false() AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0) THEN      INCLUDE ENDIF
            showIf((pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0))){
                paragraph {
                    text (
                        bokmal { + "Du forsørger barn" },
                        nynorsk { + "Du forsørgjer barn " },
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
                    showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()){
                        text (
                            bokmal { + " og/eller ektefelle" },
                            nynorsk { + "og/eller ektefelle " },
                        )
                    }
                    text (
                        bokmal { + " under oppholdet ditt i institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { + "under opphaldet ditt på institusjon. Vi har derfor kome fram til at utbetalinga di derfor ikkje skal reduserast." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND PE_UT_Forsorgeransvar_ingen_er_true()) THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.ut_forsorgeransvar_ingen_er_true())){
                paragraph {
                    text (
                        bokmal { + "Du har dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { + "Du har dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har kome fram til at utbetalinga di derfor ikkje skal reduserast." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0 AND PE_UT_Forsorgeransvar_ingen_er_false() AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                paragraph {
                    text (
                        bokmal { + "Du har dokumentert at du har faste og nødvendige utgifter til bolig, og du forsørger barn " },
                        nynorsk { + "Du har dokumentert at du har faste og nødvendige utgifter til bustad, og du forsørgjer barn" },
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
                    showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()){
                        text (
                            bokmal { + "og/eller ektefelle " },
                            nynorsk { + " og/eller ektefelle" },
                        )
                    }
                    text (
                        bokmal { + "under oppholdet ditt i institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { + " under opphaldet ditt på institusjon. Vi har derfor kome fram til at utbetalinga di ikkje skal reduserast." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                paragraph {
                    text (
                        bokmal { + "Du har dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at utbetalingen din skal reduseres til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                        nynorsk { + "Du har dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har kome fram til at utbetalinga di skal reduserast til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0  AND PE_UT_Forsorgeransvar_ingen_er_true()) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0) and pe.ut_forsorgeransvar_ingen_er_true())){
                paragraph {
                    text (
                        bokmal { + "Du forsørger ikke barn" },
                        nynorsk { + "Du forsørgjer ikkje barn" },
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
                    showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()){
                        text (
                            bokmal { + " og/eller ektefelle" },
                            nynorsk { + " og/eller ektefelle" },
                        )
                    }
                    text (
                        bokmal { + ", og det er ikke dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at uføretrygden din skal reduseres til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                        nynorsk { + ", og det er ikkje dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har derfor kome fram til at uføretrygda di skal reduserast til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                    )
                }
            }

            //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"
            showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo")){
                paragraph {
                    text (
                        bokmal { + "Utbetaling av uføretrygd for deg som er under straffegjennomføring" },
                        nynorsk { + "Utbetaling av uføretrygd når du er under straffegjennomføring" },
                    )
                }
            }

            //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"
            showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo")){
                //[TBU3115]
                paragraph {
                    text (
                        bokmal { + "Uføretrygden din er redusert fordi du er under straffegjennomføring." },
                        nynorsk { +"Uføretrygda di er redusert fordi du er under straffegjennomføring." },
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
                            nynorsk { +"Da du forsørgjer barn" },
                        )
                    }

                    //IF(FF_GetArrayElement_Boolean(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphReduksjonsperiodeListe_InstOpphReduksjonsperiode_Forsorgeransvar) = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphreduksjonsperiodeliste_instopphreduksjonsperiode_forsorgeransvar()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget())){
                        text (
                            bokmal { + " og/eller ektefelle" },
                            nynorsk { +" og/eller ektefelle" },
                        )
                    }

                    //IF(PE_UT_Forsorgeransvar_siste_er_true()) THEN      INCLUDE ENDIF
                    showIf((pe.ut_forsorgeransvar_siste_er_true())){
                        text (
                            bokmal { + ", vil utbetaling av uføretrygden din reduseres med 50 prosent. " },
                            nynorsk { +", vil utbetalinga av uføretrygda di reduserast med 50 prosent. " },
                        )
                    }
                    text (
                        bokmal { + "Utbetalingen din er redusert fra andre måned etter at straffegjennomføring tok til. Når straffegjennomføring er avsluttet, vil vi ikke lenger redusere uføretrygden din. " },
                        nynorsk { + "Utbetalinga di er redusert frå den andre månaden etter at straffegjennomføringa tok til. Når straffegjennomføringa er avslutta, vil vi ikkje lenger redusere uføretrygda di. " },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() or pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())){
                        text (
                            bokmal { + "Dersom du mottar " },
                            nynorsk { +"Dersom du mottar " },
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

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())){
                includePhrase(TBU3800_Generated)
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumUTBT > PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_GradertOppjustertIFU AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType <> "overgangsregler_2016" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom >= DateValue("01/01/2016")) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumutbt().greaterThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().notEqualTo("overgangsregler_2016") and pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().greaterThanOrEqual(FUNKSJON_DateValue("01/01/2016")))){
                //[TBU3803]
                paragraph {
                    text (
                        bokmal { + "Uføretrygden og barnetillegget ditt kan til sammen ikke utgjøre mer enn 95 prosent av inntekten din før du ble ufør. 95 prosent av den inntekten du hadde før du ble ufør tilsvarer i dag en inntekt på " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format() + " kroner. Uføretrygden og barnetillegget ditt er til sammen høyere enn dette. Årlig barnetillegg før reduksjon ut fra inntekt blir " },
                        nynorsk { + "Uføretrygda di og barnetillegget ditt kan til saman ikkje utgjere meir enn 95 prosent av inntekta di før du blei ufør. 95 prosent av den inntekta du hadde før du blei ufør, tilsvarer i dag ei inntekt på " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format() + " kroner. Uføretrygda di og barnetillegget ditt er til saman høgare enn dette. Brutto årleg barnetillegg før reduksjon ut frå inntekt blir " },
                    )

                    //IF( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT > 0) THEN     INCLUDE END IF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().greaterThan(0))){
                        text (
                            bokmal { + "derfor redusert til " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().format() + " kroner. " },
                            nynorsk { + "derfor redusert til " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().format() + " kroner. " },
                        )
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0) THEN     INCLUDE END IF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0))){
                        text (
                            bokmal { + "ikke utbetalt. " },
                            nynorsk { + "ikkje utbetalt. " },
                        )
                    }

                    //IF( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT > 0) THEN     INCLUDE END IF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().greaterThan(0))){
                        text (
                            bokmal { + "Har du inntekt ved siden av uføretrygden vil dette også kunne ha betydning for størrelsen på barnetillegget ditt." },
                            nynorsk { + "Har du inntekt ved sida av uføretrygda, kan dette også ha noko å seie for storleiken på barnetillegget ditt." },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_AndelYtelseAvOIFU > 95) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016") and pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_andelytelseavoifu().greaterThan(95))){
                includePhrase(TBU3802_Generated(pe))
            }

            //IF (   NOT   (     PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0     AND     PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0   ) ) THEN   INCLUDE ENDIF
            showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0)))){

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
                                bokmal { + "inntektene til deg og ektefellen, partneren eller samboeren din " },
                                nynorsk { + "inntektene til deg og ektefella, partneren eller samboaren din " },
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
                            bokmal { + "Inntekten din er " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning().format() + " kroner og inntekten til ektefellen, partneren eller samboeren din er " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format() + " kroner. " },
                            nynorsk { + "Inntekta di er " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning().format() + " kroner, og inntekta til ektefella, partneren eller samboaren din er " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format() + " kroner. " },
                        )

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbelopFratrukketAnnenForeldersInntekt > 0) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt().greaterThan(0))){
                            text (
                                bokmal { + "Folketrygdens grunnbeløp på inntil " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner er holdt utenfor inntekten til ektefellen, partneren eller samboeren din. " },
                                nynorsk { + "Grunnbeløpet i folketrygda på inntil " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner er halde utanfor inntekta til ektefella, partneren eller samboaren din. "}
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
                                nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
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
                                nynorsk { + "Inntekta di er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner." },
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
                                bokmal { + "inntektene til deg og ektefellen, partneren eller samboeren din " + pe.ut_bruttoetterreduksjonbt_høyere_lavere() + " enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre. " },
                                nynorsk { + "inntektene til deg og ektefella, partneren eller samboaren din " + pe.ut_bruttoetterreduksjonbt_høyere_lavere() + " enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine. " },
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
                                nynorsk { + "Barnetillegga er derfor" },
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
                                nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                            )
                        }
                    }
                }

                //IF( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) ) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) or not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())))){
                    paragraph {
                        text (
                            bokmal { + "Barnetillegget " },
                            nynorsk { + "Barnetillegget " },
                        )

                        //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                        showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()){
                            text (
                                bokmal { + "for " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene, " },
                                nynorsk { + "for " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra sine, " },
                            )
                        }
                        text (
                            bokmal { + "blir ikke utbetalt fordi du " },
                            nynorsk { + "blir ikkje utbetalt fordi du " },
                        )

                        //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                        showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()){
                            text (
                                bokmal { + "alene " },
                                nynorsk { + "åleine " },
                            )
                        }
                        text (
                            bokmal { + "har en samlet inntekt som er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + " kroner. Inntekten din er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { + "har ei samla inntekt som er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + " kroner. Inntekta di er over grensa for å få utbetalt barnetillegg. " },
                        )
                    }
                }

                //IF( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false) ) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) or not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())))){
                    paragraph {
                        text (
                            bokmal { + "Barnetillegget" },
                            nynorsk { + "Barnetillegget " },
                        )

                        //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true
                        showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()){
                            text (
                                bokmal { + " for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre," },
                                nynorsk { + "for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine, " },
                            )
                        }
                        text (
                            bokmal { + " blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + " kroner. De samlede inntektene er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { + "blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + " kroner. Dei samla inntektene er over grensa for å få utbetalt barnetillegg." },
                        )
                    }
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

            //IF (PE_UT_KravLinjeKode_VedtakResultat_forekomst_bt_innv()) THEN INCLUDE ENDIF
            showIf((pe.ut_kravlinjekode_vedtakresultat_forekomst_bt_innv().greaterThan(0))){
                includePhrase(TBU5005_Generated)
                includePhrase(TBU5007_Generated)
            }

            //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
            showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()){
                includePhrase(TBU1214_Generated)
                paragraph {
                    text (
                        bokmal { + "Du er innvilget uføretrygd med gjenlevendetillegg. Tillegget er beregnet etter ditt eget og din avdøde ektefelles beregningsgrunnlag og trygdetid. Tjener du mer enn inntektsgrensen din, reduserer vi gjenlevendetillegget ditt med samme prosent som vi reduserer uføretrygden din med. " },
                        nynorsk { + "Du er innvilga uføretrygd med attlevandetillegg. Tillegget er rekna ut etter utrekningsgrunnlaget og trygdetida både for deg og for den avdøde ektefellen din. Tener du meir enn inntektsgrensa di, reduserer vi attlevandetillegget ditt med same prosent som vi reduserer uføretrygda di med. " },
                    )

                    //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_NyttGjenlevendetillegg = true
                    showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg()){
                        text (
                            bokmal { + "Tillegget er tidsbegrenset til fem år fra virkningstidspunktet. " },
                            nynorsk { + "Tillegget er tidsbegrensa til fem år frå verknadstidspunktet. " },
                        )
                    }
                }
            }

            //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
            showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()){
                includePhrase(TBU1133_Generated)
            }

            //IF(PE_SaksData_SakAPogUP = true AND PE_SaksData_SakAPStatus = "lopende") THEN      INCLUDE ENDIF
            showIf(pe.vedtaksdata_harLopendealderspensjon()){
                paragraph {
                    text (
                        bokmal { + "For deg som kombinerer uføretrygd og alderspensjon" },
                        nynorsk { + "For deg som kombinerer uføretrygd og alderspensjon" },
                    )
                    text (
                        bokmal { + "Du mottar alderspensjon fra folketrygden. Hvis du kombinerer uføretrygd og alderspensjon kan disse til sammen ikke utgjøre mer enn 100 prosent." },
                        nynorsk { + "Du mottar alderspensjon frå folketrygda. Viss du kombinerer uføretrygd og alderspensjon, kan den totale prosenten ikkje vere høgare enn 100 prosent." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_onsketVirkningsDato < PE_VedtakFattetDato_minus_1mnd) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_belopokt() and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()) and onsketvirkningsdato.legacyLessThan(pe.vedtakfattetdato_minus_1mnd()))){
                paragraph {
                    text (
                        bokmal { + "Etterbetaling av uføretrygd" },
                        nynorsk { + "Etterbetaling av uføretrygd" },
                    )
                    text (
                        bokmal { + "Du får etterbetalt uføretrygd fra " + onsketvirkningsdato.format() + ". Beløpet blir vanligvis utbetalt i løpet av sju virkedager. Det kan bli beregnet fradrag i etterbetalingen for skatt og ytelser du har mottatt fra Nav eller andre, som for eksempel tjenestepensjonsordninger. I disse tilfellene kan etterbetalingen bli forsinket med inntil ni uker. Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen." },
                        nynorsk { + "Du får etterbetalt uføretrygd frå " + onsketvirkningsdato.format() + ". Beløpet blir vanlegvis utbetalt innan sju vyrkedagar. Det kan bli rekna ut frådrag i etterbetalinga for skatt og ytingar du har fått frå Nav eller andre, som til dømes tenestepensjonsordningar. I desse tilfella kan etterbetalinga bli forseinka med inntil ni veker. Frådrag i etterbetalinga kjem fram av utbetalingsmeldinga." },
                    )
                }
            }

            //IF(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland(1) <> "nor" AND PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland(1) <> "") THEN      INCLUDE ENDIF
            showIf(bostedutland){
                title1 {
                    text(
                        bokmal { +"Beregningen din kan bli endret" },
                        nynorsk { +"Berekninga di kan bli endra" },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Når vi mottar vedtak fra utenlandske trygdemyndigheter, fatter vi et nytt vedtak der vi gjør en endelig beregning av uføretrygden din. Hvis det viser seg at du har fått utbetalt mer enn du skulle, kan vi kreve at du betaler tilbake det du skylder. Hvis du får etterbetalt penger fra en utenlandsk trygdemyndighet, kan vi trekke det du skylder fra etterbetalingen." },
                        nynorsk { + "Når vi får vedtak frå utanlandske trygdeorgan, fattar vi eit nytt vedtak der vi reknar ut uføretrygda di endeleg. Viss det viser seg at du har fått betalt ut meir enn du skulle ha, kan vi krevje at du betaler tilbake det du skuldar. Viss du får etterbetalt pengar frå eit utanlandsk trygdeorgan, kan vi trekkje det du skuldar frå etterbetalinga." },
                    )
                }
            }

            //IF(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland(1) <> "nor" AND PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland(1) <> "") THEN      INCLUDE ENDIF
            showIf(bostedutland){
                paragraph {
                    text (
                        bokmal { + "<STRYK TEKSTEN UNDER DERSOM DEN IKKE ER AKTUELL>" },
                        nynorsk { + "<STRYK TEKSTEN UNDER DERSOM DEN IKKE ER AKTUELL>" },
                    )
                    text (
                        bokmal { + "En utenlandsk myndighet krever refusjon" },
                        nynorsk { + "Eit utanlandsk organ krev refusjon" },
                    )
                    text (
                        bokmal { + "<FRITEKST: land> har varslet Nav at de kan ha utbetalt for mye penger til deg. De har mulighet til å kreve dette tilbake i etterbetalingen av den norske uføretrygden din. Vi vil holde tilbake etterbetalingen inntil vi har fått svar fra <FRITEKST: land>. Har du spørsmål om dette, kan du ta kontakt med <FRITEKST: nasjonalitet> myndigheter." },
                        nynorsk { + "<FRITEKST: Land> har varsla Nav at dei kan ha betalt ut for mykje pengar til deg. Dei har høve til å krevje dette tilbake i etterbetalinga av den norske uføretrygda di. Vi vil halde tilbake etterbetalinga inntil vi har fått svar frå <FRITEKST: Land>. Har du spørsmål om dette, kan du ta kontakt med <FRITEKST: Nasjonalitet> styresmakter." },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Denne retten har <FRITEKST: land> etter EØS-forordningen 987/2009 artikkel 72." },
                        nynorsk { + "Denne retten har <FRITEKST: Land> etter EØS-forordninga 987/2009 artikkel 72." },
                    )
                }
            }
            title1 {
                text (
                    bokmal { + "Du må melde fra om endringer" },
                    nynorsk { + "Du må melde frå om endringar" },
                )
            }
            paragraph {
                text (
                    bokmal { + "Skjer det endringer, må du melde fra til oss med en gang. I vedlegget «Orientering om rettigheter og plikter» ser du hvilke endringer du må si fra om." },
                    nynorsk { + "Skjer det endringar, må du melde frå til oss med ein gong. I vedlegget «Orientering om rettar og plikter» ser du kva endringar du må seie frå om." },
                )
            }

            title1 {
                text(
                    bokmal { + "Du har rett til å klage " },
                    nynorsk { + "Du har rett til å klage " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du fikk vedtaket. " +
                            "I vedlegget " },
                    nynorsk { + "Dersom du meiner at vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen du fekk vedtaket. " +
                            "I vedlegget " },
                )
                namedReference(vedleggDineRettigheterOgPlikterUfoere)
                text(
                    bokmal { + " får du vite mer om hvordan du går fram. Du finner skjema og informasjon på $KLAGE_URL. " },
                    nynorsk { + " kan du lese meir om korleis du går fram. Du finn skjema og informasjon på $KLAGE_URL. " },
                )
            }

            //IF(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland(1) <> "nor" AND PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland(1) <> "") THEN      INCLUDE ENDIF
            showIf(bostedutland){
                paragraph {
                    text (
                        bokmal { + "Du kan sende klagen direkte til Nav eller gjennom <FRITEKST: utenlandsk trygdemyndighet>." },
                        nynorsk { + "Du kan sende klaga direkte til Nav eller gjennom <FRITEKST: Utenlandsk trygdemyndighet>." },
                    )
                }
            }
            title1 {
                text (
                    bokmal { + "Du har rett til innsyn" },
                    nynorsk { + "Du har rett til innsyn" },
                )
            }
            paragraph {
                text (
                    bokmal { + "Du har rett til å se dokumentene i saken din. Se vedlegg «Orientering om rettigheter og plikter» for informasjon om hvordan du går fram." },
                    nynorsk { + "Du har rett til å sjå dokumenta i saka di. Sjå vedlegg «Orientering om rettar og plikter» for informasjon om korleis du går fram." },
                )
            }

            title1 {
                text(
                    bokmal { +"Sjekk utbetalingene dine" },
                    nynorsk { +"Sjekk utbetalingane dine" },
                )
            }
            paragraph {
                text (
                    bokmal { + "Du får uføretrygd utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. Se alle utbetalinger du har mottatt: $UFOERETRYGD_URL. Her kan du også endre kontonummer." },
                    nynorsk { + "Du får uføretrygd betalt ut den 20. kvar månad eller seinast siste vyrkedag før denne datoen. Sjå alle utbetalingar du har fått: $UFOERETRYGD_URL. Her kan du også endre kontonummer." },
                )
            }

            title1 {
                text(
                    bokmal { +"Skattekort" },
                    nynorsk { +"Skattekort" },
                )
            }
            paragraph {
                text (
                    bokmal { + "Uføretrygd skattlegges som lønnsinntekt. Du trenger ikke levere skattekortet ditt til Nav fordi skatteopplysningene dine sendes elektronisk fra Skatteetaten. Du bør likevel sjekke at du har riktig skattekort. Skattekortet kan du endre på $SKATTEETATEN_URL. Under menyvalget «uføretrygd» når du logger deg inn på $NAV_URL, kan du se hvilket skattetrekk som er registrert hos Nav." },
                    nynorsk { + "Uføretrygd blir skattlagd som lønsinntekt. Du treng ikkje levere skattekortet ditt til Nav, fordi skatteopplysningane dine blir sende elektronisk frå Skatteetaten. Du bør likevel sjekke at du har rett skattekort. Skattekortet kan du endre på $SKATTEETATEN_URL. Under menyvalet «uføretrygd» når du logger deg inn på $NAV_URL, kan du sjå kva skattetrekk som er registrert hos Nav." },
                )
            }

            //IF(FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) <> "nor" AND FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) <> "") THEN      INCLUDE ENDIF
            showIf(bostedutland){
                title1 {
                    text(
                        bokmal { +"Skatt for deg som bor i utlandet" },
                        nynorsk { +"Skatt for deg som bur i utlandet" },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt på $SKATTEETATEN_URL. Hvis du er bosatt i utlandet og betaler skatt i annet land enn Norge, kan du kontakte skattemyndighetene der du bor." },
                        nynorsk { + "Bur du i utlandet og betaler kjeldeskatt, finn du meir informasjon om kjeldeskatt på $SKATTEETATEN_URL. Viss du er busett i utlandet og betaler skatt i eit anna land enn Noreg, kan du kontakte skattemyndigheitene der du bur." },
                    )
                }
            }

            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, pesysData.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pesysData.pe, pesysData.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
