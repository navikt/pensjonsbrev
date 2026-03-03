package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.PESelectors.vedtaksbrev
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoerepensjonDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoerepensjonDtoSelectors.PesysDataSelectors.pe
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoerepensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etnetto
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

//PE_UT_04_001 vedtak Innvilgelse nasjonalt

@TemplateModelHelpers
object InnvilgelseUfoerepensjon : RedigerbarTemplate<InnvilgelseUfoerepensjonDto> {

    //   override val featureToggle = FeatureToggles.brevmalUtInnvilgelseUfoerepensjon.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_INNVILGELSE_UFOEREPENSJON
    override val kategori = Brevkategori.UFOEREPENSJON
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av uførepensjon",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Uførepensjon fra folketrygden - melding om vedtak" },
                nynorsk { +"Uførepensjon frå folketrygda - melding om vedtak" },
                english { +"Disability pension from the National Insurance Scheme - notice of decision" }
            )
        }

        outline {
            val pe = pesysData.pe

//PE_UT_04_001_TBU1902, 1559, 1560, 1903, 1924
            paragraph {
                text(
                    bokmal { +"Nav har innvilget din søknad om uførepensjon mottatt PE_Vedtaksdata_Kravhode_KravMottattDato. Du får uførepensjon med en uføregrad på PE_VedtaksdataBerengningsData_Beregning_Uforegrad prosent fra PE_VedtaksData_VirkningFOM. Du får utbetalt PE_Vedtaksdata_BeregningsData_Beregning_Netto kroner hver måned før skatt." },
                    nynorsk { +"Nav har innvilga søknaden din om uførepensjon motteken PE_Vedtaksdata_Kravhode_KravMottattDato. Du får uførepensjon med ein uføregrad på PE_VedtaksdataBerengningsData_Beregning_Uforegrad prosent frå PE_VedtaksData_VirkningFOM. Du får utbetalt PE_Vedtaksdata_BeregningsData_Beregning_Netto kroner kvar månad før skatt." },
                    english { +"Nav makes reference to your application for a disabililty pension, received on PE_Vedtaksdata_Kravhode_KravMottattDato. You have been granted a disability pension with a disability level of PE_VedtaksdataBerengningsData_Beregning_Uforegrad per cent as of PE_VedtaksData_VirkningFOM. You will receive PE_Vedtaksdata_BeregningsData_Beregning_Netto Norwegian kroner each month, before tax." },
                )
            }
            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETnetto = 0) THEN INCLUDE
            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etnetto().equalTo(0)) {
                paragraph {
                    text(
                        bokmal { +"Du har fått innvilget ektefelletillegg, men det vil ikke komme til utbetaling fordi den samlede inntekten din er for høy." },
                        nynorsk { +"Du har fått innvilga ektefelletillegg, men det vil ikkje komme til utbetaling fordi den samla inntekta di er for høg." },
                        english { +"You have been granted spouse supplement, however you will not receive any additional payment due to your total income being too high." },
                    )
                }
            }
            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
            //AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0
            //AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true
            //AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0)
            //OR
            //(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
            //AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0
            //AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false)
            //OR
            //(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true
            //AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0
            //AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)
            // THEN INCLUDE
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0))
                    or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))
                    or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()))
            ) {
                paragraph {
                    text(
                        bokmal { +"Du har fått innvilget barnetillegg, men det vil ikke komme til utbetaling fordi den samlede inntekten din er for høy." },
                        nynorsk { +"Du har fått innvilga barnetillegg, men det vil ikkje komme til utbetaling fordi den samla inntekta di er for høg." },
                        english { +"You have been granted child supplement, however you will not receive any additional payment due to your total income being too high." },
                    )
                }
            }
            paragraph {
                text(
                    bokmal { +"FRITEKST: Uførepensjon vil første gang bli utbetalt den <fyll inn dato>" },
                    nynorsk { +"FRITEKST: Uførepensjon vil første gong bli utbetalt den <fyll inn dato>." },
                    english { +"FRITEKST: Your first disability pension will be paid on <fyll inn dato>." },
                )
            }
            paragraph {
                text(
                    bokmal { +"FRITEKST: HER KOPIERES BEGRUNNELSEN FRA FORVALTNINGSENHETEN INN" },
                    nynorsk { +"FRITEKST: HER KOPIERES BEGRUNNELSEN FRA FORVALTNINGSENHETEN INN" },
                    english { +"FRITEKST: HER KOPIERES BEGRUNNELSEN FRA FORVALTNINGSENHETEN INN" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter bestemmelsene i folketrygdloven kapittel 12." },
                    nynorsk { +"Vedtaket er gjort etter føresegnene i folketrygdlova kapittel 12." },
                    english { +"This decision has been made in accordance with chapter 12 of the National Insurance Act." },
                )
            }
        }
    }
}

