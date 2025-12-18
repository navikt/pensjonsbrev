package no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.expression.size
import no.nav.pensjon.brev.template.dsl.text
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

    data class Utbetaling(
        val harUtbetaling: Expression<Boolean>,
        val omstillingsstoenadBeregning: Expression<OmstillingsstoenadBeregning>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(harUtbetaling) {
                val harFlerePerioder = omstillingsstoenadBeregning.beregningsperioder.size().greaterThan(1)
                val sisteUtbetaltBeloep = omstillingsstoenadBeregning.sisteBeregningsperiode.utbetaltBeloep
                val datoFomSisteBeregningsperiode = omstillingsstoenadBeregning.sisteBeregningsperiode.datoFOM

                showIf(harFlerePerioder) {
                    ifNotNull(omstillingsstoenadBeregning.sisteBeregningsperiodeNesteAar) {
                        paragraph {
                            text(
                                bokmal { +
                                        "Du får " + sisteUtbetaltBeloep.format() +
                                        " hver måned før skatt fra " + datoFomSisteBeregningsperiode.format() + ". Fra " +
                                        it.datoFOM.format() + " får du " +
                                        it.utbetaltBeloep.format() +
                                        " hver måned før skatt." },
                                nynorsk { +
                                        "Du får " + sisteUtbetaltBeloep.format() + " kvar månad før " +
                                        "skatt frå og med " + datoFomSisteBeregningsperiode.format() + ". Frå og med " +
                                        it.datoFOM.format() + " får du " +
                                        it.utbetaltBeloep.format() + " kvar månad før skatt. " },
                                english { +
                                        "You will receive " + sisteUtbetaltBeloep.format() + " each " +
                                        "month before tax, starting on " + datoFomSisteBeregningsperiode.format() +
                                        ". Starting from " + it.datoFOM.format() + ", you will receive " +
                                        it.utbetaltBeloep.format() + " each month before tax." },
                            )
                        }
                    }.orShow {
                        paragraph {
                            text(
                                bokmal { +"Du får " + sisteUtbetaltBeloep.format() +
                                        " hver måned før skatt fra " + datoFomSisteBeregningsperiode.format() + "." },
                                nynorsk { +"Du får " + sisteUtbetaltBeloep.format() + " kvar månad før " +
                                        "skatt frå og med " + datoFomSisteBeregningsperiode.format() + ". " },
                                english { +"You will receive " + sisteUtbetaltBeloep.format() + " each " +
                                        "month before tax, starting on " + datoFomSisteBeregningsperiode.format() + "." },
                            )
                        }
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal { +"Du får " + sisteUtbetaltBeloep.format() + " i stønad hver " +
                                    "måned før skatt." },
                            nynorsk { +"Du får " + sisteUtbetaltBeloep.format() + " i stønad kvar " +
                                    "månad før skatt." },
                            english { +"You will receive " + sisteUtbetaltBeloep.format() + " each " +
                                    "month before tax." },
                        )
                    }
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { +"Du får ikke utbetalt omstillingsstønad fordi du har inntekt som er høyere " +
                                "enn grensen for å få utbetalt stønaden." },
                        nynorsk { +"Du får ikkje utbetalt omstillingsstønad, då inntekta di er over " +
                                "maksimumsgrensa for å kunne få stønad." },
                        english { +"You will not receive adjustment allowance because your income is " +
                                "higher than the limit for receiving such allowance." },
                    )
                }
            }

            paragraph {
                text(
                    bokmal { +"Se hvordan vi har beregnet omstillingsstønaden din i vedlegget «Beregning av " +
                            "omstillingsstønad»." },
                    nynorsk { +"Du kan sjå i vedlegget «Utrekning av omstillingsstønad» korleis vi har " +
                            "rekna ut omstillingsstønaden din." },
                    english { +
                            "You can see how we calculated your adjustment allowance in the attachment: Calculation of adjustment allowance." },
                )
            }
        }
    }


    data class Vedtak(
        val avdoed: Expression<Avdoed?>,
        val omstillingsstoenadBeregning: Expression<OmstillingsstoenadBeregning>,
        val harUtbetaling: Expression<Boolean>,
        val tidligereFamiliepleier: Expression<Boolean>,
        val erSluttbehandling: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val formatertVirkningsdato = omstillingsstoenadBeregning.virkningsdato.format()

            showIf(tidligereFamiliepleier) {
                paragraph {
                    text(
                        bokmal { +"Du er innvilget omstillingsstønad fra " + formatertVirkningsdato +
                                " på bakgrunn av at du har pleiet et nært familiemedlem." },
                        nynorsk { +"Fordi du har pleidd eit nært familiemedlem, er du innvilga omstillingsstønad frå " +
                                formatertVirkningsdato + "." },
                        english { +"You have been granted adjustment allowance from " +
                                formatertVirkningsdato + " based on caring for a close family member." },
                    )
                }
            }.orShowIf(erSluttbehandling) {
                paragraph {
                    text(
                        bokmal { +
                                "Du har tidligere fått et foreløpig avslag på søknaden din om omstillingsstønad fordi du ikke hadde rett på stønaden kun vurdert etter nasjonale regler. Avslaget var gitt i påvente av opplysninger fra utenlandske trygdemyndigheter." },
                        nynorsk { +
                                "Du har tidlegare fått eit foreløpig avslag på søknaden din om omstillingsstønad fordi du ikkje hadde rett på stønaden berre vurdert etter nasjonale reglar. Avslaget var gitt i påvente av opplysningar frå utanlandske trygdemyndigheiter." },
                        english { +
                                "You previously received a preliminary rejection of your application for adjustment allowance because you were assessed only according to national rules, which did not entitle you to the allowance. The rejection was issued pending information from foreign social security authorities." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +
                                "Vi har nå mottatt opplysninger fra utenlandske trygdemyndigheter, som gjør at du har rett på stønaden etter EØS/avtalelandreglene." },
                        nynorsk { +
                                "Vi har no mottatt opplysningar frå utanlandske trygdemyndigheiter, som gjer at du har rett på stønaden etter EØS/avtalelandreglane." },
                        english { +
                                "We have now received information from foreign social security authorities, which means you are entitled to the allowance under the EEA/agreement country rules." },
                    )
                }
            }

            showIf(tidligereFamiliepleier.not()) {
                ifNotNull(avdoed){
                    paragraph {
                        text(
                            bokmal { +"Du er innvilget omstillingsstønad fra " + formatertVirkningsdato +
                                    " fordi " + it.navn + " døde " + it.doedsdato.format() + "." },
                            nynorsk { +"Du har fått innvilga omstillingsstønad frå " + formatertVirkningsdato +
                                    ", ettersom " + it.navn + " døydde " + it.doedsdato.format() + "." },
                            english { +"You have been granted adjustment allowance starting " +
                                    formatertVirkningsdato + " because " + it.navn + " died on " + it.doedsdato.format() + "." },
                        )
                    }
                }
            }

            includePhrase(Utbetaling(harUtbetaling, omstillingsstoenadBeregning))
        }
    }

    data class BegrunnelseForVedtaketRedigerbart(
        val etterbetaling: Expression<Boolean>,
        val tidligereFamiliepleier: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal { +"Omstillingsstønad gis på bakgrunn av at" },
                    nynorsk { +"Det blir gitt omstillingsstønad på bakgrunn av at" },
                    english { +"You have been granted adjustment allowances because" },
                )

                list {
                    showIf(tidligereFamiliepleier) {
                        item {
                            text(
                                bokmal { +"du har pleid en nærstående i minst fem år" },
                                nynorsk { +"du har pleidd ein nærståande person i minst fem år" },
                                english { +"caring for a close family member for a period of at least five years" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"du er og har vært medlem i trygden i minst fem år" },
                                nynorsk { +"du er og har vore medlem i folketrygda i minst fem år" },
                                english { +"you are a member of Norwegian national insurance and have been for at least five years" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"du er og har vært ugift i minst fem år under pleieforholdet" },
                                nynorsk { +"du er og har vore ugift i minst fem år under pleieforholdet" },
                                english { +"you are not married and was not married for at least five years of the care period" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"den du har pleid har mottatt pensjon fra folketrygden eller vært medlem i " +
                                    "trygden i minst fem år frem til pleieforholdet opphørte" },
                                nynorsk { +"den du har pleidd, har fått pensjon frå eller vore medlem av " +
                                    "folketrygda i minst fem år fram til pleieforholdet opphøyrde" },
                                english { +"the person you have been caring for has received a pension from " +
                                    "Norwegian national insurance or had Norwegian national insurance coverage for " +
                                    "at least five years until the care period ended" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"du har ikke vært i stand til å forsørge deg selv med eget arbeid på grunn av pleieforholdet" },
                                nynorsk { +
                                    "du har ikkje vore i stand til å forsørgje deg sjølv gjennom eige arbeid på grunn av pleieforholdet" },
                                english { +"you have not been able to care for yourself through employment because of the care you provided" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"du har ikke hatt mulighet for å forsørge deg selv etter at pleieforholdet opphørte" },
                                nynorsk { +"det ikkje har vore mogleg for deg å forsørgje deg sjølv etter at pleieforholdet opphøyrde" },
                                english { +"you have not been able to care for yourself since the care period ended" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"du ikke har tilstrekkelige midler til livsopphold" },
                                nynorsk { +"du ikkje har midlar nok til livsopphald" },
                                english { +"you do not have sufficient funds to support yourself" },
                            )
                        }
                    }.orShow {
                        item {
                            text(
                                bokmal { +"du som gjenlevende ektefelle på dødsfallstidspunktet hadde vært gift " +
                                    "med avdøde i minst fem år, har eller har hatt barn med avdøde, eller har " +
                                    "omsorgen for barn under 18 år med minst halvparten av full tid" },
                                nynorsk { +"du som attlevande ektefelle på tidspunktet for dødsfallet hadde vore " +
                                    "gift med avdøde i minst fem år, har eller har hatt barn med avdøde, eller har " +
                                    "omsorga for barn under 18 år med minst halvparten av full tid" },
                                english { +"as a surviving spouse at the time of the death, you were married to " +
                                    "the deceased at least five years, have or had a child with the deceased, or " +
                                    "had care for a child under the age of 18 at least half the time in which" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"du er medlem i folketrygden" },
                                nynorsk { +"du er medlem i folketrygda" },
                                english { +"you were a member of the national insurance scheme" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"avdøde i de siste fem årene før dødsfallet var medlem i folketrygden, " +
                                    "eller fikk pensjon eller uføretrygd fra folketrygden" },
                                nynorsk { +"avdøde i dei siste fem åra fram til sin død var medlem i folketrygda, " +
                                    "eller fekk pensjon eller uføretrygd frå folketrygda" },
                                english { +"in the last five years before his or her death, the deceased was a " +
                                    "member of the national insurance scheme, or received a pension or disability " +
                                    "pension from the scheme" },
                            )
                        }
                    }
                }
            }

            showIf(tidligereFamiliepleier) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven §§ " +
                            "17-15, 22-12 og 22-13." },
                        nynorsk { +"Vedtaket er fatta etter føresegnene om omstillingsstønad i folketrygdlova " +
                            "§§ 17-15, 22-12 og 22-13." },
                        english { +"This decision has been made pursuant to the provisions regarding adjustment " +
                            "allowance in the National Insurance Act – " +
                            "sections 17-15, 22-12 and 22-13." },
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { +"Samboere med felles barn og samboere som tidligere har vært gift likestilles " +
                            "med ektefeller." },
                        nynorsk { +"Sambuarar med felles barn og sambuarar som tidlegare har vore gifte, blir " +
                            "rekna som likestilte med ektefellar." },
                        english { +"Cohabiting partners with common children and cohabiting partners who were " +
                            "previously married to the deceased are considered spouses." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven §§ " +
                            "17-2, 17-3, 17-4, 17-5, 17-6, 17-9, 22-12 og 22-13." },
                        nynorsk { +"Vedtaket er fatta etter føresegnene om omstillingsstønad i folketrygdlova " +
                            "§§ 17-2, 17-3, 17-4, 17-5, 17-6, 17-9, 22-12 og 22-13." },
                        english { +"This decision has been made pursuant to the provisions regarding adjustment " +
                            "allowance in the National Insurance Act – " +
                            "sections 17-2, 17-3, 17-4, 17-5, 17-6, 17-9, 22-12 and 22-13." },
                    )
                }
            }
        }
    }

    data class UtbetalingMedEtterbetaling(
        val etterbetaling: Expression<OmstillingsstoenadEtterbetaling?>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Utbetaling av omstillingsstønad" },
                    nynorsk { +"Utbetaling av omstillingsstønad" },
                    english { +"Payment of adjustment allowance" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Omstillingsstønad blir utbetalt innen den 20. i hver måned. " +
                        "Du finner utbetalingsdatoer på ${Constants.UTBETALING_URL}." },
                    nynorsk { +"Omstillingsstønad blir utbetalt innan den 20. i kvar månad. Du finn " +
                        "utbetalingsdatoar på ${Constants.UTBETALING_URL}." },
                    english { +"Adjustment allowance are paid on or before the 20th of each month. You can " +
                        "find payout dates online: ${Constants.UTBETALING_URL}." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Utbetalingen kan bli forsinket hvis den skal samordnes med ytelser du mottar " +
                        "fra Nav eller andre, som for eksempel tjenestepensjonsordninger." },
                    nynorsk { +"Utbetalinga kan bli forseinka dersom ho skal samordnast med ytingar du får " +
                        "frå Nav eller andre (t.d. tenestepensjonsordningar)." },
                    english { +"The payment may be delayed if they are coordination with benefits you receive " +
                        "from Nav or others, such as a pension from an occupational pension scheme." },
                )
            }
            ifNotNull(etterbetaling) {
                paragraph {
                    text(
                        bokmal { +"Du får etterbetalt stønad. Vanligvis vil du få denne i løpet av " +
                            "tre uker. Hvis Skatteetaten eller andre ordninger har krav i etterbetalingen kan " +
                            "denne bli forsinket. Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen." },
                        nynorsk { +"Du får etterbetalt stønad. Vanlegvis vil du få denne i løpet av " +
                            "tre veker. Dersom Skatteetaten eller andre ordningar har krav i etterbetalinga, " +
                            "kan ho bli forseinka. Frådrag i etterbetalinga vil gå fram av utbetalingsmeldinga. " },
                        english { +"You will receive a back payment on your allowance. You will usually " +
                            "receive this back payment within three weeks. If the Norwegian Tax Administration " +
                            "or other schemes are entitled to the back payment, the payment to you may be " +
                            "delayed. Deductions from the back payment will be stated in the disbursement notice.  " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Det trekkes vanligvis skatt av etterbetaling. Gjelder " +
                            "etterbetalingen tidligere år trekker Nav skatt etter Skatteetatens standardsatser. " +
                            "Du kan lese mer om satsene på ${Constants.SKATTETREKK_ETTERBETALING_URL}." },
                        nynorsk { +"Det blir normalt sett bli trekt skatt av etterbetaling. Dersom " +
                            "etterbetalinga gjeld tidlegare år, vil Nav trekkje skatt etter standardsatsane til " +
                            "Skatteetaten. Du kan lese meir om satsane på ${Constants.SKATTETREKK_ETTERBETALING_URL}." },
                        english { +"Tax is usually deducted from back payments. If the back payment " +
                            "applies to previous years, Nav will deduct the tax at the Tax Administration's " +
                            "standard rates. You can read more about the rates here: " +
                            "${Constants.SKATTETREKK_ETTERBETALING_URL}. " },
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
                    bokmal { +"Hva er omstillingsstønad?" },
                    nynorsk { +"Kva er omstillingsstønad?" },
                    english { +"What are adjustment allowance?" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Formålet med omstillingsstønaden er å sikre deg inntekt og gi hjelp " +
                        "til selvhjelp, slik at du etter en omstillingsperiode etter " +
                        ifElse(tidligereFamiliepleier, "at pleieforholdet opphørte,", "dødsfallet") +
                        " kan bli i stand til å forsørge deg selv ved eget arbeid." },
                    nynorsk { +"Føremålet med omstillingsstønaden er å sikre deg inntekt og gi " +
                        "hjelp til sjølvhjelp, slik at du etter ein omstillingsperiode etter " +
                        ifElse(tidligereFamiliepleier, "at pleieforholdet opphøyrde", "dødsfallet") +
                        " kan bli i stand til å forsørgje deg sjølv gjennom eige arbeid." },
                    english { +"The purpose of adjustment allowance is to " +
                        ifElse(
                            tidligereFamiliepleier,
                            "secure your income and provide help for self-help, so that after a transition " +
                                "period after the care relationship has ended, you can be able to support " +
                                "yourself through your own work.",
                            "ensure you a decent income and support self-reliance, so you will be able to " +
                                "support yourself himself through employment after a transitional period " +
                                "following the death of one's spouse.",
                        ) },
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
                text(
                    bokmal { +"Du må være i aktivitet fra seks måneder etter " +
                        ifElse(tidligereFamiliepleier, "pleieforholdet opphørte", "dødsfallet") },
                    nynorsk { +"Du må vere i aktivitet når det har gått seks månader sidan " +
                        ifElse(tidligereFamiliepleier, "pleieforholdet opphøyrde", "dødsfallet") },
                    english { +"You are obligated to be active starting six months after the " +
                        ifElse(tidligereFamiliepleier, "after care period ended", "death") },
                )
            }
            paragraph {
                text(
                    bokmal { +"Det første halvåret etter " +
                        ifElse(tidligereFamiliepleier, "pleieforholdet opphørte", "dødsfallet") +
                        " stilles det ikke krav til at du er i " +
                        "arbeid eller arbeidsrettet aktivitet. Etter seks måneder er det et vilkår for å " +
                        "fortsatt ha rett til omstillingsstønad at du er i minst 50 prosent aktivitet. " +
                        "Dette kalles aktivitetsplikt. Les mer om aktivitetsplikt og hva denne innebærer i vedlegget " +
                        "«Informasjon til deg som mottar omstillingsstønad»." },
                    nynorsk { +"Det første halvåret etter " +
                        ifElse(tidligereFamiliepleier, "pleieforholdet opphøyrde", "dødsfallet") +
                        " blir det ikkje stilt krav til at du er i " +
                        "arbeid eller arbeidsretta aktivitet. For at du framleis skal ha rett på " +
                        "omstillingsstønad etter seks månader, må du vere i minst 50 prosent aktivitet. " +
                        "Dette blir kalla aktivitetsplikt.  I vedlegget «Informasjon til deg som får " +
                        "omstillingsstønad» kan du lese meir om aktivitetsplikta og kva denne inneber." },
                    english { +"There is no obligation to work or employment-related activities for the first " +
                        "six months after the " +
                        ifElse(tidligereFamiliepleier, "care period ended", "death") +
                        ". After six months have passed, one condition for continuing " +
                        "to receive adjustment allowance is that you are at least 50 percent active. This is " +
                        "called the activity obligation.  Read more about the activity obligation and what this " +
                        "involves in the attachment Information to recipients of adjustment allowance." },
                )
            }

            showIf(innvilgetMindreEnnFireMndEtterDoedsfall) {
                paragraph {
                    text(
                        bokmal { +"Vi sender deg et eget informasjonsbrev om at du må være i aktivitet når det " +
                            "nærmer seg seks måneder etter " +
                            ifElse(tidligereFamiliepleier, "pleieforholdet opphørte.", "dødsfallet.") },
                        nynorsk { +"Vi sender deg eit eige informasjonsbrev om at du må vere i aktivitet når " +
                            "det nærmar seg seks månader sidan " +
                            ifElse(tidligereFamiliepleier, "pleieforholdet vart avslutta.", "dødsfallet.") },
                        english { +"We will send you a separate letter to inform you of your activity " +
                            "obligation near the end of the six-month period." },
                    )
                }
            }
        }
    }
}
