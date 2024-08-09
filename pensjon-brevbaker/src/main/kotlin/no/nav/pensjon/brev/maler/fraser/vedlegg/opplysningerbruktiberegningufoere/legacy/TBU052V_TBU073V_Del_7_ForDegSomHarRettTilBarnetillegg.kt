package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy

import no.nav.pensjon.brev.maler.fraser.FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear
import no.nav.pensjon.brev.maler.fraser.common.Constants.SKATTEETATEN_URL
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
import java.time.LocalDate

data class TBU052V_TBU073V_SlikBeregnerViStoerrelsenPaaBarnetilleggetDitt(
    val FUNKSJON_PE_UT_Etteroppgjor_BT_Utbetalt: Expression<Boolean>,
    val FUNKSJON_PE_UT_TBU605: Expression<Boolean>,
    val FUNKSJON_PE_UT_TBU605V_eller_til_din: Expression<Boolean>,
    val FUNKSJON_PE_UT_TBU606V_TBU608V: Expression<Boolean>,
    val FUNKSJON_PE_UT_TBU606V_TBU611V: Expression<Boolean>,
    val FUNKSJON_PE_UT_TBU608_Far_Ikke: Expression<Boolean>,
    val FUNKSJON_PE_UT_TBU609V_TBU611V: Expression<Boolean>,
    val FUNKSJON_PE_UT_TBU611_Far_Ikke: Expression<Boolean>,
    val FUNKSJON_PE_UT_TBU613V: Expression<Boolean>,
    val FUNKSJON_PE_UT_TBU613V_1_3: Expression<Boolean>,
    val FUNKSJON_PE_UT_TBU613V_4_5: Expression<Boolean>,
    val FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVirkningFOM: Expression<Kroner>,
    val PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus: Expression<Kroner>,
    val PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus: Expression<Kroner>,
    val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT: Expression<String>,
    val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_EN: Expression<String>,
    val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_EN_cohabiting_partner: Expression<String>,
    val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall: Expression<String>,
    val PE_UT_BTFBInntektBruktiAvkortningMinusBTFBfribelop: Expression<Kroner>,
    val PE_UT_BTSBinntektBruktiAvkortningMinusBTSBfribelop: Expression<Kroner>,
    val PE_UT_Barnet_Barna_Felles: Expression<String>,
    val PE_UT_Barnet_Barna_Serkull: Expression<String>,
    val PE_UT_TBU069V: Expression<Boolean>,
    val PE_UT_VirkningstidpunktStorreEnn01012016: Expression<Boolean>,
    val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr: Expression<Kroner>,
    val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert: Expression<Boolean>,
    val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert: Expression<Boolean>,
    val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_Inntektstak: Expression<Kroner>,
    val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr: Expression<Kroner>,
    val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr_UtenMinus: Expression<Kroner>,
    val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr: Expression<Kroner>,
    val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert: Expression<Boolean>,
    val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert: Expression<Boolean>,
    val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_Inntektstak: Expression<Kroner>,
    val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr: Expression<Kroner>,
    val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr_UtenMinus: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbruttoPerAr: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBnettoPerAr: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBbruttoPerAr: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBnettoPerAr: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid: Expression<Int>,
    val PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt: Expression<String>,
    val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles: Expression<Int>,
    val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget: Expression<Boolean>,
    val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull: Expression<Int>,
    val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget: Expression<Boolean>,
    val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto: Expression<Kroner>,
    val PE_Vedtaksdata_Kravhode_KravArsakType: Expression<String>,
    val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVirkningFOM: Expression<LocalDate?>,
    val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat: Expression<String>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        //IF(PE_UT_TBU069V() = true) THEN      INCLUDE ENDIF
        showIf(PE_UT_TBU069V){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Slik beregner vi størrelsen på barnetillegget",
                    Nynorsk to "Slik bereknar vi storleiken på barnetillegget",
                    English to "How we calculate the amount of child supplement",
                )
            }
        }

        //IF(PE_UT_TBU069V() = true) THEN      INCLUDE ENDIF
        showIf((PE_UT_TBU069V)){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Størrelsen på barnetillegget er avhengig av samlet inntekt. ",
                    Nynorsk to "Storleiken på barnetillegget er avhengig av samla inntekt.",
                    English to "The amount of child supplement is dependent on your total income. ",
                )
            }
        }

        //IF(PE_UT_TBU069V() = true) THEN      INCLUDE ENDIF
        showIf((PE_UT_TBU069V)){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Barnetillegget kan bli redusert ut fra:uføretrygdarbeidsinntektnæringsinntektinntekt fra utlandetytelser/pensjon fra Norgepensjon fra utlandet",
                    Nynorsk to "Barnetillegget kan bli redusert ut frå:uføretrygdarbeidsinntektnæringsinntektinntekt frå utlandetytingar/pensjon frå Noregpensjon frå utlandet",
                    English to "Child supplement can be reduced based on:disability benefitsincome from employmentincome from self-employmentincome from overseaspayments/pensions from Norwaypensions from overseas",
                )
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false ) ) THEN      INCLUDE ENDIF
        showIf((PE_UT_TBU069V and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget)))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Vi fastsetter størrelsen på barnetillegget ut fra den samlede inntekten til begge foreldrene.",
                    Nynorsk to "Vi fastset storleiken på barnetillegget ut frå den samla inntekta til begge foreldra.",
                    English to "We determine the amount of child supplement based on the total income of both parents.",
                )
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false ) ) THEN      INCLUDE ENDIF
        showIf((PE_UT_TBU069V and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget)))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Barnetillegget blir redusert dersom den samlede inntekten er høyere enn fribeløpet. Fribeløpet for et barn er 4,6 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. ",
                    Nynorsk to "Barnetillegget blir redusert dersom den samla inntekta er høgare enn fribeløpet. Fribeløpet for eit barn er 4,6 gonger grunnbeløpet i folketrygda, og det aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn. ",
                    English to "The child supplement will be reduced if the total income is greater than the exemption amount. The exemption amount is 4.6 x the national insurance basic amount and it increases with 40 percent of the national insurance basic amount for each extra child. ",
                )

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid < 40 AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) <> "oppfylt") THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid.lessThan(40) and PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat.notEqualTo("oppfylt"))){
                    text (
                        Bokmal to "Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har.",
                        Nynorsk to "Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har.",
                        English to "As your period of national insurance cover is less than 40 years, the exemption amount is reduced based on the period of national insurance that you have.",
                    )
                }
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false ) ) THEN      INCLUDE ENDIF
        showIf((PE_UT_TBU069V and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget)))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Dersom begge foreldrene mottar uføretrygd blir barnetillegget gitt til den som har rett til det høyeste tillegget. Dette gjelder også dersom den ene forelderen mottar alderspensjon. ",
                    Nynorsk to "Dersom begge foreldra får uføretrygd, blir barnetillegget gitt til den som har rett til det høgaste tillegget. Dette gjeld også dersom den eine forelderen får alderspensjon.",
                    English to "If both parents receive disability benefits, child supplement is paid to the parent who has the right to the highest supplement. This also applies if one of the parents receives a retirement pension.",
                )
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF
        showIf((PE_UT_TBU069V and (not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Vi fastsetter størrelsen på barnetillegget ut fra inntekten din. Inntekt til en ektefelle/partner/samboer som ikke er forelder til barnet, har ikke betydning for størrelsen på barnetillegget. ".expr(),
                    Nynorsk to "Vi fastset storleiken på barnetillegget ut frå inntekta di. Inntekt til ein ektefelle/partnar/sambuar som ikkje er forelder til barnet, har ikkje betydning for storleiken på barnetillegget.".expr(),
                    English to "We determine the amount of child supplement based on your income. The income of a  ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_EN_cohabiting_partner + "who is not a parent of the child does not affect the size of the child supplement.",
                )
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF
        showIf((PE_UT_TBU069V and (not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Barnetillegget blir redusert dersom den samlede inntekten din er høyere enn fribeløpet. Fribeløpet for et barn er 3,1 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. ",
                    Nynorsk to "Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn er 3,1 gonger grunnbeløpet i folketrygda, og det aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn.",
                    English to "The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 3.1 x the national insurance basic amount and it increases with 40 percent of the national insurance basic amount for each extra child. ",
                )

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid < 40 AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) <> "oppfylt") THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid.lessThan(40) and PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat.notEqualTo("oppfylt"))){
                    text (
                        Bokmal to "Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har.",
                        Nynorsk to "Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har.",
                        English to "As your period of national insurance cover is less than 40 years, the exemption amount is reduced based on the period of national insurance that you have.",
                    )
                }
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF
        showIf((PE_UT_TBU069V and (not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Dersom begge foreldrene mottar uføretrygd blir barnetillegget gitt til den som har den daglige omsorgen for barnet. Dette gjelder også dersom den ene forelderen mottar alderspensjon. Har foreldrene delt omsorg for barnet, blir barnetillegget gitt til den forelderen som bor på samme folkeregistrerte adresse som barnet. ",
                    Nynorsk to "Dersom begge foreldra får uføretrygd, blir barnetillegget gitt til den som har den daglege omsorga for barnet. Dette gjeld også dersom den eine forelderen får alderspensjon. Har foreldra delt omsorg for barnet, blir barnetillegget gitt til den forelderen som bur på same folkeregistrerte adresse som barnet.",
                    English to "If both parents receive disability benefits, child supplement is paid to the parent who has the daily care of the child. This also applies if one of the parents receives a retirement pension. If the parents have joint daily care of the child, the child supplement will be paid to the parent who lives at the same address, registered in the population register, as the child.",
                )
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF
        showIf((PE_UT_TBU069V and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Vi fastsetter størrelsen på barnetillegget ut fra inntekten til deg og din ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + " for " + PE_UT_Barnet_Barna_Felles + " som bor med begge sine foreldre. Barnetillegget blir redusert dersom den samlede inntekten er høyere enn fribeløpet. Fribeløpet for et barn som bor med begge foreldrene er 4,6 ganger folketrygdens grunnbeløp, og øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. ",
                    Nynorsk to "Vi fastset storleiken på barnetillegget ut frå inntekta til deg og ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din for " + PE_UT_Barnet_Barna_Felles + " som bur med begge foreldra sine. Barnetillegget blir redusert dersom den samla inntekta er høgare enn fribeløpet. Fribeløpet for eit barn som bur med begge foreldra, er 4,6 gonger grunnbeløpet i folketrygda, og aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn. ",
                    English to "We determine the amount of the child supplement based on your and your ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_EN + "'s income for the " + PE_UT_Barnet_Barna_Felles + " that ",
                )

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles = 1
                showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles.equalTo(1)){
                    text (
                        Bokmal to "",
                        Nynorsk to "",
                        English to "lives",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles > 1) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles.greaterThan(1))){
                    text (
                        Bokmal to "",
                        Nynorsk to "",
                        English to "live",
                    )
                }
                text (
                    Bokmal to "",
                    Nynorsk to "",
                    English to " with both of their parents. The child supplement will be reduced if the total income is greater than the exemption amount. The exemption amount for a child that lives with both parents is 4.6 x the national insurance basic amount and it increases with 40 percent of the national insurance basic amount for each extra child.",
                )
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF
        showIf((PE_UT_TBU069V and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "For ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikke bor sammen med begge foreldre, fastsetter vi størrelsen på barnetillegget ut fra inntekten din. Inntekt til en ektefelle/partner/samboer som ikke er forelder til barnet, har ikke betydning for størrelsen på barnetillegget. Barnetillegget blir redusert dersom den samlede inntekten din er høyere enn fribeløpet. Fribeløpet for et barn som ikke bor sammen med begge foreldrene er 3,1 ganger folketrygdens grunnbeløp, og øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. ",
                    Nynorsk to "For ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikkje bur saman med begge foreldra, fastset vi storleiken på barnetillegget ut frå inntekta di. Inntekt til ein ektefelle/partnar/sambuar som ikkje er forelder til barnet, har ikkje betydning for storleiken på barnetillegget. Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn som ikkje bur saman med begge foreldra, er 3,1 gonger grunnbeløpet i folketrygda, og aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn. ",
                    English to "For ".expr(),
                )

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull = 1
                showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull.equalTo(1)){
                    text (
                        Bokmal to "",
                        Nynorsk to "",
                        English to "a ",
                    )
                }
                textExpr (
                    Bokmal to "".expr(),
                    Nynorsk to "".expr(),
                    English to PE_UT_Barnet_Barna_Serkull + " that ",
                )

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull = 1
                showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull.equalTo(1)){
                    text (
                        Bokmal to "",
                        Nynorsk to "",
                        English to "does",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull > 1) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull.greaterThan(1))){
                    text (
                        Bokmal to "",
                        Nynorsk to "",
                        English to "do",
                    )
                }
                text (
                    Bokmal to "",
                    Nynorsk to "",
                    English to " not live with both parents, we determine the amount of child supplement based on your income. The income of a spouse/partner/cohabiting partner who is not a parent of the child does not affect the size of the child supplement. The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount for a child that does not live with both parents is 3.1 x the national insurance basic amount and this increases by 40 percent of the national insurance basic amount for each extra child. ",
                )
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid < 40 AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) <> "oppfylt") AND   (PE_UT_VirkningstidpunktStorreEnn01012016() = true) ) THEN      INCLUDE ENDIF
        showIf((PE_UT_TBU069V and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget) and (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid.lessThan(40) and PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat.notEqualTo("oppfylt")) and (PE_UT_VirkningstidpunktStorreEnn01012016))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Siden trygdetiden din er kortere enn 40 år, blir fribeløpene redusert ut fra den trygdetiden du har.",
                    Nynorsk to "Sidan trygdetida di er kortare enn 40 år, blir fribeløpa reduserte ut frå den trygdetida du har.",
                    English to "As your period of national insurance cover is less than 40 years, the exemption amounts are reduced based on the period of national insurance that you have.",
                )
            }
        }

        // Velger å ikke slå sammen denne teksten ettersom det er vesentlige forskjeller i styringen på de ulike språklagene.
        //IF(PE_UT_TBU605() = true) THEN      INCLUDE ENDIF
        showIf((FUNKSJON_PE_UT_TBU605)){
            //[TBU052V-TBU073V]

            paragraph {

                //IF(( PE_UT_TBU605V_eller_til_din() )) THEN    INCLUDE ENDIF
                showIf(((FUNKSJON_PE_UT_TBU605V_eller_til_din))){
                    text (
                        Bokmal to "",
                        Nynorsk to "Har det vore ei endring i inntekta ",
                        English to "",
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2") )) THEN      INCLUDE ENDIF
                showIf(((FUNKSJON_PE_UT_TBU605V_eller_til_din and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed ektefelle") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed registrert partner") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1-5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1_5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 3-2"))))){
                    textExpr (
                            Bokmal to "".expr(),
                        Nynorsk to "til deg eller ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " ",
                        English to "".expr(),
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() )) THEN    INCLUDE ENDIF
                showIf(((FUNKSJON_PE_UT_TBU605V_eller_til_din))){
                    text (
                            Bokmal to "",
                        Nynorsk to "di",
                        English to "",
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2") )) THEN      INCLUDE ENDIF
                showIf(((FUNKSJON_PE_UT_TBU605V_eller_til_din and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed ektefelle") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed registrert partner") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1-5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1_5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 3-2"))))){
                    text (
                            Bokmal to "",
                        Nynorsk to "n",
                        English to "",
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() )) THEN    INCLUDE ENDIF
                showIf(((FUNKSJON_PE_UT_TBU605V_eller_til_din))){
                    text (
                        Bokmal to "",
                        Nynorsk to ",",
                        English to "",
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("endret_inntekt")){
                    text (
                        Bokmal to "",
                        Nynorsk to "Når inntekta di ",
                        English to "",
                    )
                }

                //IF(( PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2") )) THEN      INCLUDE ENDIF
                showIf(((PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("endret_inntekt") and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed ektefelle") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed registrert partner") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1-5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1_5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 3-2"))))){
                    textExpr (
                        Bokmal to "".expr(),
                        Nynorsk to "eller til ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din ",
                        English to "".expr(),
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("endret_inntekt")){
                    text (
                        Bokmal to "",
                        Nynorsk to "endrar seg" + ",",
                        English to "",
                    )
                }
                text (
                        Bokmal to "",
                    Nynorsk to " blir reduksjonen av barnetillegget vurdert på nytt. 50 prosent av inntekta som overstig fribeløpet ",
                    English to "",
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert)){
                    text (
                        Bokmal to "",
                        Nynorsk to "blir rekna om til et årleg beløp som svarer til ",
                        English to "",
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = false) THEN      INCLUDE ENDIF
                showIf((not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert) and not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert) and not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert) and not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert))){
                    text (
                        Bokmal to "",
                        Nynorsk to "er ",
                        English to "",
                    )
                }

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
                    textExpr (
                        Bokmal to "".expr(),
                        Nynorsk to PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr.format(),
                        English to "".expr(),
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget))){
                    textExpr (
                        Bokmal to "".expr(),
                        Nynorsk to PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr.format(),
                        English to "".expr(),
                    )
                }
                text (
                    Bokmal to "",
                    Nynorsk to " kroner. ",
                    English to "",
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0))){
                    text (
                        Bokmal to "",
                        Nynorsk to "Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året. ",
                        English to "",
                    )
                }
            }
        }

        // Velger å ikke slå sammen denne teksten ettersom det er vesentlige forskjeller i styringen på de ulike språklagene.
        //IF(PE_UT_TBU605() = true) THEN      INCLUDE ENDIF
        showIf((FUNKSJON_PE_UT_TBU605)){
            //[TBU052V-TBU073V]

            paragraph {

                //IF(( PE_UT_TBU605V_eller_til_din() )) THEN    INCLUDE ENDIF
                showIf(((FUNKSJON_PE_UT_TBU605V_eller_til_din))){
                    text (
                        Bokmal to "Har det vært en endring i inntekten din",
                        Nynorsk to "",
                        English to "",
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2") )) THEN      INCLUDE ENDIF
                showIf(((FUNKSJON_PE_UT_TBU605V_eller_til_din and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed ektefelle") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed registrert partner") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1-5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1_5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 3-2"))))){
                    text (
                        Bokmal to " eller til din ",
                        Nynorsk to "",
                        English to "",
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true )) THEN    INCLUDE ENDIF
                showIf(((FUNKSJON_PE_UT_TBU605V_eller_til_din and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget))){
                    textExpr (
                        Bokmal to PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + ",",
                        Nynorsk to "".expr(),
                        English to "".expr(),
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("endret_inntekt")){
                    text (
                        Bokmal to "Når inntekten din ",
                        Nynorsk to "",
                        English to "",
                    )
                }

                //IF(( PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2") )) THEN      INCLUDE ENDIF
                showIf(((PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("endret_inntekt") and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed ektefelle") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed registrert partner") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1-5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1_5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 3-2"))))){
                    textExpr (
                        Bokmal to "eller til din ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + " ",
                        Nynorsk to "".expr(),
                        English to "".expr(),
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("endret_inntekt")){
                    text (
                        Bokmal to "endrer seg,",
                        Nynorsk to "",
                        English to "",
                    )
                }
                text (
                    Bokmal to " blir reduksjonen av barnetillegget vurdert på nytt. 50 prosent av den inntekten som overstiger fribeløpet ",
                    Nynorsk to "",
                    English to "",
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert)){
                    text (
                        Bokmal to "blir omregnet til et årlig beløp som tilsvarer ",
                        Nynorsk to "",
                        English to "",
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = false) THEN      INCLUDE ENDIF
                showIf((not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert) and not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert) and not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert) and not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert))){
                    text (
                        Bokmal to "er ",
                        Nynorsk to "",
                        English to "",
                    )
                }

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
                    textExpr (
                        Bokmal to PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr.format(),
                        Nynorsk to "".expr(),
                        English to "".expr(),
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget))){
                    textExpr (
                        Bokmal to PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr.format(),
                        Nynorsk to "".expr(),
                        English to "".expr(),
                    )
                }
                text (
                    Bokmal to " kroner. ",
                    Nynorsk to "",
                    English to "",
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0))){
                    text (
                        Bokmal to "Dette beløpet bruker vi til å redusere barnetillegget ditt for hele året. ",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        }

        // Velger å ikke slå sammen denne teksten ettersom det er vesentlige forskjeller i styringen på de ulike språklagene.
        //IF(PE_UT_TBU605() = true) THEN      INCLUDE ENDIF
        showIf((FUNKSJON_PE_UT_TBU605)){
            //[TBU052V-TBU073V]

            paragraph {

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt") THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("endret_inntekt"))){
                    text (
                        Bokmal to "",
                        Nynorsk to "",
                        English to "If",

                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("endret_inntekt")){
                    text (
                        Bokmal to "",
                        Nynorsk to "",
                        English to "When",
                    )
                }
                text (
                    Bokmal to "",
                    Nynorsk to "",
                    English to " your ",
                )

                //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2") )) THEN      INCLUDE ENDIF
                showIf(((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed ektefelle") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed registrert partner") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1-5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1_5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 3-2"))))){
                    textExpr (
                        Bokmal to "".expr(),
                        Nynorsk to "".expr(),
                        English to "or your ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_EN_cohabiting_partner + "'s ",
                    )
                }
                text (
                    Bokmal to "",
                    Nynorsk to "",
                    English to "income has been changed, your child supplement will be recalculated. 50 percent of income that exceeds the exemption amount ",
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert)){
                    text (
                        Bokmal to "",
                        Nynorsk to "",
                        English to "is recalculated to an annual amount of ",
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = false) THEN      INCLUDE ENDIF
                showIf((not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert) and not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert) and not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert) and not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert))){
                    text (
                        Bokmal to "",
                        Nynorsk to "",
                        English to "is ",
                    )
                }
                text (
                    Bokmal to "",
                    Nynorsk to "",
                    English to "NOK ",
                )

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
                    textExpr (
                        Bokmal to "".expr(),
                        Nynorsk to "".expr(),
                        English to PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr.format(),
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                showIf((not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget)){
                    textExpr (
                        Bokmal to "".expr(),
                        Nynorsk to "".expr(),
                        English to PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr.format(),
                    )
                }
                text (
                    Bokmal to "",
                    Nynorsk to "",
                    English to ". ",
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0))){
                    text (
                        Bokmal to "",
                        Nynorsk to "",
                        English to "This amount will be used to reduce your child supplement during the calendar year.",
                    )
                }
            }
        }

        //IF (PE_UT_TBU605() = true      AND (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0     OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0)    ) THEN   INCLUDE ENDIF
        showIf((FUNKSJON_PE_UT_TBU605 and (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0) or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0)))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor ",
                    Nynorsk to "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor  ",
                    English to "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been ",
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr > 0 OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr > 0) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.greaterThan(0) or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.greaterThan(0))){
                    text (
                        Bokmal to "lagt til",
                        Nynorsk to "lagt til",
                        English to "increased",
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr < 0 OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr < 0) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.lessThan(0) or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.lessThan(0))){
                    text (
                        Bokmal to "redusert",
                        Nynorsk to "trekt frå",
                        English to "reduced",
                    )
                }
                text (
                    Bokmal to " ",
                    Nynorsk to " ",
                    English to " with NOK ",
                )

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
                    textExpr (
                        Bokmal to PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus.format(),
                        Nynorsk to PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus.format(),
                        English to PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus.format(),
                    )
                }

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true
                showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget){
                    textExpr (
                        Bokmal to PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus.format(),
                        Nynorsk to PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus.format(),
                        English to PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus.format(),
                    )
                }
                text (
                    Bokmal to " kroner i beløpet vi reduserer barnetillegget med for resten av året. ",
                    Nynorsk to " kroner i beløpet vi reduserer barnetillegget med for resten av året. ",
                    English to ". ",
                )
            }
        }

        // Velger å ikke slå sammen denne teksten ettersom det er vesentlige forskjeller i styringen på de ulike språklagene.
        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF
        showIf((FUNKSJON_PE_UT_TBU613V and FUNKSJON_PE_UT_TBU613V_1_3)){
            //[TBU052V-TBU073V]

            paragraph {

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt") THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("endret_inntekt"))){
                    text (
                        Bokmal to "",
                        Nynorsk to "Har det vore ei endring i inntekta ",
                        English to "",
                    )
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt"  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2")) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("endret_inntekt") and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed ektefelle") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed registrert") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1-5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1_5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 3-2")))){
                    textExpr (
                        Bokmal to "".expr(),
                        Nynorsk to "til deg eller ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " ",
                        English to "".expr(),
                    )
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt") THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("endret_inntekt"))){
                    text (
                        Bokmal to "",
                        Nynorsk to "di",
                        English to "",
                    )
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt"  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2")) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("endret_inntekt") and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed ektefelle") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed registrert") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1-5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1_5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 3-2")))){
                    text (
                        Bokmal to "",
                        Nynorsk to "n",
                        English to "",
                    )
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt") THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("endret_inntekt"))){
                    text (
                        Bokmal to "",
                        Nynorsk to ",",
                        English to "",
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("endret_inntekt")){
                    text (
                        Bokmal to "",
                        Nynorsk to "Når inntekta di ",
                        English to "",
                    )
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2")) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("endret_inntekt") and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed ektefelle") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed registrert") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1-5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1_5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 3-2")))){
                    textExpr (
                        Bokmal to "".expr(),
                        Nynorsk to "eller til ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din ",
                        English to "".expr(),
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("endret_inntekt")){
                    text (
                        Bokmal to "",
                        Nynorsk to "endrar seg,",
                        English to "",
                    )
                }
                text (
                    Bokmal to "",
                    Nynorsk to " blir reduksjonen av barnetilleggene vurdert på nytt.",
                    English to "",
                )
            }
        }

        // Velger å ikke slå sammen denne teksten ettersom det er vesentlige forskjeller i styringen på de ulike språklagene.
        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF
        showIf((FUNKSJON_PE_UT_TBU613V and FUNKSJON_PE_UT_TBU613V_1_3)){
            //[TBU052V-TBU073V]

            paragraph {

                //IF(( PE_UT_TBU605V_eller_til_din() )) THEN    INCLUDE ENDIF
                showIf(((FUNKSJON_PE_UT_TBU605V_eller_til_din))){
                    text (
                        Bokmal to "Har det vært en endring i inntekten din",
                        Nynorsk to "",
                        English to "",
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2") )) THEN      INCLUDE ENDIF
                showIf(((FUNKSJON_PE_UT_TBU605V_eller_til_din and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed ektefelle") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed registrert partner") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1-5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1_5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 3-2"))))){
                    text (
                        Bokmal to " eller til din",
                        Nynorsk to "",
                        English to "",
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() )) THEN    INCLUDE ENDIF
                showIf(((FUNKSJON_PE_UT_TBU605V_eller_til_din))){
                    textExpr (
                        Bokmal to " ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + ",",
                        Nynorsk to "".expr(),
                        English to "".expr(),
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("endret_inntekt")){
                    text (
                        Bokmal to "Når inntekten din ",
                        Nynorsk to "",
                        English to "",
                    )
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2")) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("endret_inntekt") and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed ektefelle") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed registrert") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1-5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1_5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 3-2")))){
                    textExpr (
                        Bokmal to "eller til din ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + " ",
                        Nynorsk to "".expr(),
                        English to "".expr(),
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("endret_inntekt")){
                    text (
                        Bokmal to "endrer seg,",
                        Nynorsk to "",
                        English to "",
                    )
                }
                text (
                    Bokmal to " blir reduksjonen av barnetilleggene vurdert på nytt. ",
                    Nynorsk to "",
                    English to "",
                )
            }
        }

        // Velger å ikke slå sammen denne teksten ettersom det er vesentlige forskjeller i styringen på de ulike språklagene.
        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF
        showIf((FUNKSJON_PE_UT_TBU613V and FUNKSJON_PE_UT_TBU613V_1_3)){
            //[TBU052V-TBU073V]

            paragraph {

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt") THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("endret_inntekt"))){
                    text (
                        Bokmal to "",
                        Nynorsk to "",
                        English to "If",
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("endret_inntekt")){
                    text (
                        Bokmal to "",
                        Nynorsk to "",
                        English to "When",
                    )
                }
                text (
                    Bokmal to "",
                    Nynorsk to "",
                    English to " your ",
                )

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5") THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed ektefelle") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed registrert partner") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1-5") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 3-2") or PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt.equalTo("bormed 1_5"))){
                    textExpr (
                        Bokmal to "".expr(),
                        Nynorsk to "".expr(),
                        English to "or your ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_EN_cohabiting_partner + "'s ",
                    )
                }
                text (
                    Bokmal to "",
                    Nynorsk to "",
                    English to "income has been changed, your child supplement will be recalculated.",
                )
            }
        }
        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF

        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF

        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF
        showIf((FUNKSJON_PE_UT_TBU613V and FUNKSJON_PE_UT_TBU613V_1_3)){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr(
                    Bokmal to "50 prosent av den inntekten som overstiger fribeløpet for ".expr() + PE_UT_Barnet_Barna_Felles + " som bor med begge sine foreldre ",
                    Nynorsk to "50 prosent av inntekta som overstig fribeløpet for ".expr() + PE_UT_Barnet_Barna_Felles + " som bur med begge foreldra ",
                    English to "50 percent of the income that exceeds the exemption amount for the ".expr() + PE_UT_Barnet_Barna_Felles + " that ",
                )

//PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles = 1
                showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles.equalTo(1)){
                    text(
                        Bokmal to "",
                        Nynorsk to "",
                        English to "lives",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles > 1) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles.greaterThan(1))){
                    text(
                        Bokmal to "",
                        Nynorsk to "",
                        English to "live",
                    )
                }
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to " with both of their parents, ",
                )
                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true
                showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert){
                    text(
                        Bokmal to "blir omregnet til et årlig beløp som tilsvarer ",
                        Nynorsk to "sine blir rekna om til et årleg beløp som svarer til ",
                        English to "is recalculated to an annual amount of",
                    )
                }

                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = false
                showIf(not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert)){
                    text(
                        Bokmal to "er ",
                        Nynorsk to "er ",
                        English to "is",
                    )
                }
                textExpr (
                    Bokmal to PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr.format() + " kroner. ",
                    Nynorsk to PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr.format() + " kroner. ",
                    English to " NOK ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr.format() + ". ",
                )

                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0
                showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0)){
                    text (
                        Bokmal to "Dette beløpet bruker vi til å redusere dette barnetillegget for hele året.",
                        Nynorsk to "Dette beløpet bruker vi til å redusere dette barnetillegget for hele året.",
                        English to "This amount will be used to reduce this child supplement during the calendar year.",
                    )
                }
            }
        }

        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
        showIf((FUNKSJON_PE_UT_TBU613V and FUNKSJON_PE_UT_TBU613V_1_3 and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor ",
                    Nynorsk to "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor ",
                    English to "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been ",
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr > 0) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.greaterThan(0))){
                    text (
                        Bokmal to "lagt til",
                        Nynorsk to "lagt til",
                        English to "increased",
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr < 0) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.lessThan(0))){
                    text (
                        Bokmal to "trukket fra",
                        Nynorsk to "trekt frå",
                        English to "reduced",
                    )
                }
                textExpr (
                    Bokmal to " ".expr() + PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.",
                    Nynorsk to " ".expr() + PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.",
                    English to " with NOK ".expr() + PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus.format() + ".",
                )
            }
        }

        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_4_5() = true) THEN      INCLUDE ENDIF
        showIf((FUNKSJON_PE_UT_TBU613V and FUNKSJON_PE_UT_TBU613V_4_5)){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "For ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikke bor sammen med begge foreldrene ",
                    Nynorsk to "For ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikkje bur med begge foreldra ",
                    English to "For ".expr(),
                )
                showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull.equalTo(1)){
                    text (
                        Bokmal to "",
                        Nynorsk to "",
                        English to "a ",
                    )
                }
                textExpr (
                    Bokmal to "".expr(),
                    Nynorsk to "".expr(),
                    English to PE_UT_Barnet_Barna_Serkull + " that ",
                )

                showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull.equalTo(1)){
                    text (
                        Bokmal to "",
                        Nynorsk to "",
                        English to "does",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull > 1) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull.greaterThan(1))){
                    text (
                        Bokmal to "",
                        Nynorsk to "",
                        English to "do",
                    )
                }
                text (
                    Bokmal to "",
                    Nynorsk to "",
                    English to " not live with both parents ",
                )
                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = false
                showIf(not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert)){
                    text (
                        Bokmal to "er 50 prosent av den inntekten som overstiger fribeløpet ",
                        Nynorsk to "er 50 prosent av den inntekta som overstig fribeløpet ",
                        English to "50 percent of the income that exceeds the exemption amount ",
                    )
                }

                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = true
                showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert){
                    text (
                        Bokmal to "blir 50 prosent av den inntekten som overstiger fribeløpet omregnet til et årlig beløp som tilsvarer ",
                        Nynorsk to "blir 50 prosent av den inntekta som overstig fribeløpet regna om til et årleg beløp som svarer til ",
                        English to "is recalculated to an annual amount of",

                    )
                }
                showIf(not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert)){
                    text (
                        Bokmal to "",
                        Nynorsk to "",
                        English to "is",
                    )
                }

                textExpr (
                    Bokmal to PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr.format() + " kroner. ",
                    Nynorsk to PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr.format() + " kroner. ",
                    English to " NOK ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr.format() + ". ",
                )

                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0
                showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0)){
                    text (
                        Bokmal to "Dette beløpet bruker vi til å redusere dette barnetillegget for hele året. ",
                        Nynorsk to "Dette beløpet bruker vi til å redusere dette barnetillegget for heile året. ",
                        English to "This amount will be used to reduce this child supplement during the calendar year.",
                    )
                }
            }
        }

        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_4_5() = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
        showIf((FUNKSJON_PE_UT_TBU613V and FUNKSJON_PE_UT_TBU613V_4_5 and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor ",
                    Nynorsk to "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor ",
                    English to "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been ",
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr > 0) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.greaterThan(0))){
                    text (
                        Bokmal to "lagt til",
                        Nynorsk to "lagt til",
                        English to "increased",
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr < 0) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.lessThan(0))){
                    text (
                        Bokmal to "trukket fra",
                        Nynorsk to "trekt frå",
                        English to "reduced",
                    )
                }
                textExpr (
                    Bokmal to " ".expr() + PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.",
                    Nynorsk to " ".expr() + PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.",
                    English to " with NOK ".expr() + PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus.format() + ".",
                )
            }
        }

        //IF (PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU606V_TBU608V() = true) THEN    INCLUDE END IF
        showIf((FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU606V_TBU608V)){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Reduksjon av barnetillegg for fellesbarn før skatt ",
                    Nynorsk to "Reduksjon av barnetillegg for fellesbarn før skatt ",
                    English to "Reduction of child supplement payment for joint children before tax,",
                )

                //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVirkningFOM(1)) = false) THEN      INCLUDE ENDIF
                showIf((not(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVirkningFOM)))){
                    text (
                        Bokmal to "i år",
                        Nynorsk to "i år",
                        English to " for this year",
                    )
                }

                //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVirkningFOM(1)) = true) THEN      INCLUDE ENDIF
                showIf((FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVirkningFOM))){
                    text (
                        Bokmal to "for neste år",
                        Nynorsk to "for neste år",
                        English to " for next year",
                    )
                }
            }
        }

        //IF (PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU606V_TBU608V() = true) THEN    INCLUDE END IF
        showIf((FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU606V_TBU608V)){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Årlig barnetillegg før reduksjon ut fra inntekt",
                    Nynorsk to "Årleg barnetillegg før reduksjon ut frå inntekt",
                    English to "Yearly child supplement before income reduction  ",
                )
                textExpr (
                    Bokmal to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbruttoPerAr.format() + " kr",
                    Nynorsk to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbruttoPerAr.format() + " kr",
                    English to "NOK ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbruttoPerAr.format(),
                )
            }
        }

        //IF (PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU606V_TBU608V() = true) THEN    INCLUDE END IF
        showIf((FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU606V_TBU608V)){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Samlet inntekt brukt i fastsettelse av barnetillegget er ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning.format() + " kr",
                    Nynorsk to "Samla inntekt brukt i fastsetting av barnetillegget er ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning.format() + " kr",
                    English to "Total income applied in calculation of reduction in child supplement for joint children areNOK ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning.format(),
                )
            }
        }

        //IF (PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU606V_TBU608V() = true      AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0              OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0                     AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0                    ) 	)     ) THEN    INCLUDE END IF
        showIf((FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU606V_TBU608V and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.greaterThan(0) or (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.equalTo(0) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0))))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Fribeløp brukt i fastsettelsen av barnetillegget er ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop.format() + " kr",
                    Nynorsk to "Fribeløp brukt i fastsetting av barnetillegget er ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop.format() + " kr",
                    English to "Exemption amount applied in calculation of reduction in child supplement are NOK ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop.format(),
                )
            }
        }

        //IF (PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU606V_TBU608V() = true      AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0              OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0                     AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0                    ) 		)     ) THEN    INCLUDE END IF
        showIf((FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU606V_TBU608V and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.notEqualTo(0) or (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.equalTo(0) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0))))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Inntekt over fribeløpet er ".expr() + PE_UT_BTFBInntektBruktiAvkortningMinusBTFBfribelop.format() + " kr",
                    Nynorsk to "Inntekt over fribeløpet er ".expr() + PE_UT_BTFBInntektBruktiAvkortningMinusBTFBfribelop.format() + " kr",
                    English to "Income exceeding the exemption amount NOK ".expr() + PE_UT_BTFBInntektBruktiAvkortningMinusBTFBfribelop.format(),
                )
            }
        }

        //IF (PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU606V_TBU608V() = true      AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0              OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0                     AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0                    ) 		) 	 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr > 0     ) THEN    INCLUDE END IF
        showIf((FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU606V_TBU608V and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.notEqualTo(0) or (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.equalTo(0) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0))) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr.greaterThan(0))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet",
                    Nynorsk to "- 50 prosent av inntekta som overstig fribeløpet",
                    English to "- 50 percent of income exceeding the allowance amount ",
                )

                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true
                showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert){
                    text (
                        Bokmal to "(oppgitt som et årlig beløp)",
                        Nynorsk to "(oppgitt som eit årleg beløp)",
                        English to "(calculated to an annual amount)",
                    )
                }
                textExpr (
                    Bokmal to PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr.format() + " kr",
                    Nynorsk to PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr.format() + " kr",
                    English to "NOK ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr.format(),
                )
            }
        }

        //IF((PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU606V_TBU608V() = true) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
        showIf(((FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU606V_TBU608V) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0))){
            //[TBU052V-TBU073V]

            paragraph {

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr > 0) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.greaterThan(0))){
                    text (
                        Bokmal to "-",
                        Nynorsk to "-",
                        English to "-",
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr < 0) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.lessThan(0))){
                    text (
                        Bokmal to "+",
                        Nynorsk to "+",
                        English to "+",
                    )
                }
                text (
                    Bokmal to " Beløp som er brukt for å justere reduksjonen av barnetillegget",
                    Nynorsk to " Beløp som er brukt for å justere reduksjonen av barnetillegget",
                    English to " Amount which is used to adjust the reduction of child supplement",
                )
                textExpr (
                    Bokmal to PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr_UtenMinus.format() + " kr",
                    Nynorsk to PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus.format() + " kr",
                    English to "NOK ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr_UtenMinus.format(),
                )
            }
        }

        //IF (PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU606V_TBU608V() = true      AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0              OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0                     AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0                    ) 		)     ) THEN    INCLUDE END IF
        showIf((FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU606V_TBU608V and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.notEqualTo(0) or (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.equalTo(0) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0))))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "= Årlig barnetillegg etter reduksjon ut fra inntekt",
                    Nynorsk to "= Årleg barnetillegg etter reduksjon ut frå inntekt",
                    English to "= Calculation of the yearly reduction of the child supplement",
                )
                textExpr (
                    Bokmal to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBnettoPerAr.format() + " kr",
                    Nynorsk to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBnettoPerAr.format() + " kr",
                    English to "NOK ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBnettoPerAr.format(),
                )
            }
        }

        //IF (PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU606V_TBU608V() = true      AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0              OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0                     AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0                    ) 		)     ) THEN    INCLUDE END IF
        showIf((FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU606V_TBU608V and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.notEqualTo(0) or (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.equalTo(0) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0))))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Utbetaling av barnetillegg per måned ",
                    Nynorsk to "Utbetaling av barnetillegg per månad ",
                    English to "Child supplement payment for the remaining months of the year",
                )
                textExpr (
                    Bokmal to PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.format() + " kr",
                    Nynorsk to PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.format() + " kr",
                    English to "NOK ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.format(),
                )
            }
        }

        //IF (PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU606V_TBU608V() = true     AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0     AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0     ) THEN    INCLUDE END IF
        showIf((FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU606V_TBU608V and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.equalTo(0) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Grensen for å få utbetalt barnetillegg",
                    Nynorsk to "Grensa for å få utbetalt barnetillegg",
                    English to "The income limit for receiving child supplement",
                )
                textExpr (
                    Bokmal to PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_Inntektstak.format() + " kr",
                    Nynorsk to PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_Inntektstak.format() + " kr",
                    English to "NOK ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_Inntektstak.format(),
                )
            }
        }


        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0 AND PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU606V_TBU608V() = true) THEN      INCLUDE ENDIF
        showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.greaterThan(0) and FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU606V_TBU608V)){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Du vil få utbetalt ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.format() + " kroner i måneden før skatt i barnetillegg",
                    Nynorsk to "Du vil få utbetalt ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.format() + " kroner i månaden før skatt i barnetillegg",
                    English to "You will receive a monthly child supplement payment of NOK ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.format(),
                )

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_UT_Etteroppgjor_BT_Utbetalt() = true) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and FUNKSJON_PE_UT_Etteroppgjor_BT_Utbetalt)){
                    textExpr (
                        Bokmal to " for ".expr() + PE_UT_Barnet_Barna_Felles + " som bor med begge sine foreldre",
                        Nynorsk to " for ".expr() + PE_UT_Barnet_Barna_Felles + " som bur saman med begge foreldra sine",
                        English to " for the ".expr() + PE_UT_Barnet_Barna_Felles + " who live together with both parents",
                    )
                }
                text (
                    Bokmal to ". ",
                    Nynorsk to ".",
                    English to ".",
                )
            }
        }

        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU606V_TBU608V() = true) THEN      INCLUDE ENDIF
        showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.equalTo(0) and FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU606V_TBU608V)){
            //[TBU052V-TBU073V]

            paragraph {

                //IF(PE_UT_TBU608_Far_Ikke() = true) THEN      INCLUDE ENDIF
                showIf((FUNKSJON_PE_UT_TBU608_Far_Ikke)){
                    text (
                        Bokmal to "Du får ikke utbetalt barnetillegget ",
                        Nynorsk to "Du får ikkje utbetalt barnetillegget ",
                        English to "You will not receive a child supplement ",
                    )
                }

                //IF(PE_UT_TBU608_Far_Ikke() = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                showIf((FUNKSJON_PE_UT_TBU608_Far_Ikke and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget)){
                    textExpr (
                        Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bor med begge sine foreldre ",
                        Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bur saman med begge foreldra sine ",
                        English to "for the ".expr() + PE_UT_Barnet_Barna_Felles + " who live together with both parents ",
                    )
                }

                //IF(PE_UT_TBU608_Far_Ikke() = true) THEN      INCLUDE ENDIF
                showIf((FUNKSJON_PE_UT_TBU608_Far_Ikke)){
                    text (
                        Bokmal to "fordi samlet inntekt er over grensen for å få utbetalt barnetillegg. ",
                        Nynorsk to "fordi samla inntekt er over grensa for å få utbetalt barnetillegg. ",
                        English to "because your income is over the income limit for receiving a child supplement. ",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.equalTo(0) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0))){
                    text (
                        Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. ",
                        Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. ",
                        English to "You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year.",
                    )
                }
            }
        }

        //IF (PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU609V_TBU611V() = true) THEN    INCLUDE END IF
        showIf((FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU609V_TBU611V)){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Reduksjon av barnetillegg for særkullsbarn før skatt ",
                    Nynorsk to "Reduksjon av barnetillegg for særkullsbarn før skatt ",
                    English to "Reduction of child supplement payment for children from a previous relationship before tax, ",
                )

                //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVirkningFOM(1)) = false) THEN      INCLUDE ENDIF
                showIf((not(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVirkningFOM)))){
                    text (
                        Bokmal to "i år",
                        Nynorsk to "i år",
                        English to "for this year",
                    )
                }

                //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVirkningFOM(1)) = true) THEN      INCLUDE ENDIF
                showIf((FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVirkningFOM))){
                    text (
                        Bokmal to "for neste år",
                        Nynorsk to "for neste år",
                        English to "for next year",
                    )
                }
            }
        }

        //IF (PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU609V_TBU611V() = true) THEN    INCLUDE END IF
        showIf((FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU609V_TBU611V)){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Årlig barnetillegg før reduksjon ut fra inntekt",
                    Nynorsk to "Årleg barnetillegg før reduksjon ut frå inntekt",
                    English to "Yearly child supplement before income reduction  ",
                )
                textExpr (
                    Bokmal to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBbruttoPerAr.format() + " kr",
                    Nynorsk to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBbruttoPerAr.format() + " kr",
                    English to "NOK ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBbruttoPerAr.format(),
                )
            }
        }

        //IF (PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU609V_TBU611V() = true) THEN    INCLUDE END IF
        showIf((FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU609V_TBU611V)){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Samlet inntekt brukt i fastsettelse av barnetillegget er ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.format() + " kr",
                    Nynorsk to "Samla inntekt brukt i fastsetting av barnetillegget er ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.format() + " kr",
                    English to "Total income applied in calculation of reduction in child supplement from a previous relationship NOK ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.format(),
                )
            }
        }

        //IF (PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU609V_TBU611V() = true     AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0         OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0             AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0            )         ) 	) THEN    INCLUDE END IF
        showIf((FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU609V_TBU611V and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.greaterThan(0) or (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.equalTo(0) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0))))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Fribeløp brukt i fastsettelsen av barnetillegget er ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop.format() + " kr",
                    Nynorsk to "Fribeløp brukt i fastsetting av barnetillegget er ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop.format() + " kr",
                    English to "Exemption amount applied in calculation of reduction in child supplement are NOK ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop.format(),
                )
            }
        }

        //IF (PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU609V_TBU611V() = true     AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0         OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0             AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0            )         ) 	) THEN    INCLUDE END IF
        showIf((FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU609V_TBU611V and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.notEqualTo(0) or (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.equalTo(0) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0))))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Inntekt over fribeløpet er ".expr() + PE_UT_BTSBinntektBruktiAvkortningMinusBTSBfribelop.format() + " kr",
                    Nynorsk to "Inntekt over fribeløpet er ".expr() + PE_UT_BTSBinntektBruktiAvkortningMinusBTSBfribelop.format() + " kr",
                    English to "Income exceeding the exemption amount NOK ".expr() + PE_UT_BTSBinntektBruktiAvkortningMinusBTSBfribelop.format(),
                )
            }
        }

        //IF (PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU609V_TBU611V() = true     AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0         OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0             AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0            )      AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr > 0         ) 	) THEN    INCLUDE END IF
        showIf((FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU609V_TBU611V and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.notEqualTo(0) or (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.equalTo(0) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0)) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr.greaterThan(0)))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet",
                    Nynorsk to "- 50 prosent av inntekta som overstig fribeløpet",
                    English to "- 50 percent of income exceeding the allowance amount ",
                )

                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true
                showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert){
                    text (
                        Bokmal to "(oppgitt som et årlig beløp)",
                        Nynorsk to "(oppgitt som eit årleg beløp)",
                        English to "(calculated to an annual amount)",
                    )
                }
                textExpr (
                    Bokmal to PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr.format() + " kr",
                    Nynorsk to PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr.format() + " kr",
                    English to "NOK ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr.format(),
                )
            }
        }

        //IF((PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU609V_TBU611V() = true) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
        showIf(((FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU609V_TBU611V) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0))){
            //[TBU052V-TBU073V]

            paragraph {

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr > 0) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.greaterThan(0))){
                    text (
                        Bokmal to "-",
                        Nynorsk to "-",
                        English to "-",
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr < 0) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.lessThan(0))){
                    text (
                        Bokmal to "+",
                        Nynorsk to "+",
                        English to "+",
                    )
                }
                text (
                    Bokmal to " Beløp som er brukt for å justere reduksjonen av barnetillegget",
                    Nynorsk to " Beløp som er brukt for å justere reduksjonen av barnetillegget",
                    English to " Amount which is used to adjust the reduction of child supplement",
                )
                textExpr (
                    Bokmal to PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr_UtenMinus.format() + " kr",
                    Nynorsk to PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus.format() + " kr",
                    English to "NOK ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr_UtenMinus.format(),
                )
            }
        }

        //IF (PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU609V_TBU611V() = true     AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0         OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0             AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0            )         ) 	) THEN    INCLUDE END IF
        showIf((FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU609V_TBU611V and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.notEqualTo(0) or (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.equalTo(0) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0))))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "= Årlig barnetillegg etter reduksjon ut fra inntekt",
                    Nynorsk to "= Årleg barnetillegg etter reduksjon ut frå inntekt",
                    English to "= Calculation of the yearly reduction of the child supplement",
                )
                textExpr (
                    Bokmal to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBnettoPerAr.format() + " kr",
                    Nynorsk to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBnettoPerAr.format() + " kr",
                    English to "NOK ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBnettoPerAr.format(),
                )
            }
        }

        //IF (PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU609V_TBU611V() = true     AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0         OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0             AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0            )         ) 	) THEN    INCLUDE END IF
        showIf((FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU609V_TBU611V and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.notEqualTo(0) or (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.equalTo(0) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0))))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Utbetaling av barnetillegg per måned ",
                    Nynorsk to "Utbetaling av barnetillegg per månad ",
                    English to "Child supplement payment for the remaining months of the year",
                )
                textExpr (
                    Bokmal to PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.format() + " kr",
                    Nynorsk to PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.format() + " kr",
                    English to "NOK ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.format(),
                )
            }
        }

        //IF (PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU609V_TBU611V() = true     AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0     AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 	) THEN    INCLUDE END IF
        showIf((FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU609V_TBU611V and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.equalTo(0) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Grensen for å få utbetalt barnetillegg",
                    Nynorsk to "Grensa for å få utbetalt barnetillegg",
                    English to "The income limit for receiving child supplement ",
                )
                textExpr (
                    Bokmal to PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_Inntektstak.format() + " kr",
                    Nynorsk to PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_Inntektstak.format() + " kr",
                    English to "NOK ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_Inntektstak.format(),
                )
            }
        }


        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0 AND PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU609V_TBU611V() = true) THEN      INCLUDE ENDIF
        showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.greaterThan(0) and FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU609V_TBU611V)){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Du vil få utbetalt ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.format() + " kroner i måneden før skatt i barnetillegg",
                    Nynorsk to "Du vil få utbetalt ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.format() + " kroner i månaden før skatt i barnetillegg",
                    English to "You will receive a monthly child supplement payment of NOK ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.format(),
                )

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_UT_Etteroppgjor_BT_Utbetalt() = true) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and FUNKSJON_PE_UT_Etteroppgjor_BT_Utbetalt)){
                    textExpr (
                        Bokmal to " for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikke bor med begge sine foreldre",
                        Nynorsk to " for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikkje bur saman med begge foreldra",
                        English to " for the ".expr() + PE_UT_Barnet_Barna_Serkull + " who do not live together with both parents",
                    )
                }
                text (
                    Bokmal to ". ",
                    Nynorsk to ". ",
                    English to ".",
                )
            }
        }

        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU609V_TBU611V() = true) THEN      INCLUDE ENDIF
        showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.equalTo(0) and FUNKSJON_PE_UT_TBU606V_TBU611V and FUNKSJON_PE_UT_TBU609V_TBU611V)){
            //[TBU052V-TBU073V]

            paragraph {

                //IF(PE_UT_TBU611_Far_Ikke() = true) THEN      INCLUDE ENDIF
                showIf((FUNKSJON_PE_UT_TBU611_Far_Ikke)){
                    text (
                        Bokmal to "Du får ikke utbetalt barnetillegget ",
                        Nynorsk to "Du får ikkje utbetalt barnetillegget ",
                        English to "You will not receive a child supplement ",
                    )
                }

                //IF(PE_UT_TBU611_Far_Ikke() = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                showIf((FUNKSJON_PE_UT_TBU611_Far_Ikke and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget)){
                    textExpr (
                        Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikke bor med begge sine foreldre ",
                        Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikkje bur saman med begge foreldra ",
                        English to "for the ".expr() + PE_UT_Barnet_Barna_Serkull + " who do not live together with both parents ",
                    )
                }

                //IF(PE_UT_TBU611_Far_Ikke() = true) THEN      INCLUDE ENDIF
                showIf((FUNKSJON_PE_UT_TBU611_Far_Ikke)){
                    text (
                        Bokmal to "fordi samlet inntekt er over grensen for å få utbetalt barnetillegg. ",
                        Nynorsk to "fordi samla inntekt er over grensa for å få utbetalt barnetillegg. ",
                        English to "because your income is over the income limit for receiving a child supplement. ",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
                showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.equalTo(0) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0))){
                    text (
                        Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                        Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. ",
                        English to "You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year. ",
                    )
                }
            }
        }
    }
}