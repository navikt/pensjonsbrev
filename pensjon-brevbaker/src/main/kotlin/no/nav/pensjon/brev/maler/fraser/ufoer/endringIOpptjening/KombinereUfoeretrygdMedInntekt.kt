package no.nav.pensjon.brev.maler.fraser.ufoer.endringIOpptjening

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

object KombinereUfoeretrygdMedInntekt {

    // TBU1201, TBBU1203
    data class KombinereUfoeretrygdOgInntektOverskrift(
        val harDelvisUfoeregrad: Expression<Boolean>,
        val harFullUfoeregrad: Expression<Boolean>,
        val harFullUtbetalingsgrad: Expression<Boolean>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                showIf(harFullUfoeregrad and harFullUtbetalingsgrad) {
                    text(
                        Bokmal to "Skal du kombinere uføretrygd og inntekt?",
                        Nynorsk to "Skal du kombinere uføretrygd og inntekt?",
                        English to "Will you combine disability benefit with income?"
                    )
                }.orShowIf(harDelvisUfoeregrad) {
                    text(
                        Bokmal to "For deg som kombinerer uføretrygd og inntekt",
                        Nynorsk to "For deg som kombinerer uføretrygd og inntekt",
                        English to "For you who combines disabilty benefit with income"
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
                    text(
                        Bokmal to "Du har mulighet til å ha inntekt ved siden av uføretrygden din. Det lønner seg å jobbe, fordi inntekt og uføretrygd alltid vil være høyere enn uføretrygd alene.",
                        Nynorsk to "Det er mogleg for deg å ha inntekt ved sida av uføretrygda di. Det lønner seg å jobbe fordi inntekt og uføretrygd alltid vil vere høgare enn uføretrygd åleine.",
                        English to "You have the possibilty to earn an income in addition to your disability benefit. It pays to work, your income and disabiltiy benefit combined will always be higher than disability benefit alone."
                    )
                }
            }.orShowIf(utbetalingsgrad.lessThan(ufoeregrad)) {
                paragraph {
                    text(
                        Bokmal to "Utbetalingen av uføretrygden din er redusert fordi du har inntekt utover inntektsgrensen. Det lønner seg likevel å jobbe, fordi inntekt og uføretrygd alltid vil være høyere enn uføretrygd alene.",
                        Nynorsk to "Utbetalinga av uføretrygda di er redusert fordi du har inntekt utover inntektsgrensa. Det løne seg likevel å jobbe, fordi inntekt og uføretrygd alltid vil vere høgare enn uføretryd aleine.",
                        English to "Your disability benefit payment is reduced because your income exceeds the income limit for disability benefit. It still pays to work, your income and disabiltiy benefit combined will always be higher than disability benefit alone."
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
            val inntektsgrenseFaktisk = inntektsgrenseNesteAar.greaterThan(0)
            // inntektsgrenseFaktisk = if inntektsgrenseNesteAar > 0 -> inntektsgrenseNesteAar, else -> inntektsgrense
            showIf(
                harFullUfoeregrad and not(harInntektEtterUfoere) and (beloepsgrense.notEqualTo(grunnbeloep))
            ) {
                paragraph {
                    textExpr(
                        Bokmal to "Du kan ha en årlig inntekt på 40 prosent av folketrygdens grunnbeløp, uten at uføretrygden din blir redusert. I dag er dette ".expr() +
                                ifElse(
                                    inntektsgrenseFaktisk,
                                    ifTrue = inntektsgrenseNesteAar.format(),
                                    ifFalse = inntektsgrense.format()
                                ) + " kroner. Dette er inntektsgrensen din.".expr(),
                        Nynorsk to "Du kan ha ei årleg inntekt på 40 prosent av grunnbeløpet i folketrygda utan at uføretrygda di blir redusert. I dag er dette ".expr() +
                                ifElse(
                                    inntektsgrenseFaktisk,
                                    ifTrue = inntektsgrenseNesteAar.format(),
                                    ifFalse = inntektsgrense.format()
                                ) + " kroner. Dette er inntektsgrensa di.".expr(),
                        English to "You can have a yearly income that is 40 percent of National Insurance Scheme basic amount, without your disability benefit being reduced. This amount is currently NOK ".expr() +
                                ifElse(
                                    inntektsgrenseFaktisk,
                                    ifTrue = inntektsgrenseNesteAar.format(),
                                    ifFalse = inntektsgrense.format()
                                ) + ". This is your income limit before disability benefit is reduced.".expr()
                    )
                }
            }.orShowIf(grunnbeloep.equalTo(beloepsgrense)) {
                paragraph {
                    textExpr(
                        Bokmal to "Du kan ha en årlig inntekt på folketrygdens grunnbeløp fordi du er i varig tilrettelagt arbeid, uten at uføretrygden din blir redusert. I dag er dette ".expr() +
                                inntektsgrense.format() + " kroner. Dette er inntektsgrensen din.".expr(),
                        Nynorsk to "Du kan ha ei årleg inntekt på grunnbeløpet i folketrygda mens du er i varig tilrettelagt arbeid, utan at uføretrygda di blir redusert. I dag er dette ".expr() +
                                inntektsgrense.format() + " kroner. Dette er inntektsgrensa di.".expr(),
                        English to "You can have a yearly income that is the same as the National Insurance Scheme basic amount because you are in permanent adapted work, without your disability benefit being reduced. This amount is currently NOK ".expr() +
                                inntektsgrense.format() + ". This is your income limit before disability benefit is reduced."

                    )
                }
            }
        }
    }

    // TBU1207   (TBU1296, TBU2357 er ikke tatt med - konvertering til UT utgikk 31. desember 2018) Dermed endres flette logikk for TBU1205.
    data class InntektsgrenseLagtTilGrunn(
        val beloepsgrense: Expression<Kroner>,
        val grunnbeloep: Expression<Kroner>,
        val harDelvisUfoeregrad: Expression<Boolean>,
        val harFullUfoeregrad: Expression<Boolean>,
        val harInntektEtterUfoere: Expression<Boolean>,
        val inntektsgrense: Expression<Kroner>,
        val inntektsgrenseNesteAar: Expression<Kroner>,
        val oppjustertInntektEtterUfoere: Expression<Kroner>,
        val ufoeregrad: Expression<Int>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val inntektsgrenseFaktisk = inntektsgrenseNesteAar.greaterThan(0)

            showIf(beloepsgrense.notEqualTo(grunnbeloep) and (harDelvisUfoeregrad or (harInntektEtterUfoere and harFullUfoeregrad))) {
                paragraph {
                    textExpr(
                        Bokmal to "Vi har lagt til grunn at du framover skal ha en inntekt på ".expr() + oppjustertInntektEtterUfoere.format() + " kroner per år. Du kan i tillegg ha en årlig inntekt på 40 prosent av folketrygdens grunnbeløp, uten at uføretrygden din blir redusert. Inntektsgrensen din blir derfor ".expr() +
                                ifElse(
                                    inntektsgrenseFaktisk,
                                    ifTrue = inntektsgrenseNesteAar.format(),
                                    ifFalse = inntektsgrense.format()
                                ) + " kroner.".expr(),
                        Nynorsk to "Vi har lagt til grunn at du framover skal ha ei inntekt på ".expr() + oppjustertInntektEtterUfoere.format() + " kroner per år. Du kan i tillegg ha ei årleg inntekt på 40 prosent av grunnbeløpet i folketrygda utan at uføretrygda di blir redusert. Inntektsgrensa di blir difor ".expr() +
                                ifElse(
                                    inntektsgrenseFaktisk,
                                    ifTrue = inntektsgrenseNesteAar.format(),
                                    ifFalse = inntektsgrense.format()
                                ) + " kroner.".expr(),
                        English to "We have assumed that you will have an income of NOK ".expr() + oppjustertInntektEtterUfoere.format() + " per year. You can in addition have an annual income of 40 percent of the National Insurance Scheme basic amount, without your disability benefit being reduced. This amount is currently NOK ".expr() +
                                ifElse(
                                    inntektsgrenseFaktisk,
                                    ifTrue = inntektsgrenseNesteAar.format(),
                                    ifFalse = inntektsgrense.format()
                                ) + ".".expr()
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
            val inntektsgrenseFaktisk = inntektsgrenseNesteAar.greaterThan(0)

            showIf(inntektsgrense.lessThan(inntektstak)) {
                paragraph {
                    text(
                        Bokmal to "Vi bruker en fastsatt prosentandel når vi justerer uføretrygden din ut fra inntekt. Denne prosentandelen kaller vi kompensasjonsgrad.",
                        Nynorsk to "Vi bruker ein fastsett prosentdel når vi justerer uføretrygda di ut frå inntekt. Denne prosentdelen kallar vi kompensasjonsgrad.",
                        English to "We use a stipulated percentage rate when we adjust your disability benefit in relation to income. This stipulated percentage rate is called the adjustment rate."
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "For deg utgjør kompensasjonsgraden ".expr() + kompensasjonsgrad.format() + " prosent. Det er bare den delen av inntekten din som overstiger ".expr() +
                                ifElse(
                                    inntektsgrenseFaktisk,
                                    ifTrue = inntektsgrenseNesteAar.format(),
                                    ifFalse = inntektsgrense.format()
                                ) + " kroner, som vi justerer uføretrygden din ut fra. Det betyr at et beløp som tilsvarer ".expr() +
                                kompensasjonsgrad.format() + " prosent av den inntekten du har over ".expr() +
                                ifElse(
                                    inntektsgrenseFaktisk,
                                    ifTrue = inntektsgrenseNesteAar.format(),
                                    ifFalse = inntektsgrense.format()
                                ) + " kroner trekkes fra uføretrygden din.".expr(),
                        Nynorsk to "For deg utgjer kompensasjongraden ".expr() + kompensasjonsgrad.format() + " prosent. Det er berre den delen av inntekta di som overstig ".expr() +
                                ifElse(
                                    inntektsgrenseFaktisk,
                                    ifTrue = inntektsgrenseNesteAar.format(),
                                    ifFalse = inntektsgrense.format()
                                ) + " kroner, som vi justerer uføretrygda di ut frå. Det betyr at eit beløp som svarer til ".expr() +
                                kompensasjonsgrad.format() + " prosent av inntekta du har over ".expr() +
                                ifElse(
                                    inntektsgrenseFaktisk,
                                    ifTrue = inntektsgrenseNesteAar.format(),
                                    ifFalse = inntektsgrense.format()
                                ) + " kroner blir trekt frå uføretrygda di.".expr(),
                        English to "For you, the adjustment rate is ".expr() + kompensasjonsgrad.format() + " procent. It is only the part of your income that exceeds NOK ".expr() +
                                ifElse(
                                    inntektsgrenseFaktisk,
                                    ifTrue = inntektsgrenseNesteAar.format(),
                                    ifFalse = inntektsgrense.format()
                                ) + " that is subtracted from your disability benefit.".expr()
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
                        Nynorsk to "Du har tidlegare meldt frå om ein inntekt på ".expr() + forventetInntekt.format() + " kroner for i år.".expr(),
                        English to "You have previously reported an income of NOK ".expr() + forventetInntekt.format() + " for this year.".expr()
                    )
                }.orShowIf(
                    utbetalingsgrad.lessThan(ufoeregrad) and harBeloepRedusert and inntektsgrense.lessThan(
                        oppjustertInntektFoerUfoere80prosent
                    )
                ) {
                    text(
                        Bokmal to "Vi har derfor redusert utbetalingen av uføretrygden din for resten av kalenderåret.",
                        Nynorsk to "Vi har difor redusert utbetalinga av uføretrygda di for resten av kalenderåret.",
                        English to "We have therefore redused the payment of your disability benefit for the rest of the calendar year."
                    )
                }.orShowIf(
                    utbetalingsgrad.lessThan(ufoeregrad) and harBeloepOekt and inntektsgrense.lessThan(
                        oppjustertInntektFoerUfoere80prosent
                    )
                ) {
                    text(
                        Bokmal to "Vi har derfor økt utbetalingen av uføretrygden din for resten av kalenderåret.",
                        Nynorsk to "Vi har difor auka utbetalinga av uføretrygda di for resten av kalendaråret.",
                        English to "We have therefore increased your disability payment for the rest of the calendar year."
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
                        Nynorsk to "Ut frå den årlege inntekta di vil uføretrygda utgjera ".expr() +
                                nettoAkkumulertePlussNettoRestAar.format() + " kroner. Hittil i år har du fått utbetalt ".expr() +
                                nettoAkkumulerteBeloepUtbetalt.format() + " kroner. Du har derfor rett til ein utbetaling av uføretrygd på ".expr() +
                                nettoUfoeretrygdUtbetaltPerMaaned.format() + " kroner per månad for resten av året".expr(),
                        English to "Given your annual income, your disabililty benefit will be NOK ".expr() +
                                nettoAkkumulertePlussNettoRestAar.format() + ". To date this year, you have been paid NOK ".expr() +
                                nettoAkkumulerteBeloepUtbetalt.format() + ". You are therefore entitled to a disability payment of NOK ".expr() +
                                nettoUfoeretrygdUtbetaltPerMaaned.format() + " per calendar month for the rest of the year.".expr()
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
                    Bokmal to "Blir uføretrygden din redusert på grunn av inntekt beholder du likevel uføregraden din på ".expr() +
                            ufoeregrad.format() + " prosent. Du får utbetalt hele uføretrygden igjen dersom du tjener mindre enn inntektsgrensen din.".expr(),
                    Nynorsk to "Blir uføretrygda di blir redusert på grunn av inntekt, beheld du likevel uføregraden på ".expr() +
                            ufoeregrad.format() + " prosent. Du får utbetalt heile uføretrygda att dersom du tener mindre enn inntektsgrensa di.".expr(),
                    English to "If your disability benefit is reduced because of income, you nonetheless keep your degree of disability that is ".expr() +
                            ufoeregrad.format() + " procent. You will get paid the entire disability benefit again if you earn less than your income limit.".expr()
                )
            }
        }
    }

    // TBU2366, TBU2367, TBU2279, TBU3740, TBU2280
    data class MeldeFraOmEndringerIInntekten(
        val forventetInntekt: Expression<Kroner>,
        val inntektsgrense: Expression<Kroner>,
        val inntektstak: Expression<Kroner>,
        val oppjustertInntektFoerUfoere80prosent: Expression<Kroner>,
        val ufoeregrad: Expression<Int>,
        val utbetalingsgrad: Expression<Int>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(utbetalingsgrad.lessThan(ufoeregrad)) {
                title1 {
                    text(
                        Bokmal to "Du må melde fra om endringer i inntekten",
                        Nynorsk to "Du må melde frå om endringar i inntekta",
                        English to "You must notify us of changes in income"
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du kan melde fra om inntektsendringer under menyvalget «uføretrygd» når du logger deg inn på ${Constants.NAV_URL}. Her kan du legge inn endringer i den forventede årlige inntekten, og se hva dette betyr for utbetalingen av uføretrygden din. For at du skal få en jevn utbetaling av uføretrygden, er det viktig at du melder fra om inntektsendringer så tidlig som mulig.",
                        Nynorsk to "Du kan melde frå om inntektsendringar under menyvalet «uføretrygd» når du logger deg inn på ${Constants.NAV_URL}. Her kan du leggje inn kor mykje du forventar å tene i løpet av året, og sjå hva dette betyr for utbetalinga av uføretrygda di. For at du skal få ein jamn utbetaling av uføretrygda, er det viktig at du melde frå om inntektsendringar så tidleg som mogleg",
                        English to "You can register changes in income under the option «uføretrygd» at ${Constants.NAV_URL}. You can register how much you expect to earn in the calendar year. You will then be able to see how much disabilty benefit you will receive. In order for you to receive even payments of disability benefit, it is important that you register income changes as soon as possible."
                    )
                }
                showIf(inntektsgrense.lessThan(oppjustertInntektFoerUfoere80prosent)) {
                    paragraph {
                        textExpr(
                            Bokmal to "Vi gjør oppmerksom på at det ikke utbetales uføretrygd når inntekten din utgjør mer enn 80 prosent av oppjustert inntekt før du ble ufør, det vil si ".expr() +
                                    oppjustertInntektFoerUfoere80prosent.format() + " kroner per år.".expr(),
                            Nynorsk to "Ver merksom på at det ikkje utbetales uføretrygd når inntekta di utgjer meir enn 80 prosent av oppjustert inntekt før du ble ufør, det vil si ".expr() +
                                    oppjustertInntektFoerUfoere80prosent.format() + " kroner per år.".expr(),
                            English to "We wish to point out that disability benefit payments stop when you earn more than 80 percent of the upwards adjusted income you had before you had a disability, which is NOK ".expr() +
                            oppjustertInntektFoerUfoere80prosent.format() + " per year.".expr()
                        )
                    }
                }
            }
            showIf(forventetInntekt.lessThan(inntektstak) and inntektstak.lessThanOrEqual(inntektsgrense)) {
                paragraph {
                    textExpr(
                        Bokmal to "Vi gjør oppmerksom på at det ikke utbetales uføretrygd når inntekten din utgjør mer enn inntektsgrensen din, det vil si ".expr() +
                                inntektsgrense.format() + " kroner per år.".expr(),
                        Nynorsk to "Ver merksom på at det ikkje utbetales uføretrygd når inntekta di utgjer meir enn inntektsgrensa di, det vil si ".expr() +
                                inntektsgrense.format() + " kroner per år.".expr(),
                        English to "We wish to point out that disability benefit payments stop when you earn more than your income limit, which is NOK ".expr() +
                                inntektsgrense.format() + " per year.".expr()
                    )
                }
            }
            showIf(utbetalingsgrad.lessThan(ufoeregrad)) {
                paragraph {
                    text(
                        Bokmal to "Vi vil foreta et etteroppgjør dersom du har fått utbetalt for mye eller for lite uføretrygd. Dette gjør vi når likningen fra Skatteetaten er klar. Du kan lese mer om dette i vedlegget «Opplysninger om beregningen».",
                        Nynorsk to "Vi vil foreta eit etteroppgjør dersom du har fått utbetalt for mykje eller for lite utbetalt uføretrygd. Dette gjer vi når likninga fra Skatteetaten er klar. Du kan lesa meir om dette i vedlegget «Opplysningar om utrekninga».",
                        English to "We will calculate a final settlement if you have received too much or too little disability benefit in a calendar year. We do this when your tax assessment is completed. You can read more about this in the appendix «Information about calculations»."
                    )
                }
            }
        }
    }
}