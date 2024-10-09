package no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.expression.size
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.doedsdato
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.navn
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.sisteBeregningsperiode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.sisteBeregningsperiodeNesteAar
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadEtterbetaling
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants

object OmstillingsstoenadInnvilgelseFraser {
    data class Vedtak(
        val avdoed: Expression<Avdoed>,
        val omstillingsstoenadBeregning: Expression<OmstillingsstoenadBeregning>,
        val harUtbetaling: Expression<Boolean>,
        val tidligereFamiliepleier: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val harFlerePerioder = omstillingsstoenadBeregning.beregningsperioder.size().greaterThan(1)

            paragraph {
                val formatertVirkningsdato = omstillingsstoenadBeregning.virkningsdato.format()
                val formatertDoedsdato = avdoed.doedsdato.format()
                showIf(tidligereFamiliepleier) {
                    textExpr(
                        Bokmal to "Du er innvilget omstillingsstønad fra ".expr() + formatertVirkningsdato +
                            " på bakgrunn av at du har pleiet et nært familiemedlem.",
                        Nynorsk to "Fordi du har pleidd eit nært familiemedlem, er du innvilga omstillingsstønad frå ".expr() +
                            formatertVirkningsdato + ".",
                        English to "You have been granted adjustment allowance from ".expr() +
                            formatertVirkningsdato + " based on caring for a close family member.",
                    )
                }.orShow {
                    textExpr(
                        Bokmal to "Du er innvilget omstillingsstønad fra ".expr() + formatertVirkningsdato +
                            " fordi " + avdoed.navn + " døde " + formatertDoedsdato + ".",
                        Nynorsk to "Du har fått innvilga omstillingsstønad frå ".expr() + formatertVirkningsdato +
                            ", ettersom " + avdoed.navn + " døydde " + formatertDoedsdato + ".",
                        English to "You have been granted adjustment allowance starting ".expr() +
                            formatertVirkningsdato + " because " + avdoed.navn + " died on " + formatertDoedsdato + ".",
                    )
                }
            }

            showIf(harUtbetaling) {
                val sisteUtbetaltBeloep = omstillingsstoenadBeregning.sisteBeregningsperiode.utbetaltBeloep
                val datoFomSisteBeregningsperiode = omstillingsstoenadBeregning.sisteBeregningsperiode.datoFOM

                showIf(harFlerePerioder) {
                    ifNotNull(omstillingsstoenadBeregning.sisteBeregningsperiodeNesteAar) {
                        paragraph {
                            textExpr(
                                Bokmal to
                                    "Du får ".expr() + sisteUtbetaltBeloep.format() +
                                    " kroner hver måned før skatt fra " + datoFomSisteBeregningsperiode.format() + ". Fra ".expr() +
                                    it.datoFOM.format() + " får du " +
                                    it.utbetaltBeloep.format() +
                                    " kroner hver måned før skatt.",
                                Nynorsk to
                                    "Du får ".expr() + sisteUtbetaltBeloep.format() + " kroner kvar månad før " +
                                    "skatt frå og med " + datoFomSisteBeregningsperiode.format() + ". Frå og med ".expr() +
                                    it.datoFOM.format() + " får du " +
                                    it.utbetaltBeloep.format() + " kroner kvar månad før skatt. ",
                                English to
                                    "You will receive NOK ".expr() + sisteUtbetaltBeloep.format() + " each " +
                                    "month before tax, starting on " + datoFomSisteBeregningsperiode.format() +
                                    ". Starting from ".expr() + it.datoFOM.format() + ", you will receive NOK " +
                                    it.utbetaltBeloep.format() + " each month before tax.",
                            )
                        }
                    }.orShow {
                        paragraph {
                            textExpr(
                                Bokmal to "Du får ".expr() + sisteUtbetaltBeloep.format() +
                                    " kroner hver måned før skatt fra " + datoFomSisteBeregningsperiode.format() + ".",
                                Nynorsk to "Du får ".expr() + sisteUtbetaltBeloep.format() + " kroner kvar månad før " +
                                    "skatt frå og med " + datoFomSisteBeregningsperiode.format() + ". ",
                                English to "You will receive NOK ".expr() + sisteUtbetaltBeloep.format() + " each " +
                                    "month before tax, starting on " + datoFomSisteBeregningsperiode.format() + ".",
                            )
                        }
                    }
                }.orShow {
                    paragraph {
                        textExpr(
                            Bokmal to "Du får ".expr() + sisteUtbetaltBeloep.format() + " kroner i stønad hver " +
                                "måned før skatt.",
                            Nynorsk to "Du får ".expr() + sisteUtbetaltBeloep.format() + " kroner i stønad kvar " +
                                "månad før skatt.".expr(),
                            English to "You will receive NOK ".expr() + sisteUtbetaltBeloep.format() + " each " +
                                "month before tax.",
                        )
                    }
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Du får ikke utbetalt omstillingsstønad fordi du har inntekt som er høyere " +
                            "enn grensen for å få utbetalt stønaden.",
                        Nynorsk to "Du får ikkje utbetalt omstillingsstønad, då inntekta di er over " +
                            "maksimumsgrensa for å kunne få stønad.",
                        English to "You will not receive adjustment allowance because your income is " +
                            "higher than the limit for receiving such allowance.",
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Se hvordan vi har beregnet omstillingsstønaden din i vedlegget «Beregning av " +
                        "omstillingsstønad».",
                    Nynorsk to "Du kan sjå i vedlegget «Utrekning av omstillingsstønad» korleis vi har " +
                        "rekna ut omstillingsstønaden din.",
                    English to
                        "You can see how we calculated your adjustment allowance in the attachment: Calculation of adjustment allowance.",
                )
            }
        }
    }

    data class BegrunnelseForVedtaketRedigerbart(
        val etterbetaling: Expression<Boolean>,
        val tidligereFamiliepleier: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Omstillingsstønad gis på bakgrunn av at",
                    Nynorsk to "Det blir gitt omstillingsstønad på bakgrunn av at",
                    English to "You have been granted adjustment allowances because",
                )

                list {
                    showIf(tidligereFamiliepleier) {
                        item {
                            text(
                                Bokmal to "du har pleid en nærstående i minst fem år",
                                Nynorsk to "du har pleidd ein nærståande person i minst fem år",
                                English to "caring for a close family member for a period of at least five years",
                            )
                        }
                        item {
                            text(
                                Bokmal to "du er og har vært medlem i trygden i minst fem år",
                                Nynorsk to "du er og har vore medlem i folketrygda i minst fem år",
                                English to "you are a member of Norwegian national insurance and have been for at least five years",
                            )
                        }
                        item {
                            text(
                                Bokmal to "du er og har vært ugift i minst fem år under pleieforholdet",
                                Nynorsk to "du er og har vore ugift i minst fem år under pleieforholdet",
                                English to "you are not married and was not married for at least five years of the care period",
                            )
                        }
                        item {
                            text(
                                Bokmal to "den du har pleid har mottatt pensjon fra folketrygden eller vært medlem i " +
                                    "trygden i minst fem år frem til pleieforholdet opphørte",
                                Nynorsk to "den du har pleidd, har fått pensjon frå eller vore medlem av " +
                                    "folketrygda i minst fem år fram til pleieforholdet opphøyrde",
                                English to "the person you have been caring for has received a pension from " +
                                    "Norwegian national insurance or had Norwegian national insurance coverage for " +
                                    "at least five years until the care period ended",
                            )
                        }
                        item {
                            text(
                                Bokmal to "du har ikke vært i stand til å forsørge deg selv med eget arbeid på grunn av pleieforholdet",
                                Nynorsk to
                                    "du har ikkje vore i stand til å forsørgje deg sjølv gjennom eige arbeid på grunn av pleieforholdet",
                                English to "you have not been able to care for yourself through employment because of the care you provided",
                            )
                        }
                        item {
                            text(
                                Bokmal to "du har ikke hatt mulighet for å forsørge deg selv etter at pleieforholdet opphørte",
                                Nynorsk to "det ikkje har vore mogleg for deg å forsørgje deg sjølv etter at pleieforholdet opphøyrde",
                                English to "you have not been able to care for yourself since the care period ended",
                            )
                        }
                        item {
                            text(
                                Bokmal to "du ikke har tilstrekkelige midler til livsopphold",
                                Nynorsk to "du ikkje har midlar nok til livsopphald",
                                English to "you do not have sufficient funds to support yourself",
                            )
                        }
                    }.orShow {
                        item {
                            text(
                                Bokmal to "du som gjenlevende ektefelle på dødsfallstidspunktet hadde vært gift " +
                                    "med avdøde i minst fem år, har eller har hatt barn med avdøde, eller har " +
                                    "omsorgen for barn under 18 år med minst halvparten av full tid",
                                Nynorsk to "du som attlevande ektefelle på tidspunktet for dødsfallet hadde vore " +
                                    "gift med avdøde i minst fem år, har eller har hatt barn med avdøde, eller har " +
                                    "omsorga for barn under 18 år med minst halvparten av full tid",
                                English to "as a surviving spouse at the time of the death, you were married to " +
                                    "the deceased at least five years, have or had a child with the deceased, or " +
                                    "had care for a child under the age of 18 at least half the time in which",
                            )
                        }
                        item {
                            text(
                                Bokmal to "du er medlem i folketrygden",
                                Nynorsk to "du er medlem i folketrygda",
                                English to "you were a member of the national insurance scheme",
                            )
                        }
                        item {
                            text(
                                Bokmal to "avdøde i de siste fem årene før dødsfallet var medlem i folketrygden, " +
                                    "eller fikk pensjon eller uføretrygd fra folketrygden.",
                                Nynorsk to "avdøde i dei siste fem åra fram til sin død var medlem i folketrygda, " +
                                    "eller fekk pensjon eller uføretrygd frå folketrygda",
                                English to "in the last five years before his or her death, the deceased was a " +
                                    "member of the national insurance scheme, or received a pension or disability " +
                                    "pension from the scheme",
                            )
                        }
                    }
                }
            }

            showIf(tidligereFamiliepleier) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven §§ " +
                            "17-15, 22-12 og 22-13.",
                        Nynorsk to "Vedtaket er fatta etter føresegnene om omstillingsstønad i folketrygdlova " +
                            "§§ 17-15, 22-12 og 22-13.",
                        English to "This decision has been made pursuant to the provisions regarding adjustment " +
                            "allowance in the National Insurance Act – " +
                            "sections 17-15, 22-12 and 22-13.",
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Samboere med felles barn og samboere som tidligere har vært gift likestilles " +
                            "med ektefeller.",
                        Nynorsk to "Sambuarar med felles barn og sambuarar som tidlegare har vore gifte, blir " +
                            "rekna som likestilte med ektefellar.",
                        English to "Cohabiting partners with common children and cohabiting partners who were " +
                            "previously married to the deceased are considered spouses.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven §§ " +
                            "17-2, 17-3, 17-4, 17-5, 17-6, 17-9, 22-12 og 22-13.",
                        Nynorsk to "Vedtaket er fatta etter føresegnene om omstillingsstønad i folketrygdlova " +
                            "§§ 17-2, 17-3, 17-4, 17-5, 17-6, 17-9, 22-12 og 22-13.",
                        English to "This decision has been made pursuant to the provisions regarding adjustment " +
                            "allowance in the National Insurance Act – " +
                            "sections 17-2, 17-3, 17-4, 17-5, 17-6, 17-9, 22-12 and 22-13.",
                    )
                }
            }
        }
    }

    data class Utbetaling(
        val etterbetaling: Expression<OmstillingsstoenadEtterbetaling?>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Utbetaling av omstillingsstønad",
                    Nynorsk to "Utbetaling av omstillingsstønad",
                    English to "Payment of adjustment allowance",
                )
            }
            paragraph {
                text(
                    Bokmal to "Omstillingsstønad blir utbetalt innen den 20. i hver måned. " +
                        "Du finner utbetalingsdatoer på ${Constants.UTBETALING_URL}.",
                    Nynorsk to "Omstillingsstønad blir utbetalt innan den 20. i kvar månad. Du finn " +
                        "utbetalingsdatoar på ${Constants.UTBETALING_URL}.",
                    English to "Adjustment allowance are paid on or before the 20th of each month. You can " +
                        "find payout dates online: ${Constants.UTBETALING_URL}.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Utbetalingen kan bli forsinket hvis den skal samordnes med ytelser du mottar " +
                        "fra NAV eller andre, som for eksempel tjenestepensjonsordninger.",
                    Nynorsk to "Utbetalinga kan bli forseinka dersom ho skal samordnast med ytingar du får " +
                        "frå NAV eller andre (t.d. tenestepensjonsordningar).",
                    English to "The payment may be delayed if they are coordination with benefits you receive " +
                        "from NAV or others, such as a pension from an occupational pension scheme.",
                )
            }
            ifNotNull(etterbetaling) {
                paragraph {
                    text(
                        Bokmal to "Du får etterbetalt stønad. Vanligvis vil du få denne i løpet av " +
                            "tre uker. Hvis Skatteetaten eller andre ordninger har krav i etterbetalingen kan " +
                            "denne bli forsinket. Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen.",
                        Nynorsk to "Du får etterbetalt stønad. Vanlegvis vil du få denne i løpet av " +
                            "tre veker. Dersom Skatteetaten eller andre ordningar har krav i etterbetalinga, " +
                            "kan ho bli forseinka. Frådrag i etterbetalinga vil gå fram av utbetalingsmeldinga. ",
                        English to "You will receive a back payment on your allowance. You will usually " +
                            "receive this back payment within three weeks. If the Norwegian Tax Administration " +
                            "or other schemes are entitled to the back payment, the payment to you may be " +
                            "delayed. Deductions from the back payment will be stated in the disbursement notice.  ",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Det trekkes vanligvis skatt av etterbetaling. Gjelder " +
                            "etterbetalingen tidligere år trekker NAV skatt etter Skatteetatens standardsatser. " +
                            "Du kan lese mer om satsene på ${Constants.SKATTETREKK_ETTERBETALING_URL}.",
                        Nynorsk to "Det blir normalt sett bli trekt skatt av etterbetaling. Dersom " +
                            "etterbetalinga gjeld tidlegare år, vil NAV trekkje skatt etter standardsatsane til " +
                            "Skatteetaten. Du kan lese meir om satsane på ${Constants.SKATTETREKK_ETTERBETALING_URL}.",
                        English to "Tax is usually deducted from back payments. If the back payment " +
                            "applies to previous years, NAV will deduct the tax at the Tax Administration's " +
                            "standard rates. You can read more about the rates here: " +
                            "${Constants.SKATTETREKK_ETTERBETALING_URL}. ",
                    )
                }
            }
        }
    }

    data class HvaErOmstillingsstoenad(
        val tidligereFamiliepleier: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Hva er omstillingsstønad?",
                    Nynorsk to "Kva er omstillingsstønad?",
                    English to "What are adjustment allowance?",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Formålet med omstillingsstønaden er å sikre deg inntekt og gi hjelp ".expr() +
                        "til selvhjelp, slik at du etter en omstillingsperiode etter " +
                        ifElse(tidligereFamiliepleier, "at pleieforholdet opphørte,", "dødsfallet") +
                        " kan bli i stand til å forsørge deg selv ved eget arbeid.",
                    Nynorsk to "Føremålet med omstillingsstønaden er å sikre deg inntekt og gi ".expr() +
                        "hjelp til sjølvhjelp, slik at du etter ein omstillingsperiode etter " +
                        ifElse(tidligereFamiliepleier, "at pleieforholdet opphøyrde", "dødsfallet") +
                        " kan bli i stand til å forsørgje deg sjølv gjennom eige arbeid.",
                    English to "The purpose of adjustment allowance is to ".expr() +
                        ifElse(
                            tidligereFamiliepleier,
                            "secure your income and provide help for self-help, so that after a transition " +
                                "period after the care relationship has ended, you can be able to support " +
                                "yourself through your own work.",
                            "ensure you a decent income and support self-reliance, so you will be able to " +
                                "support yourself himself through employment after a transitional period " +
                                "following the death of one's spouse.",
                        ),
                )
            }
        }
    }

    data class Aktivitetsplikt(
        val innvilgetMindreEnnFireMndEtterDoedsfall: Expression<Boolean>,
        val tidligereFamiliepleier: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                textExpr(
                    Bokmal to "Du må være i aktivitet fra seks måneder etter ".expr() +
                        ifElse(tidligereFamiliepleier, "pleieforholdet opphørte", "dødsfallet"),
                    Nynorsk to "Du må vere i aktivitet når det har gått seks månader sidan ".expr() +
                        ifElse(tidligereFamiliepleier, "pleieforholdet opphøyrde", "dødsfallet"),
                    English to "You are obligated to be active starting six months after the ".expr() +
                        ifElse(tidligereFamiliepleier, "after care period ended", "death"),
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Det første halvåret etter ".expr() +
                        ifElse(tidligereFamiliepleier, "pleieforholdet opphørte", "dødsfallet") +
                        " stilles det ikke krav til at du er i " +
                        "arbeid eller arbeidsrettet aktivitet. Etter seks måneder er det et vilkår for å " +
                        "fortsatt ha rett til omstillingsstønad at du er i minst 50 prosent aktivitet. " +
                        "Dette kalles aktivitetsplikt. Les mer om aktivitetsplikt og hva denne innebærer i vedlegget " +
                        "«Informasjon til deg som mottar omstillingsstønad».",
                    Nynorsk to "Det første halvåret etter ".expr() +
                        ifElse(tidligereFamiliepleier, "pleieforholdet opphøyrde", "dødsfallet") +
                        " blir det ikkje stilt krav til at du er i " +
                        "arbeid eller arbeidsretta aktivitet. For at du framleis skal ha rett på " +
                        "omstillingsstønad etter seks månader, må du vere i minst 50 prosent aktivitet. " +
                        "Dette blir kalla aktivitetsplikt.  I vedlegget «Informasjon til deg som får " +
                        "omstillingsstønad» kan du lese meir om aktivitetsplikta og kva denne inneber.",
                    English to "There is no obligation to work or employment-related activities for the first ".expr() +
                        "six months after the " +
                        ifElse(tidligereFamiliepleier, "care period ended", "death") +
                        ". After six months have passed, one condition for continuing " +
                        "to receive adjustment allowance is that you are at least 50 percent active. This is " +
                        "called the Activity Obligation.  Read more about the activity obligation and what this " +
                        "involves in the attachment Information to Recipients of adjustment allowance.",
                )
            }

            showIf(innvilgetMindreEnnFireMndEtterDoedsfall) {
                paragraph {
                    textExpr(
                        Bokmal to "Vi sender deg et eget informasjonsbrev om at du må være i aktivitet når det ".expr() +
                            "nærmer seg seks måneder etter " +
                            ifElse(tidligereFamiliepleier, "pleieforholdet opphørte.", "dødsfallet."),
                        Nynorsk to "Vi sender deg eit eige informasjonsbrev om at du må vere i aktivitet når ".expr() +
                            "det nærmar seg seks månader sidan " +
                            ifElse(tidligereFamiliepleier, "pleieforholdet vart avslutta.", "dødsfallet."),
                        English to "We will send you a separate letter to inform you of your activity ".expr() +
                            "obligation near the end of the six-month period.",
                    )
                }
            }
        }
    }
}
