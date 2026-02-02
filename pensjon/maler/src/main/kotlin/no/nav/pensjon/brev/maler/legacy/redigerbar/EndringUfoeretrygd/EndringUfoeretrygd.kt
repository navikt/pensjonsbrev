package no.nav.pensjon.brev.maler.legacy.redigerbar.EndringUfoeretrygd

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.maler.legacy.PESelectors.vedtaksbrev
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.PesysDataSelectors.pe
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.pesysData
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.safe
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate

//kravArsakType<>"tilst_dod"

//fun Expression<PE>.vedtaksdata_kravhode_onsketvirkningsdato(): Expression<LocalDate?> = vedtaksbrev.safe{ vedtaksdata }.safe{ kravhode }.safe{ onsketvirkningsdato }
//fun Expression<PE>.vedtaksdata_kravhode_kravmottatdato(): Expression<LocalDate> = vedtaksbrev.vedtaksdata.safe{ kravhode }.safe{ kravmottattdato }.ifNull(LocalDate.now())
//fun Expression<PE>.sivilstand_ektefelle_partner_samboer_bormed_ut(): Expression<String> = functions.pe_sivilstand_ektefelle_partner_samboer_bormed_ut.ifNull("")
//fun Expression<PE>.sivilstand_ektefelle_partner_samboer_bormed_ut_en(): Expression<String> = functions.pe_sivilstand_ektefelle_partner_samboer_bormed_ut_en.ifNull("")
//fun Expression<PE>.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner(): Expression<String> = functions.pe_sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner.ifNull("")


/*If PE_Vedtaksdata_Kravhode_KravGjelder <> ("sok_uu" AND "sok_ys")
AND PE_Vedtaksdata_KravArsakType <> ("endring_ifu" AND "barn_endret_inntekt" AND "eps_endret_inntekt" AND "begge_for_end_inn AND "soknad_bt" AND "instopphold" AND "omgj_etter_klage" AND "omgj_etter_anke" AND "gjnl_skal_vurd")
THEN INCLUDE
*/
@TemplateModelHelpers
object EndringUfoeretrygd : RedigerbarTemplate<EndringUfoeretrygdDto> {

    // override val featuretoggle = FeatureToggles.brevmalUtEndring.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_ENDRING_UFOERETRYGD
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Endring av uføretrgd",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val pe = pesysData.pe
        title {
            /* IF PE_Vedtaksdata_Kravhode_KravArsakType <> 'tilst_dod'
            AND NOT ( PE_Vedtaksdata_Kravhode_KravGjelder = "revurd"
            AND   PE_Vedtaksdata_Kravhode_KravArsakType = "soknad_bt"
            AND  PE_UT_KravLinjeKode_Og_PaaFolkgende_bt_avslag_v2() )
            THEN INCLUDE */
            text(
                bokmal { +"Nav har endret uføretrygden din" },
                nynorsk { +"Nav har endret uføretrygden din" },
                english { +"Nav has changed your disability benefit" }
            )
            // IF PE_UT_KravLinjeKode_Og_PaaFolgende_bt_avsl() THEN INCLUDE
            text(
                bokmal { +"Nav har avslått søknaden din om barnetillegg" },
                nynorsk { +"Nav har avslått søknaden din om barnetillegg" },
                english { +"Nav has denied your application for child supplement" }
            )
            /* IF PE_Vedtaksdata_Kravhode_KravArsakType = "tilst_dod"
            AND NOT ( PE_Vedtaksdata_Kravhode_KravGjelder = "revurd"
            AND PE_Vedtaksdata_Kravhode_KravArsakType = "soknad_bt"
            AND  PE_UT_KravLinjeKode_Og_PaaFolkgende_bt_avslag_v2() )
            THEN INCLUDE */
            text(
                bokmal { +"Nav har regnet om uføretrygden din" },
                nynorsk { +"Nav har rekna om uføretrygda di" },
                english { +"Nav has altered your disability benefit" }
            )
        }
        outline {
            /* IF(PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_uu"
            AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_ys"
            AND PE_Vedtaksdata_Kravhode_KravArsakType <> ("endring_ifu" AND "endret_inntekt" AND "barn_endret_inntekt" AND "eps_endret_inntekt"
            AND "begge_for_end_inn" AND "soknad_bt" AND "instopphold" AND "omgj_etter_klage" AND "omgj_etter_anke" AND "gjnl_skal_vurd")) THEN INCLUDE */

            //TBU2287, TBU2287NN, TBU2287EN
            paragraph {
                text(
                    bokmal { +"Vi har endret uføretrygden din fra <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>." },
                    nynorsk { +"Vi har endra uføretrygda di frå <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>." },
                    english { +"We have changed your disability benefit effective as of <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>." },
                )
            }

            /* IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
            OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)
            AND PE_Vedtaksdata_Kravhode_KravArsakType = "soknad_bt") THEN INCLUDE */

            //TBU2288, TBU2288NN, TBU2288EN
            paragraph {
                text(
                    bokmal {
                        +"Vi har innvilget søknaden din om barnetillegg som vi mottok <PE_Vedtaksdata_Kravhode_KravMottatDato>. "
                        +"Vi har endret uføretrygden din fra <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>. "
                        +"Tillegget blir ikke utbetalt fordi inntekten til deg og din <PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT> er over grensen for å få utbetalt barnetillegg. "
                        +"Tillegget blir ikke utbetalt fordi inntekten din er over grensen for å få utbetalt barnetillegg."
                    },
                    nynorsk {
                        +"Vi har innvilga søknaden din om barnetillegg som vi fekk <PE_Vedtaksdata_Kravhode_KravMottatDato>. "
                        +"Vi har endra uføretrygda di frå <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>. "
                        +"Tillegget blir ikkje utbetalt, fordi inntekta til deg og <PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall> din er over grensa for å få utbetalt barnetillegg. "
                        +"Tillegget blir ikkje utbetalt fordi inntekta di er over grensa for å få utbetalt barnetillegg."
                    },
                    english {
                        +"We have granted your application for child supplement, received by us on <PE_Vedtaksdata_Kravhode_KravMottatDato>. "
                        +"We have changed your disability benefit, effective as of <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>. "
                        +"You will not receive child supplement because your income and your <PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_EN_cohabiting_partner>'s income exceeds the income limit. "
                        +"You will not receive child supplement because your income exceeds the income limit."
                    }
                )
            }

            // IF((PE_Vedtaksdata_Kravhode_KravArsakType = "omgj_etter_klage" OR PE_Vedtaksdata_Kravhode_KravArsakType = "omgj_etter_anke"))THEN INCLUDE

            //TBU3332, TBU3332NN, TBU3332EN
            paragraph {
                text(
                    bokmal { +"Vi har endret uføretrygden din fra <PE_Vedtaksdata_Kravhode_onsketVirkningsDato> fordi du har fått medhold i klagen din." },
                    nynorsk { +"Vi har endra uføretrygda di frå <PE_Vedtaksdata_Kravhode_onsketVirkningsDato> fordi du har fått medhald i klaga di." },
                    english { +"We have changed your disability benefit effective as of <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>, because your appeal has been successful." },
                )
            }

            /*  IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
            OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)
            AND (PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt" OR "barn_endret_inntekt" OR "eps_endret_inntekt" OR "begge_for_end_inn")) THEN INCLUDE */

            //TBU2289, TBU2289NN, TBU2289EN
            paragraph {
                text(
                    bokmal { +"Vi har endret barnetillegget i uføretrygden din fordi du har meldt fra om inntektsendring." },
                    nynorsk { +"Vi har endra barnetillegget i uføretrygda di fordi du har meldt frå om inntektsendring." },
                    english { +"We have changed the child supplement in your disability benefit, because you have reported a change in income." },
                )
            }

            // IF(PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_bt, PE_UT_KONST_VilkarsVedtakResultat_opphor)) THEN INCLUDE

            //TBU2290, TBU2290NN, TBU2290EN
            paragraph {
                text(
                    bokmal { +"Vi har vedtatt at barnetillegget i uføretrygden din opphører fra <PE_Vedtaksdata_Kravhode_onsketVirkningsDato> for barn født <PE_UT_FodselsdatoBarn>." },
                    nynorsk { +"Vi har stansa barnetillegget i uføretrygda di frå <PE_Vedtaksdata_Kravhode_onsketVirkningsDato> for barn fødd <PE_UT_FodselsdatoBarn>." },
                    english { +"The child supplement in your disability benefit has been discontinued, effective as of <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>, for child born <PE_UT_FodselsdatoBarn_EN>." },
                )
            }

            // IF(PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_et, PE_UT_KONST_VilkarsVedtakResultat_opphor)) THEN INCLUDE
            //Ektefelletillegg kan fjernes?
            //TBU2291, TBU291NN, TBU2291EN
            paragraph {
                text(
                    bokmal { +"Vi har opphørt ektefelletillegget i uføretrygden din fra <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>" },
                    nynorsk { +"Vi har stansa ektefelletillegget i uføretrygda di frå <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>." },
                    english { +"The spouse supplement in your disability benefit has been discontinued, effective as of <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>." },
                )
            }

            // IF(PE_Vedtaksdata_Kravhode_KravGjelder = "sok_uu") THEN INCLUDE

            //TBU2292, TBU2292NN, TBU2292EN
            paragraph {
                text(
                    bokmal {
                        +"Vi har innvilget søknaden din om rettighet som ung ufør som vi mottok <PE_Vedtaksdata_Kravhode_KravMottatDato>. "
                        +"Vi har endret uføretrygden din fra <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>."
                    },
                    nynorsk {
                        +"Vi har innvilga søknaden din om å få rettar som ung ufør som vi fekk <PE_Vedtaksdata_Kravhode_KravMottatDato>. "
                        +"Vi har endra uføretrygda di frå <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>."
                    },
                    english {
                        +"We have granted your application for your disability benefit to be calculated in accordance with special rights for young disabled individuals, "
                        +"which we received on <PE_Vedtaksdata_Kravhode_KravMottatDato>. "
                        +"We have changed your disability benefit effective as of <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>."
                    },
                )
            }

            /* IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) = FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad)
            AND PE_Vedtaksdata_Kravhode_KravGjelder = "sok_ys") THEN INCLUDE */

            //TBU2293, TBU2293NN, TBU2293EN
            paragraph {
                text(
                    bokmal { +"Vi har innvilget søknaden din om uføretrygd etter særbestemmelsene for yrkesskade eller yrkessykdom som vi mottok <PE_Vedtaksdata_Kravhode_KravMottatDato>. "
                        +"Vi har kommet fram til at hele uførheten din skyldes en godkjent yrkesskade eller yrkessykdom, "
                        +"og uføretrygden din er endret fra <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>." },
                    nynorsk { +"Vi har innvilga søknaden din om uføretrygd etter særreglane for yrkesskade eller yrkessjukdom som vi fekk <PE_Vedtaksdata_Kravhode_KravMottatDato>. "
                        +"Vi har kome fram til at heile uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom, "
                        +"og uføretrygda di er endra frå <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>." },
                    english { +"We have granted your application for disability benefit in accordance with special rules in connection with occupational injury or occupational illness, "
                        +"which we received on <PE_Vedtaksdata_Kravhode_KravMottatDato>. "
                        +"We have concluded that your disability in its entirety was caused by a certified occupational injury or occupational illness, "
                        +"and your disability benefit has been changed accordingly, effective as of <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>." },
                )
            }

            /* IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad
            AND PE_Vedtaksdata_Kravhode_KravGjelder = "sok_ys") THEN INCLUDE */

            //TBU2294, TBU2294NN, TBU2294EN
            paragraph {
                text(
                    bokmal { +"Vi har innvilget søknaden din om uføretrygd etter særbestemmelsene for yrkesskade eller yrkessykdom som vi mottok "
                        +"<PE_Vedtaksdata_Kravhode_KravMottatDato>. Vi har kommet fram til at "
                        +"<PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad> prosent av uførheten din skyldes en godkjent yrkesskade eller yrkessykdom, "
                        +"og uføretrygden din er endret fra <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>." },
                    nynorsk { +"Vi har innvilga søknaden din om uføretrygd etter særreglane for yrkesskade eller yrkessjukdom som vi fekk "
                        +"<PE_Vedtaksdata_Kravhode_KravMottatDato>. Vi har kome fram til at "
                        +"<PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad> prosent av uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom, "
                        +"og uføretrygda di er endra frå <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>." },
                    english { +"We have granted your application for disability benefit in accordance with special rules in connection with occupational injury or occupational illness, which we received on "
                        +"<PE_Vedtaksdata_Kravhode_KravMottatDato>. We have concluded that "
                        +"<PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad> percent of your disability was caused by a certified occupational injury or occupational illness, "
                        +"and your disability benefit has been changed accordingly, effective as of <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>." },
                )
            }

            // IF(PE_Vedtaksdata_Kravhode_KravArsakType = "endring_ifu") THEN INCLUDE

            //TBU2295, TBU2295NN, TBU2295EN
            paragraph {
                text(
                    bokmal { +"Vi har innvilget søknaden din om endring av inntektsgrense." },
                    nynorsk { +"Vi har innvilga søknaden din om endring av inntektsgrense." },
                    english { +"We have granted your application for alteration of your income limit." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Den nye inntektsgrensen din har økt til <PE_UT_Inntektsgrense_faktisk> kroner fra <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>." },
                    nynorsk { +"Den nye inntektsgrensa di har auka til <PE_UT_Inntektsgrense_faktisk> kroner frå <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>." },
                    english { +"Your new income limit has increased to NOK <PE_UT_Inntektsgrense_faktisk> from <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>." },
                )
            }

            /* IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_Kravhode_KravArsakType = "gjnl_skal_vurd") THEN INCLUDE */

            //TBU2296, TBU2296NN, TBU2296EN  - FJERNES? Skal være tidsbegrenset omstillsstønad?
            paragraph {
                text(
                    bokmal { +"Vi har innvilget deg gjenlevenderettigheter i uføretrygden din. Vi har endret uføretrygden din fra <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>." },
                    nynorsk { +"Vi har innvilga deg attlevanderettar i uføretrygda di. Vi har endra uføretrygda di frå <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>." },
                    english { +"You have been granted survivor's rights in connection with your disability benefit. We have changed your disability benefit, effective as of <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>." },
                )
            }

            // IF(PE_UT_KravLinjeKode_Og_PaaFolgende_VedtakRes(PE_UT_KONST_KralinjeKode_ut_gjt, PE_UT_KONST_VilkarsVedtakResultat_opphor) THEN INCLUDE

            // TBU2297, TBU2297NN, TBU2297EN
            paragraph {
                text(
                    bokmal { +"Vi har opphørt gjenlevendetillegget i uføretrygden din fra <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>." },
                    nynorsk { +"Vi har stansa attlevandetillegget i uføretrygda di frå <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>." },
                    english { +"The survivor's supplement in your disability benefit has been discontinued, effective as of <PE_Vedtaksdata_Kravhode_onsketVirkningsDato>." },
                )
            }
            }
        }
    }
