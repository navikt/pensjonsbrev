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
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadEtterbetaling
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants

object OmstillingsstoenadInnvilgelseFraser {

    data class Vedtak(
        val avdoed: Expression<Avdoed>,
        val omstillingsstoenadBeregning: Expression<OmstillingsstoenadBeregning>,
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val utbetaltBeloep = omstillingsstoenadBeregning.sisteBeregningsperiode.utbetaltBeloep
            val harFlerePerioder = omstillingsstoenadBeregning.beregningsperioder.size().greaterThan(1)

            paragraph {
                val formatertVirkningsdato = omstillingsstoenadBeregning.virkningsdato.format()
                val formatertDoedsdato = avdoed.doedsdato.format()
                textExpr(
                    Bokmal to "Du er innvilget omstillingsstønad fra ".expr() + formatertVirkningsdato +
                            " fordi " + avdoed.navn + " døde " + formatertDoedsdato + ".",
                    Nynorsk to "Du har fått innvilga omstillingsstønad frå ".expr() + formatertVirkningsdato +
                            ", ettersom " + avdoed.navn + " døydde " + formatertDoedsdato + ".",
                    English to "You have been granted adjustment allowance starting ".expr() +
                            formatertVirkningsdato + " because " + avdoed.navn + " died on " + formatertDoedsdato + "."
                )
            }

            showIf(utbetaltBeloep.greaterThan(0)) {
                showIf(harFlerePerioder) {
                    val datoFomSisteBeregningsperiode = omstillingsstoenadBeregning.sisteBeregningsperiode.datoFOM

                    paragraph {
                        textExpr(
                            Bokmal to "Du får ".expr() + utbetaltBeloep.format() +
                                    " kroner hver måned før skatt fra " + datoFomSisteBeregningsperiode.format() +
                                    ". Se utbetalingsbeløp for tidligere perioder i vedlegget " +
                                    "«Etterbetaling av omstillingsstønad».",
                            Nynorsk to "Du får ".expr() + utbetaltBeloep.format() + " kroner kvar månad før " +
                                    "skatt frå og med " + datoFomSisteBeregningsperiode.format() + ". " +
                                    "Utbetalte beløp for tidlegare periodar står i vedlegget " +
                                    "«Etterbetaling av omstillingsstønad».",
                            English to "You will receive NOK ".expr() + utbetaltBeloep.format() + " each " +
                                    "month before tax, starting on " + datoFomSisteBeregningsperiode.format() + ". " +
                                    "See the payment amount for previous periods in the attachment " +
                                    "Back Payments."
                        )
                    }
                }.orShow {
                    paragraph {
                        textExpr(
                            Bokmal to "Du får ".expr() + utbetaltBeloep.format() + " kroner i stønad hver " +
                                    "måned før skatt.",
                            Nynorsk to "Du får ".expr() + utbetaltBeloep.format() + " kroner i stønad kvar " +
                                    "månad før skatt.".expr(),
                            English to "You will receive NOK ".expr() + utbetaltBeloep.format() + " each " +
                                    "month before tax."
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
                    English to "You can see how we calculated your adjustment allowance in the " +
                            "attachment Calculation of Adjustment Allowance.",
                )
            }
        }
    }

    data class BegrunnelseForVedtaketRedigerbart(
        val etterbetaling: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Omstillingsstønad gis på bakgrunn av at",
                    Nynorsk to "Det blir gitt omstillingsstønad på bakgrunn av at",
                    English to "You have been granted adjustment allowances because",
                )
                list {
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
                textExpr(
                    Bokmal to ("Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven " +
                            "§ 17-2, § 17-3, § 17-4, § 17-5, § 17-6, § 17-9").expr() +
                            ifElse(etterbetaling, ", § 22-12 og § 22-13.", " og § 22-12."),
                    Nynorsk to ("Vedtaket er fatta etter føresegnene om omstillingsstønad i folketrygdlova " +
                            "§§ 17-2, 17-3, 17-4, 17-5, 17-6, 17-9").expr() +
                            ifElse(etterbetaling, ", 22-12 og 22-13.", " og 22-12."),
                    English to ("This decision has been made pursuant to the provisions regarding adjustment " +
                            "allowance in the National Insurance Act – " +
                            "sections 17-2, 17-3, 17-4, 17-5, 17-6, 17-9").expr() +
                            ifElse(etterbetaling, ", 22-12 and 22-13.", " and 22-12."),
                )
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
            paragraph {
                text(
                    Bokmal to "Har du rett til etterbetaling, vil du vanligvis få dette i løpet av tre uker. ",
                    Nynorsk to "Dersom du har rett på etterbetaling, får du vanlegvis denne i løpet av tre veker. ",
                    English to "If you are entitled to a back payment, you will normally receive this within three weeks. ",
                )
                ifNotNull(etterbetaling) {
                    text(
                        Bokmal to "Du finner mer informasjon om etterbetaling i vedlegget " +
                                "«Etterbetaling av omstillingsstønad».",
                        Nynorsk to "Du finn meir informasjon om etterbetaling i vedlegget «Etterbetaling " +
                                "av omstillingsstønad».",
                        English to "You can find more information about back payments in the attachment " +
                                "Back Payments.",
                    )
                }
            }
        }
    }

    object HvaErOmstillingsstoenad : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Hva er omstillingsstønad",
                    Nynorsk to "Kva er omstillingsstønad?",
                    English to "What are adjustment allowance?",
                )
            }
            paragraph {
                text(
                    Bokmal to "Formålet med omstillingsstønaden er å sikre den gjenlevende inntekt og gi hjelp " +
                            "til selvhjelp, slik at den gjenlevende etter en omstillingsperiode etter dødsfallet " +
                            "kan bli i stand til å forsørge seg selv ved eget arbeid.",
                    Nynorsk to "Føremålet med omstillingsstønaden er å sikre den attlevande inntekt og gi " +
                            "hjelp til sjølvhjelp, slik at den attlevande etter ein omstillingsperiode etter " +
                            "dødsfallet kan bli i stand til å forsørgje seg sjølv gjennom eige arbeid.",
                    English to "The purpose of adjustment allowance is to ensure survivors of the deceased a " +
                            "decent income and support self-reliance, so the survivor will be able to support " +
                            "himself through employment after a transitional period following the death of " +
                            "one's spouse.",
                )
            }
        }
    }

    data class Aktivitetsplikt(
        val innvilgetMindreEnnFireMndEtterDoedsfall: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du må være i aktivitet fra seks måneder etter dødsfallet",
                    Nynorsk to "Du må vere i aktivitet når det har gått seks månader sidan dødsfallet",
                    English to "You are obligated to be active starting six months after the death",
                )
            }
            paragraph {
                text(
                    Bokmal to "Det første halvåret etter dødsfallet stilles det ikke krav til at du er i " +
                            "arbeid eller arbeidsrettet aktivitet. Etter seks måneder er det et vilkår for å " +
                            "fortsatt ha rett til omstillingsstønad at du er i minst 50 prosent aktivitet. " +
                            "Dette kalles aktivitetsplikt. Les mer om aktivitetsplikt og hva denne innebærer i vedlegget " +
                            "«Informasjon til deg som mottar omstillingsstønad».",
                    Nynorsk to "Det første halvåret etter dødsfallet blir det ikkje stilt krav til at du er i " +
                            "arbeid eller arbeidsretta aktivitet. For at du framleis skal ha rett på " +
                            "omstillingsstønad etter seks månader, må du vere i minst 50 prosent aktivitet. " +
                            "Dette blir kalla aktivitetsplikt.  I vedlegget «Informasjon til deg som får " +
                            "omstillingsstønad» kan du lese meir om aktivitetsplikta og kva denne inneber.",
                    English to "There is no obligation to work or employment-related activities for the first " +
                            "six months after the death. After six months have passed, one condition for continuing " +
                            "to receive adjustment allowance is that you are at least 50 percent active. This is " +
                            "called the Activity Obligation.  Read more about the activity obligation and what this " +
                            "involves in the attachment Information to Recipients of Adjustment Allowance.",
                )
            }

            showIf(innvilgetMindreEnnFireMndEtterDoedsfall) {
                paragraph {
                    text(
                        Bokmal to "Vi sender deg et eget informasjonsbrev om at du må være i aktivitet når det " +
                                "nærmer seg seks måneder etter dødsfallet.",
                        Nynorsk to "Vi sender deg eit eige informasjonsbrev om at du må vere i aktivitet når " +
                                "det nærmar seg seks månader sidan dødsfallet.   ",
                        English to "We will send you a separate letter to inform you of your activity " +
                                "obligation near the end of the six-month period.",
                    )
                }
            }
        }
    }
}
