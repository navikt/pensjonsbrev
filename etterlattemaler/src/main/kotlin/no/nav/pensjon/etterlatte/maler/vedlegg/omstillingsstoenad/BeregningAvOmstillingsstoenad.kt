package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.erYrkesskade
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
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.beregnetTrygdetidAar
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.beregningsMetodeAnvendt
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.beregningsMetodeFraGrunnlag
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.harFremtidigTrygdetid
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.mindreEnnFireFemtedelerAvOpptjeningstiden
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.prorataBroek
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.trygdetidsperioder
import no.nav.pensjon.etterlatte.maler.vedlegg.Trygdetidstabell

fun beregningAvOmstillingsstoenad(
    tidligereFamiliepleier: Boolean,
    innvilgelsesaar: Boolean
): AttachmentTemplate<LangBokmalNynorskEnglish, OmstillingsstoenadBeregning> =
    createAttachment(
        title = {
            text(
                bokmal { +"Beregning av omstillingsstønad" },
                nynorsk { +"Utrekning av omstillingsstønad" },
                english { +"Calculation of adjustment allowance" },
            )
        },
        includeSakspart = false,
    ) {
        beregning(tidligereFamiliepleier.expr(), innvilgelsesaar.expr())
        trygdetid(trygdetid, tidligereFamiliepleier.expr(), erYrkesskade)
        perioderMedRegistrertTrygdetid(trygdetid, tidligereFamiliepleier.expr())
        meldFraTilNav()
    }

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, OmstillingsstoenadBeregning>.beregning(
    tidligereFamiliepleier: Expression<Boolean>,
    innvilgelsesAar: Expression<Boolean>,
) {
    val sisteInntekt = sisteBeregningsperiode.inntekt
    val sisteGrunnbeloep = sisteBeregningsperiode.grunnbeloep
    val sisteOppgittInntekt = sisteBeregningsperiode.oppgittInntekt
    val sisteFratrekkInnAar = sisteBeregningsperiode.fratrekkInnAar
    val sisteInnvilgaMaaneder = sisteBeregningsperiode.innvilgaMaaneder


    paragraph {
        text(
            bokmal { +"Full omstillingsstønad beregnes utfra 2,25 ganger folketrygdens " +
                "grunnbeløp (G). Dagens verdi av grunnbeløpet er " + sisteGrunnbeloep.format() + ". " +
                "Grunnbeløpet blir regulert 1. mai hvert år. Økningen etterbetales vanligvis i juni hvert år." },
            nynorsk { +"Full omstillingsstønad blir rekna ut etter 2,25 gongar grunnbeløpet i " +
                "folketrygda (G). Dagens verdi av grunnbeløpet er " + sisteGrunnbeloep.format() + ". " +
                "Grunnbeløpet blir regulert 1. mai kvart år. Auken blir vanlegvis etterbetalt i " +
                "juni kvart år." },
            english { +"Full adjustment allowance are calculated based on 2.25 × the national " +
                "insurance basic amount (G). The current value of the basic amount is " + sisteGrunnbeloep.format() +
                ". The basic amount is adjusted on 1 May each year. You will receive payment of any increase in " +
                "June of each year." },
        )
    }
    paragraph {
        text(
            bokmal { +"For at du skal få full stønad må " +
                ifElse(tidligereFamiliepleier, "trygdetiden din", "avdødes trygdetid") +
                " være minst 40 år. Er trygdetiden mindre enn 40 år vil stønaden avkortes." },
            nynorsk { +"For at du skal få full stønad må " +
                ifElse(tidligereFamiliepleier, "du", "avdøde") +
                " ha hatt ei trygdetid på minst 40 år. Dersom trygdetida er mindre enn 40 år, blir stønaden avkorta." },
            english { +"To receive the maximum allowance, " +
                ifElse(
                    tidligereFamiliepleier,
                    "your period of national insurance coverage",
                    "the deceased's contribution time to the national insurance scheme",
                ) +
                " must be at least 40 years. If the contribution time is less than 40 years, the allowance is reduced." },
        )
    }
    paragraph {
        text(
            bokmal { +"Inntekten din avgjør hvor mye du kan få. Stønaden reduseres med 45 prosent av " +
                "arbeidsinntekt eller tilsvarende inntekt som er over halvparten av grunnbeløpet." },
            nynorsk { +"Inntekta di avgjer kor mykje du kan få. Stønaden blir redusert med 45 prosent av " +
                "arbeidsinntekt eller tilsvarande inntekt som er over halvparten av grunnbeløpet." },
            english { +"Your income determines how much money you are entitled to. The allowance is reduced by " +
                "45 percent of the employment income or similar income when this is over half the basic amount." },
        )
    }

    title2 {
        text(
            bokmal { +"Beregnet omstillingsstønad" },
            nynorsk { +"Utrekna omstillingsstønad" },
            english { +"Calculated adjustment allowance" },
        )
    }

    includePhrase(Beregningsperiodetabell(beregningsperioder))

    paragraph {
        text(
            bokmal { +"Beløpene i tabellen er før skatt." },
            nynorsk { +"Beløpa i tabellen er før skatt." },
            english { +"The amounts in the table are stated before tax." },
        )
    }

    title2 {
        text(
            bokmal { +"Stønad før reduksjon for inntekt" },
            nynorsk { +"Stønad før reduksjon for inntekt" },
            english { +"Allowance paid before income reduction" },
        )
    }

    showIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)) {
        paragraph {
            text(
                bokmal { +"" + "Stønad per år før reduksjon av inntekt er beregnet til 2,25 ganger " +
                    "grunnbeløpet i folketrygden (G) ganget med " + trygdetid.beregnetTrygdetidAar.format() + "/40 " +
                    "trygdetid. Beløpet fordeles på 12 utbetalinger i året. " },
                nynorsk { +"" + "Stønad per år før reduksjon av inntekt er rekna ut til 2,25 gongar " +
                    "grunnbeløpet i folketrygda (G) gonga med " + trygdetid.beregnetTrygdetidAar.format() + "/40 " +
                    "trygdetid. Beløpet blir fordelt på 12 utbetalingar i året. " },
                english { +"" + "Allowance per year before reduction of income are calculated " +
                    "based on 2.25 × national insurance basic amount (G) × " +
                    trygdetid.beregnetTrygdetidAar.format() + "/40 years of contribution time. This amount " +
                    "is distributed in 12 payments a year. " },
            )
        }
    }.orShowIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
        paragraph {
            text(
                bokmal { +"" + "Stønad per år før reduksjon for inntekt er beregnet til 2,25 ganger " +
                    "grunnbeløpet i folketrygden ganget med " + trygdetid.beregnetTrygdetidAar.format() + "/40 " +
                    "trygdetid, ganget med forholdstallet " + trygdetid.prorataBroek.formatBroek() +
                    ". Beløpet fordeles på 12 utbetalinger i året." },
                nynorsk { +"" + "Stønad per år før reduksjon for inntekt er rekna ut til 2,25 gongar " +
                    "grunnbeløpet i folketrygda gonga med " + trygdetid.beregnetTrygdetidAar.format() + "/40 " +
                    "trygdetid, gonga med forholdstalet " + trygdetid.prorataBroek.formatBroek() +
                    ". Beløpet blir fordelt på 12 utbetalingar i året." },
                english { +"" + "Allowance per year before reduction of income are calculated based on " +
                    "2.25 × national insurance basic amount (G) × " + trygdetid.beregnetTrygdetidAar.format() +
                    "/40 years of contribution time, multiplied by the proportional fraction " +
                    trygdetid.prorataBroek.formatBroek() + ". This amount is distributed in 12 payments a year." },
            )
        }
    }

    title2 {
        text(
            bokmal { +"Inntekten din" },
            nynorsk { +"Inntekta di" },
            english { +"Your income" },
        )
    }

    showIf(sisteInntekt.greaterThan(0)) {
        paragraph {
            text(
                bokmal { +
                    "Stønaden reduseres etter inntekten du har hvert kalenderår. Det er bare inntekt for måneder med innvilget stønad som skal medregnes. Inntekten blir fordelt på antall innvilgede måneder. " },
                nynorsk { +
                    "Stønaden blir redusert etter inntekta du har kvart kalenderår. Det er berre inntekt for månader med innvilga stønad som skal medreknast. Inntekta blir fordelt på talet på innvilga månader. " },
                english { +
                    "The allowance is reduced according to your income each calendar year. Only income for months with granted allowance must be included. The income is distributed over the number of months granted. " },
            )
        }
        paragraph {
            text(
                bokmal { +"Din oppgitte inntekt for " + virkningsdato.formatAar() + " er " +
                    sisteOppgittInntekt.format() + "." },

                nynorsk { +"Du har ei oppgitt inntekt for " + virkningsdato.formatAar() + " på " +
                    sisteOppgittInntekt.format() + "." },

                english { +"Your estimated income for " + virkningsdato.formatAar() + " is " +
                    sisteOppgittInntekt.format() + "." },
            )
            showIf(opphoerNesteAar.not()) {
                ifNotNull(oppphoersdato) { opphoer ->
                    text(
                        bokmal { +" Dette er forventet inntekt frem til " + opphoer.format() +
                            ", som er forventet opphørsdato for mottak av stønaden." },

                        nynorsk { +" Dette er forventa inntekt fram til " + opphoer.format() +
                            ", som er forventa opphøyrsdato for mottak av stønaden." },

                        english { +" This is the expected income until " + opphoer.format() +
                            ", which is the expected end date for receiving the allowance." }
                    )
                }
            }
            showIf(innvilgelsesAar and sisteInnvilgaMaaneder.lessThan(12)) {
                text(
                    bokmal { +" Fratrekk for inntekt i måneder før du er innvilget stønad er " +
                        sisteFratrekkInnAar.format() + ". Vi har lagt til grunn at du har en inntekt på " +
                        sisteInntekt.format() + " i innvilgede måneder dette året." },

                    nynorsk { +" Fråtrekk for inntekt i månader før du er innvilga stønad er " +
                        sisteFratrekkInnAar.format() + ". Vi har lagt til grunn at du har ei inntekt på " +
                        sisteInntekt.format() + " i innvilga månader dette året." },

                    english { +" Deduction for income in months before you are granted allowance is " +
                        sisteFratrekkInnAar.format() + ". We have assumed that you have an income of " +
                        sisteInntekt.format() + " in granted months this year." },
                )
            }
            text(
                bokmal { +" Inntekt blir alltid avrundet ned til nærmeste tusen." },
                nynorsk { +" Inntekt blir alltid avrunda ned til næraste tusen." },
                english { +" The amount is rounded down to the nearest thousand." },
            )
        }
        paragraph {
            text(
                bokmal { +"Stønad per måned er redusert på følgende måte: (" + sisteInntekt.format() +
                    " / " + sisteInnvilgaMaaneder.format() + " måneder) minus (0,5 G / 12 måneder). " +
                    "Beløpet ganges med 45 prosent." },
                nynorsk { +"Stønaden per månad har blitt redusert på følgjande måte: (" + sisteInntekt.format() +
                    " / " + sisteInnvilgaMaaneder.format() + " månader) minus (0,5 G / 12 månader). " +
                    "Beløpet blir gonga med 45 prosent." },
                english { +"The monthly allowance amount has been reduced as follows: (" +
                    sisteInntekt.format() + " / " + sisteInnvilgaMaaneder.format() + " months) minus (0.5 G / 12 months). " +
                    "The amount is multiplied by 45 percent." },
            )
        }

        ifNotNull(sisteBeregningsperiodeNesteAar) {
            val sisteInntektNesteAar = it.inntekt
            val innvilgaMaanederNesteAar = it.innvilgaMaaneder
            val sisteInntektNesteAarFom = it.datoFOM

            paragraph {
                text(
                    bokmal { +
                        "Fra " + sisteInntektNesteAarFom.format() + " har vi lagt til grunn din oppgitte forventede inntekt på " +
                        sisteInntektNesteAar.format() + "." },
                    nynorsk { +
                        "Frå " + sisteInntektNesteAarFom.format() + " har vi lagt til grunn den oppgitte forventa inntekta di på " +
                        sisteInntektNesteAar.format() + "." },
                    english { +
                        "From " + sisteInntektNesteAarFom.format() + ", we have based your stated expected income on " +
                        sisteInntektNesteAar.format() + "." },
                )
                showIf(opphoerNesteAar) {
                    ifNotNull(oppphoersdato) { dato ->
                        text(
                            bokmal { +" Dette er forventet inntekt frem til " + dato.format() +
                                ", som er forventet opphørsdato for mottak av stønaden." },

                            nynorsk { +" Dette er forventa inntekt fram til " + dato.format() +
                                ", som er forventa opphøyrsdato for mottak av stønaden." },

                            english { +" This is the expected income from the grant until " + dato.format() +
                                ", which is the expected end date for receiving the allowance." },
                        )
                    }
                }
                text(
                    bokmal { +" Inntekt blir alltid avrundet ned til nærmeste tusen." },
                    nynorsk { +" Inntekt blir alltid avrunda ned til næraste tusen." },
                    english { +" The amount is rounded down to the nearest thousand." }
                )
            }

            paragraph {
                text(
                    bokmal { +"Stønad per måned er redusert på følgende måte: (" + sisteInntektNesteAar.format() +
                        " / " + innvilgaMaanederNesteAar.format() + " måneder) minus (0,5 G / 12 måneder). " +
                        "Beløpet ganges med 45 prosent." },
                    nynorsk { +"Stønaden per månad har blitt redusert på følgjande måte: (" + sisteInntektNesteAar.format() +
                        " / " + innvilgaMaanederNesteAar.format() + " månader) minus (0,5 G / 12 månader). " +
                        "Beløpet blir gonga med 45 prosent." },
                    english { +"The monthly allowance amount has been reduced as follows: (" +
                        sisteInntektNesteAar.format() + " / " + innvilgaMaanederNesteAar.format() +
                        " months) minus (0.5 G / 12 months). " +
                        "The amount is multiplied by 45 percent." },
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
                text(
                    bokmal { +ifElse(erRestanseTrekk, "Trekk", "Tillegg") + " i utbetalingen" },
                    nynorsk { +ifElse(erRestanseTrekk, "Trekk", "Tillegg") + " i utbetalinga" },
                    english { +ifElse(erRestanseTrekk, "Deduction from", "Addition to") + " payment" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Den forventede inntekten din for inneværende år er blitt justert. " +
                        "Det blir " + ifElse(erRestanseTrekk, "gjort et trekk", "gitt et tillegg") +
                        " i utbetalingen for resten av året. Dette blir gjort for å unngå eller redusere et etteroppgjør. " +
                        ifElse(erRestanseTrekk, "Trekket", "Tillegget") + " er på " + restanse.format() +
                        " per måned. Dette er medregnet i “Utbetaling per måned”, som fremgår i tabellen over." },
                    nynorsk { +"Den forventa inntekta di for inneverande år har blitt justert. " +
                        "Det blir " + ifElse(erRestanseTrekk, "gjort eit trekk", "gitt eit tillegg") +
                        " i utbetalinga for resten av året. Dette blir gjort for å unngå eller redusere eit etteroppgjer. " +
                        ifElse(erRestanseTrekk, "Trekket", "Tillegget") + " er på " + restanse.format() +
                        " per månad. Dette er medrekna i “Utbetaling per månad”, som kjem fram i tabellen over." },
                    english { +"Your estimated income for the current year has been adjusted. " +
                        "Your payment amount has therefore been " + ifElse(erRestanseTrekk, "reduced", "increased") +
                        " for the remainder of the year to avoid or reduce a final settlement. " +
                        ifElse(erRestanseTrekk, "The deduction", "Addition") + " is " + restanse.format() +
                        " per month. This is included in \"Payout per month\", which appears in the table above." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Det utbetales aldri mer enn stønadsbeløpet før reduksjon for inntekt. Har du rett på mer etterbetaling enn tillegget som utbetales resten av året, blir dette tatt hensyn til i etteroppgjøret." },
                    nynorsk { +"Det blir aldri utbetalt meir enn stønadsbeløpet før reduksjon for inntekt. Har du rett på meir etterbetaling enn tillegget som blir utbetalt resten av året, blir dette teke omsyn til i etteroppgjeret." },
                    english { +"No more is paid out than the allowance amount before reduction for income. If you are entitled to a larger back payment than the supplement paid for the rest of the year, this will be taken into account in the final settlement." }
                )
            }
        }
        showIf(
            sisteBeregningsperiode.restanse.notEqualTo(0) and
            sisteBeregningsperiode.utbetaltBeloep.equalTo(0)
        ) {
            title2 {
                text(
                    bokmal { +"Feilutbetalt ytelse vil bli behandlet i et etteroppgjør" },
                    nynorsk { +"Feilutbetalt yting vil bli behandla i eit etteroppgjer" },
                    english { +"Incorrectly paid allowance will be dealt with in a final settlement" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Den forventede inntekten din for inneværende år er blitt justert. " +
                            "Siden du ikke får utbetalt stønad, kan vi ikke trekke inn for mye utbetalt stønad " +
                            "i utbetalingene for resten av året. " +
                            "Feilutbetalt stønad vil derfor bli behandlet i etteroppgjøret for i år, " +
                            "som blir gjort etter at skatteåret er ferdig lignet neste år." },
                    nynorsk { +"Den forventa inntekta di for inneverande år har vorte justert. " +
                            "Sidan du ikkje får utbetalt stønad, kan vi ikkje trekkja inn for mykje utbetalt stønad " +
                            "i utbetalingane for resten av året. " +
                            "Feilutbetalt stønad vil derfor bli behandla i etteroppgjeret for i år, " +
                            "som blir gjort etter at skatteåret er ferdig likna neste år." },
                    english { +"Your estimated income for the current year has been adjusted. Since you are " +
                            "not receiving any allowance for the rest of the year, we cannot deduct any overpaid " +
                            "benefits from payments during this period. Any overpaid benefits will therefore be " +
                            "addressed in the final settlement for this year, which will be completed after " +
                            "the tax settlement for this year is finalized next year." }
                )
            }
        }
    }.orShow {
        paragraph {
            text(
                bokmal { +"Vi har lagt til grunn at du ikke har arbeidsinntekt eller tilsvarende inntekt som " +
                    "omstillingsstønaden skal reduseres etter." },
                nynorsk { +"Vi har lagt til grunn at du ikkje har arbeidsinntekt eller tilsvarande inntekt som " +
                    "omstillingsstønaden skal reduserast etter. " },
                english { +"Our calculation shows that you had no employment income or similar income from " +
                    "which the adjustment allowance can be reduced." },
            )
        }
    }

    showIf(sisteBeregningsperiode.sanksjon) {
        title2 {
            text(
                bokmal { +"Stans i utbetalingen av omstillingsstønaden" },
                nynorsk { +"Stans i utbetaling av omstillingsstønad" },
                english { +"Stoppage of payment of adjustment allowance" },
            )
        }

        paragraph {
            text(
                bokmal { +"Omstillingsstønaden din er stanset fordi du ikke har oppfylt kravet om aktivitetsplikt. " +
                    "“Utbetaling per måned” er derfor satt til 0 fra gjeldende tidspunkt, slik det fremgår i tabellen over." },
                nynorsk { +"Då du ikkje har oppfylt kravet om aktivitetsplikt, har omstillingsstønaden din blitt stansa. " +
                    "«Utbetaling per månad» er difor sett til 0 frå gjeldande tidspunkt, slik det går fram av tabellen over." },
                english { +"Your adjustment allowance has been stopped because you have not met the mandatory activity requirements. " +
                    "«Payment per month is therefore set at «0» from the relevant date, as evident in the table above." },
            )
        }
    }

    konverterElementerTilBrevbakerformat(innhold)
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, OmstillingsstoenadBeregning>.trygdetid(
    trygdetid: Expression<Trygdetid>,
    tidligereFamiliepleier: Expression<Boolean>,
    erYrkesskade: Expression<Boolean>,
) {
    title2 {
        text(
            bokmal { +"Trygdetid" },
            nynorsk { +"Trygdetid" },
            english { +"Period of national insurance coverage" },
        )
    }

    showIf(trygdetid.beregningsMetodeFraGrunnlag.equalTo(BeregningsMetode.NASJONAL)) {
        paragraph {
            text(
                bokmal { +"Trygdetiden tilsvarer det antall år " +
                    ifElse(tidligereFamiliepleier, "du", "avdøde ") +
                    " har vært medlem i folketrygden etter fylte 16 år. " },
                nynorsk { +"Trygdetida svarer til talet på år " +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " var medlem i folketrygda etter fylte 16 år. " },
                english { +"Contribution time is the number of years " +
                    ifElse(tidligereFamiliepleier, "you", "the deceased") +
                    " has been a member of the Norwegian National Insurance Scheme after reaching the age of 16. " },
            )

            showIf(tidligereFamiliepleier) {
                text(
                    bokmal { +"Det blir vanligvis beregnet framtidig trygdetid fra pleieforholdet opphørte fram " +
                        "til og med det året du fyller 66 år." },
                    nynorsk { +" I tillegg blir det vanlegvis rekna ut framtidig trygdetid frå pleieforholdet " +
                        "opphøyrde fram til og med det året du fyller 66 år." },
                    english { +"In addition, future national insurance coverage is normally calculated from the " +
                        "time when the care period ended through the year you turn 66 years old." },
                )
            }.orShow {
                text(
                    bokmal { +"Når avdøde var under 67 år ved dødsfallet blir det vanligvis " +
                        "beregnet framtidig trygdetid fram til og med det året avdøde ville ha fylt 66 år." },
                    nynorsk { +"Dersom personen døydde før fylte 67 år, blir det vanlegvis rekna ut framtidig " +
                        "trygdetid fram til og med det året vedkomande ville ha fylt 66 år. " },
                    english { +"For deceased persons under 67 years of age at the time of death, the general rule " +
                        "is to calculate future contribution time up to and including the year the deceased would have turned 66." },
                )
            }
        }

        showIf(trygdetid.harFremtidigTrygdetid.not()) {
            paragraph {
                text(
                    bokmal { +"Omstillingsstønaden din er beregnet etter unntaksbestemmelser siden avdøde ikke var medlem i folketrygden de siste fem årene før dødsfallet. Unntaksvilkåret er at avdøde kunne fått beregnet alderspensjon på grunn av tidligere opptjening. Det gis ikke framtidig trygdetid når stønaden innvilges etter unntaksvilkår." },
                    nynorsk { +"Omstillingsstønaden din er utrekna etter unntaksføresegner sidan avdøde ikkje var medlem i folketrygda dei siste fem åra før dødsfallet. Unntaksvilkåret er at avdøde kunne fått utrekna alderspensjon på grunn av tidlegare opptening. Det blir ikkje gitt framtidig trygdetid når stønaden blir innvilga etter unntaksføresegnene."},
                    english { +"Your adjustment allowance has been calculated under exceptional provisions because the deceased was not a member of the National Insurance Scheme during the last five years prior to the death. The exception criterion is that the deceased could have been granted an retirement pension based on previous pension accrual. No future insurance periods are granted when the allowance is awarded under exceptional provisions."},
                )
            }
        }

        paragraph {
            text(
                bokmal { +"For å få full omstillingsstønad må " +
                    ifElse(tidligereFamiliepleier, "trygdetiden din", "avdødes trygdetid") +
                    " være beregnet til minst 40 år. Trygdetid over 40 år blir ikke tatt med i beregningen. " +
                    ifElse(tidligereFamiliepleier, "Din", "Avdødes") +
                    " samlede trygdetid er beregnet til " + trygdetid.beregnetTrygdetidAar.format() + " år." },
                nynorsk { +"For at du skal kunne få full omstillingsstønad, må den utrekna trygdetida " +
                    ifElse(tidligereFamiliepleier, "di", "til avdøde") +
                    " vere minst 40 år. Trygdetid over 40 år blir ikkje teken med i utrekninga. " +
                    ifElse(tidligereFamiliepleier, "Du", "Avdøde") +
                    " har ei samla trygdetid på " + trygdetid.beregnetTrygdetidAar.format() + " år. " },
                english { +"To be entitled to full adjustment allowance," +
                    ifElse(tidligereFamiliepleier, "you", "the deceased") +
                    " must have accumulated at least 40 years of contribution time. Contribution time above 40 years of " +
                    "coverage is not included in the calculation. " +
                    ifElse(
                        tidligereFamiliepleier,
                        "Your total period of National Insurance is calculated at ",
                        "The deceased's total calculated contribution time is ",
                    ) +
                    trygdetid.beregnetTrygdetidAar.format() + " years." },
            )
        }

        showIf(erYrkesskade) {
            paragraph {
                text(
                    bokmal { +"Det er bekreftet at dødsfallet skyldes en godkjent yrkesskade eller yrkessykdom. " +
                            "Det gis derfor omstillingsstønad etter egne særbestemmelser. Selv om den avdøde hadde mindre " +
                            "enn 40 års trygdetid i Norge, er omstillingsstønaden beregnet med full trygdetid. " +
                            "Dette framkommer ikke i tabellen nedenfor." },
                    nynorsk { +"Det er stadfesta at dødsfallet kjem av ein godkjend yrkesskade eller yrkessjukdom. " +
                            "Det blir derfor gitt omstillingsstønad etter eigne særreglar. Sjølv om den avdøde hadde " +
                            "mindre enn 40 års trygdetid i Noreg, er omstillingsstønaden berekna med full trygdetid. Dette kjem ikkje fram i tabellen nedanfor." },
                    english { +"It has been confirmed that the death was caused by an approved occupational " +
                            "injury or disease. The adjustment allowance is granted under special regulations. " +
                            "Although the deceased had less than 40 years of social security coverage in Norway, " +
                            "the adjustment allowance is calculated based on a full social security period. " +
                            "This is not reflected in the table below." },
                )
            }
        }.orShowIf(trygdetid.mindreEnnFireFemtedelerAvOpptjeningstiden) {
            paragraph {
                text(
                    bokmal { +"Tabellen under «Perioder med registrert trygdetid» viser full framtidig " +
                        "trygdetid. Siden " + ifElse(tidligereFamiliepleier, "du", "avdøde") +
                        " har vært medlem i folketrygden i mindre enn 4/5 av tiden mellom fylte 16 år og " +
                        ifElse(tidligereFamiliepleier, "til pleieforholdet opphørte", "dødsfallstidspunktet") +
                        " (opptjeningstiden), blir framtidig trygdetid redusert til 40 år minus 4/5 av opptjeningstiden. " +
                        "Dette er mindre enn det tabellen viser." },
                    nynorsk { +"Tabellen under «Periodar med registrert trygdetid» viser full framtidig " +
                        "trygdetid. Ettersom " + ifElse(tidligereFamiliepleier, "du", "avdøde") +
                        "avdøde var medlem av folketrygda i mindre enn 4/5 av tida mellom fylte 16 år og fram til " +
                        ifElse(tidligereFamiliepleier, "pleieforholdet opphøyrde", "sin død") +
                        " (oppteningstid), blir framtidig trygdetid redusert til 40 år minus 4/5 av oppteningstida. " +
                        "Dette er mindre enn det tabellen viser." },
                    english { +"The table in “Periods with Registered Contribution Time” shows the entire " +
                        "future contribution time. Since " +
                        ifElse(tidligereFamiliepleier, "you", "the deceased") +
                        " has been a member of the National Insurance Scheme for less than 4/5 of the time between " +
                        "turning 16 and the date of " +
                        ifElse(tidligereFamiliepleier, "care period ended", "the death") +
                        " (qualifying period), the future contribution time is reduced to 40 years minus 4/5 of " +
                        "the qualifying period. This is less than what the table show." },
                )
            }
        }
    }
    showIf(trygdetid.beregningsMetodeFraGrunnlag.equalTo(BeregningsMetode.PRORATA)) {
        paragraph {
            text(
                bokmal { +"For å få full omstillingsstønad må " +
                    ifElse(tidligereFamiliepleier, "trygdetiden din", "avdødes trygdetid") +
                    " være beregnet til minst 40 år. Trygdetid over 40 år blir ikke tatt med i beregningen." },
                nynorsk { +"For at du skal kunne få full omstillingsstønad, må den utrekna trygdetida " +
                    ifElse(tidligereFamiliepleier, "di", "til avdøde") +
                    " vere minst 40 år. Trygdetid over 40 år blir ikkje teken med i utrekninga." },
                english { +"To be entitled to full adjustment allowance, " +
                    ifElse(tidligereFamiliepleier, "you", "the deceased") +
                    " must have accumulated at least 40 years of contribution time. Contribution time above 40 years " +
                    "of coverage is not included in the calculation." },
            )
        }

        paragraph {
            text(
                bokmal { +"Omstillingsstønaden din er beregnet etter bestemmelsene i EØS-avtalen " +
                    "fordi vilkårene for rett til omstillingsstønad er oppfylt ved sammenlegging av " +
                    ifElse(tidligereFamiliepleier, "din", "avdødes") +
                    " opptjeningstid i Norge og andre EØS- eller avtaleland. Trygdetiden er beregnet etter " +
                    ifElse(tidligereFamiliepleier, "din", "avdødes") +
                    " samlede opptjeningstid i disse landene. For å beregne norsk del av denne trygdetiden ganges " +
                    ifElse(tidligereFamiliepleier, "din", "avdødes") +
                    " samlede opptjeningstid med et forholdstall, som angir forholdet mellom faktisk " +
                    "opptjeningstid i Norge og samlet faktisk opptjeningstid i Norge og andre EØS- eller avtaleland. " +
                    ifElse(tidligereFamiliepleier, "Din", "Avdødes") +
                    " samlede trygdetid er beregnet til " +
                    trygdetid.beregnetTrygdetidAar.format() + " år, og forholdstallet til " +
                    trygdetid.prorataBroek.formatBroek() + "." },
                nynorsk { +"Omstillingsstønaden din er rekna ut etter føresegnene i EØS-avtalen, då vilkåra " +
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
                    " år, og forholdstalet er " + trygdetid.prorataBroek.formatBroek() + "." },
                english { +"Your adjustment allowance are calculated according to the provisions " +
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
                    " years, and the proportional fraction of " + trygdetid.prorataBroek.formatBroek() + "." },
            )
        }

        showIf(trygdetid.mindreEnnFireFemtedelerAvOpptjeningstiden) {
            paragraph {
                text(
                    bokmal { +"Siden avdøde har vært medlem i et EØS-land i mindre enn 4/5 av tiden mellom fylte " +
                            "16 år og dødsfallstidspunktet (opptjeningstiden), blir den framtidige trygdetiden redusert. " +
                            "Denne reduksjonen skjer før prorata-beregningen utføres. " },
                    nynorsk { +"Sidan avdøde har vært medlem i eit EØS-land i mindre enn 4/5 av tiden mellom fylte " +
                            "16 år og dødsfallstidspunktet (oppteningstida), blir den framtidige trygdetida redusert. " +
                            "Reduksjonen skjer før utrekning av prorata. " },
                    english { +"Since the deceased has been a member of the National Insurance Scheme for less " +
                            "than 4/5 of the period between the age of 16 and the time of death (qualifying period), " +
                            "the future social contribution time is reduced to 40 years minus 4/5 of the qualifying " +
                            "period. This reduction takes place before the pro rata calculation is performed. " },
                )
            }
        }
    }
    showIf(trygdetid.beregningsMetodeFraGrunnlag.equalTo(BeregningsMetode.BEST)) {
        paragraph {
            text(
                bokmal { +"For å få full omstillingsstønad må " +
                    ifElse(tidligereFamiliepleier, "din", "avdøde") +
                    " trygdetid være beregnet til minst 40 år. Trygdetid over 40 år blir ikke tatt med i beregningen. " +
                    "Når grunnlag for omstillingsstønaden er oppfylt etter nasjonale regler, og " +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " også har opptjening av medlemsperioder i land som Norge har trygdeavtale med, skal trygdetid gis " +
                    "etter den beste beregningen av kun nasjonal opptjening og av sammenlagt opptjening i Norge og avtaleland." },
                nynorsk { +"For at du skal kunne få full omstillingsstønad, må den utrekna trygdetida " +
                    ifElse(tidligereFamiliepleier, "di", "til avdøde") +
                    " vere minst 40 år. Trygdetid over 40 år blir ikkje teken med i utrekninga. Når " +
                    "grunnlaget for stønad er oppfylt etter nasjonale reglar, og " +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " også har opptening av medlemsperiodar i land som Noreg har trygdeavtale med, skal det bli gitt " +
                    "trygdetid etter gunstigaste utrekning: anten berre nasjonal opptening eller samanlagd opptening " +
                    "i Noreg og avtaleland." },
                english { +"To be entitled to full adjustment allowance, " +
                    ifElse(tidligereFamiliepleier, "you", "the deceased") +
                    " must have accumulated at least 40 years of contribution time. Contribution time above 40 years " +
                    "of coverage is not included in the calculation. When the basis for the allowance is met according " +
                    "to national rules, and " +
                    ifElse(tidligereFamiliepleier, "you", "the deceased") +
                    " has also accrued membership periods in countries with " +
                    "which Norway has a national insurance agreement, the contribution time must be stated " +
                    "according to the best calculation of (only) national contribution and of the combined " +
                    "contribution time in Norway and the agreement countries." },
            )
        }
        paragraph {
            text(
                bokmal { +"Ved nasjonal beregning av trygdetid tilsvarer denne det antall år " +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " har vært medlem i folketrygden etter fylte 16 år. " +
                    ifElse(
                        tidligereFamiliepleier,
                        "I tillegg blir det  vanligvis beregnet framtidig trygdetid fra pleieforholdet opphørte fram til og med det året du fyller 66 år.",
                        "Når avdøde var under 67 år ved dødsfallet blir det vanligvis beregnet framtidig trygdetid fram til og med det året avdøde ville ha fylt 66 år.",
                    ) },
                nynorsk { +"Ved nasjonal utrekning av trygdetida vil denne svare til talet på år " +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " var medlem i folketrygda etter fylte 16 år. " +
                    ifElse(
                        tidligereFamiliepleier,
                        "I tillegg blir det vanlegvis rekna ut framtidig trygdetid frå pleieforholdet opphøyrde fram til og med det året du fyller 66 år.",
                        "Dersom personen døydde før fylte 67 år, blir det vanlegvis rekna ut framtidig trygdetid fram til og med det året vedkomande ville ha fylt 66 år.",
                    ) },
                english { +"For calculating national contribution time, this equals the number of years " +
                    ifElse(tidligereFamiliepleier, "you", "the deceased") +
                    " has been a member of the Norwegian National Insurance Scheme after reaching the age of 16. " +
                    ifElse(
                        tidligereFamiliepleier,
                        "In addition, future national insurance coverage is normally calculated from the time when the care period ended through the year you turn 66 years old.",
                        "For deceased persons under 67 years of age at the time of death, the general rule is to calculate future contribution time up to and including the year the deceased would have turned 66.",
                    ) },
            )
        }
        paragraph {
            text(
                bokmal { +"Ved sammenlegging av " +
                    ifElse(tidligereFamiliepleier, "din", "avdødes") +
                    " opptjeningstid i Norge og andre EØS/avtale-land er trygdetiden beregnet etter " +
                    ifElse(tidligereFamiliepleier, "din", "avdødes") +
                    " samlede opptjeningstid i disse landene. For å beregne norsk del av denne trygdetiden ganges " +
                    ifElse(tidligereFamiliepleier, "din", "avdødes") +
                    " samlede opptjeningstid med et forholdstall, som angir forholdet mellom faktisk opptjeningstid " +
                    "i Norge og samlet faktisk opptjeningstid i Norge og andre EØS-land." },
                nynorsk { +"Dersom ein legg saman oppteningstida som " +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " hadde i Noreg og andre EØS-/avtaleland, blir trygdetida rekna ut etter den samla oppteningstida til " +
                    ifElse(tidligereFamiliepleier, "deg", "avdøde") +
                    " i desse landa. For å rekne ut den norske del av denne trygdetida blir den samla oppteningstida til " +
                    ifElse(tidligereFamiliepleier, "deg", "avdøde") +
                    " gonga med eit forholdstal som angir forholdet mellom faktisk oppteningstid i Noreg og samla " +
                    "faktisk oppteningstid i Noreg og andre EØS-land." },
                english { +"For comparing " +
                    ifElse(tidligereFamiliepleier, "your", "the deceased's") +
                    " contribution time in Norway with other EEA/agreement countries, the contribution time is " +
                    "calculated according to " +
                    ifElse(tidligereFamiliepleier, "your", "the deceased's") +
                    " total contribution time in these the countries. To calculate the Norwegian part of this contribution time, " +
                    ifElse(tidligereFamiliepleier, "your", "the deceased's") +
                    " total contribution time is multiplied with a proportional fraction that provides the ratio " +
                    "between the actual contribution time in Norway and the total actual contribution time in " +
                    "Norway and any other EEA or agreement country." },
            )
        }

        showIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
            paragraph {
                text(
                    bokmal { +ifElse(tidligereFamiliepleier, "Din", "Avdødes") +
                        " samlede trygdetid fra avtaleland er beregnet til " +
                        trygdetid.beregnetTrygdetidAar.format() + " år, og forholdstallet til " +
                        trygdetid.prorataBroek.formatBroek() + ". Dette gir den beste beregningen av trygdetid." },
                    nynorsk { +ifElse(tidligereFamiliepleier, "Du", "Avdøde") +
                        " har ei samla trygdetid på " +
                        trygdetid.beregnetTrygdetidAar.format() + " år frå avtaleland, og forholdstalet " +
                        "er " + trygdetid.prorataBroek.formatBroek() + ". Dette gir den gunstigaste utrekninga " +
                        "av trygdetid." },
                    english { +ifElse(tidligereFamiliepleier, "Your", "The deceased's") +
                        " total contribution time from agreement countries " +
                        "amounts to " + trygdetid.beregnetTrygdetidAar.format() + " years, and the proportional " +
                        "fraction of " + trygdetid.prorataBroek.formatBroek() + ". This provides the best " +
                        "calculation for total contribution time." },
                )
            }
        }.orShowIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)) {
            paragraph {
                text(
                    bokmal { +ifElse(tidligereFamiliepleier, "Din", "Avdødes") +
                        " samlede trygdetid er beregnet til " +
                        trygdetid.beregnetTrygdetidAar.format() + " år ved nasjonal opptjening. " +
                        "Dette gir den beste beregningen av trygdetid." },
                    nynorsk { +ifElse(tidligereFamiliepleier, "Du", "Avdøde") +
                        " har ei samla trygdetid på " +
                        trygdetid.beregnetTrygdetidAar.format() + " år ved nasjonal opptening. Dette gir den " +
                        "gunstigaste utrekninga av trygdetid." },
                    english { +ifElse(tidligereFamiliepleier, "Your", "The deceased's") +
                        " total calculated contribution time is " +
                        trygdetid.beregnetTrygdetidAar.format() + " years in national contributions. " +
                        "This provides the best calculation for total contribution time." },
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
            bokmal { +"Perioder med registrert trygdetid" },
            nynorsk { +"Periodar med registrert trygdetid" },
            english { +"Periods of registered contribution time" },
        )
    }

    showIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)) {
        paragraph {
            text(
                bokmal { +"Tabellen viser perioder " +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " har vært medlem av folketrygden, og registrert " +
                    "fremtidig trygdetid." },
                nynorsk { +"Tabellen viser periodar " +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " har vore medlem av folketrygda og registrert " +
                    "framtidig trygdetid. " },
                english { +"The table shows the periods in which " +
                    ifElse(tidligereFamiliepleier, "you", "the deceased") +
                    " was a member of the National Insurance Scheme, and registered future contribution time." },
            )
        }
        includePhrase(Trygdetidstabell(trygdetid.trygdetidsperioder))
    }.orShowIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
        paragraph {
            text(
                bokmal { +"Tabellen viser perioder " +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " har vært medlem av folketrygden og medlemsperioder " +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " har hatt i land som Norge har trygdeavtale med, som er tatt med i beregningen." },
                nynorsk { +"Tabellen viser periodar " +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " har vore medlem av folketrygda og medlemsperiodar " +
                    ifElse(tidligereFamiliepleier, "du", "avdøde") +
                    " har hatt i land som Noreg har trygdeavtale med, som er tekne med i utrekninga." },
                english { +"The table shows periods in which " +
                    ifElse(tidligereFamiliepleier, "you", "the deceased") +
                    " was a member of the National Insurance Scheme and member periods which " +
                    ifElse(tidligereFamiliepleier, "you", "the deceased") +
                    " contributed in countries which Norway had a national insurance agreement which are included in the calculation." },
            )
        }
        includePhrase(Trygdetidstabell(trygdetid.trygdetidsperioder))
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, OmstillingsstoenadBeregning>.meldFraTilNav() {
    paragraph {
        text(
            bokmal { +
                "Hvis du mener at opplysningene brukt i beregningen er feil, må du melde fra til Nav. Det kan ha betydning for størrelsen på omstillingsstønaden din." },
            nynorsk { +
                "Sei frå til Nav dersom du meiner at det er brukt feil opplysningar i utrekninga. Det kan ha betydning for kor mykje omstillingsstønad du får." },
            english { +
                "If you believe the information applied in the calculation is incorrect, you must notify Nav. Errors may affect your allowance amount." },
        )
    }
}
