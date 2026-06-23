package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.redigerbar

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.absoluteValue
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningRedigerbartVedlegg
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningRedigerbartVedleggDataSelectors.erInnvilgelsesAar
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningRedigerbartVedleggDataSelectors.omstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningRedigerbartVedleggSelectors.data
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.opphoerNesteAar
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.oppphoersdato
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.sisteBeregningsperiode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.sisteBeregningsperiodeNesteAar
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.fratrekkInnAar
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.inntekt
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.innvilgaMaaneder
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.oppgittInntekt
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.restanse
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.sanksjon
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.Vedlegg
import no.nav.pensjon.etterlatte.maler.formatAar

@TemplateModelHelpers
object OmstillingsstoenadVedleggBeregningRedigerbartUtfall : EtterlatteTemplate<OmstillingsstoenadBeregningRedigerbartVedlegg>, Vedlegg {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_VEDLEGG_BEREGNING_UTFALL

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Utfall beregning",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"" },
                nynorsk { +"" },
                english { +"" },
            )
        }

        outline {
            val beregning = data.omstillingsstoenadBeregning
            val innvilgelsesAar = data.erInnvilgelsesAar
            val sisteBeregningsperiode = beregning.sisteBeregningsperiode
            val sisteInntekt = sisteBeregningsperiode.inntekt
            val sisteOppgittInntekt = sisteBeregningsperiode.oppgittInntekt
            val sisteFratrekkInnAar = sisteBeregningsperiode.fratrekkInnAar
            val sisteInnvilgaMaaneder = sisteBeregningsperiode.innvilgaMaaneder
            val virkningsdato = beregning.virkningsdato
            val opphoerNesteAar = beregning.opphoerNesteAar
            val oppphoersdato = beregning.oppphoersdato
            val sisteBeregningsperiodeNesteAar = beregning.sisteBeregningsperiodeNesteAar

            showIf(sisteInntekt.greaterThan(0)) {
                paragraph {
                    text(
                        bokmal {
                            +
                            "Stønaden reduseres etter inntekten du har hvert kalenderår. Det er bare inntekt for måneder med innvilget stønad som skal medregnes. Inntekten blir fordelt på antall innvilgede måneder. "
                        },
                        nynorsk {
                            +
                            "Stønaden blir redusert etter inntekta du har kvart kalenderår. Det er berre inntekt for månader med innvilga stønad som skal medreknast. Inntekta blir fordelt på talet på innvilga månader. "
                        },
                        english {
                            +
                            "The allowance is reduced according to your income each calendar year. Only income for months with granted allowance must be included. The income is distributed over the number of months granted. "
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Din oppgitte inntekt for " + virkningsdato.formatAar() + " er " +
                                    sisteOppgittInntekt.format() + "."
                        },

                        nynorsk {
                            +"Du har ei oppgitt inntekt for " + virkningsdato.formatAar() + " på " +
                                    sisteOppgittInntekt.format() + "."
                        },

                        english {
                            +"Your estimated income for " + virkningsdato.formatAar() + " is " +
                                    sisteOppgittInntekt.format() + "."
                        },
                    )
                    showIf(opphoerNesteAar.not()) {
                        ifNotNull(oppphoersdato) { opphoer ->
                            text(
                                bokmal {
                                    +" Dette er forventet inntekt frem til " + opphoer.format() +
                                            ", som er forventet opphørsdato for mottak av stønaden."
                                },

                                nynorsk {
                                    +" Dette er forventa inntekt fram til " + opphoer.format() +
                                            ", som er forventa opphøyrsdato for mottak av stønaden."
                                },

                                english {
                                    +" This is the expected income until " + opphoer.format() +
                                            ", which is the expected end date for receiving the allowance."
                                }
                            )
                        }
                    }
                    showIf(innvilgelsesAar and sisteInnvilgaMaaneder.lessThan(12)) {
                        text(
                            bokmal {
                                +" Fratrekk for inntekt i måneder før du er innvilget stønad er " +
                                        sisteFratrekkInnAar.format() + ". Vi har lagt til grunn at du har en inntekt på " +
                                        sisteInntekt.format() + " i innvilgede måneder dette året."
                            },

                            nynorsk {
                                +" Fråtrekk for inntekt i månader før du er innvilga stønad er " +
                                        sisteFratrekkInnAar.format() + ". Vi har lagt til grunn at du har ei inntekt på " +
                                        sisteInntekt.format() + " i innvilga månader dette året."
                            },

                            english {
                                +" Deduction for income in months before you are granted allowance is " +
                                        sisteFratrekkInnAar.format() + ". We have assumed that you have an income of " +
                                        sisteInntekt.format() + " in granted months this year."
                            },
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
                        bokmal {
                            +"Stønad per måned er redusert på følgende måte: (" + sisteInntekt.format() +
                                    " / " + sisteInnvilgaMaaneder.format() + " måneder) minus (0,5 G / 12 måneder). " +
                                    "Beløpet ganges med 45 prosent."
                        },
                        nynorsk {
                            +"Stønaden per månad har blitt redusert på følgjande måte: (" + sisteInntekt.format() +
                                    " / " + sisteInnvilgaMaaneder.format() + " månader) minus (0,5 G / 12 månader). " +
                                    "Beløpet blir gonga med 45 prosent."
                        },
                        english {
                            +"The monthly allowance amount has been reduced as follows: (" +
                                    sisteInntekt.format() + " / " + sisteInnvilgaMaaneder.format() + " months) minus (0.5 G / 12 months). " +
                                    "The amount is multiplied by 45 percent."
                        },
                    )
                }

                ifNotNull(sisteBeregningsperiodeNesteAar) {
                    val sisteInntektNesteAar = it.inntekt
                    val innvilgaMaanederNesteAar = it.innvilgaMaaneder
                    val sisteInntektNesteAarFom = it.datoFOM

                    paragraph {
                        text(
                            bokmal {
                                +
                                "Fra " + sisteInntektNesteAarFom.format() + " har vi lagt til grunn din oppgitte forventede inntekt på " +
                                        sisteInntektNesteAar.format() + "."
                            },
                            nynorsk {
                                +
                                "Frå " + sisteInntektNesteAarFom.format() + " har vi lagt til grunn den oppgitte forventa inntekta di på " +
                                        sisteInntektNesteAar.format() + "."
                            },
                            english {
                                +
                                "From " + sisteInntektNesteAarFom.format() + ", we have based your stated expected income on " +
                                        sisteInntektNesteAar.format() + "."
                            },
                        )
                        showIf(opphoerNesteAar) {
                            ifNotNull(oppphoersdato) { dato ->
                                text(
                                    bokmal {
                                        +" Dette er forventet inntekt frem til " + dato.format() +
                                                ", som er forventet opphørsdato for mottak av stønaden."
                                    },

                                    nynorsk {
                                        +" Dette er forventa inntekt fram til " + dato.format() +
                                                ", som er forventa opphøyrsdato for mottak av stønaden."
                                    },

                                    english {
                                        +" This is the expected income from the grant until " + dato.format() +
                                                ", which is the expected end date for receiving the allowance."
                                    },
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
                            bokmal {
                                +"Stønad per måned er redusert på følgende måte: (" + sisteInntektNesteAar.format() +
                                        " / " + innvilgaMaanederNesteAar.format() + " måneder) minus (0,5 G / 12 måneder). " +
                                        "Beløpet ganges med 45 prosent."
                            },
                            nynorsk {
                                +"Stønaden per månad har blitt redusert på følgjande måte: (" + sisteInntektNesteAar.format() +
                                        " / " + innvilgaMaanederNesteAar.format() + " månader) minus (0,5 G / 12 månader). " +
                                        "Beløpet blir gonga med 45 prosent."
                            },
                            english {
                                +"The monthly allowance amount has been reduced as follows: (" +
                                        sisteInntektNesteAar.format() + " / " + innvilgaMaanederNesteAar.format() +
                                        " months) minus (0.5 G / 12 months). " +
                                        "The amount is multiplied by 45 percent."
                            },
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
                            bokmal { +ifElse(erRestanseTrekk, "Trekk i utbetalingen", "Tillegg i utbetalingen") },
                            nynorsk { +ifElse(erRestanseTrekk, "Trekk i utbetalinga", "Tillegg i utbetalinga") },
                            english { +ifElse(erRestanseTrekk, "Deduction from payment", "Addition to payment") },
                        )
                    }
                    paragraph {
                        text(
                            bokmal {
                                +"Den forventede inntekten din for inneværende år er blitt justert. " +
                                        "Det blir " + ifElse(erRestanseTrekk, "gjort et trekk", "gitt et tillegg") +
                                        " i utbetalingen for resten av året. Dette blir gjort for å unngå eller redusere et etteroppgjør. " +
                                        ifElse(erRestanseTrekk, "Trekket", "Tillegget") + " er på " + restanse.format() +
                                        " per måned. Dette er medregnet i “Utbetaling per måned”, som fremgår i tabellen over."
                            },
                            nynorsk {
                                +"Den forventa inntekta di for inneverande år har blitt justert. " +
                                        "Det blir " + ifElse(erRestanseTrekk, "gjort eit trekk", "gitt eit tillegg") +
                                        " i utbetalinga for resten av året. Dette blir gjort for å unngå eller redusere eit etteroppgjer. " +
                                        ifElse(erRestanseTrekk, "Trekket", "Tillegget") + " er på " + restanse.format() +
                                        " per månad. Dette er medrekna i “Utbetaling per månad”, som kjem fram i tabellen over."
                            },
                            english {
                                +"Your estimated income for the current year has been adjusted. " +
                                        "Your payment amount has therefore been " + ifElse(erRestanseTrekk, "reduced", "increased") +
                                        " for the remainder of the year to avoid or reduce a final settlement. " +
                                        ifElse(erRestanseTrekk, "The deduction", "Addition") + " is " + restanse.format() +
                                        " per month. This is included in \"Payout per month\", which appears in the table above."
                            },
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
                            bokmal {
                                +"Den forventede inntekten din for inneværende år er blitt justert. " +
                                        "Siden du ikke får utbetalt stønad, kan vi ikke trekke inn for mye utbetalt stønad " +
                                        "i utbetalingene for resten av året. " +
                                        "Feilutbetalt stønad vil derfor bli behandlet i etteroppgjøret for i år, " +
                                        "som blir gjort etter at skatteåret er ferdig lignet neste år."
                            },
                            nynorsk {
                                +"Den forventa inntekta di for inneverande år har vorte justert. " +
                                        "Sidan du ikkje får utbetalt stønad, kan vi ikkje trekkja inn for mykje utbetalt stønad " +
                                        "i utbetalingane for resten av året. " +
                                        "Feilutbetalt stønad vil derfor bli behandla i etteroppgjeret for i år, " +
                                        "som blir gjort etter at skatteåret er ferdig likna neste år."
                            },
                            english {
                                +"Your estimated income for the current year has been adjusted. Since you are " +
                                        "not receiving any allowance for the rest of the year, we cannot deduct any overpaid " +
                                        "benefits from payments during this period. Any overpaid benefits will therefore be " +
                                        "addressed in the final settlement for this year, which will be completed after " +
                                        "the tax settlement for this year is finalized next year."
                            }
                        )
                    }
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal {
                            +"Vi har lagt til grunn at du ikke har arbeidsinntekt eller tilsvarende inntekt som " +
                                    "omstillingsstønaden skal reduseres etter."
                        },
                        nynorsk {
                            +"Vi har lagt til grunn at du ikkje har arbeidsinntekt eller tilsvarande inntekt som " +
                                    "omstillingsstønaden skal reduserast etter. "
                        },
                        english {
                            +"Our calculation shows that you had no employment income or similar income from " +
                                    "which the adjustment allowance can be reduced."
                        },
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
                        bokmal {
                            +"Omstillingsstønaden din er stanset fordi du ikke har oppfylt kravet om aktivitetsplikt. " +
                                    "“Utbetaling per måned” er derfor satt til 0 fra gjeldende tidspunkt, slik det fremgår i tabellen over."
                        },
                        nynorsk {
                            +"Då du ikkje har oppfylt kravet om aktivitetsplikt, har omstillingsstønaden din blitt stansa. " +
                                    "«Utbetaling per månad» er difor sett til 0 frå gjeldande tidspunkt, slik det går fram av tabellen over."
                        },
                        english {
                            +"Your adjustment allowance has been stopped because you have not met the mandatory activity requirements. " +
                                    "«Payment per month is therefore set at «0» from the relevant date, as evident in the table above."
                        },
                    )
                }
            }
        }
    }
}
