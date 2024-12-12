package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.pebrevkode
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravarsaktype
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text

data class TBU052V_TBU073V_ForDegSomMottarEktefelletillegg(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        //IF(PE_pebrevkode <> "PE_UT_04_101"  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt"  AND PE_pebrevkode <> "PE_UT_04_108"  AND PE_pebrevkode <> "PE_UT_04_109"  AND PE_pebrevkode <> "PE_UT_04_500" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_pebrevkode <> "PE_UT_06_300" AND (PE_pebrevkode <> "PE_UT_04_102"      OR (PE_pebrevkode = "PE_UT_04_102"     AND PE_Vedtaksdata_Kravhode_KravArsakType <> "tilst_dod"))) THEN      INCLUDE ENDIF
        showIf((pe.pebrevkode().notEqualTo("PE_UT_04_101")
                and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()
                and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt")
                and pe.pebrevkode().notEqualTo("PE_UT_04_108")
                and pe.pebrevkode().notEqualTo("PE_UT_04_109")
                and pe.pebrevkode().notEqualTo("PE_UT_04_500")
                and pe.pebrevkode().notEqualTo("PE_UT_07_200")
                and pe.pebrevkode().notEqualTo("PE_UT_06_300")
                and (pe.pebrevkode()
            .notEqualTo("PE_UT_04_102") or (pe.pebrevkode().equalTo("PE_UT_04_102") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo(
            "tilst_dod"
        ))))){
            //[TBU052V-TBU073V]

            title1 {
                text (
                    Bokmal to "For deg som mottar ektefelletillegg",
                    Nynorsk to "For deg som får ektefelletillegg",
                    English to "For those receiving a spouse supplement",
                )
            }

            paragraph {
                text (
                    Bokmal to "Ektefelletillegget blir utbetalt som et fast tillegg ved siden av uføretrygden. Tillegget blir ikke endret i perioden ektefelletillegget er innvilget.",
                    Nynorsk to "Ektefelletillegget blir utbetalt som eit fast tillegg ved sida av uføretrygda. Tillegget blir ikkje endra i den perioden ektefelletillegget er innvilga for.",
                    English to "The spouse supplement is paid as a fixed supplement in addition to your disability benefit. The supplement will not be amended during the period for which the spouse supplement has been granted.",
                )
            }
        }

        //IF( ( (PE_pebrevkode = "PE_UT_04_300"  OR  PE_pebrevkode = "PE_UT_14_300") AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true ) OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true AND PE_pebrevkode <> "PE_UT_04_101" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108"  AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_04_500" AND PE_pebrevkode <> "PE_UT_06_300" AND (PE_pebrevkode <> "PE_UT_04_102"      OR (PE_pebrevkode = "PE_UT_04_102"     AND PE_Vedtaksdata_Kravhode_KravArsakType <> "tilst_dod")))  ) THEN      INCLUDE ENDIF
        showIf((((pe.pebrevkode().equalTo("PE_UT_04_300") or pe.pebrevkode()
            .equalTo("PE_UT_14_300")) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() and pe.pebrevkode()
            .notEqualTo("PE_UT_04_101") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.pebrevkode()
            .notEqualTo("PE_UT_04_108") and pe.pebrevkode().notEqualTo("PE_UT_04_109") and pe.pebrevkode()
            .notEqualTo("PE_UT_04_500") and pe.pebrevkode()
            .notEqualTo("PE_UT_06_300") and (pe.pebrevkode()
            .notEqualTo("PE_UT_04_102") or (pe.pebrevkode().equalTo("PE_UT_04_102") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo(
            "tilst_dod"
        )))))){
            //[TBU052V-TBU073V]

            paragraph {

                //IF(PE_pebrevkode = "PE_UT_04_300" OR PE_pebrevkode = "PE_UT_14_300") THEN      INCLUDE ENDIF
                showIf((pe.pebrevkode().equalTo("PE_UT_04_300") or pe.pebrevkode().equalTo("PE_UT_14_300"))){
                    text (
                        Bokmal to "Når vi beregner ektefelletillegget tar vi utgangspunkt i den årlige uførepensjonen du har rett til i desember 2014. Deretter regner vi ut tillegget ut fra fastsatte overgangsregler. ",
                        Nynorsk to "Når vi bereknar ektefelletillegget, tek vi utgangspunkt i den årlege uførepensjonen du har rett til i desember 2014. Deretter reknar vi ut tillegget ut frå fastsette overgangsreglar. ",
                        English to "When we calculate the spouse supplement, we base calculations on your annual disability pension as of December 2014. Then, the supplement will be calculated in accordance with established transition rules. ",
                    )
                }

                //IF(PE_pebrevkode <> "PE_UT_04_101"  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt"  AND PE_pebrevkode <> "PE_UT_04_108"  AND PE_pebrevkode <> "PE_UT_04_109"  AND  PE_pebrevkode <> "PE_UT_04_500"  AND PE_pebrevkode <> "PE_UT_07_200" AND PE_pebrevkode <> "PE_UT_06_300" AND (PE_pebrevkode <> "PE_UT_04_102"      OR (PE_pebrevkode = "PE_UT_04_102"     AND PE_Vedtaksdata_Kravhode_KravArsakType <> "tilst_dod"))) THEN      INCLUDE ENDIF
                showIf((pe.pebrevkode()
                    .notEqualTo("PE_UT_04_101") and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() and pe.vedtaksdata_kravhode_kravarsaktype()
                    .notEqualTo(
                        "soknad_bt"
                    ) and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode()
                    .notEqualTo("PE_UT_04_109") and pe.pebrevkode().notEqualTo("PE_UT_04_500") and pe.pebrevkode()
                    .notEqualTo("PE_UT_07_200") and pe.pebrevkode()
                    .notEqualTo("PE_UT_06_300") and (pe.pebrevkode()
                    .notEqualTo("PE_UT_04_102") or (pe.pebrevkode().equalTo("PE_UT_04_102") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo(
                    "tilst_dod"
                ))))){
                    text (
                        Bokmal to "Du kan beholde ektefelletillegget ut vedtaksperioden, men det opphører senest 31. desember 2024.",
                        Nynorsk to "Du kan behalde ektefelletillegget ut vedtaksperioden, men det tek slutt seinast 31. desember 2024.",
                        English to "You may retain the spouse supplement until the end of the period, but it will expire on 31 December 2024 at the latest.",
                    )
                }
            }
        }

        //IF(PE_pebrevkode <> "PE_UT_04_101"  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt"  AND PE_pebrevkode <> "PE_UT_04_108"  AND PE_pebrevkode <> "PE_UT_04_109"  AND  PE_pebrevkode <> "PE_UT_04_500"  AND PE_pebrevkode <> "PE_UT_07_200" AND PE_pebrevkode <> "PE_UT_06_300" AND (PE_pebrevkode <> "PE_UT_04_102"      OR (PE_pebrevkode = "PE_UT_04_102"     AND PE_Vedtaksdata_Kravhode_KravArsakType <> "tilst_dod"))) THEN      INCLUDE ENDIF
        showIf((pe.pebrevkode()
            .notEqualTo("PE_UT_04_101") and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() and pe.vedtaksdata_kravhode_kravarsaktype()
            .notEqualTo(
                "soknad_bt"
            ) and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode().notEqualTo("PE_UT_04_109") and pe.pebrevkode()
            .notEqualTo("PE_UT_04_500") and pe.pebrevkode().notEqualTo("PE_UT_07_200") and pe.pebrevkode()
            .notEqualTo("PE_UT_06_300") and (pe.pebrevkode()
            .notEqualTo("PE_UT_04_102") or (pe.pebrevkode().equalTo("PE_UT_04_102") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo(
            "tilst_dod"
        ))))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Ektefelletillegget vil falle bort hvis du skiller deg, uføretrygden opphører eller hvis ektefellen din dør.",
                    Nynorsk to "Ektefelletillegget fell bort dersom du skil deg, uføretrygda tek slutt eller dersom ektefellen din døyr.",
                    English to "The spouse supplement will terminate if you get divorced, your disability benefit ceases, or your spouse dies.",
                )
            }
        }

        //IF(PE_pebrevkode = "PE_UT_04_300" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_Sats = 3.76 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true) THEN      INCLUDE ENDIF
        showIf(
            (pe.pebrevkode()
                .equalTo("PE_UT_04_300") and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats()
                .equalTo(
                    3.76
                ) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget())){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Du har minstepensjon og tillegg for ektefelle som har fylt 60 år. Du har derfor rett til å motta uføretrygd som minst tilsvarer 3,76 ganger folketrygdens grunnbeløp. Dette grunnlaget justeres ut fra uføregraden og trygdetiden din, og du beholder dette ut vedtaksperioden for ektefelletillegget. Etter dette vil vi beregne uføretrygden etter ordinære regler.",
                    Nynorsk to "Du har minstepensjon og tillegg for ektefelle som har fylt 60 år. Du har derfor rett til å få uføretrygd som minst tilsvarer 3,76 gonger grunnbeløpet i folketrygda. Dette grunnlaget blir justert ut frå uføregraden din og trygdetida di, og du beheld dette ut vedtaksperioden for ektefelletillegget. Etter dette bereknar vi uføretrygda etter ordinære reglar.",
                    English to "You are receiving the minimum pension and a spouse supplement for a spouse who is aged 60 or older. Consequently, you are entitled to a disability benefit of minimum 3.76 times the National Insurance basic amount. This basis is adjusted in accordance with your degree of disability and period of national insurance coverage, and you will retain it for the duration of the period for which the spouse supplement has been granted. After this period, your disability benefit will be calculated in accordance with the regular rules.",
                )
            }
        }
    }
}