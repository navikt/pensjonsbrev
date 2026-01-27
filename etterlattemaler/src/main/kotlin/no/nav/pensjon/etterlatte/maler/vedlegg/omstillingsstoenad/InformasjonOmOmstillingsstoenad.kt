package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad

import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.InformasjonOmOmstillingsstoenadDataSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.InformasjonOmOmstillingsstoenadDataSelectors.tidligereFamiliepleier


data class InformasjonOmOmstillingsstoenadData(
    val tidligereFamiliepleier: Boolean = false,
    val bosattUtland: Boolean = false,
) : VedleggData

fun informasjonOmOmstillingsstoenad(): AttachmentTemplate<LangBokmalNynorskEnglish, InformasjonOmOmstillingsstoenadData> {
    return createAttachment(
        title = {
            text(
                bokmal { +"Informasjon til deg som mottar omstillingsstønad" },
                nynorsk { +"Informasjon til deg som får omstillingsstønad" },
                english { +"Information for recipients of adjustment allowance" },
            )
        },
        includeSakspart = false,
    ) {
        aktivitet(argument.tidligereFamiliepleier)
        hvisDuIkkeFyllerAktivitetsplikten()
        inntektOgOmstillingsstoenad()
        reguleringAvOmstillingsstoenad()
        endretInntekt()
        hvilkenInntektReduseresEtter()
        hvordanMeldeEndringer(argument.bosattUtland)
        utbetalingTilKontonummer()
        skatt(argument.bosattUtland)
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, InformasjonOmOmstillingsstoenadData>.aktivitet(
    tidligereFamiliepleier: Expression<Boolean>
) {
    title2 {
        text(
            bokmal { +"Du må være i aktivitet når du mottar omstillingsstønad" },
            nynorsk { +"Du må vere i aktivitet når du får omstillingsstønad" },
            english { +"You must be active while receiving adjustment allowances" },
        )
    }
    paragraph {
        text(
            bokmal {
                +"Når det er gått seks måneder etter " +
                        ifElse(tidligereFamiliepleier, "pleieforholdet opphørte", "dødsfallet") +
                        " er det et krav for å motta omstillingsstønad at du er i minst 50 prosent arbeid eller annen " +
                        "aktivitet med sikte på å komme i arbeid. Etter et år kan det forventes at du er i 100 prosent aktivitet. " +
                        "Dette kalles for aktivitetsplikt."
            },
            nynorsk {
                +"For å kunne halde fram med å få omstillingsstønad når det har gått seks månader sidan " +
                        ifElse(tidligereFamiliepleier, "pleieforholdet opphøyrde", "dødsfallet") +
                        ", må du vere i minst 50 prosent arbeid eller annan aktivitet med sikte på å kome i arbeid. Etter " +
                        "eitt år er det forventa at du er i 100 prosent aktivitet. Dette blir kalla aktivitetsplikt."
            },
            english {
                +"Once six months have passed after the " +
                        ifElse(tidligereFamiliepleier, "care period ended", "death") +
                        ", receiving adjustment allowance is contingent upon working at least 50 percent or involved in " +
                        "another activity with the aim of finding employment. After one year, you will be expected to be " +
                        "active 100 percent. This is called the activity obligation."
            },
        )
    }
    paragraph {
        text(
            bokmal { +"Du fyller aktivitetsplikten hvis du" },
            nynorsk { +"Du innfrir aktivitetsplikta dersom du" },
            english { +"You meet the activity obligation if you" },
        )
        list {
            item {
                text(
                    bokmal { +"jobber" },
                    nynorsk { +"jobbar" },
                    english { +"are working" },
                )
            }
            item {
                text(
                    bokmal { +"er selvstendig næringsdrivende" },
                    nynorsk { +"er sjølvstendig næringsdrivande" },
                    english { +"are self-employed/sole proprietor" },
                )
            }
            item {
                text(
                    bokmal { +"etablerer egen virksomhet" },
                    nynorsk { +"etablerer eiga verksemd" },
                    english { +"setting up your own business" },
                )
            }
            item {
                text(
                    bokmal { +"tar utdanning som er nødvendig og hensiktsmessig" },
                    nynorsk { +"tek utdanning som er nødvendig og føremålstenleg" },
                    english { +"taking a necessary and suitable education" },
                )
            }
            item {
                text(
                    bokmal { +"er reell arbeidssøker" },
                    nynorsk { +"er reell arbeidssøkjar" },
                    english { +"are a genuine job seeker" },
                )
            }
            item {
                text(
                    bokmal { +"har fått tilbud om jobb" },
                    nynorsk { +"har fått tilbod om jobb" },
                    english { +"have received a job offer" },
                )
            }
        }
    }
    paragraph {
        text(
            bokmal {
                +"Det er unntak fra aktivitetsplikten som gir rett til omstillingsstønad. Dette gjelder " +
                        "blant annet hvis du har omsorgen for barn under ett år, om du har dokumentert sykdom som " +
                        "forhindrer deg i å være i aktivitet, eller om du er innvilget etter unntaksregelen for de " +
                        "født i 1963 eller tidligere med lav inntekt."
            },
            nynorsk {
                +"Det finst unntak frå aktivitetsplikta som gir rett til omstillingsstønad. Dette gjeld " +
                        "mellom anna dersom du har omsorg for barn under eitt år, eller du har dokumentert sjukdom som " +
                        "hindrar deg i å vere i aktivitet."
            },
            english {
                +"There are some exemptions to the activity obligation which entitle you to adjustment " +
                        "allowance. This applies e.g. if you are caring for children under one year of age, or if you " +
                        "have documented an illness that prevents you from being active. "
            },
        )
    }
    paragraph {
        text(
            bokmal { +"Du kan lese mer om aktivitetsplikten og unntakene på " + Constants.OMS_AKTIVITET_URL + "." },
            nynorsk { +"Du kan lese meir om aktivitetsplikta og unntaka på " + Constants.OMS_AKTIVITET_URL + "." },
            english {
                +"You can read more about the activity obligation and its exceptions online: " +
                        Constants.OMS_AKTIVITET_URL + "."
            },
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, InformasjonOmOmstillingsstoenadData>.hvisDuIkkeFyllerAktivitetsplikten() {
    title2 {
        text(
            bokmal { +"Hva skjer hvis du ikke fyller aktivitetsplikten?" },
            nynorsk { +"Kva skjer dersom du ikkje innfrir aktivitetsplikta?" },
            english { +"What happens if you do not fulfil the activity obligation?" },
        )
    }
    paragraph {
        text(
            bokmal {
                +"Hvis du ikke fyller aktivitetsplikten, kan utbetalingen av stønaden stoppe inntil " +
                        "vilkårene er oppfylt igjen. Det blir midlertidig stans av utbetalingene dine hvis du"
            },
            nynorsk {
                +"Dersom du ikkje innfrir aktivitetsplikta, kan utbetalinga av stønaden bli stansa " +
                        "fram til du igjen oppfyller vilkåra. Utbetalingane blir stansa mellombels dersom du"
            },
            english {
                +"If you do not meet the activity obligation, allowance payments may stop until the " +
                        "condition is met again. Your payments will be temporarily suspended if you"
            },
        )
        list {
            item {
                text(
                    bokmal { +"sier nei til jobb" },
                    nynorsk { +"takkar nei til jobb" },
                    english { +"say no to work" },
                )
            }
            item {
                text(
                    bokmal { +"sier nei til å delta i, eller slutter i et arbeidsmarkedstiltak" },
                    nynorsk { +"sluttar i eller seier nei til å delta i eit arbeidsmarknadstiltak" },
                    english { +"say no to participating in, or quit, a labour market scheme" },
                )
            }
            item {
                text(
                    bokmal { +"sier opp, eller på andre måter slutter i jobben din" },
                    nynorsk { +"seier opp eller på andre måtar sluttar i jobben" },
                    english { +"resign, or otherwise quit your job" },
                )
            }
            item {
                text(
                    bokmal { +"blir avskjediget eller sagt opp på grunn av forhold som du selv er skyld i" },
                    nynorsk { +"får avskjed eller blir oppsagd på grunn av forhold du sjølv er skuld i" },
                    english { +"are fired or dismissed due to circumstances for which you are at fault" },
                )
            }
        }
    }
    paragraph {
        text(
            bokmal {
                +"Nav må kunne komme i kontakt med deg for å følge deg opp ved behov. Får vi ikke " +
                        "kontakt med deg, kan vi stoppe stønaden din."
            },
            nynorsk {
                +"Nav må kunne kontakte deg for å gi oppfølging ved behov. Dersom vi ikkje får kontakt " +
                        "med deg, kan vi stoppe stønaden."
            },
            english {
                +"Nav must be able to get in touch with you to follow up your case, whenever needed. " +
                        "We may stop your allowance if we cannot get in touch with you. "
            },
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, InformasjonOmOmstillingsstoenadData>.inntektOgOmstillingsstoenad() {
    title2 {
        text(
            bokmal { +"Inntekt og omstillingsstønad" },
            nynorsk { +"Inntekt og omstillingsstønad" },
            english { +"Income and adjustment allowance" },
        )
    }
    paragraph {
        text(
            bokmal {
                +"Omstillingsstønaden skal reduseres med 45 prosent av inntekten din som er over " +
                        "halvparten av grunnbeløpet i folketrygden (G). Stønaden blir redusert ut fra hva du oppgir " +
                        "som forventet inntekt for gjeldende år.  "
            },
            nynorsk {
                +"Omstillingsstønaden skal reduserast med 45 prosent av inntekta di som er over " +
                        "halvparten av grunnbeløpet i folketrygda (G). Stønaden blir redusert ut frå kva du oppgir " +
                        "som forventa inntekt for gjeldande år."
            },
            english {
                +"The adjustment allowance are reduced by 45 percent of your income, if it is more " +
                        "than half of the basic amount in the National Insurance Scheme (G). The allowance will be " +
                        "reduced based on what you declare as anticipated income for the current year."
            },
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, InformasjonOmOmstillingsstoenadData>.reguleringAvOmstillingsstoenad() {
    title2 {
        text(
            bokmal { +"Regulering av omstillingsstønad fra 1. mai" },
            nynorsk { +"Regulering av omstillingsstønad frå 1. mai" },
            english { +"Adjustment allowance from May 1" },
        )
    }
    paragraph {
        text(
            bokmal {
                +"Satsen for omstillingsstønad justeres hvert år når grunnbeløpet i folketrygden endres " +
                        "1. mai. Dette betyr at omstillingsstønaden øker i takt med grunnbeløpet. Inntekten som " +
                        "omstillingsstønaden reduseres etter, forblir derimot uendret så fremt du ikke melder fra om endring."
            },
            nynorsk { +"Satsen for omstillingsstønad blir justert kvart år når grunnbeløpet i folketrygda " +
                    "endrar seg 1. mai. Dette betyr at omstillingsstønaden aukar i takt med grunnbeløpet. Inntekta " +
                    "som omstillingsstønaden blir redusert etter, forblir derimot uendra så lenge du ikkje melder frå om endring. " },
            english { +"The rate for the adjustment allowance is adjusted every year when the National " +
                    "Insurance base amount changes on May 1. This means that the adjustment allowance increases in " +
                    "line with the base amount. However, the income that the adjustment allowance is reduced " +
                    "against remains unchanged unless you report a change. " },
        )
    }
    paragraph {
        text(
            bokmal {
                +"Dersom du tidligere ikke har fått utbetalt omstillingsstønad fordi inntekten din har vært " +
                        "for høy, kan dette endre seg fra 1. mai. Du må selv kontrollere at stønaden er beregnet ut fra riktig " +
                        "inntekt, og melde fra dersom inntekten din endres. "
            },
            nynorsk { +"Dersom du tidlegare ikkje har fått utbetalt omstillingsstønad fordi inntekta di har " +
                    "vore for høg, kan dette endre seg frå 1. mai. Du må sjølv kontrollere at stønaden er berekna ut " +
                    "frå rett inntekt, og melde frå dersom inntekta di endrar seg. " },
            english { +"If you previously did not receive the adjustment allowance because your income was " +
                    "too high, this may change from May 1. You must check that the adjustment allowance is calculated " +
                    "based on the correct income and report any changes in your income. " }
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, InformasjonOmOmstillingsstoenadData>.endretInntekt() {
    title2 {
        text(
            bokmal { +"Får du endret inntekt i løpet av året?" },
            nynorsk { +"Får du endra inntekt i løpet av året?" },
            english { +"Will your income change during the year?" },
        )
    }
    paragraph {
        text(
            bokmal {
                +"Om du melder fra om endring av inntekt i løpet av året, vil vi justere " +
                        "omstillingsstønaden fra måneden etter du har gitt beskjed. Inntekten din beregnes ut fra " +
                        "det du har tjent så langt i år, lagt sammen med det du forventer å tjene resten av året. " +
                        "Inntekt som er opptjent før mottak av omstillingsstønaden tas ikke med i beregningen."
            },
            nynorsk {
                +"Dersom du melder frå om endring av inntekt i løpet av året, vil vi justere " +
                        "omstillingsstønaden frå månaden etter du har gitt beskjed. Inntekta di blir rekna ut med " +
                        "utgangspunkt i det du har tent så langt i år, lagt saman med det du forventar å tene resten " +
                        "av året. Inntekt du hadde tent opp før du fekk omstillingsstønaden, blir ikkje teken med i " +
                        "utrekninga. "
            },
            english {
                +"If you report a change in income during the year, we will adjust the adjustment " +
                        "allowance starting in the month after you reported the change. Your income is calculated based " +
                        "on what you earned so far this year, added together with what you expect to earn for the rest " +
                        "of the year. Income earned before receiving any adjustment allowance is not taken into " +
                        "account."
            },
        )
    }
    paragraph {
        text(
            bokmal {
                +"Utbetalingen for resten av året vil justeres utfra det du har fått utbetalt så langt " +
                        "det gjeldende året. Meld derfor fra snarest mulig for å få mest mulig riktig utbetalt " +
                        "omstillingsstønad, slik at etteroppgjøret blir så riktig som mulig. Du kan finne mer " +
                        "informasjon om etteroppgjør på ${Constants.OMS_ETTEROPPGJOER_URL}."
            },
            nynorsk {
                +"Utbetalinga for resten av året vil bli justert ut frå det du har fått utbetalt så " +
                        "langt det gjeldande året. Meld difor frå snarast råd, slik at du får utbetalt " +
                        "omstillingsstønaden du har krav på, og etteroppgjeret blir mest mogleg rett. Du finn meir " +
                        "informasjon om etteroppgjer på ${Constants.OMS_ETTEROPPGJOER_URL}."
            },
            english {
                +"The payment for the rest of the year will be adjusted based on what you have been " +
                        "paid so far in the current year. It is therefore important to notify us as soon as possible " +
                        "to receive the correct amount of adjustment allowance, so that any final settlement will " +
                        "be as correct as possible. You can find more information about final settlements " +
                        "online: ${Constants.OMS_ETTEROPPGJOER_URL}."
            },
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, InformasjonOmOmstillingsstoenadData>.hvilkenInntektReduseresEtter() {
    title2 {
        text(
            bokmal { +"Hvilken inntekt skal omstillingsstønaden reduseres etter?" },
            nynorsk { +"Etter kva inntekt skal omstillingsstønaden reduserast?" },
            english { +"What income forms the basis for reducing the adjustment allowance?" },
        )
    }
    paragraph {
        text(
            bokmal {
                +"Omstillingsstønaden skal reduseres etter arbeidsinntekt og annen inntekt som er " +
                        "likestilt med arbeidsinntekt. Dette er blant annet:"
            },
            nynorsk {
                +"Omstillingsstønaden skal reduserast etter arbeidsinntekt og anna inntekt som er " +
                        "likestilt med arbeidsinntekt. Dette er mellom anna"
            },
            english {
                +"The adjustment allowance shall be reduced according to income from employment and " +
                        "other income that is similar to employment income. These include:"
            },
        )
        list {
            item {
                text(
                    bokmal { +"arbeidsinntekt fra alle arbeidsgivere, inkludert feriepenger" },
                    nynorsk { +"arbeidsinntekt (inkludert feriepengar) frå alle arbeidsgivarar" },
                    english { +"earned income from all employers, including holiday pay" },
                )
            }
            item {
                text(
                    bokmal { +"næringsinntekt, og inntekt fra salg av næringsvirksomhet" },
                    nynorsk { +"næringsinntekt og inntekt frå sal av næringsverksemd" },
                    english { +"Business income and income from the sale of a business enterprise" },
                )
            }
            item {
                text(
                    bokmal { +"styregodtgjørelse og andre godtgjørelser" },
                    nynorsk { +"styregodtgjersle og andre godtgjersler" },
                    english { +"remuneration of board members and other remuneration" },
                )
            }
            item {
                text(
                    bokmal { +"royalties" },
                    nynorsk { +"royalties" },
                    english { +"royalties" },
                )
            }
            item {
                text(
                    bokmal { +"dagpenger, sykepenger og arbeidsavklaringspenger" },
                    nynorsk { +"dagpengar, sjukepengar og arbeidsavklaringspengar" },
                    english { +"unemployment benefits, sickness benefits and Work Assessment Allowance" },
                )
            }
            item {
                text(
                    bokmal { +"svangerskapspenger og foreldrepenger" },
                    nynorsk { +"svangerskapspengar og foreldrepengar" },
                    english { +"pregnancy benefits and parental benefits" },
                )
            }
            item {
                text(
                    bokmal { +"omsorgsstønad" },
                    nynorsk { +"omsorgsstønad" },
                    english { +"care benefits" },
                )
            }
        }
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, InformasjonOmOmstillingsstoenadData>.hvordanMeldeEndringer(
    bosattUtland: Expression<Boolean>
) {
    title2 {
        text(
            bokmal { +"Hvordan melder du fra om endringer?" },
            nynorsk { +"Slik melder du frå om endringar" },
            english { +"How do I report changes" },
        )
    }

    paragraph {
        text(
            bokmal { +"Du kan gi beskjed om endringer på følgende måter:" },
            nynorsk { +"Du kan melde frå om endringar på følgjande måtar:" },
            english { +"You can report changes in the following ways:" }
        )

        list {
            item {
                text(
                    bokmal { +"benytte endringsskjema på ${Constants.OMS_MELD_INN_ENDRING_URL}" },
                    nynorsk { +"bruk endringsskjema på ${Constants.OMS_MELD_INN_ENDRING_URL}" },
                    english { +"use the change form on ${Constants.OMS_MELD_INN_ENDRING_URL}" }
                )
            }
            item {
                text(
                    bokmal { +"ettersende dokumentasjon angående omstillingsstønad ved å gå inn på ${Constants.ETTERSENDE_OMS_URL}" },
                    nynorsk { +"ettersend dokumentasjon angåande omstillingsstønad ved å gå inn på ${Constants.ETTERSENDE_OMS_URL}" },
                    english { +"send more documentation regarding adjustment allowance by going to: ${Constants.ETTERSENDE_OMS_URL}" }
                )
            }
        }

        showIf(bosattUtland) {
            text(
                bokmal { +"Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside nav.no, må du sende brev til ${Constants.Utland.POSTADRESSE}." },
                nynorsk { +"Har du ikkje BankID eller andre moglegheiter til å logge på heimesida vår nav.no, må du sende dokumentasjon per post til ${Constants.Utland.POSTADRESSE}." },
                english { +"Please send documentation as normal post if you do not use BankID or another login option. Send to ${Constants.Utland.POSTADRESSE}." }
            )
        }.orShow {
            text(
                bokmal { +"Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside nav.no, må du sende brev til ${Constants.POSTADRESSE}." },
                nynorsk { +"Har du ikkje BankID eller andre moglegheiter til å logge på heimesida vår nav.no, må du sende dokumentasjon per post til ${Constants.POSTADRESSE}." },
                english { +"Please send documentation as normal post if you do not use BankID or another login option. Send to ${Constants.POSTADRESSE}." }
            )
        }
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, InformasjonOmOmstillingsstoenadData>.utbetalingTilKontonummer() {
    title2 {
        text(
            bokmal { +"Utbetaling til kontonummer" },
            nynorsk { +"Utbetaling til kontonummer" },
            english { +"Payments go to your bank account" },
        )
    }
    paragraph {
        text(
            bokmal {
                +"Du kan bare ha ett kontonummer registrert hos Nav. Du kan endre kontonummeret i " +
                        "«Personopplysninger» ved å logge på nav.no. Du kan også sende endring per post. " +
                        "Du finner skjema og riktig adresse på " + Constants.ENDRING_KONTONUMMER_URL + "."
            },
            nynorsk {
                +"Du kan berre ha eitt kontonummer registrert hos Nav. Du kan endre kontonummer under " +
                        "«Personopplysningar» ved å logge på nav.no. Du kan også sende endring per post. Du finn skjema " +
                        "og rett adresse på " + Constants.ENDRING_KONTONUMMER_URL + "."
            },
            english {
                +"You can only have one account number registered with Nav. You can change your account " +
                        "number online (in Personal Data) by logging in to nav.no. You can also report changes by " +
                        "conventional mail. You will find the form and the correct address online: " +
                        Constants.ENDRING_KONTONUMMER_URL + "."
            },
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, InformasjonOmOmstillingsstoenadData>.skatt(bosattUtland: Expression<Boolean>) {
    title2 {
        text(
            bokmal { +"Skatt" },
            nynorsk { +"Skatt" },
            english { +"Tax" },
        )
    }

    showIf(bosattUtland) {
        paragraph {
            text(
                bokmal { +"Skattereglene for omstillingsstønad avhenger av om du er skattemessig bosatt i Norge." },
                nynorsk { +"Skattereglar for omstillingsstønad avheng av om du er skattemessig busett i Noreg." },
                english { +"The tax rules for the adjustment allowance vary depending on whether or not you are a tax resident in Norway." }
            )
        }
        paragraph {
            text(
                bokmal { +"Skattemessig bosatt i Norge:" },
                nynorsk { +"Skattemessig busett i Noreg:" },
                english { +"Tax reident in Norway:" }
            )
            text(
                bokmal {
                    +"Hvis du er skattemessig bosatt i Norge, skal du betale skatt på all inntekt og formue. " +
                            "Husk å kontrollere skattekortet ditt på ${Constants.SKATTEETATEN_ENDRE_URL}. Ønsker du å avslutte skatteplikten, " +
                            "kan du søke om skattemessig emigrasjon"
                },
                nynorsk {
                    +"Viss du er skattemessig busett i Norge, skal du betale skatt for all inntekt og formue. " +
                            "Husk å sjekke skattekortet ditt på ${Constants.SKATTEETATEN_ENDRE_URL}. Ønskjer du å avslutte skatteplikta, " +
                            "kan du søkje om skattemessig emigrasjon."
                },
                english {
                    +"If you are resident in Norway for tax purposes, you must pay tax on all income and wealth. " +
                            "Remember to check your tax deduction card at ${Constants.Engelsk.SKATTEETATEN_ENDRE_URL}. If you wish to terminate your tax liability, " +
                            "you can apply for tax emigration."
                }
            )
        }
        paragraph {
            text(
                bokmal { +"Ikke skattemessig bosatt i Norge:" },
                nynorsk { +"Ikkje skattemessig busett i Noreg:" },
                english { +"Non-tax resident in Norway:" }
            )
            text(
                bokmal {
                    +"Er du ikke skattemessig bosatt i Norge, skal du betale 15 prosent kildeskatt på brutto omstillingsstønad." +
                            "Bor du i et land med skatteavtale med Norge, kan du ha rett til fritak fra kildeskatt. Bor du i et land med skatteavtale med Norge, " +
                            "kan du ha rett til fritak fra kildeskatt. Hvis du bor i et EU- eller EØS-land, kan du bli skatteberegnet som bosatt i Norge."
                },
                nynorsk {
                    +"Er du ikkje skattemessig busett i Noreg, skal du betale 15 prosent kjeldeskatt av brutto omstillingsstønad. " +
                            "Bur du i eit land som Noreg har skatteavtale med, kan du ha rett til fritak frå kjeldeskatt. Viss du bur i eit EU- eller EØS-land, " +
                            "kan du bli skatteberekna som busett i Noreg."
                },
                english {
                    +"If you are not resident in Norway for tax purposes, you must pay 15 per cent withholding tax on your adjustment allowance. " +
                            "If you are resident in a country Norway has a tax treaty with, you may be entitled to an exemption from withholding tax on certain benefits. " +
                            "If you live in an EU/EEA country, you may qualify to be considered as resident in Norway for tax purposes."
                }
            )
        }
        paragraph {
            text(
                bokmal { +"Les mer på ${Constants.SKATTEETATEN_KILDESKATTPENSJON_URL} eller kontakt Skatteetaten på telefon ${Constants.SKATTEETATEN_KONTAKTTELEFON_MED_LANDKODE} fra utlandet" },
                nynorsk { +"Les meir på ${Constants.SKATTEETATEN_KILDESKATTPENSJON_URL} eller kontakt Skatteetaten på telefon ${Constants.SKATTEETATEN_KONTAKTTELEFON_MED_LANDKODE} frå utlandet." },
                english { +"Read more at ${Constants.SKATTEETATEN_KILDESKATTPENSJON_URL} or contact the Norwegian Tax Administration by phone at ${Constants.SKATTEETATEN_KONTAKTTELEFON_MED_LANDKODE} from abroad." }
            )
        }
    }.orShow {
        paragraph {
            text(
                bokmal {
                    +"Omstillingsstønaden er skattepliktig. Du trenger ikke levere skattekortet til Nav " +
                            "fordi skatteopplysningene dine sendes elektronisk fra Skatteetaten."
                },
                nynorsk {
                    +"Omstillingsstønaden er skattepliktig. Du treng ikkje levere skattekortet til Nav, " +
                            "då skatteopplysningane dine blir sende elektronisk frå Skatteetaten."
                },
                english {
                    +"Adjustment allowance are taxable. You do not need to submit your tax deduction " +
                            "card to Nav because your tax information is sent to Nav electronically from the " +
                            "Norwegian Tax Administration."
                },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Endring av skattekort gjøres enklest på Skatteetatens nettsider www.skatteetaten.no. " +
                            "Har du spørsmål kan du ringe Skatteetaten på telefon " + Constants.KONTAKTTELEFON_SKATT + ". " +
                            "Fra utlandet ringer du " + Constants.Utland.KONTAKTTELEFON_SKATT + "."
                },
                nynorsk {
                    +"Skattekortet endrar du enklast frå nettsidene til Skatteetaten, www.skatteetaten.no. " +
                            "Viss du har spørsmål, kan du ringje Skatteetaten på telefon " + Constants.KONTAKTTELEFON_SKATT + ". " +
                            "Frå utlandet ringjer du " + Constants.Utland.KONTAKTTELEFON_SKATT + "."
                },
                english {
                    +"The easiest way to change your tax deduction card is done on the Tax Administration's " +
                            "website: www.skatteetaten.no. If you have any questions, please call the Tax Administration " +
                            "by phone: " + Constants.KONTAKTTELEFON_SKATT + ". For calls from abroad: " +
                            Constants.Utland.KONTAKTTELEFON_SKATT + "."
                },
            )
        }
    }
    paragraph {
        text(
            bokmal { +"Omstillingsstønaden er pensjonsgivende inntekt. Den gir ikke opptjening av feriepenger." },
            nynorsk { +"Omstillingsstønaden er pensjonsgivande inntekt. Den gir ikkje opptening av feriepengar." },
            english { +"Adjustment allowance are considered pensionable income. They do not earn you holiday pay." },
        )
    }

}
