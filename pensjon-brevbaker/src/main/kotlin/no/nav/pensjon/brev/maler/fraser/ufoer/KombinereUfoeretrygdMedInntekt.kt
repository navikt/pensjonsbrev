package no.nav.pensjon.brev.maler.fraser.ufoer

import no.nav.pensjon.brev.api.model.Kroner
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

    // TBU1205, TBU1206  (TBU1296 er ikke tatt med - konvertering til UT utgikk 31. desember 2018) Dermed endres flette logikk for TBU1205.
    data class Inntektsgrense(
        val beloepsgrense: Expression<Kroner>,
        val grunnbeloep: Expression<Kroner>,
        val harFullUfoeregrad: Expression<Boolean>,
        val harInntektEtterUfoere: Expression<Boolean>,
        val inntektsgrense: Expression<Kroner>,
        val inntektsgrenseNesteAar: Expression<Kroner>,
        val ufoeregrad: Expression<Int>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val inntektsgrenseFaktisk = inntektsgrense.equalTo(0)
            // inntektsgrenseFaktisk = if inntektsgrenseNesteAar = 0 -> inntektsgrense, or -> inntektsgrenseNesteAar
            showIf(
                harFullUfoeregrad and not(harInntektEtterUfoere) and (beloepsgrense.notEqualTo(grunnbeloep))
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

    // TBU1207, TBU2357   (TBU1296 er ikke tatt med - konvertering til UT utgikk 31. desember 2018) Dermed endres flette logikk for TBU1205.
    data class InntektsgrenseLagtTilGrunn(
        val inntektsgrense: Expression<Kroner>,
        val inntektsgrenseNesteAar: Expression<Kroner>,
        val beloepsgrense: Expression<Kroner>,
        val oppjustertInntektEtterUfoere: Expression<Kroner>,
        val grunnbeloep: Expression<Kroner>,
        val ufoeregrad: Expression<Int>,
        val harInntektEtterUfoere: Expression<Boolean>,
        val harFullUfoeregrad: Expression<Boolean>,
        val harDelvisUfoeregrad: Expression<Boolean>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val inntektsgrenseFaktisk = inntektsgrense.equalTo(0)

            showIf(beloepsgrense.notEqualTo(grunnbeloep) and (harDelvisUfoeregrad or (harInntektEtterUfoere and harFullUfoeregrad))) {
                paragraph {
                    textExpr(
                        Bokmal to "Vi har lagt til grunn at du framover skal ha en inntekt på ".expr() + oppjustertInntektEtterUfoere.format() + " kroner per år. Du kan i tillegg ha en årlig inntekt på 40 prosent av folketrygdens grunnbeløp, uten at uføretrygden din blir redusert. Inntektsgrensen din blir derfor ".expr() +
                                ifElse(
                                    inntektsgrenseFaktisk,
                                    ifTrue = inntektsgrense.format(),
                                    ifFalse = inntektsgrenseNesteAar.format()
                                ) + " kroner.".expr(),
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
                                ifElse(
                                    inntektsgrenseFaktisk,
                                    ifTrue = inntektsgrense.format(),
                                    ifFalse = inntektsgrenseNesteAar.format()
                                ) + " kroner, som vi justerer uføretrygden din ut fra. Det betyr at et beløp som tilsvarer ".expr()
                                + kompensasjonsgrad.format() + " prosent av den inntekten du har over ".expr() +
                                ifElse(
                                    inntektsgrenseFaktisk,
                                    ifTrue = inntektsgrense.format(),
                                    ifFalse = inntektsgrenseNesteAar.format()
                                ) + " kroner trekkes fra uføretrygden din.".expr(),
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
        val harBeloepOekt: Expression<Boolean>,
        val harBeloepRedusert: Expression<Boolean>,
        val inntektsgrense: Expression<Kroner>,
        val inntektstak: Expression<Kroner>,
        val oppjustertInntektFoerUfoere80prosent: Expression<Kroner>,
        val ufoeregrad: Expression<Int>,
        val utbetalingsgrad: Expression<Int>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

            paragraph {
                showIf(
                    utbetalingsgrad.lessThan(ufoeregrad) and forventetInntekt.greaterThan(inntektsgrense) and inntektsgrense.lessThan(
                        oppjustertInntektFoerUfoere80prosent
                    )
                ) {
                    textExpr(
                        Bokmal to "Du har tidligere meldt fra om en inntekt på ".expr() + forventetInntekt.format() + " kroner for i år.".expr(),
                        Nynorsk to "".expr(),
                        English to "".expr()
                    )
                }.orShowIf(
                    utbetalingsgrad.lessThan(ufoeregrad) and harBeloepRedusert and inntektsgrense.lessThan(
                        oppjustertInntektFoerUfoere80prosent
                    )
                ) {
                    textExpr(
                        Bokmal to "Vi har derfor redusert utbetalingen av uføretrygden din for resten av kalenderåret.".expr(),
                        Nynorsk to "".expr(),
                        English to "".expr()
                    )
                }.orShowIf(
                    utbetalingsgrad.lessThan(ufoeregrad) and harBeloepOekt and inntektsgrense.lessThan(
                        oppjustertInntektFoerUfoere80prosent
                    )
                ) {
                    textExpr(
                        Bokmal to "Vi har derfor økt utbetalingen av uføretrygden din for resten av kalenderåret.".expr(),
                        Nynorsk to "".expr(),
                        English to "".expr()
                    )
                }
            }
        }
    }


    // TBU2261
    data class ReduksjonAvInntektUfoere(
        val inntektsgrense: Expression<Kroner>,
        val nettoAkkumulerteBeloepUtbetalt: Expression<Kroner>,
        val nettoAkkumulertePlussNettoRestAar: Expression<Kroner>,
        val nettoUfoeretrygdUtbetaltPerMaaned: Expression<Kroner>,
        val oppjustertInntektFoerUfoere80prosent: Expression<Kroner>,
        val ufoeregrad: Expression<Int>,
        val utbetalingsgrad: Expression<Int>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(utbetalingsgrad.lessThan(ufoeregrad) and inntektsgrense.lessThan(oppjustertInntektFoerUfoere80prosent)) {
                paragraph {
                    textExpr(
                        Bokmal to "Ut fra den årlige inntekten din vil uføretrygden utgjøre ".expr() +
                                nettoAkkumulertePlussNettoRestAar.format() + " kroner. Hittil i år har du fått utbetalt ".expr() +
                                nettoAkkumulerteBeloepUtbetalt.format() + " kroner. Du har derfor rett til en utbetaling av uføretrygd på ".expr() +
                                nettoUfoeretrygdUtbetaltPerMaaned.format() + " kroner per måned for resten av året.".expr(),
                        Nynorsk to "".expr(),
                        English to "".expr()
                    )
                }
            }
        }
    }

    // TBU1210
    data class BeholderUfoeregraden(
        val ufoeregrad: Expression<Int>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                textExpr(
                    Bokmal to "Blir uføretrygden din redusert på grunn av inntekt beholder du likevel uføregraden din på ".expr() + ufoeregrad.format() + " prosent. Du får utbetalt hele uføretrygden igjen dersom du tjener mindre enn inntektsgrensen din.".expr(),
                    Nynorsk to "".expr(),
                    English to "".expr()
                )
            }
        }
    }
}