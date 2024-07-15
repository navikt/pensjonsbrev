package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner

data class TBU052V_TBU073V_Del_2_SlikHarViFastsattKompensasjonsgradenDin(
    val FUNKSJON_PE_UT_TBU056V: Expression<Boolean>,
    val FUNKSJON_PE_UT_TBU056V_51: Expression.BinaryInvoke<Boolean, Boolean, Boolean>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad: Expression<Int>,
    val PE_pebrevkode: Expression<String>,
    val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_UgradertBruttoPerAr: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad: Expression<Int>,
): OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
    //IF(PE_UT_TBU056V() = true) THEN      INCLUDE ENDIF
        showIf(FUNKSJON_PE_UT_TBU056V){
            //[TBU052V-TBU073V]

            title1 {
                text (
                    Bokmal to "Slik har vi fastsatt kompensasjonsgraden din",
                    Nynorsk to "Slik har vi fastsett kompensasjonsgraden din",
                    English to "This is your degree of compensation",
                )
            }

            paragraph {
                text (
                    Bokmal to "Vi fastsetter kompensasjonsgraden ved å sammenligne det du ",
                    Nynorsk to "Vi fastset kompensasjonsgraden ved å samanlikne det du ",
                    English to "Your degree of compensation is established by comparing what you ",
                )

                //PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100
                showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad.equalTo(100)){
                    text (
                        Bokmal to "har rett til i",
                        Nynorsk to "har rett til i",
                        English to "are entitled to with a ",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad.lessThan(100))){
                    text (
                        Bokmal to "ville hatt rett til i",
                        Nynorsk to "ville hatt rett til i",
                        English to "would have been entitled to had your ",
                    )
                }
                text (
                    Bokmal to " 100 prosent uføretrygd med din oppjusterte inntekt før du ble ufør. Kompensasjonsgraden brukes til å beregne hvor mye vi reduserer uføretrygden din, hvis du har inntekt som er høyere enn inntektsgrensen.",
                    Nynorsk to " 100 prosent uføretrygd, med den oppjusterte inntekta di før du blei ufør. Kompensasjonsgraden blir brukt til å berekne kor mykje vi reduserer uføretrygda di, dersom du har inntekt som er høgare enn inntektsgrensa.",
                    English to "degree of disability ",
                )

                //PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100
                showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad.equalTo(100)){
                    text (
                        Bokmal to "",
                        Nynorsk to "",
                        English to "of ",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad.lessThan(100))){
                    text (
                        Bokmal to "",
                        Nynorsk to "",
                        English to "been ",
                    )
                }
                text (
                    Bokmal to "",
                    Nynorsk to "",
                    English to "100 percent, and your recalculated income prior to your disability. The degree of compensation is used to calculate how much your disability benefit will be reduced if your income exceeds the income limit.",
                )
            }
        }

        //IF(PE_UT_TBU056V() = true AND  (PE_pebrevkode <> "PE_UT_04_114" AND PE_pebrevkode <> "PE_UT_04_102" AND PE_pebrevkode <> "PE_UT_05_100" AND PE_pebrevkode <> "PE_UT_07_100") ) THEN      INCLUDE ENDIF
        showIf((FUNKSJON_PE_UT_TBU056V and (PE_pebrevkode.notEqualTo("PE_UT_04_114") and PE_pebrevkode.notEqualTo("PE_UT_04_102") and PE_pebrevkode.notEqualTo("PE_UT_05_100") and PE_pebrevkode.notEqualTo("PE_UT_07_100")))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Inntekten din før du ble ufør er fastsatt til ".expr() + PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt.format() + " kroner. For å kunne fastsette kompensasjonsgraden din, må denne inntekten oppjusteres til dagens verdi. Oppjustert til dagens verdi tilsvarer dette en inntekt på " + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu.format() + " kroner.",
                    Nynorsk to "Inntekta di før du blei ufør er fastsett til ".expr() + PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt.format() + " kroner. For å kunne fastsetje kompensasjonsgraden din, må inntekta oppjusterast til dagens verdi. Oppjustert til dagens verdi utgjer dette ei inntekt på " + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu.format() + " kroner.",
                    English to "Your income before you became disabled has been determined to be NOK ".expr() + PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt.format() + ". To establish your degree of compensation this is adjusted to today’s value and is equivalent to an income of NOK " + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu.format() + ".",
                )
            }

            paragraph {
                textExpr (
                    Bokmal to "Du har rett til 100 prosent uføretrygd, som utgjør ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_UgradertBruttoPerAr.format() + " kroner per år.",
                    Nynorsk to "Du har rett til 100 prosent uføretrygd, som utgjer ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_UgradertBruttoPerAr.format() + " kroner per år.",
                    English to "For you, a 100-percent disability benefit will total NOK ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_UgradertBruttoPerAr.format() + " per year.",
                )
            }

            paragraph {
                textExpr (
                    Bokmal to "Du har rett til ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad.format() + " prosent uføretrygd. Regnet om til 100 prosent uføretrygd, utgjør dette " + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_UgradertBruttoPerAr.format() + " kroner per år.",
                    Nynorsk to "Du har rett til ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad.format() + " prosent uføretrygd. Rekna om til 100 prosent uføretrygd, utgjer dette " + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_UgradertBruttoPerAr.format() + " kroner per år.",
                    English to "You are entitled to ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad.format() + " percent disability benefit. This equals NOK " + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_UgradertBruttoPerAr.format() + " per year, as a 100-percent disability benefit.",
                )
            }

            paragraph {
                textExpr (
                    Bokmal to "Vi beregner kompensasjonsgraden din slik:(".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_UgradertBruttoPerAr.format() + " / " + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu.format() + ") * 100 = " + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad.format() + " prosent.",
                    Nynorsk to "Vi bereknar kompensasjonsgraden din slik:(".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_UgradertBruttoPerAr.format() + " / " + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu.format() + ") * 100 = " + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad.format() + " prosent.",
                    English to "We have calculated your degree of compensation as follows:(".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_UgradertBruttoPerAr.format() + " / " + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu.format() + ") * 100 = " + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad.format() + " percent.",
                )
            }
        }

        //IF(PE_UT_TBU056V() = true) THEN      INCLUDE ENDIF
        showIf(FUNKSJON_PE_UT_TBU056V){
            //[TBU052V-TBU073V]

            paragraph {

                //IF(PE_UT_TBU056V_51() = true) THEN      INCLUDE ENDIF
                showIf(FUNKSJON_PE_UT_TBU056V_51){
                    text (
                        Bokmal to "Kompensasjonsgraden skal ved beregningen ikke settes høyere enn 70 prosent. ",
                        Nynorsk to "Kompensasjonsgraden skal ikkje setjast høgare enn 70 prosent i berekninga. ",
                        English to "Your degree of compensation will not be set higher than 70 percent. ",
                    )
                }
                text (
                    Bokmal to "Hvis uføretrygden din i løpet av et kalenderår endres, bruker vi en gjennomsnittlig kompensasjonsgrad i beregningen.",
                    Nynorsk to "Dersom uføretrygda di blir endra i løpet av eit kalenderår, vil vi bruke ein gjennomsnittleg kompensasjonsgrad i berekninga.",
                    English to "If your degree of compensation has changed over the course of a calendar year, your disability benefit payment will be recalculated based on your average degree of compensation.",
                )
            }
        }
    }

}
