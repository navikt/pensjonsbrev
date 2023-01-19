package no.nav.pensjon.brev.maler.fraser.ufoer

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDto
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.textExpr

object KombinereUfoeretrygdMedInntekt {

    // TBU1201, TBBU1203, TBU1204
    data class KombinereUfoeretrygdOgInntektOverskrift(
        val ufoeregrad: Expression<Int>,
        val utbetalingsgrad: Expression<Int>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val harFullUfoeregrad = ufoeregrad.equalTo(100)
            val harDelvisUfoeregrad = (ufoeregrad.greaterThan(0) and not(harFullUfoeregrad))
            val harFullUtbetalingsgrad = utbetalingsgrad.equalTo(100)
            title1 {
                showIf(harFullUfoeregrad and harFullUtbetalingsgrad) {
                    textExpr(
                        Bokmal to "Skal du kombinere uføretrygd og inntekt?".expr(),
                        Nynorsk to "Skal du kombinere uføretrygd og inntekt?".expr(),
                        English to "Will you combine disability benefit with salary income?".expr()
                    )
                }.orShowIf(harDelvisUfoeregrad) {
                    textExpr(
                        Bokmal to "For deg som kombinerer uføretrygd og inntekt".expr(),
                        Nynorsk to "For deg som kombinerer uføretrygd og inntekt".expr(),
                        English to "If you combine disabilty benefit with salary income".expr()
                    )
                }
            }
        }
    }

    // TBU1204, TBU2251
    data class InntektVedSidenAvUfoeretrygd(
        val ufoeregrad: Expression<Int>,
        val utbetalingsgrad: Expression<Int>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(ufoeregrad.equalTo(utbetalingsgrad)) {
                paragraph {
                    textExpr(
                        Bokmal to "Du har mulighet til å ha inntekt ved siden av uføretrygden din. Det lønner seg å jobbe, fordi inntekt og uføretrygd alltid vil være høyere enn uføretrygd alene.".expr(),
                        Nynorsk to "".expr(),
                        English to "You have the possibilty to earn an income in addition to your disability benefit. It pays to work, your income and disabiltiy benefit combined will always be higher than disability benefit alone.".expr()
                    )
                }
            }.orShowIf(utbetalingsgrad.lessThan(ufoeregrad)) {
                paragraph {
                    textExpr(
                        Bokmal to "Utbetalingen av uføretrygden din er redusert fordi du har inntekt utover inntektsgrensen. Det lønner seg likevel å jobbe, fordi inntekt og uføretrygd alltid vil være høyere enn uføretrygd alene.".expr(),
                        Nynorsk to "".expr(),
                        English to "Your disability benefit payment is reduced because your salary income exceeds the income limit for disability benefit. It still pays to work however, because income and disability benefit combined will always be higher than disability benefit alone.".expr()
                    )
                }
            }
        }
    }

    // TBU1205, TBU1296, TBU1206
    data class Inntektsgrense(
        val beloepsgrense: Expression<Kroner>,
        val grunnbeloep: Expression<Kroner>,
        val harInntektEtterUfoere: Expression<Boolean>,
        val inntektsgrense: Expression<Kroner>,
        val inntektsgrenseNesteAar: Expression<Kroner>,
        val ufoeregrad: Expression<Int>,
        val harBeloepsgrense60000: Expression<Boolean>,
        val harFullUfoeregrad: Expression<Boolean>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val inntektsgrenseFaktisk = inntektsgrense.equalTo(0)
            // inntektsgrenseFaktisk = if inntektsgrenseNesteAar = 0 -> inntektsgrense, or -> inntektsgrenseNesteAar
            showIf(
                harFullUfoeregrad and not(harInntektEtterUfoere) and (beloepsgrense.notEqualTo(grunnbeloep) and not(
                    harBeloepsgrense60000
                )
                        and not(harInntektEtterUfoere))
            ) {
                paragraph {
                    textExpr(
                        Bokmal to "Du kan ha en årlig inntekt på 40 prosent av folketrygdens grunnbeløp, uten at uføretrygden din blir redusert. I dag er dette ".expr() +
                                ifElse(
                                    inntektsgrenseFaktisk,
                                    ifTrue = inntektsgrense.format(),
                                    ifFalse = inntektsgrenseNesteAar.format()
                                ) + " kroner. Dette er inntektsgrensen din.".expr(),
                        Nynorsk to "".expr(),
                        English to "".expr()
                    )
                }
            }.orShowIf(harFullUfoeregrad and beloepsgrense.equalTo(60000)) {
                paragraph {
                    textExpr(
                        Bokmal to "Du kan ha en årlig inntekt på 60 000 kroner uten at uføretrygden din blir redusert. Dette er inntektsgrensen din.".expr(),
                        Nynorsk to "".expr(),
                        English to "".expr()
                    )
                }
            }.orShowIf(grunnbeloep.equalTo(beloepsgrense)) {
                paragraph {
                    textExpr(
                        Bokmal to "Du kan ha en årlig inntekt på folketrygdens grunnbeløp fordi du er i varig tilrettelagt arbeid, uten at uføretrygden din blir redusert. I dag er dette ".expr() + inntektsgrense.format() + " kroner. Dette er inntektsgrensen din.".expr(),
                        Nynorsk to "".expr(),
                        English to "".expr()

                    )
                }
            }
        }
    }

    // TBU1207, TBU2357   TODO: Er TBU2357 foreldret?
    data class InntektsgrenseLagtTilGrunn(
        val inntektsgrense: Expression<Kroner>,
        val inntektsgrenseNesteAar: Expression<Kroner>,
        val beloepsgrense: Expression<Kroner>,
        val oieu: Expression<Kroner>,
        val grunnbeloep: Expression<Kroner>,
        val ufoeregrad: Expression<Int>,
        val harInntektEtterUfoere: Expression<Boolean>,
        val harBeloepsgrense60000: Expression<Boolean>,
        val harFullUfoeregrad: Expression<Boolean>,
        val harDelvisUfoeregrad: Expression<Boolean>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val inntektsgrenseFaktisk = inntektsgrense.equalTo(0)

            showIf(not(harBeloepsgrense60000) and beloepsgrense.notEqualTo(grunnbeloep) and (harDelvisUfoeregrad or (harInntektEtterUfoere and harFullUfoeregrad))) {
                paragraph {
                    textExpr(
                        Bokmal to "Vi har lagt til grunn at du framover skal ha en inntekt på ".expr() + oieu.format() + " kroner per år. Du kan i tillegg ha en årlig inntekt på 40 prosent av folketrygdens grunnbeløp, uten at uføretrygden din blir redusert. Inntektsgrensen din blir derfor ".expr() +
                                ifElse(
                                    inntektsgrenseFaktisk,
                                    ifTrue = inntektsgrense.format(),
                                    ifFalse = inntektsgrenseNesteAar.format()
                                ) + " kroner.".expr(),
                        Nynorsk to "".expr(),
                        English to "".expr()
                    )
                }
            }.orShowIf(harBeloepsgrense60000 and harDelvisUfoeregrad) {
                paragraph {
                    textExpr(
                        Bokmal to "Vi har lagt til grunn at du framover skal ha en inntekt på <PE_UT_Inntektsgrense_faktisk_minus_60000> kroner per år. Du kan i tillegg ha en årlig inntekt på 60 000 kroner, uten at uføretrygden din blir redusert. Inntektsgrensen din blir derfor <PE_UT_Inntektsgrense_faktisk> kroner.".expr(),
                        Nynorsk to "".expr(),
                        English to "".expr()
                    )
                }
            }
        }
    }

    // TBU1208
    data class Kompensasjonsgrad(
        val inntektsgrense: Expression<Kroner>,
        val inntektsgrenseNesteAar: Expression<Kroner>,
        val inntektstak: Expression<Kroner>,
        val kompensasjonsgrad: Expression<Int>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val inntektsgrenseFaktisk = inntektsgrense.equalTo(0)

            showIf(inntektsgrense.lessThan(inntektstak)) {
                paragraph {
                    textExpr(
                        Bokmal to "Vi bruker en fastsatt prosentandel når vi justerer uføretrygden din ut fra inntekt. Denne prosentandelen kaller vi kompensasjonsgrad.".expr(),
                        Nynorsk to "".expr(),
                        English to "".expr()
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "For deg utgjør kompensasjonsgraden ".expr() + kompensasjonsgrad.format() + " prosent. Det er bare den delen av inntekten din som overstiger ".expr() +
                                ifElse(inntektsgrenseFaktisk, ifTrue = inntektsgrense.format(), ifFalse = inntektsgrenseNesteAar.format() ) + " kroner, som vi justerer uføretrygden din ut fra. Det betyr at et beløp som tilsvarer ".expr()
                                + kompensasjonsgrad.format() + " prosent av den inntekten du har over ".expr() +
                                ifElse(inntektsgrenseFaktisk, ifTrue = inntektsgrense.format(), ifFalse = inntektsgrenseNesteAar.format()) + " kroner trekkes fra uføretrygden din.".expr(),
                        Nynorsk to "".expr(),
                        English to "".expr()
                    )
                }
            }
        }
    }

    // TBU2361, TBU2362, TBU2363
    data class OekeUfoereUtbetalingForRestenAvKalenderAaret(
        val forventetInntekt: Expression<Kroner>,
        val utbetalingsgrad: Expression<Int>,
        val ufoeregrad: Expression<Int>,
        val inntektsgrense: Expression<Kroner>,
        val inntektstak: Expression<Kroner>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

            showIf(utbetalingsgrad.lessThan(ufoeregrad) and forventetInntekt.greaterThan(inntektsgrense) and inntektsgrense.lessThan(inntektstak)) {
                showIf(utbetalingsgrad.lessThan(ufoeregrad) and forventetInntekt.greaterThan(inntektsgrense) and inntektsgrense.lessThan(inntektstak))  {
                    paragraph {
                        Du har tidligere meldt fra om en inntekt på <PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt> kroner i år.
                    }
                }
            }
        }
    }
}