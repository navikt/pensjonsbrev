package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

data class TBU052V_TBU073V_SlikHarViFastsattKompensasjonsgradenDin(
    val pe: Expression<PE>
): OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        //IF(PE_UT_TBU056V() = true) THEN      INCLUDE ENDIF
        showIf(pe.ut_tbu056v()) {
            //[TBU052V-TBU073V]

            title1 {
                text(
                    bokmal { + "Slik har vi fastsatt kompensasjonsgraden din" },
                    nynorsk { + "Slik har vi fastsett kompensasjonsgraden din" },
                    english { + "This is your degree of compensation" },
                )
            }

            paragraph {
                text(
                    bokmal { + "Vi fastsetter kompensasjonsgraden ved å sammenligne det du " },
                    nynorsk { + "Vi fastset kompensasjonsgraden ved å samanlikne det du " },
                    english { + "Your degree of compensation is established by comparing what you " },
                )

                //PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100
                showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().equalTo(100)) {
                    text(
                        bokmal { + "har rett til i" },
                        nynorsk { + "har rett til i" },
                        english { + "are entitled to with a " },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().lessThan(100))) {
                    text(
                        bokmal { + "ville hatt rett til i" },
                        nynorsk { + "ville hatt rett til i" },
                        english { + "would have been entitled to had your " },
                    )
                }
                text(
                    bokmal { + " 100 prosent uføretrygd med din oppjusterte inntekt før du ble ufør. Kompensasjonsgraden brukes til å beregne hvor mye vi reduserer uføretrygden din, hvis du har inntekt som er høyere enn inntektsgrensen." },
                    nynorsk { + " 100 prosent uføretrygd, med den oppjusterte inntekta di før du blei ufør. Kompensasjonsgraden blir brukt til å berekne kor mykje vi reduserer uføretrygda di, dersom du har inntekt som er høgare enn inntektsgrensa." },
                    english { + "degree of disability " },
                )

                //PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100
                showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().equalTo(100)) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "of " },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().lessThan(100))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "been " },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + "" },
                    english { + "100 percent, and your recalculated income prior to your disability. The degree of compensation is used to calculate how much your disability benefit will be reduced if your income exceeds the income limit." },
                )
            }
        }

        //IF(PE_UT_TBU056V() = true AND  (PE_pebrevkode <> "PE_UT_04_114" AND PE_pebrevkode <> "PE_UT_04_102" AND PE_pebrevkode <> "PE_UT_05_100" AND PE_pebrevkode <> "PE_UT_07_100") ) THEN      INCLUDE ENDIF
        showIf(
            (pe.ut_tbu056v() and (pe.pebrevkode()
                .notEqualTo("PE_UT_04_114") and pe.pebrevkode().notEqualTo("PE_UT_04_102") and pe.pebrevkode().notEqualTo(
                "PE_UT_05_100"
            ) and pe.pebrevkode().notEqualTo("PE_UT_07_100")))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    bokmal { + "Inntekten din før du ble ufør er fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt()
                        .format() + ". For å kunne fastsette kompensasjonsgraden din, må denne inntekten oppjusteres til dagens verdi. Oppjustert til dagens verdi tilsvarer dette en inntekt på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oifu()
                        .format() + "." },
                    nynorsk { + "Inntekta di før du blei ufør er fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt()
                        .format() + ". For å kunne fastsetje kompensasjonsgraden din, må inntekta oppjusterast til dagens verdi. Oppjustert til dagens verdi utgjer dette ei inntekt på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oifu()
                        .format() + "." },
                    english { + "Your income before you became disabled has been determined to be " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt()
                        .format() + ". To establish your degree of compensation this is adjusted to today’s value and is equivalent to an income of " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oifu()
                        .format() + "." },
                )
            }

            paragraph {
                text (
                    bokmal { + "Du har rett til 100 prosent uføretrygd, som utgjør " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_ugradertbruttoperar()
                        .format() + " per år." },
                    nynorsk { + "Du har rett til 100 prosent uføretrygd, som utgjer " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_ugradertbruttoperar()
                        .format() + " per år." },
                    english { + "For you, a 100-percent disability benefit will total " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_ugradertbruttoperar()
                        .format() + " per year." },
                )
            }

            paragraph {
                text (
                    bokmal { + "Du har rett til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()
                        .format() + " prosent uføretrygd. Regnet om til 100 prosent uføretrygd, utgjør dette " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_ugradertbruttoperar()
                        .format() + " per år." },
                    nynorsk { + "Du har rett til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()
                        .format() + " prosent uføretrygd. Rekna om til 100 prosent uføretrygd, utgjer dette " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_ugradertbruttoperar()
                        .format() + " per år." },
                    english { + "You are entitled to " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()
                        .format() + " percent disability benefit. This equals " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_ugradertbruttoperar()
                        .format() + " per year, as a 100-percent disability benefit." },
                )
            }

            paragraph {
                text (
                    bokmal { + "Vi beregner kompensasjonsgraden din slik:(" + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_ugradertbruttoperar()
                        .format(false) + " / " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oifu()
                        .format(false) + ") * 100 = " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
                        .format() + " prosent." },
                    nynorsk { + "Vi bereknar kompensasjonsgraden din slik:(" + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_ugradertbruttoperar()
                        .format(false) + " / " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oifu()
                        .format(false) + ") * 100 = " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
                        .format() + " prosent." },
                    english { + "We have calculated your degree of compensation as follows:(" + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_ugradertbruttoperar()
                        .format(false) + " / " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oifu()
                        .format(false) + ") * 100 = " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
                        .format() + " percent." },
                )
            }
        }

        //IF(PE_UT_TBU056V() = true) THEN      INCLUDE ENDIF
        showIf(pe.ut_tbu056v()) {
            //[TBU052V-TBU073V]

            paragraph {

                //IF(PE_UT_TBU056V_51() = true) THEN      INCLUDE ENDIF
                showIf(pe.ut_tbu056v_51()) {
                    text(
                        bokmal { + "Kompensasjonsgraden skal ved beregningen ikke settes høyere enn 70 prosent. " },
                        nynorsk { + "Kompensasjonsgraden skal ikkje setjast høgare enn 70 prosent i berekninga. " },
                        english { + "Your degree of compensation will not be set higher than 70 percent. " },
                    )
                }
                text(
                    bokmal { + "Hvis uføretrygden din i løpet av et kalenderår endres, bruker vi en gjennomsnittlig kompensasjonsgrad i beregningen." },
                    nynorsk { + "Dersom uføretrygda di blir endra i løpet av eit kalenderår, vil vi bruke ein gjennomsnittleg kompensasjonsgrad i berekninga." },
                    english { + "If your degree of compensation has changed over the course of a calendar year, your disability benefit payment will be recalculated based on your average degree of compensation." },
                )
            }
        }
    }

}
