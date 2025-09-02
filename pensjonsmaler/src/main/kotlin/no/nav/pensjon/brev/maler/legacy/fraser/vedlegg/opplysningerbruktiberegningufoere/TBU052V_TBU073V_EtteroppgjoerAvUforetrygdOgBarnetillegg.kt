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

data class TBU052V_TBU073V_EtteroppgjoerAvUforetrygdOgBarnetillegg(
    val pe: Expression<PE>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        //[TBU052V-TBU073V]

        title1 {
            text (
                bokmal { + "Etteroppgjør av uføretrygd" },
                nynorsk { + "Etteroppgjer av uføretrygd" },
                english { + "Final settlement of disability benefit" },
            )

            //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true )) THEN   INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))){
                text (
                    bokmal { + " og barnetillegg" },
                    nynorsk { + " og barnetillegg" },
                    english { + " and child supplement" },
                )
            }
        }

        paragraph {
            text(
                bokmal { + "Hvert år når likningen er klar mottar vi opplysninger om inntekten" },
                nynorsk { + "Kvart år når likninga er klar får vi opplysningar om inntekta" },
                english { + "Once the tax assessment for the year in question is complete, we will receive information about your income from the Tax Administration. We will use the tax assessment data to calculate the correct disability benefit payments " },
            )

            //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true )) THEN   INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()))) {
                text(
                    bokmal { + " til deg og din " },
                    nynorsk { + " til deg og " },
                    english { + "" },
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
                    bokmal { + "ektefelle" },
                    nynorsk { + "ektefellen" },
                    english { + "" },

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
                    bokmal { + "partner" },
                    nynorsk { + "partnaren" },
                    english { + "" },

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
                    bokmal { + "samboer" },
                    nynorsk { + "sambuaren" },
                    english { + "" },
                )
            }

            //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true )) THEN   INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()))) {
                text(
                    bokmal { + "" },
                    nynorsk { + " din" },
                    english { + "" },
                )
            }

            //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false )) THEN   INCLUDE ENDIF
            showIf(((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())))) {
                text(
                    bokmal { + " din" },
                    nynorsk { + " di" },
                    english { + "" },
                )
            }
            text(
                bokmal { + " fra Skatteetaten. Vi bruker likningsopplysningene til å beregne riktig utbetaling av uføretrygd" },
                nynorsk { + " frå Skatteetaten. Vi bruker opplysningane om inntekt til å berekne riktig utbetaling av uføretrygd" },
                english { + "" },
            )

            //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true )) THEN   INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))) {
                text(
                    bokmal { + " og barnetillegg" },
                    nynorsk { + " og barnetillegg" },
                    english { + "and child supplement " },
                )
            }
            text(
                bokmal { + " for året likningen gjelder for. Har du fått for mye eller for lite utbetalt i uføretrygd" },
                nynorsk { + " for det året som likninga gjeld for. Har du fått for mykje eller for lite utbetalt i uføretrygd" },
                english { + "for the year the tax assessment is due to. If you have received too much or too little in disability payments" },
            )

            //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true )) THEN   INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))) {
                text(
                    bokmal { + " og barnetillegg" },
                    nynorsk { + " og barnetillegg" },
                    english { + " and child supplement" },
                )
            }
            text(
                bokmal { + ", vil vi foreta et etteroppgjør. " },
                nynorsk { + ", vil vi gjere eit etteroppgjer. " },
                english { + ", we will conduct a final settlement. " },
            )

            //IF(FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) <> "nor" AND FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) <> "") THEN      INCLUDE ENDIF
            showIf(
                (pe.grunnlag_persongrunnlagsliste_personbostedsland()
                    .notEqualTo("nor") and pe.grunnlag_persongrunnlagsliste_personbostedsland().notEqualTo(
                    ""
                ))
            ) {
                text(
                    bokmal { + "Har du meldt inn inntekt fra arbeid i et annet land enn Norge, og vi ikke mottar inntektsopplysninger fra Skatteetaten, gjør vi etteroppgjøret ut fra inntekten din fra utlandet. " },
                    nynorsk { + "Har du meldt inn inntekt frå arbeid i eit anna land enn Noreg, og vi ikkje får opplysningar om inntekt frå Skatteetaten, gjer vi eit etteroppgjer ut frå inntekta di frå utlandet. " },
                    english { + "We will do the final settlement based on your reported income from another country, if you have not received income in Norway. " },
                )
            }
            text(
                bokmal { + "Har du fått utbetalt for lite, vil vi utbetale dette beløpet til deg. Har du fått utbetalt for mye, må du betale dette tilbake." },
                nynorsk { + "Har du fått utbetalt for lite, vil vi utbetale dette beløpet til deg. Har du fått utbetalt for mykje, må du betale dette tilbake." },
                english { + "If you have received too little, we will pay out the amount to you. If you have received too much, you will have to pay the amount back." },
            )
        }
        paragraph {

            text(
                bokmal { + "Det er viktig at du melder fra om inntektsendringer slik at uføretrygden" },
                nynorsk { + "Det er viktig at du melder frå om inntektsendringar slik at uføretrygda " },
                english { + "It is important that you report changes in income, so that you receive the correct disability benefit payments and child supplement. You can easily register change in income under the option " + quoted("uføretrygd") +" at $NAV_URL." },
            )
            //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true )) THEN   INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))){
                text (
                    bokmal { + " og barnetillegget" },
                    nynorsk { + "og barnetillegget " },
                    english { + "" },
                )
            }
            text (
                bokmal { + " blir så riktig som mulig. Du kan enkelt melde fra om inntektsendringer under menyvalget " + quoted("uføretrygd") +" når du logger deg inn på $NAV_URL." },
                nynorsk { + "blir riktig utbetalt. Du kan enkelt melde frå om inntektsendringar under menyvalet " + quoted("uføretrygd") +" når du loggar deg inn på $NAV_URL." },
                english { + "" },
            )
        }

    }
}