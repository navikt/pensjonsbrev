package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.BeregningsMetode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.innhold
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.opphoerNesteAar
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.oppphoersdato
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.sisteBeregningsperiode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.sisteBeregningsperiodeNesteAar
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.trygdetid
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.fratrekkInnAar
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.grunnbeloep
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.inntekt
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.innvilgaMaaneder
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.oppgittInntekt
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.restanse
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.sanksjon
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.Trygdetid
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.beregnetTrygdetidAar
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.beregningsMetodeAnvendt
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.beregningsMetodeFraGrunnlag
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.mindreEnnFireFemtedelerAvOpptjeningstiden
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.prorataBroek
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.trygdetidsperioder
import no.nav.pensjon.etterlatte.maler.formatAar
import no.nav.pensjon.etterlatte.maler.formatBroek
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.Trygdetidstabell

fun beregningAvOmstillingsstoenad(
    tidligereFamiliepleier: Boolean,
    inntektsjustering: Boolean = false
): AttachmentTemplate<LangBokmalNynorskEnglish, OmstillingsstoenadBeregning> =
    createAttachment(
        title =
        newText(
            Bokmal to "Beregning av omstillingsstønad",
            Nynorsk to "Utrekning av omstillingsstønad",
            English to "Calculation of adjustment allowance",
        ),
        includeSakspart = false,
    ) {
        beregning(tidligereFamiliepleier.expr(), inntektsjustering.expr())
        trygdetid(trygdetid, tidligereFamiliepleier.expr())
        perioderMedRegistrertTrygdetid(trygdetid, tidligereFamiliepleier.expr())
        meldFraTilNav()
    }

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, OmstillingsstoenadBeregning>.beregning(
    tidligereFamiliepleier: Expression<Boolean>,
    inntektsjustering: Expression<Boolean>,
) {
    val sisteInntekt = sisteBeregningsperiode.inntekt
    val sisteGrunnbeloep = sisteBeregningsperiode.grunnbeloep
    val sisteOppgittInntekt = sisteBeregningsperiode.oppgittInntekt
    val sisteFratrekkInnAar = sisteBeregningsperiode.fratrekkInnAar
    val sisteInnvilgaMaaneder = sisteBeregningsperiode.innvilgaMaaneder

    paragraph {
        textExpr(
            Bokmal to "Full omstillingsstønad beregnes utfra 2,25 ganger folketrygdens ".expr() +
                "grunnbeløp (G). Dagens verdi av grunnbeløpet er " + sisteGrunnbeloep.format() + " kroner. " +
                "Grunnbeløpet blir regulert 1. mai hvert år. Økningen etterbetales vanligvis i juni hvert år.",
            Nynorsk to "Full omstillingsstønad blir rekna ut etter 2,25 gongar grunnbeløpet i ".expr() +
                "folketrygda (G). Dagens verdi av grunnbeløpet er " + sisteGrunnbeloep.format() + " kroner. " +
                "Grunnbeløpet blir regulert 1. mai kvart år. Auken blir vanlegvis etterbetalt i " +
                "juni kvart år.",
            English to "Full adjustment allowance are calculated based on 2.25 × the national ".expr() +
                "insurance basic amount (G). The current value of the basic amount is NOK " + sisteGrunnbeloep.format() +
                ". The basic amount is adjusted on 1 May each year. You will receive payment of any increase in " +
                "June of each year.",
        )
    }
    paragraph {
        textExpr(
            Bokmal to "For at du skal få full stønad må ".expr() +
                ifElse(tidligereFamiliepleier, "trygdetiden din", "avdødes trygdetid") +
                " være minst 40 år. Er trygdetiden mindre enn 40 år vil stønaden avkortes.",
            Nynorsk to "For at du skal få full stønad må ".expr() +
                ifElse(tidligereFamiliepleier, "du", "avdøde") +
                " ha hatt ei trygdetid på minst 40 år. Dersom trygdetida er mindre enn 40 år, blir stønaden avkorta.",
            English to "To receive the maximum allowance, ".expr() +
                ifElse(
                    tidligereFamiliepleier,
                    "your period of national insurance coverage",
                    "the deceased's contribution time to the national insurance scheme",
                ) +
                " must be at least 40 years. If the contribution time is less than 40 years, the allowance is reduced.",
        )
    }
    paragraph {
        text(
            Bokmal to "Inntekten din avgjør hvor mye du kan få. Stønaden reduseres med 45 prosent av " +
                "arbeidsinntekt eller tilsvarende inntekt som er over halvparten av grunnbeløpet.",
            Nynorsk to "Inntekta di avgjer kor mykje du kan få. Stønaden blir redusert med 45 prosent av " +
                "arbeidsinntekt eller tilsvarande inntekt som er over halvparten av grunnbeløpet.",
            English to "Your income determines how much money you are entitled to. The allowance is reduced by " +
                "45 percent of the employment income or similar income when this is over half the basic amount.",
        )
    }

    title2 {
        text(
            Bokmal to "Beregnet omstillingsstønad",
            Nynorsk to "Utrekna omstillingsstønad",
            English to "Calculated adjustment allowance",
        )
    }

    includePhrase(Beregningsperiodetabell(beregningsperioder))

    paragraph {
        text(
            Bokmal to "Beløpene i tabellen er før skatt.",
            Nynorsk to "Beløpa i tabellen er før skatt.",
            English to "The amounts in the table are stated before tax.",
        )
    }

    title2 {
        text(
            Bokmal to "Stønad før reduksjon for inntekt",
            Nynorsk to "Stønad før reduksjon for inntekt",
            English to "Allowance paid before income reduction",
        )
    }

    showIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)) {
        paragraph {
            textExpr(
                Bokmal to "".expr() + "Stønad per år før reduksjon av inntekt er beregnet til 2,25 ganger " +
                    "grunnbeløpet i folketrygden (G) ganget med " + trygdetid.beregnetTrygdetidAar.format() + "/40 " +
                    "trygdetid. Beløpet fordeles på 12 utbetalinger i året. ",
                Nynorsk to "".expr() + "Stønad per år før reduksjon av inntekt er rekna ut til 2,25 gongar " +
                    "grunnbeløpet i folketrygda (G) gonga med " + trygdetid.beregnetTrygdetidAar.format() + "/40 " +
                    "trygdetid. Beløpet blir fordelt på 12 utbetalingar i året. ",
                English to "".expr() + "Allowance per year before reduction of income are calculated " +
                    "based on 2.25 × national insurance basic amount (G) × " +
                    trygdetid.beregnetTrygdetidAar.format() + "/40 years of contribution time. This amount " +
                    "is distributed in 12 payments a year. ",
            )
        }
    }.orShowIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
        paragraph {
            textExpr(
                Bokmal to "".expr() + "Stønad per år før reduksjon for inntekt er beregnet til 2,25 ganger " +
                    "grunnbeløpet i folketrygden ganget med " + trygdetid.beregnetTrygdetidAar.format() + "/40 " +
                    "trygdetid, ganget med forholdstallet " + trygdetid.prorataBroek.formatBroek() +
                    ". Beløpet fordeles på 12 utbetalinger i året.",
                Nynorsk to "".expr() + "Stønad per år før reduksjon for inntekt er rekna ut til 2,25 gongar " +
                    "grunnbeløpet i folketrygda gonga med " + trygdetid.beregnetTrygdetidAar.format() + "/40 " +
                    "trygdetid, gonga med forholdstalet " + trygdetid.prorataBroek.formatBroek() +
                    ". Beløpet blir fordelt på 12 utbetalingar i året.",
                English to "".expr() + "Allowance per year before reduction of income are calculated based on " +
                    "2.25 × national insurance basic amount (G) × " + trygdetid.beregnetTrygdetidAar.format() +
                    "/40 years of contribution time, multiplied by the proportional fraction " +
                    trygdetid.prorataBroek.formatBroek() + ". This amount is distributed in 12 payments a year.",
            )
        }
    }

    title2 {
        text(
            Bokmal to "Inntekten din",
            Nynorsk to "Inntekta di",
            English to "Your income",
        )
    }

    showIf(sisteInntekt.greaterThan(0)) {
        paragraph {
            text(
                Bokmal to
                    "Stønaden reduseres etter inntekten du har hvert kalenderår. Det er bare inntekt for måneder med innvilget stønad som skal medregnes. Inntekten blir fordelt på antall innvilgede måneder. ",
                Nynorsk to
                    "Stønaden blir redusert etter inntekta du har kvart kalenderår. Det er berre inntekt for månader med innvilga stønad som skal medreknast. Inntekta blir fordelt på talet på innvilga månader. ",
                English to
                    "The allowance is reduced according to your income each calendar year. Only income for months with granted allowance must be included. The income is distributed over the number of months granted. ",
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Din oppgitte inntekt for ".expr() + virkningsdato.formatAar() + " er " +
                    sisteOppgittInntekt.format() + " kroner.",

                Nynorsk to "Du har ei oppgitt inntekt for ".expr() + virkningsdato.formatAar() + " på " +
                    sisteOppgittInntekt.format() + " kroner.",

                English to "Your estimated income for ".expr() + virkningsdato.formatAar() + " is NOK " +
                    sisteOppgittInntekt.format() + ".",
            )
            showIf(opphoerNesteAar.not()) {
                ifNotNull(oppphoersdato) { opphoer ->
                    textExpr(
                        Bokmal to " Dette er forventet inntekt frem til ".expr() + opphoer.format() +
                            ", som er forventet opphørsdato for mottak av stønaden.",

                        Nynorsk to " Dette er forventa inntekt fram til ".expr() + opphoer.format() +
                            ", som er forventa opphøyrsdato for mottak av stønaden.",

                        English to " This is the expected income until ".expr() + opphoer.format() +
                            ", which is the expected end date for receiving the allowance."
                    )
                }
            }
            showIf(inntektsjustering.not() and sisteInnvilgaMaaneder.lessThan(12)) {
                textExpr(
                    Bokmal to " Fratrekk for inntekt i måneder før du er innvilget stønad er ".expr() +
                        sisteFratrekkInnAar.format() + " kroner. Vi har lagt til grunn at du har en inntekt på " +
                        sisteInntekt.format() + " kroner i innvilgede måneder i år.",

                    Nynorsk to " Fråtrekk for inntekt i månader før du er innvilga stønad er ".expr() +
                        sisteFratrekkInnAar.format() + " kroner. Vi har lagt til grunn at du har ei inntekt på " +
                        sisteInntekt.format() + " kroner i innvilga månader i år.",

                    English to " Deduction for income in months before you are granted allowance is NOK ".expr() +
                        sisteFratrekkInnAar.format() + ". We have assumed that you have an income of NOK " +
                        sisteInntekt.format() + " in granted months this year.",
                )
            }
            text(
                Bokmal to " Beløpet er avrundet ned til nærmeste tusen.",
                Nynorsk to " Beløpet er avrunda ned til næraste tusen.",
                English to " The amount is rounded down to the nearest thousand.",
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Stønad per måned er redusert på følgende måte: (".expr() + sisteInntekt.format() +
                    " kroner / " + sisteInnvilgaMaaneder.format() + " måneder) minus (0,5 G / 12 måneder). " +
                    "Beløpet ganges med 45 prosent.",
                Nynorsk to "Stønaden per månad har blitt redusert på følgjande måte: (".expr() + sisteInntekt.format() +
                    " kroner / " + sisteInnvilgaMaaneder.format() + " månader) minus (0,5 G / 12 månader). " +
                    "Beløpet blir gonga med 45 prosent.",
                English to "The monthly allowance amount has been reduced as follows: (NOK ".expr() +
                    sisteInntekt.format() + " / " + sisteInnvilgaMaaneder.format() + " months) minus (0.5 G / 12 months). " +
                    "The amount is multiplied by 45 percent.",
            )
        }

        ifNotNull(sisteBeregningsperiodeNesteAar) {
            val sisteInntektNesteAar = it.inntekt
            val innvilgaMaanederNesteAar = it.innvilgaMaaneder
            val sisteInntektNesteAarFom = it.datoFOM

            paragraph {
                textExpr(
                    Bokmal to
                        "Fra ".expr() + sisteInntektNesteAarFom.format() + " har vi lagt til grunn din oppgitte forventede inntekt på " +
                        sisteInntektNesteAar.format() + " kroner.",
                    Nynorsk to
                        "Frå ".expr() + sisteInntektNesteAarFom.format() + " har vi lagt til grunn den oppgitte forventa inntekta di på " +
                        sisteInntektNesteAar.format() + " kroner.",
                    English to
                        "From ".expr() + sisteInntektNesteAarFom.format() + ", we have based your stated expected income on NOK " +
                        sisteInntektNesteAar.format() + ".",
                )
                showIf(opphoerNesteAar) {
                    ifNotNull(oppphoersdato) { dato ->
                        textExpr(
                            Bokmal to " Dette er forventet inntekt frem til ".expr() + dato.format() +
                                ", som er forventet opphørsdato for mottak av stønaden.".expr(),

                            Nynorsk to " Dette er forventa inntekt fram til ".expr() + dato.format() +
                                ", som er forventa opphøyrsdato for mottak av stønaden.".expr(),

                            English to " This is the expected income from the grant until ".expr() + dato.format() +
                                ", which is the expected end date for receiving the allowance.".expr(),
                        )
                    }
                }
                text(
                    Bokmal to " Beløpet er avrundet ned til nærmeste tusen.",
                    Nynorsk to " Beløpet er avrunda ned til næraste tusen.",
                    English to " The amount is rounded down to the nearest thousand."
                )
            }

            paragraph {
                textExpr(
                    Bokmal to "Stønad per måned er redusert på følgende måte: (".expr() + sisteInntektNesteAar.format() +
                        " kroner / " + innvilgaMaanederNesteAar.format() + " måneder) minus (0,5 G / 12 måneder). " +
                        "Beløpet ganges med 45 prosent.",
                    Nynorsk to "Stønaden per månad har blitt redusert på følgjande måte: (".expr() + sisteInntektNesteAar.format() +
                        " kroner / " + innvilgaMaanederNesteAar.format() + " månader) minus (0,5 G / 12 månader). " +
                        "Beløpet blir gonga med 45 prosent.",
                    English to "The monthly allowance amount has been reduced as follows: (NOK ".expr() +
                        sisteInntektNesteAar.format() + " / " + innvilgaMaanederNesteAar.format() +
                        " months) minus (0.5 G / 12 months). " +
                        "The amount is multiplied by 45 percent.",
                )
            }
        }

        showIf(
            sisteBeregningsperiode.restanse.notEqualTo(0) and
                sisteBeregningsperiode.utbetaltBeloep.notEqualTo(0),
        ) {
            val erRestanseTrekk = sisteBeregningsperiode.restanse.greaterThan(0)
            val restanse = sisteBeregningsperiode.restanse.absoluteValue()
            title2 {
                textExpr(
                    Bokmal to ifElse(erRestanseTrekk, "Trekk", "Tillegg") + " i utbetalingen",
                    Nynorsk to ifElse(erRestanseTrekk, "Trekk", "Tillegg") + " i utbetalinga",
                    English to ifElse(erRestanseTrekk, "Deduction from", "Addition to") + " payment",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Den forventede inntekten din for inneværende år er blitt justert. ".expr() +
                        "Det blir " + ifElse(erRestanseTrekk, "gjort et trekk", "gitt et tillegg") +
                        " i utbetalingen for resten av året. Dette blir gjort for å unngå eller redusere et etteroppgjør. " +
                        ifElse(erRestanseTrekk, "Trekket", "Tillegget") + " er på " + restanse.format() +
                        " kroner per måned. Dette er medregnet i “Utbetaling per måned”, som fremgår i tabellen over.",
                    Nynorsk to "Den forventa inntekta di for inneverande år har blitt justert. ".expr() +
                        "Det blir " + ifElse(erRestanseTrekk, "gjort eit trekk", "gitt eit tillegg") +
                        " i utbetalinga for resten av året. Dette blir gjort for å unngå eller redusere eit etteroppgjer. " +
                        ifElse(erRestanseTrekk, "Trekket", "Tillegget") + " er på " + restanse.format() +
                        " kroner per månad. Dette er medrekna i “Utbetaling per månad”, som kjem fram i tabellen over.",
                    English to "Your estimated income for the current year has been adjusted. ".expr() +
                        "Your payment amount has therefore been " + ifElse(erRestanseTrekk, "reduced", "increased") +
                        " for the remainder of the year to avoid or reduce a final settlement. " +
                        ifElse(erRestanseTrekk, "The deduction", "Addition") + " is NOK " + restanse.format() +
                        " per month. This is included in \"Payout per month\", which appears in the table above.",
                )
            }
        }
    }.orShow {
        paragraph {
            text(
                Bokmal to "Vi har lagt til grunn at du ikke har arbeidsinntekt eller tilsvarende inntekt som " +
                    "omstillingsstønaden skal reduseres etter.",
                Nynorsk to "Vi har lagt til grunn at du ikkje har arbeidsinntekt eller tilsvarande inntekt som " +
                    "omstillingsstønaden skal reduserast etter. ",
                English to "Our calculation shows that you had no employment income or similar income from " +
                    "which the adjustment allowance can be reduced.",
            )
        }
    }

    showIf(sisteBeregningsperiode.sanksjon) {
        title2 {
            text(
                Bokmal to "Sanksjon - stans i utbetalingen av omstillingsstønaden",
                Nynorsk to "Sanksjon – stans i utbetaling av omstillingsstønad",
                English to "Sanction – stoppage of payment of adjustment allowance",
            )
        }

        paragraph {
            text(
                Bokmal to "Omstillingsstønaden din er stanset fordi du ikke har oppfylt kravet om aktivitetsplikt. " +
                    "“Utbetaling per måned” er derfor satt til 0 fra gjeldende tidspunkt, slik det fremgår i tabellen over.",
                Nynorsk to "Då du ikkje har oppfylt kravet om aktivitetsplikt, har omstillingsstønaden din blitt stansa. " +
                    "«Utbetaling per månad» er difor sett til 0 frå gjeldande tidspunkt, slik det går fram av tabellen over.",
                English to "Your adjustment allowance has been stopped because you have not met the mandatory activity requirements. " +
                    "«Payment per month is therefore set at «0» from the relevant date, as evident in the table above.",
            )
        }
    }

    konverterElementerTilBrevbakerformat(innhold)
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, OmstillingsstoenadBeregning>.trygdetid(
    trygdetid: Expression<Trygdetid>,
    tidligereFamiliepleier: Expression<Boolean>,
) {
    title2 {
        text(
            Bokmal to "Trygdetid",
            Nynorsk to "Trygdetid",
            English to "Period of national insurance coverage",
        )
    }

    showIf(trygdetid.beregningsMetodeFraGrunnlag.equalTo(BeregningsMetode.NASJONAL)) {
        paragraph {
            textExpr(
                Bokmal to "Trygdetiden tilsvarer det antall år ".expr() +
                    ifElse(tidligereFamiliepleier, "du", "avdøde ") +
                    " har vært medlem i folketrygden etter fylte 16 år. ",
                Nynorsk to "Trygdetida svarer til talet på år ".expr() +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " var medlem i folketrygda etter fylte 16 år. ",
                English to "Contribution time is the number of years ".expr() +
                    ifElse(tidligereFamiliepleier, "you", "the deceased") +
                    " has been a member of the Norwegian National Insurance Scheme after reaching the age of 16. ",
            )

            showIf(tidligereFamiliepleier) {
                text(
                    Bokmal to "Det blir vanligvis beregnet framtidig trygdetid fra pleieforholdet opphørte fram " +
                        "til og med det året du fyller 66 år.",
                    Nynorsk to " I tillegg blir det vanlegvis rekna ut framtidig trygdetid frå pleieforholdet " +
                        "opphøyrde fram til og med det året du fyller 66 år.",
                    English to "In addition, future national insurance coverage is normally calculated from the " +
                        "time when the care period ended through the year you turn 66 years old.",
                )
            }.orShow {
                text(
                    Bokmal to "Når avdøde var under 67 år ved dødsfallet blir det vanligvis " +
                        "beregnet framtidig trygdetid fram til og med det året avdøde ville ha fylt 66 år.",
                    Nynorsk to "Dersom personen døydde før fylte 67 år, blir det vanlegvis rekna ut framtidig " +
                        "trygdetid fram til og med det året vedkomande ville ha fylt 66 år. ",
                    English to "For deceased persons under 67 years of age at the time of death, the general rule " +
                        "is to calculate future contribution time up to and including the year the deceased would have turned 66.",
                )
            }
        }
        paragraph {
            textExpr(
                Bokmal to "For å få full omstillingsstønad må ".expr() +
                    ifElse(tidligereFamiliepleier, "trygdetiden din", "avdødes trygdetid") +
                    " være beregnet til minst 40 år. Trygdetid over 40 år blir ikke tatt med i beregningen. " +
                    ifElse(tidligereFamiliepleier, "Din", "Avdødes") +
                    " samlede trygdetid er beregnet til " + trygdetid.beregnetTrygdetidAar.format() + " år.",
                Nynorsk to "For at du skal kunne få full omstillingsstønad, må den utrekna trygdetida ".expr() +
                    ifElse(tidligereFamiliepleier, "di", "til avdøde") +
                    " vere minst 40 år. Trygdetid over 40 år blir ikkje teken med i utrekninga. " +
                    ifElse(tidligereFamiliepleier, "Du", "Avdøde") +
                    " har ei samla trygdetid på " + trygdetid.beregnetTrygdetidAar.format() + " år. ",
                English to "To be entitled to full adjustment allowance,".expr() +
                    ifElse(tidligereFamiliepleier, "you", "the deceased") +
                    " must have accumulated at least 40 years of contribution time. Contribution time above 40 years of " +
                    "coverage is not included in the calculation. " +
                    ifElse(
                        tidligereFamiliepleier,
                        "Your total period of National Insurance is calculated at ",
                        "The deceased's total calculated contribution time is ",
                    ) +
                    trygdetid.beregnetTrygdetidAar.format() + " years.",
            )
        }
        showIf(trygdetid.mindreEnnFireFemtedelerAvOpptjeningstiden) {
            paragraph {
                textExpr(
                    Bokmal to "Tabellen under «Perioder med registrert trygdetid» viser full framtidig ".expr() +
                        "trygdetid. Siden " + ifElse(tidligereFamiliepleier, "du", "avdøde") +
                        " har vært medlem i folketrygden i mindre enn 4/5 av tiden mellom fylte 16 år og " +
                        ifElse(tidligereFamiliepleier, "til pleieforholdet opphørte", "dødsfallstidspunktet") +
                        " (opptjeningstiden), blir framtidig trygdetid redusert til 40 år minus 4/5 av opptjeningstiden. " +
                        "Dette er mindre enn det tabellen viser.",
                    Nynorsk to "Tabellen under «Periodar med registrert trygdetid» viser full framtidig ".expr() +
                        "trygdetid. Ettersom " + ifElse(tidligereFamiliepleier, "du", "avdøde") +
                        "avdøde var medlem av folketrygda i mindre enn 4/5 av tida mellom fylte 16 år og fram til " +
                        ifElse(tidligereFamiliepleier, "pleieforholdet opphøyrde", "sin død") +
                        " (oppteningstid), blir framtidig trygdetid redusert til 40 år minus 4/5 av oppteningstida. " +
                        "Dette er mindre enn det tabellen viser.",
                    English to "The table in “Periods with Registered Contribution Time” shows the entire ".expr() +
                        "future contribution time. Since " +
                        ifElse(tidligereFamiliepleier, "you", "the deceased") +
                        " has been a member of the National Insurance Scheme for less than 4/5 of the time between " +
                        "turning 16 and the date of " +
                        ifElse(tidligereFamiliepleier, "care period ended", "the death") +
                        " (qualifying period), the future contribution time is reduced to 40 years minus 4/5 of " +
                        "the qualifying period. This is less than what the table show.",
                )
            }
        }
    }
    showIf(trygdetid.beregningsMetodeFraGrunnlag.equalTo(BeregningsMetode.PRORATA)) {
        paragraph {
            textExpr(
                Bokmal to "For å få full omstillingsstønad må ".expr() +
                    ifElse(tidligereFamiliepleier, "trygdetiden din", "avdødes trygdetid") +
                    " være beregnet til minst 40 år. Trygdetid over 40 år blir ikke tatt med i beregningen.",
                Nynorsk to "For at du skal kunne få full omstillingsstønad, må den utrekna trygdetida ".expr() +
                    ifElse(tidligereFamiliepleier, "di", "til avdøde") +
                    " vere minst 40 år. Trygdetid over 40 år blir ikkje teken med i utrekninga.",
                English to "To be entitled to full adjustment allowance, ".expr() +
                    ifElse(tidligereFamiliepleier, "you", "the deceased") +
                    " must have accumulated at least 40 years of contribution time. Contribution time above 40 years " +
                    "of coverage is not included in the calculation.",
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Omstillingsstønaden din er beregnet etter bestemmelsene i EØS-avtalen ".expr() +
                    "fordi vilkårene for rett til omstillingsstønad er oppfylt ved sammenlegging av " +
                    ifElse(tidligereFamiliepleier, "din", "avdødes") +
                    "opptjeningstid i Norge og andre EØS- eller avtaleland. Trygdetiden er beregnet etter " +
                    ifElse(tidligereFamiliepleier, "din", "avdødes") +
                    "samlede opptjeningstid i disse landene. For å beregne norsk del av denne trygdetiden ganges " +
                    ifElse(tidligereFamiliepleier, "din", "avdødes") +
                    "avdødes samlede opptjeningstid med et forholdstall, som angir forholdet mellom faktisk " +
                    "opptjeningstid i Norge og samlet faktisk opptjeningstid i Norge og andre EØS- eller avtaleland. " +
                    ifElse(tidligereFamiliepleier, "Din", "Avdødes") +
                    " samlede trygdetid er beregnet til " +
                    trygdetid.beregnetTrygdetidAar.format() + " år, og forholdstallet til " +
                    trygdetid.prorataBroek.formatBroek() + ".",
                Nynorsk to "Omstillingsstønaden din er rekna ut etter føresegnene i EØS-avtalen, då vilkåra ".expr() +
                    "for rett til stønad er oppfylte ved samanlegging av oppteningstida " +
                    ifElse(tidligereFamiliepleier, "di", "til avdøde") +
                    " i Noreg og andre EØS- eller avtaleland. Trygdetida er rekna ut etter den samla oppteningstida som " +
                    ifElse(tidligereFamiliepleier, "di", "avdøde") +
                    " hadde i desse landa. For å rekne ut den norske del av denne trygdetida blir den " +
                    "samla oppteningstida til " +
                    ifElse(tidligereFamiliepleier, "di", "avdøde") +
                    " gonga med eit forholdstal som angir forholdet mellom faktisk oppteningstid i Noreg og samla " +
                    "faktisk oppteningstid i Noreg og andre EØS- eller avtaleland. " +
                    ifElse(tidligereFamiliepleier, "Di", "Avdøde") +
                    " har ei samla trygdetid på " + trygdetid.beregnetTrygdetidAar.format() +
                    " år, og forholdstalet er " + trygdetid.prorataBroek.formatBroek() + ".",
                English to "Your adjustment allowance are calculated according to the provisions ".expr() +
                    "in the EEA Agreement because the conditions that entitle you to the allowance have been met, by compiling " +
                    ifElse(tidligereFamiliepleier, "your", "the deceased's") +
                    " contribution time in Norway and other EEA countries or other " +
                    "countries with which Norway has an agreement. Contribution time is calculated according to " +
                    ifElse(tidligereFamiliepleier, "your", "the deceased's") +
                    " total contribution time in these the countries. To calculate the Norwegian " +
                    "part of this contribution time, " +
                    ifElse(tidligereFamiliepleier, "your", "the deceased's") +
                    " total contribution time is multiplied with a proportional fraction that provides a ratio " +
                    "between the actual contribution time in Norway and the total actual contribution time in " +
                    "Norway and any other EEA or agreement country. " +
                    ifElse(tidligereFamiliepleier, "Your", "The deceased's") +
                    " total contribution time amounts to " + trygdetid.beregnetTrygdetidAar.format() +
                    " years, and the proportional fraction of " + trygdetid.prorataBroek.formatBroek() + ".",
            )
        }
    }
    showIf(trygdetid.beregningsMetodeFraGrunnlag.equalTo(BeregningsMetode.BEST)) {
        paragraph {
            textExpr(
                Bokmal to "For å få full omstillingsstønad må ".expr() +
                    ifElse(tidligereFamiliepleier, "din", "avdøde") +
                    " trygdetid være beregnet til minst 40 år. Trygdetid over 40 år blir ikke tatt med i beregningen. " +
                    "Når grunnlag for omstillingsstønaden er oppfylt etter nasjonale regler, og " +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " også har opptjening av medlemsperioder i land som Norge har trygdeavtale med, skal trygdetid gis " +
                    "etter den beste beregningen av kun nasjonal opptjening og av sammenlagt opptjening i Norge og avtaleland.",
                Nynorsk to "For at du skal kunne få full omstillingsstønad, må den utrekna trygdetida ".expr() +
                    ifElse(tidligereFamiliepleier, "di", "til avdøde") +
                    " vere minst 40 år. Trygdetid over 40 år blir ikkje teken med i utrekninga. Når " +
                    "grunnlaget for stønad er oppfylt etter nasjonale reglar, og " +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " også har opptening av medlemsperiodar i land som Noreg har trygdeavtale med, skal det bli gitt " +
                    "trygdetid etter gunstigaste utrekning: anten berre nasjonal opptening eller samanlagd opptening " +
                    "i Noreg og avtaleland.",
                English to "To be entitled to full adjustment allowance, ".expr() +
                    ifElse(tidligereFamiliepleier, "you", "the deceased") +
                    " must have accumulated at least 40 years of contribution time. Contribution time above 40 years " +
                    "of coverage is not included in the calculation. When the basis for the allowance is met according " +
                    "to national rules, and " +
                    ifElse(tidligereFamiliepleier, "you", "the deceased") +
                    " has also accrued membership periods in countries with " +
                    "which Norway has a national insurance agreement, the contribution time must be stated " +
                    "according to the best calculation of (only) national contribution and of the combined " +
                    "contribution time in Norway and the agreement countries.",
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Ved nasjonal beregning av trygdetid tilsvarer denne det antall år ".expr() +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " har vært medlem i folketrygden etter fylte 16 år. " +
                    ifElse(
                        tidligereFamiliepleier,
                        "I tillegg blir det  vanligvis beregnet framtidig trygdetid fra pleieforholdet opphørte fram til og med det året du fyller 66 år.",
                        "Når avdøde var under 67 år ved dødsfallet blir det vanligvis beregnet framtidig trygdetid fram til og med det året avdøde ville ha fylt 66 år.",
                    ),
                Nynorsk to "Ved nasjonal utrekning av trygdetida vil denne svare til talet på år ".expr() +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " var medlem i folketrygda etter fylte 16 år. " +
                    ifElse(
                        tidligereFamiliepleier,
                        "I tillegg blir det vanlegvis rekna ut framtidig trygdetid frå pleieforholdet opphøyrde fram til og med det året du fyller 66 år.",
                        "Dersom personen døydde før fylte 67 år, blir det vanlegvis rekna ut framtidig trygdetid fram til og med det året vedkomande ville ha fylt 66 år.",
                    ),
                English to "For calculating national contribution time, this equals the number of years ".expr() +
                    ifElse(tidligereFamiliepleier, "you", "the deceased") +
                    " has been a member of the Norwegian National Insurance Scheme after reaching the age of 16. " +
                    ifElse(
                        tidligereFamiliepleier,
                        "In addition, future national insurance coverage is normally calculated from the time when the care period ended through the year you turn 66 years old.",
                        "For deceased persons under 67 years of age at the time of death, the general rule is to calculate future contribution time up to and including the year the deceased would have turned 66.",
                    ),
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Ved sammenlegging av ".expr() +
                    ifElse(tidligereFamiliepleier, "din", "avdødes") +
                    " opptjeningstid i Norge og andre EØS/avtale-land er trygdetiden beregnet etter " +
                    ifElse(tidligereFamiliepleier, "din", "avdødes") +
                    " samlede opptjeningstid i disse landene. For å beregne norsk del av denne trygdetiden ganges " +
                    ifElse(tidligereFamiliepleier, "din", "avdødes") +
                    " samlede opptjeningstid med et forholdstall, som angir forholdet mellom faktisk opptjeningstid " +
                    "i Norge og samlet faktisk opptjeningstid i Norge og andre EØS-land.",
                Nynorsk to "Dersom ein legg saman oppteningstida som ".expr() +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " hadde i Noreg og andre EØS-/avtaleland, blir trygdetida rekna ut etter den samla oppteningstida til " +
                    ifElse(tidligereFamiliepleier, "deg", "avdøde") +
                    " i desse landa. For å rekne ut den norske del av denne trygdetida blir den samla oppteningstida til " +
                    ifElse(tidligereFamiliepleier, "deg", "avdøde") +
                    " gonga med eit forholdstal som angir forholdet mellom faktisk oppteningstid i Noreg og samla " +
                    "faktisk oppteningstid i Noreg og andre EØS-land.",
                English to "For comparing ".expr() +
                    ifElse(tidligereFamiliepleier, "your", "the deceased's") +
                    " contribution time in Norway with other EEA/agreement countries, the contribution time is " +
                    "calculated according to " +
                    ifElse(tidligereFamiliepleier, "your", "the deceased's") +
                    " total contribution time in these the countries. To calculate the Norwegian part of this contribution time, " +
                    ifElse(tidligereFamiliepleier, "your", "the deceased's") +
                    " total contribution time is multiplied with a proportional fraction that provides the ratio " +
                    "between the actual contribution time in Norway and the total actual contribution time in " +
                    "Norway and any other EEA or agreement country.",
            )
        }

        showIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
            paragraph {
                textExpr(
                    Bokmal to ifElse(tidligereFamiliepleier, "Din", "Avdødes") +
                        " samlede trygdetid fra avtaleland er beregnet til ".expr() +
                        trygdetid.beregnetTrygdetidAar.format() + " år, og forholdstallet til " +
                        trygdetid.prorataBroek.formatBroek() + ". Dette gir den beste beregningen av trygdetid.",
                    Nynorsk to ifElse(tidligereFamiliepleier, "Du", "Avdøde") +
                        " har ei samla trygdetid på ".expr() +
                        trygdetid.beregnetTrygdetidAar.format() + " år frå avtaleland, og forholdstalet " +
                        "er " + trygdetid.prorataBroek.formatBroek() + ". Dette gir den gunstigaste utrekninga " +
                        "av trygdetid.",
                    English to ifElse(tidligereFamiliepleier, "Your", "The deceased's") +
                        " total contribution time from agreement countries ".expr() +
                        "amounts to " + trygdetid.beregnetTrygdetidAar.format() + " years, and the proportional " +
                        "fraction of " + trygdetid.prorataBroek.formatBroek() + ". This provides the best " +
                        "calculation for total contribution time.",
                )
            }
        }.orShowIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)) {
            paragraph {
                textExpr(
                    Bokmal to ifElse(tidligereFamiliepleier, "Din", "Avdødes") +
                        " samlede trygdetid er beregnet til ".expr() +
                        trygdetid.beregnetTrygdetidAar.format() + " år ved nasjonal opptjening. " +
                        "Dette gir den beste beregningen av trygdetid.",
                    Nynorsk to ifElse(tidligereFamiliepleier, "Du", "Avdøde") +
                        " har ei samla trygdetid på ".expr() +
                        trygdetid.beregnetTrygdetidAar.format() + " år ved nasjonal opptening. Dette gir den " +
                        "gunstigaste utrekninga av trygdetid.",
                    English to ifElse(tidligereFamiliepleier, "Your", "The deceased's") +
                        " total calculated contribution time is ".expr() +
                        trygdetid.beregnetTrygdetidAar.format() + " years in national contributions. " +
                        "This provides the best calculation for total contribution time.",
                )
            }
        }
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, OmstillingsstoenadBeregning>.perioderMedRegistrertTrygdetid(
    trygdetid: Expression<Trygdetid>,
    tidligereFamiliepleier: Expression<Boolean>,
) {
    title2 {
        text(
            Bokmal to "Perioder med registrert trygdetid",
            Nynorsk to "Periodar med registrert trygdetid",
            English to "Periods of registered contribution time",
        )
    }

    showIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)) {
        paragraph {
            textExpr(
                Bokmal to "Tabellen viser perioder ".expr() +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " har vært medlem av folketrygden, og registrert " +
                    "fremtidig trygdetid.",
                Nynorsk to "Tabellen viser periodar ".expr() +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " har vore medlem av folketrygda og registrert " +
                    "framtidig trygdetid. ",
                English to "The table shows the periods in which ".expr() +
                    ifElse(tidligereFamiliepleier, "you", "the deceased") +
                    " was a member of the National Insurance Scheme, and registered future contribution time.",
            )
        }
        includePhrase(Trygdetidstabell(trygdetid.trygdetidsperioder))
    }.orShowIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
        paragraph {
            textExpr(
                Bokmal to "Tabellen viser perioder ".expr() +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " har vært medlem av folketrygden og medlemsperioder " +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " har hatt i land som Norge har trygdeavtale med, som er tatt med i beregningen.",
                Nynorsk to "Tabellen viser periodar ".expr() +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " har vore medlem av folketrygda og medlemsperiodar " +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " har hatt i land som Noreg har trygdeavtale med, som er tekne med i utrekninga.",
                English to "The table shows periods in which ".expr() +
                    ifElse(tidligereFamiliepleier, "you", "the deceased") +
                    " was a member of the National Insurance Scheme and member periods which " +
                    ifElse(tidligereFamiliepleier, "you", "the deceased") +
                    " contributed in countries which Norway had a national insurance agreement which are included in the calculation.",
            )
        }
        includePhrase(Trygdetidstabell(trygdetid.trygdetidsperioder))
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, OmstillingsstoenadBeregning>.meldFraTilNav() {
    paragraph {
        text(
            Bokmal to
                "Hvis du mener at opplysningene brukt i beregningen er feil, må du melde fra til Nav. Det kan ha betydning for størrelsen på omstillingsstønaden din.",
            Nynorsk to
                "Sei frå til Nav dersom du meiner at det er brukt feil opplysningar i utrekninga. Det kan ha betydning for kor mykje omstillingsstønad du får.",
            English to
                "If you believe the information applied in the calculation is incorrect, you must notify Nav. Errors may affect your allowance amount.",
        )
    }
}
