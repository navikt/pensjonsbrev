package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.legacy.grunnlag_persongrunnlagsliste_personbostedsland
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.quoted
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class TBU052V_TBU073V_EtteroppgjoerAvUforetrygdOgBarnetillegg(
    val pe: Expression<PE>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        //[TBU052V-TBU073V]

        title1 {
            text (
                Bokmal to "Etteroppgjør av uføretrygd",
                Nynorsk to "Etteroppgjer av uføretrygd",
                English to "Final settlement of disability benefit",
            )

            //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true )) THEN   INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))){
                text (
                    Bokmal to " og barnetillegg",
                    Nynorsk to " og barnetillegg",
                    English to " and child supplement",
                )
            }
        }

        paragraph {
            text(
                Bokmal to "Hvert år når likningen er klar mottar vi opplysninger om inntekten",
                Nynorsk to "Kvart år når likninga er klar får vi opplysningar om inntekta",
                English to "Once the tax assessment for the year in question is complete, we will receive information about your income from the Tax Administration. We will use the tax assessment data to calculate the correct disability benefit payments ",
            )

            //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true )) THEN   INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()))) {
                text(
                    Bokmal to " til deg og din ",
                    Nynorsk to " til deg og ",
                    English to "",
                )
            }

            //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle") )) THEN   INCLUDE ENDIF
            showIf(
                ((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                    .equalTo(
                        "bormed ektefelle"
                    ))))
            ) {
                text(
                    Bokmal to "ektefelle",
                    Nynorsk to "ektefellen",
                    English to "",

                    )
            }

            //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner") )) THEN   INCLUDE ENDIF
            showIf(
                ((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                    .equalTo(
                        "bormed registrert partner"
                    ))))
            ) {
                text(
                    Bokmal to "partner",
                    Nynorsk to "partnaren",
                    English to "",

                    )
            }

            //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5"     OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2") )) THEN   INCLUDE ENDIF
            showIf(
                ((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                    .equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2"))))
            ) {
                text(
                    Bokmal to "samboer",
                    Nynorsk to "sambuaren",
                    English to "",
                )
            }

            //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true )) THEN   INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()))) {
                text(
                    Bokmal to "",
                    Nynorsk to " din",
                    English to "",
                )
            }

            //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false )) THEN   INCLUDE ENDIF
            showIf(((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())))) {
                text(
                    Bokmal to " din",
                    Nynorsk to " di",
                    English to "",
                )
            }
            text(
                Bokmal to " fra Skatteetaten. Vi bruker likningsopplysningene til å beregne riktig utbetaling av uføretrygd",
                Nynorsk to " frå Skatteetaten. Vi bruker opplysningane om inntekt til å berekne riktig utbetaling av uføretrygd",
                English to "",
            )

            //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true )) THEN   INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))) {
                text(
                    Bokmal to " og barnetillegg",
                    Nynorsk to " og barnetillegg",
                    English to "and child supplement ",
                )
            }
            text(
                Bokmal to " for året likningen gjelder for. Har du fått for mye eller for lite utbetalt i uføretrygd",
                Nynorsk to " for det året som likninga gjeld for. Har du fått for mykje eller for lite utbetalt i uføretrygd",
                English to "for the year the tax assessment is due to. If you have received too much or too little in disability payments",
            )

            //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true )) THEN   INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))) {
                text(
                    Bokmal to " og barnetillegg",
                    Nynorsk to " og barnetillegg",
                    English to " and child supplement",
                )
            }
            text(
                Bokmal to ", vil vi foreta et etteroppgjør. ",
                Nynorsk to ", vil vi gjere eit etteroppgjer. ",
                English to ", we will conduct a final settlement. ",
            )

            //IF(FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) <> "nor" AND FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) <> "") THEN      INCLUDE ENDIF
            showIf(
                (pe.grunnlag_persongrunnlagsliste_personbostedsland()
                    .notEqualTo("nor") and pe.grunnlag_persongrunnlagsliste_personbostedsland().notEqualTo(
                    ""
                ))
            ) {
                text(
                    Bokmal to "Har du meldt inn inntekt fra arbeid i et annet land enn Norge, og vi ikke mottar inntektsopplysninger fra Skatteetaten, gjør vi etteroppgjøret ut fra inntekten din fra utlandet. ",
                    Nynorsk to "Har du meldt inn inntekt frå arbeid i eit anna land enn Noreg, og vi ikkje får opplysningar om inntekt frå Skatteetaten, gjer vi eit etteroppgjer ut frå inntekta di frå utlandet. ",
                    English to "We will do the final settlement based on your reported income from another country, if you have not received income in Norway. ",
                )
            }
            text(
                Bokmal to "Har du fått utbetalt for lite, vil vi utbetale dette beløpet til deg. Har du fått utbetalt for mye, må du betale dette tilbake.",
                Nynorsk to "Har du fått utbetalt for lite, vil vi utbetale dette beløpet til deg. Har du fått utbetalt for mykje, må du betale dette tilbake.",
                English to "If you have received too little, we will pay out the amount to you. If you have received too much, you will have to pay the amount back.",
            )
        }
        paragraph {

            textExpr(
                Bokmal to "Det er viktig at du melder fra om inntektsendringer slik at uføretrygden".expr(),
                Nynorsk to "Det er viktig at du melder frå om inntektsendringar slik at uføretrygda ".expr(),
                English to "It is important that you report changes in income, so that you receive the correct disability benefit payments and child supplement. You can easily register change in income under the option ".expr() + quoted("uføretrygd") +" at $NAV_URL.",
            )
            //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true )) THEN   INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))){
                text (
                    Bokmal to " og barnetillegget",
                    Nynorsk to "og barnetillegget ",
                    English to "",
                )
            }
            textExpr (
                Bokmal to " blir så riktig som mulig. Du kan enkelt melde fra om inntektsendringer under menyvalget ".expr() + quoted("uføretrygd") +" når du logger deg inn på $NAV_URL.",
                Nynorsk to "blir riktig utbetalt. Du kan enkelt melde frå om inntektsendringar under menyvalet ".expr() + quoted("uføretrygd") +" når du loggar deg inn på $NAV_URL.",
                English to "".expr(),
            )
        }

    }
}